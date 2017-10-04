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
}
