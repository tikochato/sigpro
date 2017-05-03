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
	
	public static List<Meta> getMetasPagina(int pagina, int numeroMetas, int id, int tipo,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<Meta> ret = new ArrayList<Meta>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT m FROM Meta m WHERE m.estado = 1 and m.objetoId=:objetoId AND m.objetoTipo=:objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " m.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " m.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(m.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join(""," AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Meta> criteria = session.createQuery(query,Meta.class);
			criteria.setParameter("objetoId", id);
			criteria.setParameter("objetoTipo", tipo);
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
	
	public static Long getTotalMetas(Integer id, Integer tipo,String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(m.id) FROM Meta m WHERE m.estado=1 AND m.objetoId = :objetoId AND m.objetoTipo = :objetotipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " m.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " m.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(m.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("objetoId", id);
			conteo.setParameter("objetotipo", tipo);
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
