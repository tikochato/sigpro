package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.MetaTipoDAO;
import dao.ProductoDAO;
import pojo.Producto;
import utilities.Utils;

@WebServlet("/SPlanEjecucion")
public class SPlanEjecucion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SPlanEjecucion() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getDatosPlan")){
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
			Date fecha_actual = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
			List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyectoId, null);
			BigDecimal ejecucionFisica = new BigDecimal("0");
			for(Producto producto : productos){
				BigDecimal mvReal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 1);
				BigDecimal mvFinal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 4);
				if (mvReal!=null && mvFinal!=null){
					//ejecucionFisica = ejecucionFisica +  mvReal.divide(mvFinal, 2, BigDecimal.ROUND_HALF_UP)  * ((double) producto.getPeso() / 100);
					ejecucionFisica = ejecucionFisica.add(mvReal.divide(mvFinal, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal((double) producto.getPeso() / 100)));
				}
				else
					ejecucionFisica = new BigDecimal("0");
			}
			response_text = String.join("", "{ \"success\": true ,",
					"\"ejecucionFisica\": \"" , ejecucionFisica + "" , "\",",
					"\"fecha\": \"" , sdf.format(fecha_actual), "\"",
					"}");
			
		}else{
			response_text = "{ \"success\": false }";
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
