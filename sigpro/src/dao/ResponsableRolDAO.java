package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import pojo.ResponsableRol;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ResponsableRolDAO {
	public static List<ResponsableRol> getResponsableRol(Integer responsableTipo){
		List<ResponsableRol> ret = new ArrayList<ResponsableRol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ResponsableRol> criteria = builder.createQuery(ResponsableRol.class);
			Root<ResponsableRol> root = criteria.from(ResponsableRol.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			if (responsableTipo!=null)
				criteria.select( root ).where(builder.equal(root.get("responsableTipo"),responsableTipo));
			
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ResponsableRolDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
