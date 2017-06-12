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
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.EstadoInformeDAO;
import pojo.EstadoInforme;
import pojo.Proyecto;
import utilities.Utils;

@WebServlet("/SEstadoInforme")
public class SEstadoInforme extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SEstadoInforme() {
        super();
    }
    
    class stEstadoInforme{
    	int tipoInforme;
    	String nombre;
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		if(accion.equals("getEstadoInforme")){
			int prestamoId = Utils.String2Int(map.get("prestamoId"));
			
			List<EstadoInforme> estadoInforme = new ArrayList<EstadoInforme>();
			estadoInforme = EstadoInformeDAO.getEstadoInforme(prestamoId);
			
			if(estadoInforme.size() == 0){
				EstadoInforme informeEstado = new EstadoInforme();
				
				Proyecto proyecto =new Proyecto();
				proyecto.setId(prestamoId);
				informeEstado.setProyecto(proyecto);
				
				informeEstado.setNombre("Informe Financiero Base");
				informeEstado.setTipoInforme(1);
				informeEstado.setUsuarioCreo(usuario);
				informeEstado.setFechaCreacion(new DateTime().toDate());
				informeEstado.setEstado(1);			
				
				EstadoInformeDAO.crearInforme(informeEstado);
				
				estadoInforme = EstadoInformeDAO.getEstadoInforme(prestamoId);
				
				List<stEstadoInforme> lstEstadoInforme = new ArrayList<stEstadoInforme>();
				for(EstadoInforme informe : estadoInforme){
					stEstadoInforme temp = new stEstadoInforme();
					temp.tipoInforme = informe.getTipoInforme();
					temp.nombre = informe.getNombre();
					lstEstadoInforme.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstEstadoInforme);
		        response_text = String.join("", "\"estadoInforme\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else{
				List<stEstadoInforme> lstEstadoInforme = new ArrayList<stEstadoInforme>();
				for(EstadoInforme informe : estadoInforme){
					stEstadoInforme temp = new stEstadoInforme();
					temp.tipoInforme = informe.getTipoInforme();
					temp.nombre = informe.getNombre();
					lstEstadoInforme.add(temp);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstEstadoInforme);
		        response_text = String.join("", "\"estadoInforme\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}
		}else if (accion.equals("guardar")){
			int idPrestamo = Utils.String2Int(map.get("idPrestamo"));
			int tipoInforme = Utils.String2Int(map.get("tipoInforme"));
			int cantidad = EstadoInformeDAO.getCantidadInformesPrestamo(idPrestamo);
			EstadoInforme informeEstado = new EstadoInforme();
			
			Proyecto proyecto =new Proyecto();
			proyecto.setId(idPrestamo);
			informeEstado.setProyecto(proyecto);
			
			informeEstado.setNombre("Informe Financiero Base Copia(" + cantidad + ")");
			informeEstado.setTipoInforme(cantidad+1);
			informeEstado.setUsuarioCreo(usuario);
			informeEstado.setFechaCreacion(new DateTime().toDate());
			informeEstado.setEstado(1);			
			
			EstadoInformeDAO.crearInforme(informeEstado);
			
			List<EstadoInforme> estadoInforme = new ArrayList<EstadoInforme>();
			estadoInforme = EstadoInformeDAO.getEstadoInforme(idPrestamo);
			
			List<stEstadoInforme> lstEstadoInforme = new ArrayList<stEstadoInforme>();
			for(EstadoInforme informe : estadoInforme){
				stEstadoInforme temp = new stEstadoInforme();
				temp.tipoInforme = informe.getTipoInforme();
				temp.nombre = informe.getNombre();
				lstEstadoInforme.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstEstadoInforme);
	        response_text = String.join("", "\"estadoInforme\":",response_text);
	        cantidad = cantidad + 1;
	        response_text = String.join("", "{\"success\":true, \"nuevoIdEstadoInforme\": " + cantidad + ",", response_text,"}");
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
