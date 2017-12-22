<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="subcomponenteController as subcomponentec" ng-class="subcomponentec.esTreeview ? 'maincontainer_treeview all_page' : 'maincontainer all_page'" id="title">
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
    
	    <script type="text/ng-template" id="buscarPorSubComponente.jsp">
    		<%@ include file="/app/components/subcomponente/buscarPorSubComponente.jsp"%>
  	    </script>
  	    <script type="text/ng-template" id="buscarAcumulacionCosto.jsp">
    		<%@ include file="/app/components/subcomponente/buscarAcumulacionCosto.jsp"%>
  	    </script>
  	    <script type="text/ng-template" id="pago_planificado.jsp">
    		<%@ include file="/app/components/pago_planificado/pago_planificado.jsp"%>
  	    </script>
  	    <shiro:lacksPermission name="5010">
			<span ng-init="subcomponentec.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		
		<div class="panel panel-default" ng-if="!subcomponentec.esTreeview">
			<div class="panel-heading"><h3>Subcomponentes</h3></div>
		</div>
		<div class="subtitulo" ng-if="!subcomponentec.esTreeview">
			{{ subcomponentec.objetoTipoNombre }} {{ subcomponentec.componenteNombre }}
		</div>
		
		<div  align="center" ng-hide="subcomponentec.mostraringreso" ng-if="!subcomponentec.esTreeview">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="5040">
			    <label class="btn btn-primary" ng-disabled="subcomponentec.congelado" ng-click="subcomponentec.congelado != 1 ? subcomponentec.nuevo() : ''" uib-tooltip="Nuevo">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="5020">
			    <label class="btn btn-primary" ng-click="subcomponentec.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="5030">
			    <label class="btn btn-danger" ng-disabled="subcomponentec.congelado==1" ng-click="subcomponentec.congelado != 1 ? subcomponentec.borrar() : ''" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
			<br><br>
				<div class="col-sm-12" ng-if="subcomponentec.sobrepaso != null && subcomponentec.sobrepaso == true">
					<div class="alert alert-danger" style="text-align: center;">La planificación sobrepasa la asignación presupuestaria</div>
				</div>
			<br>
    		<shiro:hasPermission name="5010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="subcomponentec.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="subcomponentec.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!subcomponentec.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  subcomponentec.totalSubComponentes + (subcomponentec.totalSubComponentes == 1 ? " Subcomponente" : " Subcomponentes" ) }}</div>
				<ul uib-pagination total-items="subcomponentec.totalSubComponentes"
						ng-model="subcomponentec.paginaActual"
						max-size="subcomponentec.numeroMaximoPaginas"
						items-per-page="subcomponentec.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="subcomponentec.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="subcomponentec.mostraringreso || subcomponentec.esTreeview">
			<div class="page-header">
				<h2 ng-if="subcomponentec.esnuevo"><small>Nuevo Subcomponente</small></h2>
				<h2 ng-if="!subcomponentec.esnuevo"><small>Edición de Subcomponente</small></h2>
			</div>
			
    		<div class="operation_buttons">
    		  <div class="btn-group" ng-hide="subcomponentec.esnuevo" ng-if="!subcomponentec.esTreeview">
				<label class="btn btn-default" ng-click="subcomponentec.botones ? subcomponentec.irAProductos(subcomponentec.subcomponente.id) : ''" uib-tooltip="Productos" tooltip-placement="bottom" ng-disabled="!subcomponentec.botones">
				<span class="glyphicon glyphicon-certificate"></span></label>
				<label class="btn btn-default" ng-click="subcomponentec.botones ? subcomponentec.irAActividades(subcomponentec.subcomponente.id) : ''" uib-tooltip="Actividades" tooltip-placement="bottom" ng-disabled="!subcomponentec.botones">
				<span class="glyphicon glyphicon-time"></span></label>
				<label class="btn btn-default" ng-click="subcomponentec.verHistoria()" uib-tooltip="Ver Historia">
				<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
				<label class="btn btn-default" ng-click="subcomponentec.subcomponente.acumulacionCostoId == 2 ? subcomponentec.agregarPagos() : ''"
					 uib-tooltip="Pagos planificados" tooltip-placement="left" ng-disabled = "subcomponentec.subcomponente.acumulacionCostoId != 2">
					<span class="glyphicon glyphicon-piggy-bank"></span></label>
		      </div>
		      <div ng-if="subcomponentec.esTreeview">
			      <div class="btn-group">
			      	<label class="btn btn-default" ng-click="subcomponentec.verHistoria()" uib-tooltip="Ver Historia">
					<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
					<label class="btn btn-default" ng-click="subcomponentec.subcomponente.acumulacionCostoId == 2 ? subcomponentec.agregarPagos() : ''" uib-tooltip="Pagos planificados"
							ng-disabled = "subcomponentec.subcomponente.acumulacionCostoId != 2" >
							<span class="glyphicon glyphicon-piggy-bank"></span></label>
					</div>
		      </div>
			  <div class="btn-group" style="float: right;">
			    <shiro:hasPermission name="5020">
			      <label class="btn btn-success" ng-click="subcomponentec.mForm.$valid && subcomponentec.botones ? subcomponentec.guardar() : ''" ng-disabled="!subcomponentec.mForm.$valid || !subcomponentec.botones" uib-tooltip="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label ng-if="!subcomponentec.esTreeview" class="btn btn-primary" ng-click="subcomponentec.botones ? subcomponentec.irATabla() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!subcomponentec.botones">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			    <label ng-if="subcomponentec.esTreeview && !subcomponentec.subcomponente.esDeSigade" class="btn btn-danger" ng-click=" subcomponentec.botones && subcomponentec.subcomponente.id>0 ? subcomponentec.congelado != 1 ? subcomponentec.t_borrar() : '' : ''" ng-disabled="!(subcomponentec.subcomponente.id>0) || !subcomponentec.botones || subcomponentec.congelado" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </div>
			</div>
			<br><br>
				<div class="col-sm-12" ng-if="subcomponentec.sobrepaso != null && subcomponentec.sobrepaso == true">
					<div class="alert alert-danger" style="text-align: center;">La planificación sobrepasa la asignación presupuestaria</div>
				</div>
			<br>
			<div class="col-sm-12">
				<form name="subcomponentec.mForm">
					<uib-tabset active="subcomponentec.active">
					<uib-tab index="0" heading="Subcomponente" >
						<div class="form-group">
						  <label for="id" class="floating-label id_class">ID {{ subcomponentec.subcomponente.id }}</label>
						  <br/><br/>
						</div>
						
						<div class="form-group">
						   <input type="text" name="nombre" class="inputText" id="nombre" 
						     ng-model="subcomponentec.subcomponente.nombre" ng-value="subcomponentec.subcomponente.nombre"   
						     ng-readonly="subcomponentec.congelado"
						     onblur="this.setAttribute('value', this.value);" ng-required="true" ng-readonly="subcomponentec.subcomponente.esDeSigade">
						   <label class="floating-label">* Nombre</label>
						</div>
						
						<div class="form-group-row row" >
							<div style="width: 100%">
								<table style="width: 100%">
									<tr>
										<td style="width: 14%; padding-right:5px;">
											<input name="programa" type="number" class="inputText" ng-model="subcomponentec.subcomponente.programa" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.programa" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" />
							       			<label for="programa" class="floating-label">Programa</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="subcomponentec.subcomponente.subprograma" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.subprograma" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="isubprog" class="floating-label">Subprograma</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="subcomponentec.subcomponente.proyecto_" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.proyecto_" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="iproy_" class="floating-label">Proyecto</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="subcomponentec.subcomponente.actividad" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.actividad" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
								  			<label for="iobra" class="floating-label">Actividad</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="subcomponentec.subcomponente.obra" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.obra" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							 				<label for="iobra" class="floating-label">Obra</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="subcomponentec.subcomponente.renglon" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.renglon" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="fuente" class="floating-label">Renglón</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="subcomponentec.subcomponente.ubicacionGeografica" 
											ng-readonly="subcomponentec.congelado"
											ng-value="subcomponentec.subcomponente.ubicacionGeografica" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  				<label for="fuente" class="floating-label">Geográfico</label>
										</td>
									</tr>
								</table>
							</div>
						</div>
						
						<div class="form-group">
						   <input type="number" name="snip"  class="inputText" id="snip" 
						     ng-model="subcomponentec.subcomponente.snip" ng-value="subcomponentec.subcomponente.snip" 
						     ng-readonly="subcomponentec.congelado"  
						     onblur="this.setAttribute('value', this.value);" ng-required="false" >
						   <label class="floating-label">SNIP</label>
						</div>
						
						<div class="form-group" ng-show="subcomponentec.unidadejecutoranombre.length>0"  >
			            	<input type="text" class="inputText" id="iunie" name="iunie" ng-model="subcomponentec.entidadnombre" ng-readonly="true"  
			            	 ng-readonly="subcomponentec.congelado"
			            	 ng-value="subcomponentec.entidadnombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
						</div>
						
						<div class="form-group" ng-hide="true">
				            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="subcomponentec.unidadejecutoranombre" ng-readonly="true" 
				            	ng-click="subcomponentec.prestamoId != null ? '' : subcomponentec.buscarUnidadEjecutora()" ng-value="subcomponentec.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="subcomponentec.prestamoId != null ? '' : subcomponentec.buscarUnidadEjecutora()" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				          	<label for="campo3" class="floating-label">Unidad Ejecutora</label>
						</div>
						<div class="form-group">
				            <input type="text" class="inputText" id="icomptipo" name="icomptipo" ng-model="subcomponentec.subcomponentetiponombre" ng-readonly="true" ng-required="true" 
				            	ng-click="subcomponentec.congelado != 1 ? subcomponentec.buscarSubComponenteTipo() : ''" ng-value="subcomponentec.subcomponentetiponombre" onblur="this.setAttribute('value', this.value);"/>
				            <span class="label-icon" ng-click="subcomponentec.congelado != 1 ? subcomponentec.buscarSubComponenteTipo() : ''" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				          	<label for="campo3" class="floating-label">* Tipo Subcomponente</label>
						</div>
						<div class="form-group" >
						    <input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="subcomponentec.coordenadas" ng-value="subcomponentec.coordenadas" 
							ng-click="subcomponentec.congelado != 1 ? subcomponentec.open(subcomponentec.subcomponente.latitud, subcomponentec.subcomponente.longitud) : ''; " onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="false"/>
							<span class="label-icon" ng-click="subcomponentec.congelado != 1 ? subcomponentec.open(subcomponentec.subcomponente.latitud, subcomponentec.subcomponente.longitud) : ''; " tabindex="-1"><i class="glyphicon glyphicon-map-marker"></i></span>
							<label for="campo3" class="floating-label">Coordenadas</label>
						</div>
						
						<div class="form-group" >
					       <input type="text" class="inputText" ng-model="subcomponentec.subcomponente.costo" ng-value="subcomponentec.subcomponente.costo" ui-number-mask="2"
					       ng-required="subcomponentec.subcomponente.acumulacionCostoId > 0" onblur="this.setAttribute('value', this.value);" style="text-align: left"
					       ng-change="subcomponentec.validarAsignado();"
					       ng-readonly="subcomponentec.subcomponente.tieneHijos || subcomponentec.congelado == 1 || subcomponentec.bloquearCosto" />
					       <label for="iprog" class="floating-label">{{subcomponentec.subcomponente.acumulacionCostoId > 0 ? "* Monto Planificado" : "Monto Planificado"}}</label>
						</div>
						
						<div class="form-group" >
			        		<input type="text" class="inputText" ng-model="subcomponentec.asignado" ng-value="subcomponentec.asignado" ui-number-mask="2"
				       		onblur="this.setAttribute('value', this.value);" style="text-align: left" 
				       		ng-readonly="true"/>
				       		<label for="iprog" class="floating-label">Presupuesto Asignado (Año Fiscal)</label>
						</div>
												
						<div class="form-group">
		            		<div id="acumulacionCosto" angucomplete-alt placeholder="" pause="100" selected-object="subcomponentec.cambioAcumulacionCosto"
		            		disable-input="subcomponentec.subcomponente.tieneHijos || subcomponentec.congelado"
							  local-data="subcomponentec.acumulacionesCosto" search-fields="nombre" title-field="nombre" field-required="subcomponentec.subcomponente.costo!=null && subcomponentec.subcomponente.costo>0" 
						  field-label="{{subcomponentec.subcomponente.costo!=null && subcomponentec.subcomponente.costo>0 ? '* ':''}}Tipo de Acumulación Monto Planificado"
							  minlength="1" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight"
							  initial-value="subcomponentec.subcomponente.acumulacionCostoNombre" focus-out="subcomponentec.blurCategoria()" input-name="acumulacionCosto"></div>
						</div>
						
						<div class = "row">
							<div class="col-sm-6">
								<div class="form-group">
								   <input class="inputText"  type="number"
								     ng-model="subcomponentec.subcomponente.duracion" ng-value="subcomponentec.subcomponente.duracion"   
								     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
								     ng-readonly="subcomponentec.subcomponente.tieneHijos != true ? (subcomponentec.duracionDimension.value != 0 ? false : true) : subcomponentec.subcomponente.tieneHijos"
								     ng-change="subcomponentec.subcomponente.fechaInicio != null && subcomponentec.duracionDimension.value != 0 ? subcomponentec.cambioDuracion(subcomponentec.duracionDimension) : ''">
								   <label class="floating-label">* Duración</label>
								</div>	
							</div>
							
							<div class="col-sm-6">
								<div class="form-group">
									<select class="inputText" ng-model="subcomponentec.duracionDimension"
										ng-readonly="subcomponentec.congelado"
										ng-options="dim as dim.nombre for dim in subcomponentec.dimensiones track by dim.value"
										 ng-required="true" ng-readonly="subcomponentec.subcomponente.tieneHijos">
									</select>
									<label for="nombre" class="floating-label">* Dimension</label>
								</div>
							</div>
														
							<div class="col-sm-6">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{subcomponentec.formatofecha}}" alt-input-formats="{{subcomponentec.altformatofecha}}"
								  			ng-model="subcomponentec.subcomponente.fechaInicio" is-open="subcomponentec.fi_abierto"
								  			ng-readonly="subcomponentec.congelado || subcomponentec.subcomponente.tieneHijos"
								            datepicker-options="subcomponentec.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="subcomponentec.cambioDuracion(subcomponentec.duracionDimension);" ng-required="true"
								            ng-value="subcomponentec.subcomponente.fechaInicio" onblur="this.setAttribute('value', this.value);">
								            <span class="label-icon" ng-click="subcomponentec.subcomponente.tieneHijos != true ? subcomponentec.abrirPopupFecha(1000) : ''" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
								</div>
							</div>
						
							<div class="col-sm-6">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{subcomponentec.formatofecha}}" alt-input-formats="{{subcomponentec.altformatofecha}}"
								  			ng-model="subcomponentec.subcomponente.fechaFin" is-open="subcomponentec.ff_abierto"
								            datepicker-options="subcomponentec.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true"
								            ng-readonly="true"
								            ng-value="subcomponentec.subcomponente.fechaFin" onblur="this.setAttribute('value', this.value);"
								            ng-readonly="true"/>
								            <span class="label-icon" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
								</div>
							</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{subcomponentec.formatofecha}}" alt-input-formats="{{subcomponentec.altformatofecha}}"
							  			ng-model="subcomponentec.subcomponente.fechaInicioReal"
							            datepicker-options="subcomponentec.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
							            ng-value="subcomponentec.subcomponente.fechaInicioReal" onblur="this.setAttribute('value', this.value);"
						            	readonly="readonly"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label class="floating-label">Fecha de Inicio Real</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{subcomponentec.formatofecha}}"
							  			ng-model="subcomponentec.subcomponente.fechaFinReal"
							            datepicker-options="subcomponentec.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
							            readonly="readonly" ng-value="subcomponentec.subcomponente.fechaFinReal" onblur="this.setAttribute('value', this.value);"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label class="floating-label">Fecha de Fin Real</label>
							</div>
						</div>						
						</div>
												
						<div class="form-group">
    						<input type="checkbox"  ng-model="subcomponentec.subcomponente.inversionNueva" /> 
    						<label class="floating-label">Es Inversion Nueva</label>   						
						</div>
						
						<div ng-repeat="campo in subcomponentec.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-readonly="subcomponentec.congelado"
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"  
									ng-readonly="subcomponentec.congelado" 
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-readonly="subcomponentec.congelado"
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-readonly="subcomponentec.congelado" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{subcomponentec.formatofecha}}" alt-input-formats="{{subcomponentec.altformatofecha}}"
								  						ng-model="campo.valor" is-open="campo.isOpen"
								  						ng-readonly="subcomponentec.congelado"
														datepicker-options="subcomponentec.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="subcomponentec.congelado != 1 ? subcomponentec.abrirPopupFecha($index) : ''"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="subcomponentec.congelado != 1 ? subcomponentec.abrirPopupFecha($index) : ''">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-readonly="subcomponentec.congelado" ng-model="campo.valor">
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
						   ng-model="subcomponentec.subcomponente.descripcion" ng-value="subcomponentec.subcomponente.descripcion"
						   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
						   <label class="floating-label">Descripción</label>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6" >
										<div class="form-group" style="text-align: right">
											<label  class="label-form" for="usuarioCreo">Usuario que creo</label>
				    						<p>{{ subcomponentec.subcomponente.usuarioCreo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="label-form"  for="fechaCreacion">Fecha de creación</label>
				    						<p>{{ subcomponentec.subcomponente.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label class="label-form"  for="usuarioActualizo">Usuario que actualizo</label>
				    						<p>{{ subcomponentec.subcomponente.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaActualizacion" class="label-form" >Fecha de actualizacion</label>
				    						<p>{{ subcomponentec.subcomponente.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</uib-tab>
					<uib-tab index="2" heading="Riesgos" ng-click="subcomponentec.riesgos=true" >
					<div ng-if="subcomponentec.riesgos !== undefined"><%@include file="/app/components/riesgo/riesgo.jsp" %></div>
				</uib-tab>
				</form>
			</div>
			
			<div class="col-sm-12 operation_buttons" align="right" style="margin-top: 15px;">
				<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/aaaa</div>
				<br/>
				<div class="col-sm-12">
					<div class="btn-group" ng-disabled="!subcomponentec.botones">
						 <shiro:hasPermission name="5020">
						      <label class="btn btn-success" ng-click="subcomponentec.mForm.$valid && subcomponentec.botones ? subcomponentec.guardar() : ''" ng-disabled="!subcomponentec.mForm.$valid || !subcomponentec.botones" uib-tooltip="Guardar" tooltip-placement="top">
						      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						    </shiro:hasPermission>
						    <label ng-if="!subcomponentec.esTreeview" class="btn btn-primary" ng-click="subcomponentec.botones ? subcomponentec.irATabla() :''" ng-disabled="!subcomponentec.botones" uib-tooltip="Ir a Tabla" tooltip-placement="top">
						    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
						    <label ng-if="subcomponentec.esTreeview && !subcomponentec.subcomponente.esDeSigade" class="btn btn-danger" ng-click="subcomponentec.botones && subcomponentec.subcomponente.id>0  ? subcomponentec.congelado != 1 ? subcomponentec.t_borrar() : '' : ''" ng-disabled="!(subcomponentec.subcomponente.id>0) || !subcomponentec.botones || subcomponentec.congelado" uib-tooltip="Borrar" tooltip-placement="top">
							<span class="glyphicon glyphicon-trash"></span> Borrar</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
