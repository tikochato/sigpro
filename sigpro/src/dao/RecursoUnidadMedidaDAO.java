package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.RecursoUnidadMedida;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RecursoUnidadMedidaDAO {
	
	public static List<RecursoUnidadMedida> getRecursoUnidadMedidas(){
		List<RecursoUnidadMedida> ret = new ArrayList<RecursoUnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RecursoUnidadMedida> criteria = builder.createQuery(RecursoUnidadMedida.class);
			Root<RecursoUnidadMedida> root = criteria.from(RecursoUnidadMedida.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static RecursoUnidadMedida getRecursoUnidadMedidaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RecursoUnidadMedida ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RecursoUnidadMedida> criteria = builder.createQuery(RecursoUnidadMedida.class);
			Root<RecursoUnidadMedida> root = criteria.from(RecursoUnidadMedida.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRecursoUnidadMedida(RecursoUnidadMedida RecursoUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(RecursoUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRecursoUnidadMedida(RecursoUnidadMedida RecursoUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			RecursoUnidadMedida.setEstado(0);
			session.beginTransaction();
			session.update(RecursoUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRecursoUnidadMedida(RecursoUnidadMedida RecursoUnidadMedida){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(RecursoUnidadMedida);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RecursoUnidadMedida> getRecursoUnidadMedidasPagina(int pagina, int numeroRecursoUnidadMedidas,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<RecursoUnidadMedida> ret = new ArrayList<RecursoUnidadMedida>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query =" SELECT c FROM RecursoUnidadMedida c WHERE estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<RecursoUnidadMedida> criteria = session.createQuery(query,RecursoUnidadMedida.class);
			criteria.setFirstResult(((pagina-1)*(numeroRecursoUnidadMedidas)));
			criteria.setMaxResults(numeroRecursoUnidadMedidas);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRecursoUnidadMedidas(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM RecursoUnidadMedida c WHERE c.estado=1";
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
			CLogger.write("7", RecursoUnidadMedidaDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
