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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import dao.CategoriaAdquisicionDAO;
import dao.PlanAdquisicionDAO;
import dao.PlanAdquisicionPagoDAO;
import dao.TipoAdquisicionDAO;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisicion")
public class SPlanAdquisicion extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public class stpago{
		String fechaPago;
		BigDecimal pago;
	}
	
	public class stadquisicion{
		Integer id;
		String tipoNombre;
		Integer tipoId;
		String categoriaNombre;
		Integer categoriaId;
		String medidaNombre;
		Integer cantidad;
		BigDecimal precioUnitario;
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
			
			if(accion.equals("guardarAdquisiciones")){
				Integer objetoId = Utils.String2Int(map.get("objetoId"));
				Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
				String sadquisiciones = map.get("adquisiciones");
				JsonParser parser = new JsonParser();
				JsonArray adquisiciones = parser.parse(sadquisiciones).getAsJsonArray();
				ArrayList<Integer> ids=new ArrayList<Integer>();
				for(int i=0; i<adquisiciones.size(); i++){
					JsonObject objeto = adquisiciones.get(i).getAsJsonObject();
					Date adjudicacionPlanificado = objeto.has("adjudicacionPlanificado")==false || objeto.get("adjudicacionPlanificado").isJsonNull() ? null : Utils.stringToDate(objeto.get("adjudicacionPlanificado").getAsString());
					Date adjudicacionReal = objeto.has("adjudicacionReal")==false || objeto.get("adjudicacionReal").isJsonNull() ? null : Utils.stringToDate(objeto.get("adjudicacionReal").getAsString());
					Integer cantidad = objeto.has("cantidad")==false || objeto.get("cantidad").isJsonNull() ?  null : objeto.get("cantidad").getAsInt();
					Integer categoriaId = objeto.has("categoriaId")==false || objeto.get("categoriaId").isJsonNull() ? null : objeto.get("categoriaId").getAsInt();
					Date firmaContratoPlanificado = objeto.has("firmaContratoPlanificado")==false || objeto.get("firmaContratoPlanificado").isJsonNull() ? null : Utils.stringToDate(objeto.get("firmaContratoPlanificado").getAsString());
					Date firmaContratoReal = objeto.has("firmaContratoReal")==false || objeto.get("firmaContratoReal").isJsonNull() ? null : Utils.stringToDate(objeto.get("firmaContratoReal").getAsString());
					Integer id = objeto.has("id")==false || objeto.get("id").isJsonNull() ?  null : objeto.get("id").getAsInt();
					Date lanzamientoEventoPlanificado = objeto.has("lanzamientoEventoPlanificado")==false || objeto.get("lanzamientoEventoPlanificado").isJsonNull() ? null : Utils.stringToDate(objeto.get("lanzamientoEventoPlanificado").getAsString());
					Date lanzamientoEventoReal = objeto.has("lanzamientoEventoReal")==false || objeto.get("lanzamientoEventoReal").isJsonNull() ? null : Utils.stringToDate(objeto.get("lanzamientoEventoReal").getAsString());
					BigDecimal montoContrato = objeto.has("montoContrato")==false || objeto.get("montoContrato").isJsonNull() ? null : objeto.get("montoContrato").getAsBigDecimal();
					Integer nog = objeto.has("nog")==false || objeto.get("nog").isJsonNull() ? null : objeto.get("nog").getAsInt();
					String numeroContrato =  objeto.has("numeroContrato")==false || objeto.get("numeroContrato").isJsonNull() ?  null :  objeto.get("numeroContrato").getAsString();
					BigDecimal precioUnitario =  objeto.has("precioUnitario")==false || objeto.get("precioUnitario").isJsonNull() ? null : objeto.get("precioUnitario").getAsBigDecimal();
					Date preparacionDocPlanificado = objeto.has("preparacionDocumentoPlanificado")==false || objeto.get("preparacionDocumentoPlanificado").isJsonNull() ? null : Utils.stringToDate(objeto.get("preparacionDocumentoPlanificado").getAsString());
					Date preparacionDocReal = objeto.has("preparacionDocumentoReal")==false || objeto.get("preparacionDocumentoReal").isJsonNull() ? null : Utils.stringToDate(objeto.get("preparacionDocumentoReal").getAsString());
					Date recepcionOfertasPlanificado = objeto.has("recepcionOfertasPlanificado")==false || objeto.get("recepcionOfertasPlanificado").isJsonNull() ? null : Utils.stringToDate(objeto.get("recepcionOfertasPlanificado").getAsString());
					Date recepcionOfertasReal = objeto.has("recepcionOfertasReal")==false || objeto.get("recepcionOfertasReal").isJsonNull() ? null : Utils.stringToDate(objeto.get("recepcionOfertasReal").getAsString());
					Integer tipoId = objeto.has("tipoId")==false || objeto.get("tipoId").isJsonNull() ? null : objeto.get("tipoId").getAsInt();
					BigDecimal total = objeto.has("total")==false || objeto.get("total").isJsonNull() ? null : objeto.get("total").getAsBigDecimal();
					String unidadMedida =  objeto.has("medidaNombre")==false || objeto.has("medidaNombre")==false || objeto.get("medidaNombre").isJsonNull() ?  null :  objeto.get("medidaNombre").getAsString();
					PlanAdquisicion pa;
					if(id==null || id == -1)
						pa = new PlanAdquisicion(CategoriaAdquisicionDAO.getCategoriaPorId(categoriaId), 
							TipoAdquisicionDAO.getTipoAdquisicionPorId(tipoId), unidadMedida, cantidad, total, precioUnitario, 
							preparacionDocPlanificado, preparacionDocReal, lanzamientoEventoPlanificado, lanzamientoEventoReal, 
							recepcionOfertasPlanificado, recepcionOfertasReal, adjudicacionPlanificado, adjudicacionReal, 
							firmaContratoPlanificado, firmaContratoReal, objetoId, objetoTipo,usuario, null, 
							new DateTime().toDate(), null, 1, 0, numeroContrato, montoContrato, nog, null);
					else{
						pa = PlanAdquisicionDAO.getPlanAdquisicionById(id);
						pa.setCategoriaAdquisicion(CategoriaAdquisicionDAO.getCategoriaPorId(categoriaId));
						pa.setTipoAdquisicion(TipoAdquisicionDAO.getTipoAdquisicionPorId(tipoId));
						pa.setUnidadMedida(unidadMedida);
						pa.setCantidad(cantidad);
						pa.setTotal(total);
						pa.setPrecioUnitario(precioUnitario);
						pa.setPreparacionDocPlanificado(preparacionDocPlanificado);
						pa.setPreparacionDocReal(preparacionDocReal);
						pa.setLanzamientoEventoPlanificado(lanzamientoEventoPlanificado);
						pa.setLanzamientoEventoReal(lanzamientoEventoReal);
						pa.setRecepcionOfertasPlanificado(recepcionOfertasPlanificado);
						pa.setRecepcionOfertasReal(recepcionOfertasReal);
						pa.setAdjudicacionPlanificado(adjudicacionPlanificado);
						pa.setAdjudicacionReal(adjudicacionReal);
						pa.setFirmaContratoPlanificado(firmaContratoPlanificado);
						pa.setFirmaContratoReal(firmaContratoReal);
						pa.setObjetoId(objetoId);
						pa.setObjetoTipo(objetoTipo);
						pa.setUsuarioActualizo(usuario);
						pa.setFechaActualizacion(new DateTime().toDate());
						pa.setEstado(1);
						pa.setBloqueado(0);
						pa.setNumeroContrato(numeroContrato);
						pa.setMontoContrato(montoContrato);
						pa.setNog(nog);
						PlanAdquisicionPagoDAO.eliminarPagos(new ArrayList<PlanAdquisicionPago>(pa.getPlanAdquisicionPagos()));
					}
					PlanAdquisicionDAO.guardarPlanAdquisicion(pa);
					ids.add(pa.getId());
					JsonArray pagos = objeto.get("pagos").getAsJsonArray();
					for(int j=0; j<pagos.size(); j++){
						JsonObject objeto_pago = pagos.get(j).getAsJsonObject();
						Date fechaPago = objeto_pago.get("fecha").isJsonNull() ? null : Utils.stringToDate(objeto_pago.get("fecha").getAsString());
						BigDecimal dpago = objeto_pago.get("pago").isJsonNull() ? null : objeto_pago.get("pago").getAsBigDecimal();
						PlanAdquisicionPago pago = new PlanAdquisicionPago(pa, fechaPago, dpago, "", usuario, null, new DateTime().toDate(), null, 1);
						PlanAdquisicionPagoDAO.guardarPago(pago);
					}
				}
				List<PlanAdquisicion> aborrar=PlanAdquisicionDAO.getAdquisicionesNotIn(objetoId,objetoTipo,ids);
				for(PlanAdquisicion borrar:aborrar){
					PlanAdquisicionDAO.borrarPlan(borrar);
				}
				String sids="";
				for(int k=0; k<ids.size(); k++)
					sids = sids + "," +ids.get(k);
				response_text = String.join("","{ \"success\": true, \"ids\":\"",(sids.length()>0 ? sids.substring(1, sids.length()-1) : ""),"\" }");
			}
			else if(accion.equals("getPlanAdquisicionPorObjeto")){
				Integer objetoId = Utils.String2Int(map.get("objetoId"));
				Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
				List<PlanAdquisicion> adquisiciones=PlanAdquisicionDAO.getPlanAdquisicionByObjeto(objetoTipo, objetoId);
				ArrayList<stadquisicion> adquisiciones_t=new ArrayList<stadquisicion>();
				if(adquisiciones!=null){
					for(PlanAdquisicion adq: adquisiciones){
						stadquisicion temp = new stadquisicion();
						temp.id = adq.getId();
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
						temp.precioUnitario = adq.getPrecioUnitario();
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
								pagos[i] = new stpago();
								pagos[i].fechaPago = Utils.formatDate(apagos.get(i).getFechaPago());
								pagos[i].pago = apagos.get(i).getPago();
							}
							temp.pagos = pagos;
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
