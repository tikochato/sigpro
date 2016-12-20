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

import dao.DesembolsoTipoDAO;
import pojo.DesembolsoTipo;



@WebServlet("/SDesembolsoTipo")
public class SDesembolsoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stdesembolsotipo{
		Integer id;
		String nombre;
		String descripcion;
		int estado;
	}
    
    public SDesembolsoTipo() {
        super();
    
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		
		if(accion.equals("getDesembolsotiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroDesembolsoTipos = map.get("numerodesembolsotipos")!=null  ? Integer.parseInt(map.get("numerodesembolsotipos")) : 0;
			List<DesembolsoTipo> desembolsoTipos = DesembolsoTipoDAO.geDesembolsoTiposPagina(pagina, numeroDesembolsoTipos);
			List<stdesembolsotipo> stdesembolsotipos = new ArrayList<stdesembolsotipo>();
			for(DesembolsoTipo desembolsoTipo :desembolsoTipos){
				stdesembolsotipo temp =new stdesembolsotipo();
				temp.id = desembolsoTipo.getId();
				temp.nombre = desembolsoTipo.getNombre();
				temp.descripcion = desembolsoTipo.getDescripcion();
				temp.estado = desembolsoTipo.getEstado();
				
				stdesembolsotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsotipos);
	        response_text = String.join("", "\"desembolsotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarDesembolsoTipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				DesembolsoTipo  desembolsoTipo;
				if(esnuevo){
					desembolsoTipo = new DesembolsoTipo(nombre, descripcion, 1, null);
					
				}
				else{
					desembolsoTipo = DesembolsoTipoDAO.getDesembolosTipoPorId(id);
					
					desembolsoTipo.setNombre(nombre);
					desembolsoTipo.setDescripcion(descripcion);				}
				result = DesembolsoTipoDAO.guardarDesembolsoTipo(desembolsoTipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + desembolsoTipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarDesembolsoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				DesembolsoTipo desembolsoTipo = DesembolsoTipoDAO.getDesembolosTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(DesembolsoTipoDAO.eliminarDesembolsoTipo(desembolsoTipo) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroDesembolsoTipo")){
			response_text = String.join("","{ \"success\": true, \"totaldesembolsotipo\":",DesembolsoTipoDAO.getTotalDesembolsoTipo().toString()," }");
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
