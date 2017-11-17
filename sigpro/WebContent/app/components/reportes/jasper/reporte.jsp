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
	
	HttpSession sesionweb = request.getSession();
	String usuario = sesionweb.getAttribute("usuario")!= null ? sesionweb.getAttribute("usuario").toString() : null;
	
	JasperPrint jasperPrint = null;
	switch(reporteId){
		case 0: 
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("proyectoId",proyectoId);
			parameters.put("usuario",usuario);
			jasperPrint = CJasperReport.reporteJasperPrint(CJasperReport.PLANTILLA_PLANANUAL, parameters);
			break;
		case 1: jasperPrint = PlanEjecucionDAO.generarJasper(proyectoId, usuario);
			break;
		case 2: jasperPrint = PlanEjecucionDAO.generarJasper(proyectoId, usuario);
			break;
		case 3: 
			jasperPrint = InformacionPresupuestariaDAO.generarJasper(proyectoId, usuario);
			break;
	}
	
	if(jasperPrint!=null)
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
 
%>