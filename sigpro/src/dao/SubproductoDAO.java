package dao;

import org.hibernate.Session;

import pojo.Subproducto;
import utilities.CHibernateSession;
import utilities.CLogger;

public class SubproductoDAO {
	public static boolean guardarSubproducto(Subproducto subproducto) {
		boolean ret = false;
		Session session = CHibernateSession.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(subproducto);
			session.getTransaction().commit();
			ret = true;
		} catch (Throwable e) {
			CLogger.write("3", ProductoDAO.class, e);
		} finally {
			session.close();
		}
		return ret;
	}

}
