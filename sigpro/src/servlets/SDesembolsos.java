package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.DataSigadeDAO;
import dao.DesembolsoDAO;
import dao.PrestamoDAO;
import pojo.Prestamo;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CPdf;
import utilities.Utils;


@WebServlet("/SDesembolsos")
public class SDesembolsos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	final int AGRUPACION_MES= 1;
	final int AGRUPACION_BIMESTRE = 2;
	final int AGRUPACION_TRIMESTRE = 3;
	final int AGRUPACION_CUATRIMESTRE= 4;
	final int AGRUPACION_SEMESTRE= 5;
	final int AGRUPACION_ANUAL= 6;
	
	class stanio{
		Integer anio;
		BigDecimal[] mesPlanificado = new BigDecimal[12];
		BigDecimal[] mesReal = new BigDecimal[12];
		BigDecimal totalPlanificado;
		BigDecimal totalReal;
	}
	
    public SDesembolsos() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{\"success\": false }").append(request.getContextPath());
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
		}else if (accion.equals("exportarExcel")){
			Integer ejercicioFiscal = Utils.String2Int(map.get("ejercicioFiscal"));
			Integer proyectoId = Utils.String2Int(map.get("proyectoid"));
			Integer anioInicial = Utils.String2Int(map.get("anioInicial"));
			Integer anioFinal = Utils.String2Int(map.get("anioFinal"));
			Integer agrupacion = Utils.String2Int(map.get("agrupacion"));
			
			try{
		        byte [] outArray = exportarExcel(proyectoId, anioInicial, anioFinal, ejercicioFiscal, agrupacion, usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Desembolsos.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SDesembolsos.class, e);
			}
		}else if(accion.equals("exportarPdf")){
			Integer ejercicioFiscal = Utils.String2Int(map.get("ejercicioFiscal"));
			Integer proyectoId = Utils.String2Int(map.get("proyectoid"));
			Integer anioInicial = Utils.String2Int(map.get("anioInicial"));
			Integer anioFinal = Utils.String2Int(map.get("anioFinal"));
			Integer agrupacion = Utils.String2Int(map.get("agrupacion"));
			CPdf archivo = new CPdf("Desembolsos");
			String headers[][];
			String datos[][];
			headers = generarHeaders(anioInicial, anioFinal, ejercicioFiscal, agrupacion);
			datos = generarDatos(proyectoId, anioInicial, anioFinal, ejercicioFiscal, agrupacion, headers, usuario);
			String path = archivo.exportarDesembolsos(headers, datos,usuario);
			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
					CLogger.write("5", SDesembolsos.class, e);
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
				response.setHeader("Content-Disposition", "in-line; 'Desembolsos.pdf'");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
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
		
	private byte[] exportarExcel(int proyectoId, int anioInicio, int anioFin, int ejercicioFiscal, int agrupacion, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders(anioInicio, anioFin, ejercicioFiscal, agrupacion);
			datos = generarDatos(proyectoId, anioInicio, anioFin, ejercicioFiscal, agrupacion, headers, usuario);
			CGraficaExcel grafica = generarGrafica(datos, headers);
			excel = new CExcel("Desembolsos", false, grafica);
			wb=excel.generateExcelOfData(datos, "Desembolsos", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write_simple("2", SDesembolsos.class, e.getMessage());
		}
		return outArray;
	}
	
	private String[][] generarHeaders(int anioInicio, int anioFin, int ejercicioFiscal, int agrupacion){
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
		int columnasTotal = AgrupacionesTitulo[agrupacion-1].length;
				
		if(agrupacion == AGRUPACION_ANUAL){
			columnasTotal = aniosDiferencia;
		}
		columnasTotal = 1+columnasTotal+totalesCant;
				
		String titulo[] = new String[columnasTotal];
		String tipo[] = new String[columnasTotal];
		String columnasOperacion[] = new String[columnasTotal];
		String filasOperacion[] = new String[columnasTotal];
		String filasOperadores[]= new String[columnasTotal];
		String totalesFila = "";
		titulo[0]="Tipo";
		tipo[0]="string";
		columnasOperacion[0]="";
		filasOperacion[0]="";
		filasOperadores[0]="";
		
		int pos=1;
		
		if(agrupacion == AGRUPACION_ANUAL){
			for(int a=0; a<aniosDiferencia; a++){
				titulo[pos]=String.valueOf(anioInicio+a);
				tipo[pos]="currency";
				columnasOperacion[pos]="sub";
				filasOperacion[pos]="";
				filasOperadores[pos]="";
				totalesFila+=pos+",";
				pos++;
			}
		}else{
			for(int i=0; i<AgrupacionesTitulo[agrupacion-1].length; i++){
				titulo[pos] = AgrupacionesTitulo[agrupacion-1][i] + " " + (ejercicioFiscal);
				tipo[pos] = "currency";
				columnasOperacion[pos]="sub";
				filasOperacion[pos]="";
				filasOperadores[pos]="";
				totalesFila+=pos+",";
				pos++;
			}
		}
		titulo[pos] = "Total";
		tipo[pos] = "currency";
		columnasOperacion[pos]="sub";
		filasOperacion[pos]="sum";
		filasOperadores[pos]=totalesFila;
		
		headers = new String[][]{
			titulo,  //titulos
			null, //mapeo
			tipo, //tipo dato
			columnasOperacion, //operaciones columnas
			null, //operaciones div
			null,
			filasOperacion,
			filasOperadores
			};
			
		return headers;
	}
	
	public String[][] generarDatos(int proyectoId, int anioInicio, int anioFin, int ejercicioFiscal, int agrupacion, String[][] headers, String usuario){
		String[][] datos = new String[2][headers[0].length];
		
		Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyectoId, 1);
		
		for (int i=0; i<2; i++){
			for (int j=0; j<headers[0].length; j++){
				datos[i][j] = "0";
			}
		}
		
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
			
			datos[0][0]= "Planificado";
			datos[1][0]= "Real";
			
			Iterator<Integer> iterator = anios.iterator();
		    while(iterator.hasNext()) {
		        Integer anio = iterator.next();
				Integer columna = 1;
		        
		        Map<Integer,BigDecimal> planTemp = desembolsosPlanificado.get(anio);
		        Map<Integer,BigDecimal> realTemp = desembolsosReal.get(new BigDecimal(anio));
		        
				stanio anioDesembolso = new stanio();
				anioDesembolso.anio = anio;
				anioDesembolso.totalPlanificado = new BigDecimal(0);
				anioDesembolso.totalReal= new BigDecimal(0);
				
		        for (int i = 1; i <=12 ; i++){
		        	BigDecimal planificado=(planTemp!=null && planTemp.get(i)!=null ? planTemp.get(i) : new BigDecimal(0));
		        	BigDecimal real=(realTemp!=null && realTemp.get(i)!=null ?  realTemp.get(i) : new BigDecimal(0));
		        	anioDesembolso.mesPlanificado[i-1] = planificado;
		        	anioDesembolso.mesReal[i-1] = real;

		        	anioDesembolso.totalPlanificado = anioDesembolso.totalPlanificado.add(planificado);
		        	anioDesembolso.totalReal = anioDesembolso.totalReal.add(real);
		        }
		        
		        if(anio == ejercicioFiscal && agrupacion != AGRUPACION_ANUAL){
		        	switch(agrupacion){
					case AGRUPACION_MES:
							datos[0][columna]= anioDesembolso.mesPlanificado[0].toString();
							datos[0][columna+1]= anioDesembolso.mesPlanificado[1].toString();
							datos[0][columna+2]= anioDesembolso.mesPlanificado[2].toString();
							datos[0][columna+3]= anioDesembolso.mesPlanificado[3].toString();
							datos[0][columna+4]= anioDesembolso.mesPlanificado[4].toString();
							datos[0][columna+5]= anioDesembolso.mesPlanificado[5].toString();
							datos[0][columna+6]= anioDesembolso.mesPlanificado[6].toString();
							datos[0][columna+7]= anioDesembolso.mesPlanificado[7].toString();
							datos[0][columna+8]= anioDesembolso.mesPlanificado[8].toString();
							datos[0][columna+9]= anioDesembolso.mesPlanificado[9].toString();
							datos[0][columna+10]= anioDesembolso.mesPlanificado[10].toString();
							datos[0][columna+11]= anioDesembolso.mesPlanificado[11].toString();
							datos[0][columna+12]= anioDesembolso.totalPlanificado.toString();
							
							datos[1][columna]= anioDesembolso.mesReal[0].toString();
							datos[1][columna+1]= anioDesembolso.mesReal[1].toString();
							datos[1][columna+2]= anioDesembolso.mesReal[2].toString();
							datos[1][columna+3]= anioDesembolso.mesReal[3].toString();
							datos[1][columna+4]= anioDesembolso.mesReal[4].toString();
							datos[1][columna+5]= anioDesembolso.mesReal[5].toString();
							datos[1][columna+6]= anioDesembolso.mesReal[6].toString();
							datos[1][columna+7]= anioDesembolso.mesReal[7].toString();
							datos[1][columna+8]= anioDesembolso.mesReal[8].toString();
							datos[1][columna+9]= anioDesembolso.mesReal[9].toString();
							datos[1][columna+10]= anioDesembolso.mesReal[10].toString();
							datos[1][columna+11]= anioDesembolso.mesReal[11].toString();
							datos[1][columna+12]= anioDesembolso.totalReal.toString();
						break;
					case AGRUPACION_BIMESTRE:
							datos[0][columna]= (anioDesembolso.mesPlanificado[0].add(anioDesembolso.mesPlanificado[1])).toString();
							datos[0][columna+1]= (anioDesembolso.mesPlanificado[2].add(anioDesembolso.mesPlanificado[3])).toString();
							datos[0][columna+2]= (anioDesembolso.mesPlanificado[4].add(anioDesembolso.mesPlanificado[5])).toString();
							datos[0][columna+3]= (anioDesembolso.mesPlanificado[6].add(anioDesembolso.mesPlanificado[7])).toString();
							datos[0][columna+4]= (anioDesembolso.mesPlanificado[8].add(anioDesembolso.mesPlanificado[9])).toString();
							datos[0][columna+5]= (anioDesembolso.mesPlanificado[10].add(anioDesembolso.mesPlanificado[11])).toString();
							datos[0][columna+6]= anioDesembolso.totalPlanificado.toString();

							datos[1][columna]= (anioDesembolso.mesReal[0].add(anioDesembolso.mesReal[1])).toString();
							datos[1][columna+1]= (anioDesembolso.mesReal[2].add(anioDesembolso.mesReal[3])).toString();
							datos[1][columna+2]= (anioDesembolso.mesReal[4].add(anioDesembolso.mesReal[5])).toString();
							datos[1][columna+3]= (anioDesembolso.mesReal[6].add(anioDesembolso.mesReal[7])).toString();
							datos[1][columna+4]= (anioDesembolso.mesReal[8].add(anioDesembolso.mesReal[9])).toString();
							datos[1][columna+5]= (anioDesembolso.mesReal[10].add(anioDesembolso.mesReal[11])).toString();
							datos[1][columna+6]= anioDesembolso.totalReal.toString();
						break;
					case AGRUPACION_TRIMESTRE:
							datos[0][columna]= (anioDesembolso.mesPlanificado[0].add(anioDesembolso.mesPlanificado[1].add(anioDesembolso.mesPlanificado[2]))).toString();
							datos[0][columna+1]= (anioDesembolso.mesPlanificado[3].add(anioDesembolso.mesPlanificado[4].add(anioDesembolso.mesPlanificado[5]))).toString();
							datos[0][columna+2]= (anioDesembolso.mesPlanificado[6].add(anioDesembolso.mesPlanificado[7].add(anioDesembolso.mesPlanificado[8]))).toString();
							datos[0][columna+3]= (anioDesembolso.mesPlanificado[9].add(anioDesembolso.mesPlanificado[10].add(anioDesembolso.mesPlanificado[11]))).toString();
							datos[0][columna+4]= anioDesembolso.totalPlanificado.toString();
						
							datos[1][columna]= (anioDesembolso.mesReal[0].add(anioDesembolso.mesReal[1].add(anioDesembolso.mesReal[2]))).toString();
							datos[1][columna+1]= (anioDesembolso.mesReal[3].add(anioDesembolso.mesReal[4].add(anioDesembolso.mesReal[5]))).toString();
							datos[1][columna+2]= (anioDesembolso.mesReal[6].add(anioDesembolso.mesReal[7].add(anioDesembolso.mesReal[8]))).toString();
							datos[1][columna+3]= (anioDesembolso.mesReal[9].add(anioDesembolso.mesReal[10].add(anioDesembolso.mesReal[11]))).toString();
							datos[1][columna+4]= anioDesembolso.totalReal.toString();
						break;
					case AGRUPACION_CUATRIMESTRE:
							datos[0][columna]= (anioDesembolso.mesPlanificado[0].add(anioDesembolso.mesPlanificado[1]).add(anioDesembolso.mesPlanificado[2].add(anioDesembolso.mesPlanificado[3]))).toString();
							datos[0][columna+1]= (anioDesembolso.mesPlanificado[4].add(anioDesembolso.mesPlanificado[5].add(anioDesembolso.mesPlanificado[6].add(anioDesembolso.mesPlanificado[7])))).toString();
							datos[0][columna+2]= (anioDesembolso.mesPlanificado[8].add(anioDesembolso.mesPlanificado[9]).add(anioDesembolso.mesPlanificado[10].add(anioDesembolso.mesPlanificado[11]))).toString();
							datos[0][columna+3]= anioDesembolso.totalPlanificado.toString();

							datos[1][columna]= (anioDesembolso.mesReal[0].add(anioDesembolso.mesReal[1].add(anioDesembolso.mesReal[2].add(anioDesembolso.mesReal[3])))).toString();
							datos[1][columna+1]= (anioDesembolso.mesReal[4]).add(anioDesembolso.mesReal[5].add(anioDesembolso.mesReal[6].add(anioDesembolso.mesReal[7]))).toString();
							datos[1][columna+2]= (anioDesembolso.mesReal[8].add(anioDesembolso.mesReal[9]).add(anioDesembolso.mesReal[10].add(anioDesembolso.mesReal[11]))).toString();
							datos[1][columna+3]= anioDesembolso.totalReal.toString();
						break;
					case AGRUPACION_SEMESTRE:
							datos[0][columna]= (anioDesembolso.mesPlanificado[0].add(anioDesembolso.mesPlanificado[1]).add(anioDesembolso.mesPlanificado[2].add(anioDesembolso.mesPlanificado[3].add(anioDesembolso.mesPlanificado[4].add(anioDesembolso.mesPlanificado[5]))))).toString();
							datos[0][columna+1]= (anioDesembolso.mesPlanificado[6].add(anioDesembolso.mesPlanificado[7]).add(anioDesembolso.mesPlanificado[8].add(anioDesembolso.mesPlanificado[9].add(anioDesembolso.mesPlanificado[10].add(anioDesembolso.mesPlanificado[11]))))).toString();
							datos[0][columna+2]= anioDesembolso.totalPlanificado.toString();

							datos[1][columna]= (anioDesembolso.mesReal[0].add(anioDesembolso.mesReal[1]).add(anioDesembolso.mesReal[2].add(anioDesembolso.mesReal[3].add(anioDesembolso.mesReal[4].add(anioDesembolso.mesReal[5]))))).toString();
							datos[1][columna+1]= (anioDesembolso.mesReal[6].add(anioDesembolso.mesReal[7]).add(anioDesembolso.mesReal[8].add(anioDesembolso.mesReal[9].add(anioDesembolso.mesReal[10].add(anioDesembolso.mesReal[11]))))).toString();
							datos[1][columna+2]= anioDesembolso.totalReal.toString();
						break;
					}
		        	break;
	        	}
		         
		        if(agrupacion == AGRUPACION_ANUAL){
		        	datos[0][anioDesembolso.anio-anioInicio+1] = anioDesembolso.totalPlanificado.toString();
		        	datos[1][anioDesembolso.anio-anioInicio+1] = anioDesembolso.totalReal.toString();
		        }
		        columna++;
		    }
		}
					
		return datos;
	}
	
	public CGraficaExcel generarGrafica(String[][] datosTabla, String[][] headers){
		
		String[][] datos = new String[3][datosTabla[0].length-2];
		String[][] datosIgualar = new String[3][datosTabla[0].length-2];
		String[] tipoData = new String[]{"string","currency","currency"};
				
		for(int c=1; c<datosTabla[0].length-1; c++){
			datos[0][c-1] = headers[0][c];
			datos[1][c-1] = datosTabla[0][c];
			datos[2][c-1] = datosTabla[1][c];
			datosIgualar[0][c-1]="";
			datosIgualar[1][c-1]=(c)+"."+(datosTabla.length-2+30);
			datosIgualar[2][c-1]=(c)+"."+(datosTabla.length-1+30);
			
		}
		
		CGraficaExcel grafica = new CGraficaExcel("Desembolsos", CGraficaExcel.EXCEL_CHART_AREA, "Meses", "Planificado", datos, tipoData, datosIgualar);
	
		return grafica;
	}

}
