<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="formularioController as formularioc"
	class="maincontainer all_page" id="title">
	<script type="text/ng-template" id="buscarPorFormulario.jsp">
    		<%@ include file="/app/components/formulario/buscarPorFormulario.jsp"%>
  	</script>
  	<shiro:lacksPermission name="12010">
		<p ng-init="formularioc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<h3>Formularios</h3>
	<br />
	<div class="row" align="center" ng-if="!formularioc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="12040">
					<label class="btn btn-primary" ng-click="formularioc.nuevo()">Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="12010">
				<label class="btn btn-primary" ng-click="formularioc.editar()">Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="12030">
					<label class="btn btn-primary" ng-click="formularioc.borrar()">Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="12010">
			<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="formularioc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="formularioc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!formularioc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<ul uib-pagination total-items="formularioc.totalFormularios"
				ng-model="formularioc.paginaActual"
				max-size="formularioc.numeroMaximoPaginas"
				items-per-page="formularioc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="formularioc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>
		
	</div>

	<div class="row second-main-form" ng-show="formularioc.mostraringreso">
		<h4 ng-hide="!formularioc.esnuevo">Nuevo Formulario</h4>
		<h4 ng-hide="formularioc.esnuevo">Edición de Formulario</h4>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="12020">
					<label class="btn btn-success" ng-click="form.$valid ? formularioc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="formularioc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id">ID</label> 
					<p class="form-control-static">{{ formularioc.formulario.id }}</p>
				</div>
				<div class="form-group">
					<label for="nombre">* Código</label> <input type="text"
						class="form-control" id="nombre" placeholder="Código"
						ng-model="formularioc.formulario.codigo">
				</div>
				
				<div class="form-group">
					<label for="campo3">* Tipo Formulario</label>
		          	<div class="input-group">
		            	<input type="hidden" class="form-control" ng-model="formularioc.formulariotipoid" /> 
		            	<input type="text" class="form-control" id="iproyt" name="iproyt" placeholder="Nombre Tipo Formulario" ng-model="formularioc.formulariotiponombre" ng-readonly="true" required/>
		            	<span class="input-group-addon" ng-click="formularioc.buscarFormularioTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          	</div>
				</div>
				
				<div class="form-group">
					<label for="descripcion">Descripción</label> <input type="text"
						class="form-control" id="descripcion" placeholder="Descripción"
						ng-model="formularioc.formulario.descripcion">
				</div>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static" id="usuarioCreo"> {{ formularioc.formulario.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ formularioc.formulario.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ formularioc.formulario.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ formularioc.formulario.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>

				<br />
				<div align="center">
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="formularioc.buscarFormularioItem()" role="button"
									uib-tooltip="Asignar nuevo item" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					
					<table style="width: 75%;"
					st-table="formularioc.formularioitemtipos"
					class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th>ID</th>
							<th>Nombre</th>
							<th>Descripicon</th>
							<th>Tipo Dato</th>
							<th style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in formularioc.formularioitemtipos">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="formularioc.eliminarPropiedad2(row)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				
				
			</form>
		</div>
		<br />
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="12020">
					<label class="btn btn-success" ng-click="form.$valid ? formularioc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="formularioc.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
