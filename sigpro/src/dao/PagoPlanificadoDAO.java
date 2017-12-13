package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PagoPlanificado;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PagoPlanificadoDAO {
	public static List<PagoPlanificado> getPagosPlanificadosPorObjeto(int objetoId, int objetoTipo){
		List<PagoPlanificado> ret = new ArrayList<PagoPlanificado>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<PagoPlanificado> criteria = session.createQuery("SELECT p FROM PagoPlanificado p where p.objetoId = ?1 and p.objetoTipo = ?2 and p.estado = 1", PagoPlanificado.class);
			criteria.setParameter(1, objetoId);
			criteria.setParameter(2, objetoTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", PagoPlanificado.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardar(PagoPlanificado pagoPlanificado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(pagoPlanificado);
			session.flush();
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", PagoPlanificadoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarPagoPlanificado(PagoPlanificado pagoPlanificado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			pagoPlanificado.setEstado(0);
			session.beginTransaction();
			session.update(pagoPlanificado);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3",PagoPlanificadoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalPagoPlanificado(PagoPlanificado pagoPlanificado){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(pagoPlanificado);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", PagoPlanificadoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
