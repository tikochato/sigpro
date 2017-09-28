package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import utilities.CHibernateSession;
import utilities.CLogger;

public class AdministracionTransaccionalDAO {
	public static List<?> obtenerTotalesPorUsuarios(Date fechaInicio, Date fechaFin){
		List<?> ret = new ArrayList<List<String>>();
		Session session = CHibernateSession.getSessionFactory().openSession();
        try{
        	String str_Query = String.join(" ","Select Q1.usuario, sum(Q1.creacion) creacion, sum(Q1.actualizacion) actualizacion, sum(Q1.eliminado) eliminado FROM (",
        			"select usuario, sum(creacion) creacion, actualizacion, eliminado from (",
        			"select usuario, count(p.id) creacion, 0 actualizacion, 0 eliminado from usuario u",
        			"left outer join proyecto p on (p.usuario_creo=u.usuario)",
        			"where p.fecha_creacion between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, count(c.id) creacion, 0 actualizacion, 0 eliminado from usuario u",
        			"left outer join componente c on (c.usuario_creo=u.usuario)",
        			"where c.fecha_creacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, count(pr.id) creacion, 0 actualizacion, 0 eliminado from usuario u",
        			"left outer join producto pr on (pr.usuario_creo=u.usuario)",
        			"where pr.fecha_creacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, count(sp.id) creacion, 0 actualizacion, 0 eliminado from usuario u",
        			"left outer join subproducto sp on (sp.usuario_creo=u.usuario)",
        			"where sp.fecha_creacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, count(a.id) creacion, 0 actualizacion, 0 eliminado from usuario u",
        			"left outer join actividad a on (a.usuario_creo=u.usuario)",
        			"where a.fecha_creacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario) t1 group by t1.usuario",
        			"UNION all",
        			"select usuario, creacion, sum(actualizacion) actualizacion, eliminado from (",
        			"select usuario, 0 creacion, count(p.id) actualizacion, 0 eliminado from usuario u",
        			"left join proyecto p on (p.usuario_actualizo=u.usuario)",
        			"where p.fecha_actualizacion between :fechaInicio and :fechaFin and p.estado=1",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, count(c.id) actualizacion, 0 eliminado from usuario u",
        			"left outer join componente c on (c.usuario_actualizo=u.usuario)",
        			"where c.fecha_actualizacion between :fechaInicio and :fechaFin and c.estado=1",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, count(pr.id) actualizacion, 0 eliminado from usuario u",
        			"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
        			"where pr.fecha_actualizacion between :fechaInicio and :fechaFin and pr.estado=1",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, count(sp.id) actualizacion, 0 eliminado from usuario u",
        			"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
        			"where sp.fecha_actualizacion between :fechaInicio and :fechaFin and sp.estado=1",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, count(a.id) actualizacion, 0 eliminado from usuario u",
        			"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
        			"where a.fecha_actualizacion  between :fechaInicio and :fechaFin and a.estado=1",
        			"group by u.usuario) t2 group by t2.usuario",
        			"UNION all",
        			"select usuario, creacion, actualizacion, sum(eliminados) eliminados from (",
        			"select usuario, 0 creacion, 0 actualizacion, count(p.id) eliminados from usuario u",
        			"left join proyecto p on (p.usuario_actualizo=u.usuario)",
        			"where p.estado=0 and p.fecha_actualizacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, 0 actualizacion, count(c.id) eliminados from usuario u",
        			"left outer join componente c on (c.usuario_actualizo=u.usuario)",
        			"where c.estado=0 and c.fecha_actualizacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, 0 actualizacion, count(pr.id) eliminados from usuario u",
        			"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
        			"where pr.estado=0 and pr.fecha_actualizacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, 0 actualizacion, count(sp.id) eliminados from usuario u",
        			"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
        			"where sp.estado=0 and sp.fecha_actualizacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			"union all",
        			"select usuario, 0 creacion, 0 actualizacion, count(a.id) eliminados from usuario u",
        			"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
        			"where a.estado=0 and a.fecha_actualizacion  between :fechaInicio and :fechaFin",
        			"group by u.usuario",
        			")t3 group by t3.usuario) Q1",
        			"group by Q1.usuario");
        	Query<?> criteria = session.createNativeQuery(str_Query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			ret = criteria.getResultList();
        }catch(Throwable e){
            CLogger.write("1", AdministracionTransaccionalDAO.class, e);
        }finally {
        	session.close();
		}
		return ret;
	}
	
	public static List<?> obtenerTransaccionesPorUsuario(String usuarioDetalle, Date fechaInicio, Date fechaFin){
		List<?> ret = new ArrayList<List<String>>();
		Session session = CHibernateSession.getSessionFactory().openSession();
        try{
        	String str_Query = String.join(" ","select usuario, p.nombre creacion, 'Prestamo', 'Creado', p.fecha_creacion fecha from usuario u",
        			"left outer join proyecto p on (p.usuario_creo=u.usuario)",
        			"where p.fecha_creacion between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, c.nombre creacion, 'Componente', 'Creado', c.fecha_creacion fecha from usuario u",
        			"left outer join componente c on (c.usuario_creo=u.usuario)",
        			"where c.fecha_creacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, pr.nombre creacion, 'Producto', 'Creado', pr.fecha_creacion fecha from usuario u",
        			"left outer join producto pr on (pr.usuario_creo=u.usuario)",
        			"where pr.fecha_creacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, sp.nombre creacion, 'Sub Producto', 'Creado', sp.fecha_creacion fecha from usuario u",
        			"left outer join subproducto sp on (sp.usuario_creo=u.usuario)",
        			"where sp.fecha_creacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, a.nombre creacion, 'Actividad', 'Creado', a.fecha_creacion fecha from usuario u",
        			"left outer join actividad a on (a.usuario_creo=u.usuario)",
        			"where a.fecha_creacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"UNION all",
        			"select usuario, p.nombre creacion, 'Prestamo', 'Modificado', p.fecha_actualizacion fecha from usuario u",
        			"left join proyecto p on (p.usuario_actualizo=u.usuario)",
        			"where p.fecha_actualizacion between :fechaInicio and :fechaFin and p.estado=1 and u.usuario=:usuario",
        			"union all",
        			"select usuario, c.nombre creacion, 'Componente', 'Modificado', c.fecha_actualizacion fecha from usuario u",
        			"left outer join componente c on (c.usuario_actualizo=u.usuario)",
        			"where c.fecha_actualizacion between :fechaInicio and :fechaFin and c.estado=1 and u.usuario=:usuario",
        			"union all",
        			"select usuario, pr.nombre creacion, 'Producto', 'Modificado', pr.fecha_actualizacion fecha from usuario u",
        			"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
        			"where pr.fecha_actualizacion between :fechaInicio and :fechaFin and pr.estado=1 and u.usuario=:usuario",
        			"union all",
        			"select usuario, sp.nombre creacion, 'Sub Producto', 'Modificado', sp.fecha_actualizacion fecha from usuario u",
        			"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
        			"where sp.fecha_actualizacion between :fechaInicio and :fechaFin and sp.estado=1 and u.usuario=:usuario",
        			"union all",
        			"select usuario, a.nombre creacion, 'Actividad', 'Modificado', a.fecha_actualizacion fecha from usuario u",
        			"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
        			"where a.fecha_actualizacion  between :fechaInicio and :fechaFin and a.estado=1 and u.usuario=:usuario",
        			"UNION all",
        			"select usuario, p.nombre creacion, 'Prestamo', 'Eliminado', p.fecha_actualizacion fecha from usuario u",
        			"left join proyecto p on (p.usuario_actualizo=u.usuario)",
        			"where p.estado=0 and p.fecha_actualizacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, c.nombre creacion, 'Componente', 'Eliminado', c.fecha_actualizacion fecha from usuario u",
        			"left outer join componente c on (c.usuario_actualizo=u.usuario)",
        			"where c.estado=0 and c.fecha_actualizacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, pr.nombre creacion, 'Producto', 'Eliminado', pr.fecha_actualizacion fecha from usuario u",
        			"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
        			"where pr.estado=0 and pr.fecha_actualizacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, sp.nombre creacion, 'Sub Producto', 'Eliminado', sp.fecha_actualizacion fecha from usuario u",
        			"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
        			"where sp.estado=0 and sp.fecha_actualizacion  between :fechaInicio and :fechaFin and u.usuario=:usuario",
        			"union all",
        			"select usuario, a.nombre creacion, 'Actividad', 'Eliminado', a.fecha_actualizacion fecha from usuario u",
        			"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
        			"where a.estado=0 and a.fecha_actualizacion  between :fechaInicio and :fechaFin and u.usuario=:usuario");
        	Query<?> criteria = session.createNativeQuery(str_Query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			criteria.setParameter("usuario", usuarioDetalle);
			ret = criteria.getResultList();
        }catch(Throwable e){
            CLogger.write("2", AdministracionTransaccionalDAO.class, e);
        }finally {
        	session.close();
		}
		return ret;
	}
	
	public static List<?> obtenerTransaccionesCreadas(Date fechaInicio, Date fechaFin){
		List<?> ret = new ArrayList<List<String>>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String str_Query = String.join(" ","select sum(creacion) creacion, month(fecha_creacion) mes, year(fecha_creacion) anio from (",
					"select count(p.id) creacion, p.fecha_creacion from usuario u",
					"left outer join proyecto p on (p.usuario_creo=u.usuario)",
					"where p.fecha_creacion between :fechaInicio and :fechaFin",
					"union all",
					"select count(c.id) creacion, c.fecha_creacion fecha  from usuario u",
					"left outer join componente c on (c.usuario_creo=u.usuario)",
					"where c.fecha_creacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(pr.id) creacion, pr.fecha_creacion fecha  from usuario u",
					"left outer join producto pr on (pr.usuario_creo=u.usuario)",
					"where pr.fecha_creacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(sp.id) creacion, sp.fecha_creacion fecha  from usuario u",
					"left outer join subproducto sp on (sp.usuario_creo=u.usuario)",
					"where sp.fecha_creacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(a.id) creacion, a.fecha_creacion fecha  from usuario u",
					"left outer join actividad a on (a.usuario_creo=u.usuario)",
					"where a.fecha_creacion  between :fechaInicio and :fechaFin",
					") t1", 
					"group by month(t1.fecha_creacion)",
					"order by anio, mes");
			Query<?> criteria = session.createNativeQuery(str_Query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			ret = criteria.getResultList();
		}catch(Throwable e){
            CLogger.write("3", AdministracionTransaccionalDAO.class, e);
        }finally {
        	session.close();
		}
		return ret;
	}
	
	public static List<?> obtenerTransaccionesActualizadas(Date fechaInicio, Date fechaFin){
		List<?> ret = new ArrayList<List<String>>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String str_Query = String.join(" ","select sum(actualizacion) actualizacion, month(fecha_actualizacion) mes, year(fecha_actualizacion) anio from (",
					"select count(p.id) actualizacion, p.fecha_actualizacion from usuario u",
					"left join proyecto p on (p.usuario_actualizo=u.usuario)",
					"where p.fecha_actualizacion between :fechaInicio and :fechaFin and p.estado=1",
					"union all",
					"select count(c.id) actualizacion, c.fecha_actualizacion from usuario u",
					"left outer join componente c on (c.usuario_actualizo=u.usuario)",
					"where c.fecha_actualizacion between :fechaInicio and :fechaFin and c.estado=1",
					"union all",
					"select count(pr.id) actualizacion, pr.fecha_actualizacion from usuario u",
					"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
					"where pr.fecha_actualizacion between :fechaInicio and :fechaFin and pr.estado=1",
					"union all",
					"select count(sp.id) actualizacion, sp.fecha_actualizacion from usuario u",
					"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
					"where sp.fecha_actualizacion between :fechaInicio and :fechaFin and sp.estado=1",
					"union all",
					"select count(a.id) actualizacion, a.fecha_actualizacion from usuario u",
					"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
					"where a.fecha_actualizacion  between :fechaInicio and :fechaFin and a.estado=1",
					") t1",
					"group by month(t1.fecha_actualizacion)",
					"order by anio, mes");
			Query<?> criteria = session.createNativeQuery(str_Query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			ret = criteria.getResultList();
		}catch(Throwable e){
            CLogger.write("4", AdministracionTransaccionalDAO.class, e);
        }finally {
        	session.close();
		}
		return ret;
	}
	
	public static List<?> obtenerTransaccionesEliminadas(Date fechaInicio, Date fechaFin){
		List<?> ret = new ArrayList<List<String>>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String str_Query = String.join(" ","select sum(eliminados) eliminados, month(fecha_actualizacion) mes, year(fecha_actualizacion) anio from (",
					"select count(p.id) eliminados, p.fecha_actualizacion from usuario u",
					"left join proyecto p on (p.usuario_actualizo=u.usuario)",
					"where p.estado=0 and p.fecha_actualizacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(c.id) eliminados, c.fecha_actualizacion from usuario u",
					"left outer join componente c on (c.usuario_actualizo=u.usuario)",
					"where c.estado=0 and c.fecha_actualizacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(pr.id) eliminados, pr.fecha_actualizacion from usuario u",
					"left outer join producto pr on (pr.usuario_actualizo=u.usuario)",
					"where pr.estado=0 and pr.fecha_actualizacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(sp.id) eliminados, sp.fecha_actualizacion from usuario u",
					"left outer join subproducto sp on (sp.usuario_actualizo=u.usuario)",
					"where sp.estado=0 and sp.fecha_actualizacion  between :fechaInicio and :fechaFin",
					"union all",
					"select count(a.id) eliminados, a.fecha_actualizacion from usuario u",
					"left outer join actividad a on (a.usuario_actualizo=u.usuario)",
					"where a.estado=0 and a.fecha_actualizacion  between :fechaInicio and :fechaFin",
					") t1",
					"group by month(t1.fecha_actualizacion)",
					"order by anio, mes");
			Query<?> criteria = session.createNativeQuery(str_Query);
			criteria.setParameter("fechaInicio", fechaInicio);
			criteria.setParameter("fechaFin", fechaFin);
			ret = criteria.getResultList();
		}catch(Throwable e){
            CLogger.write("5", AdministracionTransaccionalDAO.class, e);
        }finally {
        	session.close();
		}
		return ret;
	}
}
