<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="hitoController as hitoc" class="maincontainer all_page" id="title">
		<script type="text/ng-template" id="buscarHitoTipo.jsp">
    		<%@ include file="/app/components/hito/buscarHitoTipo.jsp"%>
  	    </script>
  	    <shiro:lacksPermission name="15010">
			<p ng-init="hitoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>	
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Hitos</h3></div>
		</div>
		<div class="subtitulo">
			{{ hitoc.objetoTipoNombre }} {{ hitoc.proyectoNombre }}
		</div>
		
		<div class="row" align="center" ng-hide="hitoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="15040">
			       		<label class="btn btn-primary" ng-click="hitoc.nuevo()">
			       			<span class="glyphicon glyphicon-plus"></span>Nuevo
			       		</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="15010">
			       		<label class="btn btn-primary" ng-click="hitoc.editar()">
			       			<span class="glyphicon glyphicon-pencil"></span>Editar
			       		</label>
			       	</shiro:hasPermission>
			       <shiro:hasPermission name="15030">
			       		<label class="btn btn-danger" ng-click="hitoc.borrar()">
			       		<span class="glyphicon glyphicon-trash"></span>Borrar
			       		</label>
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
				<br>
				<div class="total-rows">
				  Total de {{  hitoc.totalHitos + (hitoc.totalHitos == 1 ? " hito" : " hitos" ) }}
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
		<div class="row second-main-form" ng-show="hitoc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!hitoc.esnuevo"><small>Nuevo hito</small></h2>
				<h2 ng-hide="hitoc.esnuevo"><small>Edición de hito</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="15020">
			        	<label class="btn btn-success" ng-click="form.$valid ? hitoc.guardar(): ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="hitoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form" id="form">
						<div class="form-group" ng-show="!hitoc.esnuevo">
							<label for="id" class="floating-label">ID  {{ hitoc.hito.id }} </label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText"  ng-model="hitoc.hito.nombre" ng-required="true"
    						 ng-value="hitoc.hito.nombre" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							
							<input type="text" class="inputText" uib-datepicker-popup="{{hitoc.formatofecha}}" ng-model="hitoc.fecha" 
	    						is-open="hitoc.popupfecha.abierto" datepicker-options="hitoc.fechaOptions"  close-text="Cerrar" 
	    						alt-input-formats="altInputFormats" current-text="Hoy" clear-text="Borrar" ng-required="true"
	    						ng-click="hitoc.abirpopup()"
	    						ng-value="hitoc.fecha" onblur="this.setAttribute('value', this.value);" ng-readonly="true"/>
	    						
	    						<span class="label-icon" ng-click="hitoc.abirpopup()">
						        	<i class="glyphicon glyphicon-calendar"></i>
						        </span>
						        <label class="floating-label">* Fecha</label>
						        
					        
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="hitoc.hito.descripcion" 
    						ng-value="hitoc.hito.descripcion" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Descripción</label>
						</div>
						<div class="form-group"> 
			            	<input type="text" class="inputText"  ng-model="hitoc.hitotipoNombre" ng-readonly="true" ng-required="true"
			            	ng-click="hitoc.buscarHitoTipo()"
			            	ng-value="hitoc.hitotipoNombre" onblur="this.setAttribute('value', this.value);"/>
			            	<span  class="label-icon"  ng-click="hitoc.buscarHitoTipo()"><i class="glyphicon glyphicon-search"></i></span>
			            	<label class="floating-label" >* Tipo hito</label>
			          	
						</div>
						<div class="form-group" ng-if="hitoc.hitotipoid>0">
							
							<div ng-switch="hitoc.hitodatotipoid" class="form-group">
								<input ng-switch-when="1" type="text"  ng-model="hitoc.hitoresultado" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
								<input ng-switch-when="2" type="number"  numbers-Only ng-model="hitoc.hitoresultado" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
								<input ng-switch-when="3" type="number"   ng-model="hitoc.hitoresultado" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
								<input ng-switch-when="4" type="checkbox"  ng-model="hitoc.hitoresultado"/>
								<p ng-switch-when="5">
									<input type="text"  class="inputText" uib-datepicker-popup="{{hitoc.formatofecha}}" ng-model="hitoc.hitoresultado" is-open="hitoc.popupfecharesultado.abierto"
											datepicker-options="hitoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
											ng-value="hitoc.hitoresultado" onblur="this.setAttribute('value', this.value);"
											ng-click="hitoc.abirpopupreultado()"/>
									<span class="label-icon" ng-click="hitoc.abirpopupreultado()">
										<i class="glyphicon glyphicon-calendar"></i>
									</span>
								</p>
								<label  class="floating-label" >Resultado</label>
							</div>
							
						</div>
						
						<div class="form-group" ng-if="hitoc.hitotipoid>0">
    						<input type="text" class="inputText" ng-model="hitoc.hitoresultadocomentario"
    						ng-value="hitoc.hitoresultadocomentario" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Comentario</label>
						</div>
					<br/>
					<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group" style="text-align: right">
										<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
										<p> {{ hitoc.hito.usuarioCreo }}</p>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group" >
										<label for="fechaCreacion" class="label-form">Fecha de creación</label>
										<p  id="fechaCreacion"> {{ hitoc.hito.fechaCreacion }} </p>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group" style="text-align: right">
										<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
										<p  id="usuarioCreo">{{ hitoc.hito.usuarioActualizo }} </p>
									</div>	
								</div>
								<div class="col-sm-6">		
									<div class="form-group">
										<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
										<p id="usuarioCreo">{{ hitoc.hito.fechaActualizacion }} </p>
									</div>
								</div>
							</div>
						</div>
				</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="15020">
				        	<label class="btn btn-success" ng-click="form.$valid ? hitoc.guardar(): ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="hitoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
