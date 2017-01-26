package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import utilities.CLogger;

public class ClienteHttp {
	
	public static String peticionHttp(String url, String parametros){
		String ret = "";
		try{
			URL Url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput (true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Accept-Encoding", "gzip");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.connect();  
	        DataOutputStream printout =  new DataOutputStream(conn.getOutputStream ());
	        printout.writeBytes(parametros);
	        String salida;
	        printout.flush ();
	        printout.close ();
	        BufferedReader entrada = null;
	        if ("gzip".equals(conn.getContentEncoding())) {
	        	entrada =new BufferedReader( new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
	        }else{
	        	entrada = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        }
	        StringBuffer response = new StringBuffer();  
	        
	        while ((salida = entrada.readLine()) != null) {  
	         response.append(salida);  
	        }  
	        entrada.close();  
	        ret = response.toString();
		}catch(Exception e){
			CLogger.write("1",ClienteHttp2.class,e);
		}		
		return ret;
	
	}
	
}
