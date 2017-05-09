<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="programatipoController as programatipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarpropiedad.jsp">
    	<%@ include file="/app/components/programatipo/buscarpropiedad.jsp"%>
  	</script>
  	<shiro:lacksPermission name="39010">
		<p ng-init="programatipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Tipo de Programa</h3></div>
	</div>
	
	<div align="center" ng-if="!programatipoc.mostraringreso">
		<br />
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="39040">
					<label class="btn btn-primary" ng-click="programatipoc.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="39010">
					<label class="btn btn-primary" ng-click="programatipoc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="39030">
					<label class="btn btn-danger" ng-click="programatipoc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="39010">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="programatipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="programatipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!programatipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  programatipoc.totalProgramatipos + (programatipoc.totalProgramatipos == 1 ? " Tipo de Programa" : " Tipos de Programa" ) }}</div>
			<ul uib-pagination total-items="programatipoc.totalProgramatipos"
				ng-model="programatipoc.paginaActual"
				max-size="programatipoc.numeroMaximoPaginas"
				items-per-page="programatipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="programatipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>	
	</div>

	<div class="row second-main-form" ng-if="programatipoc.mostraringreso">
		<div class="page-header">
			<h2 ng-hide="!programatipoc.esnuevo"><small>Nuevo tipo de programa</small></h2>
			<h2 ng-hide="programatipoc.esnuevo"><small>Edición tipo de programa</small></h2>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="39020">
					<label class="btn btn-success" ng-click="form.$valid ? programatipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programatipoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="form" id="form">
				<div class="form-group">
					<label for="id" class="floating-label">ID {{programatipoc.programatipo.id }}</label>
					<br/><br/>
				</div>

				<div class="form-group">
					<input type="text" class="inputText" id="nombre" ng-model="programatipoc.programatipo.nombre" ng-value="programatipoc.programatipo.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true">
						<label for="nombre" class="floating-label">* Nombre</label>
				</div>
				<div class="form-group">
					<input type="text" class="inputText" id="descripcion" 
						ng-model="programatipoc.programatipo.descripcion"  ng-value="programatipoc.programatipo.descripcion" onblur="this.setAttribute('value', this.value);" >
					<label for="descripcion" class="floating-label">Descripción</label> 
				</div>
				<br />
				<div align="center">
					<h5 class="label-form">Propiedades</h5>
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="programatipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table style="width: 75%;"
					st-table="programatipoc.programapropiedades"
					class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th class="label-form">ID</th>
							<th class="label-form">Nombre</th>
							<th class="label-form">Descripicon</th>
							<th class="label-form">Tipo Dato</th>
							<th class="label-form" style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in programatipoc.programapropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="programatipoc.eliminarPropiedad2(row)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p class=""> {{ programatipoc.programatipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ programatipoc.programatipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" id="usuarioCreo">{{ programatipoc.programatipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" id="usuarioCreo">{{ programatipoc.programatipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<br />
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="39020">
					<label class="btn btn-success" ng-click="form.$valid ? programatipoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programatipoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>
