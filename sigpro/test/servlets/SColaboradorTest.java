package servlets;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import utils.ClienteHttp;
import utils.DecodificadorJson;
public class SColaboradorTest {
	String direccionServlet = "http://localhost:8080/SColaborador";
	@Test
	public void ListarTest() throws UnsupportedEncodingException{
		String respuesta= ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar\", \"pagina\":1, \"registros\":1}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
	
	/*@Test
	public void crearTest(){
		String respuesta= ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"crear\", \"primerNombre\":\"unit_test\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");
	}*/
}
