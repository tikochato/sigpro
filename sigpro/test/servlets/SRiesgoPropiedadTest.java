package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SRiesgoPropiedadTest {
	String direccionServlet = "http://localhost:8080/SRiesgoPropiedad";
	@Test
	public void getRiesgoPropiedadPaginaPorTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRiesgoPropiedadPaginaPorTipo\", \"pagina\":\"0\", \"idRiesgoTipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}

	@Test
	public void getRiesgoPropiedadPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRiesgoPropiedadPagina\", \"pagina\":\"0\", \"numeroriesgopropiedad\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void getRiesgoPropiedadesTotalDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRiesgoPropiedadesTotalDisponibles\", \"pagina\":\"0\", \"idspropiedades\":\"0\", \"numerocomponentepropiedad\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void numeroRiesgoPropiedadesDisponiblesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRiesgoPropiedadesDisponibles\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void numeroRiesgoPropiedadesTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRiesgoPropiedades\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void guardarRiesgoPropiedadTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarRiesgoPropiedad\", \"esnuevo\":\"false\",  \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
	
	@Test
	public void borrarComponentePropiedadTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarComponentePropiedad\",  \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}

	@Test
	public void getRiesgoPropiedadPorTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarComponentePropiedad\", \"idRiesgo\":\"0\", \"idRiesgoTipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
	

}
