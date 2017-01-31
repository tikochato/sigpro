package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SDatoTipoTest {

	String direccionServlet = "http://localhost:8080/SDatoTipo";
	@Test
	public void cargarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void cargarComboTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargarCombo\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
}
