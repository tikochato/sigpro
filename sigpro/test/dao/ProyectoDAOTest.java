package dao;


import static org.junit.Assert.*;

import org.junit.Test;

import pojo.Proyecto;
public class ProyectoDAOTest {
	
	@Test
	public void getProyectosTest(){
		assertNotNull(ProyectoDAO.getProyectos(""));
	}
	
	@Test
	public void guardarProyectoTest(){
		assertEquals(ProyectoDAO.guardarProyecto(new Proyecto()),false);
	}
	
	@Test
	public void getProyectoPorIdTest(){
		assertEquals(ProyectoDAO.getProyectoPorId(0,""),null);
	}
	
	@Test
	public void eliminarProyectoTest(){
		assertEquals(ProyectoDAO.eliminarProyecto(new Proyecto()),false);
	}
	
	@Test
	public void getTotalProyectosTest(){
		assertEquals(ProyectoDAO.getTotalProyectos("","","","").getClass(),Long.class);
	}
	
	@Test
	public void getProyectosPaginaTest(){
		assertNotNull(ProyectoDAO.getProyectosPagina(0,0,"","","","","",""));
	}
	
	@Test
	public void getProyectosPaginaDisponiblesTest(){
		assertNotNull(ProyectoDAO.getProyectosPaginaDisponibles(0,0,"","","","","",""));
	}
	
	@Test
	public void getTotalProyectosDisponiblesTest(){
		assertEquals(ProyectoDAO.getTotalProyectosDisponibles("","","","").getClass(), Long.class);
	}
	@Test
	public void getProyectosPorProgramaTest(){
		assertNotNull(ProyectoDAO.getProyectosPorPrograma(0));
	}

}
