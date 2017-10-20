package dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.SubcomponentePropiedadValor;
import pojo.SubcomponentePropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SubComponentePropiedadValorDAO {
	public static SubcomponentePropiedadValor getValorPorSubComponenteYPropiedad(int idPropiedad,int idSubComponente){
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubcomponentePropiedadValor ret = null;
		List<SubcomponentePropiedadValor> listRet = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<SubcomponentePropiedadValor> criteria = builder.createQuery(SubcomponentePropiedadValor.class);
			Root<SubcomponentePropiedadValor> root = criteria.from(SubcomponentePropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new SubcomponentePropiedadValorId(idSubComponente, idPropiedad)));
			listRet = session.createQuery(criteria).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("1", SubComponentePropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubComponentePropiedadValor(SubcomponentePropiedadValor subcomponentePropiedadValor){
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
	
	public static boolean eliminarTotalSubComponentePropiedadValor(SubcomponentePropiedadValor subcomponentePropiedadValor){
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
