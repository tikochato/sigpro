package servlets;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import dao.ActividadDAO;
import dao.AsignacionRaciDAO;
import dao.ComponenteDAO;
import dao.InformacionPresupuestariaDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.Colaborador;
import pojo.Componente;
import pojo.Producto;
import pojo.Proyecto;
import pojo.Subproducto;
import utilities.CMariaDB;
import utilities.Utils;


@WebServlet("/SCargaTrabajo")
public class SCargaTrabajo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	class stcargatrabajo{
		int id;
		String responsable;
		int actividadesAtrasadas;
		int actividadesAlerta;
		int actividadesACumplir;
		int actividadesCompletadas;
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
				String idPrestamos = map.get("idPrestamos") != null && map.get("idPrestamos").length() > 0? map.get("idPrestamos") : "0";
				String idComponentes = map.get("idComponentes") != null && map.get("idComponentes").length() > 0 ? map.get("idComponentes") : "0";
				String idProductos = map.get("idProductos") != null && map.get("idProductos").length() > 0 ? map.get("idProductos") : "0";
				String idSubproductos = map.get("idSubproductos") != null && map.get("idSubproductos").length() > 0 ? map.get("idSubproductos") : "0";
				
				
				List<Actividad> actividades = ActividadDAO.getActividadsPorObjetos(idPrestamos, idComponentes, idProductos, idSubproductos);
				
				List<stcargatrabajo> cargas = new ArrayList<stcargatrabajo>();
				
				for(Actividad actividad : actividades){
					
					
					
						
						Colaborador colaborador = AsignacionRaciDAO.getResponsablePorRol(actividad.getId(), 5, "r");
						
						
						Date hoy = new Date();
						Date siguienteSemana = sumarDiasFecha(hoy, 7);
						Date siguienteMes = sumarDiasFecha(siguienteSemana, 23);

						if (actividad.getPorcentajeAvance() != 100){
							if (actividad.getFechaFin().before(hoy)){
								agregarCarga(cargas, colaborador, 0, 1, 0, 0);
							}else {
								if (actividad.getFechaFin().after(hoy) && actividad.getFechaFin().before(siguienteSemana)){
									agregarCarga(cargas, colaborador, 0, 0, 0, 1);
								}else if (actividad.getFechaFin().after(siguienteSemana) && actividad.getFechaFin().before(siguienteMes)){
									agregarCarga(cargas, colaborador, 0, 0, 1, 0);
								}
							}
							
						}else{
							agregarCarga(cargas, colaborador, 1, 0, 0, 0);
						}
				}
				
				response_text=new GsonBuilder().serializeNulls().create().toJson(cargas);
		        response_text = String.join("", "\"cargatrabajo\":",response_text);
		        response_text = String.join("", "{\"success\":true,", response_text,"}");
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
				"\"objetoTipo\" :",objetoTipo.toString(),
				(hijos!=null && hijos.length()>0 ? ",\"children\": [" + hijos + "]": ""),
				"}");
	}
	
	
	private void agregarCarga(List<stcargatrabajo> cargas, Colaborador colaborador, 
			Integer completadas,Integer retrasadas, Integer aCumplir, Integer alerta){
		boolean existe = false;
		for (stcargatrabajo carga : cargas){
			if (colaborador.getId() == carga.id){
				carga.actividadesAtrasadas = carga.actividadesAtrasadas + retrasadas;
				carga.actividadesCompletadas = carga.actividadesCompletadas + completadas;
				carga.actividadesAlerta = carga.actividadesAlerta + alerta;
				carga.actividadesACumplir = carga.actividadesACumplir + aCumplir;
				existe = true;
				break;
			}
		}
		if (!existe){
			stcargatrabajo carga = new stcargatrabajo();
			carga.id = colaborador.getId();
			carga.responsable = colaborador.getPnombre() + " " + colaborador.getPapellido();
			carga.actividadesAtrasadas = retrasadas;
			carga.actividadesCompletadas = completadas;
			carga.actividadesAlerta = alerta;
			carga.actividadesACumplir = aCumplir;
			cargas.add(carga);
		}
	}
	
	public Date sumarDiasFecha(Date fecha, int dias){
		Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fecha); 
		      calendar.add(Calendar.DAY_OF_YEAR, dias);  
		      return calendar.getTime(); 
    }
	
	public Date sumarDiasMes(Date fecha, int dias){
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(fecha); 
        cal.add(Calendar.MONTH, 1);
        return  cal.getTime();
    }
	
	
	
	
	

}
