package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Utils {

	public static boolean isNullOrEmpty(String cadena) {
		if (cadena == null)
			return true;

		return cadena.isEmpty();
	}

	public static Map<String, String> getParams(HttpServletRequest request) {

		Map<String, String> map = null;

		try {

			request.setCharacterEncoding("UTF-8");

			String str;
			BufferedReader br = request.getReader();

			StringBuilder sb = new StringBuilder();

			while ((str = br.readLine()) != null) {
				sb.append(str);
			}

			map = getParams(sb.toString());

		} catch (Exception e) {

		}

		return map;
	}

	public static Map<String, String> getParams(String json) {

		Map<String, String> map = null;

		try {
			Gson gson = new Gson();

			Type type = new TypeToken<Map<String, String>>() {
			}.getType();

			map = gson.fromJson(json, type);

		} catch (Exception e) {

		}

		return map;
	}

	public static void writeJSon(HttpServletResponse response, String jsonText) throws IOException {
		response.setContentType("application/json");
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

		OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);

		gz.write(jsonText.getBytes("UTF-8"));
		gz.close();
		output.close();

	}

	public static String getJSonString(String nombre, List<?> objetos) {

		String jsonText = new GsonBuilder().serializeNulls().create().toJson(objetos);

		jsonText = String.join("", "\"" + nombre + "\":", jsonText);

		return jsonText;
	}
	
	public static String getJSonString(String nombre, Object objeto) {

		String jsonText = new GsonBuilder().serializeNulls().create().toJson(objeto);

		jsonText = String.join("", "\"" + nombre + "\":", jsonText);

		return jsonText;
	}

	public static Integer String2Int(String value) {

		return String2Int(value, 0);

	}

	public static Integer String2Int(String value, Integer defaultValue) {

		try {

			Integer val = Integer.parseInt(value);

			return val;

		} catch (Exception e) {

			return defaultValue;
		}

	}

	public static Long String2Long(String value) {

		return String2Long(value, 0l);

	}

	public static Long String2Long(String value, Long defaultValue) {
		try {
			Long val = Long.parseLong(value);
			return val;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Integer String2Boolean(String value, Integer defaultValue) {
		try {
			if(value!=null && value.compareTo("true")==0)
				return 	1;
			else
				return 0;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static BigDecimal String2BigDecimal(String value, BigDecimal defaultValue) {
		try {
			BigDecimal val = new BigDecimal(value);
			return val;

		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (date != null)
			return sdf.format(date);
		return "";
	}
	
	public static String formatDateHour(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
		if (date != null)
			return sdf.format(date);
		return "";
	}
	
	public static Date dateFromString(String date){
		Date rdate=null;
		if(date!=null && date.length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				rdate = sdf.parse(date);
			} catch (ParseException e) {
				
			}
		}
		return rdate;
	}
	
	public static Timestamp stringToTimestamp(String date){
		Timestamp ret=null;
		if(date!=null && date.length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
			try {
				ret = new Timestamp(sdf.parse(date).getTime());
			} catch (ParseException e) {
				ret = null;
			}
		}
		return ret;
	}
	
	public static Integer getParameterInteger(Map<String, String> map,String name){
		Integer ret = null;
		if(name!=null && name.length()>0 && map!=null){
			ret = (map.get(name)!=null && map.get(name).length()>0) ? Integer.parseInt(map.get(name)) : null;
		}
		return ret;
	}
	
	public static String formatDateHour24(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
		if (date != null)
			return sdf.format(date);
		return "";
	}

}
