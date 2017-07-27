package servlets;
import java.sql.Connection;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import dao.InformacionPresupuestariaDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.CMariaDB;
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
		Integer objeto_tipo;
		Integer nivel;
		stanio[] anios; 
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
		
		BigDecimal enero;
		BigDecimal febrero;
		BigDecimal marzo;
		BigDecimal abril;
		BigDecimal mayo;
		BigDecimal junio;
		BigDecimal julio;
		BigDecimal agosto;
		BigDecimal septiembre;
		BigDecimal octubre;
		BigDecimal noviembre;
		BigDecimal diciembre;
		BigDecimal anio;
		
	}
	
	
	
	
	String[] columnaNames = null;
	List<Integer> actividadesCosto = null;
       
    public SInformacionPresupuestaria() {
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
		Integer anoInicial = Utils.String2Int(map.get("anoInicial"),0);
		Integer anoFinal = Utils.String2Int(map.get("anoFinal"),0);
		
		
		if(accion.equals("generarInforme")){
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
				
			Prestamo objPrestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(idPrestamo, 1);
			String codigoPresupuestario = "";
			Integer fuente = 0;
			Integer organismo = 0;
			Integer correlativo = 0;
			if(objPrestamo != null){
				codigoPresupuestario = Long.toString(objPrestamo.getCodigoPresupuestario());
				fuente = Utils.String2Int(codigoPresupuestario.substring(0,2));
				organismo = Utils.String2Int(codigoPresupuestario.substring(2,6));
				correlativo = Utils.String2Int(codigoPresupuestario.substring(6,10));
			}
			
			if(proyecto != null){
				List<stprestamo> lstPrestamo = new ArrayList<>();
				stprestamo tempPrestamo = null;
			
				tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
				if(CMariaDB.connect()){
						Connection conn = CMariaDB.getConnection();
						ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
						
						tempPrestamo.nombre = proyecto.getNombre();
						tempPrestamo.objeto_id = proyecto.getId();
						tempPrestamo.objeto_tipo = 1;
						tempPrestamo.nivel = 1;
						
						ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoProyecto
								(fuente, organismo, correlativo,anoInicial,anoFinal, conn);
						
						tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
						
						lstPrestamo.add(tempPrestamo);
						
						for(Integer componente:componentes){
							tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
							Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
							tempPrestamo.nombre = objComponente.getNombre();
							tempPrestamo.objeto_id = objComponente.getId();
							tempPrestamo.objeto_tipo = 2;
							tempPrestamo.nivel = 2;
							
							 presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
									anoInicial, anoFinal, objComponente.getPrograma(), objComponente.getSubprograma(), objComponente.getProyecto_1(), 
									objComponente.getActividad(), objComponente.getObra(), conn);
							
							tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
							
							lstPrestamo.add(tempPrestamo);							
							ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
							for(Integer producto: productos){
								tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
								Producto objProducto = ProductoDAO.getProductoPorId(producto);
								tempPrestamo.nombre = objProducto.getNombre();
								tempPrestamo.objeto_id = objProducto.getId();
								tempPrestamo.objeto_tipo = 3;
								tempPrestamo.nivel = 3;
								
								
								
								presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
										anoInicial, anoFinal, objProducto.getPrograma(), objProducto.getSubprograma(), objProducto.getProyecto(), 
										objProducto.getActividad(), objProducto.getObra(), conn);
								
								tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
								lstPrestamo.add(tempPrestamo);
								
							
								ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
								for(Integer subproducto: subproductos){
									tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
									Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
									tempPrestamo.nombre = objSubProducto.getNombre();
									tempPrestamo.objeto_id = objSubProducto.getId();
									tempPrestamo.objeto_tipo = 4;
									tempPrestamo.nivel = 4;
									
									presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
											anoInicial, anoFinal, objSubProducto.getPrograma(), objSubProducto.getSubprograma(), objSubProducto.getProyecto(), 
											objSubProducto.getActividad(), objSubProducto.getObra(), conn);
									
									tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
									lstPrestamo.add(tempPrestamo);
									
							
									//actividades sub producto
									ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),objSubProducto.getId(), conn);
									for(ArrayList<Integer> actividad : actividades){
										tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
										Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
										tempPrestamo.nombre = objActividad.getNombre();
										tempPrestamo.objeto_id = objActividad.getId();
										tempPrestamo.objeto_tipo = 5;
										tempPrestamo.nivel = 5 + actividad.get(1);
										
										presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
												anoInicial, anoFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
												objActividad.getActividad(), objActividad.getObra(), conn);
										
										tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
										lstPrestamo.add(tempPrestamo);
										
										
									
									}
								}
								
								//actividades producto
								ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
								for(ArrayList<Integer> actividad : actividades){
									tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
									Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
									tempPrestamo.nombre = objActividad.getNombre();
									tempPrestamo.objeto_id = objActividad.getId();
									tempPrestamo.objeto_tipo = 5;
									tempPrestamo.nivel = 4 + actividad.get(1);
									
									presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
											anoInicial, anoFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
											objActividad.getActividad(), objActividad.getObra(), conn);
									
									tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
									
									lstPrestamo.add(tempPrestamo);
									
								}  
							} 
							//actividades componente
							ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);							
							for(ArrayList<Integer> actividad : actividades){
								tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
								Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
								tempPrestamo.nombre = objActividad.getNombre();
								tempPrestamo.objeto_id = objActividad.getId();
								tempPrestamo.objeto_tipo = 5;
								tempPrestamo.nivel = 3 + actividad.get(1);
								
								presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
										anoInicial, anoFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
										objActividad.getActividad(), objActividad.getObra(), conn);
								
								tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
								lstPrestamo.add(tempPrestamo);
								
							} 
						}
						
						
						//actividades prestamo
						ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
						
						for(ArrayList<Integer> actividad : actividades){
							tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
							Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
							tempPrestamo.nombre = objActividad.getNombre();
							tempPrestamo.objeto_id = objActividad.getId();
							tempPrestamo.objeto_tipo = 5;
							tempPrestamo.nivel = 2 + actividad.get(1);
							
							 presupuestoPrestamo = InformacionPresupuestariaDAO.getPresupuestoPorObjeto(fuente, organismo, correlativo, 
									anoInicial, anoFinal, objActividad.getPrograma(), objActividad.getSubprograma(), objActividad.getProyecto(), 
									objActividad.getActividad(), objActividad.getObra(), conn);
							
							tempPrestamo = getPresupuesto(presupuestoPrestamo, anoInicial, anoFinal, tempPrestamo);
							
							
							lstPrestamo.add(tempPrestamo);
						
						}
						
						CMariaDB.close();
						response_text=new GsonBuilder().serializeNulls().create().toJson(lstPrestamo);
				        response_text = String.join("", "\"prestamo\":",response_text);
				        response_text = String.join("", "{\"success\":true,", response_text, "}");
					}else{
						response_text = String.join("", "{\"success\":false}");
					}
				}
				
				
			
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
	
	private stprestamo getPresupuesto (ArrayList<ArrayList<BigDecimal>> presupuestoPrestamo,
			int anoInicial, int anoFinal, stprestamo prestamo){
		
		stanio[] anios = inicializarStanio(anoInicial, anoFinal);
		if(presupuestoPrestamo.size() > 0){
			
			
			for(ArrayList<BigDecimal> objprestamopresupuesto : presupuestoPrestamo){
				
				stanio aniotemp = new stanio(); 
				aniotemp.enero = objprestamopresupuesto.get(0);
				aniotemp.febrero = objprestamopresupuesto.get(1);
				aniotemp.marzo = objprestamopresupuesto.get(2);
				aniotemp.abril = objprestamopresupuesto.get(3);
				aniotemp.mayo = objprestamopresupuesto.get(4);
				aniotemp.junio = objprestamopresupuesto.get(5);
				aniotemp.julio = objprestamopresupuesto.get(6);
				aniotemp.agosto = objprestamopresupuesto.get(7);
				aniotemp.septiembre = objprestamopresupuesto.get(8);
				aniotemp.octubre = objprestamopresupuesto.get(9);
				aniotemp.noviembre = objprestamopresupuesto.get(10);
				aniotemp.diciembre = objprestamopresupuesto.get(11);
				int pos = anoFinal- objprestamopresupuesto.get(12).intValue();
				aniotemp.anio = new BigDecimal(anoInicial + pos);
				anios[pos] = aniotemp;
			}
		}
			prestamo.anios = anios;
			return prestamo;
	}
	
	private stprestamo inicializarEstructura(Integer anoInicio, Integer anoFinal){
			stprestamo temp = new stprestamo();
			return temp;
	}
	
	private stanio[] inicializarStanio (int anioInicial, int anioFinal){
		
		
		int longitudArrelgo = anioFinal - anioInicial+1;
		
		stanio[] anios = new stanio[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stanio temp = new stanio();
			temp.enero = null;
			temp.febrero = null;
			temp.marzo = null;
			temp.abril=null;
			temp.mayo = null;
			temp.junio = null;
			temp.julio = null;
			temp.agosto = null;
			temp.septiembre = null;
			temp.octubre = null;
			temp.noviembre = null;
			temp.diciembre = null;
			
			temp.anio = new BigDecimal(anioInicial + i);
			anios[i] = temp;
		}
		return anios;
		
		
		
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
			CLogger.write("2", SInformacionPresupuestaria.class, e);
		}
	}
}