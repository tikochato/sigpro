package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SDesembolsoTipoTest {

	String direccionServlet = "http://localhost:8080/SDesembolsoTipo";
	@Test
	public void getDesembolsotiposPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getDesembolsotiposPagina\" ,\"pagina\":\"0\", \"numerodesembolsotipos\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarDesembolsoTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarDesembolsoTipo\" ,\"esnuevo\":\"false\", \"numerodesembolsotipos\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	
	@Test
	public void borrarDesembolsoTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarDesembolsoTipo\" ,\"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void numeroDesembolsoTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroDesembolsoTipo\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}

	
	
}
