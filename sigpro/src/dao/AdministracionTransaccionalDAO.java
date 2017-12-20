package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import utilities.CHibernateSession;
import utilities.CLogger;

public class AdministracionTransaccionalDAO {
	public static Integer obtenerCreadosProyectoUsuario(Date fechaInicio, Date fechaFin, Integer proyectoId, String usuario){
		Integer ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT count(p) FROM Proyecto p where p.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and p.id=:proyectoId and p.usuarioCreo=:usuario");
			
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret = criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(c) FROM Componente c where c.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and c.proyecto.id=:proyectoId and c.usuarioCreo=:usuario");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(pr) FROM Producto pr where pr.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and pr.componente.proyecto.id=:proyectoId and pr.usuarioCreo=:usuario");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(sp) FROM Subproducto sp where sp.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and sp.producto.componente.proyecto.id=:proyectoId and sp.usuarioCreo=:usuario");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(a) FROM Actividad a where a.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and a.treePath like '"+(10000000+proyectoId)+"%' and a.usuarioCreo=:usuario");
			
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
		}catch(Exception e){
			CLogger.write("1", AdministracionTransaccionalDAO.class, e);
		}
		
		return ret;
	}
	
	public static Integer obtenerActualizadosProyectoUsuario(Date fechaInicio, Date fechaFin, Integer proyectoId, String usuario){
		Integer ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT count(p) FROM Proyecto p where p.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and p.id=:proyectoId and p.usuarioActualizo=:usuario and p.estado=1");
			
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret = criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(c) FROM Componente c where c.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and c.proyecto.id=:proyectoId and c.usuarioActualizo=:usuario and c.estado=1");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(pr) FROM Producto pr where pr.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and pr.componente.proyecto.id=:proyectoId and pr.usuarioActualizo=:usuario and pr.estado=1");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(sp) FROM Subproducto sp where sp.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and sp.producto.componente.proyecto.id=:proyectoId and sp.usuarioActualizo=:usuario and sp.estado=1");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(a) FROM Actividad a where a.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and a.treePath like '"+(10000000+proyectoId)+"%' and a.usuarioActualizo=:usuario and a.estado=1");
			
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
		}catch(Exception e){
			CLogger.write("2", AdministracionTransaccionalDAO.class, e);
		}
		
		return ret;
	}
	
	public static Integer obtenerEliminadosProyectoUsuario(Date fechaInicio, Date fechaFin, Integer proyectoId, String usuario){
		Integer ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT count(p) FROM Proyecto p where p.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and p.id=:proyectoId and p.usuarioActualizo=:usuario and p.estado=0");
			
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret = criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(c) FROM Componente c where c.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and c.proyecto.id=:proyectoId and c.usuarioActualizo=:usuario and c.estado=0");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(pr) FROM Producto pr where pr.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and pr.componente.proyecto.id=:proyectoId and pr.usuarioActualizo=:usuario and pr.estado=0");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(sp) FROM Subproducto sp where sp.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and sp.producto.componente.proyecto.id=:proyectoId and sp.usuarioActualizo=:usuario and sp.estado=0");
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
			
			query = String.join(" ", "SELECT count(a) FROM Actividad a where a.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and a.treePath like '"+(10000000+proyectoId)+"%' and a.usuarioActualizo=:usuario and a.estado=0");
			
			criteria = session.createQuery(query,Long.class);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("usuario", usuario);
			
			ret += criteria.getSingleResult().intValue();
		}catch(Exception e){
			CLogger.write("3", AdministracionTransaccionalDAO.class, e);
		}
		
		return ret;
	}
	
	public static List<?> obtenerTransaccionesCreadosProyectoUsuario(Date fechaInicio, Date fechaFin, Integer proyectoId, String usuario){
		List<Object> ret = new ArrayList<Object>();
		List<?> ret2 = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT month(p.fechaCreacion), year(p.fechaCreacion), p.nombre, 1, p.usuarioCreo, 'Prestamo', 'Creado', p.fechaCreacion FROM Proyecto p where p.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and p.id=:proyectoId",  usuario != null ? " and p.usuarioCreo=:usuario" : "");
			
			Query<?> criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(c.fechaCreacion), year(c.fechaCreacion), c.nombre, 2, c.usuarioCreo, 'Componente', 'Creado', c.fechaCreacion FROM Componente c where c.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and c.proyecto.id=:proyectoId", usuario != null ? "and c.usuarioCreo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(pr.fechaCreacion), year(pr.fechaCreacion), pr.nombre, 3, pr.usuarioCreo , 'Producto', 'Creado', pr.fechaCreacion FROM Producto pr where pr.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and pr.componente.proyecto.id=:proyectoId", usuario != null ? "and pr.usuarioCreo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(sp.fechaCreacion), year(sp.fechaCreacion), sp.nombre, 4, sp.usuarioCreo, 'Sub Producto', 'Creado', sp.fechaCreacion FROM Subproducto sp where sp.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and sp.producto.componente.proyecto.id=:proyectoId", usuario != null ? "and sp.usuarioCreo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(a.fechaCreacion), year(a.fechaCreacion), a.nombre, 5, a.usuarioCreo, 'Actividad', 'Creado', a.fechaCreacion FROM Actividad a where a.fechaCreacion Between :fechaInicio AND :fechaFin",
					"and a.treePath like '"+(10000000+proyectoId)+"%'", usuario != null ? "and a.usuarioCreo=:usuario" : "");
			
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
		}catch(Exception e){
			CLogger.write("4", AdministracionTransaccionalDAO.class, e);
		}
		
		return ret;
	}
	
	public static List<?> obtenerTransaccionesActualizadosProyectoUsuario(Date fechaInicio, Date fechaFin, Integer proyectoId, String usuario){
		List<Object> ret = new ArrayList<Object>();
		List<?> ret2 = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT month(p.fechaActualizacion), year(p.fechaActualizacion), p.nombre, 1, p.usuarioActualizo, 'Prestamo', 'Actualizado', p.fechaActualizacion FROM Proyecto p where p.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and p.id=:proyectoId and p.estado=1", usuario != null ? "and p.usuarioActualizo=:usuario" : "");
			
			Query<?> criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(c.fechaActualizacion), year(c.fechaActualizacion), c.nombre, 2, c.usuarioActualizo, 'Componente', 'Actualizado', c.fechaActualizacion FROM Componente c where c.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and c.proyecto.id=:proyectoId and c.estado=1", usuario != null ? "and c.usuarioActualizo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(pr.fechaActualizacion), year(pr.fechaActualizacion), pr.nombre, 3, pr.usuarioActualizo , 'Producto', 'Actualizado', pr.fechaActualizacion FROM Producto pr where pr.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and pr.componente.proyecto.id=:proyectoId and pr.estado=1", usuario != null ? "and pr.usuarioActualizo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(sp.fechaActualizacion), year(sp.fechaActualizacion), sp.nombre, 4, sp.usuarioActualizo, 'Sub Producto', 'Actualizado', sp.fechaActualizacion FROM Subproducto sp where sp.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and sp.producto.componente.proyecto.id=:proyectoId and sp.estado=1", usuario != null ? "and sp.usuarioActualizo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(a.fechaActualizacion), year(a.fechaActualizacion), a.nombre, 5, a.usuarioActualizo, 'Actividad', 'Actualizado', a.fechaActualizacion FROM Actividad a where a.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and a.treePath like '"+(10000000+proyectoId)+"%' and a.estado=1", usuario != null ? "and a.usuarioActualizo=:usuario" : "");
			
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
		}catch(Exception e){
			CLogger.write("5", AdministracionTransaccionalDAO.class, e);
		}
		
		return ret;
	}
	
	public static List<?> obtenerTransaccionesEliminadasProyectoUsuario(Date fechaInicio, Date fechaFin, Integer proyectoId, String usuario){
		List<Object> ret = new ArrayList<Object>();
		List<?> ret2 = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "SELECT month(p.fechaActualizacion), year(p.fechaActualizacion), p.nombre, 1, p.usuarioActualizo, 'Prestamo', 'Eliminadas', p.fechaActualizacion FROM Proyecto p where p.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and p.id=:proyectoId and p.estado=0", usuario != null ? "and p.usuarioActualizo=:usuario" : "");
			
			Query<?> criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(c.fechaActualizacion), year(c.fechaActualizacion), c.nombre, 2, c.usuarioActualizo, 'Componente', 'Eliminadas', c.fechaActualizacion FROM Componente c where c.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and c.proyecto.id=:proyectoId and c.estado=0", usuario != null ? "and c.usuarioActualizo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(pr.fechaActualizacion), year(pr.fechaActualizacion), pr.nombre, 3, pr.usuarioActualizo , 'Producto', 'Eliminadas', pr.fechaActualizacion FROM Producto pr where pr.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and pr.componente.proyecto.id=:proyectoId and pr.estado=0", usuario != null ? "and pr.usuarioActualizo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(sp.fechaActualizacion), year(sp.fechaActualizacion), sp.nombre, 4, sp.usuarioActualizo, 'Sub Producto', 'Eliminadas', sp.fechaActualizacion FROM Subproducto sp where sp.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and sp.producto.componente.proyecto.id=:proyectoId and sp.estado=0", usuario != null ? "and sp.usuarioActualizo=:usuario" : "");
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("proyectoId", proyectoId);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
			
			query = String.join(" ", "SELECT month(a.fechaActualizacion), year(a.fechaActualizacion), a.nombre, 5, a.usuarioActualizo, 'Eliminadas', 'Actualizado', a.fechaActualizacion FROM Actividad a where a.fechaActualizacion Between :fechaInicio AND :fechaFin",
					"and a.treePath like '"+(10000000+proyectoId)+"%' and a.estado=0", usuario != null ? "and a.usuarioActualizo=:usuario" : "");
			
			criteria = session.createQuery(query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			if(usuario != null)
				criteria.setParameter("usuario", usuario);
			
			ret2 = criteria.getResultList();
			ret.addAll(ret2);
		}catch(Exception e){
			CLogger.write("6", AdministracionTransaccionalDAO.class, e);
		}
		
		return ret;
	}
}
