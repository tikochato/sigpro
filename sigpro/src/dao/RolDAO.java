package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Rol;
import pojo.RolPermiso;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RolDAO {
	public static List<Rol> getRoles(){
		List<Rol> ret = new ArrayList<Rol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Rol> criteria = session.createQuery("FROM Rol r where r.estado = 1", Rol.class);
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
	public static Rol getRol(int id){
		Rol ret= new Rol();
		List<Rol> tmp = new ArrayList<Rol>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Rol> criteria = session.createQuery("FROM Rol r where r.id =:id", Rol.class);
			criteria.setParameter("id",id);
			tmp = criteria.getResultList();
			ret = tmp.get(0);
		}
		catch(Throwable e){
			CLogger.write("1", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RolPermiso> getPermisosPorRol(int rolid){
		List <RolPermiso> ret = new ArrayList<RolPermiso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RolPermiso> criteria = session.createQuery("FROM RolPermiso where estado=1 and id.rolid = :rolid", RolPermiso.class);
			criteria.setParameter("rolid", rolid);
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
