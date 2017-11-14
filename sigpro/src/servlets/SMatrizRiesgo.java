package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ProyectoDAO;
import dao.RiesgoDAO;
import pojo.ObjetoRiesgo;
import pojo.Riesgo;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SMatrizRiesgo")
public class SMatrizRiesgo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stmatriz {
		Integer idRiesgo;
		String nombre;
		Integer objetoTipo;
		String objetoTipoNombre;
		Integer tipoId;
		String tipoNombre;
		BigDecimal impacto;
		BigDecimal probabilidad;
		BigDecimal impactoMonto;
		BigDecimal impactoTiempo;
		String gatillos;
		String consecuencia;
		String solucion;
		String riesgosSecundarios;
		Integer ejecutado;
		String fechaEjecucion;
		Integer colaboradorId;
		String colaboradorNombre;
		String resultado;
		String observaciones;		
	}
       
    public SMatrizRiesgo() {
        super();
    
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
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
				temp.impacto = riesgo.getImpacto();
				temp.probabilidad = riesgo.getProbabilidad();
				temp.impactoMonto = riesgo.getImpactoMonto()!=null ? riesgo.getImpactoMonto() : new BigDecimal(0);
				temp.impactoTiempo = riesgo.getImpactoTiempo()!=null ? riesgo.getImpactoTiempo() : new BigDecimal(0);
				temp.gatillos = riesgo.getGatillo();
				temp.consecuencia = riesgo.getConsecuencia();
				temp.solucion = riesgo.getSolucion();
				temp.riesgosSecundarios = riesgo.getRiesgosSegundarios();
				temp.ejecutado = riesgo.getEjecutado();
				temp.fechaEjecucion = Utils.formatDate(riesgo.getFechaEjecucion());
				temp.resultado = riesgo.getResultado();
				temp.observaciones = riesgo.getObservaciones();
				temp.colaboradorNombre = riesgo.getColaborador()!=null ? riesgo.getColaborador().getPnombre() : "";
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
					case -1: temp.objetoTipoNombre = "Préstamo"; break;
					case 0: temp.objetoTipoNombre = "Pep"; break;
					case 1: temp.objetoTipoNombre = "Componente"; break;
					case 2: temp.objetoTipoNombre = "Subcomponente"; break;
					case 3: temp.objetoTipoNombre = "Producto"; break;
					case 4: temp.objetoTipoNombre = "Subproducto"; break;
					case 5: temp.objetoTipoNombre = "Actividad"; break;
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
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			
			try{
		        byte [] outArray = exportarExcel(proyectoId, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Matriz_de_Riesgos.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SMatrizRiesgo.class, e);
			}
		}
	}
	
	private byte[] exportarExcel(int proyectoId, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{		
			excel = new CExcel("Matriz de Riesgos", false, null);
			headers = generarHeaders();
			datos = generarDatos(proyectoId,headers[0].length);
			wb=excel.generateExcelOfData(datos, "Matriz de Riesgos - "+ProyectoDAO.getProyecto(proyectoId).getNombre(), headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("2", SMatrizRiesgo.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];				
		String titulo[] = new String[21];
		String tipo[] = new String[21];
		
		titulo[0]="Id";
		tipo[0]="int";
		titulo[1]="Riesgo";
		tipo[1]="string";
		titulo[2]="Descripción";
		tipo[2]="string";
		titulo[3]="Categoría";
		tipo[3]="string";
		titulo[4]="Nivel";
		tipo[4]="string";
		titulo[5]="Impacto";
		tipo[5]="double";
		titulo[6]="Probabilidad";
		tipo[6]="double";
		titulo[7]="Calificación";
		tipo[7]="double";
		titulo[8]="Impacto en Tiempo";
		tipo[8]="double";
		titulo[9]="Contingenica en Tiempo";
		tipo[9]="double";
		titulo[10]="Impacto en Monto (Q)";
		tipo[10]="double";
		titulo[11]="Contingencia en Monto (Q)";
		tipo[11]="double";
		titulo[12]="Evento Iniciador";
		tipo[12]="string";
		titulo[13]="Consecuencias";
		tipo[13]="string";
		titulo[14]="Riesgos Secundarios";
		tipo[14]="string";
		titulo[15]="Solución";
		tipo[15]="string";
		titulo[16]="Responsable";
		tipo[16]="string";
		titulo[17]="¿Ha sido ejecutado?";
		tipo[17]="string";
		titulo[18]="Fecha de Ejecución";
		tipo[18]="string";
		titulo[19]="Resultado";
		tipo[19]="string";
		titulo[20]="Observaciones";
		tipo[20]="string";
		
		headers = new String[][]{
			titulo,  //titulos
			{""}, //mapeo
			tipo, //tipo dato
			{""}, //operaciones columnas
			{""}, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatos(int proyectoId, int columnasTotal){
		String[][] datos = null;
		List<Riesgo> riesgos = RiesgoDAO.getMatrizRiesgo(proyectoId);		
		if (riesgos != null && !riesgos.isEmpty()){
			datos = new String[riesgos.size()][columnasTotal];
			for (int i=0; i<riesgos.size(); i++){
				Riesgo riesgo = riesgos.get(i);
				
				String colaboradorNombre = riesgo.getColaborador()!=null ? riesgo.getColaborador().getPnombre() : "";
				if (riesgo.getColaborador()!=null ){
					colaboradorNombre = String.join(" ", riesgo.getColaborador().getPnombre(),
						riesgo.getColaborador().getSnombre() !=null ? riesgo.getColaborador().getSnombre() : "",
						riesgo.getColaborador().getPapellido()!=null ? riesgo.getColaborador().getPapellido() : "",
						riesgo.getColaborador().getSapellido()!=null ? riesgo.getColaborador().getSapellido() : ""	
					);
				}
				ObjetoRiesgo objetoRiesgo = RiesgoDAO.getObjetoRiesgo(riesgo.getId());
				String objetoTipoNombre="";
				if (objetoRiesgo!=null){
					int objetoTipo = objetoRiesgo.getId().getObjetoTipo();
					switch (objetoTipo){
					case -1: objetoTipoNombre = "Préstamo"; break;
					case 0: objetoTipoNombre = "Pep"; break;
					case 1: objetoTipoNombre = "Componente"; break;
					case 2: objetoTipoNombre = "Subcomponente"; break;
					case 3: objetoTipoNombre = "Producto"; break;
					case 4: objetoTipoNombre = "Subproducto"; break;
					case 5: objetoTipoNombre = "Actividad"; break;
					}
				}
				BigDecimal calificacion = (riesgo.getImpacto().multiply(riesgo.getProbabilidad())).setScale(2, BigDecimal.ROUND_DOWN);
				BigDecimal contingenciaMonto = (calificacion.multiply(riesgo.getImpactoMonto()!=null?riesgo.getImpactoMonto():new BigDecimal(0))).setScale(2, BigDecimal.ROUND_DOWN);
				BigDecimal contingenciaTiempo = (calificacion.multiply(riesgo.getImpactoTiempo()!=null?riesgo.getImpactoTiempo():new BigDecimal(0))).setScale(2, BigDecimal.ROUND_DOWN);

				datos[i][0] = riesgo.getId().toString();
				datos[i][1] = riesgo.getNombre();
				datos[i][2] = riesgo.getDescripcion();
				datos[i][3] = riesgo.getRiesgoTipo().getNombre();
				datos[i][4] = objetoTipoNombre;
				datos[i][5] = riesgo.getImpacto().setScale(2, BigDecimal.ROUND_FLOOR).toString();
				datos[i][6] = riesgo.getProbabilidad().setScale(2, BigDecimal.ROUND_FLOOR).toString();
				datos[i][7] = calificacion.toString();
				datos[i][8] = riesgo.getImpactoTiempo()!=null ? riesgo.getImpactoTiempo().toString() : "0";
				datos[i][9] = contingenciaTiempo.toString();
				datos[i][10] = riesgo.getImpactoMonto()!=null ? riesgo.getImpactoMonto().toString() : "0";
				datos[i][11] = contingenciaMonto.toString();
				datos[i][12] = riesgo.getGatillo();
				datos[i][13] = riesgo.getConsecuencia();
				datos[i][14] = riesgo.getRiesgosSegundarios();
				datos[i][15] = riesgo.getSolucion();
				datos[i][16] = colaboradorNombre;
				datos[i][17] = riesgo.getEjecutado()==1 ? "Si" : "No";
				datos[i][18] = Utils.formatDate(riesgo.getFechaEjecucion());
				datos[i][19] = riesgo.getResultado();
				datos[i][20] = riesgo.getObservaciones();
			}
		}
		return datos;
	}

}
