package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoImpacto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoImpactoDAO {
	public static ProyectoImpacto getProyectoImpacto(int idProyecto,int entidad){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoImpacto ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProyectoImpacto> criteria = builder.createQuery(ProyectoImpacto.class);
			Root<ProyectoImpacto> root = criteria.from(ProyectoImpacto.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("proyectoid"), idProyecto), 
					builder.equal(root.get("entidadentidad"), entidad),builder.equal(root.get("estado"), 1));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProyectoImpactoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProyectoImpacto(ProyectoImpacto proyectoImpacto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyectoImpacto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoImpactoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProyectoImpacto (ProyectoImpacto proyectoImpacto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			proyectoImpacto.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(proyectoImpacto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoImpactoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProyectoImpacto(ProyectoImpacto proyectoImpacto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(proyectoImpacto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoImpactoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoImpacto> getProyectoImpactoPorProyecto(int idProyecto){
		List<ProyectoImpacto> ret = new ArrayList<ProyectoImpacto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoImpacto> criteria = session.createNativeQuery(" select * "
					+ "from proyecto_impacto pi "
					+ "join proyecto p on p.id = pi.proyectoid "
					+ "where p.id = :idProy "
					+ "and pi.estado = 1 ",ProyectoImpacto.class);
			
			criteria.setParameter("idProy", idProyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoImpactoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
