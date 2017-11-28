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
					"where c.codigoPresupuestario = ?1 and c.numeroComponente = ?2 and c.estado = 1");
			
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
	
	public static ComponenteSigade getComponenteSigadePorId(Integer id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponenteSigade ret = null;
		List<ComponenteSigade> listRet = null;
		try{
			String Str_query = String.join(" ", "Select c FROM ComponenteSigade c",
					"where c.id = ?1");
			
			Query<ComponenteSigade> criteria = session.createQuery(Str_query, ComponenteSigade.class);
			criteria.setParameter(1, id);
			listRet = criteria.getResultList();
			if(listRet != null && !listRet.isEmpty()){
				ret = listRet.get(0);
			}
		}catch(Exception e){
			CLogger.write("3", ComponenteSigadeDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static ComponenteSigade getComponenteSigadePorIdHistory(Integer id, String lineaBase){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponenteSigade ret = null;
		List<ComponenteSigade> listRet = null;
		try{
			String Str_query = String.join(" ", "select * from sipro_history.componente_sigade c",
							"where c.id = ?1",
							lineaBase != null ? "and c.linea_base = ?2 ": "and c.actual = 1");
			
			Query<ComponenteSigade> criteria = session.createNativeQuery(Str_query, ComponenteSigade.class);
			criteria.setParameter(1, id);
			if (lineaBase != null)
				criteria.setParameter(2, lineaBase);
			listRet = criteria.getResultList();
			if(listRet != null && !listRet.isEmpty()){
				ret = listRet.get(0);
			}
		}catch(Exception e){
			CLogger.write("3", ComponenteSigadeDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}

}
