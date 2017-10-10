package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Etiqueta;
import utilities.CHibernateSession;
import utilities.CLogger;

public class EtiquetaDAO {
	
	public static List<Etiqueta> getEtiquetas(){
		List<Etiqueta> ret = new ArrayList<Etiqueta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Etiqueta> criteria = session.createQuery("FROM Etiqueta e where estado = 1", Etiqueta.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", EtiquetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Etiqueta getEtiquetaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		List<Etiqueta> listRet = null;
		Etiqueta ret = null;
		try{
			String query = "FROM Etiqueta e where e.id=:id";
			Query<Etiqueta> criteria = session.createQuery(query, Etiqueta.class);
			criteria.setParameter("id", id);
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", EtiquetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
