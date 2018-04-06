package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Cooperante;
import utilities.CHibernateSession;
import utilities.CLogger;

public class CooperanteDAO {
	
	public static List<Cooperante> getCooperantes(){
		List<Cooperante> ret = new ArrayList<Cooperante>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Cooperante> criteria = builder.createQuery(Cooperante.class);
			Root<Cooperante> root = criteria.from(Cooperante.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Cooperante getCooperantePorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Cooperante ret = null;
		List<Cooperante> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<Cooperante> criteria = builder.createQuery(Cooperante.class);
			Root<Cooperante> root = criteria.from(Cooperante.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarCooperante(Cooperante cooperante){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(cooperante);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarCooperante(Cooperante cooperante){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			cooperante.setEstado(0);
			session.beginTransaction();
			session.update(cooperante);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalCooperante(Cooperante cooperante){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(cooperante);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Cooperante> getCooperantesPagina(int pagina, int numerocooperantes, String filtro_codigo,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion){
		List<Cooperante> ret = new ArrayList<Cooperante>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM Cooperante c WHERE estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_codigo!=null && filtro_codigo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(c.codigo) LIKE '%",filtro_codigo,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Cooperante> criteria = session.createQuery(query,Cooperante.class);
			criteria.setFirstResult(((pagina-1)*(numerocooperantes)));
			criteria.setMaxResults(numerocooperantes);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalCooperantes(String filtro_codigo,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM Cooperante c WHERE c.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_codigo!=null && filtro_codigo.trim().length()>0)
				query_a = String.join("",query_a, " str(c.codigo) LIKE '%",filtro_codigo,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		} catch(Throwable e){
			CLogger.write("7", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Cooperante getCooperantePorCodigo(int codigo){
		Cooperante ret=null;
		List<Cooperante> listRet = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM Cooperante c WHERE c.estado=1 and c.id.codigo= :codigo";
			
			Query<Cooperante> conteo = session.createQuery(query,Cooperante.class);
			conteo.setParameter("codigo", codigo);
			listRet = conteo.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("7", CooperanteDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
