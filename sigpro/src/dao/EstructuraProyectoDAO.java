package dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;

import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Prestamo;
import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.CPrestamoCostos;
import utilities.Utils;

public class EstructuraProyectoDAO {
		
	
	
	public static List<?> getEstructuraProyecto(Integer idProyecto){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{

			String query =
					"select * from ( "+
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
					"where a.estado=1 and  a.treepath like '"+(10000000+idProyecto)+"%'"+
					") arbol "+
					"order by treePath ";			
				
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", idProyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", EstructuraProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<?> getEstructuraProyecto(Integer idProyecto, String usuario){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =
				"select * from ( "+
				"select p.id, p.nombre, 1 objeto_tipo,  p.treePath, p.fecha_inicio, "+
				"p.fecha_fin, p.duracion, p.duracion_dimension,p.costo,0, p.acumulacion_costoid  "+
				"from proyecto p "+
				"where p.id= ?1 and p.estado=1 and p.id in ( select proyectoid from proyecto_usuario where usuario = ?2 ) "+
				"union "+
				"select c.id, c.nombre, 2 objeto_tipo,  c.treePath, c.fecha_inicio, "+
				"c.fecha_fin , c.duracion, c.duracion_dimension,c.costo,0,c.acumulacion_costoid "+
				"from componente c "+
				"where c.proyectoid=?1 and c.estado=1 and c.id in (select componenteid from componente_usuario where usuario = ?2 ) "+
				"union "+
				"select pr.id, pr.nombre, 3 objeto_tipo , pr.treePath, pr.fecha_inicio, "+
				"pr.fecha_fin, pr.duracion, pr.duracion_dimension,pr.costo,0,pr.acumulacion_costoid "+
				"from producto pr "+
				"left outer join componente c on c.id=pr.componenteid "+
				"left outer join proyecto p on p.id=c.proyectoid "+
				"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1 and pr.id in ( select productoid from producto_usuario where usuario = ?2 )  "+
				"union "+
				"select sp.id, sp.nombre, 4 objeto_tipo,  sp.treePath, sp.fecha_inicio, "+
				"sp.fecha_fin , sp.duracion, sp.duracion_dimension,sp.costo,0,sp.acumulacion_costoid "+
				"from subproducto sp "+
				"left outer join producto pr on pr.id=sp.productoid "+
				"left outer join componente c on c.id=pr.componenteid "+
				"left outer join proyecto p on p.id=c.proyectoid "+
				"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1 and sp.estado=1 and sp.id and pr.id in ( select productoid from producto_usuario where usuario = ?2 ) "+
				"union "+
				"select a.id, a.nombre, 5 objeto_tipo,  a.treePath, a.fecha_inicio, "+
				"a.fecha_fin , a.duracion, a.duracion_dimension,a.costo,a.pred_objeto_id,a.acumulacion_costo acumulacion_costoid "+
				"from actividad a "+
				"where a.estado=1 and  a.treepath like '"+(10000000+idProyecto)+"%'"+
				") arbol "+
				"order by treePath ";			
			
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", idProyecto);
			criteria.setParameter("2", usuario);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", EstructuraProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Nodo getEstructuraProyectoArbol(int id, String usuario){
		Nodo root = null;
		List<?> estructuras = EstructuraProyectoDAO.getEstructuraProyecto(id, usuario);
		if(estructuras.size()>0){
			try{
				Object[] dato = (Object[]) estructuras.get(0);
				int id_ = dato[0]!=null ? (Integer)dato[0] : 0;
				int objeto_tipo = dato[2]!=null ? ((BigInteger)dato[2]).intValue() : 0;
				String nombre = dato[1]!=null ? (String)dato[1] : null;
				int nivel = (dato[3]!=null) ? ((String)dato[3]).length()/8 : 0;
				root = new Nodo(id_, objeto_tipo, nombre, nivel, new ArrayList<Nodo>(), null, false);
				Nodo nivel_actual_estructura = root;
				for(int i=1; i<estructuras.size(); i++){
					dato = (Object[]) estructuras.get(i);
					id_ = dato[0]!=null ? (Integer)dato[0] : 0;
					objeto_tipo = dato[2]!=null ? ((BigInteger)dato[2]).intValue() : 0;
					nombre = dato[1]!=null ? (String)dato[1] : null;
					nivel = (dato[3]!=null) ? ((String)dato[3]).length()/8 : 0;
					Nodo nodo = new Nodo(id_, objeto_tipo, nombre, nivel, new ArrayList<Nodo>(), null, false);
					if(nodo.nivel!=nivel_actual_estructura.nivel+1){
						if(nodo.nivel>nivel_actual_estructura.nivel){
							nivel_actual_estructura = nivel_actual_estructura.children.get(nivel_actual_estructura.children.size()-1);
						}
						else{
							for(int j=0; j<=(nivel_actual_estructura.nivel-nodo.nivel+1); j++)
								nivel_actual_estructura=nivel_actual_estructura.parent;
						}
					}
					nodo.parent = nivel_actual_estructura;
					nivel_actual_estructura.children.add(nodo);
				}
			}
			catch(Throwable e){
				root = null;
				CLogger.write("2", EstructuraProyectoDAO.class, e);
			}
		}
		return root;
	}
	
	public static Nodo getEstructuraProyectoArbolProyectosComponentesProductos(int id,String usuario){
		Nodo root = null;
		List<?> estructuras = EstructuraProyectoDAO.getEstructuraProyecto(id);
		if(estructuras.size()>0){
			try{
				Object[] dato = (Object[]) estructuras.get(0);
				int id_ = dato[0]!=null ? (Integer)dato[0] : 0;
				int objeto_tipo = dato[2]!=null ? ((BigInteger)dato[2]).intValue() : 0;
				String nombre = dato[1]!=null ? (String)dato[1] : null;
				int nivel = dato[4]!=null ? (Integer)dato[4] : 0;
				boolean estado = checkPermiso(id,objeto_tipo,usuario);
				root = new Nodo(id_, objeto_tipo, nombre, nivel, new ArrayList<Nodo>(), null, estado);
				Nodo nivel_actual_estructura = root;
				for(int i=1; i<estructuras.size(); i++){
					dato = (Object[]) estructuras.get(i);
					id_ = dato[0]!=null ? (Integer)dato[0] : 0;
					objeto_tipo = dato[2]!=null ? ((BigInteger)dato[2]).intValue() : 0;
					nombre = dato[1]!=null ? (String)dato[1] : null;
					nivel = dato[4]!=null ? (Integer)dato[4] : 0;
					estado = checkPermiso(id_,objeto_tipo,usuario);
					if(objeto_tipo<4){
						Nodo nodo = new Nodo(id_, objeto_tipo, nombre, nivel, new ArrayList<Nodo>(), null, estado);
						if(nodo.nivel!=nivel_actual_estructura.nivel+1){
							if(nodo.nivel>nivel_actual_estructura.nivel){
								nivel_actual_estructura = nivel_actual_estructura.children.get(nivel_actual_estructura.children.size()-1);
							}
							else{
								for(int j=0; j<=(nivel_actual_estructura.nivel-nodo.nivel+1); j++)
									nivel_actual_estructura=nivel_actual_estructura.parent;
							}
						}
						nodo.parent = nivel_actual_estructura;
						nivel_actual_estructura.children.add(nodo);
					}
				}
			}
			catch(Throwable e){
				root = null;
				CLogger.write("3", EstructuraProyectoDAO.class, e);
			}
		}
		return root;
	}
	
	public static List<?> getActividadesProyecto(Integer prestamoId){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String str_Query = String.join(" ", "select a.id, a.nombre, 5 objeto_tipo,  a.treePath, a.nivel, a.fecha_inicio,", 
					"a.fecha_fin , a.duracion, a.duracion_dimension,a.costo,a.pred_objeto_id,a.acumulacion_costo acumulacion_costoid,",
					"a.porcentaje_avance", 
					"from actividad a", 
					"where a.estado=1 and ((a.proyecto_base= ?1)", 
					"OR (a.componente_base in (select id from componente where proyectoid= ?1))", 
					"OR (a.producto_base in (select p.id from producto p, componente c where p.componenteid=c.id and c.proyectoid= ?1))",
					")");
			
			Query<?> criteria = session.createNativeQuery(str_Query);
			criteria.setParameter("1", prestamoId);
			ret = criteria.getResultList();
		}catch(Exception e){
			CLogger.write("4", EstructuraProyectoDAO.class, e);
		}finally{
			session.close();
		}
		
		return ret;
	}
	
	public static List<?> getActividadesByTreePath(String treePath, Integer idPrestamo){
		List<Object[]> ret = new ArrayList<Object[]>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			List<?> lstActividadesPrestamo = getActividadesProyecto(idPrestamo);
			Object[] temp = new Object[5];
			for(Object objeto : lstActividadesPrestamo){
				Object[] obj = (Object[])objeto;
				String treePathObj = (String)obj[3];
				if(treePathObj != null && treePath != null){
					//if(treePath.length() + 6 == treePathObj.length()){
						if(treePathObj.substring(0, treePath.length()).equals(treePath)){
							temp = new Object[]{(Integer)obj[0], (String)obj[1], (Date)obj[5], (Date)obj[6], (Integer)obj[12]};
							ret.add(temp);
						}
					//}
				}
			}
		}catch(Exception e){
			CLogger.write("5", EstructuraProyectoDAO.class, e);
		}finally{
			session.close();
		}
		
		return ret;
	}

	public static ArrayList<Nodo> getEstructuraProyectosArbol(String usuario) {
		ArrayList<Nodo> ret = new ArrayList<Nodo>();
		List<Proyecto> proyectos = ProyectoDAO.getTodosProyectos();
		if(proyectos!=null){
			for(int i=0; i<proyectos.size(); i++){
				Nodo proyecto = getEstructuraProyectoArbolProyectosComponentesProductos(proyectos.get(i).getId(),usuario);
				if(proyecto!=null)
					ret.add(proyecto);
			}
		}
		return (ret.size()>0 ? ret : null);
	}
	
	public static boolean checkPermiso(int id, int objeto_tipo, String usuario){
		boolean ret = false;
		switch(objeto_tipo){
			case 1: ret = UsuarioDAO.checkUsuarioProyecto(usuario,id); break;
			case 2: ret = UsuarioDAO.checkUsuarioComponente(usuario,id); break;
			case 3: ret = UsuarioDAO.checkUsuarioProducto(usuario,id); break;
		}
		return ret;
	}
	
	public static List<List<Integer>> getHijosCompleto(String treePathPadre, List<?> estruturaProyecto){
		ArrayList<List<Integer>> ret = new ArrayList<List<Integer>>();
		for(Object objeto : estruturaProyecto){
			Object[] obj = (Object[])objeto;
			String treePath = (String)obj[3];
			if(treePath != null){
				if(treePath.length() >= treePathPadre.length()+6){
					String path = treePath.substring(0, treePathPadre.length()); 
					if(path.equals(treePathPadre)){
						List<Integer> valor = new ArrayList<Integer>();
						valor.add((Integer)obj[0]);
						valor.add(((BigInteger) obj[2]).intValue());
						ret.add(valor);
					}	
				}	
			}
		}
		
		return ret;
	}
	
	public static List<String> getHijos(String treePathPadre, List<?> estruturaProyecto){
		ArrayList<String> ret = new ArrayList<String>();
		for(Object objeto : estruturaProyecto){
			Object[] obj = (Object[])objeto;
			String treePath = (String)obj[3];
			if(treePath != null){
				if(treePath.length() == treePathPadre.length()+6){
					String path = treePath.substring(0, treePathPadre.length()); 
					if(path.equals(treePathPadre)){
						ret.add((Integer)obj[0]+ "," + ((BigInteger) obj[2]).intValue());
					}	
				}	
			}
		}
		
		return ret;
	}
	

	public List<CPrestamoCostos> getEstructuraConCostos(int idPrestamo, int anioInicial, int anioFinal, String usuario){
		List<CPrestamoCostos> lstPrestamo = new ArrayList<>();
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
		
		Prestamo objPrestamo;
		String codigoPresupuestario = "";
		Integer fuente = 0;
		Integer organismo = 0;
		Integer correlativo = 0;
		
		for(Object objeto : estructuraProyecto){
			Object[] obj = (Object[]) objeto;
			Integer nivel = obj[4]!=null ? (Integer)obj[4] : null;
			if(nivel != null){
				Integer objeto_id = obj[0]!=null ? (Integer)obj[0] : null;
				String nombre = obj[1]!=null ? (String)obj[1] : null;
				Integer objeto_tipo = obj[2]!=null ? ((BigInteger) obj[2]).intValue() : null;
				DateTime fecha_inicial = obj[5]!=null ? new DateTime((Timestamp)obj[5]) : null;
				DateTime fecha_final = obj[6]!=null ? new DateTime((Timestamp)obj[6]) : null;
				Integer acumulacion_costoid = obj[11]!=null ? Integer.valueOf(obj[11].toString()) : null;
				BigDecimal costo = obj[9]!=null ? (BigDecimal)obj[9] : null;
				Integer programa = obj[12]!=null ? (Integer)obj[12] : null;
				Integer subprograma = obj[13]!=null ? (Integer)obj[13] : null;
				Integer proyecto = obj[14]!=null ? (Integer)obj[14] : null;
				Integer actividad = obj[15]!=null ? (Integer)obj[15] : null;
				Integer obra = obj[16]!=null ? (Integer)obj[16] : null;
				
				CPrestamoCostos tempPrestamo =  new CPrestamoCostos(nombre, objeto_id, objeto_tipo, nivel, fecha_inicial, fecha_final, null,
						acumulacion_costoid, costo, programa, subprograma, proyecto, actividad, obra);
				
				tempPrestamo.setAnios(tempPrestamo.inicializarStanio(anioInicial, anioFinal));
				
				
//				CPrestamoCostos tempPrestamo =  new CPrestamoCostos();
//				tempPrestamo.setObjeto_id(obj[0]!=null ? (Integer)obj[0] : null);
//				tempPrestamo.setNombre(obj[1]!=null ? (String)obj[1] : null);
//				tempPrestamo.setNivel(nivel);
//				tempPrestamo.setObjeto_tipo(obj[2]!=null ? ((Integer) obj[2]).intValue() : null);
//				tempPrestamo.setFecha_inicial(obj[5]!=null ? new DateTime((Timestamp)obj[5]) : null);
//				tempPrestamo.setFecha_final(obj[6]!=null ? new DateTime((Timestamp)obj[6]) : null);
//				tempPrestamo.setAnios(tempPrestamo.inicializarStanio(anioInicial, anioFinal));
//				tempPrestamo.setAcumulacion_costoid(obj[11]!=null ? (Integer)obj[11] : null);
//				tempPrestamo.setCosto(obj[9]!=null ? (BigDecimal)obj[9] : null);
//				tempPrestamo.setPrograma(obj[12]!=null ? (Integer)obj[12] : null);
//				tempPrestamo.setSubprograma(obj[13]!=null ? (Integer)obj[13] : null);
//				tempPrestamo.setProyecto(obj[14]!=null ? (Integer)obj[14] : null);
//				tempPrestamo.setActividad(obj[15]!=null ? (Integer)obj[15] : null);
//				tempPrestamo.setObra(obj[16]!=null ? (Integer)obj[16] : null);
				
				try {
					if(CMariaDB.connect()){
						Connection conn = CMariaDB.getConnection();
						if(CMariaDB.connectAnalytic()){
							Connection conn_analytic = CMariaDB.getConnection_analytic();
							if(tempPrestamo.getObjeto_tipo() == 1){
							objPrestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(tempPrestamo.getObjeto_tipo(), 1);
								if(objPrestamo != null ){
									codigoPresupuestario = Long.toString(objPrestamo.getCodigoPresupuestario());
									if(codigoPresupuestario!=null && !codigoPresupuestario.isEmpty()){
										fuente = Utils.String2Int(codigoPresupuestario.substring(0,2));
										organismo = Utils.String2Int(codigoPresupuestario.substring(2,6));
										correlativo = Utils.String2Int(codigoPresupuestario.substring(6,10));
										tempPrestamo = getPresupuestoReal(tempPrestamo, fuente, organismo, correlativo, anioInicial, anioFinal, conn_analytic, usuario);
									}
								}
							}			
							tempPrestamo = getPresupuestoPlanificado(tempPrestamo, usuario);
							conn_analytic.close();
						}
						conn.close();
					}
				} catch (SQLException e) {
					CLogger.write("3", EstructuraProyectoDAO.class, e);
				}
				tempPrestamo.setFecha_inicial(null);
				tempPrestamo.setFecha_final(null);
				lstPrestamo.add(tempPrestamo);

			}
		}
		if(lstPrestamo!=null && !lstPrestamo.isEmpty()){
			CPrestamoCostos.stanio[] aniosTemp = calcularCostoRecursivo(lstPrestamo, 1, 1, anioInicial, anioFinal);
			CPrestamoCostos.stanio[] anios = lstPrestamo.get(0).getAnios();
			for(int a=0; a<anios.length; a++){
				for(int m=0; m<12; m++){
					if(aniosTemp[a].mes[m].planificado.compareTo(BigDecimal.ZERO) > 0){
						anios[a].mes[m].planificado = aniosTemp[a].mes[m].planificado;
					}
					
					if(aniosTemp[a].mes[m].real.compareTo(BigDecimal.ZERO) > 0){
						anios[a].mes[m].real = aniosTemp[a].mes[m].real;
					}
				}
			}
		}
		return lstPrestamo;
	}
	
	private CPrestamoCostos getPresupuestoPlanificado(CPrestamoCostos prestamo, String usuario){
		if(prestamo!=null && prestamo.getObjeto_id()!=null){
			List<PlanAdquisicion> lstplan = PlanAdquisicionDAO.getPlanAdquisicionByObjeto(prestamo.getObjeto_tipo(), prestamo.getObjeto_id());	
				Calendar fechaInicial = Calendar.getInstance();
				for(CPrestamoCostos.stanio anioObj: prestamo.getAnios()){
					if(lstplan!=null && !lstplan.isEmpty()){
						for(PlanAdquisicion plan : lstplan){
							List<PlanAdquisicionPago> pagos = PlanAdquisicionPagoDAO.getPagosByPlan(plan.getId());
							if(pagos!= null && pagos.size() > 0){
								for(PlanAdquisicionPago pago : pagos){
									fechaInicial.setTime(pago.getFechaPago());
									int mes = fechaInicial.get(Calendar.MONTH);
									int anio = fechaInicial.get(Calendar.YEAR);					
									if(anio == anioObj.anio){
										anioObj.mes[mes].planificado = anioObj.mes[mes].planificado.add(pago.getPago());
									}
								}
							}
						}
					}else{
						int diaInicial = prestamo.getFecha_inicial().getDayOfMonth();
						int mesInicial = prestamo.getFecha_inicial().getMonthOfYear() -1;
						int anioInicial = prestamo.getFecha_inicial().getYear();
						int diaFinal = prestamo.getFecha_final().getDayOfMonth();
						int mesFinal = prestamo.getFecha_final().getMonthOfYear() -1;
						int anioFinal = prestamo.getFecha_final().getYear();
						if(anioObj.anio >= anioInicial && anioObj.anio<=anioFinal){
							if(prestamo.getAcumulacion_costoid() != null){
								if(prestamo.getAcumulacion_costoid() == 1){						
									if(anioInicial == anioObj.anio){
										anioObj.mes[mesInicial].planificado =  prestamo.getCosto() != null ? prestamo.getCosto() : new BigDecimal(0);
									}
								}else if(prestamo.getAcumulacion_costoid() == 2){
									int dias = (int)((prestamo.getFecha_final().getMillis() - prestamo.getFecha_inicial().getMillis())/(1000*60*60*24));
									BigDecimal costoDiario = prestamo.getCosto() != null ? prestamo.getCosto().divide(new BigDecimal(dias),5, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
									int inicioActual = 0;
									if(anioObj.anio == anioInicial){
										inicioActual = mesInicial;
									}
									
									int finMes = anioObj.anio==anioFinal ? mesFinal : 11;
									for(int m=inicioActual; m<=finMes; m++){
										if(anioObj.anio == anioInicial && m==mesInicial){
											if(m==mesFinal){
												int diasMes = diaFinal-diaInicial;
												anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
											}else{
												Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
												int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
												diasMes = diasMes-diaInicial;
												anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
											}
										}else if(anioObj.anio == anioFinal && m== mesFinal){
											anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diaFinal));
										}else{
											Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
											int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
											anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
										}
									}
								}else if(prestamo.getAcumulacion_costoid() ==3){
									if(anioFinal == anioObj.anio){
										anioObj.mes[mesFinal].planificado =  prestamo.getCosto() != null ? prestamo.getCosto() : new BigDecimal(0);
									}
								}
							}
						}
					}
				}
		}
		return prestamo;
	}
	
	private CPrestamoCostos getPresupuestoReal(CPrestamoCostos prestamo, Integer fuente, Integer organismo, Integer correlativo, Integer anioInicial, Integer anioFinal, Connection conn, String usuario){
		ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo = new ArrayList<ArrayList<BigDecimal>>();
		
			if(prestamo.getObjeto_tipo() == 1){
				presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoProyecto(fuente, organismo, correlativo,anioInicial,anioFinal, conn);
			}else{
				presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
						anioInicial, anioFinal, prestamo.getPrograma(), prestamo.getSubprograma(), prestamo.getProyecto(), 
						prestamo.getActividad(), prestamo.getObra(), conn);
			}
		
		if(presupuestoPrestamo.size() > 0){
			int pos = 0;
			for(ArrayList<BigDecimal> objprestamopresupuesto : presupuestoPrestamo){
				for (int m=0; m<12; m++){
					prestamo.getAnios()[pos].mes[m].real = objprestamopresupuesto.get(m) != null ? objprestamopresupuesto.get(m) : new BigDecimal(0);
				}
				prestamo.getAnios()[pos].anio = objprestamopresupuesto.get(12) != null ? objprestamopresupuesto.get(12).intValueExact() : 0;
				pos = pos + 1;
			}
		}
		return prestamo;
	}
	
	private CPrestamoCostos.stanio[] calcularCostoRecursivo(List<CPrestamoCostos> lstPrestamo, Integer posicion, Integer nivel, Integer anioInicial, Integer anioFinal){
		CPrestamoCostos.stanio[] anios = lstPrestamo.get(0).inicializarStanio(anioInicial, anioFinal);
		while(posicion <lstPrestamo.size()){
			CPrestamoCostos prestamo = lstPrestamo.get(posicion);
			if(prestamo.getNivel() != null){
				if(prestamo.getNivel().equals(nivel)){
					if(posicion+1<lstPrestamo.size()){
						if(lstPrestamo.get(posicion+1).getNivel().equals(nivel+1)){
							CPrestamoCostos.stanio[] aniosTemp = calcularCostoRecursivo(lstPrestamo, posicion+1, nivel+1, anioInicial, anioFinal);				
							for(int a=0; a<anios.length; a++){
								for(int m=0; m<12; m++){
									if(aniosTemp[a].mes[m].planificado.compareTo(BigDecimal.ZERO) > 0){
										prestamo.getAnios()[a].mes[m].planificado = aniosTemp[a].mes[m].planificado;
									}
									
									if(aniosTemp[a].mes[m].real.compareTo(BigDecimal.ZERO) > 0){
										prestamo.getAnios()[a].mes[m].real = aniosTemp[a].mes[m].real;
									}
								}
							}
						}
					}
					for(int a=0; a<anios.length; a++){
						for(int m=0; m<12;m++){
							anios[a].mes[m].planificado = anios[a].mes[m].planificado.add(prestamo.getAnios()[a].mes[m].planificado);
							anios[a].mes[m].real = anios[a].mes[m].real.add(prestamo.getAnios()[a].mes[m].real);
						}
					}
				}else if(prestamo.getNivel() < nivel){
					return anios;
				}
			}
			posicion++;
		}
		return anios;
	}

}
