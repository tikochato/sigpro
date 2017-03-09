<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div ng-controller="controlSubproducto as subproducto" class="maincontainer all_page">

	<script type="text/ng-template" id="buscarPorProducto.jsp">
	    <%@ include file="/app/components/subproducto/buscarPorSubproducto.jsp"%>
	</script>
	<shiro:lacksPermission name="21010">
		<p ng-init="subproducto.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>

	<h3>{{ subproducto.esForma ? (subproducto.esNuevo ? "Nuevo Subproducto" : "Editar Subproducto") : "Subproducto" }}</h3>
	<h4>{{ subproducto.componenteNombre }}</h4><br/>

	<br />
  
	<div align="center" ng-hide="subproducto.esForma">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="21040">
					<label class="btn btn-primary" ng-click="subproducto.nuevo()">Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="21010">
					<label class="btn btn-primary" ng-click="subproducto.editar()">Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="21030">
					<label class="btn btn-primary" ng-click="subproducto.borrar()">Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="21010">
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

	<div ng-show="subproducto.esForma" class="row main-form">
		<h4 ng-hide="!subproducto.esNuevo">Nueva Subproducto</h4>
		<h4 ng-hide="subproducto.esNuevo">Edición de Subproducto</h4>
		<div class="col-sm-12 operation_buttons" align="left" ng-hide="subproducto.esNuevo">
			<div class="btn-group">
				<label class="btn btn-default" ng-click="subproducto.irAActividades()">Actividades</label>
			</div>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="form.$valid ? subproducto.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="subproducto.cancelar()">Ir a Tabla</label>
			</div>
		</div>
		<div>
		<div class="col-sm-12">
			<form name="form" class="css-form">
				
					<div class="form-group">
						<label>Id</label> 
						<p class="form-control-static" id="campo0" name="campo0" >{{subproducto.subproducto.id}}</p>
					</div>
				
				
					<div class="form-group">
						<label>* Nombre</label> 
						<input type="text" class="form-control" placeholder="Nombre del subproducto" ng-model="subproducto.subproducto.nombre" ng-required="true" />
					</div>
					
					<div class="form-group"  >
						<label for="isnip">SNIP</label>
						<input type="number"   ng-model="subproducto.subproducto.snip" class="form-control" placeholder="SNIP" >
					</div>
				
					<div class="form-group row" >
						<div class="form-group col-sm-2" >
						       <label for="iprog">Programa</label>
						       <input type="number" class="form-control" placeholder="Programa" ng-model="subproducto.subproducto.programa"  ng-maxlength="4" style="text-align: center" />
						</div>
						<div class="form-group col-sm-2" >
						  <label for="isubprog">Subprograma</label>
						  <input type="number" class="form-control" placeholder="Sub-programa" ng-model="subproducto.subproducto.subprograma" ng-maxlength="4" style="text-align: center" />
						</div>
						<div class="form-group col-sm-2" >
						  <label for="iproy_">Proyecto</label>
						  <input type="number" class="form-control" placeholder="Proyecto" ng-model="subproducto.subproducto.proyecto_"  ng-maxlength="4" style="text-align: center" />
						</div>
						<div class="form-group col-sm-2" >
						  <label for="iproy_">Actividad</label>
						  <input type="number" class="form-control" placeholder="Proyecto" ng-model="subproducto.subproducto.actividad" ng-maxlength="4" style="text-align: center"  />
						</div>
						<div class="form-group col-sm-2" >
						  <label for="iobra">Obra</label>
						  <input type="number" class="form-control" placeholder="Obra" ng-model="subproducto.subproducto.obra" ng-maxlength="4" style="text-align: center"/>
						</div>
						<div class="form-group col-sm-2" >
						  <label for="campo5">Fuente</label>
						  <input type="number" class="form-control" placeholder="Fuente" ng-model="subproducto.subproducto.fuente" ng-maxlength="4" style="text-align: center"/>
						</div>
					</div>

					<div class="form-group" >
						<label for="campo2"> Descripción</label> 
						<input type="text" class="form-control" placeholder="Descripcion del subproducto" ng-model="subproducto.subproducto.descripcion" />
					</div>
					
					<div class="form-group" >
			          <label for="campo3">* Tipo</label>
			          <div class="input-group"> 
			            <input type="text" class="form-control" placeholder="Tipo de subproducto" ng-model="subproducto.tipoNombre" ng-readonly="true" ng-required="true"/>
			            <span class="input-group-addon" ng-click="subproducto.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>
			        
			        <div class="form-group">
			          <label for="campo5">Unidad Ejecutora</label>
			          <div class="input-group"> 
			            <input type="text" class="form-control" placeholder="Unidad Ejecutora" ng-model="subproducto.unidadEjecutoraNombre" ng-readonly="true" ng-required="true"/>
			            <span class="input-group-addon" ng-click="subproducto.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>
			        
			        <div class="form-group" ng-repeat="campo in subproducto.camposdinamicos">
						<label for="campo.id">{{ campo.label }}</label>
						<div ng-switch="campo.tipo">
							<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" placeholder="{{ campo.label }}" />
							<input ng-switch-when="entero" type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" placeholder="{{ campo.label }}"/>
							<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control"placeholder="{{ campo.label }}" />
							<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" placeholder="{{ campo.label }}"/>
							<p ng-switch-when="fecha" class="input-group">
								<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{subproducto.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
										datepicker-options="subproducto.fechaOptions" close-text="Cerrar" placeholder="{{ campo.label }}"/>
								<span class="input-group-btn">
									<button type="button" class="btn btn-default" ng-click="subproducto.abrirPopupFecha($index)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
					</div>
					
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label>
				  					<p class="form-control-static">{{ subproducto.subproducto.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion">Fecha de creación</label>
				  					<p class="form-control-static">{{ subproducto.subproducto.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label>
				  					<p class="form-control-static">{{ subproducto.subproducto.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label>
				  					<p class="form-control-static">{{ subproducto.subproducto.fechaactualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			</div>
		</div>
		<br />
		<div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>
		<br />
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="21020">
					<label class="btn btn-success" ng-click="form.$valid ? subproducto.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="subproducto.cancelar()">Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>