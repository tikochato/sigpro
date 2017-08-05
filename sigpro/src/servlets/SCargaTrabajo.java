package servlets;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dao.ActividadDAO;
import dao.AsignacionRaciDAO;
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.ReporteDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.AsignacionRaci;
import pojo.Colaborador;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import servlets.SReporte.cargaTrabajo;
import utilities.CMariaDB;
import utilities.Utils;


@WebServlet("/SCargaTrabajo")
public class SCargaTrabajo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class cargaTrabajo{
		int id;
		String responsable;
		int actividadesAtrasadas;
		int actividadesProceso;
	}
	
    public SCargaTrabajo() {
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
		
		if(accion.equals("getEstructrua")){
			Integer idPrestamo = Utils.String2Int(map.get("idPrestamo"),0);
			
			Proyecto proyecto = ProyectoDAO.getProyectoPorId(idPrestamo, usuario);
			if(proyecto != null){
				if(CMariaDB.connect()){
						Connection conn = CMariaDB.getConnection();
						ArrayList<Integer> componentes = InformacionPresupuestariaDAO.getEstructuraArbolComponentes(idPrestamo, conn);
						
						String items_componente="";
						for(Integer componente:componentes){
							
							Componente objComponente = ComponenteDAO.getComponentePorId(componente, usuario);
							ArrayList<Integer> productos = InformacionPresupuestariaDAO.getEstructuraArbolProducto(idPrestamo, objComponente.getId(), conn);
							String items_producto = "";
							for(Integer producto: productos){
								Producto objProducto = ProductoDAO.getProductoPorId(producto);
								
								ArrayList<Integer> subproductos = InformacionPresupuestariaDAO.getEstructuraArbolSubProducto(idPrestamo,objComponente.getId(),objProducto.getId(), conn);
								String items_subproducto = "";
								for(Integer subproducto: subproductos){
									Subproducto objSubProducto = SubproductoDAO.getSubproductoPorId(subproducto);
									items_subproducto = String.join(items_subproducto.length()>0 ? ",":"", 
											items_subproducto,
											construirItem(objSubProducto.getNombre(), objSubProducto.getId(), 
													4, "sp_"+objSubProducto.getId(),null));
								}
								
								items_producto = String.join(items_producto.length()>0 ? ",":"", 
										 items_producto,
										construirItem(objProducto.getNombre(), objProducto.getId(), 
												3, "pr_"+objProducto.getId(),items_subproducto));
								
								items_subproducto = "";
							}
							items_componente = String.join(items_componente.length()>0 ? ",":"", 
									 items_componente,
									construirItem(objComponente.getNombre(), objComponente.getId(), 
											2, "c_"+objComponente.getId(),items_producto));
							items_producto = "";	
						}
						
						response_text = String.join("", construirItem(proyecto.getNombre(), proyecto.getId(),
								1, "p_"+proyecto.getId(), items_componente));
						
						
						
						CMariaDB.close();
						 response_text = String.join("", "\"estructura\":",response_text);
				        response_text = String.join("", "{\"success\":true,", response_text, "}");
					}else{
						response_text = String.join("", "{\"success\":false}");
					}
				}
			}else if(accion.equals("getCargaTrabajoPrestamo")){
				Integer idProyecto = Utils.String2Int(map.get("idPrestamo"),0);
				Integer idComponente = Utils.String2Int(map.get("idComponente"),0);
				Integer idProducto = Utils.String2Int(map.get("idProducto"),0);
				Integer idSubProducto = Utils.String2Int(map.get("idSubProducto"),0);
				
				List<?> actividades = ReporteDAO.getActividadesCargaTrabajo(idProyecto, idComponente, idProducto, idSubProducto);
				List<cargaTrabajo> atrasadas = new ArrayList<cargaTrabajo>();
				List<cargaTrabajo> proceso = new ArrayList<cargaTrabajo>();
				
				for(Object obj : actividades){
					cargaTrabajo temp1 = new cargaTrabajo();
					cargaTrabajo temp2 = new cargaTrabajo();
					
					Object[] row = (Object[]) obj;
					
					int idActividad = (Integer)row[0];
					int porcentajeAvance = (Integer)row[2];
					
					if (porcentajeAvance > 0 && porcentajeAvance < 100){
						Actividad actividad = ActividadDAO.getActividadPorId(idActividad, usuario);
						String[] fechaInicioFin = ActividadDAO.getFechaInicioFin(actividad,usuario).split(";");
						
						try{
							Date inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
				                    .parse(fechaInicioFin[0]);
				            Date fin = new SimpleDateFormat("dd/MM/yyyy", Locale.US)
				                    .parse(fechaInicioFin[1]);
				            Date hoy = new DateTime().toDate();
							
							if(hoy.after(inicio) && hoy.before(fin)){
								temp1.id = (Integer)row[3];
								temp1.responsable = (String)row[4];
								temp1.actividadesAtrasadas = 0;
								temp1.actividadesProceso = 1;
								proceso.add(temp1);
							} else if(hoy.after(fin)){
								temp2.id = (Integer)row[3];
								temp2.responsable = (String)row[4];
								temp2.actividadesAtrasadas = 1;
								temp2.actividadesProceso = 0;
								atrasadas.add(temp2);
							}
						}
						catch (Throwable e) {
				            e.printStackTrace();
				        }
					}
				}
				
				String JsonProceso = Utils.getJSonString("actividadesProceso", proceso);
				String JsonAtrasadas = Utils.getJSonString("actividadesAtrasadas", atrasadas);
				
				response_text = JsonProceso + "," + JsonAtrasadas;
				response_text = String.join("", "{\"success\":true,", response_text, "}");
			}
		
		
		else
					response_text = String.join("", "{\"success\":false}");
		
		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");
		
	    OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
	    gz.write(response_text.getBytes("UTF-8"));
	    gz.close();
	    output.close();
	
		}
	
	private String construirItem(String nombre, Integer objetoId, Integer objetoTipo, String value,String hijos){
		return String.join("", "{\"label\" :\"",nombre,"\",",
				"\"value\" :\"",value,"\",",
				"\"objetoId\" :",objetoId.toString(),",",
				"\"objetoTipol\" :",objetoTipo.toString(),
				(hijos!=null && hijos.length()>0 ? ",\"children\": [" + hijos + "]": ""),
				"}");
	}
	
	

}
