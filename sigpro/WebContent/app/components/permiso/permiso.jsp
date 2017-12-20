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
		<shiro:lacksPermission name="20010">
			<p ng-init="permisosc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Permisos</h3></div>
	</div>
	
		<div align="center" ng-hide="permisosc.isCollapsed">
		<br>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="20040">
						<label class="btn btn-primary" ng-click="permisosc.nuevoPermiso()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="20020">
						<label class="btn btn-primary" ng-click="permisosc.editarPermiso()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="20030">
						<label class="btn btn-danger" ng-click="permisosc.borrarPermiso()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="20010">
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
				<br>
				<div class="total-rows">
				  Total de {{  permisosc.totalPermisos + (permisosc.totalPermisos == 1 ? " Permiso" : " Permisos" ) }}
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
		<shiro:hasPermission name="20010">
		<div class="row  second-main-form" ng-show="permisosc.isCollapsed">
		<div class="page-header">
			<h2 ng-hide="!permisosc.esNuevo"><small>Nuevo Permiso</small></h2>
			<h2 ng-hide="permisosc.esNuevo"><small>Edición de Permiso</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="20020">
			        	<label class="btn btn-success" ng-click="form.$valid ? permisosc.guardarPermiso() : ''" ng-disabled="form.$invalid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="permisosc.cancelar()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			<br>
	    	<div class="col-sm-12">
    		<form name="form">
	    					<div class="form-group" ng-show="!permisosc.esNuevo">
								<label for="id" class="floating-label">ID {{permisosc.permisoSelected.id}}</label>
								<br/><br/>
							</div>
							
							<div class="form-group" ng-show="permisosc.esNuevo">
	    						<input type="number" class="inputText" id="id" ng-model="permisosc.permisoSelected.id"  ng-value="permisosc.permisoSelected.id" onblur="this.setAttribute('value', this.value);" ng-required="permisosc.esNuevo" >
								<label for="id1" class="floating-label">* ID</label>
							</div>
							
							<div class="form-group">
	    						<input type="text" class="inputText" id="nombre" ng-model="permisosc.permisoSelected.nombre"  ng-value="permisosc.permisoSelected.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true">
								<label for="nombre" class="floating-label">* Nombre</label>
							</div>
							<div class="form-group">
							   <textarea class="inputText" rows="4"
							   ng-model="permisosc.permisoSelected.descripcion" ng-value="permisosc.permisoSelected.descripcion"   
							   onblur="this.setAttribute('value', this.value);" ng-required="true" ></textarea>
							   <label class="floating-label">* Descripción</label>
							</div>
							<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;" >Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label>
									<p class=""> {{ permisosc.permisoSelected.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ permisosc.permisoSelected.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label>
									<p class="" id="usuarioCreo">{{ permisosc.permisoSelected.usuarioActualizo }} </p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label>
									<p class="" id="usuarioCreo">{{ permisosc.permisoSelected.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
    		</form>
			</div>

			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="20020">
			        	<label class="btn btn-success" ng-click="form.$valid ? permisosc.guardarPermiso() : ''" ng-disabled="form.$invalid"  uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="permisosc.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
		</div>
		</shiro:hasPermission>


	</div>
