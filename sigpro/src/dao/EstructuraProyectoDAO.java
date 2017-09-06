package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import utilities.CHibernateSession;
import utilities.CLogger;

public class EstructuraProyectoDAO {
	public static List<?> getEstructuraProyecto(Integer idProyecto){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		
		try{
			String query =String.join(" ",
				"select * from (",
				"select p.id, p.nombre, 1 objeto_tipo,  p.treePath, p.nivel, p.fecha_inicio,",
				"p.fecha_fin, p.duracion, p.duracion_dimension,p.costo",
				"from proyecto p",
				"where p.id= ?1 and p.estado=1",
				"union",
				"select c.id, c.nombre, 2 objeto_tipo,  c.treePath, c.nivel, c.fecha_inicio,",
				"c.fecha_fin , c.duracion, c.duracion_dimension,c.costo",
				"from componente c",
				"where c.proyectoid= ?1 and c.estado=1",
				"union",
				"select pr.id, pr.nombre, 3 objeto_tipo , pr.treePath, pr.nivel, pr.fecha_inicio,",
				"pr.fecha_fin, pr.duracion, pr.duracion_dimension,pr.costo",
				"from producto pr",
				"left outer join componente c on c.id=pr.componenteid",
				"left outer join proyecto p on p.id=c.proyectoid",
				"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1",
				"union",
				"select sp.id, sp.nombre, 4 objeto_tipo,  sp.treePath, sp.nivel, sp.fecha_inicio,",
				"sp.fecha_fin , sp.duracion, sp.duracion_dimension,sp.costo",
				"from subproducto sp",
				"left outer join producto pr on pr.id=sp.productoid",
				"left outer join componente c on c.id=pr.componenteid",
				"left outer join proyecto p on p.id=c.proyectoid",
				"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1 and sp.estado=1",
				"union",
				"select a.id, a.nombre, 5 objeto_tipo,  a.treePath, a.nivel, a.fecha_inicio,",
				"a.fecha_fin , a.duracion, a.duracion_dimension,a.costo",
				"from actividad a",
				"left outer join proyecto p on p.id=a.proyecto_base",
				"where p.id= ?1 and a.estado=1 and p.estado=1",
				") arbol",
				"where arbol.treePath is not null",
				"order by treePath");			
			
			Query<?> criteria = session.createNativeQuery(query);
			criteria.setParameter("1", idProyecto);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", EstructuraProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

}
