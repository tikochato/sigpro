package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.EstructuraProyectoDAO;
import dao.PlanAdquisicionDAO;
import dao.ProyectoDAO;
import pojo.Actividad;
import pojo.PlanAdquisicion;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stplanadquisiciones{
		Integer objetoId;
		Integer objetoTipo;
		Integer predecesorId;
		Integer objetoPredecesorTipo;
		Integer idPlanAdquisicion;
		String nombre;
		Integer nivel;
		Integer tipoAdquisicion;
		String tipoAdquisicionNombre;
		Integer categoriaAdquisicion;
		String categoriaAdquisicionNombre;
		String unidadMedida;
		Integer cantidad;
		BigDecimal costo;
		BigDecimal total;
		Long nog;
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
		String numeroContrato;
		BigDecimal montoContrato;
		List<String> hijos;
		Integer tipoRevision;
		String tipoRevisionNombre;
	}
	
    public SPlanAdquisiciones() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
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
			
			if (accion.equals("generarPlan")){
				
				try{
					String lineaBase = map.get("lineaBase");
					List<stplanadquisiciones> lstprestamo = generarPlan(proyectoId, lineaBase, usuario);
										
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			        response_text = String.join("", "\"proyecto\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}
				catch (Exception e){
					CLogger.write("2", SPlanAdquisiciones.class, e);
				}
			}else if(accion.equals("exportarExcel")){
				Integer idPlanAdquisicion = Utils.String2Int(map.get("idPlanAdquisicion"), null);
				try{ 
					String lineaBase = map.get("lineaBase");
					byte [] outArray = exportarExcel(idPlanAdquisicion, proyectoId, lineaBase, usuario);
					
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "attachment; Plan_de_Adquisiciones.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}catch(Exception e){
					CLogger.write("4", SPlanAdquisiciones.class, e);
				}
			}else if(accion.equals("exportarPdf")){
				CPdf archivo = new CPdf("Plan de adquisiciones");
				String headers[][];
				String datos[][];
				headers = generarHeaders();
				String lineaBase = map.get("lineaBase");
				datos = generarDatos(proyectoId, lineaBase, usuario);
				String path = archivo.exportarPlanAdquisiciones(headers, datos,usuario);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("5", SAdministracionTransaccional.class, e);
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
					response.setContentType("application/pdf");
					response.setContentLength(outArray.length);
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "in-line; 'PlanAdquisicion.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
			}
			else{
				response_text = "{ \"success\": false }";
			}
			
			if(!accion.equals("exportarExcel") && !accion.equals("exportarPdf")){
				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");

		        OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
		        gz.write(response_text.getBytes("UTF-8"));
		        gz.close();
		        output.close();
			}
		}catch(Exception e){
			CLogger.write("1", SPlanAdquisiciones.class, e);
		}
	}
	
	private List<stplanadquisiciones> generarPlan(Integer IdProyecto, String lineaBase, String usuario) throws Exception{
		try{
			List<stplanadquisiciones> lstPrestamo = new ArrayList<>();
			List<?> estruturaProyecto = EstructuraProyectoDAO.getEstructuraProyecto(IdProyecto, lineaBase);
			stplanadquisiciones temp = null;
			Integer proyectoId = 0;
			Integer componenteId = 0;
			Integer productoId = 0;
			Integer subProductoId = 0;
			Integer actividadId = 0;
			
			for(Object objeto: estruturaProyecto){
				Object[] obj = (Object[]) objeto;
				Integer nivel = (obj[3]!=null) ? ((String)obj[3]).length()/8 : 0;
				if(nivel != null){
					temp = new stplanadquisiciones();
					temp.objetoId = (Integer)obj[0];
					temp.nombre = (String)obj[1];
					temp.objetoTipo = ((BigInteger)obj[2]).intValue();
					temp.nivel = nivel;
					List<PlanAdquisicion> adquisiciones = null;
					inicializarPlanAdquisicion(temp);
					
					switch(temp.objetoTipo){
						case 1:
							temp.objetoPredecesorTipo = 0;
							temp.predecesorId = 0;
							proyectoId = temp.objetoId;
							
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
							
							adquisiciones = PlanAdquisicionDAO.getPlanAdquisicionesByObjetoLB(temp.objetoTipo, proyectoId, lineaBase);
							break;
						case 2:
							temp.objetoPredecesorTipo = 1;
							temp.predecesorId = proyectoId;
							componenteId = temp.objetoId;
							
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
							
							adquisiciones = PlanAdquisicionDAO.getPlanAdquisicionesByObjetoLB(temp.objetoTipo, componenteId, lineaBase);
						break;
						case 3:
							temp.objetoPredecesorTipo = 2;
							temp.predecesorId = componenteId;
							productoId = temp.objetoId;
							
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
							
							adquisiciones = PlanAdquisicionDAO.getPlanAdquisicionesByObjetoLB(temp.objetoTipo, productoId, lineaBase);
						break;
						case 4:
							temp.objetoPredecesorTipo = 3;
							temp.predecesorId = productoId;
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
							subProductoId = temp.objetoId;
							
							adquisiciones = PlanAdquisicionDAO.getPlanAdquisicionesByObjetoLB(temp.objetoTipo, subProductoId, lineaBase);
						break;
						case 5:
							Actividad actividad = ActividadDAO.getActividadPorId(temp.objetoId);
							if(actividad != null){
								temp.predecesorId = actividad.getObjetoId();
								temp.objetoPredecesorTipo = actividad.getPredObjetoTipo();
								temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
								actividadId = temp.objetoId;
								
								adquisiciones = PlanAdquisicionDAO.getPlanAdquisicionesByObjetoLB(temp.objetoTipo, actividadId, lineaBase);
							}
						break;
					}
					
					if(adquisiciones != null){
						for(PlanAdquisicion adquisicion : adquisiciones){
							temp.planificadoDocs = Utils.formatDate(adquisicion.getPreparacionDocPlanificado());
							temp.realDocs = Utils.formatDate(adquisicion.getPreparacionDocReal());
							temp.planificadoRecepcionEval = Utils.formatDate(adquisicion.getRecepcionOfertasPlanificado());
							temp.realRecepcionEval = Utils.formatDate(adquisicion.getRecepcionOfertasReal());
							temp.planificadoLanzamiento = Utils.formatDate(adquisicion.getLanzamientoEventoPlanificado());
							temp.realLanzamiento = Utils.formatDate(adquisicion.getLanzamientoEventoReal());
							temp.planificadoAdjudica = Utils.formatDate(adquisicion.getAdjudicacionPlanificado());
							temp.realAdjudica = Utils.formatDate(adquisicion.getAdjudicacionReal());
							temp.planificadoFirma = Utils.formatDate(adquisicion.getFirmaContratoPlanificado());
							temp.realFirma = Utils.formatDate(adquisicion.getFirmaContratoReal());
							temp.tipoAdquisicion = adquisicion.getTipoAdquisicion() != null ? adquisicion.getTipoAdquisicion().getId() : 0;
							temp.tipoAdquisicionNombre = adquisicion.getTipoAdquisicion() != null ? adquisicion.getTipoAdquisicion().getNombre() : "";
							temp.categoriaAdquisicion = adquisicion.getCategoriaAdquisicion() != null ? adquisicion.getCategoriaAdquisicion().getId() : 0;
							temp.categoriaAdquisicionNombre = adquisicion.getCategoriaAdquisicion() != null ? adquisicion.getCategoriaAdquisicion().getNombre() : "";
							temp.unidadMedida = adquisicion.getUnidadMedida();
							temp.cantidad = adquisicion.getCantidad() != null ? adquisicion.getCantidad() : 0;
							temp.costo = adquisicion.getPrecioUnitario() != null ? adquisicion.getPrecioUnitario() : new BigDecimal(0);
							temp.total = adquisicion.getTotal() != null ? adquisicion.getTotal() : new BigDecimal(0);
							temp.tipoRevision = adquisicion.getTipoRevision();
							temp.tipoRevisionNombre = temp.tipoRevision != null ? temp.tipoRevision == 1 ? "Ex-ante" : temp.tipoRevision == 2 ? "Ex-Post" : null : null;
							temp.nog = adquisicion.getNog();
							temp.numeroContrato = adquisicion.getNumeroContrato();
							temp.montoContrato = adquisicion.getMontoContrato().compareTo(BigDecimal.ZERO) > 0 ? adquisicion.getMontoContrato() : null;							
						}
						lstPrestamo.add(temp);
					}	
				}
			}
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("1", SPlanAdquisiciones.class, e);
			return null;
		}
	}
	
	private void inicializarPlanAdquisicion(stplanadquisiciones tempPrestamo){
		tempPrestamo.idPlanAdquisicion = 0;
		tempPrestamo.tipoAdquisicion = 0;
		tempPrestamo.tipoAdquisicionNombre = "";
		tempPrestamo.categoriaAdquisicion = 0;
		tempPrestamo.categoriaAdquisicionNombre = "";
		tempPrestamo.unidadMedida = "";
		tempPrestamo.cantidad = 0;
		tempPrestamo.costo = new BigDecimal(0);
		tempPrestamo.total = new BigDecimal(0);
		tempPrestamo.planificadoDocs = null;
		tempPrestamo.realDocs = null;
		tempPrestamo.planificadoLanzamiento = null;
		tempPrestamo.realLanzamiento = null;
		tempPrestamo.planificadoRecepcionEval = null;
		tempPrestamo.realRecepcionEval = null;
		tempPrestamo.planificadoAdjudica = null;
		tempPrestamo.realAdjudica = null;
		tempPrestamo.planificadoFirma = null;
		tempPrestamo.realFirma = null;
		tempPrestamo.numeroContrato = null;
		tempPrestamo.nog = null;
		tempPrestamo.montoContrato = new BigDecimal(0);
	}
		
	private byte[] exportarExcel(Integer idPlanAdquisicion, Integer idPrestamo, String lineaBase, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamo, lineaBase, usuario);
			excel = new CExcel("Plan de Adquisiciones", false, null);
			Proyecto proyecto = ProyectoDAO.getProyecto(idPrestamo);
			wb=excel.generateExcelOfData(datos, "Plan de Adquisiciones - "+proyecto.getNombre(), headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("3", SPlanAdquisiciones.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Tipo de Adquisicion", "Unidad de Medida", "Categoria de Aquisicion", "Cantidad", "Costo", "Total", "Preparacion de Documentos", "", "Lanzamiento de Evento","", 
				"Recepcion y Evaluacion de Ofertas", "", "Adjudicacion", "", "Firma de Contrato", ""},  //titulos
			null, //mapeo
			{"string", "string", "string", "string", "double", "double", "double", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string"}, //tipo dato
			null, //operaciones columnas
			null, //operaciones div
			{"", "", "", "", "", "", "", "Planificado", "Real", "Planificado","Real", 
				"Planificado", "Real", "Planificado", "Real", "Planificado", "Real"},
			null,
			null
			};				
			
		return headers;
	}
	
	public String[][] generarDatos(Integer idPrestamo, String lineaBase, String usuario){
		String[][] datos = null;
		List<stplanadquisiciones> lstprestamo;
		try {
			lstprestamo = generarPlan(idPrestamo, lineaBase, usuario);
			
			if (lstprestamo != null && !lstprestamo.isEmpty()){ 
				datos = new String[lstprestamo.size()][17];
				for (int i=0; i<lstprestamo.size(); i++){
					
					String sangria="";
					for(int s=1; s<lstprestamo.get(i).nivel; s++){
						sangria+="   ";
					}
						datos[i][0] = sangria + lstprestamo.get(i).nombre;
						datos[i][1] = lstprestamo.get(i).tipoAdquisicionNombre;
						datos[i][2] = lstprestamo.get(i).unidadMedida;
						datos[i][3] = lstprestamo.get(i).categoriaAdquisicionNombre;
						datos[i][4] = lstprestamo.get(i).cantidad.toString();
						datos[i][5] = lstprestamo.get(i).costo.toString();
						datos[i][6] = lstprestamo.get(i).total.toString();
						datos[i][7] = lstprestamo.get(i).planificadoDocs;
						datos[i][8] = lstprestamo.get(i).realDocs;
						datos[i][9] = lstprestamo.get(i).planificadoLanzamiento;
						datos[i][10] = lstprestamo.get(i).realLanzamiento;
						datos[i][11] = lstprestamo.get(i).planificadoRecepcionEval;
						datos[i][12] = lstprestamo.get(i).realRecepcionEval;
						datos[i][13] = lstprestamo.get(i).planificadoAdjudica;
						datos[i][14] = lstprestamo.get(i).realAdjudica;
						datos[i][15] = lstprestamo.get(i).planificadoFirma;
						datos[i][16] = lstprestamo.get(i).realFirma;
				}
			}
		} catch (Exception e) {
			CLogger.write("1", SPlanAdquisiciones.class, e);
		}
		
return datos;
	}
}