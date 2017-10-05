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
	
	public static List<PlanAdquisicion> getPlanAdquisicionByObjeto(int objetoTipo, int ObjetoId){
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
			CLogger.write("4", PlanAdquisicionDAO.class, e);
		}
		finally{
			session.close();
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
			CLogger.write("5", PlanAdquisicionDAO.class, e);
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
			ret.add(actividad.getCosto());
		switch(objetoTipo){
			case 1: Proyecto proyecto = ProyectoDAO.getProyecto(objetoId);
				if(proyecto.getComponentes()==null || proyecto.getComponentes().size()==0){
					if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
						Iterator<PlanAdquisicionPago> iPagos = pa.getPlanAdquisicionPagos().iterator();
						while(iPagos.hasNext()){
							PlanAdquisicionPago pago = iPagos.next();
							ret.add(pago.getPago());
						}
					}
					else
						ret=pa.getTotal();
				}
				else{
					for(Componente componente:proyecto.getComponentes())
						ret.add(componente.getCosto());
				}
				proyecto.setCosto(ret);
				session.save(proyecto);
				break;
			case 2: Componente componente = ComponenteDAO.getComponente(objetoId);
				if(componente.getProductos()==null || componente.getProductos().size()==0){
					if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
						Iterator<PlanAdquisicionPago> iPagos = pa.getPlanAdquisicionPagos().iterator();
						while(iPagos.hasNext()){
							PlanAdquisicionPago pago = iPagos.next();
							ret.add(pago.getPago());
						}
					}
					else
						ret=pa.getTotal();
				}
				else{
					for(Producto producto:componente.getProductos())
						ret.add(producto.getCosto());
				}
				componente.setCosto(ret);
				session.save(componente);
				break;
			case 3: Producto producto = ProductoDAO.getProductoPorId(objetoId);
				if(producto.getSubproductos()==null || producto.getSubproductos().size()==0){
					if(pa.getPlanAdquisicionPagos()!=null && pa.getPlanAdquisicionPagos().size()>0){
						Iterator<PlanAdquisicionPago> iPagos = pa.getPlanAdquisicionPagos().iterator();
						while(iPagos.hasNext()){
							PlanAdquisicionPago pago = iPagos.next();
							ret.add(pago.getPago());
						}
					}
					else
						ret= pa.getTotal();
				}
				else{
					for(Subproducto subproducto:producto.getSubproductos())
						ret.add(subproducto.getCosto());
				}
				producto.setCosto(ret);
				session.save(producto);
				break;
			case 4:
				Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId);
				subproducto.setCosto(ret);
				session.save(subproducto);
				break;
			case 5:
				Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
				actividad.setCosto(ret);
				session.save(actividad);
				break;
		}
	}
	
}
