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
				string_query = string_query + "select a.nombre from Proyecto p "
						+ "inner join Actividad a where a.objetoId=p.id "
						+ "and p.id=:idProyecto";
				
				Query<Object> query = session.createQuery(string_query,Object.class);
				query.setParameter("idProyecto", idProyecto);
				result = query.getResultList();
			} else if(idProyecto > 0 && idComponente > 0 && idProducto == 0 && idSubProducto == 0){
				
			} else if(idProyecto > 0 && idComponente > 0 && idProducto > 0 && idSubProducto == 0){
				
			} else if(idProyecto > 0 && idComponente > 0 && idProducto > 0 && idSubProducto > 0){
				
			}
			
			
			/*
			if (idComponente > 0)
				string_query = string_query + "inner join Componente c on p.id=c.proyecto.id inner join ca.actividad ca on c.id=ca.objeto_id ";
			if(idProducto > 0)
				string_query = string_query + "inner join Producto pd on c.id=pd.componente.id inner join Actividad a on pd.id=a.objeto_id ";
			if(idSubProducto > 0)
				string_query = string_query + "inner join Subproducto sp on pd.id=sp.producto.id inner join Actividad a on sp.id=a.objeto_id ";
			string_query = string_query + "where p.id=:idProyecto ";*/
			

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