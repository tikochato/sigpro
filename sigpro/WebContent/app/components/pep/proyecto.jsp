<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style type="text/css">
		.filaIngreso  > td{
			padding: 0px !important;
			padding-left: 8px !important;
			padding-right: 8px !important;
			vertical-align: middle !important; 
		}
		
		.filaIngreso > td > table > tbody > tr > td > input {
			border-bottom: none !important;
		}
		
		.filaIngreso > td > table > tbody > tr > td > input:focus {
			border-bottom: 2px solid rgb(63, 81, 181) !important;
		}
	
		.divTabla{
		    width: 100%;
		    height: 100%;
		    overflow-y: hidden;
		    margin-top: 40px;
		    overflow-x: auto;
		}
	</style>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="proyectoController as controller"
	ng-class=" controller.esTreeview ? 'maincontainer_treeview all_page' : 'maincontainer all_page'" id="title">
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
			 <div class="btn-group">
				<label class="btn btn-success" ng-click="ok()"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="cancel()">Quitar</label>
			<div class="btn-group">
        </div>
    </script>
	<script type="text/ng-template" id="buscarPorProyecto.jsp">
    		<%@ include file="/app/components/pep/buscarPorProyecto.jsp"%>
  	 </script>
  	 <script type="text/ng-template" id="cargarArchivo.jsp">
    		<%@ include file="/app/components/pep/cargarArchivo.jsp"%>
  	 </script>
  	 <script type="text/ng-template" id="agregarImpacto.jsp">
    		<%@ include file="/app/components/pep/agregarImpacto.jsp"%>
  	 </script>
  	 <script type="text/ng-template" id="generarReporte.jsp">
    		<%@ include file="/app/components/pep/generarReporte.jsp"%>
  	 </script>
  	 <script type="text/ng-template" id="congelar.jsp">
    		<%@ include file="/app/components/pep/congelar.jsp"%>
  	 </script>
	<shiro:lacksPermission name="24010">
		<span ng-init="controller.redireccionSinPermisos()"></span>
	</shiro:lacksPermission>
	<shiro:hasPermission name="43010">
		<span ng-init="controller.cambioOrden()"></span>
	</shiro:hasPermission>
	<div class="panel panel-default" ng-if="!controller.esTreeview">
	  <div class="panel-heading"><h3>{{etiquetas.proyecto}}s</h3></div>
	</div>
	<div class="subtitulo" ng-if="!componentec.esTreeview">
		{{ controller.objetoTipoNombre }} {{ controller.prestamoNombre }}
	</div>
	<div align="center" ng-hide="controller.esColapsado" ng-if="!controller.esTreeview">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.congelado?'':controller.nuevo()" ng-disabled="controller.congelado" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.congelado?'':controller.cargarArchivo()" ng-disabled="controller.congelado" uib-tooltip="Importar PEP desde project">   
				<span class="glyphicon glyphicon glyphicon-file"></span>Project</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24020">
				<label class="btn btn-primary" ng-click="controller.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24030">
				<label class="btn btn-danger" ng-click="controller.congelado?'':controller.borrar()" ng-disabled="controller.congelado" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</shiro:hasPermission>
			</div>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
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
			<div class="total-rows">Total de {{  controller.totalProyectos + (controller.totalProyectos == 1 ? " "+etiquetas.proyecto : " "+etiquetas.proyecto+"s" ) }}</div>
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
	<div class="row second-main-form" ng-show="controller.esColapsado || controller.esTreeview">
		<div class="page-header">
			<h2 ng-if="controller.esNuevo"><small>Nuevo {{etiquetas.proyecto}}</small></h2>
			<h2 ng-if="!controller.esNuevo"><small>Edición de {{etiquetas.proyecto}}</small></h2>
			</div>
		<div class="operation_buttons">
			<div class="btn-group" ng-hide="controller.esNuevo">
				<label class="btn btn-default" ng-if="!controller.esTreeview" ng-click="controller.botones ? controller.irAComponentes(controller.proyecto.id) : ''" uib-tooltip="Componentes" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-th"></span></label>
				<label class="btn btn-default" ng-if="!controller.esTreeview" ng-hide="true" ng-click="controller.botones ? controller.irAHitos(controller.proyecto.id) : ''" uib-tooltip="Hitos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-screenshot"></span></label>
				<label class="btn btn-default" ng-if="!controller.esTreeview" ng-click="controller.botones ? controller.irAActividades(controller.proyecto.id) : ''" uib-tooltip="Actividades" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-time"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.irAMapa(controller.proyecto.id) : ''" uib-tooltip="Mapa" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-globe"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.irAGantt(controller.proyecto.id) : ''" uib-tooltip="Ir a Gantt" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-indent-left"></span></label>
				<label class="btn btn-default" ng-click="controller.botones  ? controller.completarConArchivo(controller.proyecto.id) : ''"
					ng-if="controller.proyecto.projectCargado!=1" uib-tooltip="Importar PEP desde Project" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-copy"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.exportar() : ''" uib-tooltip="Exportar PEP a Project" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-paste"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.irAKanban(controller.proyecto.id) : ''" uib-tooltip="Cartelera de Actividades" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-blackboard"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.irAAgenda(controller.proyecto.id) : ''" uib-tooltip="Agenda" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-calendar"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.irAMatrizRiesgos(controller.proyecto.id) : ''" uib-tooltip="Matriz de Riesgos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span></label>
				<label class="btn btn-default" ng-click="controller.botones && !controller.congelado ? controller.calcularCostoFecha(controller.proyecto.id) : ''" ng-disabled="controller.congelado" uib-tooltip="Cálculo de costos y fechas" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-wrench"></span></label>
				<label class="btn btn-default" ng-click="controller.botones ? controller.irAMiembrosUnidadEjecutora(controller.proyecto.id) : ''" uib-tooltip="Miembros de la Unidad Ejecutora" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-user"></span></label>
				<label class="btn btn-default" ng-click="controller.generarReporte()" uib-tooltip="Plan Anual de Ejecución">
				<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
				<label class="btn btn-default" ng-click="controller.congelado?'':controller.congelar()" uib-tooltip="Congelar línea base" ng-disabled="controller.congelado">
				<span class="glyphicon glyphicon glyphicon-bookmark" aria-hidden="true"></span></label>
				<label class="btn btn-default" ng-click="controller.verHistoria()" uib-tooltip="Ver Historia">
				<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
		</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="controller.mForm.$valid && controller.botones ? controller.guardar(form.$valid) : ''" ng-disabled="!controller.mForm.$valid || !controller.botones" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label ng-if="!controller.esTreeview" class="btn btn-primary" ng-click="controller.botones ? controller.irATabla() : ''" uib-tooltip="Ir a Tabla" tooltip-placement="bottom" ng-disabled="!controller.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="controller.esTreeview" class="btn btn-danger" ng-click=" controller.botones && !controller.congelado ? controller.t_borrar() : ''" ng-disabled="!(controller.proyecto.id>0) || !controller.botones || controller.congelado" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" ng-if="controller.proyecto.projectCargado==1">
				<div class="componente_sigade">Estructura importada desde un archivo de Project</div>
			</div>
		<br>
		<div class="col-sm-12">
			<form name="controller.mForm" style="margin-top: 10px;">
			<uib-tabset active="controller.active">
				<uib-tab index="0" heading="Generales" >
					<div class="form-group">
						<label for="id" class="floating-label id_class">ID {{ controller.proyecto.id }}</label>
						<br/><br/>
					</div>
					
					<div class="form-group" >
				      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="controller.proyecto.nombre" ng-value="controller.proyecto.nombre" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-required="true" >
				      <label class="floating-label">* Nombre</label>
					</div>
	
					<div class="form-group"  ng-hide="true"  >
						<input type="number" class="inputText" name="isnip" id="isnip"  ng-model="controller.proyecto.snip" ng-value="controller.proyecto.snip" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);">
					      <label class="floating-label">SNIP</label>
					</div>
					
					<div class="form-group-row row" ng-hide="true"  >
						<div class="form-group col-sm-2" >
						       <input type="number" class="inputText" ng-model="controller.proyecto.programa" ng-value="controller.proyecto.programa" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center" />
						       <label for="iprog" class="floating-label">Programa</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false" >
						  <input type="number" class="inputText" ng-model="controller.proyecto.subprograma" ng-value="controller.proyecto.subprograma" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="isubprog" class="floating-label">Subprograma</label>
						</div>
						<div class="form-group col-sm-2"  ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.proyecto" ng-value="controller.proyecto.proyecto" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iproy_" class="floating-label">Proyecto</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.actividad" ng-value="controller.proyecto.actividad" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iobra" class="floating-label">Actividad</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.obra" ng-value="controller.proyecto.obra" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iobra" class="floating-label">Obra</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.fuente" ng-value="controller.proyecto.fuente" ng-readonly="controller.congelado" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="campo5" class="floating-label">Fuente</label>
						</div>
					</div>
						
					<div class="form-group" >
			            	<input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="controller.proyectotiponombre" ng-value="controller.proyectotiponombre" 
			            		ng-click="controller.congelado?'':controller.buscarProyectoTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
			            	<span class="label-icon" ng-click="controller.congelado?'':controller.buscarProyectoTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label for="campo3" class="floating-label">* Caracterización {{etiquetas.proyecto}}</label>
					</div>
					
					<div class="form-group" ng-show="controller.unidadejecutoranombre.length>0"  >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="controller.entidadnombre" ng-readonly="true"  
			            	 ng-value="controller.entidadnombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
					</div>
	
					<div class="form-group" >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="controller.unidadejecutoranombre" ng-readonly="true"
			            	ng-click="controller.prestamoid != null ? '' : controller.buscarUnidadEjecutora()" ng-value="controller.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
			            <span class="label-icon" ng-click="controller.prestamoid != null ? '' : controller.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label for="campo3" class="floating-label">Unidad Ejecutora</label>
					</div>
	
					<div class="form-group" ng-if="false"  >
			            	<input type="text" class="inputText" id="icoope" name="icoope" ng-model="controller.cooperantenombre" ng-readonly="true" ng-required="true" 
			            		ng-click="controller.buscarCooperante()" ng-value="controller.cooperantenombre" onblur="this.setAttribute('value', this.value);"/>
			            	<span class="label-icon" ng-click="controller.buscarCooperante()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label for="campo3" class="floating-label">* Organismo financiero internacional</label>
					</div>
					
					<div class="form-group" ng-if="false" >
			            	<input type="text" class="inputText" ng-model="controller.coordenadas" ng-readonly="true" 
			            		ng-value="controller.coordenadas" onblur="this.setAttribute('value', this.value);"
			            		ng-click="controller.open(controller.proyecto.latitud, controller.proyecto.longitud); "/>
			            	<span class="label-icon" ng-click="controller.open(controller.proyecto.latitud, controller.proyecto.longitud); "><i class="glyphicon glyphicon-map-marker"></i></span>
				          	<label class="floating-label">Coordenadas</label>
					</div>
					
					<div class="form-group" ng-if="false" >
							<input  ng-model="controller.proyecto.objetivo"
								class="inputText" id="objetivo"
								ng-value="controller.proyecto.objetivo" onblur="this.setAttribute('value', this.value);">
							<label for="objetivo" class="floating-label">Objetivo</label>
					</div>
					
					<div class="form-group" ng-if="false" >
							<input  ng-model="controller.proyecto.objetivoEspecifico"
								class="inputText" 
								ng-value="controller.proyecto.objetivoEspecifico" onblur="this.setAttribute('value', this.value);">
							<label for="objetivo" class="floating-label">Objetivos específicos del PEP</label>
					</div>
					
					<div class="form-group" ng-if="false" >
							<input  ng-model="controller.proyecto.visionGeneral"
								class="inputText" 
								ng-value="controller.proyecto.visionGeneral" onblur="this.setAttribute('value', this.value);">
							<label for="objetivo" class="floating-label">Visión general del PEP</label>
					</div>
					<div ng-repeat="campo in controller.camposdinamicos" >
						<div ng-switch="campo.tipo">
							<div ng-switch-when="texto" class="form-group" >
								<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);" ng-readonly="controller.congelado"/>	
								<label for="campo.id" class="floating-label">{{ campo.label }}</label>
							</div>
							<div ng-switch-when="entero" class="form-group" >
								<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
								ng-value="campo.valor" onblur="this.setAttribute('value', this.value);" ng-readonly="controller.congelado"/>
								<label for="campo.id" class="floating-label">{{ campo.label }}</label>
							</div>
							<div ng-switch-when="decimal" class="form-group" >
								<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
								ng-value="campo.valor" onblur="this.setAttribute('value', this.value);" ng-readonly="controller.congelado"/>
								<label for="campo.id" class="floating-label">{{ campo.label }}</label>
							</div>
							<div ng-switch-when="booleano" class="form-group" >
								<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" ng-readonly="controller.congelado"/>
								<label for="campo.id" class="floating-label">{{ campo.label }}</label>
							</div>
							<div ng-switch-when="fecha" class="form-group" >
								<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" alt-input-formats="{{controller.altformatofecha}}"
													ng-model="campo.valor" is-open="campo.isOpen"  ng-readonly="controller.congelado"
													datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
													ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"
													/>
													<span class="label-icon" ng-click="controller.congelado?'':controller.abrirPopupFecha($index)">
														<i class="glyphicon glyphicon-calendar"></i>
													</span>
								<label for="campo.id" class="floating-label">{{ campo.label }}</label>
							</div>
							<div ng-switch-when="select" class="form-group" >
								<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor" ng-disabled="controller.congelado">
												<option value="">Seleccione una opción</option>
												<option ng-repeat="number in campo.opciones"
													ng-value="number.valor">{{number.label}}</option>
							</select>
								<label for="campo.id" class="floating-label">{{ campo.label }}</label>
							</div>
						</div>
					</div>
					<br/>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="number" style="text-align: right;"
								 class="inputText "  
								 ng-model="controller.proyecto.ejecucionFisicaReal"
								 ng-value="controller.proyecto.ejecucionFisicaReal"
								 onblur="this.setAttribute('value', this.value);"
								 min="0" max="100">
								<label class="floating-label" >Ejecucion Física %</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group money-input">
								<input type="text" 
								 class="inputText input-money"  
								 ng-model="controller.proyecto.costo"
								 ng-value="controller.proyecto.costo"
								 onblur="this.setAttribute('value', this.value);" 
								 ui-number-mask="2"
								 ng-readonly="true"
								 >
								<label class="floating-label" >Costo</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group money-input">
								<input type="text" class="inputText "  
								 ng-model="controller.fechaInicioTemp"
								 ng-value="controller.fechaInicioTemp"
								 onblur="this.setAttribute('value', this.value);"
								 ng-readonly="true"
								 >
								<label class="floating-label" >Fecha Inicio</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group money-input">
								<input type="text" class="inputText "  
								 ng-model="controller.fechaFinalTemp"
								 ng-value="controller.fechaFinalTemp"
								 onblur="this.setAttribute('value', this.value);"
								 ng-readonly="true"
								 >
								<label class="floating-label" >Fecha Fin</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group money-input">
								<input type="text" class="inputText "  
								 ng-model="controller.fechaInicioRealTemp"
								 ng-value="controller.fechaInicioRealTemp"
								 onblur="this.setAttribute('value', this.value);"
								 ng-readonly="true"
								 >
								<label class="floating-label" >Fecha Inicio Real</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group money-input">
								<input type="text" class="inputText "  
								 ng-model="controller.fechaFinalRealTemp"
								 ng-value="controller.fechaFinalRealTemp"
								 onblur="this.setAttribute('value', this.value);"
								 ng-readonly="true"
								 >
								<label class="floating-label" >Fecha Fin Real</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								   <input class="inputText"  type="number" style="text-align: right"
								     ng-model="controller.duracionReal" ng-value="controller.duracionReal"   
								     onblur="this.setAttribute('value', this.value);"  min="1"
								     ng-readonly="true">
								   <label class="floating-label">* Duración Real (días)</label>
								</div>	
						</div>
					</div>
					<div class="row" ng-if="controller.proyecto.coordinador == 1">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="number" style="text-align: right;"
								 class="inputText "  
								 ng-model="controller.proyecto.porcentajeAvance"
								 ng-value="controller.proyecto.porcentajeAvance"
								 onblur="this.setAttribute('value', this.value);"
								 min="0" max="100">
								<label class="floating-label" >Avance del Préstamo %</label>
							</div>
						</div>
					</div>
					
					<div class="panel panel-default" ng-hide="controller.esNuevoDocumento" >
						<div class="panel-heading label-form" style="text-align: center;">Archivos adjuntos</div>
						<div class="panel-body">
							<div style="width: 95%; float: left">
							<table st-table="controller.displayedCollection" st-safe-src="controller.rowCollection" class="table table-striped">
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
								<tr ng-repeat="row in controller.displayedCollection">
									<td style="display: none;">{{row.id}}</td>
									<td>{{row.nombre}}</td>
									<td>{{row.extension}}</td>
									<td align="center">
										<button type="button"
											ng-click="controller.descargarDocumento(row)"
											uib-tooltip="Descargar documento" tooltip-placement="bottom"
											class="btn btn-default">
											<i class="glyphicon glyphicon-download-alt"> </i>
										</button>
									</td>
									<td align="center">
										<button type="button"
											ng-click="controller.eliminarDocumento(row)"
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
									<label class="btn btn-default" ng-model="controller.adjuntarDocumento"
										ng-click="controller.adjuntarDocumentos();" uib-tooltip="Adjuntar documento" tooltip-placement="bottom">
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
										<label for="usuarioCreo" class="label-form">Usuario que creo</label>
					  					<p class="">{{ controller.proyecto.usuarioCreo }}</pl>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label for="fechaCreacion" class="label-form">Fecha de creación</label>
					  					<p class="">{{ controller.proyecto.fechaCreacion }}</p>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group" style="text-align: right">
										<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label>
					  					<p class="">{{ controller.proyecto.usuarioactualizo }}</p>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
										<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label>
					  					<p class="">{{ controller.proyecto.fechaactualizacion }}</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</uib-tab>
				<shiro:hasPermission name="43010">
				<uib-tab index="controller.ordenTab+1" heading="Adicionales" ng-if="controller.mostrarPrestamo" >
					<div class="row" style="margin-top: 15px">
						<div class="form-group">
						   <textarea class="inputText" rows="4"
						   ng-model="controller.proyecto.observaciones" ng-value="controller.proyecto.observaciones"   
						   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
						   <label class="floating-label">Observaciones</label>
						</div>
					</div>
		
				</uib-tab>
				<uib-tab index= "controller.ordenTab+2" heading="Acta de constitución" ng-if="false">
					
					<div class="form-group">
						<label for="id" class="floating-label">Nombre del Proyecto {{ controller.proyecto.nombre }}</label>
						<br/><br/>
					</div>
					
					<div class="form-group">
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="controller.directorProyectoNombre" ng-readonly="true" ng-required="true"
			            	ng-click="controller.buscarDirecotorProyecto()" ng-value="controller.directorProyectoNombre" onblur="this.setAttribute('value', this.value);"/>
			            <span class="label-icon" ng-click="controller.buscarDirecotorProyecto()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label for="campo3" class="floating-label">* Director del Proyecto</label>
					</div>
					<div align="center">
						<h5 class="label-form">Impacto en gobierno </h5>
						<div style="height: 35px; width: 90%">
							<div style="text-align: right;">
								<div class="btn-group" role="group" aria-label="">
									<a class="btn btn-default" href
										ng-click="controller.agregarImpacto()" role="button"
										uib-tooltip="Asignar un nuevo proyecto" tooltip-placement="left">
										<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
									</a>
								</div>
							</div>
						</div>
					
					
						<br/>
						<table style="width: 90%;"
						st-table="controller.impactos"
						class="table table-striped  table-bordered">
							<thead >
								<tr>
									<th class="label-form">ID</th>
									<th class="label-form">Organización</th>
									<th class="label-form">Impacto y participación de la organización</th>
									<th  class="label-form" style="width: 30px;">Quitar</th>
		
								</tr>
							</thead>
							<tbody>
								<tr st-select-row="row"
									ng-repeat="row in controller.impactos">
									<td>{{row.entidadId}}</td>
									<td>{{row.entidadNombre}}</td>
									<td>{{row.impacto}}</td>
									<td>
										<button type="button"
											ng-click="controller.quitarImpacto(row)"
											class="btn btn-sm btn-danger">
											<i class="glyphicon glyphicon-minus-sign"> </i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<br/><br/>
					<div align="center">
						<h5 class="label-form">Otros miembros </h5>
						<div style="height: 35px; width: 90%">
							<div style="text-align: right;">
								<div class="btn-group" role="group" aria-label="">
									<a class="btn btn-default" href
										ng-click="controller.agregarMiembro()" role="button"
										uib-tooltip="Asignar un nuevo proyecto" tooltip-placement="left">
										<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
									</a>
								</div>
							</div>
						</div>
					
					
						<br/>
						<table style="width: 90%;"
						st-table="controller.miembros"
						class="table table-striped  table-bordered">
							<thead >
								<tr>
									<th class="label-form">Nombre</th>
									<th  class="label-form" style="width: 30px;">Quitar</th>
		
								</tr>
							</thead>
							<tbody>
								<tr st-select-row="row"
									ng-repeat="row in controller.miembros">
									<td>{{row.nombre}}</td>
									<td>
										<button type="button"
											ng-click="controller.quitarMiembro(row)"
											class="btn btn-sm btn-danger">
											<i class="glyphicon glyphicon-minus-sign"> </i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</uib-tab>
				<uib-tab index="3" heading="Desembolsos" ng-click="controller.desembolsos=true" >
					<div ng-if="controller.desembolsos !== undefined"><%@include file="/app/components/desembolso/desembolso.jsp" %></div>
				</uib-tab>
				<uib-tab index="4" heading="Riesgos" ng-click="controller.riesgos=true" >
					<div ng-if="controller.riesgos !== undefined"><%@include file="/app/components/riesgo/riesgo.jsp" %></div>
				</uib-tab>
				<uib-tab index="5" heading="Componentes" ng-click="controller.riesgos=true" >
					
					<div class="row">
						
							<div class="divTabla" ng-hide="controller.m_componentes.length == 0">
								<table class="table " >
								 	<tr>
								 		<th rowspan="2" class="label-form" style="vertical-align: middle;"> COMPONENTES</th>
								 		<th colspan="3"  class="label-form" ng-repeat= "organismo in controller.m_organismosEjecutores"
								 			style="text-align: center; vertical-align: middle;">
								 			<div>ASIGNADO A {{ organismo.nombre }}</div>
								 		</th>
								 		<th rowspan="2" class="label-form" style="text-align: center; vertical-align: middle;">TECHO TOTAL</th>
								 	</tr>
								 	<tr>
								 		<th colspan="3" ng-repeat= "organismo in controller.m_organismosEjecutores">
								 			<table style="width: 100%">
								 				<tr>
								 					<td class="label-form" style="min-width: 100px; text-align: center;">Prestamo</td>
								 					<td class="label-form" style="min-width: 100px; text-align: center;">Donación</td>
								 					<td class="label-form" style="min-width: 100px;text-align: center;">Nacional</td>
								 				</tr>
								 			</table>
								 		</th>
								 	</tr>
								 	
								 	<tr ng-repeat="row in controller.m_componentes track by $index " class="filaIngreso"
								 	    ng-click="controller.componenteSeleccionado(row)" ng-class="row.isSelected ? 'st-selected' : ''">
								 		<td style="min-width: 200px;" class="label-form"> {{ row.nombre }} </td>
								 		<td  colspan="3" ng-repeat = "ue in row.unidadesEjecutoras track by $index">
								 			<table style="width: 100%;">
								 				<tr>
								 					<td style="text-align: center;">
								 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
															class="inputText input-money"
															ng-model="ue.prestamo"
															ng-value="ue.prestamo"
															onblur="this.setAttribute('value', this.value);"
															ui-number-mask="2"
															ng-readonly="true"
														/>
								 					</td>
								 					<td style="text-align: center;">
								 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
															class="inputText input-money"
															ng-model="ue.donacion"
															ng-value="ue.donacion"
															onblur="this.setAttribute('value', this.value);"
															ui-number-mask="2"
															ng-readonly="true"
														/>
								 					</td>
								 					<td style="text-align: center; border-right: 1px solid #ddd;">
								 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
															class="inputText input-money"
															ng-model="ue.nacional"
															ng-value="ue.nacional"
															onblur="this.setAttribute('value', this.value);"
															ui-number-mask="2"
															ng-readonly="true"
														/>
								 					</td>
								 				<tr>
								 			</table>
								 		</td>
								 		<td style="width: 150px; text-align: right;"
								 		 	class="label-form"> {{ row.techo | formatoMillones : desembolsosc.enMillones }} 
								 		 </td>
								 	</tr>
								</table>
							</div>
						</div>
					
					
				</uib-tab>
				</shiro:hasPermission>
			</uib-tabset>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right" style="margin-top: 15px;">
			<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/aaaa</div>
			<br/>
			<div class="btn-group" ng-disabled="!controller.botones">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="controller.mForm.$valid && controller.botones ? controller.guardar(controller.mForm.$valid) : ''" ng-disabled="!controller.mForm.$valid || !controller.botones" uib-tooltip="Guardar" tooltip-placement="top">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label ng-if="!controller.esTreeview" class="btn btn-primary" ng-click="controller.botones ? controller.irATabla() : ''" uib-tooltip="Ir a Tabla" tooltip-placement="top" ng-disabled="!controller.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="controller.esTreeview" class="btn btn-danger" ng-click=" controller.botones && !controller.congelado ? controller.t_borrar() : ''" ng-disabled="!(controller.proyecto.id>0) || !controller.botones || controller.congelado" uib-tooltip="Borrar" tooltip-placement="top">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
	</div>
</div>

