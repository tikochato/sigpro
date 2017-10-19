package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.SubcomponenteTipo;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SubComponenteTipoDAO {

	public static List<SubcomponenteTipo> getSubComponenteTipos(){
		List<SubcomponenteTipo> ret = new ArrayList<SubcomponenteTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubcomponenteTipo> criteria = builder.createQuery(SubcomponenteTipo.class);
			Root<SubcomponenteTipo> root = criteria.from(SubcomponenteTipo.class);
			criteria.select( root ).where(builder.equal(root.get("estado"),1));
			ret = session.createQuery( criteria ).getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
		

	
	public static SubcomponenteTipo getSubComponenteTipoPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		SubcomponenteTipo ret = null;
		List<SubcomponenteTipo> listRet = null;
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();

			CriteriaQuery<SubcomponenteTipo> criteria = builder.createQuery(SubcomponenteTipo.class);
			Root<SubcomponenteTipo> root = criteria.from(SubcomponenteTipo.class);
			criteria.select( root );
			criteria.where( builder.and(builder.equal( root.get("id"), id ),builder.equal(root.get("estado"), 1)));
			listRet = session.createQuery( criteria ).getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("2", SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubComponenteTipo(SubcomponenteTipo subcomponenteTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(subcomponenteTipo);
			session.flush();
			//TODO: crear ctipo_propiedad
//			if (subcomponenteTipo.getCtipoPropiedads()!=null && subcomponenteTipo.getCtipoPropiedads().size()>0){
//				for (CtipoPropiedad propiedad : subcomponenteTipo.getCtipoPropiedads()){
//					session.saveOrUpdate(propiedad);	
//				}
//			}
			session.flush();
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	
	
	public static boolean eliminarSubComponenteTipo(SubcomponenteTipo subcomponenteTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			subcomponenteTipo.setEstado(0);
			session.beginTransaction();
			session.update(subcomponenteTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6",SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	
	public static boolean eliminarTotalSubComponenteTipo(SubcomponenteTipo subcomponenteTipo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(subcomponenteTipo);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List<SubcomponenteTipo> getSubComponenteTiposPagina(int pagina, int numerosubcomponentestipo 
			,String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<SubcomponenteTipo> ret = new ArrayList<SubcomponenteTipo>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT c FROM SubcomponenteTipo c WHERE c.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<SubcomponenteTipo> criteria = session.createQuery(query,SubcomponenteTipo.class);
			criteria.setFirstResult(((pagina-1)*(numerosubcomponentestipo)));
			criteria.setMaxResults(numerosubcomponentestipo);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalSubComponenteTipo(String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT count(c.id) FROM SubcomponenteTipo c WHERE c.estado=1 ";
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
		} catch(Throwable e){
			CLogger.write("7", SubComponenteTipoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
