package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.PrestamoTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PrestamoTipoDAO {
	public static List<PrestamoTipo> getPrestamoTipos(){
		List<PrestamoTipo> ret = new ArrayList<PrestamoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<PrestamoTipo> criteria = builder.createQuery(PrestamoTipo.class);
			Root<PrestamoTipo> root = criteria.from(PrestamoTipo.class);
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
	
	public static PrestamoTipo getPrestamoTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		PrestamoTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<PrestamoTipo> criteria = builder.createQuery(PrestamoTipo.class);
			Root<PrestamoTipo> root = criteria.from(PrestamoTipo.class);
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
	
	public static boolean guardarPrestamoTipo(PrestamoTipo prestamotipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(prestamotipo);
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
	
	public static List<PrestamoTipo> getPrestamosTipoPagina(int pagina, int numeroproyectotipos,
			String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<PrestamoTipo> ret = new ArrayList<PrestamoTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT p FROM PrestamoTipo p WHERE estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<PrestamoTipo> criteria = session.createQuery(query,PrestamoTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroproyectotipos)));
			criteria.setMaxResults(numeroproyectotipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", PrestamoTipo.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalPrestamosTipos(String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM PrestamoTipo p WHERE p.estado=1 ";
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
	
	public static boolean eliminarPrestamoTipo(PrestamoTipo prestamoTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			prestamoTipo.setEstado(0);
			session.beginTransaction();
			session.update(prestamoTipo);
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
