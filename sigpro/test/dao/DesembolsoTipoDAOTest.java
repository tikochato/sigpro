package dao;

import static org.junit.Assert.*;

import org.junit.Test;
import pojo.DesembolsoTipo;

public class DesembolsoTipoDAOTest {

	
	@Test
	public void getTotalDesenbolseTipoTest(){
		assertNotNull(DesembolsoTipoDAO.getTotalDesembolsoTipo());
	}
	
	@Test
	public void getDesembolosTipoPorIdTest(){
		assertNotNull(DesembolsoTipoDAO.getDesembolosTipoPorId(1).getClass());
	}
	
	@Test
	public void guardarDesembolsoTipoTest(){
		assertEquals(DesembolsoTipoDAO.guardarDesembolsoTipo(new DesembolsoTipo()),false);
	}
	
	@Test
	public void eliminarDesembolsoTipoTest(){
		assertEquals(DesembolsoTipoDAO.eliminarDesembolsoTipo(new DesembolsoTipo()),false);
	}
	
	@Test
	public void  getDesembolsoTiposPagina(){
		assertNotNull(DesembolsoTipoDAO.geDesembolsoTiposPagina(1,1));
	}
	
}
