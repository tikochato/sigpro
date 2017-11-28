package dao;

import java.util.List;

import org.hibernate.Session;

import pojo.LineaBase;
import utilities.CHibernateSession;
import utilities.CLogger;
import org.hibernate.query.Query;

public class LineaBaseDAO {
	public static List<LineaBase> getLineasBaseById(Integer proyectoid){
		List<LineaBase> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<LineaBase> criteria = session.createQuery("FROM LineaBase lb where lb.proyecto.id=:proyectoid", LineaBase.class);
			criteria.setParameter("proyectoid", proyectoid);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", LineaBaseDAO.class, e);
		}finally{
			session.close();
		}
		
		return ret;
	}
}
