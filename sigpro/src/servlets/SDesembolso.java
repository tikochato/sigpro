package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.DesembolsoDAO;
import pojo.Desembolso;
import pojo.DesembolsoTipo;
import pojo.Proyecto;
import utilities.Utils;

/**
 * Servlet implementation class SDesembolso
 */
@WebServlet("/SDesembolso")
public class SDesembolso extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stdesembolso{
		Integer id;
		String fecha;
		BigDecimal monto;
		BigDecimal tipocambio;
		Integer proyectoid;
		String proyecto;
		Integer desembolsotipoid;
		String desembolsotipo;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SDesembolso() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
		String accion = map.get("accion");
		String response_text="";
		
		if(accion.equals("getDesembolsosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroDesembolsos = map.get("numerodesembolsos")!=null  ? Integer.parseInt(map.get("numerodesembolsos")) : 0;
			List<Desembolso> desembolsos = DesembolsoDAO.getDesembolsosPagina(pagina, numeroDesembolsos);
			List<stdesembolso> stdesembolsos=new ArrayList<stdesembolso>();
			for(Desembolso desembolso:desembolsos){
				stdesembolso temp =new stdesembolso();
				temp.id = desembolso.getId();
				temp.fecha = Utils.formatDate(desembolso.getFecha());
				temp.monto = desembolso.getMonto();
				temp.tipocambio = desembolso.getTipoCambio();
				temp.estado = desembolso.getEstado();
				temp.desembolsotipoid = desembolso.getDesembolsoTipo().getId();
				temp.desembolsotipo = desembolso.getDesembolsoTipo().getNombre();
				temp.proyecto = desembolso.getProyecto().getNombre();
				temp.proyectoid = desembolso.getProyecto().getId();
				temp.fechaActualizacion = Utils.formatDateHour(desembolso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(desembolso.getFechaCreacion());
				temp.usuarioActualizo = desembolso.getUsuarioActualizo();
				temp.usuarioCreo = desembolso.getUsuarioCreo();
				stdesembolsos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getDesembolsoss")){
			List<Desembolso> desembolsos = DesembolsoDAO.getDesembolsos();
			List<stdesembolso> stdesembolsos=new ArrayList<stdesembolso>();
			for(Desembolso desembolso:desembolsos){
				stdesembolso temp =new stdesembolso();
				temp.id = desembolso.getId();
				temp.fecha = Utils.formatDate(desembolso.getFecha());
				temp.monto = desembolso.getMonto();
				temp.tipocambio = desembolso.getTipoCambio();
				temp.estado = desembolso.getEstado();
				temp.desembolsotipoid = desembolso.getDesembolsoTipo().getId();
				temp.desembolsotipo = desembolso.getDesembolsoTipo().getNombre();
				temp.proyecto = desembolso.getProyecto().getNombre();
				temp.proyectoid = desembolso.getProyecto().getId();
				temp.fechaActualizacion = Utils.formatDateHour(desembolso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(desembolso.getFechaCreacion());
				temp.usuarioActualizo = desembolso.getUsuarioActualizo();
				temp.usuarioCreo = desembolso.getUsuarioCreo();
				stdesembolsos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarDesembolso")){
			
			boolean result = false;
			boolean esnuevo = map.get("esnuevo").equals("true");
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0 || esnuevo){
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Date fecha = null;
				try {
					fecha = sdf.parse(map.get("fecha"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				BigDecimal monto = map.get("monto")!=null && map.get("monto").length()>0 ?
						new BigDecimal(map.get("monto")) : null;
				BigDecimal tipoCambio = new BigDecimal(map.get("tipocambio"));
				
				
				Proyecto proyecto = new Proyecto();
				proyecto.setId(map.get("proyectoid")!=null && map.get("proyectoid").length()>0 ? 
						Integer.parseInt(map.get("proyectoid")) : null);
				
				DesembolsoTipo desembolsoTipo = new DesembolsoTipo();
				desembolsoTipo.setId(map.get("desembolsotipoid")!=null && map.get("desembolsotipoid").length()>0 ? 
						Integer.parseInt(map.get("desembolsotipoid")): null);
				
				
				Desembolso desembolso;
				if(esnuevo){
					desembolso = new Desembolso(desembolsoTipo, proyecto, fecha, 1, monto, tipoCambio,
							usuario, new DateTime().toDate(),0);
				}
				else{
					
					desembolso = DesembolsoDAO.getDesembolsoPorId(id);
					desembolso.setMonto(monto);
					desembolso.setTipoCambio(tipoCambio);
					desembolso.setProyecto(proyecto);
					desembolso.setDesembolsoTipo(desembolsoTipo);
					desembolso.setFechaActualizacion(new DateTime().toDate());
					desembolso.setUsuarioActualizo(usuario);
					
				}
				result = DesembolsoDAO.guardarDesembolso(desembolso);
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						+ "\"id\": " + desembolso.getId() +" }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("borrarDesembolso")){
			
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Desembolso desembolso = DesembolsoDAO.getDesembolsoPorId(id);
				desembolso.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(DesembolsoDAO.eliminarDesembolso(desembolso) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroDesembolsos")){
			response_text = String.join("","{ \"success\": true, \"totaldesembolsos\":",DesembolsoDAO.getTotalDesembolsos().toString()," }");
		}
		else if(accion.equals("numeroDesembolsosPorProyecto")){
			String filtro_fecha = map.get("filtro_fecha");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			response_text = String.join("","{ \"success\": true, \"totaldesembolsos\":",DesembolsoDAO.
					getTotalDesembolsosPorProyecto(proyectoId,filtro_fecha,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("getDesembolsosPaginaPorProyecto")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			int numeroDesembolsos = map.get("numerodesembolsos")!=null  ? Integer.parseInt(map.get("numerodesembolsos")) : 0;
			String filtro_fecha = map.get("filtro_fecha");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Desembolso> desembolsos = DesembolsoDAO.
					getDesembolsosPaginaPorProyecto(pagina, numeroDesembolsos,proyectoId
							,filtro_fecha,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<stdesembolso> stdesembolsos=new ArrayList<stdesembolso>();
			for(Desembolso desembolso:desembolsos){
				stdesembolso temp =new stdesembolso();
				temp.id = desembolso.getId();
				temp.fecha = Utils.formatDate(desembolso.getFecha());
				temp.monto = desembolso.getMonto();
				temp.tipocambio = desembolso.getTipoCambio();
				temp.estado = desembolso.getEstado();
				temp.desembolsotipoid = desembolso.getDesembolsoTipo().getId();
				temp.desembolsotipo = desembolso.getDesembolsoTipo().getNombre();
				temp.proyecto = desembolso.getProyecto().getNombre();
				temp.proyectoid = desembolso.getProyecto().getId();
				temp.fechaActualizacion = Utils.formatDateHour(desembolso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(desembolso.getFechaCreacion());
				temp.usuarioActualizo = desembolso.getUsuarioActualizo();
				temp.usuarioCreo = desembolso.getUsuarioCreo();
				stdesembolsos.add(temp);
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
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
