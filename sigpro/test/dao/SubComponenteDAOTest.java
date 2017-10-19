package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.Subcomponente;

public class SubComponenteDAOTest {
	
	@Test
	public void getSubComponentesTest(){
		assertNotNull(SubComponenteDAO.getSubComponentes(""));
	} 
	
	@Test
	public void getSubComponentePorIdTest(){
		assertNull(SubComponenteDAO.getSubComponentePorId(1, ""));
	}
	
	@Test
	public void guardarSubComponenteTest(){
		assertNotNull(SubComponenteDAO.guardarSubComponente(new Subcomponente(), true));
	}
	
	@Test
	public void eliminarSubComponenteTest(){
		assertNotNull(SubComponenteDAO.eliminarSubComponente(new Subcomponente()));
	}
	
	@Test
	public void eliminarTotalSubComponenteTest(){
		assertNotNull(SubComponenteDAO.eliminarTotalSubComponente(new Subcomponente()));
	}
	
	@Test
	public void getSubComponentesPaginaTest(){
		assertNotNull(SubComponenteDAO.getSubComponentesPagina(1, 1,""));
	}
	
	@Test
	public void getTotalSubComponentesTest(){
		assertEquals(SubComponenteDAO.getTotalSubComponentes("").getClass(), Long.class);
	}

	@Test
	public void getSubComponentesPaginaPorProyectoTest(){
		assertNotNull(SubComponenteDAO.getSubComponentesPaginaPorProyecto(0,0,0,"","","","","",""));
	}
	
	@Test
	public void getTotalSubComponentesPorProyecto(){
		assertNotNull(SubComponenteDAO.getTotalSubComponentesPorProyecto(0,"","","",""));
	}
}
