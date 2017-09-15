  package servlets;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
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
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CExcel;
import utilities.CLogger;
import utilities.Utils;


@WebServlet("/SAgenda")
public class SAgenda extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int OBJETO_ID_PROYECTO = 1;
	private static int OBJETO_ID_COMPONENTE = 2;
	private static int OBJETO_ID_PRODUCTO = 3;
	private static int OBJETO_ID_SUBPRODUCTO = 4;
	private static int OBJETO_ID_ACTIVIDAD= 5;
	private String strEdtActividad = "";
	private int edtActividad = 1;
	class stagenda {
		Integer id;
		String edt;
		Integer objetoTipo;
		String objetoTipoNombre;
		String nombre;;
		String fechaInicio;
		String fechaFin;
		String estado;
	}
    
    public SAgenda() {
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
		
		if(accion.equals("getAgenda")){
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			List<stagenda> lstagenda = obtenerListado(proyectoId, usuario);
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(lstagenda);
	        response_text = String.join("", "\"agenda\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
	        
	        response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");


	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();
	        
		}else if (accion.equals("exportarExcel")){
			CExcel excel = new CExcel("Agenda",false,null);
			int proyectoId = Utils.String2Int(map.get("proyectoid"), 0);
			List<stagenda> lstagenda = obtenerListado(proyectoId, usuario);
			
			Map<String,Object[]> datos = new HashMap<>();
			datos.put("0",   new Object[] {"EDT","Actividad", "Fecha Inicio","Fecha Fin", "Estado"});
			int fila = 1;
			
			for (stagenda agenda : lstagenda){
				String sangria;
				switch (agenda.objetoTipo){
					case 1: sangria = ""; break;
					case 2: sangria = "\t"; break;
					case 3: sangria = "\t\t"; break;
					case 4: sangria = "\t\t\t"; break;
					case 5: sangria = "\t\t\t\t"; break;
					default: sangria = "";
				}
				datos.put(fila+"", new Object [] { agenda.edt, sangria + agenda.nombre ,agenda.fechaInicio,agenda.fechaFin,agenda.estado});
				fila++;
			}
			
			String path = excel.ExportarExcel(datos, "Agenda de Actividades", usuario);

			File file=new File(path);
			if(file.exists()){
		        FileInputStream is = null;
		        try {
		        	is = new FileInputStream(file);
		        }
		        catch (Exception e) {
					CLogger.write_simple("4", CExcel.class, e.getMessage());
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
				response.setHeader("Cache-Control", "no-cache"); 
				response.setHeader("Content-Disposition", "attachment; Agenda.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
			}
		}
	}
	
	private List<stagenda> obtenerListado(int proyectoId, String usuario){
		Proyecto proyecto = ProyectoDAO.getProyectoPorId(proyectoId, usuario);
		List<stagenda> lstagenda = new ArrayList<>();
		if (proyecto!=null){
			stagenda agenda = new stagenda();
			agenda.objetoTipo = OBJETO_ID_PROYECTO;
			agenda.objetoTipoNombre = "Actividad";
			agenda.id = proyecto.getActividad();
			agenda.edt = "1.";
			agenda.nombre = proyecto.getNombre();
			agenda.fechaInicio = "";
			agenda.fechaFin = "";
			lstagenda.add(agenda);
		}
		List<Componente> componentes = ComponenteDAO.getComponentesPaginaPorProyecto(0, 0, proyectoId,
				null, null, null, null, null, usuario);
		int edtComponente = 1;
		for (Componente componente : componentes){
			stagenda agenda = new stagenda();
			agenda.objetoTipo = OBJETO_ID_COMPONENTE;
			agenda.objetoTipoNombre = "Componente";
			agenda.id = componente.getId();
			agenda.edt = "1."+edtComponente;
			agenda.nombre = componente.getNombre();
			agenda.fechaInicio = "";
			agenda.fechaFin = "";
			lstagenda.add(agenda);
			
			List<Producto> productos = ProductoDAO.getProductosPagina(0, 0, componente.getId(),
					null, null, null, null, null, usuario);
			int edtProducto = 1;
			for (Producto producto : productos){
				List<Subproducto> subproductos = SubproductoDAO.getSubproductosPagina(0, 0, producto.getId(),
						null, null, null, null, null, usuario);
				agenda = new stagenda();
				agenda.objetoTipo = OBJETO_ID_PRODUCTO;
				agenda.objetoTipoNombre = "Producto";
				agenda.nombre = producto.getNombre();
				agenda.id = producto.getId();
				agenda.edt = "1."+edtComponente+"."+edtProducto;
				agenda.fechaInicio = "";
				agenda.fechaFin = "";
				lstagenda.add(agenda);
				
				int edtSubproducto = 1;
				for (Subproducto subproducto : subproductos){
					List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, subproducto.getId(), OBJETO_ID_SUBPRODUCTO,
							null, null, null, null, null, usuario);
					agenda = new stagenda();
					agenda.objetoTipo = OBJETO_ID_SUBPRODUCTO;
					agenda.objetoTipoNombre = "Subroducto";
					agenda.id = subproducto.getId();
					agenda.edt = "1."+edtComponente+"."+edtProducto+"."+edtSubproducto;
					agenda.nombre =   subproducto.getNombre();
					agenda.fechaInicio = "";
					agenda.fechaFin = "";
					
					lstagenda.add(agenda);
					edtActividad = 1;
					strEdtActividad = agenda.edt;
					for (Actividad actividad : actividades ){
						lstagenda = ObtenerActividades(actividad,usuario,lstagenda, OBJETO_ID_ACTIVIDAD);
					}
					edtSubproducto++;
				}
				List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, producto.getId(), OBJETO_ID_PRODUCTO,
						null, null, null, null, null, usuario);
				edtActividad = edtSubproducto;
				strEdtActividad = "1."+edtComponente+"."+edtProducto;
				for (Actividad actividad : actividades ){
					lstagenda = ObtenerActividades(actividad,usuario,lstagenda,OBJETO_ID_ACTIVIDAD);
				}
				edtProducto++;
			}
			List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, componente.getId(), OBJETO_ID_COMPONENTE,
					null, null, null, null, null, usuario);
			edtActividad = edtProducto;
			strEdtActividad = "1."+edtComponente;
			for (Actividad actividad : actividades ){
				lstagenda = ObtenerActividades(actividad,usuario,lstagenda,OBJETO_ID_ACTIVIDAD);
			}
			edtComponente++;
		}
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, proyectoId, OBJETO_ID_PROYECTO,
				null, null, null, null, null, usuario);
		edtActividad = edtComponente;
		strEdtActividad = "1";
		for (Actividad actividad : actividades ){
			lstagenda = ObtenerActividades(actividad,usuario,lstagenda,OBJETO_ID_ACTIVIDAD);
		}
		
		return lstagenda;
	}
	
	private List<stagenda> ObtenerActividades(Actividad actividad, String usuario, List<stagenda> lstagenda, int tipo_Objeto){
		List<Actividad> actividades = ActividadDAO.getActividadsPaginaPorObjeto(0, 0, actividad.getId(), OBJETO_ID_ACTIVIDAD, 
				null, null,null, null, null, usuario);
		
		stagenda agenda = new stagenda();
		agenda = new stagenda();
		agenda.objetoTipo = tipo_Objeto;
		agenda.objetoTipoNombre = "Actividad";
		agenda.id = actividad.getId();
		agenda.edt = strEdtActividad +"."+edtActividad;
		agenda.nombre =   actividad.getNombre();
		agenda.fechaInicio = Utils.formatDate(actividad.getFechaInicio());
		agenda.fechaFin = Utils.formatDate(actividad.getFechaFin());
		agenda.estado = actividad.getPorcentajeAvance() == 0 ? "Nuevo" :
						(actividad.getPorcentajeAvance() > 0 && actividad.getPorcentajeAvance() <100 ? 
								"Proceso" : "Finalizado") ;
		lstagenda.add(agenda);
		edtActividad++;
		
		for(Actividad subActividad : actividades){
			lstagenda = ObtenerActividades(subActividad, usuario, lstagenda, tipo_Objeto + 1);
		}
		
		return lstagenda;
	}

}
