<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<style>
	.ui-grid-tree-header-row {
    	font-weight: normal !important;
	}
	
	.ui-grid-tree-padre {
    	font-weight: bold;
	}
</style>

	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="prestamometasingresoController as pmetasc" class="maincontainer all_page" id="title">
	    
  	    <shiro:lacksPermission name="30010">
			<p ng-init="pmetasc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Metas de Préstamo</h3></div>
		</div>
		<div class="subtitulo">
			{{ pmetasc.proyectoNombre }}
		</div>
		
		<div class="row" align="center" >
		
			<div class="operation_buttons" align="right">
					<div class="btn-group">
						<label class="btn btn-primary"  ng-click="pmetasc.exportarExcel()" uib-tooltip="Exportar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span> Exportar</label>
					</div>
			</div>
			
			<div id="grid" ui-grid="pmetasc.opcionesGrid"
				ui-grid-resize-columns ui-grid-selection ui-grid-pinning 
				ui-grid-grouping ui-grid-edit ui-grid-row-edit ui-grid-cellNav >
				<div class="grid_loading" ng-hide="!pmetasc.mostrarCargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
							<br />
							<br /> <b>Cargando, por favor espere...</b> 
						</span>
					</div>
				</div>
			</div>
		  
	</div>
</div>
