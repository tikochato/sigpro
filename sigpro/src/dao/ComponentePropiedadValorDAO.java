package dao;

import java.text.SimpleDateFormat;


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
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			CriteriaQuery<ComponentePropiedadValor> criteria = builder.createQuery(ComponentePropiedadValor.class);
			Root<ComponentePropiedadValor> root = criteria.from(ComponentePropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ComponentePropiedadValorId(idComponente, idPropiedad, "admin", sdf.parse("2017-01-10 00:00:00"))));
			ret = session.createQuery(criteria).getSingleResult();
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
