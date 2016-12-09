package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.UnidadEjecutora;
import utilities.CHibernateSession;
import utilities.CLogger;

public class UnidadEjecutoraDAO {
	public static List<UnidadEjecutora> getUnidadEjecutoras(){
		List<UnidadEjecutora> ret = new ArrayList<UnidadEjecutora>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<UnidadEjecutora> criteria = builder.createQuery(UnidadEjecutora.class);
			Root<UnidadEjecutora> root = criteria.from(UnidadEjecutora.class);
			criteria.select( root );
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", UnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
