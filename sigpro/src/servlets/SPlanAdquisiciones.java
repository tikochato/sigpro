package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import dao.ActividadPropiedadDAO;
import dao.ActividadPropiedadValorDAO;
import dao.ComponenteDAO;
import dao.ConfiguracionesDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Actividad;
import pojo.ActividadPropiedad;
import pojo.ActividadPropiedadValor;
import pojo.ActividadPropiedadValorId;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
	private static Integer idTipoActividad;
	
	class stInformePresupuesto{
		int id;
		int idPrestamo;
		int objetoTipo;
		int idObjetoTipo;
		String nombre;
		int posicionArbol;
		int objetoTipoPredecesor;
		int idPredecesor;
		String hijo;
		String fechaInicio;
		String fechaFin;
		int acumulacionCostos;
		String columnas;
		String planificadoDocs;
		String realDocs;
		String planificadoLanzamiento;
		String realLanzamiento;
		String planificadoRecepcionEval;
		String realRecepcionEval;
		String planificadoAdjudica;
		String realAdjudica;
		String planificadoFirma;
		String realFirma;
		int tipo;
	}
       
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
		idTipoActividad = Utils.String2Int(ConfiguracionesDAO.getValorConfiguracionByNombre("IdTipoActividadPlanAdquisiciones"));
		
		if (accion.equals("generarPlan")){
			List<stInformePresupuesto> componentes = obtenerComponentes(idPrestamo,usuario);
			
			List<stInformePresupuesto> resultPrestamo = new ArrayList<stInformePresupuesto>();
			for (stInformePresupuesto p : componentes ){
				stInformePresupuesto dataEjecutado = new stInformePresupuesto();
				
				dataEjecutado = new stInformePresupuesto();
				dataEjecutado.id = p.id;
				dataEjecutado.idPrestamo = idPrestamo;
				dataEjecutado.objetoTipo = p.objetoTipo;
				dataEjecutado.posicionArbol = p.posicionArbol;
				dataEjecutado.idObjetoTipo = p.idObjetoTipo;
				dataEjecutado.nombre = p.nombre;
				dataEjecutado.idPredecesor = p.idPredecesor;
				dataEjecutado.objetoTipoPredecesor = p.objetoTipoPredecesor;
				dataEjecutado.fechaInicio = p.fechaInicio;
				dataEjecutado.fechaFin = p.fechaFin;
				dataEjecutado.planificadoDocs = p.planificadoDocs == null ? "" : p.planificadoDocs;
				dataEjecutado.realDocs = p.realDocs == null ? "" : p.realDocs;
				dataEjecutado.planificadoLanzamiento = p.planificadoLanzamiento == null ? "" : p.planificadoLanzamiento;
				dataEjecutado.realLanzamiento = p.realLanzamiento == null ? "" : p.realLanzamiento;
				dataEjecutado.planificadoRecepcionEval = p.planificadoRecepcionEval == null ? "" : p.planificadoRecepcionEval;
				dataEjecutado.realRecepcionEval = p.realRecepcionEval == null ? "" : p.realRecepcionEval;
				dataEjecutado.planificadoAdjudica = p.planificadoAdjudica == null ? "" : p.planificadoAdjudica;
				dataEjecutado.realAdjudica = p.realAdjudica == null ? "" : p.realAdjudica;
				dataEjecutado.planificadoFirma = p.planificadoFirma == null ? "" : p.planificadoFirma;
				dataEjecutado.realFirma = p.realFirma == null ? "" : p.realFirma;
				dataEjecutado.tipo = p.tipo;
				resultPrestamo.add(dataEjecutado);	
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
	        response_text = String.join("", "\"componentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("guardarPlan")){
			try{
				
				Integer id = Utils.String2Int(map.get("id"));
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
				
				int contador = 0;
				Actividad actividad = ActividadDAO.getActividadPorId(id, usuario);
				List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadesPorTipoActividad(actividad.getActividadTipo().getId());
				for(ActividadPropiedad actividadPropiedad : actividadpropiedades){
					ActividadPropiedadValorId idValor = new ActividadPropiedadValorId(actividad.getId(),actividadPropiedad.getId());
					ActividadPropiedadValor valor = new ActividadPropiedadValor(idValor, actividad, actividadPropiedad,null, null, null, null,
							usuario, null, new DateTime().toDate(), null, 1);
					
					if(contador == 0){
						valor.setValorTiempo(planificadoDocs);
					}else if(contador == 1){
						valor.setValorTiempo(realDocs);
					}else if(contador == 2){
						valor.setValorTiempo(planificadoLanzamiento);
					}else if(contador == 3){
						valor.setValorTiempo(realLanzamiento);
					}else if(contador == 4){
						valor.setValorTiempo(planificadoRecepcionEval);
					}else if(contador == 5){
						valor.setValorTiempo(realRecepcionEval);
					}else if(contador == 6){
						valor.setValorTiempo(planificadoAdjudica);
					}else if(contador == 7){
						valor.setValorTiempo(realAdjudica);
					}else if(contador == 8){
						valor.setValorTiempo(planificadoFirma);
					}else if(contador == 9){
						valor.setValorTiempo(realFirma);
					}
					
					contador++;
					ActividadPropiedadValorDAO.guardarActividadPropiedadValor(valor);
				}
			}
			catch (Throwable e) {
				e.printStackTrace();
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
	
	private List<stInformePresupuesto> obtenerComponentes(int proyectoId, String usuario){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		List<stInformePresupuesto> lstdataEjecutado = new ArrayList<>();
		if (proyecto!=null){
			stInformePresupuesto dataEjecutado = new stInformePresupuesto();
			
			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);
			for (Componente componente : componentes){
				dataEjecutado = new stInformePresupuesto();
				dataEjecutado.objetoTipo = OBJETO_ID_COMPONENTE;
				dataEjecutado.posicionArbol = 2;
				dataEjecutado.idObjetoTipo = componente.getId();
				dataEjecutado.nombre = componente.getNombre();
				dataEjecutado.idPredecesor = proyecto.getId();
				dataEjecutado.objetoTipoPredecesor = 1;
				lstdataEjecutado.add(dataEjecutado);
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				for (Producto producto : productos){
					dataEjecutado = new stInformePresupuesto();
					dataEjecutado.objetoTipo = OBJETO_ID_PRODUCTO;
					dataEjecutado.posicionArbol = 3;
					dataEjecutado.nombre = producto.getNombre();
					dataEjecutado.idObjetoTipo = producto.getId();
					dataEjecutado.idPredecesor = componente.getId();
					dataEjecutado.objetoTipoPredecesor = 2;
					lstdataEjecutado.add(dataEjecutado);
					
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){
						lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado,OBJETO_ID_SUBPRODUCTO,producto.getId(), OBJETO_ID_PRODUCTO);
					}
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado,OBJETO_ID_PRODUCTO, componente.getId(),OBJETO_ID_COMPONENTE);
				}
			}
		}
		
		return lstdataEjecutado;
	}
	
	private List<stInformePresupuesto> ObtenerActividades(Actividad actividad, String usuario, List<stInformePresupuesto> lstdataEjecutado, int posicionArbol, 
			int idPredecesor, int objetoTipoPredecesor){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		if(actividad.getActividadTipo().getId() == idTipoActividad){
			List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadesPorTipoActividad(actividad.getActividadTipo().getId());
			
			List<HashMap<String,Object>> campos = new ArrayList<>();
			for(ActividadPropiedad propiedad : actividadpropiedades){
				HashMap <String,Object> campo = new HashMap<String, Object>();
				ActividadPropiedadValor valor = ActividadPropiedadValorDAO.getValorPorActividadYPropiedad(propiedad.getId(),actividad.getId());
				campo.put("valor", valor.getValorTiempo());
				campos.add(campo);
			}
			
			stInformePresupuesto dataEjecutado = new stInformePresupuesto();
			dataEjecutado = new stInformePresupuesto();
			dataEjecutado.objetoTipo = OBJETO_ID_ACTIVIDAD;
			dataEjecutado.posicionArbol = posicionArbol;
			dataEjecutado.idObjetoTipo = actividad.getId();
			dataEjecutado.nombre =   actividad.getNombre();
			dataEjecutado.idPredecesor = idPredecesor;
			dataEjecutado.objetoTipoPredecesor = objetoTipoPredecesor;
			if (campos.size() > 0){
				Date valor = null;
				for(int i = 0; i < campos.size(); i++){
					HashMap <String,Object> campo = campos.get(i);
					valor = (Date)campo.get("valor");
					if (i == 0){
						dataEjecutado.planificadoDocs = Utils.formatDate(valor);
					}else if (i == 1){
						dataEjecutado.realDocs = Utils.formatDate(valor);
					}else if (i == 2){
						dataEjecutado.planificadoLanzamiento = Utils.formatDate(valor);
					}else if (i == 3){
						dataEjecutado.realLanzamiento = Utils.formatDate(valor);
					}else if (i == 4){
						dataEjecutado.planificadoRecepcionEval = Utils.formatDate(valor);
					}else if (i == 5){
						dataEjecutado.realRecepcionEval = Utils.formatDate(valor);
					}else if (i == 6){
						dataEjecutado.planificadoAdjudica = Utils.formatDate(valor);
					}else if (i == 7){
						dataEjecutado.realAdjudica = Utils.formatDate(valor);
					}else if (i == 8){
						dataEjecutado.planificadoFirma = Utils.formatDate(valor);
					}else if (i == 9){
						dataEjecutado.realFirma = Utils.formatDate(valor);
					}
					
				}

				dataEjecutado.tipo = 1;
			}else{
				dataEjecutado.planificadoDocs = "";
				dataEjecutado.realDocs = "";
				dataEjecutado.planificadoLanzamiento = "";
				dataEjecutado.realLanzamiento = "";
				dataEjecutado.planificadoRecepcionEval = "";
				dataEjecutado.realRecepcionEval = "";
				dataEjecutado.planificadoAdjudica = "";
				dataEjecutado.realAdjudica = "";
				dataEjecutado.planificadoFirma = "";
				dataEjecutado.realFirma = "";
				dataEjecutado.tipo = 1;
			}
			lstdataEjecutado.add(dataEjecutado);
			
			for(Actividad subActividad : actividades){
				lstdataEjecutado = ObtenerActividades(subActividad, usuario, lstdataEjecutado, posicionArbol + 1, actividad.getId(), OBJETO_ID_ACTIVIDAD);
			}
		}
		
		return lstdataEjecutado;
	}
}
