package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.codec.Base64;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.ReporteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.EstadoInforme;
import pojo.InformePresupuesto;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SReporte")
public class SReporte extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	class duracionFecha{
		int id;
		int duracion;
		String fecha_inicial;
	}
	
    public SReporte() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
		
		if(accion.equals("getCargaTrabajoPrestamo")){
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
			Integer idProyecto = Utils.String2Int(map.get("idPrestamo"),0);
			Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
			Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
			Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
			
			List<?> actividades = ReporteDAO.getActividadesCargaTrabajo(idProyecto, idComponente, idProducto, idSubProducto);
			
			
			
			/*
			List<?> actividades_proceso = ReporteDAO.getCargaTrabajo(0,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
			List<?> actividades_atrasadas = ReporteDAO.getCargaTrabajo(1,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
			
			String JsonProceso = Utils.getJSonString("actividadesProceso", actividades_proceso);
			String JsonAtrasadas = Utils.getJSonString("actividadesAtrasadas", actividades_atrasadas);
			
			response_text = JsonProceso + "," + JsonAtrasadas;*/
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("exportarExcel")){
			String reporte = map.get("reporte");
			String nombreInforme = "";
			
			Map<String,Object[]> datos = new HashMap<>();
			if (reporte.equals("cargaTrabajo")){
				nombreInforme = "Informe de recursos (Carga de Trabajo)";
				Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
				Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
				Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
				Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
				String mes = map.get("mes");
				
				List<?> actividades_proceso = ReporteDAO.getCargaTrabajo(0,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
				List<?> actividades_atrasadas = ReporteDAO.getCargaTrabajo(1,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
				
				datos.put("0", new Object[] {"Responsable", "Actividades Atrasadas", "Actividades a Cumplir " + mes});
				Object[] temp = new Object []{};
				
				for (int i=0; i< actividades_proceso.size(); i++){

				}
			}
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private void exportarExcel(Map<String,Object[]> datos, String nombreInforme, String usuario, HttpServletResponse response){
		try{
			CExcel excel = new CExcel("Reporte",false);
			String path = excel.ExportarExcel(datos, nombreInforme, usuario);
			File file=new File(path);
			if(file.exists()){
				FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
		        	
		        }
		        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		        
		        int readByte = 0;
		        byte[] buffer = new byte[2024];

	            while(true)
	            {
	                readByte = is.read(buffer);
	                if(readByte == -1)
	                {
	                    break;
	                }
	                outByteStream.write(buffer);
	            }
	            
	            file.delete();
	            
	            is.close();
	            outByteStream.flush();
	            outByteStream.close();
	            
		        byte [] outArray = Base64.encode(outByteStream.toByteArray());
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; Informe_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		catch(Throwable e){
			CLogger.write("2", SReporte.class, e);
		}
	}
}
