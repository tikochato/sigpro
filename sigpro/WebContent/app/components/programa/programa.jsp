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

	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Programas</h3></div>
	</div>
	
	<div class="row" align="center" ng-hide="programac.esColapsado">
	<br />
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="37040">
				<label class="btn btn-primary" ng-click="programac.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37010">
				<label class="btn btn-primary" ng-click="programac.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37030">
				<label class="btn btn-danger" ng-click="programac.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
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
	<div class="row second-main-form" ng-show="programac.esColapsado">
		<div class="page-header">
			<h2 ng-hide="!programac.esNuevo"><small>Nuevo Programa</small></h2>
			<h2 ng-hide="programac.esNuevo"><small>Edición de programa</small></h2>
			</div>
		
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label">ID {{ programac.programa.id }}</label>
  					<br/><br/>
				</div>
				<div class="form-group">
					<input type="text"  class="inputText" ng-model="programac.programa.nombre" value="{{programac.programa.nombre}}" onblur="this.setAttribute('value', this.value);" ng-required="true" >
					<label  class="floating-label">* Nombre</label>
				</div>
				<div class="form-group" >
		            	<input type="text" class="inputText" ng-model="programac.programatiponombre" value="{{programac.programatiponombre}}" 
		            		ng-click="programac.buscarProgramaTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
		            	<span class="label-icon" ng-click="programac.buscarProgramaTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          	<label  class="floating-label">* Tipo Programa</label>
				</div>

				<div ng-repeat="campo in programac.camposdinamicos">
					<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="controller.abrirPopupFecha($index)"
														value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="controller.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														value="{{number.valor}}">{{number.label}}</option>
								</select>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
				</div>
				
				<div class="form-group">
					<input type="text" ng-model="programac.programa.descripcion"
						class="inputText" id="campo2" 
						value="{{programac.programa.descripcion}}" onblur="this.setAttribute('value', this.value);">
					<label for="campo2" class="floating-label">Descripción</label>
				</div>
				<br />
				<div align="center">
					<h5 class="label-form">Proyectos </h5>
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
							<th class="label-form">ID</th>
							<th class="label-form">Nombre</th>
							<th class="label-form">Descripicon</th>
							<th  class="label-form" style="width: 30px;">Quitar</th>

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
				<div class="panel panel-default" ng-hide="programac.esNuevoDocumento">
					<div class="panel-heading label-form" style="text-align: center;">Archivos adjuntos</div>
					<div class="panel-body">
						<div style="width: 95%; float: left">
						<table st-table="programac.displayedCollection" st-safe-src="programac.rowCollection" class="table table-striped">
							<thead>
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form">Nombre</th>
									<th class="label-form">Extensión</th>
									<th class="label-form">Descripción</th>
									<th class="label-form">Descarga</th>
									<th class="label-form">Eliminar</th>
								</tr>
								<tr>
									<th colspan="5"><input st-search="" class="form-control" placeholder="busqueda global ..." type="text"/></th>
								</tr>
							</thead>
							<tbody>
							<tr ng-repeat="row in programac.displayedCollection">
								<td style="display: none;">{{row.id}}</td>
								<td>{{row.nombre}}</td>
								<td>{{row.extension}}</td>
								<td>{{row.descripcion}}</td>
								<td align="center">
									<button type="button"
										ng-click="programac.descargarDocumento(row)"
										uib-tooltip="Descargar documento" tooltip-placement="bottom"
										class="btn btn-default">
										<i class="glyphicon glyphicon-download-alt"> </i>
									</button>
								</td>
								<td align="center">
									<button type="button"
										ng-click="programac.eliminarDocumento(row)"
										uib-tooltip="Eliminar documento" tooltip-placement="bottom"
										class="btn btn-default">
										<i class="glyphicon glyphicon-minus-sign"> </i>
									</button>
								</td>
							</tr>
							</tbody>
						</table>
        				</div>
    					<div style="width: 5%; float: right" align="right">
	        				<label class="btn btn-default" ng-model="programac.adjuntarDocumento" 
	        					uib-tooltip="Adjuntar documento" tooltip-placement="bottom" ng-click="programac.adjuntarDocumentos();">
								<i class="glyphicon glyphicon glyphicon-plus"> </i>
							</label>
        				</div>
					</div>
				</div>
				<br/>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form">Usuario que creo</label>
				  					<p class="">{{ programac.programa.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label  class="label-form">Fecha de creación</label>
				  					<p class="">{{ programac.programa.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form">Usuario que actualizo</label>
				  					<p class="">{{ programac.programa.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label  class="label-form">Fecha de actualizacion</label>
				  					<p class="">{{ programac.programa.fechaactualizacion }}</p>
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
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>
