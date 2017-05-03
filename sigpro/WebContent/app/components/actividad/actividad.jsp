<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	  <style>
		.event_title {
			font-size: 14px;
			font-weight: bold;
		}
	</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="actividadController as actividadc" class="maincontainer all_page" id="title">
	<script type="text/ng-template" id="map.html">
        <div class="modal-header">
            <h3 class="modal-title">Mapa de Ubicación</h3>
        </div>
        <div class="modal-body" style="height: 400px;">
            			<ui-gmap-google-map id="mainmap" ng-if="refreshMap" center="map.center" zoom="map.zoom" options="map.options" events="map.events"  >
							<ui-gmap-marker idkey="1" coords="posicion"></ui-gmap-marker>
						</ui-gmap-google-map>
		</div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
        </div>
    </script>

	    <script type="text/ng-template" id="buscarActividadTipo.jsp">
    		<%@ include file="/app/components/actividad/buscarActividadTipo.jsp"%>
  	    </script>



  	    <shiro:lacksPermission name="1010">

			<p ng-init="actividadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		    <div class="panel-heading"><h3>Actividades</h3></div>
		</div>
		<h3><small>{{ actividadc.objetoTipoNombre }} {{ actividadc.objetoNombre }}</small></h3>
		<div class="row" align="center" ng-hide="actividadc.mostraringreso">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="1040">
			    <label class="btn btn-primary" ng-click="actividadc.nuevo()" uib-tooltip="Nueva">
			    <span class="glyphicon glyphicon-plus"></span> Nueva</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="1010">
			    <label class="btn btn-primary" ng-click="actividadc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="1030">
			    <label class="btn btn-danger" ng-click="actividadc.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
    		<shiro:hasPermission name="1010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="actividadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="actividadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!actividadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  actividadc.totalActividades + (actividadc.totalActividades == 1 ? " Actividad" : " Actividades" ) }}</div>
				<ul uib-pagination total-items="actividadc.totalActividades"
						ng-model="actividadc.paginaActual"
						max-size="actividadc.numeroMaximoPaginas"
						items-per-page="actividadc.elementosPorPagina"
						first-text="Primera"
						last-text="Última"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="actividadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="actividadc.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!actividadc.esnuevo"><small>Nueva actividad</small></h2>
				<h2 ng-hide="actividadc.esnuevo"><small>Edición de actividad</small></h2>
			</div>
			
			<div class="operation_buttons">
				<div class="btn-group" ng-hide="actividadc.esnuevo">
					<label class="btn btn-default" ng-click="actividadc.irARiesgos(actividadc.actividad.id)" uib-tooltip="Riesgos" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-warning-sign"></span></label>
				</div>
				<div class="btn-group" style="float: right;">
					<shiro:hasPermission name="24020">
						<label class="btn btn-success" ng-click="form.$valid ? actividadc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
					<label class="btn btn-primary" ng-click="actividadc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				</div>
			</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label">ID {{actividadc.actividad.id }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
    						<div class="form-group">
							   <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="actividadc.actividad.nombre" value="{{actividadc.actividad.nombre}}"  onblur="this.setAttribute('value', this.value);" ng-required="true" >
							   <label class="floating-label">* Nombre</label>
							</div>
						</div>
						<div class="form-group-row row" >
							<div class="form-group col-sm-2" >
							       <input type="number" class="inputText" ng-model="actividadc.actividad.programa" value="{{actividadc.actividad.programa}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center" />
							       <label for="iprog" class="floating-label">Programa</label>
							</div>
							<div class="form-group col-sm-2" >
							  <input type="number" class="inputText" ng-model="actividadc.actividad.subprograma" value="{{actividadc.actividad.subprograma}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
							  <label for="isubprog" class="floating-label">Subprograma</label>
							</div>
							<div class="form-group col-sm-2" >
							  <input type="number" class="inputText" ng-model="actividadc.actividad.proyecto" value="{{actividadc.actividad.proyecto}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
							  <label for="iproy_" class="floating-label">Préstamo</label>
							</div>
							<div class="form-group col-sm-2" >
							  <input type="number" class="inputText" ng-model="actividadc.actividad.actividad" value="{{actividadc.actividad.actividad}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
							  <label for="iobra" class="floating-label">Actividad</label>
							</div>
							<div class="form-group col-sm-2" >
							  <input type="number" class="inputText" ng-model="actividadc.actividad.obra" value="{{actividadc.actividad.obra}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
							  <label for="iobra" class="floating-label">Obra</label>
							</div>
							 <div class="form-group col-sm-2">
							  <input type="number" class="inputText" ng-model="actividadc.actividad.fuente" value="{{actividadc.actividad.fuente}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
							  <label for="fuente" class="floating-label">Fuente</label>
							</div>
						</div>
						<div class="form-group" >
						    <input type="text" class="inputText" id="iactividadtipo" name="iactividadtipo" ng-model="actividadc.actividad.actividadtiponombre" value="{{actividadc.actividad.actividadtiponombre}}" 
							ng-click="actividadc.buscarActividadTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
							<span class="label-icon" ng-click="actividadc.buscarActividadTipo()"><i class="glyphicon glyphicon-search"></i></span>
							<label for="campo3" class="floating-label">* Tipo de Actividad</label>
						</div>
						
						
						<div class="form-group" >
						    <input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="actividadc.coordenadas" value="{{actividadc.coordenadas}}" 
								            		ng-click="actividadc.open(actividadc.actividad.latitud, actividadc.actividad.longitud); " onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="false"/>
							<span class="label-icon" ng-click="actividadc.open(actividadc.actividad.latitud, actividadc.actividad.longitud); "><i class="glyphicon glyphicon-map-marker"></i></span>
							<label for="campo3" class="floating-label">Coordenadas</label>
						</div>
						
						
						<div class="form-group">
						   <input type="text" name="inombre"  class="inputText" id="idescripcion" 
						     ng-model="actividadc.actividad.descripcion" value="{{actividadc.actividad.descripcion}}"   
						     onblur="this.setAttribute('value', this.value);" >
						   <label class="floating-label">Descripción</label>
						</div>
						
						
						<div class="row">
							
							<div class="col-sm-6">
								
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="actividadc.actividad.fechaInicio" is-open="actividadc.fi_abierto"
								            datepicker-options="actividadc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="actividadc.actualizarfechafin()" ng-required="true"  ng-click="actividadc.abrirPopupFecha(1000)"
								            value="{{actividadc.actividad.fechaInicio}}" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" ng-click="actividadc.abrirPopupFecha(1000)">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">*Fecha de Inicio</label>
								</div>
							</div>
							
							<div class="col-sm-6">
							
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="actividadc.actividad.fechaFin" is-open="actividadc.ff_abierto"
								            datepicker-options="actividadc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="actividadc.actualizarfechafin()" ng-required="true"  ng-click="actividadc.abrirPopupFecha(1001)"
								            value="{{actividadc.actividad.fechaFin}}" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" ng-click="actividadc.abrirPopupFecha(1001)">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
								</div>
							</div>
						</div>
											
						<div class="form-group">
						   <input type="number" name="iavance"  class="inputText" id="inombre" 
						     ng-model="actividadc.actividad.porcentajeavance" value="{{actividadc.actividad.porcentajeavance}}"   
						     onblur="this.setAttribute('value', this.value);"  min="0" max="100" >
						   <label class="floating-label">Avance %</label>
						</div>
						
						<div ng-repeat="campo in actividadc.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									value="{{ccampo.valor}}" onblur="this.setAttribute('value', this.value);"/>
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
				<br/>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioCreo">Usuario que creo</label>
				  					<p>{{ actividadc.actividad.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label  class="label-form"  for="fechaCreacion">Fecha de creación</label>
				  					<p>{{ actividadc.actividad.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Usuario que actualizo</label>
				  					<p>{{ actividadc.actividad.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha de actualizacion</label>
				  					<p>{{ actividadc.actividad.fechaActualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>

				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			
			
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? actividadc.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="actividadc.irATabla()" title="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>