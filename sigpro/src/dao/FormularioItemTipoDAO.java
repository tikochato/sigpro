package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.FormularioItemTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class FormularioItemTipoDAO {
	public static Long getTotalFormularioItemTipo(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(f.id) FROM FormularioItemTipo f WHERE f.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("1", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static FormularioItemTipo getFormularioItemTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		FormularioItemTipo ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<FormularioItemTipo> criteria = builder.createQuery(FormularioItemTipo.class);
			Root<FormularioItemTipo> root = criteria.from(FormularioItemTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarFormularioItemTipo(FormularioItemTipo formularioItemTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(formularioItemTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarFormularioItemTipo(FormularioItemTipo formularioItemTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			formularioItemTipo.setEstado(0);
			session.beginTransaction();
			session.update(formularioItemTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<FormularioItemTipo> geFormularioItemTiposPagina(int pagina, int numeroFormularioItemTipos,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<FormularioItemTipo> ret = new ArrayList<FormularioItemTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query ="SELECT f FROM FormularioItemTipo f WHERE f.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " f.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " f.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(f.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<FormularioItemTipo> criteria = session.createQuery(query,FormularioItemTipo.class);
			criteria.setFirstResult(((pagina-1)*(numeroFormularioItemTipos)));
			criteria.setMaxResults(numeroFormularioItemTipos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", FormularioItemTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
