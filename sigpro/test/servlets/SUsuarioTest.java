package servlets;

import static org.junit.Assert.*;

import org.junit.Test;
import utils.ClienteHttp;
import utils.DecodificadorJson;
public class SUsuarioTest {

	String direccionServlet = "http://localhost:8080/SUsuario";
	@Test
	public void getUsuarioTest(){
		String resultado  = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"getUsuarios\", \"pagina\":\"1\" ,  \"numeroUsuarios\":\"1\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "true");

	}
	
	@Test
	public void getTotalUsuarioTest(){
		String resultado = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"registroUsuario\", \"usuario\":\"\" ,  \"password\":\"\" ,  \"email\":\"\"  ,  \"permisos\":\"[]\"    }");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "false");
	}
	
	@Test
	public void actualizarPermisosTest(){
		String resultado = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"actualizarPermisos\", \"usuario\":\"unit_test\" ,  \"permisosNuevos\":\"[]\" ,    \"permisosEliminados\":\"[]\"    }");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "false");
	}
	
	@Test 
	public void eliminarUsuarioTest(){
		String resultado = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"eliminarUsuario\", \"usuario\":\"unit_test\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "false");
	}
	
	@Test
	public void obtenerPermisosTest(){
		String resultado = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"obtenerPermisos\", \"usuario\":\"unit_test\"}");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "true");
	}
	@Test
	public void editarUsuarioTest(){
		String resultado = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"editarUsuario\", \"usuario\":\"unit_test\" , \"email\":\"unit_test.\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "false");
	}
	
	@Test
	public void cambiarPasswordTest(){
		String resultado = ClienteHttp.peticionHttp(direccionServlet, "{\"accion\":\"cambiarPassword\", \"usuario\":\"unit_test\" , \"password\":\"unit_test.\" }");
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "false");
	}
	
	
}
