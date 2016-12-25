package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Desembolso;
import utilities.CHibernateSession;
import utilities.CLogger;

public class DesembolsoDAO {
	
	public static List<Desembolso> getDesembolsos(){
		List<Desembolso> ret = new ArrayList<Desembolso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Desembolso> criteria = builder.createQuery(Desembolso.class);
			Root<Desembolso> root = criteria.from(Desembolso.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Desembolso getDesembolsoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Desembolso ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Desembolso> criteria = builder.createQuery(Desembolso.class);
			Root<Desembolso> root = criteria.from(Desembolso.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarDesembolso(Desembolso desembolso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(desembolso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarDesembolso(Desembolso desembolso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			desembolso.setEstado(0);
			session.beginTransaction();
			session.update(desembolso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalDesembolso(Desembolso desembolso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(desembolso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Desembolso> getDesembolsosPagina(int pagina, int numerodesembolsos){
		List<Desembolso> ret = new ArrayList<Desembolso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Desembolso> criteria = session.createQuery("SELECT d FROM Desembolso d WHERE estado = 1",Desembolso.class);
			criteria.setFirstResult(((pagina-1)*(numerodesembolsos)));
			criteria.setMaxResults(numerodesembolsos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalDesembolsos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(d.id) FROM Desembolso d WHERE d.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", DesembolsoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	

}
