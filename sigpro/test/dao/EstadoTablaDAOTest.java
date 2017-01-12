package dao;

import static org.junit.Assert.*;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import pojo.EstadoTabla;
import pojo.EstadoTablaId;
import utilities.CHibernateSession;
import utilities.TestSetUp;

public class EstadoTablaDAOTest {
	private static Configuration config;
    private static SessionFactory factory;	
   

	@Before
	public void setUp() throws Exception {
		config = new Configuration();
        config.configure(new File("src/hibernate_tmp.cfg.xml"));
        factory = config.buildSessionFactory();
        TestSetUp.setTestState();
        CHibernateSession.changeEnviroment(factory);
   	}

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

}
