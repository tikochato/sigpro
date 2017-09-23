package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.DataSigadeDAO;
import dao.DesembolsoDAO;
import dao.MetaTipoDAO;
import dao.PrestamoDAO;
import dao.ProductoDAO;
import dao.ProyectoDAO;
import pojo.Prestamo;
import pojo.Producto;
import pojo.Proyecto;
import utilities.Utils;

@WebServlet("/SPlanEjecucion")
public class SPlanEjecucion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public SPlanEjecucion() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("{ \"success\": false }").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
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
		Date fecha_actual = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
		if(accion.equals("getDatosPlan")){
			Integer proyectoId = Utils.String2Int(map.get("proyectoId"),0);
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			Prestamo prestamo = PrestamoDAO.getPrestamoPorObjetoYTipo(proyectoId, 1);
			
			BigDecimal ejecucionFisicaReal = calcularEjecucionFisica(proyectoId);
			
			BigDecimal ejecucionFinancieraPlanificada = calcularEjecucionFinanciaeraPlanificada(proyectoId, prestamo.getCodigoPresupuestario()+"", new Date());
			Double plazoEjecucionPlanificada = calcularPlazoEjecucionPlanificada(proyecto);
			BigDecimal ejecucionFisicaPlanificada = calcularEjecucionFisicaPlanificada(proyectoId);
			
			DecimalFormat df = new DecimalFormat();
		    df.setMinimumFractionDigits(2);
		    df.setMaximumFractionDigits(2);
		    
			response_text = String.join("", "{ \"success\": true ,",
					"\"ejecucionFisicaR\": \"" , df.format((ejecucionFisicaReal.floatValue() * 100.00)) + "" , "\",",
					"\"ejecucionFinancieraP\": \"" , df.format((ejecucionFinancieraPlanificada.floatValue() * 100.00)) + "" , "\",",
					"\"plazoEjecucionP\": \"" , df.format((plazoEjecucionPlanificada.floatValue() * 100.00)) + "" , "\",",
					"\"ejecucionFisicaP\": \"" , df.format((ejecucionFisicaPlanificada.floatValue() * 100.00)) + "" , "\",",
					
					"\"fecha\": \"" , sdf.format(fecha_actual), "\"",
					"}");
			
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
	
	public BigDecimal calcularEjecucionFisica(Integer proyectoId){
		
		List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyectoId, null);
		BigDecimal ejecucionFisica = new BigDecimal("0");
		
		for(Producto producto : productos){
			BigDecimal mvReal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 1);
			BigDecimal mvFinal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 4);
			if (mvReal!=null && mvFinal!=null){
				ejecucionFisica = ejecucionFisica.add(mvReal.divide(mvFinal, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(
						(producto.getPeso() !=null ? (double) producto.getPeso() : (Double) (100.0 / productos.size())) / 100)));
			}
			
		}
		return ejecucionFisica;
	}
	
	public BigDecimal calcularEjecucionFinanciaeraPlanificada(int proyectoId, String codigoPresupuestario,Date fecha){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anio = Integer.parseInt(sdf.format(fecha));
		sdf = new SimpleDateFormat("MM");
		int mes = Integer.parseInt(sdf.format(fecha));
		BigDecimal totalDesembolsosReales = 
				DataSigadeDAO.totalDesembolsadoAFechaReal(codigoPresupuestario, anio, mes);
		BigDecimal totalDesembolsosPlanificados = 
				DesembolsoDAO.getTotalDesembolsosFuturos(proyectoId,   fecha);
		BigDecimal  ejecucionFinanciera = new BigDecimal("0");
		if (totalDesembolsosReales!= null && totalDesembolsosPlanificados != null){
			BigDecimal total = totalDesembolsosReales.add( totalDesembolsosPlanificados);
			ejecucionFinanciera = totalDesembolsosReales.divide(total,2,BigDecimal.ROUND_HALF_UP);
		}
		return ejecucionFinanciera;
	}
	
	public Double calcularPlazoEjecucionPlanificada(Proyecto proyecto){	
		if (proyecto!=null && proyecto.getFechaInicio()!=null && proyecto.getFechaFin()!=null ){
			
			Long hoy = new Date().getTime();
			Long inicio = proyecto.getFechaInicio().getTime();			
			Long fin = proyecto.getFechaFin().getTime();
			
			Long total = fin - inicio;
			Long transcurrido = fin - hoy;
			
			return transcurrido*1.0/total;
			
		}else
			return 0.0;
		
	}
	
public BigDecimal calcularEjecucionFisicaPlanificada(Integer proyectoId){
		
		List<Producto> productos = ProductoDAO.getProductosPorProyecto(proyectoId, null);
		BigDecimal ejecucionFisica = new BigDecimal("0");
		
		for(Producto producto : productos){
			BigDecimal mvPlanificada = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 2);
			BigDecimal mvFinal = MetaTipoDAO.getMetaValorPorIdObjetoTipoMeta(producto.getId(), 3, 4);
			if (mvPlanificada!=null && mvFinal!=null){
				ejecucionFisica = ejecucionFisica.add(mvPlanificada.divide(mvFinal, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(
						(producto.getPeso() !=null ? (double) producto.getPeso() : (Double) (100.0 / productos.size())) / 100)));
			}
			
		}
		return ejecucionFisica;
	}
}
