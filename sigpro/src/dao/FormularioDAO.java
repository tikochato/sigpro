package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Formulario;
import pojo.FormularioItem;
import utilities.CHibernateSession;
import utilities.CLogger;

public class FormularioDAO {
	public static List<Formulario> getFormularios(){
		List<Formulario> ret = new ArrayList<Formulario>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Formulario> criteria = builder.createQuery(Formulario.class);
			Root<Formulario> root = criteria.from(Formulario.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", FormularioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Formulario getFormularioPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Formulario ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Formulario> criteria = builder.createQuery(Formulario.class);
			Root<Formulario> root = criteria.from(Formulario.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", FormularioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarFormulario(Formulario formulario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(formulario);
			session.flush();
			
			for (FormularioItem formularioItem : formulario.getFormularioItems()){
				session.saveOrUpdate(formularioItem);	
			}
			session.flush();
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", FormularioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Formulario> getFormularioPagina(int pagina, int numeroformularios){
		List<Formulario> ret = new ArrayList<Formulario>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Formulario> criteria = session.createQuery("SELECT f FROM Formulario f WHERE f.estado = 1",Formulario.class);
			criteria.setFirstResult(((pagina-1)*(numeroformularios)));
			criteria.setMaxResults(numeroformularios);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", Formulario.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalFormularios(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(f.id) FROM Formulario p WHERE f.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", FormularioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarFormulario(Formulario formulario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			formulario.setEstado(0);
			session.beginTransaction();
			session.update(formulario);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", FormularioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
