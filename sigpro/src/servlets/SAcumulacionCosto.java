package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dao.AcumulacionCostoDAO;
import pojo.AcumulacionCosto;

@WebServlet("/SAcumulacionCosto")
public class SAcumulacionCosto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stAcumulacionCosto{
		int id;
		String nombre;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
       
    public SAcumulacionCosto() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "";
		try{
			request.setCharacterEncoding("UTF-8");
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
			
			if(accion.equals("getAcumulacionCosto")){
				int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
				int numeroAcumulacionCosto = map.get("numeroresponsablerol")!=null  ? Integer.parseInt(map.get("numeroacumulacioncosto")) : 0;
				List<AcumulacionCosto> acumulacionCostos = AcumulacionCostoDAO.getAcumulacionCostoPagina(pagina, numeroAcumulacionCosto);
				
				List<stAcumulacionCosto> stacumulacioncosto=new ArrayList<stAcumulacionCosto>();
				for(AcumulacionCosto acumulacionCosto:acumulacionCostos){
					stAcumulacionCosto temp =new stAcumulacionCosto();
					temp.id = acumulacionCosto.getId();
					temp.nombre = acumulacionCosto.getNombre();
					stacumulacioncosto.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(stacumulacioncosto);
		        response_text = String.join("", "\"resposablerol\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.equals("numeroAcumulacionCosto")){
				response_text = String.join("","{ \"success\": true, \"totalresponsablerol\":",AcumulacionCostoDAO.getTotalAcumulacionCosto().toString()," }");
			}
		}catch (Exception ex){
			response_text = String.join("","{ \"success\": false }");
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
