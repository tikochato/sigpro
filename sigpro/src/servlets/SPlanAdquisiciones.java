package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import dao.CategoriaAdquisicionDAO;
import dao.EstructuraProyectoDAO;
import dao.PlanAdquisicionDAO;
import dao.PlanAdquisicionPagoDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.CategoriaAdquisicion;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Producto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stpresupuestoPlan{
		BigDecimal planificado;
	}
	
	class stplan{
		stpresupuestoPlan[] mes = new stpresupuestoPlan[12];
		Integer anio;
	}
	
	class sttotal{
		stpresupuestoPlan[] total = new stpresupuestoPlan[1];
		Integer anio;
	}
	
	class stcomponenteplanadquisicion{
		Integer objetoId;
		String nombre;
		Integer nivel;
		Integer objetoTipo;
		Integer planadquisicionId;
		Integer acumulacionCosto;
		Integer objetoTipoPadre;
		Integer objetoIdPadre;
		DateTime fechaIncial;
		DateTime fechaFinal;
		BigDecimal costo;
		stplan[] anioPlan;
		sttotal[] anioTotalPlan;
		BigDecimal total;
	}
	
	class stcategoria{
		Integer categoriaId;
		String nombre;
		ArrayList<List<Integer>> objetos = new ArrayList<List<Integer>>();
	}
	
	final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;
		
    public SPlanAdquisiciones() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			Integer fechaInicio = Utils.String2Int(map.get("fechaInicio"));
			Integer fechaFin = Utils.String2Int(map.get("fechaFin"));
			
			if(accion.equals("generarPlan")){
				try{
					List<stcomponenteplanadquisicion> lstprestamo = generarPlan(idPrestamo, usuario, fechaInicio, fechaFin);
										
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			        response_text = String.join("", "\"proyecto\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}
				catch (Exception e){
					CLogger.write("2", SControlAdquisiciones.class, e);
				}
			}else if(accion.equals("exportarExcel")){
				Integer agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
				Integer tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
				byte [] outArray = exportarExcel(idPrestamo, agrupacion, usuario, fechaInicio, fechaFin, tipoVisualizacion);
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Ejecucion_Presupuestaria.xls");
				ServletOutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}catch(Exception e){
			CLogger.write("1", SPlanAdquisiciones.class, e);
		}
	}
	
	private List<stcomponenteplanadquisicion> generarPlan(Integer idPrestamo, String usuario, Integer fechaInicial, Integer fechaFinal) throws Exception{
		try{
			List<stcomponenteplanadquisicion> lstPrestamo = new ArrayList<>();
			List<?> estruturaProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
			stcomponenteplanadquisicion temp = null;
			
			List<CategoriaAdquisicion> lstCategorias = CategoriaAdquisicionDAO.getCategoriaAdquisicion(); 
			List<stcategoria> lsttempCategorias = new ArrayList<stcategoria>();
			stcategoria tempCategoria = null;
			for(CategoriaAdquisicion categoria : lstCategorias){
				tempCategoria = new stcategoria();
				tempCategoria.categoriaId = categoria.getId();
				tempCategoria.nombre = categoria.getNombre();
				lsttempCategorias.add(tempCategoria);
			}
			
			for(Object objeto: estruturaProyecto){
				temp = new stcomponenteplanadquisicion();
				Object[] obj = (Object[])objeto;
				BigInteger objetoTipo = (BigInteger)obj[2];
				Integer tipo = objetoTipo.intValue();
				if(tipo == 2){
					Integer componenteid = (Integer)obj[0]; 
					temp.objetoId = (Integer)obj[0];
					temp.nombre = (String)obj[1];
					temp.nivel = 0;
					temp.objetoTipo = tipo;
					lstPrestamo.add(temp);
					
					List<List<Integer>> hijos = EstructuraProyectoDAO.getHijosCompleto((String)obj[3], estruturaProyecto);
					
					for(List<Integer> objetoHijo : hijos){
						List<PlanAdquisicion> lstplan = PlanAdquisicionDAO.getPlanAdquisicionesByObjeto(objetoHijo.get(1), objetoHijo.get(0));
						
						if(lstplan != null && !lstplan.isEmpty()){
							for(PlanAdquisicion plan : lstplan){
								for(stcategoria cat : lsttempCategorias){
									if(cat.categoriaId == plan.getCategoriaAdquisicion().getId()){
										List<Integer> objH = new ArrayList<Integer>();
										objH.add(objetoHijo.get(0));
										objH.add(objetoHijo.get(1));
										objH.add(plan.getId());
										cat.objetos.add(objH);
									}
								}
							}
						}
					}
					
					for(stcategoria cat : lsttempCategorias){
						if(!cat.objetos.isEmpty()){
							temp = new stcomponenteplanadquisicion();
							temp.nombre = cat.nombre;
							temp.nivel = 1;
							lstPrestamo.add(temp);
							
							for(List<Integer> objCat : cat.objetos){
								temp = new stcomponenteplanadquisicion();
								String nombre = null;
								Date fechaInicio = new Date();
								Date fechaFin = new Date();
								Integer acumulacionCosto = 0;
								BigDecimal costo = new BigDecimal(0);
								
								switch(objCat.get(1)){
								case 3:
									Producto producto = ProductoDAO.getProductoPorId(objCat.get(0));
									costo = producto.getCosto();
									nombre = producto.getNombre();
									fechaInicio = producto.getFechaInicio();
									fechaFin = producto.getFechaFin();
									acumulacionCosto = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getId() : 3;
									break;
								case 4:
									Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objCat.get(0));
									costo = subproducto.getCosto();
									nombre = subproducto.getNombre();
									fechaInicio = subproducto.getFechaInicio();
									fechaFin = subproducto.getFechaFin();
									acumulacionCosto = subproducto.getAcumulacionCosto() != null ? subproducto.getAcumulacionCosto().getId() : 3;
									break;
								case 5:
									Actividad actividad = ActividadDAO.getActividadPorId(objCat.get(0));
									costo = actividad.getCosto();
									nombre = actividad.getNombre();
									fechaInicio = actividad.getFechaInicio();
									fechaFin = actividad.getFechaFin();
									acumulacionCosto = actividad.getAcumulacionCosto() != null ? actividad.getAcumulacionCosto().getId() : 3;
									break;
								}
								
								temp.nombre = nombre;
								temp.objetoId = objCat.get(0);
								temp.objetoTipo = objCat.get(1);
								temp.nivel = 2;
								temp.anioPlan = inicializarStplan(fechaInicial, fechaFinal);
								temp.costo = costo;
								temp.acumulacionCosto = acumulacionCosto;
								temp.fechaIncial = new DateTime(fechaInicio);
								temp.fechaFinal = new DateTime(fechaFin);
								temp.planadquisicionId = objCat.get(2);
								temp.objetoIdPadre = componenteid;
								temp.objetoTipoPadre = 2;
								
								temp = getCostoPagos(temp);
								
								temp.fechaIncial = null;
								temp.fechaFinal = null;
								lstPrestamo.add(temp);
							}
						}
					}
					
					inicializarObjCategoria(lsttempCategorias);
				}
			}
			for(stcomponenteplanadquisicion stprestamo : lstPrestamo){
				sttotal[] sttotaltemp = inicializarStTotalPlan(fechaInicial, fechaFinal);
				if(stprestamo.anioPlan != null){
					for(int j = 0; j < stprestamo.anioPlan.length; j++){
						for(int i = 0; i< stprestamo.anioPlan[j].mes.length; i++){
							if(sttotaltemp[j].anio.equals(stprestamo.anioPlan[j].anio)){
								sttotaltemp[j].total[0].planificado = sttotaltemp[j].total[0].planificado.add(stprestamo.anioPlan[j].mes[i].planificado);
							}
						}
					}
					
					stprestamo.anioTotalPlan =  sttotaltemp;
				}				
			}
			
			int posComponente = 0;
			for(int i = 0; i < lstPrestamo.size(); i++){
				if(lstPrestamo.get(i).objetoTipo != null && lstPrestamo.get(i).objetoTipo == 2){
					posComponente = i;
					lstPrestamo.get(i).anioTotalPlan = inicializarStTotalPlan(fechaInicial, fechaFinal);
				}else{
					if(lstPrestamo.get(i).anioTotalPlan != null){
						for(int j=0; j < lstPrestamo.get(i).anioTotalPlan.length; j++){
							for(int h = 0; h < lstPrestamo.get(i).anioTotalPlan[j].total.length; h++){
								if(lstPrestamo.get(posComponente).anioTotalPlan[j].anio.equals(lstPrestamo.get(i).anioTotalPlan[j].anio)){
									lstPrestamo.get(posComponente).anioTotalPlan[j].total[h].planificado = lstPrestamo.get(posComponente).anioTotalPlan[j].total[h].planificado.add(lstPrestamo.get(i).anioTotalPlan[j].total[h].planificado); 
								}
							}
						}						
					}
				}		
			}
			
			for(stcomponenteplanadquisicion stprestamo : lstPrestamo){
				if(stprestamo.anioTotalPlan != null){
					stprestamo.total = new BigDecimal(0);
					for(int j = 0; j < stprestamo.anioTotalPlan.length; j++){
						for(int i = 0; i< stprestamo.anioTotalPlan[j].total.length; i++){
							stprestamo.total = stprestamo.total.add(stprestamo.anioTotalPlan[j].total[i].planificado);
						}
					}
				}
			}
			
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("1", SControlAdquisiciones.class, e);
			return null;
		}
	}
	
	private stcomponenteplanadquisicion getCostoPagos(stcomponenteplanadquisicion stcomponente){
		PlanAdquisicion plan = PlanAdquisicionDAO.getPlanAdquisicionById(stcomponente.planadquisicionId);
		Calendar fechaInicial = Calendar.getInstance();
		for(stplan anioObj: stcomponente.anioPlan){
			List<PlanAdquisicionPago> pagos = PlanAdquisicionPagoDAO.getPagosByPlan(plan.getId());
			if(pagos != null && !pagos.isEmpty()){
				for(PlanAdquisicionPago pago : pagos){
					fechaInicial.setTime(pago.getFechaPago());
					int mes = fechaInicial.get(Calendar.MONTH);
					int anio = fechaInicial.get(Calendar.YEAR);					
					if(anio == anioObj.anio){
						anioObj.mes[mes].planificado = anioObj.mes[mes].planificado.add(pago.getPago());
					}
				}
			}else{
				int diaInicial = stcomponente.fechaIncial.getDayOfMonth();
				int mesInicial = stcomponente.fechaIncial.getMonthOfYear() -1;
				int anioInicial = stcomponente.fechaIncial.getYear();
				int diaFinal = stcomponente.fechaFinal.getDayOfMonth();
				int mesFinal = stcomponente.fechaFinal.getMonthOfYear() -1;
				int anioFinal = stcomponente.fechaFinal.getYear();
				
				if(anioObj.anio >= anioInicial && anioObj.anio<=anioFinal){
					if(stcomponente.acumulacionCosto != null){
						if(stcomponente.acumulacionCosto == 1){						
							if(anioInicial == anioObj.anio){
								anioObj.mes[mesInicial].planificado =  stcomponente.costo != null ? stcomponente.costo : new BigDecimal(0);
							}
						}else if(stcomponente.acumulacionCosto == 2){
							int dias = (int)((stcomponente.fechaFinal.getMillis() - stcomponente.fechaIncial.getMillis())/(1000*60*60*24));
							BigDecimal costoDiario = stcomponente.costo != null ? stcomponente.costo.divide(new BigDecimal(dias),5, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
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
						}else if(stcomponente.acumulacionCosto ==3){
							if(anioFinal == anioObj.anio){
								anioObj.mes[mesFinal].planificado =  stcomponente.costo != null ? stcomponente.costo : new BigDecimal(0);
							}
						}
					}
				}
			}
		}
		return stcomponente;
	}
	
	private void inicializarObjCategoria(List<stcategoria> lsttempCategorias){
		for(stcategoria cat : lsttempCategorias){
			cat.objetos = new ArrayList<List<Integer>>();
		}
	}
	
	private stplan[] inicializarStplan(Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stplan[] anios = new stplan[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stplan temp = new stplan();
			for(int m=0; m<12; m++){
				temp.mes[m]= new stpresupuestoPlan();
				temp.mes[m].planificado = new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
	
	private sttotal[] inicializarStTotalPlan(Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		sttotal[] anios = new sttotal[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			sttotal temp = new sttotal();
			for(int m=0; m<1; m++){
				temp.total[m]= new stpresupuestoPlan();
				temp.total[m].planificado = new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
	
	private byte[] exportarExcel(int prestamoId, int agrupacion, String usuario, Integer fechaInicial, Integer fechaFinal, Integer tipoVisualizacion){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datosInforme[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders(fechaInicial, fechaFinal, agrupacion, tipoVisualizacion);
			List<stcomponenteplanadquisicion> lstPrestamo = generarPlan(prestamoId, usuario, fechaInicial, fechaFinal);	
			datosInforme = generarDatosReporte(lstPrestamo, fechaInicial, fechaFinal, agrupacion, tipoVisualizacion, headers[0].length, usuario);
			excel = new CExcel("Plan de Adquisiciones", false, null);
			wb=excel.generateExcelOfData(datosInforme, "Plan de Adquisiciones", headers, null, true, usuario);
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
		String operacionesFila[] = new String[columnasTotal];
		String columnasOperacion[] = new String[columnasTotal];
		String totales[] = new String[totalesCant];
		String sumtotalesCol[] = new String[columnasTotal];
		titulo[0]="Nombre";
		tipo[0]="string";
		operacionesFila[0]="";
		columnasOperacion[0]="";
		sumtotalesCol[0]="";
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
				sumtotalesCol[pos]="sum";
				pos++;
			}
		}
		for (int j=0; j<aniosDiferencia; j++){
			titulo[pos] = "Total " + (anioInicio+j);
			tipo[pos] = "double";
			operacionesFila[pos]="";
			columnasOperacion[pos]= "";
			totales[totalesCant-factorVisualizacion] += pos+",";
			sumtotalesCol[pos]="";
			pos++;
		}
		titulo[pos] = "Total";
		tipo[pos] = "double";
		operacionesFila[pos]="sum";
		sumtotalesCol[pos]="";
		columnasOperacion[pos]=totales[totalesCant-factorVisualizacion];
		pos++;
		
		headers = new String[][]{
			titulo,  //titulos
			{""}, //mapeo
			tipo, //tipo dato
			sumtotalesCol, //operaciones columnas
			{""}, //operaciones div
			null,
			operacionesFila,
			columnasOperacion
			};
			
		return headers;
	}
	
	public String[][] generarDatosReporte(List<stcomponenteplanadquisicion> lstPrestamo, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, int columnasTotal, String usuario){
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
				stcomponenteplanadquisicion prestamo = lstPrestamo.get(i);
				String sangria;
				switch (prestamo.nivel + 1){
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
				//Valores planificado-real
				
				if(prestamo.anioPlan != null){
					for(int a=0; a<prestamo.anioPlan.length; a++){
						posicion = columna + (a*factorVisualizacion);
						
						for(int m=0; m<12; m++){
							prestamo.anioPlan[a].mes[m].planificado=(prestamo.anioPlan[a].mes[m].planificado==null) ? new BigDecimal(0) : prestamo.anioPlan[a].mes[m].planificado.setScale(2, BigDecimal.ROUND_DOWN);							
						}
						
						switch(agrupacion){
							case AGRUPACION_MES:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= prestamo.anioPlan[a].mes[0].planificado.toString();
									datos[i][posicion+(1*sumaColumnas)]= prestamo.anioPlan[a].mes[1].planificado.toString();
									datos[i][posicion+(2*sumaColumnas)]= prestamo.anioPlan[a].mes[2].planificado.toString();
									datos[i][posicion+(3*sumaColumnas)]= prestamo.anioPlan[a].mes[3].planificado.toString();
									datos[i][posicion+(4*sumaColumnas)]= prestamo.anioPlan[a].mes[4].planificado.toString();
									datos[i][posicion+(5*sumaColumnas)]= prestamo.anioPlan[a].mes[5].planificado.toString();
									datos[i][posicion+(6*sumaColumnas)]= prestamo.anioPlan[a].mes[6].planificado.toString();
									datos[i][posicion+(7*sumaColumnas)]= prestamo.anioPlan[a].mes[7].planificado.toString();
									datos[i][posicion+(8*sumaColumnas)]= prestamo.anioPlan[a].mes[8].planificado.toString();
									datos[i][posicion+(9*sumaColumnas)]= prestamo.anioPlan[a].mes[9].planificado.toString();
									datos[i][posicion+(10*sumaColumnas)]= prestamo.anioPlan[a].mes[10].planificado.toString();
									datos[i][posicion+(11*sumaColumnas)]= prestamo.anioPlan[a].mes[11].planificado.toString();
								}							
								posicion = posicion+(12*sumaColumnas)+1;
								break;
							case AGRUPACION_BIMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (prestamo.anioPlan[a].mes[0].planificado.add(prestamo.anioPlan[a].mes[1].planificado)).toString();
									datos[i][posicion+(1*sumaColumnas)]= (prestamo.anioPlan[a].mes[2].planificado.add(prestamo.anioPlan[a].mes[3].planificado)).toString();
									datos[i][posicion+(2*sumaColumnas)]= (prestamo.anioPlan[a].mes[4].planificado.add(prestamo.anioPlan[a].mes[5].planificado)).toString();
									datos[i][posicion+(3*sumaColumnas)]= (prestamo.anioPlan[a].mes[6].planificado.add(prestamo.anioPlan[a].mes[7].planificado)).toString();
									datos[i][posicion+(4*sumaColumnas)]= (prestamo.anioPlan[a].mes[8].planificado.add(prestamo.anioPlan[a].mes[9].planificado)).toString();
									datos[i][posicion+(5*sumaColumnas)]= (prestamo.anioPlan[a].mes[10].planificado.add(prestamo.anioPlan[a].mes[11].planificado)).toString();
								}
								posicion = posicion+(6*sumaColumnas)+1;
								break;
							case AGRUPACION_TRIMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (prestamo.anioPlan[a].mes[0].planificado.add(prestamo.anioPlan[a].mes[1].planificado.add(prestamo.anioPlan[a].mes[2].planificado))).toString();
									datos[i][posicion+(1*sumaColumnas)]= (prestamo.anioPlan[a].mes[3].planificado.add(prestamo.anioPlan[a].mes[4].planificado.add(prestamo.anioPlan[a].mes[5].planificado))).toString();
									datos[i][posicion+(2*sumaColumnas)]= (prestamo.anioPlan[a].mes[6].planificado.add(prestamo.anioPlan[a].mes[7].planificado.add(prestamo.anioPlan[a].mes[8].planificado))).toString();
									datos[i][posicion+(3*sumaColumnas)]= (prestamo.anioPlan[a].mes[9].planificado.add(prestamo.anioPlan[a].mes[10].planificado.add(prestamo.anioPlan[a].mes[11].planificado))).toString();
								}
								posicion = posicion+(4*sumaColumnas)+1;
								break;
							case AGRUPACION_CUATRIMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (prestamo.anioPlan[a].mes[0].planificado.add(prestamo.anioPlan[a].mes[1].planificado).add(prestamo.anioPlan[a].mes[2].planificado.add(prestamo.anioPlan[a].mes[3].planificado))).toString();
									datos[i][posicion+(1*sumaColumnas)]= (prestamo.anioPlan[a].mes[4].planificado).add(prestamo.anioPlan[a].mes[5].planificado.add(prestamo.anioPlan[a].mes[6].planificado.add(prestamo.anioPlan[a].mes[7].planificado))).toString();
									datos[i][posicion+(2*sumaColumnas)]= (prestamo.anioPlan[a].mes[8].planificado.add(prestamo.anioPlan[a].mes[9].planificado).add(prestamo.anioPlan[a].mes[10].planificado.add(prestamo.anioPlan[a].mes[11].planificado))).toString();
								}
								posicion = posicion+(3*sumaColumnas)+1;
								break;
							case AGRUPACION_SEMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (prestamo.anioPlan[a].mes[0].planificado.add(prestamo.anioPlan[a].mes[1].planificado).add(prestamo.anioPlan[a].mes[2].planificado.add(prestamo.anioPlan[a].mes[3].planificado.add(prestamo.anioPlan[a].mes[4].planificado.add(prestamo.anioPlan[a].mes[5].planificado))))).toString();
									datos[i][posicion+(1*sumaColumnas)]= (prestamo.anioPlan[a].mes[6].planificado.add(prestamo.anioPlan[a].mes[7].planificado).add(prestamo.anioPlan[a].mes[8].planificado.add(prestamo.anioPlan[a].mes[9].planificado.add(prestamo.anioPlan[a].mes[10].planificado.add(prestamo.anioPlan[a].mes[11].planificado))))).toString();
								}
								posicion = posicion+(2*sumaColumnas)+1;
								break;
							case AGRUPACION_ANUAL:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									BigDecimal totalAnualP = new BigDecimal(0);
									for(int m=0; m<12; m++){
										totalAnualP = totalAnualP.add(prestamo.anioPlan[a].mes[m].planificado);						
									}
									totalAniosP = totalAniosP.add(totalAnualP);
									datos[i][posicion]= totalAnualP.toString();
								}
								posicion = posicion+(1*sumaColumnas)+1;
								break;
							}
						}				
					}
					
					posicion = columnasTotal-1-sumaColumnas;
					if(prestamo.anioTotalPlan != null){
						for(int a=0; a < prestamo.anioTotalPlan.length; a++){	
							datos[i][posicion] = prestamo.anioTotalPlan[a].total[0].planificado.toString();
							posicion++;
						}
					}
					
					datos[i][columnasTotal-1] = prestamo.total != null ? prestamo.total.toString() : null;
				}
			}			
		return datos;
	}
}