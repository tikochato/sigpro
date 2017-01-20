package dao;

import static org.junit.Assert.*;
import org.junit.Test;
import pojo.HitoTipo;

public class HitoTipoDAOTest {
	
	@Test
	public void getHitoTiposTest(){
		assertNotNull(HitoTipoDAO.getHitoTipos());
	}
	
	@Test
	public void getHitoTipoPorIdTest(){
		assertEquals(HitoTipoDAO.getHitoTipoPorId(1).getClass(),HitoTipo.class);
	}
	
	@Test
	public void guardarHitoTipoTest(){
		assertEquals(HitoTipoDAO.guardarHitoTipo(new HitoTipo()),false);
	}
	
	@Test
	public void  eliminarHitoTipoTest(){
		assertEquals(HitoTipoDAO.eliminarHitoTipo(new HitoTipo()),false);
	}
	
	@Test
	public void eliminarTotalHitoTipoTest(){
		assertNotNull(HitoTipoDAO.eliminarTotalHitoTipo( new HitoTipo()));
	}
	
	@Test
	public void getHitoTiposPaginaTest(){
		assertNotNull(HitoTipoDAO.getHitoTiposPagina(1, 1));
	}
	
	@Test
	public void getTotalHitoTiposTest(){
		assertEquals(HitoTipoDAO.getTotalHitoTipos().getClass(),Long.class);
	}

}
