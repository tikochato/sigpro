package dao;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.ComponentePropiedadValor;
import pojo.ComponentePropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponentePropiedadValorDAO {
	public static ComponentePropiedadValor getValorPorComponenteYPropiedad(int idPropiedad,int idComponente){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponentePropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ComponentePropiedadValor> criteria = builder.createQuery(ComponentePropiedadValor.class);
			Root<ComponentePropiedadValor> root = criteria.from(ComponentePropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ComponentePropiedadValorId(idComponente, idPropiedad)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (NoResultException e){
			
		} catch (Throwable e) {
			CLogger.write("1", ProductoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarComponentePropiedadValor(ComponentePropiedadValor componentePropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(componentePropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ComponentePropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalComponentePropiedadValor(ComponentePropiedadValor componentePropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(componentePropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ComponentePropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	

}
