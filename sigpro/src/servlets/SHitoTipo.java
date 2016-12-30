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

import dao.HitoTipoDAO;
import pojo.HitoTipo;

/**
 * Servlet implementation class SHitoTipo
 */
@WebServlet("/SHitoTipo")
public class SHitoTipo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class sthitotipo{
		Integer id;
		String codigo;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SHitoTipo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
		if(accion.equals("getHitoTiposPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroHitoTipos = map.get("numerohitotiposs")!=null  ? Integer.parseInt(map.get("numerohitotiposs")) : 0;
			List<HitoTipo> hitotipos = HitoTipoDAO.getHitoTiposPagina(pagina, numeroHitoTipos);
			List<sthitotipo> sthitotipos=new ArrayList<sthitotipo>();
			for(HitoTipo hitotipo:hitotipos){
				sthitotipo temp =new sthitotipo();
				
				temp.descripcion = hitotipo.getDescripcion();
				temp.estado = hitotipo.getEstado();	
				temp.id = hitotipo.getId();
				temp.nombre = hitotipo.getNombre();
				sthitotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitotipos);
	        response_text = String.join("", "\"hitotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getHitoTipos")){
			List<HitoTipo> hitotipos = HitoTipoDAO.getHitoTipos();
			List<sthitotipo> sthitotipos=new ArrayList<sthitotipo>();
			for(HitoTipo hitotipo:hitotipos){
				sthitotipo temp =new sthitotipo();
				temp.descripcion = hitotipo.getDescripcion();
				temp.estado = hitotipo.getEstado();
				temp.id = hitotipo.getId();
				temp.nombre = hitotipo.getNombre();
				sthitotipos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(sthitotipos);
	        response_text = String.join("", "\"hitotipos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarHitoTipo")){
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				String nombre = map.get("nombre");
				String descripcion = map.get("descripcion");
				HitoTipo hitotipo;
				if(esnuevo){
					hitotipo = new  HitoTipo(nombre, descripcion, 1,null ) ;
							
				}
				else{
					hitotipo = HitoTipoDAO.getHitoTipoPorId(id);
					hitotipo.setNombre(nombre);
					hitotipo.setDescripcion(descripcion);
				}
				result = HitoTipoDAO.guardarHitoTipo(hitotipo);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + hitotipo.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarHitoTipo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				HitoTipo cooperante = HitoTipoDAO.getHitoTipoPorId(id);
				response_text = String.join("","{ \"success\": ",(HitoTipoDAO.eliminarHitoTipo(cooperante) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroHitoTipos")){
			response_text = String.join("","{ \"success\": true, \"totalhitotipos\":",HitoTipoDAO.getTotalHitoTipos().toString()," }");
		}
		else{
			response_text = "{ \"success\": false }";
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
