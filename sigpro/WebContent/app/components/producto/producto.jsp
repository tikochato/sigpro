<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div ng-controller="controlProducto as producto" class="maincontainer all_page">
	
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
    
	<script type="text/ng-template" id="buscarPorProducto.jsp">
	    <%@ include file="/app/components/producto/buscarPorProducto.jsp"%>
	</script>
	<shiro:lacksPermission name="21010">
		<p ng-init="producto.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>

	<div class="panel panel-default">
		<div class="panel-heading">
			<h3>Producto</h3>
		</div>
	</div>
		<div class="subtitulo">
			{{ producto.objetoTipoNombre }} {{ producto.componenteNombre }}
		</div>
	
  
	<div align="center" ng-hide="producto.esForma">
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

	<div ng-show="producto.esForma" class="row second-main-form">
		<div class="page-header">
			<h2 ng-hide="!producto.esNuevo"><small>Nuevo Producto</small></h2>
			<h2 ng-hide="producto.esNuevo"><small>Edición de Producto</small></h2>
		</div>
		<div class="operation_buttons" >
			<div class="btn-group" ng-hide="producto.esNuevo">
				<label class="btn btn-default" ng-click="producto.irASubproductos()" uib-tooltip="Subproductos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-link"></span></label>
				<label class="btn btn-default" ng-click="producto.irAActividades()" uib-tooltip="Actividades" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-th-list"></span></label>
				<label class="btn btn-default" ng-click="producto.irARiesgos()" uib-tooltip="Riesgos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-warning-sign"></span></label>
				<label class="btn btn-default" ng-click="producto.irAMetas()" uib-tooltip="Metas" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-scale"></span></label>
			</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="form.$valid ? producto.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="producto.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<div>
		<div class="col-sm-12">
			<form name="form" class="css-form">
					<div class="form-group">
						<label id="campo0" name="campo0" class="floating-label">ID {{ producto.producto.id }}</label>
						<br/><br/>
					</div>
								
					<div class="form-group">
						<input type="text" class="inputText" ng-model="producto.producto.nombre" ng-value="producto.producto.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" />
						<label class="floating-label">* Nombre</label> 
					</div>
					
					<div class="form-group"  >
						<input type="number"  class="inputText" ng-model="producto.producto.snip" ng-value="producto.producto.snip" onblur="this.setAttribute('value', this.value);">
						<label for="isnip" class="floating-label">SNIP</label>
					</div>
				
					<div class="form-group-row row" >
						<div class="form-group col-sm-2" >
						       <input type="number" class="inputText" ng-model="producto.producto.programa" ng-value="producto.producto.programa" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center" />
						       <label for="iprog" class="floating-label">Programa</label>
						</div>
						<div class="form-group col-sm-2" >
						  <input type="number" class="inputText" ng-model="producto.producto.subprograma" ng-value="producto.producto.subprograma" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center" />
						  <label for="isubprog" class="floating-label">Subprograma</label>
						</div>
						<div class="form-group col-sm-2" >
						  <input type="number" class="inputText" ng-model="producto.producto.proyecto_" ng-value="producto.producto.proyecto_" onblur="this.setAttribute('value', this.value);"  ng-maxlength="4" style="text-align: center" />
						  <label for="iproy_" class="floating-label">Proyecto</label>
						</div>
						<div class="form-group col-sm-2" >
						  <input type="number" class="inputText" ng-model="producto.producto.actividad" ng-value="producto.producto.actividad" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"  />
						  <label for="iproy_" class="floating-label">Actividad</label>
						</div>
						<div class="form-group col-sm-2" >
						  <input type="number" class="inputText" ng-model="producto.producto.obra" ng-value="producto.producto.obra" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iobra" class="floating-label">Obra</label>
						</div>
						<div class="form-group col-sm-2" >
						  <input type="number" class="inputText" ng-model="producto.producto.fuente" ng-value="producto.producto.fuente" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="campo5" class="floating-label">Fuente</label>
						</div>
					</div>
					
					<div class="form-group" >
			          <input type="text" class="inputText" ng-model="producto.tipoNombre" ng-value="producto.tipoNombre" 
			          	ng-click="producto.buscarTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
			           <span class="label-icon" ng-click="producto.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          <label for="campo3" class="floating-label">* Tipo</label>
			        </div>
			        
			        <div class="form-group">
			            <input type="text" class="inputText" ng-model="producto.unidadEjecutoraNombre" ng-value="producto.unidadEjecutoraNombre" 
			            	ng-click="producto.buscarUnidadEjecutora()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
			            <span class="label-icon" ng-click="producto.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          <label for="campo5" class="floating-label">* Unidad Ejecutora</label>
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
		<div class="col-sm-12 operation_buttons" align="right">
		
		<div class="label-form" align="center">Los campos marcados con * son obligatorios</div>
			<div class="btn-group">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="form.$valid ? producto.guardar() : ''" ng-disabled="!form.$valid" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="producto.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>