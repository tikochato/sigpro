package dao;

import static org.junit.Assert.*;

import org.junit.Test;

import pojo.ComponenteTipo;

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
		assertEquals(ComponenteTipoDAO.guardarComponenteTipo(new ComponenteTipo()),false);
	}
	
	@Test
	public void eliminarComponenteTipoTest(){
		assertEquals(ComponenteTipoDAO.eliminarComponenteTipo(new ComponenteTipo()),false);
	}
	
	@Test
	public void eliminarTotalComponenteTipo(){
		assertNotNull(ComponenteTipoDAO.eliminarTotalComponenteTipo(new ComponenteTipo()));
	}
	
	@Test
	public void getComponenteTiposPagina(){
		assertNotNull(ComponenteTipoDAO.getComponenteTiposPagina(1, 1));
	}
	
	@Test
	public void getTotalComponenteTipo(){
		assertNotNull(ComponenteTipoDAO.getTotalComponenteTipo());
	}
	/*@AfterClass
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
	}*/

}
