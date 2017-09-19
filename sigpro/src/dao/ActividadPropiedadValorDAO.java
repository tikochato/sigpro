package dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.ActividadPropiedadValor;
import pojo.ActividadPropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ActividadPropiedadValorDAO {
	public static ActividadPropiedadValor getValorPorActividadYPropiedad(int idPropiedad,int idActividad){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ActividadPropiedadValor ret = null;
		List<ActividadPropiedadValor> listRet = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ActividadPropiedadValor> criteria = builder.createQuery(ActividadPropiedadValor.class);
			Root<ActividadPropiedadValor> root = criteria.from(ActividadPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ActividadPropiedadValorId(idActividad, idPropiedad)));
			listRet = session.createQuery(criteria).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("1", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarActividadPropiedadValor(ActividadPropiedadValor actividadPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(actividadPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ActividadPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalActividadPropiedadValor(ActividadPropiedadValor actividadPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(actividadPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ActividadPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	

}
