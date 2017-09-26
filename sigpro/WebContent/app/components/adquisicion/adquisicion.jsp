<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="adquisicionController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="17010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Adquisición de {{ controller.objetoNombre }}</h3></div>
		</div>
		<div class="subtitulo">
			{{controller.objetoNombre}}
		</div>
				
		<div align="center" ng-hide="controller.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="17040">
			       		<label class="btn btn-primary" ng-click="controller.nuevo()" title="Nuevo" click="setFocusToTextBox()">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="17010">
			       		<label class="btn btn-primary" ng-click="controller.editar()" title="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="17030">
			       		<label class="btn btn-danger" ng-click="controller.borrar()" title="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
    			</div>				
    		</div>
    		<shiro:hasPermission name="17010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="controller.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
						<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="controller.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!controller.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  controller.totalAdquisiciones + (controller.totalAdquisiciones == 1 ? " Adquisición" : " Adquisiciones" ) }}</div>
				<ul uib-pagination total-items="controller.totalAdquisiciones" 
						ng-model="controller.paginaActual" 
						max-size="controller.numeroMaximoPaginas" 
						items-per-page="controller.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="controller.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
		</div>
		<div class="row second-main-form" ng-show="controller.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!controller.esnuevo"><small>Nueva adquisicion</small></h2>
			    <h2 ng-hide="controller.esnuevo"><small>Edición de adquisicion</small></h2>
			</div>
		
		<div class="operation_buttons">
			<div class="btn-group"  style="float: right;">
				<shiro:hasPermission name="17020">
		        	<label class="btn btn-success" ng-click="form.$valid ? controller.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-floppy-saved"></span>Guardar</label>
		        </shiro:hasPermission>
		        <label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
			<span class="glyphicon glyphicon-list-alt"></span>Ir a Tabla</label>
   			</div>
    	</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label id_class">ID {{ controller.adquisicion.id  }}</label>
							<br/><br/>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<select class="inputText" ng-model="controller.tipoadquisicionSeleccionado"
										ng-options="tipo as tipo.nombre for tipo in controller.adquisiciontipos track by tipo.id"
										ng-readonly="true"
										ng-required="true">
										<option value="">Seleccione un tipo</option>
									</select>
									<label class="floating-label">* Tipo adquisicion</label>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<select class="inputText" ng-model="controller.tipoadquisicionSeleccionado"
										ng-options="tipo as tipo.nombre for tipo in controller.adquisiciontipos track by tipo.id"
										ng-readonly="true"
										ng-required="true">
										<option value="">Seleccione un tipo</option>
									</select>
									<label class="floating-label">* Categoría de Adquisición</label>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-3">
								<div class="form-group">
		    						<input type="text" class="inputText"  ng-model="controller.adquisicion.unidadMedida"
		    						ng-value="controller.adquisicion.unidadMedida" onblur="this.setAttribute('value', this.value);">
		    						<label  class="floating-label">Unidad medida</label>
								</div>
							</div>
							
							<div class="col-sm-3">
								<div class="form-group">
		    						<input type="number" class="inputText"  ng-model="controller.adquisicion.cantidad"
		    						ng-value="controller.adquisicion.cantidad" min=0 onblur="this.setAttribute('value', this.value);">
		    						<label  class="floating-label">Cantidad</label>
								</div>
							</div>
							
							<div class="col-sm-3">
								<div class="form-group">
		    						<input type="number" class="inputText"  ng-model="controller.adquisicion.costo"
		    						ng-value="controller.adquisicion.costo" min=0 onblur="this.setAttribute('value', this.value);">
		    						<label  class="floating-label">Costo</label>
								</div>
							</div>
							
							<div class="col-sm-3">
								<div class="form-group">
		    						<input type="number" class="inputText"  ng-model="controller.adquisicion.total"
		    						ng-value="controller.adquisicion.total" min=0 onblur="this.setAttribute('value', this.value);">
		    						<label  class="floating-label">Total</label>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<select class="inputText" ng-model="controller.duracionDimension"
										ng-options="dim as dim.nombre for dim in controller.dimensiones track by dim.value"
										 ng-required="true">
									</select>
									<label for="nombre" class="floating-label">* Dimension</label>
								</div>
							</div>
							
							<div class="col-sm-6">
								<div class="form-group">
								   <input class="inputText"  type="number"
								     ng-model="controller.adquisicion.duracion" ng-value="controller.actividad.duracion"   
								     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
								     ng-readonly="controller.duracionDimension.value != 0 ? false : true"
								     ng-change="controller.adquisicion.fechaInicio != null && controller.duracionDimension != '' ? controller.cambioDuracion(controller.duracionDimension) : ''">
								   <label class="floating-label">* Duración</label>
								</div>	
							</div>
						</div>
						<div class="row">
							<div class="col-sm-4">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.adquisicion.fechaInicio" is-open="controller.fi_abierto"
								            datepicker-options="controller.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.actualizarfechafin(); controller.cambioDuracion(controller.duracionDimension);" ng-required="true"  
								            ng-value="controller.adquisicion.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">*Fecha de Inicio</label>
								</div>
							</div>
							
							<div class="col-sm-4">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.adquisicion.fechaFin" is-open="controller.ff_abierto"
								            datepicker-options="controller.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.actualizarfechafin()" ng-required="true"
								            readonly="readonly"
								            ng-value="controller.adquisicion.fechaFin" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" ng-click="controller.abrirPopupFecha(1001)">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
								</div>
							</div>
							
							<div class="col-sm-4">
								<div class="form-group" >
								    <input type="text" class="inputText" id="acumulacionCosto" name="acumulacionCosto" ng-model="controller.adquisicion.acumulacionCostoNombre" ng-value="controller.adquisicion.acumulacionCostoNombre" 
									ng-click="controller.buscarAcumulacionCosto()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="controller.adquisicion.costo != null && controller.adquisicion.costo > 0"/>
									<span class="label-icon" ng-click="controller.buscarAcumulacionCosto()"><i class="glyphicon glyphicon-search"></i></span>
									<label for="campo3" class="floating-label">{{controller.validarRequiredCosto(controller.adquisicion.costo)}} </label>
								</div>	
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="form-group">
		    						<input type="text" class="inputText"  ng-model="controller.adquisicion.observaciones"
		    						ng-value="controller.adquisicion.observaciones" onblur="this.setAttribute('value', this.value);">
		    						<label  class="floating-label">Observaciones</label>
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
										<label class="label-form">Usuario que creo</label> 
										<p class=""> {{ controller.adquisicion.usuarioCreo }}</p>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group" >
										<label class="label-form">Fecha de creación</label>
										<p > {{ controller.adquisicion.fechaCreacion }} </p>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group" style="text-align: right">
										<label class="label-form">Usuario que actualizo</label> 
										<p >{{ controller.adquisicion.usuarioActualizo }} </p>
									</div>	
								</div>
								<div class="col-sm-6">		
									<div class="form-group">
										<label class="label-form">Fecha de actualizacion</label> 
										<p>{{ controller.adquisicion.fechaActualizacion }} </p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="17020">
				        	<label class="btn btn-success" ng-click="form.$valid ? controller.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
