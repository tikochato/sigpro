package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.PagoDAO;
import pojo.Pago;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPago")
public class SPago extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stPago{
		int id;
		String fecha;
		String fechaReal;
		BigDecimal pago;
		String descripcion;
	}
       
    public SPago() {
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
		if(accion.equals("getPagos")){
			Integer idObjeto = Utils.String2Int(map.get("idObjeto"));
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
			List<Pago> Pagos = PagoDAO.getPagosByObjetoTipo(idObjeto, objetoTipo);
			
			List<stPago> resultado = new ArrayList<stPago>();
			for(Pago pago :Pagos){
				stPago temp = new stPago();
				temp.id = pago.getId();
				temp.fecha = "";
				temp.fechaReal = Utils.formatDate(pago.getFechaPago());
				temp.pago = pago.getPago();
				temp.descripcion = pago.getDescripcion();
				resultado.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultado);
	        response_text = String.join("", "\"pagos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("guardarPagos")){
			Integer idObjeto = Utils.String2Int(map.get("idObjeto"));
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
			
			String dataPagos = map.get("pagos");
			
			Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
			List<Map<String, String>> pagos = gson.fromJson(dataPagos, listType);
			
			Pago nuevoPago = null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			try{
				for(Map<String, String> pago : pagos){
					if(Utils.String2Int(pago.get("id")) == 0){
						nuevoPago = new Pago(idObjeto, objetoTipo, formatter.parse(pago.get("fechaReal")), new BigDecimal(pago.get("pago")), pago.get("descripcion"), usuario, null, new Date(), null,1);
						PagoDAO.guardarPago(nuevoPago);
					}
				}
			}catch(Throwable e){
				CLogger.write("1", SPago.class, e);
			}
			
			List<Pago> Pagos = PagoDAO.getPagosByObjetoTipo(idObjeto,objetoTipo);
			
			List<stPago> resultado = new ArrayList<stPago>();
			for(Pago pago :Pagos){
				stPago temp = new stPago();
				temp.id = pago.getId();
				temp.fecha = Utils.formatDate(pago.getFechaPago());
				temp.pago = pago.getPago();
				temp.descripcion = pago.getDescripcion();
				resultado.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultado);
	        response_text = String.join("", "\"pagos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if (accion.equals("eliminarPago")){
			Integer idPago = Utils.String2Int(map.get("idPago"));
			
			Pago pago = PagoDAO.getPagobyId(idPago);
			boolean eliminado = PagoDAO.eliminarPago(pago);
			
			if(eliminado)
				response_text = String.join("", "{\"success\":true}");
			else
				response_text = String.join("", "{\"success\":false}");
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
