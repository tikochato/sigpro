package servlets;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import utils.ClienteHttp;
import utils.DecodificadorJson;
public class SColaboradorTest {

	@Test
	public void ListarTest() throws UnsupportedEncodingException{
		String respuesta= ClienteHttp.peticionHttp("http://localhost:8080/SColaborador", "{\"accion\":\"cargar\", \"pagina\":1, \"registros\":1}");
		assertEquals(DecodificadorJson.decodificarObjeto(respuesta, "success"), "true");
	}
}
