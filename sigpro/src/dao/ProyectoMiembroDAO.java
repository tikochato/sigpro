package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoMiembro;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoMiembroDAO {
	public static ProyectoMiembro getProyectoMiembro(int idProyecto,int colaborador){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoMiembro ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProyectoMiembro> criteria = builder.createQuery(ProyectoMiembro.class);
			Root<ProyectoMiembro> root = criteria.from(ProyectoMiembro.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("proyectoid"), idProyecto), 
					builder.equal(root.get("colaboradorid"), colaborador),builder.equal(root.get("estado"), 1));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProyectoMiembroDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProyectoMiembro(ProyectoMiembro ProyectoMiembro){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(ProyectoMiembro);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoMiembroDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProyectoMiembro (ProyectoMiembro ProyectoMiembro){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			ProyectoMiembro.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(ProyectoMiembro);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoMiembroDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProyectoMiembro(ProyectoMiembro ProyectoMiembro){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(ProyectoMiembro);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoMiembroDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoMiembro> getProyectoMiembroPorProyecto(int idProyecto){
		List<ProyectoMiembro> ret = new ArrayList<ProyectoMiembro>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoMiembro> criteria = session.createNativeQuery(" select * "
					+ "from proyecto_miembro pm "
					+ "join proyecto p on p.id = pm.proyectoid "
					+ "where p.id = :idProy "
					+ "and pm.estado = 1 ",ProyectoMiembro.class);
			
			criteria.setParameter("idProy", idProyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoMiembroDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
