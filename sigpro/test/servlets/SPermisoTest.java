package servlets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.ClienteHttp;
import utils.DecodificadorJson;

public class SPermisoTest {
	String direccionServlet = "http://localhost:8080/SPermiso";
	@Test
	public void guardarPermisoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"guardarPermiso\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "false");	
	}
	
	@Test
	public void editarPermisoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"editarPermiso\", \"nombre\":\"nombre\" , \"descripcion\":\"descripcion\"  , \"id\":\"0\"}");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void eliminarPermisoTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"eliminarPermiso\", \"id\":\"null\" }");
		assertEquals(respuesta, "");	
	}
	
	@Test
	public void getPermisosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getPermisos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getPermisosPaginaTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getPermisosPagina\", \"pagina\":\"0\" , \"numeroPermisos\":\"0\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	
	@Test
	public void getTotalPermisosTest(){
		String respuesta =ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getTotalPermisos\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");	
	}
	

}
