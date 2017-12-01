package dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.Componente;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subcomponente;
import pojo.Subproducto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionDAO {
	
	public static int guardarPlanAdquisicion(PlanAdquisicion planAdquisicion){
		int ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(planAdquisicion);
			actualizarCostoPlanificadoObjeto(planAdquisicion, session);
			session.getTransaction().commit();
			ret = planAdquisicion.getId();
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisicion getPlanAdquisicionById(int planAdquisicionId){
		PlanAdquisicion ret = null;
		List<PlanAdquisicion> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicion where id=:planAdquisicionId";
			Query<PlanAdquisicion> criteria = session.createQuery(query, PlanAdquisicion.class);
			criteria.setParameter("planAdquisicionId", planAdquisicionId);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static PlanAdquisicion getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
		List<PlanAdquisicion> retList = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicion pa where pa.objetoId=:objetoId and pa.objetoTipo=:objetoTipo and pa.estado=1";
			Query<PlanAdquisicion> criteria = session.createQuery(query, PlanAdquisicion.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			retList = criteria.getResultList();
			
		}catch(Throwable e){
			CLogger.write("3", PlanAdquisicionDAO.class, e);
		}
		finally{
			retList = (retList.size()>0) ? retList : null;
			session.close();
		}
		return retList!=null ? retList.get(0) : null;
	}
	
	public static List<PlanAdquisicion> getPlanAdquisicionesByObjeto(int objetoTipo, int ObjetoId){
		List<PlanAdquisicion> retList = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM PlanAdquisicion pa where pa.objetoId=:objetoId and pa.objetoTipo=:objetoTipo and pa.estado=1";
			Query<PlanAdquisicion> criteria = session.createQuery(query, PlanAdquisicion.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			retList = criteria.getResultList();
			
		}catch(Throwable e){
			CLogger.write("4", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
			retList = (retList.size()>0) ? retList : null;
		}
		return retList;
	}
	
	public static boolean borrarPlan(PlanAdquisicion plan){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(plan);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("5", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean borrarTodosPlan(Integer objetoId, Integer objetoTipo){
		boolean ret = false;
		if(objetoId!=null){
			List<PlanAdquisicion> planes = getPlanAdquisicionesByObjeto(objetoTipo, objetoId);
			Session session = CHibernateSession.getSessionFactory().openSession();
			try{
				session.beginTransaction();
				if(planes!=null){
					for(int i=0; i<planes.size();i++){
						planes.get(i).setEstado(0);
						session.saveOrUpdate(planes.get(i));
					}
				}
				session.getTransaction().commit();
				ret = true;
			}catch(Throwable e){
				CLogger.write("6", PlanAdquisicionDAO.class, e);
			}
			finally{
				session.close();
			}
		}
		return ret;
	}
	
	public static List<PlanAdquisicion> getAdquisicionesNotIn(Integer objetoId, Integer objetoTipo,List<Integer> adquisiciones){
		List<PlanAdquisicion> ret = new ArrayList<PlanAdquisicion>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT pa FROM PlanAdquisicion as pa "
					+ "WHERE pa.estado = 1 "
					+ "and pa.objetoId = :objid "
					+ "and pa.objetoTipo = :objetoTipo "
					+ "and pa.id NOT IN (:ids)";
			
			Query<PlanAdquisicion> criteria = session.createQuery(query,PlanAdquisicion.class);
			criteria.setParameter("objid", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameterList("ids", adquisiciones);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("7", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static void actualizarCostoPlanificadoObjeto(PlanAdquisicion pa,Session session){
		BigDecimal ret = new BigDecimal(0);
		Integer objetoId = pa.getObjetoId();
		Integer objetoTipo = pa.getObjetoTipo();
		List<Actividad> actividades = ActividadDAO.getActividadesPorObjeto(objetoId, objetoTipo);
		for(Actividad actividad: actividades)
			ret = ret.add(actividad.getCosto());
		switch(objetoTipo){
			case 0: Proyecto proyecto = ProyectoDAO.getProyecto(objetoId);
				if(proyecto.getComponentes()==null || proyecto.getComponentes().size()==0){
					if(actividades==null || actividades.size()==0){
						if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
							Iterator<PlanAdquisicionPago> iPagos = pa.getPlanAdquisicionPagos().iterator();
							while(iPagos.hasNext()){
								PlanAdquisicionPago pago = iPagos.next();
								ret=ret.add(pago.getPago()!=null ? pago.getPago() : new BigDecimal(0));
							}
						}
						else
							ret=pa.getTotal();
					}
				}
				else{
					for(Componente componente:proyecto.getComponentes())
						ret = ret.add(componente.getCosto()!=null ? componente.getCosto() : new BigDecimal(0));
				}
				proyecto.setCosto(ret);
				session.saveOrUpdate(proyecto);
				break;
			case 1: Componente componente = ComponenteDAO.getComponente(objetoId);
				if(componente.getProductos()==null || componente.getProductos().size()==0 || componente.getSubcomponentes()==null || componente.getSubcomponentes().size()==0 ){
					if(actividades==null || actividades.size()==0){
						if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
							Iterator<PlanAdquisicionPago> iPagos = pa.getPlanAdquisicionPagos().iterator();
							while(iPagos.hasNext()){
								PlanAdquisicionPago pago = iPagos.next();
								ret=ret.add(pago.getPago()!=null ? pago.getPago() : new BigDecimal(0));
							}
						}
						else
							ret=pa.getTotal();
					}
				}
				else{
					if(componente.getProductos()!=null)
						for(Producto producto:componente.getProductos())
							ret = ret.add(producto.getCosto()!=null ? producto.getCosto() : new BigDecimal(0));
					if(componente.getSubcomponentes()!=null)
						for(Subcomponente subcomponente:componente.getSubcomponentes())
							ret = ret.add(subcomponente.getCosto()!=null ? subcomponente.getCosto() : new BigDecimal(0));
				}
				componente.setCosto(ret);
				session.saveOrUpdate(componente);
				break;
			case 3: Producto producto = ProductoDAO.getProductoPorId(objetoId);
				if(producto.getSubproductos()==null || producto.getSubproductos().size()==0){
					if(actividades==null || actividades.size()==0){
						if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
							Iterator<PlanAdquisicionPago> iPagos = pa.getPlanAdquisicionPagos().iterator();
							while(iPagos.hasNext()){
								PlanAdquisicionPago pago = iPagos.next();
								ret=ret.add(pago.getPago()!=null ? pago.getPago() : new BigDecimal(0));
							}
						}
						else
							ret= pa.getTotal();
					}
				}
				else{
					for(Subproducto subproducto:producto.getSubproductos())
						ret = ret.add(subproducto.getCosto()!=null ? subproducto.getCosto() : new BigDecimal(0));
				}
				producto.setCosto(ret);
				session.saveOrUpdate(producto);
				break;
			case 4:
				Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId);
				if(actividades!=null && actividades.size()>0){
					subproducto.setCosto(ret);
					session.saveOrUpdate(subproducto);
				}
				break;
			case 5:
				Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
				if(actividades!=null && actividades.size()>0){
					actividad.setCosto(ret);
					session.saveOrUpdate(actividad);
				}
				break;
		}
	}
	
	public static List<PlanAdquisicionPago> getPagos(Integer planadquisicionId){
		List<PlanAdquisicionPago> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<PlanAdquisicionPago> criteria = session.createQuery("FROM PlanAdquisicionPago pap where pap.planAdquisicion.id=:planadquisicionId order by pap.fechaPago", PlanAdquisicionPago.class);
			criteria.setParameter("planadquisicionId", planadquisicionId);
			ret = criteria.getResultList();
		}catch(Exception e){
			CLogger.write("8", PlanAdquisicionDAO.class, e);
		}finally {
			session.close();
		}
		
		return ret;
	}
	
	public static List<?> getInfoNog(Integer nog){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "Select * from sipro_analytic.mv_gc_adquisiciones",
					"where nog=?1");
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", nog);
			ret = criteria.getResultList();
		}catch(Exception e){
			CLogger.write("9", PlanAdquisicionDAO.class, e);
		}finally{
			session.close();
		}
		
		return ret;
	}
	
	
	public static List<PlanAdquisicion> getPlanAdquisicionesByObjetoLB(int objetoTipo, int ObjetoId, String lineaBase){
		List<PlanAdquisicion> retList = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = String.join(" ", "SELECT pa.* FROM sipro_history.plan_adquisicion pa",
					"where pa.objeto_id=:objetoId",
					"and pa.objeto_tipo=:objetoTipo",
					lineaBase != null ? "and pa.linea_base like '%"+lineaBase+"%'" : "and pa.actual=1",
					"and pa.estado=1");
			Query<PlanAdquisicion> criteria = session.createNativeQuery(query, PlanAdquisicion.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			retList = criteria.getResultList();
			
		}catch(Throwable e){
			CLogger.write("4", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
			retList = (retList.size()>0) ? retList : null;
		}
		return retList;
	}
	
	public static PlanAdquisicion getPlanAdquisicionByObjetoLB(int objetoTipo, int ObjetoId, String lineaBase){
		List<PlanAdquisicion> retList = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = String.join(" ", "SELECT pa.* FROM sipro_history.plan_adquisicion pa",
					"where pa.objeto_id=:objetoId",
					"and pa.objeto_tipo=:objetoTipo",
					lineaBase != null ? "and pa.linea_base like '%" + lineaBase + "%'" : "and pa.actual=1",
					"and pa.estado=1");
			Query<PlanAdquisicion> criteria = session.createNativeQuery(query, PlanAdquisicion.class);
			criteria.setParameter("objetoId", ObjetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			retList = criteria.getResultList();
			
		}catch(Throwable e){
			CLogger.write("3", PlanAdquisicionDAO.class, e);
		}
		finally{
			retList = (retList.size()>0) ? retList : null;
			session.close();
		}
		return retList!=null ? retList.get(0) : null;
	}
}
