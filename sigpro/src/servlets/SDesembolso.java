package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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

import dao.DataSigadeDAO;
import dao.DesembolsoDAO;
import dao.DesembolsoTipoDAO;
import dao.ProyectoDAO;
import dao.TipoMonedaDAO;
import pojo.Desembolso;
import pojo.Proyecto;
import pojoSigade.DtmAvanceFisfinanDetDti;
import utilities.Utils;


@WebServlet("/SDesembolso")
public class SDesembolso extends HttpServlet {
	private static final long serialVersionUID = 1L;
	class stdesembolso{
		Integer id;
		String fecha;
		BigDecimal monto;
		BigDecimal tipocambio;
		Integer proyectoid;
		String proyecto;
		Integer desembolsotipoid;
		Integer tipo_moneda;
		String tipo_moneda_nombre;
		String tipomonedasimbolo;
		String desembolsotipo;
		String usuarioCreo;
		String usuarioActualizo;
		String fechaCreacion;
		String fechaActualizacion;
		int estado;
	}
	
	class stdesembolsoreal {
		Long ejercicioFiscal;
		String mesDesembolso;
		String codigoPresupuestario;
		Integer entidadId;
		Integer unidadEjecutoraId;
		String monedaDesembolso;
		BigDecimal desembolsosMesUsd;
		BigDecimal desembolsosMesGtq;
	
	}
    
    public SDesembolso() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String response_text = "{ \"success\": false }";

		response.setHeader("Content-Encoding", "gzip");
		response.setCharacterEncoding("UTF-8");

        OutputStream output = response.getOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(output);
        gz.write(response_text.getBytes("UTF-8"));
        gz.close();
        output.close();
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
		String accion = map.get("accion");
		String response_text="";
		
		if(accion.equals("getDesembolsosPagina")){
			int pagina = map.get("pagina")!=null  ? Integer.parseInt(map.get("pagina")) : 0;
			int numeroDesembolsos = map.get("numerodesembolsos")!=null  ? Integer.parseInt(map.get("numerodesembolsos")) : 0;
			List<Desembolso> desembolsos = DesembolsoDAO.getDesembolsosPagina(pagina, numeroDesembolsos);
			List<stdesembolso> stdesembolsos=new ArrayList<stdesembolso>();
			for(Desembolso desembolso:desembolsos){
				stdesembolso temp =new stdesembolso();
				temp.id = desembolso.getId();
				temp.fecha = Utils.formatDate(desembolso.getFecha());
				temp.monto = desembolso.getMonto();
				temp.tipocambio = desembolso.getTipoCambio();
				temp.estado = desembolso.getEstado();
				temp.desembolsotipoid = desembolso.getDesembolsoTipo().getId();
				temp.desembolsotipo = desembolso.getDesembolsoTipo().getNombre();
				temp.proyecto = desembolso.getProyecto().getNombre();
				temp.tipo_moneda=desembolso.getTipoMoneda().getId();
				temp.tipo_moneda_nombre=desembolso.getTipoMoneda().getNombre();
				temp.tipomonedasimbolo=desembolso.getTipoMoneda().getSimbolo();
				temp.proyectoid = desembolso.getProyecto().getId();
				temp.fechaActualizacion = Utils.formatDateHour(desembolso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(desembolso.getFechaCreacion());
				temp.usuarioActualizo = desembolso.getUsuarioActualizo();
				temp.usuarioCreo = desembolso.getUsuarioCreo();
				stdesembolsos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("getDesembolsoss")){
			List<Desembolso> desembolsos = DesembolsoDAO.getDesembolsos();
			List<stdesembolso> stdesembolsos=new ArrayList<stdesembolso>();
			for(Desembolso desembolso:desembolsos){
				stdesembolso temp =new stdesembolso();
				temp.id = desembolso.getId();
				temp.fecha = Utils.formatDate(desembolso.getFecha());
				temp.monto = desembolso.getMonto();
				temp.tipocambio = desembolso.getTipoCambio();
				temp.estado = desembolso.getEstado();
				temp.desembolsotipoid = desembolso.getDesembolsoTipo().getId();
				temp.desembolsotipo = desembolso.getDesembolsoTipo().getNombre();
				temp.proyecto = desembolso.getProyecto().getNombre();
				temp.proyectoid = desembolso.getProyecto().getId();
				temp.tipo_moneda=desembolso.getTipoMoneda().getId();
				temp.tipo_moneda_nombre=desembolso.getTipoMoneda().getNombre();
				temp.tipomonedasimbolo=desembolso.getTipoMoneda().getSimbolo();
				temp.fechaActualizacion = Utils.formatDateHour(desembolso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(desembolso.getFechaCreacion());
				temp.usuarioActualizo = desembolso.getUsuarioActualizo();
				temp.usuarioCreo = desembolso.getUsuarioCreo();
				stdesembolsos.add(temp);
			}
			
			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
		}
		else if(accion.equals("guardarDesembolsos")){
			
				boolean result = true;
				Integer proyectoid = Utils.String2Int(map.get("proyectoid"));
				
				String desembolsos = map.get("desembolsos");
				String[] desembolsos_array = new String[]{};
				if(desembolsos!=null && desembolsos.length()>0){
					desembolsos_array = desembolsos.split("},");
				}
				Integer id=null;
				BigDecimal monto=null;
				Timestamp tfecha=null;
				Integer tipo_moneda=null;
				List<Desembolso> ui_desembolsos = new ArrayList<Desembolso>();
				for(int i=0; i<desembolsos_array.length; i++){
					if(!desembolsos_array[i].substring(desembolsos_array[i].length()-1).equals("}"))
						desembolsos_array[i] = String.join("", desembolsos_array[i],"}");
					map = gson.fromJson(desembolsos_array[i], type);
					id = Utils.String2Int(map.get("id"));
					monto = Utils.String2BigDecimal(map.get("monto"), null);
					tfecha = Utils.stringToTimestamp(map.get("fecha"));
					tipo_moneda = Utils.String2Int(map.get("tipo_moneda"), null);
					Desembolso desembolso =null;
					if(id>0){
						desembolso = DesembolsoDAO.getDesembolsoPorId(id);
						desembolso.setMonto(monto);
						desembolso.setFecha(tfecha);
						desembolso.setTipoMoneda(TipoMonedaDAO.getTipoMonedaPorId(tipo_moneda));
					}
					else
						desembolso = new Desembolso(DesembolsoTipoDAO.getDesembolosTipoPorId(1), 
								ProyectoDAO.getProyecto(proyectoid), TipoMonedaDAO.getTipoMonedaPorId(tipo_moneda), tfecha, 1, monto, 
								new BigDecimal(0), null, usuario, null, (id==-1) ? new Date() : null, (id>0) ? new Date() : null);
					if(id>0)
						desembolso.setId(id);
					ui_desembolsos.add(desembolso);
					result = result && DesembolsoDAO.guardarDesembolso(desembolso);
				}
				List<Desembolso> db_desembolsos = DesembolsoDAO.getDesembolsosPorProyecto(proyectoid);
				for(int i=0; i<db_desembolsos.size(); i++){
					boolean no_existe=true;
					for(int j=0; j<ui_desembolsos.size();j++){
						if(db_desembolsos.get(i).getId()==ui_desembolsos.get(j).getId())
							no_existe=false;
					}
					if(no_existe)
						DesembolsoDAO.eliminarDesembolso(db_desembolsos.get(i));
				}
				response_text = String.join("","{ \"success\": ",(result ? "true" : "false"),"}");
		}
		else if(accion.equals("getDesembolsosPorProyecto")){
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			List<Desembolso> desembolsos = DesembolsoDAO.getDesembolsosPorProyecto(proyectoId);
			List<stdesembolso> stdesembolsos=new ArrayList<stdesembolso>();
			for(Desembolso desembolso:desembolsos){
				stdesembolso temp =new stdesembolso();
				temp.id = desembolso.getId();
				temp.fecha = Utils.formatDate(desembolso.getFecha());
				temp.monto = desembolso.getMonto();
				temp.tipocambio = desembolso.getTipoCambio();
				temp.estado = desembolso.getEstado();
				temp.desembolsotipoid = desembolso.getDesembolsoTipo().getId();
				temp.desembolsotipo = desembolso.getDesembolsoTipo().getNombre();
				temp.proyecto = desembolso.getProyecto().getNombre();
				temp.tipo_moneda=desembolso.getTipoMoneda().getId();
				temp.tipo_moneda_nombre=desembolso.getTipoMoneda().getNombre();
				temp.tipomonedasimbolo=desembolso.getTipoMoneda().getSimbolo();
				temp.proyectoid = desembolso.getProyecto().getId();
				temp.fechaActualizacion = Utils.formatDateHour(desembolso.getFechaActualizacion());
				temp.fechaCreacion = Utils.formatDateHour(desembolso.getFechaCreacion());
				temp.usuarioActualizo = desembolso.getUsuarioActualizo();
				temp.usuarioCreo = desembolso.getUsuarioCreo();
				stdesembolsos.add(temp);
			}
			
			Integer tipoMonedaId;
			String tipoMonedaNombre;
			tipoMonedaId = 1;
			tipoMonedaNombre = "GTQ";
			

			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,", \"tipoMonedaId\" : " + tipoMonedaId + ", \"fechaActual\" : \"" + Utils.formatDate(new Date()) + "\", \"tipoMonedaNombre\" : \"" + tipoMonedaNombre +"\" }");
		} else if(accion.equals("getDesembolsosReales")){
			int proyectoId = map.get("proyectoid")!=null  ? Integer.parseInt(map.get("proyectoid")) : 0;
			Proyecto proyecto = ProyectoDAO.getProyecto(proyectoId);
			List<stdesembolsoreal> stdesembolsos=new ArrayList<stdesembolsoreal>();
			proyecto.getPrestamo();
			if (proyecto!=null && proyecto.getPrestamo()!=null && proyecto.getUnidadEjecutora()!=null){
				Long codigoPresupuestario = proyecto.getPrestamo().getCodigoPresupuestario();
				List<DtmAvanceFisfinanDetDti> desembolsosReales = DataSigadeDAO.getInfPorUnidadEjecutoraALaFecha(codigoPresupuestario.toString(), proyecto.getUnidadEjecutora().getId().getEntidadentidad(),
						proyecto.getUnidadEjecutora().getId().getUnidadEjecutora());
				
				for (DtmAvanceFisfinanDetDti dr : desembolsosReales){
					stdesembolsoreal temp =new stdesembolsoreal();
					temp.ejercicioFiscal = dr.getId().getEjercicioFiscal();
					temp.mesDesembolso = dr.getId().getMesDesembolso();
					temp.codigoPresupuestario = dr.getId().getCodigoPresupuestario();
					temp.desembolsosMesUsd = dr.getId().getDesembolsosMesUsd();
					temp.desembolsosMesGtq = dr.getId().getDesembolsosMesGtq();
					stdesembolsos.add(temp);
				}
			}

			response_text=new GsonBuilder().serializeNulls().create().toJson(stdesembolsos);
	        response_text = String.join("", "\"desembolsos\":",response_text);
	        response_text = String.join("", "{\"success\":true,", response_text,"}");
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

}
