package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.RolUnidadEjecutora;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RolUnidadEjecutoraDAO {
	public static List<RolUnidadEjecutora> getRolesPorDefecto(){
		List<RolUnidadEjecutora> ret = new ArrayList<RolUnidadEjecutora>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RolUnidadEjecutora> criteria = session.createQuery("FROM RolUnidadEjecutora r where r.rolPredeterminado = 1 AND r.estado  = 1", RolUnidadEjecutora.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean guardarRolUnidadEjecutora(RolUnidadEjecutora rolUnidadEjecutora){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(rolUnidadEjecutora);
			session.flush();
			session.flush();
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("2", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalMiembro(RolUnidadEjecutora rolUnidadEjecutora){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(rolUnidadEjecutora);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ActividadTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RolUnidadEjecutora> getRoles(){
		List<RolUnidadEjecutora> ret = new ArrayList<RolUnidadEjecutora>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<RolUnidadEjecutora> criteria = session.createQuery("FROM RolUnidadEjecutora r where r.estado  = 1", RolUnidadEjecutora.class);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<RolUnidadEjecutora> getRolesUnidadEjecutoraPagina(int pagina, int numeroRoles,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<RolUnidadEjecutora> ret = new ArrayList<RolUnidadEjecutora>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT r FROM RolUnidadEjecutora r WHERE r.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<RolUnidadEjecutora> criteria = session.createQuery(query,RolUnidadEjecutora.class);
			criteria.setFirstResult(((pagina-1)*(numeroRoles)));
			criteria.setMaxResults(numeroRoles);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRolesUnidadEjecutora(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(r.id) FROM RolUnidadEjecutora r WHERE r.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " r.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " r.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(r.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("6", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static RolUnidadEjecutora getRolUnidadEjecutoraPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		RolUnidadEjecutora ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<RolUnidadEjecutora> criteria = builder.createQuery(RolUnidadEjecutora.class);
			Root<RolUnidadEjecutora> root = criteria.from(RolUnidadEjecutora.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("7", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRolUnidadEjecutora(RolUnidadEjecutora rolUnidadEjecutora){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			rolUnidadEjecutora.setEstado(0);
			session.beginTransaction();
			session.update(rolUnidadEjecutora);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RolUnidadEjecutoraDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
