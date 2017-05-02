package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.AutorizacionTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class AutorizacionTipoDAO {
	public static Long getTotalAuotirzacionTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(a.id) FROM AutorizacionTipo a ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", AutorizacionTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<AutorizacionTipo> getAutorizacionTiposPagina(int pagina, int numeroAutorizacionTipo){
		List<AutorizacionTipo> ret = new ArrayList<AutorizacionTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM AutorizacionTipo a ";
			Query<AutorizacionTipo> criteria = session.createQuery(query,AutorizacionTipo.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroAutorizacionTipo)));
			criteria.setMaxResults(numeroAutorizacionTipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", AutorizacionTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
