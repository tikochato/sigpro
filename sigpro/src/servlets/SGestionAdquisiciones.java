package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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

import dao.CategoriaAdquisicionDAO;
import dao.ObjetoCosto;
import dao.ObjetoDAO;
import dao.PlanAdquisicionDAO;
import dao.ProyectoDAO;
import pojo.CategoriaAdquisicion;
import pojo.PlanAdquisicion;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SGestionAdquisiciones")
public class SGestionAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class stpresupuestoGestion{
		BigDecimal planificado;
	}
	
	class stgestion{
		stpresupuestoGestion[] mes = new stpresupuestoGestion[12];
		Integer anio;
	}
	
	class sttotalGestion{
		stpresupuestoGestion[] total = new stpresupuestoGestion[1];
		Integer anio;
	}
	
	class stcomponentegestionadquisicion{
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
		stgestion[] anioPlan;
		sttotalGestion[] anioTotalGestion;
		BigDecimal total;
		BigDecimal acumulado;
		Integer cantidadAdquisiciones;
	}
	
	class stcategoriaG{
		Integer categoriaId;
		String nombre;
		Integer adquisicionId;
		ArrayList<ObjetoCosto> objCosto = new ArrayList<ObjetoCosto>();		
	}
	
    public SGestionAdquisiciones() {
        super();
    }

    final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion")!=null ? map.get("accion") : "";
		String response_text = "";
		
		Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
		Integer fechaInicio = Utils.String2Int(map.get("fechaInicio"));
		Integer fechaFin = Utils.String2Int(map.get("fechaFin"));
		
		if(accion.equals("generarGestion")){
			try {
				String lineaBase = map.get("lineaBase");
				List<stcomponentegestionadquisicion> lstprestamo = generarPlan(proyectoId, usuario, fechaInicio, fechaFin, lineaBase);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
		        response_text = String.join("", "\"proyecto\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			} catch (Exception e) {
				CLogger.write("1", SReporteFinancieroAdquisiciones.class, e);
			}
		}else if(accion.equals("exportarExcel")){
			Integer agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
			Integer tipoVisualizacion = Utils.String2Int(map.get("tipoVisualizacion"), 0);
			String lineaBase = map.get("lineaBase");
			byte [] outArray = exportarExcel(proyectoId, agrupacion, usuario, fechaInicio, fechaFin, tipoVisualizacion, lineaBase);
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
	}
	
	private byte[] exportarExcel(int prestamoId, int agrupacion, String usuario, Integer fechaInicial, Integer fechaFinal, Integer tipoVisualizacion, String lineaBase){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datosInforme[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders(fechaInicial, fechaFinal, agrupacion, tipoVisualizacion);
			List<stcomponentegestionadquisicion> lstPrestamo = generarPlan(prestamoId, usuario, fechaInicial, fechaFinal, lineaBase);	
			datosInforme = generarDatosReporte(lstPrestamo, fechaInicial, fechaFinal, agrupacion, tipoVisualizacion, headers[0].length, usuario);
			excel = new CExcel("Gestión de Adquisiciones", false, null);
			Proyecto proyecto = ProyectoDAO.getProyecto(prestamoId);
			wb=excel.generateExcelOfData(datosInforme, "Gestión de Adquisiciones - "+proyecto.getNombre(), headers, null, true, usuario);
			wb.write(outByteStream);
			outByteStream.close();
			outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("5", SInformacionPresupuestaria.class, e);
		}
		return outArray;
	}
	
	public String[][] generarDatosReporte(List<stcomponentegestionadquisicion> lstPrestamo, int anioInicio, int anioFin, int agrupacion, int tipoVisualizacion, int columnasTotal, String usuario){
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
				stcomponentegestionadquisicion prestamo = lstPrestamo.get(i);
				datos[i][columna] = prestamo.nombre;
				columna++;
				int posicion = columna;
				BigDecimal totalAniosP = new BigDecimal(0);
				//Valores planificado-real
				
				if(prestamo.anioPlan != null){
					stgestion[] anioPlan = (stgestion[])prestamo.anioPlan; 
					for(int a=0; a<anioPlan.length; a++){
						posicion = columna + (a*factorVisualizacion);
						
						for(int m=0; m<12; m++){
							anioPlan[a].mes[m].planificado= (anioPlan[a].mes[m].planificado==null ? new BigDecimal(0) : anioPlan[a].mes[m].planificado.setScale(2, BigDecimal.ROUND_DOWN));							
						}
						
						switch(agrupacion){
							case AGRUPACION_MES:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= anioPlan[a].mes[0].planificado.toString();
									datos[i][posicion+(1*sumaColumnas)]= anioPlan[a].mes[1].planificado.toString();
									datos[i][posicion+(2*sumaColumnas)]= anioPlan[a].mes[2].planificado.toString();
									datos[i][posicion+(3*sumaColumnas)]= anioPlan[a].mes[3].planificado.toString();
									datos[i][posicion+(4*sumaColumnas)]= anioPlan[a].mes[4].planificado.toString();
									datos[i][posicion+(5*sumaColumnas)]= anioPlan[a].mes[5].planificado.toString();
									datos[i][posicion+(6*sumaColumnas)]= anioPlan[a].mes[6].planificado.toString();
									datos[i][posicion+(7*sumaColumnas)]= anioPlan[a].mes[7].planificado.toString();
									datos[i][posicion+(8*sumaColumnas)]= anioPlan[a].mes[8].planificado.toString();
									datos[i][posicion+(9*sumaColumnas)]= anioPlan[a].mes[9].planificado.toString();
									datos[i][posicion+(10*sumaColumnas)]= anioPlan[a].mes[10].planificado.toString();
									datos[i][posicion+(11*sumaColumnas)]= anioPlan[a].mes[11].planificado.toString();
								}							
								posicion = posicion+(12*sumaColumnas)+1;
								break;
							case AGRUPACION_BIMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (anioPlan[a].mes[0].planificado.add(anioPlan[a].mes[1].planificado)).toString();
									datos[i][posicion+(1*sumaColumnas)]= (anioPlan[a].mes[2].planificado.add(anioPlan[a].mes[3].planificado)).toString();
									datos[i][posicion+(2*sumaColumnas)]= (anioPlan[a].mes[4].planificado.add(anioPlan[a].mes[5].planificado)).toString();
									datos[i][posicion+(3*sumaColumnas)]= (anioPlan[a].mes[6].planificado.add(anioPlan[a].mes[7].planificado)).toString();
									datos[i][posicion+(4*sumaColumnas)]= (anioPlan[a].mes[8].planificado.add(anioPlan[a].mes[9].planificado)).toString();
									datos[i][posicion+(5*sumaColumnas)]= (anioPlan[a].mes[10].planificado.add(anioPlan[a].mes[11].planificado)).toString();
								}
								posicion = posicion+(6*sumaColumnas)+1;
								break;
							case AGRUPACION_TRIMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (anioPlan[a].mes[0].planificado.add(anioPlan[a].mes[1].planificado.add(anioPlan[a].mes[2].planificado))).toString();
									datos[i][posicion+(1*sumaColumnas)]= (anioPlan[a].mes[3].planificado.add(anioPlan[a].mes[4].planificado.add(anioPlan[a].mes[5].planificado))).toString();
									datos[i][posicion+(2*sumaColumnas)]= (anioPlan[a].mes[6].planificado.add(anioPlan[a].mes[7].planificado.add(anioPlan[a].mes[8].planificado))).toString();
									datos[i][posicion+(3*sumaColumnas)]= (anioPlan[a].mes[9].planificado.add(anioPlan[a].mes[10].planificado.add(anioPlan[a].mes[11].planificado))).toString();
								}
								posicion = posicion+(4*sumaColumnas)+1;
								break;
							case AGRUPACION_CUATRIMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (anioPlan[a].mes[0].planificado.add(anioPlan[a].mes[1].planificado).add(anioPlan[a].mes[2].planificado.add(anioPlan[a].mes[3].planificado))).toString();
									datos[i][posicion+(1*sumaColumnas)]= (anioPlan[a].mes[4].planificado).add(anioPlan[a].mes[5].planificado.add(anioPlan[a].mes[6].planificado.add(anioPlan[a].mes[7].planificado))).toString();
									datos[i][posicion+(2*sumaColumnas)]= (anioPlan[a].mes[8].planificado.add(anioPlan[a].mes[9].planificado).add(anioPlan[a].mes[10].planificado.add(anioPlan[a].mes[11].planificado))).toString();
								}
								posicion = posicion+(3*sumaColumnas)+1;
								break;
							case AGRUPACION_SEMESTRE:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									datos[i][posicion]= (anioPlan[a].mes[0].planificado.add(anioPlan[a].mes[1].planificado).add(anioPlan[a].mes[2].planificado.add(anioPlan[a].mes[3].planificado.add(anioPlan[a].mes[4].planificado.add(anioPlan[a].mes[5].planificado))))).toString();
									datos[i][posicion+(1*sumaColumnas)]= (anioPlan[a].mes[6].planificado.add(anioPlan[a].mes[7].planificado).add(anioPlan[a].mes[8].planificado.add(anioPlan[a].mes[9].planificado.add(anioPlan[a].mes[10].planificado.add(anioPlan[a].mes[11].planificado))))).toString();
								}
								posicion = posicion+(2*sumaColumnas)+1;
								break;
							case AGRUPACION_ANUAL:
								if(tipoVisualizacion==0 || tipoVisualizacion==2){
									BigDecimal totalAnualP = new BigDecimal(0);
									for(int m=0; m<12; m++){
										totalAnualP = totalAnualP.add(anioPlan[a].mes[m].planificado);						
									}
									totalAniosP = totalAniosP.add(totalAnualP);
									datos[i][posicion]= totalAnualP.toString();
								}
								posicion = posicion+(1*sumaColumnas)+1;
								break;
							}
						}				
					}
					
					posicion = columnasTotal-2-sumaColumnas;
					if(prestamo.anioTotalGestion != null){
						for(int a=0; a < prestamo.anioTotalGestion.length; a++){	
							datos[i][posicion] = prestamo.anioTotalGestion[a].total[0].planificado.toString();
							posicion++;
						}
					}
					
					datos[i][columnasTotal-2] = prestamo.cantidadAdquisiciones != null ? prestamo.cantidadAdquisiciones.toString() : null;
					
					datos[i][columnasTotal-1] = prestamo.total != null ? prestamo.total.toString() : null;
				}
			}			
		return datos;
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
		
		int totalesCant = 2;
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
			sumtotalesCol[pos]="sum";
			pos++;
		}
		titulo[pos] = "Cantidad";
		tipo[pos] = "int";
		operacionesFila[pos] = "";
		sumtotalesCol[pos]="sum";
		columnasOperacion[pos]=totales[totalesCant-factorVisualizacion];
		pos++;
		titulo[pos] = "Total";
		tipo[pos] = "double";
		operacionesFila[pos]="sum";
		sumtotalesCol[pos]="sum";
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
	
	private List<stcomponentegestionadquisicion> generarPlan(Integer proyectoId, String usuario, Integer fechaInicial, Integer fechaFinal, String lineaBase) throws Exception{
		try{
			List<stcomponentegestionadquisicion> lstPrestamo = new ArrayList<>();
			
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(proyecto.getFechaInicio());
			int anioInicial = cal.get(Calendar.YEAR);
			
			List<ObjetoCosto> estructuraProyecto = ObjetoDAO.getEstructuraConCosto(proyectoId, anioInicial, fechaFinal, true, false, false, lineaBase, usuario);
			stcomponentegestionadquisicion temp = null;
			
			List<CategoriaAdquisicion> lstCategorias = CategoriaAdquisicionDAO.getCategoriaAdquisicion(); 
			List<stcategoriaG> lsttempCategorias = new ArrayList<stcategoriaG>();
			stcategoriaG tempCategoria = null;
			for(CategoriaAdquisicion categoria : lstCategorias){
				tempCategoria = new stcategoriaG();
				tempCategoria.categoriaId = categoria.getId();
				tempCategoria.nombre = categoria.getNombre();
				lsttempCategorias.add(tempCategoria);
			}
			
			for(stcategoriaG cat : lsttempCategorias){
				temp = new stcomponentegestionadquisicion();
				temp.nombre = cat.nombre;
				temp.anioPlan = inicializarStAnioGestion(fechaInicial, fechaFinal);
				temp.cantidadAdquisiciones = 0;
				for(ObjetoCosto objeto: estructuraProyecto){
					if(objeto.getObjeto_tipo() == 3 || objeto.getObjeto_tipo() == 4 || objeto.getObjeto_tipo() == 5){
						PlanAdquisicion lstplan = PlanAdquisicionDAO.getPlanAdquisicionByObjetoLB(objeto.getObjeto_tipo(), objeto.getObjeto_id(), lineaBase);
						if(lstplan != null){
							if(cat.categoriaId==lstplan.getCategoriaAdquisicion().getId()){
								temp.cantidadAdquisiciones++;
								temp.anioPlan = mergeGestionPlan(temp.anioPlan, objeto, fechaInicial, fechaFinal);
								temp.acumulado = getAcumuladoPlan(temp.anioPlan, objeto);
							}
						}
					}
				}
				
				if(temp.cantidadAdquisiciones > 0)
					lstPrestamo.add(temp);
			}
			
			for(int i = 0; i < lstPrestamo.size(); i++){
				lstPrestamo.get(i).anioTotalGestion = inicializarStTotalGestion(fechaInicial, fechaFinal);				
				for(int j=0; j < lstPrestamo.get(i).anioPlan.length; j++){
					for(int h=0; h < lstPrestamo.get(i).anioPlan[j].mes.length; h++){
						if(lstPrestamo.get(i).anioPlan[j].anio.equals(lstPrestamo.get(i).anioTotalGestion[j].anio)){
							lstPrestamo.get(i).anioTotalGestion[j].total[0].planificado  = lstPrestamo.get(i).anioTotalGestion[j].total[0].planificado.add(lstPrestamo.get(i).anioPlan[j].mes[h].planificado);
						}
					}
				}
			}
			
			for(stcomponentegestionadquisicion stprestamo : lstPrestamo){
				if(stprestamo.anioTotalGestion != null){
					stprestamo.total = new BigDecimal(0);
					for(int j = 0; j < stprestamo.anioTotalGestion.length; j++){
						for(int i = 0; i< stprestamo.anioTotalGestion[j].total.length; i++){
							stprestamo.total = stprestamo.total.add(stprestamo.anioTotalGestion[j].total[i].planificado);
						}
					}
				}
			}
			
			return lstPrestamo;
		} catch(Exception e){
			CLogger.write("1", SGestionAdquisiciones.class, e);
			return null;
		}
	}
	
	private sttotalGestion[] inicializarStTotalGestion(Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		sttotalGestion[] anios = new sttotalGestion[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			sttotalGestion temp = new sttotalGestion();
			for(int m=0; m<1; m++){
				temp.total[m]= new stpresupuestoGestion();
				temp.total[m].planificado = new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
	
	private stgestion[] mergeGestionPlan(Object anios, ObjetoCosto objetoAnios, Integer fechaInicial, Integer fechaFinal){
		stgestion[] aAnios = (stgestion[])anios;
		try{
			ObjetoCosto.stanio[] anioPlan = objetoAnios.getAnios();
			for(ObjetoCosto.stanio objAnioPlan : anioPlan){
				for(int i=0; i<aAnios.length; i++){
					if(objAnioPlan.anio.equals(aAnios[i].anio)){
						for(int j=0;j<12;j++){
							aAnios[i].mes[j].planificado = aAnios[i].mes[j].planificado.add(objAnioPlan.mes[j].planificado != null ? objAnioPlan.mes[j].planificado : new BigDecimal(0));	
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return aAnios;
	}
	
	private BigDecimal getAcumuladoPlan(Object anios, ObjetoCosto objetoAnios){
		BigDecimal totalAcumulado = new BigDecimal(0);
		try{
			ObjetoCosto.stanio[] anioPlan = objetoAnios.getAnios();
			for(ObjetoCosto.stanio objAnioPlan : anioPlan){
				for(int j=0;j<12;j++){
					totalAcumulado = totalAcumulado.add(objAnioPlan.mes[j].planificado);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return totalAcumulado;
	}

	private stgestion[] inicializarStAnioGestion(Integer anioInicial, Integer anioFinal){		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stgestion[] anios = new stgestion[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stgestion temp = new stgestion();
			for(int m=0; m<12; m++){
				temp.mes[m]= new stpresupuestoGestion();
				temp.mes[m].planificado = new BigDecimal(0);
			}
			temp.anio = anioInicial+i;
			anios[i] = temp;
		}
		return anios;
	}
}
