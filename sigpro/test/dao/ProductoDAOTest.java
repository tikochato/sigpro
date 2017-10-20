package dao;

import org.junit.Test;

import pojo.Producto;

import static org.junit.Assert.*;

public class ProductoDAOTest {
	

	@Test
	public void getProductosTest(){
		assertNotNull(ProductoDAO.getProductos(""));
	}
	
	@Test
	public void getProductoPorId(){
		assertEquals(ProductoDAO.getProductoPorId(0,""), null);
	}
	
	@Test
	public void guardarProductoTest(){
		assertEquals(ProductoDAO.guardarProducto(new Producto(), true),false);
	}
	
	@Test
	public void eliminarProductoTest(){
		assertEquals(ProductoDAO.eliminarProducto(new Producto()),false);
	}
	
	@Test
	public void eliminarTotalProductoTest(){
		assertNotNull(ProductoDAO.eliminarTotalProducto(new Producto()));
	}
	
	@Test
	public void getProductosPaginaTest(){
		assertNotNull(ProductoDAO.getProductosPagina(1, 1,1,null, "", "", "","", "",""));
	}
	
	@Test
	public void getTotalProductosTest(){
		assertEquals(ProductoDAO.getTotalProductos(1,null,"", "","","").getClass(),Long.class);
	}
	
	@Test
	public void guardarTest(){
		//assertEquals(ProductoDAO.guardar("unit_test", "unit_test", 0, 0, 0, "unit_test", "[]", "admin"),false);
	}
	
	@Test
	public void actualizarTest(){
		//assertEquals(ProductoDAO.actualizar(1, "unit_test", "unit_test", 0, 0, 0, "[]", "unit_test", "admin"),false);
	}
	
	@Test
	public void eliminarTest(){
		assertEquals(ProductoDAO.eliminar(0, "unit_test"),false);
	}
	
	
}
