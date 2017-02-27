package dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import pojo.ProgramaProyecto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class ProgramaProyectoDAO {
	
	public static boolean eliminarProgramaProyecto(ProgramaProyecto programaProyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			programaProyecto.setEstado(0);
			session.beginTransaction();
			session.update(programaProyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			CLogger.write("1", ProgramaProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static boolean eliminarTotalProgramaProyecto(ProgramaProyecto programaProyecto){
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(programaProyecto);
			session.getTransaction().commit();
			ret = true;
		}
		catch(Throwable e){
			e.printStackTrace();
			CLogger.write("2", ProgramaProyectoDAO.class, e);
		}
		finally{
			session.close();
		}
		return ret;
	}
	
	public static List<ProgramaProyecto> getProgramaProyectosPorPrograma (int idPrograma){
		List<ProgramaProyecto> ret = new ArrayList<ProgramaProyecto>();
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			Query<ProgramaProyecto> criteria = session.createQuery("select pp from ProgramaProyecto where pp.estado = 1 and pp.id.programaid = :idProg )", ProgramaProyecto.class);
			criteria.setParameter("idProg", idPrograma);
			ret =   criteria.getResultList();
		}
		catch(Throwable e){
			CLogger.write("3", ProgramaProyectoDAO.class, e);
		}
		finally{
			session.close();
		}

		return ret;
	}

}
