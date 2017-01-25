package utils;

import java.io.*;
import java.net.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import utilities.CLogger;

public class ClienteHttp2  {
	public static String peticionHttp(String url, String parametros){
		
		String ret = "";
		try{
			URL Url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput (true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.connect();  
	        DataOutputStream printout =  new DataOutputStream(conn.getOutputStream ());
	        //printout.writeBytes(URLEncoder.encode(jsonParam.toString(),"UTF-8"));
	        printout.writeBytes(parametros);
	        //printout.flush ();
	        //printout.close ();
	        String output;
	        System.out.println(conn.getInputStream().toString());
	        /*BufferedReader br = new BufferedReader(new InputStreamReader(
	                (conn.getInputStream())));*/
	        System.out.println("Output from Server .... \n");
	        
	        /*while ((output = br.readLine()) != null) {
	            System.out.println(output);
	        }*/
	        printout.flush ();
	        printout.close ();
	        BufferedReader in = new BufferedReader(  
	                new InputStreamReader(conn.getInputStream()));   
	        StringBuffer response = new StringBuffer();  
	        
	        while ((output = in.readLine()) != null) {  
	         response.append(output);  
	        }  
	        in.close();  
	        System.out.println(response.toString());  
	        ret= response.toString();
		}catch(Exception e){
			CLogger.write("1",ClienteHttp2.class,e);
		}
		
        
		return ret;
	}
}
