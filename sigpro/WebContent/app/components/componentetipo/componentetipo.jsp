<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="componentetipoController as componentetipoc"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarcomponentepropiedad.jsp">
    	<%@ include file="/app/components/componentetipo/buscarcomponentepropiedad.jsp"%>
  	</script>
  	<shiro:lacksPermission name="7010">
		<p ng-init="componentetipoc.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Tipo de Componente</h3></div>
		</div>


	<div class="row" align="center" ng-if="!componentetipoc.mostraringreso">
		
		<div class="col-sm-12 operation_buttons" align="right">
		  <div class="btn-group">
		  <shiro:hasPermission name="7040">
		    <label class="btn btn-primary" ng-click="componentetipoc.nuevo()" uib-tooltip="Nuevo">
		    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
		  </shiro:hasPermission>
		  <shiro:hasPermission name="7010">
		    <label class="btn btn-primary" ng-click="componentetipoc.editar()" uib-tooltip="Editar">
		    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
		  </shiro:hasPermission>
		  <shiro:hasPermission name="7030">
		    <label class="btn btn-danger" ng-click="componentetipoc.borrar()" uib-tooltip="Borrar">
		    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
		  </shiro:hasPermission>
		  </div>
		</div>
		<shiro:hasPermission name="7010">
			<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;">
					<div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href
							ng-click="componentetipoc.reiniciarVista()" role="button"
							uib-tooltip="Reiniciar la vista de la tabla"
							tooltip-placement="left"><span
							class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
				</div>
			</div>
			<br />
			<div id="maingrid" ui-grid="componentetipoc.gridOptions"
				ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
				ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
				<div class="grid_loading" ng-hide="!componentetipoc.mostrarcargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
						<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  componentetipoc.totalComponentetipos + (componentetipoc.totalComponentetipos == 1 ? " Tipo de Componente" : " Tipos de Componentes" ) }}</div>
			<ul uib-pagination total-items="componentetipoc.totalComponentetipos"
				ng-model="componentetipoc.paginaActual"
				max-size="componentetipoc.numeroMaximoPaginas"
				items-per-page="componentetipoc.elementosPorPagina"
				first-text="Primero" last-text="Último" next-text="Siguiente"
				previous-text="Anterior" class="pagination-sm" boundary-links="true"
				force-ellipses="true" ng-change="componentetipoc.cambioPagina()"></ul>
		</div>
		</shiro:hasPermission>
	
	</div>

	<div class="row second-main-form" ng-if="componentetipoc.mostraringreso">
		<div class="page-header">
		    <h2 ng-hide="!componentetipoc.esnuevo"><small>Nuevo Tipo Componente</small></h2>
		    <h2 ng-hide="componentetipoc.esnuevo"><small>Edición de Tipo Componente</small></h2>
		</div>
		
		<div class="operation_buttons" align="right">
		  <div class="btn-group">
		    <shiro:hasPermission name="7020">
		      <label class="btn btn-success" ng-click="form.$valid ? componentetipoc.guardar():''" ng-disabled="!form.$valid" title="Guardar">
		      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
		    </shiro:hasPermission>
		    <label class="btn btn-primary" ng-click="componentetipoc.irATabla()" title="Ir a Tabla">
		    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
		  </div>
		</div>
		<div class="col-sm-12">
			<form name="form" id="form">
				<div class="form-group">
				  <label for="id" class="floating-label">ID {{componentetipoc.componentetipo.id }}</label>
				  <br/><br/>
				</div>

				<div class="form-group">
				   <input type="text" name="inombre"  class="inputText" id="inombre" 
				     ng-model="componentetipoc.componentetipo.nombre" value="{{componentetipoc.componentetipo.nombre}}"   
				     onblur="this.setAttribute('value', this.value);" ng-required="true" show-focus="componentetipoc.mostraringreso">
				   <label class="floating-label">* Nombre</label>
				</div>
				
				<div class="form-group">
				   <input type="text" name="idescrip"  class="inputText" id="idescrip" 
				     ng-model="componentetipoc.componentetipo.descripcion" value="{{componentetipoc.componentetipo.descripcion}}"   
				     onblur="this.setAttribute('value', this.value);" ng-required="false" >
				   <label class="floating-label">Descripción</label>
				</div>
				<br/>
				<h5>Propiedades</h5>
				<div align="center">
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="componentetipoc.buscarPropiedad()" role="button"
									uib-tooltip="Asignar nueva propiedad" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table style="width: 75%;"
					st-table="componentetipoc.componentepropiedades"
					class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th>ID</th>
							<th>Nombre</th>
							<th>Descripción</th>
							<th>Tipo Dato</th>
							<th style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in componentetipoc.componentepropiedades">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>{{row.datotiponombre}}</td>
							<td>
								<button type="button"
									ng-click="componentetipoc.eliminarPropiedad2(row)"
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
									<label for="usuarioCreo" class="label-form" >Usuario que creo</label> 
									<p  id="usuarioCreo"> {{ componentetipoc.componentetipo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label class="label-form"for="fechaCreacion">Fecha de creación</label>
									<p id="fechaCreacion"> {{ componentetipoc.componentetipo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioActualizo">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ componentetipoc.componentetipo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ componentetipoc.componentetipo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<br />
			</form>
		</div>
		<br />
		<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
		
		<div class="col-sm-12 operation_buttons" align="right">
		  <div class="btn-group">
		    <shiro:hasPermission name="7020">
		      <label class="btn btn-success" ng-click="form.$valid ? componentetipoc.guardar():''" ng-disabled="!form.$valid" title="Guardar">
		      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
		    </shiro:hasPermission>
		    <label class="btn btn-primary" ng-click="componentetipoc.irATabla()" title="Ir a Tabla">
		    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
		  </div>
		</div>
	</div>
</div>
