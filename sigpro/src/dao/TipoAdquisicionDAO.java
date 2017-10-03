package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import pojo.TipoAdquisicion;
import utilities.CHibernateSession;
import utilities.CLogger;

public class TipoAdquisicionDAO {
	public static Long getTotalTipoAdquisicionDisponibles(String idTiposAdquisiciones){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Long> conteo = session.createQuery("select count(ta.id) from TipoAdquisicion ta  WHERE ta.estado = 1 "
					+ (idTiposAdquisiciones!=null && idTiposAdquisiciones.length()>0 ?  " and ta.id not in ("+ idTiposAdquisiciones + ")" : "") 
					,Long.class);
					
			ret = conteo.getSingleResult();
		}
		catch(NonUniqueResultException e){
			CLogger.write("1", TipoAdquisicionDAO.class, e);
		}
		catch(Throwable e){
			CLogger.write("1", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<TipoAdquisicion> getTipoAdquisicionPaginaTotalDisponibles(int pagina, int numeroTipoAdquisicion, String idTipoAdquisicion){
		List<TipoAdquisicion> ret = new ArrayList<TipoAdquisicion>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<TipoAdquisicion> criteria = session.createQuery("select ta from TipoAdquisicion ta  WHERE ta.estado = 1 "
					+ (idTipoAdquisicion!=null && idTipoAdquisicion.length()>0 ?  " and ta.id not in ("+ idTipoAdquisicion + ")" : "") 
					,TipoAdquisicion.class);
			criteria.setFirstResult(((pagina-1)*(numeroTipoAdquisicion)));
			criteria.setMaxResults(numeroTipoAdquisicion);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("2", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalTipoAdquisicion(String filtro_cooperante, String filtro_nombre,String filtro_usuario_creo, String filtro_fecha_creacion){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(ta.id) FROM TipoAdquisicion ta WHERE ta.estado=1 ";
			String query_a="";
			if(filtro_cooperante!= null && filtro_cooperante.trim().length()>0)
				query_a = String.join("", query_a, " ta.cooperante.nombre LIKE '%", filtro_cooperante,"%' ");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " ta.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " ta.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(ta.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> criteria = session.createQuery(query,Long.class);
			ret = criteria.getSingleResult();
		}
		catch(NonUniqueResultException e){
			CLogger.write("3", TipoAdquisicionDAO.class, e);
		}
		catch(Throwable e){
			CLogger.write("3", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<TipoAdquisicion> getTipoAdquisicionPorObjeto(int objetoId, int objetoTipo){
		List<TipoAdquisicion> ret = new ArrayList<TipoAdquisicion>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		int cooperanteCodigo=0;
		switch(objetoTipo){
			case 3: 
				Producto producto = ProductoDAO.getProductoPorId(objetoId);
				cooperanteCodigo = producto.getComponente().getProyecto().getCooperante().getCodigo();
				break;
			case 4: 
				Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId);
				cooperanteCodigo = subproducto.getProducto().getComponente().getProyecto().getCooperante().getCodigo();
				break;
			case 5: 
				Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
				if(actividad.getProyectoBase()!=null){
					Proyecto proyecto = ProyectoDAO.getProyecto(actividad.getProyectoBase());
					cooperanteCodigo = (proyecto!=null) ? proyecto.getCooperante().getCodigo() : 0;
				}
				else if(actividad.getComponenteBase()!=null){
					Componente componente = ComponenteDAO.getComponente(actividad.getComponenteBase());
					cooperanteCodigo = (componente!=null) ? componente.getProyecto().getCooperante().getCodigo() : 0;
				}
				else if(actividad.getProductoBase()!=null){
					Producto tproducto = ProductoDAO.getProductoPorId(actividad.getProductoBase());
					cooperanteCodigo = (tproducto!=null) ? tproducto.getComponente().getProyecto().getCooperante().getCodigo() : 0;
				}
				break;
		}
		try{
			String str_query = "SELECT ta from TipoAdquisicion ta where ta.cooperantecodigo=:codigo and ta.estado=1";
			Query<TipoAdquisicion> criteria = session.createQuery(str_query,TipoAdquisicion.class);
			criteria.setParameter("codigo", cooperanteCodigo);
			ret = criteria.getResultList();
		}

		catch(Throwable e){
			CLogger.write("4", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<TipoAdquisicion> getTipoAdquisicionPagina(int pagina, int numeroTipoAdquisicion, String filtro_cooperante, String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion){
		
		List<TipoAdquisicion> ret = new ArrayList<TipoAdquisicion>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			String query = "SELECT ta FROM TipoAdquisicion ta WHERE ta.estado = 1 ";
			String query_a="";
			if(filtro_cooperante!= null && filtro_cooperante.trim().length()>0)
				query_a = String.join("", query_a, " ta.cooperante.nombre LIKE '%", filtro_cooperante, "%' ");
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " ta.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " ta.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(ta.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<TipoAdquisicion> criteria = session.createQuery(query,TipoAdquisicion.class);
			criteria.setFirstResult(((pagina-1)*(numeroTipoAdquisicion)));
			criteria.setMaxResults(numeroTipoAdquisicion);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static TipoAdquisicion getTipoAdquisicionPorId(Integer tipoAdquisicionId){
		TipoAdquisicion ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<TipoAdquisicion> criteria = session.createQuery("FROM TipoAdquisicion ta where ta.id=:tipoAdquisicionId"
					,TipoAdquisicion.class);
			List<TipoAdquisicion> listRet = null;
			criteria.setParameter("tipoAdquisicionId", tipoAdquisicionId);
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		}
		catch(NoResultException e){
			CLogger.write("6", TipoAdquisicionDAO.class, e);
		}
		catch(Throwable e){
			CLogger.write("6", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarTipoAdquisicion(TipoAdquisicion tipoAdquisicion){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			session.beginTransaction();
			session.saveOrUpdate(tipoAdquisicion);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("7", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean borrarTipoAdquisicion(TipoAdquisicion tipoAdquisicion){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			session.beginTransaction();
			tipoAdquisicion.setEstado(0);
			session.saveOrUpdate(tipoAdquisicion);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("8", TipoAdquisicionDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
