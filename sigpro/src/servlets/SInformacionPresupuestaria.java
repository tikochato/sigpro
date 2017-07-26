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
		/*stpresupuesto[] enero;
		stpresupuesto[] febrero;
		stpresupuesto[] marzo;
		stpresupuesto[] abril;
		stpresupuesto[] mayo;
		stpresupuesto[] junio;
		stpresupuesto[] julio;
		stpresupuesto[] agosto;
		stpresupuesto[] septiembre;
		stpresupuesto[] octubre;
		stpresupuesto[] noviembre;
		stpresupuesto[] diciembre;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total; */
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
	
	class stprestamotrimestre{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stpresupuesto[] trimestre1;
		stpresupuesto[] trimestre2;
		stpresupuesto[] trimestre3;
		stpresupuesto[] trimestre4;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total;
	}
	
	class stprestamocuatrimestre{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stpresupuesto[] cuatrimestre1;
		stpresupuesto[] cuatrimestre2;
		stpresupuesto[] cuatrimestre3;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total;
	}
	
	class stprestamosemestre{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stpresupuesto[] semestre1;
		stpresupuesto[] semestre2;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total;
	}
	
	class stprestamoanual{
		String nombre;
		Integer objeto_id;
		Integer objeto_tipo;
		Integer nivel;
		stpresupuesto[] ano;
		stpresupuesto[] totalAnual;
		stpresupuesto[] total;
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
						
						lstPrestamo.add(tempPrestamo);
						
						
						
						for(Integer componente:componentes){
							tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
							Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
							tempPrestamo.nombre = objComponente.getNombre();
							tempPrestamo.objeto_id = objComponente.getId();
							tempPrestamo.objeto_tipo = 2;
							tempPrestamo.nivel = 2;
							
							lstPrestamo.add(tempPrestamo);
							
							
							ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
							for(Integer producto: productos){
								tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
								Producto objProducto = ProductoDAO.getProductoPorId(producto);
								tempPrestamo.nombre = objProducto.getNombre();
								tempPrestamo.objeto_id = objProducto.getId();
								tempPrestamo.objeto_tipo = 3;
								tempPrestamo.nivel = 3;
								
								lstPrestamo.add(tempPrestamo);
								
							
								ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
								for(Integer subproducto: subproductos){
									tempPrestamo = inicializarEstructura(anoInicial, anoFinal);
									Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
									tempPrestamo.nombre = objSubProducto.getNombre();
									tempPrestamo.objeto_id = objSubProducto.getId();
									tempPrestamo.objeto_tipo = 4;
									tempPrestamo.nivel = 4;
									
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
	
	private stprestamo inicializarEstructura(Integer anoInicio, Integer anoFinal){
		stprestamo temp = new stprestamo();
		/*temp.enero = new stpresupuesto[anoFinal-anoInicio+1];
		temp.febrero = new stpresupuesto[anoFinal-anoInicio+1];
		temp.marzo = new stpresupuesto[anoFinal-anoInicio+1];
		temp.abril = new stpresupuesto[anoFinal-anoInicio+1];
		temp.mayo = new stpresupuesto[anoFinal-anoInicio+1];
		temp.junio = new stpresupuesto[anoFinal-anoInicio+1];
		temp.julio = new stpresupuesto[anoFinal-anoInicio+1];
		temp.agosto = new stpresupuesto[anoFinal-anoInicio+1];
		temp.septiembre = new stpresupuesto[anoFinal-anoInicio+1];
		temp.octubre = new stpresupuesto[anoFinal-anoInicio+1];
		temp.noviembre = new stpresupuesto[anoFinal-anoInicio+1];
		temp.diciembre = new stpresupuesto[anoFinal-anoInicio+1];*/
		return temp;
	}
	
	private stprestamobimestre inicializarEstructuraBimestre(Integer anoInicio, Integer anoFinal){
		stprestamobimestre temp = new stprestamobimestre();
		temp.bimestre1 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.bimestre2 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.bimestre3 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.bimestre4 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.bimestre5 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.bimestre6 = new stpresupuesto[anoFinal-anoInicio+1];
		return temp;
	}
	
	private stprestamotrimestre inicializarEstructuraTrimestre(Integer anoInicio, Integer anoFinal){
		stprestamotrimestre temp = new stprestamotrimestre();
		temp.trimestre1 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.trimestre2 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.trimestre3 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.trimestre4 = new stpresupuesto[anoFinal-anoInicio+1];
		return temp;
	}
	
	private stprestamocuatrimestre inicializarEstructuraCuatrimestre(Integer anoInicio, Integer anoFinal){
		stprestamocuatrimestre temp = new stprestamocuatrimestre();
		temp.cuatrimestre1 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.cuatrimestre2 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.cuatrimestre3 = new stpresupuesto[anoFinal-anoInicio+1];
		return temp;
	}
	
	private stprestamosemestre inicializarEstructuraSemestre(Integer anoInicio, Integer anoFinal){
		stprestamosemestre temp = new stprestamosemestre();
		temp.semestre1 = new stpresupuesto[anoFinal-anoInicio+1];
		temp.semestre2 = new stpresupuesto[anoFinal-anoInicio+1];
		return temp;
	}
	
	private stprestamoanual inicializarEstructuraAnio(Integer anoInicio, Integer anoFinal){
		stprestamoanual temp = new stprestamoanual();
		temp.ano = new stpresupuesto[anoFinal-anoInicio+1];
		return temp;
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