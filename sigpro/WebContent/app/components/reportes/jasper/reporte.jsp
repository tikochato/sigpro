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
	String usuario = request.getAttribute("usuario")!= null ? request.getAttribute("usuario").toString() : null;

	JasperPrint jasperPrint = null;
	switch(reporteId){
		case 1: jasperPrint = PlanEjecucionDAO.generarJasper(proyectoId, usuario);
			break;
		case 2: jasperPrint = PlanEjecucionDAO.generarJasper(proyectoId, usuario);
			break;
	}
	
	if(jasperPrint!=null)
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
 
%>