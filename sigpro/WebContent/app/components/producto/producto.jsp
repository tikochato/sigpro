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
			{{ producto.objetoTipoNombre }}: {{ producto.componenteNombre }}
		</div>
	
  
	<div align="center" ng-hide="producto.esForma" ng-if="!producto.esTreeview">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="21040">
					<label class="btn btn-primary" ng-click="producto.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="21010">
					<label class="btn btn-primary" ng-click="producto.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="21030">
					<label class="btn btn-danger" ng-click="producto.borrar()" uib-tooltip="Borrar">
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
				<span class="glyphicon glyphicon-th-list"></span></label>
				<label class="btn btn-default" ng-click="producto.botones ? producto.irARiesgos() : ''" uib-tooltip="Riesgos" tooltip-placement="bottom" ng-disabled="!producto.botones">
				<span class="glyphicon glyphicon-warning-sign"></span></label>
				<label class="btn btn-default" ng-click="producto.botones ? producto.irAMetas() : ''" uib-tooltip="Metas" tooltip-placement="bottom" ng-disabled="!producto.botones">
				<span class="glyphicon glyphicon-scale"></span></label>
			</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="producto.mForm.$valid && producto.botones ? producto.guardar() : ''" ng-disabled="!producto.mForm.$valid || !producto.botones" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label ng-if="!producto.esTreeview" class="btn btn-primary" ng-click="producto.botones ? producto.cancelar() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!producto.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="producto.esTreeview" class="btn btn-danger" ng-click=" producto.botones ? producto.t_borrar() : ''" ng-disabled="!(producto.producto.id>0) || !producto.botones" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" style="margin-top: 10px;">
		<uib-tabset active="active">
    	<uib-tab index="0" name="tproducto">
		<uib-tab-heading>
	        <i class="glyphicon glyphicon-certificate"></i> Producto
	      </uib-tab-heading>
		<div>
		<div class="col-sm-12">
			<form name="producto.mForm" class="css-form">
					<div class="form-group">
						<label id="campo0" name="campo0" class="floating-label id_class">ID {{ producto.producto.id }}</label>
						<br/><br/>
					</div>
								
					<div class="form-group">
						<input type="text" class="inputText" ng-model="producto.producto.nombre" ng-value="producto.producto.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" id="nombre"/>
						<label class="floating-label">* Nombre</label> 
					</div>
					
					<div class="form-group"  >
						<input type="number"  class="inputText" ng-model="producto.producto.snip" ng-value="producto.producto.snip" onblur="this.setAttribute('value', this.value);">
						<label for="isnip" class="floating-label">SNIP</label>
					</div>
				
					<div class="form-group-row row" >
						<div style="width: 100%">
							<table style="width: 100%">
								<tr>
									<td style="width: 14%; padding-right:5px;">
										<input name="programa" type="number" class="inputText" ng-model="producto.producto.programa" ng-value="producto.producto.programa" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;" />
						       			<label for="programa" class="floating-label">Programa</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.subprograma" ng-value="producto.producto.subprograma" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="isubprog" class="floating-label">Subprograma</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.proyecto_" ng-value="producto.producto.proyecto_" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="iproy_" class="floating-label">Proyecto</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.actividad" ng-value="producto.producto.actividad" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
							  			<label for="iobra" class="floating-label">Actividad</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.obra" ng-value="producto.producto.obra" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						 				<label for="iobra" class="floating-label">Obra</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.renglon" ng-value="producto.producto.renglon" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="fuente" class="floating-label">Renglon</label>
									</td>
									<td style="width: 14%; padding-right:5px;">
										<input type="number" class="inputText" ng-model="producto.producto.ubicacionGeografica" ng-value="producto.producto.ubicacionGeografica" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center;"/>
						  				<label for="fuente" class="floating-label">Geográfico</label>
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<div class="form-group" >
			          <input type="text" class="inputText" ng-model="producto.tipoNombre" ng-value="producto.tipoNombre" 
			          	ng-click="producto.buscarTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
			           <span class="label-icon" ng-click="producto.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          <label for="campo3" class="floating-label">* Tipo</label>
			        </div>
			        
			        <div class="form-group" ng-show="producto.unidadEjecutoraNombre.length>0"  >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="producto.entidadnombre" ng-readonly="true"  
			            	 ng-value="producto.entidadnombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
					</div>
			        
			        <div class="form-group">
			            <input type="text" class="inputText" ng-model="producto.unidadEjecutoraNombre" ng-value="producto.unidadEjecutoraNombre" 
			            	ng-click="producto.buscarUnidadEjecutora()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" />
			            <span class="label-icon" ng-click="producto.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          <label for="campo5" class="floating-label">Unidad Ejecutora</label>
			        </div>
			        
			        <div class="form-group">
			          	<input type="text" class="inputText" ng-model="producto.coordenadas" ng-value="producto.coordenadas" 
			          		onblur="this.setAttribute('value', this.value);"ng-click="producto.open(producto.producto.latitud, producto.producto.longitud); " ng-readonly="true" />
			            <span class="label-icon" ng-click="producto.open(producto.producto.latitud, producto.producto.longitud); "><i class="glyphicon glyphicon-map-marker"></i></span>
			          	<label  class="floating-label">Coordenadas</label>
					</div>
						
					<div class="form-group" >
						<input type="text" class="inputText" ng-model="producto.producto.descripcion" ng-value="producto.producto.descripcion" onblur="this.setAttribute('value', this.value);"/>
						<label for="campo2" class="floating-label"> Descripción</label> 
					</div>
			        
			        <div class="form-group" ng-repeat="campo in producto.camposdinamicos">
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
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="producto.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="producto.abrirPopupFecha($index)"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="producto.abrirPopupFecha($index)">
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
				       <input type="text" class="inputText" ng-model="producto.producto.costo" ng-value="producto.producto.costo" onblur="this.setAttribute('value', this.value);" style="text-align: left"
				       ng-required="producto.producto.acumulacionCostoNombre != null"
										ui-number-mask="2" />
				       <label for="iprog" class="floating-label">{{producto.producto.acumulacionCostoNombre  != null ?"* Costo":"Costo"}}</label>
					</div>
						
					<div class="form-group" >
					    <input type="text" class="inputText" id="acumulacionCosto" name="acumulacionCosto" ng-model="producto.producto.acumulacionCostoNombre" ng-value="producto.producto.acumulacionCostoNombre" 
						ng-click="producto.buscarAcumulacionCosto()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="producto.producto.costo != null && producto.producto.costo>0"/>
						<span class="label-icon" ng-click="producto.buscarAcumulacionCosto()"><i class="glyphicon glyphicon-search"></i></span>
						<label for="campo3" class="floating-label">{{producto.validarRequiredCosto(producto.producto.costo)}}</label>
					</div>
					
					<div class = "row">
						<div class="col-sm-6">
							<div class="form-group">
								<select class="inputText" ng-model="producto.duracionDimension"
									ng-options="dim as dim.nombre for dim in producto.dimensiones track by dim.value"
									 ng-required="true">
								</select>
								<label for="nombre" class="floating-label">* Dimension</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group">
							   <input class="inputText"  type="number"
							     ng-model="producto.producto.duracion" ng-value="producto.producto.duracion"   
							     onblur="this.setAttribute('value', this.value);"  min="1" ng-required="true" 
							     ng-readonly="producto.duracionDimension.value != 0 ? false : true"
							     ng-change="producto.producto.fechaInicio != null && producto.duracionDimension.value != 0 ? producto.cambioDuracion(producto.duracionDimension) : ''">
							   <label class="floating-label">* Duración</label>
							</div>	
						</div>
							
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" min={{producto.fechaInicioPadre}} ng-model="producto.producto.fechaInicio" is-open="producto.fi_abierto"
							            datepicker-options="producto.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="producto.cambioDuracion(producto.duracionDimension);" ng-required="true"  
							            ng-value="producto.producto.fechaInicio" onblur="this.setAttribute('value', this.value);"/>
							            <span class="label-icon" ng-click="producto.abrirPopupFecha(1000)">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{producto.formatofecha}}" ng-model="producto.producto.fechaFin" is-open="producto.ff_abierto"
							            datepicker-options="producto.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true" ng-click=""
							            ng-value="producto.producto.fechaFin" onblur="this.setAttribute('value', this.value);"
							            ng-readonly="true"/>
							            <span class="label-icon">
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
			</form>
			</div>
		</div>
		</uib-tab>
		<uib-tab index="1" ng-click="producto.metasActivo()">
			<uib-tab-heading>
	        	<i class="glyphicon glyphicon-scale"></i> Metas
	      	</uib-tab-heading>
			<shiro:lacksPermission name="17010">
				<span ng-init="producto.redireccionSinPermisos()"></span>
			</shiro:lacksPermission>
			<%@include file="/app/components/meta/meta.jsp" %>
    	</uib-tab>
    	<uib-tab index="2">
	      <uib-tab-heading>
	        <i class="glyphicon glyphicon-bell"></i> Riesgos
	      </uib-tab-heading>
	    </uib-tab>
	    <uib-tab index="3">
	      <uib-tab-heading>
	        <i class="glyphicon glyphicon-bell"></i> Adquisiciones
	      </uib-tab-heading>
	    </uib-tab>
	    <uib-tab index="4">
	      <uib-tab-heading>
	        <i class="glyphicon glyphicon-bell"></i> Desembolsos
	      </uib-tab-heading>
	    </uib-tab>
	  </uib-tabset>
	</div>	
		<div class="col-sm-12 operation_buttons" align="right">
			<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/yyyy</div>
			<br/>
			<div class="btn-group" ng-disabled="!producto.botones">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="producto.mForm.$valid && producto.botones ? producto.guardar() : ''" ng-disabled="!producto.mForm.$valid || !producto.botones" uib-tooltip="Guardar" tooltip-placement="top">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label ng-if="!producto.esTreeview" class="btn btn-primary" ng-click="producto.botones ? producto.cancelar() : ''" uib-tooltip="Ir a Tabla" ng-disabled="!producto.botones" tooltip-placement="top">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="producto.esTreeview" class="btn btn-danger" ng-click="producto.botones ? producto.t_borrar() : ''" ng-disabled="!(producto.producto.id>0) || !producto.botones" uib-tooltip="Borrar" tooltip-placement="top">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		
	</div>
</div>