package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Meta;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MetaDAO {
	
	public static List<Meta> getMetas(){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Meta> criteria = builder.createQuery(Meta.class);
			Root<Meta> root = criteria.from(Meta.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Meta getMetaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Meta ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Meta> criteria = builder.createQuery(Meta.class);
			Root<Meta> root = criteria.from(Meta.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(Meta);
			session.getTransaction().commit();
			session.beginTransaction();
			if(Meta.getProducto()!=null){
				Meta.getProducto().getMetas().add(Meta);
				ProductoDAO.guardarProducto(Meta.getProducto());
			}
			if(Meta.getComponente()!=null){
				Meta.getComponente().getMetas().add(Meta);
				ComponenteDAO.guardarComponente(Meta.getComponente());
			}
			if(Meta.getProyecto()!=null){
				Meta.getProyecto().getMetas().add(Meta);
				ProyectoDAO.guardarProyecto(Meta.getProyecto());
			}
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Meta.setEstado(0);
			session.beginTransaction();
			session.update(Meta);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMeta(Meta Meta){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Meta);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Meta> getMetasPagina(int pagina, int numeroMetas, int id, int tipo){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String where="";
			switch(tipo){
				case 1: where = "m.proyecto.id = "; break;
				case 2: where = "m.componente.id = "; break;
				case 3: where = "m.producto.id = "; break;
			}
			where = String.join("", where, String.valueOf(id));
			Query<Meta> criteria = session.createQuery(String.join("","SELECT m FROM Meta m WHERE estado = 1 and ",where),Meta.class);
			criteria.setFirstResult(((pagina-1)*(numeroMetas)));
			criteria.setMaxResults(numeroMetas);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalMetas(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(m.id) FROM Meta m WHERE m.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", MetaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
