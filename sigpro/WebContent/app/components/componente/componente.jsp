<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="componenteController as componentec" ng-class="componentec.esTreeview ? 'maincontainer_treeview all_page' : 'maincontainer all_page'" id="title">
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
			<button class="btn btn-danger" type="button" ng-click="borrar()">Quitar coordenadas</button>
        </div>
    </script>
    
	    <script type="text/ng-template" id="buscarPorComponente.jsp">
    		<%@ include file="/app/components/componente/buscarPorComponente.jsp"%>
  	    </script>
  	    <script type="text/ng-template" id="buscarAcumulacionCosto.jsp">
    		<%@ include file="/app/components/componente/buscarAcumulacionCosto.jsp"%>
  	    </script>
  	    
  	    <script type="text/ng-template" id="pago_planificado.jsp">
    		<%@ include file="/app/components/pago_planificado/pago_planificado.jsp"%>
  	    </script>
  	    
  	    <shiro:lacksPermission name="5010">
			<span ng-init="componentec.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		
		<div class="panel panel-default" ng-if="!componentec.esTreeview">
			<div class="panel-heading"><h3>Componentes</h3></div>
		</div>
		<div class="subtitulo" ng-if="!componentec.esTreeview">
			{{ componentec.objetoTipoNombre }} {{ componentec.proyectoNombre }}
		</div>
		
		<div  align="center" ng-hide="componentec.mostraringreso" ng-if="!componentec.esTreeview">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="5040">
			    <label class="btn btn-primary" ng-disabled="componentec.congelado==1" ng-click="!componentec.esDeSigade ? componentec.congelado != 1 ? componentec.nuevo() : '' : ''" uib-tooltip="Nuevo" ng-disabled = "componentec.esDeSigade">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="5020">
			    <label class="btn btn-primary" ng-click="componentec.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="5030">
			    <label class="btn btn-danger" ng-disabled="componentec.congelado==1" ng-click="!componentec.esDeSigade ?  componentec.congelado != 1 ? componentec.borrar() : '' : ''" uib-tooltip="Borrar" ng-disabled = "componentec.esDeSigade">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
			<br><br>
				<div class="col-sm-12" ng-if="componentec.sobrepaso != null && componentec.sobrepaso == true">
					<div class="alert alert-danger" style="text-align: center;">La planificación sobrepasa la asignación presupuestaria</div>
				</div>
			<br>
    		<shiro:hasPermission name="5010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="componentec.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="componentec.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!componentec.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  componentec.totalComponentes + (componentec.totalComponentes == 1 ? " Componente" : " Componentes" ) }}</div>
				<ul uib-pagination total-items="componentec.totalComponentes"
						ng-model="componentec.paginaActual"
						max-size="componentec.numeroMaximoPaginas"
						items-per-page="componentec.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="componentec.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="componentec.mostraringreso || componentec.esTreeview">
			<div class="page-header">
				<h2 ng-if="componentec.esnuevo"><small>Nuevo componente</small></h2>
				<h2 ng-if="!componentec.esnuevo"><small>Edición de componente</small></h2>
			</div>
			
    		<div class="operation_buttons">
    		  <div class="btn-group" ng-hide="componentec.esnuevo" ng-if="!componentec.esTreeview">
				<label class="btn btn-default" ng-click="componentec.botones ? componentec.irASubComponente(componentec.componente.id) : ''" uib-tooltip="Subcomponentes" tooltip-placement="bottom" ng-disabled="!componentec.botones ">
				<span class="glyphicon glyphicon-equalizer"></span></label>
				<label class="btn btn-default" ng-click="componentec.botones ? componentec.irAProductos(componentec.componente.id) : ''" uib-tooltip="Productos" tooltip-placement="bottom" ng-disabled="!componentec.botones">
				<span class="glyphicon glyphicon-certificate"></span></label>
				<label class="btn btn-default" ng-click="componentec.botones ? componentec.irAActividades(componentec.componente.id) : ''" uib-tooltip="Actividades" tooltip-placement="bottom" ng-disabled="!componentec.botones">
				<span class="glyphicon glyphicon-time"></span></label>
				<label class="btn btn-default" ng-click="componentec.verHistoria()" uib-tooltip="Ver Historia">
				<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
				<label class="btn btn-default" ng-click="componentec.componente.acumulacionCostoId == 2 ? componentec.agregarPagos() : ''"
					 uib-tooltip="Pagos planificados" tooltip-placement="left" ng-disabled = "componentec.componentec.acumulacionCostoId != 2">
					<span class="glyphicon glyphicon-piggy-bank"></span></label>
		      </div>
		      <div ng-if="componentec.esTreeview">
			      <div class="btn-group">
			      	<label class="btn btn-default" ng-click="componentec.verHistoria()" uib-tooltip="Ver Historia">
					<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
					<label class="btn btn-default" ng-click="componentec.componente.acumulacionCostoId == 2 ? componentec.agregarPagos() : ''" uib-tooltip="Pagos planificados"
							ng-disabled = "componentec.componente.acumulacionCostoId != 2" >
							<span class="glyphicon glyphicon-piggy-bank"></span></label>
				  </div>
		      </div>
			  <div class="btn-group" style="float: right;">
			    <shiro:hasPermission name="5020">
			      <label class="btn btn-success" ng-click="componentec.mForm.$valid && componentec.botones   ? componentec.guardar() : ''" ng-disabled="!componentec.mForm.$valid || !componentec.botones" uib-tooltip="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label ng-if="!componentec.esTreeview" class="btn btn-primary" ng-click="componentec.botones ? componentec.irATabla() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!componentec.botones">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			    <label ng-if="componentec.esTreeview && !componentec.componente.esDeSigade" class="btn btn-danger" ng-click=" componentec.botones && componentec.componente.id>0 ? componentec.t_borrar() : ''" ng-disabled="!(componentec.componente.id>0) || !componentec.botones" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </div>
			</div>
			<div class="col-sm-12" ng-if="componentec.componente.esDeSigade">
				<div class="componente_sigade">Componente SIGADE</div>
			</div>
			
			<br><br>
				<div class="col-sm-12" ng-if="componentec.sobrepaso != null && componentec.sobrepaso == true">
					<div class="alert alert-danger" style="text-align: center;">La planificación sobrepasa la asignación presupuestaria</div>
				</div>
			<br>
			<div class="col-sm-12">
				<form name="componentec.mForm">
					<uib-tabset active="componentec.active">
					<uib-tab index="0" heading="Componente" >
						<div class="form-group">
						  <label for="id" class="floating-label id_class">ID {{ componentec.componente.id }}</label>
						  <br/><br/>
						</div>
						
						<div class="form-group">
						   <input type="text" name="nombre" class="inputText" id="nombre" 
						     ng-model="componentec.componente.nombre" ng-value="componentec.componente.nombre" 
						     ng-readonly="componentec.congelado"  
						     onblur="this.setAttribute('value', this.value);" ng-required="true" ng-readonly="componentec.componente.esDeSigade">
						   <label class="floating-label">* Nombre</label>
						</div>
						
						<div class="form-group-row row" >
							<div style="width: 100%">
								<table style="width: 100%">
									<tr>
										<td style="width: 14%; padding-right:5px;">
											<input name="programa" type="number" class="inputText" ng-model="componentec.componente.programa" ng-value="componentec.componente.programa" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" />
							       			<label for="programa" class="floating-label">Programa</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="componentec.componente.subprograma" ng-value="componentec.componente.subprograma" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="isubprog" class="floating-label">Subprograma</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="componentec.componente.proyecto_" ng-value="componentec.componente.proyecto_" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="iproy_" class="floating-label">Proyecto</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="componentec.componente.actividad" ng-value="componentec.componente.actividad" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
								  			<label for="iobra" class="floating-label">Actividad</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="componentec.componente.obra" ng-value="componentec.componente.obra" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							 				<label for="iobra" class="floating-label">Obra</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="componentec.componente.renglon" ng-value="componentec.componente.renglon" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="fuente" class="floating-label">Renglón</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="componentec.componente.ubicacionGeografica" ng-value="componentec.componente.ubicacionGeografica" 
											ng-readonly="componentec.congelado"
											onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="fuente" class="floating-label">Geográfico</label>
										</td>
									</tr>
								</table>
							</div>
						</div>
						
						<div class="form-group" ng-hide="true">
						   <input type="number" name="snip"  class="inputText" id="snip" 
						     ng-model="componentec.componente.snip" ng-value="componentec.componente.snip" 
						     ng-readonly="componentec.congelado"  
						     onblur="this.setAttribute('value', this.value);" ng-required="false" >
						   <label class="floating-label">SNIP</label>
						</div>
						
						<div class="form-group" ng-show="componentec.unidadejecutoranombre.length>0"  >
			            	<input type="text" class="inputText" id="iunie" name="iunie" ng-model="componentec.entidadnombre" ng-readonly="true"  
			            	 ng-value="componentec.entidadnombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
						</div>
						
						<div class="form-group" ng-hide="true">
				            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="componentec.unidadejecutoranombre" ng-readonly="true" 
				            	ng-click="componentec.prestamoId != null ? '' : componentec.buscarUnidadEjecutora()" ng-value="componentec.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="componentec.prestamoId != null ? '' : componentec.buscarUnidadEjecutora()" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				          	<label for="campo3" class="floating-label">Unidad Ejecutora</label>
						</div>
						<div class="form-group">
				            <input type="text" class="inputText" id="icomptipo" name="icomptipo" ng-model="componentec.componentetiponombre" ng-readonly="true" ng-required="true" 
				            	ng-click="componentec.congelado != 1 ? componentec.buscarComponenteTipo() : ''" ng-value="componentec.componentetiponombre" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="componentec.congelado != 1 ? componentec.buscarComponenteTipo() : ''" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				          	<label for="campo3" class="floating-label">* Tipo Componente</label>
						</div>
						<div class="form-group" >
						    <input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="componentec.coordenadas" ng-value="componentec.coordenadas" 
							ng-click="componentec.congelado != 1 ? componentec.open(componentec.componente.latitud, componentec.componente.longitud) : ''; " onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="false"/>
							<span class="label-icon" ng-click="componentec.congelado != 1 ? componentec.open(componentec.componente.latitud, componentec.componente.longitud) : ''; " tabindex="-1"><i class="glyphicon glyphicon-map-marker"></i></span>
							<label for="campo3" class="floating-label">Coordenadas</label>
						</div>
						
						<div class="form-group" >
					       <input type="text" class="inputText" ng-model="componentec.componente.fuentePrestamo" ng-value="componentec.componente.fuentePrestamo" ui-number-mask="2"
					        onblur="this.setAttribute('value', this.value);" style="text-align: left" 
					       ng-readonly="true" />
					       <label for="iprog" class="floating-label">Total de Fuente Prestamo Asignada</label>
						</div>
						
						<div class="form-group" >
					       <input type="text" class="inputText" ng-model="componentec.componente.fuenteDonacion" ng-value="componentec.componente.fuenteDonacion" ui-number-mask="2"
					        onblur="this.setAttribute('value', this.value);" style="text-align: left" 
					       ng-readonly="true" />
					       <label for="iprog" class="floating-label">Total de Fuente Donación Asignada</label>
						</div>
						
						<div class="form-group" >
					       <input type="text" class="inputText" ng-model="componentec.componente.fuenteNacional" ng-value="componentec.componente.fuenteNacional" ui-number-mask="2"
					        onblur="this.setAttribute('value', this.value);" style="text-align: left" 
					       ng-readonly="true" />
					       <label for="iprog" class="floating-label">Total de Fuente Nacional Asignada</label>
						</div>
						
						<div class="form-group" >
					       <input type="text" class="inputText" ng-model="componentec.componente.costo" ng-value="componentec.componente.costo" ui-number-mask="2"
					       ng-required="componentec.componente.acumulacionCostoId > 0" onblur="this.setAttribute('value', this.value);" style="text-align: left" 
					       ng-change="componentec.validarAsignado();"
					       ng-readonly="componentec.componente.tieneHijos || componentec.congelado == 1 || componentec.bloquearCosto" />
					       <label for="iprog" class="floating-label">{{componentec.componente.acumulacionCostoId > 0 ? "* Monto Planificado" : "Monto Planificado"}}</label>
						</div>
						
						<div class="form-group" >
			        		<input type="text" class="inputText" ng-model="componentec.asignado" ng-value="componentec.asignado" ui-number-mask="2"
				       		onblur="this.setAttribute('value', this.value);" style="text-align: left" 
				       		ng-readonly="true"/>
				       		<label for="iprog" class="floating-label">Presupuesto Asignado (Año Fiscal)</label>
						</div>
						
						<div class="form-group">
		            		<div id="acumulacionCosto" angucomplete-alt placeholder="" pause="100" selected-object="componentec.cambioAcumulacionCosto"
		            		disable-input="componentec.componente.tieneHijos || componentec.congelado"
							  local-data="componentec.acumulacionesCosto" search-fields="nombre" title-field="nombre" field-required="componentec.componente.costo!=null && componentec.componente.costo>0" 
						  field-label="{{componentec.componente.costo!=null && componentec.componente.costo>0 ? '* ':''}}Tipo de Acumulación Monto Planificado"
							  minlength="1" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight"
							  initial-value="componentec.componente.acumulacionCostoNombre" focus-out="componentec.blurCategoria()" input-name="acumulacionCosto"></div>
						</div>
						
						<div class = "row">
							<div class="col-sm-6">
								<div class="form-group">
								   <input class="inputText"  type="number"
								     ng-model="componentec.componente.duracion" ng-value="componentec.componente.duracion"   
								     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
								     ng-readonly="componentec.componente.tieneHijos != true ? (componentec.duracionDimension.value != 0 ? false : true) : componentec.componente.tieneHijos"
								     ng-change="componentec.componente.fechaInicio != null && componentec.duracionDimension.value != 0 ? componentec.cambioDuracion(componentec.duracionDimension) : ''">
								   <label class="floating-label">* Duración</label>
								</div>	
							</div>
						
							<div class="col-sm-6">
								<div class="form-group">
									<select class="inputText" ng-model="componentec.duracionDimension"
										ng-options="dim as dim.nombre for dim in componentec.dimensiones track by dim.value"
										 ng-required="true" ng-readonly="componentec.componente.tieneHijos || componentec.congelado == 1">
									</select>
									<label for="nombre" class="floating-label">* Dimension</label>
								</div>
							</div>
							
							
							<div class="col-sm-6">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}" alt-input-formats="{{componentec.altformatofecha}}"
								  			ng-model="componentec.componente.fechaInicio" is-open="componentec.fi_abierto"
								            datepicker-options="componentec.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="componentec.cambioDuracion(componentec.duracionDimension);" ng-required="true"
								            ng-value="componentec.componente.fechaInicio" onblur="this.setAttribute('value', this.value);" ng-readonly="componentec.componente.tieneHijos">
								            <span class="label-icon" ng-click="componentec.componente.tieneHijos != true ? componentec.abrirPopupFecha(1000) : ''" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
								</div>
							</div>
						
							<div class="col-sm-6">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}" alt-input-formats="{{componentec.altformatofecha}}"
								  			ng-model="componentec.componente.fechaFin" is-open="componentec.ff_abierto"
								            datepicker-options="componentec.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true"
								            ng-readonly="true"
								            ng-value="componentec.componente.fechaFin" onblur="this.setAttribute('value', this.value);"
								            ng-readonly="true"/>
								            <span class="label-icon" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
								</div>
							</div>
							
							<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}" alt-input-formats="{{componentec.altformatofecha}}"
							  			ng-model="componentec.componente.fechaInicioReal"
							            datepicker-options="componentec.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
							            ng-value="componentec.componente.fechaInicioReal" onblur="this.setAttribute('value', this.value);"
						            	readonly="readonly"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label class="floating-label">Fecha de Inicio Real</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}"
							  			ng-model="componentec.componente.fechaFinReal"
							            datepicker-options="componentec.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
							            readonly="readonly" ng-value="componentec.componente.fechaFinReal" onblur="this.setAttribute('value', this.value);"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label class="floating-label">Fecha de Fin Real</label>
							</div>
						</div>						
						</div>
						
						<div class="form-group">
    						<input type="checkbox"  ng-model="componentec.componente.inversionNueva" /> 
    						<label class="floating-label">Es Inversion Nueva</label>   						
						</div>
						
						<div ng-repeat="campo in componentec.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-readonly="componentec.congelado"
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"
									ng-readonly="componentec.congelado"   
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-readonly="componentec.congelado"
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-readonly="componentec.congelado" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{componentec.formatofecha}}" alt-input-formats="{{componentec.altformatofecha}}"
								  			ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="componentec.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="componentec.abrirPopupFecha($index)"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="componentec.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-readonly="componentec.congelado" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														ng-value="number.valor">{{number.label}}</option>
								</select>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
						</div>
						
						<div class="form-group">
						   <textarea class="inputText" rows="4"
						   ng-model="componentec.componente.descripcion" ng-value="componentec.componente.descripcion" 
						   onblur="this.setAttribute('value', this.value);" ng-required="false" ng-readonly="componentec.componente.esDeSigade"></textarea>
						   <label class="floating-label">Descripción</label>
						</div>
						
						<div class="panel panel-default">
							<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6" >
										<div class="form-group" style="text-align: right">
											<label  class="label-form" for="usuarioCreo">Usuario que creo</label>
				    						<p>{{ componentec.componente.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="label-form"  for="fechaCreacion">Fecha de creación</label>
				    						<p>{{ componentec.componente.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label class="label-form"  for="usuarioActualizo">Usuario que actualizo</label>
				    						<p>{{ componentec.componente.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaActualizacion" class="label-form" >Fecha de actualizacion</label>
				    						<p>{{ componentec.componente.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</uib-tab>
					<uib-tab index="2" heading="Riesgos" ng-click="componentec.riesgos=true" >
					<div ng-if="componentec.riesgos !== undefined"><%@include file="/app/components/riesgo/riesgo.jsp" %></div>
				</uib-tab>
				</form>
			</div>
			
			<div class="col-sm-12 operation_buttons" align="right" style="margin-top: 15px;">
				<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/aaaa</div>
				<br/>
				<div class="col-sm-12">
					<div class="btn-group" ng-disabled="!componentec.botones">
						 <shiro:hasPermission name="5020">
						      <label class="btn btn-success" ng-click="componentec.mForm.$valid && componentec.botones ? componentec.guardar() : ''" ng-disabled="!componentec.mForm.$valid || !componentec.botones " uib-tooltip="Guardar" tooltip-placement="top">
						      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						    </shiro:hasPermission>
						    <label ng-if="!componentec.esTreeview" class="btn btn-primary" ng-click="componentec.botones ? componentec.irATabla() :''" ng-disabled="!componentec.botones" uib-tooltip="Ir a Tabla" tooltip-placement="top">
						    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
						    <label ng-if="componentec.esTreeview && !componentec.componente.esDeSigade" class="btn btn-danger" ng-click=" componentec.botones && componentec.componente.id>0  ? componentec.t_borrar() : ''" ng-disabled="!(componentec.componente.id>0) || !componentec.botones || componentec.congelado==1" uib-tooltip="Borrar" tooltip-placement="top">
							<span class="glyphicon glyphicon-trash"></span> Borrar</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
