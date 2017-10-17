package dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.ComponentePropiedadValor;
import pojo.ComponentePropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SubComponentePropiedadValorDAO {
	public static ComponentePropiedadValor getValorPorSubComponenteYPropiedad(int idPropiedad,int idSubComponente){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponentePropiedadValor ret = null;
		List<ComponentePropiedadValor> listRet = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ComponentePropiedadValor> criteria = builder.createQuery(ComponentePropiedadValor.class);
			Root<ComponentePropiedadValor> root = criteria.from(ComponentePropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ComponentePropiedadValorId(idSubComponente, idPropiedad)));
			listRet = session.createQuery(criteria).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("1", SubComponentePropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubComponentePropiedadValor(ComponentePropiedadValor subcomponentePropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(subcomponentePropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", SubComponentePropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalSubComponentePropiedadValor(ComponentePropiedadValor subcomponentePropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(subcomponentePropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", SubComponentePropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	

}
