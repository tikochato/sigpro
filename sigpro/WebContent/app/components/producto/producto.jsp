<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div ng-controller="controlProducto as producto" ng-class=" producto.esTreeview ? 'maincontainer_treeview all_page' : 'maincontainer all_page'">
	
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
    
	<script type="text/ng-template" id="buscarPorProducto.jsp">
	    <%@ include file="/app/components/producto/buscarPorProducto.jsp"%>
	</script>
	<shiro:lacksPermission name="21010">
		<span ng-init="producto.redireccionSinPermisos()"></span>
	</shiro:lacksPermission>

	<div class="panel panel-default" ng-if="!producto.esTreeview">
		<div class="panel-heading">
			<h3>Producto</h3>
		</div>
	</div>
		<div class="subtitulo" ng-if="!producto.esTreeview">
			{{ producto.objetoTipoNombre }}: {{ producto.objetoNombre }}
		</div>
	
  
	<div align="center" ng-hide="producto.esForma" ng-if="!producto.esTreeview">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="21040">
					<label class="btn btn-primary" ng-disabled="producto.congelado == 1" ng-click="producto.congelado != 1 ? producto.nuevo() : ''" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="21020">
					<label class="btn btn-primary" ng-click="producto.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="21030">
					<label class="btn btn-danger" ng-disabled="producto.congelado == 1" ng-click="producto.congelado != 1 ? producto.borrar() : ''" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="21010">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="producto.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
								<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="producto.opcionesGrid"
					ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!producto.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  producto.totalElementos + (producto.totalElementos == 1 ? " Producto" : " Productos" ) }}</div>
				<ul uib-pagination total-items="producto.totalElementos"
					ng-model="producto.paginaActual"
					max-size="producto.numeroMaximoPaginas"
					items-per-page="producto.elementosPorPagina"
					first-text="Primero" last-text="Último" next-text="Siguiente"
					previous-text="Anterior" class="pagination-sm"
					boundary-links="true" force-ellipses="true"
					ng-change="producto.cambioPagina()"></ul>
			</div>
		</shiro:hasPermission>

	</div>

	<div ng-show="producto.esForma || producto.esTreeview" class="row second-main-form">
		<div class="page-header">
			<h2 ng-if="producto.esNuevo"><small>Nuevo Producto</small></h2>
			<h2 ng-if="!producto.esNuevo"><small>Edición de Producto</small></h2>
		</div>
		<div class="operation_buttons">
			<div class="btn-group" ng-hide="producto.esNuevo" ng-if="!producto.esTreeview">
				<label class="btn btn-default" ng-click="producto.botones ? producto.irASubproductos() : ''" uib-tooltip="Subproductos" tooltip-placement="bottom" ng-disabled="!producto.botones">
				<span class="glyphicon glyphicon-link"></span></label>
				<label class="btn btn-default" ng-click="producto.botones ? producto.irAActividades() : ''" uib-tooltip="Actividades" tooltip-placement="bottom" ng-disabled="!producto.botones">
				<span class="glyphicon glyphicon-time"></span></label>
				<label class="btn btn-default" ng-click="producto.verHistoria()" uib-tooltip="Ver Historia">
				<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
			</div>
			<div ng-if="producto.esTreeview">
		      	<label class="btn btn-default" ng-click="producto.verHistoria()" uib-tooltip="Ver Historia">
				<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
		     </div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="producto.mForm.$valid && producto.botones ? producto.guardar() : ''" ng-disabled="!producto.mForm.$valid || !producto.botones" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label ng-if="!producto.esTreeview" class="btn btn-primary" ng-click="producto.botones ? producto.cancelar() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!producto.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="producto.esTreeview" class="btn btn-danger" ng-click=" producto.botones ? producto.congelado != 1 ? producto.t_borrar() : '' : ''" ng-disabled="!(producto.producto.id>0) || !producto.botones || producto.congelado == 1" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" style="margin-top: 10px;">
		<form name="producto.mForm" class="css-form">
		<uib-tabset active="producto.activeTab">
    	<uib-tab index="0" name="tproducto" heading="Producto">
		<div>
		<div class="col-sm-12">
					<div class="form-group">
						<label id="campo0" name="campo0" class="floating-label id_class">ID {{ producto.producto.id }}</label>
						<br/><br/>
					</div>
								
					<div class="form-group">
						<input type="text" class="inputText" ng-model="producto.producto.nombre" ng-value="producto.producto.nombre" onblur="this.setAttribute('value', this.value);" 
						ng-readonly="producto.congelado"
						ng-required="true" id="nombre"/>						
						<label class="floating-label">* Nombre</label> 
					</div>
					
					<div class="form-group"  >
						<input type="number"  class="inputText" ng-model="producto.producto.snip" ng-value="producto.producto.snip" onblur="this.setAttribute('value', this.value);"
						ng-readonly="producto.congelado">
						<label for="isnip" class="floating-label">SNIP</label>
					</div>
				
					<div class="form-group-row row" >
						<div style="width: 100%">
							<table style="width: 100%">
								<tr>
									<td style="width: 14%; padding-right:5px;">
										<input name="programa" type="number" class="inputText" ng-model="producto.producto.programa" ng-value="producto.producto.programa" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" />
						       			<label for="programa" class="floating-label">Programa</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.subprograma" ng-value="producto.producto.subprograma" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="isubprog" class="floating-label">Subprograma</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.proyecto_" ng-value="producto.producto.proyecto_" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="iproy_" class="floating-label">Proyecto</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.actividad" ng-value="producto.producto.actividad" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  			<label for="iobra" class="floating-label">Actividad</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.obra" ng-value="producto.producto.obra" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						 				<label for="iobra" class="floating-label">Obra</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.renglon" ng-value="producto.producto.renglon" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="fuente" class="floating-label">Renglon</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.ubicacionGeografica" ng-value="producto.producto.ubicacionGeografica" 
										ng-readonly="producto.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="fuente" class="floating-label">Geográfico</label>
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="form-group" >
			          <input type="text" class="inputText" ng-model="producto.tipoNombre" ng-value="producto.tipoNombre" 
			          	ng-click="producto.congelado != 1 ? producto.buscarTipo() : ''" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
			           <span class="label-icon" ng-click="producto.congelado != 1 ? producto.buscarTipo() : ''" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
			          <label for="campo3" class="floating-label">* Tipo</label>
			        </div>
			        
			        <div class="form-group" ng-show="producto.unidadEjecutoraNombre.length>0"  >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="producto.entidadnombre" ng-readonly="true"  
			            	 ng-value="producto.entidadnombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
					</div>
			        
			        <div class="form-group">
			            <input type="text" class="inputText" ng-model="producto.unidadEjecutoraNombre" ng-value="producto.unidadEjecutoraNombre" 
			            	ng-click="producto.prestamoId != null ? '' : producto.buscarUnidadEjecutora()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" />
			            <span class="label-icon" ng-click="producto.prestamoId != null ? '' : producto.buscarUnidadEjecutora()" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
			          <label for="campo5" class="floating-label">Unidad Ejecutora</label>
			        </div>
			        
			        <div class="form-group">
			          	<input type="text" class="inputText" ng-model="producto.coordenadas" ng-value="producto.coordenadas"
			          		ng-readonly="producto.congelado" 
			          		onblur="this.setAttribute('value', this.value);"ng-click="producto.congelado != 1 ? producto.open(producto.producto.latitud, producto.producto.longitud) : ''; " ng-readonly="true" />
			            <span class="label-icon" ng-click="producto.congelado != 1 ? producto.open(producto.producto.latitud, producto.producto.longitud) : ''; " tabindex="-1"><i class="glyphicon glyphicon-map-marker"></i></span>
			          	<label  class="floating-label">Coordenadas</label>
					</div>
						
			        <div class="form-group" >
				       <input type="text" class="inputText" ng-model="producto.producto.costo" ng-value="producto.producto.costo" onblur="this.setAttribute('value', this.value);" style="text-align: left"
				       		ng-required="producto.producto.acumulacionCostoNombre != null" ng-readonly="producto.congelado"
							ui-number-mask="2" ng-readonly="producto.producto.tieneHijos"/>
				       <label for="iprog" class="floating-label">{{producto.producto.acumulacionCostoNombre  != null ?"* Monto Planificado":"Monto Planificado"}}</label>
					</div>
						
				    <div class="form-group">
	            		<div id="acumulacionCosto" angucomplete-alt placeholder="" pause="100" selected-object="producto.cambioAcumulacionCosto"
	            		  disable-input="producto.congelado"
						  local-data="producto.acumulacionesCosto" search-fields="nombre" title-field="nombre" field-required="producto.producto.costo!=null && producto.producto.costo>0" 
						  field-label="{{producto.producto.costo!=null && producto.producto.costo>0 ? '* ':''}}Tipo de Acumulación Monto Planificado"
						  minlength="1" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight"
						  initial-value="producto.producto.acumulacionCostoNombre" focus-out="producto.blurCategoria()" input-name="acumulacionCosto"></div>
					</div>
					
					<div class = "row">	
						<div class="col-sm-6">
							<div class="form-group">
							   <input class="inputText"  type="number"
							     ng-model="producto.producto.duracion" ng-value="producto.producto.duracion"   
							     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
							     ng-readonly="producto.congelado"
							     ng-change="producto.producto.fechaInicio != null && producto.duracionDimension.value != 0 ? producto.cambioDuracion(producto.duracionDimension) : ''"  
							     ng-readonly="producto.producto.tieneHijos">
							   <label class="floating-label">* Duración</label>
							</div>	
						</div>
							
						<div class="col-sm-6">
							<div class="form-group">
								<select class="inputText" ng-model="producto.duracionDimension"
									ng-readonly="producto.congelado"
									ng-options="dim as dim.nombre for dim in producto.dimensiones track by dim.value"
									 ng-required="true">
								</select>
								<label for="nombre" class="floating-label">* Dimension</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" alt-input-formats="{{producto.altformatofecha}}"
										min={{producto.fechaInicioPadre}} ng-model="producto.producto.fechaInicio" is-open="producto.fi_abierto" 
							            datepicker-options="producto.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="producto.cambioDuracion(producto.duracionDimension);" ng-required="true"  
							            ng-value="producto.producto.fechaInicio" onblur="this.setAttribute('value', this.value);" 
							            ng-readonly="producto.producto.tieneHijos || producto.congelado"/>
							            <span class="label-icon" ng-click="producto.producto.tieneHijos!=true ? producto.abrirPopupFecha(1000) : ''" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" alt-input-formats="{{producto.altformatofecha}}"
										ng-model="producto.producto.fechaFin" is-open="producto.ff_abierto"
							            datepicker-options="producto.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true" ng-click=""
							            ng-value="producto.producto.fechaFin" onblur="this.setAttribute('value', this.value);"
							            ng-readonly="true"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" alt-input-formats="{{producto.altformatofecha}}"
							  			ng-model="producto.producto.fechaInicioReal"
							            datepicker-options="producto.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
							            ng-value="producto.producto.fechaInicioReal" onblur="this.setAttribute('value', this.value);"
						            	readonly="readonly"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label class="floating-label">Fecha de Inicio Real</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{producto.formatofecha}}"
							  			ng-model="producto.producto.fechaFinReal"
							            datepicker-options="producto.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
							            readonly="readonly" ng-value="producto.producto.fechaFinReal" onblur="this.setAttribute('value', this.value);"/>
							            <span class="label-icon" tabindex="-1">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label class="floating-label">Fecha de Fin Real</label>
							</div>
						</div>
					</div>
					<div class="form-group" ng-repeat="campo in producto.camposdinamicos">
						<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-readonly="producto.congelado"
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"  
									ng-readonly="producto.congelado" 
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-readonly="producto.congelado"
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" ng-readonly="producto.congelado" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" alt-input-formats="{{producto.altformatofecha}}"
														ng-model="campo.valor" is-open="campo.isOpen" ng-readonly="producto.congelado"
														datepicker-options="producto.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="producto.abrirPopupFecha($index)"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="producto.abrirPopupFecha($index)" tabindex="-1">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor" ng-readonly="producto.congelado">
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
					   ng-model="producto.producto.descripcion" ng-value="producto.producto.descripcion"  
					   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
					   <label class="floating-label">Descripción</label>
					</div>
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label>
				  					<p class="">{{ producto.producto.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
				  					<p class="">{{ producto.producto.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label>
				  					<p class="">{{ producto.producto.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label>
				  					<p class="">{{ producto.producto.fechaactualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		</uib-tab>
		<uib-tab index="1" ng-click="producto.metasActivo()" heading="Metas">
			<shiro:lacksPermission name="17010">
				<span ng-init="producto.redireccionSinPermisos()"></span>
			</shiro:lacksPermission>
			<div ng-if="producto.metasCargadas">
				<%@include file="/app/components/meta/meta.jsp" %>
			</div>
    	</uib-tab>
    	<uib-tab index="2" heading="Adquisición" ng-click="producto.adquisicionesActivo()">
    		<div ng-if="producto.adquisicionesCargadas">
				<%@include file="/app/components/adquisicion/adquisicion.jsp" %>
			</div>
	    </uib-tab>
	    <uib-tab index="3" heading="Riesgos" ng-click="producto.riesgosActivo()" >
			<div ng-if="producto.riesgos"><%@include file="/app/components/riesgo/riesgo.jsp" %></div>
		</uib-tab>
	  </uib-tabset>
	  </form>
	</div>	
		<div class="col-sm-12 operation_buttons" align="right"  style="margin-top: 15px;">
			<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/aaaa</div>
			<br/>
			<div class="btn-group" ng-disabled="!producto.botones">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="producto.mForm.$valid && producto.botones ? producto.guardar() : ''" ng-disabled="!producto.mForm.$valid || !producto.botones" uib-tooltip="Guardar" tooltip-placement="top">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label ng-if="!producto.esTreeview" class="btn btn-primary" ng-click="producto.botones ? producto.cancelar() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!producto.botones" tooltip-placement="top">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="producto.esTreeview" class="btn btn-danger" ng-click="producto.botones ? producto.congelado != 1 ? producto.t_borrar() : '' : ''" ng-disabled="!(producto.producto.id>0) || !producto.botones || producto.congelado == 1" uib-tooltip="Borrar" tooltip-placement="top">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		
	</div>
</div>