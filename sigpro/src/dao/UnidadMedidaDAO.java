package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;

import pojo.UnidadMedida;
import utilities.CHibernateSession;
import utilities.CLogger;

public class UnidadMedidaDAO {
	public static List<UnidadMedida> getUnidadMedida(){
		List<UnidadMedida> ret = new ArrayList<UnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<UnidadMedida> criteria = session.createQuery("FROM UnidadMedida um where um.estado=1", UnidadMedida.class);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", UnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static UnidadMedida getUnidadMedidaById(int id){
		UnidadMedida ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query = "FROM UnidadMedida where id=:id";
			Query<UnidadMedida> criteria = session.createQuery(query, UnidadMedida.class);
			criteria.setParameter("id", id);
			ret = criteria.getSingleResult();
		}catch(Throwable e){
			CLogger.write("2", UnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
}
