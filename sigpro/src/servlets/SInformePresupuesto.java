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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.PagoDAO;
import dao.PlanAdquisicionesDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.ReporteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Pago;
import pojo.PlanAdquisiciones;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SInformePresupuesto")
public class SInformePresupuesto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stprestamo{
		Integer id;
		Integer idPrestamo;
		Integer objetoTipo;
		Integer posicionArbol;
		Integer $$treeLevel;
		Integer idObjetoTipo;
		Integer idPredecesor;
		Integer objetoTipoPredecesor;
		BigDecimal Costo;
		Integer acumulacionCostos;		
		String nombre;
		String fechaInicio;
		String fechaFin;
		String[] hijos;
		BigDecimal mes1r;
		BigDecimal mes1p;
		BigDecimal mes2r;
		BigDecimal mes2p;
		BigDecimal mes3r;
		BigDecimal mes3p;
		BigDecimal mes4r;
		BigDecimal mes4p;
		BigDecimal mes5r;
		BigDecimal mes5p;
		BigDecimal mes6r;
		BigDecimal mes6p;
		BigDecimal mes7r;
		BigDecimal mes7p;
		BigDecimal mes8r;
		BigDecimal mes8p;
		BigDecimal mes9r;
		BigDecimal mes9p;
		BigDecimal mes10r;
		BigDecimal mes10p;
		BigDecimal mes11r;
		BigDecimal mes11p;
		BigDecimal mes12r;
		BigDecimal mes12p;
	}
	
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
       
    public SInformePresupuesto() {
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
		Integer anoInicial = Utils.String2Int(map.get("anoInicial"));
		Integer anoFinal = Utils.String2Int(map.get("anoFinal"));
		
		if(accion.equals("generarInforme")){
			List<stprestamo> resultPrestamo = obtenerProyecto(idPrestamo,usuario, anoInicial, anoFinal);
			
			for(stprestamo prestamo : resultPrestamo){
				Integer posicion = prestamo.posicionArbol;
				prestamo.$$treeLevel = posicion -1;
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultPrestamo);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("exportarExcel")){
			String data = map.get("data");
			String columnas = map.get("columnas");
			String cabeceras = map.get("cabeceras");
			String[] col = cabeceras.split(",");
			Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
			List<Map<String, String>> datos = gson.fromJson(data, listType);

			String nombreInforme = "Informe Ejecución";
			Map<String,Object[]> reporte = new HashMap<>();
			Object[] obj = new Object[col.length];
			
			for(int i=0; i< col.length;i++){
				obj[i] = col[i];
			}
			
			reporte.put("0", obj);
			
			col = columnas.split(",");
			
			obj = new Object[col.length];
			int fila = 1;
			for(Map<String, String> d : datos){
				for(int i=0; i< col.length;i++){
					if(!col[i].equals("nombre"))
						obj[i] = new BigDecimal(d.get(col[i])).doubleValue();
					else
						obj[i] = d.get(col[i]);
				}
				reporte.put(fila+"",obj);
				fila++;
				obj = new Object[col.length];
			}
			
			exportarExcel(reporte,nombreInforme,usuario,response);
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}
	
	private void exportarExcel(Map<String,Object[]> datos, String nombreInforme, String usuario, HttpServletResponse response){
		try{
			CExcel excel = new CExcel("Reporte",false);
			String path = excel.ExportarExcel(datos, nombreInforme, usuario);
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
			CLogger.write("2", SInformePresupuesto.class, e);
		}
	}
	
	private List<stprestamo> obtenerProyecto(int proyectoId, String usuario, Integer ejercicioInicio, Integer ejercicioFin){
		List<stprestamo> lstPrestamo = new ArrayList<stprestamo>();
		stprestamo estructura = new stprestamo();
		
		String[] hijos = null;
		int contadorHijos =0;
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		
		if (proyecto!=null){
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyectoId, 1);
			
			Integer fuente = 0;
			Integer organismo = 0;
			Integer correlativo = 0;
			
			if (prestamo != null){
				Long codigoPresupuestario = prestamo.getCodigoPresupuestario();
				
				if(codigoPresupuestario > 0){
					fuente = Utils.String2Int(Long.toString(codigoPresupuestario).substring(0,2));
					organismo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(2,6));
					correlativo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(6,10));
				}
			}

			estructura.objetoTipo = OBJETO_ID_PROYECTO;
			estructura.posicionArbol = 1;
			estructura.idObjetoTipo = proyecto.getId();
			estructura.nombre = proyecto.getNombre();
			estructura.idPredecesor = 0;
			estructura.objetoTipoPredecesor = 0;
			
			for(int i = ejercicioInicio; i <= ejercicioFin; i++){
				List<?> objetoPrestamo = ReporteDAO.getPresupuestoProyecto(fuente, organismo, correlativo, i);
				
				if(objetoPrestamo.size() > 0){
					for(Object obj : objetoPrestamo){
						Object[] ob = (Object[])obj;
						estructura.mes1r = (BigDecimal)ob[0] == null ? new BigDecimal(0) : (BigDecimal)ob[0];
						estructura.mes2r = (BigDecimal)ob[1] == null ? new BigDecimal(0) : (BigDecimal)ob[1];
						estructura.mes3r = (BigDecimal)ob[2] == null ? new BigDecimal(0) : (BigDecimal)ob[2];
						estructura.mes4r = (BigDecimal)ob[3] == null ? new BigDecimal(0) : (BigDecimal)ob[3];
						estructura.mes5r = (BigDecimal)ob[4] == null ? new BigDecimal(0) : (BigDecimal)ob[4];
						estructura.mes6r = (BigDecimal)ob[5] == null ? new BigDecimal(0) : (BigDecimal)ob[5];
						estructura.mes7r = (BigDecimal)ob[6] == null ? new BigDecimal(0) : (BigDecimal)ob[6];
						estructura.mes8r = (BigDecimal)ob[7] == null ? new BigDecimal(0) : (BigDecimal)ob[7];
						estructura.mes9r = (BigDecimal)ob[8] == null ? new BigDecimal(0) : (BigDecimal)ob[8];
						estructura.mes10r = (BigDecimal)ob[9] == null ? new BigDecimal(0) : (BigDecimal)ob[9];
						estructura.mes11r = (BigDecimal)ob[10] == null ? new BigDecimal(0) : (BigDecimal)ob[10];
						estructura.mes12r = (BigDecimal)ob[11] == null ? new BigDecimal(0) : (BigDecimal)ob[11];
					}
					
					estructura.mes1p = new BigDecimal(0);
					estructura.mes2p = new BigDecimal(0);
					estructura.mes3p = new BigDecimal(0);
					estructura.mes4p = new BigDecimal(0);
					estructura.mes5p = new BigDecimal(0);
					estructura.mes6p = new BigDecimal(0);
					estructura.mes7p = new BigDecimal(0);
					estructura.mes8p = new BigDecimal(0);
					estructura.mes9p = new BigDecimal(0);
					estructura.mes10p = new BigDecimal(0);
					estructura.mes11p = new BigDecimal(0);
					estructura.mes12p = new BigDecimal(0);
					
					List<PlanAdquisiciones> listaPlan = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(1, proyecto.getId());
					
					if(listaPlan != null){
						List<Pago> pagos = new ArrayList<Pago>();
						for(PlanAdquisiciones plan : listaPlan){
							pagos = PagoDAO.getPagosByIdPlan(plan.getId());
							
							for(Pago pago : pagos){
								Date fechaPago = pago.getFechaPago();
								Integer mes = Utils.String2Int(Utils.formatDate(fechaPago).split("/")[1]);
								
								switch (mes){
								case 1:
									estructura.mes1p = estructura.mes1p.add(pago.getPago());
									break;
								case 2:
									estructura.mes2p = estructura.mes2p.add(pago.getPago());
									break;
								case 3:
									estructura.mes3p = estructura.mes3p.add(pago.getPago());
									break;
								case 4:
									estructura.mes4p = estructura.mes4p.add(pago.getPago());
									break;
								case 5: 
									estructura.mes5p = estructura.mes5p.add(pago.getPago());
									break;
								case 6:
									estructura.mes6p = estructura.mes6p.add(pago.getPago());
									break;
								case 7:
									estructura.mes7p = estructura.mes7p.add(pago.getPago());
									break;
								case 8:
									estructura.mes8p = estructura.mes8p.add(pago.getPago());
									break;
								case 9:
									estructura.mes9p = estructura.mes9p.add(pago.getPago());
									break;
								case 10:
									estructura.mes10p = estructura.mes10p.add(pago.getPago());
									break;
								case 11:
									estructura.mes11p = estructura.mes11p.add(pago.getPago());
									break;
								case 12:
									estructura.mes12p = estructura.mes1p.add(pago.getPago());
									break;
								}
							}
						}
					}
				}else{
					estructura.mes1r = new BigDecimal(0);
					estructura.mes2r = new BigDecimal(0);
					estructura.mes3r = new BigDecimal(0);
					estructura.mes4r = new BigDecimal(0);
					estructura.mes5r = new BigDecimal(0);
					estructura.mes6r = new BigDecimal(0);
					estructura.mes7r = new BigDecimal(0);
					estructura.mes8r = new BigDecimal(0);
					estructura.mes9r = new BigDecimal(0);
					estructura.mes10r = new BigDecimal(0);
					estructura.mes11r = new BigDecimal(0);
					estructura.mes12r = new BigDecimal(0);
				}
			}

			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);
			
			hijos = new String[componentes.size()];
			for (Componente componente : componentes){
				hijos[contadorHijos] = componente.getId().toString() + ",2";
				contadorHijos++;
			}
			estructura.hijos = hijos;
			lstPrestamo.add(estructura);
						
			for (Componente componente : componentes){
				estructura = new stprestamo();
				estructura.objetoTipo = OBJETO_ID_COMPONENTE;
				estructura.posicionArbol = 2;
				estructura.idObjetoTipo = componente.getId();
				estructura.nombre = componente.getNombre();
				estructura.idPredecesor = proyecto.getId();
				estructura.objetoTipoPredecesor = 1;
				
				List<?> objeto = new ArrayList<Object>();
				
				for(int i = ejercicioInicio; i <= ejercicioFin; i++){
					objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, componente.getPrograma(), componente.getSubprograma(), componente.getProyecto_1(), componente.getActividad(), componente.getObra());
					
					if(objeto.size() > 0){
						for(Object obj : objeto){
							Object[] ob = (Object[])obj;
							estructura.mes1r = (BigDecimal)ob[0] == null ? new BigDecimal(0) : (BigDecimal)ob[0];
							estructura.mes2r = (BigDecimal)ob[1] == null ? new BigDecimal(0) : (BigDecimal)ob[1];
							estructura.mes3r = (BigDecimal)ob[2] == null ? new BigDecimal(0) : (BigDecimal)ob[2];
							estructura.mes4r = (BigDecimal)ob[3] == null ? new BigDecimal(0) : (BigDecimal)ob[3];
							estructura.mes5r = (BigDecimal)ob[4] == null ? new BigDecimal(0) : (BigDecimal)ob[4];
							estructura.mes6r = (BigDecimal)ob[5] == null ? new BigDecimal(0) : (BigDecimal)ob[5];
							estructura.mes7r = (BigDecimal)ob[6] == null ? new BigDecimal(0) : (BigDecimal)ob[6];
							estructura.mes8r = (BigDecimal)ob[7] == null ? new BigDecimal(0) : (BigDecimal)ob[7];
							estructura.mes9r = (BigDecimal)ob[8] == null ? new BigDecimal(0) : (BigDecimal)ob[8];
							estructura.mes10r = (BigDecimal)ob[9] == null ? new BigDecimal(0) : (BigDecimal)ob[9];
							estructura.mes11r = (BigDecimal)ob[10] == null ? new BigDecimal(0) : (BigDecimal)ob[10];
							estructura.mes12r = (BigDecimal)ob[11] == null ? new BigDecimal(0) : (BigDecimal)ob[11];
						}
						
						estructura.mes1p = new BigDecimal(0);
						estructura.mes2p = new BigDecimal(0);
						estructura.mes3p = new BigDecimal(0);
						estructura.mes4p = new BigDecimal(0);
						estructura.mes5p = new BigDecimal(0);
						estructura.mes6p = new BigDecimal(0);
						estructura.mes7p = new BigDecimal(0);
						estructura.mes8p = new BigDecimal(0);
						estructura.mes9p = new BigDecimal(0);
						estructura.mes10p = new BigDecimal(0);
						estructura.mes11p = new BigDecimal(0);
						estructura.mes12p = new BigDecimal(0);
						
						List<PlanAdquisiciones> listaPlan = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(2, componente.getId());
						
						if(listaPlan != null && listaPlan.size() > 0){
							List<Pago> pagos = new ArrayList<Pago>();
							for(PlanAdquisiciones plan : listaPlan){
								pagos = PagoDAO.getPagosByIdPlan(plan.getId());
								
								for(Pago pago : pagos){
									Date fechaPago = pago.getFechaPago();
									Integer mes = Utils.String2Int(Utils.formatDate(fechaPago).split("/")[1]);
									
									switch (mes){
									case 1:
										estructura.mes1p = estructura.mes1p.add(pago.getPago());
										break;
									case 2:
										estructura.mes2p = estructura.mes2p.add(pago.getPago());
										break;
									case 3:
										estructura.mes3p = estructura.mes3p.add(pago.getPago());
										break;
									case 4:
										estructura.mes4p = estructura.mes4p.add(pago.getPago());
										break;
									case 5: 
										estructura.mes5p = estructura.mes5p.add(pago.getPago());
										break;
									case 6:
										estructura.mes6p = estructura.mes6p.add(pago.getPago());
										break;
									case 7:
										estructura.mes7p = estructura.mes7p.add(pago.getPago());
										break;
									case 8:
										estructura.mes8p = estructura.mes8p.add(pago.getPago());
										break;
									case 9:
										estructura.mes9p = estructura.mes9p.add(pago.getPago());
										break;
									case 10:
										estructura.mes10p = estructura.mes10p.add(pago.getPago());
										break;
									case 11:
										estructura.mes11p = estructura.mes11p.add(pago.getPago());
										break;
									case 12:
										estructura.mes12p = estructura.mes12p.add(pago.getPago());
										break;
									}
								}
							}
						}
					}else{
						estructura.mes1r = new BigDecimal(0);
						estructura.mes2r = new BigDecimal(0);
						estructura.mes3r = new BigDecimal(0);
						estructura.mes4r = new BigDecimal(0);
						estructura.mes5r = new BigDecimal(0);
						estructura.mes6r = new BigDecimal(0);
						estructura.mes7r = new BigDecimal(0);
						estructura.mes8r = new BigDecimal(0);
						estructura.mes9r = new BigDecimal(0);
						estructura.mes10r = new BigDecimal(0);
						estructura.mes11r = new BigDecimal(0);
						estructura.mes12r = new BigDecimal(0);
						
						estructura.mes1p = new BigDecimal(0);
						estructura.mes2p = new BigDecimal(0);
						estructura.mes3p = new BigDecimal(0);
						estructura.mes4p = new BigDecimal(0);
						estructura.mes5p = new BigDecimal(0);
						estructura.mes6p = new BigDecimal(0);
						estructura.mes7p = new BigDecimal(0);
						estructura.mes8p = new BigDecimal(0);
						estructura.mes9p = new BigDecimal(0);
						estructura.mes10p = new BigDecimal(0);
						estructura.mes11p = new BigDecimal(0);
						estructura.mes12p = new BigDecimal(0);
					}
					
				}
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				
				hijos = new String[productos.size()];
				contadorHijos = 0;
				for (Producto producto : productos){
					hijos[contadorHijos] = producto.getId().toString() + ",3";
					contadorHijos++;
				}
				estructura.hijos = hijos;
				lstPrestamo.add(estructura);
				
				for (Producto producto : productos){
					estructura = new stprestamo();
					estructura.objetoTipo = OBJETO_ID_PRODUCTO;
					estructura.posicionArbol = 3;
					estructura.idObjetoTipo = producto.getId();
					estructura.nombre = producto.getNombre();
					estructura.idPredecesor = componente.getId();
					estructura.objetoTipoPredecesor = 2;
							
					for(int i = ejercicioInicio; i <= ejercicioFin; i++){
						objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, producto.getPrograma(), producto.getSubprograma(), producto.getProyecto(), producto.getActividad(), producto.getObra());
						
						if(objeto.size() > 0){
							for(Object obj : objeto){
								Object[] ob = (Object[])obj;
								estructura.mes1r = (BigDecimal)ob[0] == null ? new BigDecimal(0) : (BigDecimal)ob[0];
								estructura.mes2r = (BigDecimal)ob[1] == null ? new BigDecimal(0) : (BigDecimal)ob[1];
								estructura.mes3r = (BigDecimal)ob[2] == null ? new BigDecimal(0) : (BigDecimal)ob[2];
								estructura.mes4r = (BigDecimal)ob[3] == null ? new BigDecimal(0) : (BigDecimal)ob[3];
								estructura.mes5r = (BigDecimal)ob[4] == null ? new BigDecimal(0) : (BigDecimal)ob[4];
								estructura.mes6r = (BigDecimal)ob[5] == null ? new BigDecimal(0) : (BigDecimal)ob[5];
								estructura.mes7r = (BigDecimal)ob[6] == null ? new BigDecimal(0) : (BigDecimal)ob[6];
								estructura.mes8r = (BigDecimal)ob[7] == null ? new BigDecimal(0) : (BigDecimal)ob[7];
								estructura.mes9r = (BigDecimal)ob[8] == null ? new BigDecimal(0) : (BigDecimal)ob[8];
								estructura.mes10r = (BigDecimal)ob[9] == null ? new BigDecimal(0) : (BigDecimal)ob[9];
								estructura.mes11r = (BigDecimal)ob[10] == null ? new BigDecimal(0) : (BigDecimal)ob[10];
								estructura.mes12r = (BigDecimal)ob[11] == null ? new BigDecimal(0) : (BigDecimal)ob[11];
							}
							
							estructura.mes1p = new BigDecimal(0);
							estructura.mes2p = new BigDecimal(0);
							estructura.mes3p = new BigDecimal(0);
							estructura.mes4p = new BigDecimal(0);
							estructura.mes5p = new BigDecimal(0);
							estructura.mes6p = new BigDecimal(0);
							estructura.mes7p = new BigDecimal(0);
							estructura.mes8p = new BigDecimal(0);
							estructura.mes9p = new BigDecimal(0);
							estructura.mes10p = new BigDecimal(0);
							estructura.mes11p = new BigDecimal(0);
							estructura.mes12p = new BigDecimal(0);
							
							List<PlanAdquisiciones> listaPlan = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(3, producto.getId());
							
							if(listaPlan != null && listaPlan.size() > 0){
								List<Pago> pagos = new ArrayList<Pago>();
								for(PlanAdquisiciones plan : listaPlan){
									pagos = PagoDAO.getPagosByIdPlan(plan.getId());
									
									for(Pago pago : pagos){
										Date fechaPago = pago.getFechaPago();
										Integer mes = Utils.String2Int(Utils.formatDate(fechaPago).split("/")[1]);
										
										switch (mes){
										case 1:
											estructura.mes1p = estructura.mes1p.add(pago.getPago());
											break;
										case 2:
											estructura.mes2p = estructura.mes2p.add(pago.getPago());
											break;
										case 3:
											estructura.mes3p = estructura.mes3p.add(pago.getPago());
											break;
										case 4:
											estructura.mes4p = estructura.mes4p.add(pago.getPago());
											break;
										case 5: 
											estructura.mes5p = estructura.mes5p.add(pago.getPago());
											break;
										case 6:
											estructura.mes6p = estructura.mes6p.add(pago.getPago());
											break;
										case 7:
											estructura.mes7p = estructura.mes7p.add(pago.getPago());
											break;
										case 8:
											estructura.mes8p = estructura.mes8p.add(pago.getPago());
											break;
										case 9:
											estructura.mes9p = estructura.mes9p.add(pago.getPago());
											break;
										case 10:
											estructura.mes10p = estructura.mes10p.add(pago.getPago());
											break;
										case 11:
											estructura.mes11p = estructura.mes11p.add(pago.getPago());
											break;
										case 12:
											estructura.mes12p = estructura.mes12p.add(pago.getPago());
											break;
										}
									}
								}
							}
						}else{
							estructura.mes1r = new BigDecimal(0);
							estructura.mes2r = new BigDecimal(0);
							estructura.mes3r = new BigDecimal(0);
							estructura.mes4r = new BigDecimal(0);
							estructura.mes5r = new BigDecimal(0);
							estructura.mes6r = new BigDecimal(0);
							estructura.mes7r = new BigDecimal(0);
							estructura.mes8r = new BigDecimal(0);
							estructura.mes9r = new BigDecimal(0);
							estructura.mes10r = new BigDecimal(0);
							estructura.mes11r = new BigDecimal(0);
							estructura.mes12r = new BigDecimal(0);
							
							estructura.mes1p = new BigDecimal(0);
							estructura.mes2p = new BigDecimal(0);
							estructura.mes3p = new BigDecimal(0);
							estructura.mes4p = new BigDecimal(0);
							estructura.mes5p = new BigDecimal(0);
							estructura.mes6p = new BigDecimal(0);
							estructura.mes7p = new BigDecimal(0);
							estructura.mes8p = new BigDecimal(0);
							estructura.mes9p = new BigDecimal(0);
							estructura.mes10p = new BigDecimal(0);
							estructura.mes11p = new BigDecimal(0);
							estructura.mes12p = new BigDecimal(0);
						}
					}

					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					
					hijos = new String[subproductos.size()];
					contadorHijos = 0;
					for (Subproducto subproducto : subproductos){
						hijos[contadorHijos] = subproducto.getId().toString() + ",4";
						contadorHijos++;
					}
					estructura.hijos = hijos;
					lstPrestamo.add(estructura);
					
					for (Subproducto subproducto : subproductos){
						estructura = new stprestamo();
						estructura.objetoTipo = OBJETO_ID_SUBPRODUCTO;
						estructura.posicionArbol = 4;
						estructura.idObjetoTipo = subproducto.getId();
						estructura.nombre = subproducto.getNombre();
						estructura.idPredecesor = producto.getId();
						estructura.objetoTipoPredecesor = 3;
						
						for(int i = ejercicioInicio; i <= ejercicioFin; i++){
							objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, subproducto.getPrograma(), subproducto.getSubprograma(), subproducto.getProyecto(), subproducto.getActividad(), subproducto.getObra());
							
							if(objeto.size() > 0){
								for(Object obj : objeto){
									Object[] ob = (Object[])obj;
									estructura.mes1r = (BigDecimal)ob[0] == null ? new BigDecimal(0) : (BigDecimal)ob[0];
									estructura.mes2r = (BigDecimal)ob[1] == null ? new BigDecimal(0) : (BigDecimal)ob[1];
									estructura.mes3r = (BigDecimal)ob[2] == null ? new BigDecimal(0) : (BigDecimal)ob[2];
									estructura.mes4r = (BigDecimal)ob[3] == null ? new BigDecimal(0) : (BigDecimal)ob[3];
									estructura.mes5r = (BigDecimal)ob[4] == null ? new BigDecimal(0) : (BigDecimal)ob[4];
									estructura.mes6r = (BigDecimal)ob[5] == null ? new BigDecimal(0) : (BigDecimal)ob[5];
									estructura.mes7r = (BigDecimal)ob[6] == null ? new BigDecimal(0) : (BigDecimal)ob[6];
									estructura.mes8r = (BigDecimal)ob[7] == null ? new BigDecimal(0) : (BigDecimal)ob[7];
									estructura.mes9r = (BigDecimal)ob[8] == null ? new BigDecimal(0) : (BigDecimal)ob[8];
									estructura.mes10r = (BigDecimal)ob[9] == null ? new BigDecimal(0) : (BigDecimal)ob[9];
									estructura.mes11r = (BigDecimal)ob[10] == null ? new BigDecimal(0) : (BigDecimal)ob[10];
									estructura.mes12r = (BigDecimal)ob[11] == null ? new BigDecimal(0) : (BigDecimal)ob[11];
								}
								
								estructura.mes1p = new BigDecimal(0);
								estructura.mes2p = new BigDecimal(0);
								estructura.mes3p = new BigDecimal(0);
								estructura.mes4p = new BigDecimal(0);
								estructura.mes5p = new BigDecimal(0);
								estructura.mes6p = new BigDecimal(0);
								estructura.mes7p = new BigDecimal(0);
								estructura.mes8p = new BigDecimal(0);
								estructura.mes9p = new BigDecimal(0);
								estructura.mes10p = new BigDecimal(0);
								estructura.mes11p = new BigDecimal(0);
								estructura.mes12p = new BigDecimal(0);
								
								List<PlanAdquisiciones> listaPlan = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(4, subproducto.getId());
								
								if(listaPlan != null && listaPlan.size() > 0){
									List<Pago> pagos = new ArrayList<Pago>();
									for(PlanAdquisiciones plan : listaPlan){
										pagos = PagoDAO.getPagosByIdPlan(plan.getId());
										
										for(Pago pago : pagos){
											Date fechaPago = pago.getFechaPago();
											Integer mes = Utils.String2Int(Utils.formatDate(fechaPago).split("/")[1]);
											
											switch (mes){
											case 1:
												estructura.mes1p = estructura.mes1p.add(pago.getPago());
												break;
											case 2:
												estructura.mes2p = estructura.mes2p.add(pago.getPago());
												break;
											case 3:
												estructura.mes3p = estructura.mes3p.add(pago.getPago());
												break;
											case 4:
												estructura.mes4p = estructura.mes4p.add(pago.getPago());
												break;
											case 5: 
												estructura.mes5p = estructura.mes5p.add(pago.getPago());
												break;
											case 6:
												estructura.mes6p = estructura.mes6p.add(pago.getPago());
												break;
											case 7:
												estructura.mes7p = estructura.mes7p.add(pago.getPago());
												break;
											case 8:
												estructura.mes8p = estructura.mes8p.add(pago.getPago());
												break;
											case 9:
												estructura.mes9p = estructura.mes9p.add(pago.getPago());
												break;
											case 10:
												estructura.mes10p = estructura.mes10p.add(pago.getPago());
												break;
											case 11:
												estructura.mes11p = estructura.mes11p.add(pago.getPago());
												break;
											case 12:
												estructura.mes12p = estructura.mes12p.add(pago.getPago());
												break;
											}
										}
									}
								}
							}else{
								estructura.mes1r = new BigDecimal(0);
								estructura.mes2r = new BigDecimal(0);
								estructura.mes3r = new BigDecimal(0);
								estructura.mes4r = new BigDecimal(0);
								estructura.mes5r = new BigDecimal(0);
								estructura.mes6r = new BigDecimal(0);
								estructura.mes7r = new BigDecimal(0);
								estructura.mes8r = new BigDecimal(0);
								estructura.mes9r = new BigDecimal(0);
								estructura.mes10r = new BigDecimal(0);
								estructura.mes11r = new BigDecimal(0);
								estructura.mes12r = new BigDecimal(0);
								
								estructura.mes1p = new BigDecimal(0);
								estructura.mes2p = new BigDecimal(0);
								estructura.mes3p = new BigDecimal(0);
								estructura.mes4p = new BigDecimal(0);
								estructura.mes5p = new BigDecimal(0);
								estructura.mes6p = new BigDecimal(0);
								estructura.mes7p = new BigDecimal(0);
								estructura.mes8p = new BigDecimal(0);
								estructura.mes9p = new BigDecimal(0);
								estructura.mes10p = new BigDecimal(0);
								estructura.mes11p = new BigDecimal(0);
								estructura.mes12p = new BigDecimal(0);
							}
						}
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null, null, null, null, null, usuario);
						
						
						hijos = new String[actividades.size()];
						contadorHijos = 0;
						for (Actividad actividad : actividades){
							if(actividad.getCosto().compareTo(BigDecimal.ZERO) != 0 && actividad.getCostoReal().compareTo(BigDecimal.ZERO) != 0){
								hijos[contadorHijos] = actividad.getId().toString() + ",5";
								contadorHijos++;
							}
						}
						estructura.hijos = hijos;
						lstPrestamo.add(estructura);
						
						for (Actividad actividad : actividades ){
							lstPrestamo = ObtenerActividades(actividad,usuario,lstPrestamo, OBJETO_ID_ACTIVIDAD,subproducto.getId(), OBJETO_ID_SUBPRODUCTO, ejercicioInicio, ejercicioFin, proyectoId);
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){
						lstPrestamo = ObtenerActividades(actividad,usuario,lstPrestamo,OBJETO_ID_SUBPRODUCTO,producto.getId(), OBJETO_ID_PRODUCTO, ejercicioInicio,ejercicioFin, proyectoId);
					}
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					lstPrestamo = ObtenerActividades(actividad,usuario,lstPrestamo,OBJETO_ID_PRODUCTO, componente.getId(),OBJETO_ID_COMPONENTE, ejercicioInicio, ejercicioFin,proyectoId);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				lstPrestamo = ObtenerActividades(actividad,usuario,lstPrestamo,OBJETO_ID_COMPONENTE, proyecto.getId(), OBJETO_ID_PROYECTO, ejercicioInicio,ejercicioFin,proyectoId);
			}
		}
		
		return lstPrestamo;
	}
	
	private List<stprestamo> ObtenerActividades(Actividad actividad, String usuario, List<stprestamo> lstPrestamo, int posicionArbol,
			int idPredecesor, int objetoTipoPredecesor, Integer ejercicioInicio, Integer ejercicioFin, Integer idPrestamo){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		if ((actividad.getCosto() != null && actividad.getCosto().compareTo(BigDecimal.ZERO) != 0) || (actividad.getCostoReal() != null && actividad.getCostoReal().compareTo(BigDecimal.ZERO) != 0)){			
			Integer actividadId = actividad.getId();
			stprestamo estructura = new stprestamo();
			estructura.objetoTipo = OBJETO_ID_ACTIVIDAD;
			estructura.posicionArbol = posicionArbol;
			estructura.idObjetoTipo = actividadId;
			estructura.nombre = actividad.getNombre();
			estructura.idPredecesor = idPredecesor;
			estructura.objetoTipoPredecesor = objetoTipoPredecesor;
			estructura.Costo = actividad.getCosto() == null ? new BigDecimal(0) : actividad.getCosto();
			
			String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad, usuario).split(";");

			estructura.fechaInicio = fechaInicioFin[0];
			estructura.fechaFin = fechaInicioFin[1];
			estructura.acumulacionCostos = actividad.getAcumulacionCosto() == null ? 3 : actividad.getAcumulacionCosto().getId();

			
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(idPrestamo, 1);
			Long codigoPresupuestario = prestamo.getCodigoPresupuestario();
			
			Integer fuente = Utils.String2Int(Long.toString(codigoPresupuestario).substring(0,2));
			Integer organismo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(2,6));
			Integer correlativo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(6,10));
			
			for(int i = ejercicioInicio; i <= ejercicioFin; i++){
				List<?> objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, actividad.getPrograma(), actividad.getSubprograma(), actividad.getProyecto(), actividad.getActividad(), actividad.getObra());
				
				if(objeto.size() > 0){
					for(Object obj : objeto){
						Object[] ob = (Object[])obj;
						estructura.mes1r = (BigDecimal)ob[0] == null ? new BigDecimal(0) : (BigDecimal)ob[0];
						estructura.mes2r = (BigDecimal)ob[1] == null ? new BigDecimal(0) : (BigDecimal)ob[1];
						estructura.mes3r = (BigDecimal)ob[2] == null ? new BigDecimal(0) : (BigDecimal)ob[2];
						estructura.mes4r = (BigDecimal)ob[3] == null ? new BigDecimal(0) : (BigDecimal)ob[3];
						estructura.mes5r = (BigDecimal)ob[4] == null ? new BigDecimal(0) : (BigDecimal)ob[4];
						estructura.mes6r = (BigDecimal)ob[5] == null ? new BigDecimal(0) : (BigDecimal)ob[5];
						estructura.mes7r = (BigDecimal)ob[6] == null ? new BigDecimal(0) : (BigDecimal)ob[6];
						estructura.mes8r = (BigDecimal)ob[7] == null ? new BigDecimal(0) : (BigDecimal)ob[7];
						estructura.mes9r = (BigDecimal)ob[8] == null ? new BigDecimal(0) : (BigDecimal)ob[8];
						estructura.mes10r = (BigDecimal)ob[9] == null ? new BigDecimal(0) : (BigDecimal)ob[9];
						estructura.mes11r = (BigDecimal)ob[10] == null ? new BigDecimal(0) : (BigDecimal)ob[10];
						estructura.mes12r = (BigDecimal)ob[11] == null ? new BigDecimal(0) : (BigDecimal)ob[11];
					}
					
					estructura.mes1p = new BigDecimal(0);
					estructura.mes2p = new BigDecimal(0);
					estructura.mes3p = new BigDecimal(0);
					estructura.mes4p = new BigDecimal(0);
					estructura.mes5p = new BigDecimal(0);
					estructura.mes6p = new BigDecimal(0);
					estructura.mes7p = new BigDecimal(0);
					estructura.mes8p = new BigDecimal(0);
					estructura.mes9p = new BigDecimal(0);
					estructura.mes10p = new BigDecimal(0);
					estructura.mes11p = new BigDecimal(0);
					estructura.mes12p = new BigDecimal(0);
					
					if(estructura.Costo.compareTo(BigDecimal.ZERO) == 0){
						List<PlanAdquisiciones> listaPlan = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(2, actividad.getId());
						
						if(listaPlan != null && listaPlan.size() > 0){
							List<Pago> pagos = new ArrayList<Pago>();
							for(PlanAdquisiciones plan : listaPlan){
								pagos = PagoDAO.getPagosByIdPlan(plan.getId());
								
								for(Pago pago : pagos){
									Date fechaPago = pago.getFechaPago();
									Integer mes = Utils.String2Int(Utils.formatDate(fechaPago).split("/")[1]);
									
									switch (mes){
									case 1:
										estructura.mes1p = estructura.mes1p.add(pago.getPago());
										break;
									case 2:
										estructura.mes2p = estructura.mes2p.add(pago.getPago());
										break;
									case 3:
										estructura.mes3p = estructura.mes3p.add(pago.getPago());
										break;
									case 4:
										estructura.mes4p = estructura.mes4p.add(pago.getPago());
										break;
									case 5: 
										estructura.mes5p = estructura.mes5p.add(pago.getPago());
										break;
									case 6:
										estructura.mes6p = estructura.mes6p.add(pago.getPago());
										break;
									case 7:
										estructura.mes7p = estructura.mes7p.add(pago.getPago());
										break;
									case 8:
										estructura.mes8p = estructura.mes8p.add(pago.getPago());
										break;
									case 9:
										estructura.mes9p = estructura.mes9p.add(pago.getPago());
										break;
									case 10:
										estructura.mes10p = estructura.mes10p.add(pago.getPago());
										break;
									case 11:
										estructura.mes11p = estructura.mes11p.add(pago.getPago());
										break;
									case 12:
										estructura.mes12p = estructura.mes12p.add(pago.getPago());
										break;
									}
								}
							}
						}
					}else{
						Integer mes = Utils.String2Int(estructura.fechaFin.split("/")[1]);
						
						switch (mes){
						case 1:
							estructura.mes1p = estructura.mes1p.add(estructura.Costo);
							break;
						case 2:
							estructura.mes2p = estructura.mes2p.add(estructura.Costo);
							break;
						case 3:
							estructura.mes3p = estructura.mes3p.add(estructura.Costo);
							break;
						case 4:
							estructura.mes4p = estructura.mes4p.add(estructura.Costo);
							break;
						case 5: 
							estructura.mes5p = estructura.mes5p.add(estructura.Costo);
							break;
						case 6:
							estructura.mes6p = estructura.mes6p.add(estructura.Costo);
							break;
						case 7:
							estructura.mes7p = estructura.mes7p.add(estructura.Costo);
							break;
						case 8:
							estructura.mes8p = estructura.mes8p.add(estructura.Costo);
							break;
						case 9:
							estructura.mes9p = estructura.mes9p.add(estructura.Costo);
							break;
						case 10:
							estructura.mes10p = estructura.mes10p.add(estructura.Costo);
							break;
						case 11:
							estructura.mes11p = estructura.mes11p.add(estructura.Costo);
							break;
						case 12:
							estructura.mes12p = estructura.mes12p.add(estructura.Costo);
							break;
						}
					}
				}else{
					estructura.mes1r = new BigDecimal(0);
					estructura.mes2r = new BigDecimal(0);
					estructura.mes3r = new BigDecimal(0);
					estructura.mes4r = new BigDecimal(0);
					estructura.mes5r = new BigDecimal(0);
					estructura.mes6r = new BigDecimal(0);
					estructura.mes7r = new BigDecimal(0);
					estructura.mes8r = new BigDecimal(0);
					estructura.mes9r = new BigDecimal(0);
					estructura.mes10r = new BigDecimal(0);
					estructura.mes11r = new BigDecimal(0);
					estructura.mes12r = new BigDecimal(0);
					
					estructura.mes1p = new BigDecimal(0);
					estructura.mes2p = new BigDecimal(0);
					estructura.mes3p = new BigDecimal(0);
					estructura.mes4p = new BigDecimal(0);
					estructura.mes5p = new BigDecimal(0);
					estructura.mes6p = new BigDecimal(0);
					estructura.mes7p = new BigDecimal(0);
					estructura.mes8p = new BigDecimal(0);
					estructura.mes9p = new BigDecimal(0);
					estructura.mes10p = new BigDecimal(0);
					estructura.mes11p = new BigDecimal(0);
					estructura.mes12p = new BigDecimal(0);
				}
			}
			
			String[] hijos = new String[actividades.size()];
			int contadorHijos = 0;
			for(Actividad subActividad : actividades){
					hijos[contadorHijos] = subActividad.getId().toString() + ",5";
					contadorHijos++;
			}
			estructura.hijos = hijos;
			lstPrestamo.add(estructura);
			for(Actividad subActividad : actividades){
				lstPrestamo = ObtenerActividades(subActividad, usuario, lstPrestamo, posicionArbol + 1, actividadId, OBJETO_ID_ACTIVIDAD, ejercicioInicio,ejercicioFin,idPrestamo);
			}
		}
		return lstPrestamo;
	}
}