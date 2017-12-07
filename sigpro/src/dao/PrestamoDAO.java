package dao;

import java.util.List;
import javax.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ObjetoPrestamo;
import pojo.Prestamo;
import pojo.PrestamoUsuario;
import pojo.PrestamoUsuarioId;
import pojo.Usuario;
import utilities.CHibernateSession;
import utilities.CLogger;

public class PrestamoDAO {

	public static Prestamo getPrestamoPorObjetoYTipo(int objetoId, int objetoTipo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Prestamo ret = null;
		try{
			Query<Prestamo> criteria = session.createQuery("SELECT p FROM Prestamo p "
					+ "INNER JOIN p.objetoPrestamos op "
					+ " where op.id.objetoId = :objId "
					+ " and op.id.objetoTipo = :objTipo "
					+ " and p.id=op.prestamo.id"
					+ " and p.estado = 1 ", Prestamo.class);
			criteria.setParameter("objId", objetoId);
			criteria.setParameter("objTipo", objetoTipo);
			ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			CLogger.write("1", PrestamoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static ObjetoPrestamo getObjetoPrestamo(int idPrestamo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		ObjetoPrestamo ret = null;
		try{
			Query<ObjetoPrestamo> criteria = session.createQuery("SELECT op FROM Prestamo p "
					+ "INNER JOIN p.objetoPrestamos op "
					+ " where op.id.prestamoid= :idPrestamo "
					+ " and op.estado= 1", ObjetoPrestamo.class);
			criteria.setParameter("idPrestamo", idPrestamo);
			ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", PrestamoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static Prestamo getObjetoPrestamoPorId(int idPrestamo){
		Session session = CHibernateSession.getSessionFactory().openSession();
		Prestamo ret = null;
		try{
			Query<Prestamo> criteria = session.createQuery("FROM Prestamo p "
					+ " where p.id= :idPrestamo "
					+ " and p.estado= 1", Prestamo.class);
			criteria.setParameter("idPrestamo", idPrestamo);
			ret = criteria.getSingleResult();
		} catch (NoResultException e){
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("1", PrestamoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}

	public static boolean guardarPrestamo(Prestamo prestamo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(prestamo);
			
			Usuario usu = UsuarioDAO.getUsuario( prestamo.getUsuarioCreo());
			PrestamoUsuario pu = new PrestamoUsuario(new PrestamoUsuarioId(prestamo.getId(), prestamo.getUsuarioCreo()), prestamo,usu);
			session.saveOrUpdate(pu);
			
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", PrestamoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Prestamo> getPrestamosPagina(Integer pagina, Integer elementosPorPagina, String filtro_nombre, Long filtro_codigo_presupuestario, 
			String filtro_numero_prestamo, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada,
			String orden_direccion, String usuario){
		List<Prestamo> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Prestamo p where p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.proyectoPrograma LIKE '%",filtro_nombre,"%' ");
			if(filtro_codigo_presupuestario!=null)
				query_a = String.join("",query_a, " p.codigoPresupuestario= ",filtro_codigo_presupuestario.toString());
			if(filtro_numero_prestamo!=null && filtro_numero_prestamo.trim().length()>0)
				query_a = String.join("",query_a, " p.numeroPrestamo LIKE '%",filtro_numero_prestamo,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.prestamoid from PrestamoUsuario u where u.id.usuario=:usuario )");
			
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) :
				String.join(" ", query, "ORDER BY fecha_creacion ASC");
			
			Query<Prestamo> criteria = session.createQuery(query, Prestamo.class);
			criteria.setFirstResult(((pagina-1)*(elementosPorPagina)));
			criteria.setMaxResults(elementosPorPagina);
			if(usuario != null){
				criteria.setParameter("usuario", usuario);
			}
			ret = criteria.getResultList();
		}catch(Exception e){
			ret = null;
			CLogger.write("3", PrestamoDAO.class, e);
		}finally{
			session.close();
		}
		
		
		return ret;
	}
	
	public static Long getTotalPrestamos(String filtro_nombre, Long filtro_codigo_presupuestario, 
			String filtro_numero_prestamo, String filtro_usuario_creo, String filtro_fecha_creacion, String usuario){
		Long ret=0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT count(p.id) FROM Prestamo p WHERE p.estado=1 ";
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " p.proyectoPrograma LIKE '%",filtro_nombre,"%' ");
			if(filtro_codigo_presupuestario!=null)
				query_a = String.join("",query_a, " p.codigoPresupuestario= ",filtro_codigo_presupuestario.toString());
			if(filtro_numero_prestamo!=null && filtro_numero_prestamo.trim().length()>0)
				query_a = String.join("",query_a, " p.numeroPrestamo LIKE '%",filtro_numero_prestamo,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " p.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(p.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND p.id in (SELECT u.id.prestamoid from PrestamoUsuario u where u.id.usuario=:usuario )");
			Query<Long> criteria = session.createQuery(query,Long.class);
			if(usuario != null){
				criteria.setParameter("usuario", usuario);	
			}			
			ret = criteria.getSingleResult();
		}
		catch(Throwable e){
			CLogger.write("5", ProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean borrarPrestamo(Prestamo prestamo){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			prestamo.setEstado(0);
			session.beginTransaction();
			session.saveOrUpdate(prestamo);
			session.getTransaction().commit();
			ret = true;
		}catch(Exception e){
			CLogger.write("6", PrestamoDAO.class, e);
		}finally {
			session.close();
		}
		
		return ret;
	}
	
	public static Prestamo getPrestamoById(Integer idPrestamo){
		Prestamo ret = null;
		List<Prestamo> lstret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Prestamo> criteria = session.createQuery("FROM Prestamo p where p.id=:idPrestamo", Prestamo.class);
			criteria.setParameter("idPrestamo", idPrestamo);
			lstret = criteria.getResultList();
			if(lstret != null && !lstret.isEmpty()){
				ret = lstret.get(0);
			}
		}catch(Exception e){
			CLogger.write("7", PrestamoDAO.class, e);
		}finally {
			session.close();
		}
		
		return ret;
	}
	
	public static List<Prestamo> getPrestamos(String usuario){
		List<Prestamo> ret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = "SELECT p FROM Prestamo p where p.estado=1 ";
			if(usuario!=null){
				query+= "and p.id in (SELECT u.id.prestamoid from PrestamoUsuario u where u.id.usuario=:usuario ) ";
			}
			Query<Prestamo> criteria = session.createQuery(query,Prestamo.class);
			if(usuario!=null){
				criteria.setParameter("usuario", usuario);
			}
			ret = criteria.getResultList();
		}catch(Exception e){
			CLogger.write("8", PrestamoDAO.class, e);
		}finally{
			session.close();
		}
		
		return ret;
	}
	
	public static Prestamo getPrestamoByIdHistory(Integer idPrestamo, String lineaBase){
		Prestamo ret = null;
		List<Prestamo> lstret = null;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			String query = String.join(" ", "select * from sipro_history.prestamo p ",
					"where p.id = ?1 ",
					"and p.estado = 1 and p.actual = 1");
					//lineaBase != null ? "and p.linea_base like '%"+lineaBase+"%'" : "and p.actual = 1 ");
			Query<Prestamo> criteria = session.createNativeQuery(query, Prestamo.class);
			criteria.setParameter(1, idPrestamo);
			lstret = criteria.getResultList();
			if(lstret != null && !lstret.isEmpty()){
				ret = lstret.get(0);
			}
		}catch(Exception e){
			CLogger.write("7", PrestamoDAO.class, e);
		}finally {
			session.close();
		}
		
		return ret;
	}
	
}
