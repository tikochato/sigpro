<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
	pre {
    
    margin: 0 0 0px;
    font-size: medium;
    line-height: normal;
    color: #333;
    word-break: none;
    word-wrap: break-word;
    background-color: transparent;
    border: none;
    border-radius: none;
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    padding: 0px;
    line-height: normal;
     
}

	</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="agendaController as agendac" class="maincontainer all_page" id="title">
	    
  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Agenda</h3><br/>
		<div class="row" align="center" >
		
			<div class="operation_buttons" align="left">
					<div class="btn-group">
						<label class="btn btn-primary"  ng-click="agendac.exportarExcel()" uib-tooltip="Exportar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true">&nbsp;Exportar</span></label>
					</div>
			</div>
			<div class="col-sm-12 ">
				<table st-table="agendac.agenda" st-safe-src="agendac.lista" class="table table-condensed table-hover">
					<thead>
						<tr>
							<th>Actividad</th>
							<th>Fecba Inicio</th>
							<th >Fecha Fin</th>
							<th>Estado</th>
						</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in agendac.agenda">
						
						<td>
							<pre>{{row.nombre}}</pre>
						</td>
						<td>{{row.fechaInicio}}</td>
						<td>{{row.fechaFin}}</td>
						<td>{{row.estado}}</td>
					</tr>
					</tbody>
				</table>
				
				<br/>
				
				
			</div>
		  
	</div>
</div>
