<%@page import="dao.ProyectoDAO"%>
<%@page import="pojo.PepDetalle"%>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dao.ObjetoCostoJasper"%>
<%@page import="java.util.List"%>
<%@page import="dao.ObjetoDAO"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="dao.InformacionPresupuestariaDAO"%>
<%@page import="dao.PlanEjecucionDAO"%>
<%@page import="utilities.Utils"%>
<%@ page contentType="application/pdf" %>
 
<%@ page trimDirectiveWhitespaces="true"%>
 
<%@ page import="net.sf.jasperreports.engine.*" %>
<%@ page import="java.util.HashMap" %>
<%@page import="java.util.Map"%>
<%@ page import="utilities.CJasperReport" %>
 
<%
	Integer reporteId = request.getParameter("reporte")!=null ? Utils.String2Int(request.getParameter("reporte").toString()):0;
	Integer proyectoId = request.getParameter("proyecto")!=null ? Utils.String2Int(request.getParameter("proyecto").toString()):0;
	Long fecha = request.getParameter("fecha")!=null ? Utils.String2Long(request.getParameter("fecha").toString()): new Date().getTime();
	String lineaBase = request.getParameter("lb")!=null ? ("|lb"+request.getParameter("lb").toString()+"|"): null;
	
	HttpSession sesionweb = request.getSession();
	String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
	
	JasperPrint jasperPrint = null;
	switch(reporteId){
		case 0: 
			Date fechaCorte = new Date(fecha);
			DateTime dateTime = new DateTime(fechaCorte);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("proyectoId",proyectoId);
			parameters.put("usuario",usuario);
			
			List<ObjetoCostoJasper> listadoCostos = ObjetoDAO.getEstructuraConCostoJasper(proyectoId, dateTime.getYear(), dateTime.getYear(), lineaBase, usuario);
			parameters.put("costos",listadoCostos);
			
			ArrayList<BigDecimal> costoReal = new ArrayList<BigDecimal>();
			costoReal.add(listadoCostos.get(0).getEneroP());
			costoReal.add(listadoCostos.get(0).getFebreroP());
			costoReal.add(listadoCostos.get(0).getMarzoP());
			costoReal.add(listadoCostos.get(0).getAbrilP());
			costoReal.add(listadoCostos.get(0).getMayoP());
			costoReal.add(listadoCostos.get(0).getJunioP());
			costoReal.add(listadoCostos.get(0).getJulioP());
			costoReal.add(listadoCostos.get(0).getAgostoP());
			costoReal.add(listadoCostos.get(0).getSeptiembreP());
			costoReal.add(listadoCostos.get(0).getOctubreP());
			costoReal.add(listadoCostos.get(0).getNoviembreP());
			costoReal.add(listadoCostos.get(0).getDiciembreP());
			
			parameters.put("costoReal",costoReal);
			parameters.put("fechaCorte", fechaCorte);
			parameters.put("lineaBase", lineaBase);
			
			PepDetalle detalle = ProyectoDAO.getPepDetalle(proyectoId);
			if(detalle!=null){
				parameters.put("observaciones",detalle.getObservaciones());
				parameters.put("alertivos",detalle.getAlertivos());
				parameters.put("elaborado",detalle.getElaborado());
				parameters.put("aprobado",detalle.getAprobado());
				parameters.put("autoridad",detalle.getAutoridad());
			}
			jasperPrint = CJasperReport.reporteJasperPrint(CJasperReport.PLANTILLA_PLANANUAL, parameters);
			break;
		case 1: jasperPrint = PlanEjecucionDAO.generarJasper(proyectoId, usuario);
			break;
		case 2: jasperPrint = PlanEjecucionDAO.generarJasper(proyectoId, usuario);
			break;
		case 3: 
			jasperPrint = InformacionPresupuestariaDAO.generarJasper(proyectoId, DateTime.now().getYear(), lineaBase, usuario);
			break;
	}
	
	if(jasperPrint!=null)
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
 
%>