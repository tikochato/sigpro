package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.AsignacionRaciDAO;
import dao.EstructuraProyectoDAO;
import dao.ProyectoDAO;
import pojo.Actividad;
import pojo.Colaborador;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;


@WebServlet("/SCargaTrabajo")
public class SCargaTrabajo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stEstructura{
		String idComponentes="";
		String idSubComponentes="";
		String idPrestamos="";
		String idProductos="";
		String idSubproductos="";
	}
	
	class stcargatrabajo{
		int id;
		String responsable;
		int actividadesAtrasadas;
		int actividadesAlerta;
		int actividadesACumplir;
		int actividadesCompletadas;
	}
	
	class stactividadterminada{
		int anio;
		int mes;
		BigInteger total;
	}
	
	class stestructuracolaborador{
		int objetoId;
		int objetoTipo;
		int nivel;
		String nombre;
		int estado;
		String nombreEstado;
		boolean mostrar;
		String fechaInicio;
		String fechaFin;
		String fechaInicioReal;
		String fechaFinReal;
	}
	
	class stcolaborador{
		int id;
		String nombre;
		String apellido;
		String label;
	}
	
    public SCargaTrabajo() {
        super();
    
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		
		if(accion.equals("getEstructrua")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			stEstructura estructura = new stEstructura();
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
			if(proyecto != null){
				String lineaBase = map.get("lineaBase");
				List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo, lineaBase);
				for(Object objeto : estructuraProyecto){
					Object[] obj = (Object[]) objeto;
					int objeto_id = (Integer)obj[0];
					int objeto_tipo = ((BigInteger) obj[2]).intValue();
					switch(objeto_tipo){
					case 0: 
						estructura.idPrestamos += estructura.idPrestamos.isEmpty() ? objeto_id : ","+objeto_id;
						break;
					case 1: 
						estructura.idComponentes += estructura.idComponentes.isEmpty() ? objeto_id : ","+objeto_id;
						break;
					case 2: 
						estructura.idSubComponentes += estructura.idSubComponentes.isEmpty() ? objeto_id : ","+objeto_id;
						break;
					case 3: 
						estructura.idProductos += estructura.idProductos.isEmpty() ? objeto_id : ","+objeto_id;
						break;
					case 4: 
						estructura.idSubproductos += estructura.idSubproductos.isEmpty() ? objeto_id : ","+objeto_id;
						break;
					}
				}
				response_text=new GsonBuilder().serializeNulls().create().toJson(estructura);
				response_text = String.join("", "\"estructura\":",response_text);
				response_text = String.join("", "{\"success\":true,", response_text, "}");
				}else{
					response_text = String.join("", "{\"success\":false}");
				}
			}else if(accion.equals("getCargaTrabajoPrestamo")){
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				Integer idPrestamos = Utils.String2Int(map.get("idPrestamos"));
				String lineaBase = map.get("lineaBase");
								
				List<stcargatrabajo> cargas = getCargaTrabajoPrestamo(idPrestamos,  anio_inicio, anio_fin, lineaBase);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(cargas);
		        response_text = String.join("", "\"cargatrabajo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.equals("getActividadesTerminadas")){
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				Integer proyectoId = map.get("idPrestamos") != null && map.get("idPrestamos").length() > 0? Utils.String2Int(map.get("idPrestamos")) : 0;
				Integer colaboradorId = Utils.String2Int(map.get("colaboradorid"));
							
				List<?> objActividades =ActividadDAO.getActividadesTerminadas(proyectoId, colaboradorId, anio_inicio, anio_fin, null);
				
				List<stactividadterminada> actividadesTerminadas = new ArrayList<>();
				if (objActividades!=null && objActividades.size() > 0)
					for(Object obj : objActividades){
						stactividadterminada actividadTterminada = new stactividadterminada();
						actividadTterminada.anio = (Integer)((Object[]) obj)[0];
						actividadTterminada.mes = (Integer)((Object[]) obj)[1];
						actividadTterminada.total = (BigInteger)((Object[]) obj)[2];
						actividadesTerminadas.add(actividadTterminada);
					}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(actividadesTerminadas);
		        response_text = String.join("", "\"actividadesterminadas\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
		        
			}else if(accion.equals("getEstructruaPorResponsable")){
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				String idColaboradores = map.get("idColaboradores");
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				
				if(idPrestamo > 0){
					ArrayList<stestructuracolaborador> estructuracolaborador = new ArrayList<>();
					String lineaBase = map.get("lineaBase");
					List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo, lineaBase);
					stestructuracolaborador stprestamo=null;
					stestructuracolaborador stcomponente=null;
					stestructuracolaborador stsubcomponente=null;
					stestructuracolaborador stproducto=null;
					stestructuracolaborador stsubproducto=null;
					
					for(Object objeto : estructuraProyecto){
						Object[] obj = (Object[]) objeto;
						Integer objeto_id = (Integer)obj[0];
						String nombre = (String)obj[1];
						Integer objeto_tipo = ((BigInteger) obj[2]).intValue();
						Integer nivel = (obj[3]!=null) ? ((String)obj[3]).length()/8 : 0;
							
						switch(objeto_tipo){
						case 0: 
							stprestamo = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);
							stprestamo.nivel = nivel;
							stcomponente=null;
							stsubcomponente=null;
							stproducto=null;
							stsubproducto=null;
							break;
						case 1: 
							stcomponente = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);
							stcomponente.nivel = nivel;
							stproducto=null;
							stsubcomponente=null;
							stsubproducto=null;
							break;
						case 2: 
							stsubcomponente = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);
							stsubcomponente.nivel = nivel;
							stproducto=null;
							stsubproducto=null;
							break;
						case 3: 
							stproducto = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);
							stproducto.nivel = nivel;
							stsubproducto=null;
							break;
						case 4: 
							stsubproducto = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);
							stsubproducto.nivel = nivel;
							break;
						case 5: 
							Actividad objActividad = ActividadDAO.getActividadPorIdResponsable(objeto_id,  idColaboradores, "r",lineaBase);
							
							if (objActividad!=null){
								stestructuracolaborador stactividad = construirItemPorColaborador(
										objActividad.getNombre(), objActividad.getId(), 5, true,objActividad.getFechaInicio(),objActividad.getFechaFin(),
										objActividad.getFechaInicioReal(),objActividad.getFechaFinReal());
								stactividad.nivel = nivel;
								getEstado(stactividad, objActividad, anio_inicio, anio_fin);
								if(stactividad.estado > 0){
									estructuracolaborador.add(stactividad);
									if(stprestamo!=null) stprestamo.mostrar=true;
									if(stcomponente!=null) stcomponente.mostrar=true;
									if(stsubcomponente!=null) stsubcomponente.mostrar=true;
									if(stproducto!=null) stproducto.mostrar = true;
									if(stsubproducto!=null) stsubproducto.mostrar = true;
								}
							}
							break;
						}		
						
					}
					
					ArrayList<stestructuracolaborador> tempestructrua = new ArrayList<>();
					for (stestructuracolaborador temp:estructuracolaborador){
							tempestructrua.add(temp);
					}
					
					response_text=new GsonBuilder().serializeNulls().create().toJson(tempestructrua);
			        response_text = String.join("", "\"estructura\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text,"}");
												 
					}else{
						response_text = String.join("", "{\"success\":false}");
					}
			}else if (accion.equals("getResponsables")){
				Integer proyectoId = Utils.String2Int(map.get("idPrestamo"));
				List<Colaborador> colaboradores = AsignacionRaciDAO.getColaboradoresPorProyecto(proyectoId, null);
				List<stcolaborador> stcolaboradores = new ArrayList<>();
				for (Colaborador c : colaboradores){
					stcolaborador temp = new stcolaborador();
					temp.id = c.getId();
					temp.label = String.join(" ", c.getPnombre(),c.getPapellido());
					stcolaboradores.add(temp);
				}
				response_text=new GsonBuilder().serializeNulls().create().toJson(stcolaboradores);
		        response_text = String.join("", "\"colaboradores\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
				
			}else if (accion.equals("exportarExcel")){
				try{
					Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
					Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
					Integer idPrestamos = Utils.String2Int(map.get("idPrestamos"));
					String lineaBase = map.get("lineaBase");
					String idComponentes = map.get("idComponentes") != null && map.get("idComponentes").length() > 0 ? map.get("idComponentes") : "0";
					String idSubComponentes = map.get("idSubComponentes") != null && map.get("idSubComponentes").length() > 0 ? map.get("idSubComponentes") : "0";
					String idProductos = map.get("idProductos") != null && map.get("idProductos").length() > 0 ? map.get("idProductos") : "0";
					String idSubproductos = map.get("idSubproductos") != null && map.get("idSubproductos").length() > 0 ? map.get("idSubproductos") : "0";
					
			        byte [] outArray = exportarExcel(idPrestamos, idComponentes, idSubComponentes, idProductos, idSubproductos, anio_inicio, anio_fin, usuario, lineaBase);
				
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "attachment; CargaTrabajo_de_Trabajo.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}catch(Exception e){
				    CLogger.write("2", SCargaTrabajo.class, e);
				}
			}else if(accion.equals("exportarPdf")){
				CPdf archivo = new CPdf("Carga de trabajo");
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				Integer idPrestamos = Utils.String2Int(map.get("idPrestamos"));
				String lineaBase = map.get("lineaBase");
				String idComponentes = map.get("idComponentes") != null && map.get("idComponentes").length() > 0 ? map.get("idComponentes") : "0";
				String idSubComponentes = map.get("idSubComponentes") != null && map.get("idSubComponentes").length() > 0 ? map.get("idSubComponentes") : "0";
				String idProductos = map.get("idProductos") != null && map.get("idProductos").length() > 0 ? map.get("idProductos") : "0";
				String idSubproductos = map.get("idSubproductos") != null && map.get("idSubproductos").length() > 0 ? map.get("idSubproductos") : "0";
				String headers[][];
				String datos[][];
				headers = generarHeaders();
				datos = generarDatos(idPrestamos, idComponentes, idSubComponentes, idProductos, idSubproductos, anio_inicio, anio_fin, usuario, lineaBase);
				String path = archivo.ExportarPdfCargaTrabajo(headers, datos,usuario);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("5", SInformacionPresupuestaria.class, e);
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
					response.setHeader("Content-Disposition", "in-line; 'EjecucionPresupuestaria.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
			}else if (accion.equals("exportarEstructuraExcel")){
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				String idColaboradores = map.get("idColaboradores");
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				String lineaBase = map.get("lineaBase");
				
				byte [] outArray = exportarEstructuraExcel(idPrestamo, idColaboradores, anio_inicio, anio_fin,  usuario, lineaBase);
				 
					
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "attachment; CargaTrabajo_de_Trabajo.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				
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
		}
	
	private void agregarCarga(List<stcargatrabajo> cargas, Colaborador colaborador, 
			Integer completadas,Integer retrasadas, Integer aCumplir, Integer alerta){
		boolean existe = false;
		for (stcargatrabajo carga : cargas){
			if (colaborador.getId() == carga.id){
				carga.actividadesAtrasadas = carga.actividadesAtrasadas + retrasadas;
				carga.actividadesCompletadas = carga.actividadesCompletadas + completadas;
				carga.actividadesAlerta = carga.actividadesAlerta + alerta;
				carga.actividadesACumplir = carga.actividadesACumplir + aCumplir;
				existe = true;
				break;
			}
		}
		if (!existe){
			stcargatrabajo carga = new stcargatrabajo();
			carga.id = colaborador.getId();
			carga.responsable = colaborador.getPnombre() + " " + colaborador.getPapellido();
			carga.actividadesAtrasadas = retrasadas;
			carga.actividadesCompletadas = completadas;
			carga.actividadesAlerta = alerta;
			carga.actividadesACumplir = aCumplir;
			cargas.add(carga);
		}
	}
	
	private List<stcargatrabajo> getCargaTrabajoPrestamo(Integer idPrestamos,  Integer anio_inicio, Integer anio_fin, String lineaBase){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPorObjetos(idPrestamos, anio_inicio,anio_fin, lineaBase);
		
		List<stcargatrabajo> cargas = new ArrayList<stcargatrabajo>();
		
		for(Actividad actividad : actividades){
				
				Colaborador colaborador = AsignacionRaciDAO.getResponsablePorRol(actividad.getId(), 5, "r", null);
								
				Date hoy = new Date();
				Date siguienteSemana = sumarDiasFecha(hoy, 7);
				Date siguienteMes = sumarDiasFecha(siguienteSemana, 23);

				if (colaborador !=null){
					if (actividad.getPorcentajeAvance() != 100){
						if (actividad.getFechaFin().before(hoy)){
							agregarCarga(cargas, colaborador, 0, 1, 0, 0);
						}else {
							if (actividad.getFechaFin().after(hoy) && actividad.getFechaFin().before(siguienteSemana)){
								agregarCarga(cargas, colaborador, 0, 0, 0, 1);
							}else if (actividad.getFechaFin().after(siguienteSemana) && actividad.getFechaFin().before(siguienteMes)){
								agregarCarga(cargas, colaborador, 0, 0, 1, 0);
							}
						}
						
					}else{
						agregarCarga(cargas, colaborador, 1, 0, 0, 0);
					}
				}
		}
		return cargas;
	}
	
	public Date sumarDiasFecha(Date fecha, int dias){
		Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fecha); 
		      calendar.add(Calendar.DAY_OF_YEAR, dias);  
		      return calendar.getTime(); 
    }
	
	public Date sumarDiasMes(Date fecha, int dias){
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(fecha); 
        cal.add(Calendar.MONTH, 1);
        return  cal.getTime();
    }
	
	private stestructuracolaborador construirItemPorColaborador(String nombre, Integer objetoId, Integer objetoTipo,
			boolean mostrar,Date fecha_inicio, Date fecha_fin, Date fechaInicioReal, Date fechaFinReal){
		stestructuracolaborador temp = new stestructuracolaborador();
		temp.nombre = nombre;
		temp.objetoId = objetoId;
		temp.objetoTipo = objetoTipo;
		temp.mostrar = mostrar;
		temp.fechaInicio = Utils.formatDate(fecha_inicio);
		temp.fechaFin = Utils.formatDate(fecha_fin);
		temp.fechaInicioReal = Utils.formatDate(fechaInicioReal);
		temp.fechaFinReal = Utils.formatDate(fechaFinReal);
		return temp;
		
				
	}
	
	private void getEstado(stestructuracolaborador temp, Actividad actividad, int anio_inicio, int anio_fin){
		Date hoy = new Date();
		Date siguienteSemana = sumarDiasFecha(hoy, 7);
		Date siguienteMes = sumarDiasFecha(siguienteSemana, 23);
		
		DateTime fechaInicial = new DateTime(actividad.getFechaInicio());
		int actividad_Inicio = fechaInicial.getYear();
		DateTime fechaFin = new DateTime(actividad.getFechaFin());
		int actividad_Fin = fechaFin.getYear();
		

		if(anio_inicio <= actividad_Inicio && anio_inicio <= actividad_Fin && anio_fin >= actividad_Inicio && actividad_Fin <= anio_fin){
			if (actividad.getPorcentajeAvance() != 100){
				if (actividad.getFechaFin().before(hoy)){
					temp.nombreEstado ="Retrasadas";
					temp.estado = 1;
				}else if (actividad.getFechaFin().after(hoy) && actividad.getFechaFin().before(siguienteSemana)){
					temp.nombreEstado ="Alerta";
					temp.estado = 2;
				}else if (actividad.getFechaFin().after(siguienteSemana) && actividad.getFechaFin().before(siguienteMes)){
					temp.nombreEstado ="A cumplir";
					temp.estado = 3;
				}else{
					temp.estado=0;
				}
				
			}else{
				temp.nombreEstado ="Completadas";
				temp.estado = 4;
			}
		}else{
			temp.estado=0;
		}
	}
	
	private byte[] exportarExcel(Integer idPrestamos, String idComponentes, String idSubComponentes, String idProductos, String idSubproductos, Integer anio_inicio, Integer anio_fin, String usuario, String lineaBase) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamos, idComponentes, idSubComponentes, idProductos, idSubproductos, anio_inicio, anio_fin, usuario, lineaBase);
			CGraficaExcel grafica = generarGrafica(datos);
			excel = new CExcel("Carga de Trabajo", false, grafica);
			Proyecto proyecto = ProyectoDAO.getProyecto(idPrestamos);
			wb=excel.generateExcelOfData(datos, "Carga de Trabajo - "+proyecto.getNombre(), headers, null, true, usuario);
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
		    CLogger.write("1", SCargaTrabajo.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Responsable", "Actividades Retrasadas", "Actividades en Alerta", "Actividades a Cumplir", "Actividades Completadas"},  //titulos
			null, //mapeo
			{"string", "int", "int", "int", "int"}, //tipo dato
			{"", "sum", "sum", "sum", "sum"}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatos(Integer idPrestamos, String idComponentes, String idSubComponentes, String idProductos, String idSubproductos, Integer anio_inicio, Integer anio_fin, String usuario, String lineaBase){
		List<stcargatrabajo> cargas = getCargaTrabajoPrestamo(idPrestamos, anio_inicio, anio_fin, lineaBase);
		String[][] datos = null;
		
		if (cargas != null && !cargas.isEmpty()){ 
			datos = new String[cargas.size()][5];
			for (int i=0; i<cargas.size(); i++){
				datos[i][0]=cargas.get(i).responsable;
				datos[i][1]=String.valueOf(cargas.get(i).actividadesAtrasadas);
				datos[i][2]=String.valueOf(cargas.get(i).actividadesAlerta);
				datos[i][3]=String.valueOf(cargas.get(i).actividadesACumplir);
				datos[i][4]=String.valueOf(cargas.get(i).actividadesCompletadas);
			}
		}
			
		return datos;
	}
	
	public CGraficaExcel generarGrafica(String[][] datosTabla){
		
		String[][] datos = new String[][]{
			{"Actividades Retrasadas", "Actividades en Alerta", "Actividades a Cumplir", "Actividades Completadas"},
			{"0","0","0","0"}
		};
		String[][] datosIgualar= new String[][]{
			{"","","",""},
			{"1."+(datosTabla.length+30),"2."+(datosTabla.length+30),"3."+(datosTabla.length+30),"4."+(datosTabla.length+30)}
		};
		
		String[] tipoData = new String[]{"string","int"};
		CGraficaExcel grafica = new CGraficaExcel("Carga de Trabajo", CGraficaExcel.EXCEL_CHART_PIE, "Cantidad", "Estados", datos, tipoData, datosIgualar);
	
		return grafica;
	}
	
	
	private byte[] exportarEstructuraExcel(Integer idPrestamos, String idsColaboradores, Integer anio_inicio, Integer anio_fin, String usuario, String lineaBase) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeadersEstructura();
			datos = generarDatosEstructura(idPrestamos, idsColaboradores, anio_inicio,  anio_fin, usuario, lineaBase);
			
			excel = new CExcel("Carga de Trabajo", false, null);
			wb=excel.generateExcelOfData(datos, "Estado actividades asignadas", headers, null, true, usuario);
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
		    CLogger.write("1", SCargaTrabajo.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeadersEstructura(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Estado","Fecha Inicio", "Fecha Fin","Fecha Inicio Real", "Fecha Fin Real"},  //titulos
			null, //mapeo
			{"string","string","string", "string","string","string"}, //tipo dato
			{"", "", "", "", "","",""}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatosEstructura(Integer idPrestamo, String idsColaboradores, Integer anio_inicio, Integer anio_fin, String usuario, String lineaBase){
		
		ArrayList<stestructuracolaborador> estructura = getEstructura(idPrestamo, idsColaboradores, anio_inicio, anio_fin, lineaBase);
		
		String[][] datos = null;
		int i = 0;
		datos = new String[estructura.size()][6];
		for (stestructuracolaborador estr : estructura){
			String indent="";
			for(int s=1; s<estr.nivel; s++){
				indent+="  ";
			}
			datos[i][0] = indent +  estr.nombre;
			datos[i][1] = estr.nombreEstado;
			datos[i][2] = estr.fechaInicio;
			datos[i][3] = estr.fechaFin;
			datos[i][4] = estr.fechaInicioReal;
			datos[i][5] = estr.fechaFinReal;
			
			i++;
		}	
		return datos;
	}
	
	public ArrayList<stestructuracolaborador> getEstructura(Integer idPrestamo, String idColaboradores
			, Integer anio_inicio, Integer anio_fin, String lineaBase){
		ArrayList<stestructuracolaborador> estructuracolaborador = new ArrayList<>();
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo, lineaBase);
		stestructuracolaborador stprestamo=null;
		stestructuracolaborador stcomponente=null;
		stestructuracolaborador stsubcomponente=null;
		stestructuracolaborador stproducto=null;
		stestructuracolaborador stsubproducto=null;
		
		for(Object objeto : estructuraProyecto){
			Object[] obj = (Object[]) objeto;
			Integer objeto_id = (Integer)obj[0];
			String nombre = (String)obj[1];
			Integer objeto_tipo = ((BigInteger) obj[2]).intValue();
			Integer nivel = (obj[3]!=null) ? ((String)obj[3]).length()/8 : 0;
				
			switch(objeto_tipo){
			case 0: 
				stprestamo = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);						 
				stprestamo.nivel = nivel;
				estructuracolaborador.add(stprestamo);
				stcomponente=null;
				stsubcomponente=null;
				stproducto=null;
				stsubproducto=null;
				break;
			case 1: 
				stcomponente = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);	
				stcomponente.nivel = nivel;
				estructuracolaborador.add(stcomponente);
				stsubcomponente=null;
				stproducto=null;
				stsubproducto=null;
				break;
			case 2: 
				stsubcomponente = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);	
				stsubcomponente.nivel = nivel;
				estructuracolaborador.add(stsubcomponente);
				stproducto=null;
				stsubproducto=null;
				break;
			case 3: 
				stproducto = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);						 
				stproducto.nivel = nivel;
				estructuracolaborador.add(stproducto);
				stsubproducto=null;
				break;
			case 4: 
				stsubproducto = construirItemPorColaborador(nombre, objeto_id, objeto_tipo, false,null,null,null,null);						 
				stsubproducto.nivel = nivel;
				estructuracolaborador.add(stsubproducto);
				break;
			case 5: 
				Actividad objActividad = ActividadDAO.getActividadPorIdResponsable(objeto_id,  idColaboradores, "r",lineaBase);
				if (objActividad!=null){
					stestructuracolaborador stactividad = construirItemPorColaborador(
							objActividad.getNombre(), objActividad.getId(), 5, true,objActividad.getFechaInicio(),objActividad.getFechaFin(),
							objActividad.getFechaInicioReal(),objActividad.getFechaFinReal());
					stactividad.nivel = nivel;
					getEstado(stactividad, objActividad, anio_inicio, anio_fin);
					if(stactividad.estado > 0){
						estructuracolaborador.add(stactividad);
						if(stprestamo!=null) stprestamo.mostrar=true;
						if(stcomponente!=null) stcomponente.mostrar=true;
						if(stsubcomponente!=null) stsubcomponente.mostrar=true;
						if(stproducto!=null) stproducto.mostrar = true;
						if(stsubproducto!=null) stsubproducto.mostrar = true;
					}
				}
				break;
			}						
		}
		
		ArrayList<stestructuracolaborador> tempestructrua = new ArrayList<>();
		for (stestructuracolaborador temp:estructuracolaborador){
			if(temp.mostrar==true)
				tempestructrua.add(temp);
		}
		return tempestructrua;
	}
}