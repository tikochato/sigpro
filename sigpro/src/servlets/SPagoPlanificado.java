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

import dao.PagoPlanificadoDAO;
import pojo.PagoPlanificado;
import utilities.Utils;


@WebServlet("/SPagoPlanificado")
public class SPagoPlanificado extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stpago{
		int id;
		String fechaPago;
		BigDecimal pago;
		int estado;
	}
    
    public SPagoPlanificado() {
        super();
    
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		};
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getPagos")){
			Integer objetoId = Utils.String2Int(map.get("objetoId"));
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"));
			List<PagoPlanificado> pagos = PagoPlanificadoDAO.getPagosPlanificadosPorObjeto(objetoId, objetoTipo);
			List<stpago> stpagos = new ArrayList<stpago>();
			for (PagoPlanificado pago : pagos){
				stpago temp = new stpago();
				temp.id = pago.getId();
				temp.fechaPago = Utils.formatDate(pago.getFechaPago());
				temp.pago = pago.getPago();
				temp.estado = pago.getEstado();
				stpagos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stpagos);
	        response_text = String.join("", "\"pagos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
			
		}
		else if(accion.equals("borrarPago")){
			int idPago = Utils.String2Int(map.get("idPago"));
			if (idPago>0){
				PagoPlanificado pagoPlanificado = PagoPlanificadoDAO.getPagosPlanificadosPorId(idPago);
				PagoPlanificadoDAO.eliminarTotalPagoPlanificado(pagoPlanificado);
			}
			
		} else
			response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");


        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}

}
