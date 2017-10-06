package dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import utilities.CHibernateSession;
import utilities.CLogger;

public class ObjetoDAO {

	public static List<?> getCostoObjeto(Integer objetoId, Integer objetoTipo){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query =
					"select arbol.*, costo.total, costo.pago, pagos.* from ( "+
					"select p.id, 1 objeto_tipo, p.costo, p.acumulacion_costoid "+
					"from proyecto p "+
					"where p.id=?1 and ?2=1 and p.estado=1  "+
					"union "+
					"select c.id, 2 objeto_tipo, c.costo, c.acumulacion_costoid "+
					"from componente c "+
					"where c.id=?1 and ?2=2 and c.estado=1 "+
					"union "+
					"select pr.id, 3 objeto_tipo,pr.costo,pr.acumulacion_costoid "+
					"from producto pr "+
					"where pr.id= ?1 and ?2 = 3 and pr.estado=1 "+
					"union "+
					"select sp.id, 4 objeto_tipo, sp.costo,sp.acumulacion_costoid "+
					"from subproducto sp "+
					"where sp.id= ?1  and ?2 = 4 and sp.estado=1 "+
					"union "+
					"select a.id, 5 objeto_tipo, a.costo, a.acumulacion_costo acumulacion_costoid "+
					"from actividad a "+
					"where a.id=?1 and ?2=5 and a.estado=1 "+
					") arbol "+
					"left outer join ( "+
					"select pa.id, pa.objeto_id, pa.objeto_tipo, SUM(pa.total) total, pp.pago pago from plan_adquisicion pa "+
					"left outer join (select plan_adquisicionid id, SUM(pago) pago "+
					"from plan_adquisicion_pago group by plan_adquisicionid) pp on pp.id = pa.id "+
					"group by pa.objeto_id, pa.objeto_tipo) costo on costo.objeto_id = arbol.id and costo.objeto_tipo = arbol.objeto_tipo "+
					"left outer join ( "+
					"select t1.ejercicio, t1.objeto_id objeto_id_pago, t1.objeto_tipo objeto_tipo_pago, "+
					"SUM(case when t1.mes = 1 then t1.pago end) enero, "+
					"SUM(case when t1.mes = 2 then t1.pago end) febrero, "+
					"SUM(case when t1.mes = 3 then t1.pago end) marzo, "+
					"SUM(case when t1.mes = 4 then t1.pago end) abril, "+
					"SUM(case when t1.mes = 5 then t1.pago end) mayo, "+
					"SUM(case when t1.mes = 6 then t1.pago end) junio, "+
					"SUM(case when t1.mes = 7 then t1.pago end) julio, "+
					"SUM(case when t1.mes = 8 then t1.pago end) agosto, "+
					"SUM(case when t1.mes = 9 then t1.pago end) septiembre, "+
					"SUM(case when t1.mes = 10 then t1.pago end) octubre, "+
					"SUM(case when t1.mes = 11 then t1.pago end) noviembre, "+
					"SUM(case when t1.mes = 12 then t1.pago end) diciembre "+
					"from "+
					"( "+
					"select pa.objeto_id, pa.objeto_tipo, year(pp.fecha_pago) ejercicio, month(pp.fecha_pago) mes, pp.pago "+
					"from plan_adquisicion_pago pp "+
					"join plan_adquisicion pa on pp.plan_adquisicionid = pa.id "+
					") t1 "+
					"where t1.ejercicio between 2016 and 2017 "+
					"group by t1.ejercicio "+
					") pagos on pagos.objeto_id_pago = arbol.id and pagos.objeto_tipo_pago = arbol.objeto_tipo "+
					"order by ejercicio asc ";			
				
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", objetoId);
			criteria.setParameter("2", objetoTipo);
			ret = criteria.getResultList();
			
		}
		catch(Throwable e){
			CLogger.write("1", ObjetoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ObjetoCosto> getEstructuraConCosto(int idPrestamo, int anioInicial, int anioFinal, boolean obtenerPlanificado, boolean obtenerReal, String usuario){
		List<ObjetoCosto> lstPrestamo = new ArrayList<>();
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
		
		Iterator<?> iterador = estructuraProyecto.iterator();
		while (iterador.hasNext()) {
			Object objeto = iterador.next();
			Object[] obj = (Object[]) objeto;
			Integer nivel = ((String)obj[3]).length() / 8;
			if(nivel != null){
				Integer objeto_id = obj[0]!=null ? (Integer)obj[0] : null;
				String nombre = obj[1]!=null ? (String)obj[1] : null;
				Integer objeto_tipo = obj[2]!=null ? ((BigInteger) obj[2]).intValue() : null;
				DateTime fecha_inicial = obj[4]!=null ? new DateTime((Timestamp)obj[4]) : null;
				DateTime fecha_final = obj[5]!=null ? new DateTime((Timestamp)obj[5]) : null;
				Integer acumulacion_costoid = obj[10]!=null ? Integer.valueOf(obj[10].toString()) : null;
				BigDecimal costo = obj[8]!=null ? (BigDecimal)obj[8] : null;
				Integer programa = obj[11]!=null ? (Integer)obj[11] : null;
				Integer subprograma = obj[12]!=null ? (Integer)obj[12] : null;
				Integer proyecto = obj[13]!=null ? (Integer)obj[13] : null;
				Integer actividad = obj[14]!=null ? (Integer)obj[14] : null;
				Integer obra = obj[15]!=null ? (Integer)obj[15] : null;
				
				ObjetoCosto objetoCosto =  new ObjetoCosto(nombre, objeto_id, objeto_tipo, nivel, fecha_inicial, fecha_final, null,
						acumulacion_costoid, costo, programa, subprograma, proyecto, actividad, obra);
				objetoCosto.inicializarStanio(anioInicial, anioFinal);
				
				if(obtenerPlanificado){
					List<?> costoPlanificado = getCostoObjeto(objetoCosto.objeto_id, objetoCosto.objeto_tipo);
					if(costoPlanificado!=null){
						Iterator<?> iteradorCosto = costoPlanificado.iterator();
						while (iteradorCosto.hasNext()) {
							Object filaCosto = iteradorCosto.next();
							Object[] objFila = (Object[]) filaCosto;
							BigDecimal totalPagos = objFila[5]!=null ? (BigDecimal)objFila[5] : null;
							if(totalPagos!=null && totalPagos.compareTo(BigDecimal.ZERO)==0){
								Integer ejercicio = objFila[6]!=null ? (Integer)objFila[6] : null;
								objetoCosto.anios[ejercicio-anioInicial].anio = ejercicio;
								for(int m=0; m<12; m++){
									objetoCosto.anios[ejercicio-anioInicial].mes[m].planificado = objFila[9+m]!=null ? (BigDecimal)objFila[9+m] : null;
								}
							}else{
								for(int a=0; a<(anioFinal-anioInicial+1); a++){
									objetoCosto.anios[a].anio=anioInicial+a;
									ObjetoCosto.stanio anioObj = objetoCosto.anios[a];
									int diaInicial = objetoCosto.getFecha_inicial().getDayOfMonth();
									int mesInicial = objetoCosto.getFecha_inicial().getMonthOfYear() -1;
									int anioInicialObj = objetoCosto.getFecha_inicial().getYear();
									int diaFinal = objetoCosto.getFecha_final().getDayOfMonth();
									int mesFinal = objetoCosto.getFecha_final().getMonthOfYear() -1;
									int anioFinalObj = objetoCosto.getFecha_final().getYear();
									if((anioInicial+a) >= anioInicialObj && (anioInicial+a)<=anioFinalObj){
										if(objetoCosto.getAcumulacion_costoid() != null){
											if(objetoCosto.getAcumulacion_costoid() == 1){						
												if(anioInicialObj == (anioInicial+a)){
													anioObj.mes[mesInicial].planificado =  objetoCosto.getCosto() != null ? objetoCosto.getCosto() : new BigDecimal(0);
												}
											}else if(objetoCosto.getAcumulacion_costoid() == 2){
												int dias = (int)((objetoCosto.getFecha_final().getMillis() - objetoCosto.getFecha_inicial().getMillis())/(1000*60*60*24));
												BigDecimal costoDiario = objetoCosto.getCosto() != null ? objetoCosto.getCosto().divide(new BigDecimal(dias),5, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
												int inicioActual = 0;
												if(anioObj.anio == anioInicialObj){
													inicioActual = mesInicial;
												}
												
												int finMes = anioObj.anio==anioFinalObj ? mesFinal : 11;
												for(int m=inicioActual; m<=finMes; m++){
													if(anioObj.anio == anioInicialObj && m==mesInicial){
														if(m==mesFinal){
															int diasMes = diaFinal-diaInicial;
															anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
														}else{
															Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
															int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
															diasMes = diasMes-diaInicial;
															anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
														}
													}else if(anioObj.anio == anioFinalObj && m== mesFinal){
														anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diaFinal));
													}else{
														Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
														int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
														anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
													}
												}
											}else if(objetoCosto.getAcumulacion_costoid() ==3){
												if(anioFinalObj == anioObj.anio){
													anioObj.mes[mesFinal].planificado =  objetoCosto.getCosto() != null ? objetoCosto.getCosto() : new BigDecimal(0);
												}
											}
										}
									}
								}
							
							}
						}
					}
				}
				
				if(obtenerReal){
					
				}
				
				lstPrestamo.add(objetoCosto);
			}
		}
		return lstPrestamo;
	}
	

	
	private ObjetoCosto getCostoReal(ObjetoCosto objetoCosto, Integer fuente, Integer organismo, Integer correlativo, Integer anioInicial, Integer anioFinal, Connection conn, String usuario){
		ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo = new ArrayList<ArrayList<BigDecimal>>();
		
			if(objetoCosto.getObjeto_tipo() == 1){
				presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoProyecto(fuente, organismo, correlativo,anioInicial,anioFinal, conn);
			}else{
				presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
						anioInicial, anioFinal, objetoCosto.getPrograma(), objetoCosto.getSubprograma(), objetoCosto.getProyecto(), 
						objetoCosto.getActividad(), objetoCosto.getObra(), conn);
			}
		
		if(presupuestoPrestamo.size() > 0){
			int pos = 0;
			for(ArrayList<BigDecimal> objprestamopresupuesto : presupuestoPrestamo){
				for (int m=0; m<12; m++){
					objetoCosto.getAnios()[pos].mes[m].real = objprestamopresupuesto.get(m) != null ? objprestamopresupuesto.get(m) : new BigDecimal(0);
				}
				objetoCosto.getAnios()[pos].anio = objprestamopresupuesto.get(12) != null ? objprestamopresupuesto.get(12).intValueExact() : 0;
				pos = pos + 1;
			}
		}
		return objetoCosto;
	}
}
