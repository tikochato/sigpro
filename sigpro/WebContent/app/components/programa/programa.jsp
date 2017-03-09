<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="programaController as programac"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarPorPrograma.jsp">
    		<%@ include file="/app/components/programa/buscarPorPrograma.jsp"%>
  	 </script>
  	 <shiro:lacksPermission name="37010">
		<p ng-init="programac.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>

	<h3>Programas</h3>
	<br />
	<div class="row" align="center" ng-hide="programac.esColapsado">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="37040">
				<label class="btn btn-primary" ng-click="programac.nuevo()">Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37010">
				<label class="btn btn-primary" ng-click="programac.editar()">Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37030">
				<label class="btn btn-primary" ng-click="programac.borrar()">Borrar</label>
			</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="37010">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
					<shiro:hasPermission name="37010">
						<a class="btn btn-default" href ng-click="programac.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</shiro:hasPermission>
					</div>
				</div>
			</div>
			<br/>
			<div id="grid1" ui-grid="programac.gridOpciones" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination
				ui-grid-pagination>
				<div class="grid_loading" ng-hide="!programac.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  programac.totalProgramas + (programac.totalProgramas == 1 ? " Programa" : " Programas" ) }}</div>
				<ul uib-pagination total-items="programac.totalProgramas"
						ng-model="programac.paginaActual"
						max-size="programac.numeroMaximoPaginas"
						items-per-page="programac.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="programac.cambioPagina()"
				></ul>
		</div>

		</shiro:hasPermission>


	</div>
	<div class="row main-form" ng-show="programac.esColapsado">
		<h4 ng-hide="!programac.esNuevo">Nuevo Programa</h4>
		<h4 ng-hide="programac.esNuevo">Edición de programa</h4>
		
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id">ID</label>
  					<p class="form-control-static">{{ programac.programa.id }}</p>
				</div>
				<div class="form-group">
					<label >* Nombre</label>
					<input type="text"  ng-model="programac.programa.nombre"
						class="form-control" placeholder="Nombre" ng-required="true" >
				</div>
				<div class="form-group" >
					<label >* Tipo Programa</label>
		          	<div class="input-group">
		            	<input type="text" class="form-control" placeholder="Nombre Tipo Programa" ng-model="programac.programatiponombre" ng-readonly="true" ng-required="true"/>
		            	<span class="input-group-addon" ng-click="programac.buscarProgramaTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          	</div>
				</div>

				<div class="form-group" ng-repeat="campo in programac.camposdinamicos">
					<label for="campo.id">{{ campo.label }}</label>
					<div ng-switch="campo.tipo">
						<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" placeholder="{{campo.label}}" />
						<input ng-switch-when="entero" type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" placeholder="{{campo.label}}"  />
						<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" placeholder="{{campo.label}}" />
						<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
						<p ng-switch-when="fecha" class="input-group">
							<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"/>
												<span class="input-group-btn">
													<button type="button" class="btn btn-default"
														ng-click="programac.abrirPopupFecha($index)">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span>
						</p>
						<select ng-switch-when="select" id="{{ 'campo_'+campo.id }}" class="form-control" ng-model="campo.valor">
											<option value="">Seleccione una opción</option>
											<option ng-repeat="number in campo.opciones"
												value="{{number.valor}}">{{number.label}}</option>
						</select>
					</div>
				</div>
				
				<div class="form-group">
					<label for="campo2">Descripción</label>
					<input type="text" ng-model="programac.programa.descripcion"
						class="form-control" id="campo2" placeholder="Descripción">
				</div>
				<br />
				<div align="center">
					<h5>Proyectos </h5>
					<div style="height: 35px; width: 75%">
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="programac.buscarProyecto()" role="button"
									uib-tooltip="Asignar un nuevo proyecto" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table style="width: 75%;"
					st-table="programac.proyectos"
					class="table table-striped  table-bordered">
					<thead >
						<tr>
							<th>ID</th>
							<th>Nombre</th>
							<th>Descripicon</th>
							<th style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in programac.proyectos">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.descripcion}}</td>
							<td>
								<button type="button"
									ng-click="programac.eliminarProyecto(row)"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<br/>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label >Usuario que creo</label>
				  					<p class="form-control-static">{{ programac.programa.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label >Fecha de creación</label>
				  					<p class="form-control-static">{{ programac.programa.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label >Usuario que actualizo</label>
				  					<p class="form-control-static">{{ programac.programa.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label >Fecha de actualizacion</label>
				  					<p class="form-control-static">{{ programac.programa.fechaactualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>
