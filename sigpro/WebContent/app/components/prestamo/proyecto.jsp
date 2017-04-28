<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="proyectoController as controller"
	class="maincontainer all_page" id="title">
		
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

	<script type="text/ng-template" id="buscarPorProyecto.jsp">
    		<%@ include file="/app/components/prestamo/buscarPorProyecto.jsp"%>
  	 </script>
  	 <script type="text/ng-template" id="cargarArchivo.jsp">
    		<%@ include file="/app/components/prestamo/cargarArchivo.jsp"%>
  	 </script>
	<shiro:lacksPermission name="24010">
		<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Préstamos</h3></div>
	</div>
	
	<div class="row" align="center" ng-hide="controller.esColapsado">
		<br>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.cargarArchivo()">Desde archivo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.nuevo()" title="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24010">
				<label class="btn btn-primary" ng-click="controller.editar()" title="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24030">
				<label class="btn btn-danger" ng-click="controller.borrar()" title="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</shiro:hasPermission>
			</div>
			<div class="btn-group" role="group" aria-label="">
					<shiro:hasPermission name="24010">
						<a class="btn btn-default" href ng-click="controller.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</shiro:hasPermission>
					</div>
				</div>
		<shiro:hasPermission name="24010">
		<div class="col-sm-12" align="center">
			<br/>
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
			<br/>
			<div class="total-rows">Total de {{  controller.totalProyectos + (controller.totalProyectos == 1 ? " Préstamo" : " Préstamos" ) }}</div>
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
	<div class="row second-main-form" ng-show="controller.esColapsado">
		<div class="page-header">
			<h1 ng-hide="!controller.esNuevo"><small>Nuevo Préstamo</small></h1>
			<h1 ng-hide="controller.esNuevo"><small>Edición de Préstamo</small></h1>
			</div>
		<div class="operation_buttons">
			<div class="btn-group" ng-hide="controller.esNuevo">
				<label class="btn btn-default" ng-click="controller.irAComponentes(controller.proyecto.id)" title="Componentes">
				<span class="glyphicon glyphicon-th"></span> Componentes</label>
				<label class="btn btn-default" ng-click="controller.irAHitos(controller.proyecto.id)" title="Hitos">
				<span class="glyphicon glyphicon-screenshot"></span> Hitos</label>
				<label class="btn btn-default" ng-click="controller.irADesembolsos(controller.proyecto.id)" title="Desembolsos">
				<span class="glyphicon glyphicon-shopping-cart"></span> Desembolsos</label>
				<label class="btn btn-default" ng-click="controller.irARiesgos(controller.proyecto.id)" title="Riesgos">
				<span class="glyphicon glyphicon-warning-sign"></span> Riesgos</label>
				<label class="btn btn-default" ng-click="controller.irAGantt(controller.proyecto.id)" title="Gantt">
				<span class="glyphicon glyphicon glyphicon-indent-left"></span> Gantt</label>
				<label class="btn btn-default" ng-click="controller.irAActividades(controller.proyecto.id)" title="Actividades">
				<span class="glyphicon glyphicon-th-list"></span> Actividades</label>
				<label class="btn btn-default" ng-click="controller.irAMapa(controller.proyecto.id)">Mapa</label>
				<label class="btn btn-default" ng-click="controller.irAGantt(controller.proyecto.id)">Gantt</label>
				<label class="btn btn-default" ng-click="controller.irAKanban(controller.proyecto.id)">Kanban</label>
				<label class="btn btn-default" ng-click="controller.irAAgenda(controller.proyecto.id)">Agenda</label>
				<label class="btn btn-default" ng-click="controller.irAMatrizRiesgos(controller.proyecto.id)">Matriz de Riesgos</label>
			
		</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid" title="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="controller.irATabla()" title="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label">ID {{ controller.proyecto.id }}</label>
					<br/><br/>
				</div>
				
				<div class="form-group">      
			      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="controller.proyecto.nombre" value="{{controller.proyecto.nombre}}" onblur="this.setAttribute('value', this.value);" ng-required="true" >
			      <label class="floating-label">* Nombre</label>
			    </div>

				<div class="form-group">   
					<input type="number" class="inputText" name="isnip" id="isnip"  ng-model="controller.proyecto.snip" value="{{controller.proyecto.snip}}" onblur="this.setAttribute('value', this.value);">
				      <label class="floating-label">SNIP</label>
				</div>
				
				<div class="form-group-row row" >
					<div class="form-group col-sm-2" >
					       <input type="number" class="inputText" ng-model="controller.proyecto.programa" value="{{controller.proyecto.programa}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center" />
					       <label for="iprog" class="floating-label">Programa</label>
					</div>
					<div class="form-group col-sm-2" >
					  <input type="number" class="inputText" ng-model="controller.proyecto.subprograma" value="{{controller.proyecto.subprograma}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
					  <label for="isubprog" class="floating-label">Subprograma</label>
					</div>
					<div class="form-group col-sm-2" >
					  <input type="number" class="inputText" ng-model="controller.proyecto.proyecto" value="{{controller.proyecto.proyecto}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
					  <label for="iproy_" class="floating-label">Proyecto</label>
					</div>
					<div class="form-group col-sm-2" >
					  <input type="number" class="inputText" ng-model="controller.proyecto.actividad" value="{{controller.proyecto.actividad}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
					  <label for="iobra" class="floating-label">Actividad</label>
					</div>
					<div class="form-group col-sm-2" >
					  <input type="number" class="inputText" ng-model="controller.proyecto.obra" value="{{controller.proyecto.obra}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
					  <label for="iobra" class="floating-label">Obra</label>
					</div>
					<div class="form-group col-sm-2" >
					  <input type="number" class="inputText" ng-model="controller.proyecto.fuente" value="{{controller.proyecto.fuente}}" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
					  <label for="campo5" class="floating-label">Fuente</label>
					</div>
				</div>
				<div class="form-group" >
		            	<input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="controller.proyectotiponombre" value="{{controller.proyectotiponombre}}" 
		            		ng-click="controller.buscarProyectoTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
		            	<span class="label-icon" ng-click="controller.buscarProyectoTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          	<label for="campo3" class="floating-label">* Tipo Préstamo</label>
				</div>
				
				<div ng-repeat="campo in controller.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									value="{{ccampo.valor}}" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="controller.abrirPopupFecha($index)"
														value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="controller.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor">
														<option value="">Seleccione una opción</option>
														<option ng-repeat="number in campo.opciones"
															value="{{number.valor}}">{{number.label}}</option>
									</select>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
						</div>

				<div class="form-group">
		            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="controller.unidadejecutoranombre" ng-readonly="true" ng-required="true" 
		            	ng-click="controller.buscarUnidadEjecutora()" value="{{controller.unidadejecutoranombre}}" onblur="this.setAttribute('value', this.value);"/>
		            <span class="label-icon" ng-click="controller.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
		          	<label for="campo3" class="floating-label">* Unidad Ejecutora</label>
				</div>

				<div class="form-group" >
		            	<input type="hidden" class="form-control" ng-model="controller.cooperanteid" />
		            	<input type="text" class="inputText" id="icoope" name="icoope" ng-model="controller.cooperantenombre" ng-readonly="true" ng-required="true" 
		            		ng-click="controller.buscarCooperante()" value="{{controller.cooperantenombre}}" onblur="this.setAttribute('value', this.value);"/>
		            	<span class="label-icon" ng-click="controller.buscarCooperante()"><i class="glyphicon glyphicon-search"></i></span>
		          	<label for="campo3" class="floating-label">* Cooperante</label>
				</div>
				
				<div class="form-group">
		            	<input type="text" class="inputText" placeholder="Latitud, Longitud" ng-model="controller.coordenadas" ng-readonly="true" 
		            		value="{{controller.coordenadas}}" onblur="this.setAttribute('value', this.value);"/>
		            	<span class="label-icon" ng-click="controller.open(controller.proyecto.latitud, controller.proyecto.longitud); "><i class="glyphicon glyphicon-map-marker"></i></span>
			          	<label class="floating-label">Coordenadas</label>
				</div>

				<div class="form-group">
					<input type="text" ng-model="controller.proyecto.descripcion"
						class="inputText" id="campo2" 
						value="{{controller.proyecto.descripcion}}" onblur="this.setAttribute('value', this.value);">
					<label for="campo2" class="floating-label">Descripción</label>
				</div>
				<br/>
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label>
				  					<p class="form-control-static">{{ controller.proyecto.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion">Fecha de creación</label>
				  					<p class="form-control-static">{{ controller.proyecto.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label>
				  					<p class="form-control-static">{{ controller.proyecto.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label>
				  					<p class="form-control-static">{{ controller.proyecto.fechaactualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid" title="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="controller.irATabla()" title="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>
