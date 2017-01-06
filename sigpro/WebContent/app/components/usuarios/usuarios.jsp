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

	<div ng-controller="usuariosController as usuariosc" class="maincontainer all_page" id="title">
		<h3>Usuarios</h3><br/>
		<div class="row" align="center" ng-hide="usuariosc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="crearCooperante">
						<label class="btn btn-primary" ng-click="usuariosc.nuevoUsuario()">Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="editarCooperante">
						<label class="btn btn-primary" ng-click="usuariosc.editarUsuario()">Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="eliminarCooperante">
						<label class="btn btn-primary" ng-click="usuariosc.eliminarUsuario()">Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="verCooperante">
    			<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="usuariosc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="usuariosc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="usuariosc.myGrid">
						<div class="grid_loading" ng-hide="!usuariosc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="usuariosc.totalUsuarios" 
						ng-model="usuariosc.paginaActual" 
						max-size="usuariosc.numeroMaximoPaginas" 
						items-per-page="usuariosc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="usuariosc.cambiarPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<shiro:hasPermission name="verCooperante">
		<div class="row" ng-show="usuariosc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="usuariosc.guardarUsuario()">Guardar</label>
			        <label class="btn btn-primary" ng-click="usuariosc.cancelar()">Ir a Tabla</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form>
						<div class="form-group">
							<label for="nombre">Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="usuariosc.permisoSelected.nombre">
						</div>
						<div class="form-group">
							<label for="Descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="usuariosc.permisoSelected.descripcion">
						</div>
				</form>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="usuariosc.guardarUsuario()">Guardar</label>
			        <label class="btn btn-primary" ng-click="usuariosc.cancelar()">Ir a Tabla</label>
    			</div>
    		</div>
		</div>
		</shiro:hasPermission>
		

	</div>
