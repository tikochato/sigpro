package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.codec.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.AdministracionTransaccionalDAO;
import utilities.CExcel;
import utilities.CGraficaExcel;
import utilities.CLogger;
import utilities.CMariaDB;
import utilities.CPdf;
import utilities.Utils;

@WebServlet("/SAdministracionTransaccional")
public class SAdministracionTransaccional extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	class sttransaccion{
		Integer objeto_id;
		String nombre;
		Integer objeto_tipo;
		String nombre_objeto_tipo;
		String usuario_creo;
		String usuario_actualizo;
		String fecha_creacion;
		String fecha_modificacion;
		String estado;
	}
	
	class stusuario{
		String usuario;
		Integer creados;
		Integer actualizados;
		Integer eliminados;
	}

	Integer totalCreados = 0;
	Integer totalActualizados = 0;
	Integer totalEliminados = 0;
       
    public SAdministracionTransaccional() {
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
		;
		Map<String, String> map = gson.fromJson(sb.toString(), type);
		String accion = map.get("accion");
		String response_text="";
		if(accion.equals("getComponentes")){
			List<stusuario> lstusuarios = getAdministracionTransaccional();
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstusuarios);
	        response_text = String.join("", "\"usuarios\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}else if (accion.equals("exportarExcel")){
			try{
		        byte [] outArray = exportarExcel(usuario);
			
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Administracion_Transaccional.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}catch(Exception e){
				CLogger.write("1", SAdministracionTransaccional.class, e);
			}
		}else if(accion.equals("exportarPdf")){
			CPdf archivo = new CPdf("Administraci�n Transaccional");
			String headers[][];
			String datos[][];
			headers = generarHeaders();
			datos = generarDatos(usuario);
			String path = archivo.ExportarPdfAdministracionTransaccional(headers, datos,usuario);
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
				response.setHeader("Content-Disposition", "in-line; 'AdministracionTransaccional.pdf'");
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

		if (accion.equals("exportarExcel")){
	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
		}
	}
	
	private List<stusuario> getAdministracionTransaccional(){
		List<stusuario> lstusuarios = new ArrayList<stusuario>();
		if(CMariaDB.connect()){
			Connection conn = CMariaDB.getConnection();
			List<List<String>> usuarios = AdministracionTransaccionalDAO.obtenerUsuarios(conn);
			for(List<String> user : usuarios){
				stusuario temp = new stusuario();
				temp.usuario = user.get(0);
				temp.creados = Utils.String2Int(user.get(1));
				temp.actualizados = Utils.String2Int(user.get(2));
				temp.eliminados = Utils.String2Int(user.get(3));
				lstusuarios.add(temp);
			}
		}
		return lstusuarios;
	}
	
	private byte[] exportarExcel(String usuario){
		byte [] outArray = null;
		CExcel excel=null;
		String headers[][];
		String datos[][];
		
		Workbook wb=null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		try{			
			headers = generarHeaders();
			datos = generarDatos(usuario);
			CGraficaExcel grafica = generarGrafica(datos);
			excel = new CExcel("Administración Transaccional", false, grafica);
			wb=excel.generateExcelOfData(datos, "Administración Transaccional", headers, null, true, usuario);
		
		wb.write(outByteStream);
		outArray = Base64.encode(outByteStream.toByteArray());
		}catch(Exception e){
			CLogger.write("2", SAdministracionTransaccional.class, e);
		}
		return outArray;
	}
	
	private String[][] generarHeaders(){
		String headers[][];
		
		headers = new String[][]{
			{"Usuario", "Creados", "Actualizados", "Eliminados"},  //titulos
			null, //mapeo
			{"string", "int", "int", "int"}, //tipo dato
			{"", "sum", "sum", "sum"}, //operaciones columnas
			null, //operaciones div
			null,
			null,
			null
			};
			
		return headers;
	}
	
	public String[][] generarDatos(String usuario){
		List<stusuario> lstusuarios = getAdministracionTransaccional();
		String[][] datos = null;
		totalCreados = 0;
		totalActualizados = 0;
		totalEliminados = 0;
		if (lstusuarios != null && !lstusuarios.isEmpty()){ 
			datos = new String[lstusuarios.size()][4];
			for (int i=0; i<lstusuarios.size(); i++){
				datos[i][0]=lstusuarios.get(i).usuario;
				datos[i][1]=lstusuarios.get(i).creados.toString();
				datos[i][2]=lstusuarios.get(i).actualizados.toString();
				datos[i][3]=lstusuarios.get(i).eliminados.toString();
				totalCreados += lstusuarios.get(i).creados;
				totalActualizados += lstusuarios.get(i).actualizados;
				totalEliminados += lstusuarios.get(i).eliminados;
			}
		}
			
		return datos;
	}
	
	public CGraficaExcel generarGrafica(String[][] datosTabla){
		
		String[][] datos = new String[][]{
			{"Creados","Actualizados","Eliminados"},
			{totalCreados.toString(),totalActualizados.toString(),totalEliminados.toString()}
		};
		String[][] datosIgualar= new String[][]{
			{"","",""},
			{"1."+(datosTabla.length+30),"2."+(datosTabla.length+30),"3."+(datosTabla.length+30)}
		};
		
		String[] tipoData = new String[]{"string","int"};
		CGraficaExcel grafica = new CGraficaExcel("Administración Transaccional", CGraficaExcel.EXCEL_CHART_BAR, "Cantidad", "Estados", datos, tipoData, datosIgualar);
	
		return grafica;
	}
} 
