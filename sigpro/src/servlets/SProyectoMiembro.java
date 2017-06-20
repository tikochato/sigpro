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

import dao.ProyectoMiembroDAO;
import pojo.ProyectoMiembro;
import utilities.Utils;


@WebServlet("/SProyectoMiembro")
public class SProyectoMiembro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stmiembro{
		Integer proyectoId;
		Integer id;
		String nombre;
		int estado;
		String fechaCreacion;
		String usuarioCreo;
		String fechaactualizacion;
		String usuarioactualizo;
	}
       
    
    public SProyectoMiembro() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";

		if (accion.equals("getMiembrosPorProyect")) {
			int idProyecto = Utils.String2Int(map.get("proyectoId"));
			List<ProyectoMiembro> ProyectoMiembros = ProyectoMiembroDAO.getProyectoMiembroPorProyecto(idProyecto);
			List<stmiembro> miembros = new ArrayList<>();
			if (ProyectoMiembros!=null){
				for (ProyectoMiembro pi : ProyectoMiembros){
					stmiembro temp = new stmiembro();
					temp.proyectoId = pi.getProyecto().getId();
					temp.id = pi.getColaborador().getId();
					temp.nombre = pi.getColaborador()!= null ? (pi.getColaborador().getPnombre()
							+ " " + (pi.getColaborador().getSnombre() != null ? pi.getColaborador().getSnombre() : "") 
							+ " " + pi.getColaborador().getPapellido()
							+ " " + (pi.getColaborador().getSapellido() != null ? pi.getColaborador().getSapellido() : "")   
							) : "";
					temp.estado = pi.getEstado();
					miembros.add(temp);
				}
			}
			response_text = new GsonBuilder().serializeNulls().create().toJson(miembros);
			response_text = String.join("", "\"miembros\":", response_text);
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else{
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
