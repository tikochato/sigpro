package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import dao.ReporteDAO;
import pojo.Actividad;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SReporte")
public class SReporte extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	class cargaTrabajo{
		int id;
		String responsable;
		int actividadesAtrasadas;
		int actividadesProceso;
	}
	
	class stActividades{
		int id;
		String nombre;
		String fechaInicio;
		String fechaFin;
		int porcentaje;
		String estado;
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
		
		if(accion.equals("getCargaTrabajoPrestamo")){
			Integer idProyecto = Utils.String2Int(map.get("idPrestamo"),0);
			Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
			Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
			Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
			
			List<?> actividades = ReporteDAO.getActividadesCargaTrabajo(idProyecto, idComponente, idProducto, idSubProducto);
			List<cargaTrabajo> atrasadas = new ArrayList<cargaTrabajo>();
			List<cargaTrabajo> proceso = new ArrayList<cargaTrabajo>();
			
			for(Object obj : actividades){
				cargaTrabajo temp1 = new cargaTrabajo();
				cargaTrabajo temp2 = new cargaTrabajo();
				
				Object[] row = (Object[]) obj;
				
				int idActividad = (Integer)row[0];
				int porcentajeAvance = (Integer)row[2];
				
				if (porcentajeAvance > 0 && porcentajeAvance < 100){
					Actividad actividad = ActividadDAO.getActividadPorId(idActividad, usuario);
					String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad,usuario).split(";");
					
					try{
						Date inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(fechaInicioFin[0]);
			            Date fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
			                    .parse(fechaInicioFin[1]);
			            Date hoy = new DateTime().toDate();
						
						if(hoy.after(inicio) && hoy.before(fin)){
							temp1.id = (Integer)row[3];
							temp1.responsable = (String)row[4];
							temp1.actividadesAtrasadas = 0;
							temp1.actividadesProceso = 1;
							proceso.add(temp1);
						} else if(hoy.after(fin)){
							temp2.id = (Integer)row[3];
							temp2.responsable = (String)row[4];
							temp2.actividadesAtrasadas = 1;
							temp2.actividadesProceso = 0;
							atrasadas.add(temp2);
						}
					}
					catch (Throwable e) {
			            e.printStackTrace();
			        }
				}
			}
			
			String JsonProceso = Utils.getJSonString("actividadesProceso", proceso);
			String JsonAtrasadas = Utils.getJSonString("actividadesAtrasadas", atrasadas);
			
			response_text = JsonProceso + "," + JsonAtrasadas;
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("getActividades")){
			int responsableId = Utils.String2Int(map.get("responsableId"));
			Integer idProyecto = Utils.String2Int(map.get("idPrestamo"),0);
			Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
			Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
			Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
			
			//List<Actividad> actividades = ReporteDAO.getActividadesResponsable(responsableId, usuario);
			List<stActividades> lstActividades = new ArrayList<stActividades>();
			List<?> actividades = ReporteDAO.getActividadesCargaTrabajo(idProyecto, idComponente, idProducto, idSubProducto);
			
			for(Object obj : actividades){
				Object[] row = (Object[]) obj;
				
				if((Integer)row[3] == responsableId){
					int idActividad = (Integer)row[0];
					int porcentajeAvance = (Integer)row[2];
				
					if (porcentajeAvance > 0 && porcentajeAvance < 100){
						Actividad actividad = ActividadDAO.getActividadPorId(idActividad, usuario);
						String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad, usuario).split(";");
						stActividades temp = new stActividades();
						
						try{
							Date inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
				                    .parse(fechaInicioFin[0]);
				            Date fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
				                    .parse(fechaInicioFin[1]);
				            Date hoy = new DateTime().toDate();
							
							if(hoy.after(inicio) && hoy.before(fin)){
								temp.estado = "En Proceso";
							} else if(hoy.after(fin)){
								temp.estado = "Atrasado";
							}
						}
						catch (Throwable e) {
				            e.printStackTrace();
				        }
						
						temp.id = actividad.getId();
						temp.nombre = actividad.getNombre();
						temp.fechaInicio = fechaInicioFin[0];
						temp.fechaFin = fechaInicioFin[1];
						temp.porcentaje = actividad.getPorcentajeAvance();
						lstActividades.add(temp);
					}
				}
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstActividades);
	        response_text = String.join("", "\"actividades\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("exportarExcel")){
			String tipoReporte = map.get("tipoReporte");
			String nombreInforme = "Carga de Trabajo";
			
			if (tipoReporte.equals("cargaTrabajo")){
				String data = map.get("data");
				nombreInforme = "Informe de recursos (Carga de Trabajo)";
				String[] totales = map.get("totales").split(",");
				String mes = map.get("mes");
				
				Map<String,Object[]> reporte = new HashMap<>();
				Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
				List<Map<String, String>> datos = gson.fromJson(data, listType);
				
				reporte.put("0", new Object[] {"Responsable", "Actividades Atrasadas", "Actividades a Cumplir " + mes});
				
				int fila = 1;
				for(Map<String, String> d : datos){
					reporte.put(fila+"", new Object[] {d.get("responsable"), Utils.String2Int(d.get("actividadesAtrasadas")), Utils.String2Int(d.get("actividadesProceso"))});
					fila++;
				}
				
				reporte.put(fila+"", new Object[] {totales[0],Utils.String2Int(totales[1]),Utils.String2Int(totales[2])});
				exportarExcel(reporte,nombreInforme,usuario,response);
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
			CExcel excel = new CExcel("Reporte",false,null);
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