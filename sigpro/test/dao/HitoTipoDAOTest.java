package dao;

import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Test;

import pojo.Hito;
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
		//HitoTipo hitoTipo = new HitoTipo("unit_test","unit_test",1, new HashSet<Hito>(0));
		//assertNotNull(HitoTipoDAO.guardarHitoTipo(hitoTipo));
	}
	
	@Test
	public void  eliminarHitoTipoTest(){
		//assertNotNull(HitoTipoDAO.eliminarHitoTipo( new HitoTipo("unit_test","unit_test",1, new HashSet<Hito>(0))));
	}
	
	@Test
	public void eliminarTotalHitoTipoTest(){
		//assertNotNull(HitoTipoDAO.eliminarTotalHitoTipo( new HitoTipo("unit_test","unit_test",1, new HashSet<Hito>(0))));
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
