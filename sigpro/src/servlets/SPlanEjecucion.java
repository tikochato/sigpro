package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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
import pojo.MetaValor;
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
			double ejecucionFisica = 0d;
			for(Producto producto : productos){
				MetaValor mvReal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 1);
				MetaValor mvFinal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 4);
				ejecucionFisica = ejecucionFisica +  ((((double)mvReal.getValorEntero() / mvFinal.getValorEntero()) * 100) ) * ((double) producto.getPeso() / 100);
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
