package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ComponenteTipo;
import pojo.CtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ComponenteTipoDAO {

	public static List<ComponenteTipo> getComponenteTipos(){
		List<ComponenteTipo> ret = new ArrayList<ComponenteTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ComponenteTipo> criteria = builder.createQuery(ComponenteTipo.class);
			Root<ComponenteTipo> root = criteria.from(ComponenteTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		

	
	public static ComponenteTipo getComponenteTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ComponenteTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ComponenteTipo> criteria = builder.createQuery(ComponenteTipo.class);
			Root<ComponenteTipo> root = criteria.from(ComponenteTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarComponenteTipo(ComponenteTipo componenteTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(componenteTipo);
			session.flush();
			
			if (componenteTipo.getCtipoPropiedads()!=null && componenteTipo.getCtipoPropiedads().size()>0){
				for (CtipoPropiedad propiedad : componenteTipo.getCtipoPropiedads()){
					session.saveOrUpdate(propiedad);	
				}
			}
			session.flush();
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	
	
	public static boolean eliminarComponenteTipo(ComponenteTipo componenteTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			componenteTipo.setEstado(0);
			session.beginTransaction();
			session.update(componenteTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6",ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarTotalComponenteTipo(ComponenteTipo componenteTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(componenteTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List<ComponenteTipo> getComponenteTiposPagina(int pagina, int numerocomponentestipo 
			,String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ComponenteTipo> ret = new ArrayList<ComponenteTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT c FROM ComponenteTipo c WHERE c.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<ComponenteTipo> criteria = session.createQuery(query,ComponenteTipo.class);
			criteria.setFirstResult(((pagina-1)*(numerocomponentestipo)));
			criteria.setMaxResults(numerocomponentestipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalComponenteTipo(String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(c.id) FROM ComponenteTipo c WHERE c.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
