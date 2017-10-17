package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.Componente;

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
		assertNotNull(SubComponenteDAO.guardarSubComponente(new Componente(), true));
	}
	
	@Test
	public void eliminarSubComponenteTest(){
		assertNotNull(SubComponenteDAO.eliminarSubComponente(new Componente()));
	}
	
	@Test
	public void eliminarTotalSubComponenteTest(){
		assertNotNull(SubComponenteDAO.eliminarTotalSubComponente(new Componente()));
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
