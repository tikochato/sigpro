package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.HitoResultado;

public class HitoResultadoDAOTest {
	
	
	@Test
	public void getHitoResultadoActivoPorHitoTest(){
		assertEquals(HitoResultadoDAO.getHitoResultadoActivoPorHito(0),null);
	}
	
	@Test
	public void getHitoResultadoPorIdTest(){
		assertEquals(HitoResultadoDAO.getHitoResultadoPorId(0),null);
	}
	
	@Test
	public void guardarHitoResultadoTest(){
		assertEquals(HitoResultadoDAO.guardarHitoResultado(new HitoResultado()),false);
	}
	
	@Test
	public void eliminarHitoResultadoTest(){
		assertEquals(HitoResultadoDAO.eliminarHitoResultado(new HitoResultado()),false);
	}
	
	@Test
	public void eliminarTotalHitoResultadoTest(){
		assertNotNull(HitoResultadoDAO.eliminarTotalHitoResultado(new HitoResultado()));
	}
	
	
}
