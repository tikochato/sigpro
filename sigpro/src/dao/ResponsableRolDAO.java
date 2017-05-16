package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ResponsableRol;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ResponsableRolDAO {
	public static List<ResponsableRol> getResponsableTiposPagina(int pagina, int numeroResponsableRol){
		List<ResponsableRol> ret = new ArrayList<ResponsableRol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM ResponsableRol a ";
			Query<ResponsableRol> criteria = session.createQuery(query,ResponsableRol.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroResponsableRol)));
			criteria.setMaxResults(numeroResponsableRol);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ResponsableRol.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalResponsableRol(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(t.id) FROM ResponsableRol t ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", ResponsableRol.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
