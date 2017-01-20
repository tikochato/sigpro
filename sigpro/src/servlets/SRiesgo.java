package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import dao.RiesgoDAO;
import dao.RiesgoPropiedadDAO;
import dao.RiesgoPropiedadValorDAO;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Riesgo;
import pojo.RiesgoPropiedad;
import pojo.RiesgoPropiedadValor;
import pojo.RiesgoPropiedadValorId;
import pojo.RiesgoTipo;
import utilities.Utils;

/**
 * Servlet implementation class SRiesgo
 */
@WebServlet("/SRiesgo")
public class SRiesgo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class striesgo{
		Integer id;
		String nombre;
		String descripcion;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		Integer riesgotipoid;
		String riesgotiponombre;
		Integer componenteid;
		String componentenombre;
		Integer productoid;
		String productonombre;
		int estado;
	}
	
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
	}

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SRiesgo() {
        super();
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
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getRiesgosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRiesgos = map.get("numerocomponentes")!=null  ? Integer.parseInt(map.get("numerocomponentes")) : 0;
			List<Riesgo> riesgos = RiesgoDAO.getRiesgosPagina(pagina, numeroRiesgos);
			List<striesgo> striesgos=new ArrayList<striesgo>();
			for(Riesgo riesgo:riesgos){
				striesgo temp =new striesgo();
				temp.descripcion = riesgo.getDescripcion();
				temp.estado = riesgo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(riesgo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgo.getFechaCreacion());
				temp.id = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.usuarioActualizo = riesgo.getUsuarioActualizo();
				temp.usuarioCreo = riesgo.getUsuarioCreo();
				temp.riesgotipoid = riesgo.getRiesgoTipo().getId();
				temp.riesgotiponombre = riesgo.getRiesgoTipo().getNombre();
				temp.componenteid = riesgo.getComponente().getId();
				temp.componentenombre = riesgo.getComponente().getNombre();
				temp.productoid = riesgo.getProducto() != null ? riesgo.getProducto().getId() : 0;
				temp.productonombre = riesgo.getProducto() !=null ? riesgo.getProducto().getNombre() : "";
				striesgos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgos);
	        response_text = String.join("", "\"riesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRiesgosPaginaPorProyecto")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int proyectoid = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			int numeroRiesgos = map.get("numeroriesgos")!=null  ? Integer.parseInt(map.get("numeroriesgos")) : 0;
			List<Riesgo> riesgos = RiesgoDAO.getRiesgosPaginaPorProyecto(pagina, numeroRiesgos,proyectoid);
			List<striesgo> striesgos=new ArrayList<striesgo>();
			for(Riesgo riesgo:riesgos){
				striesgo temp =new striesgo();
				temp.descripcion = riesgo.getDescripcion();
				temp.estado = riesgo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(riesgo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgo.getFechaCreacion());
				temp.id = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.usuarioActualizo = riesgo.getUsuarioActualizo();
				temp.usuarioCreo = riesgo.getUsuarioCreo();
				temp.riesgotipoid = riesgo.getRiesgoTipo().getId();
				temp.riesgotiponombre = riesgo.getRiesgoTipo().getNombre();
				temp.componenteid = riesgo.getComponente().getId();
				temp.componentenombre = riesgo.getComponente().getNombre();
				temp.productoid = riesgo.getProducto() != null ? riesgo.getProducto().getId() : 0;
				temp.productonombre = riesgo.getProducto() !=null ? riesgo.getProducto().getNombre() : "";
				striesgos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgos);
	        response_text = String.join("", "\"riesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		} 
		else if(accion.equals("getRiesgos")){
			List<Riesgo> riesgos = RiesgoDAO.getRiesgos();
			List<striesgo> striesgos=new ArrayList<striesgo>();
			for(Riesgo riesgo:riesgos){
				striesgo temp =new striesgo();
				temp.descripcion = riesgo.getDescripcion();
				temp.estado = riesgo.getEstado();
				temp.fechaActualizacion = Utils.formatDate(riesgo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDate(riesgo.getFechaCreacion());
				temp.id = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.usuarioActualizo = riesgo.getUsuarioActualizo();
				temp.usuarioCreo = riesgo.getUsuarioCreo();
				temp.riesgotipoid = riesgo.getRiesgoTipo().getId();
				temp.riesgotiponombre = riesgo.getRiesgoTipo().getNombre();
				temp.componenteid = riesgo.getComponente().getId();
				temp.componentenombre = riesgo.getComponente().getNombre();
				temp.productoid = riesgo.getProducto() != null ? riesgo.getProducto().getId() : 0;
				temp.productonombre = riesgo.getProducto() !=null ? riesgo.getProducto().getNombre() : "";
				striesgos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgos);
	        response_text = String.join("", "\"riesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarRiesgo")){
			try{
				boolean result = false;
				boolean esnuevo = map.get("esnuevo").equals("true");
				int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
				if(id>0 || esnuevo){
					String nombre = map.get("nombre");
					String descripcion = map.get("descripcion");
					int riesgotipoid = Integer.parseInt(map.get("riesgotipoid"));
					int proyectoid= Integer.parseInt(map.get("proyectoid"));
					int componenteid= Integer.parseInt(map.get("componenteid"));
					int productoid= Integer.parseInt(map.get("productoid"));
					
					RiesgoTipo riesgoTipo= new RiesgoTipo();
					riesgoTipo.setId(riesgotipoid);
					
					Proyecto proyecto = new Proyecto();
					proyecto .setId(proyectoid);
					
					Componente componente = new Componente();
					componente.setId(componenteid);
					
					Producto producto = new Producto();
					producto.setId(productoid);
					
					type = new TypeToken<List<stdatadinamico>>() {
					}.getType();
	
					List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);
					
					Riesgo riesgo;
					if(esnuevo){
						riesgo = new Riesgo(componente, producto, proyecto
								,riesgoTipo, nombre, usuario, new DateTime().toDate(), 1);
						riesgo.setDescripcion(descripcion);
					}
					else{
						
						riesgo = RiesgoDAO.getRiesgoPorId(id);
						riesgo.setComponente(componente);
						riesgo.setProducto(producto);
						riesgo.setRiesgoTipo(riesgoTipo);
						riesgo.setNombre(nombre);
						riesgo.setDescripcion(descripcion);
						riesgo.setUsuarioActualizo(usuario);
						riesgo.setFechaActualizacion(new DateTime().toDate());
					}
					result = RiesgoDAO.guardarRiesgo(riesgo);
					
					List<RiesgoPropiedadValor> valores_temp = RiesgoPropiedadValorDAO.getRiesgoPropiedadadesValoresPorRiesgo(riesgo.getId());
					
					riesgo.setRiesgoPropiedadValors(null);
					if (valores_temp!=null){
						for (RiesgoPropiedadValor valor : valores_temp){
							RiesgoPropiedadValorDAO.eliminarRiesgoPropiedadValor(valor);
						}
					}
					
					for (stdatadinamico data : datos) {
						RiesgoPropiedad riesgoPropiedad = RiesgoPropiedadDAO.getRiesgoPropiedadPorId(Integer.parseInt(data.id));
						RiesgoPropiedadValorId idValor = new RiesgoPropiedadValorId(riesgo.getId(),Integer.parseInt(data.id));
						RiesgoPropiedadValor valor = new RiesgoPropiedadValor(idValor, riesgo, 
								riesgoPropiedad, 1, usuario, new DateTime().toDate());
	
						switch (1){  //// id del tipo de dato
							case 1:
								valor.setValorString(data.valor);
								break;
							case 2:
								valor.setValorEntero(Integer.parseInt(data.valor));
								break;
							case 3:
								valor.setValorDecimal(new BigDecimal(data.valor));
								break;
							case 4:
	
								break;
							case 5:
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								valor.setValorTiempo(sdf.parse(data.valor));
								break;
						}
	
						result = (result && RiesgoPropiedadValorDAO.guardarRiesgoPropiedadValor(valor));
					}
					
					
					
					response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
							+ "\"id\": " + riesgo.getId() +" }");
				}
				else{
					response_text = "{ \"success\": false }";
				}
			}
			catch (Throwable e){
				response_text = "{ \"success\": false }";
			}
		}
		else if(accion.equals("borrarRiesgo")){
			int id = map.get("id")!=null ? Integer.parseInt(map.get("id")) : 0;
			if(id>0){
				Riesgo riesgo = RiesgoDAO.getRiesgoPorId(id);
				riesgo.setUsuarioActualizo(usuario);
				response_text = String.join("","{ \"success\": ",(RiesgoDAO.eliminarRiesgo(riesgo) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroRiesgos")){
			response_text = String.join("","{ \"success\": true, \"totalriesgos\":",RiesgoDAO.getTotalRiesgos().toString()," }");
		}
		else if(accion.equals("numeroRiesgosPorProyecto")){
			int proyectoid = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			response_text = String.join("","{ \"success\": true, \"totalriesgos\":",RiesgoDAO.getTotalRiesgosPorProyecto(proyectoid).toString()," }");
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
