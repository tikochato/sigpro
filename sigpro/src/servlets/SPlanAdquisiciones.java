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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ActividadPropiedadDAO;
import dao.ActividadPropiedadValorDAO;
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Actividad;
import pojo.ActividadPropiedad;
import pojo.ActividadPropiedadValor;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
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
		BigDecimal Costo;
		BigDecimal CostoReal;
		String fechaInicio;
		String fechaFin;
		int acumulacionCostos;
		String columnas;
		String planificadoDocs;
		String planificadoLanzamiento;
		String planificadoRecepcionEval;
		String planificadoAdjudica;
		String planificadoFirma;
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
				dataEjecutado.Costo = p.Costo;
				dataEjecutado.CostoReal = p.CostoReal;
				dataEjecutado.fechaInicio = p.fechaInicio;
				dataEjecutado.fechaFin = p.fechaFin;
				dataEjecutado.planificadoDocs = p.planificadoDocs == null ? "" : p.planificadoDocs;
				dataEjecutado.planificadoLanzamiento = p.planificadoLanzamiento == null ? "" : p.planificadoLanzamiento;
				dataEjecutado.planificadoRecepcionEval = p.planificadoRecepcionEval == null ? "" : p.planificadoRecepcionEval;
				dataEjecutado.planificadoAdjudica = p.planificadoAdjudica == null ? "" : p.planificadoAdjudica;
				dataEjecutado.planificadoFirma = p.planificadoFirma == null ? "" : p.planificadoFirma;
				resultPrestamo.add(dataEjecutado);	
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
	        response_text = String.join("", "\"componentes\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
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
			/*dataEjecutado.objetoTipo = OBJETO_ID_PROYECTO;
			dataEjecutado.posicionArbol = 1;
			dataEjecutado.idObjetoTipo = proyecto.getId();
			dataEjecutado.nombre = proyecto.getNombre();
			dataEjecutado.idPredecesor = 0;
			dataEjecutado.objetoTipoPredecesor = 0;
			lstdataEjecutado.add(dataEjecutado);*/
			
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
		
		if(actividad.getActividadTipo().getId() == 1){
			List<ActividadPropiedad> actividadpropiedades = ActividadPropiedadDAO.getActividadPropiedadesPorTipoActividad(actividad.getActividadTipo().getId());
			List<Date> valores = new ArrayList<Date>();
			for(ActividadPropiedad propiedad : actividadpropiedades){
				ActividadPropiedadValor valor = ActividadPropiedadValorDAO.getValorPorActividadYPropiedad(propiedad.getId(),actividad.getId());
				if (valor != null){
					valores.add(valor.getValorTiempo() == null ? null : valor.getValorTiempo());
				}
			}
			
			stInformePresupuesto dataEjecutado = new stInformePresupuesto();
			dataEjecutado = new stInformePresupuesto();
			dataEjecutado.objetoTipo = OBJETO_ID_ACTIVIDAD;
			dataEjecutado.posicionArbol = posicionArbol;
			dataEjecutado.idObjetoTipo = actividad.getId();
			dataEjecutado.nombre =   actividad.getNombre();
			dataEjecutado.idPredecesor = idPredecesor;
			dataEjecutado.objetoTipoPredecesor = objetoTipoPredecesor;
			dataEjecutado.Costo = actividad.getCosto();
			dataEjecutado.CostoReal = actividad.getCostoReal();
			if (valores.size() > 0){
				dataEjecutado.planificadoDocs = Utils.formatDate(valores.get(0));
				dataEjecutado.planificadoLanzamiento = Utils.formatDate(valores.get(1));
				dataEjecutado.planificadoRecepcionEval = Utils.formatDate(valores.get(2));
				dataEjecutado.planificadoAdjudica = Utils.formatDate(valores.get(3));
				dataEjecutado.planificadoFirma = Utils.formatDate(valores.get(4));
			}else{
				dataEjecutado.planificadoDocs = "";
				dataEjecutado.planificadoLanzamiento = "";
				dataEjecutado.planificadoRecepcionEval = "";
				dataEjecutado.planificadoAdjudica = "";
				dataEjecutado.planificadoFirma = "";
			}
			lstdataEjecutado.add(dataEjecutado);
			
			for(Actividad subActividad : actividades){
				lstdataEjecutado = ObtenerActividades(subActividad, usuario, lstdataEjecutado, posicionArbol + 1, actividad.getId(), OBJETO_ID_ACTIVIDAD);
			}
		}
		
		return lstdataEjecutado;
	}
}
