package servlets;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.AsignacionRaciDAO;
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Colaborador;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CMariaDB;
import utilities.Utils;


@WebServlet("/SCargaTrabajo")
public class SCargaTrabajo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		String nombre;
		int estado;
		String nombreEstado;
		boolean mostrar;
		String fechaInicio;
		String fechaFin;
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
			
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
			if(proyecto != null){
				if(CMariaDB.connect()){
						Connection conn = CMariaDB.getConnection();
						ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
						
						String items_componente="";
						for(Integer componente:componentes){
							
							Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
							ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
							String items_producto = "";
							for(Integer producto: productos){
								Producto objProducto = ProductoDAO.getProductoPorId(producto);
								
								ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
								String items_subproducto = "";
								for(Integer subproducto: subproductos){
									Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
									items_subproducto = String.join(items_subproducto.length()>0 ? ",":"", 
											items_subproducto,
											construirItem(objSubProducto.getNombre(), objSubProducto.getId(), 
													4, "sp_"+objSubProducto.getId(),null));
								}
								
								items_producto = String.join(items_producto.length()>0 ? ",":"", 
										 items_producto,
										construirItem(objProducto.getNombre(), objProducto.getId(), 
												3, "pr_"+objProducto.getId(),items_subproducto));
								
								items_subproducto = "";
							}
							items_componente = String.join(items_componente.length()>0 ? ",":"", 
									 items_componente,
									construirItem(objComponente.getNombre(), objComponente.getId(), 
											2, "c_"+objComponente.getId(),items_producto));
							items_producto = "";	
						}
						
						response_text = String.join("", construirItem(proyecto.getNombre(), proyecto.getId(),
								1, "p_"+proyecto.getId(), items_componente));
						
						
						
						CMariaDB.close();
						 response_text = String.join("", "\"estructura\":",response_text);
				        response_text = String.join("", "{\"success\":true,", response_text, "}");
					}else{
						response_text = String.join("", "{\"success\":false}");
					}
				}
			}else if(accion.equals("getCargaTrabajoPrestamo")){
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				String idPrestamos = map.get("idPrestamos") != null && map.get("idPrestamos").length() > 0? map.get("idPrestamos") : "0";
				String idComponentes = map.get("idComponentes") != null && map.get("idComponentes").length() > 0 ? map.get("idComponentes") : "0";
				String idProductos = map.get("idProductos") != null && map.get("idProductos").length() > 0 ? map.get("idProductos") : "0";
				String idSubproductos = map.get("idSubproductos") != null && map.get("idSubproductos").length() > 0 ? map.get("idSubproductos") : "0";
								
				List<stcargatrabajo> cargas = getCargaTrabajoPrestamo(idPrestamos, idComponentes, idProductos, idSubproductos, anio_inicio, anio_fin);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(cargas);
		        response_text = String.join("", "\"cargatrabajo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
			}else if(accion.equals("getActividadesTerminadas")){
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				String idPrestamos = map.get("idPrestamos") != null && map.get("idPrestamos").length() > 0? map.get("idPrestamos") : "0";
				String idComponentes = map.get("idComponentes") != null && map.get("idComponentes").length() > 0 ? map.get("idComponentes") : "0";
				String idProductos = map.get("idProductos") != null && map.get("idProductos").length() > 0 ? map.get("idProductos") : "0";
				String idSubproductos = map.get("idSubproductos") != null && map.get("idSubproductos").length() > 0 ? map.get("idSubproductos") : "0";
				Integer colaboradorId = Utils.String2Int(map.get("colaboradorid"));
							
				List<?> objActividades =ActividadDAO.getActividadesTerminadas(idPrestamos, idComponentes, idProductos,
						idSubproductos, colaboradorId, anio_inicio, anio_fin);
				
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
				Integer idColaborador = Utils.String2Int(map.get("idColaborador"),0);
				
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
				if(proyecto != null){
					if(CMariaDB.connect()){
						ArrayList<stestructuracolaborador> estructuracolaborador = new ArrayList<>();
						
							stestructuracolaborador stprestamo = construirItemPorColaborador(proyecto.getNombre(), 
									proyecto.getId(), 1, false,null,null);
							 
							estructuracolaborador.add(stprestamo);
							Connection conn = CMariaDB.getConnection();
							ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
							
							
							for(Integer componente:componentes){
								
								Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
								stestructuracolaborador stcomponente = construirItemPorColaborador(objComponente.getNombre(), 
										objComponente.getId(), 2, false,null,null);
								
								estructuracolaborador.add(stcomponente);
								ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
								
								for(Integer producto: productos){
									Producto objProducto = ProductoDAO.getProductoPorId(producto);
									stestructuracolaborador stproducto = construirItemPorColaborador(objProducto.getNombre(),
											objProducto.getId(), 3, false,null,null);
									estructuracolaborador.add(stproducto);
									ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
									
									for(Integer subproducto: subproductos){
										Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
										stestructuracolaborador stsubproducto = construirItemPorColaborador(
												objSubProducto.getNombre(), objSubProducto.getId(), 4, false,null,null);
										estructuracolaborador.add(stsubproducto);
										ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), objSubProducto.getId(), conn);
										
										for(ArrayList<Integer> actividad : actividades){
											Actividad objActividad = ActividadDAO.getActividadPorIdResponsable(actividad.get(0), usuario, idColaborador, "r");
											if (objActividad!=null){
												stestructuracolaborador stactividad = construirItemPorColaborador(
														objActividad.getNombre(), objActividad.getId(), 5, true,objActividad.getFechaInicio(),objActividad.getFechaFin());
												estructuracolaborador.add(stactividad);
												getEstado(stactividad, objActividad);
												stprestamo.mostrar=true;
												stcomponente.mostrar=true;
												stproducto.mostrar = true;
												stsubproducto.mostrar = true;
												
											}
										}
										
																			
									}
									
									ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),  conn);
									
									for(ArrayList<Integer> actividad : actividades){
										Actividad objActividad = ActividadDAO.getActividadPorIdResponsable(actividad.get(0), usuario, idColaborador, "r");
										if (objActividad!=null){
											stestructuracolaborador stactividad = construirItemPorColaborador(
													objActividad.getNombre(), objActividad.getId(), 5, true,objActividad.getFechaInicio(),objActividad.getFechaFin());
											estructuracolaborador.add(stactividad);
											getEstado(stactividad, objActividad);
											stprestamo.mostrar=true;
											stcomponente.mostrar=true;
											stproducto.mostrar = true;
										}
									}
									
										
								}
								
								ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(),  conn);
								for(ArrayList<Integer> actividad : actividades){
									Actividad objActividad = ActividadDAO.getActividadPorIdResponsable(actividad.get(0), usuario, idColaborador, "r");
									if (objActividad!=null){
										stestructuracolaborador stactividad = construirItemPorColaborador(
												objActividad.getNombre(), objActividad.getId(), 5, true,objActividad.getFechaInicio(),objActividad.getFechaFin());
										estructuracolaborador.add(stactividad);
										getEstado(stactividad, objActividad);
										stprestamo.mostrar=true;
										stcomponente.mostrar=true;
									}
								}
							}
							CMariaDB.close();
							ArrayList<stestructuracolaborador> tempestructrua = new ArrayList<>();
							for (stestructuracolaborador temp:estructuracolaborador){
								if(temp.mostrar==true)
									tempestructrua.add(temp);
							}
							
							response_text=new GsonBuilder().serializeNulls().create().toJson(tempestructrua);
					        response_text = String.join("", "\"estructura\":",response_text);
					        response_text = String.join("", "{\"success\":true,", response_text,"}");
							
							
							
							
							 
						}else{
							response_text = String.join("", "{\"success\":false}");
						}
					}
			}else if (accion.equals("exportarExcel")){
				Integer anio_inicio = Utils.String2Int(map.get("anio_inicio"));
				Integer anio_fin = Utils.String2Int(map.get("anio_fin"));
				String idPrestamos = map.get("idPrestamos") != null && map.get("idPrestamos").length() > 0? map.get("idPrestamos") : "0";
				String idComponentes = map.get("idComponentes") != null && map.get("idComponentes").length() > 0 ? map.get("idComponentes") : "0";
				String idProductos = map.get("idProductos") != null && map.get("idProductos").length() > 0 ? map.get("idProductos") : "0";
				String idSubproductos = map.get("idSubproductos") != null && map.get("idSubproductos").length() > 0 ? map.get("idSubproductos") : "0";
				
		        byte [] outArray = exportarExcel(idPrestamos, idComponentes, idProductos, idSubproductos, anio_inicio, anio_fin, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; CargaTrabajo_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}else{
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
	
	private String construirItem(String nombre, Integer objetoId, Integer objetoTipo, String value,String hijos){
		return String.join("", "{\"label\" :\"",nombre,"\",",
				"\"value\" :\"",value,"\",",
				"\"objetoId\" :",objetoId.toString(),",",
				"\"objetoTipo\" :",objetoTipo.toString(),
				(hijos!=null && hijos.length()>0 ? ",\"children\": [" + hijos + "]": ""),
				"}");
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
	
	private List<stcargatrabajo> getCargaTrabajoPrestamo(String idPrestamos, String idComponentes, String idProductos, String idSubproductos, Integer anio_inicio, Integer anio_fin){
		List<Actividad> actividades = ActividadDAO.getActividadsPorObjetos
				(idPrestamos, idComponentes, idProductos, idSubproductos,anio_inicio,anio_fin);
		
		List<stcargatrabajo> cargas = new ArrayList<stcargatrabajo>();
		
		for(Actividad actividad : actividades){
			
				
				Colaborador colaborador = AsignacionRaciDAO.getResponsablePorRol(actividad.getId(), 5, "r");
				
				
				Date hoy = new Date();
				Date siguienteSemana = sumarDiasFecha(hoy, 7);
				Date siguienteMes = sumarDiasFecha(siguienteSemana, 23);

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
			boolean mostrar,Date fecha_inicio, Date fecha_fin){
		stestructuracolaborador temp = new stestructuracolaborador();
		temp.nombre = nombre;
		temp.objetoId = objetoId;
		temp.objetoTipo = objetoTipo;
		temp.mostrar = mostrar;
		temp.fechaInicio = Utils.formatDate(fecha_inicio);
		temp.fechaFin = Utils.formatDate(fecha_fin);
		return temp;
		
				
	}
	
	private void getEstado(stestructuracolaborador temp, Actividad actividad){
		Date hoy = new Date();
		Date siguienteSemana = sumarDiasFecha(hoy, 7);
		Date siguienteMes = sumarDiasFecha(siguienteSemana, 23);

		if (actividad.getPorcentajeAvance() != 100){
			if (actividad.getFechaFin().before(hoy)){
				temp.nombreEstado ="Retrasadas";
				temp.estado = 1;
			}else {
				if (actividad.getFechaFin().after(hoy) && actividad.getFechaFin().before(siguienteSemana)){
					temp.nombreEstado ="Alerta";
					temp.estado = 2;
				}else if (actividad.getFechaFin().after(siguienteSemana) && actividad.getFechaFin().before(siguienteMes)){
					temp.nombreEstado ="A cumplir";
					temp.estado = 3;
				}
			}
			
		}else{
			temp.nombreEstado ="Completadas";
			temp.estado = 4;
		}
	}
	
	private byte[] exportarExcel(String idPrestamos, String idComponentes, String idProductos, String idSubproductos, Integer anio_inicio, Integer anio_fin, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamos, idComponentes, idProductos, idSubproductos, anio_inicio, anio_fin, usuario);
			CGraficaExcel grafica = generarGrafica(datos);
			excel = new CExcel("Administración Transaccional", false, grafica);
			wb=excel.generateExcelOfData(datos, "Carga de Trabajo", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			System.out.println("exportarExcel: "+e);
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
	
	public String[][] generarDatos(String idPrestamos, String idComponentes, String idProductos, String idSubproductos, Integer anio_inicio, Integer anio_fin, String usuario){
		List<stcargatrabajo> cargas = getCargaTrabajoPrestamo(idPrestamos, idComponentes, idProductos, idSubproductos, anio_inicio, anio_fin);
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
}