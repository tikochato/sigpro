package servlets;

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
import java.math.BigInteger;

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
import dao.SubComponenteDAO;
import dao.EstructuraProyectoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.AsignacionRaci;
import pojo.Colaborador;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subcomponente;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
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
			List<stmatriz> lstMatriz;
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			String lineaBase = map.get("lineaBase");
			lstMatriz = getMatriz(idPrestamo, lineaBase);
			boolean sinColaborador = false;
			List<stcolaborador> stcolaboradores = getColaboradores(idPrestamo, usuario);
			if (stcolaboradores.size() ==0){
				sinColaborador = true;
				stcolaborador temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "R";
				stcolaboradores.add(temp);
				temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "A";
				stcolaboradores.add(temp);
				temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "C";
				stcolaboradores.add(temp);
				temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "I";
				stcolaboradores.add(temp);	
			}
			if(lstMatriz!=null && stcolaboradores!=null){
				String response_col = new GsonBuilder().serializeNulls().create().toJson(stcolaboradores);
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(lstMatriz);
		        response_text = String.join("", "\"matriz\":",response_text,",",
		        		"\"sinColaboradores\":",sinColaborador ? "true" : "false",",",
		        		"\"colaboradores\":",response_col);
		        
		        response_text = String.join("", "{\"success\":true,", response_text, "}");
			}else{
				response_text = String.join("", "{\"success\":false}");
			}
		}else if(accion.equals("getInformacionTarea")){
			Integer objetoId = Utils.String2Int(map.get("objetoId"),0);
			Integer objetoTipo = Utils.String2Int(map.get("objetoTipo"),0);
			String lineaBase = map.get("lineaBase");
			String rol = map.get("rol");
			stinformacion informacion = new stinformacion();
			
			switch (objetoTipo){
			case 0:
				Proyecto proyecto = ProyectoDAO.getProyectoPorId(objetoId, usuario);
				informacion.nombreTarea = proyecto.getNombre();
			break;
			case 1:
				Componente componente = ComponenteDAO.getComponentePorId(objetoId, usuario);
				informacion.nombreTarea = componente.getNombre();
			break;
			case 2:
				Subcomponente subcomponente = SubComponenteDAO.getSubComponentePorId(objetoId, usuario);
				informacion.nombreTarea = subcomponente.getNombre();
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
				Actividad actividad = ActividadDAO.getActividadPorId(objetoId);
				informacion.nombreTarea = actividad.getNombre();
			break;
			}
			
			AsignacionRaci asignacion = AsignacionRaciDAO.getAsignacionPorRolTarea(objetoId, objetoTipo, rol, lineaBase);
			
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
			List<AsignacionRaci> asignaciones  = AsignacionRaciDAO.getAsignacionesRaci(objetoId, objetoTipo, null);
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
				byte [] outArray = exportarExcel(idPrestamo, null, usuario);
		
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Matriz_RACI.xls");
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
			datos = generarDatos(idPrestamo, colaboradores, null, usuario);
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
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "in-line; 'MatrizRACI.pdf'");
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
	
	private List<stmatriz> getMatriz(Integer idPrestamo, String lineaBase){
		List<stmatriz> lstMatriz= new ArrayList<>();
		List<?> estructuraProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo, lineaBase);
		for(Object objeto : estructuraProyecto){
			Object[] obj = (Object[]) objeto;
			stmatriz tempmatriz = new stmatriz();
			tempmatriz.objetoId = (Integer)obj[0];
			tempmatriz.objetoNombre = (String)obj[1];
			tempmatriz.nivel = (obj[3]!=null) ? ((String)obj[3]).length()/8 : 0;
			tempmatriz.objetoTipo = ((BigInteger) obj[2]).intValue();
			getAsignacionRACI(tempmatriz, lineaBase);
			lstMatriz.add(tempmatriz);
		}
		return lstMatriz;
	}
	
	private List<stcolaborador> getColaboradores(Integer idPrestamo, String usuario){
		List<stcolaborador> stcolaboradores = null;
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
		
		if(proyecto != null){
			List<Colaborador> colaboradores = AsignacionRaciDAO.getColaboradoresPorProyecto(idPrestamo, null);
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
	
	public void getAsignacionRACI(stmatriz item, String lineaBase){
		List<AsignacionRaci> asignaciones = AsignacionRaciDAO.getAsignacionesRaci(item.objetoId,item.objetoTipo, lineaBase);
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
	
	private byte[] exportarExcel(Integer idPrestamo, String lineaBase, String usuario) throws IOException{
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			List<stcolaborador> colaboradores = getColaboradores(idPrestamo, usuario);
			if (colaboradores.size() ==0){
			
				stcolaborador temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "R";
				colaboradores.add(temp);
				temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "A";
				colaboradores.add(temp);
				temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "C";
				colaboradores.add(temp);
				temp = new stcolaborador();
				temp.id = 1;
				temp.nombre = "I";
				colaboradores.add(temp);
				
			}
			
			headers = generarHeaders(colaboradores);
			datos = generarDatos(idPrestamo, colaboradores, lineaBase, usuario);
			excel = new CExcel("Matriz RACI", false, null);
			Proyecto proyecto = ProyectoDAO.getProyecto(idPrestamo);
			wb=excel.generateExcelOfData(datos, "Matriz RACI - "+proyecto.getNombre(), headers, null, true, usuario);
			wb.write(outByteStream);
			outArray = Base64.encode(outByteStream.toByteArray());
			
		}catch(Exception e){
			CLogger.write("2", SMatrizRACI.class, e);
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
	
	public String[][] generarDatos(Integer idPrestamo, List<stcolaborador> colaboradores, String lineaBase, String usuario){
		List<stmatriz> stmatriz = getMatriz(idPrestamo, lineaBase);
		String[][] datos = null;
		
		if(stmatriz!= null && colaboradores!=null){
			datos = new String[stmatriz.size()][colaboradores.size()+1];
			for (int i=0; i<stmatriz.size(); i++){
				stmatriz matriz = stmatriz.get(i);
				String sangria="";
				for(int s=1; s<matriz.nivel; s++){
					sangria+="   ";
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
