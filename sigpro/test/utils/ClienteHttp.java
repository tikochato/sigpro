package utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

public class ClienteHttp {
	
	public static String peticionHttp(String url, String parametros){
		HttpClient httpClient = HttpClientBuilder.create().build(); 
		String respuesta ="";
		try {

		    HttpPost request = new HttpPost(url);
		    StringEntity params =new StringEntity(parametros);
		    request.addHeader("content-type", "application/x-www-form-urlencoded");
		    request.setEntity(params);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            respuesta =httpClient.execute(request,responseHandler);

		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return respuesta;
	}
	
}
