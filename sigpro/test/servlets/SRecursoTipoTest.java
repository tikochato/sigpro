package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SRecursoTipoTest {


	String direccionServlet = "http://localhost:8080/SRecursoTipo";
	@Test
	public void getRecursotiposPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getRecursotiposPagina\", \"pagina\":\"0\", \"numerorecursostipo\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	
	@Test
	public void numeroRecursoTiposTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"numeroRecursoTipos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void guardarRecursotipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarRecursotipo\", \"esnuevo\":\"false\",\"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
	
	@Test
	public void borrarRecursoTipoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarRecursoTipo\",\"id\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");		
	}
}
