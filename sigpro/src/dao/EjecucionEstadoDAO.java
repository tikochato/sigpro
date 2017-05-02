package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.EjecucionEstado;
import utilities.CHibernateSession;
import utilities.CLogger;

public class EjecucionEstadoDAO {
	public static Long getTotalEjecucionEstado(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(a.id) FROM EjecucionEstado a ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", EjecucionEstadoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<EjecucionEstado> getEjecucionEstadosPagina(int pagina, int numeroEjecucionEstado){
		List<EjecucionEstado> ret = new ArrayList<EjecucionEstado>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM EjecucionEstado a ";
			Query<EjecucionEstado> criteria = session.createQuery(query,EjecucionEstado.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroEjecucionEstado)));
			criteria.setMaxResults(numeroEjecucionEstado);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", EjecucionEstadoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
