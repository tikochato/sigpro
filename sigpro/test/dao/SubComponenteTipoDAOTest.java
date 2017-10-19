package dao;

import static org.junit.Assert.*;

import org.junit.Test;

import pojo.ComponenteTipo;

public class SubComponenteTipoDAOTest {
	

	
	@Test
	public void getComponenteTiposTest(){
		assertNotNull(SubComponenteTipoDAO.getSubComponenteTipos());
	}
	
	@Test
	public void getComponenteTipoPorIdTest(){
		assertEquals(SubComponenteTipoDAO.getSubComponenteTipoPorId(1),null);
	}
	
	@Test
	public void guardarComponenteTipoTest(){
		assertEquals(SubComponenteTipoDAO.guardarSubComponenteTipo(new ComponenteTipo()),false);
	}
	
	@Test
	public void eliminarComponenteTipoTest(){
		assertEquals(SubComponenteTipoDAO.eliminarComponenteTipo(new ComponenteTipo()),false);
	}
	
	@Test
	public void eliminarTotalComponenteTipo(){
		assertNotNull(SubComponenteTipoDAO.eliminarTotalComponenteTipo(new ComponenteTipo()));
	}
	
	@Test
	public void getComponenteTiposPagina(){
		assertNotNull(SubComponenteTipoDAO.getComponenteTiposPagina(1, 1,"","","","",""));
	}
	
	@Test
	public void getTotalComponenteTipo(){
		assertNotNull(SubComponenteTipoDAO.getTotalComponenteTipo("","",""));
	}

}
