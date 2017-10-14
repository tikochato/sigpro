package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Weeks;

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
	
	public static Date dateFromStringCeroHoras(String date){
		Calendar rdate=null;
		if(date!=null && date.length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				rdate = Calendar.getInstance();
				rdate.setTime(sdf.parse(date));
				rdate.set(Calendar.HOUR_OF_DAY, 0);
				rdate.set(Calendar.MINUTE, 0);
				rdate.set(Calendar.SECOND, 0);
				rdate.set(Calendar.MILLISECOND, 0);
			} catch (ParseException e) {
				
			}
		}
		return rdate.getTime();
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
	
	public static Date stringToDateZ(String date){
		Date ret=null;
		if(date!=null && date.length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
			try {
				ret = sdf.parse(date);
			} catch (ParseException e) {
				ret = null;
			}
		}
		return ret;
	}
	
	public static Date stringToDate(String date){
		Date ret=null;
		if(date!=null && date.length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				ret = sdf.parse(date);
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
	
	public static int getWorkingDays(DateTime fecha_inicio, DateTime fecha_fin) {
		int fecha_inicio_dia_semana = fecha_inicio.getDayOfWeek();
	    int fecha_fin_dia_semana = fecha_fin.getDayOfWeek();

	    int dias_sin_fines_de_semana = 5 * Weeks.weeksBetween(
	            fecha_inicio.withDayOfWeek(DateTimeConstants.MONDAY), fecha_fin).getWeeks();

	    if (fecha_inicio_dia_semana == DateTimeConstants.SUNDAY) {
	    	fecha_inicio_dia_semana = DateTimeConstants.SATURDAY;
	    }
	    if (fecha_fin_dia_semana == DateTimeConstants.SUNDAY) {
	    	fecha_fin_dia_semana = DateTimeConstants.SATURDAY;
	    }

	    return dias_sin_fines_de_semana - (fecha_inicio_dia_semana - fecha_fin_dia_semana);
	}
	
	public static Date setDateCeroHoras(Date fecha){
		Calendar cfecha = Calendar.getInstance();
		cfecha.setTime(fecha);
		cfecha.set(Calendar.HOUR_OF_DAY, 0);
		cfecha.set(Calendar.MINUTE, 0);
		cfecha.set(Calendar.SECOND, 0);
		cfecha.set(Calendar.MILLISECOND, 0);
		return cfecha.getTime();
		
	}

}
