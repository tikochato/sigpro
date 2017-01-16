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
		assertNotNull(DesembolsoTipoDAO.getDesembolosTipoPorId(0).getClass());
	}
	
	@Test
	public void guardarDesembolsoTipoTest(){
		//DesembolsoTipo desembolsoTipo = new DesembolsoTipo("unit_test", "unit_test",1, new HashSet<Desembolso>(0));
		assertEquals(DesembolsoTipoDAO.guardarDesembolsoTipo(new DesembolsoTipo()),false);
	}
	
	@Test
	public void eliminarDesembolsoTipoTest(){
		//DesembolsoTipo desembolsoTipo = new DesembolsoTipo("unit_test", "unit_test",1, new HashSet<Desembolso>(0));
		assertEquals(DesembolsoTipoDAO.eliminarDesembolsoTipo(new DesembolsoTipo()),false);
	}
	
	@Test
	public void  getDesembolsoTiposPagina(){
		assertNotNull(DesembolsoTipoDAO.geDesembolsoTiposPagina(1,1));
	}
	
}
