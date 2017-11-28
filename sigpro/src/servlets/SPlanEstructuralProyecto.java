package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.ObjetoCosto;
import dao.ObjetoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubComponenteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subcomponente;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanEstructuralProyecto")
public class SPlanEstructuralProyecto extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stplanestructuralproyecto{
		Integer objetoId;
		Integer objetoTipo;
		String nombre;
		Integer nivel;
		Integer duracion;
		String fechaInicial;
		String fechaFinal;
		String fechaInicialReal;
		String fechaFinReal;
		Integer avance;
		String acumulacionCosto;
		BigDecimal presupuestoAprobado;
		BigDecimal costoPlanificado;
		BigDecimal asignacionPresupuestariaVigente;
		BigDecimal presupuestoDevengado;
		double avanceFinanciero;
	}
	
	
    public SPlanEstructuralProyecto() {
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
		
		Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
		
		if(accion.equals("generarPlan")){
			try{
				String lineaBase = map.get("lineaBase");
				List<stplanestructuralproyecto> lstprestamo = generarPlan(proyectoId, lineaBase, usuario);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
		        response_text = String.join("", "\"proyecto\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}catch (Exception e){
				CLogger.write("1", SPlanEstructuralProyecto.class, e);
			}
		}else if(accion.equals("exportarExcel")){
			try{ 
				String lineaBase = map.get("lineaBase");
				byte [] outArray = exportarExcel(proyectoId, lineaBase, usuario);
				
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Plan_estructural_préstamo.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("4", SPlanEstructuralProyecto.class, e);
			}
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private byte[] exportarExcel(Integer proyectoId, String lineaBase, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(proyectoId, lineaBase, usuario);
			excel = new CExcel("Plan estructural del préstamo", false, null);
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			wb=excel.generateExcelOfData(datos, "Plan estructural del préstamo - "+proyecto.getNombre(), headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("3", SPlanEstructuralProyecto.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Duración", "Fecha Inicial", "Fecha Final", "Fecha Inicial Real", "Fecha Fin Real", "% de Avance", "Acumulación Costo", "Presupuesto Aprobado", "Costo Planificado", "Asignación Presupuestaria Vigente", "Presupuesto Devengado","% Avance Financiero"},  //titulos
			null, //mapeo
			{"string", "int", "string", "string", "string", "string", "double", "string", "double", "double", "double", "double", "double"}, //tipo dato
			null, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};				
			
		return headers;
	}
	
	public String[][] generarDatos(Integer proyectoId, String lineaBase, String usuario){
		String[][] datos = null;
		List<stplanestructuralproyecto> lstprestamo;
		try {
			lstprestamo = generarPlan(proyectoId, lineaBase, usuario);
			
			if (lstprestamo != null && !lstprestamo.isEmpty()){ 
				datos = new String[lstprestamo.size()][13];
				for (int i=0; i<lstprestamo.size(); i++){
					
					String sangria="";
					for(int s=1; s<lstprestamo.get(i).nivel; s++){
						sangria+="   ";
					}
						datos[i][0] = sangria + lstprestamo.get(i).nombre;
						datos[i][1] = lstprestamo.get(i).duracion.toString();
						datos[i][2] = lstprestamo.get(i).fechaInicial;
						datos[i][3] = lstprestamo.get(i).fechaFinal;
						datos[i][4] = lstprestamo.get(i).fechaInicialReal;
						datos[i][5] = lstprestamo.get(i).fechaFinReal;
						datos[i][6] = lstprestamo.get(i).avance != null ? lstprestamo.get(i).avance.toString() : null;
						datos[i][7] = lstprestamo.get(i).acumulacionCosto;
						datos[i][8] = lstprestamo.get(i).presupuestoAprobado != null ? lstprestamo.get(i).presupuestoAprobado.toString() : "0";
						datos[i][9] = lstprestamo.get(i).costoPlanificado != null ? lstprestamo.get(i).costoPlanificado.toString() : "0";
						datos[i][10] = lstprestamo.get(i).asignacionPresupuestariaVigente != null ? lstprestamo.get(i).asignacionPresupuestariaVigente.toString() : "0";
						datos[i][11] = lstprestamo.get(i).presupuestoDevengado != null ? lstprestamo.get(i).presupuestoDevengado.toString() : "0";
						datos[i][12] = new Double(lstprestamo.get(i).avanceFinanciero).toString();

				}
			}
		} catch (Exception e) {
			CLogger.write("1", SPlanEstructuralProyecto.class, e);
		}
		
		return datos;
	}
	
	private List<stplanestructuralproyecto> generarPlan(Integer IdProyecto, String lineaBase, String usuario) throws Exception{
		try{
			List<stplanestructuralproyecto> lstPrestamo = new ArrayList<>();
			
			Calendar now = Calendar.getInstance();
			int anio = now.get(Calendar.YEAR);
			
			List<ObjetoCosto> lstArbol = ObjetoDAO.getEstructuraConCosto(IdProyecto, anio, anio, true, true, true, lineaBase, usuario);
			
			stplanestructuralproyecto temp = null;
			
			for(ObjetoCosto objeto: lstArbol){
				Integer nivel = objeto.getNivel();
				if(nivel != null){
					temp = new stplanestructuralproyecto();
					temp.objetoId = objeto.getObjeto_id();
					temp.nombre = objeto.getNombre();
					temp.objetoTipo = objeto.getObjeto_tipo();
					temp.nivel = nivel;
					temp.duracion = objeto.getDuracion();
					temp.fechaInicial = Utils.formatDate(objeto.getFecha_inicial().toDate());
					temp.fechaFinal = Utils.formatDate(objeto.getFecha_final().toDate());
					temp.fechaInicialReal = objeto.getFecha_inicial_real() != null ? Utils.formatDate(objeto.getFecha_inicial_real().toDate()) : null;
					temp.fechaFinReal = objeto.getFecha_final_real() != null ? Utils.formatDate(objeto.getFecha_final_real().toDate()) : null;
					
					switch(temp.objetoTipo){
					case 1:
						Componente componente = ComponenteDAO.getComponente(temp.objetoId);
						temp.acumulacionCosto = componente.getAcumulacionCosto() != null ? componente.getAcumulacionCosto().getNombre() : null;
						break;
					case 2:
						Subcomponente subcomponente = SubComponenteDAO.getSubComponente(temp.objetoId);
						temp.acumulacionCosto = subcomponente.getAcumulacionCosto() != null ? subcomponente.getAcumulacionCosto().getNombre() : null;
						break;
					case 3:
						Producto producto = ProductoDAO.getProductoPorId(temp.objetoId);
						temp.acumulacionCosto = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getNombre() : null;
						break;
					case 4:
						Subproducto subproducto = SubproductoDAO.getSubproductoPorId(temp.objetoId);
						temp.acumulacionCosto = subproducto.getAcumulacionCosto() != null ? subproducto.getAcumulacionCosto().getNombre() : null;
						break;
					case 5:
						Actividad actividad = ActividadDAO.getActividadPorId(temp.objetoId);
						temp.avance = actividad.getPorcentajeAvance();
						temp.acumulacionCosto = actividad.getAcumulacionCosto() != null ? actividad.getAcumulacionCosto().getNombre() : null;
						break;
					}
					
					temp.costoPlanificado = objeto.getCosto();
					temp.presupuestoDevengado = objeto.getEjecutado();
					temp.presupuestoAprobado = objeto.getAsignado();
					temp.asignacionPresupuestariaVigente = objeto.getModificaciones();
					temp.avanceFinanciero = objeto.getModificaciones() != null && objeto.getEjecutado() != null ? (objeto.getModificaciones().compareTo(BigDecimal.ZERO) > 0 ? objeto.getEjecutado().divide(objeto.getModificaciones()).doubleValue() : new BigDecimal(0).doubleValue()) : new BigDecimal(0).doubleValue();
					lstPrestamo.add(temp);
				}
			}
			
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("2", SPlanEstructuralProyecto.class, e);
			return null;
		}
	}

}
