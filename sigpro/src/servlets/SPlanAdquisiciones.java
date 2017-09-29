package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

import dao.CategoriaAdquisicionDAO;
import dao.EstructuraProyectoDAO;
import pojo.CategoriaAdquisicion;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stpresupuestoPlan{
		BigDecimal planificado;
		BigDecimal real;
	}
	
	class stplan{
		stpresupuestoPlan[] mes = new stpresupuestoPlan[12];
		String objetoPlanNombre;
		String objetoPlanId;
		String objetoPlanTipo;
		Integer anio;
	}
	
	class stcomponenteplanadquisicion{
		Integer objetoId;
		String nombre;
		Integer nivel;
		Integer objetoTipo;
		stplan[] anioPlan;
	}
		
    public SPlanAdquisiciones() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
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
			
			if(accion.equals("generarPlan")){
				try{
					List<stcomponenteplanadquisicion> lstprestamo = generarPlan(idPrestamo, usuario);
										
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			        response_text = String.join("", "\"proyecto\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}
				catch (Exception e){
					CLogger.write("2", SControlAdquisiciones.class, e);
				}
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}catch(Exception e){
			CLogger.write("1", SPlanAdquisiciones.class, e);
		}
	}
	
	private List<stcomponenteplanadquisicion> generarPlan(Integer idPrestamo, String usuario) throws Exception{
		try{
			List<stcomponenteplanadquisicion> lstPrestamo = new ArrayList<>();
			List<?> estruturaProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
			stcomponenteplanadquisicion temp = null;
			
			List<CategoriaAdquisicion> lstCategorias = CategoriaAdquisicionDAO.getCategoriaAdquisicion(); 
			//PlanAdquisicionesDetalle detallePlan = null;
			for(Object objeto: estruturaProyecto){
				temp = new stcomponenteplanadquisicion();
				Object[] obj = (Object[])objeto;
				BigInteger objetoTipo = (BigInteger)obj[2];
				Integer tipo = objetoTipo.intValue();
				if(tipo == 2){
					temp.objetoId = (Integer)obj[0];
					temp.nombre = (String)obj[1];
					temp.nivel = 0;
					temp.objetoTipo = tipo;
					lstPrestamo.add(temp);
					
					//detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(2, temp.objetoId);
					
					for(CategoriaAdquisicion cat : lstCategorias){
						temp = new stcomponenteplanadquisicion();
						temp.nombre = cat.getNombre();
						temp.nivel = 1;
						lstPrestamo.add(temp);
					}
					
					
				}
			}
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("1", SControlAdquisiciones.class, e);
			return null;
		}
	}
}
