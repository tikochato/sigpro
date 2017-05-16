package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ResponsableTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ResponsableTipoDAO {

	public static List<ResponsableTipo> getResponsableTiposPagina(int pagina, int numeroResponsableTipo){
		List<ResponsableTipo> ret = new ArrayList<ResponsableTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM ResponsableTipo a ";
			Query<ResponsableTipo> criteria = session.createQuery(query,ResponsableTipo.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroResponsableTipo)));
			criteria.setMaxResults(numeroResponsableTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalResponsableTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(t.id) FROM ResponsableTipo t ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ResponsableTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}