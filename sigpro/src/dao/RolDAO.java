package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Cooperante;
import pojo.Rol;
import pojo.RolPermiso;
import pojo.RolUsuarioProyecto;
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
	public static RolUsuarioProyecto getRolUser(String user){
		RolUsuarioProyecto ret= new RolUsuarioProyecto();
		List<RolUsuarioProyecto> tmp = new ArrayList<RolUsuarioProyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RolUsuarioProyecto> criteria = session.createQuery("FROM RolUsuarioProyecto r where r.id.usuario = :usuario", RolUsuarioProyecto.class);
			criteria.setParameter("usuario",user);
			tmp = criteria.getResultList();
			if(tmp.size()>0)
				ret = tmp.get(0);
		}
		catch(Throwable e){
			CLogger.write("1", RolDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static Cooperante getCooperante(String user){
		Cooperante ret = new Cooperante();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			List <Cooperante> tmp = new ArrayList<Cooperante>();
			String query="Select p.proyecto.cooperante from ProyectoUsuario p where p.id.usuario=:usuario";
			Query<Cooperante> criteria = session.createQuery(query, Cooperante.class);
			criteria.setParameter("usuario",user);
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
