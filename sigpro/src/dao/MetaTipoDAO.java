package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Meta;
import pojo.MetaPlanificado;
import pojo.MetaTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class MetaTipoDAO {
	
	public static List<MetaTipo> getMetaTipos(){
		List<MetaTipo> ret = new ArrayList<MetaTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaTipo> criteria = builder.createQuery(MetaTipo.class);
			Root<MetaTipo> root = criteria.from(MetaTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static MetaTipo getMetaTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		MetaTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<MetaTipo> criteria = builder.createQuery(MetaTipo.class);
			Root<MetaTipo> root = criteria.from(MetaTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarMetaTipo(MetaTipo MetaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(MetaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarMetaTipo(MetaTipo MetaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			MetaTipo.setEstado(0);
			session.beginTransaction();
			session.update(MetaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMetaTipo(MetaTipo MetaTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(MetaTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<MetaTipo> getMetaTiposPagina(int pagina, int numeroMetaTipos, String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<MetaTipo> ret = new ArrayList<MetaTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT m FROM MetaTipo m WHERE estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " m.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " m.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(m.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<MetaTipo> criteria = session.createQuery(query,MetaTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroMetaTipos)));
			criteria.setMaxResults(numeroMetaTipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalMetaTipos(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(m.id) FROM MetaTipo m WHERE m.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " m.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " m.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(m.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Meta> getMetasPorObjeto(Integer objetoId, Integer objetoTipo){
		List<Meta> ret=null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select  *",
				"from meta m",
				"where m.objeto_id = ?1",
				"and m.objeto_tipo = ?2",
				"and m.dato_tipoid in (2,3)");
			
			Query<Meta> metavalor = session.createNativeQuery(query,Meta.class);
			metavalor.setParameter("1", objetoId);
			metavalor.setParameter("2", objetoTipo);
			
			ret =  metavalor.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<MetaPlanificado> getMetasPlanificadas(Integer metaId){
		List<MetaPlanificado> ret=null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select *",
					"from meta_planificado mp",
					"where mp.metaid = ?1",
					"and mp.estado = 1");
			
			Query<MetaPlanificado> metavalor = session.createNativeQuery(query,MetaPlanificado.class);
			metavalor.setParameter("1", metaId);
			
			ret =  metavalor.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", MetaTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
