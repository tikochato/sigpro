<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<style type="text/css">
	.anchocolumna {
	 	width: 20%;
	}
</style>
<div ng-controller="proyectoController as controller"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarPorProyecto.jsp">
    		<%@ include file="/app/components/proyecto/buscarPorProyecto.jsp"%>
  	 </script>

	<h3>Proyectos</h3>
	<br />
	<div class="row" align="center" ng-hide="controller.esColapsado">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="crearProyecto">
				<label class="btn btn-primary" ng-click="controller.nuevo()">Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="editarProyecto">
				<label class="btn btn-primary" ng-click="controller.editar()">Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="eliminarProyecto">
				<label class="btn btn-primary" ng-click="controller.borrar()">Borrar</label>
			</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="verProyecto">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
					<shiro:hasPermission name="verProyecto">
						<a class="btn btn-default" href ng-click="controller.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</shiro:hasPermission>

					</div>
				</div>
			</div>

			<div id="grid1" ui-grid="controller.gridOpciones" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination
				ui-grid-pagination>
				<div class="grid_loading" ng-hide="!controller.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
			</div>
				<ul uib-pagination total-items="controller.totalProyectos"
						ng-model="controller.paginaActual"
						max-size="controller.numeroMaximoPaginas"
						items-per-page="controller.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="controller.cambioPagina()"
				></ul>
		</div>

		</shiro:hasPermission>


	</div>
	<div class="row" ng-show="controller.esColapsado">
		<h4 ng-hide="!controller.esNuevo">Nuevo Proyecto</h4>
		<h4 ng-hide="controller.esNuevo">Edición de proyecto</h4>
		<div class="col-sm-12 operation_buttons" align="left">
			<div class="btn-group">
				<label class="btn btn-default" ng-click="controller.irAComponentes(controller.proyecto.id)">Componentes</label>
				<!-- productos -->
				<!-- <label class="btn btn-default" ng-click="controller.irAActividades(controller.proyecto.id)">Actividades</label>-->
				<label class="btn btn-default" ng-click="controller.irAHitos(controller.proyecto.id)">Hitos</label>
				<!-- metas -->
				<label class="btn btn-default" ng-click="controller.irADesembolsos(controller.proyecto.id)">Desembolsos</label>
				<label class="btn btn-default" ng-click="controller.irARiesgos(controller.proyecto.id)">Riesgos</label>
				<!--  recursos -->

			</div>
		</div>

		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid">Guardar</label>
				<label class="btn btn-primary" ng-click="controller.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id">ID</label>
  					<label class="form-control" id="id">{{ controller.proyecto.id }}</label>
				</div>
				<div class="form-group" show-errors >
					<label for="inombre">* Nombre</label>
					<input type="text" name="inombre" id="inombre" ng-model="controller.proyecto.nombre"
						class="form-control" placeholder="Nombre" ng-required="true" >
				</div>

				<div class="form-group"  >
					<label for="isnip">* SNIP</label>
					<input type="number" name="isnip" id="isnip"  ng-model="controller.proyecto.snip"
						class="form-control" placeholder="Nombre" ng-required="true">
				</div>
				
				<div class="form-group row" >
					<div class="form-group col-sm-2" >
					       <label for="iprog">Programa</label>
					       <input type="number" class="form-control" placeholder="Programa" ng-model="controller.programa"  />
					</div>
					<div class="form-group col-sm-2" >
					  <label for="isubprog">Subprograma</label>
					  <input type="number" class="form-control" ng-model="controller.subprograma" />
					</div>
					<div class="form-group col-sm-2" >
					  <label for="iproy_">Proyecto</label>
					  <input type="number" class="form-control" ng-model="controller.proyecto_"  />
					</div>
					<div class="form-group col-sm-2" >
					  <label for="iobra">Actividad</label>
					  <input type="number" class="form-control" ng-model="controller.actividad" ng-maxlength="4"/>
					</div>
					<div class="form-group col-sm-2" >
					  <label for="iobra">Obra</label>
					  <input type="number" class="form-control" ng-model="controller.obra" ng-maxlength="4"/>
					</div>
					<div class="form-group col-sm-2" >
					  <label for="campo5">Fuente</label>
					  <input type="number" class="form-control" ng-model="controller.fuente" ng-maxlength="4"/>
					</div>
				</div>
				<div class="form-group" >
					<label for="campo3">* Tipo Proyecto</label>
		          	<div class="input-group">
		            	<input type="text" class="form-control" id="iproyt" name="iproyt" placeholder="Nombre Tipo Proyecto" ng-model="controller.proyectotiponombre" ng-readonly="true" ng-required="true"/>
		            	<span class="input-group-addon" ng-click="controller.buscarProyectoTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          	</div>
				</div>

				<div class="form-group" ng-repeat="campo in controller.camposdinamicos">
							<label for="campo.id">{{ campo.label }}</label>
							<div ng-switch="campo.tipo">
								<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="entero" type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
								<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
								<p ng-switch-when="fecha" class="input-group">
									<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"/><span
														class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="controller.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
								</p>
							</div>
						</div>

				<div class="form-group">
					<label for="campo3">* Unidad Ejecutora</label>
		          	<div class="input-group">
		            	<input type="text" class="form-control" id="iunie" name="iunie" placeholder="Nombre Unidad Ejecutora" ng-model="controller.unidadejecutoranombre" ng-readonly="true" ng-required="true"/>
		            	<span class="input-group-addon" ng-click="controller.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
		          	</div>
				</div>

				<div class="form-group" >
					<label for="campo3">* Cooperante</label>
		          	<div class="input-group">
		            	<input type="hidden" class="form-control" ng-model="controller.cooperanteid" />
		            	<input type="text" class="form-control" id="icoope" name="icoope" placeholder="Nombre Cooperante" ng-model="controller.cooperantenombre" ng-readonly="true" ng-required="true"/>
		            	<span class="input-group-addon" ng-click="controller.buscarCooperante()"><i class="glyphicon glyphicon-search"></i></span>
		          	</div>
				</div>

				<div class="form-group">
					<label for="campo2">Descripción</label>
					<input type="text" ng-model="controller.proyecto.descripcion"
						class="form-control" id="campo2" placeholder="Descripción">
				</div>

				<div class="form-group">
					<label for="usuarioCreo">Usuario que creo</label>
  					<p class="form-control-static">{{ controller.proyecto.usuarioCreo }}</pl>
				</div>
				<div class="form-group">
					<label for="fechaCreacion">Fecha de creación</label>
  					<p class="form-control-static">{{ controller.proyecto.fechaCreacion }}</p>
				</div>
				<div class="form-group">
					<label for="usuarioActualizo">Usuario que actualizo</label>
  					<p class="form-control-static">{{ controller.proyecto.usuarioactualizo }}</p>
				</div>
				<div class="form-group">
					<label for="fechaActualizacion">Fecha de actualizacion</label>
  					<p class="form-control-static">{{ controller.proyecto.fechaactualizacion }}</p>
				</div>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid">Guardar</label>
				<label class="btn btn-primary" ng-click="controller.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>
