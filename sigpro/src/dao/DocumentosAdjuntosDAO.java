package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.Documento;
import pojo.Proyecto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class DocumentosAdjuntosDAO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public static Integer guardarDocumentoAdjunto(Documento documento){
		Integer ret = 0;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(documento);
			int id = documento.getId();
			session.getTransaction().commit();
			ret = id;
		}
		catch(Throwable e){
			CLogger.write("1", DocumentosAdjuntosDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<Documento> getDocumentos(Integer idObjeto, Integer idTipoObjeto){
		List<Documento> ret = new ArrayList<Documento>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		session.clear();
		try{
			Query<Documento> criteria = session.createQuery("FROM Documento d where d.estado=1 and d.idObjeto=:idObjeto and d.idTipoObjeto=:idTipoObjeto ORDER BY d.fechaCreacion", Documento.class);
			criteria.setParameter("idObjeto", idObjeto);
			criteria.setParameter("idTipoObjeto", idTipoObjeto);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", Proyecto.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
	
	public static List<Documento> getDocumentoById(Integer idDocumento){
		List<Documento> ret = new ArrayList<Documento>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		session.clear();
		try{
			Query<Documento> criteria = session.createQuery("FROM Documento d where d.id=:id", Documento.class);
			criteria.setParameter("id", idDocumento);			
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("1", Proyecto.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}
	
	public static List<Documento> eliminarDocumentoAdjunto(Integer idDocumento){
		List<Documento> ret = new ArrayList<Documento>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<Documento> criteria = session.createQuery("FROM Documento d where d.id=:id", Documento.class);
			criteria.setParameter("id", idDocumento);
			ret =   criteria.getResultList();
			session.beginTransaction();
			session.delete(ret.get(0));
			session.getTransaction().commit();
		}
		catch(Throwable e){
			CLogger.write("4", ProyectoPropiedadValorDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
}
