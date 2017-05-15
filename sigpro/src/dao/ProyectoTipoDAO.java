package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProyectoTipo;
import pojo.PtipoPropiedad;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProyectoTipoDAO {

	public static List<ProyectoTipo> getProyectoTipos(){
		List<ProyectoTipo> ret = new ArrayList<ProyectoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProyectoTipo> criteria = builder.createQuery(ProyectoTipo.class);
			Root<ProyectoTipo> root = criteria.from(ProyectoTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static ProyectoTipo getProyectoTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ProyectoTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<ProyectoTipo> criteria = builder.createQuery(ProyectoTipo.class);
			Root<ProyectoTipo> root = criteria.from(ProyectoTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", FormularioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarProyectoTipo(ProyectoTipo proyectotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(proyectotipo);
			session.flush();
			if(!proyectotipo.getPtipoPropiedads().isEmpty()){
				for (PtipoPropiedad propiedad : proyectotipo.getPtipoPropiedads()){
					session.saveOrUpdate(propiedad);
				}
			}
			
			session.flush();

			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<ProyectoTipo> getProyectosTipoPagina(int pagina, int numeroproyectotipos,
			String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<ProyectoTipo> ret = new ArrayList<ProyectoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT p FROM ProyectoTipo p WHERE estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<ProyectoTipo> criteria = session.createQuery(query,ProyectoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectotipos)));
			criteria.setMaxResults(numeroproyectotipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalProyectoTipos(String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM ProyectoTipo p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));			
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarProyectoTipo(ProyectoTipo proyectoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{

			proyectoTipo.setEstado(0);
			session.beginTransaction();
			session.update(proyectoTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", ProyectoTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
