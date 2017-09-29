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
import dao.CategoriaAdquisicionDAO;
import dao.ComponenteDAO;
import dao.EstructuraProyectoDAO;
import dao.PlanAdquisicionesDetalleDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.CategoriaAdquisicion;
import pojo.Componente;
import pojo.PlanAdquisiciones;
import pojo.PlanAdquisicionesDetalle;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SControlAdquisiciones")
public class SControlAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stcontroladquisiciones{
		Integer objetoId;
		Integer objetoTipo;
		Integer predecesorId;
		Integer objetoPredecesorTipo;
		Integer idPlanAdquisiciones;
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
	}
	
    public SControlAdquisiciones() {
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
			
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			
			if (accion.equals("generarPlan")){
				
				try{
					List<stcontroladquisiciones> lstprestamo = generarPlan(idPrestamo, usuario);
										
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			        response_text = String.join("", "\"proyecto\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}
				catch (Exception e){
					CLogger.write("2", SControlAdquisiciones.class, e);
				}
			}else if(accion.equals("guardarPlan")){
				try{
					boolean result = false;
					String data = map.get("data");
					String[] datos = data.split("\\|");
					
					for(String str1: datos){
						String[] row = str1.split(",");
						
						Integer objetoId = Utils.String2Int(row[0]);
						Integer objetoTipo = Utils.String2Int(row[1]);
						Integer tipoAdquisicion = Utils.String2Int(row[2]);
						Integer categoriaAdquisicionId = Utils.String2Int(row[3]);
						
						CategoriaAdquisicion categoriaAdquisicion;
						if(categoriaAdquisicionId != 0){
							categoriaAdquisicion = new CategoriaAdquisicion();
							categoriaAdquisicion.setId(categoriaAdquisicionId);					
						}else
						{
							categoriaAdquisicion = null;
						}
						
						Integer planAdquisicionesId = Utils.String2Int(row[4]);
						PlanAdquisiciones planAdquisiciones;
						if(planAdquisicionesId != 0){
							planAdquisiciones = new PlanAdquisiciones();
							planAdquisiciones.setId(planAdquisicionesId);
						}else{
							planAdquisiciones = null;
						}
							
						String unidadMedida = row[5] == null ? "" : row[5]; 
						
						Integer cantidad = 0;
						if(!row[6].equals("null"))
							cantidad = Utils.String2Int(row[6]);
						BigDecimal costo = BigDecimal.ZERO;
						if(!row[7].equals("null"))
							costo = new BigDecimal(row[7]);
						BigDecimal total = BigDecimal.ZERO;
						if(!row[8].equals("null"))
							total = new BigDecimal(row[8]);

						if(objetoTipo == 1 && !total.equals(BigDecimal.ZERO)){
							Proyecto proyecto = ProyectoDAO.getProyectoPorId(objetoId, usuario);
							proyecto.setCosto(total);
							proyecto.setUsuarioActualizo(usuario);
							proyecto.setFechaActualizacion(new Date());
							ProyectoDAO.guardarProyecto(proyecto);
						}else if(objetoTipo == 2 && !total.equals(BigDecimal.ZERO)){
							Componente componente = ComponenteDAO.getComponentePorId(objetoId, usuario);
							componente.setCosto(total);
							componente.setUsuarioActualizo(usuario);
							componente.setFechaActualizacion(new Date());
							ComponenteDAO.guardarComponente(componente);
						}else if(objetoTipo == 3 && !total.equals(BigDecimal.ZERO)){
							Producto producto = ProductoDAO.getProductoPorId(objetoId,usuario);
							producto.setCosto(total);
							producto.setUsuarioActualizo(usuario);
							producto.setFechaActualizacion(new Date());
							ProductoDAO.guardarProducto(producto);
						}else if(objetoTipo == 4 && !total.equals(BigDecimal.ZERO)){
							Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId, usuario);
							subproducto.setCosto(total);
							subproducto.setUsuarioActualizo(usuario);
							subproducto.setFechaActualizacion(new Date());
							SubproductoDAO.guardarSubproducto(subproducto);
						}else if(objetoTipo == 5 && !total.equals(BigDecimal.ZERO) ){
							Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
							actividad.setCosto(total);
							actividad.setUsuarioActualizo(usuario);
							actividad.setFechaActualizacion(new Date());
							ActividadDAO.guardarActividad(actividad);
						}
						
						Date planificadoDocs = Utils.dateFromString(row[9]);
						Date realDocs = Utils.dateFromString(row[10]);
						Date planificadoLanzamiento = Utils.dateFromString(row[11]);
						Date realLanzamiento = Utils.dateFromString(row[12]);
						Date planificadoRecepcionEval = Utils.dateFromString(row[13]);
						Date realRecepcionEval = Utils.dateFromString(row[14]);
						Date planificadoAdjudica = Utils.dateFromString(row[15]);
						Date realAdjudica = Utils.dateFromString(row[16]);
						Date planificadoFirma =  Utils.dateFromString(row[17]);
						Date realFirma = Utils.dateFromString(row[18]);
						Integer bloqueado = Utils.String2Boolean(row[19],0);
						Long nog = Utils.String2Long(row[20]);
						String numeroContrato = row[21];
						BigDecimal montoContrato = BigDecimal.ZERO;
						if (!row[22].equals("null"))
							montoContrato = new BigDecimal(row[22]);
						PlanAdquisicionesDetalle plan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(objetoTipo, objetoId);
						if(plan != null){
							plan.setTipoAdquisicion(tipoAdquisicion);
							plan.setCategoriaAdquisicion(categoriaAdquisicion);
							plan.setUnidadMedida(unidadMedida);
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
							plan.setObjetoId(objetoId);
							plan.setObjetoTipo(objetoTipo);
							plan.setUsuarioCreo(usuario);
							plan.setFechaCreacion(new DateTime().toDate());
							plan.setEstado(1);
							plan.setBloqueado(bloqueado);
							plan.setNog(nog);
							plan.setNumeroContrato(numeroContrato);
							plan.setMontoContrato(montoContrato);
						}else{
							plan = new PlanAdquisicionesDetalle(categoriaAdquisicion,planAdquisiciones, tipoAdquisicion,unidadMedida,cantidad, total,  costo, 
									planificadoDocs, realDocs, planificadoLanzamiento, realLanzamiento, planificadoRecepcionEval, realRecepcionEval, 
									planificadoAdjudica, realAdjudica, planificadoFirma, realFirma, objetoId, objetoTipo, usuario, null,new DateTime().toDate(), null,1,
									bloqueado,  numeroContrato, montoContrato,nog);
						}
						
						result = PlanAdquisicionesDetalleDAO.guardarPlanAdquisicion(plan);
						
						if(!result)
							break;
					}
					
					response_text = String.join("","{ \"success\": ",(result ? "true" : "false"), "}");
				}
				catch (Throwable e) {
					CLogger.write("3", SControlAdquisiciones.class, e);
				}
			}else if(accion.equals("guardarMontoContrato")){
				Integer objetoId = Utils.String2Int(map.get("objetoId"));
				Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
				String numeroContrato = map.get("numeroContrato");
				BigDecimal montoContrato = Utils.String2BigDecimal(map.get("montoContrato"), null);
				
				PlanAdquisicionesDetalle planAdquisicionDetalle = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(objetoTipo, objetoId);
				planAdquisicionDetalle.setMontoContrato(montoContrato);
				planAdquisicionDetalle.setNumeroContrato(numeroContrato);
				PlanAdquisicionesDetalleDAO.guardarPlanAdquisicion(planAdquisicionDetalle);
				
			}else if(accion.equals("exportarExcel")){
				Integer idPlanAdquisiciones = Utils.String2Int(map.get("idPlanAdquisiciones"), null);
				try{ 
					byte [] outArray = exportarExcel(idPlanAdquisiciones, idPrestamo, usuario);
					
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "attachment; Plan_de_Adquisiciones.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}catch(Exception e){
					CLogger.write("4", SControlAdquisiciones.class, e);
				}
			}else if(accion.equals("exportarPdf")){
				CPdf archivo = new CPdf("Plan de adquisiciones");
				String headers[][];
				String datos[][];
				headers = generarHeaders();
				datos = generarDatos(idPrestamo, usuario);
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
					response.setHeader("Content-Disposition", "in-line; 'planAdquisiciones.pdf'");
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
			CLogger.write("1", SControlAdquisiciones.class, e);
		}
	}
	
	private List<stcontroladquisiciones> generarPlan(Integer idPrestamo, String usuario) throws Exception{
		try{
			List<stcontroladquisiciones> lstPrestamo = new ArrayList<>();
			List<?> estruturaProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
			stcontroladquisiciones temp = null;
			Integer proyectoId = 0;
			Integer componenteId = 0;
			Integer productoId = 0;
			
			for(Object objeto: estruturaProyecto){
				//Integer idPlanAdquisiciones = 0;
				Object[] obj = (Object[]) objeto;
				Integer nivel = (Integer)obj[4];
				if(nivel != null){
					temp = new stcontroladquisiciones();
					temp.objetoId = (Integer)obj[0];
					String tabs = "%"+nivel+"s";
					temp.nombre = nivel != 0 ? String.format(tabs, (String)obj[1]).replace(' ', '\t') : (String)obj[1];
					temp.objetoTipo = ((BigInteger)obj[2]).intValue();
					temp.nivel = nivel;
					
					inicializarPlanAdquisiciones(temp);
					
					switch(temp.objetoTipo){
						case 1:
							temp.objetoPredecesorTipo = 0;
							temp.predecesorId = 0;
							proyectoId = temp.objetoId;
							
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
							//PlanAdquisiciones planAdquisicion = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(temp.objetoTipo, temp.objetoId);
							//idPlanAdquisiciones = planAdquisicion != null ? planAdquisicion.getId() : null;
							break;
						case 2:
							temp.objetoPredecesorTipo = 1;
							temp.predecesorId = proyectoId;
							componenteId = temp.objetoId;
							
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
						break;
						case 3:
							temp.objetoPredecesorTipo = 2;
							temp.predecesorId = componenteId;
							productoId = temp.objetoId;
							
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
						break;
						case 4:
							temp.objetoPredecesorTipo = 3;
							temp.predecesorId = productoId;
							temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);
						break;
						case 5:
							Actividad actividad = ActividadDAO.getActividadPorId(temp.objetoId);
							if(actividad != null){
								temp.predecesorId = actividad.getObjetoId();
								temp.objetoPredecesorTipo = actividad.getPredObjetoTipo();
								temp.hijos = EstructuraProyectoDAO.getHijos((String)obj[3], estruturaProyecto);	
							}
						break;
					}
					
					lstPrestamo.add(temp);
				}
			}
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("1", SControlAdquisiciones.class, e);
			return null;
		}
	}
	
	private void inicializarPlanAdquisiciones(stcontroladquisiciones tempPrestamo){
		tempPrestamo.idPlanAdquisiciones = 0;
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
	
//	private List<stcontroladquisiciones> generarPlan(Integer idPrestamo, String usuario) throws Exception{
//		try{
//			List<stcontroladquisiciones> lstprestamo = new ArrayList<stcontroladquisiciones>();
//			stcontroladquisiciones tempPrestamo = new stcontroladquisiciones();
//			
//			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
//					
//			PlanAdquisiciones planAdquisicion = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(1, proyecto.getId());
//			Integer idPlanAdquisiciones = planAdquisicion != null ? planAdquisicion.getId() : null;
//			
//			tempPrestamo.objetoId = proyecto.getId();
//			tempPrestamo.nombre = proyecto.getNombre();
//			tempPrestamo.objetoTipo = 1;
//			tempPrestamo.nivel = 1;
//			tempPrestamo.predecesorId = 0;
//			tempPrestamo.objetoPredecesorTipo = 0;
//			inicializarColumnasOcultas(tempPrestamo);
//			
//			if(idPlanAdquisiciones == null){
//				planAdquisicion = new PlanAdquisiciones();
//				planAdquisicion.setObjetoId(proyecto.getId());
//				planAdquisicion.setObjetoTipo(1);
//				planAdquisicion.setUsuarioCreo(usuario);
//				planAdquisicion.setFechaCreacion(new DateTime().toDate());
//				planAdquisicion.setEstado(1);
//				PlanAdquisicionesDAO.guardarPlanAdquisicion(planAdquisicion);
//				idPlanAdquisiciones = planAdquisicion.getId();
//			}
//			
//			if(idPlanAdquisiciones != null){
//				PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(1, proyecto.getId());
//					tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//					tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//					tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//					tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//					tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//					tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//					tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//					tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//					tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//					tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//					tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//					tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//					tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//					tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//					tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//					tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//					tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//					tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//					tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//					tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//					tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//			}
//			
//			if(CMariaDB.connect()){
//				Connection conn = CMariaDB.getConnection();
//				
//				ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
//				
//				tempPrestamo.hijos = new ArrayList<String>();
//				for(Integer componente: componentes){
//					tempPrestamo.hijos.add(componente+",2");
//				}
//				
//				//calcular actividades hijas prestamo
//				ArrayList<ArrayList<Integer>> actividadesPrestamo = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
//				
//				if(tempPrestamo.hijos == null){
//					tempPrestamo.hijos = new ArrayList<String>();
//				}
//				for(ArrayList<Integer> actividad: actividadesPrestamo){
//					if(actividad.get(1) == 0)
//						tempPrestamo.hijos.add(actividad.get(0)+",5");
//				}
//				
//				stcontroladquisiciones padre = null;
//				
//				int nivel = 0;
//				
//				lstprestamo.add(tempPrestamo);
//				
//				for(Integer componente:componentes){
//					tempPrestamo = new stcontroladquisiciones();
//					
//					Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
//					tempPrestamo.objetoId = objComponente.getId();
//					tempPrestamo.nombre = objComponente.getNombre();
//					tempPrestamo.objetoTipo = 2;
//					tempPrestamo.nivel = 2;
//					tempPrestamo.predecesorId = idPrestamo;
//					tempPrestamo.objetoPredecesorTipo = 1;
//					inicializarColumnasOcultas(tempPrestamo);
//					
//					if(idPlanAdquisiciones != null){
//						PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(2, objComponente.getId());
//						tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//						tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//						tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0: 0;
//						tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//						tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//						tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//						tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//						tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//						tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//						tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//						tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//						tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//						tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//						tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//						tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//						tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//						tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//						tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//						tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//						tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//						tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//					}
//										
//					ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
//					
//					tempPrestamo.hijos = new ArrayList<String>();
//					for(Integer producto: productos){
//						tempPrestamo.hijos.add(producto+",3");
//					}
//					
//					//actividades hijas componente
//					ArrayList<ArrayList<Integer>> actividadesComponente = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);
//					
//					if(tempPrestamo.hijos == null){
//						tempPrestamo.hijos = new ArrayList<String>();
//					}
//					for(ArrayList<Integer> actividad: actividadesComponente){
//						if(actividad.get(1) == 0)
//							tempPrestamo.hijos.add(actividad.get(0)+",5");
//					}
//					
//					lstprestamo.add(tempPrestamo);
//					for(Integer producto: productos){
//						tempPrestamo = new stcontroladquisiciones();
//						
//						Producto objProducto = ProductoDAO.getProductoPorId(producto);
//						tempPrestamo.objetoId = objProducto.getId();
//						tempPrestamo.nombre = objProducto.getNombre();
//						tempPrestamo.objetoTipo = 3;
//						tempPrestamo.nivel = 3;
//						tempPrestamo.predecesorId = objComponente.getId();
//						tempPrestamo.objetoPredecesorTipo = 2;
//						inicializarColumnasOcultas(tempPrestamo);
//						
//						if(idPlanAdquisiciones != null){
//							PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(3, objProducto.getId());
//							tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//							tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//							tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//							tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//							tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//							tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//							tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//							tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//							tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//							tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//							tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//							tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//							tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//							tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//							tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//							tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//							tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//							tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//							tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//							tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//							tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//						}
//						
//						ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
//						
//						tempPrestamo.hijos = new ArrayList<String>();
//						for(Integer subproducto: subproductos){
//							tempPrestamo.hijos.add(subproducto+",4");
//						}
//						
//						//actividades hijas de producto
//						ArrayList<ArrayList<Integer>> actividadesProducto = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
//						
//						if(tempPrestamo.hijos == null){
//							tempPrestamo.hijos = new ArrayList<String>();
//						}
//						for(ArrayList<Integer> actividad: actividadesProducto){
//							if(actividad.get(1) == 0)
//								tempPrestamo.hijos.add(actividad.get(0)+",5");
//						}
//						
//						lstprestamo.add(tempPrestamo);
//						
//						for(Integer subproducto: subproductos){
//							tempPrestamo = new stcontroladquisiciones();
//							
//							Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
//							tempPrestamo.objetoId = objSubProducto.getId();
//							tempPrestamo.nombre = objSubProducto.getNombre();
//							tempPrestamo.objetoTipo = 4;
//							tempPrestamo.nivel = 4;
//							tempPrestamo.predecesorId = objProducto.getId();
//							tempPrestamo.objetoPredecesorTipo = 3;
//							inicializarColumnasOcultas(tempPrestamo);
//							
//							if(idPlanAdquisiciones != null){
//								PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(4, objSubProducto.getId());
//								tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//								tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//								tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//								tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//								tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//								tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//								tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//								tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//								tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//								tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//								tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//								tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//								tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//								tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//								tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//								tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//								tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//								tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//								tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//								tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//								tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//							}
//							
//							lstprestamo.add(tempPrestamo);
//							
//							//actividades de sub producto
//							ArrayList<ArrayList<Integer>> actividadesSubProducto = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),objSubProducto.getId(), conn);
//							
//							tempPrestamo.hijos = new ArrayList<String>();
//							for(ArrayList<Integer> actividad: actividadesSubProducto){
//								if(actividad.get(1) == 0)
//									tempPrestamo.hijos.add(actividad.get(0)+",5");
//							}
//	
//							padre = null;
//							nivel = 0;
//	
//							for(ArrayList<Integer> actividad : actividadesSubProducto){
//								tempPrestamo = new stcontroladquisiciones();
//								
//								Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0));
//								tempPrestamo.objetoId = objActividad.getId();
//								tempPrestamo.nombre = objActividad.getNombre();
//								tempPrestamo.objetoTipo = 5;
//								tempPrestamo.nivel = 5 + actividad.get(1);
//								if(actividad.get(1)== 0){
//									tempPrestamo.predecesorId = objSubProducto.getId();
//									tempPrestamo.objetoPredecesorTipo = 4;
//								}else{
//									if(nivel != actividad.get(1)){
//										padre = lstprestamo.get(lstprestamo.size() - 1);
//									}
//									
//									if(padre.hijos == null)
//										padre.hijos = new ArrayList<String>();
//									
//									padre.hijos.add(actividad.get(0)+",5");	
//									
//									tempPrestamo.predecesorId = padre.objetoId;
//									tempPrestamo.objetoPredecesorTipo = 5;
//									
//									nivel = actividad.get(1);
//								}
//								inicializarColumnasOcultas(tempPrestamo);
//								
//								if(idPlanAdquisiciones != null){
//									PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
//									tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//									tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//									tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//									tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//									tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//									tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//									tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//									tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//									tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//									tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//									tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//									tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//									tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//									tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//									tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//									tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//									tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//									tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//									tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//									tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//									tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//								}
//								
//								lstprestamo.add(tempPrestamo);
//							}
//						}
//						
//						//actividades producto
//						//ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
//						
//						tempPrestamo.hijos = new ArrayList<String>();
//						for(ArrayList<Integer> actividad: actividadesProducto){
//							if(actividad.get(1) == 0)
//								tempPrestamo.hijos.add(actividad.get(0)+",5");
//						}
//	
//						padre = null;
//						nivel = 0;
//	
//						for(ArrayList<Integer> actividad : actividadesProducto){
//							tempPrestamo = new stcontroladquisiciones();
//							
//							Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0));
//							tempPrestamo.objetoId = objActividad.getId();
//							tempPrestamo.nombre = objActividad.getNombre();
//							tempPrestamo.objetoTipo = 5;
//							tempPrestamo.nivel = 4 + actividad.get(1);
//							if(actividad.get(1)==0){
//								tempPrestamo.predecesorId = objProducto.getId();
//								tempPrestamo.objetoPredecesorTipo = 3;
//							}else{
//								if(nivel != actividad.get(1)){
//									padre = lstprestamo.get(lstprestamo.size() - 1);
//								}
//								
//								if(padre.hijos == null)
//									padre.hijos = new ArrayList<String>();
//								
//								padre.hijos.add(actividad.get(0)+",5");	
//								
//								tempPrestamo.predecesorId = padre.objetoId;
//								tempPrestamo.objetoPredecesorTipo = 5;
//	
//								nivel = actividad.get(1);
//							}
//							inicializarColumnasOcultas(tempPrestamo);
//							
//							if(idPlanAdquisiciones != null){
//								PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
//								tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//								tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//								tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//								tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//								tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//								tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//								tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//								tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//								tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//								tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//								tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//								tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//								tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//								tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//								tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//								tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//								tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//								tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//								tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//								tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//								tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//							}
//							
//							lstprestamo.add(tempPrestamo);
//						}
//					}
//					
//					//actividades componente
//					//ArrayList<ArrayList<Integer>> actividadesComponente = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);
//					
//					padre = null;
//					nivel = 0;
//					for(ArrayList<Integer> actividad : actividadesComponente){
//						tempPrestamo = new stcontroladquisiciones();
//						
//						Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0));
//						tempPrestamo.objetoId = objActividad.getId();
//						tempPrestamo.nombre = objActividad.getNombre();
//						tempPrestamo.objetoTipo = 5;
//						tempPrestamo.nivel = 3 + actividad.get(1);
//						if(actividad.get(1) == 0){
//							tempPrestamo.predecesorId = objComponente.getId();
//							tempPrestamo.objetoPredecesorTipo = 2;
//						}else{
//							if(nivel != actividad.get(1)){
//								padre = lstprestamo.get(lstprestamo.size() - 1);
//							}
//							
//							if(padre.hijos == null)
//								padre.hijos = new ArrayList<String>();
//							
//							padre.hijos.add(actividad.get(0)+",5");	
//							
//							tempPrestamo.predecesorId = padre.objetoId;
//							//tempPrestamo.predecesorId = objActividad.getId();
//							tempPrestamo.objetoPredecesorTipo = 5;
//							
//							nivel = actividad.get(1);
//						}
//						inicializarColumnasOcultas(tempPrestamo);
//						
//						if(idPlanAdquisiciones != null){
//							PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
//							tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//							tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//							tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//							tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//							tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//							tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//							tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//							tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//							tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//							tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//							tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//							tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//							tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//							tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//							tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//							tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//							tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//							tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//							tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//							tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//							tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//						}
//						
//						lstprestamo.add(tempPrestamo);
//					}
//				}
//				
//				//actividades prestamo
//				actividadesPrestamo = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
//				
//				padre = null;
//				nivel = 0;
//				for(ArrayList<Integer> actividad : actividadesPrestamo){
//					tempPrestamo = new stcontroladquisiciones();
//					
//					Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0));
//					tempPrestamo.objetoId = objActividad.getId();
//					tempPrestamo.nombre = objActividad.getNombre();
//					tempPrestamo.objetoTipo = 5;
//					tempPrestamo.nivel = 2 + actividad.get(1);
//					if(actividad.get(1)==0){
//						tempPrestamo.predecesorId = idPrestamo;
//						tempPrestamo.objetoPredecesorTipo = 1;
//					}else{
//						if(nivel != actividad.get(1)){
//							padre = lstprestamo.get(lstprestamo.size() - 1);
//						}
//						
//						if(padre.hijos == null)
//							padre.hijos = new ArrayList<String>();
//						
//						padre.hijos.add(actividad.get(0)+",5");	
//						
//						tempPrestamo.predecesorId = padre.objetoId;
//						tempPrestamo.objetoPredecesorTipo = 5;
//						nivel = actividad.get(1);
//					}
//					inicializarColumnasOcultas(tempPrestamo);
//					
//					if(idPlanAdquisiciones != null){
//						PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
//						tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
//						tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
//						tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
//						tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
//						tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
//						tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
//						tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
//						tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
//						tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
//						tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
//						tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
//						tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
//						tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
//						tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
//						tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
//						tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
//						tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
//						tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
//						tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
//						tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
//						tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
//					}
//					
//					lstprestamo.add(tempPrestamo);
//				}	
//				conn.close();
//			}
//			return lstprestamo;
//		}catch(Exception e){
//			throw e;
//		}
//	}
	
	private byte[] exportarExcel(Integer idPlanAdquisiciones, Integer idPrestamo, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamo, usuario);
			excel = new CExcel("Plan Adquisiciones", false, null);
			wb=excel.generateExcelOfData(datos, "Plan Adquisiciones", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("3", SControlAdquisiciones.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Nombre", "Tipo de Adquisicion", "Unidad de Medida", "Categoria de Aquisicion", "Cantidad", "Costo", "Total", "Preparacion de Documentos", "", "Lanzamiento de Evento","", 
				"Recepcion y Evaluacion de Ofertas", "", "Adjudicacion", "", "Firma de Contrato", ""},  //titulos
			null, //mapeo
			{"string", "string", "string", "string", "double", "currency", "currency", "string", "string", "string", "string", "string", "string", "string", "string", "string", "string"}, //tipo dato
			null, //operaciones columnas
			null, //operaciones div
			{"", "", "", "", "", "", "", "Planificado", "Real", "Planificado","Real", 
				"Planificado", "Real", "Planificado", "Real", "Planificado", "Real"},
			null,
			null
			};				
			
		return headers;
	}
	
	public String[][] generarDatos(Integer idPrestamo, String usuario){
		String[][] datos = null;
		List<stcontroladquisiciones> lstprestamo;
		try {
			lstprestamo = generarPlan(idPrestamo, usuario);
			
			if (lstprestamo != null && !lstprestamo.isEmpty()){ 
				datos = new String[lstprestamo.size()][17];
				for (int i=0; i<lstprestamo.size(); i++){
						datos[i][0] = lstprestamo.get(i).nombre;
						datos[i][1] = lstprestamo.get(i).tipoAdquisicion.toString();
						datos[i][2] = lstprestamo.get(i).unidadMedida;
						String strCategoria = "";
						Integer categoria = lstprestamo.get(i).categoriaAdquisicion;
						if(categoria > 0){
							strCategoria = CategoriaAdquisicionDAO.getCategoriaPorId(categoria).getNombre();
						}
						datos[i][3] = strCategoria;
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
			CLogger.write("1", SControlAdquisiciones.class, e);
		}
		
return datos;
	}
}