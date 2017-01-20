package dao;


import static org.junit.Assert.*;
import org.junit.Test;

import pojo.Meta;

public class MetaDAOTest {
	
	@Test
	public void getMetasTest(){
		assertNotNull(MetaDAO.getMetas());
	}
	
	@Test
	public void getMetaPorIdTest(){
		assertEquals(MetaDAO.getMetaPorId(0),null);
	}
	
	@Test
	public void guardarMetaTest(){
		assertEquals(MetaDAO.guardarMeta(new Meta()),false);
	}
	
	@Test
	public void eliminarMetaTest(){
		assertEquals(MetaDAO.eliminarMeta(new Meta()),false);
	}
	
	@Test
	public void eliminarTotalMetaTest(){
		assertNotNull(MetaDAO.eliminarTotalMeta(new Meta()));
	}
	
	@Test
	public void getMetasPaginaTest(){
		assertNotNull(MetaDAO.getMetasPagina(1, 1, 1, 1));
	}
	
	@Test
	public void getTotalMetasTest(){
		assertEquals(MetaDAO.getTotalMetas().getClass(),Long.class);
	}
	

}
