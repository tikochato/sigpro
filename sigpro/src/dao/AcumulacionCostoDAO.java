package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.AcumulacionCosto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class AcumulacionCostoDAO {
	
	public static List<AcumulacionCosto> getAcumulacionesCosto(){
		List<AcumulacionCosto> ret = new ArrayList<AcumulacionCosto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM AcumulacionCosto a WHERE a.estado = 1";
			Query<AcumulacionCosto> criteria = session.createQuery(query,AcumulacionCosto.class);
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
	
	public static AcumulacionCosto getAcumulacionCostoById(Integer id){
		AcumulacionCosto ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		List<AcumulacionCosto> lstret = null;
		try{
			Query<AcumulacionCosto> criteria = session.createQuery("FROM AcumulacionCosto ac where ac.id= " + id, AcumulacionCosto.class);
			lstret = criteria.getResultList();
			if(lstret != null && lstret.size() > 0){
				ret = lstret.get(0);
			}
		}catch(Throwable e){
			CLogger.write("2", AcumulacionCosto.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
