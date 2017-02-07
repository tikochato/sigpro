package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Riesgo;
import utilities.CHibernateSession;
import utilities.CLogger;


public class RiesgoDAO {
	public static List<Riesgo> getRiesgos(){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Riesgo> criteria = builder.createQuery(Riesgo.class);
			Root<Riesgo> root = criteria.from(Riesgo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Riesgo getRiesgoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Riesgo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Riesgo> criteria = builder.createQuery(Riesgo.class);
			Root<Riesgo> root = criteria.from(Riesgo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			riesgo.setEstado(0);
			session.beginTransaction();
			session.update(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRiesgo(Riesgo riesgo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(riesgo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Riesgo> getRiesgosPagina(int pagina, int numeroRiesgos){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Riesgo> criteria = session.createQuery("SELECT r FROM Riesgo r "
					+ "WHERE r.estado = 1 ",Riesgo.class);
			criteria.setFirstResult(((pagina-1)*(numeroRiesgos)));
			criteria.setMaxResults(numeroRiesgos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(r.id) FROM Riesgo r WHERE r.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Riesgo> getRiesgosPaginaPorObjeto (int pagina, int numeroRiesgos, int objetoId, int objetoTipo
			,String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<Riesgo> ret = new ArrayList<Riesgo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT r FROM Riesgo r "
					+ "join r.objetoRiesgos o "
					+ "WHERE r.estado = 1 "
					+ "and o.id.objetoId = :objid "
					+ "and o.id.objetoTipo = :objetoTipo";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY r.",columna_ordenada,orden_direccion ) : query;
			
			Query<Riesgo> criteria = session.createQuery(query,Riesgo.class);
			criteria.setParameter("objid", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setFirstResult(((pagina-1)*(numeroRiesgos)));
			criteria.setMaxResults(numeroRiesgos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRiesgosPorProyecto(int objetoId, int objetoTipo
			,String filtro_nombre, String filtro_usuario_creo,String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(r.id) FROM Riesgo r "
					+ "join r.objetoRiesgos o "
					+ "WHERE r.estado = 1 "
					+ "and o.id.objetoId = :objid "
					+ "and o.id.objetoTipo = :objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("objid",objetoId);
			conteo.setParameter("objetoTipo", objetoTipo);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", RiesgoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
