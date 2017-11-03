package servlets;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

import dao.RiesgoDAO;
import pojo.ObjetoRiesgo;
import pojo.Riesgo;
import utilities.Utils;

@WebServlet("/SMatrizRiesgo")
public class SMatrizRiesgo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stmatriz {
		Integer idRiesgo;
		Integer objetoTipo;
		String objetoTipoNombre;
		String nombre;
		Integer tipoId;
		String tipoNombre;
		String impactoProyectado;
		Integer impacto;
		Integer puntuacionImpacto;
		Integer probabilidad;
		Double punteoPrioridad;
		String gatillosSintomas;
		String respuesta;
		Integer colaboradorId;
		String colaboradorNombre;
		String riesgosSecundarios;
		Integer ejecutado;
		String fechaEjecucion;
	}
       
    public SMatrizRiesgo() {
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
		
		if(accion.equals("getMatrizRiesgos")){
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			List<Riesgo> riesgos = RiesgoDAO.getMatrizRiesgo(proyectoId);
			List<stmatriz> matriz = new ArrayList<>();
			for (Riesgo riesgo : riesgos){
				stmatriz temp = new stmatriz();
				temp.idRiesgo = riesgo.getId();
				temp.nombre = riesgo.getNombre();
				temp.tipoId = riesgo.getRiesgoTipo().getId();
				temp.tipoNombre = riesgo.getRiesgoTipo().getNombre();
				//temp.impactoProyectado = riesgo.getImapctoProyectado();
				//temp.impacto = riesgo.getImpacto();
				//temp.puntuacionImpacto = riesgo.getPuntuacionImpacto();
				//temp.probabilidad = riesgo.getProbabilidad();
				//temp.gatillosSintomas = riesgo.getGatillosSintomas();
				//temp.respuesta = riesgo.getRespuesta();
				temp.colaboradorNombre = riesgo.getColaborador()!=null ? riesgo.getColaborador().getPnombre() : "";
				temp.riesgosSecundarios = riesgo.getRiesgosSegundarios();
				temp.ejecutado = riesgo.getEjecutado();
				temp.fechaEjecucion = Utils.formatDate(riesgo.getFechaEjecucion());
				if (riesgo.getColaborador()!=null ){
					temp.colaboradorId = riesgo.getColaborador().getId();	
					temp.colaboradorNombre = String.join(" ", riesgo.getColaborador().getPnombre(),
						riesgo.getColaborador().getSnombre() !=null ? riesgo.getColaborador().getSnombre() : "",
						riesgo.getColaborador().getPapellido()!=null ? riesgo.getColaborador().getPapellido() : "",
						riesgo.getColaborador().getSapellido()!=null ? riesgo.getColaborador().getSapellido() : ""	
					);
				}
				ObjetoRiesgo objetoRiesgo = RiesgoDAO.getObjetoRiesgo(riesgo.getId());
				if (objetoRiesgo!=null){
					temp.objetoTipo = objetoRiesgo.getId().getObjetoTipo();
					switch (temp.objetoTipo){
					case 1: temp.objetoTipoNombre = "Proyecto"; break;
					case 2: temp.objetoTipoNombre = "Componente"; break;
					case 3: temp.objetoTipoNombre = "Producto"; break;
					case 4: temp.objetoTipoNombre = "Subproducto"; break;
					case 5: temp.objetoTipoNombre = "Actividad"; break;
					}
				}
				if (temp.probabilidad!=null){
					switch (temp.probabilidad){
						case 1: temp.punteoPrioridad = temp.puntuacionImpacto * 0.3;
								temp.punteoPrioridad =  BigDecimal.valueOf(temp.punteoPrioridad)
									    .setScale(2, RoundingMode.HALF_UP)
									    .doubleValue();
						break;
						case 2: temp.punteoPrioridad = temp.puntuacionImpacto * 0.6;
								temp.punteoPrioridad =  BigDecimal.valueOf(temp.punteoPrioridad)
							    .setScale(2, RoundingMode.HALF_UP)
							    .doubleValue();
								break;
						case 3: temp.punteoPrioridad = temp.puntuacionImpacto * 0.9;
							temp.punteoPrioridad =  BigDecimal.valueOf(temp.punteoPrioridad)
						    .setScale(2, RoundingMode.HALF_UP)
						    .doubleValue();
							break;
					}
				}
				matriz.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(matriz);
	        response_text = String.join("", "\"matrizriesgos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");
	        
	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
	        
		} else if (accion.equals("exportarExcel")){
			/*CExcel excel = new CExcel("MatrizRiesgos",false,null);
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			List<Riesgo> riesgos = RiesgoDAO.getMatrizRiesgo(proyectoId);
			Map<String,Object[]> datos = new HashMap<>();
			datos.put("0",   new Object[] {"Id Riesgo", "Nombre","Nivel", "Categoría","Impacto Proyectado","Impacto",
					"Puntuación Impacto","Probabilidad","Punteo Probabilidad","Gatillos/Sintomas",
					"Respuesta","Responsable","Riesgos Secundarios","¿Ha sido ejecutado?","Fecha de Ejecucion"});
			int fila = 1;
			for (Riesgo riesgo : riesgos){
				String responsable = "";
				String objetoTipoNombre="";
				if (riesgo.getColaborador()!=null ){
					responsable = String.join(" ", riesgo.getColaborador().getPnombre(),
						riesgo.getColaborador().getSnombre() !=null ? riesgo.getColaborador().getSnombre() : "",
						riesgo.getColaborador().getPapellido()!=null ? riesgo.getColaborador().getPapellido() : "",
						riesgo.getColaborador().getSapellido()!=null ? riesgo.getColaborador().getSapellido() : ""	
					);
				}
				ObjetoRiesgo objetoRiesgo = RiesgoDAO.getObjetoRiesgo(riesgo.getId());
				if (objetoRiesgo!=null){
					switch (objetoRiesgo.getId().getObjetoTipo()){
					case 1: objetoTipoNombre = "Proyecto"; break;
					case 2: objetoTipoNombre = "Componente"; break;
					case 3: objetoTipoNombre = "Producto"; break;
					case 4: objetoTipoNombre = "Subproducto"; break;
					case 5: objetoTipoNombre = "Actividad"; break;
					}
				}
				Double punteoPrioridad =  new Double("0");
				switch (riesgo.getProbabilidad()){
					case 1: punteoPrioridad = riesgo.getPuntuacionImpacto() * 0.3;
						punteoPrioridad =  BigDecimal.valueOf(punteoPrioridad)
								    .setScale(2, RoundingMode.HALF_UP)
								    .doubleValue();
					break;
					case 2:punteoPrioridad = riesgo.getPuntuacionImpacto() * 0.6;
						punteoPrioridad =  BigDecimal.valueOf(punteoPrioridad)
						    .setScale(2, RoundingMode.HALF_UP)
						    .doubleValue();
							break;
					case 3: punteoPrioridad = riesgo.getPuntuacionImpacto() * 0.9;
						punteoPrioridad =  BigDecimal.valueOf(punteoPrioridad)
						    .setScale(2, RoundingMode.HALF_UP)
						    .doubleValue();
						break;
				}
				datos.put(fila+"", new Object [] {riesgo.getId(),riesgo.getNombre(),objetoTipoNombre,riesgo.getRiesgoTipo().getNombre(),
						riesgo.getImapctoProyectado(),riesgo.getImpacto(),riesgo.getPuntuacionImpacto(),riesgo.getProbabilidad(),
						punteoPrioridad, riesgo.getGatillosSintomas(),riesgo.getRespuesta(),responsable,riesgo.getRiesgosSegundarios(),
						riesgo.getEjecutado(),riesgo.getFechaEjecucion()});
				fila++;
				
			}
			String path = excel.ExportarExcel(datos, "Matriz de Riesgos",usuario);
			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
		        	
		        }
		        //
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
				response.setHeader("Cache-Control", "no-cache"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; Matriz_de_Riesgos.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
	            
			}
			
			*/
			
		}
		
		
	}

}
