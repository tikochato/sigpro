package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.RiesgoTipo;

public class RiesgoTipoDAOTest {
	@Test
	public void getRiesgoTiposTest(){
		assertNotNull(RiesgoTipoDAO.getRiesgoTipos());
	}
	
	@Test
	public  void getRiesgoTipoPorIdTest(){
		assertNotNull(RiesgoTipoDAO.getRiesgoTipoPorId(1));
	}
	
	@Test
	public void guardarRiesgoTipoTest(){
		assertNotNull(RiesgoTipoDAO.guardarRiesgoTipo(new RiesgoTipo()));
	}
	
	@Test
	public void eliminarRiesgoTipoTest(){
		assertNotNull(RiesgoTipoDAO.eliminarRiesgoTipo(new RiesgoTipo()));
	}
	
	@Test
	public void eliminarTotalRiesgoTipoTest(){
		assertNotNull(RiesgoTipoDAO.eliminarTotalRiesgoTipo(new RiesgoTipo()));
	}
	
	@Test
	public void getRiesgoTiposPaginaTest(){
		assertNotNull(RiesgoTipoDAO.getRiesgoTiposPagina(1,1));
	}
	
	@Test
	public void getTotalRiesgoTipoTest(){
		assertEquals(RiesgoTipoDAO.getTotalRiesgoTipo().getClass(), Long.class);
	}

}
