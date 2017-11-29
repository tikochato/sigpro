package servlets;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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

import dao.ObjetoCosto;
import dao.ObjetoDAO;
import dao.ProyectoDAO;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SInformacionPresupuestaria")
public class SInformacionPresupuestaria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;
       
    public SInformacionPresupuestaria() {
        super();
   }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
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
			if(accion.equals("generarInforme")){
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
				Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
				String lineaBase = map.get("lineaBase");
				List<ObjetoCosto> lstPrestamo = ObjetoDAO.getEstructuraConCosto(idPrestamo, anioInicial, anioFinal, true, true, false, lineaBase, usuario);
				
				if (null != lstPrestamo && !lstPrestamo.isEmpty()){
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstPrestamo);
			        response_text = String.join("", "\"prestamo\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}else{
					response_text = String.join("", "{\"success\":false}");
				}

				response.setHeader("Content-Encoding", "gzip");
				response.setCharacterEncoding("UTF-8");
		
		        OutputStream output = response.getOutputStream();
				GZIPOutputStream gz = new GZIPOutputStream(output);
		        gz.write(response_text.getBytes("UTF-8"));
		        gz.close();
		        output.close();
			}else if(accion.equals("exportarExcel")){
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
				Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
				Integer agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
				Integer tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
				String lineaBase = map.get("lineaBase");
		        byte [] outArray = exportarExcel(idPrestamo, anioInicial, anioFinal, agrupacion, tipoVisualizacion, lineaBase, usuario);
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Ejecucion_Presupuestaria.xls");
				ServletOutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}else if(accion.equals("exportarPdf")){
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
				Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
				Integer agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
				Integer tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
				CPdf archivo = new CPdf("Ejecucion presupuestaria");
				String headers[][];
				String datosMetas[][];
				headers = generarHeaders(anioInicial, anioFinal, agrupacion, tipoVisualizacion);
				String lineaBase = map.get("lineaBase");
				List<ObjetoCosto> lstPrestamo = ObjetoDAO.getEstructuraConCosto(idPrestamo, anioInicial, anioFinal, true, true, false, lineaBase, usuario);
				datosMetas = generarDatosReporte(lstPrestamo, anioInicial, anioFinal, agrupacion, tipoVisualizacion, headers[0].length, usuario);
				String path = archivo.ExportarPdfEjecucionPresupuestaria(headers, datosMetas,tipoVisualizacion);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
			        try {
			        	is = new FileInputStream(file);
			        }
			        catch (Exception e) {
						CLogger.write("1", SInformacionPresupuestaria.class, e);
			        }
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
					response.setContentType("application/pdf");
					response.setContentLength(outArray.length);
					response.setHeader("Cache-Control", "no-cache"); 
					response.setHeader("Content-Disposition", "in-line; 'Ejecucion_Presupuestaria.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}
	
				
			}
		}catch(Exception e){
			CLogger.write("2", SInformacionPresupuestaria.class, e);		
		}
	}
	
		
	private byte[] exportarExcel(int prestamoId, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, String lineaBase, String usuario){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datosInforme[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders(anioInicio, anioFin, agrupacion, tipoVisualizacion);
			List<ObjetoCosto> lstPrestamo = ObjetoDAO.getEstructuraConCosto(prestamoId, anioInicio, anioFin, true, true, false, lineaBase, usuario);
			datosInforme = generarDatosReporte(lstPrestamo, anioInicio, anioFin, agrupacion, tipoVisualizacion, headers[0].length, usuario);
			CGraficaExcel grafica = generarGrafica(datosInforme, tipoVisualizacion, agrupacion, anioInicio, anioFin);
			excel = new CExcel("Ejecucion presupuestaria", false, grafica);
			Proyecto proyecto = ProyectoDAO.getProyecto(prestamoId);
			wb=excel.generateExcelOfData(datosInforme, "Ejecucion presupuestaria - "+proyecto.getNombre(), headers, null, true, usuario);
			wb.write(outByteStream);
			outByteStream.close();
			outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("5", SInformacionPresupuestaria.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion){
		String headers[][];
		String[][] AgrupacionesTitulo = new String[][]{{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"},
			{"Bimestre 1", "Bimestre 2","Bimestre 3","Bimestre 4","Bimestre 5","Bimestre 6"},
			{"Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4"},
			{"Cuatrimestre 1", "Cuatrimestre 2", "Cuatrimestre 3"},
			{"Semestre 1","Semestre 2"},
			{"Anual"}
		};
		
		int totalesCant = 1;
		int aniosDiferencia =(anioFin-anioInicio)+1; 
		int columnasTotal = (aniosDiferencia*(AgrupacionesTitulo[agrupacion-1].length));
		int factorVisualizacion = 1;
		if(tipoVisualizacion == 2){
			columnasTotal = columnasTotal*2;
			totalesCant=(aniosDiferencia*2)+(totalesCant*2);
			columnasTotal += 1+totalesCant;
			factorVisualizacion = 2;
		}else{
			totalesCant+=aniosDiferencia;
			columnasTotal += 1+totalesCant; //Nombre, totales por a�o, total
		}
		
		String titulo[] = new String[columnasTotal];
		String tipo[] = new String[columnasTotal];
		String subtitulo[] = new String[columnasTotal];
		String operacionesFila[] = new String[columnasTotal];
		String columnasOperacion[] = new String[columnasTotal];
		String totales[] = new String[totalesCant];
		titulo[0]="Nombre";
		tipo[0]="string";
		subtitulo[0]="";
		operacionesFila[0]="";
		columnasOperacion[0]="";
		for(int i=0;i<totalesCant;i++){ //Inicializar totales
			totales[i]="";
		}
		int pos=1;
		for(int i=0; i<AgrupacionesTitulo[agrupacion-1].length; i++){
			for (int j=0; j<aniosDiferencia; j++){
				titulo[pos] = AgrupacionesTitulo[agrupacion-1][i] + " " + (anioInicio+j);
				tipo[pos] = "double";
				operacionesFila[pos]="";
				columnasOperacion[pos]="";
				totales[j*factorVisualizacion]+=pos+",";
				if(tipoVisualizacion==1){
					subtitulo[pos]="Real";
				}else{
					subtitulo[pos]="Planificado";
				}
				pos++;
				if(tipoVisualizacion == 2){
					titulo[pos] = "";
					tipo[pos] = "double";
					subtitulo[pos]="Real";
					operacionesFila[pos]="";
					columnasOperacion[pos]="";
					totales[(j*factorVisualizacion)+1]+=pos+",";
					pos++;
				}
			}
		}
		for (int j=0; j<aniosDiferencia; j++){
			titulo[pos] = "Total " + (anioInicio+j);
			tipo[pos] = "double";
			operacionesFila[pos]="sum";
			columnasOperacion[pos]= totales[j*factorVisualizacion];
			totales[totalesCant-factorVisualizacion] += pos+",";
			if(tipoVisualizacion==1){
				subtitulo[pos]="Real";
			}else{
				subtitulo[pos]="Planificado";
			}
			pos++;
			if(tipoVisualizacion == 2){
				titulo[pos] = "";
				tipo[pos] = "double";
				subtitulo[pos]="Real";
				operacionesFila[pos]="sum";
				columnasOperacion[pos]=totales[(j*factorVisualizacion)+1];
				totales[totalesCant-1] += pos+",";
				pos++;
			}
		}
		titulo[pos] = "Total";
		tipo[pos] = "double";
		operacionesFila[pos]="sum";
		columnasOperacion[pos]=totales[totalesCant-factorVisualizacion];
		if(tipoVisualizacion==1){
			subtitulo[pos]="Real";
		}else{
			subtitulo[pos]="Planificado";
		}
		pos++;
		if(tipoVisualizacion == 2){
			titulo[pos] = "";
			tipo[pos] = "double";
			subtitulo[pos]="Real";
			operacionesFila[pos]="sum";
			columnasOperacion[pos]=totales[totalesCant-1];
			pos++;
		}
		
		headers = new String[][]{
			titulo,  //titulos
			{""}, //mapeo
			tipo, //tipo dato
			{""}, //operaciones columnas
			{""}, //operaciones div
			subtitulo,
			operacionesFila,
			columnasOperacion
			};
			
		return headers;
	}
	
	public String[][] generarDatosReporte(List<ObjetoCosto> lstPrestamo, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, int columnasTotal, String usuario){
		String[][] datos = null;
		int columna = 0;int factorVisualizacion=1;
		if(tipoVisualizacion==2){
			factorVisualizacion = 2;
		}
		int sumaColumnas = ((anioFin-anioInicio) + 1)*factorVisualizacion;
		
		if (lstPrestamo != null && !lstPrestamo.isEmpty()){
			datos = new String[lstPrestamo.size()][columnasTotal];
			for (int i=0; i<lstPrestamo.size(); i++){
				columna = 0;
				ObjetoCosto prestamo = lstPrestamo.get(i);
				String sangria="";
				for(int s=1; s<prestamo.getNivel(); s++){
					sangria+="   ";
				}
				datos[i][columna] = sangria+prestamo.getNombre();
				columna++;
					int posicion = columna;
					BigDecimal totalAniosP = new BigDecimal(0);
					BigDecimal totalAniosR = new BigDecimal(0);
					//Valores planificado-real
					for(int a=0; a<prestamo.getAnios().length; a++){
						posicion = columna + (a*factorVisualizacion);
						
						for(int m=0; m<12; m++){
							prestamo.getAnios()[a].mes[m].planificado=(prestamo.getAnios()[a].mes[m].planificado==null) ? new BigDecimal(0) : prestamo.getAnios()[a].mes[m].planificado.setScale(2, BigDecimal.ROUND_DOWN);;
							prestamo.getAnios()[a].mes[m].real=(prestamo.getAnios()[a].mes[m].real==null) ? new BigDecimal(0) : prestamo.getAnios()[a].mes[m].real.setScale(2, BigDecimal.ROUND_DOWN);;
						}
						
						BigDecimal totalAnualP = new BigDecimal(0);
						BigDecimal totalAnualR = new BigDecimal(0);
						for(int m=0; m<12; m++){
							totalAnualP = totalAnualP.add(prestamo.getAnios()[a].mes[m].planificado);
							totalAnualR = totalAnualR.add(prestamo.getAnios()[a].mes[m].real);
						}
						totalAniosP = totalAniosP.add(totalAnualP);
						totalAniosR = totalAniosR.add(totalAnualR);
						switch(agrupacion){
						case AGRUPACION_MES:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= prestamo.getAnios()[a].mes[0].planificado.toString();
								datos[i][posicion+(1*sumaColumnas)]= prestamo.getAnios()[a].mes[1].planificado.toString();
								datos[i][posicion+(2*sumaColumnas)]= prestamo.getAnios()[a].mes[2].planificado.toString();
								datos[i][posicion+(3*sumaColumnas)]= prestamo.getAnios()[a].mes[3].planificado.toString();
								datos[i][posicion+(4*sumaColumnas)]= prestamo.getAnios()[a].mes[4].planificado.toString();
								datos[i][posicion+(5*sumaColumnas)]= prestamo.getAnios()[a].mes[5].planificado.toString();
								datos[i][posicion+(6*sumaColumnas)]= prestamo.getAnios()[a].mes[6].planificado.toString();
								datos[i][posicion+(7*sumaColumnas)]= prestamo.getAnios()[a].mes[7].planificado.toString();
								datos[i][posicion+(8*sumaColumnas)]= prestamo.getAnios()[a].mes[8].planificado.toString();
								datos[i][posicion+(9*sumaColumnas)]= prestamo.getAnios()[a].mes[9].planificado.toString();
								datos[i][posicion+(10*sumaColumnas)]= prestamo.getAnios()[a].mes[10].planificado.toString();
								datos[i][posicion+(11*sumaColumnas)]= prestamo.getAnios()[a].mes[11].planificado.toString();
								datos[i][posicion+(12*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= prestamo.getAnios()[a].mes[0].real.toString();
								datos[i][posicion+(1*sumaColumnas)]= prestamo.getAnios()[a].mes[1].real.toString();
								datos[i][posicion+(2*sumaColumnas)]= prestamo.getAnios()[a].mes[2].real.toString();
								datos[i][posicion+(3*sumaColumnas)]= prestamo.getAnios()[a].mes[3].real.toString();
								datos[i][posicion+(4*sumaColumnas)]= prestamo.getAnios()[a].mes[4].real.toString();
								datos[i][posicion+(5*sumaColumnas)]= prestamo.getAnios()[a].mes[5].real.toString();
								datos[i][posicion+(6*sumaColumnas)]= prestamo.getAnios()[a].mes[6].real.toString();
								datos[i][posicion+(7*sumaColumnas)]= prestamo.getAnios()[a].mes[7].real.toString();
								datos[i][posicion+(8*sumaColumnas)]= prestamo.getAnios()[a].mes[8].real.toString();
								datos[i][posicion+(9*sumaColumnas)]= prestamo.getAnios()[a].mes[9].real.toString();
								datos[i][posicion+(10*sumaColumnas)]= prestamo.getAnios()[a].mes[10].real.toString();
								datos[i][posicion+(11*sumaColumnas)]= prestamo.getAnios()[a].mes[11].real.toString();
								datos[i][posicion+(12*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(12*sumaColumnas)+1;
							break;
						case AGRUPACION_BIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].planificado.add(prestamo.getAnios()[a].mes[1].planificado)).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[2].planificado.add(prestamo.getAnios()[a].mes[3].planificado)).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.getAnios()[a].mes[4].planificado.add(prestamo.getAnios()[a].mes[5].planificado)).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.getAnios()[a].mes[6].planificado.add(prestamo.getAnios()[a].mes[7].planificado)).toString();
								datos[i][posicion+(4*sumaColumnas)]= (prestamo.getAnios()[a].mes[8].planificado.add(prestamo.getAnios()[a].mes[9].planificado)).toString();
								datos[i][posicion+(5*sumaColumnas)]= (prestamo.getAnios()[a].mes[10].planificado.add(prestamo.getAnios()[a].mes[11].planificado)).toString();
								datos[i][posicion+(6*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].real.add(prestamo.getAnios()[a].mes[1].real)).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[2].real.add(prestamo.getAnios()[a].mes[3].real)).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.getAnios()[a].mes[4].real.add(prestamo.getAnios()[a].mes[5].real)).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.getAnios()[a].mes[6].real.add(prestamo.getAnios()[a].mes[7].real)).toString();
								datos[i][posicion+(4*sumaColumnas)]= (prestamo.getAnios()[a].mes[8].real.add(prestamo.getAnios()[a].mes[9].real)).toString();
								datos[i][posicion+(5*sumaColumnas)]= (prestamo.getAnios()[a].mes[10].real.add(prestamo.getAnios()[a].mes[11].real)).toString();
								datos[i][posicion+(6*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(6*sumaColumnas)+1;
							break;
						case AGRUPACION_TRIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].planificado.add(prestamo.getAnios()[a].mes[1].planificado.add(prestamo.getAnios()[a].mes[2].planificado))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[3].planificado.add(prestamo.getAnios()[a].mes[4].planificado.add(prestamo.getAnios()[a].mes[5].planificado))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.getAnios()[a].mes[6].planificado.add(prestamo.getAnios()[a].mes[7].planificado.add(prestamo.getAnios()[a].mes[8].planificado))).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.getAnios()[a].mes[9].planificado.add(prestamo.getAnios()[a].mes[10].planificado.add(prestamo.getAnios()[a].mes[11].planificado))).toString();
								datos[i][posicion+(4*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].real.add(prestamo.getAnios()[a].mes[1].real.add(prestamo.getAnios()[a].mes[2].real))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[3].real.add(prestamo.getAnios()[a].mes[4].real.add(prestamo.getAnios()[a].mes[5].real))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.getAnios()[a].mes[6].real.add(prestamo.getAnios()[a].mes[7].real.add(prestamo.getAnios()[a].mes[8].real))).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.getAnios()[a].mes[9].real.add(prestamo.getAnios()[a].mes[10].real.add(prestamo.getAnios()[a].mes[11].real))).toString();
								datos[i][posicion+(4*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(4*sumaColumnas)+1;
							break;
						case AGRUPACION_CUATRIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].planificado.add(prestamo.getAnios()[a].mes[1].planificado).add(prestamo.getAnios()[a].mes[2].planificado.add(prestamo.getAnios()[a].mes[3].planificado))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[4].planificado).add(prestamo.getAnios()[a].mes[5].planificado.add(prestamo.getAnios()[a].mes[6].planificado.add(prestamo.getAnios()[a].mes[7].planificado))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.getAnios()[a].mes[8].planificado.add(prestamo.getAnios()[a].mes[9].planificado).add(prestamo.getAnios()[a].mes[10].planificado.add(prestamo.getAnios()[a].mes[11].planificado))).toString();
								datos[i][posicion+(3*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].real.add(prestamo.getAnios()[a].mes[1].real).add(prestamo.getAnios()[a].mes[2].real.add(prestamo.getAnios()[a].mes[3].real))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[4].real).add(prestamo.getAnios()[a].mes[5].real.add(prestamo.getAnios()[a].mes[6].real.add(prestamo.getAnios()[a].mes[7].real))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.getAnios()[a].mes[8].real.add(prestamo.getAnios()[a].mes[9].real).add(prestamo.getAnios()[a].mes[10].real.add(prestamo.getAnios()[a].mes[11].real))).toString();
								datos[i][posicion+(3*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(3*sumaColumnas)+1;
							break;
						case AGRUPACION_SEMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].planificado.add(prestamo.getAnios()[a].mes[1].planificado).add(prestamo.getAnios()[a].mes[2].planificado.add(prestamo.getAnios()[a].mes[3].planificado.add(prestamo.getAnios()[a].mes[4].planificado.add(prestamo.getAnios()[a].mes[5].planificado))))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[6].planificado.add(prestamo.getAnios()[a].mes[7].planificado).add(prestamo.getAnios()[a].mes[8].planificado.add(prestamo.getAnios()[a].mes[9].planificado.add(prestamo.getAnios()[a].mes[10].planificado.add(prestamo.getAnios()[a].mes[11].planificado))))).toString();
								datos[i][posicion+(2*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.getAnios()[a].mes[0].real.add(prestamo.getAnios()[a].mes[1].real).add(prestamo.getAnios()[a].mes[2].real.add(prestamo.getAnios()[a].mes[3].real.add(prestamo.getAnios()[a].mes[4].real.add(prestamo.getAnios()[a].mes[5].real))))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.getAnios()[a].mes[6].real.add(prestamo.getAnios()[a].mes[7].real).add(prestamo.getAnios()[a].mes[8].real.add(prestamo.getAnios()[a].mes[9].real.add(prestamo.getAnios()[a].mes[10].real.add(prestamo.getAnios()[a].mes[11].real))))).toString();
								datos[i][posicion+(2*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(2*sumaColumnas)+1;
							break;
						case AGRUPACION_ANUAL:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= totalAnualP.toString();
								datos[i][posicion+(1*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= totalAnualR.toString();
								datos[i][posicion+(1*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(1*sumaColumnas)+1;
							break;
						}
					}
					if(tipoVisualizacion==0 || tipoVisualizacion==2){
						datos[i][posicion]= totalAniosP.toString();
					}
					if(tipoVisualizacion == 2){
						posicion++;
					}
					if(tipoVisualizacion==1 || tipoVisualizacion == 2){
						datos[i][posicion]= totalAniosR.toString();
					}
			}
		}
		return datos;
	}
	

	
	public CGraficaExcel generarGrafica(String[][] datosTabla, Integer tipoVisualizacion, Integer agrupacion, Integer anioInicio, Integer anioFin){
		String[][] AgrupacionesTitulo = new String[][]{{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"},
			{"Bimestre 1", "Bimestre 2","Bimestre 3","Bimestre 4","Bimestre 5","Bimestre 6"},
			{"Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4"},
			{"Cuatrimestre 1", "Cuatrimestre 2", "Cuatrimestre 3"},
			{"Semestre 1","Semestre 2"},
			{"Anual"}
		};
		
		int aniosDiferencia =(anioFin-anioInicio)+1; 
		int columnasTotal = (aniosDiferencia*(AgrupacionesTitulo[agrupacion-1].length));
		
		String titulo[] = new String[columnasTotal];
		String valor[] = new String[columnasTotal];
		Integer columna=0;
		for(int i=0; i<aniosDiferencia; i++){
			for(int j=0; j<AgrupacionesTitulo[agrupacion-1].length; j++){
				titulo[columna] = AgrupacionesTitulo[agrupacion-1][j] + " - "+(anioInicio+i);
				valor[columna]="0";
				columna++;
			}
			
		}
		String[][] datos = new String[][]{
			titulo,
			valor, 
			valor
		};
		

		columna = 0;
		int columnaDatos=1;
		int factorVisualizacion=1;
		if(tipoVisualizacion==2){
			factorVisualizacion = 2;
		}
		int sumaColumnas = ((anioFin-anioInicio) + 1)*factorVisualizacion;
		
		String[][] datosIgualar= new String[3][columnasTotal];
		
		for(int i=0; i<aniosDiferencia; i++){
			Integer posicionDatos = columnaDatos;
			for(int j=0; j<AgrupacionesTitulo[agrupacion-1].length; j++){
				datosIgualar[0][columna]="";
				
				datosIgualar[1][columna]=posicionDatos+"."+(31);
				if(tipoVisualizacion==2){
					datosIgualar[2][columna]=(posicionDatos+1)+"."+(31);
				}else{
					datosIgualar[2][columna]="";
				}
				posicionDatos+=sumaColumnas;
				columna++;
			}
			columnaDatos+=factorVisualizacion;
		}
		
		String[] tipoData = new String[]{"string","double","double"};
		CGraficaExcel grafica = new CGraficaExcel("Ejecucion Presupuestaria", CGraficaExcel.EXCEL_CHART_AREA, "Meses", "Planificado", datos, tipoData, datosIgualar);
	
		return grafica;
	}
	
}
