package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

import dao.RiesgoDAO;
import dao.RiesgoPropiedadDAO;
import dao.RiesgoPropiedadValorDAO;
import pojo.Colaborador;
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
		String impactoProyectado;
		Integer impacto;
		Integer puntuacionImpacto;
		Integer probabilidad;
		String gatillosSintomas;
		String respuesta;
		Integer colaboradorid;
		String colaboradorNombre;
		String riesgosSecundarios;
		Integer ejecutado;
		String fechaEjecucion;
		int estado;
	}
	
	class stdatadinamico {
		String id;
		String tipo;
		String label;
		String valor;
		String valor_f;
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getRiesgosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRiesgos = map.get("numeroriesgos")!=null  ? Integer.parseInt(map.get("numeroriesgos")) : 0;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Riesgo> riesgos = RiesgoDAO.getRiesgosPagina(pagina, numeroRiesgos
					,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
			List<striesgo> striesgos=new ArrayList<striesgo>();
			for(Riesgo riesgo:riesgos){
				striesgo temp =new striesgo();
				temp.descripcion = riesgo.getDescripcion();
				temp.estado = riesgo.getEstado();
				temp.fechaActualizacion = Utils.formatDateHour(riesgo.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(riesgo.getFechaCreacion());
				temp.id = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.usuarioActualizo = riesgo.getUsuarioActualizo();
				temp.usuarioCreo = riesgo.getUsuarioCreo();
				temp.riesgotipoid = riesgo.getRiesgoTipo().getId();
				temp.riesgotiponombre = riesgo.getRiesgoTipo().getNombre();
				temp.impactoProyectado = riesgo.getImapctoProyectado();
				
				temp.impacto = riesgo.getImpacto();
				temp.puntuacionImpacto = riesgo.getPuntuacionImpacto();
				temp.probabilidad = riesgo.getProbabilidad();
				temp.gatillosSintomas = riesgo.getGatillosSintomas();
				temp.respuesta = riesgo.getRespuesta();
				temp.riesgosSecundarios = riesgo.getRiesgosSegundarios();
										  riesgo.getRiesgosSegundarios();
				temp.ejecutado = riesgo.getEjecutado();
				temp.fechaEjecucion = Utils.formatDate(riesgo.getFechaEjecucion());
				temp.colaboradorid = riesgo.getColaborador().getId();
				if (riesgo.getColaborador()!=null )
				temp.colaboradorNombre = String.join(" ", riesgo.getColaborador().getPnombre(),
						riesgo.getColaborador().getSnombre() !=null ? riesgo.getColaborador().getSnombre() : "",
						riesgo.getColaborador().getPapellido()!=null ? riesgo.getColaborador().getPapellido() : "",
						riesgo.getColaborador().getSapellido()!=null ? riesgo.getColaborador().getSapellido() : ""	
				);
								
						
				striesgos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(striesgos);
	        response_text = String.join("", "\"riesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getRiesgosPaginaPorObjeto")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroRiesgos = map.get("numeroriesgos")!=null  ? Integer.parseInt(map.get("numeroriesgos")) : 0;
			int objetoId = map.get("objetoid")!=null && map.get("objetoid").length()>0 ?
					Integer.parseInt(map.get("objetoid")): 0;
			int objetoTipo = map.get("objetotipo")!=null && map.get("objetotipo").length()>0 ? 
					Integer.parseInt(map.get("objetotipo")) : 0 ;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			String columna_ordenada = map.get("columna_ordenada");
			String orden_direccion = map.get("orden_direccion");
			List<Riesgo> riesgos = RiesgoDAO.getRiesgosPaginaPorObjeto(pagina, numeroRiesgos,objetoId,objetoTipo
					,filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion,columna_ordenada,orden_direccion);
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
				temp.impactoProyectado = riesgo.getImapctoProyectado();
				temp.impacto = riesgo.getImpacto();
				temp.puntuacionImpacto = riesgo.getPuntuacionImpacto();
				temp.probabilidad = riesgo.getProbabilidad();
				temp.riesgosSecundarios = riesgo.getRiesgosSegundarios();
				temp.gatillosSintomas = riesgo.getGatillosSintomas();
				temp.respuesta = riesgo.getRespuesta();
				temp.ejecutado = riesgo.getEjecutado();
				temp.fechaEjecucion = Utils.formatDate(riesgo.getFechaEjecucion());
				
				if (riesgo.getColaborador()!=null ){
					temp.colaboradorid = riesgo.getColaborador().getId();
				temp.colaboradorNombre = String.join(" ", riesgo.getColaborador().getPnombre(),
						riesgo.getColaborador().getSnombre() !=null ? riesgo.getColaborador().getSnombre() : "",
						riesgo.getColaborador().getPapellido()!=null ? riesgo.getColaborador().getPapellido() : "",
						riesgo.getColaborador().getSapellido()!=null ? riesgo.getColaborador().getSapellido() : ""	
				);
				}
							
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
				temp.impactoProyectado = riesgo.getImapctoProyectado();
				temp.impacto = riesgo.getImpacto();
				temp.puntuacionImpacto = riesgo.getPuntuacionImpacto();
				temp.probabilidad = riesgo.getProbabilidad();
				temp.gatillosSintomas = riesgo.getGatillosSintomas();
				temp.respuesta = riesgo.getRespuesta();
				temp.riesgosSecundarios = riesgo.getRiesgosSegundarios();
				temp.ejecutado = riesgo.getEjecutado();
				temp.fechaEjecucion = Utils.formatDate(riesgo.getFechaEjecucion());
				temp.colaboradorid = riesgo.getColaborador().getId();
				if (riesgo.getColaborador()!=null )
				temp.colaboradorNombre = String.join(" ", riesgo.getColaborador().getPnombre(),
						riesgo.getColaborador().getSnombre() !=null ? riesgo.getColaborador().getSnombre() : "",
						riesgo.getColaborador().getPapellido()!=null ? riesgo.getColaborador().getPapellido() : "",
						riesgo.getColaborador().getSapellido()!=null ? riesgo.getColaborador().getSapellido() : ""	
				);
							
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
					int riesgotipoid = map.get("riesgotipoid")!=null && map.get("riesgotipoid").length() > 0 ?
							Integer.parseInt(map.get("riesgotipoid")) : 0;
				    String impactoProyectado = map.get("impactoproyectado");
				    Integer impacto = Utils.String2Int(map.get("impacto"),null);
				    Integer puntuacionImpacto = Utils.String2Int(map.get("puntuacionimpoacto"), null);
				    Integer probabilidad = Utils.String2Int(map.get("probabilidad"),null);
				    String gatillosOSintomas = map.get("gatillossintomas");
				    String respuesta = map.get("respuesta");
				    String riesgosSecundarios = map.get("riesgossecundarios");
				    Integer ejecutado = Utils.String2Int(map.get("ejecutado"), null);
				    Date fechaEjecucion = Utils.dateFromString(map.get("fechaejecucion"));
				    
				    
				    Colaborador colaborador = null;
				    Integer colobaradorId = Utils.String2Int(map.get("colaboradorid"),null);
				    if (colobaradorId!=null){
				    	colaborador = new Colaborador();
				    	colaborador.setId(colobaradorId);
				    }
				    
					
					RiesgoTipo riesgoTipo= new RiesgoTipo();
					riesgoTipo.setId(riesgotipoid);
					
					type = new TypeToken<List<stdatadinamico>>() {
					}.getType();
	
					List<stdatadinamico> datos = gson.fromJson(map.get("datadinamica"), type);
					
					Riesgo riesgo;
					if(esnuevo){
						
						riesgo = new Riesgo(colaborador, riesgoTipo, nombre, descripcion, usuario, null, new Date(), null, 1,
								impactoProyectado, impacto, puntuacionImpacto, probabilidad, gatillosOSintomas, respuesta,
								riesgosSecundarios, ejecutado, fechaEjecucion, null, null);
						
						
					}
					else{
						
						riesgo = RiesgoDAO.getRiesgoPorId(id);
						riesgo.setRiesgoTipo(riesgoTipo);
						riesgo.setNombre(nombre);
						riesgo.setDescripcion(descripcion);
						riesgo.setUsuarioActualizo(usuario);
						riesgo.setFechaActualizacion(new DateTime().toDate());
						riesgo.setImapctoProyectado(impactoProyectado);
						riesgo.setImpacto(impacto);
						riesgo.setPuntuacionImpacto(puntuacionImpacto);
						riesgo.setProbabilidad(probabilidad);
						riesgo.setGatillosSintomas(gatillosOSintomas);
						riesgo.setRespuesta(respuesta);
						riesgo.setRiesgosSegundarios(riesgosSecundarios);
						riesgo.setEjecutado(ejecutado);
						riesgo.setFechaEjecucion(fechaEjecucion);
						riesgo.setColaborador(colaborador);
					}
					int objetoId = Utils.String2Int(map.get("objetoId"));
					int objetoTipo = Utils.String2Int(map.get("objetoTipo"));
					result = RiesgoDAO.guardarRiesgo(riesgo,objetoId, objetoTipo);
					
					List<RiesgoPropiedadValor> valores_temp = RiesgoPropiedadValorDAO.getRiesgoPropiedadadesValoresPorRiesgo(riesgo.getId());
					
					riesgo.setRiesgoPropiedadValors(null);
					if (valores_temp!=null){
						for (RiesgoPropiedadValor valor : valores_temp){
							RiesgoPropiedadValorDAO.eliminarRiesgoPropiedadValor(valor);
						}
					}
					
					for (stdatadinamico data : datos) {
						if (data.valor!=null && data.valor.length()>0 && data.valor.compareTo("null")!=0){
							RiesgoPropiedad riesgoPropiedad = RiesgoPropiedadDAO.getRiesgoPropiedadPorId(Integer.parseInt(data.id));
							RiesgoPropiedadValorId idValor = new RiesgoPropiedadValorId(riesgo.getId(),Integer.parseInt(data.id));
							RiesgoPropiedadValor valor = new RiesgoPropiedadValor(idValor, riesgo, 
									riesgoPropiedad, 1, usuario, new DateTime().toDate());
		
							switch (riesgoPropiedad.getDatoTipo().getId()){
							case 1:
								valor.setValorString(data.valor);
								break;
							case 2:
								valor.setValorEntero(Utils.String2Int(data.valor, null));
								break;
							case 3:
								valor.setValorDecimal(Utils.String2BigDecimal(data.valor, null));
								break;
							case 4:
								valor.setValorEntero(Utils.String2Boolean(data.valor, null));
								break;
							case 5:
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								valor.setValorTiempo(data.valor_f.compareTo("")!=0 ? sdf.parse(data.valor_f) : null);
								break;
						}
							result = (result && RiesgoPropiedadValorDAO.guardarRiesgoPropiedadValor(valor));
						}
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
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			response_text = String.join("","{ \"success\": true, \"totalriesgos\":",RiesgoDAO
					.getTotalRiesgos(filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
		}
		else if(accion.equals("numeroRiesgosPorObjeto")){
			int objetoId = map.get("objetoid")!=null && map.get("objetoid").length()>0 ?
					Integer.parseInt(map.get("objetoid")): 0;
			int objetoTipo = map.get("objetotipo")!=null && map.get("objetotipo").length()>0 ? 
					Integer.parseInt(map.get("objetotipo")) : 0 ;
			String filtro_nombre = map.get("filtro_nombre");
			String filtro_usuario_creo = map.get("filtro_usuario_creo");
			String filtro_fecha_creacion = map.get("filtro_fecha_creacion");
			
			response_text = String.join("","{ \"success\": true, \"totalriesgos\":",RiesgoDAO
					.getTotalRiesgosPorProyecto(objetoId,objetoTipo, filtro_nombre,filtro_usuario_creo,filtro_fecha_creacion).toString()," }");
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
