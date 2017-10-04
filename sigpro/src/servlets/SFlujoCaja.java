package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
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

import dao.DesembolsoDAO;
import dao.EstructuraProyectoDAO;
import dao.ProyectoDAO;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;
import utilities.CPrestamoCostos;

@WebServlet("/SFlujoCaja")
public class SFlujoCaja extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stTotales{
		BigDecimal[] filaPlanificado;
		BigDecimal[] filaPlanificadoAcumulado;
		BigDecimal[] filaEjecutado;
		BigDecimal[] filaEjecutadoAcumulado;
		BigDecimal[] filaVariacion;
		BigDecimal[] filaVariacionPorcentaje;
		BigDecimal[] filaDesembolsos;
		BigDecimal[] filaSaldo;
		
		BigDecimal[] totAnualPlanificado;
		BigDecimal[] totAnualPlanificadoAcumulado;
		BigDecimal[] totAnualEjecutado;
		BigDecimal[] totAnualEjecutadoAcumulado;
		BigDecimal[] totAnualVariacion;
		BigDecimal[] totAnualVariacionPorcentaje;
		BigDecimal[] totAnualDesembolsos;
		BigDecimal[] totAnualSaldo;
		
		BigDecimal totalPlanificado;
		BigDecimal totalPlanificadoAcumulado;
		BigDecimal totalEjecutado;
		BigDecimal totalEjecutadoAcumulado;
		BigDecimal totalVariacion;
		BigDecimal totalVariacionPorcentaje;
		BigDecimal totalDesembolsos;
		BigDecimal totalSaldo;

	}
	
	final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;

    public SFlujoCaja() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
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
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";

		if(accion.equals("getFlujoCaja")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			Integer anioInicial = Utils.String2Int(map.get("anioInicial"),0);
			Integer anioFinal = Utils.String2Int(map.get("anioFinal"),0);
			List<CPrestamoCostos> lstPrestamo = getFlujoCaja(idPrestamo, anioInicial, anioFinal, usuario);
			
			
			if (null != lstPrestamo && !lstPrestamo.isEmpty()){
				stTotales stTotales = getFlujoCajaTotales(lstPrestamo, anioInicial, anioFinal, usuario);
				String totales = new GsonBuilder().serializeNulls().create().toJson(stTotales);
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstPrestamo);
		        response_text = String.join("", "\"prestamo\":",response_text);
		        response_text = String.join("", response_text, ", \"totales\":",totales);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}else{
				response_text = String.join("", "{\"success\":false}");
			}
		}else if (accion.equals("exportarExcel")){
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			int anioInicio = Utils.String2Int(map.get("fechaInicio"), 0);
			int anioFin = Utils.String2Int(map.get("fechaFin"), 0);
			int agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
			
			try{
		        byte [] outArray = exportarExcel(proyectoId, anioInicio, anioFin, agrupacion, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Flujo_de_Caja.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SFlujoCaja.class, e);
			}
		}else if(accion.equals("exportarPdf")){
//			CPdf archivo = new CPdf("Flujo de Caja");
//			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
//			int anioInicio = Utils.String2Int(map.get("fechaInicio"), 0);
//			int anioFin = Utils.String2Int(map.get("fechaFin"), 0);
//			int agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
//			int tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
//			String headers[][];
//			String datosMetas[][];
//			headers = generarHeaders(anioInicio, anioFin, agrupacion, tipoVisualizacion);
//			datosMetas = generarDatosFlujoCaja(proyectoId, anioInicio, anioFin, agrupacion, tipoVisualizacion, headers[0].length, usuario);
////			String path = archivo.ExportPdfFlujoCaja(headers, datosMetas,tipoVisualizacion);
//			String path="";
//			File file=new File(path);
//			if(file.exists()){
//		        FileInputStream is = null;
//		        try {
//		        	is = new FileInputStream(file);
//		        }
//		        catch (Exception e) {
//		        	
//		        }
//		        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//		        
//		        int readByte = 0;
//		        byte[] buffer = new byte[2024];
//
//                while(true)
//                {
//                    readByte = is.read(buffer);
//                    if(readByte == -1)
//                    {
//                        break;
//                    }
//                    outByteStream.write(buffer);
//                }
//                
//                file.delete();
//                
//                is.close();
//                outByteStream.flush();
//                outByteStream.close();
//                
//		        byte [] outArray = Base64.encode(outByteStream.toByteArray());
//				response.setContentType("application/pdf");
//				response.setContentLength(outArray.length);
//				response.setHeader("Cache-Control", "no-cache");  
//				response.setHeader("Content-Disposition", "in-line; 'Flujo_de_Caja.pdf'");
//				OutputStream outStream = response.getOutputStream();
//				outStream.write(outArray);
//				outStream.flush();
//			}
			
		}
		else{
			response_text = "{ \"success\": false }";
		}

		if(!accion.equals("exportarExcel") && !accion.equals("exportarPdf")){
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}
	}
	
	private List<CPrestamoCostos> getFlujoCaja(int idPrestamo, int anioInicial, int anioFinal, String usuario){
		EstructuraProyectoDAO estructura = new EstructuraProyectoDAO();
		List<CPrestamoCostos> lstPrestamo = estructura.getEstructuraConCostos(idPrestamo, anioInicial, anioFinal, usuario);
		estructura = null;
		return lstPrestamo;
	}
	
	private stTotales getFlujoCajaTotales(List<CPrestamoCostos> lstPrestamo, int anioInicial, int anioFinal, String usuario){
		stTotales totales = new stTotales();		
//		int aniosTotales = anioFinal - anioInicial + 1; 	
//		int pos = 0;
		
		totales.filaPlanificado = new BigDecimal[12];
		totales.filaPlanificadoAcumulado = new BigDecimal[12];
		totales.filaEjecutado = new BigDecimal[12];
		totales.filaEjecutadoAcumulado = new BigDecimal[12];
		totales.filaVariacion = new BigDecimal[12];
		totales.filaVariacionPorcentaje = new BigDecimal[12];
		totales.filaDesembolsos = new BigDecimal[12];
		totales.filaSaldo = new BigDecimal[12];
		
		totales.totAnualPlanificado = new BigDecimal[1];
		totales.totAnualPlanificadoAcumulado = new BigDecimal[1];
		totales.totAnualEjecutado = new BigDecimal[1];
		totales.totAnualEjecutadoAcumulado = new BigDecimal[1];
		totales.totAnualVariacion = new BigDecimal[1];
		totales.totAnualVariacionPorcentaje = new BigDecimal[1];
		totales.totAnualDesembolsos = new BigDecimal[1];
		totales.totAnualSaldo = new BigDecimal[1];
		
		totales.totalPlanificado = new BigDecimal(0);
		totales.totalPlanificadoAcumulado = new BigDecimal(0);
		totales.totalEjecutado = new BigDecimal(0);
		totales.totalEjecutadoAcumulado = new BigDecimal(0);
		totales.totalVariacion = new BigDecimal(0);
		totales.totalVariacionPorcentaje = new BigDecimal(0);
		totales.totalDesembolsos = new BigDecimal(0);
		totales.totalSaldo = new BigDecimal(0);
		
		totales.filaDesembolsos = new BigDecimal[12];
		
		BigDecimal planificadoAcumulado = new BigDecimal(0);
		BigDecimal ejecutadoAcumulado = new BigDecimal(0);
		for(int i=0;i<lstPrestamo.size();i++){
			CPrestamoCostos prestamo = lstPrestamo.get(i);
			if(prestamo.getObjeto_tipo() == 1){				
				for (int a=0; a<prestamo.getAnios().length;a++){
					totales.totAnualPlanificado[a] = new BigDecimal(0);
					totales.totAnualPlanificadoAcumulado[a] = new BigDecimal(0);
					totales.totAnualEjecutado[a] = new BigDecimal(0);
					totales.totAnualEjecutadoAcumulado[a] = new BigDecimal(0);
					totales.totAnualVariacion[a] = new BigDecimal(0);
					totales.totAnualVariacionPorcentaje[a] = new BigDecimal(0);
					totales.totAnualDesembolsos[a] = new BigDecimal(0);
					totales.totAnualSaldo[a] = new BigDecimal(0);
					
					for(int m=0; m<12; m++){
						BigDecimal planificadoActual = prestamo.getAnios()[a].mes[m].planificado!=null ? prestamo.getAnios()[a].mes[m].planificado : new BigDecimal(0);
						totales.filaPlanificado[m] = planificadoActual;
						planificadoAcumulado = planificadoAcumulado.add(planificadoActual);
						totales.filaPlanificadoAcumulado[m] = planificadoAcumulado;
						totales.totAnualPlanificado[a] = totales.totAnualPlanificado[a].add(planificadoActual); 
						totales.totalPlanificado = totales.totalPlanificado.add(planificadoActual); 
						totales.totalPlanificadoAcumulado = planificadoAcumulado;
						BigDecimal ejecutadoActual = prestamo.getAnios()[a].mes[m].real!=null ? prestamo.getAnios()[a].mes[m].real : new BigDecimal(0);
						totales.filaEjecutado[m] = ejecutadoActual;
						ejecutadoAcumulado = ejecutadoAcumulado.add(ejecutadoActual);
						totales.filaEjecutadoAcumulado[m] = ejecutadoAcumulado;
						totales.totAnualEjecutado[a] = totales.totAnualEjecutado[a].add(ejecutadoActual); 
						totales.totalEjecutado = totales.totalEjecutado.add(ejecutadoActual); 
						totales.totalEjecutadoAcumulado = ejecutadoAcumulado;
						
						totales.filaVariacion[m] = planificadoActual.subtract(ejecutadoActual);
						totales.filaVariacionPorcentaje[m] = planificadoActual.compareTo(BigDecimal.ZERO)==0 ? new BigDecimal(0) : totales.filaVariacion[m].divide(planificadoActual);
						
					}
				}
				//TODO: obtener desembolsos y saldo
				List<?> objDesembolso =DesembolsoDAO.getDesembolsosPorEjercicio(prestamo.getObjeto_id(),anioInicial,anioFinal);
				for(int d=0; d<objDesembolso.size(); d++){
					Integer anio = (Integer) ((Object[]) objDesembolso.get(d))[0];
					Integer mes = (Integer) ((Object[]) objDesembolso.get(d))[1];
					BigDecimal valor = (BigDecimal) ((Object[]) objDesembolso.get(d))[2];
					if (anio.compareTo((anioInicial)) == 0){
						totales.filaDesembolsos[mes-1] = totales.filaDesembolsos[mes-1]!=null ? totales.filaDesembolsos[mes-1].add(valor) : valor;
					}
					totales.totalDesembolsos = totales.totalDesembolsos!=null ? totales.totalDesembolsos.add(valor) : valor; 
				}
				break;
			}
		}
		
		
		return totales;
	}
	
	private byte[] exportarExcel(int prestamoId, int anioInicio, int anioFin, int agrupacion, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datosMetas[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{		
			excel = new CExcel("Flujo de Caja", false, null);
			headers = generarHeaders(anioInicio, anioFin, agrupacion);
			datosMetas = generarDatosFlujoCaja(prestamoId, anioInicio, anioFin, agrupacion, headers[0].length, usuario);
			wb=excel.generateExcelOfData(datosMetas, "Flujo de Caja - Préstamo "+ProyectoDAO.getProyecto(prestamoId).getNombre(), headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("4", SFlujoCaja.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(int anioInicio, int anioFin, int agrupacion){
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
		totalesCant+=aniosDiferencia;
		columnasTotal += 1+totalesCant; 
		
		String titulo[] = new String[columnasTotal];
		String tipo[] = new String[columnasTotal];
		String operacionesFila[] = new String[columnasTotal];
		String columnasOperacion[] = new String[columnasTotal];
		String totales[] = new String[totalesCant];
		titulo[0]="Nombre";
		tipo[0]="string";
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
				totales[j]+=pos+",";
				pos++;
			}
		}
		for (int j=0; j<aniosDiferencia; j++){
			titulo[pos] = "Total " + (anioInicio+j);
			tipo[pos] = "double";
			operacionesFila[pos]="sum";
			columnasOperacion[pos]= totales[j];
			totales[totalesCant-1] += pos+",";
			pos++;
		}
		titulo[pos] = "Total";
		tipo[pos] = "double";
		operacionesFila[pos]="sum";
		columnasOperacion[pos]=totales[totalesCant];
		pos++;
		
		headers = new String[][]{
			titulo,  //titulos
			{""}, //mapeo
			tipo, //tipo dato
			{""}, //operaciones columnas
			{""}, //operaciones div
			null,
			operacionesFila,
			columnasOperacion
			};
			
		return headers;
	}
	
	public String[][] generarDatosFlujoCaja(int prestamoId, int anioInicio, int anioFin, int agrupacion, int columnasTotal, String usuario){
		String[][] datos = null;
		int columna = 0;
//		int sumaColumnas = ((anioFin-anioInicio) + 1);
		List<CPrestamoCostos> lstPrestamo = getFlujoCaja(prestamoId, anioInicio, anioFin, usuario);		
		if (lstPrestamo != null && !lstPrestamo.isEmpty()){
			datos = new String[lstPrestamo.size()][columnasTotal];
			for (int i=0; i<lstPrestamo.size(); i++){
				columna = 0;
				CPrestamoCostos prestamo = lstPrestamo.get(i);
				String sangria;
				switch (prestamo.getObjeto_tipo()){
					case 0: sangria = "         "; break;
					case 1: sangria = ""; break;
					case 2: sangria = "   "; break;
					case 3: sangria = "      "; break;
					case 4: sangria = "         "; break;
					case 5: sangria = "            "; break;
					default: sangria = "";
				}
				datos[i][columna] = sangria+prestamo.getNombre();
				columna++;

//				if(lstPrestamo.get(i).objeto_tipo == 0){ //es meta
//					int posicion = columna;
//					BigDecimal totalAniosP = new BigDecimal(0);
//					BigDecimal totalAniosR = new BigDecimal(0);
//					//Valores planificado-real
//					for(int a=0; a<prestamo.anios.length; a++){
//						posicion = columna + (a*factorVisualizacion);
//						
//						//Verificar nullos y volverlos 0
//						prestamo.anios[a].enero[0]=(prestamo.anios[a].enero[0]==null) ? new BigDecimal(0) : prestamo.anios[a].enero[0];
//						prestamo.anios[a].febrero[0]=(prestamo.anios[a].febrero[0]==null) ? new BigDecimal(0) : prestamo.anios[a].febrero[0];
//						prestamo.anios[a].marzo[0]=(prestamo.anios[a].marzo[0]==null) ? new BigDecimal(0) : prestamo.anios[a].marzo[0];
//						prestamo.anios[a].abril[0]=(prestamo.anios[a].abril[0]==null) ? new BigDecimal(0) : prestamo.anios[a].abril[0];
//						prestamo.anios[a].mayo[0]=(prestamo.anios[a].mayo[0]==null) ? new BigDecimal(0) : prestamo.anios[a].mayo[0];
//						prestamo.anios[a].junio[0]=(prestamo.anios[a].junio[0]==null) ? new BigDecimal(0) : prestamo.anios[a].junio[0];
//						prestamo.anios[a].julio[0]=(prestamo.anios[a].julio[0]==null) ? new BigDecimal(0) : prestamo.anios[a].julio[0];
//						prestamo.anios[a].agosto[0]=(prestamo.anios[a].agosto[0]==null) ? new BigDecimal(0) : prestamo.anios[a].agosto[0];
//						prestamo.anios[a].septiembre[0]=(prestamo.anios[a].septiembre[0]==null) ? new BigDecimal(0) : prestamo.anios[a].septiembre[0];
//						prestamo.anios[a].octubre[0]=(prestamo.anios[a].octubre[0]==null) ? new BigDecimal(0) : prestamo.anios[a].octubre[0];
//						prestamo.anios[a].noviembre[0]=(prestamo.anios[a].noviembre[0]==null) ? new BigDecimal(0) : prestamo.anios[a].noviembre[0];
//						prestamo.anios[a].diciembre[0]=(prestamo.anios[a].diciembre[0]==null) ? new BigDecimal(0) : prestamo.anios[a].diciembre[0];
//						prestamo.anios[a].enero[1]=(prestamo.anios[a].enero[1]==null) ? new BigDecimal(0) : prestamo.anios[a].enero[1];
//						prestamo.anios[a].febrero[1]=(prestamo.anios[a].febrero[1]==null) ? new BigDecimal(0) : prestamo.anios[a].febrero[1];
//						prestamo.anios[a].marzo[1]=(prestamo.anios[a].marzo[1]==null) ? new BigDecimal(0) : prestamo.anios[a].marzo[1];
//						prestamo.anios[a].abril[1]=(prestamo.anios[a].abril[1]==null) ? new BigDecimal(0) : prestamo.anios[a].abril[1];
//						prestamo.anios[a].mayo[1]=(prestamo.anios[a].mayo[1]==null) ? new BigDecimal(0) : prestamo.anios[a].mayo[1];
//						prestamo.anios[a].junio[1]=(prestamo.anios[a].junio[1]==null) ? new BigDecimal(0) : prestamo.anios[a].junio[1];
//						prestamo.anios[a].julio[1]=(prestamo.anios[a].julio[1]==null) ? new BigDecimal(0) : prestamo.anios[a].julio[1];
//						prestamo.anios[a].agosto[1]=(prestamo.anios[a].agosto[1]==null) ? new BigDecimal(0) : prestamo.anios[a].agosto[1];
//						prestamo.anios[a].septiembre[1]=(prestamo.anios[a].septiembre[1]==null) ? new BigDecimal(0) : prestamo.anios[a].septiembre[1];
//						prestamo.anios[a].octubre[1]=(prestamo.anios[a].octubre[1]==null) ? new BigDecimal(0) : prestamo.anios[a].octubre[1];
//						prestamo.anios[a].noviembre[1]=(prestamo.anios[a].noviembre[1]==null) ? new BigDecimal(0) : prestamo.anios[a].noviembre[1];
//						prestamo.anios[a].diciembre[1]=(prestamo.anios[a].diciembre[1]==null) ? new BigDecimal(0) : prestamo.anios[a].diciembre[1];
//						
//						BigDecimal totalAnualP = (prestamo.anios[a].enero[0].add(prestamo.anios[a].febrero[0]).add(prestamo.anios[a].marzo[0].add(prestamo.anios[a].abril[0].add(prestamo.anios[a].mayo[0].add(prestamo.anios[a].junio[0]))))
//								.add(prestamo.anios[a].julio[0].add(prestamo.anios[a].agosto[0]).add(prestamo.anios[a].septiembre[0].add(prestamo.anios[a].octubre[0].add(prestamo.anios[a].noviembre[0].add(prestamo.anios[a].diciembre[0]))))));
//						BigDecimal totalAnualR = (prestamo.anios[a].enero[1].add(prestamo.anios[a].febrero[1]).add(prestamo.anios[a].marzo[1].add(prestamo.anios[a].abril[1].add(prestamo.anios[a].mayo[1].add(prestamo.anios[a].junio[1]))))
//								.add(prestamo.anios[a].julio[1].add(prestamo.anios[a].agosto[1]).add(prestamo.anios[a].septiembre[1].add(prestamo.anios[a].octubre[1].add(prestamo.anios[a].noviembre[1].add(prestamo.anios[a].diciembre[1]))))));
//						totalAniosP = totalAniosP.add(totalAnualP);
//						totalAniosR = totalAniosR.add(totalAnualR);
//						switch(agrupacion){
//						case AGRUPACION_MES:
//							if(tipoVisualizacion==0 || tipoVisualizacion==2){
//								datos[i][posicion]= prestamo.anios[a].enero[0].toString();
//								datos[i][posicion+(1*sumaColumnas)]= prestamo.anios[a].febrero[0].toString();
//								datos[i][posicion+(2*sumaColumnas)]= prestamo.anios[a].marzo[0].toString();
//								datos[i][posicion+(3*sumaColumnas)]= prestamo.anios[a].abril[0].toString();
//								datos[i][posicion+(4*sumaColumnas)]= prestamo.anios[a].mayo[0].toString();
//								datos[i][posicion+(5*sumaColumnas)]= prestamo.anios[a].junio[0].toString();
//								datos[i][posicion+(6*sumaColumnas)]= prestamo.anios[a].julio[0].toString();
//								datos[i][posicion+(7*sumaColumnas)]= prestamo.anios[a].agosto[0].toString();
//								datos[i][posicion+(8*sumaColumnas)]= prestamo.anios[a].septiembre[0].toString();
//								datos[i][posicion+(9*sumaColumnas)]= prestamo.anios[a].octubre[0].toString();
//								datos[i][posicion+(10*sumaColumnas)]= prestamo.anios[a].noviembre[0].toString();
//								datos[i][posicion+(11*sumaColumnas)]= prestamo.anios[a].diciembre[0].toString();
//								datos[i][posicion+(12*sumaColumnas)]= totalAnualP.toString();
//							}
//							if(tipoVisualizacion == 2){
//								posicion++;
//							}
//							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//								datos[i][posicion]= prestamo.anios[a].enero[1].toString();
//								datos[i][posicion+(1*sumaColumnas)]= prestamo.anios[a].febrero[1].toString();
//								datos[i][posicion+(2*sumaColumnas)]= prestamo.anios[a].marzo[1].toString();
//								datos[i][posicion+(3*sumaColumnas)]= prestamo.anios[a].abril[1].toString();
//								datos[i][posicion+(4*sumaColumnas)]= prestamo.anios[a].mayo[1].toString();
//								datos[i][posicion+(5*sumaColumnas)]= prestamo.anios[a].junio[1].toString();
//								datos[i][posicion+(6*sumaColumnas)]= prestamo.anios[a].julio[1].toString();
//								datos[i][posicion+(7*sumaColumnas)]= prestamo.anios[a].agosto[1].toString();
//								datos[i][posicion+(8*sumaColumnas)]= prestamo.anios[a].septiembre[1].toString();
//								datos[i][posicion+(9*sumaColumnas)]= prestamo.anios[a].octubre[1].toString();
//								datos[i][posicion+(10*sumaColumnas)]= prestamo.anios[a].noviembre[1].toString();
//								datos[i][posicion+(11*sumaColumnas)]= prestamo.anios[a].diciembre[1].toString();
//								datos[i][posicion+(12*sumaColumnas)]= totalAnualR.toString();
//							}
//							posicion = posicion+(12*sumaColumnas)+1;
//							break;
//						case AGRUPACION_BIMESTRE:
//							if(tipoVisualizacion==0 || tipoVisualizacion==2){
//								datos[i][posicion]= (prestamo.anios[a].enero[0].add(prestamo.anios[a].febrero[0])).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].marzo[0].add(prestamo.anios[a].abril[0])).toString();
//								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mayo[0].add(prestamo.anios[a].junio[0])).toString();
//								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].julio[0].add(prestamo.anios[a].agosto[0])).toString();
//								datos[i][posicion+(4*sumaColumnas)]= (prestamo.anios[a].septiembre[0].add(prestamo.anios[a].octubre[0])).toString();
//								datos[i][posicion+(5*sumaColumnas)]= (prestamo.anios[a].noviembre[0].add(prestamo.anios[a].diciembre[0])).toString();
//								datos[i][posicion+(6*sumaColumnas)]= totalAnualP.toString();
//							}
//							if(tipoVisualizacion == 2){
//								posicion++;
//							}
//							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//								datos[i][posicion]= (prestamo.anios[a].enero[1].add(prestamo.anios[a].febrero[1])).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].marzo[1].add(prestamo.anios[a].abril[1])).toString();
//								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mayo[1].add(prestamo.anios[a].junio[1])).toString();
//								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].julio[1].add(prestamo.anios[a].agosto[1])).toString();
//								datos[i][posicion+(4*sumaColumnas)]= (prestamo.anios[a].septiembre[1].add(prestamo.anios[a].octubre[1])).toString();
//								datos[i][posicion+(5*sumaColumnas)]= (prestamo.anios[a].noviembre[1].add(prestamo.anios[a].diciembre[1])).toString();
//								datos[i][posicion+(6*sumaColumnas)]= totalAnualR.toString();
//							}
//							posicion = posicion+(6*sumaColumnas)+1;
//							break;
//						case AGRUPACION_TRIMESTRE:
//							if(tipoVisualizacion==0 || tipoVisualizacion==2){
//								datos[i][posicion]= (prestamo.anios[a].enero[0].add(prestamo.anios[a].febrero[0].add(prestamo.anios[a].marzo[0]))).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].abril[0].add(prestamo.anios[a].mayo[0].add(prestamo.anios[a].junio[0]))).toString();
//								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].julio[0].add(prestamo.anios[a].agosto[0].add(prestamo.anios[a].septiembre[0]))).toString();
//								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].octubre[0].add(prestamo.anios[a].noviembre[0].add(prestamo.anios[a].diciembre[0]))).toString();
//								datos[i][posicion+(4*sumaColumnas)]= totalAnualP.toString();
//							}
//							if(tipoVisualizacion == 2){
//								posicion++;
//							}
//							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//								datos[i][posicion]= (prestamo.anios[a].enero[1].add(prestamo.anios[a].febrero[1].add(prestamo.anios[a].marzo[1]))).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].abril[1].add(prestamo.anios[a].mayo[1].add(prestamo.anios[a].junio[1]))).toString();
//								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].julio[1].add(prestamo.anios[a].agosto[1].add(prestamo.anios[a].septiembre[1]))).toString();
//								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].octubre[1].add(prestamo.anios[a].noviembre[1].add(prestamo.anios[a].diciembre[1]))).toString();
//								datos[i][posicion+(4*sumaColumnas)]= totalAnualR.toString();
//							}
//							posicion = posicion+(4*sumaColumnas)+1;
//							break;
//						case AGRUPACION_CUATRIMESTRE:
//							if(tipoVisualizacion==0 || tipoVisualizacion==2){
//								datos[i][posicion]= (prestamo.anios[a].enero[0].add(prestamo.anios[a].febrero[0]).add(prestamo.anios[a].marzo[0].add(prestamo.anios[a].abril[0]))).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mayo[0]).add(prestamo.anios[a].junio[0].add(prestamo.anios[a].julio[0].add(prestamo.anios[a].agosto[0]))).toString();
//								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].septiembre[0].add(prestamo.anios[a].octubre[0]).add(prestamo.anios[a].noviembre[0].add(prestamo.anios[a].diciembre[0]))).toString();
//								datos[i][posicion+(3*sumaColumnas)]= totalAnualP.toString();
//							}
//							if(tipoVisualizacion == 2){
//								posicion++;
//							}
//							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//								datos[i][posicion]= (prestamo.anios[a].enero[1].add(prestamo.anios[a].febrero[1]).add(prestamo.anios[a].marzo[1].add(prestamo.anios[a].abril[1]))).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mayo[1]).add(prestamo.anios[a].junio[1].add(prestamo.anios[a].julio[1].add(prestamo.anios[a].agosto[1]))).toString();
//								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].septiembre[1].add(prestamo.anios[a].octubre[1]).add(prestamo.anios[a].noviembre[1].add(prestamo.anios[a].diciembre[1]))).toString();
//								datos[i][posicion+(3*sumaColumnas)]= totalAnualR.toString();
//							}
//							posicion = posicion+(3*sumaColumnas)+1;
//							break;
//						case AGRUPACION_SEMESTRE:
//							if(tipoVisualizacion==0 || tipoVisualizacion==2){
//								datos[i][posicion]= (prestamo.anios[a].enero[0].add(prestamo.anios[a].febrero[0]).add(prestamo.anios[a].marzo[0].add(prestamo.anios[a].abril[0].add(prestamo.anios[a].mayo[0].add(prestamo.anios[a].junio[0]))))).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].julio[0].add(prestamo.anios[a].agosto[0]).add(prestamo.anios[a].septiembre[0].add(prestamo.anios[a].octubre[0].add(prestamo.anios[a].noviembre[0].add(prestamo.anios[a].diciembre[0]))))).toString();
//								datos[i][posicion+(2*sumaColumnas)]= totalAnualP.toString();
//							}
//							if(tipoVisualizacion == 2){
//								posicion++;
//							}
//							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//								datos[i][posicion]= (prestamo.anios[a].enero[1].add(prestamo.anios[a].febrero[1]).add(prestamo.anios[a].marzo[1].add(prestamo.anios[a].abril[1].add(prestamo.anios[a].mayo[1].add(prestamo.anios[a].junio[1]))))).toString();
//								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].julio[1].add(prestamo.anios[a].agosto[1]).add(prestamo.anios[a].septiembre[1].add(prestamo.anios[a].octubre[1].add(prestamo.anios[a].noviembre[1].add(prestamo.anios[a].diciembre[1]))))).toString();
//								datos[i][posicion+(2*sumaColumnas)]= totalAnualR.toString();
//							}
//							posicion = posicion+(2*sumaColumnas)+1;
//							break;
//						case AGRUPACION_ANUAL:
//							if(tipoVisualizacion==0 || tipoVisualizacion==2){
//								datos[i][posicion]= totalAnualP.toString();
//								datos[i][posicion+(1*sumaColumnas)]= totalAnualP.toString();
//							}
//							if(tipoVisualizacion == 2){
//								posicion++;
//							}
//							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//								datos[i][posicion]= totalAnualR.toString();
//								datos[i][posicion+(1*sumaColumnas)]= totalAnualR.toString();
//							}
//							posicion = posicion+(1*sumaColumnas)+1;
//							break;
//						}
//					}
//					if(tipoVisualizacion==0 || tipoVisualizacion==2){
//						datos[i][posicion]= totalAniosP.toString();
//					}
//					if(tipoVisualizacion == 2){
//						posicion++;
//					}
//					if(tipoVisualizacion==1 || tipoVisualizacion == 2){
//						datos[i][posicion]= totalAniosR.toString();
//					}
//					datos[i][columnasTotal-2]=prestamo.metaFinal!=null ? prestamo.metaFinal.toString() : "";
//					datos[i][columnasTotal-1]=prestamo.porcentajeAvance!=null ? prestamo.porcentajeAvance.toString() : "";
//				}
			}
		}
		return datos;
	}
	
}