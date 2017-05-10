package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.TipoMoneda;
import utilities.CHibernateSession;
import utilities.CLogger;

public class TipoMonedaDAO {
	public static Long getTotalAuotirzacionTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(t.id) FROM TipoMoneda t ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", TipoMonedaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<TipoMoneda> getAutorizacionTiposPagina(int pagina, int numeroTipoMoneda){
		List<TipoMoneda> ret = new ArrayList<TipoMoneda>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM TipoMoneda a ";
			Query<TipoMoneda> criteria = session.createQuery(query,TipoMoneda.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroTipoMoneda)));
			criteria.setMaxResults(numeroTipoMoneda);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", TipoMonedaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static TipoMoneda getTipoMonedaPorSimbolo(String simbolo){
		TipoMoneda ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM TipoMoneda a where a.simbolo = :simb ";
			Query<TipoMoneda> criteria = session.createQuery(query,TipoMoneda.class);
			criteria.setParameter("simb", simbolo);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", TipoMonedaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
