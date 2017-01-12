package dao;

import static org.junit.Assert.*;

import java.awt.List;
import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pojo.DatoTipo;
import utilities.CHibernateSession;
import utilities.TestSetUp;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class DatoTipoDAOTest {
	private static Configuration config;
    private static SessionFactory factory;	
    
	@BeforeClass
	public static void before(){
		config = new Configuration();
        config.configure(new File("src/hibernate_tmp.cfg.xml"));
        factory = config.buildSessionFactory();
        TestSetUp.setTestState();
        CHibernateSession.changeEnvinroment(factory);
	}
	
	@Test
	public void getDatoTipoTest(){
		assertEquals(DatoTipoDAO.getDatoTipo(1).getClass(),DatoTipo.class);
	}
	
	@Test
	public void guardarTest(){
		assertEquals(DatoTipoDAO.guardar(1000, "unit_test", "unit_test"),true);
	}
	
	
	@Test
	public void getJsonComboTest(){
		assertNotNull(DatoTipoDAO.getJsonCombo());
	}
	
	@Test
	public void getJsonTest(){
		assertNotNull(DatoTipoDAO.getJson());
	}
	
	@Test
	public void getDatoTiposTest(){
		assertNotNull(DatoTipoDAO.getDatoTipos());
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(DatoTipoDAO.getTotal().getClass(),Long.class);
	}
	@Test
	public void getPaginaTest(){
		assertNotNull(DatoTipoDAO.getPagina(1, 1));
	}
	
	@Test
	public void actualizarTest(){
		assertEquals(DatoTipoDAO.actualizar(1, "texto",""), true);
	}
	
	@After
	public void cleanData(){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<DatoTipo> criteria = builder.createQuery(DatoTipo.class);
			Root<DatoTipo> root = criteria.from(DatoTipo.class);
			criteria.where( builder.and(builder.equal(root.get("nombre"), "unit_test")));
			DatoTipo datoTipoTmp =(DatoTipo) session.createQuery( criteria ).getSingleResult();
			session.beginTransaction();
			session.delete(datoTipoTmp);
			session.getTransaction().commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	
}
