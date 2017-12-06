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
	<div ng-controller="actividadController as actividadc" ng-class="actividadc.esTreeview ? 'maincontainer_treeview all_page':'maincontainer all_page'" id="title">
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

	    <script type="text/ng-template" id="buscarActividadTipo.jsp">
    		<%@ include file="/app/components/actividad/buscarActividadTipo.jsp"%>
  	    </script>

		<script type="text/ng-template" id="buscarAcumulacionCosto.jsp">
    		<%@ include file="/app/components/actividad/buscarAcumulacionCosto.jsp"%>
  	    </script>

  	    <shiro:lacksPermission name="1010">
			<span ng-init="actividadc.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		
		<div class="panel panel-default" ng-if="!actividadc.esTreeview">
			<div class="panel-heading"><h3>Actividades</h3></div>
		</div>
		<div class="subtitulo" ng-if="!actividadc.esTreeview">
			{{ actividadc.objetoTipoNombre }} {{ actividadc.objetoNombre }}
		</div>
		
		<div class="row" align="center" ng-hide="actividadc.mostraringreso" ng-if="!actividadc.esTreeview">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="1040">
			    <label class="btn btn-primary" ng-click="actividadc.congelado?'':actividadc.nuevo()" ng-disabled="actividadc.congelado" uib-tooltip="Nueva">
			    <span class="glyphicon glyphicon-plus"></span> Nueva</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="1020">
			    <label class="btn btn-primary" ng-click="actividadc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="1030">
			    <label class="btn btn-danger" ng-click="actividadc.congelado?'':actividadc.borrar()" ng-disabled="actividadc.congelado" uib-tooltip="Borrar">
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
				<div class="total-rows">Total de {{  actividadc.totalActividades + (actividadc.totalActividades > 1 ? " Actividades" : " Actividad" ) }}</div>
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
		<div class="row second-main-form" ng-show="actividadc.mostraringreso || actividadc.esTreeview">
			<div class="page-header">
			    <h2 ng-if="actividadc.esnuevo"><small>Nueva actividad</small></h2>
				<h2 ng-if="!actividadc.esnuevo"><small>Edición de actividad</small></h2>
			</div>
			
			<div class="operation_buttons">
				<div class="btn-group" ng-hide="actividadc.esnuevo" ng-if="!actividadc.esTreeview">
					<label class="btn btn-default" ng-click="actividadc.botones ? actividadc.irAActividades(actividadc.actividad.id) : ''" uib-tooltip="Actividad" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-time"></span></label>
					<label class="btn btn-default" ng-click="actividadc.verHistoria()" uib-tooltip="Ver Historia">
					<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
				</div>
				<div ng-if="actividadc.esTreeview">
			      	<label class="btn btn-default" ng-click="actividadc.verHistoria()" uib-tooltip="Ver Historia">
					<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
			     </div>
				<div class="btn-group" style="float: right;">
					<shiro:hasPermission name="1020">
						<label class="btn btn-success" ng-click="actividadc.mForm.$valid && actividadc.botones ? actividadc.guardar() : ''" ng-disabled="!actividadc.mForm.$valid || !actividadc.botones" uib-tooltip="Guardar" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
					<label ng-if="!actividadc.esTreeview" class="btn btn-primary" ng-click="actividadc.botones ? actividadc.irATabla() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!actividadc.botones" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
					<label ng-if="actividadc.esTreeview" class="btn btn-danger" ng-click="actividadc.botones && actividadc.actividad.id>0 && !actividadc.congelado ? actividadc.t_borrar() : ''" ng-disabled="!(actividadc.actividad.id>0) || !actividadc.botones || actividadc.congelado" uib-tooltip="Borrar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</div>
			</div>
			<div class="col-sm-12">
				<form name="actividadc.mForm">
					<uib-tabset active="actividadc.activeTab">
    					<uib-tab index="0" name="tproducto" heading="Actividad">
						<div class="form-group">
							<label for="id" class="floating-label id_class">ID {{actividadc.actividad.id }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
    						<div class="form-group">
							   <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="actividadc.actividad.nombre" ng-value="actividadc.actividad.nombre"  onblur="this.setAttribute('value', this.value);" ng-required="true" ng-readonly="actividadc.congelado" >
							   <label class="floating-label">* Nombre</label>
							</div>
						</div>
						<div class="form-group-row row" >
							<div style="width: 100%">
								<table style="width: 100%">
									<tr>
										<td style="width: 14%; padding-right:5px;">
											<input name="programa" type="number" class="inputText" ng-model="actividadc.actividad.programa" ng-value="actividadc.actividad.programa" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"  ng-readonly="actividadc.congelado"/>
							       			<label class="floating-label">Programa</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="actividadc.actividad.subprograma" ng-value="actividadc.actividad.subprograma" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" ng-readonly="actividadc.congelado"/>
							  				<label class="floating-label">Subprograma</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="actividadc.actividad.proyecto" ng-value="actividadc.actividad.proyecto" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" ng-readonly="actividadc.congelado"/>
							  				<label class="floating-label">Proyecto</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="actividadc.actividad.actividad" ng-value="actividadc.actividad.actividad" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" ng-readonly="actividadc.congelado"/>
								  			<label class="floating-label">Actividad</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="actividadc.actividad.obra" ng-value="actividadc.actividad.obra" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" ng-readonly="actividadc.congelado"/>
							 				<label class="floating-label">Obra</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="actividadc.actividad.renglon" ng-value="actividadc.actividad.renglon" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" ng-readonly="actividadc.congelado"/>
							  				<label class="floating-label">Renglón</label>
										</td>
										<td style="width: 14%; padding-right:5px;">
											<input type="number" class="inputText" ng-model="actividadc.actividad.ubicacionGeografica" ng-value="actividadc.actividad.ubicacionGeografica" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" ng-readonly="actividadc.congelado"/>
							  				<label class="floating-label">Geográfico</label>
										</td>
									</tr>
								</table>
							</div>
						</div>
							<div class="form-group">
								<div id="tipoNombre" angucomplete-alt placeholder="" pause="100" selected-object="actividadc.cambioTipo"
								  local-data="actividadc.tipos" search-fields="nombre" title-field="nombre" field-required="true" field-label="* Tipo de Actividad"
								  minlength="1" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight"
								  initial-value="actividadc.actividad.actividadtiponombre" focus-out="actividadc.blurTipo()" input-name="tipoNombre" disable-input="actividadc.congelado"></div>
							</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
								   <input class="inputText"  type="number"
								     ng-model="actividadc.actividad.duracion" ng-value="actividadc.actividad.duracion"   
								     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
								     ng-change="actividadc.actividad.fechaInicio != null && actividadc.duracionDimension != '' ? actividadc.cambioDuracion(actividadc.duracionDimension) : ''"  
								     ng-readonly="actividadc.actividad.tieneHijos || actividadc.congelado">
								   <label class="floating-label">* Duración</label>
								</div>	
							</div>
							
							<div class="col-sm-6">
								<div class="form-group">
									<select class="inputText" ng-model="actividadc.duracionDimension"
										ng-options="dim as dim.nombre for dim in actividadc.dimensiones track by dim.value"
										 ng-required="true" ng-readonly="actividadc.congelado">
									</select>
									<label class="floating-label">* Dimension</label>
								</div>
							</div>
							
							<div class="col-sm-6">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}" alt-input-formats="{{actividadc.altformatofecha}}"
								  			ng-model="actividadc.actividad.fechaInicio" is-open="actividadc.fi_abierto"
								            datepicker-options="actividadc.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="actividadc.actualizarfechafin(); actividadc.cambioDuracion(actividadc.duracionDimension);" ng-required="true"  
								            ng-value="actividadc.actividad.fechaInicio" onblur="this.setAttribute('value', this.value);"
								             ng-readonly="actividadc.actividad.tieneHijos || actividadc.congelado"/>
								            <span class="label-icon" ng-click="actividadc.actividad.tieneHijos!=true ? actividadc.abrirPopupFecha(1000) : ''" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label class="floating-label">*Fecha de Inicio</label>
								</div>
							</div>
							
							<div class="col-sm-6">
							
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}"
								  			ng-model="actividadc.actividad.fechaFin" is-open="actividadc.ff_abierto"
								            datepicker-options="actividadc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="actividadc.actualizarfechafin()" ng-required="true"
								            readonly="readonly"
								            ng-value="actividadc.actividad.fechaFin" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label class="floating-label">* Fecha de Fin</label>
								</div>
							</div>
							
							<div class="col-sm-6">
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}" alt-input-formats="{{actividadc.altformatofecha}}"
								  			ng-model="actividadc.actividad.fechaInicioReal"
								            datepicker-options="actividadc.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
								            ng-value="actividadc.actividad.fechaInicioReal" onblur="this.setAttribute('value', this.value);"
							            	readonly="readonly"/>
								            <span class="label-icon" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label class="floating-label">Fecha de Inicio Real</label>
								</div>
							</div>
							
							<div class="col-sm-6">
							
								<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}"
								  			ng-model="actividadc.actividad.fechaFinReal"
								            datepicker-options="actividadc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
								            readonly="readonly" ng-value="actividadc.actividad.fechaFinReal" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" tabindex="-1">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label class="floating-label">Fecha de Fin Real</label>
								</div>
							</div>
						</div>
											
						<div class="form-group">
						   <input type="number" name="iavance"  class="inputText" id="inombre" 
						     ng-model="actividadc.actividad.porcentajeavance" ng-value="actividadc.actividad.porcentajeavance" 
						     onblur="this.setAttribute('value', this.value);"  min="0" max="100" ng-required="true" >
						   <label class="floating-label">* Avance %</label>
						</div>
						
						<div class="form-group" >
					       <input type="text" class="inputText" ng-model="actividadc.actividad.costo" ng-value="actividadc.actividad.costo" ui-number-mask="2"
					       	onblur="this.setAttribute('value', this.value);" style="text-align: left" ng-required="actividadc.actividad.acumulacionCostoId > 0" 
					       	ng-readonly="actividadc.actividad.tieneHijos || actividadc.congelado"/>
					       <label for="iprog" class="floating-label">{{actividadc.actividad.acumulacionCostoId > 0 ? "* Monto Planificado" : "Monto Planificado"}}</label>
						</div>
						<div class="form-group" >
							<div id="acumulacionCosto" angucomplete-alt placeholder="" pause="100" selected-object="actividadc.cambioAcumulacionCosto"
									local-data="actividadc.acumulacionCostos" search-fields="nombre" title-field="nombre" field-required="actividadc.actividad.costo!=null && actividadc.actividad.costo>0" 
						  			field-label="{{actividadc.actividad.costo!=null && actividadc.actividad.costo>0 ? '* ':''}}Tipo Acumulación de Monto Planificado"
									minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
									initial-value="actividadc.actividad.acumulacionCostoNombre" focus-out="actividadc.blurAcumulacionCosto()" input-name="acumulacionCosto" disable-input="actividadc.congelado"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
						<div class="form-group" >
						    <input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="actividadc.coordenadas" ng-value="actividadc.coordenadas" 
								 ng-click="actividadc.congelado?'':actividadc.open(actividadc.actividad.latitud, actividadc.actividad.longitud); " onblur="this.setAttribute('value', this.value);" ng-readonly="true"/>
							<span class="label-icon" ng-click="actividadc.congelado?'':actividadc.open(actividadc.actividad.latitud, actividadc.actividad.longitud); " tabindex="-1"><i class="glyphicon glyphicon-map-marker"></i></span>
							<label class="floating-label">Coordenadas</label>
						</div>
						
						
						<div ng-repeat="campo in actividadc.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);" ng-readonly="actividadc.congelado"/>	
									<label class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"  ng-readonly="actividadc.congelado"/>
									<label class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);" ng-readonly="actividadc.congelado"/>
									<label class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor"  ng-readonly="actividadc.congelado"/>
									<label class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}"  alt-input-formats="{{actividadc.altformatofecha}}"
												ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="actividadc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="actividadc.abrirPopupFecha($index)"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);" ng-readonly="actividadc.congelado"/>
														<span class="label-icon" ng-click="actividadc.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor" ng-readonly="actividadc.congelado">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														ng-value="number.valor">{{number.label}}</option>
								</select>
									<label class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
						</div>
						
						<div class="form-group">
						   <textarea class="inputText" rows="4"
						   ng-model="actividadc.actividad.descripcion" ng-value="actividadc.actividad.descripcion"   
						   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
						   <label class="floating-label">Descripción</label>
						</div>
				<br/>
				
				<div align="center">
					<h5>Asignación de Responsables</h5>
					<div style="height: 35px;" >
						<div style="text-align: right;">
							<div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href
									ng-click="actividadc.congelado?'':actividadc.buscarActividadResponsable()" role="button"
									ng-disabled="actividadc.congelado"
									uib-tooltip="Asignar Nuevo Responsable" tooltip-placement="left">
									<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
								</a>
							</div>
						</div>
					</div>
					<br/>
					<table
					st-table="actividadc.responsables"
					class="table table-striped table-bordered table-hover table-propiedades">
					<thead >
						<tr>
							<th style="width: 60px;">ID</th>
							<th>Nombre</th>
							<th>Rol</th>
							<th style="width: 30px;">Quitar</th>

						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row"
							ng-repeat="row in actividadc.responsables">
							<td>{{row.id}}</td>
							<td>{{row.nombre}}</td>
							<td>{{row.nombrerol}}</td>
							
							<td>
								<button type="button"
									ng-click="actividadc.congelado?'':actividadc.eliminarResponsable(row)"
									ng-disabled="actividadc.congelado"
									class="btn btn-sm btn-danger">
									<i class="glyphicon glyphicon-minus-sign"> </i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
				<br/>
				
				<div class="panel panel-default" ng-hide="actividadc.esNuevoDocumento" >
						<div class="panel-heading label-form" style="text-align: center;">Archivos adjuntos</div>
						<div class="panel-body">
							<div style="width: 95%; float: left">
							<table st-table="actividadc.displayedCollection" st-safe-src="actividadc.rowCollection" class="table table-striped">
								<thead>
									<tr>
										<th style="display: none;">Id</th>
										<th class="label-form">Nombre</th>
										<th class="label-form">Extensión</th>
										<th class="label-form">Descarga</th>
										<th class="label-form">Eliminar</th>
									</tr>
									<tr>
										<th colspan="5"><input st-search="" class="form-control" placeholder="busqueda global ..." type="text"/></th>
									</tr>
								</thead>
								<tbody>
								<tr ng-repeat="row in actividadc.displayedCollection">
									<td style="display: none;">{{row.id}}</td>
									<td>{{row.nombre}}</td>
									<td>{{row.extension}}</td>
									<td align="center">
										<button type="button"
											ng-click="actividadc.descargarDocumento(row)"
											uib-tooltip="Descargar documento" tooltip-placement="bottom"
											class="btn btn-default">
											<i class="glyphicon glyphicon-download-alt"> </i>
										</button>
									</td>
									<td align="center">
										<button type="button"
											ng-click="actividadc.eliminarDocumento(row)"
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
	    						<div class="btn-group">
									<label class="btn btn-default" ng-model="actividadc.adjuntarDocumento"
										ng-click="actividadc.adjuntarDocumentos();" uib-tooltip="Adjuntar documento" tooltip-placement="bottom">
									<span class="glyphicon glyphicon-plus"></span></label>
								</div>
	        				</div>
						</div>
					</div>
				
				
				
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
				</uib-tab>
		    	<uib-tab index="1" heading="Adquisición" ng-click="actividadc.adquisicionesActivo()">
		    		<div ng-if="actividadc.adquisicionesCargadas">
						<%@include file="/app/components/adquisicion/adquisicion.jsp" %>
					</div>
			    </uib-tab>
			  </uib-tabset>
			  
				</form>
				
			</div>
		<div class="col-sm-12 operation_buttons" align="right" style="margin-top: 15px;">
			<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/aaaa</div>
			<br/>
			<div class="btn-group">
				<shiro:hasPermission name="1020">
					<label class="btn btn-success" ng-click="actividadc.mForm.$valid && actividadc.botones ? actividadc.guardar() : ''" ng-disabled="!actividadc.mForm.$valid || !actividadc.botones" uib-tooltip="Guardar" tooltip-placement="top">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label ng-if="!actividadc.esTreeview"  class="btn btn-primary" ng-click="actividadc.botones ? actividadc.irATabla() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!actividadc.botones" tooltip-placement="top">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="actividadc.esTreeview" class="btn btn-danger" ng-click="actividadc.botones && actividadc.actividad.id>0 && !actividadc.congelado ? actividadc.t_borrar() : ''" ng-disabled="!(actividadc.actividad.id>0) || !actividadc.botones || actividadc.congelado" uib-tooltip="Borrar" tooltip-placement="top">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
	</div>
</div>