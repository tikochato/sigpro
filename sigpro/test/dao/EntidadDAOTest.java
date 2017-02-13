package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntidadDAOTest {
	
	@Test
	public void getEntidadesTest(){
		assertNotNull(EntidadDAO.getEntidades());
	}
	
	@Test
	public void getEntidadTest(){
		assertEquals(EntidadDAO.getEntidad(0),null);
	}
	
	@Test
	public void guardarEntidadTest(){
		assertEquals(EntidadDAO.guardarEntidad(0, null, null),false);
	}
	
	@Test
	public void actualizarEntidadTest(){
		assertEquals(EntidadDAO.actualizarEntidad(0, null),false);
	}
	
	@Test
	public void getEntidadesPaginaTest(){
		assertNotNull(EntidadDAO.getEntidadesPagina(1, 1,"","","","",""));
	}
	
	@Test
	public void getJsonEntidadesTest(){
		assertEquals(EntidadDAO.getJsonEntidades(0, 1,"","","","","").getClass(),String.class);
	}
	
	@Test
	public void getTotalEntidadesTest(){
		assertEquals(EntidadDAO.getTotalEntidades("","","").getClass(), Long.class);
	}
}
