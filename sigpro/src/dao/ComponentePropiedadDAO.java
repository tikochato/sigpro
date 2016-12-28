package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.ComponentePropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponentePropiedadDAO {
	
	public static List<ComponentePropiedad> getComponentePropiedadesPorTipoComponentePagina(int pagina,int idTipoComponente){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("select p from ComponentePropiedad p " 
					+ "inner join p.ctipoPropiedads ptp " 
					+ "inner join ptp.componenteTipo pt  "
					+ "where pt.id =  " + idTipoComponente + " ",ComponentePropiedad.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalComponentePropiedades(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(p.id) FROM CompoentePropiedad p ",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	public static List<ComponentePropiedad> getComponentePropiedadPaginaTotalDisponibles(int pagina, int numerocomponentepropiedades, String idPropiedades){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("select p from ComponentePropiedad p  "
					+ (idPropiedades!=null && idPropiedades.length()>0 ?  " where p.id not in ("+ idPropiedades + ")" : "") 
					,ComponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numerocomponentepropiedades)));
			criteria.setMaxResults(numerocomponentepropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	public static ComponentePropiedad getComponentePropiedadPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponentePropiedad ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ComponentePropiedad> criteria = builder.createQuery(ComponentePropiedad.class);
			Root<ComponentePropiedad> root = criteria.from(ComponentePropiedad.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id )));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("3", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarComponentePropiedad(ComponentePropiedad componentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(componentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarComponentePropiedad(ComponentePropiedad componentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			//componentePropiedad.setEstado(0);
			session.beginTransaction();
			session.update(componentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalComponentePropiedad(ComponentePropiedad componentePropiedad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(componentePropiedad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ComponentePropiedad> getComponentePropiedadesPagina(int pagina, int numeroComponentePropiedades){
		List<ComponentePropiedad> ret = new ArrayList<ComponentePropiedad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ComponentePropiedad> criteria = session.createQuery("SELECT c FROM ComponentePropiedad c ",ComponentePropiedad.class);
			criteria.setFirstResult(((pagina-1)*(numeroComponentePropiedades)));
			criteria.setMaxResults(numeroComponentePropiedades);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalComponentePropiedad(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM ComponentePropiedad c ",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", ComponentePropiedadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	

}
