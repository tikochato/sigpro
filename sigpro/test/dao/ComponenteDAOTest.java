package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.Componente;

public class ComponenteDAOTest {
	
	@Test
	public void getComponentesTest(){
		assertNotNull(ComponenteDAO.getComponentes(""));
	} 
	
	@Test
	public void getComponentePorIdTest(){
		assertNull(ComponenteDAO.getComponentePorId(1, ""));
	}
	
	@Test
	public void guardarComponenteTest(){
		assertNotNull(ComponenteDAO.guardarComponente(new Componente(), true));
	}
	
	@Test
	public void eliminarComponenteTest(){
		assertNotNull(ComponenteDAO.eliminarComponente(new Componente()));
	}
	
	@Test
	public void eliminarTotalComponenteTest(){
		assertNotNull(ComponenteDAO.eliminarTotalComponente(new Componente()));
	}
	
	@Test
	public void getComponentesPaginaTest(){
		assertNotNull(ComponenteDAO.getComponentesPagina(1, 1,""));
	}
	
	@Test
	public void getTotalComponentesTest(){
		assertEquals(ComponenteDAO.getTotalComponentes("").getClass(), Long.class);
	}

	@Test
	public void getComponentesPaginaPorProyectoTest(){
		assertNotNull(ComponenteDAO.getComponentesPaginaPorProyecto(0,0,0,"","","","","",""));
	}
	
	@Test
	public void getTotalComponentesPorProyecto(){
		assertNotNull(ComponenteDAO.getTotalComponentesPorProyecto(0,"","","",""));
	}
}
