package dao;



import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pojo.Usuario;
import pojo.UsuarioLog;
import pojo.UsuarioLogId;
import pojo.UsuarioPermiso;
import pojo.UsuarioPermisoId;
import pojo.Colaborador;
import pojo.Componente;
import pojo.ComponenteUsuario;
import pojo.ComponenteUsuarioId;
import pojo.Cooperante;
import pojo.Permiso;
import pojo.Prestamo;
import pojo.PrestamoUsuario;
import pojo.PrestamoUsuarioId;
import pojo.Producto;
import pojo.ProductoUsuario;
import pojo.ProductoUsuarioId;
import pojo.Proyecto;
import pojo.ProyectoUsuario;
import pojo.ProyectoUsuarioId;
import pojo.RolUsuarioProyecto;
import pojo.RolUsuarioProyectoId;
import pojo.Subcomponente;
import pojo.SubcomponenteUsuario;
import pojo.SubcomponenteUsuarioId;
import pojo.UnidadEjecutora;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utilities.CHibernateSession;
import utilities.CLogger;

public class UsuarioDAO {

	public static Usuario getUsuario(String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Usuario ret = null;
		try{
			String query = "FROM Usuario u where u.usuario=:usuario";
			Query<Usuario> criteria = session.createQuery(query, Usuario.class);
			criteria.setParameter("usuario", usuario);
			List<Usuario> listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch(Throwable e){
			CLogger.write("1", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static void userLoginHistory(String usuario) {
		UsuarioLogId usuariologid = new UsuarioLogId(usuario, new DateTime().toDate());
		UsuarioLog usuariolog = new UsuarioLog(usuariologid);
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(usuariolog);
			session.getTransaction().commit();
		}catch(Throwable e){
			CLogger.write("2", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
	}

	public static boolean tienePermiso(String usuario, String permisonombre) {
		boolean ret=false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder(); 
			CriteriaQuery<Permiso> criteria = builder.createQuery(Permiso.class);
			Root<Permiso> root = criteria.from(Permiso.class);
			criteria.select( root );
			criteria.where( builder.equal( root.get("id"), permisonombre ) );
			List<Permiso> listRet = null;
			listRet =session.createQuery( criteria ).getResultList();
			Permiso permiso = !listRet.isEmpty() ? listRet.get(0) : null;
			if(permiso !=null){
				UsuarioPermisoId usuariopermisoid = new UsuarioPermisoId(usuario,permiso.getId());
				UsuarioPermiso usuariopermiso = session.get(UsuarioPermiso.class, usuariopermisoid);
				if(usuariopermiso!=null){
					if(usuariopermiso.getEstado()==1){
						ret = true;
					}
				}
			}
			
		}
		catch(Throwable e){
			CLogger.write("4", UsuarioDAO.class, e);
		}
		return ret;
	}
	
	public static boolean registroUsuario(String cadenausuario, String email, String passwordTextoPlano, String usuarioCreo, Integer sistemaUsuario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Usuario usuario = new Usuario();
			usuario.setUsuario(cadenausuario);
			usuario.setEmail(email);
			RandomNumberGenerator rng = new SecureRandomNumberGenerator();
			Object salt = rng.nextBytes();
			String hashedPasswordBase64 = new Sha256Hash(passwordTextoPlano, salt,1024).toBase64();
			usuario.setPassword(hashedPasswordBase64);
			usuario.setSalt(salt.toString());
			usuario.setFechaCreacion(new DateTime().toDate());
			usuario.setUsuarioCreo(usuarioCreo);
			usuario.setEstado(1);
			usuario.setSistemaUsuario(sistemaUsuario);
			session.save(usuario);
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("5", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;		
	}
	public static boolean cambiarPassword(String usuario,String password, String usuarioActualizo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Usuario usuarioCambio= session.get(Usuario.class,usuario);
			if(usuarioCambio!=null){
				session.beginTransaction();
				RandomNumberGenerator rng = new SecureRandomNumberGenerator();
				Object salt = rng.nextBytes();
				String hashedPasswordBase64 = new Sha256Hash(password, salt,1024).toBase64();
				usuarioCambio.setPassword(hashedPasswordBase64);
				usuarioCambio.setSalt(salt.toString());
				usuarioCambio.setUsuarioActualizo(usuarioActualizo);
				usuarioCambio.setFechaActualizacion(new DateTime().toDate());
				session.update(usuarioCambio);
				session.getTransaction().commit();
				ret = true;
			} 
		}catch(Throwable e){
			CLogger.write("6", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	public static boolean asignarPermisosUsuario(String usuario, List <Integer> permisos,String usuarioTexto){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i =0; i<permisos.size();i++){
				UsuarioPermisoId usuariopermisoid = new UsuarioPermisoId(usuario, permisos.get(i));
				UsuarioPermiso usuariopermiso = new UsuarioPermiso();
				usuariopermiso.setId(usuariopermisoid);
				usuariopermiso.setUsuarioCreo(usuarioTexto);
				usuariopermiso.setEstado(1);
				usuariopermiso.setFechaCreacion(new DateTime().toDate());
				session.save(usuariopermiso);
				if( i % 20 == 0 ){
					session.flush();
		            session.clear();
		        }
			}
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("7", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean asignarPrestamos(String usuario, List <Integer> prestamos, String usuario_creo){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			
			for(int i =0; i<prestamos.size();i++){
				Prestamo prestamo = PrestamoDAO.getPrestamoById(prestamos.get(i));
				PrestamoUsuario pu = new PrestamoUsuario(new PrestamoUsuarioId(prestamos.get(i), usuario),prestamo, UsuarioDAO.getUsuario(usuario),
						usuario_creo,null, new Date(), null);
				pu.setPrestamo(prestamo);
				session.save(pu);
			}			
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("8", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	
	public static boolean asignarProyectos(String usuario, List <Integer> proyectos, String usuario_creo){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			
			for(int i =0; i<proyectos.size();i++){
				Proyecto proyecto = ProyectoDAO.getProyecto(proyectos.get(i));
				ProyectoUsuario pu = new ProyectoUsuario(new ProyectoUsuarioId(proyectos.get(i), usuario), proyecto, UsuarioDAO.getUsuario(usuario),
						usuario_creo,null, new Date(), null);
				pu.setProyecto(proyecto);
				session.save(pu);
			}			
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("8", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean asignarPrestamoRol(String usuario, List <Integer> prestamos, int rol){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i =0; i<prestamos.size();i++){
				RolUsuarioProyectoId rolUsuarioProyectoId = new RolUsuarioProyectoId(rol,prestamos.get(i).intValue(),usuario);
				RolUsuarioProyecto rolUsuario = new RolUsuarioProyecto(rolUsuarioProyectoId);
				session.saveOrUpdate(rolUsuario);
				if( i % 20 == 0 ){
					session.flush();
		            session.clear();
		        }	
			}		
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("9", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean desasignarPrestamo(String usuario, List <Integer> prestamos){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i =0; i<prestamos.size();i++){
				Query<ProyectoUsuario> criteria = session.createQuery("FROM ProyectoUsuario where id.proyectoid=:id AND id.usuario=:usuario ", ProyectoUsuario.class);
				criteria.setParameter("id", prestamos.get(i));
				criteria.setParameter("usuario", usuario);
				List<ProyectoUsuario> listRet = null;
				listRet = criteria.getResultList();				            
				ProyectoUsuario pu = !listRet.isEmpty() ? listRet.get(0) : null;
				session.delete(pu);
			}			
			session.getTransaction().commit();
			session.flush();
			ret = true;
		}catch(Throwable e){
			CLogger.write("10", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	
	public static boolean desactivarPermisosUsuario(String usuario, List <Integer> permisos, String usuarioTexto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			for(int i =0; i<permisos.size();i++){
				UsuarioPermisoId usuariopermisoid = new UsuarioPermisoId(usuario, permisos.get(i));
				UsuarioPermiso usuariopermiso= session.get(UsuarioPermiso.class, usuariopermisoid);
				usuariopermiso.setUsuarioActualizo(usuarioTexto);
				usuariopermiso.setEstado(0);
				usuariopermiso.setFechaActualizacion(new DateTime().toDate());
				session.saveOrUpdate(usuariopermiso);
				if( i % 20 == 0 ){
					session.flush();
		            session.clear();
		        }
			}
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("11", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean existeUsuario(String usuario){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			if (session.get(Usuario.class,usuario)!=null){
				ret=true;
			}
		}
		catch(Throwable e){
			CLogger.write("12", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean desactivarUsuario(String usuario, String usuarioActualizo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Usuario usuarioDesactivado = session.get(Usuario.class,usuario);
			usuarioDesactivado.setEstado(0);
			usuarioDesactivado.setUsuarioActualizo(usuarioActualizo);
			usuarioDesactivado.setFechaActualizacion(new DateTime().toDate());
			session.saveOrUpdate(usuarioDesactivado);
			session.getTransaction().commit();
			ret=true;
		}
		catch(Throwable e){
			CLogger.write("13", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	public static UnidadEjecutora  getUnidadEjecutora(String usuario){
		UnidadEjecutora ret = new UnidadEjecutora();
		Session session = CHibernateSession.getSessionFactory().openSession();
		List <UnidadEjecutora> unidades = new ArrayList <UnidadEjecutora> ();
		String consulta ="Select u FROM UnidadEjecutora u, RolUsuarioProyecto r, Proyecto p where p.id =r.id.proyecto and p.unidadEjecutora.id = u.id and r.id.usuario =:usuario ";
		try{
			session.beginTransaction();
			Query<UnidadEjecutora> criteria = session.createQuery(consulta, UnidadEjecutora.class);
			criteria.setParameter("usuario", usuario);
			unidades = criteria.getResultList();
			if(unidades.size()>0){
				ret = unidades.get(0);
			}
			
		}
		catch(Throwable e){
			CLogger.write("14", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}				
		return ret;
	}
	
	public static Cooperante getCooperantePorUsuario(String usuario){
		Cooperante ret = new Cooperante();
		Session session = CHibernateSession.getSessionFactory().openSession();
		List <Cooperante> unidades = new ArrayList <Cooperante> ();
		String consulta ="Select u FROM Cooperante u, RolUsuarioProyecto r, Proyecto p where r.id.proyecto=p.id and p.cooperante.id=u.id and r.id.usuario =:usuario ";
		try{
			session.beginTransaction();
			Query<Cooperante> criteria = session.createQuery(consulta, Cooperante.class);
			criteria.setParameter("usuario", usuario);
			unidades = criteria.getResultList();
			if(unidades.size()>0){
				ret = unidades.get(0);
			}
			
		}
		catch(Throwable e){
			CLogger.write("15", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static int getRolPorUsuario(String usuario){
		int ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		List <Integer> unidades = new ArrayList <Integer> ();
		String consulta ="Select r.id.rol FROM  RolUsuarioProyecto r where r.id.usuario =:usuario ";
		try{
			session.beginTransaction();
			Query<Integer> criteria = session.createQuery(consulta, Integer.class);
			criteria.setParameter("usuario", usuario);
			unidades = criteria.getResultList();
			if(unidades.size()>0){
				ret = unidades.get(0);
			}
		}
		catch(Throwable e){
			CLogger.write("16", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	
	public static boolean editarUsuario(Usuario usuario, String usuarioActualizo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			usuario.setUsuarioActualizo(usuarioActualizo);
			usuario.setFechaActualizacion(new DateTime().toDate());
			session.saveOrUpdate(usuario);
			session.getTransaction().commit();
			ret=true;
		}catch(Throwable e){
			CLogger.write("17", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static List <UsuarioPermiso> getPermisosActivosUsuario(String usuario){
		List <UsuarioPermiso> ret = new ArrayList <UsuarioPermiso> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<UsuarioPermiso> criteria = session.createQuery("FROM UsuarioPermiso where usuariousuario=:usuario and estado = 1 ORDER BY permiso.nombre", UsuarioPermiso.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("17", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static List <ProyectoUsuario> getPrestamosAsignadosPorUsuario(String usuario){
		List <ProyectoUsuario> ret = new ArrayList <ProyectoUsuario> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<ProyectoUsuario> criteria = session.createQuery("FROM ProyectoUsuario where id.usuario=:usuario", ProyectoUsuario.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("18", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static List <Permiso> getPermisosDisponibles (String usuario){
		List <Permiso> ret = new ArrayList <Permiso> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<Permiso> criteria = session.createQuery("FROM Permiso where id not in (Select permiso from UsuarioPermiso where usuariousuario =:usuario ) ORDER BY nombre", Permiso.class);
			criteria.setParameter("usuario", usuario);
			ret = criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("19", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static List <Usuario> getUsuarios(int pagina, int numeroUsuarios, String usuario, String email, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		List <Usuario> ret = new ArrayList<Usuario> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "FROM Usuario u where estado =:estado";
			String query_a="";
			if(usuario!=null && usuario.trim().length()>0)
				query_a = String.join("",query_a, " u.usuario LIKE '%",usuario,"%' ");
			if(email!=null && email.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :"") ," u.email LIKE '%",email,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " u.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(u.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND ",query_a,"") : ""));
			Query <Usuario> criteria = session.createQuery(query, Usuario.class);
			criteria.setParameter("estado",1);
			criteria.setFirstResult(((pagina-1)*(numeroUsuarios)));
			criteria.setMaxResults(numeroUsuarios);
			ret =criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("20", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static Long getTotalUsuarios( String usuario, String email, String filtro_usuario_creo,
			String filtro_fecha_creacion){
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query ="SELECT count(u.usuario) FROM Usuario u WHERE u.estado=1";
			String query_a="";
			if(usuario!=null && usuario.trim().length()>0)
				query_a = String.join("",query_a, " u.usuario LIKE '%",usuario,"%' ");
			if(email!=null && email.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " u.email LIKE '%",email,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " u.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(u.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			Query<Long> conteo = session.createQuery(query,Long.class);
			ret = conteo.getSingleResult();
		}catch(Throwable e){
			CLogger.write("21", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;		
	}
	
	public static Colaborador getColaborador(String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Colaborador ret = null;
		try {
			Query <Colaborador> criteria = session.createQuery("FROM Colaborador where usuariousuario =:usuario", Colaborador.class);
			criteria.setParameter("usuario",usuario);
			List<Colaborador> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? listRet.get(0) : null;
		} catch (Throwable e) {
			CLogger.write("22", UsuarioDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static List <Usuario> getUsuariosDisponibles(){
		List <Usuario> ret = new ArrayList<Usuario> ();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query <Usuario> criteria = session.createQuery("FROM Usuario where estado =:estado", Usuario.class);
			criteria.setParameter("estado",1);
			ret =criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("23", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
		
	public static List <Proyecto> getPrestamosPorElemento(int elemento, int id_elemento,String usuario){
		String busqueda="";
		if(elemento==4 || elemento==5){
			busqueda="Select p FROM Proyecto p, ProyectoUsuario u where p.unidadEjecutora.unidadEjecutora =:id and p.estado=1 and p.id =u.id.proyectoid and u.id.usuario=:usuario";
		}else if(elemento==6){
			busqueda= "Select p FROM Proyecto p, ProyectoUsuario u where p.cooperante.id =:id and p.estado=1 and p.id =u.id.proyectoid and u.id.usuario=:usuario";
		}else{
			busqueda= "Select p FROM Proyecto p where  p.estado=1";
		}
		List <Proyecto> ret = new ArrayList<Proyecto> ();
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query <Proyecto> criteria = session.createQuery(busqueda, Proyecto.class);
			if(elemento>=4){
				criteria.setParameter("id",id_elemento);
				criteria.setParameter("usuario",usuario);
			}
			ret =criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("24", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	
	public static List <RolUsuarioProyecto> getUsuariosPorPrestamo(int proyecto){
		List <RolUsuarioProyecto> ret = new ArrayList<RolUsuarioProyecto> ();
		
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query <RolUsuarioProyecto> criteria = session.createQuery("FROM RolUsuarioProyecto where id.proyecto=:proyecto", RolUsuarioProyecto.class);
			criteria.setParameter("proyecto",proyecto);
			ret =criteria.getResultList();
		}catch(Throwable e){
			CLogger.write("25", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean asignarComponentes(String usuario, List <Integer> componentes, String usuario_creo){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			
			for(int i =0; i<componentes.size();i++){
				Componente componente = ComponenteDAO.getComponente(componentes.get(i));
				ComponenteUsuario cu = new ComponenteUsuario(new ComponenteUsuarioId(componentes.get(i), usuario), componente, UsuarioDAO.getUsuario(componente.getUsuarioCreo()));
				cu.setComponente(componente);
				session.save(cu);
			}			
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("26", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean asignarSubComponentes(String usuario, List <Integer> subcomponentes, String usuario_creo){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			
			for(int i =0; i<subcomponentes.size();i++){
				Subcomponente subcomponente = SubComponenteDAO.getSubComponente(subcomponentes.get(i));
				SubcomponenteUsuario cu = new SubcomponenteUsuario(new SubcomponenteUsuarioId(subcomponentes.get(i), usuario), subcomponente);
				cu.setSubcomponente(subcomponente);
				session.save(cu);
			}			
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("27", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean checkUsuarioPrestamo(String usuario, int prestmoid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		boolean ret = false;
		try {
			Query <PrestamoUsuario> criteria = session.createQuery("FROM PrestamoUsuario where usuario.usuario=:usuario and prestamo.id=:id", PrestamoUsuario.class);
			criteria.setParameter("usuario",usuario);
			criteria.setParameter("id", prestmoid);
			List<PrestamoUsuario> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? true : false;
		} catch (Throwable e) {
			CLogger.write("28", UsuarioDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean checkUsuarioProyecto(String usuario, int proyectoid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		boolean ret = false;
		try {
			Query <ProyectoUsuario> criteria = session.createQuery("FROM ProyectoUsuario where usuario.usuario=:usuario and proyecto.id=:id", ProyectoUsuario.class);
			criteria.setParameter("usuario",usuario);
			criteria.setParameter("id", proyectoid);
			List<ProyectoUsuario> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? true : false;
		} catch (Throwable e) {
			CLogger.write("28", UsuarioDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean checkUsuarioComponente(String usuario, int componenteid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		boolean ret = false;
		try {
			Query <ComponenteUsuario> criteria = session.createQuery("FROM ComponenteUsuario where id.usuario=:usuario and id.componenteid=:id", ComponenteUsuario.class);
			criteria.setParameter("usuario",usuario);
			criteria.setParameter("id", componenteid);
			List<ComponenteUsuario> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? true : false;
		} catch (Throwable e) {
			CLogger.write("29", UsuarioDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean checkUsuarioSubComponente(String usuario, int subcomponenteid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		boolean ret = false;
		try {
			Query <SubcomponenteUsuario> criteria = session.createQuery("FROM SubcomponenteUsuario where id.usuario=:usuario and id.subcomponenteid=:id", SubcomponenteUsuario.class);
			criteria.setParameter("usuario",usuario);
			criteria.setParameter("id", subcomponenteid);
			List<SubcomponenteUsuario> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? true : false;
		} catch (Throwable e) {
			CLogger.write("30", UsuarioDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean checkUsuarioProducto(String usuario, int productoid){
		Session session = CHibernateSession.getSessionFactory().openSession();
		boolean ret = false;
		try {
			Query <ProductoUsuario> criteria = session.createQuery("FROM ProductoUsuario where id.usuario=:usuario and id.productoid=:id", ProductoUsuario.class);
			criteria.setParameter("usuario",usuario);
			criteria.setParameter("id", productoid);
			List<ProductoUsuario> listRet = null;
			listRet = criteria.getResultList();
			ret = !listRet.isEmpty() ? true : false;
		} catch (Throwable e) {
			CLogger.write("31", UsuarioDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	
	public static boolean asignarProductos(String usuario, List <Integer> productos, String usuario_creo){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			
			for(int i =0; i<productos.size();i++){
				Producto producto = ProductoDAO.getProductoPorId(productos.get(i));
				ProductoUsuario uu = new ProductoUsuario(new ProductoUsuarioId(productos.get(i), usuario), producto, UsuarioDAO.getUsuario(usuario));
				session.save(uu);
			}			
			session.getTransaction().commit();
			ret = true;
		}catch(Throwable e){
			CLogger.write("32", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean desasignarEstructurasPermisos(String usuario){
		boolean ret =false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<?> criteria_prs = session.createQuery("delete PrestamoUsuario where id.usuario=:usuario ");
			criteria_prs.setParameter("usuario", usuario);
			criteria_prs.executeUpdate();
			Query<?> criteria = session.createQuery("delete ProyectoUsuario where id.usuario=:usuario ");
			criteria.setParameter("usuario", usuario);
			criteria.executeUpdate();
			Query<?> criteria_c = session.createQuery("delete ComponenteUsuario where id.usuario=:usuario ");
			criteria_c.setParameter("usuario", usuario);
			criteria_c.executeUpdate();
			Query<?> criteria_sc = session.createQuery("delete SubcomponenteUsuario where id.usuario=:usuario ");
			criteria_sc.setParameter("usuario", usuario);
			criteria_sc.executeUpdate();
			Query<?> criteria_u = session.createQuery("delete ProductoUsuario where id.usuario=:usuario ");
			criteria_u.setParameter("usuario", usuario);
			criteria_u.executeUpdate();
			session.getTransaction().commit();
//			session.flush();
			ret = true;
		}catch(Throwable e){
			CLogger.write("33", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	public static boolean desasignarPermisos(String usuario){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			Query<?> criteria = session.createQuery("delete UsuarioPermiso up where up.usuario.usuario=:usuario ");
			criteria.setParameter("usuario", usuario);
			criteria.executeUpdate();
			session.getTransaction().commit();
			//session.flush();
			ret = true;
		}catch(Throwable e){
			CLogger.write("34", UsuarioDAO.class, e);
		}
		finally{
			session.close();
		}
		
		return ret;
	}
	
	
	public static Usuario setNuevoPassword(Usuario usuario,String password){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			if(usuario!=null){
				session.beginTransaction();
				RandomNumberGenerator rng = new SecureRandomNumberGenerator();
				Object salt = rng.nextBytes();
				String hashedPasswordBase64 = new Sha256Hash(password, salt,1024).toBase64();
				usuario.setPassword(hashedPasswordBase64);
				usuario.setSalt(salt.toString());
			} 
		}catch(Throwable e){
			CLogger.write("35", UsuarioDAO.class, e);
		}finally{
			session.close();
		}
		return usuario;
	}
}
