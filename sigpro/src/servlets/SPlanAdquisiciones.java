package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
import dao.CategoriaAdquisicionDAO;
import dao.EstructuraProyectoDAO;
import dao.PlanAdquisicionDAO;
import dao.PlanAdquisicionPagoDAO;
import dao.ProductoDAO;
import dao.SubproductoDAO;
import pojo.Actividad;
import pojo.CategoriaAdquisicion;
import pojo.PlanAdquisicion;
import pojo.PlanAdquisicionPago;
import pojo.Producto;
import pojo.Subproducto;
import utilities.CLogger;
import utilities.Utils;

@WebServlet("/SPlanAdquisiciones")
public class SPlanAdquisiciones extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	class stpresupuestoPlan{
		BigDecimal planificado;
		BigDecimal real;
	}
	
	class stplan{
		stpresupuestoPlan[] mes = new stpresupuestoPlan[12];
		Integer anio;
	}
	
	class stcomponenteplanadquisicion{
		Integer objetoId;
		String nombre;
		Integer nivel;
		Integer objetoTipo;
		Integer planadquisicionId;
		Integer acumulacionCosto;
		Integer objetoTipoPadre;
		Integer objetoIdPadre;
		DateTime fechaIncial;
		DateTime fechaFinal;
		BigDecimal costo;
		stplan[] anioPlan;
	}
	
	class stcategoria{
		Integer categoriaId;
		String nombre;
		List<List<Integer>> objetos = new ArrayList<List<Integer>>();
	}
		
    public SPlanAdquisiciones() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
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
			Integer anio = Utils.String2Int(map.get("anio"),0);
			
			if(accion.equals("generarPlan")){
				try{
					List<stcomponenteplanadquisicion> lstprestamo = generarPlan(idPrestamo, usuario, anio);
										
					response_text=new GsonBuilder().serializeNulls().create().toJson(lstprestamo);
			        response_text = String.join("", "\"proyecto\":",response_text);
			        response_text = String.join("", "{\"success\":true,", response_text, "}");
				}
				catch (Exception e){
					CLogger.write("2", SControlAdquisiciones.class, e);
				}
			}
			
			response.setHeader("Content-Encoding", "gzip");
			response.setCharacterEncoding("UTF-8");

	        OutputStream output = response.getOutputStream();
			GZIPOutputStream gz = new GZIPOutputStream(output);
	        gz.write(response_text.getBytes("UTF-8"));
	        gz.close();
	        output.close();

		}catch(Exception e){
			CLogger.write("1", SPlanAdquisiciones.class, e);
		}
	}
	
	private List<stcomponenteplanadquisicion> generarPlan(Integer idPrestamo, String usuario, Integer anio) throws Exception{
		try{
			List<stcomponenteplanadquisicion> lstPrestamo = new ArrayList<>();
			List<?> estruturaProyecto = EstructuraProyectoDAO.getEstructuraProyecto(idPrestamo);
			stcomponenteplanadquisicion temp = null;
			
			List<CategoriaAdquisicion> lstCategorias = CategoriaAdquisicionDAO.getCategoriaAdquisicion(); 
			List<stcategoria> lsttempCategorias = new ArrayList<stcategoria>();
			stcategoria tempCategoria = null;
			for(CategoriaAdquisicion categoria : lstCategorias){
				tempCategoria = new stcategoria();
				tempCategoria.categoriaId = categoria.getId();
				tempCategoria.nombre = categoria.getNombre();
				lsttempCategorias.add(tempCategoria);
			}
			
			for(Object objeto: estruturaProyecto){
				temp = new stcomponenteplanadquisicion();
				Object[] obj = (Object[])objeto;
				BigInteger objetoTipo = (BigInteger)obj[2];
				Integer tipo = objetoTipo.intValue();
				if(tipo == 2){
					Integer componenteid = (Integer)obj[0]; 
					temp.objetoId = (Integer)obj[0];
					temp.nombre = (String)obj[1];
					temp.nivel = 0;
					temp.objetoTipo = tipo;
					lstPrestamo.add(temp);
					
					List<List<Integer>> hijos = EstructuraProyectoDAO.getHijosCompleto((String)obj[3], estruturaProyecto);
					
					for(List<Integer> objetoHijo : hijos){
						List<PlanAdquisicion> lstplan = PlanAdquisicionDAO.getPlanAdquisicionByObjeto(objetoHijo.get(1), objetoHijo.get(0));
						
						if(lstplan != null && !lstplan.isEmpty()){
							for(PlanAdquisicion plan : lstplan){
								for(stcategoria cat : lsttempCategorias){
									if(cat.categoriaId == plan.getCategoriaAdquisicion().getId()){
										List<Integer> objH = new ArrayList<Integer>();
										objH.add(objetoHijo.get(0));
										objH.add(objetoHijo.get(1));
										objH.add(plan.getId());
										cat.objetos.add(objH);
									}
								}
							}
						}
					}
					
					for(stcategoria cat : lsttempCategorias){
						if(!cat.objetos.isEmpty()){
							temp = new stcomponenteplanadquisicion();
							temp.nombre = cat.nombre;
							temp.nivel = 1;
							lstPrestamo.add(temp);
							
							for(List<Integer> objCat : cat.objetos){
								temp = new stcomponenteplanadquisicion();
								String nombre = null;
								Date fechaInicio = new Date();
								Date fechaFin = new Date();
								Integer acumulacionCosto = 0;
								BigDecimal costo = new BigDecimal(0);
								
								switch(objCat.get(1)){
								case 3:
									Producto producto = ProductoDAO.getProductoPorId(objCat.get(0));
									costo = producto.getCosto();
									nombre = producto.getNombre();
									fechaInicio = producto.getFechaInicio();
									fechaFin = producto.getFechaFin();
									acumulacionCosto = producto.getAcumulacionCosto() != null ? producto.getAcumulacionCosto().getId() : 3;
									break;
								case 4:
									Subproducto subproducto = SubproductoDAO.getSubproductoPorId(objCat.get(0));
									costo = subproducto.getCosto();
									nombre = subproducto.getNombre();
									fechaInicio = subproducto.getFechaInicio();
									fechaFin = subproducto.getFechaFin();
									acumulacionCosto = subproducto.getAcumulacionCosto() != null ? subproducto.getAcumulacionCosto().getId() : 3;
									break;
								case 5:
									Actividad actividad = ActividadDAO.getActividadPorId(objCat.get(0));
									costo = actividad.getCosto();
									nombre = actividad.getNombre();
									fechaInicio = actividad.getFechaInicio();
									fechaFin = actividad.getFechaFin();
									acumulacionCosto = actividad.getAcumulacionCosto() != null ? actividad.getAcumulacionCosto().getId() : 3;
									break;
								}
								
								temp.nombre = nombre;
								temp.objetoId = objCat.get(0);
								temp.objetoTipo = objCat.get(1);
								temp.nivel = 2;
								temp.anioPlan = inicializarStplan(anio);
								temp.costo = costo;
								temp.acumulacionCosto = acumulacionCosto;
								temp.fechaIncial = new DateTime(fechaInicio);
								temp.fechaFinal = new DateTime(fechaFin);
								temp.planadquisicionId = objCat.get(2);
								temp.objetoIdPadre = componenteid;
								temp.objetoTipoPadre = 2;
								
								temp = getCostoPagos(temp);
								
								temp.fechaIncial = null;
								temp.fechaFinal = null;
								lstPrestamo.add(temp);
							}
						}
					}
					
					inicializarObjCategoria(lsttempCategorias);
				}
			}
			return lstPrestamo;
		}catch(Exception e){
			CLogger.write("1", SControlAdquisiciones.class, e);
			return null;
		}
	}
	
	private stcomponenteplanadquisicion getCostoPagos(stcomponenteplanadquisicion stcomponente){
		PlanAdquisicion plan = PlanAdquisicionDAO.getPlanAdquisicionById(stcomponente.planadquisicionId);
		Calendar fechaInicial = Calendar.getInstance();
		for(stplan anioObj: stcomponente.anioPlan){
 			//for(PlanAdquisicion plan : lstplan){
				List<PlanAdquisicionPago> pagos = PlanAdquisicionPagoDAO.getPagosByPlan(plan.getId());
				if(pagos != null && !pagos.isEmpty()){
					for(PlanAdquisicionPago pago : pagos){
						fechaInicial.setTime(pago.getFechaPago());
						int mes = fechaInicial.get(Calendar.MONTH);
						int anio = fechaInicial.get(Calendar.YEAR);					
						if(anio == anioObj.anio){
							anioObj.mes[mes].planificado = anioObj.mes[mes].planificado.add(pago.getPago());
						}
					}
				}else{
					int diaInicial = stcomponente.fechaIncial.getDayOfMonth();
					int mesInicial = stcomponente.fechaIncial.getMonthOfYear() -1;
					int anioInicial = stcomponente.fechaIncial.getYear();
					int diaFinal = stcomponente.fechaFinal.getDayOfMonth();
					int mesFinal = stcomponente.fechaFinal.getMonthOfYear() -1;
					int anioFinal = stcomponente.fechaFinal.getYear();
					
					if(anioObj.anio >= anioInicial && anioObj.anio<=anioFinal){
						if(stcomponente.acumulacionCosto != null){
							if(stcomponente.acumulacionCosto == 1){						
								if(anioInicial == anioObj.anio){
									anioObj.mes[mesInicial].planificado =  stcomponente.costo != null ? stcomponente.costo : new BigDecimal(0);
								}
							}else if(stcomponente.acumulacionCosto == 2){
								int dias = (int)((stcomponente.fechaFinal.getMillis() - stcomponente.fechaIncial.getMillis())/(1000*60*60*24));
								BigDecimal costoDiario = stcomponente.costo != null ? stcomponente.costo.divide(new BigDecimal(dias),5, BigDecimal.ROUND_HALF_UP) : new BigDecimal(0);
								int inicioActual = 0;
								if(anioObj.anio == anioInicial){
									inicioActual = mesInicial;
								}
								
								int finMes = anioObj.anio==anioFinal ? mesFinal : 11;
								for(int m=inicioActual; m<=finMes; m++){
									if(anioObj.anio == anioInicial && m==mesInicial){
										if(m==mesFinal){
											int diasMes = diaFinal-diaInicial;
											anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
										}else{
											Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
											int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
											diasMes = diasMes-diaInicial;
											anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
										}
									}else if(anioObj.anio == anioFinal && m== mesFinal){
										anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diaFinal));
									}else{
										Calendar cal = new GregorianCalendar(anioObj.anio, m, 1); 
										int diasMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
										anioObj.mes[m].planificado = costoDiario.multiply(new BigDecimal(diasMes));
									}
								}
							}else if(stcomponente.acumulacionCosto ==3){
								if(anioFinal == anioObj.anio){
									anioObj.mes[mesFinal].planificado =  stcomponente.costo != null ? stcomponente.costo : new BigDecimal(0);
								}
							}
						}
					}
				}
			//}
		}
		return stcomponente;
	}
	
	private void inicializarObjCategoria(List<stcategoria> lsttempCategorias){
		for(stcategoria cat : lsttempCategorias){
			cat.objetos = new ArrayList<List<Integer>>();
		}
	}
	
	private stplan[] inicializarStplan(Integer anio){		
		int longitudArrelgo = 1;
		
		stplan[] anios = new stplan[longitudArrelgo];
		
		for (int i = 0;i <longitudArrelgo; i++){
			stplan temp = new stplan();
			for(int m=0; m<12; m++){
				temp.mes[m]= new stpresupuestoPlan();
				temp.mes[m].planificado = new BigDecimal(0);
				temp.mes[m].real =  new BigDecimal(0);
			}
			temp.anio = anio+i;
			anios[i] = temp;
		}
		return anios;
	}
}
