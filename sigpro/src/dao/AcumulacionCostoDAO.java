package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.AcumulacionCosto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class AcumulacionCostoDAO {
	public static List<AcumulacionCosto> getAcumulacionCostoPagina(int pagina, int numeroAcumulacionCosto){
		List<AcumulacionCosto> ret = new ArrayList<AcumulacionCosto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM AcumulacionCosto a ";
			Query<AcumulacionCosto> criteria = session.createQuery(query,AcumulacionCosto.class);
			
			criteria.setFirstResult(((pagina-1)*(numeroAcumulacionCosto)));
			criteria.setMaxResults(numeroAcumulacionCosto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", AcumulacionCosto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalAcumulacionCosto(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(a.id) FROM AcumulacionCosto a ";
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		} catch (NoResultException e){
			
		} catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", AcumulacionCosto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
