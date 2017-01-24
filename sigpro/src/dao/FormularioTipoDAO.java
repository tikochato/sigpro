package dao;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.FormularioTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class FormularioTipoDAO {
	
	public static List<FormularioTipo> getFormularioTipos(){
		List<FormularioTipo> ret = new ArrayList<FormularioTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FormularioTipo> criteria = builder.createQuery(FormularioTipo.class);
			Root<FormularioTipo> root = criteria.from(FormularioTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static FormularioTipo getFormularioTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		FormularioTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FormularioTipo> criteria = builder.createQuery(FormularioTipo.class);
			Root<FormularioTipo> root = criteria.from(FormularioTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarFormularioTipo(FormularioTipo formularioTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(formularioTipo);
			session.flush();
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarFormularioTipo(FormularioTipo formularioTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			formularioTipo.setEstado(0);
			session.beginTransaction();
			session.update(formularioTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4",FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarTotalFormularioTipo(FormularioTipo formularioTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(formularioTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List<FormularioTipo> getFormularioTiposPagina(int pagina, int numeroformulariostipo){
		List<FormularioTipo> ret = new ArrayList<FormularioTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<FormularioTipo> criteria = session.createQuery("SELECT f FROM FormularioTipo f WHERE f.estado = 1",FormularioTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroformulariostipo)));
			criteria.setMaxResults(numeroformulariostipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalFormularioTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(f.id) FROM FormularioTipo f WHERE f.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", FormularioTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
	


