package servlets;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.AsignacionRaciDAO;
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.AsignacionRaci;
import pojo.Colaborador;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.CPdf;
import utilities.Utils;


@WebServlet("/SMatrizRACI")
public class SMatrizRACI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	class stmatriz{
		Integer objetoId;
		String objetoNombre;
		String nombreR;
		int idR;
		String nombreA;
		int idA;
		String nombreC;
		int idC;
		String nombreI;
		int idI;
		int objetoTipo;
		int nivel;
	}
	
	class stcolaborador{
		int id;
		String nombre;
	}
	
	class stasignacion{
		int colaboradorId;
		String colaboradorNombre;
		String rolId;
		String rolNombre;
	}
	
	class stinformacion{
		String nombreTarea;
		String estadoTarea;
		String rol;
		String nombreColaborador;
		String estadoColaborador;
		String fechaInicio;
		String fechaFin;
		String email;
	}

    public SMatrizRACI() {
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
		
		if(accion.equals("getMatriz")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			List<stmatriz> lstMatriz = getMatriz(idPrestamo, usuario);
			List<stcolaborador> stcolaboradores = getColaboradores(idPrestamo, usuario);
			if(lstMatriz!=null && stcolaboradores!=null){
				String response_col = new GsonBuilder().serializeNulls().create().toJson(stcolaboradores);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstMatriz);
		        response_text = String.join("", "\"matriz\":",response_text,",",
		        		"\"colaboradores\":",response_col);
		        
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}else{
				response_text = String.join("", "{\"success\":false}");
			}
		}else if(accion.equals("getInformacionTarea")){
			Integer objetoId = Utils.String2Int(map.get("objetoId"),0);
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
			String rol = map.get("rol");
			stinformacion informacion = new stinformacion();
			
			switch (objetoTipo){
			case 1:
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(objetoId, usuario);
				informacion.nombreTarea = proyecto.getNombre();
			break;
			case 2:
				Componente componente = ComponenteDAO.getComponentePorId(objetoId, usuario);
				informacion.nombreTarea = componente.getNombre();
			break;
			case 3:
				Producto producto = ProductoDAO.getProductoPorId(objetoId, usuario);
				informacion.nombreTarea = producto.getNombre();
			break;
			case 4:
				Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objetoId, usuario);
				informacion.nombreTarea = subproducto.getNombre();
			break;
				case 5:
					Actividad actividad = ActividadDAO.getActividadPorId(objetoId, usuario);
					informacion.nombreTarea = actividad.getNombre();
				break;
			}
			
			AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(objetoId, objetoTipo, rol);
			
			if (rol.equalsIgnoreCase("R")){
				informacion.rol = "Responsable";
			} else if (rol.equalsIgnoreCase("a")){
				informacion.rol = "Cuentadante";
			}else if (rol.equalsIgnoreCase("c")){
				informacion.rol = "Consultor";
			}else if (rol.equalsIgnoreCase("i")){
				informacion.rol = "Quien informa";
			}
			
			informacion.nombreColaborador = String.join(" ", asignacion.getColaborador().getPnombre(),
					asignacion.getColaborador().getSnombre()!=null ? asignacion.getColaborador().getSnombre() : "",
					asignacion.getColaborador().getPapellido(),
					asignacion.getColaborador().getSapellido() !=null ? asignacion.getColaborador().getSapellido() : "");
			
			informacion.estadoColaborador = asignacion.getColaborador().getEstado() ==1 ? "Alta" : "Baja";
			informacion.email = asignacion.getColaborador().getUsuario() !=null ? asignacion.getColaborador().getUsuario().getEmail() : ""; 
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(informacion);
	        response_text = String.join("", "\"informacion\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");	
		}else if  (accion.equals("getAsignacionPorObjeto")){
			Integer objetoId = Utils.String2Int(map.get("objetoId"),0);
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
			List<AsignacionRaci> asignaciones  = AsignacionRaciDAO.getAsignacionesRaci(objetoId, objetoTipo);
			List<stasignacion> asignacionesRet = new ArrayList<>();
			for (AsignacionRaci asignacion : asignaciones){
				stasignacion temp = new stasignacion();
				temp.colaboradorId = asignacion.getColaborador().getId();
				temp.colaboradorNombre = asignacion.getColaborador().getPnombre() + " "
						+ asignacion.getColaborador().getPapellido();
				temp.rolId = asignacion.getRolRaci();
				asignacionesRet.add(temp);
			}
			response_text=new GsonBuilder().serializeNulls().create().toJson(asignacionesRet);
	        response_text = String.join("", "\"asignaciones\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text, "}");
			
		}else if (accion.equals("exportarExcel")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			
			try{
	        byte [] outArray = exportarExcel(idPrestamo, usuario);
		
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "attachment; MatrizRACI_.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SMatrizRACI.class, e);
			}
		}else if(accion.equals("exportarPdf")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			CPdf archivo = new CPdf("Matriz Raci");
			String headers[][];
			String datos[][];
			List<stcolaborador> colaboradores = getColaboradores(idPrestamo, usuario);
			headers = generarHeaders(colaboradores);
			datos = generarDatos(idPrestamo, colaboradores, usuario);
			String path = archivo.exportarMatrizRaci(headers, datos,usuario);
			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
					CLogger.write("5", SAdministracionTransaccional.class, e);
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
				response.setHeader("Expires:", "0"); 
				response.setHeader("Content-Disposition", "in-line; 'AvanceActividades.pdf'");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
		else{
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
	
	private List<stmatriz> getMatriz(Integer idPrestamo, String usuario){
		List<stmatriz> lstMatriz=null;
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
		
		if(proyecto != null){
			stmatriz tempmatriz = new stmatriz();
			lstMatriz = new ArrayList<>();
			if(CMariaDB.connect()){
					Connection conn = CMariaDB.getConnection();
					ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
					tempmatriz = new stmatriz();
					tempmatriz.objetoId = proyecto.getId();
					tempmatriz.objetoNombre = proyecto.getNombre();
					tempmatriz.nivel = 1;
					tempmatriz.objetoTipo = 1;
					getAsignacionRACI(tempmatriz);
					lstMatriz.add(tempmatriz);
					
					for(Integer componente:componentes){
						
						Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
						tempmatriz = new stmatriz();
						tempmatriz.objetoId = objComponente.getId();
						tempmatriz.objetoNombre = objComponente.getNombre();
						tempmatriz.nivel = 2;
						tempmatriz.objetoTipo = 2;
						getAsignacionRACI(tempmatriz);
						lstMatriz.add(tempmatriz);
						
						
						ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
						for(Integer producto: productos){
							
							Producto objProducto = ProductoDAO.getProductoPorId(producto);
							tempmatriz = new stmatriz();
							tempmatriz.objetoId = objProducto.getId();
							tempmatriz.objetoNombre = objProducto.getNombre();
							tempmatriz.nivel = 3;
							tempmatriz.objetoTipo = 3;
							getAsignacionRACI(tempmatriz);
							lstMatriz.add(tempmatriz);
						
							ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
							for(Integer subproducto: subproductos){
								
								Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
								tempmatriz = new stmatriz();
								tempmatriz.objetoId = objSubProducto.getId();
								tempmatriz.objetoNombre = objSubProducto.getNombre();
								tempmatriz.nivel = 4;
								tempmatriz.objetoTipo = 4;
								getAsignacionRACI(tempmatriz);
								lstMatriz.add(tempmatriz);
						
								ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolSubProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(),objSubProducto.getId(), conn);
								for(ArrayList<Integer> actividad : actividades){
									
									Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
									tempmatriz = new stmatriz();
									tempmatriz.objetoId = objActividad.getId();
									tempmatriz.objetoNombre = objActividad.getNombre();
									tempmatriz.nivel = 5 + actividad.get(1);
									tempmatriz.objetoTipo = 5;
									getAsignacionRACI(tempmatriz);
									lstMatriz.add(tempmatriz);
														
								}
							}
					
							ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolProductoActividades(idPrestamo, objComponente.getId(), objProducto.getId(), conn);
							for(ArrayList<Integer> actividad : actividades){
								Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
								tempmatriz = new stmatriz();
								tempmatriz.objetoId = objActividad.getId();
								tempmatriz.objetoNombre = objActividad.getNombre();
								tempmatriz.nivel = 5 + actividad.get(1);
								tempmatriz.objetoTipo = 5;
								getAsignacionRACI(tempmatriz);
								lstMatriz.add(tempmatriz);
							}  
						} 
					
						ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolComponentesActividades(idPrestamo, objComponente.getId(), conn);							
						for(ArrayList<Integer> actividad : actividades){
							Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
							tempmatriz = new stmatriz();
							tempmatriz.objetoId = objActividad.getId();
							tempmatriz.objetoNombre = objActividad.getNombre();
							tempmatriz.nivel = 5 + actividad.get(1);
							tempmatriz.objetoTipo = 5;
							getAsignacionRACI(tempmatriz);
							lstMatriz.add(tempmatriz);
							
						} 
					}
				
					ArrayList<ArrayList<Integer>> actividades = InformacionPresupuestariaDAO.getEstructuraArbolPrestamoActividades(idPrestamo, conn);
					
					for(ArrayList<Integer> actividad : actividades){
						Actividad objActividad = ActividadDAO.getActividadPorId(actividad.get(0), usuario);
						tempmatriz = new stmatriz();
						tempmatriz.objetoId = objActividad.getId();
						tempmatriz.objetoNombre = objActividad.getNombre();
						tempmatriz.nivel = 5 + actividad.get(1);
						tempmatriz.objetoTipo = 5;
						getAsignacionRACI(tempmatriz);
						lstMatriz.add(tempmatriz);
					
					}
					
					CMariaDB.close();
				}
			}
		return lstMatriz;
	}
	
	private List<stcolaborador> getColaboradores(Integer idPrestamo, String usuario){
		List<stcolaborador> stcolaboradores = null;
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
		
		if(proyecto != null){
			List<Colaborador> colaboradores = AsignacionRaciDAO.getColaboradoresPorProyecto(idPrestamo);
			stcolaboradores = new ArrayList<stcolaborador>();
			for (Colaborador colaborador : colaboradores){
				stcolaborador temp = new stcolaborador();
				temp.id = colaborador.getId();
				temp.nombre = colaborador.getPnombre() + " " + colaborador.getPapellido();
				stcolaboradores.add(temp);
			}
		}
		
		return stcolaboradores;
	}
	
	public void getAsignacionRACI(stmatriz item){
		List<AsignacionRaci> asignaciones = AsignacionRaciDAO.getAsignacionesRaci(item.objetoId,item.objetoTipo);
		if (!asignaciones.isEmpty()){
			for (AsignacionRaci asignacion: asignaciones){
				
				if (asignacion.getRolRaci().equalsIgnoreCase("r")){
					item.nombreR = asignacion.getColaborador().getPnombre() + " " + asignacion.getColaborador().getPapellido();
					item.idR = asignacion.getColaborador().getId();
				}else if (asignacion.getRolRaci().equalsIgnoreCase("a")){
					item.nombreA = asignacion.getColaborador().getPnombre() + " " + asignacion.getColaborador().getPapellido();
					item.idA = asignacion.getColaborador().getId();
				}else if (asignacion.getRolRaci().equalsIgnoreCase("c")){
					item.nombreC = asignacion.getColaborador().getPnombre() +  " " + asignacion.getColaborador().getPapellido();
					item.idC = asignacion.getColaborador().getId();
				}else if (asignacion.getRolRaci().equalsIgnoreCase("i")){
					item.nombreI = asignacion.getColaborador().getPnombre() + " " +asignacion.getColaborador().getPapellido();
					item.idI = asignacion.getColaborador().getId();
				}
			}
		}
	}
	
	private byte[] exportarExcel(Integer idPrestamo, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			List<stcolaborador> colaboradores = getColaboradores(idPrestamo, usuario);
			if(colaboradores!=null){
				headers = generarHeaders(colaboradores);
				datos = generarDatos(idPrestamo, colaboradores, usuario);
				excel = new CExcel("Matriz RACI", false, null);
				wb=excel.generateExcelOfData(datos, "Matriz RACI", headers, null, true, usuario);
			
				wb.write(outByteStream);
				outArray = Base64.encode(outByteStream.toByteArray());
			}
		}catch(Exception e){
			CLogger.write("4", SMatrizRACI.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(List<stcolaborador> colaboradores){
		String headers[][];
		Integer totalColumnas = colaboradores.size()+1;
		String titulo[] = new String[totalColumnas];
		String tipo[] = new String[totalColumnas];
		String operacion[] = new String[totalColumnas];
		titulo[0]="Tareas";
		tipo[0]="string";
		operacion[0]="";
		
		for (int i=0; i<colaboradores.size(); i++){
			titulo[i+1] = colaboradores.get(i).nombre;
			tipo[i+1] = "string";
			operacion[i+1] = "";
		}
		
		headers = new String[][]{
			titulo,  //titulos
			null, //mapeo
			tipo, //tipo dato
			operacion, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatos(Integer idPrestamo, List<stcolaborador> colaboradores, String usuario){
		List<stmatriz> stmatriz = getMatriz(idPrestamo, usuario);
		String[][] datos = null;
		
		if(stmatriz!= null && colaboradores!=null){
			datos = new String[stmatriz.size()][colaboradores.size()+1];
			for (int i=0; i<stmatriz.size(); i++){
				stmatriz matriz = stmatriz.get(i);
				String sangria;
				switch (matriz.objetoTipo){
					case 1: sangria = ""; break;
					case 2: sangria = "   "; break;
					case 3: sangria = "      "; break;
					case 4: sangria = "         "; break;
					case 5: sangria = "            "; break;
					default: sangria = "";
				}
				datos[i][0]=sangria+matriz.objetoNombre;
				for(int c=0; c<colaboradores.size(); c++){
					stcolaborador colaborador = colaboradores.get(c);
					if(matriz.idR==colaborador.id){
						datos[i][c+1]="R";
					}else if(matriz.idA==colaborador.id){
						datos[i][c+1]="A";
					}else if(matriz.idC==colaborador.id){
						datos[i][c+1]="C";
					}else if(matriz.idI==colaborador.id){
						datos[i][c+1]="I";
					}else{
						datos[i][c+1]="";
					}
				}
			}
		}
		
		return datos;
	}

}
