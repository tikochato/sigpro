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



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.MetaDAO;
import dao.MetaValorDAO;
import pojo.Meta;
import pojo.MetaValor;
import pojo.MetaValorId;
import utilities.Utils;

@WebServlet("/SMetaValor")
public class SMetaValor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public class stmetavalor{
		Integer metaid;
		String fecha;
		String usuario;
		Integer datoTipo;
		Integer valorEntero;
		String valorString;
		BigDecimal valorDecimal; 
		Date valorTiempo;
		String valor;
		Integer estado;
		String fechaIngreso;
	}

    public SMetaValor() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = request.getReader();
		HttpSession sesionweb = request.getSession();
		String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
		String str;
		while ((str = br.readLine()) != null) {
			sb.append(str);
		}
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getMetaValoresTabla")){
			Integer metaid =  map.get("metaid")!=null  ? Integer.parseInt(map.get("metaid")) : 0;
			Integer datoTipoId =  map.get("datotipoid")!=null  ? Integer.parseInt(map.get("datotipoid")) : 0;
			Integer pagina = Utils.String2Int(map.get("pagina"),0);
			Integer totalValores = Utils.String2Int(map.get("totalValores"),0);
			
			List<MetaValor> MetaValores = MetaValorDAO.getValoresMeta(metaid, pagina, totalValores);
			List<stmetavalor> tmetavalores = new ArrayList<stmetavalor>();
			for(MetaValor metaValor : MetaValores){
				stmetavalor temp = new stmetavalor();
				temp.metaid = metaValor.getId().getMetaid();
				temp.fecha = Utils.formatDate( metaValor.getId().getFecha());
				temp.usuario = metaValor.getUsuario();
				temp.usuario = metaValor.getUsuario();
				temp.fechaIngreso = Utils.formatDate(metaValor.getFechaIngreso());
				temp.datoTipo = datoTipoId;
				
				switch(datoTipoId){
					case 1: //texto
						temp.valor = metaValor.getValorString();
						break;
					case 2: //entero
						temp.valor = metaValor.getValorEntero() != null ? metaValor.getValorEntero().toString() : null;
						break;
					case 3: //decimal
						temp.valor = metaValor.getValorDecimal() !=null  ? metaValor.getValorDecimal().toString(): null;
						
						break;
					case 5: //fecha
						temp.valor = Utils.formatDate(metaValor.getValorTiempo());
						break;
					
				}
				tmetavalores.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(tmetavalores);
	        response_text = String.join("", "\"MetaValores\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getMetaValores")){
			Integer metaid =  map.get("metaid")!=null  ? Integer.parseInt(map.get("metaid")) : 0;
			List<MetaValor> MetaValores = MetaValorDAO.getValoresMeta(metaid,0,0);
			response_text=new GsonBuilder().serializeNulls().create().toJson(MetaValores);
	        response_text = String.join("", "\"MetaValores\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarMetaValor")){

			
			boolean result = false;
			boolean esnuevo = map.get("esnuevo")!=null ? map.get("esnuevo").equals("true") :  false;
			int metaid = map.get("metaid")!=null ? Integer.parseInt(map.get("metaid")) : 0;
			Date fecha = Utils.dateFromString(map.get("fecha"));
			Integer datoTipoId =  map.get("datotipoid")!=null  ? Integer.parseInt(map.get("datotipoid")) : 0;
			
			if(metaid>0 || esnuevo){
				String valor = map.get("valor");
				Meta Meta = MetaDAO.getMetaPorId(metaid);
				MetaValorId MetaValorId;
				MetaValor MetaValor;
				
				String valorTexto = null;
				Integer valorEntero = null;
				BigDecimal ValorDecimal = null;
				Date valorFecha = null;
				switch (datoTipoId){
					case 1: //texto
						valorTexto = valor;
						break;
					case 2: //entero
						valorEntero = Utils.String2Int(valor);
						break;
					case 3: //decimal
						ValorDecimal = Utils.String2BigDecimal(valor, null);
						break;
					case 5: //fecha
						valorFecha = Utils.dateFromString(valor);
						break;
				}
				
				
				if(esnuevo){		
				
					
					MetaValorId = new MetaValorId(Meta.getId(), fecha);
					MetaValor = new MetaValor(MetaValorId, Meta, usuario, valorEntero, valorTexto, ValorDecimal, valorFecha, 1, new Date());
				}
				else{
					MetaValorId = new MetaValorId(Meta.getId(), fecha);
					MetaValor = MetaValorDAO.getMetaValorPorId(MetaValorId);
					
					switch (datoTipoId){
						case 1: //texto
							MetaValor.setValorString(valorTexto);
							break;
						case 2: //entero
							MetaValor.setValorEntero(valorEntero);
							break;
						case 3: //decimal
							MetaValor.setValorDecimal(ValorDecimal);
							break;
						case 5: //fecha
							MetaValor.setValorTiempo(valorFecha);
							break;
					}
					
					MetaValor.setUsuario(usuario);

				}
				result = MetaValorDAO.guardarMetaValor(MetaValor);
				
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),", "
						, "\"metaid\": " , String.valueOf(MetaValor.getId().getMetaid())
						," }");
			}
			else
				response_text = "{ \"success\": false }";
			
		}
		else if(accion.equals("borrarMetaValor")){
			int metaid = map.get("metaid")!=null ? Integer.parseInt(map.get("metaid")) : 0;
			Date fecha = Utils.dateFromString(map.get("fecha"));
			
			if(metaid>0){
				MetaValorId MetaValorId = new MetaValorId(metaid, fecha);
				MetaValor MetaValor = MetaValorDAO.getMetaValorPorId(MetaValorId);
				response_text = String.join("","{ \"success\": ",(MetaValorDAO.eliminarMetaValor(MetaValor) ? "true" : "false")," }");
			}
			else
				response_text = "{ \"success\": false }";
		}
		else if(accion.equals("numeroMetaValores")){
			Integer metaid =  map.get("metaid")!=null  ? Integer.parseInt(map.get("metaid")) : 0;
			Meta Meta = MetaDAO.getMetaPorId(metaid);
			response_text = String.join("","{ \"success\": true, \"totalMetaValores\":",String.valueOf(Meta.getMetaValors().size())," }");
		}
		else{
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
