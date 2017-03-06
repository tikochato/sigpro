package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.query.Query;


import pojo.Subproducto;
import pojo.SubproductoUsuario;
import pojo.SubproductoUsuarioId;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SubproductoDAO {
	
	public static List<Subproducto> getSubproductos(String usuario) {
		List<Subproducto> ret = new ArrayList<Subproducto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			Query<Subproducto> criteria = session.createQuery("FROM Subproducto s where s.id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )", Subproducto.class);
			criteria.setParameter("usuario", usuario);
			ret =   (List<Subproducto>)criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("1", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static Subproducto getSubproductoPorId(int id, String usuario) {
		Session session = CHibernateSession.getSessionFactory().openSession();
		Subproducto ret = null;
		try {
			Query<Subproducto> criteria = session.createQuery("FROM Subproducto where id=:id AND id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )", Subproducto.class);
			criteria.setParameter("id", id);
			criteria.setParameter("usuario", usuario);
			 ret = (Subproducto) criteria.getSingleResult();;
		} catch (Throwable e) {
			CLogger.write("2", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean guardarSubproducto(Subproducto subproducto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(subproducto);
			SubproductoUsuario su = new SubproductoUsuario(new SubproductoUsuarioId(subproducto.getId(), subproducto.getUsuarioCreo())
					, subproducto, subproducto.getUsuarioCreo(), null, new Date(), null);
			session.save(su);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("3", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarSubproducto(Subproducto subproducto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			subproducto.setEstado(0);
			session.beginTransaction();
			session.update(subproducto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("4", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalSubproducto(Subproducto subproducto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.delete(subproducto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("5", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static List<Subproducto> getSubproductosPagina(int pagina, int numeroSubproductos,Integer productoid, 
			String filtro_nombre, String filtro_usuario_creo, String filtro_fecha_creacion, String columna_ordenada, 
			String orden_direccion,String usuario) {
		List<Subproducto> ret = new ArrayList<Subproducto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT s FROM Subproducto s WHERE s.estado = 1 "
					+ (productoid!=null && productoid > 0 ? "AND s.producto.id = :idProd " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " s.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " s.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(s.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND s.id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )");
			query = columna_ordenada!=null && columna_ordenada.trim().length()>0 ? String.join(" ",query,"ORDER BY",columna_ordenada,orden_direccion ) : query;
			
			Query<Subproducto> criteria = session.createQuery(query,Subproducto.class);
			criteria.setParameter("usuario", usuario);
			if (productoid!=null && productoid>0){
				criteria.setParameter("idProd", productoid);
			}
			criteria.setFirstResult(((pagina - 1) * (numeroSubproductos)));
			criteria.setMaxResults(numeroSubproductos);
			
			ret = criteria.getResultList();
		} catch (Throwable e) {
			CLogger.write("6", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

	public static Long getTotalSubproductos(Integer productoid, String filtro_nombre, String filtro_usuario_creo, 
			String filtro_fecha_creacion, String usuario) {
		Long ret = 0L;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			
			String query = "SELECT count(s.id) FROM Producto s WHERE s.estado = 1 "
					+ (productoid!=null && productoid > 0 ? "AND s.componente.id = :idProd " : "");
			String query_a="";
			if(filtro_nombre!=null && filtro_nombre.trim().length()>0)
				query_a = String.join("",query_a, " s.nombre LIKE '%",filtro_nombre,"%' ");
			if(filtro_usuario_creo!=null && filtro_usuario_creo.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " s.usuarioCreo LIKE '%", filtro_usuario_creo,"%' ");
			if(filtro_fecha_creacion!=null && filtro_fecha_creacion.trim().length()>0)
				query_a = String.join("",query_a,(query_a.length()>0 ? " OR " :""), " str(date_format(s.fechaCreacion,'%d/%m/%YYYY')) LIKE '%", filtro_fecha_creacion,"%' ");
			query = String.join(" ", query, (query_a.length()>0 ? String.join("","AND (",query_a,")") : ""));
			if(usuario!=null)
				query = String.join("", query, " AND s.id in (SELECT u.id.subproductoid from SubproductoUsuario u where u.id.usuario=:usuario )");
			
			
			Query<Long> conteo = session.createQuery(query,Long.class);
			conteo.setParameter("usuario", usuario);
			if (productoid!=null && productoid > 0){
				conteo.setParameter("idProd", productoid);
			}
			ret = conteo.getSingleResult();
		} catch (Throwable e) {
			CLogger.write("7", SubproductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

}
