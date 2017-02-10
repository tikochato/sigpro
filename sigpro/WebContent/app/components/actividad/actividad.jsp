<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="actividadController as actividadc" class="maincontainer all_page" id="title">
	    <script type="text/ng-template" id="buscarActividadTipo.jsp">
    		<%@ include file="/app/components/actividad/buscarActividadTipo.jsp"%>
  	    </script>
		<h3>Actividades</h3><br/>
		<h4>{{ actividadc.objetoTipoNombre }} {{ actividadc.objetoNombre }}</h4><br/>
		<div class="row" align="center" ng-hide="actividadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="crearCooperante">
			       		<label class="btn btn-primary" ng-click="actividadc.nuevo()">Nueva</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="editarCooperante"><label class="btn btn-primary" ng-click="actividadc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="eliminarCooperante">
			       		<label class="btn btn-primary" ng-click="actividadc.borrar()">Borrar</label>
			       </shiro:hasPermission>


    			</div>
    		</div>
    		<shiro:hasPermission name="verCooperante">
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
				<ul uib-pagination total-items="actividadc.totalCooperantes"
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
		<div class="row main-form" ng-show="actividadc.mostraringreso">
			<h4 ng-hide="!actividadc.esnuevo">Nueva actividad</h4>
			<h4 ng-hide="actividadc.esnuevo">Edición de actividad</h4>
			<div class="col-sm-12 operation_buttons" align="left">
		</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? actividadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
			        <label class="btn btn-primary" ng-click="actividadc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id">ID</label>
    						<label class="form-control" id="id">{{ actividadc.actividad.id }}</label>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" name="nombre" placeholder="Nombre" ng-model="actividadc.actividad.nombre" ng-required="true">
						</div>

						<div class="form-group">
							<label for="campo3">* Tipo de Actividad</label>
				          	<div class="input-group">
				            	<input type="hidden" class="form-control" ng-model="actividadc.actividadtipoid" />
				            	<input type="text" class="form-control" placeholder="Tipo de Actividad" ng-model="actividadc.actividad.actividadtiponombre" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="actividadc.buscarActividadTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="actividadc.actividad.descripcion">
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<label>* Fecha de Inicio</label>
		    						<p class="input-group">
									<input type="text" class="form-control" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="actividadc.actividad.fechaInicio" is-open="actividadc.fi_abierto"
														datepicker-options="actividadc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Inicio" ng-change="actividadc.actualizarfechafin()" ng-required="true" />
														<span class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="actividadc.abrirPopupFecha(1000)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
									</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label>* Fecha de Fin</label>
		    						<p class="input-group">
									<input type="text" class="form-control" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="actividadc.actividad.fechaFin" is-open="actividadc.ff_abierto"
														datepicker-options="actividadc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Fin" ng-required="true" /><span
														class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="actividadc.abrirPopupFecha(1001)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
									</p>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="descripcion">Avance %</label>
    						<input type="number" class="form-control" placeholder="% de Avance" ng-model="actividadc.actividad.porcentajeavance" ng-required="true" min="0" max="100">
						</div>
						<div class="form-group row" >
							<div class="form-group col-sm-2" >
							       <label for="iprog">Programa</label>
							       <input type="number" class="form-control" placeholder="Programa" ng-model="actividadc.actividad.programa" ng-maxlength="4" style="text-align: center"/>
							</div>
							<div class="form-group col-sm-2" >
							  <label for="isubprog">Subprograma</label>
							  <input type="number" class="form-control" placeholder="Subprograma" ng-model="actividadc.actividad.subprograma" ng-maxlength="4" style="text-align: center"/>
							</div>
							<div class="form-group col-sm-2" >
							  <label for="iproy_">Proyecto</label>
							  <input type="number" class="form-control" placeholder="Proyecto" ng-model="actividadc.actividad.proyecto" ng-maxlength="4" style="text-align: center"/>
							</div>
							<div class="form-group col-sm-2" >
							  <label for="iobra">Actividad</label>
							  <input type="number" class="form-control" placeholder="Actividad" ng-model="actividadc.actividad.actividad" ng-maxlength="4" style="text-align: center"/>
							</div>
							<div class="form-group col-sm-2" >
							  <label for="iobra">Obra</label>
							  <input type="number" class="form-control" placeholder="Obra" ng-model="actividadc.actividad.obra" ng-maxlength="4" style="text-align: center"/>
							</div>
							<div class="form-group col-sm-2" >
							  <label for="campo5">Fuente</label>
							  <input type="number" class="form-control" placeholder="Fuente" ng-model="actividadc.actividad.fuente" ng-maxlength="4" style="text-align: center"/>
							</div>
						</div>
						<div class="form-group" ng-repeat="campo in actividadc.camposdinamicos">
							<label for="campo.id">{{ campo.label }}</label>
							<div ng-switch="campo.tipo">
								<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="entero" type="text" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
								<p ng-switch-when="fecha" class="input-group">
									<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="mi.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" /><span
														class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="actividadc.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
								</p>
								<select ng-switch-when="select" id="{{ 'campo_'+campo.id }}" class="form-control" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														value="{{number.valor}}">{{number.label}}</option>
								</select>
							</div>
						</div>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label>
				  					<p class="form-control-static">{{ actividadc.actividad.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion">Fecha de creación</label>
				  					<p class="form-control-static">{{ actividadc.actividad.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label>
				  					<p class="form-control-static">{{ actividadc.actividad.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label>
				  					<p class="form-control-static">{{ actividadc.actividad.fechaactualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
							
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
				        <label class="btn btn-success" ng-click="form.$valid ? actividadc.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
				        <label class="btn btn-primary" ng-click="actividadc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
