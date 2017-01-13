package dao;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Test;
import pojo.EstadoTabla;
import pojo.EstadoTablaId;
import utilities.CHibernateSession;

public class EstadoTablaDAOTest {

	@Test
	public void testSaveEstadoTabla() {
		EstadoTablaId estadotablaid = new EstadoTablaId("admin","unit_test");
		EstadoTabla estadotabla = new EstadoTabla();
		estadotabla.setId(estadotablaid);
		estadotabla.setValores("unit_test");
		assertEquals(EstadoTablaDAO.saveEstadoTabla(estadotabla), true );
	}

	@Test
	public void testGetStadoTabla() {
		String resultado = EstadoTablaDAO.getStadoTabla("admin", "unit_test");
		assertEquals(resultado,"unit_test");
	}
	@AfterClass
	public static void cleanData(){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			EstadoTablaId estadoTablaId = new EstadoTablaId("admin","unit_test");
			EstadoTabla estadotabla = (EstadoTabla) session.get(EstadoTabla.class,estadoTablaId);
			session.beginTransaction();
			session.delete(estadotabla);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}

}
