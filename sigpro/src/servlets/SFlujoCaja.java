package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.DesembolsoDAO;
import dao.DesembolsoReal;
import dao.ObjetoCosto;
import dao.ObjetoDAO;
import dao.PrestamoDAO;
import dao.ProyectoDAO;
import pojo.Prestamo;
import utilities.CExcel;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SFlujoCaja")
public class SFlujoCaja extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stTotales{
		BigDecimal[] filaPlanificado = new BigDecimal[12];
		BigDecimal[] filaPlanificadoAcumulado = new BigDecimal[12];
		BigDecimal[] filaEjecutado = new BigDecimal[12];
		BigDecimal[] filaEjecutadoAcumulado = new BigDecimal[12];
		BigDecimal[] filaVariacion = new BigDecimal[12];
		BigDecimal[] filaVariacionPorcentaje = new BigDecimal[12];
		BigDecimal[] filaDesembolsos = new BigDecimal[12];
		BigDecimal[] filaDesembolsosReal = new BigDecimal[12];
		BigDecimal[] filaSaldoCuenta = new BigDecimal[12];
		BigDecimal[] filaAnticipos = new BigDecimal[12];
		BigDecimal[] filaSaldo = new BigDecimal[12];
				
		BigDecimal totalPlanificado = new BigDecimal(0);
		BigDecimal totalPlanificadoAcumulado = new BigDecimal(0);
		BigDecimal totalEjecutado = new BigDecimal(0);
		BigDecimal totalEjecutadoAcumulado = new BigDecimal(0);
		BigDecimal totalVariacion = new BigDecimal(0);
		BigDecimal totalVariacionPorcentaje = new BigDecimal(0);
		BigDecimal totalDesembolsosReal = new BigDecimal(0);
		BigDecimal totalDesembolsos = new BigDecimal(0);
		BigDecimal totalSaldoCuenta = new BigDecimal(0);
		BigDecimal totalAnticipos = new BigDecimal(0);
		BigDecimal totalSaldo = new BigDecimal(0);

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
			try{
				Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
				Integer idProyecto = Utils.String2Int(map.get("idProyecto"),0);
				Date fechaCorte = Utils.dateFromString(map.get("fechaCorte"));
				String lineaBase = map.get("lineaBase");
				List<ObjetoCosto> lstPrestamo = getFlujoCaja(idPrestamo, idProyecto, fechaCorte, lineaBase, usuario);
				
				if (null != lstPrestamo && !lstPrestamo.isEmpty()){
					stTotales stTotales = getFlujoCajaTotales(idPrestamo, lstPrestamo, fechaCorte, lineaBase, usuario);
					String totales = new GsonBuilder().serializeNulls().create().toJson(stTotales);
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstPrestamo);
			        response_text = String.join("", "\"prestamo\":",response_text);
			        response_text = String.join("", response_text, ", \"totales\":",totales);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}else{
					response_text = String.join("", "{\"success\":false}");
				}
			}catch(Exception e){
				CLogger.write("1", SFlujoCaja.class, e);
			}
		}else if (accion.equals("exportarExcel")){
			int prestamoId = Utils.String2Int(map.get("prestamoid"), 0);
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			Date fechaCorte = Utils.dateFromString(map.get("fechaCorte"));
			int agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
			
			try{
				String lineaBase = map.get("lineaBase");
				byte [] outArray = exportarExcel(prestamoId, proyectoId, fechaCorte, agrupacion, lineaBase, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Flujo_de_Caja.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("2", SFlujoCaja.class, e);
			}
		}else if(accion.equals("exportarPdf")){
			try{
				int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
				int prestamoId = Utils.String2Int(map.get("prestamoid"), 0);
				Date fechaCorte = Utils.dateFromString(map.get("fechaCorte"));
				int agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
	
				CPdf archivo = new CPdf("Flujo de Caja");
				String headers[][];
				String datos[][];
				headers = generarHeaders(fechaCorte, agrupacion);
				String lineaBase = map.get("lineaBase");
				datos = generarDatosFlujoCaja(prestamoId, proyectoId, fechaCorte, agrupacion, headers[0].length, lineaBase, usuario);
				String path = archivo.ExportarPdfFlujoCaja(headers, datos, usuario);
				File file=new File(path);
				if(file.exists()){
			        FileInputStream is = null;
		        	is = new FileInputStream(file);
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
					response.setHeader("Content-Disposition", "in-line; 'Flujo_Caja.pdf'");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();
				}

			}catch(Exception e){
				CLogger.write("4", SFlujoCaja.class, e);
			}
			
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
	
	private List<ObjetoCosto> getFlujoCaja(int idPrestamo, int idProyecto, Date fechaCorte, String lineaBase, String usuario) throws SQLException{
		DateTime fecha = new DateTime(fechaCorte);
		Integer anio = fecha.getYear();
		List<ObjetoCosto> lstPrestamo = ObjetoDAO.getEstructuraConCosto(idProyecto, anio, anio, true, true, false, lineaBase, usuario);
		return lstPrestamo;
	}
	
	private stTotales getFlujoCajaTotales(Integer prestamoId, List<ObjetoCosto> lstPrestamo, Date fechaCorte, String lineaBase, String usuario) throws SQLException{
		stTotales totales = new stTotales();		
		if(CMariaDB.connectAnalytic()){
			Connection conn_analytic = CMariaDB.getConnection_analytic();
			BigDecimal planificadoAcumulado = new BigDecimal(0);
			BigDecimal ejecutadoAcumulado = new BigDecimal(0);
			for(int i=0;i<lstPrestamo.size();i++){
				ObjetoCosto prestamo = lstPrestamo.get(i);
				if(prestamo.getObjeto_tipo() == 0){				
						for(int m=0; m<12; m++){
							BigDecimal planificadoActual = prestamo.getAnios()[0].mes[m].planificado!=null ? prestamo.getAnios()[0].mes[m].planificado : new BigDecimal(0);
							totales.filaPlanificado[m] = planificadoActual;
							planificadoAcumulado = planificadoAcumulado.add(planificadoActual);
							totales.filaPlanificadoAcumulado[m] = planificadoAcumulado;
							totales.totalPlanificado = totales.totalPlanificado.add(planificadoActual); 
							totales.totalPlanificadoAcumulado = planificadoAcumulado;
							BigDecimal ejecutadoActual = prestamo.getAnios()[0].mes[m].real!=null ? prestamo.getAnios()[0].mes[m].real : new BigDecimal(0);
							totales.filaEjecutado[m] = ejecutadoActual;
							ejecutadoAcumulado = ejecutadoAcumulado.add(ejecutadoActual);
							totales.filaEjecutadoAcumulado[m] = ejecutadoAcumulado;
							totales.totalEjecutado = totales.totalEjecutado.add(ejecutadoActual); 
							totales.totalEjecutadoAcumulado = ejecutadoAcumulado;
							totales.filaVariacion[m] = planificadoActual.subtract(ejecutadoActual);
							totales.filaVariacionPorcentaje[m] = (planificadoActual.compareTo(BigDecimal.ZERO)==0 || totales.filaVariacion[m]==null) ? new BigDecimal(0) : totales.filaVariacion[m].divide(planificadoActual, 2, BigDecimal.ROUND_HALF_UP);
							totales.totalVariacion = totales.totalVariacion.add(totales.filaVariacion[m]);
							totales.filaSaldoCuenta[m] = null;
							totales.filaAnticipos[m] = null;
							totales.totalSaldoCuenta = null;
							totales.totalAnticipos = null;
						}
	
					DateTime fecha = new DateTime(fechaCorte);
					Integer anioFinal = fecha.getYear();
					Integer mesFinal = fecha.getMonthOfYear();
					Date fechaInicial = Utils.dateFromString("01/01/"+ anioFinal);
					List<?> objDesembolso =DesembolsoDAO.getDesembolsosEntreFechas(prestamo.getObjeto_id(),fechaInicial,fechaCorte, lineaBase);
					for(int d=0; d<objDesembolso.size(); d++){
						Integer anio = (Integer) ((Object[]) objDesembolso.get(d))[0];
						Integer mes = (Integer) ((Object[]) objDesembolso.get(d))[1];
						if (anio.compareTo((anioFinal)) == 0){
							BigDecimal valor = (BigDecimal) ((Object[]) objDesembolso.get(d))[2];
							totales.filaDesembolsos[mes-1] = totales.filaDesembolsos[mes-1]!=null ? totales.filaDesembolsos[mes-1].add(valor) : valor;
							totales.totalDesembolsos = totales.totalDesembolsos!=null ? totales.totalDesembolsos.add(valor) : valor;
						} 
					}
					
					Prestamo prestamoObj = PrestamoDAO.getObjetoPrestamoPorId(prestamoId);
					
					if(prestamoObj!=null){						
							ArrayList<DesembolsoReal> desembolsosReal = DesembolsoDAO.getDesembolsosReales(prestamoObj.getCodigoPresupuestario(), 1, anioFinal, mesFinal, anioFinal, conn_analytic);
							for(int d=0; d<desembolsosReal.size();d++){
								DesembolsoReal desembolso = desembolsosReal.get(d); 
								if (desembolso.getEjercicioFiscal().compareTo((anioFinal)) == 0){
									BigDecimal valor = desembolso.getDesembolsosMesGTQ();
									Integer mes = desembolso.getMes();
									totales.filaDesembolsosReal[mes-1] = totales.filaDesembolsosReal[mes-1]!=null ? totales.filaDesembolsosReal[mes-1].add(valor) : valor;
									totales.totalDesembolsosReal = totales.totalDesembolsosReal!=null ? totales.totalDesembolsosReal.add(valor) : valor;
								} 
							}
					}
					
					//Calculo primer mes diferente
					totales.filaDesembolsosReal[0] = totales.filaDesembolsosReal[0]!=null ? totales.filaDesembolsosReal[0] : new BigDecimal(0);
					totales.filaSaldo[0] = totales.filaDesembolsosReal[0].add(totales.filaDesembolsos[0]!=null ? totales.filaDesembolsos[0] : new BigDecimal(0));
					totales.filaSaldo[0] = totales.filaSaldo[0].subtract(totales.filaEjecutado[0]!=null ? totales.filaEjecutado[0] : new BigDecimal(0));
					totales.totalSaldo = totales.filaSaldo[0]; 
					for(int mes=1; mes<mesFinal; mes++){
						totales.filaSaldo[mes-1] = totales.filaSaldo[mes-1]!=null ? totales.filaSaldo[mes-1] : new BigDecimal(0);
						totales.filaSaldo[mes] = totales.filaSaldo[mes-1].add(totales.filaDesembolsos[mes]!=null ? totales.filaDesembolsos[mes] : new BigDecimal(0));
						totales.filaSaldo[mes] = totales.filaSaldo[mes].subtract(totales.filaEjecutado[mes]!=null ? totales.filaEjecutado[mes] : new BigDecimal(0));
						totales.totalSaldo = totales.totalSaldo.add(totales.filaSaldo[mes]);
					}
					for(int mes=mesFinal; mes<12; mes++){
						totales.filaSaldo[mes-1] = totales.filaSaldo[mes-1]!=null ? totales.filaSaldo[mes-1] : new BigDecimal(0);
						totales.filaSaldo[mes] = totales.filaSaldo[mes-1].add(totales.filaDesembolsos[mes]!=null ? totales.filaDesembolsos[mes] : new BigDecimal(0));
						totales.filaSaldo[mes] = totales.filaSaldo[mes].subtract(totales.filaPlanificado[mes]!=null ? totales.filaPlanificado[mes] : new BigDecimal(0));
						totales.totalSaldo = totales.totalSaldo.add(totales.filaSaldo[mes]);
					}
					break;
				}
			}
			conn_analytic.close();
		}
		return totales;
	}
	
	private byte[] exportarExcel(int prestamoId, int proyectoId, Date fechaCorte, int agrupacion, String lineaBase, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{		
			excel = new CExcel("Flujo de Caja", false, null);
			headers = generarHeaders(fechaCorte, agrupacion);
			datos = generarDatosFlujoCaja(prestamoId, proyectoId, fechaCorte, agrupacion, headers[0].length, lineaBase, usuario);
			wb=excel.generateExcelOfData(datos, "Flujo de Caja - "+ProyectoDAO.getProyecto(prestamoId).getNombre(), headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("4", SFlujoCaja.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(Date fechaCorte, int agrupacion){
		String headers[][];
		String[][] AgrupacionesTitulo = new String[][]{{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"},
			{"Bimestre 1", "Bimestre 2","Bimestre 3","Bimestre 4","Bimestre 5","Bimestre 6"},
			{"Trimestre 1", "Trimestre 2", "Trimestre 3", "Trimestre 4"},
			{"Cuatrimestre 1", "Cuatrimestre 2", "Cuatrimestre 3"},
			{"Semestre 1","Semestre 2"},
			{"Anual"}
		};
		
		DateTime fecha = new DateTime(fechaCorte);
		Integer anioInicio = fecha.getYear();
		int totalesCant = 1;
		int aniosDiferencia =1; 
		int columnasTotal = (aniosDiferencia*(AgrupacionesTitulo[agrupacion-1].length));
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
		titulo[pos] = "Total";
		tipo[pos] = "double";
		operacionesFila[pos]="sum";
		columnasOperacion[pos]=totales[0];
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
	
	public String[][] generarDatosFlujoCaja(int prestamoId, int proyectoId, Date fechaCorte, int agrupacion, int columnasTotal, String lineaBase, String usuario) throws SQLException{
		String[][] datos = null;
		int columna = 0;
		List<ObjetoCosto> lstPrestamo = getFlujoCaja(prestamoId, proyectoId, fechaCorte, lineaBase, usuario);
		if (lstPrestamo != null && !lstPrestamo.isEmpty()){
			stTotales stTotales = getFlujoCajaTotales(prestamoId, lstPrestamo, fechaCorte, lineaBase, usuario);
			datos = new String[lstPrestamo.size()+9][columnasTotal];
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
				BigDecimal total = new BigDecimal(0);
				//Valores planificado-real
				for(int a=0; a<prestamo.getAnios().length; a++){
					ObjetoCosto.stanio anio = prestamo.getAnios()[a];
					//Verificar nullos y volverlos 0
					for(int m=0; m<12; m++){
						anio.mes[m].planificado = anio.mes[m].planificado!=null ? anio.mes[m].planificado : new BigDecimal(0);
						anio.mes[m].real = anio.mes[m].real!=null ? anio.mes[m].real : new BigDecimal(0);
						total = total.add(anio.mes[m].planificado);
					}
					
					switch(agrupacion){
					case AGRUPACION_MES:
						for(int m=0; m<12; m++){
							datos[i][posicion+m]= anio.mes[m].planificado.toString();
						}
						datos[i][posicion+12]= total.toString();
						break;
					case AGRUPACION_BIMESTRE:
						datos[i][posicion+0]= (anio.mes[0].planificado.add(anio.mes[1].planificado)).toString();
						datos[i][posicion+1]= (anio.mes[2].planificado.add(anio.mes[3].planificado)).toString();
						datos[i][posicion+2]= (anio.mes[4].planificado.add(anio.mes[5].planificado)).toString();
						datos[i][posicion+3]= (anio.mes[6].planificado.add(anio.mes[7].planificado)).toString();
						datos[i][posicion+4]= (anio.mes[8].planificado.add(anio.mes[9].planificado)).toString();
						datos[i][posicion+5]= (anio.mes[10].planificado.add(anio.mes[11].planificado)).toString();
						datos[i][posicion+6]= total.toString();
						break;
					case AGRUPACION_TRIMESTRE:
						datos[i][posicion+0]= (anio.mes[0].planificado.add(anio.mes[1].planificado).add(anio.mes[2].planificado)).toString();
						datos[i][posicion+1]= (anio.mes[3].planificado.add(anio.mes[4].planificado).add(anio.mes[5].planificado)).toString();
						datos[i][posicion+2]= (anio.mes[6].planificado.add(anio.mes[7].planificado).add(anio.mes[8].planificado)).toString();
						datos[i][posicion+3]= (anio.mes[9].planificado.add(anio.mes[10].planificado).add(anio.mes[11].planificado)).toString();
						datos[i][posicion+4]= total.toString();
						break;
					case AGRUPACION_CUATRIMESTRE:
						datos[i][posicion+0]= (anio.mes[0].planificado.add(anio.mes[1].planificado).add(anio.mes[2].planificado).add(anio.mes[3].planificado)).toString();
						datos[i][posicion+1]= (anio.mes[4].planificado.add(anio.mes[5].planificado).add(anio.mes[6].planificado).add(anio.mes[7].planificado)).toString();
						datos[i][posicion+2]= (anio.mes[8].planificado.add(anio.mes[9].planificado).add(anio.mes[10].planificado).add(anio.mes[11].planificado)).toString();
						datos[i][posicion+3]= total.toString();
						break;
					case AGRUPACION_SEMESTRE:
						datos[i][posicion+0]= (anio.mes[0].planificado.add(anio.mes[1].planificado).add(anio.mes[2].planificado).add(anio.mes[3].planificado).add(anio.mes[4].planificado).add(anio.mes[5].planificado)).toString();
						datos[i][posicion+1]= (anio.mes[6].planificado.add(anio.mes[7].planificado).add(anio.mes[8].planificado).add(anio.mes[9].planificado).add(anio.mes[10].planificado).add(anio.mes[11].planificado)).toString();
						datos[i][posicion+2]= total.toString();
						break;
					case AGRUPACION_ANUAL:
						datos[i][posicion+0]= total.toString();
						datos[i][posicion+1]= total.toString();
						break;
					}
				}
			}
			
			//Inicializar desembolsos
			if(agrupacion!=AGRUPACION_MES){
				for(int m=0; m<12; m++){
					stTotales.filaDesembolsos[m] = stTotales.filaDesembolsos[m]!=null ? stTotales.filaDesembolsos[m] : new BigDecimal(0);
					stTotales.filaDesembolsosReal[m] = stTotales.filaDesembolsosReal[m]!=null ? stTotales.filaDesembolsosReal[m] : new BigDecimal(0);
				}
			}

			int fila = lstPrestamo.size();
			switch(agrupacion){
			case AGRUPACION_MES:
				datos[fila][0] = "Total Planificado";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaPlanificado[m].toString();
				}
				datos[fila][13] = stTotales.totalPlanificado.toString();
				fila++; 
				datos[fila][0] = "Total Planificado Acumulado";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaPlanificadoAcumulado[m].toString();
				}
				datos[fila][13] = stTotales.totalPlanificadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaEjecutado[m].toString();
				}
				datos[fila][13] = stTotales.totalEjecutado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado Acumulado";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaEjecutadoAcumulado[m].toString();
				}
				datos[fila][13] = stTotales.totalEjecutadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Variacion";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaVariacion[m].toString();
				}
				datos[fila][13] = stTotales.totalVariacion.toString();
				fila++; 
				datos[fila][0] = "%";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaVariacionPorcentaje[m].toString();
				}
				datos[fila][13] = stTotales.totalVariacionPorcentaje.toString();
				fila++; 
				datos[fila][0] = "Desembolsos Ejecutados";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaDesembolsosReal[m]!=null ? stTotales.filaDesembolsosReal[m].toString() : "";
				}
				datos[fila][13] = stTotales.totalDesembolsosReal.toString();
				fila++; 
				datos[fila][0] = "Desembolsos";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaDesembolsos[m]!=null ? stTotales.filaDesembolsos[m].toString() : "";
				}
				datos[fila][13] = stTotales.totalDesembolsos.toString();
				fila++; 
				datos[fila][0] = "Saldo";
				for(int m=0; m<12; m++){
					datos[fila][1+m] = stTotales.filaSaldo[m].toString();
				}
				datos[fila][13] = stTotales.totalSaldo.toString();
				break;
			case AGRUPACION_BIMESTRE:
				datos[fila][0] = "Total Planificado";
				datos[fila][1] = (stTotales.filaPlanificado[0].add(stTotales.filaPlanificado[1])).toString();
				datos[fila][2] = (stTotales.filaPlanificado[2].add(stTotales.filaPlanificado[3])).toString();
				datos[fila][3] = (stTotales.filaPlanificado[4].add(stTotales.filaPlanificado[5])).toString();
				datos[fila][4] = (stTotales.filaPlanificado[6].add(stTotales.filaPlanificado[7])).toString();
				datos[fila][5] = (stTotales.filaPlanificado[8].add(stTotales.filaPlanificado[9])).toString();
				datos[fila][6] = (stTotales.filaPlanificado[10].add(stTotales.filaPlanificado[11])).toString();
				datos[fila][7] = stTotales.totalPlanificado.toString();
				fila++; 
				datos[fila][0] = "Total Planificado Acumulado";
				datos[fila][1] = (stTotales.filaPlanificadoAcumulado[0].add(stTotales.filaPlanificadoAcumulado[1])).toString();
				datos[fila][2] = (stTotales.filaPlanificadoAcumulado[2].add(stTotales.filaPlanificadoAcumulado[3])).toString();
				datos[fila][3] = (stTotales.filaPlanificadoAcumulado[4].add(stTotales.filaPlanificadoAcumulado[5])).toString();
				datos[fila][4] = (stTotales.filaPlanificadoAcumulado[6].add(stTotales.filaPlanificadoAcumulado[7])).toString();
				datos[fila][5] = (stTotales.filaPlanificadoAcumulado[8].add(stTotales.filaPlanificadoAcumulado[9])).toString();
				datos[fila][6] = (stTotales.filaPlanificadoAcumulado[10].add(stTotales.filaPlanificadoAcumulado[11])).toString();
				datos[fila][7] = stTotales.totalPlanificadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado";
				datos[fila][1] = (stTotales.filaEjecutado[0].add(stTotales.filaEjecutado[1])).toString();
				datos[fila][2] = (stTotales.filaEjecutado[2].add(stTotales.filaEjecutado[3])).toString();
				datos[fila][3] = (stTotales.filaEjecutado[4].add(stTotales.filaEjecutado[5])).toString();
				datos[fila][4] = (stTotales.filaEjecutado[6].add(stTotales.filaEjecutado[7])).toString();
				datos[fila][5] = (stTotales.filaEjecutado[8].add(stTotales.filaEjecutado[9])).toString();
				datos[fila][6] = (stTotales.filaEjecutado[10].add(stTotales.filaEjecutado[11])).toString();
				datos[fila][7] = stTotales.totalEjecutado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado Acumulado";
				datos[fila][1] = (stTotales.filaEjecutadoAcumulado[0].add(stTotales.filaEjecutadoAcumulado[1])).toString();
				datos[fila][2] = (stTotales.filaEjecutadoAcumulado[2].add(stTotales.filaEjecutadoAcumulado[3])).toString();
				datos[fila][3] = (stTotales.filaEjecutadoAcumulado[4].add(stTotales.filaEjecutadoAcumulado[5])).toString();
				datos[fila][4] = (stTotales.filaEjecutadoAcumulado[6].add(stTotales.filaEjecutadoAcumulado[7])).toString();
				datos[fila][5] = (stTotales.filaEjecutadoAcumulado[8].add(stTotales.filaEjecutadoAcumulado[9])).toString();
				datos[fila][6] = (stTotales.filaEjecutadoAcumulado[10].add(stTotales.filaEjecutadoAcumulado[11])).toString();
				datos[fila][7] = stTotales.totalEjecutadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Variacion";
				datos[fila][1] = (stTotales.filaVariacion[0].add(stTotales.filaVariacion[1])).toString();
				datos[fila][2] = (stTotales.filaVariacion[2].add(stTotales.filaVariacion[3])).toString();
				datos[fila][3] = (stTotales.filaVariacion[4].add(stTotales.filaVariacion[5])).toString();
				datos[fila][4] = (stTotales.filaVariacion[6].add(stTotales.filaVariacion[7])).toString();
				datos[fila][5] = (stTotales.filaVariacion[8].add(stTotales.filaVariacion[9])).toString();
				datos[fila][6] = (stTotales.filaVariacion[10].add(stTotales.filaVariacion[11])).toString();
				datos[fila][7] = stTotales.totalVariacion.toString();
				fila++; 
				datos[fila][0] = "%";
				datos[fila][1] = (stTotales.filaVariacionPorcentaje[0].add(stTotales.filaVariacionPorcentaje[1])).toString();
				datos[fila][2] = (stTotales.filaVariacionPorcentaje[2].add(stTotales.filaVariacionPorcentaje[3])).toString();
				datos[fila][3] = (stTotales.filaVariacionPorcentaje[4].add(stTotales.filaVariacionPorcentaje[5])).toString();
				datos[fila][4] = (stTotales.filaVariacionPorcentaje[6].add(stTotales.filaVariacionPorcentaje[7])).toString();
				datos[fila][5] = (stTotales.filaVariacionPorcentaje[8].add(stTotales.filaVariacionPorcentaje[9])).toString();
				datos[fila][6] = (stTotales.filaVariacionPorcentaje[10].add(stTotales.filaVariacionPorcentaje[11])).toString();
				datos[fila][7] = stTotales.totalVariacionPorcentaje.toString();
				fila++; 
				datos[fila][0] = "Desembolsos Ejecutados";
				datos[fila][1] = (stTotales.filaDesembolsosReal[0].add(stTotales.filaDesembolsosReal[1])).toString();
				datos[fila][2] = (stTotales.filaDesembolsosReal[2].add(stTotales.filaDesembolsosReal[3])).toString();
				datos[fila][3] = (stTotales.filaDesembolsosReal[4].add(stTotales.filaDesembolsosReal[5])).toString();
				datos[fila][4] = (stTotales.filaDesembolsosReal[6].add(stTotales.filaDesembolsosReal[7])).toString();
				datos[fila][5] = (stTotales.filaDesembolsosReal[8].add(stTotales.filaDesembolsosReal[9])).toString();
				datos[fila][6] = (stTotales.filaDesembolsosReal[10].add(stTotales.filaDesembolsosReal[11])).toString();
				datos[fila][7] = stTotales.totalDesembolsosReal.toString();
				fila++; 
				datos[fila][0] = "Desembolsos";
				datos[fila][1] = (stTotales.filaDesembolsos[0].add(stTotales.filaDesembolsos[1])).toString();
				datos[fila][2] = (stTotales.filaDesembolsos[2].add(stTotales.filaDesembolsos[3])).toString();
				datos[fila][3] = (stTotales.filaDesembolsos[4].add(stTotales.filaDesembolsos[5])).toString();
				datos[fila][4] = (stTotales.filaDesembolsos[6].add(stTotales.filaDesembolsos[7])).toString();
				datos[fila][5] = (stTotales.filaDesembolsos[8].add(stTotales.filaDesembolsos[9])).toString();
				datos[fila][6] = (stTotales.filaDesembolsos[10].add(stTotales.filaDesembolsos[11])).toString();
				datos[fila][7] = stTotales.totalDesembolsos.toString();
				fila++; 
				datos[fila][0] = "Saldo";
				datos[fila][1] = (stTotales.filaSaldo[0].add(stTotales.filaSaldo[1])).toString();
				datos[fila][2] = (stTotales.filaSaldo[2].add(stTotales.filaSaldo[3])).toString();
				datos[fila][3] = (stTotales.filaSaldo[4].add(stTotales.filaSaldo[5])).toString();
				datos[fila][4] = (stTotales.filaSaldo[6].add(stTotales.filaSaldo[7])).toString();
				datos[fila][5] = (stTotales.filaSaldo[8].add(stTotales.filaSaldo[9])).toString();
				datos[fila][6] = (stTotales.filaSaldo[10].add(stTotales.filaSaldo[11])).toString();
				datos[fila][7] = stTotales.totalSaldo.toString();
				break;
			case AGRUPACION_TRIMESTRE:
				datos[fila][0] = "Total Planificado";
				datos[fila][1] = (stTotales.filaPlanificado[0].add(stTotales.filaPlanificado[1]).add(stTotales.filaPlanificado[2])).toString();
				datos[fila][2] = (stTotales.filaPlanificado[3].add(stTotales.filaPlanificado[4]).add(stTotales.filaPlanificado[5])).toString();
				datos[fila][3] = (stTotales.filaPlanificado[6].add(stTotales.filaPlanificado[7]).add(stTotales.filaPlanificado[8])).toString();
				datos[fila][4] = (stTotales.filaPlanificado[9].add(stTotales.filaPlanificado[10]).add(stTotales.filaPlanificado[11])).toString();
				datos[fila][5] = stTotales.totalPlanificado.toString();
				fila++; 
				datos[fila][0] = "Total Planificado Acumulado";
				datos[fila][1] = (stTotales.filaPlanificadoAcumulado[0].add(stTotales.filaPlanificadoAcumulado[1]).add(stTotales.filaPlanificadoAcumulado[2])).toString();
				datos[fila][2] = (stTotales.filaPlanificadoAcumulado[3].add(stTotales.filaPlanificadoAcumulado[4]).add(stTotales.filaPlanificadoAcumulado[5])).toString();
				datos[fila][3] = (stTotales.filaPlanificadoAcumulado[6].add(stTotales.filaPlanificadoAcumulado[7]).add(stTotales.filaPlanificadoAcumulado[8])).toString();
				datos[fila][4] = (stTotales.filaPlanificadoAcumulado[9].add(stTotales.filaPlanificadoAcumulado[10]).add(stTotales.filaPlanificadoAcumulado[11])).toString();
				datos[fila][5] = stTotales.totalPlanificadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado";
				datos[fila][1] = (stTotales.filaEjecutado[0].add(stTotales.filaEjecutado[1]).add(stTotales.filaEjecutado[2])).toString();
				datos[fila][2] = (stTotales.filaEjecutado[3].add(stTotales.filaEjecutado[4]).add(stTotales.filaEjecutado[5])).toString();
				datos[fila][3] = (stTotales.filaEjecutado[6].add(stTotales.filaEjecutado[7]).add(stTotales.filaEjecutado[8])).toString();
				datos[fila][4] = (stTotales.filaEjecutado[9].add(stTotales.filaEjecutado[10]).add(stTotales.filaEjecutado[11])).toString();
				datos[fila][5] = stTotales.totalEjecutado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado Acumulado";
				datos[fila][1] = (stTotales.filaEjecutadoAcumulado[0].add(stTotales.filaEjecutadoAcumulado[1]).add(stTotales.filaEjecutadoAcumulado[2])).toString();
				datos[fila][2] = (stTotales.filaEjecutadoAcumulado[3].add(stTotales.filaEjecutadoAcumulado[4]).add(stTotales.filaEjecutadoAcumulado[5])).toString();
				datos[fila][3] = (stTotales.filaEjecutadoAcumulado[6].add(stTotales.filaEjecutadoAcumulado[7]).add(stTotales.filaEjecutadoAcumulado[8])).toString();
				datos[fila][4] = (stTotales.filaEjecutadoAcumulado[9].add(stTotales.filaEjecutadoAcumulado[10]).add(stTotales.filaEjecutadoAcumulado[11])).toString();
				datos[fila][5] = stTotales.totalEjecutadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Variacion";
				datos[fila][1] = (stTotales.filaVariacion[0].add(stTotales.filaVariacion[1]).add(stTotales.filaVariacion[2])).toString();
				datos[fila][2] = (stTotales.filaVariacion[3].add(stTotales.filaVariacion[4]).add(stTotales.filaVariacion[5])).toString();
				datos[fila][3] = (stTotales.filaVariacion[6].add(stTotales.filaVariacion[7]).add(stTotales.filaVariacion[8])).toString();
				datos[fila][4] = (stTotales.filaVariacion[9].add(stTotales.filaVariacion[10]).add(stTotales.filaVariacion[11])).toString();
				datos[fila][5] = stTotales.totalVariacion.toString();
				fila++; 
				datos[fila][0] = "%";
				datos[fila][1] = (stTotales.filaVariacionPorcentaje[0].add(stTotales.filaVariacionPorcentaje[1]).add(stTotales.filaVariacionPorcentaje[2])).toString();
				datos[fila][2] = (stTotales.filaVariacionPorcentaje[3].add(stTotales.filaVariacionPorcentaje[4]).add(stTotales.filaVariacionPorcentaje[5])).toString();
				datos[fila][3] = (stTotales.filaVariacionPorcentaje[6].add(stTotales.filaVariacionPorcentaje[7]).add(stTotales.filaVariacionPorcentaje[8])).toString();
				datos[fila][4] = (stTotales.filaVariacionPorcentaje[9].add(stTotales.filaVariacionPorcentaje[10]).add(stTotales.filaVariacionPorcentaje[11])).toString();
				datos[fila][5] = stTotales.totalVariacionPorcentaje.toString();
				fila++; 
				datos[fila][0] = "Desembolsos Ejecutados";
				datos[fila][1] = (stTotales.filaDesembolsosReal[0].add(stTotales.filaDesembolsosReal[1]).add(stTotales.filaDesembolsosReal[2])).toString();
				datos[fila][2] = (stTotales.filaDesembolsosReal[3].add(stTotales.filaDesembolsosReal[4]).add(stTotales.filaDesembolsosReal[5])).toString();
				datos[fila][3] = (stTotales.filaDesembolsosReal[6].add(stTotales.filaDesembolsosReal[7]).add(stTotales.filaDesembolsosReal[8])).toString();
				datos[fila][4] = (stTotales.filaDesembolsosReal[9].add(stTotales.filaDesembolsosReal[10]).add(stTotales.filaDesembolsosReal[11])).toString();
				datos[fila][5] = stTotales.totalDesembolsosReal.toString();
				fila++; 
				datos[fila][0] = "Desembolsos";
				datos[fila][1] = (stTotales.filaDesembolsos[0].add(stTotales.filaDesembolsos[1]).add(stTotales.filaDesembolsos[2])).toString();
				datos[fila][2] = (stTotales.filaDesembolsos[3].add(stTotales.filaDesembolsos[4]).add(stTotales.filaDesembolsos[5])).toString();
				datos[fila][3] = (stTotales.filaDesembolsos[6].add(stTotales.filaDesembolsos[7]).add(stTotales.filaDesembolsos[8])).toString();
				datos[fila][4] = (stTotales.filaDesembolsos[9].add(stTotales.filaDesembolsos[10]).add(stTotales.filaDesembolsos[11])).toString();
				datos[fila][5] = stTotales.totalDesembolsos.toString();
				fila++; 
				datos[fila][0] = "Saldo";
				datos[fila][1] = (stTotales.filaSaldo[0].add(stTotales.filaSaldo[1]).add(stTotales.filaSaldo[2])).toString();
				datos[fila][2] = (stTotales.filaSaldo[3].add(stTotales.filaSaldo[4]).add(stTotales.filaSaldo[5])).toString();
				datos[fila][3] = (stTotales.filaSaldo[6].add(stTotales.filaSaldo[7]).add(stTotales.filaSaldo[8])).toString();
				datos[fila][4] = (stTotales.filaSaldo[9].add(stTotales.filaSaldo[10]).add(stTotales.filaSaldo[11])).toString();
				datos[fila][5] = stTotales.totalSaldo.toString();
				break;
			case AGRUPACION_CUATRIMESTRE:
				datos[fila][0] = "Total Planificado";
				datos[fila][1] = (stTotales.filaPlanificado[0].add(stTotales.filaPlanificado[1]).add(stTotales.filaPlanificado[2]).add(stTotales.filaPlanificado[3])).toString();
				datos[fila][2] = (stTotales.filaPlanificado[4].add(stTotales.filaPlanificado[5]).add(stTotales.filaPlanificado[6]).add(stTotales.filaPlanificado[7])).toString();
				datos[fila][3] = (stTotales.filaPlanificado[8].add(stTotales.filaPlanificado[9]).add(stTotales.filaPlanificado[10]).add(stTotales.filaPlanificado[11])).toString();
				datos[fila][4] = stTotales.totalPlanificado.toString();
				fila++; 
				datos[fila][0] = "Total Planificado Acumulado";

				datos[fila][1] = (stTotales.filaPlanificadoAcumulado[0].add(stTotales.filaPlanificadoAcumulado[1]).add(stTotales.filaPlanificadoAcumulado[2]).add(stTotales.filaPlanificadoAcumulado[3])).toString();
				datos[fila][2] = (stTotales.filaPlanificadoAcumulado[4].add(stTotales.filaPlanificadoAcumulado[5]).add(stTotales.filaPlanificadoAcumulado[6]).add(stTotales.filaPlanificadoAcumulado[7])).toString();
				datos[fila][3] = (stTotales.filaPlanificadoAcumulado[8].add(stTotales.filaPlanificadoAcumulado[9]).add(stTotales.filaPlanificadoAcumulado[10]).add(stTotales.filaPlanificadoAcumulado[11])).toString();
				datos[fila][4] = stTotales.totalPlanificadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado";
				datos[fila][1] = (stTotales.filaEjecutado[0].add(stTotales.filaEjecutado[1]).add(stTotales.filaEjecutado[2]).add(stTotales.filaEjecutado[3])).toString();
				datos[fila][2] = (stTotales.filaEjecutado[4].add(stTotales.filaEjecutado[5]).add(stTotales.filaEjecutado[6]).add(stTotales.filaEjecutado[7])).toString();
				datos[fila][3] = (stTotales.filaEjecutado[8].add(stTotales.filaEjecutado[9]).add(stTotales.filaEjecutado[10]).add(stTotales.filaEjecutado[11])).toString();
				datos[fila][4] = stTotales.totalEjecutado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado Acumulado";
				datos[fila][1] = (stTotales.filaEjecutadoAcumulado[0].add(stTotales.filaEjecutadoAcumulado[1]).add(stTotales.filaEjecutadoAcumulado[2]).add(stTotales.filaEjecutadoAcumulado[3])).toString();
				datos[fila][2] = (stTotales.filaEjecutadoAcumulado[4].add(stTotales.filaEjecutadoAcumulado[5]).add(stTotales.filaEjecutadoAcumulado[6]).add(stTotales.filaEjecutadoAcumulado[7])).toString();
				datos[fila][3] = (stTotales.filaEjecutadoAcumulado[8].add(stTotales.filaEjecutadoAcumulado[9]).add(stTotales.filaEjecutadoAcumulado[10]).add(stTotales.filaEjecutadoAcumulado[11])).toString();
				datos[fila][4] = stTotales.totalEjecutadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Variacion";
				datos[fila][1] = (stTotales.filaVariacion[0].add(stTotales.filaVariacion[1]).add(stTotales.filaVariacion[2]).add(stTotales.filaVariacion[3])).toString();
				datos[fila][2] = (stTotales.filaVariacion[4].add(stTotales.filaVariacion[5]).add(stTotales.filaVariacion[6]).add(stTotales.filaVariacion[7])).toString();
				datos[fila][3] = (stTotales.filaVariacion[8].add(stTotales.filaVariacion[9]).add(stTotales.filaVariacion[10]).add(stTotales.filaVariacion[11])).toString();
				datos[fila][4] = stTotales.totalVariacion.toString();
				fila++; 
				datos[fila][0] = "%";
				datos[fila][1] = (stTotales.filaVariacionPorcentaje[0].add(stTotales.filaVariacionPorcentaje[1]).add(stTotales.filaVariacionPorcentaje[2]).add(stTotales.filaVariacionPorcentaje[3])).toString();
				datos[fila][2] = (stTotales.filaVariacionPorcentaje[4].add(stTotales.filaVariacionPorcentaje[5]).add(stTotales.filaVariacionPorcentaje[6]).add(stTotales.filaVariacionPorcentaje[7])).toString();
				datos[fila][3] = (stTotales.filaVariacionPorcentaje[8].add(stTotales.filaVariacionPorcentaje[9]).add(stTotales.filaVariacionPorcentaje[10]).add(stTotales.filaVariacionPorcentaje[11])).toString();
				datos[fila][4] = stTotales.totalVariacionPorcentaje.toString();
				fila++; 
				datos[fila][0] = "Desembolsos Ejecutados";
				datos[fila][1] = (stTotales.filaDesembolsosReal[0].add(stTotales.filaDesembolsosReal[1]).add(stTotales.filaDesembolsosReal[2]).add(stTotales.filaDesembolsosReal[3])).toString();
				datos[fila][2] = (stTotales.filaDesembolsosReal[4].add(stTotales.filaDesembolsosReal[5]).add(stTotales.filaDesembolsosReal[6]).add(stTotales.filaDesembolsosReal[7])).toString();
				datos[fila][3] = (stTotales.filaDesembolsosReal[8].add(stTotales.filaDesembolsosReal[9]).add(stTotales.filaDesembolsosReal[10]).add(stTotales.filaDesembolsosReal[11])).toString();
				datos[fila][4] = stTotales.totalDesembolsosReal.toString();
				fila++; 
				datos[fila][0] = "Desembolsos";
				datos[fila][1] = (stTotales.filaDesembolsos[0].add(stTotales.filaDesembolsos[1]).add(stTotales.filaDesembolsos[2]).add(stTotales.filaDesembolsos[3])).toString();
				datos[fila][2] = (stTotales.filaDesembolsos[4].add(stTotales.filaDesembolsos[5]).add(stTotales.filaDesembolsos[6]).add(stTotales.filaDesembolsos[7])).toString();
				datos[fila][3] = (stTotales.filaDesembolsos[8].add(stTotales.filaDesembolsos[9]).add(stTotales.filaDesembolsos[10]).add(stTotales.filaDesembolsos[11])).toString();
				datos[fila][4] = stTotales.totalDesembolsos.toString();
				fila++; 
				datos[fila][0] = "Saldo";
				datos[fila][1] = (stTotales.filaSaldo[0].add(stTotales.filaSaldo[1]).add(stTotales.filaSaldo[2]).add(stTotales.filaSaldo[3])).toString();
				datos[fila][2] = (stTotales.filaSaldo[4].add(stTotales.filaSaldo[5]).add(stTotales.filaSaldo[6]).add(stTotales.filaSaldo[7])).toString();
				datos[fila][3] = (stTotales.filaSaldo[8].add(stTotales.filaSaldo[9]).add(stTotales.filaSaldo[10]).add(stTotales.filaSaldo[11])).toString();
				datos[fila][4] = stTotales.totalSaldo.toString();
				break;
			case AGRUPACION_SEMESTRE:
				datos[fila][0] = "Total Planificado";
				datos[fila][1] = (stTotales.filaPlanificado[0].add(stTotales.filaPlanificado[1]).add(stTotales.filaPlanificado[2]).add(stTotales.filaPlanificado[3]).add(stTotales.filaPlanificado[4]).add(stTotales.filaPlanificado[5])).toString();
				datos[fila][2] = (stTotales.filaPlanificado[6].add(stTotales.filaPlanificado[7]).add(stTotales.filaPlanificado[8]).add(stTotales.filaPlanificado[9]).add(stTotales.filaPlanificado[10]).add(stTotales.filaPlanificado[11])).toString();
				datos[fila][3] = stTotales.totalPlanificado.toString();
				fila++; 
				datos[fila][0] = "Total Planificado Acumulado";
				datos[fila][1] = (stTotales.filaPlanificadoAcumulado[0].add(stTotales.filaPlanificadoAcumulado[1]).add(stTotales.filaPlanificadoAcumulado[2]).add(stTotales.filaPlanificadoAcumulado[3]).add(stTotales.filaPlanificadoAcumulado[4]).add(stTotales.filaPlanificadoAcumulado[5])).toString();
				datos[fila][2] = (stTotales.filaPlanificadoAcumulado[6].add(stTotales.filaPlanificadoAcumulado[7]).add(stTotales.filaPlanificadoAcumulado[8]).add(stTotales.filaPlanificadoAcumulado[9]).add(stTotales.filaPlanificadoAcumulado[10]).add(stTotales.filaPlanificadoAcumulado[11])).toString();
				datos[fila][3] = stTotales.totalPlanificadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado";
				datos[fila][1] = (stTotales.filaEjecutado[0].add(stTotales.filaEjecutado[1]).add(stTotales.filaEjecutado[2]).add(stTotales.filaEjecutado[3]).add(stTotales.filaEjecutado[4]).add(stTotales.filaEjecutado[5])).toString();
				datos[fila][2] = (stTotales.filaEjecutado[6].add(stTotales.filaEjecutado[7]).add(stTotales.filaEjecutado[8]).add(stTotales.filaEjecutado[9]).add(stTotales.filaEjecutado[10]).add(stTotales.filaEjecutado[11])).toString();
				datos[fila][3] = stTotales.totalEjecutado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado Acumulado";
				datos[fila][1] = (stTotales.filaEjecutadoAcumulado[0].add(stTotales.filaEjecutadoAcumulado[1]).add(stTotales.filaEjecutadoAcumulado[2]).add(stTotales.filaEjecutadoAcumulado[3]).add(stTotales.filaEjecutadoAcumulado[4]).add(stTotales.filaEjecutadoAcumulado[5])).toString();
				datos[fila][2] = (stTotales.filaEjecutadoAcumulado[6].add(stTotales.filaEjecutadoAcumulado[7]).add(stTotales.filaEjecutadoAcumulado[8]).add(stTotales.filaEjecutadoAcumulado[9]).add(stTotales.filaEjecutadoAcumulado[10]).add(stTotales.filaEjecutadoAcumulado[11])).toString();
				datos[fila][3] = stTotales.totalEjecutadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Variacion";
				datos[fila][1] = (stTotales.filaVariacion[0].add(stTotales.filaVariacion[1]).add(stTotales.filaVariacion[2]).add(stTotales.filaVariacion[3]).add(stTotales.filaVariacion[4]).add(stTotales.filaVariacion[5])).toString();
				datos[fila][2] = (stTotales.filaVariacion[6].add(stTotales.filaVariacion[7]).add(stTotales.filaVariacion[8]).add(stTotales.filaVariacion[9]).add(stTotales.filaVariacion[10]).add(stTotales.filaVariacion[11])).toString();
				datos[fila][3] = stTotales.totalVariacion.toString();
				fila++; 
				datos[fila][0] = "%";
				datos[fila][1] = (stTotales.filaVariacionPorcentaje[0].add(stTotales.filaVariacionPorcentaje[1]).add(stTotales.filaVariacionPorcentaje[2]).add(stTotales.filaVariacionPorcentaje[3]).add(stTotales.filaVariacionPorcentaje[4]).add(stTotales.filaVariacionPorcentaje[5])).toString();
				datos[fila][2] = (stTotales.filaVariacionPorcentaje[6].add(stTotales.filaVariacionPorcentaje[7]).add(stTotales.filaVariacionPorcentaje[8]).add(stTotales.filaVariacionPorcentaje[9]).add(stTotales.filaVariacionPorcentaje[10]).add(stTotales.filaVariacionPorcentaje[11])).toString();
				datos[fila][3] = stTotales.totalVariacionPorcentaje.toString();
				fila++; 
				datos[fila][0] = "Desembolsos Ejecutados";
				datos[fila][1] = (stTotales.filaDesembolsosReal[0].add(stTotales.filaDesembolsosReal[1]).add(stTotales.filaDesembolsosReal[2]).add(stTotales.filaDesembolsosReal[3]).add(stTotales.filaDesembolsosReal[4]).add(stTotales.filaDesembolsosReal[5])).toString();
				datos[fila][2] = (stTotales.filaDesembolsosReal[6].add(stTotales.filaDesembolsosReal[7]).add(stTotales.filaDesembolsosReal[8]).add(stTotales.filaDesembolsosReal[9]).add(stTotales.filaDesembolsosReal[10]).add(stTotales.filaDesembolsosReal[11])).toString();
				datos[fila][3] = stTotales.totalDesembolsosReal.toString();
				fila++; 
				datos[fila][0] = "Desembolsos";
				datos[fila][1] = (stTotales.filaDesembolsos[0].add(stTotales.filaDesembolsos[1]).add(stTotales.filaDesembolsos[2]).add(stTotales.filaDesembolsos[3]).add(stTotales.filaDesembolsos[4]).add(stTotales.filaDesembolsos[5])).toString();
				datos[fila][2] = (stTotales.filaDesembolsos[6].add(stTotales.filaDesembolsos[7]).add(stTotales.filaDesembolsos[8]).add(stTotales.filaDesembolsos[9]).add(stTotales.filaDesembolsos[10]).add(stTotales.filaDesembolsos[11])).toString();
				datos[fila][3] = stTotales.totalDesembolsos.toString();
				fila++; 
				datos[fila][0] = "Saldo";
				datos[fila][1] = (stTotales.filaSaldo[0].add(stTotales.filaSaldo[1]).add(stTotales.filaSaldo[2]).add(stTotales.filaSaldo[3]).add(stTotales.filaSaldo[4]).add(stTotales.filaSaldo[5])).toString();
				datos[fila][2] = (stTotales.filaSaldo[6].add(stTotales.filaSaldo[7]).add(stTotales.filaSaldo[8]).add(stTotales.filaSaldo[9]).add(stTotales.filaSaldo[10]).add(stTotales.filaSaldo[11])).toString();
				datos[fila][3] = stTotales.totalSaldo.toString();
				break;
			case AGRUPACION_ANUAL:
				datos[fila][0] = "Total Planificado";
				datos[fila][1] = (stTotales.filaPlanificado[0].add(stTotales.filaPlanificado[1]).add(stTotales.filaPlanificado[2]).add(stTotales.filaPlanificado[3]).add(stTotales.filaPlanificado[4]).add(stTotales.filaPlanificado[5]).add(stTotales.filaPlanificado[6]).add(stTotales.filaPlanificado[7]).add(stTotales.filaPlanificado[8]).add(stTotales.filaPlanificado[9]).add(stTotales.filaPlanificado[10]).add(stTotales.filaPlanificado[11])).toString();
				datos[fila][2] = stTotales.totalPlanificado.toString();
				fila++; 
				datos[fila][0] = "Total Planificado Acumulado";
				datos[fila][1] = (stTotales.filaPlanificadoAcumulado[0].add(stTotales.filaPlanificadoAcumulado[1]).add(stTotales.filaPlanificadoAcumulado[2]).add(stTotales.filaPlanificadoAcumulado[3]).add(stTotales.filaPlanificadoAcumulado[4]).add(stTotales.filaPlanificadoAcumulado[5]).add(stTotales.filaPlanificadoAcumulado[6]).add(stTotales.filaPlanificadoAcumulado[7]).add(stTotales.filaPlanificadoAcumulado[8]).add(stTotales.filaPlanificadoAcumulado[9]).add(stTotales.filaPlanificadoAcumulado[10]).add(stTotales.filaPlanificadoAcumulado[11])).toString();
				datos[fila][2] = stTotales.totalPlanificadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado";
				datos[fila][1] = (stTotales.filaEjecutado[0].add(stTotales.filaEjecutado[1]).add(stTotales.filaEjecutado[2]).add(stTotales.filaEjecutado[3]).add(stTotales.filaEjecutado[4]).add(stTotales.filaEjecutado[5]).add(stTotales.filaEjecutado[6]).add(stTotales.filaEjecutado[7]).add(stTotales.filaEjecutado[8]).add(stTotales.filaEjecutado[9]).add(stTotales.filaEjecutado[10]).add(stTotales.filaEjecutado[11])).toString();
				datos[fila][2] = stTotales.totalEjecutado.toString();
				fila++; 
				datos[fila][0] = "Total Ejecutado Acumulado";
				datos[fila][1] = (stTotales.filaEjecutadoAcumulado[0].add(stTotales.filaEjecutadoAcumulado[1]).add(stTotales.filaEjecutadoAcumulado[2]).add(stTotales.filaEjecutadoAcumulado[3]).add(stTotales.filaEjecutadoAcumulado[4]).add(stTotales.filaEjecutadoAcumulado[5]).add(stTotales.filaEjecutadoAcumulado[6]).add(stTotales.filaEjecutadoAcumulado[7]).add(stTotales.filaEjecutadoAcumulado[8]).add(stTotales.filaEjecutadoAcumulado[9]).add(stTotales.filaEjecutadoAcumulado[10]).add(stTotales.filaEjecutadoAcumulado[11])).toString();
				datos[fila][2] = stTotales.totalEjecutadoAcumulado.toString();
				fila++; 
				datos[fila][0] = "Variacion";
				datos[fila][1] = (stTotales.filaVariacion[0].add(stTotales.filaVariacion[1]).add(stTotales.filaVariacion[2]).add(stTotales.filaVariacion[3]).add(stTotales.filaVariacion[4]).add(stTotales.filaVariacion[5]).add(stTotales.filaVariacion[6]).add(stTotales.filaVariacion[7]).add(stTotales.filaVariacion[8]).add(stTotales.filaVariacion[9]).add(stTotales.filaVariacion[10]).add(stTotales.filaVariacion[11])).toString();
				datos[fila][2] = stTotales.totalVariacion.toString();
				fila++; 
				datos[fila][0] = "%";
				datos[fila][1] = (stTotales.filaVariacionPorcentaje[0].add(stTotales.filaVariacionPorcentaje[1]).add(stTotales.filaVariacionPorcentaje[2]).add(stTotales.filaVariacionPorcentaje[3]).add(stTotales.filaVariacionPorcentaje[4]).add(stTotales.filaVariacionPorcentaje[5]).add(stTotales.filaVariacionPorcentaje[6]).add(stTotales.filaVariacionPorcentaje[7]).add(stTotales.filaVariacionPorcentaje[8]).add(stTotales.filaVariacionPorcentaje[9]).add(stTotales.filaVariacionPorcentaje[10]).add(stTotales.filaVariacionPorcentaje[11])).toString();
				datos[fila][2] = stTotales.totalVariacionPorcentaje.toString();
				fila++; 
				datos[fila][0] = "Desembolsos Ejecutados";
				datos[fila][1] = (stTotales.filaDesembolsosReal[0].add(stTotales.filaDesembolsosReal[1]).add(stTotales.filaDesembolsosReal[2]).add(stTotales.filaDesembolsosReal[3]).add(stTotales.filaDesembolsosReal[4]).add(stTotales.filaDesembolsosReal[5]).add(stTotales.filaDesembolsosReal[6]).add(stTotales.filaDesembolsosReal[7]).add(stTotales.filaDesembolsosReal[8]).add(stTotales.filaDesembolsosReal[9]).add(stTotales.filaDesembolsosReal[10]).add(stTotales.filaDesembolsosReal[11])).toString();
				datos[fila][2] = stTotales.totalDesembolsosReal.toString();
				fila++; 
				datos[fila][0] = "Desembolsos";
				datos[fila][1] = (stTotales.filaDesembolsos[0].add(stTotales.filaDesembolsos[1]).add(stTotales.filaDesembolsos[2]).add(stTotales.filaDesembolsos[3]).add(stTotales.filaDesembolsos[4]).add(stTotales.filaDesembolsos[5]).add(stTotales.filaDesembolsos[6]).add(stTotales.filaDesembolsos[7]).add(stTotales.filaDesembolsos[8]).add(stTotales.filaDesembolsos[9]).add(stTotales.filaDesembolsos[10]).add(stTotales.filaDesembolsos[11])).toString();
				datos[fila][2] = stTotales.totalDesembolsos.toString();
				fila++; 
				datos[fila][0] = "Saldo";
				datos[fila][1] = (stTotales.filaSaldo[0].add(stTotales.filaSaldo[1]).add(stTotales.filaSaldo[2]).add(stTotales.filaSaldo[3]).add(stTotales.filaSaldo[4]).add(stTotales.filaSaldo[5]).add(stTotales.filaSaldo[6]).add(stTotales.filaSaldo[7]).add(stTotales.filaSaldo[8]).add(stTotales.filaSaldo[9]).add(stTotales.filaSaldo[10]).add(stTotales.filaSaldo[11])).toString();
				datos[fila][2] = stTotales.totalSaldo.toString();
				break;
			}
		}
		return datos;
	}
	
}