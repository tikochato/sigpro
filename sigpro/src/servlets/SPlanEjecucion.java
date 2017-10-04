package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.DataSigadeDAO;
import dao.DesembolsoDAO;
import dao.MetaDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Meta;
import pojo.MetaPlanificado;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanEjecucion")
public class SPlanEjecucion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SPlanEjecucion() {
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
		Date fecha_actual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
		if(accion.equals("getDatosPlan")){
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyectoId, 1);
			
			BigDecimal ejecucionFisicaReal = calcularEjecucionFisicaReal(proyecto);
			
			BigDecimal ejecucionFinancieraPlanificada = calcularEjecucionFinanciaeraPlanificada(proyectoId, prestamo.getCodigoPresupuestario()+"", new Date());
			Double plazoEjecucionPlanificada = calcularPlazoEjecucionPlanificada(proyecto);
			BigDecimal ejecucionFisicaPlanificada = calcularEjecucionFisicaPlanificada(proyectoId);
			
			DecimalFormat df = new DecimalFormat();
		    df.setMinimumFractionDigits(2);
		    df.setMaximumFractionDigits(2);
		    
			response_text = String.join("", "{ \"success\": true ,",
					"\"ejecucionFisicaR\": \"" , df.format((ejecucionFisicaReal.floatValue() )) + "" , "\",",
					"\"ejecucionFinancieraP\": \"" , df.format((ejecucionFinancieraPlanificada.floatValue() * 100.00)) + "" , "\",",
					"\"plazoEjecucionP\": \"" , df.format((plazoEjecucionPlanificada.floatValue() * 100.00)) + "" , "\",",
					"\"ejecucionFisicaP\": \"" , df.format((ejecucionFisicaPlanificada.floatValue() * 100.00)) + "" , "\",",
					
					"\"fecha\": \"" , sdf.format(fecha_actual), "\"",
					"}");
			
		}else if(accion.equals("exportarExcel")){
			try{
				int idPrestamo = Utils.String2Int(map.get("id"),0);
				Double plazoEjecucion = Double.parseDouble(map.get("plazoEjecucion") != null ? map.get("plazoEjecucion") : "0");
				
				
		        byte [] outArray = exportarExcel(idPrestamo, usuario, plazoEjecucion);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Plan_de_Ejecución.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
			    CLogger.write("1", SPlanEjecucion.class, e);
			}
			
		}else{
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
	
	public BigDecimal calcularEjecucionFisicaReal(Proyecto proyecto){
		BigDecimal ejecucionFisica = new BigDecimal(proyecto.getEjecucionFisicaReal()!= null ? 
				proyecto.getEjecucionFisicaReal():0);
		return ejecucionFisica;
	}
	
	public BigDecimal calcularEjecucionFinanciaeraPlanificada(int proyectoId, String codigoPresupuestario,Date fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));
		BigDecimal totalDesembolsosReales = 
				DataSigadeDAO.totalDesembolsadoAFechaReal(codigoPresupuestario,new Long( anio), mes);
		BigDecimal totalDesembolsosPlanificados = 
				DesembolsoDAO.getTotalDesembolsosFuturos(proyectoId,   fecha);
		BigDecimal  ejecucionFinanciera = new BigDecimal("0");
		if (totalDesembolsosReales!= null && totalDesembolsosPlanificados != null){
			BigDecimal total = totalDesembolsosReales.add( totalDesembolsosPlanificados);
			ejecucionFinanciera = totalDesembolsosReales.divide(total,2,BigDecimal.ROUND_HALF_UP);
		}
		return ejecucionFinanciera;
	}
	
	public Double calcularPlazoEjecucionPlanificada(Proyecto proyecto){	
		if (proyecto!=null && proyecto.getFechaInicio()!=null && proyecto.getFechaFin()!=null ){
			
			Long hoy = new Date().getTime();
			Long inicio = proyecto.getFechaInicio().getTime();			
			Long fin = proyecto.getFechaFin().getTime();
			
			Long total = fin - inicio;
			Long transcurrido = hoy - inicio;
			
			return transcurrido*1.0/total;
			
		}else
			return 0.0;
		
	}
	
public BigDecimal calcularEjecucionFisicaPlanificada(Integer proyectoId){
		
		List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyectoId, null);
		BigDecimal ejecucionFisica = new BigDecimal("0");
		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));
		
		for(Producto producto : productos){
			
			List<Meta> metas = MetaDAO.getMetasPorObjeto(producto.getId(), 3);
			BigDecimal metaFinal = new BigDecimal(0);
			BigDecimal metaPlanificada = new BigDecimal(0);
			for (Meta meta : metas){
				if (meta!=null){
				metaFinal = metaFinal.add( meta.getObjetoTipo()!=null &&  meta.getObjetoTipo()==2 ? 
						new BigDecimal(meta.getMetaFinalEntero() != null ? meta.getMetaFinalEntero() : 0) : 
							(meta.getMetaFinalDecimal() != null ? meta.getMetaFinalDecimal() : new BigDecimal(0)) );
				
				List<MetaPlanificado> planificadas = MetaDAO.getMetasPlanificadas(meta.getId());
				for (MetaPlanificado planificado : planificadas){
					if (planificado.getId().getEjercicio() < anio){
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getEneroEntero()!= null ? planificado.getEneroEntero() : 0) :
									(planificado.getEneroDecimal() !=null ? planificado.getEneroDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getFebreroEntero()!= null ? planificado.getFebreroEntero() : 0) :
									(planificado.getFebreroDecimal() !=null ? planificado.getFebreroDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getMarzoEntero()!= null ? planificado.getMarzoEntero() : 0) :
									(planificado.getMarzoDecimal() !=null ? planificado.getMarzoDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getAbrilEntero()!= null ? planificado.getAbrilEntero() : 0) :
									(planificado.getAbrilDecimal() !=null ? planificado.getAbrilDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getMayoEntero()!= null ? planificado.getMayoEntero() : 0) :
									(planificado.getMayoDecimal() !=null ? planificado.getMayoDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getJunioEntero()!= null ? planificado.getJunioEntero() : 0) :
									(planificado.getJunioDecimal() !=null ? planificado.getJunioDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getJulioEntero()!= null ? planificado.getJulioEntero() : 0) :
									(planificado.getJulioDecimal() !=null ? planificado.getJulioDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getAgostoEntero()!= null ? planificado.getAgostoEntero() : 0) :
									(planificado.getAgostoDecimal() !=null ? planificado.getAgostoDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getSeptiembreEntero()!= null ? planificado.getSeptiembreEntero() : 0) :
									(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getOctubreEntero()!= null ? planificado.getOctubreEntero() : 0) :
									(planificado.getOctubreDecimal() !=null ? planificado.getOctubreDecimal() : new BigDecimal(0)));
						
						metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
									(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
						
						metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
								new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
									(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
					}else if (planificado.getId().getEjercicio() == anio) {
						for (int i = 1 ; i <= mes;i++){
							switch (i){
								case 1:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getEneroEntero()!= null ? planificado.getEneroEntero() : 0) :
												(planificado.getEneroDecimal() !=null ? planificado.getEneroDecimal() : new BigDecimal(0)));
									break;
								case 2:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getFebreroEntero()!= null ? planificado.getFebreroEntero() : 0) :
												(planificado.getFebreroDecimal() !=null ? planificado.getFebreroDecimal() : new BigDecimal(0)));
									break;
								case 3:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getMarzoEntero()!= null ? planificado.getMarzoEntero() : 0) :
												(planificado.getMarzoDecimal() !=null ? planificado.getMarzoDecimal() : new BigDecimal(0)));
									break;
								case 4:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getAbrilEntero()!= null ? planificado.getAbrilEntero() : 0) :
												(planificado.getAbrilDecimal() !=null ? planificado.getAbrilDecimal() : new BigDecimal(0)));
									break;
								case 5:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getMayoEntero()!= null ? planificado.getMayoEntero() : 0) :
												(planificado.getMayoDecimal() !=null ? planificado.getMayoDecimal() : new BigDecimal(0)));
									break;
								case 6:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getJunioEntero()!= null ? planificado.getJunioEntero() : 0) :
												(planificado.getJunioDecimal() !=null ? planificado.getJunioDecimal() : new BigDecimal(0)));
									break;
								case 7:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getJulioEntero()!= null ? planificado.getJulioEntero() : 0) :
												(planificado.getJulioDecimal() !=null ? planificado.getJulioDecimal() : new BigDecimal(0)));
									break;
								case 8:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getAgostoEntero()!= null ? planificado.getAgostoEntero() : 0) :
												(planificado.getAgostoDecimal() !=null ? planificado.getAgostoDecimal() : new BigDecimal(0)));
									break;
								case 9:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getSeptiembreEntero()!= null ? planificado.getSeptiembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
									break;
								case 10:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getOctubreEntero()!= null ? planificado.getOctubreEntero() : 0) :
												(planificado.getOctubreDecimal() !=null ? planificado.getOctubreDecimal() : new BigDecimal(0)));
									break;
								case 11:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
									break;
								case 12:
									metaPlanificada = metaPlanificada.add(meta.getObjetoTipo() == 2 ? 
											new BigDecimal(planificado.getNoviembreEntero()!= null ? planificado.getNoviembreEntero() : 0) :
												(planificado.getSeptiembreDecimal() !=null ? planificado.getSeptiembreDecimal() : new BigDecimal(0)));
									break;
							}
						}
					}	
				}	
			}
			
			if (metaPlanificada!=null && metaFinal!=null 
					&& !metaFinal.equals(new BigDecimal(0)) && productos.size() > 0  ){
				ejecucionFisica = ejecucionFisica.add(metaPlanificada.divide(metaFinal, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(
						(producto.getPeso() !=null ? (double) producto.getPeso() : (Double) (100.0 / productos.size())) / 100)));
			}
			}
		}
		return ejecucionFisica; 
		
	
	}

	
	private byte[] exportarExcel(int idPrestamo, String usuario, Double plazoEjecucion
			) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(idPrestamo, usuario,plazoEjecucion);
			//CGraficaExcel grafica = generarGrafica(datos);
			excel = new CExcel("Plan de Ejecucion", false, null);
			wb=excel.generateExcelOfData(datos, "Plan de Ejecución", headers, null, false, usuario);
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
		    CLogger.write("1", SCargaTrabajo.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{" ", " ", " ", " "},  //titulos
			null, //mapeo
			{"string", "string", "string", "string"}, //tipo dato
			{"", "", "", ""}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}

	public String[][] generarDatos(int idPrestamo, String usuario,Double plazoEjecucion){
		Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(idPrestamo, 1);
		Date fecha_actual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String[][] datos = new String[20][4];
		for (int i=0; i<14; i++){
			for (int j=0; j<4; j++){
				datos[i][j] = "";
			}
		}
		
		datos[0][1] = "Mes Reportado";
		datos[0][2] = obtenerMes(Integer.parseInt(sdf.format(fecha_actual)));
		datos[1][1] = "Año Fiscal";
		sdf = new SimpleDateFormat("YYYY");
		datos [1][2] = sdf.format(fecha_actual);
		datos[2][1] = "Proyecto/Programa";
		datos[2][2] = prestamo.getProyectoPrograma();
		datos[3][1] = "Organismo Ejecutor";
		datos[3][2] = prestamo.getUnidadEjecutora().getEntidad().getNombre();
		
		datos[5][0] = "Número del Préstamo";
		datos[5][1]= prestamo.getNumeroPrestamo();
		datos[5][2] = "Fecha ulimta actualización";
		datos[5][3] = Utils.formatDate(prestamo.getFechaActualizacion());
		
		datos[6][0] = "Código Presupuestario";
		datos[6][1]= prestamo.getCodigoPresupuestario() + "";
		datos[6][2] = "Fecha del decreto";
		datos[6][3] = Utils.formatDate(prestamo.getFechaDecreto());
		
		datos[7][0] = "Organismo Financiero";
		datos[7][1]= prestamo.getCooperante().getNombre();
		datos[7][2] = "Fecha del suscripción";
		datos[7][3] = Utils.formatDate(prestamo.getFechaSuscripcion());
		
		datos[8][0] = "Organismo Financiero";
		datos[8][1]= prestamo.getCooperante().getNombre();
		datos[8][2] = "Fecha del suscripción";
		datos[8][3] = Utils.formatDate(prestamo.getFechaSuscripcion());
		
		datos[9][0] = "Entidad Ejecutora";
		datos[9][1]= prestamo.getUnidadEjecutora().getEntidad().getNombre();
		datos[9][2] = "Fecha de vigencia";
		datos[9][3] = Utils.formatDate(prestamo.getFechaVigencia());
		
		datos[10][0] = "Unidad Ejecutroa";
		datos[10][1]= prestamo.getUnidadEjecutora().getNombre();
		datos[10][2] = "Fecha de elegibilidad";
		datos[10][3] = Utils.formatDate(prestamo.getFechaElegibilidadUe());
		
		datos[11][0] = "Moneda de préstamo";
		datos[11][1]= prestamo.getTipoMoneda().getNombre();
		datos[11][2] = "Fecha de cierre";
		datos[11][3] = Utils.formatDate(prestamo.getFechaCierreActualUe());
		
		datos[12][0] = "Monto aprobado";
		datos[12][1]=  "$" + prestamo.getMontoContratadoUsd().toString();
		datos[12][2] = "Meses de prorroga";
		datos[12][3] = prestamo.getMesesProrrogaUe()+ "";
		
		datos[13][0] = "Monto aprobado Q";
		datos[13][1]=  "Q" + prestamo.getMontoContratadoQtz();
		datos[13][2] = "Plazo ejecucion";
		datos[13][3] = plazoEjecucion.toString();
		
		
		return datos;
	}
	
	public String obtenerMes(int mes){
		switch (mes){
			case 1: return "Enero";
			case 2: return "Febrero";
			case 3: return "Marzo";
			case 4: return "Abril";
			case 5: return "Mayo";
			case 6: return "Junio";
			case 7: return "Julio";
			case 8: return "Agosto";
			case 9: return "Septiembre";
			case 10: return "Ocutubre";
			case 11: return "Noviembre";
			case 12: return "Diciembre";
			
		}
		return "";
	}
}
