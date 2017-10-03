package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.PlanAdquisicionDAO;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisicion")
public class SPlanAdquisicion extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public class stpago{
		String descripcion;
		String fechaPago;
		BigDecimal pago;
	}
	
	public class stadquisicion{
		String tipoNombre;
		Integer tipoId;
		String categoriaNombre;
		Integer categoriaId;
		String medidaNombre;
		Integer cantidad;
		BigDecimal precio;
		BigDecimal total;
		Integer nog;
		String numeroContrato;
		BigDecimal montoContrato;
		String preparacionDocumentoPlanificada;
		String preparacionDocumentoReal;
		String lanzamientoEventoPlanificada;
		String lanzamientoEventoReal;
		String recepcionOfertasPlanificada;
		String recepcionOfertasReal;
		String adjudicacionPlanificada;
		String adjudicacionReal;
		String firmaContratoPlanificada;
		String firmaContratoReal;
		stpago pagos[];
	}
		
    public SPlanAdquisicion() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			request.setCharacterEncoding("UTF-8");
			//HttpSession sesionweb = request.getSession();
			//String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
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
			
			if(accion.equals("generarPlan")){
				try{
//					List<stcomponenteplanadquisicion> lstprestamo = generarPlan(idPrestamo, usuario);
//										
//					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
//			        response_text = String.join("", "\"proyecto\":",response_text);
//			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}
				catch (Exception e){
					CLogger.write("2", SPlanAdquisicion.class, e);
				}
			}
			else if(accion.equals("getPlanAdquisicionPorObjeto")){
				Integer objetoId = Utils.String2Int(map.get("objetoId"));
				Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
				List<PlanAdquisicion> adquisiciones=PlanAdquisicionDAO.getPlanAdquisicionByObjeto(objetoTipo, objetoId);
				ArrayList<stadquisicion> adquisiciones_t=new ArrayList<stadquisicion>();
				if(adquisiciones!=null){
					for(PlanAdquisicion adq: adquisiciones){
						stadquisicion temp = new stadquisicion();
						temp.adjudicacionPlanificada = Utils.formatDate(adq.getAdjudicacionPlanificado());
						temp.adjudicacionReal = Utils.formatDate(adq.getAdjudicacionReal());
						temp.cantidad = adq.getCantidad();
						temp.categoriaId = adq.getCategoriaAdquisicion().getId();
						temp.categoriaNombre = adq.getCategoriaAdquisicion().getNombre();
						temp.firmaContratoPlanificada = Utils.formatDate(adq.getFirmaContratoPlanificado());
						temp.firmaContratoReal = Utils.formatDate(adq.getFirmaContratoReal());
						temp.lanzamientoEventoPlanificada = Utils.formatDate(adq.getLanzamientoEventoPlanificado());
						temp.lanzamientoEventoReal = Utils.formatDate(adq.getLanzamientoEventoReal());
						temp.medidaNombre = adq.getUnidadMedida();
						temp.montoContrato = adq.getMontoContrato();
						temp.nog = adq.getNog();
						temp.numeroContrato = adq.getNumeroContrato();
						temp.precio = adq.getPrecioUnitario();
						temp.preparacionDocumentoPlanificada = Utils.formatDate(adq.getPreparacionDocPlanificado());
						temp.preparacionDocumentoReal = Utils.formatDate(adq.getPreparacionDocReal());
						temp.recepcionOfertasPlanificada =  Utils.formatDate(adq.getRecepcionOfertasPlanificado());
						temp.recepcionOfertasReal = Utils.formatDate(adq.getRecepcionOfertasReal());
						temp.tipoId = adq.getTipoAdquisicion().getId();
						temp.tipoNombre = adq.getTipoAdquisicion().getNombre();
						temp.total = adq.getCantidad()!=null && adq.getPrecioUnitario()!=null ? adq.getPrecioUnitario().multiply(new BigDecimal(adq.getCantidad())) : adq.getTotal();
						if(adq.getPlanAdquisicionPagos()!=null && adq.getPlanAdquisicionPagos().size()>0){
							stpago[] pagos = new stpago[adq.getPlanAdquisicionPagos().size()];
							ArrayList<PlanAdquisicionPago> apagos = new ArrayList<PlanAdquisicionPago>(adq.getPlanAdquisicionPagos());
							for(int i=0; i<apagos.size();i++){
								pagos[i].descripcion = apagos.get(i).getDescripcion();
								pagos[i].fechaPago = Utils.formatDate(apagos.get(i).getFechaPago());
								pagos[i].pago = apagos.get(i).getPago();
							}
						}
						adquisiciones_t.add(temp);
					}
				}
				response_text = new GsonBuilder().serializeNulls().create().toJson(adquisiciones_t);
				response_text = String.join("", "\"adquisiciones\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}catch(Exception e){
			CLogger.write("1", SPlanAdquisicion.class, e);
		}
	}
	
	
}
