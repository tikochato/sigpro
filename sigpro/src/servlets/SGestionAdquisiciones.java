package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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
import org.joda.time.DateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.CategoriaAdquisicionDAO;
import dao.EstructuraProyectoDAO;
import dao.ObjetoCosto;
import dao.ObjetoDAO;
import dao.PlanAdquisicionDAO;
import pojo.CategoriaAdquisicion;
import pojo.PlanAdquisicion;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SGestionAdquisiciones")
public class SGestionAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stpresupuestoGestion{
		BigDecimal planificado;
	}
	
	class stgestion{
		stpresupuestoGestion[] mes = new stpresupuestoGestion[12];
		Integer anio;
	}
	
	class sttotal{
		stpresupuestoGestion[] total = new stpresupuestoGestion[1];
		Integer anio;
	}
	
	class stcomponentegestionadquisicion{
		Integer objetoId;
		String nombre;
		Integer nivel;
		Integer objetoTipo;
		Integer planadquisicionId;
		Integer acumulacionCosto;
		Integer objetoTipoPadre;
		Integer objetoIdPadre;
		DateTime fechaIncial;
		DateTime fechaFinal;
		BigDecimal costo;
		Object anioPlan;
		sttotal[] anioTotalPlan;
		BigDecimal total;
		Integer cantidadProcesos;
	}
	
	class stcategoriaG{
		Integer categoriaId;
		String nombre;
		Integer adquisicionId;
		ArrayList<ObjetoCosto> objCosto = new ArrayList<ObjetoCosto>();		
	}
	
    public SGestionAdquisiciones() {
        super();
    }

    final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;
    
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
		Integer fechaInicio = Utils.String2Int(map.get("fechaInicio"));
		Integer fechaFin = Utils.String2Int(map.get("fechaFin"));
		
		if(accion.equals("generarGestion")){
			try {
				List<stcomponentegestionadquisicion> lstprestamo = generarPlan(idPrestamo, usuario, fechaInicio, fechaFin);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
		        response_text = String.join("", "\"proyecto\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			} catch (Exception e) {
				CLogger.write("1", SPlanAdquisiciones.class, e);
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
	
	private List<stcomponentegestionadquisicion> generarPlan(Integer idPrestamo, String usuario, Integer fechaInicial, Integer fechaFinal) throws Exception{
		try{
			List<stcomponentegestionadquisicion> lstPrestamo = new ArrayList<>();
			List<ObjetoCosto> estructuraProyecto = ObjetoDAO.getEstructuraConCosto(idPrestamo, fechaInicial, fechaFinal, true, false, usuario);
			stcomponentegestionadquisicion temp = null;
			
			List<CategoriaAdquisicion> lstCategorias = CategoriaAdquisicionDAO.getCategoriaAdquisicion(); 
			List<stcategoriaG> lsttempCategorias = new ArrayList<stcategoriaG>();
			stcategoriaG tempCategoria = null;
			for(CategoriaAdquisicion categoria : lstCategorias){
				tempCategoria = new stcategoriaG();
				tempCategoria.categoriaId = categoria.getId();
				tempCategoria.nombre = categoria.getNombre();
				lsttempCategorias.add(tempCategoria);
			}
			
			for(ObjetoCosto objeto: estructuraProyecto){
				temp = new stcomponentegestionadquisicion();
				Integer objetoTipo = objeto.getObjeto_tipo();
				
				for(stcategoriaG cat : lsttempCategorias){
					cat.objCosto = new ArrayList<ObjetoCosto>();
				}
				
				if(objetoTipo == 1){
					List<ObjetoCosto> hijos = EstructuraProyectoDAO.getHijosCompleto(objeto.getTreePath(), estructuraProyecto);
					
					for(ObjetoCosto objetoHijo : hijos){
						PlanAdquisicion lstplan = PlanAdquisicionDAO.getPlanAdquisicionByObjeto(objetoHijo.getObjeto_tipo(), objetoHijo.getObjeto_id());
						
						if(lstplan != null){
							for(stcategoriaG cat : lsttempCategorias){
								if(cat.categoriaId == lstplan.getCategoriaAdquisicion().getId()){
									cat.adquisicionId = lstplan.getId();
									cat.objCosto.add(objetoHijo);
								}
							}
						}
					}
					
					for(stcategoriaG cat : lsttempCategorias){
						if(!cat.objCosto.isEmpty()){
							temp = new stcomponentegestionadquisicion();
							temp.nombre = cat.nombre;
							temp.nivel = 1;
							
							for(ObjetoCosto objCat2 : cat.objCosto){
								Date fechaInicio = objCat2.getFecha_inicial().toDate();
								Date fechaFin = objCat2.getFecha_final().toDate();
								Integer acumulacionCosto = objCat2.getAcumulacion_costoid();
								BigDecimal costo = objCat2.getCosto();
								
								temp.objetoId = objCat2.getObjeto_id();
								temp.objetoTipo = objCat2.getObjeto_tipo();
								temp.nivel = 2;
								temp.anioPlan = objCat2.getAnios();
								temp.costo = costo;
								temp.acumulacionCosto = acumulacionCosto;
								temp.fechaIncial = new DateTime(fechaInicio);
								temp.fechaFinal = new DateTime(fechaFin);
								temp.planadquisicionId = cat.adquisicionId;
								
								temp.fechaIncial = null;
								temp.fechaFinal = null;								
							}
							
							lstPrestamo.add(temp);
						}
					}
				}
			}
			return lstPrestamo;
		} catch(Exception e){
			CLogger.write("1", SGestionAdquisiciones.class, e);
			return null;
		}
	}

}
