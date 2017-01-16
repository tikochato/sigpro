package dao;

import static org.junit.Assert.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Test;
import pojo.ProductoTipo;
import utilities.CHibernateSession;

public class ProductoTipoDAOTest {
	
	
	@Test
	public void guardarTest(){
		assertNotNull(ProductoTipoDAO.guardar(1, "unit_test", "unit_test", "unit_test", "admin"));
	}
	
	@Test
	public void getProductoTipoTest(){
		assertNotNull(ProductoTipoDAO.getProductoTipo(2));
	}
	
	@Test
	public void actualizarTest(){
		assertNotNull(ProductoTipoDAO.actualizar(1000, "unit_test", "unit_test1", "unit_test", "admin"));
	}
	
	
	@Test
	public void  getPaginaTest(){
		assertNotNull(ProductoTipoDAO.getPagina(1, 1));
	}
	
	@Test
	public void getJsonTest(){
		assertEquals(ProductoTipoDAO.getJson(1, 1).getClass(),String.class);
	}
	
	@Test
	public void eliminarTest(){
		assertNotNull(ProductoTipoDAO.eliminar(1000, "admin"));
	}
	
	@Test
	public void getTotalTest(){
		assertEquals(ProductoTipoDAO.getTotal().getClass(),Long.class);
	}
	@AfterClass
	public static void cleanData(){
		Session session = CHibernateSession.getSessionFactory().openSession();
		try{
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<ProductoTipo> criteria = builder.createQuery(ProductoTipo.class);
			Root<ProductoTipo> root = criteria.from(ProductoTipo.class);
			criteria.where( builder.and(builder.equal(root.get("nombre"), "unit_test")));
			ProductoTipo productoTipoTmp =(ProductoTipo) session.createQuery( criteria ).getSingleResult();
			session.beginTransaction();
			session.delete(productoTipoTmp);
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
}
