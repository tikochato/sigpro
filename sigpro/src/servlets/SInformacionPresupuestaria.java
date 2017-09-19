package servlets;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.EstructuraProyectoDAO;
import dao.InformacionPresupuestariaDAO;
import dao.PagoDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Pago;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SInformacionPresupuestaria")
public class SInformacionPresupuestaria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stpresupuesto{
		BigDecimal planificado;
		BigDecimal real;
	}
	
	class stprestamo{
		String nombre;
		Integer objeto_id;
		int objeto_tipo;
		Integer nivel;
		DateTime fecha_inicial;
		DateTime fecha_final;
		stanio[] anios; 
		Integer acumulacion_costoid;
		BigDecimal costo;
		List<String> hijos;
		Integer predecesorId;
		Integer objetoPredecesorTipo;
	}
	
	class stprestamobimestre{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stpresupuesto[] bimestre1;
		stpresupuesto[] bimestre2;
		stpresupuesto[] bimestre3;
		stpresupuesto[] bimestre4;
		stpresupuesto[] bimestre5;
		stpresupuesto[] bimestre6;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total;
	}
	class stanio{
		stpresupuesto[] mes = new stpresupuesto[12];
		Integer anio;
		
	}

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
				List<stprestamo> lstPrestamo = getInformacionPresupuestaria(idPrestamo, anioInicial, anioFinal, usuario);
				
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
		        byte [] outArray = exportarExcel(idPrestamo, anioInicial, anioFinal, agrupacion, tipoVisualizacion, usuario);
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
				List<stprestamo> lstPrestamo = getInformacionPresupuestaria(idPrestamo, anioInicial, anioFinal, usuario);	
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
	
	private List<stprestamo> getInformacionPresupuestaria(int idPrestamo, int anioInicial, int anioFinal, String usuario){
		List<stprestamo> lstPrestamo = new ArrayList<>();
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
		
		Prestamo objPrestamo;
		String codigoPresupuestario = "";
		Integer fuente = 0;
		Integer organismo = 0;
		Integer correlativo = 0;
		
		for(Object objeto : estructuraProyecto){
			Object[] obj = (Object[]) objeto;
			Integer nivel = (Integer)obj[4];
			if(nivel != null){
				stprestamo tempPrestamo =  new stprestamo();
				tempPrestamo.objeto_id = (Integer)obj[0];
				tempPrestamo.nombre = (String)obj[1];
				tempPrestamo.nivel = nivel;
				tempPrestamo.objeto_tipo = ((BigInteger) obj[2]).intValue();
				tempPrestamo.fecha_inicial = new DateTime((Timestamp)obj[5]);
				tempPrestamo.fecha_final = new DateTime((Timestamp)obj[6]);
				tempPrestamo.anios = inicializarStanio(anioInicial, anioFinal);
				tempPrestamo.acumulacion_costoid = (Integer)obj[11];
				tempPrestamo.costo = (BigDecimal)obj[9];
				
				try {
					if(CMariaDB.connect()){
						Connection conn = CMariaDB.getConnection();
						if(tempPrestamo.objeto_tipo == 1){
						objPrestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(tempPrestamo.objeto_id, 1);
							if(objPrestamo != null ){
								codigoPresupuestario = Long.toString(objPrestamo.getCodigoPresupuestario());
								fuente = Utils.String2Int(codigoPresupuestario.substring(0,2));
								organismo = Utils.String2Int(codigoPresupuestario.substring(2,6));
								correlativo = Utils.String2Int(codigoPresupuestario.substring(6,10));
							}
						}			
						tempPrestamo = getPresupuestoReal(tempPrestamo, fuente, organismo, correlativo, anioInicial, anioFinal, conn, usuario);
						tempPrestamo = getPresupuestoPlanificado(tempPrestamo, usuario);
						conn.close();
					}
				} catch (SQLException e) {
					CLogger.write("3", SInformacionPresupuestaria.class, e);
				}
				tempPrestamo.fecha_inicial = null;
				tempPrestamo.fecha_final = null;
				lstPrestamo.add(tempPrestamo);

			}
		}
		if(lstPrestamo!=null && !lstPrestamo.isEmpty()){
			stanio[] aniosTemp = calcularCostoRecursivo(lstPrestamo, 1, 1, anioInicial, anioFinal);
			for(int a=0; a<lstPrestamo.get(0).anios.length; a++){
				for(int m=0; m<12; m++){
					if(aniosTemp[a].mes[m].planificado.compareTo(BigDecimal.ZERO) > 0){
						lstPrestamo.get(0).anios[a].mes[m].planificado = aniosTemp[a].mes[m].planificado;
					}
				}
			}
		}
		return lstPrestamo;
	}
	
	private stprestamo getPresupuestoPlanificado(stprestamo prestamo, String usuario){
		List<Pago> pagos = PagoDAO.getPagosByObjetoTipo(prestamo.objeto_id, prestamo.objeto_tipo);
		Calendar fechaInicial = Calendar.getInstance();
		for(stanio anioObj: prestamo.anios){			
			if(pagos!= null && pagos.size() > 0){
				for(Pago pago : pagos){
					fechaInicial.setTime(pago.getFechaPago());
					int mes = fechaInicial.get(Calendar.MONTH);
					int anio = fechaInicial.get(Calendar.YEAR);					
					if(anio == anioObj.anio){
						anioObj.mes[mes].planificado = anioObj.mes[mes].planificado.add(pago.getPago());
					}
				}
			}else{
				int diaInicial = prestamo.fecha_inicial.getDayOfMonth();
				int mesInicial = prestamo.fecha_inicial.getMonthOfYear() -1;
				int anioInicial = prestamo.fecha_inicial.getYear();
				int diaFinal = prestamo.fecha_final.getDayOfMonth();
				int mesFinal = prestamo.fecha_final.getMonthOfYear() -1;
				int anioFinal = prestamo.fecha_final.getYear();
				if(anioObj.anio >= anioInicial && anioObj.anio<=anioFinal){
					if(prestamo.acumulacion_costoid != null){
						if(prestamo.acumulacion_costoid == 1){						
							if(anioInicial == anioObj.anio){
								anioObj.mes[mesInicial].planificado =  prestamo.costo != null ? prestamo.costo : new BigDecimal(0);
							}
						}else if(prestamo.acumulacion_costoid == 2){
							int dias = (int)((prestamo.fecha_final.getMillis() - prestamo.fecha_inicial.getMillis())/(1000*60*60*24));
							BigDecimal costoDiario = prestamo.costo != null ? prestamo.costo.divide(new BigDecimal(dias),5, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
							int inicioActual = 0;
							if(anioObj.anio == anioInicial){
								inicioActual = mesInicial;
							}
							
							int finMes = anioObj.anio==anioFinal ? mesFinal : 11;
							for(int m=inicioActual; m<=finMes; m++){
								if(anioObj.anio == anioInicial && m==mesInicial){
									if(m==mesFinal){
										int diasMes = diaFinal-diaInicial;
										anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
									}else{
										Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
										int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
										diasMes = diasMes-diaInicial;
										anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
									}
								}else if(anioObj.anio == anioFinal && m== mesFinal){
									anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diaFinal));
								}else{
									Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
									int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
									anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
								}
							}
						}else if(prestamo.acumulacion_costoid ==3){
							if(anioFinal == anioObj.anio){
								anioObj.mes[mesFinal].planificado =  prestamo.costo;
							}
						}
					}
				}
			}
		}
		return prestamo;
	}
	
	private stprestamo getPresupuestoReal(stprestamo prestamo, Integer fuente, Integer organismo, Integer correlativo, Integer anioInicial, Integer anioFinal, Connection conn, String usuario){
		ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo = new ArrayList<ArrayList<BigDecimal>>();
		
			if(prestamo.objeto_tipo == 1){
				
					presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoProyecto(fuente, organismo, correlativo,anioInicial,anioFinal, conn);
			}else{
				Integer programa = null;
				Integer subprograma = null; 
				Integer proyecto = null;
				Integer actividad = null;
				Integer obra = null;
				switch(prestamo.objeto_tipo){
					case 2: Componente componente = ComponenteDAO.getComponentePorId(prestamo.objeto_id, usuario);
						programa = componente.getPrograma();
						subprograma = componente.getSubprograma(); 
						proyecto = componente.getProyecto_1();
						actividad = componente.getActividad();
						obra = componente.getObra();
						break;
					case 3: Producto producto = ProductoDAO.getProductoPorId(prestamo.objeto_id, usuario);
						programa = producto.getPrograma();
						subprograma = producto.getSubprograma(); 
						proyecto = producto.getProyecto();
						actividad = producto.getActividad();
						obra = producto.getObra();
						break;
					case 4: Subproducto subproducto = SubproductoDAO.getSubproductoPorId(prestamo.objeto_id, usuario);
						programa = subproducto.getPrograma();
						subprograma = subproducto.getSubprograma(); 
						proyecto = subproducto.getProyecto();
						actividad = subproducto.getActividad();
						obra = subproducto.getObra();
						break;
					case 5: Actividad actividadObj = ActividadDAO.getActividadPorId(prestamo.objeto_id, usuario);
						programa = actividadObj.getPrograma();
						subprograma = actividadObj.getSubprograma(); 
						proyecto = actividadObj.getProyecto();
						actividad = actividadObj.getActividad();
						obra = actividadObj.getObra();
						break;
				}
				
				
				presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
						anioInicial, anioFinal, programa, subprograma, proyecto, 
						actividad, obra, conn);
			}
		
		if(presupuestoPrestamo.size() > 0){
			int pos = 0;
			for(ArrayList<BigDecimal> objprestamopresupuesto : presupuestoPrestamo){
				for (int m=0; m<12; m++){
					prestamo.anios[pos].mes[m].real = objprestamopresupuesto.get(m) != null ? objprestamopresupuesto.get(m) : new BigDecimal(0);
				}
				prestamo.anios[pos].anio = objprestamopresupuesto.get(12) != null ? objprestamopresupuesto.get(12).intValueExact() : 0;
				pos = pos + 1;
			}
		}
		return prestamo;
	}
	
	private stanio[] inicializarStanio (Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			for(int m=0; m<12; m++){
				temp.mes[m]= new stpresupuesto();
				temp.mes[m].planificado = new BigDecimal(0);
				temp.mes[m].real =  new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
	
	
	private stanio[] calcularCostoRecursivo(List<stprestamo> lstPrestamo, Integer posicion, Integer nivel, Integer anioInicial, Integer anioFinal){
		stanio[] anios = inicializarStanio (anioInicial, anioFinal);
		while(posicion <lstPrestamo.size()){
			stprestamo prestamo = lstPrestamo.get(posicion);
			if(prestamo.nivel != null){
				if(prestamo.nivel.equals(nivel)){
					if(posicion+1<lstPrestamo.size()){
						if(lstPrestamo.get(posicion+1).nivel.equals(nivel+1)){
							stanio[] aniosTemp = calcularCostoRecursivo(lstPrestamo, posicion+1, nivel+1, anioInicial, anioFinal);				
							for(int a=0; a<anios.length; a++){
								for(int m=0; m<12; m++){
									if(aniosTemp[a].mes[m].planificado.compareTo(BigDecimal.ZERO) > 0){
										prestamo.anios[a].mes[m].planificado = aniosTemp[a].mes[m].planificado;
									}
								}
							}
						}
					}
					for(int a=0; a<anios.length; a++){
						for(int m=0; m<12;m++){
							anios[a].mes[m].planificado = anios[a].mes[m].planificado.add(prestamo.anios[a].mes[m].planificado);
						}
					}
				}else if(prestamo.nivel < nivel){
					return anios;
				}
			}
			posicion++;
		}
		return anios;
	}
		
	private byte[] exportarExcel(int prestamoId, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, String usuario){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datosInforme[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders(anioInicio, anioFin, agrupacion, tipoVisualizacion);
			List<stprestamo> lstPrestamo = getInformacionPresupuestaria(prestamoId, anioInicio, anioFin, usuario);	
			datosInforme = generarDatosReporte(lstPrestamo, anioInicio, anioFin, agrupacion, tipoVisualizacion, headers[0].length, usuario);
			CGraficaExcel grafica = generarGrafica(datosInforme, tipoVisualizacion, agrupacion, anioInicio, anioFin);
			excel = new CExcel("Ejecucion presupuestaria", false, grafica);
			wb=excel.generateExcelOfData(datosInforme, "Ejecucion presupuestaria", headers, null, true, usuario);
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
	
	public String[][] generarDatosReporte(List<stprestamo> lstPrestamo, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, int columnasTotal, String usuario){
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
				stprestamo prestamo = lstPrestamo.get(i);
				String sangria;
				switch (prestamo.objeto_tipo){
					case 1: sangria = ""; break;
					case 2: sangria = "   "; break;
					case 3: sangria = "      "; break;
					case 4: sangria = "         "; break;
					case 5: sangria = "            "; break;
					default: sangria = "";
				}
				datos[i][columna] = sangria+prestamo.nombre;
				columna++;
					int posicion = columna;
					BigDecimal totalAniosP = new BigDecimal(0);
					BigDecimal totalAniosR = new BigDecimal(0);
					//Valores planificado-real
					for(int a=0; a<prestamo.anios.length; a++){
						posicion = columna + (a*factorVisualizacion);
						
						for(int m=0; m<12; m++){
							prestamo.anios[a].mes[m].planificado=(prestamo.anios[a].mes[m].planificado==null) ? new BigDecimal(0) : prestamo.anios[a].mes[m].planificado.setScale(2, BigDecimal.ROUND_DOWN);;
							prestamo.anios[a].mes[m].real=(prestamo.anios[a].mes[m].real==null) ? new BigDecimal(0) : prestamo.anios[a].mes[m].real.setScale(2, BigDecimal.ROUND_DOWN);;
						}
						
						BigDecimal totalAnualP = new BigDecimal(0);
						BigDecimal totalAnualR = new BigDecimal(0);
						for(int m=0; m<12; m++){
							totalAnualP = totalAnualP.add(prestamo.anios[a].mes[m].planificado);
							totalAnualR = totalAnualR.add(prestamo.anios[a].mes[m].real);
						}
						totalAniosP = totalAniosP.add(totalAnualP);
						totalAniosR = totalAniosR.add(totalAnualR);
						switch(agrupacion){
						case AGRUPACION_MES:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= prestamo.anios[a].mes[0].planificado.toString();
								datos[i][posicion+(1*sumaColumnas)]= prestamo.anios[a].mes[1].planificado.toString();
								datos[i][posicion+(2*sumaColumnas)]= prestamo.anios[a].mes[2].planificado.toString();
								datos[i][posicion+(3*sumaColumnas)]= prestamo.anios[a].mes[3].planificado.toString();
								datos[i][posicion+(4*sumaColumnas)]= prestamo.anios[a].mes[4].planificado.toString();
								datos[i][posicion+(5*sumaColumnas)]= prestamo.anios[a].mes[5].planificado.toString();
								datos[i][posicion+(6*sumaColumnas)]= prestamo.anios[a].mes[6].planificado.toString();
								datos[i][posicion+(7*sumaColumnas)]= prestamo.anios[a].mes[7].planificado.toString();
								datos[i][posicion+(8*sumaColumnas)]= prestamo.anios[a].mes[8].planificado.toString();
								datos[i][posicion+(9*sumaColumnas)]= prestamo.anios[a].mes[9].planificado.toString();
								datos[i][posicion+(10*sumaColumnas)]= prestamo.anios[a].mes[10].planificado.toString();
								datos[i][posicion+(11*sumaColumnas)]= prestamo.anios[a].mes[11].planificado.toString();
								datos[i][posicion+(12*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= prestamo.anios[a].mes[0].real.toString();
								datos[i][posicion+(1*sumaColumnas)]= prestamo.anios[a].mes[1].real.toString();
								datos[i][posicion+(2*sumaColumnas)]= prestamo.anios[a].mes[2].real.toString();
								datos[i][posicion+(3*sumaColumnas)]= prestamo.anios[a].mes[3].real.toString();
								datos[i][posicion+(4*sumaColumnas)]= prestamo.anios[a].mes[4].real.toString();
								datos[i][posicion+(5*sumaColumnas)]= prestamo.anios[a].mes[5].real.toString();
								datos[i][posicion+(6*sumaColumnas)]= prestamo.anios[a].mes[6].real.toString();
								datos[i][posicion+(7*sumaColumnas)]= prestamo.anios[a].mes[7].real.toString();
								datos[i][posicion+(8*sumaColumnas)]= prestamo.anios[a].mes[8].real.toString();
								datos[i][posicion+(9*sumaColumnas)]= prestamo.anios[a].mes[9].real.toString();
								datos[i][posicion+(10*sumaColumnas)]= prestamo.anios[a].mes[10].real.toString();
								datos[i][posicion+(11*sumaColumnas)]= prestamo.anios[a].mes[11].real.toString();
								datos[i][posicion+(12*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(12*sumaColumnas)+1;
							break;
						case AGRUPACION_BIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].planificado.add(prestamo.anios[a].mes[1].planificado)).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[2].planificado.add(prestamo.anios[a].mes[3].planificado)).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mes[4].planificado.add(prestamo.anios[a].mes[5].planificado)).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].mes[6].planificado.add(prestamo.anios[a].mes[7].planificado)).toString();
								datos[i][posicion+(4*sumaColumnas)]= (prestamo.anios[a].mes[8].planificado.add(prestamo.anios[a].mes[9].planificado)).toString();
								datos[i][posicion+(5*sumaColumnas)]= (prestamo.anios[a].mes[10].planificado.add(prestamo.anios[a].mes[11].planificado)).toString();
								datos[i][posicion+(6*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].real.add(prestamo.anios[a].mes[1].real)).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[2].real.add(prestamo.anios[a].mes[3].real)).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mes[4].real.add(prestamo.anios[a].mes[5].real)).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].mes[6].real.add(prestamo.anios[a].mes[7].real)).toString();
								datos[i][posicion+(4*sumaColumnas)]= (prestamo.anios[a].mes[8].real.add(prestamo.anios[a].mes[9].real)).toString();
								datos[i][posicion+(5*sumaColumnas)]= (prestamo.anios[a].mes[10].real.add(prestamo.anios[a].mes[11].real)).toString();
								datos[i][posicion+(6*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(6*sumaColumnas)+1;
							break;
						case AGRUPACION_TRIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].planificado.add(prestamo.anios[a].mes[1].planificado.add(prestamo.anios[a].mes[2].planificado))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[3].planificado.add(prestamo.anios[a].mes[4].planificado.add(prestamo.anios[a].mes[5].planificado))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mes[6].planificado.add(prestamo.anios[a].mes[7].planificado.add(prestamo.anios[a].mes[8].planificado))).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].mes[9].planificado.add(prestamo.anios[a].mes[10].planificado.add(prestamo.anios[a].mes[11].planificado))).toString();
								datos[i][posicion+(4*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].real.add(prestamo.anios[a].mes[1].real.add(prestamo.anios[a].mes[2].real))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[3].real.add(prestamo.anios[a].mes[4].real.add(prestamo.anios[a].mes[5].real))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mes[6].real.add(prestamo.anios[a].mes[7].real.add(prestamo.anios[a].mes[8].real))).toString();
								datos[i][posicion+(3*sumaColumnas)]= (prestamo.anios[a].mes[9].real.add(prestamo.anios[a].mes[10].real.add(prestamo.anios[a].mes[11].real))).toString();
								datos[i][posicion+(4*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(4*sumaColumnas)+1;
							break;
						case AGRUPACION_CUATRIMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].planificado.add(prestamo.anios[a].mes[1].planificado).add(prestamo.anios[a].mes[2].planificado.add(prestamo.anios[a].mes[3].planificado))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[4].planificado).add(prestamo.anios[a].mes[5].planificado.add(prestamo.anios[a].mes[6].planificado.add(prestamo.anios[a].mes[7].planificado))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mes[8].planificado.add(prestamo.anios[a].mes[9].planificado).add(prestamo.anios[a].mes[10].planificado.add(prestamo.anios[a].mes[11].planificado))).toString();
								datos[i][posicion+(3*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].real.add(prestamo.anios[a].mes[1].real).add(prestamo.anios[a].mes[2].real.add(prestamo.anios[a].mes[3].real))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[4].real).add(prestamo.anios[a].mes[5].real.add(prestamo.anios[a].mes[6].real.add(prestamo.anios[a].mes[7].real))).toString();
								datos[i][posicion+(2*sumaColumnas)]= (prestamo.anios[a].mes[8].real.add(prestamo.anios[a].mes[9].real).add(prestamo.anios[a].mes[10].real.add(prestamo.anios[a].mes[11].real))).toString();
								datos[i][posicion+(3*sumaColumnas)]= totalAnualR.toString();
							}
							posicion = posicion+(3*sumaColumnas)+1;
							break;
						case AGRUPACION_SEMESTRE:
							if(tipoVisualizacion==0 || tipoVisualizacion==2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].planificado.add(prestamo.anios[a].mes[1].planificado).add(prestamo.anios[a].mes[2].planificado.add(prestamo.anios[a].mes[3].planificado.add(prestamo.anios[a].mes[4].planificado.add(prestamo.anios[a].mes[5].planificado))))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[6].planificado.add(prestamo.anios[a].mes[7].planificado).add(prestamo.anios[a].mes[8].planificado.add(prestamo.anios[a].mes[9].planificado.add(prestamo.anios[a].mes[10].planificado.add(prestamo.anios[a].mes[11].planificado))))).toString();
								datos[i][posicion+(2*sumaColumnas)]= totalAnualP.toString();
							}
							if(tipoVisualizacion == 2){
								posicion++;
							}
							if(tipoVisualizacion==1 || tipoVisualizacion == 2){
								datos[i][posicion]= (prestamo.anios[a].mes[0].real.add(prestamo.anios[a].mes[1].real).add(prestamo.anios[a].mes[2].real.add(prestamo.anios[a].mes[3].real.add(prestamo.anios[a].mes[4].real.add(prestamo.anios[a].mes[5].real))))).toString();
								datos[i][posicion+(1*sumaColumnas)]= (prestamo.anios[a].mes[6].real.add(prestamo.anios[a].mes[7].real).add(prestamo.anios[a].mes[8].real.add(prestamo.anios[a].mes[9].real.add(prestamo.anios[a].mes[10].real.add(prestamo.anios[a].mes[11].real))))).toString();
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
