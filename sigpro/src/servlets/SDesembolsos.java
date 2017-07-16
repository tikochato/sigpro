package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.DataSigadeDAO;
import dao.DesembolsoDAO;
import dao.PrestamoDAO;
import pojo.Prestamo;
import utilities.Utils;


@WebServlet("/SDesembolsos")
public class SDesembolsos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SDesembolsos() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{\"success\": false }").append(request.getContextPath());
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
		if(accion.equals("getDesembolsos")){
			Integer ejercicioFiscal = Utils.String2Int(map.get("ejercicioFiscal"));
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"));
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyectoId, 1);
			
			if (prestamo!=null){
			
				List<?> objDesembolso =DesembolsoDAO.getDesembolsosPorEjercicio(ejercicioFiscal,proyectoId);
				Set<Integer> anios = new HashSet<Integer>();
				
				Map<Integer,Map<Integer,BigDecimal>> desembolsosPlanificado = new HashMap<>();
				for(Object obj : objDesembolso){
					if (desembolsosPlanificado.get((Integer)((Object[]) obj)[0]) == null){
						desembolsosPlanificado.put((Integer)((Object[]) obj)[0], new HashMap<>());
						anios.add((Integer)((Object[]) obj)[0]);
					}
					desembolsosPlanificado.get((Integer)((Object[]) obj)[0]).put((Integer)((Object[]) obj)[1], (BigDecimal)((Object[]) obj)[2]);
				}
				
				List<?> dtmAvance = DataSigadeDAO.getAVANCE_FISFINAN_DET_DTI( prestamo.getCodigoPresupuestario()+"" );
				
				Map<BigDecimal,Map<Integer,BigDecimal>> desembolsosReal = new HashMap<>();
				for(Object obj : dtmAvance){
					if (desembolsosReal.get((BigDecimal)((Object[]) obj)[0]) == null)
						desembolsosReal.put((BigDecimal)((Object[]) obj)[0], new HashMap<>());
					if (!anios.contains(((BigDecimal)((Object[]) obj)[0]).intValue()))
						anios.add(((BigDecimal)((Object[]) obj)[0]).intValue());
					desembolsosReal.get((BigDecimal)((Object[]) obj)[0]).put(Integer.parseInt((String)((Object[]) obj)[1]),(BigDecimal)((Object[]) obj)[2]);
				}
				
				
				Iterator<Integer> iterator = anios.iterator();
				String lista = "";
			    while(iterator.hasNext()) {
			        Integer anio = iterator.next();
			        
			        Map<Integer,BigDecimal> planTemp = desembolsosPlanificado.get(anio);
			        Map<Integer,BigDecimal> realTemp = desembolsosReal.get(new BigDecimal(anio));
			        String planificado="";
					String real="";
			        
			        for (int i = 1; i <=12 ; i++){
						planificado = planificado + (planificado.length()>0 ? "," :"") +  
								(planTemp!=null && planTemp.get(i)!=null ? planTemp.get(i).toString() : "0");
			        	
								
						real = real + (real.length()>0 ? "," :"") +  
										(realTemp!=null && realTemp.get(i)!=null ?  realTemp.get(i).toString() : "0");
					}
			        
			        planificado = String.join("", "[",planificado,"]");
					real = String.join("", "[",real,"]");
					String item = String.join("","{\"anio\":",anio+",","\"desembolsos\": ", "[",planificado,",",real,"]}");
					lista = lista + (lista.length()>0 ? ",":"") + item;
			    }
			    
			    response_text = String.join("","{ \"success\": true, \"lista\": [" ,lista,"]}");
			    
			}else{
				response_text = "{ \"success\": false }";
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

}
