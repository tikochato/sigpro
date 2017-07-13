<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/ng-template" id="buscarUnidadEjecutora.jsp">
   	<%@ include file="/app/components/usuarios/buscarUnidadEjecutora.jsp"%>
</script>

<div ng-controller="informeUnidadController as controller" class="maincontainer all_page" id="title">
	<shiro:authenticated>
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Informe Unidad Ejecutora</h3></div>
		</div>
			
		<div class="second-main-form">
			<br>
			<div class="row">
			<div class="form-group col-sm-3">
				<input type="text"  class="inputText"  ng-model="controller.unidadEjecutora.nombre" 
				ng-required="true"  ng-click="controller.buscarUnidadEjecutora()" ng-readonly="true"
				ng-value="controller.unidadEjecutora.nombre" onblur="this.setAttribute('value', this.value);"/>
					<span class="label-icon" ng-click="controller.buscarUnidadEjecutora()">
				    	<i class="glyphicon glyphicon-search"></i>
				    </span>
					<label for="campo.id" class="floating-label">* Unidad Ejecutora</label>
			</div>
			</div>
			
			<br>
			
	    </div>
	    <div class="second-main-form">
			<h3>Préstamos</h3>
			
			<table style="width: 100%; overflow-y: scroll;height: 175px;display: block;"
			st-table="controller.prestamos"  ng-show="!usuarioc.esNuevo"
			class="table table-striped  table-bordered table-hover table-propiedades">
				<thead>
					<tr>
						<th style="width: 100%;">Nombre</th>
						<th style="width: 10%;">ver</th>				
					</tr>
				</thead>
				<tbody>
					<tr st-select-row="row"
					ng-repeat="row in controller.prestamos">
						<td>{{row.nombre}}</td>
						
						<td>
							<button type="button"
							ng-click="controller.verUsuarios($index,row)"
							class="btn btn-sm btn-prymary">
								<i class="glyphicon glyphicon glyphicon-info-sign"> </i>
							</button>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="grid_loading" ng-if="controller.mostrarCargandoUno" style="margin-top:80px; width: 96%; margin-left: 1%;">
				<div class="msg">
					<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						<br><br>
						<b>Cargando, por favor espere...</b>
					</span>
				</div>
			</div> 
			<br>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Roles por préstamo</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form"for="usuarioCreo">Administrador</label>
											<p>{{ controller.roles.administrador }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="label-form" for="fechaCreacion">DCP</label>
					 						<p>{{ controller.roles.dcp }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioActualizo">Unidad Ejecutora</label>
					 						<p>{{ controller.roles.unidadEjecutora }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="label-form" for="fechaActualizacion">Planificador</label>
					 						<p>{{ controller.roles.planificador }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioActualizo">Cooperante</label>
					 						<p>{{ controller.roles.cooperante }}</p>
								</div>
							</div>			
						</div>
					 </div>
				</div>
		</div>
			
	</shiro:authenticated>
	
</div>