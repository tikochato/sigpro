<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<style type="text/css">

.myGrid {
	width: 100%;
	height: 600px;
}
</style>

	<div ng-controller="permisoController as permisosc" class="maincontainer all_page" id="title">
		<h3>Permisos</h3><br/>
		<div class="row" align="center" ng-hide="permisosc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="crearCooperante">
						<label class="btn btn-primary" ng-click="permisosc.nuevoPermiso()">Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="editarCooperante">
						<label class="btn btn-primary" ng-click="permisosc.editarPermiso()">Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="eliminarCooperante">
						<label class="btn btn-primary" ng-click="permisosc.borrarPermiso()">Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="verCooperante">
    			<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="permisosc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="permisosc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="permisosc.myGrid">
						<div class="grid_loading" ng-hide="!permisosc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="permisosc.totalPermisos" 
						ng-model="permisosc.paginaActual" 
						max-size="permisosc.numeroMaximoPaginas" 
						items-per-page="permisosc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="permisosc.cambiarPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<shiro:hasPermission name="verCooperante">
		<div class="row" ng-show="permisosc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? permisosc.guardarPermiso() : ''" ng-disabled="form.$invalid">Guardar</label>
			        <label class="btn btn-primary" ng-click="permisosc.cancelar()">Ir a Tabla</label>
    			</div>
    		</div>
    		<form name="form">
	    		<div class="col-sm-12">
					
							<div class="form-group">
								<label for="nombre">Nombre</label>
	    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="permisosc.permisoSelected.nombre" ng-required="true">
							</div>
							<div class="form-group">
								<label for="Descripcion">Descripción</label>
	    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="permisosc.permisoSelected.descripcion" ng-required="true">
							</div>
				</div>
    		</form>
			
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? permisosc.guardarPermiso() : ''" ng-disabled="form.$invalid">Guardar</label>
			        <label class="btn btn-primary" ng-click="permisosc.cancelar()">Ir a Tabla</label>
    			</div>
    		</div>
		</div>
		</shiro:hasPermission>
		

	</div>
