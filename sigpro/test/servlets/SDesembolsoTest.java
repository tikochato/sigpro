package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SDesembolsoTest {
	String direccionServlet = "http://localhost:8080/SDesembolso";
	@Test
	public void getDesembolsosPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getDesembolsosPagina\" ,\"pagina\":\"0\", \"numerodesembolsos\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getDesembolsosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getDesembolsoss\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void guardarDesembolsoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarDesembolso\" , \"esnuevo\":\"false\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void borrarDesembolsoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"borrarDesembolso\" , \"id\":\"0\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
}
