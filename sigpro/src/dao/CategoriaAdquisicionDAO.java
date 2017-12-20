package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;

import pojo.CategoriaAdquisicion;
import utilities.CHibernateSession;
import utilities.CLogger;

public class CategoriaAdquisicionDAO {
	public static List<CategoriaAdquisicion> getCategoriaAdquisicion(){
		List<CategoriaAdquisicion> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<CategoriaAdquisicion> criteria = session.createQuery("FROM CategoriaAdquisicion ca where ca.estado = 1", CategoriaAdquisicion.class);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalCategoriaAdquisicion(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(ca.id) FROM CategoriaAdquisicion ca WHERE ca.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " ca.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " ca.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(ca.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		} catch(Throwable e){
			CLogger.write("2", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<CategoriaAdquisicion> getCategoriaAdquisicionPagina(int pagina, int numeroCategoriaAdquisicion, String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		List<CategoriaAdquisicion> ret = new ArrayList<CategoriaAdquisicion>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT ca FROM CategoriaAdquisicion ca WHERE ca.estado = 1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " ca.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " ca.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(ca.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));

			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query," ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<CategoriaAdquisicion> criteria = session.createQuery(query,CategoriaAdquisicion.class);
			criteria.setFirstResult(((pagina-1)*(numeroCategoriaAdquisicion)));
			criteria.setMaxResults(numeroCategoriaAdquisicion);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static CategoriaAdquisicion getCategoriaPorId(int id){
		Session session = CHibernateSession.getSessionFactory().openSession();
		CategoriaAdquisicion ret = null;
		List<CategoriaAdquisicion> listRet = null;
		
		try{
			String query = "FROM CategoriaAdquisicion where id=:id";
			Query<CategoriaAdquisicion> criteria = session.createQuery(query, CategoriaAdquisicion.class);
			criteria.setParameter("id", id);
			
			listRet = criteria.getResultList();
			
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("4", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarCategoria(CategoriaAdquisicion Categoria){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(Categoria);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarCategoria(CategoriaAdquisicion Categoria){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Categoria.setEstado(0);
			session.beginTransaction();
			session.update(Categoria);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("6", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<CategoriaAdquisicion> getCategoriaAdquisicionLB(String lineaBase){
		List<CategoriaAdquisicion> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT ca.* FROM sipro_history.categoria_adquisicion ca", 
					"where ca.estado = 1",
					lineaBase != null ? "and ca.linea_base like '%" + lineaBase + "%'" : "and ca.actual=1");
			Query<CategoriaAdquisicion> criteria = session.createNativeQuery(query, CategoriaAdquisicion.class);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("1", CategoriaAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
