package dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Pago;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PagoDAO {
	public static List<Pago> getPagosByObjetoTipo(Integer objetoId, Integer objetoTipo){
		List<Pago> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Pago> criteria = session.createQuery("FROM Pago p where p.objetoId=:objetoId and p.objetoTipo=:objetoTipo",Pago.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
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
		List<Pago> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			Query<Pago> pago = session.createQuery("FROM Pago p where p.id=:id",Pago.class);
			pago.setParameter("id", idPago);
			listRet = pago.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(Throwable e){
			CLogger.write("4", PagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean eliminarPagos(Integer objetoId, Integer objetoTipo){
		boolean ret = false;
		List<Pago> Pagos = getPagosByObjetoTipo(objetoId,objetoTipo);
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			for(Pago pago: Pagos){
				if(eliminarPago(pago))
					ret = true;
				else
					return false;
			}
			ret = true;
		}catch(Throwable e){
			CLogger.write("5", PagoDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
}
