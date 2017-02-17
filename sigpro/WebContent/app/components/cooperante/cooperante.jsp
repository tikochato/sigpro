<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="cooperanteController as cooperantec" class="maincontainer all_page" id="title">
		<h3>Cooperantes</h3><br/>
		<div class="row" align="center" ng-hide="cooperantec.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="8040">
			       		<label class="btn btn-primary" ng-click="cooperantec.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="8010"><label class="btn btn-primary" ng-click="cooperantec.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="8030">
			       		<label class="btn btn-primary" ng-click="cooperantec.borrar()">Borrar</label>
			       </shiro:hasPermission>
			        
			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="8010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="cooperantec.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="cooperantec.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!cooperantec.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="cooperantec.totalCooperantes" 
						ng-model="cooperantec.paginaActual" 
						max-size="cooperantec.numeroMaximoPaginas" 
						items-per-page="cooperantec.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="cooperantec.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row main-form" ng-show="cooperantec.mostraringreso">
			<h4 ng-hide="!cooperantec.esnuevo">Nuevo cooperante</h4>
			<h4 ng-hide="cooperantec.esnuevo">Edición de cooperante</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="8020">
			        	<label class="btn btn-success" ng-click="form.$valid ? cooperantec.guardar() : ''" ng-disabled="form.$invalid">Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="cooperantec.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" ng-show="!cooperantec.esnuevo">
							<label for="id">ID</label>
    						<p class="form-control-static" id="id" >{{ cooperantec.cooperante.id }} </p>
						</div>
						<div class="form-group">
							<label for="codigo">* Código</label>
    						<input type="text" class="form-control" id="codigo" placeholder="Código" ng-model="cooperantec.cooperante.codigo" ng-required="true">
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="cooperantec.cooperante.nombre" ng-required="true">
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="cooperantec.cooperante.descripcion">
						</div>
						<br/>
						<div class="panel panel-default">
							<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6" >
										<div class="form-group" style="text-align: right">
											<label for="usuarioCreo">Usuario que creo</label>
				    						<p class="form-control-static" id="usuarioCreo"> {{ cooperantec.cooperante.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6" >
										<div class="form-group">
											<label for="fechaCreacion">Fecha de creación</label>
				    						<p class="form-control-static" id="fechaCreacion"> {{ cooperantec.cooperante.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6" >
										<div class="form-group" style="text-align: right">
											<label for="usuarioActualizo">Usuario que actualizo</label>
				    						<p class="form-control-static" id="usuarioCreo"> {{ cooperantec.cooperante.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6" >
										<div class="form-group">
											<label for="fechaActualizacion">Fecha de actualizacion</label>
				    						<p class="form-control-static" id="usuarioCreo" > {{ cooperantec.cooperante.fechaActualizacion }}</p>
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
						<shiro:hasPermission name="8020">
							<label class="btn btn-success" ng-click="form.$valid ? cooperantec.guardar() : ''" ng-disabled="form.$invalid">Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="cooperantec.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
