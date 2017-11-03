package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.EstructuraProyectoDAO;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanEstructuralProyecto")
public class SPlanEstructuralProyecto extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stplanestructuralproyecto{
		Integer objetoId;
		Integer objetoTipo;
		String nombre;
		Integer nivel;
		Integer duracion;
		String fechaInicial;
		String fechaFinal;
		BigDecimal presupuestoAprobado;
		BigDecimal costoPlanificado;
		BigDecimal asignacionPresupuestariaVigente;
		BigDecimal presupuestoDevengado;
		double avanceFinanciero;
	}
	
	
    public SPlanEstructuralProyecto() {
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
		
		Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
		
		if(accion.equals("generarPlan")){
			try{
				List<stplanestructuralproyecto> lstprestamo = generarPlan(proyectoId, usuario);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
		        response_text = String.join("", "\"proyecto\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}catch (Exception e){
				CLogger.write("1", SPlanEstructuralProyecto.class, e);
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
	
	private List<stplanestructuralproyecto> generarPlan(Integer IdProyecto, String usuario) throws Exception{
		try{
			List<stplanestructuralproyecto> lstPrestamo = new ArrayList<>();
			List<?> estruturaProyecto = EstructuraProyectoDAO.getEstructuraProyecto(IdProyecto);
			
			stplanestructuralproyecto temp = null;
			
			for(Object objeto: estruturaProyecto){
				Object[] obj = (Object[]) objeto;
				Integer nivel = (obj[3]!=null) ? ((String)obj[3]).length()/8 : 0;
				if(nivel != null){
					temp = new stplanestructuralproyecto();
					temp.objetoId = (Integer)obj[0];
					temp.nombre = (String)obj[1];
					temp.objetoTipo = ((BigInteger)obj[2]).intValue();
					temp.nivel = nivel;
					temp.duracion = (Integer)obj[6];
					temp.fechaInicial = Utils.formatDate((Date)obj[4]);
					temp.fechaFinal = Utils.formatDate((Date)obj[5]);
					temp.costoPlanificado = (BigDecimal)obj[8];
					lstPrestamo.add(temp);
				}
			}
			
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("2", SPlanEstructuralProyecto.class, e);
			return null;
		}
	}

}
