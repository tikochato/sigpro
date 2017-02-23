<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="riesgoController as riesgoc" class="maincontainer all_page" id="title">
	    <script type="text/ng-template" id="buscarRiesgoTipo.jsp">
    		<%@ include file="/app/components/riesgo/buscarRiesgoTipo.jsp"%>
  	    </script>
  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Riesgos</h3><br/>
		<h4>{{ riesgoc.proyectoNombre }}</h4><br/>
		<div class="row" align="center" ng-if="!riesgoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="30040">
			       		<label class="btn btn-primary" ng-click="riesgoc.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="30010"><label class="btn btn-primary" ng-click="riesgoc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="30030">
			       		<label class="btn btn-primary" ng-click="riesgoc.borrar()">Borrar</label>
			       </shiro:hasPermission>			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="30010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="riesgoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="riesgoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!riesgoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  riesgoc.totalRiesgos + (riesgoc.totalRiesgos == 1 ? " Riresgo" : " Riesgos" ) }}</div>
				<ul uib-pagination total-items="riesgoc.totalRiesgos" 
						ng-model="riesgoc.paginaActual" 
						max-size="riesgoc.numeroMaximoPaginas" 
						items-per-page="riesgoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="riesgoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
		</div>
		<div class="row main-form" ng-if="riesgoc.mostraringreso">
			<h4 ng-hide="!riesgoc.esnuevo">Nuevo riesgo</h4>
			<h4 ng-hide="riesgoc.esnuevo">Edición de riesgo</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="30020">
			        	<label class="btn btn-success" ng-click="form.$valid ? riesgoc.guardar() : ''"  ng-disabled="!form.$valid">Guardar</label></shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="riesgoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" ng-show="!riesgoc.esnuevo">
							<label for="id">ID</label>
    						<p class="form-control-static">{{ riesgoc.riesgo.id }}</p>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="riesgoc.riesgo.nombre" ng-required="true">
						</div>
						
						<div class="form-group">
							<label for="campo3">* Tipo Riesgo:</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control" id="irietipo" name="irietipo" placeholder="Nombre Tipo Riesgo" ng-model="riesgoc.riesgoTipoNombre" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="riesgoc.buscarRiesgoTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group" ng-repeat="campo in riesgoc.camposdinamicos">
							<label for="campo.id">{{ campo.label }}</label>
							<div ng-switch="campo.tipo">
								<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="entero" type="text" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
								<p ng-switch-when="fecha" class="input-group">
									<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{riesgoc.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="mi.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" /><span
														class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="riesgoc.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
								</p>
								<select ng-switch-when="select" id="{{ 'field_'+field.id }}" class="form-control" ng-model="x.value">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in field.options"
														value="{{number.value}}">{{number.label}}</option>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="riesgoc.riesgo.descripcion">
						</div>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ riesgoc.riesgo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ riesgoc.riesgo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ riesgoc.riesgo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ riesgoc.riesgo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
				        <shiro:hasPermission name="30020">
				        	<label class="btn btn-success" ng-click="form.$valid ? riesgoc.guardar() : ''"  ng-disabled="!form.$valid">Guardar</label></shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="riesgoc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
