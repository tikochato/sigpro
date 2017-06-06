package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Rol;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RolDAO {
	public static List<Rol> getRoles(){
		List<Rol> ret = new ArrayList<Rol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Rol> criteria = session.createQuery("FROM Rol r where p.estado = 1", Rol.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
}
