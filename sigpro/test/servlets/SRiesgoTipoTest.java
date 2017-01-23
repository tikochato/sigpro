package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SRiesgoTipoTest {

	String direccionServlet = "http://localhost:8080/SRiesgoTipo";
	@Test
	public void getRiesgotiposPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRiesgotiposPagina\" ,\"pagina\":\"0\", \"numeroriesgostipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	@Test
	public void numeroRiesgoTiposTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRiesgoTipos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarRiesgotipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarRiesgotipo\" ,\"esnuevo\":\"false\", \"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void borrarRiesgoTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarRiesgoTipo\",\"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	
}
