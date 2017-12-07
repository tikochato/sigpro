package utilities;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class CHistoria {

	
	public CHistoria() {
		
	}
	
	public static List<?> getVersiones(String query){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{	
			Query<?> criteria = session.createNativeQuery(query);
			ret = criteria.getResultList();			
		}
		catch(Throwable e){
			CLogger.write("1", CHistoria.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static String getHistoria(String query, String[] campos){
		String resultado = "";
		if(query!=null && !query.isEmpty() && campos!=null && campos.length>0){
			List<?> datos  = getDatos(query);
			for(int d=0; d<datos.size(); d++){
				Object[] dato = (Object[])datos.get(d);
				if(!resultado.isEmpty()){
					resultado+=", ";
				}
				resultado += "[";
				String objeto = "";
				for(int c=0; c<campos.length; c++){
					if(!objeto.isEmpty()){
						objeto+=", ";
					}
					objeto += "{\"nombre\": \""+campos[c] + "\", \"valor\": \"" + (dato[c]!=null?((Object)dato[c]).toString():"") + "\"}";					
				}
				resultado += objeto + "]";
			}
		}
		resultado="["+resultado+"]";
		return resultado;
	}

	public static List<?> getDatos(String query){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{	
			Query<?> criteria = session.createNativeQuery(query);
			ret = criteria.getResultList();			
		}
		catch(Throwable e){
			CLogger.write("2", CHistoria.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static String getFechasHistoriaMatriz(Integer prestamoId, String codigoPresupuestario){
		String resultado = "";
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{	
			String query = "select fecha from( "
					+ " select distinct "
					+ " DATE_FORMAT(ifnull(c.fecha_actualizacion,c.fecha_creacion), \"%Y-%m-%d %H:%i\") fecha "
					+ " from sipro_history.componente c, "
					+ " sipro_history.proyecto py "
					+ " JOIN sipro.prestamo p ON py.prestamoid = p.id "
					+ " where c.proyectoid = py.id "
					+ " and p.id = " + prestamoId
					+ " group by c.fuente_prestamo,c.fuente_donacion,c.fuente_nacional "
					+ " union "
					+ " select distinct "
					+ " DATE_FORMAT(ifnull(cs.fecha_actualizacion,cs.fecha_creacion), \"%Y-%m-%d %H:%i\") fecha "
					+ " from sipro_history.componente_sigade cs, sipro_history.componente c "
					+ " where c.componente_sigadeid = cs.id "
					+ " and cs.codigo_presupuestario = '"+codigoPresupuestario+"' "
					+ " ) t1 "
					+ " order by fecha asc ";
			
			Query<?> criteria = session.createNativeQuery(query);
			ret = criteria.getResultList();
			if(ret!=null){
				for(int i=0; i<ret.size(); i++){
					if(!resultado.isEmpty()){
						resultado+=", ";
					}
					resultado+="\""+ret.get(i)+"\"";
				}
			}
		}
		catch(Throwable e){
			CLogger.write("3", CHistoria.class, e);
		}
		finally{
			session.close();
		}
		return resultado;
	}
	
	public static List<?> getHistoriaMatriz(Integer proyectoId, Integer componenteId, String fecha){
		List<?> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{	
			String query = "select DATE_FORMAT(c.fecha_creacion,  \"%Y-%m-%d %H:%i\") fecha_creacion, "
					+ " DATE_FORMAT(c.fecha_actualizacion, \"%Y-%m-%d %H:%i\") fecha_actualizacion, c.id componenteid,c.nombre, "
					+ " c.fuente_prestamo,c.fuente_donacion,c.fuente_nacional, "
					+ " p.unidad_ejecutoraunidad_ejecutora,p.entidad, "
					+ " ( select ue.nombre "
					+ " from unidad_ejecutora ue "
					+ " where ue.unidad_ejecutora=p.unidad_ejecutoraunidad_ejecutora "
					+ " and ue.entidadentidad=p.entidad  "
					+ " limit 1 ) nombre_unidad_ejecutora, "
					+ " ( select cs.monto_componente "
					+ " from sipro_history.componente_sigade cs, sipro_history.componente ct "
					+ " where ct.componente_sigadeid = cs.id "
					+ " and ct.id = c.id "
					+ " and DATE_FORMAT(ifnull(cs.fecha_actualizacion,cs.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " and DATE_FORMAT(ifnull(ct.fecha_actualizacion,ct.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " order by ifnull(cs.fecha_actualizacion,cs.fecha_creacion), ifnull(ct.fecha_actualizacion,ct.fecha_creacion) desc "
					+ " limit 1 ) techo, "
					+ " ( select cs.id "
					+ " from sipro_history.componente_sigade cs, sipro_history.componente ct "
					+ " where ct.componente_sigadeid = cs.id "
					+ " and ct.id = c.id "
					+ " and DATE_FORMAT(ifnull(cs.fecha_actualizacion,cs.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " and DATE_FORMAT(ifnull(ct.fecha_actualizacion,ct.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " order by ifnull(cs.fecha_actualizacion,cs.fecha_creacion), ifnull(ct.fecha_actualizacion,ct.fecha_creacion) desc "
					+ " limit 1 ) componente_sigadeid, "
					+ " ( select cs.numero_componente "
					+ " from sipro_history.componente_sigade cs, sipro_history.componente ct "
					+ " where ct.componente_sigadeid = cs.id "
					+ " and ct.id = c.id "
					+ " and DATE_FORMAT(ifnull(cs.fecha_actualizacion,cs.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " and DATE_FORMAT(ifnull(ct.fecha_actualizacion,ct.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " order by ifnull(cs.fecha_actualizacion,cs.fecha_creacion), ifnull(ct.fecha_actualizacion,ct.fecha_creacion) desc "
					+ " limit 1 ) componente_sigadeNo "
					+ " from sipro_history.componente c, "
					+ " sipro_history.proyecto p "
					+ " where c.proyectoid = p.id "
					+ " and p.id = "+ proyectoId
					+ " and c.id = "+ componenteId
					+ " and DATE_FORMAT(ifnull(c.fecha_actualizacion,c.fecha_creacion), \"%Y-%m-%d %H:%i\") <= '"+fecha+"' "
					+ " order by ifnull(c.fecha_actualizacion,c.fecha_creacion) desc "
					+ " limit 1 ";
			
			Query<?> criteria = session.createNativeQuery(query);
			ret = criteria.getResultList();			
		}
		catch(Throwable e){
			CLogger.write("4", CHistoria.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
}
