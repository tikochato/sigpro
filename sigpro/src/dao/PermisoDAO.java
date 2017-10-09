package dao;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Permiso;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PermisoDAO {
	public static List<Permiso> getPermisos(){
		List <Permiso> ret = new ArrayList <Permiso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permiso> criteria = builder.createQuery(Permiso.class);
			Root<Permiso> root = criteria.from(Permiso.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			criteria.orderBy(builder.asc(root.get("nombre")));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", PermisoDAO.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}

	public static boolean guardarPermiso(Permiso permiso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(permiso);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("1",PermisoDAO.class,e);
		}finally{
			session.close();
		}

		return ret;
	}
	public static Permiso getPermiso(String nombrepermiso){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Permiso ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Permiso> criteria = builder.createQuery(Permiso.class);
			Root<Permiso> root = criteria.from(Permiso.class);
			criteria.where( builder.and(builder.equal(root.get("nombre"), nombrepermiso)));
			ret = session.createQuery( criteria ).getSingleResult();
		}catch(Throwable e){
			CLogger.write("1",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		 return ret;
	}
	public static boolean eliminarPermiso(Permiso permiso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			permiso.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(permiso);
			session.getTransaction().commit();
			ret = true;

		}catch(Throwable e){
			CLogger.write("4",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		return ret;
	}

	public static Permiso getPermisoById(Integer idpermiso){
		Permiso ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			ret = session.get(Permiso.class,idpermiso );
		}catch(Throwable e){
			CLogger.write("1",PermisoDAO.class,e);
		}finally{
			session.close();
		}
		return ret;
	}

	public static List<Permiso> getPermisosPagina(int pagina, int numeroPermisos, String filtro_id, String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		List <Permiso> ret = new ArrayList <Permiso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query ="FROM Permiso p  where estado= :estado ";
			String query_a="";
			if(filtro_id!=null && filtro_id.trim().length()>0)
				query_a = String.join("",query_a, " p.id LIKE '%",filtro_id,"%' ");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, (query_a.length()>0 ? " OR " :""),  " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND ",query_a,"") : ""));
			Query <Permiso> criteria = session.createQuery(query,Permiso.class);
			criteria.setParameter("estado", 1);
			criteria.setFirstResult(((pagina-1)*(numeroPermisos)));
			criteria.setMaxResults(numeroPermisos);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("6", PermisoDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	public static Long getTotalPermisos( String filtro_id, String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query  = "SELECT count(p.id) FROM Permiso p WHERE p.estado=1";
			String query_a="";
			if(filtro_id!=null && filtro_id.trim().length()>0)
				query_a = String.join("",query_a, " p.id LIKE '%",filtro_id,"%' ");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", PermisoDAO.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
}
