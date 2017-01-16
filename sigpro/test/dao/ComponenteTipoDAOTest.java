package dao;

import static org.junit.Assert.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Test;

import pojo.ComponenteTipo;
import utilities.CHibernateSession;
import org.joda.time.DateTime;

public class ComponenteTipoDAOTest {
	
	@Test
	public void getComponenteTiposTest(){
		assertNotNull(ComponenteTipoDAO.getComponenteTipos());
	}
	
	@Test
	public void getComponenteTipoPorIdTest(){
		assertNotNull(ComponenteTipoDAO.getComponenteTipoPorId(1));
	}
	
	@Test
	public void guardarComponenteTipoTest(){
		ComponenteTipo componenteTipo = new ComponenteTipo("unit_test","admin", new DateTime().toDate(),1);
		assertNotNull(ComponenteTipoDAO.guardarComponenteTipo(componenteTipo));
	}
	
	@Test
	public void eliminarComponenteTipoTest(){
		ComponenteTipo componenteTipo = new ComponenteTipo("unit_test","admin", new DateTime().toDate(),1);
		assertNotNull(ComponenteTipoDAO.eliminarComponenteTipo(componenteTipo));
	}
	
	@Test
	public void eliminarTotalComponenteTipo(){
		ComponenteTipo componenteTipo = new ComponenteTipo("unit_test","admin", new DateTime().toDate(),1);
		assertNotNull(ComponenteTipoDAO.eliminarTotalComponenteTipo(componenteTipo));
	}
	
	@Test
	public void getComponenteTiposPagina(){
		assertNotNull(ComponenteTipoDAO.getComponenteTiposPagina(1, 1));
	}
	
	@Test
	public void getTotalComponenteTipo(){
		assertNotNull(ComponenteTipoDAO.getTotalComponenteTipo());
	}
	@AfterClass
	public static void cleanData(){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ComponenteTipo> criteria = builder.createQuery(ComponenteTipo.class);
			Root<ComponenteTipo> root = criteria.from(ComponenteTipo.class);
			criteria.where( builder.and(builder.equal(root.get("nombre"), "unit_test")));
			ComponenteTipo componenteTipoTmp =(ComponenteTipo) session.createQuery( criteria ).getSingleResult();
			session.beginTransaction();
			session.delete(componenteTipoTmp);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
	}

}
