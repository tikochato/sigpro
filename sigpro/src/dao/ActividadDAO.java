package dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Actividad;
import pojo.ActividadUsuario;
import pojo.ActividadUsuarioId;
import utilities.CHibernateSession;
import utilities.CLogger;
import utilities.Utils;

class duracionFecha{
	int id;
	int duracion;
	Date fecha_inicial;
	Date fecha_final;
	String dimension;
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setDuracion(int duracion){
		this.duracion = duracion;
	}
	
	public int getDuracion(){
		return this.duracion;
	}
	
	public void setFechaInicial(Date fechaInicial){
		this.fecha_inicial = fechaInicial;
	}
	
	public Date getFechaInicial(){
		return this.fecha_inicial;
	}
	
	public void setFechaFin(Date fechaFin){
		this.fecha_final = fechaFin;
	}
	
	public Date getFechaFin(){
		return this.fecha_final;
	}
	
	public void setDimension(String dimension){
		this.dimension = dimension;
	}
	
	public String getDimension(){
		return this.dimension;
	}
}

public class ActividadDAO {
	public static List<Actividad> getActividads(String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Actividad> criteria = session.createQuery("FROM Actividad p where p.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )", Actividad.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Actividad getActividadPorId(int id, String usuario){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Actividad ret = null;
		try{
			String query = "FROM Actividad where id=:id";
			if (usuario != null){
				query += " AND id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )";
			}
			Query<Actividad> criteria = session.createQuery(query, Actividad.class);
			criteria.setParameter("id", id);
			if(usuario != null){
			criteria.setParameter("usuario", usuario);
			}
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("2", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(Actividad);
			ActividadUsuario au = new ActividadUsuario(new ActividadUsuarioId(Actividad.getId(), Actividad.getUsuarioCreo()),Actividad);
			session.saveOrUpdate(au);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("3", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Actividad.setEstado(0);
			session.beginTransaction();
			session.update(Actividad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("4", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean eliminarTotalActividad(Actividad Actividad){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(Actividad);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("5", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static List<Actividad> getActividadsPagina(int pagina, int numeroActividads,
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion,
			String columna_ordenada, String orden_direccion, String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT c FROM Actividad c WHERE estado = 1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			query = String.join("", query, " AND c.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario ) ");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("usuario", usuario);
			criteria.setFirstResult(((pagina-1)*(numeroActividads)));
			criteria.setMaxResults(numeroActividads);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("6", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static Long getTotalActividads(String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(c.id) FROM Actividad c WHERE c.estado=1";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " c.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " c.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(c.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join("", query, " AND c.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario ) ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("usuario", usuario);
			ret = conteo.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("7", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}


	public static List<Actividad> getActividadsPaginaPorObjeto(int pagina, int numeroActividads, int objetoId, int objetoTipo,String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String columna_ordenada, String orden_direccion, String usuario){
		List<Actividad> ret = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.estado = 1 AND a.objetoId = :objetoId AND a.objetoTipo = :objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " a.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " a.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(a.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, "AND a.estado=1 AND a.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query," ORDER BY",columna_ordenada,orden_direccion ) : query;
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			if (usuario!=null){
				criteria.setParameter("usuario", usuario);
			}
			criteria.setFirstResult(((pagina-1)*(numeroActividads)));
			criteria.setMaxResults(numeroActividads);
			ret = criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("8", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalActividadsPorObjeto(int objetoId, int objetoTipo, String filtro_nombre, String filtro_usuario_creo,
			String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(a.id) FROM Actividad a WHERE a.estado=1 and a.objetoId=:objetoId and a.objetoTipo=:objetoTipo ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " a.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " a.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(a.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND a.id in (SELECT u.id.actividadid from ActividadUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("9", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static String getFechaInicioFin(Actividad actividad, String usuario){
		List<duracionFecha> objetoActividadFechas = new ArrayList<duracionFecha>();
		String fecha = "";
		
		duracionFecha df = new duracionFecha();
		df.setId(actividad.getId());
		df.setDimension(actividad.getDuracionDimension());
		df.setDuracion(actividad.getDuracion());
		objetoActividadFechas.add(df);
		if(actividad.getPredObjetoId() != null && actividad.getPredObjetoId() != actividad.getId()){
			objetoActividadFechas = getPredecesora(getActividadPorId(actividad.getPredObjetoId(), usuario),usuario, objetoActividadFechas);
			
			Date fechaFI = null;
			for(int i = objetoActividadFechas.size()-1; i >= 0; i--){
				if(fechaFI != null)
					objetoActividadFechas.get(i).setFechaInicial(getFechaFinal(fechaFI,1,objetoActividadFechas.get(i).getDimension().charAt(0)));
				Date fechaI = objetoActividadFechas.get(i).fecha_inicial;
				objetoActividadFechas.get(i).setFechaFin(getFechaFinal(fechaI,objetoActividadFechas.get(i).getDuracion(),objetoActividadFechas.get(i).getDimension().charAt(0)));
				fechaFI = objetoActividadFechas.get(i).getFechaFin();
			}

			fecha = Utils.formatDate(objetoActividadFechas.get(0).getFechaInicial()) + ";" + Utils.formatDate(objetoActividadFechas.get(0).getFechaFin()); 
		}else{
			fecha = Utils.formatDate(actividad.getFechaInicio()) + ";" + Utils.formatDate(getFechaFinal(actividad.getFechaInicio(),actividad.getDuracion(),actividad.getDuracionDimension().charAt(0))); 
		}
		
		return fecha;
	}
	
	private static List<duracionFecha> getPredecesora(Actividad actividad, String usuario, List<duracionFecha> fechasPredecesoras){
		duracionFecha df = new duracionFecha();
		df.setId(actividad.getId());
		df.setDimension(actividad.getDuracionDimension());
		df.setDuracion(actividad.getDuracion());
		fechasPredecesoras.add(df);
		
		if(actividad.getPredObjetoId() != null && actividad.getPredObjetoId() != actividad.getId()){
			fechasPredecesoras = getPredecesora(getActividadPorId(actividad.getPredObjetoId(), usuario),usuario, fechasPredecesoras);
		}else{
			fechasPredecesoras.get(fechasPredecesoras.size()-1).setFechaInicial(actividad.getFechaInicio());
		}
		
		return fechasPredecesoras;
	}
	
	public static List<Actividad> getActividadsSubactividadsPorObjeto(int objetoId, int objetoTipo){
		List<Actividad> ret = new ArrayList<Actividad>();
		List<Actividad> subactividades = new ArrayList<Actividad>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT a FROM Actividad a WHERE a.estado = 1 AND a.objetoId = :objetoId AND a.objetoTipo = :objetoTipo ";
			Query<Actividad> criteria = session.createQuery(query,Actividad.class);
			criteria.setParameter("objetoId", objetoId);
			criteria.setParameter("objetoTipo", objetoTipo);
			ret = criteria.getResultList();
			for(Actividad actividad : ret){
				subactividades.addAll(getActividadsSubactividadsPorObjeto(actividad.getId(), 5));
			}
			ret.addAll(subactividades);			
		}
		catch(Throwable e){
			CLogger.write("10", ActividadDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Actividad getFechasActividad(Actividad actividad){
		int predecesor = (actividad.getPredObjetoId()!= null)?actividad.getPredObjetoId():0; 
		if( predecesor > 0){
			Actividad actividad_pred = getActividadPorId(predecesor, null);
			actividad_pred = getFechasActividad(actividad_pred);
			Calendar fecha_final_pred = Calendar.getInstance();
			fecha_final_pred.setTime(actividad_pred.getFechaFin());
			fecha_final_pred.add(Calendar.DAY_OF_MONTH, 1);
			actividad.setFechaInicio(fecha_final_pred.getTime());
		}
		
		Date fechaInicio = actividad.getFechaInicio();
		if (fechaInicio != null && actividad.getDuracionDimension() != null && !actividad.getDuracionDimension().isEmpty()){
			Date fechaFinal = getFechaFinal(fechaInicio, actividad.getDuracion(), actividad.getDuracionDimension().charAt(0));
			actividad.setFechaFin(fechaFinal);
		}

		return actividad;
	}
	
	public static Date getFechaFinal(Date fecha_inicio, int duracion, char dimension){
		Calendar fecha_final = Calendar.getInstance();
		fecha_final.setTime(fecha_inicio);
		
		if (Character.toUpperCase(dimension) == 'D'){ //Restamos un día para validar que la fecha de inicio sea día hábil
			fecha_final.add(Calendar.DAY_OF_MONTH, -1);
		}
		Integer contador=0;
		while (contador < duracion){
			switch(Character.toUpperCase(dimension)){
				case 'D':  //día
					fecha_final.add(Calendar.DAY_OF_MONTH, 1);
					break;
				default: 
			}
			boolean esFechaHabil = esFechaHabil(fecha_final); 
			if (esFechaHabil) {
                contador++;
            }
		}
		return new Date(fecha_final.getTimeInMillis());
	}
	
	public static boolean esFechaHabil(Calendar fecha) {
	      switch (fecha.get(Calendar.DAY_OF_WEEK)){
	        case Calendar.SUNDAY:
	        	return false; 
	        case Calendar.SATURDAY:
	        	return false; 
	        default:
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 1 && fecha.get(Calendar.MONTH) == Calendar.JANUARY) //Año nuevo
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 1 && fecha.get(Calendar.MONTH) == Calendar.MARCH) //Día del Trabajo
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 15 && fecha.get(Calendar.MONTH) == Calendar.SEPTEMBER) //Independencia
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 20 && fecha.get(Calendar.MONTH) == Calendar.OCTOBER) //Revolución
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 1 && fecha.get(Calendar.MONTH) == Calendar.NOVEMBER) //Todos los Santos
	        	  return false;
	          if (fecha.get(Calendar.DAY_OF_MONTH) == 25 && fecha.get(Calendar.MONTH) == Calendar.DECEMBER) //Navidad
	        	  return false;
	          break;
	        }
	        return true;
	}
}
