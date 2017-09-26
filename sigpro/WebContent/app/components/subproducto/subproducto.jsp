<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div ng-controller="controlSubproducto as subproducto" ng-class="subproducto.esTreeview ? 'maincontainer_treeview all_page':'maincontainer all_page'">
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
    
	<script type="text/ng-template" id="buscarPorSubproducto.jsp">
	    <%@ include file="/app/components/subproducto/buscarPorSubproducto.jsp"%>
	</script>
	<shiro:lacksPermission name="40010">
		<span ng-init="subproducto.redireccionSinPermisos()"></span>
	</shiro:lacksPermission>

	<div class="panel panel-default" ng-if="!subproducto.esTreeview">
	   	<div class="panel-heading"><h3>Subproducto</h3></div>
	</div>
		<div class="subtitulo" ng-if="!subproducto.esTreeview">
			{{ subproducto.objetoTipoNombre }} {{ subproducto.productoNombre }}
		</div>
	
  
	<div align="center" ng-hide="subproducto.esForma" ng-if="!subproducto.esTreeview">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="40040">
					<label class="btn btn-primary" ng-click="subproducto.nuevo()" uib-tooltip="Nuevo">
					<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="40010">
					<label class="btn btn-primary" ng-click="subproducto.editar()" uib-tooltip="Editar">
					<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="40030">
					<label class="btn btn-danger" ng-click="subproducto.borrar()" uib-tooltip="Borrar">
					<span class="glyphicon glyphicon-trash"></span> Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="40010">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="subproducto.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
								<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="subproducto.opcionesGrid"
					ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!subproducto.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  subproducto.totalElementos + (subproducto.totalElementos == 1 ? " Subproducto" : " Subproductos" ) }}</div>
				<ul uib-pagination total-items="subproducto.totalElementos"
					ng-model="subproducto.paginaActual"
					max-size="subproducto.numeroMaximoPaginas"
					items-per-page="subproducto.elementosPorPagina"
					first-text="Primero" last-text="Último" next-text="Siguiente"
					previous-text="Anterior" class="pagination-sm"
					boundary-links="true" force-ellipses="true"
					ng-change="subproducto.cambioPagina()"></ul>
			</div>
		</shiro:hasPermission>

	</div>

	<div ng-show="subproducto.esForma || subproducto.esTreeview" class="row second-main-form">
		<div class="page-header">
			<h2 ng-if="subproducto.esNuevo"><small>Nuevo Subproducto</small></h2>
			<h2 ng-if="!subproducto.esNuevo"><small>Edición de Subproducto</small></h2>
		</div>
		<div class="col-sm-12 operation_buttons" >
			<div class="btn-group" ng-hide="subproducto.esNuevo" ng-if="!subproducto.esTreeview">
				<label class="btn btn-default" ng-click="supbroducto.botones ? subproducto.irAActividades() : ''" uib-tooltip="Actividades" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-th-list"></span></label>
				<label class="btn btn-default" ng-click="subproducto.botones ? subproducto.irARiesgos() : ''" uib-tooltip="Riesgos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-warning-sign"></span></label>
			</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="40020">
					<label class="btn btn-success" ng-click="subproducto.mForm.$valid && subproducto.botones ? subproducto.guardar() : ''" ng-disabled="!subproducto.mForm.$valid || !subproducto.botones" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label ng-if="!subproducto.esTreeview" class="btn btn-primary" ng-click="subproducto.botones ? subproducto.cancelar() : ''" uib-tooltip="Ir a Tabla" tooltip-placement="bottom" ng-disabled="!subproducto.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="subproducto.esTreeview" class="btn btn-danger" ng-click=" subproducto.botones ? subproducto.t_borrar() : ''" ng-disabled="!(subproducto.subproducto.id>0) || !subproducto.botones" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		<div>
		<div class="col-sm-12">
			<form name="subproducto.mForm" class="css-form">
				
					<div class="form-group">
						<label for="id" class="floating-label id_class">ID {{subproducto.subproducto.id}}</label>
						<br/><br/> 
					</div>
				
				
					<div class="form-group">
						<input type="text" id="nombre" class="inputText" ng-model="subproducto.subproducto.nombre" ng-required="true" 
						ng-value="subproducto.subproducto.nombre" onblur="this.setAttribute('value', this.value);"/>
						<label class="floating-label">* Nombre</label>
					</div>
					
					<div class="form-group"  >
						<input type="number" ng-model="subproducto.subproducto.snip" class="inputText"
							ng-value="subproducto.subproducto.snip" onblur="this.setAttribute('value', this.value);">
						<label class="floating-label">SNIP</label>
					</div>
				
					<div class="form-group-row row" >
						<div style="width: 100%">
							<table style="width: 100%">
								<tr>
									<td style="width: 14%; padding-right:5px;">
										<input name="programa" type="number" class="inputText" ng-model="subproducto.subproducto.programa" ng-value="subproducto.subproducto.programa" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" />
						       			<label for="programa" class="floating-label">Programa</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="subproducto.subproducto.subprograma" ng-value="subproducto.subproducto.subprograma" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="isubprog" class="floating-label">Subprograma</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="subproducto.subproducto.proyecto_" ng-value="subproducto.subproducto.proyecto" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="iproy_" class="floating-label">Proyecto</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="subproducto.subproducto.actividad" ng-value="subproducto.subproducto.actividad" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  			<label for="iobra" class="floating-label">Actividad</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="subproducto.subproducto.obra" ng-value="subproducto.subproducto.obra" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						 				<label for="iobra" class="floating-label">Obra</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="subproducto.subproducto.renglon" ng-value="subproducto.subproducto.renglon" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="fuente" class="floating-label">Renglon</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="subproducto.subproducto.ubicacionGeografica" ng-value="subproducto.subproducto.ubicacionGeografica" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="fuente" class="floating-label">Geográfico</label>
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="form-group">
				            	<input type="text" class="inputText" ng-model="subproducto.coordenadas" ng-readonly="true" 
				            		ng-value="subproducto.coordenadas" onblur="this.setAttribute('value', this.value);"
				            		ng-click="subproducto.open(subproducto.subproducto.latitud, subproducto.subproducto.longitud); "/>
				            	<span class="label-icon" ng-click="subproducto.open(subproducto.subproducto.latitud, subproducto.subproducto.longitud); "><i class="glyphicon glyphicon-map-marker"></i></span>
				            	<label class="floating-label">Coordenadas</label>
					</div>

					<div class="form-group" >
						<input type="text" class="inputText" ng-model="subproducto.subproducto.descripcion" 
							ng-value="subproducto.subproducto.descripcion" onblur="this.setAttribute('value', this.value);"/>
						<label for="campo2" class="floating-label">Descripción</label>
					</div>
					
					<div class="form-group" >
			            <input type="text" class="inputText" ng-model="subproducto.tipoNombre" ng-readonly="true" ng-required="true" 
			            	ng-value="subproducto.tipoNombre" onblur="this.setAttribute('value', this.value);" 
			            	ng-click="subproducto.buscarTipo()"/>
	            			<span class="label-icon" ng-click="subproducto.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
			            <label class="floating-label">* Tipo</label>
			        </div>
			        
			        <div class="form-group" ng-show="subproducto.unidadEjecutoraNombre.length>0"  >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="subproducto.entidadnombre" ng-readonly="true"  
			            	 ng-value="subproducto.entidadnombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
					</div>
			        
			        <div class="form-group">
			            <input type="text" class="inputText" ng-model="subproducto.unidadEjecutoraNombre" ng-readonly="true" 
			            	ng-value="subproducto.unidadEjecutoraNombre" onblur="this.setAttribute('value', this.value);" 
			            	ng-click="subproducto.buscarUnidadEjecutora()"/>
			            <span class="label-icon" ng-click="subproducto.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          <label class="floating-label">Unidad Ejecutora</label>
			        </div>
			        
			        <div class="form-group" ng-repeat="campo in subproducto.camposdinamicos">
						<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{subproducto.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="subproducto.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="subproducto.abrirPopupFecha($index)"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="subproducto.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														ng-value="number.valor">{{number.label}}</option>
								</select>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
					</div>
					<div class="form-group" >
				       <input type="text" class="inputText" ng-model="subproducto.subproducto.costo" ng-value="subproducto.subproducto.costo" onblur="this.setAttribute('value', this.value);" style="text-align: left" ui-number-mask="2"/>
				       <label for="iprog" class="floating-label">Costo</label>
					</div>
					
					<div class="form-group" >
					    <input type="text" class="inputText" id="acumulacionCosto" name="acumulacionCosto" ng-model="subproducto.subproducto.acumulacionCostoNombre" ng-value="subproducto.subproducto.acumulacionCostoNombre" 
						ng-click="subproducto.buscarAcumulacionCosto()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="subproducto.subproducto.costo != null && subproducto.subproducto.costo > 0"/>
						<span class="label-icon" ng-click="subproducto.buscarAcumulacionCosto()"><i class="glyphicon glyphicon-search"></i></span>
						<label for="campo3" class="floating-label">{{subproducto.validarRequiredCosto(subproducto.subproducto.costo)}}</label>
					</div>
					
					<div class = "row">
						<div class="col-sm-6">
							<div class="form-group">
								<select class="inputText" ng-model="subproducto.duracionDimension"
									ng-options="dim as dim.nombre for dim in subproducto.dimensiones track by dim.value"
									 ng-required="true">
								</select>
								<label for="nombre" class="floating-label">* Dimension</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group">
							   <input class="inputText"  type="number"
							     ng-model="subproducto.subproducto.duracion" ng-value="subproducto.subproducto.duracion"   
							     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
							     ng-readonly="subproducto.duracionDimension.value != 0 ? false : true"
							     ng-change="subproducto.subproducto.fechaInicio != null && subproducto.duracionDimension != 0 ? subproducto.cambioDuracion(subproducto.duracionDimension) : ''">
							   <label class="floating-label">* Duración</label>
							</div>	
						</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{subproducto.formatofecha}}" ng-model="subproducto.subproducto.fechaInicio" is-open="subproducto.fi_abierto"
							            datepicker-options="subproducto.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="subproducto.cambioDuracion(subproducto.duracionDimension);" ng-required="true"  
							            ng-value="subproducto.subproducto.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
							            <span class="label-icon" ng-click="subproducto.abrirPopupFecha(1000)">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{subproducto.formatofecha}}" ng-model="subproducto.subproducto.fechaFin" is-open="subproducto.ff_abierto"
							            datepicker-options="subproducto.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true" ng-click=""
							            ng-value="subproducto.subproducto.fechaFin" onblur="this.setAttribute('value', this.value);"
							            ng-readonly="true"/>
							            <span class="label-icon" ng-click="subproducto.abrirPopupFecha(1001)">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
							</div>
						</div>
					</div>
						
				<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label>
				  					<p>{{ subproducto.subproducto.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
				  					<p>{{ subproducto.subproducto.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label>
				  					<p>{{ subproducto.subproducto.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label>
				  					<p>{{ subproducto.subproducto.fechaActualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			</div>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/yyyy</div>
			<br/>
			<div class="btn-group">
				<shiro:hasPermission name="40020">
					<label class="btn btn-success" ng-click="subproducto.mForm.$valid && subproducto.botones ? subproducto.guardar() : ''" ng-disabled="!subproducto.mForm.$valid || !subproducto.botones" uib-tooltip="Guardar" tooltip-placement="top">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label ng-if="!subproducto.esTreeview" class="btn btn-primary" ng-click="subproducto.botones ? subproducto.cancelar() : ''" uib-tooltip="Ir a Tabla" tooltip-placement="top" ng-disabled="!subproducto.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="subproducto.esTreeview" class="btn btn-danger" ng-click=" subproducto.botones ? subproducto.t_borrar() : ''" ng-disabled="!(subproducto.subproducto.id>0) || !subproducto.botones" uib-tooltip="Borrar" tooltip-placement="top">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
	</div>
</div>