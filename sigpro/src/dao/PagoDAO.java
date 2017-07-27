package dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Pago;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PagoDAO {
	public static List<Pago> getPagosByIdPlan(int idPlan){
		List<Pago> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Pago> criteria = session.createQuery("FROM Pago p where p.planAdquisiciones.id=:id",Pago.class);
			criteria.setParameter("id", idPlan);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", PagoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarPago(Pago pago){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			session.beginTransaction();
			session.saveOrUpdate(pago);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("2", PagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean eliminarPago(Pago pago){
		boolean ret = false;
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(pago);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("3", PagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static Pago getPagobyId(int idPago){
		Pago ret = null;
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			Query<Pago> pago = session.createQuery("FROM Pago p where p.id=:id",Pago.class);
			pago.setParameter("id", idPago);
			ret = pago.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", PagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
}
