package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Connection;
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

import dao.AdministracionTransaccionalDAO;
import utilities.CMariaDB;
import utilities.Utils;

@WebServlet("/SAdministracionTransaccional")
public class SAdministracionTransaccional extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class sttransaccion{
		Integer objeto_id;
		String nombre;
		Integer objeto_tipo;
		String nombre_objeto_tipo;
		String usuario_creo;
		String usuario_actualizo;
		String fecha_creacion;
		String fecha_modificacion;
		String estado;
	}
	
	class stusuario{
		String usuario;
		Integer creados;
		Integer actualizados;
		Integer eliminados;
	}
       
    public SAdministracionTransaccional() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		if(accion.equals("getComponentes")){
			List<stusuario> lstusuarios = new ArrayList<stusuario>();
			if(CMariaDB.connect()){
				Connection conn = CMariaDB.getConnection();
				List<List<String>> usuarios = AdministracionTransaccionalDAO.obtenerUsuarios(conn);
				for(List<String> user : usuarios){
					stusuario temp = new stusuario();
					temp.usuario = user.get(0);
					temp.creados = Utils.String2Int(user.get(1));
					temp.actualizados = Utils.String2Int(user.get(2));
					temp.eliminados = Utils.String2Int(user.get(3));
					lstusuarios.add(temp);
				}
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstusuarios);
	        response_text = String.join("", "\"usuarios\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
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
