package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;

import pojo.Actividad;
import pojo.InformePresupuesto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ReporteDAO {
	
	public static List<?> getActividadesCargaTrabajo(int idProyecto, int idComponente, int idProducto, int idSubProducto){
		List<?> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String string_query = "";
			if(idProyecto > 0 && idComponente == 0 && idProducto == 0 && idSubProducto ==0){
				string_query = String.join(" ", "select a.id, a.nombre as nombreActividad, a.porcentaje_avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Actividad a on a.objeto_id=p.id "
						, "inner join objeto_responsable_rol orr on orr.objeto_id = a.id and orr.objeto_tipo=5 "
						, "inner join responsable_rol rr on orr.id=rr.id "
						, "where p.id=:idProyecto and a.objeto_tipo=1 and p.estado=1 "
						, "union "
						, "select a.id, a.nombre as nombreActividad, a.porcentaje_avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Actividad a on a.objeto_id=c.id "
						, "inner join objeto_responsable_rol orr on orr.objeto_id = a.id and orr.objeto_tipo=5 "
						, "inner join responsable_rol rr on orr.id=rr.id "
						, "where p.id=:idProyecto and a.objeto_tipo=2 and p.estado=1 and c.estado=1"
						, "union "
						, "select a.id, a.nombre as nombreActividad, a.porcentaje_avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Producto pd on c.id=pd.componenteid "
						, "inner join Actividad a on a.objeto_id=pd.id "
						, "inner join objeto_responsable_rol orr on orr.objeto_id = a.id and orr.objeto_tipo=5 "
						, "inner join responsable_rol rr on orr.responsable_rolid=rr.id "
						, "where p.id=:idProyecto and a.objeto_tipo=3 and p.estado=1 and c.estado=1 and p.estado=1"
						, "union "
						, "select a.id, a.nombre as nombreActividad, a.porcentaje_avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Producto pd on c.id=pd.componenteid "
						, "inner join Subproducto sp on pd.id=sp.productoid "
						, "inner join Actividad a on a.objeto_id=sp.id "
						, "inner join objeto_responsable_rol orr on orr.objeto_id = a.id and orr.objeto_tipo=5 "
						, "inner join responsable_rol rr on orr.responsable_rolid=rr.id "
						, "where p.id=:idProyecto and a.objeto_tipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1");
				Query<?> query = session.createNativeQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				result = query.getResultList();				
			} else if(idProyecto > 0 && idComponente > 0 && idProducto == 0 && idSubProducto == 0){
				string_query =  String.join(" ", "select a.id, a.nombre as nombreActividad, a.porcentaje_Avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Actividad a on a.objeto_id=c.id "
						, "inner join objeto_responsable_rol orr on orr.objeto_id = a.id and orr.objeto_tipo=5 "
						, "inner join responsable_rol rr on orr.responsable_rolid=rr.id "
						, "where p.id=:idProyecto and c.id=:idComponente and a.objeto_tipo=2 and p.estado=1 and c.estado=1"
						, "union"
						, "select a.id, a.nombre as nombreActividad, a.porcentaje_avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Producto pd on c.id=pd.componenteid "
						, "inner join Actividad a on a.objeto_id=pd.id "
						, "inner join objeto_Responsable_Rol orr on orr.objeto_Id = a.id and orr.objeto_Tipo=5 "
						, "inner join Responsable_Rol rr on orr.responsable_Rolid=rr.id "
						, "where p.id=:idProyecto and c.id=:idComponente and a.objeto_Tipo=3 and p.estado=1 and c.estado=1 and p.estado=1"
						, "union"
						, "select a.id, a.nombre as nombreActividad, a.porcentaje_Avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Producto pd on c.id=pd.componenteid "
						, "inner join Subproducto sp on pd.id=sp.productoid "
						, "inner join Actividad a on a.objeto_Id=sp.id "
						, "inner join Objeto_Responsable_Rol orr on orr.objeto_Id = a.id and orr.objeto_Tipo=5 "
						, "inner join Responsable_Rol rr on orr.responsable_Rolid=rr.id "
						, "where p.id=:idProyecto and c.id=:idComponente and a.objeto_Tipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1");
				
				Query<?> query = session.createNativeQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				result = query.getResultList();
			} else if(idProyecto > 0 && idComponente > 0 && idProducto > 0 && idSubProducto == 0){
				string_query =  String.join(" ", "select a.id, a.nombre as nombreActividad, a.porcentaje_Avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Producto pd on c.id=pd.componenteid "
						, "inner join Actividad a on a.objeto_Id=pd.id "
						, "inner join Objeto_Responsable_Rol orr on orr.objeto_Id = a.id and orr.objeto_Tipo=5 "
						, "inner join Responsable_Rol rr on orr.responsable_Rolid=rr.id "
						, "where p.id=:idProyecto and c.id=:idComponente and p.id=:idProducto and a.objeto_Tipo=3 and p.estado=1 and c.estado=1 and p.estado=1"
						, "union"
						, "select a.id, a.nombre as nombreActividad, a.porcentaje_Avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						, "inner join Componente c on p.id=c.proyectoid "
						, "inner join Producto pd on c.id=pd.componenteid "
						, "inner join Subproducto sp on pd.id=sp.productoid "
						, "inner join Actividad a on a.objeto_Id=sp.id "
						, "inner join Objeto_Responsable_Rol orr on orr.objeto_Id = a.id and orr.objeto_Tipo=5 "
						, "inner join Responsable_Rol rr on orr.responsable_Rolid=rr.id "
						, "where p.id=:idProyecto and c.id=:idComponente and p.id=:idProducto and a.objeto_Tipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1");
				
				Query<?> query = session.createNativeQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				query.setParameter("idProducto", idProducto);
				result = query.getResultList();
			} else if(idProyecto > 0 && idComponente > 0 && idProducto > 0 && idSubProducto > 0){
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentaje_Avance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyectoid "
						+ "inner join Producto pd on c.id=pd.componenteid "
						+ "inner join Subproducto sp on pd.id=sp.productoid "
						+ "inner join Actividad a on a.objeto_Id=sp.id "
						+ "inner join Objeto_Responsable_Rol orr on orr.objeto_Id = a.id and orr.objeto_Tipo=5 "
						+ "inner join Responsable_Rol rr on orr.responsable_Rolid=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and p.id=:idProducto and sp.id=:idSubProducto and a.objeto_Tipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1";
				
				Query<?> query = session.createNativeQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				query.setParameter("idProducto", idProducto);
				query.setParameter("idSubProducto", idSubProducto);
				result = query.getResultList();
			}
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	public static List<Actividad> getActividadesResponsable(int responsableId, String usuario){
		List<Actividad> result = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			List<?> actividadesIds = null;
			String string_query = "";
			string_query = "select orr.objetoId from ObjetoResponsableRol orr where orr.responsableRol.id=:responsableId";
			Query<Actividad> query = session.createQuery(string_query, Actividad.class);
			query.setParameter("responsableId", responsableId);
			actividadesIds = query.getResultList();
			
			for(Object id : actividadesIds){
				Actividad actividad = ActividadDAO.getActividadPorId((Integer)id, usuario);
				result.add(actividad);
			}
		}
		catch(Throwable e){
			CLogger.write("2", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		return result;
	}
	
	public static List<Object> getAdquisiciones(int anio, int idPrestamo){
		List<Object> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
		}
		catch(Throwable e){
			CLogger.write("2", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	public static List<InformePresupuesto> existeInformeBase(int idPrestamo, int tipoInforme){
		List<InformePresupuesto> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<InformePresupuesto> criteria = session.createQuery("FROM InformePresupuesto ip where ip.idPrestamo=:idPrestamo and ip.estadoInforme.id=:tipoInforme", InformePresupuesto.class);
			criteria.setParameter("idPrestamo", idPrestamo);
			criteria.setParameter("tipoInforme", tipoInforme);
			result = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		return result;
	}
	
	public static boolean agregarRowInformePresupuesto(InformePresupuesto informe){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(informe);
			session.getTransaction().commit();
		}
		catch(Throwable e){
			CLogger.write("4", ReporteDAO.class, e);
			return false;
		}
		finally{
			session.close();
		}
		return true;
	}
	
	public static List<InformePresupuesto> getrowInformebyId(int idrow){
		List<InformePresupuesto> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<InformePresupuesto> criteria = session.createQuery("FROM InformePresupuesto ip where ip.id=:idrow", InformePresupuesto.class);
			criteria.setParameter("idrow", idrow);
			result = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("4", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		return result;
	}
	
	public static List<?> getPresupuestoProyecto(Integer fuente, Integer organismo, Integer correlativo, Integer ejercicio){
		List<?> result = new ArrayList<Object>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<?> query = session.createNativeQuery("select mes1r, mes2r, mes3r, mes4r, mes5r, mes6r, mes7r, mes8r, mes9r, mes10r, mes11r, mes12r from mv_ep_prestamo where fuente=:fuente and organismo=:organismo and correlativo=:correlativo and ejercicio=:ejercicio")
					.setParameter("fuente", fuente)
					.setParameter("organismo", organismo)
					.setParameter("correlativo", correlativo)
					.setParameter("ejercicio", ejercicio);
					result = query.getResultList();
		}
		catch(Throwable e){
			CLogger.write("5", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	public static List<?> getPresupuestoPorObjeto(Integer fuente, Integer organismo, Integer correlativo, Integer ejercicio, Integer programa, Integer subprograma, Integer proyecto, Integer actividad, Integer obra){
		List<?> result = new ArrayList<Object>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			if(programa != null && programa >= 0){
				Query<?> query = session.createNativeQuery("select mes1r, mes2r, mes3r, mes4r, mes5r, mes6r, mes7r, mes8r, mes9r, mes10r, mes11r, mes12r from mv_ep_estructura where fuente=:fuente and organismo=:organismo and correlativo=:correlativo and ejercicio=:ejercicio and programa=:programa and subprograma=:subprograma and proyecto=:proyecto and actividad=:actividad and obra=:obra")
						.setParameter("fuente", fuente)
						.setParameter("organismo", organismo)
						.setParameter("correlativo", correlativo)
						.setParameter("ejercicio", ejercicio)
						.setParameter("programa", programa)
						.setParameter("subprograma", subprograma)
						.setParameter("proyecto", proyecto)
						.setParameter("actividad", actividad)
						.setParameter("obra", obra);
						result = query.getResultList();
			}
		}
		catch(Throwable e){
			CLogger.write("6", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
}