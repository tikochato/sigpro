package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
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
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.PlanAdquisicionesDAO;
import dao.PlanAdquisicionesDetalleDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.CategoriaAdquisicion;
import pojo.Componente;
import pojo.PlanAdquisiciones;
import pojo.PlanAdquisicionesDetalle;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stplanadquisiciones{
		Integer objetoId;
		Integer objetoTipo;
		Integer predecesorId;
		Integer objetoPredecesorTipo;
		Integer idPlanAdquisiciones;
		String nombre;
		Integer nivel;
		Integer $$treeLevel;
		Integer tipoAdquisicion;
		Integer categoriaAdquisicion;
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
		boolean c0;
		boolean c1;
		boolean c2;
		boolean c3;
		boolean c4;
		boolean c5;
		boolean c6;
		boolean c7;
		boolean c8;
		boolean c9;
		boolean c10;
		boolean c11;
		boolean c12;
		boolean c13;
		boolean c14;
		boolean c15;
		boolean bloqueado;
		boolean contieneDatos;
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
		HashMap<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
		
		if (accion.equals("generarPlan")){
			List<stplanadquisiciones> lstprestamo = new ArrayList<stplanadquisiciones>();
			stplanadquisiciones tempPrestamo = new stplanadquisiciones();
			Integer idPlanAdquisiciones = Utils.String2Int(map.get("idPlanAdquisiciones"), null);
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
			
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(idPrestamo, 1);
			String fechaSuscripcion = Utils.formatDate(prestamo.getFechaSuscripcion());
			String fechaCierre = Utils.formatDate(prestamo.getFechaCierreOrigianlUe());
			
			PlanAdquisiciones planAdquisicion = PlanAdquisicionesDAO.getPlanAdquisicionByObjeto(1, proyecto.getId());
			idPlanAdquisiciones = planAdquisicion != null ? planAdquisicion.getId() : null;
			
			tempPrestamo.objetoId = proyecto.getId();
			tempPrestamo.nombre = proyecto.getNombre();
			tempPrestamo.objetoTipo = 1;
			tempPrestamo.nivel = 1;
			tempPrestamo.predecesorId = 0;
			tempPrestamo.objetoPredecesorTipo = 0;
			inicializarColumnasOcultas(tempPrestamo);
			
			if(idPlanAdquisiciones == null){
				planAdquisicion = new PlanAdquisiciones();
				planAdquisicion.setObjetoId(proyecto.getId());
				planAdquisicion.setObjetoTipo(1);
				planAdquisicion.setUsuarioCreo(usuario);
				planAdquisicion.setFechaCreacion(new DateTime().toDate());
				planAdquisicion.setEstado(1);
				PlanAdquisicionesDAO.guardarPlanAdquisicion(planAdquisicion);
				idPlanAdquisiciones = planAdquisicion.getId();
			}
			
			if(idPlanAdquisiciones != null){
				PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(1, proyecto.getId());
					tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
					tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
					tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
					tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
					tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
					tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
					tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
					tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
					tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
					tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
					tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
					tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
					tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
					tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
					tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
					tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
					tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
					tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
					tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
					tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
					tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
			}
			
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
				
				stplanadquisiciones padre = null;
				
				int nivel = 0;
				
				lstprestamo.add(tempPrestamo);
				
				for(Integer componente:componentes){
					tempPrestamo = new stplanadquisiciones();
					
					Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
					tempPrestamo.objetoId = objComponente.getId();
					tempPrestamo.nombre = objComponente.getNombre();
					tempPrestamo.objetoTipo = 2;
					tempPrestamo.nivel = 2;
					tempPrestamo.predecesorId = idPrestamo;
					tempPrestamo.objetoPredecesorTipo = 1;
					inicializarColumnasOcultas(tempPrestamo);
					
					if(idPlanAdquisiciones != null){
						PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(2, objComponente.getId());
						tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
						tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
						tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0: 0;
						tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
						tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
						tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
						tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
						tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
						tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
						tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
						tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
						tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
						tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
						tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
						tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
						tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
						tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
						tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
						tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
						tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
						tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
					}
										
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
					
					lstprestamo.add(tempPrestamo);
					for(Integer producto: productos){
						tempPrestamo = new stplanadquisiciones();
						
						Producto objProducto = ProductoDAO.getProductoPorId(producto);
						tempPrestamo.objetoId = objProducto.getId();
						tempPrestamo.nombre = objProducto.getNombre();
						tempPrestamo.objetoTipo = 3;
						tempPrestamo.nivel = 3;
						tempPrestamo.predecesorId = objComponente.getId();
						tempPrestamo.objetoPredecesorTipo = 2;
						inicializarColumnasOcultas(tempPrestamo);
						
						if(idPlanAdquisiciones != null){
							PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(3, objProducto.getId());
							tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
							tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
							tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
							tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
							tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
							tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
							tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
							tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
							tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
							tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
							tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
							tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
							tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
							tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
							tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
							tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
							tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
							tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
							tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
							tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
							tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
						}
						
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
						
						lstprestamo.add(tempPrestamo);
						
						for(Integer subproducto: subproductos){
							tempPrestamo = new stplanadquisiciones();
							
							Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
							tempPrestamo.objetoId = objSubProducto.getId();
							tempPrestamo.nombre = objSubProducto.getNombre();
							tempPrestamo.objetoTipo = 4;
							tempPrestamo.nivel = 4;
							tempPrestamo.predecesorId = objProducto.getId();
							tempPrestamo.objetoPredecesorTipo = 3;
							inicializarColumnasOcultas(tempPrestamo);
							
							if(idPlanAdquisiciones != null){
								PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(4, objSubProducto.getId());
								tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
								tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
								tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
								tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
								tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
								tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
								tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
								tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
								tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
								tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
								tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
								tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
								tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
								tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
								tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
								tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
								tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
								tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
								tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
								tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
								tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
							}
							
							lstprestamo.add(tempPrestamo);
							
							//actividades de sub producto
							ArrayList<ArrayList<Integer>> actividadesSubProducto = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),objSubProducto.getId(), conn);
							
							tempPrestamo.hijos = new ArrayList<String>();
							for(ArrayList<Integer> actividad: actividadesSubProducto){
								if(actividad.get(1) == 0)
									tempPrestamo.hijos.add(actividad.get(0)+",5");
							}

							padre = null;
							nivel = 0;

							for(ArrayList<Integer> actividad : actividadesSubProducto){
								tempPrestamo = new stplanadquisiciones();
								
								Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
								tempPrestamo.objetoId = objActividad.getId();
								tempPrestamo.nombre = objActividad.getNombre();
								tempPrestamo.objetoTipo = 5;
								tempPrestamo.nivel = 5 + actividad.get(1);
								if(actividad.get(1)== 0){
									tempPrestamo.predecesorId = objSubProducto.getId();
									tempPrestamo.objetoPredecesorTipo = 4;
								}else{
									if(nivel != actividad.get(1)){
										padre = lstprestamo.get(lstprestamo.size() - 1);
									}
									
									if(padre.hijos == null)
										padre.hijos = new ArrayList<String>();
									
									padre.hijos.add(actividad.get(0)+",5");	
									
									tempPrestamo.predecesorId = padre.objetoId;
									tempPrestamo.objetoPredecesorTipo = 5;
									
									nivel = actividad.get(1);
								}
								inicializarColumnasOcultas(tempPrestamo);
								
								if(idPlanAdquisiciones != null){
									PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
									tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
									tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
									tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
									tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
									tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
									tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
									tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
									tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
									tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
									tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
									tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
									tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
									tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
									tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
									tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
									tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
									tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
									tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
									tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
									tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
									tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
								}
								
								lstprestamo.add(tempPrestamo);
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
							tempPrestamo = new stplanadquisiciones();
							
							Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
							tempPrestamo.objetoId = objActividad.getId();
							tempPrestamo.nombre = objActividad.getNombre();
							tempPrestamo.objetoTipo = 5;
							tempPrestamo.nivel = 4 + actividad.get(1);
							if(actividad.get(1)==0){
								tempPrestamo.predecesorId = objProducto.getId();
								tempPrestamo.objetoPredecesorTipo = 3;
							}else{
								if(nivel != actividad.get(1)){
									padre = lstprestamo.get(lstprestamo.size() - 1);
								}
								
								if(padre.hijos == null)
									padre.hijos = new ArrayList<String>();
								
								padre.hijos.add(actividad.get(0)+",5");	
								
								tempPrestamo.predecesorId = padre.objetoId;
								tempPrestamo.objetoPredecesorTipo = 5;

								nivel = actividad.get(1);
							}
							inicializarColumnasOcultas(tempPrestamo);
							
							if(idPlanAdquisiciones != null){
								PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
								tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
								tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
								tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
								tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
								tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
								tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
								tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
								tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
								tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
								tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
								tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
								tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
								tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
								tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
								tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
								tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
								tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
								tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
								tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
								tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
								tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
							}
							
							lstprestamo.add(tempPrestamo);
						}
					}
					
					//actividades componente
					//ArrayList<ArrayList<Integer>> actividadesComponente = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);
					
					padre = null;
					nivel = 0;
					for(ArrayList<Integer> actividad : actividadesComponente){
						tempPrestamo = new stplanadquisiciones();
						
						Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
						tempPrestamo.objetoId = objActividad.getId();
						tempPrestamo.nombre = objActividad.getNombre();
						tempPrestamo.objetoTipo = 5;
						tempPrestamo.nivel = 3 + actividad.get(1);
						if(actividad.get(1) == 0){
							tempPrestamo.predecesorId = objComponente.getId();
							tempPrestamo.objetoPredecesorTipo = 2;
						}else{
							if(nivel != actividad.get(1)){
								padre = lstprestamo.get(lstprestamo.size() - 1);
							}
							
							if(padre.hijos == null)
								padre.hijos = new ArrayList<String>();
							
							padre.hijos.add(actividad.get(0)+",5");	
							
							tempPrestamo.predecesorId = padre.objetoId;
							//tempPrestamo.predecesorId = objActividad.getId();
							tempPrestamo.objetoPredecesorTipo = 5;
							
							nivel = actividad.get(1);
						}
						inicializarColumnasOcultas(tempPrestamo);
						
						if(idPlanAdquisiciones != null){
							PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
							tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
							tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
							tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
							tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
							tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
							tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
							tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
							tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
							tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
							tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
							tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
							tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
							tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
							tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
							tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
							tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
							tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
							tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
							tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
							tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
							tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
						}
						
						lstprestamo.add(tempPrestamo);
					}
				}
				
				//actividades prestamo
				actividadesPrestamo = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
				
				padre = null;
				nivel = 0;
				for(ArrayList<Integer> actividad : actividadesPrestamo){
					tempPrestamo = new stplanadquisiciones();
					
					Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
					tempPrestamo.objetoId = objActividad.getId();
					tempPrestamo.nombre = objActividad.getNombre();
					tempPrestamo.objetoTipo = 5;
					tempPrestamo.nivel = 2 + actividad.get(1);
					if(actividad.get(1)==0){
						tempPrestamo.predecesorId = idPrestamo;
						tempPrestamo.objetoPredecesorTipo = 1;
					}else{
						if(nivel != actividad.get(1)){
							padre = lstprestamo.get(lstprestamo.size() - 1);
						}
						
						if(padre.hijos == null)
							padre.hijos = new ArrayList<String>();
						
						padre.hijos.add(actividad.get(0)+",5");	
						
						tempPrestamo.predecesorId = padre.objetoId;
						tempPrestamo.objetoPredecesorTipo = 5;
						nivel = actividad.get(1);
					}
					inicializarColumnasOcultas(tempPrestamo);
					
					if(idPlanAdquisiciones != null){
						PlanAdquisicionesDetalle detallePlan = PlanAdquisicionesDetalleDAO.getPlanAdquisicionByObjeto(5, objActividad.getId());
						tempPrestamo.idPlanAdquisiciones = idPlanAdquisiciones;
						tempPrestamo.tipoAdquisicion = detallePlan != null ? detallePlan.getTipoAdquisicion() != null ? detallePlan.getTipoAdquisicion() : 0 : 0;
						tempPrestamo.categoriaAdquisicion = detallePlan != null ? detallePlan.getCategoriaAdquisicion() != null ? detallePlan.getCategoriaAdquisicion().getId() : 0 : 0;
						tempPrestamo.unidadMedida = detallePlan != null ? detallePlan.getUnidadMedida() : "";
						tempPrestamo.cantidad = detallePlan != null ? detallePlan.getCantidad() : 0;
						tempPrestamo.costo = detallePlan != null ? detallePlan.getPrecioUnitario() : new BigDecimal(0);
						tempPrestamo.total = detallePlan != null ? detallePlan.getTotal() : new BigDecimal(0);
						tempPrestamo.planificadoDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocPlanificado()) : null;
						tempPrestamo.realDocs = detallePlan != null ? Utils.formatDate(detallePlan.getPreparacionDocReal()) : null;
						tempPrestamo.planificadoLanzamiento = detallePlan != null  ? Utils.formatDate(detallePlan.getLanzamientoEventoPlanificado()) : null;
						tempPrestamo.realLanzamiento = detallePlan != null ? Utils.formatDate(detallePlan.getLanzamientoEventoReal()) : null;
						tempPrestamo.planificadoRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasPlanificado()) : null;
						tempPrestamo.realRecepcionEval = detallePlan != null ? Utils.formatDate(detallePlan.getRecepcionOfertasReal()) : null;
						tempPrestamo.planificadoAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionPlanificado()) : null;
						tempPrestamo.realAdjudica = detallePlan != null ? Utils.formatDate(detallePlan.getAdjudicacionReal()) : null;
						tempPrestamo.planificadoFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoPlanificado()) : null;
						tempPrestamo.realFirma = detallePlan != null ? Utils.formatDate(detallePlan.getFirmaContratoReal()) : null;
						tempPrestamo.bloqueado = detallePlan != null ? detallePlan.getBloqueado() == 1 ? true : false : false;
						tempPrestamo.numeroContrato = detallePlan != null ? detallePlan.getNumeroContrato() : null;
						tempPrestamo.montoContrato = detallePlan != null ? detallePlan.getMontoContrato() : new BigDecimal(0);
						tempPrestamo.nog = detallePlan != null ? detallePlan.getNog() : null;
					}
					
					lstprestamo.add(tempPrestamo);
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
		        response_text = String.join("", "\"proyecto\":",response_text);
		        response_text = String.join("", "{\"success\":true,\"fechaSuscripcion\": \""+ fechaSuscripcion + "\", \"fechaCierre\": \"" + fechaCierre + "\",", response_text, "}");
			}
		}else if(accion.equals("guardarPlan")){
			try{
				boolean result = false;
				String data = map.get("data");
				String[] datos = data.split("°");
				
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
					
					Integer cantidad = Utils.String2Int(row[6]);
					BigDecimal costo = new BigDecimal(row[7]);
					BigDecimal total = new BigDecimal(row[8]);

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
						Actividad actividad = ActividadDAO.getActividadPorId(objetoId, usuario);
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
					BigDecimal montoContrato = row[22] != null ? new BigDecimal(row[22]) : new BigDecimal(0);
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
						plan = new PlanAdquisicionesDetalle(categoriaAdquisicion,planAdquisiciones, tipoAdquisicion,unidadMedida,cantidad, total, nog, costo, 
								planificadoDocs, realDocs, planificadoLanzamiento, realLanzamiento, planificadoRecepcionEval, realRecepcionEval, 
								planificadoAdjudica, realAdjudica, planificadoFirma, realFirma, numeroContrato, montoContrato, objetoId, objetoTipo, usuario, null,new DateTime().toDate(), null,1,
								bloqueado);
						
						//planificadoAdjudica, realAdjudica, planificadoFirma, realFirma, numeroContrato, objetoId, objetoTipo, usuario, null,new DateTime().toDate(), null,1,
						//bloqueado,nog, montoContrato);
					}
					
					result = PlanAdquisicionesDetalleDAO.guardarPlanAdquisicion(plan);
					
					if(!result)
						break;
				}
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"), "}");
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}else if(accion.equals("exportarExcel")){
			String data = map.get("data");
			String nombreInforme = "Plan de adquisiciones Aï¿½O FISCAL " + Year.now().getValue();
			
			Map<String,Object[]> reporte = new HashMap<>();
			Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
			List<Map<String, String>> datos = gson.fromJson(data, listType);
			
			String[] encabezadosCombinados = new String[5];
			encabezadosCombinados[0] = "Preparaciï¿½n de Documentos,2";
			encabezadosCombinados[1] = "Lanzamiento de evento,2";
			encabezadosCombinados[2] = "Recepciï¿½n y evaluaciï¿½n de ofertas,2";
			encabezadosCombinados[3] = "Adjudicaciï¿½n,2";
			encabezadosCombinados[4] = "Firma de contrato,2";
			
			reporte.put("0", new Object[] {"Componente", "Mï¿½todo", "Planificado", "Real", "Planificado", "Real", "Planificado", "Real", "Planificado", "Real", "Planificado", "Real"});
			
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
	
	private static void inicializarColumnasOcultas(stplanadquisiciones tempPrestamo){
		tempPrestamo.c0 = false;
		tempPrestamo.c1 = false;
		tempPrestamo.c2 = false;
		tempPrestamo.c3 = false;
		tempPrestamo.c4 = false;
		tempPrestamo.c5 = false;
		tempPrestamo.c6 = false;
		tempPrestamo.c7 = false;
		tempPrestamo.c8 = false;
		tempPrestamo.c9 = false;
		tempPrestamo.c10 = false;
		tempPrestamo.c11 = false;
		tempPrestamo.c12 = false;
		tempPrestamo.c13 = false;
		tempPrestamo.c14 = false;
		tempPrestamo.c15 = false;
		tempPrestamo.bloqueado = false;
	}
}
