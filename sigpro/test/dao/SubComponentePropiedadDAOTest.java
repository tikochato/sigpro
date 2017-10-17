package dao;

import static org.junit.Assert.*;
import org.junit.Test;

import pojo.ComponentePropiedad;
public class SubComponentePropiedadDAOTest {
	
	@Test
	public void getComponentePropiedadesPorTipoComponentePaginaTest(){
		assertNotNull(SubComponentePropiedadDAO.getSubComponentePropiedadesPorTipoComponentePagina(1, 1));
	}
	
	@Test
	public void getTotalComponentePropiedadesTest(){
		assertEquals(SubComponentePropiedadDAO.getTotalSubComponentePropiedades().getClass(),Long.class);
	}
	
	@Test
	public void getComponentePropiedadPaginaTotalDisponiblesTest(){
		assertNotNull(SubComponentePropiedadDAO.getSubComponentePropiedadPaginaTotalDisponibles(1, 1,"unit_test"));
	}
	
	@Test
	public void getComponentePropiedadPorIdTest(){
		assertNull(SubComponentePropiedadDAO.getSubComponentePropiedadPorId(0));
	}
	
	@Test
	public void guardarComponentePropiedadTest(){
		assertEquals(SubComponentePropiedadDAO.guardarSubComponentePropiedad(new ComponentePropiedad()), false);
	}
	
	@Test
	public void eliminarComponentePropiedadTest(){
		assertEquals(SubComponentePropiedadDAO.eliminarSubComponentePropiedad(new ComponentePropiedad()),false);
	}
	
	@Test
	public void eliminarTotalComponentePropiedadTest(){
		assertNotNull(SubComponentePropiedadDAO.eliminarTotalSubComponentePropiedad(new ComponentePropiedad()));
	}
	
	@Test
	public void getComponentePropiedadesPaginaTest(){
		assertNotNull(SubComponentePropiedadDAO.getSubComponentePropiedadesPagina(1, 1, "","","","",""));
	}
	
	@Test
	public void getTotalComponentePropiedad(){
		assertEquals(SubComponentePropiedadDAO.getTotalSubComponentePropiedad("","","").getClass(),Long.class);
	}

}
