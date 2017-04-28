package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.InteresTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class InteresTipoDAO {
	public static Long getTotalInteresTipos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(i.id) FROM InteresTipo i ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", InteresTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<InteresTipo> getInteresTiposPagina(int pagina, int numeroInteresTipo){
		List<InteresTipo> ret = new ArrayList<InteresTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT i FROM InteresTipo i ";
			Query<InteresTipo> criteria = session.createQuery(query,InteresTipo.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroInteresTipo)));
			criteria.setMaxResults(numeroInteresTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", InteresTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
