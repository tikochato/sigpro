package dao;

import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import pojo.InformePresupuesto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ReporteDAO {
	public static List<Object> getCargaTrabajo(int atrasados, int objetoTipo, int idPrestamo, int idComponente, int idProducto, int idSubProducto){
		List<Object> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			
			Query query = session.createSQLQuery("CALL carga_trabajo(:atrasados, :objetoTipo, :idPrestamo, :idComponente, :idProducto, :idSubproducto, :fecha)")
			.setParameter("atrasados", atrasados)
			.setParameter("objetoTipo", objetoTipo)
			.setParameter("idPrestamo", idPrestamo)
			.setParameter("idComponente", idComponente)
			.setParameter("idProducto", idProducto)
			.setParameter("idSubproducto", idSubProducto)
			.setParameter("fecha", new DateTime().toDate());
			result = query.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ReporteDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return result;
	}
	
	public static List<Object> getActividadesCargaTrabajo(int idProyecto, int idComponente, int idProducto, int idSubProducto){
		List<Object> result = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String string_query = "";
			if(idProyecto > 0 && idComponente == 0 && idProducto == 0 && idSubProducto ==0){
				string_query = "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Actividad a on a.objetoId=p.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and a.objetoTipo=1 and p.estado=1 ";
				
				Query query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				result = query.getResultList();
				
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Actividad a on a.objetoId=c.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and a.objetoTipo=2 and p.estado=1 and c.estado=1";
				
				query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				result.addAll(query.getResultList());
				
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Actividad a on a.objetoId=pd.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and a.objetoTipo=3 and p.estado=1 and c.estado=1 and p.estado=1";
				
				query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				result.addAll(query.getResultList());
				
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Subproducto sp on pd.id=sp.producto.id "
						+ "inner join Actividad a on a.objetoId=sp.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and a.objetoTipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1";
				
				query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				result.addAll(query.getResultList());
			} else if(idProyecto > 0 && idComponente > 0 && idProducto == 0 && idSubProducto == 0){
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Actividad a on a.objetoId=c.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and a.objetoTipo=2 and p.estado=1 and c.estado=1";
				
				Query query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				result = query.getResultList();
				
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Actividad a on a.objetoId=pd.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and a.objetoTipo=3 and p.estado=1 and c.estado=1 and p.estado=1";
				
				query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				result.addAll(query.getResultList());
				
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Subproducto sp on pd.id=sp.producto.id "
						+ "inner join Actividad a on a.objetoId=sp.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and a.objetoTipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1";
				
				query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				result.addAll(query.getResultList());
			} else if(idProyecto > 0 && idComponente > 0 && idProducto > 0 && idSubProducto == 0){
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Actividad a on a.objetoId=pd.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and p.id=:idProducto and a.objetoTipo=3 and p.estado=1 and c.estado=1 and p.estado=1";
				
				Query query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				query.setParameter("idProducto", idProducto);
				result = query.getResultList();
				
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Subproducto sp on pd.id=sp.producto.id "
						+ "inner join Actividad a on a.objetoId=sp.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and p.id=:idProducto and a.objetoTipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1";
				
				query = session.createQuery(string_query);
				query.setParameter("idProyecto", idProyecto);
				query.setParameter("idComponente", idComponente);
				query.setParameter("idProducto", idProducto);
				result.addAll(query.getResultList());
			} else if(idProyecto > 0 && idComponente > 0 && idProducto > 0 && idSubProducto > 0){
				string_query =  "select a.id, a.nombre as nombreActividad, a.porcentajeAvance, rr.id as idResponsable, rr.nombre as responsable from Proyecto p "
						+ "inner join Componente c on p.id=c.proyecto.id "
						+ "inner join Producto pd on c.id=pd.componente.id "
						+ "inner join Subproducto sp on pd.id=sp.producto.id "
						+ "inner join Actividad a on a.objetoId=sp.id "
						+ "inner join ObjetoResponsableRol orr on orr.objetoId = a.id and orr.objetoTipo=5 "
						+ "inner join ResponsableRol rr on orr.responsableRol.id=rr.id "
						+ "where p.id=:idProyecto and c.id=:idComponente and p.id=:idProducto and sp.id=:idSubProducto and a.objetoTipo=4 and p.estado=1 and c.estado=1 and p.estado=1 and sp.estado=1";
				
				Query query = session.createQuery(string_query);
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
}