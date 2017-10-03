package dao;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PlanAdquisicionPago;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PlanAdquisicionPagoDAO {
	
	public static List<PlanAdquisicionPago> getPagosByPlan(Integer planId){
		List<PlanAdquisicionPago> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<PlanAdquisicionPago> criteria = session.createQuery("FROM PlanAdquisicionPago p where p.planAdquisicion.id=:planId",PlanAdquisicionPago.class);
			criteria.setParameter("planId", planId);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarPago(PlanAdquisicionPago pago){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			session.beginTransaction();
			session.saveOrUpdate(pago);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("2", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean eliminarPago(PlanAdquisicionPago pago){
		boolean ret = false;
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(pago);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("3", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static PlanAdquisicionPago getPagobyId(int idPago){
		PlanAdquisicionPago ret = null;
		List<PlanAdquisicionPago> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			Query<PlanAdquisicionPago> pago = session.createQuery("FROM PlanAdquisicionPago p where p.id=:id",PlanAdquisicionPago.class);
			pago.setParameter("id", idPago);
			listRet = pago.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("4", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean eliminarPagos(Integer planId){
		boolean ret = false;
		List<PlanAdquisicionPago> Pagos = getPagosByPlan(planId);
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			for(PlanAdquisicionPago pago: Pagos){
				if(eliminarPago(pago))
					ret = true;
				else
					return false;
			}
			ret = true;
		}catch(Throwable e){
			CLogger.write("5", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean eliminarPagos(ArrayList<PlanAdquisicionPago> pagos){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			for(PlanAdquisicionPago pago: pagos){
				if(eliminarPago(pago))
					ret = true;
				else
					return false;
			}
			ret = true;
		}catch(Throwable e){
			CLogger.write("6", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static List<PlanAdquisicionPago> getPagosByObjetoTipo(Integer objetoId,Integer objetoTipo){
		List<PlanAdquisicionPago> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<PlanAdquisicionPago> criteria = session.createQuery("FROM PlanAdquisicionPago p where p.planAdquisicion.objetoId=:objetoId AND p.planAdquisicio.objetoTipo=:objetoTipo",PlanAdquisicionPago.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("7", PlanAdquisicionPagoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
