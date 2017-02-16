<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="hitoController as hitoc" class="maincontainer all_page" id="title">
		<script type="text/ng-template" id="buscarHitoTipo.jsp">
    		<%@ include file="/app/components/hito/buscarHitoTipo.jsp"%>
  	    </script>
		<h3>Hitos</h3><br/>
		<h4>{{ hitoc.proyectoNombre }}</h4><br/>
		<div class="row" align="center" ng-hide="hitoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="15040">
			       		<label class="btn btn-primary" ng-click="hitoc.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="15020"><label class="btn btn-primary" ng-click="hitoc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="15030">
			       		<label class="btn btn-primary" ng-click="hitoc.borrar()">Borrar</label>
			       </shiro:hasPermission>   
    			</div>				
    		</div>
    		<shiro:hasPermission name="15010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="hitoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="hitoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!hitoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="hitoc.totalHitos" 
						ng-model="hitoc.paginaActual" 
						max-size="hitoc.numeroMaximoPaginas" 
						items-per-page="hitoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="hitoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row main-form" ng-show="hitoc.mostraringreso">
			<h4 ng-hide="!hitoc.esnuevo">Nuevo hito</h4>
			<h4 ng-hide="hitoc.esnuevo">Edición de hito</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? hitoc.guardar(): ''" ng-disabled="!form.$valid">Guardar</label>
			        <label class="btn btn-primary" ng-click="hitoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form" id="form">
						<div class="form-group" ng-show="!hitoc.esnuevo">
							<label for="id">ID</label>
    						<p class="form-control-static" id="id">{{ hitoc.hito.id }}</p>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="hitoc.hito.nombre" ng-required="true">
						</div>
						<div class="form-group">
							<label for="descripcion">* Fecha</label>
							<div class="input-group">
							<input type="text" class="form-control" uib-datepicker-popup="{{hitoc.formatofecha}}" ng-model="hitoc.fecha" 
	    						is-open="hitoc.popupfecha.abierto" datepicker-options="hitoc.fechaOptions"  close-text="Cerrar" 
	    						alt-input-formats="altInputFormats" current-text="Hoy" clear-text="Borrar" ng-required="true"/>	
	    						<span class="input-group-btn">
						        	<button type="button" class="btn btn-default" ng-click="hitoc.abirpopup()"><i class="glyphicon glyphicon-calendar"></i></button>
						        </span>
					        </div>
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="hitoc.hito.descripcion">
						</div>
						<div class="form-group">
							<label for="campo3">* Tipo hito</label>
				          	<div class="input-group">
				            	<input type="hidden" class="form-control" ng-model="hitoc.hitotipoid" /> 
				            	<input type="text" class="form-control" id="icomptipo" name="icomptipo" placeholder="Nombre Tipo Hito" ng-model="hitoc.hitotipoNombre" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="hitoc.buscarHitoTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						<div class="form-group" ng-if="hitoc.hitotipoid>0">
							<label for="campo3">Resultado</label>
							<div ng-switch="hitoc.hitodatotipoid">
								<input ng-switch-when="1" type="text"  ng-model="hitoc.hitoresultado" class="form-control" />
								<input ng-switch-when="2" type="text"  numbers-Only ng-model="hitoc.hitoresultado" class="form-control" />
								<input ng-switch-when="3" type="number"  ng-model="hitoc.hitoresultado" class="form-control" />
								<input ng-switch-when="4" type="checkbox" ng-model="hitoc.hitoresultado" ng-change="hitoc.change()"/>
								<p ng-switch-when="5" class="input-group">
									<input type="text"  class="form-control" uib-datepicker-popup="{{hitoc.formatofecha}}" ng-model="hitoc.hitoresultado" is-open="hitoc.popupfecharesultado.abierto"
											datepicker-options="hitoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" />
									<span class="input-group-btn">
										<button type="button" class="btn btn-default" ng-click="hitoc.abirpopupreultado()">
											<i class="glyphicon glyphicon-calendar"></i>
										</button>
									</span>
								</p>
							</div>
						</div>
						
						<div class="form-group" ng-if="hitoc.hitotipoid>0">
							<label for="descripcion">Comentario</label>
    						<input type="text" class="form-control" id="comentario" placeholder="Comentario de resultado" ng-model="hitoc.hitoresultadocomentario">
						</div>
					<br/>
					<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group" style="text-align: right">
										<label for="usuarioCreo">Usuario que creo</label> 
										<p class="form-control-static"> {{ hitoc.hito.usuarioCreo }}</p>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group" >
										<label for="fechaCreacion">Fecha de creación</label>
										<p class="form-control-static" id="fechaCreacion"> {{ hitoc.hito.fechaCreacion }} </p>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group" style="text-align: right">
										<label for="usuarioActualizo">Usuario que actualizo</label> 
										<p class="form-control-static" id="usuarioCreo">{{ hitoc.hito.usuarioActualizo }} </p>
									</div>	
								</div>
								<div class="col-sm-6">		
									<div class="form-group">
										<label for="fechaActualizacion">Fecha de actualizacion</label> 
										<p class="form-control-static" id="usuarioCreo">{{ hitoc.hito.fechaActualizacion }} </p>
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
				        <label class="btn btn-success" ng-click="form.$valid ? hitoc.guardar(): ''" ng-disabled="!form.$valid">Guardar</label>
				        <label class="btn btn-primary" ng-click="hitoc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
