package dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import pojo.Componente;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.Utils;

public class ObjetoDAO {

	public static List<?> getConsultaEstructuraConCosto(Integer proyectoId){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query =
					"select arbol.*, costo.total, costo.pago from ( "+
					"select p.id, p.nombre, 1 objeto_tipo,  p.treePath, p.fecha_inicio, "+
					"p.fecha_fin, p.duracion, p.duracion_dimension,p.costo,0, p.acumulacion_costoid,  "+
					"p.programa, p.subprograma, p.proyecto, p.actividad, p.obra "+
					"from proyecto p "+
					"where p.id= ?1 and p.estado=1  "+
					"union "+
					"select c.id, c.nombre, 2 objeto_tipo,  c.treePath, c.fecha_inicio, "+
					"c.fecha_fin , c.duracion, c.duracion_dimension,c.costo,0,c.acumulacion_costoid, "+
					"c.programa, c.subprograma, c.proyecto, c.actividad, c.obra "+
					"from componente c "+
					"where c.proyectoid=?1 and c.estado=1  "+
					"union "+
					"select pr.id, pr.nombre, 3 objeto_tipo , pr.treePath, pr.fecha_inicio, "+
					"pr.fecha_fin, pr.duracion, pr.duracion_dimension,pr.costo,0,pr.acumulacion_costoid, "+
					"pr.programa, pr.subprograma, pr.proyecto, pr.actividad, pr.obra "+
					"from producto pr "+
					"left outer join componente c on c.id=pr.componenteid "+
					"left outer join proyecto p on p.id=c.proyectoid "+
					"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1   "+
					"union "+
					"select sp.id, sp.nombre, 4 objeto_tipo,  sp.treePath, sp.fecha_inicio, "+
					"sp.fecha_fin , sp.duracion, sp.duracion_dimension,sp.costo,0,sp.acumulacion_costoid, "+
					"sp.programa, sp.subprograma, sp.proyecto, sp.actividad, sp.obra "+
					"from subproducto sp "+
					"left outer join producto pr on pr.id=sp.productoid "+
					"left outer join componente c on c.id=pr.componenteid "+
					"left outer join proyecto p on p.id=c.proyectoid "+
					"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1 and sp.estado=1 and sp.id  "+
					"union "+
					"select a.id, a.nombre, 5 objeto_tipo,  a.treePath, a.fecha_inicio, "+
					"a.fecha_fin , a.duracion, a.duracion_dimension,a.costo,a.pred_objeto_id,a.acumulacion_costo acumulacion_costoid, "+
					"a.programa, a.subprograma, a.proyecto, a.actividad, a.obra "+
					"from actividad a "+
					"where a.estado=1 and  a.treepath like '"+(10000000+proyectoId)+"%'"+
					") arbol "+
					"left outer join ( "+
					"select pa.id, pa.objeto_id, pa.objeto_tipo, SUM(pa.total) total, pp.pago pago from plan_adquisicion pa "+
					"left outer join (select plan_adquisicionid id, SUM(pago) pago "+
					"from plan_adquisicion_pago group by plan_adquisicionid) pp on pp.id = pa.id "+
					"group by pa.objeto_id, pa.objeto_tipo) costo on costo.objeto_id = arbol.id and costo.objeto_tipo = arbol.objeto_tipo "+
					"order by treePath  ";			
				
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", proyectoId);
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
	
	public static List<?> getConsultaPagos(Integer objetoId, Integer objetoTipo, Integer anioInicial, Integer anioFinal){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query =
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
					"where t1.objeto_id = ?1 and t1.objeto_tipo = ?2 "+
					"and t1.ejercicio between ?3 and ?4 "+
					"group by t1.ejercicio  ";			
				
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", objetoId);
			criteria.setParameter("2", objetoTipo);
			criteria.setParameter("3", anioInicial);
			criteria.setParameter("4", anioFinal);
			ret = criteria.getResultList();			
		}
		catch(Throwable e){
			CLogger.write("2", ObjetoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ObjetoCosto> getEstructuraConCosto(int idPrestamo, int anioInicial, int anioFinal, boolean obtenerPlanificado, boolean obtenerReal, String usuario) throws SQLException{
		List<ObjetoCosto> lstPrestamo = new ArrayList<>();
		if(CMariaDB.connectAnalytic()){
			Connection conn_analytic = CMariaDB.getConnection_analytic();
			List<?> estructuraProyecto = getConsultaEstructuraConCosto(idPrestamo);
				if(estructuraProyecto!=null){
				Iterator<?> iterador = estructuraProyecto.iterator();
				while (iterador.hasNext()) {
					Object objeto = iterador.next();
					Object[] obj = (Object[]) objeto;
					Integer nivel = ((String)obj[3]).length() / 8;
					if(nivel != null){
						Integer objeto_id = obj[0]!=null ? (Integer)obj[0] : null;
						String nombre = obj[1]!=null ? (String)obj[1] : null;
						Integer objeto_tipo = obj[2]!=null ? ((BigInteger) obj[2]).intValue() : null;
						String treePath = obj[3] != null ? ((String) obj[3]) : null;
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
								acumulacion_costoid, costo, programa, subprograma, proyecto, actividad, obra, treePath);
						objetoCosto.inicializarStanio(anioInicial, anioFinal);
						
						if(obtenerPlanificado){
							BigDecimal totalPagos = obj[17]!=null ? (BigDecimal)obj[17] : null;					
							if(totalPagos!=null && totalPagos.compareTo(BigDecimal.ZERO)!=0){
								//obtener pagos
								List<?> estructuraPagos = getConsultaPagos(objetoCosto.objeto_id, objetoCosto.objeto_tipo, anioInicial, anioFinal);
								
								Iterator<?> iteradorPagos = estructuraPagos.iterator();
								while (iteradorPagos.hasNext()) {
									Object objetoPago = iteradorPagos.next();
									Object[] objPago = (Object[]) objetoPago;
									Integer ejercicio = objPago[0]!=null ? (Integer)objPago[0] : null;
									objetoCosto.anios[ejercicio-anioInicial].anio = ejercicio;
									for(int m=0; m<12; m++){
										objetoCosto.anios[ejercicio-anioInicial].mes[m].planificado = objPago[3+m]!=null ? (BigDecimal)objPago[3+m] : null;
									}
								}	
							}else{
								//utilizar costo del objeto
								for(int a=0; a<(anioFinal-anioInicial+1); a++){
									objetoCosto.anios[a].anio=anioInicial+a;
									ObjetoCosto.stanio anioObj = objetoCosto.anios[a];
									if(objetoCosto.getFecha_inicial()!=null && objetoCosto.getFecha_final()!=null){
										int diaInicial = objetoCosto.getFecha_inicial().getDayOfMonth();
										int mesInicial = objetoCosto.getFecha_inicial().getMonthOfYear() -1;
										int anioInicialObj = objetoCosto.getFecha_inicial().getYear();
										int diaFinal = objetoCosto.getFecha_final().getDayOfMonth();
										int mesFinal = objetoCosto.getFecha_final().getMonthOfYear() -1;
										int anioFinalObj = objetoCosto.getFecha_final().getYear();
										if((anioInicial+a) >= anioInicialObj && (anioInicial+a)<=anioFinalObj){
											Integer acumulacionCostoId = objetoCosto.getAcumulacion_costoid()!=null ? objetoCosto.getAcumulacion_costoid() : 3;
											if(acumulacionCostoId.compareTo(1)==0){						
												if(anioInicialObj == (anioInicial+a)){
													anioObj.mes[mesInicial].planificado =  objetoCosto.getCosto() != null ? objetoCosto.getCosto() : new BigDecimal(0);
												}
											}else if(acumulacionCostoId.compareTo(2)==0){
												int dias = (int)((objetoCosto.getFecha_final().getMillis() - objetoCosto.getFecha_inicial().getMillis())/(1000*60*60*24));
												BigDecimal costoDiario = objetoCosto.getCosto() != null && dias > 0 ? objetoCosto.getCosto().divide(new BigDecimal(dias),5, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
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
											}else{
												if(anioFinalObj == anioObj.anio){
													anioObj.mes[mesFinal].planificado =  objetoCosto.getCosto() != null ? objetoCosto.getCosto() : new BigDecimal(0);
												}
											}
										}
									}
								}
							}
						}
						
						if(obtenerReal){
							if(objetoCosto.getObjeto_tipo() == 1){
							Prestamo objPrestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(objetoCosto.getObjeto_tipo(), 1);
								if(objPrestamo != null ){
									String codigoPresupuestario = Long.toString(objPrestamo.getCodigoPresupuestario());
									if(codigoPresupuestario!=null && !codigoPresupuestario.isEmpty()){
										Integer fuente = Utils.String2Int(codigoPresupuestario.substring(0,2));
										Integer organismo = Utils.String2Int(codigoPresupuestario.substring(2,6));
										Integer correlativo = Utils.String2Int(codigoPresupuestario.substring(6,10));
										objetoCosto = getCostoReal(objetoCosto, fuente, organismo, correlativo, anioInicial, anioFinal, conn_analytic, usuario);
									}
								}
							}
						}
						lstPrestamo.add(objetoCosto);
					}
				}
			}
			conn_analytic.close();
		}
		return lstPrestamo;
	}
	

	
	private static ObjetoCosto getCostoReal(ObjetoCosto objetoCosto, Integer fuente, Integer organismo, Integer correlativo, Integer anioInicial, Integer anioFinal, Connection conn, String usuario){
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
	
	public static boolean tieneHijos(int objetoId, int objetoTipo){
		if(ActividadDAO.getActividadesPorObjeto(objetoId, objetoTipo)!=null && ActividadDAO.getActividadesPorObjeto(objetoId, objetoTipo).size()>0){
			return true;
		}
		switch(objetoTipo){
		case 1:
			Proyecto proyecto = ProyectoDAO.getProyecto(objetoId);
			if (proyecto.getComponentes()!=null && proyecto.getComponentes().size()>0){
				return true;
			}
			return false;
		case 2:
			Componente componente = ComponenteDAO.getComponente(objetoId);
			if (componente.getProductos()!=null && componente.getProductos().size()>0){
				return true;
			}
			return false;
		case 3:
			Producto producto = ProductoDAO.getProductoPorId(objetoId);
			if (producto.getSubproductos()!=null && producto.getSubproductos().size()>0){
				return true;
			}
			return false;
		default:
			return false;
		}
	}
}
