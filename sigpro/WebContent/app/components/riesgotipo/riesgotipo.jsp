<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="riesgotipoController as riesgotipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarriesgopropiedad.jsp">
    	<%@ include file="/app/components/riesgotipo/buscarriesgopropiedad.jsp"%>
  	</script>
  	<shiro:lacksPermission name="32010">
		<p ng-init="riesgotipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Tipo de Riesgo</h3></div>
	</div>
	<br />


	<div class="row" align="center" ng-if="!riesgotipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="32040">
					<label class="btn btn-primary" ng-click="riesgotipoc.nuevo()" uib-tooltip="Nuevo">
					<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="32010">
					<label class="btn btn-primary" ng-click="riesgotipoc.editar()" uib-tooltip="Editar">
					<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="32030">
					<label class="btn btn-danger" ng-click="riesgotipoc.borrar()" uib-tooltip="Borrar">
					<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="32010">
			<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="riesgotipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="riesgotipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!riesgotipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  riesgotipoc.totalRiesgotipos + (riesgotipoc.totalRiesgotipos == 1 ? " Tipo de Riesgos" : " Tipos de Riesgos" ) }}</div>
			<ul uib-pagination total-items="riesgotipoc.totalRiesgotipos"
				ng-model="riesgotipoc.paginaActual"
				max-size="riesgotipoc.numeroMaximoPaginas"
				items-per-page="riesgotipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="riesgotipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>
	</div>

	<div class="row main-form" ng-if="riesgotipoc.mostraringreso">
		<h2 ng-hide="!riesgotipoc.esnuevo"><small>Nuevo Tipo Riesgo</small></h2>
		<h2 ng-hide="riesgotipoc.esnuevo"><small>Edición de Tipo Riesgo</small></h2>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="32020">
					<label class="btn btn-success" ng-click="form.$valid ? riesgotipoc.guardar() : ''"  ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="riesgotipoc.irATabla()"  uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id">ID {{riesgotipoc.riesgotipo.id }}</label> 
					<br/><br/>
				</div>

				<div class="form-group">
					<input type="text" class="inputText" id="nombre" ng-model="riesgotipoc.riesgotipo.nombre" ng-required="true" 
						value="{{riesgotipoc.riesgotipo.nombre}}" onblur="this.setAttribute('value', this.value);">
					<label class="floating-label">* Nombre</label>
				</div>
				<div class="form-group">
					<input type="text"class="inputText" id="descripcion" ng-model="riesgotipoc.riesgotipo.descripcion" value="{{riesgotipoc.riesgotipo.descripcion}}"
						onblur="this.setAttribute('value', this.value);">
					<label class="floating-label">Descripción</label>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p> {{ riesgotipoc.riesgotipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p id="fechaCreacion"> {{ riesgotipoc.riesgotipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioActualizo">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ riesgotipoc.riesgotipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label class="label-form" for="fechaActualizacion">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ riesgotipoc.riesgotipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<h5>Propiedades</h5>
				<br />
				<div align="center">
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="riesgotipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table style="width: 75%;"
					st-table="riesgotipoc.riesgopropiedades"
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
							ng-repeat="row in riesgotipoc.riesgopropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="riesgotipoc.eliminarPropiedad2(row)"
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
		<div align="center">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="32020">
					<label class="btn btn-success" ng-click="form.$valid ? riesgotipoc.guardar() : ''"  ng-disabled="!form.$valid"  uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="riesgotipoc.irATabla()"  uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
