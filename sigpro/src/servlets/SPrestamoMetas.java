package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
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



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ComponenteDAO;
import dao.PrestamoMetasDAO;

import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import utilities.CMariaDB;
import utilities.Utils;

@WebServlet("/SPrestamoMetas")
public class SPrestamoMetas extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class stprestamo{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer unidadDeMedida;
		BigDecimal lineaBase;
		BigDecimal metaFinal;
		Integer nivel;
		stanio[] anios; 
	}
	
	class stanio{
		BigDecimal[] enero;
		BigDecimal[] febrero;
		BigDecimal[] marzo;
		BigDecimal[] abril;
		BigDecimal[] mayo;
		BigDecimal[] junio;
		BigDecimal[] julio;
		BigDecimal[] agosto;
		BigDecimal[] septiembre;
		BigDecimal[] octubre;
		BigDecimal[] noviembre;
		BigDecimal[] diciembre;
		BigDecimal anio;
	}

    public SPrestamoMetas() {
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

		if(accion.equals("getMetasProducto")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			Integer anoInicial = Utils.String2Int(map.get("anoInicial"),0);
			Integer anoFinal = Utils.String2Int(map.get("anoFinal"),0);
			
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
			
			if(proyecto != null){
				List<stprestamo> lstPrestamo = new ArrayList<>();
				stprestamo tempPrestamo = null;
			
				tempPrestamo = new stprestamo();
				if(CMariaDB.connect()){
						Connection conn = CMariaDB.getConnection();
						ArrayList<Integer> componentes = PrestamoMetasDAO.getEstructuraArbolComponentes(idPrestamo, conn);
						
						tempPrestamo.nombre = proyecto.getNombre();
						tempPrestamo.objeto_id = proyecto.getId();
						tempPrestamo.objeto_tipo = 1;
						tempPrestamo.nivel = 1;
						
						ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo = new ArrayList<ArrayList<BigDecimal>>();
						
						tempPrestamo = getMetas(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
						
						lstPrestamo.add(tempPrestamo);
						
						for(Integer componente:componentes){
							tempPrestamo = new stprestamo();
							Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
							tempPrestamo.nombre = objComponente.getNombre();
							tempPrestamo.objeto_id = objComponente.getId();
							tempPrestamo.objeto_tipo = 2;
							tempPrestamo.nivel = 2;
							
							 presupuestoPrestamo = new ArrayList<ArrayList<BigDecimal>>();
							
							tempPrestamo = getMetas(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
							
							lstPrestamo.add(tempPrestamo);							
							ArrayList<Integer> productos = PrestamoMetasDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
							for(Integer producto: productos){
								tempPrestamo = new stprestamo();
								Producto objProducto = ProductoDAO.getProductoPorId(producto);
								tempPrestamo.nombre = objProducto.getNombre();
								tempPrestamo.objeto_id = objProducto.getId();
								tempPrestamo.objeto_tipo = 3;
								tempPrestamo.nivel = 3;
								tempPrestamo.lineaBase = new BigDecimal(0);
								tempPrestamo.metaFinal = new BigDecimal(0);
								
								presupuestoPrestamo = PrestamoMetasDAO.getMetasPorProducto(producto, 
										anoInicial, anoFinal, conn);
								
								tempPrestamo = getMetas(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
								lstPrestamo.add(tempPrestamo);
							
							} 
							
						}
						
						CMariaDB.close();
						response_text=new GsonBuilder().serializeNulls().create().toJson(lstPrestamo);
				        response_text = String.join("", "\"prestamo\":",response_text);
				        response_text = String.join("", "{\"success\":true,", response_text, "}");
					}else{
						response_text = String.join("", "{\"success\":false}");
					}
				}
		}else if (accion.equals("exportarExcel")){
//			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
//			int anioInicio = Utils.String2Int(map.get("fechaInicio"), 0);
//			int anioFin = Utils.String2Int(map.get("fechaFin"), 0);
//			int agrupacion = Utils.String2Int(map.get("agrupacion"), 0);
//			String filas = map.get("filas");
//			String columnas = map.get("columnas");
//			
//	        byte [] outArray = exportarExcel(proyectoId, anioInicio, anioFin, agrupacion, filas, columnas, usuario);
//		
//			response.setContentType("application/ms-excel");
//			response.setContentLength(outArray.length);
//			response.setHeader("Expires:", "0"); 
//			response.setHeader("Content-Disposition", "attachment; MetasPrestamo_.xls");
//			OutputStream outStream = response.getOutputStream();
//			outStream.write(outArray);
//			outStream.flush();

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
	
	private stprestamo getMetas (ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo,
			int anoInicial, int anoFinal, stprestamo prestamo){
		
		stanio[] anios = inicializarStanio(anoInicial, anoFinal);
		if(presupuestoPrestamo.size() > 0){
			
			
			for(ArrayList<BigDecimal> objprestamopresupuesto : presupuestoPrestamo){
				
				stanio aniotemp = new stanio(); 
				aniotemp = inicializarStanio(aniotemp);
				aniotemp.enero[0] = objprestamopresupuesto.get(0);
				aniotemp.enero[1] = objprestamopresupuesto.get(1);
				aniotemp.febrero[0] = objprestamopresupuesto.get(2);
				aniotemp.febrero[1] = objprestamopresupuesto.get(3);
				aniotemp.marzo[0] = objprestamopresupuesto.get(4);
				aniotemp.marzo[1] = objprestamopresupuesto.get(5);
				aniotemp.abril[0] = objprestamopresupuesto.get(6);
				aniotemp.abril[1] = objprestamopresupuesto.get(7);
				aniotemp.mayo[0] = objprestamopresupuesto.get(8);
				aniotemp.mayo[1] = objprestamopresupuesto.get(9);
				aniotemp.junio[0] = objprestamopresupuesto.get(10);
				aniotemp.junio[1] = objprestamopresupuesto.get(11);
				aniotemp.julio[0] = objprestamopresupuesto.get(12);
				aniotemp.julio[1] = objprestamopresupuesto.get(13);
				aniotemp.agosto[0] = objprestamopresupuesto.get(14);
				aniotemp.agosto[1] = objprestamopresupuesto.get(15);
				aniotemp.septiembre[0] = objprestamopresupuesto.get(16);
				aniotemp.septiembre[1] = objprestamopresupuesto.get(17);
				aniotemp.octubre[0] = objprestamopresupuesto.get(18);
				aniotemp.octubre[1] = objprestamopresupuesto.get(19);
				aniotemp.noviembre[0] = objprestamopresupuesto.get(20);
				aniotemp.noviembre[1] = objprestamopresupuesto.get(21);
				aniotemp.diciembre[0] = objprestamopresupuesto.get(22);
				aniotemp.diciembre[1] = objprestamopresupuesto.get(23);
				int pos = anoFinal- objprestamopresupuesto.get(24).intValue();
				aniotemp.anio = new BigDecimal(anoInicial + pos);
				prestamo.unidadDeMedida = Integer.parseInt(objprestamopresupuesto.get(25).toString());
				prestamo.lineaBase = prestamo.lineaBase.add(objprestamopresupuesto.get(26));
				prestamo.metaFinal = prestamo.metaFinal.add(objprestamopresupuesto.get(27));
				anios[pos] = aniotemp;
			}
		}
			prestamo.anios = anios;
			return prestamo;
	}
	
	private stanio[] inicializarStanio (int anioInicial, int anioFinal){
		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			temp = inicializarStanio(temp);
			temp.anio = new BigDecimal(anioInicial + i);
			anios[i] = temp;
		}
		return anios;		
	}
	
	private stanio inicializarStanio(stanio anio){
		
			anio.enero = new BigDecimal[2];
			anio.febrero = new BigDecimal[2];
			anio.marzo = new BigDecimal[2];
			anio.abril = new BigDecimal[2];
			anio.mayo = new BigDecimal[2];
			anio.junio = new BigDecimal[2];
			anio.julio = new BigDecimal[2];
			anio.agosto = new BigDecimal[2];
			anio.septiembre = new BigDecimal[2];
			anio.octubre = new BigDecimal[2];
			anio.noviembre = new BigDecimal[2];
			anio.diciembre = new BigDecimal[2];
			
		return anio;		
	}
	
	private byte[] exportarExcel(int prestamoId, int anioInicio, int anioFin, int agrupacion, String filas, String columnas, String usuario) throws IOException{
		byte [] outArray = null;
//		CExcel excel = new CExcel("PrestamoMetas",false);
//		JsonParser parser = new JsonParser();
//		
//		//Cabeceras
//		JsonElement tablaJson = parser.parse(columnas);
//		JsonArray columnaArreglo = tablaJson.getAsJsonArray();
//		Object[] categoriasObject = new Object[columnaArreglo.size()];
//		Object[] columnasObject = new Object[columnaArreglo.size()];
//		
//		int i=0;
//		String categoriaAnterior="";
//		for (JsonElement columnaElement : columnaArreglo){
//			JsonObject columna = columnaElement.getAsJsonObject();
//			columnasObject[i] = columna.get("displayName").getAsString();
//			String categoria = (columna.get("category") == null || columna.get("category").isJsonNull()) ? "" : columna.get("category").getAsString();
//			if (!categoriaAnterior.equals(categoria)){
//				categoriasObject[i] = categoria;
//				categoriaAnterior = categoria;
//			}
//			if(categoria.trim().isEmpty()){
//				categoriasObject[i]=columnasObject[i];
//				columnasObject[i]="";
//			}
//			i++;
//		}						
//		
//		Map<String,Object[]> datos = new HashMap<>();
//		datos.put("0", categoriasObject);
//		datos.put("1", columnasObject);
//		int filaId = 2;
//		
//		//Filas
//		tablaJson = parser.parse(filas);
//		JsonArray tablaArreglo = tablaJson.getAsJsonArray();
//		
//		
//		for (JsonElement filaElement : tablaArreglo){
//			Object[] filasObject = new Object[columnaArreglo.size()];
//			JsonObject fila = filaElement.getAsJsonObject();
//			
//			String sangria;
//			switch (fila.get("objetoTipo").getAsInt()){
//				case 1: sangria = ""; break;
//				case 2: sangria = "\t"; break;
//				case 3: sangria = "\t\t"; break;
//				case 4: sangria = "\t\t\t"; break;
//				case 5: sangria = "\t\t\t\t"; break;
//				default: sangria = "";
//			}
//			int unidadDeMedidaId = fila.get("unidadDeMedidaId").isJsonNull() ? 0 : Integer.parseInt(fila.get("unidadDeMedidaId").getAsString()); 
//			String unidadDeMedidaNombre = "";
//			if (unidadDeMedidaId >0){
//				unidadDeMedidaNombre = MetaUnidadMedidaDAO.getMetaUnidadMedidaPorId(unidadDeMedidaId).getNombre();
//			}
//			
//			filasObject[0] = sangria + fila.get("nombreMeta").getAsString();
//			filasObject[1] = fila.get("fechaInicio").getAsString();
//			filasObject[2] = fila.get("fechaFin").getAsString();
//			filasObject[3] = unidadDeMedidaNombre;
//			i=4;
//			Double totalPlanificado = new Double(0);
//			Double totalReal = new Double(0);
//			int objetoTipo = fila.get("objetoTipo").getAsInt();
//			if (objetoTipo == OBJETO_ID_PRODUCTO){
//				if (agrupacion == AGRUPACION_MES){ 
//					for (int anioInicioC = anioInicio; anioInicioC<=(anioFin); anioInicioC++){
//						for (int mes = 0; mes < 12; mes++){
//							if(fila.get(mes+"P"+anioInicioC)!=null && !fila.get(mes+"P"+anioInicioC).isJsonNull()){
//								filasObject[i] = fila.get(mes+"P"+anioInicioC).getAsString();
//								totalPlanificado+=fila.get(mes+"P"+anioInicioC).getAsDouble();
//							}
//							i++;
//							if(fila.get(mes+"R"+anioInicioC)!=null && !fila.get(mes+"R"+anioInicioC).isJsonNull()){
//								filasObject[i] = fila.get(mes+"R"+anioInicioC).getAsString();
//								totalReal+=fila.get(mes+"R"+anioInicioC).getAsDouble();
//							}
//							i++;
//						}
//					}
//				}else if (agrupacion == AGRUPACION_CUATRIMESTRE){ 
//					for (int anioInicioC = anioInicio; anioInicioC<=(anioFin); anioInicioC++){
//						for (int contador = 1; contador < 4; contador++){
//							int mes = (contador*4)-1;
//							if(fila.get(mes+"P"+anioInicioC)!=null && !fila.get(mes+"P"+anioInicioC).isJsonNull()){
//								filasObject[i] = fila.get(mes+"P"+anioInicioC).getAsString();
//								totalPlanificado+=fila.get(mes+"P"+anioInicioC).getAsDouble();
//							}
//							i++;
//							if(fila.get(mes+"R"+anioInicioC)!=null && !fila.get(mes+"R"+anioInicioC).isJsonNull()){
//								filasObject[i] = fila.get(mes+"R"+anioInicioC).getAsString();
//								totalReal+=fila.get(mes+"R"+anioInicioC).getAsDouble();
//							}
//							i++;
//						}
//					}
//				}else if (agrupacion == AGRUPACION_SEMESTRE){ 
//					for (int anioInicioC = anioInicio; anioInicioC<=(anioFin); anioInicioC++){
//						for (int contador = 1; contador < 3; contador++){
//							int mes = (contador*6)-1;
//							if(fila.get(mes+"P"+anioInicioC)!=null && !fila.get(mes+"P"+anioInicioC).isJsonNull()){
//								filasObject[i] = fila.get(mes+"P"+anioInicioC).getAsString();
//								totalPlanificado+=fila.get(mes+"P"+anioInicioC).getAsDouble();
//							}
//							i++;
//							if(fila.get(mes+"R"+anioInicioC)!=null && !fila.get(mes+"R"+anioInicioC).isJsonNull()){
//								filasObject[i] = fila.get(mes+"R"+anioInicioC).getAsString();
//								totalReal+=fila.get(mes+"R"+anioInicioC).getAsDouble();
//							}
//							i++;
//						}
//					}
//				}else if (agrupacion == AGRUPACION_ANUAL){ 
//					for (int anioInicioC = anioInicio; anioInicioC<=(anioFin); anioInicioC++){
//						if(fila.get("11P"+anioInicioC)!=null && !fila.get("11P"+anioInicioC).isJsonNull()){
//							filasObject[i] = fila.get("11P"+anioInicioC).getAsString();
//							totalPlanificado+=fila.get("11P"+anioInicioC).getAsDouble();
//						}
//						i++;
//						if(fila.get("11R"+anioInicioC)!=null && !fila.get("11R"+anioInicioC).isJsonNull()){
//							filasObject[i] = fila.get("11R"+anioInicioC).getAsString();
//							totalReal+=fila.get("11R"+anioInicioC).getAsDouble();
//						}
//						i++;
//					}
//				}
//				filasObject[columnaArreglo.size()-3] = totalPlanificado;
//				filasObject[columnaArreglo.size()-2] = totalReal;
//				filasObject[columnaArreglo.size()-1] = fila.get("metaFinal").getAsString();
//			}	
//			
//			datos.put(filaId+"", filasObject);
//			filaId++;
//		}
//			
//		
//		String path = excel.ExportarExcel(datos, "Metas de Préstamo", usuario);
//
//		
//		File file=new File(path);
//		if(file.exists()){
//	        FileInputStream is = null;
//	        try {
//	        	is = new FileInputStream(file);
//	        }
//	        catch (Exception e) {
//	        	
//	        }
//	        //
//	        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//	        
//	        int readByte = 0;
//	        byte[] buffer = new byte[2024];
//
//            while(true)
//            {
//                readByte = is.read(buffer);
//                if(readByte == -1)
//                {
//                    break;
//                }
//                outByteStream.write(buffer);
//            }
//            
//            file.delete();
//            
//            is.close();
//            outByteStream.flush();
//            outByteStream.close();
//            
//	        outArray = Base64.encode(outByteStream.toByteArray());
//		
//		}
		return outArray;
	}
	
//
//	private stproductometa getFechaInicioFinProducto(stproductometa productometa){
//		Date fechaInicio = null;
//		Date fechaFin = null;
//		List <Actividad> actividades = getActividadesProducto(productometa.id);
//		for(Actividad actividad : actividades){
//			//actividad = ActividadDAO.getFechasActividad(actividad);
//			if (fechaInicio == null || fechaInicio.after(actividad.getFechaInicio())){
//				fechaInicio = actividad.getFechaInicio();
//			}
//			if (fechaFin == null || fechaFin.before(actividad.getFechaFin())){
//				fechaFin = actividad.getFechaFin();
//			}
//		}
//		productometa.fechaInicio = Utils.formatDate(fechaInicio);
//		productometa.fechaFin = Utils.formatDate(fechaFin);
//		return productometa;
//	}
//	
	
}