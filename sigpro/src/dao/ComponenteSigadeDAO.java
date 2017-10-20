package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ComponenteSigade;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponenteSigadeDAO {
	public static boolean guardarComponenteSigade(ComponenteSigade ComponenteSigade){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(ComponenteSigade);
			session.flush();
			session.getTransaction().commit();
			session.close();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", ComponenteSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ComponenteSigade getComponenteSigadePorCodigoNumero(String codigoPresupuestario,Integer numero){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponenteSigade ret = null;
		List<ComponenteSigade> listRet = null;
		try{
			String Str_query = String.join(" ", "Select c FROM ComponenteSigade c",
					"where c.codigoPresupuestario = ?1 and c.numeroComponente = ?2");
			
			Query<ComponenteSigade> criteria = session.createQuery(Str_query, ComponenteSigade.class);
			criteria.setParameter(1, codigoPresupuestario);
			criteria.setParameter(2, numero);
		
			listRet = criteria.getResultList();
			 
			 ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", ComponenteSigadeDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	

}
