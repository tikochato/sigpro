package servlets;

import static org.junit.Assert.*;

import org.junit.Test;
import utils.ClienteHttp;
import utils.DecodificadorJson;
public class SUsuarioTest {

	
	@Test
	public void getUsuarioTest(){
		String resultado  = ClienteHttp.peticionHttp("http://localhost:8080/SUsuario", "{\"accion\":\"getUsuarios\", \"pagina\":\"1\" ,  \"numeroUsuarios\":\"1\"}");
		System.out.println(resultado);
		assertEquals(DecodificadorJson.decodificarObjeto(resultado, "success"), "true");

	}
	
	/*@Test
	public void getTotalUsuarioTest(){
		String resultado;
	}*/
}
