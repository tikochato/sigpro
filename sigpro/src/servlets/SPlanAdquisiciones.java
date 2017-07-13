package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.codec.Base64;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.PagoDAO;
import dao.PlanAdquisicionesDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Pago;
import pojo.PlanAdquisiciones;
import pojo.Producto;
import pojo.Proyecto;
import pojo.UnidadMedida;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
	private List<String> objetoPlan = null;
       
    public SPlanAdquisiciones() {
        super();
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
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
		
		if (accion.equals("generarPlan")){
			objetoPlan = new ArrayList<String>();
			LinkedHashMap<String, Map<String, Object>> componentes = obtenerComponentes(idPrestamo,usuario);
			
			for(String op : objetoPlan){
				Map<String, Object> rowEntity = componentes.get(op);
				Integer idObjeto = (Integer)rowEntity.get("idObjetoTipo");
				Integer objetoTipo = (Integer)rowEntity.get("objetoTipo");
				
				Integer idPadre = (Integer)rowEntity.get("idPredecesor");
				Integer tipoPadre = (Integer)rowEntity.get("objetoTipoPredecesor");
				
				Map<String, Object> padre = componentes.get(idPadre+","+tipoPadre);
				List<String> hijo = (List<String>)padre.get("hijo");
				hijo.add(idObjeto+","+objetoTipo);
				padre.put("hijo",hijo);
			}
			
			List<Map<String, Object>> resultado = new ArrayList<Map<String, Object>>(componentes.values());
			
			for(Map<String, Object> prestamo : resultado){
				Integer posicion = (Integer)prestamo.get("posicionArbol");
				prestamo.put("$$treeLevel", posicion -1);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultado);
	        response_text = String.join("", "\"componentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("guardarPlan")){
			try{
				Integer idObjetoTipo = Utils.String2Int(map.get("idObjetoTipo"),0);
				Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
				Integer planAdquisicionId = Utils.String2Int(map.get("idPlanAdquisicion"), 0);
				
				PlanAdquisiciones plan;
				
				Integer unidadMedida = Utils.String2Int(map.get("unidadMedida"));
				UnidadMedida unidad = new UnidadMedida(); 
				unidad.setId(unidadMedida);
				
				Integer cantidad = Utils.String2Int(map.get("cantidad"));
				BigDecimal costo = new BigDecimal(map.get("costo"));
				BigDecimal total = new BigDecimal(map.get("total"));
				Date planificadoDocs = Utils.dateFromString(map.get("planificadoDocs"));
				Date realDocs = Utils.dateFromString(map.get("realDocs"));
				Date planificadoLanzamiento = Utils.dateFromString(map.get("planificadoLanzamiento"));
				Date realLanzamiento = Utils.dateFromString(map.get("realLanzamiento"));
				Date planificadoRecepcionEval = Utils.dateFromString(map.get("planificadoRecepcionEval"));
				Date realRecepcionEval = Utils.dateFromString(map.get("realRecepcionEval"));
				Date planificadoAdjudica = Utils.dateFromString(map.get("planificadoAdjudica"));
				Date realAdjudica = Utils.dateFromString(map.get("realAdjudica"));
				Date planificadoFirma =  Utils.dateFromString(map.get("planificadoFirma"));
				Date realFirma = Utils.dateFromString(map.get("realFirma"));
				boolean esnuevo = map.get("esnuevo").equals("true");	

				if(planAdquisicionId > 0 || esnuevo){
					plan = PlanAdquisicionesDAO.getPlanAdquisicionById(planAdquisicionId);
					if(plan == null)
						plan = new PlanAdquisiciones();
					
					plan.setUnidadMedida(unidad);
					plan.setCantidad(cantidad);
					plan.setPrecioUnitario(costo);
					plan.setTotal(total);
					plan.setPreparacionDocPlanificado(planificadoDocs);
					plan.setPreparacionDocReal(realDocs);
					plan.setLanzamientoEventoPlanificado(planificadoLanzamiento);
					plan.setLanzamientoEventoReal(realLanzamiento);
					plan.setRecepcionOfertasPlanificado(planificadoRecepcionEval);
					plan.setRecepcionOfertasReal(realRecepcionEval);
					plan.setAdjudicacionPlanificado(planificadoAdjudica);
					plan.setAdjudicacionReal(realAdjudica);
					plan.setFirmaContratoPlanificado(planificadoFirma);
					plan.setFirmaContratoReal(realFirma);
					plan.setObjetoId(idObjetoTipo);
					plan.setObjetoTipo(objetoTipo);
					plan.setUsuarioCreo(usuario);
					plan.setFechaCreacion(new DateTime().toDate());
					plan.setEstado(1);
				}else{
					plan = new PlanAdquisiciones(unidad, 0, cantidad, costo, total, planificadoDocs, realDocs, planificadoLanzamiento, 
							realLanzamiento, planificadoRecepcionEval, realRecepcionEval, planificadoAdjudica, realAdjudica, 
							planificadoFirma, realFirma, idObjetoTipo, objetoTipo, usuario, null, new DateTime().toDate(), null, 1, null);
							
				}
				
				planAdquisicionId = PlanAdquisicionesDAO.guardarPlanAdquisicion(plan);
				
				response_text = String.join("", "\"planAdquisicionId\":"+ Integer.toString(planAdquisicionId)+",\"objetoTipo\":"+ Integer.toString(objetoTipo)+",\"idObjetoTipo\":"+ Integer.toString(idObjetoTipo));
//				response_text = String.join("", ",\"objetoTipo\":"+ Integer.toString(objetoTipo));
//				response_text = String.join("", ",\"idObjetoTipo\":"+ Integer.toString(idObjetoTipo));
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}else if(accion.equals("borrarPlan")){
			Integer idPlan = Utils.String2Int(map.get("idPlanAdquisiciones"));
			PlanAdquisiciones plan = PlanAdquisicionesDAO.getPlanAdquisicionById(idPlan);
			
			
			List<Pago> pagos = PagoDAO.getPagosByIdPlan(idPlan);
			
			for(Pago pago : pagos){
				PagoDAO.eliminarPago(pago);
			}
			
			boolean borrado = PlanAdquisicionesDAO.borrarPlan(plan);
			
			if(borrado){
				response_text = String.join("", "{\"success\":true}");
			}else{
				response_text = String.join("", "{\"success\":false}");
			}
		}else if(accion.equals("exportarExcel")){
			String data = map.get("data");
			String nombreInforme = "Plan de adquisiciones AÑO FISCAL " + Year.now().getValue();
			
			Map<String,Object[]> reporte = new HashMap<>();
			Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
			List<Map<String, String>> datos = gson.fromJson(data, listType);
			
			String[] encabezadosCombinados = new String[5];
			encabezadosCombinados[0] = "Preparación de Documentos,2";
			encabezadosCombinados[1] = "Lanzamiento de evento,2";
			encabezadosCombinados[2] = "Recepción y evaluación de ofertas,2";
			encabezadosCombinados[3] = "Adjudicación,2";
			encabezadosCombinados[4] = "Firma de contrato,2";
			
			reporte.put("0", new Object[] {"Componente", "Método", "Planificado", "Real", "Planificado", "Real", "Planificado", "Real", "Planificado", "Real", "Planificado", "Real"});
			
			int fila = 1;
			
			for(Map<String, String> d : datos){
				reporte.put(fila+"", new Object[] {d.get("nombre"), d.get("metodo"), d.get("planificadoDocs"), d.get("realDocs"), d.get("planificadoLanzamiento"), d.get("realLanzamiento"), d.get("planificadoRecepcionEval"), d.get("realRecepcionEval"), d.get("planificadoAdjudica"), d.get("realAdjudica"), d.get("planificadoFirma"), d.get("realFirma")});
				fila++;
			}
			
			exportarExcel(reporte,nombreInforme,usuario,response,encabezadosCombinados, 2);
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private void exportarExcel(Map<String,Object[]> datos, String nombreInforme, String usuario, HttpServletResponse response, String[] encabezadosCombinados, int inicio){
		try{
			CExcel excel = new CExcel("Reporte",false);
			String path = excel.ExportarExcel2(datos, nombreInforme, usuario,encabezadosCombinados, inicio);
			File file=new File(path);
			if(file.exists()){
				FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
		        	
		        }
		        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		        
		        int readByte = 0;
		        byte[] buffer = new byte[2024];

	            while(true)
	            {
	                readByte = is.read(buffer);
	                if(readByte == -1)
	                {
	                    break;
	                }
	                outByteStream.write(buffer);
	            }
	            
	            file.delete();
	            
	            is.close();
	            outByteStream.flush();
	            outByteStream.close();
	            
		        byte [] outArray = Base64.encode(outByteStream.toByteArray());
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; Informe_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		catch(Throwable e){
			CLogger.write("2", SReporte.class, e);
		}
	}
	
	private Map<String, Object> getEstructura(){
		Map<String, Object> estructura = new HashMap<String, Object>();
		estructura.put("id", 0);
		estructura.put("ocultarPagos", true);
		estructura.put("modificado", false);
		estructura.put("ocultarLimpiar", true);
		estructura.put("contieneInfoPlan", false);
		estructura.put("planAdquisicionId",0);
		estructura.put("idPrestamo", 0);
		estructura.put("objetoTipo", 0);
		estructura.put("idObjetoTipo",0);
		estructura.put("estado",0);
		estructura.put("nombre", "");
		estructura.put("posicionArbol", 0);
		estructura.put("objetoTipoPredecesor",0);
		estructura.put("idPredecesor",0);
		estructura.put("unidadMedida",0);
		estructura.put("cantidad",0);
		estructura.put("costo", new BigDecimal(0));
		estructura.put("total", new BigDecimal(0));
		estructura.put("hijo",new ArrayList<Integer>());
		estructura.put("fechaInicio","");
		estructura.put("fechaFin","");
		estructura.put("metodo",0);
		estructura.put("columnas","");
		estructura.put("planificadoDocs","");
		estructura.put("realDocs","");
		estructura.put("planificadoLanzamiento","");
		estructura.put("realLanzamiento","");
		estructura.put("planificadoRecepcionEval","");
		estructura.put("realRecepcionEval","");
		estructura.put("planificadoAdjudica","");
		estructura.put("realAdjudica","");
		estructura.put("planificadoFirma","");
		estructura.put("realFirma","");
		
		return estructura;
	}
	
	private LinkedHashMap<String, Map<String, Object>> obtenerComponentes(int proyectoId, String usuario){
		LinkedHashMap<String, Map<String, Object>> resultado =  new LinkedHashMap<>();
		List<Map<String, Object>> resultPrestamo = new ArrayList<Map<String, Object>>();
		
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		if (proyecto!=null){
			Map<String, Object> estructura = getEstructura();
			estructura.put("objetoTipo",OBJETO_ID_PROYECTO);
			estructura.put("idObjetoTipo",proyecto.getId());
			estructura.put("posicionArbol",1);
			estructura.put("nombre",proyecto.getNombre());
			
			List<PlanAdquisiciones> p = new ArrayList<PlanAdquisiciones>();
			p = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(OBJETO_ID_PROYECTO, proyectoId);
			PlanAdquisiciones plan = null;
			
			for(PlanAdquisiciones pl : p){
				plan = pl;
			}
			
			estructura.put("ocultarPagos", plan != null ? false : true);
			estructura.put("modificado", plan != null ? true : false);
			estructura.put("ocultarLimpiar", plan != null ? false : true);
			estructura.put("planAdquisicionId", plan != null ? plan.getId() : 0);
			estructura.put("contieneInfoPlan", plan != null ? true : false);
			estructura.put("cantidad", plan != null ? plan.getCantidad() : 0);
			estructura.put("unidadMedida", plan != null ? plan.getUnidadMedida().getId() : 0);
			estructura.put("costo", plan != null ? plan.getPrecioUnitario() : new BigDecimal(0));
			estructura.put("total", plan != null ? plan.getTotal() : new BigDecimal(0));
			estructura.put("planificadoDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocPlanificado()) : "");
			estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocReal()) : "");
			estructura.put("planificadoLanzamiento", plan != null ? Utils.formatDate(plan.getLanzamientoEventoPlanificado()) : "");
			estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getLanzamientoEventoReal()) : "");
			estructura.put("planificadoRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasPlanificado()) : "");
			estructura.put("realRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasReal()) : "");
			estructura.put("planificadoAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionPlanificado()) : "");
			estructura.put("realAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionReal()) : "");
			estructura.put("planificadoFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoPlanificado()) : "");
			estructura.put("realFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoReal()) : "");
			
			resultPrestamo.add(estructura);
			resultado.put(proyecto.getId()+","+OBJETO_ID_PROYECTO,estructura);
			
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);

			for (Componente componente : componentes){
				estructura = getEstructura();
				estructura.put("objetoTipo", OBJETO_ID_COMPONENTE);
				estructura.put("posicionArbol", 2);
				estructura.put("idObjetoTipo", componente.getId());
				estructura.put("nombre", componente.getNombre());
				estructura.put("idPredecesor", proyecto.getId());
				estructura.put("objetoTipoPredecesor", 1);
				
				p = new ArrayList<PlanAdquisiciones>();
				p = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(OBJETO_ID_COMPONENTE, componente.getId());
				plan = null;
				for(PlanAdquisiciones pl : p){
					plan = pl;
				}
				
				objetoPlan.add(componente.getId()+","+OBJETO_ID_COMPONENTE);

				estructura.put("ocultarPagos", plan != null ? false : true);
				estructura.put("modificado", plan != null ? true : false);
				estructura.put("ocultarLimpiar", plan != null ? false : true);
				estructura.put("planAdquisicionId", plan != null ? plan.getId() : 0);
				estructura.put("contieneInfoPlan", plan != null ? true : false);
				estructura.put("cantidad", plan != null ? plan.getCantidad() : 0);
				estructura.put("unidadMedida", plan != null ? plan.getUnidadMedida().getId() : 0);
				estructura.put("costo", plan != null ? plan.getPrecioUnitario() : new BigDecimal(0));
				estructura.put("total", plan != null ? plan.getTotal() : new BigDecimal(0));
				estructura.put("planificadoDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocPlanificado()) : "");
				estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocReal()) : "");
				estructura.put("planificadoLanzamiento", plan != null ? Utils.formatDate(plan.getLanzamientoEventoPlanificado()) : "");
				estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getLanzamientoEventoReal()) : "");
				estructura.put("planificadoRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasPlanificado()) : "");
				estructura.put("realRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasReal()) : "");
				estructura.put("planificadoAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionPlanificado()) : "");
				estructura.put("realAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionReal()) : "");
				estructura.put("planificadoFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoPlanificado()) : "");
				estructura.put("realFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoReal()) : "");
				
				resultPrestamo.add(estructura);
				resultado.put(componente.getId()+","+OBJETO_ID_COMPONENTE,estructura);
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);

				for (Producto producto : productos){
					estructura = getEstructura();
					
					estructura.put("objetoTipo", OBJETO_ID_PRODUCTO);
					estructura.put("posicionArbol", 3);
					estructura.put("idObjetoTipo", producto.getId());
					estructura.put("nombre", producto.getNombre());
					estructura.put("idPredecesor", componente.getId());
					estructura.put("objetoTipoPredecesor", 2);
					
					p = new ArrayList<PlanAdquisiciones>();
					p = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(OBJETO_ID_PRODUCTO, producto.getId());
					plan = null;
					for(PlanAdquisiciones pl : p){
						plan = pl;
					}
					
					objetoPlan.add(producto.getId()+","+OBJETO_ID_PRODUCTO);
					
					estructura.put("ocultarPagos", plan != null ? false : true);
					estructura.put("modificado", plan != null ? true : false);
					estructura.put("ocultarLimpiar", plan != null ? false : true);
					estructura.put("planAdquisicionId", plan != null ? plan.getId() : 0);
					estructura.put("contieneInfoPlan", plan != null ? true : false);
					estructura.put("cantidad", plan != null ? plan.getCantidad() : 0);
					estructura.put("unidadMedida", plan != null ? plan.getUnidadMedida().getId() : 0);
					estructura.put("costo", plan != null ? plan.getPrecioUnitario() : new BigDecimal(0));
					estructura.put("total", plan != null ? plan.getTotal() : new BigDecimal(0));
					estructura.put("planificadoDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocPlanificado()) : "");
					estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocReal()) : "");
					estructura.put("planificadoLanzamiento", plan != null ? Utils.formatDate(plan.getLanzamientoEventoPlanificado()) : "");
					estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getLanzamientoEventoReal()) : "");
					estructura.put("planificadoRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasPlanificado()) : "");
					estructura.put("realRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasReal()) : "");
					estructura.put("planificadoAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionPlanificado()) : "");
					estructura.put("realAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionReal()) : "");
					estructura.put("planificadoFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoPlanificado()) : "");
					estructura.put("realFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoReal()) : "");
					
					resultPrestamo.add(estructura);
					resultado.put(producto.getId()+","+OBJETO_ID_PRODUCTO,estructura);
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);

					for (Actividad actividad : actividades ){
						resultado = ObtenerActividades(actividad,usuario,OBJETO_ID_SUBPRODUCTO,producto.getId(), OBJETO_ID_PRODUCTO,resultado);
					}
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					resultado = ObtenerActividades(actividad,usuario,OBJETO_ID_PRODUCTO, componente.getId(),OBJETO_ID_COMPONENTE,resultado);
				}
			}
		}
		
		return resultado;
	}
	
	private LinkedHashMap<String, Map<String, Object>> ObtenerActividades(Actividad actividad, String usuario, int posicionArbol, 
			int idPredecesor, int objetoTipoPredecesor, LinkedHashMap<String, Map<String, Object>> resultado){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		Integer actividadId = actividad.getId();
		Map<String, Object> estructura = getEstructura();
		estructura.put("objetoTipo", OBJETO_ID_ACTIVIDAD);
		estructura.put("posicionArbol", posicionArbol);
		estructura.put("idObjetoTipo", actividad.getId());
		estructura.put("nombre", actividad.getNombre());
		estructura.put("idPredecesor", idPredecesor);
		estructura.put("objetoTipoPredecesor", objetoTipoPredecesor);

		List<PlanAdquisiciones> p = new ArrayList<PlanAdquisiciones>();
		p = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(OBJETO_ID_ACTIVIDAD, actividad.getId());
		PlanAdquisiciones plan = null;
		for(PlanAdquisiciones pl : p){
			plan = pl;
		}

		objetoPlan.add(actividadId+","+OBJETO_ID_ACTIVIDAD);
		
		estructura.put("ocultarPagos", plan != null ? false : true);
		estructura.put("modificado", plan != null ? true : false);
		estructura.put("ocultarLimpiar", plan != null ? false : true);
		estructura.put("planAdquisicionId", plan != null ? plan.getId() : 0);
		estructura.put("contieneInfoPlan", plan != null ? true : false);
		estructura.put("cantidad", plan != null ? plan.getCantidad() : 0);
		estructura.put("unidadMedida", plan != null ? plan.getUnidadMedida().getId() : 0);
		estructura.put("costo", plan != null ? plan.getPrecioUnitario() : new BigDecimal(0));
		estructura.put("total", plan != null ? plan.getTotal() : new BigDecimal(0));
		estructura.put("planificadoDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocPlanificado()) : "");
		estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getPreparacionDocReal()) : "");
		estructura.put("planificadoLanzamiento", plan != null ? Utils.formatDate(plan.getLanzamientoEventoPlanificado()) : "");
		estructura.put("realDocs", plan != null ? Utils.formatDate(plan.getLanzamientoEventoReal()) : "");
		estructura.put("planificadoRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasPlanificado()) : "");
		estructura.put("realRecepcionEval", plan != null ? Utils.formatDate(plan.getRecepcionOfertasReal()) : "");
		estructura.put("planificadoAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionPlanificado()) : "");
		estructura.put("realAdjudica", plan != null ? Utils.formatDate(plan.getAdjudicacionReal()) : "");
		estructura.put("planificadoFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoPlanificado()) : "");
		estructura.put("realFirma", plan != null ? Utils.formatDate(plan.getFirmaContratoReal()) : "");
		
		resultado.put(actividadId+","+OBJETO_ID_ACTIVIDAD,estructura);
			
		for(Actividad subActividad : actividades){
			resultado = ObtenerActividades(subActividad, usuario, posicionArbol + 1, actividad.getId(), OBJETO_ID_ACTIVIDAD,resultado);
		}
		
		return resultado;
	}
}
