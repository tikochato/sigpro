package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.ComponenteDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.ReporteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SInformePresupuesto")
public class SInformePresupuesto extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
	private static int AGRUPACION_MES= 1;
	private static int AGRUPACION_BIMESTRE = 2;
	private static int AGRUPACION_TRIMESTRE = 3;
	private static int AGRUPACION_CUATRIMESTRE= 4;
	private static int AGRUPACION_SEMESTRE= 5;
	private static int AGRUPACION_ANUAL= 6;
	private static String[] MES_NAME = {"mes1","mes2","mes3","mes4","mes5","mes6","mes7","mes8","mes9","mes10","mes11","mes12"};
	private static String[] BIMESTRE_NAME = {"Bimestre1", "Bimestre2", "Bimestre3", "Bimestre4", "Bimestre5", "Bimestre6"};
	private static String[] TRIMESTRE_NAME = {"Trimestre1", "Trimestre2","Trimestre3","Trimestre4"};
	private static String[] CUATRIMESTRE_NAME = {"Cuatrimestre1","Cuatrimestre2","Cuatrimestre3"};
	private static String[] SEMESTRE_NAME = {"Semestre1","Semestre2"}; 	
	
	String[] columnaNames = null;
	List<Integer> actividadesCosto = null;
       
    public SInformePresupuesto() {
        super();
   }

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
		Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
		Integer anoInicial = Utils.String2Int(map.get("anoInicial"));
		Integer anoFinal = Utils.String2Int(map.get("anoFinal"));
		
		columnaNames = map.get("columnaNames").split(",");
		
		if(accion.equals("getAdquisicionesPrestamo")){
			Map<String, Map<String, Object>> prestamo = obtenerProyecto(idPrestamo,usuario, anoInicial, anoFinal);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(prestamo);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if(accion.equals("generarInforme")){
			actividadesCosto = new ArrayList<Integer>();
			Integer acumulacionCosto = 3;
			Map<String, Map<String, Object>> resultPrestamo = obtenerProyecto(idPrestamo,usuario, anoInicial, anoFinal);
			Integer agrupacion = Utils.String2Int(map.get("agrupacion"));
			
			/*for(Integer actividadId : actividadesCosto){
				Map<String, Object> rowEntity = resultPrestamo.get(actividadId+","+5);
				
				if((Integer)rowEntity.get("objetoTipo") == 5 && rowEntity.get("hijos") != ""){
					String[] fechaInicioSplit = null;
					String[] fechaFinSplit = null;
					String fechaInicio = (String)rowEntity.get("fechaInicio");
					String fechaFin = (String)rowEntity.get("fechaFin");
					
					if(rowEntity.get("acumulacionCostos") != null){
						acumulacionCosto = (Integer)rowEntity.get("acumulacionCostos");
						if((Integer)rowEntity.get("acumulacionCostos") == 1){
							fechaInicioSplit = fechaInicio.split("/");
						}else if((Integer)rowEntity.get("acumulacionCostos") == 2){
							fechaInicioSplit = fechaInicio.split("/");
							fechaFinSplit = fechaFin.split("/");
							
							if(validaProrrateo(fechaInicioSplit,fechaFinSplit, agrupacion) == true)
								acumulacionCosto = 3;
						}else if((Integer)rowEntity.get("acumulacionCostos") == 3){
							fechaFinSplit = fechaFin.split("/");
						}
					}else
						fechaFinSplit = fechaFin.split("/");
					
					if(acumulacionCosto == 2){
						rowEntity = Prorrateo(rowEntity, agrupacion, resultPrestamo, fechaInicioSplit, fechaFinSplit, rowEntity.get("Costo"), rowEntity.get("CostoReal"), acumulacionCosto);
					}else{
						rowEntity = calcular(rowEntity, agrupacion, resultPrestamo, fechaInicioSplit, fechaFinSplit, rowEntity.get("Costo"), rowEntity.get("CostoReal"), acumulacionCosto);
					}
				}
			}*/
			
			List<Map<String, Object>> resultado = new ArrayList<Map<String, Object>>(resultPrestamo.values());
			
			for(Map<String, Object> prestamo : resultado){
				Integer posicion = (Integer)prestamo.get("posicionArbol");
				prestamo.put("$$treeLevel", posicion -1);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(resultado);
	        response_text = String.join("", "\"prestamo\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
		}else if(accion.equals("exportarExcel")){
			String data = map.get("data");
			String columnas = map.get("columnas");
			String cabeceras = map.get("cabeceras");
			String[] col = cabeceras.split(",");
			Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
			List<Map<String, String>> datos = gson.fromJson(data, listType);

			String nombreInforme = "Informe Ejecución";
			Map<String,Object[]> reporte = new HashMap<>();
			Object[] obj = new Object[col.length];
			
			for(int i=0; i< col.length;i++){
				obj[i] = col[i];
			}
			
			reporte.put("0", obj);
			
			col = columnas.split(",");
			
			obj = new Object[col.length];
			int fila = 1;
			for(Map<String, String> d : datos){
				for(int i=0; i< col.length;i++){
					if(!col[i].equals("nombre"))
						obj[i] = new BigDecimal(d.get(col[i])).doubleValue();
					else
						obj[i] = d.get(col[i]);
				}
				reporte.put(fila+"",obj);
				fila++;
				obj = new Object[col.length];
			}
			
			exportarExcel(reporte,nombreInforme,usuario,response);
		}
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
	}	
	
	private boolean validaProrrateo(String[] fechaInicioSplit, String[] fechaFinSplit, int agrupacion){
		boolean pertenecen = true;
		if(Utils.String2Int(fechaInicioSplit[2]) == Utils.String2Int(fechaFinSplit[2]))
			return pertenecen;
		else{
			if(agrupacion == AGRUPACION_MES){
				if(Utils.String2Int(fechaInicioSplit[1]) != Utils.String2Int(fechaFinSplit[1]))
					pertenecen = false;					
			}else if(agrupacion == AGRUPACION_BIMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >= 1 && Utils.String2Int(fechaInicioSplit[1]) <= 2){
					if(Utils.String2Int(fechaFinSplit[1]) >= 1 && Utils.String2Int(fechaFinSplit[1]) <= 2)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 3 && Utils.String2Int(fechaInicioSplit[1]) <= 4){
					if(Utils.String2Int(fechaFinSplit[1]) >= 3 && Utils.String2Int(fechaFinSplit[1]) <= 4)
						pertenecen = true;
					else
						pertenecen = false;	
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 5 && Utils.String2Int(fechaInicioSplit[1]) <= 6){
					if(Utils.String2Int(fechaFinSplit[1]) >= 5 && Utils.String2Int(fechaFinSplit[1]) <= 6)
						pertenecen = true;
					else
						pertenecen = false;	
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 7 && Utils.String2Int(fechaInicioSplit[1]) <= 8){
					if(Utils.String2Int(fechaFinSplit[1]) >= 7 && Utils.String2Int(fechaFinSplit[1]) <= 8)
						pertenecen = true;
					else
						pertenecen = false;	
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 9 && Utils.String2Int(fechaInicioSplit[1]) <= 10){
					if(Utils.String2Int(fechaFinSplit[1]) >= 9 && Utils.String2Int(fechaFinSplit[1]) <= 10)
						pertenecen = true;
					else
						pertenecen = false;	
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 11 && Utils.String2Int(fechaInicioSplit[1]) <= 12){
					if(Utils.String2Int(fechaFinSplit[1]) >= 11 && Utils.String2Int(fechaFinSplit[1]) <= 12)
						pertenecen = true;
					else
						pertenecen = false;	
				}	
			}else if(agrupacion == AGRUPACION_TRIMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >= 1 && Utils.String2Int(fechaInicioSplit[1]) <= 3){
					if(Utils.String2Int(fechaFinSplit[1]) >= 1 && Utils.String2Int(fechaFinSplit[1]) <= 3)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 4 && Utils.String2Int(fechaInicioSplit[1]) <= 6){
					if(Utils.String2Int(fechaFinSplit[1]) >= 4 && Utils.String2Int(fechaFinSplit[1]) <= 6)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 7 && Utils.String2Int(fechaInicioSplit[1]) <= 9){
					if(Utils.String2Int(fechaFinSplit[1]) >= 7 && Utils.String2Int(fechaFinSplit[1]) <= 9)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 10 && Utils.String2Int(fechaInicioSplit[1]) <= 12){
					if(Utils.String2Int(fechaFinSplit[1]) >= 10 && Utils.String2Int(fechaFinSplit[1]) <= 12)
						pertenecen = true;
					else
						pertenecen = false;
				}
			}else if(agrupacion == AGRUPACION_CUATRIMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >= 1 && Utils.String2Int(fechaInicioSplit[1]) <= 4){
					if(Utils.String2Int(fechaFinSplit[1]) >= 1 && Utils.String2Int(fechaFinSplit[1]) <= 4)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 5 && Utils.String2Int(fechaInicioSplit[1]) <= 8){
					if(Utils.String2Int(fechaFinSplit[1]) >= 5 && Utils.String2Int(fechaFinSplit[1]) <= 8)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 9 && Utils.String2Int(fechaInicioSplit[1]) <= 12){
					if(Utils.String2Int(fechaFinSplit[1]) >= 9 && Utils.String2Int(fechaFinSplit[1]) <= 12)
						pertenecen = true;
					else
						pertenecen = false;
				}
			}else if(agrupacion == AGRUPACION_SEMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >= 1 && Utils.String2Int(fechaInicioSplit[1]) <= 6){
					if(Utils.String2Int(fechaFinSplit[1]) >= 1 && Utils.String2Int(fechaFinSplit[1]) <= 6)
						pertenecen = true;
					else
						pertenecen = false;
				}else if(Utils.String2Int(fechaInicioSplit[1]) >= 7 && Utils.String2Int(fechaInicioSplit[1]) <= 12){
					if(Utils.String2Int(fechaFinSplit[1]) >= 7 && Utils.String2Int(fechaFinSplit[1]) <= 12)
						pertenecen = true;
					else
						pertenecen = false;
				}
			}
		}
		return pertenecen;
	}
	
	private Map<String, Object> Prorrateo(Map<String, Object> entity, Integer agrupacion, Map<String, Map<String, Object>> resultadoPrestamo, 
			String[] fechaInicioSplit, String[] fechaFinSplit, Object costo, Object costoReal, int acumulacionCosto){
		
		String mesP = "";
		String mesR = "";
		List<Double> diasPorcentual = new ArrayList<Double>();
		
		if(fechaInicioSplit[1] != fechaFinSplit[1]){
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date fechaInicial = formatter.parse((String)entity.get("fechaInicio"));
				Date fechaFin = formatter.parse((String)entity.get("fechaFin"));
				
				long diferenciaEn_ms = fechaFin.getTime() - fechaInicial.getTime();
				long diasTotales = diferenciaEn_ms / (1000 * 60 * 60 * 24);
			    int d = 0;
			    int contador = 0;
				int inicio = Utils.String2Int(fechaInicioSplit[1]) <= 12 ? Utils.String2Int(fechaInicioSplit[1]) : 1;
				for(int j = Utils.String2Int(fechaFinSplit[2]); j <= Utils.String2Int(fechaFinSplit[2]); j++){
					int fin = j == Utils.String2Int(fechaFinSplit[2]) ? Utils.String2Int(fechaFinSplit[1]) : 12;
					
					for(int i = inicio; i <= fin; i++){
						if (diasPorcentual.size() == 0){
							int dias = getDiasDelMes(Utils.String2Int(fechaInicioSplit[1]),Utils.String2Int(fechaInicioSplit[2])) - Utils.String2Int(fechaInicioSplit[0]);
							diasPorcentual.add((double)dias);
						}else{
							d = getDiasDelMes(i,j);
							diasPorcentual.add((double)d);
						}
					}
					inicio = 1;
				}
				
				diasPorcentual.set(diasPorcentual.size() - 1, (double)Utils.String2Int(fechaFinSplit[0]));
				
				for(int i = 0; i<diasPorcentual.size();i++){
					double diaPorcentual = diasPorcentual.get(i);
					double dias = diaPorcentual / Utils.String2Int(Long.toString(diasTotales));
					diasPorcentual.set(i, dias);
				}
				
				inicio = (Utils.String2Int(fechaInicioSplit[1]) <= 12 ? Utils.String2Int(fechaInicioSplit[1]) : 1);
				for(int j = Utils.String2Int(fechaFinSplit[2]); j <= Utils.String2Int(fechaFinSplit[2]); j++){
					int fin = j == Utils.String2Int(fechaFinSplit[2]) ? Utils.String2Int(fechaFinSplit[1]) : 12;
					for(int i = inicio; i <= fin; i++){
						
						if(agrupacion == AGRUPACION_MES){			
							mesP = MES_NAME[i - 1] + "-" + j + "-P";
							mesR = MES_NAME[i - 1] + "-" + j + "-R";
						}else if(agrupacion == AGRUPACION_BIMESTRE){
							if(i >=1 && i <=2){
								mesP = BIMESTRE_NAME[0] + "-" + j + "-P";
								mesR = BIMESTRE_NAME[0] + "-" + j + "-R";
							}else if (i >=3 && i <=4){
								mesP = BIMESTRE_NAME[1] + "-" + j + "-P";
								mesR = BIMESTRE_NAME[1] + "-" + j + "-R";
							}else if (i >=5 && i <=6){
								mesP = BIMESTRE_NAME[2] + "-" + j + "-P";
								mesR = BIMESTRE_NAME[2] + "-" + j + "-R";
							}else if (i >=7 && i <=8){
								mesP = BIMESTRE_NAME[3] + "-" + j + "-P";
								mesR = BIMESTRE_NAME[3] + "-" + j + "-R";
							}else if (i >=9 && i <=10){
								mesP = BIMESTRE_NAME[4] + "-" + j + "-P";
								mesR = BIMESTRE_NAME[4] + "-" + j + "-R";
							}else if (i >=11 && i <=12){
								mesP = BIMESTRE_NAME[5] + "-" + j + "-P";
								mesR = BIMESTRE_NAME[5] + "-" + j + "-R";
							}
						}else if(agrupacion == AGRUPACION_TRIMESTRE){
							if(i >=1 && i <=3){
								mesP = TRIMESTRE_NAME[0] + "-" + j + "-P";
								mesR = TRIMESTRE_NAME[0] + "-" + j + "-R";
							}else if (i >=4 && i <=6){
								mesP = TRIMESTRE_NAME[1] + "-" + j + "-P";
								mesR = TRIMESTRE_NAME[1] + "-" + j + "-R";
							}else if (i >=7 && i <=9){
								mesP = TRIMESTRE_NAME[2] + "-" + j + "-P";
								mesR = TRIMESTRE_NAME[2] + "-" + j + "-R";
							}else if (i >=10 && i <=12){
								mesP = TRIMESTRE_NAME[3] + "-" + j + "-P";
								mesR = TRIMESTRE_NAME[3] + "-" + j + "-R";
							}
						}else if(agrupacion == AGRUPACION_CUATRIMESTRE){
							if(i >=1 && i <=4){
								mesP = CUATRIMESTRE_NAME[0] + "-" + j + "-P";
								mesR = CUATRIMESTRE_NAME[0] + "-" + j + "-R";
							}else if (i >=5 && i <=8){
								mesP = CUATRIMESTRE_NAME[1] + "-" + j + "-P";
								mesR = CUATRIMESTRE_NAME[1] + "-" + j + "-R";
							}else if (i >=9 && i <=12){
								mesP = CUATRIMESTRE_NAME[2] + "-" + j + "-P";
								mesR = CUATRIMESTRE_NAME[2] + "-" + j + "-R";
							}
						}else if(agrupacion == AGRUPACION_SEMESTRE){
							if(i >=1 && i <=6){
								mesP = SEMESTRE_NAME[0] + "-" + j + "-P";
								mesR = SEMESTRE_NAME[0] + "-" + j + "-R";
							}else if (i >=7 && i <=12){
								mesP = SEMESTRE_NAME[1] + "-" + j + "-P";
								mesR = SEMESTRE_NAME[1] + "-" + j + "-R";
							}
						}else if(agrupacion == AGRUPACION_ANUAL){
							mesP = "Anio-"+j+"-P";
							mesR = "Anio-"+j+"-R";
						}
						
						BigDecimal costoActual = null;
						if( entity.get(mesP) instanceof Integer ) {
							Integer ca = (Integer)entity.get(mesP); 
							costoActual = new BigDecimal(ca);
						} else if( entity.get(mesP) instanceof BigDecimal ){
							costoActual = (BigDecimal)entity.get(mesP);
						} else if(entity.get(mesP) == null){
							costoActual = new BigDecimal(0);
						}
						
						BigDecimal costoRealActual = null;
						
						if( entity.get(mesR) instanceof Integer ) {
							Integer ca = (Integer)entity.get(mesR); 
							costoRealActual = new BigDecimal(ca);
						} else if( entity.get(mesR) instanceof BigDecimal ){
							costoRealActual = (BigDecimal)entity.get(mesR);
						} else if(entity.get(mesR) == null){
							costoRealActual = new BigDecimal(0);
						}
						
						BigDecimal Costo = (BigDecimal)costo;
						BigDecimal CostoReal = (BigDecimal)costoReal;
						Costo = costoActual.add(Costo.multiply(new BigDecimal(diasPorcentual.get(contador))));
						CostoReal = costoRealActual.add(CostoReal.multiply(new BigDecimal(diasPorcentual.get(contador))));
						entity.put(mesP, Costo);
						entity.put(mesR, CostoReal);
						
						Integer padre = (Integer)entity.get("idPredecesor");
						Integer tipoPadre = (Integer)entity.get("objetoTipoPredecesor");
						
						calcularPadreProrrateo(padre+","+tipoPadre, mesP, mesR, Costo, CostoReal, resultadoPrestamo);
						contador++;
					}
				}
				
			}catch(Throwable e){
				CLogger.write("2", SInformePresupuesto.class, e);
			}
		}
		
		return entity;
	}
	
	private void calcularPadreProrrateo(String padreEntidadId, String mesP, String mesR, BigDecimal Costo, BigDecimal CostoReal, Map<String, Map<String, Object>> resultadoPrestamo){
		Map<String, Object> entityPadre = resultadoPrestamo.get(padreEntidadId);
		
		BigDecimal costoActual = null;
		if( entityPadre.get(mesP) instanceof Integer ) {
			Integer ca = (Integer)entityPadre.get(mesP); 
			costoActual = new BigDecimal(ca);
		} else if( entityPadre.get(mesP) instanceof BigDecimal ){
			costoActual = (BigDecimal)entityPadre.get(mesP);
		} else if(entityPadre.get(mesP) == null){
			costoActual = new BigDecimal(0);
		}
		
		BigDecimal costoRealActual = null;
		
		if( entityPadre.get(mesR) instanceof Integer ) {
			Integer ca = (Integer)entityPadre.get(mesR); 
			costoRealActual = new BigDecimal(ca);
		} else if( entityPadre.get(mesR) instanceof BigDecimal ){
			costoRealActual = (BigDecimal)entityPadre.get(mesR);
		} else if(entityPadre.get(mesR) == null){
			costoRealActual = new BigDecimal(0);
		}
		
		entityPadre.put(mesP, costoActual.add(Costo));
		entityPadre.put(mesR, costoRealActual.add(CostoReal));
		
		Integer padre = (Integer)entityPadre.get("idPredecesor");
		Integer tipoPadre = (Integer)entityPadre.get("objetoTipoPredecesor");
		
		if(padre > 0)
		{
			calcularPadreProrrateo(padre+","+tipoPadre, mesP, mesR, Costo, CostoReal, resultadoPrestamo);	
		}
	}
	
	private Map<String, Object> calcular(Map<String, Object> entity, Integer agrupacion, Map<String, Map<String, Object>> resultadoPrestamo, 
			String[] fechaInicioSplit, String[] fechaFinSplit, Object costo, Object costoReal, int acumulacionCosto){
		
		String mesP = "";
		String mesR = "";
		
		if(acumulacionCosto == 1){
			if(agrupacion == AGRUPACION_MES){			
				mesP = MES_NAME[Utils.String2Int(fechaInicioSplit[1]) - 1] + "-" + fechaInicioSplit[2] + "-P";
				mesR = MES_NAME[Utils.String2Int(fechaInicioSplit[1]) - 1] + "-" + fechaInicioSplit[2] + "-R";
			}else if(agrupacion == AGRUPACION_BIMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >=1 && Utils.String2Int(fechaInicioSplit[1]) <=2){
					mesP = BIMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-P";
					mesR = BIMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=3 && Utils.String2Int(fechaInicioSplit[1]) <=4){
					mesP = BIMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-P";
					mesR = BIMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=5 && Utils.String2Int(fechaInicioSplit[1]) <=6){
					mesP = BIMESTRE_NAME[2] + "-" + fechaInicioSplit[2] + "-P";
					mesR = BIMESTRE_NAME[2] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=7 && Utils.String2Int(fechaInicioSplit[1]) <=8){
					mesP = BIMESTRE_NAME[3] + "-" + fechaInicioSplit[2] + "-P";
					mesR = BIMESTRE_NAME[3] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=9 && Utils.String2Int(fechaInicioSplit[1]) <=10){
					mesP = BIMESTRE_NAME[4] + "-" + fechaInicioSplit[2] + "-P";
					mesR = BIMESTRE_NAME[4] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=11 && Utils.String2Int(fechaInicioSplit[1]) <=12){
					mesP = BIMESTRE_NAME[5] + "-" + fechaInicioSplit[2] + "-P";
					mesR = BIMESTRE_NAME[5] + "-" + fechaInicioSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_TRIMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >=1 && Utils.String2Int(fechaInicioSplit[1]) <=3){
					mesP = TRIMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=4 && Utils.String2Int(fechaInicioSplit[1]) <=6){
					mesP = TRIMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=7 && Utils.String2Int(fechaInicioSplit[1]) <=9){
					mesP = TRIMESTRE_NAME[2] + "-" + fechaInicioSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[2] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=10 && Utils.String2Int(fechaInicioSplit[1]) <=12){
					mesP = TRIMESTRE_NAME[3] + "-" + fechaInicioSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[3] + "-" + fechaInicioSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_CUATRIMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >=1 && Utils.String2Int(fechaInicioSplit[1]) <=4){
					mesP = CUATRIMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-P";
					mesR = CUATRIMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=5 && Utils.String2Int(fechaFinSplit[1]) <=8){
					mesP = CUATRIMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-P";
					mesR = CUATRIMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=9 && Utils.String2Int(fechaInicioSplit[1]) <=12){
					mesP = CUATRIMESTRE_NAME[2] + "-" + fechaInicioSplit[2] + "-P";
					mesR = CUATRIMESTRE_NAME[2] + "-" + fechaInicioSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_SEMESTRE){
				if(Utils.String2Int(fechaInicioSplit[1]) >=1 && Utils.String2Int(fechaInicioSplit[1]) <=6){
					mesP = SEMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-P";
					mesR = SEMESTRE_NAME[0] + "-" + fechaInicioSplit[2] + "-R";
				}else if (Utils.String2Int(fechaInicioSplit[1]) >=7 && Utils.String2Int(fechaInicioSplit[1]) <=12){
					mesP = SEMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-P";
					mesR = SEMESTRE_NAME[1] + "-" + fechaInicioSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_ANUAL){
				mesP = "Anio-"+fechaInicioSplit[2]+"-P";
				mesR = "Anio-"+fechaInicioSplit[2]+"-R";
			}
			
			BigDecimal costoActual = null;
			if( entity.get(mesP) instanceof Integer ) {
				Integer ca = (Integer)entity.get(mesP); 
				costoActual = new BigDecimal(ca);
			} else if( entity.get(mesP) instanceof BigDecimal ){
				costoActual = (BigDecimal)entity.get(mesP);
			} else if(entity.get(mesP) == null){
				costoActual = new BigDecimal(0);
			}
			
			BigDecimal costoRealActual = null;
			
			if( entity.get(mesR) instanceof Integer ) {
				Integer ca = (Integer)entity.get(mesR); 
				costoRealActual = new BigDecimal(ca);
			} else if( entity.get(mesR) instanceof BigDecimal ){
				costoRealActual = (BigDecimal)entity.get(mesR);
			} else if(entity.get(mesR) == null){
				costoRealActual = new BigDecimal(0);
			}		
			
			entity.put(mesP, costoActual.add((BigDecimal)costo));
			entity.put(mesR, costoRealActual.add((BigDecimal)costoReal));
			
			Integer padre = (Integer)entity.get("idPredecesor");
			Integer tipoPadre = (Integer)entity.get("objetoTipoPredecesor");
			if(padre > 0)
			{
				entity = resultadoPrestamo.get(padre+","+tipoPadre);
				calcular(entity, agrupacion, resultadoPrestamo, fechaInicioSplit, fechaFinSplit,costo, costoReal, acumulacionCosto);
			}
		}else if(acumulacionCosto == 2){
			
		}else if(acumulacionCosto == 3){
			if(agrupacion == AGRUPACION_MES){			
				mesP = MES_NAME[Utils.String2Int(fechaFinSplit[1]) - 1] + "-" + fechaFinSplit[2] + "-P";
				mesR = MES_NAME[Utils.String2Int(fechaFinSplit[1]) - 1] + "-" + fechaFinSplit[2] + "-R";
			}else if(agrupacion == AGRUPACION_BIMESTRE){
				if(Utils.String2Int(fechaFinSplit[1]) >=1 && Utils.String2Int(fechaFinSplit[1]) <=2){
					mesP = BIMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-P";
					mesR = BIMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=3 && Utils.String2Int(fechaFinSplit[1]) <=4){
					mesP = BIMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-P";
					mesR = BIMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=5 && Utils.String2Int(fechaFinSplit[1]) <=6){
					mesP = BIMESTRE_NAME[2] + "-" + fechaFinSplit[2] + "-P";
					mesR = BIMESTRE_NAME[2] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=7 && Utils.String2Int(fechaFinSplit[1]) <=8){
					mesP = BIMESTRE_NAME[3] + "-" + fechaFinSplit[2] + "-P";
					mesR = BIMESTRE_NAME[3] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=9 && Utils.String2Int(fechaFinSplit[1]) <=10){
					mesP = BIMESTRE_NAME[4] + "-" + fechaFinSplit[2] + "-P";
					mesR = BIMESTRE_NAME[4] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=11 && Utils.String2Int(fechaFinSplit[1]) <=12){
					mesP = BIMESTRE_NAME[5] + "-" + fechaFinSplit[2] + "-P";
					mesR = BIMESTRE_NAME[5] + "-" + fechaFinSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_TRIMESTRE){
				if(Utils.String2Int(fechaFinSplit[1]) >=1 && Utils.String2Int(fechaFinSplit[1]) <=3){
					mesP = TRIMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=4 && Utils.String2Int(fechaFinSplit[1]) <=6){
					mesP = TRIMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=7 && Utils.String2Int(fechaFinSplit[1]) <=9){
					mesP = TRIMESTRE_NAME[2] + "-" + fechaFinSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[2] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=10 && Utils.String2Int(fechaFinSplit[1]) <=12){
					mesP = TRIMESTRE_NAME[3] + "-" + fechaFinSplit[2] + "-P";
					mesR = TRIMESTRE_NAME[3] + "-" + fechaFinSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_CUATRIMESTRE){
				if(Utils.String2Int(fechaFinSplit[1]) >=1 && Utils.String2Int(fechaFinSplit[1]) <=4){
					mesP = CUATRIMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-P";
					mesR = CUATRIMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=5 && Utils.String2Int(fechaFinSplit[1]) <=8){
					mesP = CUATRIMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-P";
					mesR = CUATRIMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=9 && Utils.String2Int(fechaFinSplit[1]) <=12){
					mesP = CUATRIMESTRE_NAME[2] + "-" + fechaFinSplit[2] + "-P";
					mesR = CUATRIMESTRE_NAME[2] + "-" + fechaFinSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_SEMESTRE){
				if(Utils.String2Int(fechaFinSplit[1]) >=1 && Utils.String2Int(fechaFinSplit[1]) <=6){
					mesP = SEMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-P";
					mesR = SEMESTRE_NAME[0] + "-" + fechaFinSplit[2] + "-R";
				}else if (Utils.String2Int(fechaFinSplit[1]) >=7 && Utils.String2Int(fechaFinSplit[1]) <=12){
					mesP = SEMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-P";
					mesR = SEMESTRE_NAME[1] + "-" + fechaFinSplit[2] + "-R";
				}
			}else if(agrupacion == AGRUPACION_ANUAL){
				mesP = "Anio-"+fechaFinSplit[2]+"-P";
				mesR = "Anio-"+fechaFinSplit[2]+"-R";
			}
			
			BigDecimal costoActual = null;
			if( entity.get(mesP) instanceof Integer ) {
				Integer ca = (Integer)entity.get(mesP); 
				costoActual = new BigDecimal(ca);
			} else if( entity.get(mesP) instanceof BigDecimal ){
				costoActual = (BigDecimal)entity.get(mesP);
			} else if(entity.get(mesP) == null){
				costoActual = new BigDecimal(0);
			}
			
			BigDecimal costoRealActual = null;
			
			if( entity.get(mesR) instanceof Integer ) {
				Integer ca = (Integer)entity.get(mesR); 
				costoRealActual = new BigDecimal(ca);
			} else if( entity.get(mesR) instanceof BigDecimal ){
				costoRealActual = (BigDecimal)entity.get(mesR);
			} else if(entity.get(mesR) == null){
				costoRealActual = new BigDecimal(0);
			}		
			
			entity.put(mesP, costoActual.add((BigDecimal)costo));
			entity.put(mesR, costoRealActual.add((BigDecimal)costoReal));
			
			Integer padre = (Integer)entity.get("idPredecesor");
			Integer tipoPadre = (Integer)entity.get("objetoTipoPredecesor");
			if(padre > 0)
			{
				entity = resultadoPrestamo.get(padre+","+tipoPadre);
				calcular(entity, agrupacion, resultadoPrestamo, fechaInicioSplit, fechaFinSplit,costo, costoReal, acumulacionCosto);
			}
		}
		
		return entity;
	}
	
	private int getDiasDelMes(int mes, int ano)
	{
		if( (mes == 1) || (mes == 3) || (mes == 5) || (mes == 7) || (mes == 8) || (mes == 10) || (mes == 12) ) 
	        return 31;
	    else if( (mes == 4) || (mes == 6) || (mes == 9) || (mes == 11) ) 
	        return 30;
	    else if( mes == 2 )
	    {
	        if( (ano % 4 == 0) && (ano % 100 != 0) || (ano % 400 == 0) )
	            return 29;
	        else
	            return 28;
	    }  
		
		return 0;
	}
	
	private void exportarExcel(Map<String,Object[]> datos, String nombreInforme, String usuario, HttpServletResponse response){
		try{
			CExcel excel = new CExcel("Reporte",false);
			String path = excel.ExportarExcel(datos, nombreInforme, usuario);
			File file=new File(path);
			if(file.exists()){
				FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
		        	
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
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; Informe_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		catch(Throwable e){
			CLogger.write("2", SInformePresupuesto.class, e);
		}
	}
	
	private Map<String, Object> getEstructura(){
		Map<String, Object> estructura = new HashMap<String, Object>();
		estructura.put("id", 0);
		estructura.put("idPrestamo", 0);
		estructura.put("objetoTipo", 0);
		estructura.put("posicionArbol", 0);
		estructura.put("$$treeLevel", 0);
		estructura.put("idObjetoTipo", 0);
		estructura.put("nombre", "");
		estructura.put("idPredecesor", 0);
		estructura.put("objetoTipoPredecesor", 0);
		estructura.put("Costo", 0);
		estructura.put("CostoReal", 0);
		estructura.put("fechaInicio", "");
		estructura.put("fechaFin", "");
		estructura.put("hijo", "");
		estructura.put("acumulacionCostos", 0);
		estructura.put("columnas", "");
		estructura.put("Total", 0);
		
		for (String columna: columnaNames){
			estructura.put(columna, 0);
			estructura.put("total" + columna, 0);
		}
		return estructura;
	}

	private LinkedHashMap<String, Map<String, Object>> obtenerProyecto(int proyectoId, String usuario, Integer ejercicioInicio, Integer ejercicioFin){
		LinkedHashMap<String, Map<String, Object>> resultado =  new LinkedHashMap<>();
		List<Map<String, Object>> resultPrestamo = new ArrayList<Map<String, Object>>();
		String[] hijos = null;
		int contadorHijos =0;
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		
		if (proyecto!=null){
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyectoId, 1);
			Long codigoPresupuestario = prestamo.getCodigoPresupuestario();
			
			Integer fuente = Utils.String2Int(Long.toString(codigoPresupuestario).substring(0,2));
			Integer organismo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(2,6));
			Integer correlativo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(6,10));
			Map<String, Object> estructura = getEstructura();
			
			estructura.put("objetoTipo", OBJETO_ID_PROYECTO);
			estructura.put("posicionArbol", 1);
			estructura.put("idObjetoTipo", proyecto.getId());
			estructura.put("nombre", proyecto.getNombre());
			estructura.put("idPredecesor", 0);
			estructura.put("objetoTipoPredecesor", 0);
			
			for(int i = ejercicioInicio; i <= ejercicioFin; i++){
				List<Object> objetoPrestamo = ReporteDAO.getPresupuestoProyecto(fuente, organismo, correlativo, i);
				
				for(Object obj : objetoPrestamo){
					Object[] ob = (Object[])obj;
					estructura.put("mes1r"+"-"+i, ob[0]);
					estructura.put("mes2r"+"-"+i, ob[1]);
					estructura.put("mes3r"+"-"+i, ob[2]);
					estructura.put("mes4r"+"-"+i, ob[3]);
					estructura.put("mes5r"+"-"+i, ob[4]);
					estructura.put("mes6r"+"-"+i, ob[5]);
					estructura.put("mes7r"+"-"+i, ob[6]);
					estructura.put("mes8r"+"-"+i, ob[7]);
					estructura.put("mes9r"+"-"+i, ob[8]);
					estructura.put("mes10r"+"-"+i, ob[9]);
					estructura.put("mes11r"+"-"+i, ob[10]);
					estructura.put("mes12r"+"-"+i, ob[11]);
				}
			}

			List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
					null, null, null, null, null, usuario);
			
			hijos = new String[componentes.size()];
			for (Componente componente : componentes){
				hijos[contadorHijos] = componente.getId().toString() + ",2";
				contadorHijos++;
			}
			estructura.put("hijo",hijos);
			resultPrestamo.add(estructura);
			resultado.put(proyecto.getId()+","+OBJETO_ID_PROYECTO,estructura);
						
			for (Componente componente : componentes){
				estructura = getEstructura();
				estructura.put("objetoTipo", OBJETO_ID_COMPONENTE);
				estructura.put("posicionArbol", 2);
				estructura.put("idObjetoTipo", componente.getId());
				estructura.put("nombre", componente.getNombre());
				estructura.put("idPredecesor", proyecto.getId());
				estructura.put("objetoTipoPredecesor", 1);
				List<Object> objeto = new ArrayList<Object>();
				
				for(int i = ejercicioInicio; i <= ejercicioFin; i++){
					objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, componente.getPrograma(), componente.getSubprograma(), componente.getProyecto_1(), componente.getActividad(), componente.getObra());
					
					for(Object obj : objeto){
						Object[] ob = (Object[])obj;
						estructura.put("mes1r"+"-"+i, ob[0]);
						estructura.put("mes2r"+"-"+i, ob[1]);
						estructura.put("mes3r"+"-"+i, ob[2]);
						estructura.put("mes4r"+"-"+i, ob[3]);
						estructura.put("mes5r"+"-"+i, ob[4]);
						estructura.put("mes6r"+"-"+i, ob[5]);
						estructura.put("mes7r"+"-"+i, ob[6]);
						estructura.put("mes8r"+"-"+i, ob[7]);
						estructura.put("mes9r"+"-"+i, ob[8]);
						estructura.put("mes10r"+"-"+i, ob[9]);
						estructura.put("mes11r"+"-"+i, ob[10]);
						estructura.put("mes12r"+"-"+i, ob[11]);
					}
				}
				
				
				List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
						null, null, null, null, null, usuario);
				
				hijos = new String[productos.size()];
				contadorHijos = 0;
				for (Producto producto : productos){
					hijos[contadorHijos] = producto.getId().toString() + ",3";
					contadorHijos++;
				}
				estructura.put("hijo",hijos);
				resultPrestamo.add(estructura);
				resultado.put(componente.getId()+","+OBJETO_ID_COMPONENTE,estructura);
				
				for (Producto producto : productos){
					estructura = getEstructura();
					estructura.put("objetoTipo", OBJETO_ID_PRODUCTO);
					estructura.put("posicionArbol", 3);
					estructura.put("idObjetoTipo", producto.getId());
					estructura.put("nombre", producto.getNombre());
					estructura.put("idPredecesor", componente.getId());
					estructura.put("objetoTipoPredecesor", 2);
							
					for(int i = ejercicioInicio; i <= ejercicioFin; i++){
						objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, producto.getPrograma(), producto.getSubprograma(), producto.getProyecto(), producto.getActividad(), producto.getObra());
						
						for(Object obj : objeto){
							Object[] ob = (Object[])obj;
							estructura.put("mes1r"+"-"+i, ob[0]);
							estructura.put("mes2r"+"-"+i, ob[1]);
							estructura.put("mes3r"+"-"+i, ob[2]);
							estructura.put("mes4r"+"-"+i, ob[3]);
							estructura.put("mes5r"+"-"+i, ob[4]);
							estructura.put("mes6r"+"-"+i, ob[5]);
							estructura.put("mes7r"+"-"+i, ob[6]);
							estructura.put("mes8r"+"-"+i, ob[7]);
							estructura.put("mes9r"+"-"+i, ob[8]);
							estructura.put("mes10r"+"-"+i, ob[9]);
							estructura.put("mes11r"+"-"+i, ob[10]);
							estructura.put("mes12r"+"-"+i, ob[11]);
						}
					}


					
					List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
							null, null, null, null, null, usuario);
					
					hijos = new String[subproductos.size()];
					contadorHijos = 0;
					for (Subproducto subproducto : subproductos){
						hijos[contadorHijos] = subproducto.getId().toString() + ",4";
						contadorHijos++;
					}
					estructura.put("hijo",hijos);
					resultPrestamo.add(estructura);
					resultado.put(producto.getId()+","+OBJETO_ID_PRODUCTO,estructura);
					
					for (Subproducto subproducto : subproductos){
						estructura = getEstructura();
						estructura.put("objetoTipo", OBJETO_ID_SUBPRODUCTO);
						estructura.put("posicionArbol", 4);
						estructura.put("idObjetoTipo", subproducto.getId());
						estructura.put("nombre", subproducto.getNombre());
						estructura.put("idPredecesor", producto.getId());
						estructura.put("objetoTipoPredecesor", 3);
						
						for(int i = ejercicioInicio; i <= ejercicioFin; i++){
							objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, subproducto.getPrograma(), subproducto.getSubprograma(), subproducto.getProyecto(), subproducto.getActividad(), subproducto.getObra());
							
							for(Object obj : objeto){
								Object[] ob = (Object[])obj;
								estructura.put("mes1r"+"-"+i, ob[0]);
								estructura.put("mes2r"+"-"+i, ob[1]);
								estructura.put("mes3r"+"-"+i, ob[2]);
								estructura.put("mes4r"+"-"+i, ob[3]);
								estructura.put("mes5r"+"-"+i, ob[4]);
								estructura.put("mes6r"+"-"+i, ob[5]);
								estructura.put("mes7r"+"-"+i, ob[6]);
								estructura.put("mes8r"+"-"+i, ob[7]);
								estructura.put("mes9r"+"-"+i, ob[8]);
								estructura.put("mes10r"+"-"+i, ob[9]);
								estructura.put("mes11r"+"-"+i, ob[10]);
								estructura.put("mes12r"+"-"+i, ob[11]);
							}
						}
						
						List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
								null, null, null, null, null, usuario);
						
						
						hijos = new String[actividades.size()];
						contadorHijos = 0;
						for (Actividad actividad : actividades){
							if(actividad.getCosto().compareTo(BigDecimal.ZERO) != 0 && actividad.getCostoReal().compareTo(BigDecimal.ZERO) != 0){
								hijos[contadorHijos] = actividad.getId().toString() + ",5";
								contadorHijos++;
							}
						}
						estructura.put("hijo",hijos);
						resultPrestamo.add(estructura);
						resultado.put(subproducto.getId()+","+OBJETO_ID_SUBPRODUCTO,estructura);
						
						for (Actividad actividad : actividades ){
							resultado = ObtenerActividades(actividad,usuario,resultPrestamo, OBJETO_ID_ACTIVIDAD,subproducto.getId(), OBJETO_ID_SUBPRODUCTO,resultado, ejercicioInicio, ejercicioFin, proyectoId);
						}
					}
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
							null, null, null, null, null, usuario);
					for (Actividad actividad : actividades ){
						resultado = ObtenerActividades(actividad,usuario,resultPrestamo,OBJETO_ID_SUBPRODUCTO,producto.getId(), OBJETO_ID_PRODUCTO,resultado, ejercicioInicio,ejercicioFin, proyectoId);
					}
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
						null, null, null, null, null, usuario);
				for (Actividad actividad : actividades ){
					resultado = ObtenerActividades(actividad,usuario,resultPrestamo,OBJETO_ID_PRODUCTO, componente.getId(),OBJETO_ID_COMPONENTE, resultado, ejercicioInicio, ejercicioFin,proyectoId);
				}
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
					null, null, null, null, null, usuario);
			for (Actividad actividad : actividades ){
				resultado = ObtenerActividades(actividad,usuario,resultPrestamo,OBJETO_ID_COMPONENTE, proyecto.getId(), OBJETO_ID_PROYECTO, resultado, ejercicioInicio,ejercicioFin,proyectoId);
			}
		}
		
		return resultado;
	}
	
	private LinkedHashMap<String, Map<String, Object>> ObtenerActividades(Actividad actividad, String usuario, List<Map<String, Object>> lstdataEjecutado, int posicionArbol, 
			int idPredecesor, int objetoTipoPredecesor, LinkedHashMap<String, Map<String, Object>> resultado, Integer ejercicioInicio, Integer ejercicioFin, Integer idPrestamo){
		
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		if (actividad.getCosto().compareTo(BigDecimal.ZERO) != 0 || actividad.getCostoReal().compareTo(BigDecimal.ZERO) != 0){			
			Integer actividadId = actividad.getId();
			Map<String, Object> estructura = getEstructura();
			estructura.put("objetoTipo", OBJETO_ID_ACTIVIDAD);
			estructura.put("posicionArbol", posicionArbol);
			estructura.put("idObjetoTipo", actividadId);
			estructura.put("nombre", actividad.getNombre());
			estructura.put("idPredecesor", idPredecesor);
			estructura.put("objetoTipoPredecesor", objetoTipoPredecesor);
			estructura.put("Costo", actividad.getCosto() == null ? new BigDecimal(0) : actividad.getCosto());
			estructura.put("CostoReal", actividad.getCostoReal() == null ? new BigDecimal(0) : actividad.getCostoReal());
			
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(idPrestamo, 1);
			Long codigoPresupuestario = prestamo.getCodigoPresupuestario();
			
			Integer fuente = Utils.String2Int(Long.toString(codigoPresupuestario).substring(0,2));
			Integer organismo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(2,6));
			Integer correlativo = Utils.String2Int(Long.toString(codigoPresupuestario).substring(6,10));
			
			for(int i = ejercicioInicio; i <= ejercicioFin; i++){
				List<Object> objeto = ReporteDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, i, actividad.getPrograma(), actividad.getSubprograma(), actividad.getProyecto(), actividad.getActividad(), actividad.getObra());
				
				for(Object obj : objeto){
					Object[] ob = (Object[])obj;
					estructura.put("mes1r"+"-"+i, ob[0]);
					estructura.put("mes2r"+"-"+i, ob[1]);
					estructura.put("mes3r"+"-"+i, ob[2]);
					estructura.put("mes4r"+"-"+i, ob[3]);
					estructura.put("mes5r"+"-"+i, ob[4]);
					estructura.put("mes6r"+"-"+i, ob[5]);
					estructura.put("mes7r"+"-"+i, ob[6]);
					estructura.put("mes8r"+"-"+i, ob[7]);
					estructura.put("mes9r"+"-"+i, ob[8]);
					estructura.put("mes10r"+"-"+i, ob[9]);
					estructura.put("mes11r"+"-"+i, ob[10]);
					estructura.put("mes12r"+"-"+i, ob[11]);
				}
			}
			
			String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad, usuario).split(";");

			estructura.put("fechaInicio", fechaInicioFin[0]);
			estructura.put("fechaFin", fechaInicioFin[1]);
			estructura.put("acumulacionCostos", actividad.getAcumulacionCosto() == null ? 3 : actividad.getAcumulacionCosto().getId());
			
			String[] hijos = new String[actividades.size()];
			int contadorHijos = 0;
			for(Actividad subActividad : actividades){
					hijos[contadorHijos] = subActividad.getId().toString() + ",5";
					contadorHijos++;
			}
			
			estructura.put("hijo",hijos);
			lstdataEjecutado.add(estructura);
			resultado.put(actividadId+","+OBJETO_ID_ACTIVIDAD, estructura);
			actividadesCosto.add(actividadId);

			for(Actividad subActividad : actividades){
				resultado = ObtenerActividades(subActividad, usuario, lstdataEjecutado, posicionArbol + 1, actividadId, OBJETO_ID_ACTIVIDAD, resultado, ejercicioInicio,ejercicioFin,idPrestamo);
			}
		}
		return resultado;
	}
}
