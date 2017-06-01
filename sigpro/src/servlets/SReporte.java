package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import dao.ComponenteDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.ReporteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.EstadoInforme;
import pojo.InformePresupuesto;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.Utils;

@WebServlet("/SReporte")
public class SReporte extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
    
	class stdataEjecutado {
		Integer idObjetoTipo;
		Integer id_tabla;
		Integer objetoTipo;
		Integer posicionArbol;
		String objetoTipoNombre;
		String nombre;
		Integer idPredecesor;
		Integer objetoTipoPredecesor;
		String hijo;
		BigDecimal Mes1;
		BigDecimal Mes2;
		BigDecimal Mes3;
		BigDecimal Mes4;
		BigDecimal Mes5;
		BigDecimal Mes6;
		BigDecimal Mes7;
		BigDecimal Mes8;
		BigDecimal Mes9;
		BigDecimal Mes10;
		BigDecimal Mes11;
		BigDecimal Mes12;
		BigDecimal Total;
	}
	
	class stInformePresupuesto{
		int id;
		int estadoInforme;
		int idPrestamo;
		int idObjetoTipo;
		int objetoTipo;
		int posicionArbol;
		String nombre;
		int idPredecesor;
		int objetoTipoPredecesor;
		String hijo;
		BigDecimal Mes1;
		BigDecimal Mes2;
		BigDecimal Mes3;
		BigDecimal Mes4;
		BigDecimal Mes5;
		BigDecimal Mes6;
		BigDecimal Mes7;
		BigDecimal Mes8;
		BigDecimal Mes9;
		BigDecimal Mes10;
		BigDecimal Mes11;
		BigDecimal Mes12;
		BigDecimal Total;
		String anio;
		String usuarioCreo;
		String usuarioActualizo;
		Date fechaCreacion;
		Date fechaActualizacion;
		int estado;
	}
	
    public SReporte() {
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
		
		if(accion.equals("getCargaTrabajoPrestamo")){
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
			Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
			Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
			Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
			
			List<?> actividades_proceso = ReporteDAO.getCargaTrabajo(0,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
			List<?> actividades_atrasadas = ReporteDAO.getCargaTrabajo(1,objetoTipo, idPrestamo, idComponente, idProducto, idSubProducto);
			
			String JsonProceso = Utils.getJSonString("actividadesProceso", actividades_proceso);
			String JsonAtrasadas = Utils.getJSonString("actividadesAtrasadas", actividades_atrasadas);

			response_text = JsonProceso + "," + JsonAtrasadas;
			response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("getAdquisicionesPrestamo")){
			List<stdataEjecutado> prestamo = obtenerProyecto(idPrestamo,usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(prestamo);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("generarInforme")){
			Integer tipoInforme = Utils.String2Int(map.get("tipoInforme"),0);
			Date anio = Utils.dateFromString(map.get("anio"));
			List<InformePresupuesto> informePresupuesto = ReporteDAO.existeInformeBase(idPrestamo, tipoInforme, map.get("anio"));
			
			if (informePresupuesto.size() == 0 && tipoInforme != 2){
				List<stdataEjecutado> prestamo = obtenerProyecto(idPrestamo,usuario);
				
				for(stdataEjecutado p : prestamo){
					InformePresupuesto temp = new InformePresupuesto();
					
					temp.setIdPrestamo(idPrestamo);
					
					EstadoInforme estadoInforme = new EstadoInforme();
					estadoInforme.setId(tipoInforme);
					
					temp.setEstadoInforme(estadoInforme);
					temp.setObjetoTipo(p.objetoTipo);
					temp.setPosicionArbol(p.posicionArbol);
					temp.setObjetoTipoId(p.idObjetoTipo);
					temp.setObjetoNombre(p.nombre);
					temp.setIdPredecesor(p.idPredecesor);
					temp.setObjetoTipoPredecesor(p.objetoTipoPredecesor);
					temp.setMes1(p.Mes1);
					temp.setMes2(p.Mes2);
					temp.setMes3(p.Mes3);
					temp.setMes4(p.Mes4);
					temp.setMes5(p.Mes5);
					temp.setMes6(p.Mes6);
					temp.setMes7(p.Mes7);
					temp.setMes8(p.Mes8);
					temp.setMes9(p.Mes9);
					temp.setMes10(p.Mes10);
					temp.setMes11(p.Mes11);
					temp.setMes12(p.Mes12);
					temp.setTotal(p.Total);
					temp.setAnio(anio);
					temp.setUsuarioCreo(usuario);
					temp.setFechaCreacion(new DateTime().toDate());
					temp.setEstado(1);
					
					ReporteDAO.agregarRowInformePresupuesto(temp);
				}
				
				informePresupuesto = ReporteDAO.existeInformeBase(idPrestamo, tipoInforme, map.get("anio"));
				List<stInformePresupuesto> resultPrestamo = new ArrayList<stInformePresupuesto>();
				for (InformePresupuesto informe : informePresupuesto ){
					stInformePresupuesto dataEjecutado = new stInformePresupuesto();
					dataEjecutado.id = informe.getId();
					dataEjecutado.idPrestamo = idPrestamo;
					dataEjecutado.estadoInforme = tipoInforme;
					dataEjecutado.objetoTipo = informe.getObjetoTipo();
					dataEjecutado.posicionArbol = informe.getPosicionArbol();
					dataEjecutado.idObjetoTipo = informe.getObjetoTipoId();
					dataEjecutado.nombre = informe.getObjetoNombre();
					dataEjecutado.idPredecesor = informe.getIdPredecesor();
					dataEjecutado.objetoTipoPredecesor = informe.getObjetoTipoPredecesor();
					dataEjecutado.Mes1 = informe.getMes1();
					dataEjecutado.Mes2 = informe.getMes2();
					dataEjecutado.Mes3 = informe.getMes3();
					dataEjecutado.Mes4 = informe.getMes4();
					dataEjecutado.Mes5 = informe.getMes5();
					dataEjecutado.Mes6 = informe.getMes6();
					dataEjecutado.Mes7 = informe.getMes7();
					dataEjecutado.Mes8 = informe.getMes8();
					dataEjecutado.Mes9 = informe.getMes9();
					dataEjecutado.Mes10 = informe.getMes10();
					dataEjecutado.Mes11 = informe.getMes11();
					dataEjecutado.Mes12 = informe.getMes12();
					dataEjecutado.Total = informe.getTotal();
					dataEjecutado.estado = informe.getEstado();
					resultPrestamo.add(dataEjecutado);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
		        response_text = String.join("", "\"prestamo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}else{
				if(informePresupuesto.size() == 0 && tipoInforme == 2){
					response_text = String.join("", "\"prestamo\":[{\"tipoInforme\":2}]");
					response_text = String.join("", "{\"success\":false,", response_text, "}");
				}else if(informePresupuesto.size() > 0 && (tipoInforme == 2 || tipoInforme == 1)){
					List<stInformePresupuesto> resultPrestamo = new ArrayList<stInformePresupuesto>();
					for (InformePresupuesto informe : informePresupuesto ){
						stInformePresupuesto dataEjecutado = new stInformePresupuesto();
						dataEjecutado.id = informe.getId();
						dataEjecutado.idPrestamo = idPrestamo;
						dataEjecutado.estadoInforme = tipoInforme;
						dataEjecutado.objetoTipo = informe.getObjetoTipo();
						dataEjecutado.posicionArbol = informe.getPosicionArbol();
						dataEjecutado.idObjetoTipo = informe.getObjetoTipoId();
						dataEjecutado.nombre = informe.getObjetoNombre();
						dataEjecutado.idPredecesor = informe.getIdPredecesor();
						dataEjecutado.objetoTipoPredecesor = informe.getObjetoTipoPredecesor();
						dataEjecutado.Mes1 = informe.getMes1();
						dataEjecutado.Mes2 = informe.getMes2();
						dataEjecutado.Mes3 = informe.getMes3();
						dataEjecutado.Mes4 = informe.getMes4();
						dataEjecutado.Mes5 = informe.getMes5();
						dataEjecutado.Mes6 = informe.getMes6();
						dataEjecutado.Mes7 = informe.getMes7();
						dataEjecutado.Mes8 = informe.getMes8();
						dataEjecutado.Mes9 = informe.getMes9();
						dataEjecutado.Mes10 = informe.getMes10();
						dataEjecutado.Mes11 = informe.getMes11();
						dataEjecutado.Mes12 = informe.getMes12();
						dataEjecutado.Total = informe.getTotal();
						dataEjecutado.estado = informe.getEstado();
						resultPrestamo.add(dataEjecutado);
					}
					
					
					informePresupuesto = ReporteDAO.existeInformeBase(idPrestamo, 2, map.get("anio"));
					
					if(informePresupuesto.size() > 0){
						response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
				        response_text = String.join("", "\"prestamo\":",response_text);
				        response_text = String.join("", "\"existeCopia\":true,", response_text);
				        response_text = String.join("", "{\"success\":true,", response_text, "}");
					}else{
						response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
				        response_text = String.join("", "\"prestamo\":",response_text);
						response_text = String.join("", "{\"success\":true,", response_text, "}");
					}
				}
			}
		}else if(accion.equals("guardarInformePrestamo")){
			int id = Utils.String2Int(map.get("id"));
			BigDecimal Mes1 = new BigDecimal(map.get("mes1"));
			BigDecimal Mes2 = new BigDecimal(map.get("mes2"));
			BigDecimal Mes3 = new BigDecimal(map.get("mes3"));
			BigDecimal Mes4 = new BigDecimal(map.get("mes4"));
			BigDecimal Mes5 = new BigDecimal(map.get("mes5"));
			BigDecimal Mes6 = new BigDecimal(map.get("mes6"));
			BigDecimal Mes7 = new BigDecimal(map.get("mes7"));
			BigDecimal Mes8 = new BigDecimal(map.get("mes8"));
			BigDecimal Mes9 = new BigDecimal(map.get("mes8"));
			BigDecimal Mes10 = new BigDecimal(map.get("mes9"));
			BigDecimal Mes11 = new BigDecimal(map.get("mes10"));
			BigDecimal Mes12 = new BigDecimal(map.get("mes11"));
			BigDecimal Total = new BigDecimal(map.get("total"));
			
			List<InformePresupuesto> row = ReporteDAO.getrowInformebyId(id);
			
			for(InformePresupuesto r : row){
				InformePresupuesto temp = new InformePresupuesto();
				
				temp.setId(r.getId());
				temp.setAnio(r.getAnio());
				temp.setEstadoInforme(r.getEstadoInforme());
				temp.setObjetoTipoId(r.getObjetoTipoId());
				temp.setObjetoTipo(r.getObjetoTipo());
				temp.setPosicionArbol(r.getPosicionArbol());
				temp.setObjetoNombre(r.getObjetoNombre());
				temp.setIdPredecesor(r.getIdPredecesor());
				temp.setIdPrestamo(r.getIdPrestamo());
				temp.setObjetoTipoPredecesor(r.getObjetoTipoPredecesor());
				temp.setMes1(Mes1);
				temp.setMes2(Mes2);
				temp.setMes3(Mes3);
				temp.setMes4(Mes4);
				temp.setMes5(Mes5);
				temp.setMes6(Mes6);
				temp.setMes7(Mes7);
				temp.setMes8(Mes8);
				temp.setMes9(Mes9);
				temp.setMes10(Mes10);
				temp.setMes11(Mes11);
				temp.setMes12(Mes12);
				temp.setTotal(Total);
				temp.setEstado(r.getEstado());
				temp.setUsuarioCreo(r.getUsuarioCreo());
				temp.setUsuarioActualizo(usuario);
				temp.setFechaCreacion(r.getFechaCreacion());
				temp.setFechaActualizacion(new DateTime().toDate());
				
				ReporteDAO.agregarRowInformePresupuesto(temp);
				response_text = "{\"success\":true}";
			}
		}else if(accion.equals("congelarInforme")){
			int id = Utils.String2Int(map.get("id"));
			
			List<InformePresupuesto> row = ReporteDAO.getrowInformebyId(id);
			
			for(InformePresupuesto r : row){
				InformePresupuesto temp = new InformePresupuesto();
				
				temp.setId(r.getId());
				temp.setAnio(r.getAnio());
				temp.setEstadoInforme(r.getEstadoInforme());
				temp.setObjetoTipoId(r.getObjetoTipoId());
				temp.setObjetoTipo(r.getObjetoTipo());
				temp.setPosicionArbol(r.getPosicionArbol());
				temp.setObjetoNombre(r.getObjetoNombre());
				temp.setIdPredecesor(r.getIdPredecesor());
				temp.setObjetoTipoPredecesor(r.getObjetoTipoPredecesor());
				temp.setIdPrestamo(r.getIdPrestamo());
				temp.setMes1(r.getMes1());
				temp.setMes2(r.getMes2());
				temp.setMes3(r.getMes3());
				temp.setMes4(r.getMes4());
				temp.setMes5(r.getMes5());
				temp.setMes6(r.getMes6());
				temp.setMes7(r.getMes7());
				temp.setMes8(r.getMes8());
				temp.setMes9(r.getMes9());
				temp.setMes10(r.getMes10());
				temp.setMes11(r.getMes11());
				temp.setMes12(r.getMes12());
				temp.setTotal(r.getTotal());
				temp.setEstado(2);
				temp.setUsuarioCreo(r.getUsuarioCreo());
				temp.setUsuarioActualizo(usuario);
				temp.setFechaCreacion(r.getFechaCreacion());
				temp.setFechaActualizacion(new DateTime().toDate());
				
				if (ReporteDAO.agregarRowInformePresupuesto(temp))
					response_text = String.join("", "{\"success\":true}");
				else
					response_text = String.join("", "{\"success\":false}");
			}
		}else if(accion.equals("copiarInformePrestamo")){
			int id = Utils.String2Int(map.get("id"));
			List<InformePresupuesto> row = ReporteDAO.getrowInformebyId(id);
			
			for(InformePresupuesto r : row){
				if(r.getEstado() == 2){
					List<InformePresupuesto> informePresupuesto = ReporteDAO.existeInformeBase(idPrestamo, 1, map.get("anio"));
					for (InformePresupuesto informe : informePresupuesto){
						InformePresupuesto temp = new InformePresupuesto();
						temp.setIdPrestamo(informe.getIdPrestamo());
						
						EstadoInforme tipoPresupuesto = new EstadoInforme();
						tipoPresupuesto.setId(2);
						temp.setEstadoInforme(tipoPresupuesto);
						temp.setObjetoTipoId(informe.getObjetoTipoId());
						temp.setObjetoTipo(informe.getObjetoTipo());
						temp.setPosicionArbol(informe.getPosicionArbol());
						temp.setObjetoNombre(informe.getObjetoNombre());
						temp.setIdPredecesor(informe.getIdPredecesor());
						temp.setObjetoTipoPredecesor(informe.getObjetoTipoPredecesor());
						temp.setMes1(informe.getMes1());
						temp.setMes2(informe.getMes2());
						temp.setMes3(informe.getMes3());
						temp.setMes4(informe.getMes4());
						temp.setMes5(informe.getMes5());
						temp.setMes6(informe.getMes6());
						temp.setMes7(informe.getMes7());
						temp.setMes8(informe.getMes8());
						temp.setMes9(informe.getMes9());
						temp.setMes10(informe.getMes10());
						temp.setMes11(informe.getMes11());
						temp.setMes12(informe.getMes12());
						temp.setTotal(informe.getTotal());
						temp.setAnio(informe.getAnio());
						temp.setUsuarioCreo(usuario);
						temp.setFechaCreacion(new DateTime().toDate());
						temp.setEstado(1);
						ReporteDAO.agregarRowInformePresupuesto(temp);
					}
					
					informePresupuesto = ReporteDAO.existeInformeBase(idPrestamo, 2, map.get("anio"));
					List<stInformePresupuesto> resultPrestamo = new ArrayList<stInformePresupuesto>();
					for (InformePresupuesto informe : informePresupuesto ){
						stInformePresupuesto dataEjecutado = new stInformePresupuesto();
						dataEjecutado.id = informe.getId();
						dataEjecutado.idPrestamo = idPrestamo;
						dataEjecutado.estadoInforme = 2;
						dataEjecutado.objetoTipo = informe.getObjetoTipo();
						dataEjecutado.posicionArbol = informe.getPosicionArbol();
						dataEjecutado.idObjetoTipo = informe.getObjetoTipoId();
						dataEjecutado.nombre = informe.getObjetoNombre();
						dataEjecutado.idPredecesor = informe.getIdPredecesor();
						dataEjecutado.objetoTipoPredecesor = informe.getObjetoTipoPredecesor();
						dataEjecutado.Mes1 = informe.getMes1();
						dataEjecutado.Mes2 = informe.getMes2();
						dataEjecutado.Mes3 = informe.getMes3();
						dataEjecutado.Mes4 = informe.getMes4();
						dataEjecutado.Mes5 = informe.getMes5();
						dataEjecutado.Mes6 = informe.getMes6();
						dataEjecutado.Mes7 = informe.getMes7();
						dataEjecutado.Mes8 = informe.getMes8();
						dataEjecutado.Mes9 = informe.getMes9();
						dataEjecutado.Mes10 = informe.getMes10();
						dataEjecutado.Mes11 = informe.getMes11();
						dataEjecutado.Mes12 = informe.getMes12();
						dataEjecutado.Total = informe.getTotal();
						dataEjecutado.estado = informe.getEstado();
						resultPrestamo.add(dataEjecutado);
					}

					response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
			        response_text = String.join("", "\"prestamo\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}else
				{
					response_text = String.join("", "{\"success\":false}");		
				}
			}
		}else if(accion.equals("exportarExcel")){
			CExcel excel = new CExcel("Informe",false);
			Integer estadoInforme = Utils.String2Int(map.get("estadoInforme"));
			List<InformePresupuesto> informePresupuesto = ReporteDAO.existeInformeBase(idPrestamo, estadoInforme, map.get("anio"));
			
			if(informePresupuesto.size() > 0){
				Map<String,Object[]> datos = new HashMap<>();
				datos.put("0", new Object[] {"Nombre", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre", "Total"});
				
				int fila = 1;
				Object[] filaTotal = new Object []{};
				
				for(InformePresupuesto informe : informePresupuesto){
					if(fila==1)
						filaTotal = new Object [] {"Total General", informe.getMes1().doubleValue(), informe.getMes2().doubleValue(), 
								informe.getMes3().doubleValue(), informe.getMes4().doubleValue(), informe.getMes5().doubleValue(), 
								informe.getMes6().doubleValue(), informe.getMes7().doubleValue(), informe.getMes8().doubleValue(), 
								informe.getMes9().doubleValue(),informe.getMes10().doubleValue(),informe.getMes11().doubleValue(), 
								informe.getMes12().doubleValue(),informe.getTotal().doubleValue()};
					datos.put(fila+"", new Object [] {informe.getObjetoNombre(), informe.getMes1().doubleValue(), informe.getMes2().doubleValue(), 
							informe.getMes3().doubleValue(), informe.getMes4().doubleValue(), informe.getMes5().doubleValue(), 
							informe.getMes6().doubleValue(), informe.getMes7().doubleValue(), informe.getMes8().doubleValue(), 
							informe.getMes9().doubleValue(),informe.getMes10().doubleValue(),informe.getMes11().doubleValue(), 
							informe.getMes12().doubleValue(),informe.getTotal().doubleValue()});
					fila++;
				}
				datos.put(fila+"",filaTotal);
				
				String path = excel.ExportarExcel(datos, "Informe Ejecución Anual", usuario);
	
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
			        	
			        }
			        //
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
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private void setValoresMensuales(stdataEjecutado dataEjecutado){
		dataEjecutado.Mes1 = new BigDecimal(0);
		dataEjecutado.Mes2 = new BigDecimal(0);
		dataEjecutado.Mes3 = new BigDecimal(0);
		dataEjecutado.Mes4 = new BigDecimal(0);
		dataEjecutado.Mes5 = new BigDecimal(0);
		dataEjecutado.Mes6 = new BigDecimal(0);
		dataEjecutado.Mes7 = new BigDecimal(0);
		dataEjecutado.Mes8 = new BigDecimal(0);
		dataEjecutado.Mes9 = new BigDecimal(0);
		dataEjecutado.Mes10 = new BigDecimal(0);
		dataEjecutado.Mes11 = new BigDecimal(0);
		dataEjecutado.Mes12 = new BigDecimal(0);
		dataEjecutado.Total = new BigDecimal(0);
	}
	
	private List<stdataEjecutado> obtenerProyecto(int proyectoId, String usuario){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		List<stdataEjecutado> lstdataEjecutado = new ArrayList<>();
		if (proyecto!=null){
			stdataEjecutado dataEjecutado = new stdataEjecutado();
			dataEjecutado.objetoTipo = OBJETO_ID_PROYECTO;
			dataEjecutado.posicionArbol = 1;
			dataEjecutado.objetoTipoNombre = "Prestamo";
			dataEjecutado.idObjetoTipo = proyecto.getId();
			dataEjecutado.nombre = proyecto.getNombre();
			dataEjecutado.idPredecesor = 0;
			dataEjecutado.objetoTipoPredecesor = 0;
			setValoresMensuales(dataEjecutado);
			lstdataEjecutado.add(dataEjecutado);

			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);
			for (Componente componente : componentes){
				dataEjecutado = new stdataEjecutado();
				dataEjecutado.objetoTipo = OBJETO_ID_COMPONENTE;
				dataEjecutado.posicionArbol = 2;
				dataEjecutado.objetoTipoNombre = "Componente";
				dataEjecutado.idObjetoTipo = componente.getId();
				dataEjecutado.nombre = componente.getNombre();
				dataEjecutado.idPredecesor = proyecto.getId();
				dataEjecutado.objetoTipoPredecesor = 1;
				setValoresMensuales(dataEjecutado);
				lstdataEjecutado.add(dataEjecutado);
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				for (Producto producto : productos){
					dataEjecutado = new stdataEjecutado();
					dataEjecutado.objetoTipo = OBJETO_ID_PRODUCTO;
					dataEjecutado.posicionArbol = 3;
					dataEjecutado.objetoTipoNombre = "Producto";
					dataEjecutado.nombre = producto.getNombre();
					dataEjecutado.idObjetoTipo = producto.getId();
					dataEjecutado.idPredecesor = componente.getId();
					dataEjecutado.objetoTipoPredecesor = 2;
					setValoresMensuales(dataEjecutado);
					lstdataEjecutado.add(dataEjecutado);
					
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					for (Subproducto subproducto : subproductos){
						dataEjecutado = new stdataEjecutado();
						dataEjecutado.objetoTipo = OBJETO_ID_SUBPRODUCTO;
						dataEjecutado.posicionArbol = 4;
						dataEjecutado.objetoTipoNombre = "Subroducto";
						dataEjecutado.idObjetoTipo = subproducto.getId();
						dataEjecutado.nombre =   subproducto.getNombre();
						dataEjecutado.idPredecesor = producto.getId();
						dataEjecutado.objetoTipoPredecesor = 3;
						lstdataEjecutado.add(dataEjecutado);
						setValoresMensuales(dataEjecutado);
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null, null, null, null, null, usuario);
						for (Actividad actividad : actividades ){
							lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado, OBJETO_ID_ACTIVIDAD,subproducto.getId(), OBJETO_ID_SUBPRODUCTO);
						}
					}
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
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				lstdataEjecutado = ObtenerActividades(actividad,usuario,lstdataEjecutado,OBJETO_ID_COMPONENTE, proyecto.getId(), OBJETO_ID_PROYECTO);
			}
		}
		
		return lstdataEjecutado;
	}
	
	private List<stdataEjecutado> ObtenerActividades(Actividad actividad, String usuario, List<stdataEjecutado> lstdataEjecutado, int posicionArbol, 
			int idPredecesor, int objetoTipoPredecesor){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		stdataEjecutado dataEjecutado = new stdataEjecutado();
		dataEjecutado = new stdataEjecutado();
		dataEjecutado.objetoTipo = OBJETO_ID_ACTIVIDAD;
		dataEjecutado.posicionArbol = posicionArbol;
		dataEjecutado.objetoTipoNombre = "Actividad";
		dataEjecutado.idObjetoTipo = actividad.getId();
		dataEjecutado.nombre =   actividad.getNombre();
		dataEjecutado.idPredecesor = idPredecesor;
		dataEjecutado.objetoTipoPredecesor = objetoTipoPredecesor;
		setValoresMensuales(dataEjecutado);
		lstdataEjecutado.add(dataEjecutado);
		
		for(Actividad subActividad : actividades){
			lstdataEjecutado = ObtenerActividades(subActividad, usuario, lstdataEjecutado, posicionArbol + 1, actividad.getId(), OBJETO_ID_ACTIVIDAD);
		}
		
		return lstdataEjecutado;
	}
}
