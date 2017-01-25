package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ObjetoRecurso;
import pojo.Recurso;
import utilities.CHibernateSession;
import utilities.CLogger;

public class RecursoDAO {
	
	public static List<Recurso> getRecursos(){
		List<Recurso> ret = new ArrayList<Recurso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Recurso> criteria = builder.createQuery(Recurso.class);
			Root<Recurso> root = criteria.from(Recurso.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Recurso getRecursoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Recurso ret = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Recurso> criteria = builder.createQuery(Recurso.class);
			Root<Recurso> root = criteria.from(Recurso.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			ret = session.createQuery( criteria ).getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarRecurso(Recurso Recurso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(Recurso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarRecurso(Recurso Recurso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Recurso.setEstado(0);
			session.beginTransaction();
			session.update(Recurso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalRecurso(Recurso Recurso){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Recurso);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Recurso> getRecursosPagina(int pagina, int numeroRecursos, String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<Recurso> ret = new ArrayList<Recurso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM Recurso c WHERE c.estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Recurso> criteria = session.createQuery(query,Recurso.class);
			criteria.setFirstResult(((pagina-1)*(numeroRecursos)));
			criteria.setMaxResults(numeroRecursos);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRecursos(){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id) FROM Recurso c WHERE c.estado=1",Long.class);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Recurso> getRecursosPaginaPorObjeto(int pagina, int numeroRecursos, int objetoId, int objetoTipo){
		List<ObjetoRecurso> objetos_recurso = new ArrayList<ObjetoRecurso>();
		List<Recurso> ret=new ArrayList<Recurso>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ObjetoRecurso> criteria = session.createQuery("SELECT c FROM ObjetoRecurso c WHERE estado = 1 AND c.id.objetoid= :objetoId AND c.id.tipoObjeto= :tipoObjeto", ObjetoRecurso.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("tipoObjeto", objetoTipo);
			criteria.setFirstResult(((pagina-1)*(numeroRecursos)));
			criteria.setMaxResults(numeroRecursos);
			objetos_recurso = criteria.getResultList();
			for(ObjetoRecurso objeto_recurso : objetos_recurso){
				ret.add(RecursoDAO.getRecursoPorId(objeto_recurso.getId().getRecursoid()));
			}
		}
		catch(Throwable e){
			CLogger.write("8", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalRecursosPorObjeto(int objetoId, int objetoTipo){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("SELECT count(c.id.objetoid) FROM ObjetoRecurso c WHERE c.estado=1 AND c.id.objetoid= :objetoId AND c.id.tipoObjeto= :tipoObjeto",Long.class);
			conteo.setParameter("objetoId", objetoId);
			conteo.setParameter("tipoObjeto", objetoTipo);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", RecursoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
