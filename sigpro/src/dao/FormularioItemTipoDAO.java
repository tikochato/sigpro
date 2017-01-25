package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.FormularioItemTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class FormularioItemTipoDAO {
	public static Long getTotalFormularioItemTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(f.id) FROM FormularioItemTipo f WHERE f.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static FormularioItemTipo getFormularioItemTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		FormularioItemTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FormularioItemTipo> criteria = builder.createQuery(FormularioItemTipo.class);
			Root<FormularioItemTipo> root = criteria.from(FormularioItemTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarFormularioItemTipo(FormularioItemTipo formularioItemTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(formularioItemTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarFormularioItemTipo(FormularioItemTipo formularioItemTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			formularioItemTipo.setEstado(0);
			session.beginTransaction();
			session.update(formularioItemTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<FormularioItemTipo> geFormularioItemTiposPagina(int pagina, int numeroFormularioItemTipos){
		List<FormularioItemTipo> ret = new ArrayList<FormularioItemTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<FormularioItemTipo> criteria = session.createQuery("SELECT f FROM FormularioItemTipo f WHERE f.estado = 1",FormularioItemTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroFormularioItemTipos)));
			criteria.setMaxResults(numeroFormularioItemTipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
