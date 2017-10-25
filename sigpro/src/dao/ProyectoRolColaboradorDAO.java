package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoRolColaborador;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoRolColaboradorDAO {
	public static List<ProyectoRolColaborador> getMiembrosPorProyecto(Integer proyectoId){
		List<ProyectoRolColaborador> ret = new ArrayList<ProyectoRolColaborador>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoRolColaborador> criteria = session.createQuery("FROM ProyectoRolColaborador p where p.proyecto.id = ?1", ProyectoRolColaborador.class);
			criteria.setParameter(1, proyectoId);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ProyectoRolColaboradorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMiembro(ProyectoRolColaborador miembro){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(miembro);
			session.flush();
			session.flush();
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoRolColaboradorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMiembro(ProyectoRolColaborador miembro){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(miembro);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoRolColaboradorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
