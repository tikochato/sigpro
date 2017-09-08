package servlets;
import java.sql.Connection;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Pago;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SInformacionPresupuestaria")
public class SInformacionPresupuestaria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stpresupuesto{
		BigDecimal planificado;
		BigDecimal real;
	}
	
	class stprestamo{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stanio[] anios; 
		List<String> hijos;
		Integer predecesorId;
		Integer objetoPredecesorTipo;
	}
	
	class stprestamobimestre{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stpresupuesto[] bimestre1;
		stpresupuesto[] bimestre2;
		stpresupuesto[] bimestre3;
		stpresupuesto[] bimestre4;
		stpresupuesto[] bimestre5;
		stpresupuesto[] bimestre6;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total;
	}
	class stanio{
		
		stpresupuesto enero;
		stpresupuesto febrero;
		stpresupuesto marzo;
		stpresupuesto abril;
		stpresupuesto mayo;
		stpresupuesto junio;
		stpresupuesto julio;
		stpresupuesto agosto;
		stpresupuesto septiembre;
		stpresupuesto octubre;
		stpresupuesto noviembre;
		stpresupuesto diciembre;
		stpresupuesto anio;
		
	}

	final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;

	String[] columnaNames = null;
	List<Integer> actividadesCosto = null;
       
    public SInformacionPresupuestaria() {
        super();
   }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		try{
			
		    CLogger.write_simple("1", SInformacionPresupuestaria.class, "Inicia doPost - 130");
			
			request.setCharacterEncoding("UTF-8");
			HttpSession sesionweb = request.getSession();
			String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
			

		    CLogger.write_simple("2", SInformacionPresupuestaria.class, "Inicia doPost - 137");
			
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			
			CLogger.write_simple("3", SInformacionPresupuestaria.class, "StringBuilder - 142");
		    
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str;
			
			CLogger.write_simple("4", SInformacionPresupuestaria.class, "while str - 148");
		    
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			Map<String, String> map = gson.fromJson(sb.toString(), type);
			String accion = map.get("accion")!=null ? map.get("accion") : "";
			String response_text = "";
			
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "if accion - ");
		    
			if(accion.equals("generarInforme")){
				

			    CLogger.write_simple("1", SInformacionPresupuestaria.class, "Generar Informe - 162");
			    
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
				Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
				List<stprestamo> lstPrestamo = getInformacionPresupuestaria(idPrestamo, anioInicial, anioFinal, usuario);
				
				if (null != lstPrestamo && !lstPrestamo.isEmpty()){
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstPrestamo);
			        response_text = String.join("", "\"prestamo\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}else{
					response_text = String.join("", "{\"success\":false}");
				}
			}else if(accion.equals("exportarExcel")){
				CLogger.write_simple("6", SInformacionPresupuestaria.class, "accion: exportarExcel - 173");
				
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
				Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
				Integer agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
				Integer tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
				
				CLogger.write_simple("7", SInformacionPresupuestaria.class, "exportarExcel() - 181");
				
		        byte [] outArray = exportarExcel(idPrestamo, anioInicial, anioFinal, agrupacion, tipoVisualizacion, usuario);
			
		        CLogger.write_simple("8", SInformacionPresupuestaria.class, "repuesta exportarExcel() - 185");
		        
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; EjecucionPresupuestaria_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				
			}else if(accion.equals("exportarPdf")){
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
				Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
				Integer agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
				Integer tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
				CPdf archivo = new CPdf("Metas de Préstamo");
				String headers[][];
				String datosMetas[][];
				headers = generarHeaders(anioInicial, anioFinal, agrupacion, tipoVisualizacion);
				List<stprestamo> lstPrestamo = getInformacionPresupuestaria(idPrestamo, anioInicial, anioFinal, usuario);	
				lstPrestamo = calcularCostos(lstPrestamo, 0);
				datosMetas = generarDatosReporte(lstPrestamo, anioInicial, anioFinal, agrupacion, tipoVisualizacion, headers[0].length, usuario);
				String path = archivo.ExportPdfMetasPrestamo(headers, datosMetas,tipoVisualizacion);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("1", SInformacionPresupuestaria.class, e);
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
					response.setHeader("Expires:", "0"); 
					response.setHeader("Content-Disposition", "in-line; 'PrestamoMetas.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
	
				
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
	
	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}catch(Exception e){
			CLogger.write("2", SInformacionPresupuestaria.class, e);		
		}
	}
	
	private List<stprestamo>  getInformacionPresupuestaria(int idPrestamo, int anioInicial, int anioFinal, String usuario){
		List<stprestamo> lstPrestamo = new ArrayList<>();
		
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
		
		Prestamo objPrestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(idPrestamo, 1);
		String codigoPresupuestario = "";
		Integer fuente = 0;
		Integer organismo = 0;
		Integer correlativo = 0;
		if(objPrestamo != null){
			codigoPresupuestario = Long.toString(objPrestamo.getCodigoPresupuestario());
			fuente = Utils.String2Int(codigoPresupuestario.substring(0,2));
			organismo = Utils.String2Int(codigoPresupuestario.substring(2,6));
			correlativo = Utils.String2Int(codigoPresupuestario.substring(6,10));
		}
		
		if(proyecto != null){
			stprestamo tempPrestamo = null;
		
			tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
			if(CMariaDB.connect()){
					Connection conn = CMariaDB.getConnection();
					ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);

					tempPrestamo.hijos = new ArrayList<String>();
					for(Integer componente: componentes){
						tempPrestamo.hijos.add(componente+",2");
					}
					
					//calcular actividades hijas prestamo
					ArrayList<ArrayList<Integer>> actividadesPrestamo = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
					
					if(tempPrestamo.hijos == null){
						tempPrestamo.hijos = new ArrayList<String>();
					}
					for(ArrayList<Integer> actividad: actividadesPrestamo){
						if(actividad.get(1) == 0)
							tempPrestamo.hijos.add(actividad.get(0)+",5");
					}
					
					stprestamo padre = null;
					int nivel = 0;

					tempPrestamo.nombre = proyecto.getNombre();
					tempPrestamo.objeto_id = proyecto.getId();
					tempPrestamo.objeto_tipo = 1;
					tempPrestamo.nivel = 1;
					tempPrestamo.predecesorId = 0;
					tempPrestamo.objetoPredecesorTipo = 0;
					
					ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoProyecto
							(fuente, organismo, correlativo,anioInicial,anioFinal, conn);
					
					List<Date> fechaInicioFin = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoFecha(idPrestamo, conn);
					
					tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, null, proyecto, null, null, null, null, fechaInicioFin);
					
					lstPrestamo.add(tempPrestamo);
					
					for(Integer componente:componentes){
						tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
						Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
						tempPrestamo.nombre = objComponente.getNombre();
						tempPrestamo.objeto_id = objComponente.getId();
						tempPrestamo.objeto_tipo = 2;
						tempPrestamo.nivel = 2;
						tempPrestamo.predecesorId = proyecto.getId();
						tempPrestamo.objetoPredecesorTipo = 1;
						
						fechaInicioFin = InformacionPresupuestariaDAO.getEstructuraArbolComponentesFecha(idPrestamo, objComponente.getId(), conn);
						
						presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
								anioInicial, anioFinal, objComponente.getPrograma(), objComponente.getSubprograma(), objComponente.getProyecto_1(), 
								objComponente.getActividad(), objComponente.getObra(), conn);
						
						tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, null, null, objComponente, null, null, null, fechaInicioFin);
						
						ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
						
						tempPrestamo.hijos = new ArrayList<String>();
						for(Integer producto: productos){
							tempPrestamo.hijos.add(producto+",3");
						}
						
						//actividades hijas componente
						ArrayList<ArrayList<Integer>> actividadesComponente = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);
						
						if(tempPrestamo.hijos == null){
							tempPrestamo.hijos = new ArrayList<String>();
						}
						for(ArrayList<Integer> actividad: actividadesComponente){
							if(actividad.get(1) == 0)
								tempPrestamo.hijos.add(actividad.get(0)+",5");
						}
						
						lstPrestamo.add(tempPrestamo);
						
						for(Integer producto: productos){
							tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
							Producto objProducto = ProductoDAO.getProductoPorId(producto);
							tempPrestamo.nombre = objProducto.getNombre();
							tempPrestamo.objeto_id = objProducto.getId();
							tempPrestamo.objeto_tipo = 3;
							tempPrestamo.nivel = 3;
							tempPrestamo.predecesorId = objComponente.getId();
							tempPrestamo.objetoPredecesorTipo = 2;
							
							fechaInicioFin = InformacionPresupuestariaDAO.getEstructuraArbolProductoFecha(idPrestamo, objComponente.getId(), objProducto.getId(), conn);

							presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
									anioInicial, anioFinal, objProducto.getPrograma(), objProducto.getSubprograma(), objProducto.getProyecto(), 
									objProducto.getActividad(), objProducto.getObra(), conn);
							
							tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, null, null, null, objProducto, null, null, fechaInicioFin);
							//lstPrestamo.add(tempPrestamo);
							
						
							ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
							
							tempPrestamo.hijos = new ArrayList<String>();
							for(Integer subproducto: subproductos){
								tempPrestamo.hijos.add(subproducto+",4");
							}
							
							//actividades hijas de producto
							ArrayList<ArrayList<Integer>> actividadesProducto = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
							
							if(tempPrestamo.hijos == null){
								tempPrestamo.hijos = new ArrayList<String>();
							}
							for(ArrayList<Integer> actividad: actividadesProducto){
								if(actividad.get(1) == 0)
									tempPrestamo.hijos.add(actividad.get(0)+",5");
							}
							
							lstPrestamo.add(tempPrestamo);
							
							for(Integer subproducto: subproductos){
								tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
								Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
								tempPrestamo.nombre = objSubProducto.getNombre();
								tempPrestamo.objeto_id = objSubProducto.getId();
								tempPrestamo.objeto_tipo = 4;
								tempPrestamo.nivel = 4;
								tempPrestamo.predecesorId = objProducto.getId();
								tempPrestamo.objetoPredecesorTipo = 3;
								
								fechaInicioFin = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoFecha(idPrestamo, objComponente.getId(), objProducto.getId(), objSubProducto.getId(), conn);
								
								presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
										anioInicial, anioFinal, objSubProducto.getPrograma(), objSubProducto.getSubprograma(), objSubProducto.getProyecto(), 
										objSubProducto.getActividad(), objSubProducto.getObra(), conn);
								
								tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, null, null, null, null, objSubProducto, null, fechaInicioFin);
								lstPrestamo.add(tempPrestamo);
								
						
								//actividades sub producto
								ArrayList<ArrayList<Integer>> actividadesSubProducto = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),objSubProducto.getId(), conn);
								
								tempPrestamo.hijos = new ArrayList<String>();
								for(ArrayList<Integer> actividad: actividadesSubProducto){
									if(actividad.get(1) == 0)
										tempPrestamo.hijos.add(actividad.get(0)+",5");
								}

								padre = null;
								nivel = 0;
								
								for(ArrayList<Integer> actividad : actividadesSubProducto){
									tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
									Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
									tempPrestamo.nombre = objActividad.getNombre();
									tempPrestamo.objeto_id = objActividad.getId();
									tempPrestamo.objeto_tipo = 5;
									tempPrestamo.nivel = 5 + actividad.get(1);
									
									if(actividad.get(1)== 0){
										tempPrestamo.predecesorId = objSubProducto.getId();
										tempPrestamo.objetoPredecesorTipo = 4;
									}else{
										if(nivel != actividad.get(1)){
											padre = lstPrestamo.get(lstPrestamo.size() - 1);
										}
										
										if(padre.hijos == null)
											padre.hijos = new ArrayList<String>();
										
										padre.hijos.add(actividad.get(0)+",5");	
										
										tempPrestamo.predecesorId = padre.objeto_id;
										tempPrestamo.objetoPredecesorTipo = 5;
										
										nivel = actividad.get(1);
									}
									
									List<Pago> pagos = PagoDAO.getPagosByObjetoTipo(objActividad.getId(), 5);
									
									presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
											anioInicial, anioFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
											objActividad.getActividad(), objActividad.getObra(), conn);
									
									tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, pagos, null, null, null, null, objActividad, null);
									lstPrestamo.add(tempPrestamo);
									
									
								
								}
							}
							
							//actividades producto
							//ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
							
							tempPrestamo.hijos = new ArrayList<String>();
							for(ArrayList<Integer> actividad: actividadesProducto){
								if(actividad.get(1) == 0)
									tempPrestamo.hijos.add(actividad.get(0)+",5");
							}

							padre = null;
							nivel = 0;

							for(ArrayList<Integer> actividad : actividadesProducto){
								tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
								Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
								tempPrestamo.nombre = objActividad.getNombre();
								tempPrestamo.objeto_id = objActividad.getId();
								tempPrestamo.objeto_tipo = 5;
								tempPrestamo.nivel = 4 + actividad.get(1);
								
								if(actividad.get(1)==0){
									tempPrestamo.predecesorId = objProducto.getId();
									tempPrestamo.objetoPredecesorTipo = 3;
								}else{
									if(nivel != actividad.get(1)){
										padre = lstPrestamo.get(lstPrestamo.size() - 1);
									}
									
									if(padre.hijos == null)
										padre.hijos = new ArrayList<String>();
									
									padre.hijos.add(actividad.get(0)+",5");	
									
									tempPrestamo.predecesorId = padre.objeto_id;
									tempPrestamo.objetoPredecesorTipo = 5;

									nivel = actividad.get(1);
								}

								
								
								List<Pago> pagos = PagoDAO.getPagosByObjetoTipo(objActividad.getId(), 5);
								
								presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
										anioInicial, anioFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
										objActividad.getActividad(), objActividad.getObra(), conn);
								
								tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, pagos, null, null, null, null, objActividad, null);
								
								lstPrestamo.add(tempPrestamo);
								
							}  
						} 
						
						//actividades componente
						//ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);
						
						padre = null;
						nivel = 0;
						for(ArrayList<Integer> actividad : actividadesComponente){
							tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
							Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
							tempPrestamo.nombre = objActividad.getNombre();
							tempPrestamo.objeto_id = objActividad.getId();
							tempPrestamo.objeto_tipo = 5;
							tempPrestamo.nivel = 3 + actividad.get(1);
							
							if(actividad.get(1) == 0){
								tempPrestamo.predecesorId = objComponente.getId();
								tempPrestamo.objetoPredecesorTipo = 2;
							}else{
								if(nivel != actividad.get(1)){
									padre = lstPrestamo.get(lstPrestamo.size() - 1);
								}
								
								if(padre.hijos == null)
									padre.hijos = new ArrayList<String>();
								
								padre.hijos.add(actividad.get(0)+",5");	
								
								tempPrestamo.predecesorId = padre.objeto_id;
								tempPrestamo.objetoPredecesorTipo = 5;
								
								nivel = actividad.get(1);
							}
							
							List<Pago> pagos = PagoDAO.getPagosByObjetoTipo(objActividad.getId(), 5);
							
							presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
									anioInicial, anioFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
									objActividad.getActividad(), objActividad.getObra(), conn);
							
							tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, pagos, null, null, null, null, objActividad, null);
							lstPrestamo.add(tempPrestamo);
							
						} 
					}
					
					//actividades prestamo
					//ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
					actividadesPrestamo = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
					
					padre = null;
					nivel = 0;
					for(ArrayList<Integer> actividad : actividadesPrestamo){
						tempPrestamo = inicializarEstructura(anioInicial, anioFinal);
						Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
						tempPrestamo.nombre = objActividad.getNombre();
						tempPrestamo.objeto_id = objActividad.getId();
						tempPrestamo.objeto_tipo = 5;
						tempPrestamo.nivel = 2 + actividad.get(1);
						if(actividad.get(1)==0){
							tempPrestamo.predecesorId = idPrestamo;
							tempPrestamo.objetoPredecesorTipo = 1;
						}else{
							if(nivel != actividad.get(1)){
								padre = lstPrestamo.get(lstPrestamo.size() - 1);
							}
							
							if(padre.hijos == null)
								padre.hijos = new ArrayList<String>();
							
							padre.hijos.add(actividad.get(0)+",5");	
							
							tempPrestamo.predecesorId = padre.objeto_id;
							tempPrestamo.objetoPredecesorTipo = 5;
							nivel = actividad.get(1);
						}
						
						List<Pago> pagos = PagoDAO.getPagosByObjetoTipo(objActividad.getId(), 5);
						
						presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
								anioInicial, anioFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
								objActividad.getActividad(), objActividad.getObra(), conn);
						
						tempPrestamo = getPresupuesto(presupuestoPrestamo, anioInicial, anioFinal, tempPrestamo, pagos, null, null, null, null, objActividad, null);
						
						lstPrestamo.add(tempPrestamo);
					
					}
					
					CMariaDB.close();
				}
			}
		return lstPrestamo;
	}
	
	private stprestamo getPresupuesto (ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo,
			int anioInicial, int anioFinal, stprestamo prestamo, List<Pago> pagos, Proyecto proyecto, Componente componente, Producto producto,
			Subproducto subproducto, Actividad actividad, List<Date> fechasNoActividades){
		
		stanio[] anios = inicializarStanio(anioInicial, anioFinal);
		if(presupuestoPrestamo.size() > 0){
			
			int pos = 0;
			for(ArrayList<BigDecimal> objprestamopresupuesto : presupuestoPrestamo){
				
				stanio aniotemp = new stanio();
				
				aniotemp.enero = new stpresupuesto();
				aniotemp.enero.real = objprestamopresupuesto.get(0) != null ? objprestamopresupuesto.get(0) : new BigDecimal(0);
				
				aniotemp.febrero = new stpresupuesto();
				aniotemp.febrero.real = objprestamopresupuesto.get(1) != null ? objprestamopresupuesto.get(1) : new BigDecimal(0);
				
				aniotemp.marzo = new stpresupuesto();
				aniotemp.marzo.real = objprestamopresupuesto.get(2) != null ? objprestamopresupuesto.get(2) : new BigDecimal(0);
				
				aniotemp.abril = new stpresupuesto();
				aniotemp.abril.real = objprestamopresupuesto.get(3) != null ? objprestamopresupuesto.get(3) : new BigDecimal(0);
				
				aniotemp.mayo = new stpresupuesto();
				aniotemp.mayo.real = objprestamopresupuesto.get(4) != null ? objprestamopresupuesto.get(4) : new BigDecimal(0);
				
				aniotemp.junio = new stpresupuesto();
				aniotemp.junio.real = objprestamopresupuesto.get(5) != null ? objprestamopresupuesto.get(5) : new BigDecimal(0);
				
				aniotemp.julio = new stpresupuesto();
				aniotemp.julio.real = objprestamopresupuesto.get(6) != null ? objprestamopresupuesto.get(6) : new BigDecimal(0);
				
				aniotemp.agosto = new stpresupuesto();
				aniotemp.agosto.real = objprestamopresupuesto.get(7) != null ? objprestamopresupuesto.get(7) : new BigDecimal(0);
				
				aniotemp.septiembre = new stpresupuesto();
				aniotemp.septiembre.real = objprestamopresupuesto.get(8) != null ? objprestamopresupuesto.get(8) : new BigDecimal(0);
				
				aniotemp.octubre = new stpresupuesto();
				aniotemp.octubre.real = objprestamopresupuesto.get(9) != null ? objprestamopresupuesto.get(9) : new BigDecimal(0);
				
				aniotemp.noviembre = new stpresupuesto();
				aniotemp.noviembre.real = objprestamopresupuesto.get(10) != null ? objprestamopresupuesto.get(10) : new BigDecimal(0);
				
				aniotemp.diciembre = new stpresupuesto();
				aniotemp.diciembre.real = objprestamopresupuesto.get(11) != null ? objprestamopresupuesto.get(11) : new BigDecimal(0);
				
				aniotemp.enero.planificado = new BigDecimal(0);
				aniotemp.febrero.planificado = new BigDecimal(0);
				aniotemp.marzo.planificado = new BigDecimal(0);
				aniotemp.abril.planificado = new BigDecimal(0);
				aniotemp.mayo.planificado = new BigDecimal(0);
				aniotemp.junio.planificado = new BigDecimal(0);
				aniotemp.julio.planificado = new BigDecimal(0);
				aniotemp.agosto.planificado = new BigDecimal(0);
				aniotemp.septiembre.planificado = new BigDecimal(0);
				aniotemp.octubre.planificado = new BigDecimal(0);
				aniotemp.noviembre.planificado = new BigDecimal(0);
				aniotemp.diciembre.planificado = new BigDecimal(0);
				
				if(pagos!= null && pagos.size() > 0){
					Calendar cal = Calendar.getInstance();
					for(Pago pago : pagos){
						cal.setTime(pago.getFechaPago());
						int mes = cal.get(Calendar.MONTH);
						int anio = cal.get(Calendar.YEAR);
						
						if(anio == objprestamopresupuesto.get(12).intValue()){
							switch(mes){
							case 0:
								aniotemp.enero.planificado.add(pago.getPago());
								break;
							case 1:
								aniotemp.febrero.planificado.add(pago.getPago());
								break;
							case 2:
								aniotemp.marzo.planificado.add(pago.getPago());
								break;
							case 3:
								aniotemp.abril.planificado.add(pago.getPago());
								break;
							case 4:
								aniotemp.mayo.planificado.add(pago.getPago());
								break;
							case 5:
								aniotemp.junio.planificado.add(pago.getPago());
								break;
							case 6:
								aniotemp.julio.planificado.add(pago.getPago());
								break;
							case 7:
								aniotemp.agosto.planificado.add(pago.getPago());
								break;
							case 8:
								aniotemp.septiembre.planificado.add(pago.getPago());
								break;
							case 9:
								aniotemp.octubre.planificado.add(pago.getPago());
								break;
							case 10:
								aniotemp.noviembre.planificado.add(pago.getPago());
								break;
							case 11:
								aniotemp.diciembre.planificado.add(pago.getPago());
								break;
							}
						}
					}
				}else{
					Integer acumulacionCosto = 0;
					String[] fechaInicioSplit = new String[2];
					String[] fechaFinSplit = new String[2];
					Date fechaInicial = new Date();;
					Date fechaFin = new Date();
					try{
						if(proyecto != null){
							acumulacionCosto = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getId() : 3;
							fechaInicioSplit = Utils.formatDate(fechasNoActividades.get(0)).split("/");
							fechaFinSplit = Utils.formatDate(fechasNoActividades.get(1)).split("/");
							fechaInicial = fechasNoActividades.get(0);
							fechaFin = fechasNoActividades.get(1);
						}else if(componente != null){
							acumulacionCosto = componente.getAcumulacionCosto() != null ? componente.getAcumulacionCosto().getId() : 3;
							fechaInicioSplit = Utils.formatDate(fechasNoActividades.get(0)).split("/");
							fechaFinSplit = Utils.formatDate(fechasNoActividades.get(1)).split("/");
							fechaInicial = fechasNoActividades.get(0);
							fechaFin = fechasNoActividades.get(1);
						}else if(producto != null){
							acumulacionCosto = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getId() : 3;
							fechaInicioSplit = Utils.formatDate(fechasNoActividades.get(0)).split("/");
							fechaFinSplit = Utils.formatDate(fechasNoActividades.get(1)).split("/");
							fechaInicial = fechasNoActividades.get(0);
							fechaFin = fechasNoActividades.get(1);
						}else if(subproducto != null){
							acumulacionCosto = subproducto.getAcumulacionCosto() != null ? subproducto.getAcumulacionCosto().getId() : 3;
							fechaInicioSplit = Utils.formatDate(fechasNoActividades.get(0)).split("/");
							fechaFinSplit = Utils.formatDate(fechasNoActividades.get(1)).split("/");
							fechaInicial = fechasNoActividades.get(0);
							fechaFin = fechasNoActividades.get(1);
						}else if(actividad != null){
							acumulacionCosto = actividad.getAcumulacionCosto() != null ? actividad.getAcumulacionCosto().getId() : 3;	
							fechaInicioSplit = Utils.formatDate(actividad.getFechaInicio()).split("/");
							fechaFinSplit = Utils.formatDate(actividad.getFechaFin()).split("/");
							fechaInicial = actividad.getFechaInicio();
							fechaFin = actividad.getFechaFin();
						}
					}
					catch(Exception e){
						CLogger.write("3", SInformacionPresupuestaria.class, e);
					}
					
					Calendar cal = Calendar.getInstance();
					int mes = 0;
					int anio = 0;
					if(acumulacionCosto == 1){
						if(actividad != null){
							cal.setTime(actividad.getFechaInicio());
							mes = cal.get(Calendar.MONTH);
							anio = cal.get(Calendar.YEAR);
						}else{
							cal.setTime(fechaInicial);
							mes = cal.get(Calendar.MONTH);
							anio = cal.get(Calendar.YEAR);
						}
						
					}else if(acumulacionCosto == 2){
						List<Double> diasPorcentual = new ArrayList<Double>();
						
						if(fechaInicioSplit[1] != fechaFinSplit[1]){
							try {

								long diferenciaEn_ms = fechaFin.getTime() - fechaInicial.getTime();
								long diasTotales = diferenciaEn_ms / (1000 * 60 * 60 * 24);
							    int d = 0;
							    int contador = 0;
								int inicio = Utils.String2Int(fechaInicioSplit[1]) <= 12 ? Utils.String2Int(fechaInicioSplit[1]) : 1;
								for(int j = Utils.String2Int(fechaFinSplit[2]); j <= Utils.String2Int(fechaFinSplit[2]); j++){
									int fin = j == Utils.String2Int(fechaFinSplit[2]) ? Utils.String2Int(fechaFinSplit[1]) : 12;
									
									for(int i = inicio; i <= fin; i++){
										if (diasPorcentual.size() == 0){
											int dias = getDiasDelMes(Utils.String2Int(fechaInicioSplit[1]),Utils.String2Int(fechaInicioSplit[2])) - Utils.String2Int(fechaInicioSplit[0]);
											diasPorcentual.add((double)dias);
										}else{
											d = getDiasDelMes(i,j);
											diasPorcentual.add((double)d);
										}
									}
									inicio = 1;
								}
								
								diasPorcentual.set(diasPorcentual.size() - 1, (double)Utils.String2Int(fechaFinSplit[0]));
								
								for(int i = 0; i<diasPorcentual.size();i++){
									double diaPorcentual = diasPorcentual.get(i);
									double dias = diaPorcentual / Utils.String2Int(Long.toString(diasTotales));
									diasPorcentual.set(i, dias);
								}
								
								inicio = (Utils.String2Int(fechaInicioSplit[1]) <= 12 ? Utils.String2Int(fechaInicioSplit[1]) : 1);
								
								for(int j = Utils.String2Int(fechaFinSplit[2]); j <= Utils.String2Int(fechaFinSplit[2]); j++){
									int fin = j == Utils.String2Int(fechaFinSplit[2]) ? Utils.String2Int(fechaFinSplit[1]) : 12;
									
									for(int i = inicio; i <= fin; i++){
										if(j == objprestamopresupuesto.get(12).intValue()){
											switch(i-1){
											case 0:
												if(proyecto != null){
													aniotemp.enero.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.enero.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.enero.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.enero.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.enero.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));	
												}
												break;
											case 1:
												if(proyecto != null){
													aniotemp.febrero.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.febrero.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.febrero.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.febrero.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.febrero.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 2:
												if(proyecto != null){
													aniotemp.marzo.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.marzo.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.marzo.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.marzo.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.marzo.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 3:
												if(proyecto != null){
													aniotemp.abril.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.abril.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.abril.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.abril.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.abril.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 4:
												if(proyecto != null){
													aniotemp.mayo.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.mayo.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.mayo.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.mayo.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.mayo.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 5:
												if(proyecto != null){
													aniotemp.junio.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.junio.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.junio.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.junio.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.junio.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 6:
												if(proyecto != null){
													aniotemp.julio.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.julio.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.julio.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.julio.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.julio.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 7:
												if(proyecto != null){
													aniotemp.agosto.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.agosto.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.agosto.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.agosto.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.agosto.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 8:
												if(proyecto != null){
													aniotemp.septiembre.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.septiembre.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.septiembre.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.septiembre.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.septiembre.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 9:
												if(proyecto != null){
													aniotemp.octubre.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.octubre.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.octubre.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.octubre.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.octubre.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 10:
												if(proyecto != null){
													aniotemp.noviembre.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.noviembre.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.noviembre.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.noviembre.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.noviembre.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											case 11:
												if(proyecto != null){
													aniotemp.diciembre.planificado = proyecto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(componente != null){
													aniotemp.diciembre.planificado = componente.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(producto != null){
													aniotemp.diciembre.planificado = producto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(subproducto != null){
													aniotemp.diciembre.planificado = subproducto.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}else if(actividad != null){
													aniotemp.diciembre.planificado = actividad.getCosto().multiply(new BigDecimal(diasPorcentual.get(contador)));
												}
												break;
											}
											
											contador++;
										}
									}
								}
							}catch(Throwable e){
								CLogger.write("4", SInformacionPresupuestaria.class, e);
							}
						}						
					}else if(acumulacionCosto ==3){
						if(actividad != null){
							cal.setTime(actividad.getFechaFin());
							mes = cal.get(Calendar.MONTH);
							anio = cal.get(Calendar.YEAR);
						}else{
							cal.setTime(fechaFin);
							mes = cal.get(Calendar.MONTH);
							anio = cal.get(Calendar.YEAR);
						}
					}
					
					if(acumulacionCosto != 2){
						if(anio == objprestamopresupuesto.get(12).intValue()){
							switch(mes){
							case 0:
								if(proyecto != null){
									aniotemp.enero.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.enero.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.enero.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.enero.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.enero.planificado = actividad.getCosto();
								}
								break;
							case 1:
								if(proyecto != null){
									aniotemp.febrero.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.febrero.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.febrero.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.febrero.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.febrero.planificado = actividad.getCosto();
								}
								break;
							case 2:
								if(proyecto != null){
									aniotemp.marzo.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.marzo.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.marzo.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.marzo.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.marzo.planificado = actividad.getCosto();
								}
								break;
							case 3:
								if(proyecto != null){
									aniotemp.abril.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.abril.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.abril.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.abril.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.abril.planificado = actividad.getCosto();
								}
								break;
							case 4:
								if(proyecto != null){
									aniotemp.mayo.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.mayo.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.mayo.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.mayo.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.mayo.planificado = actividad.getCosto();
								}
								break;
							case 5:
								if(proyecto != null){
									aniotemp.junio.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.junio.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.junio.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.junio.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.junio.planificado = actividad.getCosto();
								}
								break;
							case 6:
								if(proyecto != null){
									aniotemp.julio.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.julio.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.julio.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.julio.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.julio.planificado = actividad.getCosto();
								}
								break;
							case 7:
								if(proyecto != null){
									aniotemp.agosto.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.agosto.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.agosto.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.agosto.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.agosto.planificado = actividad.getCosto();
								}
								break;
							case 8:
								if(proyecto != null){
									aniotemp.septiembre.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.septiembre.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.septiembre.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.septiembre.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.septiembre.planificado = actividad.getCosto();
								}
								break;
							case 9:
								if(proyecto != null){
									aniotemp.octubre.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.octubre.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.octubre.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.octubre.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.octubre.planificado = actividad.getCosto();
								}
								break;
							case 10:
								if(proyecto != null){
									aniotemp.noviembre.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.noviembre.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.noviembre.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.noviembre.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.noviembre.planificado = actividad.getCosto();
								}
								break;
							case 11:
								if(proyecto != null){
									aniotemp.diciembre.planificado = proyecto.getCosto();
								}else if(componente != null){
									aniotemp.diciembre.planificado = componente.getCosto();
								}else if(producto != null){
									aniotemp.diciembre.planificado = producto.getCosto();
								}else if(subproducto != null){
									aniotemp.diciembre.planificado = subproducto.getCosto();
								}else if(actividad != null){
									aniotemp.diciembre.planificado = actividad.getCosto();
								}
								break;
							}
						}
					}
				}
				
				/*Integer acumulacionCosto = 0;
				if(actividad == null && fechasNoActividades != null){
					
					if(proyecto != null){
						acumulacionCosto = proyecto.getAcumulacionCosto() != null ? proyecto.getAcumulacionCosto().getId() : 3;
					}else if(componente != null){
						acumulacionCosto = componente.getAcumulacionCosto() != null ? componente.getAcumulacionCosto().getId() : 3;
					}else if(producto != null){
						acumulacionCosto = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getId() : 3;
					}else if(subproducto != null){
						acumulacionCosto = subproducto.getAcumulacionCosto() != null ? subproducto.getAcumulacionCosto().getId() : 3;
					}
					
					
				}else if(actividad != null){
					acumulacionCosto = actividad.getAcumulacionCosto() != null ? actividad.getAcumulacionCosto().getId() : 0;
					
				}	*/		
				
				//int pos = anioFinal- objprestamopresupuesto.get(12).intValue();
				
				aniotemp.anio = new stpresupuesto();
				aniotemp.anio.planificado = new BigDecimal(anioInicial + pos);
				aniotemp.anio.real = new BigDecimal(anioInicial + pos);
				anios[pos] = aniotemp;
				pos = pos + 1;
			}
		}
			prestamo.anios = anios;
			return prestamo;
	}
			
	private int getDiasDelMes(int mes, int anio)
	{
		if( (mes == 1) || (mes == 3) || (mes == 5) || (mes == 7) || (mes == 8) || (mes == 10) || (mes == 12) ) 
	        return 31;
	    else if( (mes == 4) || (mes == 6) || (mes == 9) || (mes == 11) ) 
	        return 30;
	    else if( mes == 2 )
	    {
	        if( (anio % 4 == 0) && (anio % 100 != 0) || (anio % 400 == 0) )
	            return 29;
	        else
	            return 28;
	    }  
		
		return 0;
	}
	
	private stprestamo inicializarEstructura(Integer anioInicio, Integer anioFinal){
			stprestamo temp = new stprestamo();
			return temp;
	}
	
	private stanio[] inicializarStanio (int anioInicial, int anioFinal){
		
		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			
			temp.enero = new stpresupuesto();
			temp.enero.planificado = new BigDecimal(0);
			temp.enero.real =  new BigDecimal(0);
			
			temp.febrero = new stpresupuesto();
			temp.febrero.planificado =  new BigDecimal(0);
			temp.febrero.real =  new BigDecimal(0);
			
			temp.marzo = new stpresupuesto();
			temp.marzo.planificado =  new BigDecimal(0);
			temp.marzo.real =  new BigDecimal(0);
			
			temp.abril = new stpresupuesto();
			temp.abril.planificado =  new BigDecimal(0);
			temp.abril.real =  new BigDecimal(0);
			
			temp.mayo = new stpresupuesto();
			temp.mayo.planificado =  new BigDecimal(0);
			temp.mayo.real =  new BigDecimal(0);
			
			temp.junio = new stpresupuesto();
			temp.junio.planificado =  new BigDecimal(0);
			temp.junio.real =  new BigDecimal(0);
			
			temp.julio = new stpresupuesto();
			temp.julio.planificado =  new BigDecimal(0);
			temp.julio.real =  new BigDecimal(0);
			
			temp.agosto = new stpresupuesto();
			temp.agosto.planificado =  new BigDecimal(0);
			temp.agosto.real =  new BigDecimal(0);
			
			temp.septiembre = new stpresupuesto();
			temp.septiembre.planificado =  new BigDecimal(0);
			temp.septiembre.real =  new BigDecimal(0);
			
			temp.octubre = new stpresupuesto();
			temp.octubre.planificado =  new BigDecimal(0);
			temp.octubre.real =  new BigDecimal(0);
			
			temp.noviembre = new stpresupuesto();
			temp.noviembre.planificado =  new BigDecimal(0);
			temp.noviembre.real =  new BigDecimal(0);
			
			temp.diciembre = new stpresupuesto();
			temp.diciembre.planificado =  new BigDecimal(0);
			temp.diciembre.real =  new BigDecimal(0);
			
			temp.anio = new stpresupuesto();
			temp.anio.planificado = new BigDecimal(anioInicial + i);
			temp.anio.real = new BigDecimal(anioInicial + i);
			anios[i] = temp;
		}
		return anios;
		
		
		
	}
	
	
	private Integer obtenerPosicionEntidad(List<stprestamo> lstPrestamo, Integer objetoId, Integer objetoTipo){
		for (int i=0; i<lstPrestamo.size(); i++){
			stprestamo prestamo = lstPrestamo.get(i);
			Integer objeto_id =prestamo.objeto_id;
			Integer objeto_tipo = prestamo.objeto_tipo;
			if (objetoId.intValue() == objeto_id.intValue() && objetoTipo.intValue() == objeto_tipo.intValue()){
				return i;
			}
		}
		return -1;
	}
	
	private List<stprestamo> calcularCostos(List<stprestamo> lstPrestamo, Integer posicion){
		stprestamo prestamo = lstPrestamo.get(posicion);
		if(prestamo.hijos!=null && !prestamo.hijos.isEmpty()){
			for(int i=0; i<prestamo.hijos.size(); i++){
				String strhijo = prestamo.hijos.get(i);
				Integer posicionHijo = obtenerPosicionEntidad(lstPrestamo, Integer.parseInt(strhijo.split(",")[0]), Integer.parseInt(strhijo.split(",")[1]));
				stprestamo prestamohijo = calcularCostosRecursivo(lstPrestamo, posicionHijo);
				if(prestamohijo!=null){
					for(int a=0; a<prestamo.anios.length; a++){
						prestamo.anios[a].enero.planificado = prestamo.anios[a].enero.planificado!=null ? prestamo.anios[a].enero.planificado : new BigDecimal(0);
						prestamo.anios[a].febrero.planificado = prestamo.anios[a].febrero.planificado!=null ? prestamo.anios[a].febrero.planificado : new BigDecimal(0);
						prestamo.anios[a].marzo.planificado = prestamo.anios[a].marzo.planificado!=null ? prestamo.anios[a].marzo.planificado : new BigDecimal(0);
						prestamo.anios[a].abril.planificado = prestamo.anios[a].abril.planificado!=null ? prestamo.anios[a].abril.planificado : new BigDecimal(0);
						prestamo.anios[a].mayo.planificado = prestamo.anios[a].mayo.planificado!=null ? prestamo.anios[a].mayo.planificado : new BigDecimal(0);
						prestamo.anios[a].junio.planificado = prestamo.anios[a].junio.planificado!=null ? prestamo.anios[a].junio.planificado : new BigDecimal(0);
						prestamo.anios[a].julio.planificado = prestamo.anios[a].julio.planificado!=null ? prestamo.anios[a].julio.planificado : new BigDecimal(0);
						prestamo.anios[a].agosto.planificado = prestamo.anios[a].agosto.planificado!=null ? prestamo.anios[a].agosto.planificado : new BigDecimal(0);
						prestamo.anios[a].septiembre.planificado = prestamo.anios[a].septiembre.planificado!=null ? prestamo.anios[a].septiembre.planificado : new BigDecimal(0);
						prestamo.anios[a].octubre.planificado = prestamo.anios[a].octubre.planificado!=null ? prestamo.anios[a].octubre.planificado : new BigDecimal(0);
						prestamo.anios[a].noviembre.planificado = prestamo.anios[a].noviembre.planificado!=null ? prestamo.anios[a].noviembre.planificado : new BigDecimal(0);
						prestamo.anios[a].diciembre.planificado = prestamo.anios[a].diciembre.planificado!=null ? prestamo.anios[a].diciembre.planificado : new BigDecimal(0);
						
						prestamohijo.anios[a].enero.planificado = prestamohijo.anios[a].enero.planificado!=null ? prestamohijo.anios[a].enero.planificado : new BigDecimal(0);
						prestamohijo.anios[a].febrero.planificado = prestamohijo.anios[a].febrero.planificado!=null ? prestamohijo.anios[a].febrero.planificado : new BigDecimal(0);
						prestamohijo.anios[a].marzo.planificado = prestamohijo.anios[a].marzo.planificado!=null ? prestamohijo.anios[a].marzo.planificado : new BigDecimal(0);
						prestamohijo.anios[a].abril.planificado = prestamohijo.anios[a].abril.planificado!=null ? prestamohijo.anios[a].abril.planificado : new BigDecimal(0);
						prestamohijo.anios[a].mayo.planificado = prestamohijo.anios[a].mayo.planificado!=null ? prestamohijo.anios[a].mayo.planificado : new BigDecimal(0);
						prestamohijo.anios[a].junio.planificado = prestamohijo.anios[a].junio.planificado!=null ? prestamohijo.anios[a].junio.planificado : new BigDecimal(0);
						prestamohijo.anios[a].julio.planificado = prestamohijo.anios[a].julio.planificado!=null ? prestamohijo.anios[a].julio.planificado : new BigDecimal(0);
						prestamohijo.anios[a].agosto.planificado = prestamohijo.anios[a].agosto.planificado!=null ? prestamohijo.anios[a].agosto.planificado : new BigDecimal(0);
						prestamohijo.anios[a].septiembre.planificado = prestamohijo.anios[a].septiembre.planificado!=null ? prestamohijo.anios[a].septiembre.planificado : new BigDecimal(0);
						prestamohijo.anios[a].octubre.planificado = prestamohijo.anios[a].octubre.planificado!=null ? prestamohijo.anios[a].octubre.planificado : new BigDecimal(0);
						prestamohijo.anios[a].noviembre.planificado = prestamohijo.anios[a].noviembre.planificado!=null ? prestamohijo.anios[a].noviembre.planificado : new BigDecimal(0);
						prestamohijo.anios[a].diciembre.planificado = prestamohijo.anios[a].diciembre.planificado!=null ? prestamohijo.anios[a].diciembre.planificado : new BigDecimal(0);
						
						prestamo.anios[a].enero.planificado = prestamo.anios[a].enero.planificado.add(prestamohijo.anios[a].enero.planificado);
						prestamo.anios[a].febrero.planificado = prestamo.anios[a].febrero.planificado.add(prestamohijo.anios[a].febrero.planificado);
						prestamo.anios[a].marzo.planificado = prestamo.anios[a].marzo.planificado.add(prestamohijo.anios[a].marzo.planificado);
						prestamo.anios[a].abril.planificado = prestamo.anios[a].abril.planificado.add(prestamohijo.anios[a].abril.planificado);
						prestamo.anios[a].mayo.planificado = prestamo.anios[a].mayo.planificado.add(prestamohijo.anios[a].mayo.planificado);
						prestamo.anios[a].junio.planificado = prestamo.anios[a].junio.planificado.add(prestamohijo.anios[a].junio.planificado);
						prestamo.anios[a].julio.planificado = prestamo.anios[a].julio.planificado.add(prestamohijo.anios[a].julio.planificado);
						prestamo.anios[a].agosto.planificado = prestamo.anios[a].agosto.planificado.add(prestamohijo.anios[a].agosto.planificado);
						prestamo.anios[a].septiembre.planificado = prestamo.anios[a].septiembre.planificado.add(prestamohijo.anios[a].septiembre.planificado);
						prestamo.anios[a].octubre.planificado = prestamo.anios[a].octubre.planificado.add(prestamohijo.anios[a].octubre.planificado);
						prestamo.anios[a].noviembre.planificado = prestamo.anios[a].noviembre.planificado.add(prestamohijo.anios[a].noviembre.planificado);
						prestamo.anios[a].diciembre.planificado = prestamo.anios[a].diciembre.planificado.add(prestamohijo.anios[a].diciembre.planificado);
					}
				}
			}
		}
		return lstPrestamo;
	}
	
	private stprestamo calcularCostosRecursivo(List<stprestamo> lstPrestamo, Integer posicion){
		stprestamo prestamo = lstPrestamo.get(posicion);
		if(prestamo.hijos!=null && !prestamo.hijos.isEmpty()){
			for(int i=0; i<prestamo.hijos.size(); i++){
				String strhijo = prestamo.hijos.get(i);
				Integer posicionHijo = obtenerPosicionEntidad(lstPrestamo, Integer.parseInt(strhijo.split(",")[0]), Integer.parseInt(strhijo.split(",")[1]));
				stprestamo prestamohijo = calcularCostosRecursivo(lstPrestamo, posicionHijo);
				if(prestamohijo!=null){
					for(int a=0; a<prestamo.anios.length; a++){
						prestamo.anios[a].enero.planificado = prestamo.anios[a].enero.planificado!=null ? prestamo.anios[a].enero.planificado : new BigDecimal(0);
						prestamo.anios[a].febrero.planificado = prestamo.anios[a].febrero.planificado!=null ? prestamo.anios[a].febrero.planificado : new BigDecimal(0);
						prestamo.anios[a].marzo.planificado = prestamo.anios[a].marzo.planificado!=null ? prestamo.anios[a].marzo.planificado : new BigDecimal(0);
						prestamo.anios[a].abril.planificado = prestamo.anios[a].abril.planificado!=null ? prestamo.anios[a].abril.planificado : new BigDecimal(0);
						prestamo.anios[a].mayo.planificado = prestamo.anios[a].mayo.planificado!=null ? prestamo.anios[a].mayo.planificado : new BigDecimal(0);
						prestamo.anios[a].junio.planificado = prestamo.anios[a].junio.planificado!=null ? prestamo.anios[a].junio.planificado : new BigDecimal(0);
						prestamo.anios[a].julio.planificado = prestamo.anios[a].julio.planificado!=null ? prestamo.anios[a].julio.planificado : new BigDecimal(0);
						prestamo.anios[a].agosto.planificado = prestamo.anios[a].agosto.planificado!=null ? prestamo.anios[a].agosto.planificado : new BigDecimal(0);
						prestamo.anios[a].septiembre.planificado = prestamo.anios[a].septiembre.planificado!=null ? prestamo.anios[a].septiembre.planificado : new BigDecimal(0);
						prestamo.anios[a].octubre.planificado = prestamo.anios[a].octubre.planificado!=null ? prestamo.anios[a].octubre.planificado : new BigDecimal(0);
						prestamo.anios[a].noviembre.planificado = prestamo.anios[a].noviembre.planificado!=null ? prestamo.anios[a].noviembre.planificado : new BigDecimal(0);
						prestamo.anios[a].diciembre.planificado = prestamo.anios[a].diciembre.planificado!=null ? prestamo.anios[a].diciembre.planificado : new BigDecimal(0);
						
						prestamohijo.anios[a].enero.planificado = prestamohijo.anios[a].enero.planificado!=null ? prestamohijo.anios[a].enero.planificado : new BigDecimal(0);
						prestamohijo.anios[a].febrero.planificado = prestamohijo.anios[a].febrero.planificado!=null ? prestamohijo.anios[a].febrero.planificado : new BigDecimal(0);
						prestamohijo.anios[a].marzo.planificado = prestamohijo.anios[a].marzo.planificado!=null ? prestamohijo.anios[a].marzo.planificado : new BigDecimal(0);
						prestamohijo.anios[a].abril.planificado = prestamohijo.anios[a].abril.planificado!=null ? prestamohijo.anios[a].abril.planificado : new BigDecimal(0);
						prestamohijo.anios[a].mayo.planificado = prestamohijo.anios[a].mayo.planificado!=null ? prestamohijo.anios[a].mayo.planificado : new BigDecimal(0);
						prestamohijo.anios[a].junio.planificado = prestamohijo.anios[a].junio.planificado!=null ? prestamohijo.anios[a].junio.planificado : new BigDecimal(0);
						prestamohijo.anios[a].julio.planificado = prestamohijo.anios[a].julio.planificado!=null ? prestamohijo.anios[a].julio.planificado : new BigDecimal(0);
						prestamohijo.anios[a].agosto.planificado = prestamohijo.anios[a].agosto.planificado!=null ? prestamohijo.anios[a].agosto.planificado : new BigDecimal(0);
						prestamohijo.anios[a].septiembre.planificado = prestamohijo.anios[a].septiembre.planificado!=null ? prestamohijo.anios[a].septiembre.planificado : new BigDecimal(0);
						prestamohijo.anios[a].octubre.planificado = prestamohijo.anios[a].octubre.planificado!=null ? prestamohijo.anios[a].octubre.planificado : new BigDecimal(0);
						prestamohijo.anios[a].noviembre.planificado = prestamohijo.anios[a].noviembre.planificado!=null ? prestamohijo.anios[a].noviembre.planificado : new BigDecimal(0);
						prestamohijo.anios[a].diciembre.planificado = prestamohijo.anios[a].diciembre.planificado!=null ? prestamohijo.anios[a].diciembre.planificado : new BigDecimal(0);
						
						prestamo.anios[a].enero.planificado = prestamo.anios[a].enero.planificado.add(prestamohijo.anios[a].enero.planificado);
						prestamo.anios[a].febrero.planificado = prestamo.anios[a].febrero.planificado.add(prestamohijo.anios[a].febrero.planificado);
						prestamo.anios[a].marzo.planificado = prestamo.anios[a].marzo.planificado.add(prestamohijo.anios[a].marzo.planificado);
						prestamo.anios[a].abril.planificado = prestamo.anios[a].abril.planificado.add(prestamohijo.anios[a].abril.planificado);
						prestamo.anios[a].mayo.planificado = prestamo.anios[a].mayo.planificado.add(prestamohijo.anios[a].mayo.planificado);
						prestamo.anios[a].junio.planificado = prestamo.anios[a].junio.planificado.add(prestamohijo.anios[a].junio.planificado);
						prestamo.anios[a].julio.planificado = prestamo.anios[a].julio.planificado.add(prestamohijo.anios[a].julio.planificado);
						prestamo.anios[a].agosto.planificado = prestamo.anios[a].agosto.planificado.add(prestamohijo.anios[a].agosto.planificado);
						prestamo.anios[a].septiembre.planificado = prestamo.anios[a].septiembre.planificado.add(prestamohijo.anios[a].septiembre.planificado);
						prestamo.anios[a].octubre.planificado = prestamo.anios[a].octubre.planificado.add(prestamohijo.anios[a].octubre.planificado);
						prestamo.anios[a].noviembre.planificado = prestamo.anios[a].noviembre.planificado.add(prestamohijo.anios[a].noviembre.planificado);
						prestamo.anios[a].diciembre.planificado = prestamo.anios[a].diciembre.planificado.add(prestamohijo.anios[a].diciembre.planificado);
					}
				}
			}
		}
		return prestamo;
	}
	
	private byte[] exportarExcel(int prestamoId, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, String usuario){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datosInforme[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "Generando headers");
			headers = generarHeaders(anioInicio, anioFin, agrupacion, tipoVisualizacion);
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1430");
			List<stprestamo> lstPrestamo = getInformacionPresupuestaria(prestamoId, anioInicio, anioFin, usuario);	
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1432");
			lstPrestamo = calcularCostos(lstPrestamo, 0);
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1434");
			
			//datosInforme = generarDatosReporte(lstPrestamo, anioInicio, anioFin, agrupacion, tipoVisualizacion, headers[0].length, usuario);
			datosInforme = new String[][]{
					{"",""}
			};
			
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1437");
			
			//CGraficaExcel grafica = generarGrafica(datosInforme, tipoVisualizacion, agrupacion, anioInicio, anioFin);
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1439");
			
			excel = new CExcel("Ejecución presupuestaria", false, null);
			
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1441");
			wb=excel.generateExcelOfData(datosInforme, "Ejecución presupuestaria", headers, null, true, usuario);
			CLogger.write_simple("5", SInformacionPresupuestaria.class, "1443");
			
		wb.write(outByteStream);
		CLogger.write_simple("5", SInformacionPresupuestaria.class, "1445");
		outArray = Base64.encode(outByteStream.toByteArray());
		CLogger.write_simple("5", SInformacionPresupuestaria.class, "1447");
		}catch(Exception e){
			CLogger.write("5", SInformacionPresupuestaria.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion){
		String headers[][];
		String[][] AgrupacionesTitulo = new String[][]{{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"},
			{"Bimestre 1", "Bimestre 2","Bimestre 3","Bimestre 4","Bimestre 5","Bimestre 6"},
			{"Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4"},
			{"Cuatrimestre 1", "Cuatrimestre 2", "Cuatrimestre 3"},
			{"Semestre 1","Semestre 2"},
			{"Anual"}
		};
		
		int totalesCant = 1;
		int aniosDiferencia =(anioFin-anioInicio)+1; 
		int columnasTotal = (aniosDiferencia*(AgrupacionesTitulo[agrupacion-1].length));
		int factorVisualizacion = 1;
		if(tipoVisualizacion == 2){
			columnasTotal = columnasTotal*2;
			totalesCant=(aniosDiferencia*2)+(totalesCant*2);
			columnasTotal += 1+totalesCant;
			factorVisualizacion = 2;
		}else{
			totalesCant+=aniosDiferencia;
			columnasTotal += 1+totalesCant; //Nombre, totales por año, total
		}
		
		String titulo[] = new String[columnasTotal];
		String tipo[] = new String[columnasTotal];
		String subtitulo[] = new String[columnasTotal];
		String operacionesFila[] = new String[columnasTotal];
		String columnasOperacion[] = new String[columnasTotal];
		String totales[] = new String[totalesCant];
		titulo[0]="Nombre";
		tipo[0]="string";
		subtitulo[0]="";
		operacionesFila[0]="";
		columnasOperacion[0]="";
		for(int i=0;i<totalesCant;i++){ //Inicializar totales
			totales[i]="";
		}
		int pos=1;
		for(int i=0; i<AgrupacionesTitulo[agrupacion-1].length; i++){
			for (int j=0; j<aniosDiferencia; j++){
				titulo[pos] = AgrupacionesTitulo[agrupacion-1][i] + " " + (anioInicio+j);
				tipo[pos] = "double";
				operacionesFila[pos]="";
				columnasOperacion[pos]="";
				totales[j*factorVisualizacion]+=pos+",";
				if(tipoVisualizacion==1){
					subtitulo[pos]="Real";
				}else{
					subtitulo[pos]="Planificado";
				}
				pos++;
				if(tipoVisualizacion == 2){
					titulo[pos] = "";
					tipo[pos] = "double";
					subtitulo[pos]="Real";
					operacionesFila[pos]="";
					columnasOperacion[pos]="";
					totales[(j*factorVisualizacion)+1]+=pos+",";
					pos++;
				}
			}
		}
		for (int j=0; j<aniosDiferencia; j++){
			titulo[pos] = "Total " + (anioInicio+j);
			tipo[pos] = "double";
			operacionesFila[pos]="sum";
			columnasOperacion[pos]= totales[j*factorVisualizacion];
			totales[totalesCant-factorVisualizacion] += pos+",";
			if(tipoVisualizacion==1){
				subtitulo[pos]="Real";
			}else{
				subtitulo[pos]="Planificado";
			}
			pos++;
			if(tipoVisualizacion == 2){
				titulo[pos] = "";
				tipo[pos] = "double";
				subtitulo[pos]="Real";
				operacionesFila[pos]="sum";
				columnasOperacion[pos]=totales[(j*factorVisualizacion)+1];
				totales[totalesCant-1] += pos+",";
				pos++;
			}
		}
		titulo[pos] = "Total";
		tipo[pos] = "double";
		operacionesFila[pos]="sum";
		columnasOperacion[pos]=totales[totalesCant-factorVisualizacion];
		if(tipoVisualizacion==1){
			subtitulo[pos]="Real";
		}else{
			subtitulo[pos]="Planificado";
		}
		pos++;
		if(tipoVisualizacion == 2){
			titulo[pos] = "";
			tipo[pos] = "double";
			subtitulo[pos]="Real";
			operacionesFila[pos]="sum";
			columnasOperacion[pos]=totales[totalesCant-1];
			pos++;
		}
		
		headers = new String[][]{
			titulo,  //titulos
			{""}, //mapeo
			tipo, //tipo dato
			{""}, //operaciones columnas
			{""}, //operaciones div
			subtitulo,
			operacionesFila,
			columnasOperacion
			};
			
		return headers;
	}
	
	public String[][] generarDatosReporte(List<stprestamo> lstPrestamo, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, int columnasTotal, String usuario){
		String[][] datos = null;
		int columna = 0;int factorVisualizacion=1;
		if(tipoVisualizacion==2){
			factorVisualizacion = 2;
		}
		int sumaColumnas = ((anioFin-anioInicio) + 1)*factorVisualizacion;
		
		if (lstPrestamo != null && !lstPrestamo.isEmpty()){
			datos = new String[lstPrestamo.size()][columnasTotal];
			for (int i=0; i<lstPrestamo.size(); i++){
				columna = 0;
				stprestamo prestamo = lstPrestamo.get(i);
				String sangria;
				switch (prestamo.objeto_tipo){
					case 1: sangria = ""; break;
					case 2: sangria = "   "; break;
					case 3: sangria = "      "; break;
					case 4: sangria = "         "; break;
					case 5: sangria = "            "; break;
					default: sangria = "";
				}
				datos[i][columna] = sangria+prestamo.nombre;
				columna++;
					int posicion = columna;
					BigDecimal totalAniosP = new BigDecimal(0);
					BigDecimal totalAniosR = new BigDecimal(0);
					//Valores planificado-real
					for(int a=0; a<prestamo.anios.length; a++){
						posicion = columna + (a*factorVisualizacion);
						
						//Verificar nullos y volverlos 0
						prestamo.anios[a].enero.planificado=(prestamo.anios[a].enero.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].enero.planificado;
						prestamo.anios[a].febrero.planificado=(prestamo.anios[a].febrero.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].febrero.planificado;
						prestamo.anios[a].marzo.planificado=(prestamo.anios[a].marzo.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].marzo.planificado;
						prestamo.anios[a].abril.planificado=(prestamo.anios[a].abril.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].abril.planificado;
						prestamo.anios[a].mayo.planificado=(prestamo.anios[a].mayo.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].mayo.planificado;
						prestamo.anios[a].junio.planificado=(prestamo.anios[a].junio.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].junio.planificado;
						prestamo.anios[a].julio.planificado=(prestamo.anios[a].julio.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].julio.planificado;
						prestamo.anios[a].agosto.planificado=(prestamo.anios[a].agosto.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].agosto.planificado;
						prestamo.anios[a].septiembre.planificado=(prestamo.anios[a].septiembre.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].septiembre.planificado;
						prestamo.anios[a].octubre.planificado=(prestamo.anios[a].octubre.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].octubre.planificado;
						prestamo.anios[a].noviembre.planificado=(prestamo.anios[a].noviembre.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].noviembre.planificado;
						prestamo.anios[a].diciembre.planificado=(prestamo.anios[a].diciembre.planificado==null) ? new BigDecimal(0) : prestamo.anios[a].diciembre.planificado;
						prestamo.anios[a].enero.real=(prestamo.anios[a].enero.real==null) ? new BigDecimal(0) : prestamo.anios[a].enero.real;
						prestamo.anios[a].febrero.real=(prestamo.anios[a].febrero.real==null) ? new BigDecimal(0) : prestamo.anios[a].febrero.real;
						prestamo.anios[a].marzo.real=(prestamo.anios[a].marzo.real==null) ? new BigDecimal(0) : prestamo.anios[a].marzo.real;
						prestamo.anios[a].abril.real=(prestamo.anios[a].abril.real==null) ? new BigDecimal(0) : prestamo.anios[a].abril.real;
						prestamo.anios[a].mayo.real=(prestamo.anios[a].mayo.real==null) ? new BigDecimal(0) : prestamo.anios[a].mayo.real;
						prestamo.anios[a].junio.real=(prestamo.anios[a].junio.real==null) ? new BigDecimal(0) : prestamo.anios[a].junio.real;
						prestamo.anios[a].julio.real=(prestamo.anios[a].julio.real==null) ? new BigDecimal(0) : prestamo.anios[a].julio.real;
						prestamo.anios[a].agosto.real=(prestamo.anios[a].agosto.real==null) ? new BigDecimal(0) : prestamo.anios[a].agosto.real;
						prestamo.anios[a].septiembre.real=(prestamo.anios[a].septiembre.real==null) ? new BigDecimal(0) : prestamo.anios[a].septiembre.real;
						prestamo.anios[a].octubre.real=(prestamo.anios[a].octubre.real==null) ? new BigDecimal(0) : prestamo.anios[a].octubre.real;
						prestamo.anios[a].noviembre.real=(prestamo.anios[a].noviembre.real==null) ? new BigDecimal(0) : prestamo.anios[a].noviembre.real;
						prestamo.anios[a].diciembre.real=(prestamo.anios[a].diciembre.real==null) ? new BigDecimal(0) : prestamo.anios[a].diciembre.real;
						
						BigDecimal totalAnualP = (prestamo.anios[a].enero.planificado.add(prestamo.anios[a].febrero.planificado).add(prestamo.anios[a].marzo.planificado.add(prestamo.anios[a].abril.planificado.add(prestamo.anios[a].mayo.planificado.add(prestamo.anios[a].junio.planificado))))
								.add(prestamo.anios[a].julio.planificado.add(prestamo.anios[a].agosto.planificado).add(prestamo.anios[a].septiembre.planificado.add(prestamo.anios[a].octubre.planificado.add(prestamo.anios[a].noviembre.planificado.add(prestamo.anios[a].diciembre.planificado))))));
						BigDecimal totalAnualR = (prestamo.anios[a].enero.real.add(prestamo.anios[a].febrero.real).add(prestamo.anios[a].marzo.real.add(prestamo.anios[a].abril.real.add(prestamo.anios[a].mayo.real.add(prestamo.anios[a].junio.real))))
								.add(prestamo.anios[a].julio.real.add(prestamo.anios[a].agosto.real).add(prestamo.anios[a].septiembre.real.add(prestamo.anios[a].octubre.real.add(prestamo.anios[a].noviembre.real.add(prestamo.anios[a].diciembre.real))))));
						totalAniosP = totalAniosP.add(totalAnualP);
						totalAniosR = totalAniosR.add(totalAnualR);
						switch(agrupacion){
						case AGRUPACION_MES:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= prestamo.anios[a].enero.planificado.toString();
								datos[i][posicion+(1*sumaColumnas)]= prestamo.anios[a].febrero.planificado.toString();
								datos[i][posicion+(2*sumaColumnas)]= prestamo.anios[a].marzo.planificado.toString();
								datos[i][posicion+(3*sumaColumnas)]= prestamo.anios[a].abril.planificado.toString();
								datos[i][posicion+(4*sumaColumnas)]= prestamo.anios[a].mayo.planificado.toString();
								datos[i][posicion+(5*sumaColumnas)]= prestamo.anios[a].junio.planificado.toString();
								datos[i][posicion+(6*sumaColumnas)]= prestamo.anios[a].julio.planificado.toString();
								datos[i][posicion+(7*sumaColumnas)]= prestamo.anios[a].agosto.planificado.toString();
								datos[i][posicion+(8*sumaColumnas)]= prestamo.anios[a].septiembre.planificado.toString();
								datos[i][posicion+(9*sumaColumnas)]= prestamo.anios[a].octubre.planificado.toString();
								datos[i][posicion+(10*sumaColumnas)]= prestamo.anios[a].noviembre.planificado.toString();
								datos[i][posicion+(11*sumaColumnas)]= prestamo.anios[a].diciembre.planificado.toString();
								datos[i][posicion+(12*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= prestamo.anios[a].enero.real.toString();
								datos[i][posicion+(1*sumaColumnas)]= prestamo.anios[a].febrero.real.toString();
								datos[i][posicion+(2*sumaColumnas)]= prestamo.anios[a].marzo.real.toString();
								datos[i][posicion+(3*sumaColumnas)]= prestamo.anios[a].abril.real.toString();
								datos[i][posicion+(4*sumaColumnas)]= prestamo.anios[a].mayo.real.toString();
								datos[i][posicion+(5*sumaColumnas)]= prestamo.anios[a].junio.real.toString();
								datos[i][posicion+(6*sumaColumnas)]= prestamo.anios[a].julio.real.toString();
								datos[i][posicion+(7*sumaColumnas)]= prestamo.anios[a].agosto.real.toString();
								datos[i][posicion+(8*sumaColumnas)]= prestamo.anios[a].septiembre.real.toString();
								datos[i][posicion+(9*sumaColumnas)]= prestamo.anios[a].octubre.real.toString();
								datos[i][posicion+(10*sumaColumnas)]= prestamo.anios[a].noviembre.real.toString();
								datos[i][posicion+(11*sumaColumnas)]= prestamo.anios[a].diciembre.real.toString();
								datos[i][posicion+(12*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(12*sumaColumnas)+1;
							break;
						case AGRUPACION_BIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].enero.planificado.add(prestamo.anios[a].febrero.planificado)).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].marzo.planificado.add(prestamo.anios[a].abril.planificado)).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mayo.planificado.add(prestamo.anios[a].junio.planificado)).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].julio.planificado.add(prestamo.anios[a].agosto.planificado)).toString();
								datos[i][posicion+(4*sumaColumnas)]= (prestamo.anios[a].septiembre.planificado.add(prestamo.anios[a].octubre.planificado)).toString();
								datos[i][posicion+(5*sumaColumnas)]= (prestamo.anios[a].noviembre.planificado.add(prestamo.anios[a].diciembre.planificado)).toString();
								datos[i][posicion+(6*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].enero.real.add(prestamo.anios[a].febrero.real)).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].marzo.real.add(prestamo.anios[a].abril.real)).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mayo.real.add(prestamo.anios[a].junio.real)).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].julio.real.add(prestamo.anios[a].agosto.real)).toString();
								datos[i][posicion+(4*sumaColumnas)]= (prestamo.anios[a].septiembre.real.add(prestamo.anios[a].octubre.real)).toString();
								datos[i][posicion+(5*sumaColumnas)]= (prestamo.anios[a].noviembre.real.add(prestamo.anios[a].diciembre.real)).toString();
								datos[i][posicion+(6*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(6*sumaColumnas)+1;
							break;
						case AGRUPACION_TRIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].enero.planificado.add(prestamo.anios[a].febrero.planificado.add(prestamo.anios[a].marzo.planificado))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].abril.planificado.add(prestamo.anios[a].mayo.planificado.add(prestamo.anios[a].junio.planificado))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].julio.planificado.add(prestamo.anios[a].agosto.planificado.add(prestamo.anios[a].septiembre.planificado))).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].octubre.planificado.add(prestamo.anios[a].noviembre.planificado.add(prestamo.anios[a].diciembre.planificado))).toString();
								datos[i][posicion+(4*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].enero.real.add(prestamo.anios[a].febrero.real.add(prestamo.anios[a].marzo.real))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].abril.real.add(prestamo.anios[a].mayo.real.add(prestamo.anios[a].junio.real))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].julio.real.add(prestamo.anios[a].agosto.real.add(prestamo.anios[a].septiembre.real))).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].octubre.real.add(prestamo.anios[a].noviembre.real.add(prestamo.anios[a].diciembre.real))).toString();
								datos[i][posicion+(4*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(4*sumaColumnas)+1;
							break;
						case AGRUPACION_CUATRIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].enero.planificado.add(prestamo.anios[a].febrero.planificado).add(prestamo.anios[a].marzo.planificado.add(prestamo.anios[a].abril.planificado))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mayo.planificado).add(prestamo.anios[a].junio.planificado.add(prestamo.anios[a].julio.planificado.add(prestamo.anios[a].agosto.planificado))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].septiembre.planificado.add(prestamo.anios[a].octubre.planificado).add(prestamo.anios[a].noviembre.planificado.add(prestamo.anios[a].diciembre.planificado))).toString();
								datos[i][posicion+(3*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].enero.real.add(prestamo.anios[a].febrero.real).add(prestamo.anios[a].marzo.real.add(prestamo.anios[a].abril.real))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mayo.real).add(prestamo.anios[a].junio.real.add(prestamo.anios[a].julio.real.add(prestamo.anios[a].agosto.real))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].septiembre.real.add(prestamo.anios[a].octubre.real).add(prestamo.anios[a].noviembre.real.add(prestamo.anios[a].diciembre.real))).toString();
								datos[i][posicion+(3*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(3*sumaColumnas)+1;
							break;
						case AGRUPACION_SEMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].enero.planificado.add(prestamo.anios[a].febrero.planificado).add(prestamo.anios[a].marzo.planificado.add(prestamo.anios[a].abril.planificado.add(prestamo.anios[a].mayo.planificado.add(prestamo.anios[a].junio.planificado))))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].julio.planificado.add(prestamo.anios[a].agosto.planificado).add(prestamo.anios[a].septiembre.planificado.add(prestamo.anios[a].octubre.planificado.add(prestamo.anios[a].noviembre.planificado.add(prestamo.anios[a].diciembre.planificado))))).toString();
								datos[i][posicion+(2*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].enero.real.add(prestamo.anios[a].febrero.real).add(prestamo.anios[a].marzo.real.add(prestamo.anios[a].abril.real.add(prestamo.anios[a].mayo.real.add(prestamo.anios[a].junio.real))))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].julio.real.add(prestamo.anios[a].agosto.real).add(prestamo.anios[a].septiembre.real.add(prestamo.anios[a].octubre.real.add(prestamo.anios[a].noviembre.real.add(prestamo.anios[a].diciembre.real))))).toString();
								datos[i][posicion+(2*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(2*sumaColumnas)+1;
							break;
						case AGRUPACION_ANUAL:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= totalAnualP.toString();
								datos[i][posicion+(1*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= totalAnualR.toString();
								datos[i][posicion+(1*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(1*sumaColumnas)+1;
							break;
						}
					}
					if(tipoVisualizacion==0 || tipoVisualizacion==2){
						datos[i][posicion]= totalAniosP.toString();
					}
					if(tipoVisualizacion == 2){
						posicion++;
					}
					if(tipoVisualizacion==1 || tipoVisualizacion == 2){
						datos[i][posicion]= totalAniosR.toString();
					}
			}
		}
		return datos;
	}
	

	
	public CGraficaExcel generarGrafica(String[][] datosTabla, Integer tipoVisualizacion, Integer agrupacion, Integer anioInicio, Integer anioFin){
		String[][] AgrupacionesTitulo = new String[][]{{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"},
			{"Bimestre 1", "Bimestre 2","Bimestre 3","Bimestre 4","Bimestre 5","Bimestre 6"},
			{"Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4"},
			{"Cuatrimestre 1", "Cuatrimestre 2", "Cuatrimestre 3"},
			{"Semestre 1","Semestre 2"},
			{"Anual"}
		};
		
		int aniosDiferencia =(anioFin-anioInicio)+1; 
		int columnasTotal = (aniosDiferencia*(AgrupacionesTitulo[agrupacion-1].length));
		
		String titulo[] = new String[columnasTotal];
		String valor[] = new String[columnasTotal];
		Integer columna=0;
		for(int i=0; i<aniosDiferencia; i++){
			for(int j=0; j<AgrupacionesTitulo[agrupacion-1].length; j++){
				titulo[columna] = AgrupacionesTitulo[agrupacion-1][j] + " - "+(anioInicio+i);
				valor[columna]="0";
				columna++;
			}
			
		}
		String[][] datos = new String[][]{
			titulo,
			valor, 
			valor
		};
		

		columna = 0;
		int columnaDatos=1;
		int factorVisualizacion=1;
		if(tipoVisualizacion==2){
			factorVisualizacion = 2;
		}
		int sumaColumnas = ((anioFin-anioInicio) + 1)*factorVisualizacion;
		
		String[][] datosIgualar= new String[3][columnasTotal];
		
		for(int i=0; i<aniosDiferencia; i++){
			Integer posicionDatos = columnaDatos;
			for(int j=0; j<AgrupacionesTitulo[agrupacion-1].length; j++){
				datosIgualar[0][columna]="";
				
				datosIgualar[1][columna]=posicionDatos+"."+(31);
				if(tipoVisualizacion==2){
					datosIgualar[2][columna]=(posicionDatos+1)+"."+(31);
				}else{
					datosIgualar[2][columna]="";
				}
				posicionDatos+=sumaColumnas;
				columna++;
			}
			columnaDatos+=factorVisualizacion;
		}
		
		String[] tipoData = new String[]{"string","double","double"};
		CGraficaExcel grafica = new CGraficaExcel("Administración Transaccional", CGraficaExcel.EXCEL_CHART_AREA, "Meses", "Planificado", datos, tipoData, datosIgualar);
	
		return grafica;
	}
	
}