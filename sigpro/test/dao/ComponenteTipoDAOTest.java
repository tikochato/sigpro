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
		assertNotNull(ComponenteTipoDAO.getComponenteTiposPagina(1, 1,"","","","",""));
	}
	
	@Test
	public void getTotalComponenteTipo(){
		assertNotNull(ComponenteTipoDAO.getTotalComponenteTipo("","",""));
	}

}
