package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.ProyectoTipo;

public class ProyectoTipoDAOTest {

	
	@Test
	public void getProyectoTiposTest(){
		assertNotNull(ProyectoTipoDAO.getProyectoTipos());
	}
	
	@Test
	public void getProyectoTipoPorIdTest(){
		assertNull(ProyectoTipoDAO.getProyectoTipoPorId(1));
	}
	
	@Test
	public void guardarProyectoTipoTest(){
		assertEquals(ProyectoTipoDAO.guardarProyectoTipo(new ProyectoTipo()),false);
	}
	
	@Test
	public void getProyectosTipoPaginaTest(){
		assertNotNull(ProyectoTipoDAO.getProyectosTipoPagina(1, 1,"", "", "", "", ""));
	}
	
	
	@Test 
	public void getTotalProyectoTiposTest(){
		assertEquals(ProyectoTipoDAO.getTotalProyectoTipos( "", "", "").getClass(),Long.class);
	}
	
	@Test
	public void eliminarProyectoTipo(){
		assertEquals(ProyectoTipoDAO.eliminarProyectoTipo(new ProyectoTipo()),false);
	}

}
