package dao;

import java.math.BigInteger;
import java.util.ArrayList;
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
				"p.fecha_fin, p.duracion, p.duracion_dimension,p.costo,0, p.acumulacion_costoid",
				"from proyecto p",
				"where p.id= ?1 and p.estado=1",
				"union",
				"select c.id, c.nombre, 2 objeto_tipo,  c.treePath, c.nivel, c.fecha_inicio,",
				"c.fecha_fin , c.duracion, c.duracion_dimension,c.costo,0,c.acumulacion_costoid",
				"from componente c",
				"where c.proyectoid=?1 and c.estado=1",
				"union",
				"select pr.id, pr.nombre, 3 objeto_tipo , pr.treePath, pr.nivel, pr.fecha_inicio,",
				"pr.fecha_fin, pr.duracion, pr.duracion_dimension,pr.costo,0,pr.acumulacion_costoid",
				"from producto pr",
				"left outer join componente c on c.id=pr.componenteid",
				"left outer join proyecto p on p.id=c.proyectoid",
				"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1",
				"union",
				"select sp.id, sp.nombre, 4 objeto_tipo,  sp.treePath, sp.nivel, sp.fecha_inicio,",
				"sp.fecha_fin , sp.duracion, sp.duracion_dimension,sp.costo,0,sp.acumulacion_costoid",
				"from subproducto sp",
				"left outer join producto pr on pr.id=sp.productoid",
				"left outer join componente c on c.id=pr.componenteid",
				"left outer join proyecto p on p.id=c.proyectoid",
				"where p.id= ?1 and p.estado=1 and c.estado=1 and pr.estado=1 and sp.estado=1",
				"union",
				"select a.id, a.nombre, 5 objeto_tipo,  a.treePath, a.nivel, a.fecha_inicio,",
				"a.fecha_fin , a.duracion, a.duracion_dimension,a.costo,a.pred_objeto_id,a.acumulacion_costo acumulacion_costoid",
				"from actividad a",
				"left outer join proyecto p on p.id=a.proyecto_base",
				"where p.id= ?1 and a.estado=1 and p.estado=1",
				") arbol",
				"order by treePath;");			
			
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
	
	public static Nodo getEstructuraProyectoArbol(int id){
		Nodo root = null;
		List<?> estructuras = EstructuraProyectoDAO.getEstructuraProyecto(id);
		if(estructuras.size()>0){
			Object[] dato = (Object[]) estructuras.get(0);
			root = new Nodo((Integer)dato[0],((BigInteger)dato[2]).intValue(),(String)dato[1],(Integer)dato[4],(String)dato[3], new ArrayList<Nodo>(), null);
			Nodo nivel_actual_estructura = root;
			for(int i=1; i<estructuras.size(); i++){
				dato = (Object[]) estructuras.get(i);
				Nodo nodo = new Nodo((Integer)dato[0],((BigInteger)dato[2]).intValue(),(String)dato[1],(Integer)dato[4],(String)dato[3], new ArrayList<Nodo>(), null);
				if(nodo.nivel!=nivel_actual_estructura.nivel+1){
					if(nodo.nivel>nivel_actual_estructura.nivel){
						nivel_actual_estructura = nivel_actual_estructura.children.get(nivel_actual_estructura.children.size()-1);
					}
					else{
						for(int j=0; j<=(nivel_actual_estructura.nivel-nodo.nivel+1); j++)
							nivel_actual_estructura=nivel_actual_estructura.parent;
					}
				}
				nodo.parent = nivel_actual_estructura;
				nivel_actual_estructura.children.add(nodo);
			}
		}
		return root;
	}

}
