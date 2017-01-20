package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoPropedadValor;
import pojo.ProyectoPropedadValorId;
import utilities.CHibernateSession;
import utilities.CLogger;


public class ProyectoPropiedadValorDAO {
	public static ProyectoPropedadValor getValorPorProyectoYPropiedad(int idPropiedad,int idProyecto){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoPropedadValor ret = null;
		try {
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProyectoPropedadValor> criteria = builder.createQuery(ProyectoPropedadValor.class);
			Root<ProyectoPropedadValor> root = criteria.from(ProyectoPropedadValor.class);
			criteria.select(root);
			criteria.where(builder.equal(root.get("id"), new ProyectoPropedadValorId(idProyecto, idPropiedad)));
			ret = session.createQuery(criteria).getSingleResult();
		} catch (Throwable e) {
			CLogger.write("1", ProyectoPropiedadValorDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarProyectoPropiedadValor(ProyectoPropedadValor proyectoPropiedadValor){
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
	
	public static boolean eliminarProyectoPropiedadValor(ProyectoPropedadValor proyectoPropiedadValor){
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
	
	public static boolean eliminarTotalRiesgoPropiedadValor(ProyectoPropedadValor proyectoPropiedadValor){
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
	
	public static List<ProyectoPropedadValor> getProyectoPropiedadadesValoresPorProyecto(int idProyecto){
		List<ProyectoPropedadValor> ret = new ArrayList<ProyectoPropedadValor>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProyectoPropedadValor> criteria = session.createNativeQuery(" select * "
					+ "from proyecto_propedad_valor ppv "
					+ "join proyecto p on p.id = ppv.proyectoid "
					+ "where p.id = :idProy "
					+ "and ppv.estado = 1 ",ProyectoPropedadValor.class);
			
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
