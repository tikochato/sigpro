package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoPropiedadValor;
import pojo.ProyectoPropiedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;


public class ProyectoPropiedadValorDAO {
	public static ProyectoPropiedadValor getValorPorProyectoYPropiedad(int idPropiedad,int idProyecto){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoPropiedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProyectoPropiedadValor> criteria = builder.createQuery(ProyectoPropiedadValor.class);
			Root<ProyectoPropiedadValor> root = criteria.from(ProyectoPropiedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ProyectoPropiedadValorId(idProyecto, idPropiedad)),builder.equal(root.get("estado"), 1));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (NoResultException e){
			
		} catch (Throwable e) {
			CLogger.write("1", ProyectoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProyectoPropiedadValor(ProyectoPropiedadValor proyectoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyectoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", ProyectoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarProyectoPropiedadValor(ProyectoPropiedadValor proyectoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			proyectoPropiedadValor.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(proyectoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRiesgoPropiedadValor(ProyectoPropiedadValor proyectoPropiedadValor){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(proyectoPropiedadValor);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProyectoPropiedadValor> getProyectoPropiedadadesValoresPorProyecto(int idProyecto){
		List<ProyectoPropiedadValor> ret = new ArrayList<ProyectoPropiedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropiedadValor> criteria = session.createNativeQuery(" select * "
					+ "from proyecto_propiedad_valor ppv "
					+ "join proyecto p on p.id = ppv.proyectoid "
					+ "where p.id = :idProy "
					+ "and ppv.estado = 1 ",ProyectoPropiedadValor.class);
			
			criteria.setParameter("idProy", idProyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
