package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SEntidadTest {

	String direccionServlet = "http://localhost:8080/SEntidad";
	@Test
	public void cargarTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cargar\" ,\"pagina\":\"0\", \"registros\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	@Test
	public void crearTest(){
		String respuesta = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"crear\" ,\"entidad\":\"\", \"nombre\":\"\" , \"abreviatura\":\"\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
	@Test
	public void actualizarTest(){
		String respuesta = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"actualizar\" ,\"entidad\":\"\", \"abreviatura\":\"\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void totalEntidadesTest(){
		String respuesta = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"totalEntidades\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");		
	}
	
}
