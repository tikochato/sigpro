<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="actividadtipoController as actividadtipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscaractividadpropiedad.jsp">
    	<%@ include file="/app/components/actividadtipo/buscaractividadpropiedad.jsp"%>
  	</script>
  	 <shiro:lacksPermission name="3010">
			<p ng-init="actividadtipoc.redireccionSinPermisos()"></p>
	 </shiro:lacksPermission>
	<div class="panel panel-default">
		<div class="panel-heading"><h3>Tipo de Actividad</h3></div>
	</div>
		


	<div class="row" align="center" ng-if="!actividadtipoc.mostraringreso">
		<div class="col-sm-12 operation_buttons" align="right">
		  <div class="btn-group">
		  <shiro:hasPermission name="3040">
		    <label class="btn btn-primary" ng-click="actividadtipoc.nuevo()" uib-tooltip="Nueva">
		    <span class="glyphicon glyphicon-plus"></span> Nueva</label>
		  </shiro:hasPermission>
		  <shiro:hasPermission name="3010">
		    <label class="btn btn-primary" ng-click="actividadtipoc.editar()" uib-tooltip="Editar">
		    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
		  </shiro:hasPermission>
		  <shiro:hasPermission name="3030">
		    <label class="btn btn-danger" ng-click="actividadtipoc.borrar()" uib-tooltip="Borrar">
		    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
		  </shiro:hasPermission>
		  </div>
		</div>
		<shiro:hasPermission name="3010">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="actividadtipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="actividadtipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!actividadtipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  actividadtipoc.totalActividadtipos + (actividadtipoc.totalActividadtipos == 1 ? " Tipo de Actividad" : " Tipos de actividades" ) }}</div>
			<ul uib-pagination total-items="actividadtipoc.totalActividadtipos"
				ng-model="actividadtipoc.paginaActual"
				max-size="actividadtipoc.numeroMaximoPaginas"
				items-per-page="actividadtipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="actividadtipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>
		
	</div>

	<div class="row second-main-form" ng-if="actividadtipoc.mostraringreso">
		<div class="page-header">
		    <h2 ng-hide="!actividadtipoc.esnuevo"><small>Nuevo Tipo de Actividad</small></h2>
		    <h2 ng-hide="actividadtipoc.esnuevo"><small>Edición de Tipo Actividad</small></h2>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
		  <div class="btn-group">
		    <shiro:hasPermission name="3020">
		      <label class="btn btn-success" ng-click="form.$valid ? actividadtipoc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
		      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
		    </shiro:hasPermission>
		    <label class="btn btn-primary" ng-click="actividadtipoc.irATabla()" title="Ir a Tabla">
		    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
		  </div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label">ID {{actividadtipoc.actividadtipo.id }}</label>
					<br/><br/>
				</div>
				<div class="form-group">
				   <input type="text" name="nombre"  class="inputText" id="nombre" 
				     ng-model="actividadtipoc.actividadtipo.nombre" value="{{actividadtipoc.actividadtipo.nombre}}"   
				     onblur="this.setAttribute('value', this.value);" ng-required="true" >
				   <label class="floating-label">* Nombre</label>
				</div>
				<div class="form-group">
				   <input type="text" name="descripcion"  class="inputText" id="descripcion" 
				     ng-model="actividadtipoc.actividadtipo.descripcion" value="{{actividadtipoc.actividadtipo.descripcion}}"   
				     onblur="this.setAttribute('value', this.value);"  >
				   <label class="floating-label">Descripción</label>
				</div>
				<br/>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label>Usuario que creo</label> 
									<p class="form-control-static">{{ actividadtipoc.actividadtipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label>Fecha de creación</label> 
									<p class="form-control-static">{{ actividadtipoc.actividadtipo.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label>Usuario que actualizo</label> 
									<p class="form-control-static">{{ actividadtipoc.actividadtipo.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label>Fecha de actualizacion</label> 
									<p class="form-control-static">{{ actividadtipoc.actividadtipo.fechaActualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<br />
				<div align="center">
					<h5>Propiedades</h5>
					<div style="height: 35px;" >
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="actividadtipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table
					st-table="actividadtipoc.actividadpropiedades"
					class="table table-striped table-bordered table-hover table-propiedades">
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
							ng-repeat="row in actividadtipoc.actividadpropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="actividadtipoc.eliminarPropiedad2(row)"
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
		    <shiro:hasPermission name="3020">
		      <label class="btn btn-success" ng-click="form.$valid ? actividadtipoc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
		      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
		    </shiro:hasPermission>
		    <label class="btn btn-primary" ng-click="actividadtipoc.irATabla()" title="Ir a Tabla">
		    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
		  </div>
		</div>
	</div>
</div>
