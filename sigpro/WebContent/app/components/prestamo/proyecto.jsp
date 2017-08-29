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
  	 <script type="text/ng-template" id="agregarImpacto.jsp">
    		<%@ include file="/app/components/prestamo/agregarImpacto.jsp"%>
  	 </script>
	<shiro:lacksPermission name="24010">
		<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Préstamos</h3></div>
	</div>
	
	<div align="center" ng-hide="controller.esColapsado">
		<br>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="controller.cargarArchivo()" uib-tooltip="Cargar desde project">   
				<span class="glyphicon glyphicon glyphicon-file"></span>Project</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24010">
				<label class="btn btn-primary" ng-click="controller.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24030">
				<label class="btn btn-danger" ng-click="controller.borrar()" uib-tooltip="Borrar">
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
			<h2 ng-hide="!controller.esNuevo"><small>Nuevo Préstamo</small></h2>
			<h2 ng-hide="controller.esNuevo"><small>Edición de Préstamo</small></h2>
			</div>
		<div class="operation_buttons">
			<div class="btn-group" ng-hide="controller.esNuevo">
				<label class="btn btn-default" ng-click="controller.irAComponentes(controller.proyecto.id)" uib-tooltip="Componentes" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-th"></span></label>
				<label class="btn btn-default" ng-click="controller.irAHitos(controller.proyecto.id)" uib-tooltip="Hitos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-screenshot"></span></label>
				<label class="btn btn-default" ng-click="controller.irADesembolsos(controller.proyecto.id)" uib-tooltip="Desembolsos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-usd"></span></label>
				<label class="btn btn-default" ng-click="controller.irARiesgos(controller.proyecto.id)" uib-tooltip="Riesgos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-warning-sign"></span></label>
				<label class="btn btn-default" ng-click="controller.irAActividades(controller.proyecto.id)" uib-tooltip="Actividades" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-th-list"></span></label>
				<label class="btn btn-default" ng-click="controller.irAMapa(controller.proyecto.id)" uib-tooltip="Mapa" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-globe"></span></label>
				<label class="btn btn-default" ng-click="controller.irAGantt(controller.proyecto.id)" uib-tooltip="Gantt" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-indent-left"></span></label>
				<label class="btn btn-default" ng-click="controller.irAKanban(controller.proyecto.id)" uib-tooltip="Cartelera de Actividades" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-blackboard"></span></label>
				<label class="btn btn-default" ng-click="controller.irAAgenda(controller.proyecto.id)" uib-tooltip="Agenda" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-calendar"></span></label>
				<label class="btn btn-default" ng-click="controller.irAMatrizRiesgos(controller.proyecto.id)" uib-tooltip="Matriz de Riesgos" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span></label>
			
		</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="form">
			<uib-tabset active="active">
				
				<uib-tab ng-click="controller.getPorcentajes();" index="0" heading="Datos del préstamo" >
					<div class="panel panel-default">
						<div class="panel-heading label-form" style="text-align: center;">Información General del Préstamo</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-5">
									<div class="form-group">
										<input  type="number" class="inputText" ng-model="controller.prestamo.codigoPresupuestario" ng-required="true" 
										ng-value="controller.prestamo.codigoPresupuestario" onblur="this.setAttribute('value', this.value);">
										<label class="floating-label" >* Código presupuestario</label>
									</div>
								</div>
								<div class="col-sm-2" align="right" >
								<label class="btn btn-default" ng-click="controller.cargaSigade()" uib-tooltip="Cargar datos de SIGADE" tooltip-placement="bottom">
								<span class="glyphicon glyphicon-search"></span></label>
								</div>
								
								
							</div>
							<div class="row">
								<div class="col-sm-5">
									<div class="form-group">
										<input type="text" class="inputText"  ng-model="controller.prestamo.numeroPrestamo" ng-required="true" 
										ng-value="controller.prestamo.numeroPrestamo" onblur="this.setAttribute('value', this.value);">
										<label class="floating-label" >* Número de prestamo</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
									<input type="text" class="inputText"   ng-model="controller.prestamo.proyectoPrograma" ng-required="true"
									onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.proyectoPrograma"  >
									<label class="floating-label">* Proyecto/Programa</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<input type="text" class="inputText"   
										ng-model="controller.prestamo.unidadEjecutoraNombre" ng-readonly="true" ng-required="true"
										ng-click="controller.buscarUnidadEjecutoraPrestamo()"
										onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.unidadEjecutoraNombre" />			            	
										<span class="label-icon" ng-click="controller.buscarUnidadEjecutoraPrestamo()">
											<i class="glyphicon glyphicon-search"></i>
										</span>
										<label class="floating-label">* Unidad Ejecutor</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group" >
										<input type="text" class="inputText" id="icoope" name="icoope" ng-model="controller.prestamo.cooperantenombre" ng-readonly="true" ng-required="true" 
											ng-click="controller.buscarCooperante(true)" ng-value="controller.prestamo.cooperantenombre" onblur="this.setAttribute('value', this.value);"/>
										<span class="label-icon" ng-click="controller.buscarCooperante(true)"><i class="glyphicon glyphicon-search"></i></span>
										<label for="campo3" class="floating-label">* Organismo financiero internacional</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-4">
									<div class="form-group">    						
										<input type="text" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaDecreto" is-open="controller.fd_abierto"
											datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
											ng-click="controller.abrirPopupFecha(1007)" ng-value="controller.prestamo.fechaDecreto" onblur="this.setAttribute('value', this.value);"/>
										<span class="label-icon" ng-click="controller.abrirPopupFecha(1007)">	
											<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha Decreto</label>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaSuscripcion" is-open="controller.fs_abierto"
											datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
											ng-click="controller.abrirPopupFecha(1008)" ng-value="controller.prestamo.fechaSuscripcion" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="controller.abrirPopupFecha(1008)">
												<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha de Suscripción</label>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaVigencia" is-open="controller.fv_abierto"
											datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
											ng-click="controller.abrirPopupFecha(1012)" ng-value="controller.prestamo.fechaVigencia" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="controller.abrirPopupFecha(1012)">
												<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha de vigencia</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="text" class="inputText"  
										ng-model="controller.prestamo.tipoMonedaNombre" ng-readonly="true" ng-required="true"
										ng-click="controller.buscarTipoMoneda()"
										onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.tipoMonedaNombre"/>
										<span class="label-icon" ng-click="controller.buscarTipoMoneda()">
											<i class="glyphicon glyphicon-search"></i>
										</span>
										<label class="floating-label">* Tipo de Moneda</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group money-input">
										<input type="number" 
										 class="inputText"  
										 ng-model="controller.prestamo.montoContratado" 
										 ng-required="true"
										 ng-value="controller.prestamo.montoContratado"
										 onblur="this.setAttribute('value', this.value);" 
										 ng-change="controller.setPorcentaje(1);" 
										 ng-blur="controller.ocultarLabel('label_class_1')" 
										 id="label_class_1"
										 >
										<label class="money-label" ng-class="controller.label_class_1" ng-click="controller.ocultarLabel('label_class_1')">{{controller.prestamo.montoContratado | number}}</label>
										<label class="floating-label" >* Monto Contratado</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" 
										 class="inputText"  
										 ng-model="controller.prestamo.montoContratadoUsd" 
										 ng-required="true"
										 ng-value="controller.prestamo.montoContratadoUsd" 
										 onblur="this.setAttribute('value', this.value);" 
										 ng-change="controller.setPorcentaje(2);"
										 ng-blur="controller.ocultarLabel('label_class_2')" 
										 id="label_class_2"
										 ng-click="controller.ocultarLabel('label_class_2')"
										 >
										 <label class="money-label" ng-class="controller.label_class_2" ng-click="controller.ocultarLabel('label_class_2')">{{controller.prestamo.montoContratadoUsd | number}}</label>
										<label class="floating-label" >* Monto Contratado $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" 
										class="inputText" 
										ng-model="controller.prestamo.montoContratadoQtz" 
										ng-required="true"
										ng-value="controller.prestamo.montoContratadoQtz" 
										onblur="this.setAttribute('value', this.value);"
										ng-blur="controller.ocultarLabel('label_class_3')" 
										id="label_class_3"
										>
										<label class="money-label" ng-class="controller.label_class_3" ng-click="controller.ocultarLabel('label_class_3')">{{controller.prestamo.montoContratadoQtz | number}}</label>
										<label class="floating-label" >* Monto Contratado Q</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" 
										class="inputText" 
										ng-model="controller.prestamo.desembolsoAFechaUsd" 
										ng-required="true"
										ng-value="controller.prestamo.desembolsoAFechaUsd" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="controller.setPorcentaje(1);"
										ng-blur="controller.ocultarLabel('label_class_4')" 
										id="label_class_4"
										>
										<label class="money-label" 
										ng-class="controller.label_class_4" 
										ng-click="controller.ocultarLabel('label_class_4')" 
										>{{ controller.prestamo.desembolsoAFechaUsd | number}}</label>
										<label class="floating-label">* Desembolso a la Fecha $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="controller.prestamo.desembolsoAFechaUsdP" ng-disabled="true"
										ng-value="controller.prestamo.desembolsoAFechaUsdP" onblur="this.setAttribute('value', this.value);" />
										<label class="floating-label">Desembolso a la Fecha %</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" 
										class="inputText" 
										ng-model="controller.prestamo.montoPorDesembolsarUsd" 
										ng-required="true"
										ng-value="controller.prestamo.montoPorDesembolsarUsd" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="controller.setPorcentaje(2);"
										ng-disabled="true"
										ng-blur="controller.ocultarLabel('label_class_5')" 
										id="label_class_5"
										/>
										<label class="money-label" 
										ng-class="controller.label_class_5" 
										ng-click="controller.ocultarLabel('label_class_5')" 
										>{{ controller.prestamo.montoPorDesembolsarUsd | number}}</label>
										<label class="floating-label">* Monto por Desembolsar $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="controller.prestamo.montoPorDesembolsarUsdP" ng-disabled="true"
										ng-value="controller.prestamo.montoPorDesembolsarUsdP" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Monto por Desembolsar %</label>
									</div>
								</div>
							</div>
						</div>
					</div>
					
					
				</uib-tab>
				
				<uib-tab index="1" heading="Datos generales" >
					<div class="form-group">
						<label for="id" class="floating-label">ID {{ controller.proyecto.id }}</label>
						<br/><br/>
					</div>
					
					<div class="form-group" >
				      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="controller.proyecto.nombre" ng-value="controller.proyecto.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" >
				      <label class="floating-label">* Nombre</label>
					</div>
	
					<div class="form-group"  ng-hide="true"  >
						<input type="number" class="inputText" name="isnip" id="isnip"  ng-model="controller.proyecto.snip" ng-value="controller.proyecto.snip" onblur="this.setAttribute('value', this.value);">
					      <label class="floating-label">SNIP</label>
					</div>
					
					<div class="form-group-row row" ng-hide="true"  >
						<div class="form-group col-sm-2" >
						       <input type="number" class="inputText" ng-model="controller.proyecto.programa" ng-value="controller.proyecto.programa" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center" />
						       <label for="iprog" class="floating-label">Programa</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false" >
						  <input type="number" class="inputText" ng-model="controller.proyecto.subprograma" ng-value="controller.proyecto.subprograma" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="isubprog" class="floating-label">Subprograma</label>
						</div>
						<div class="form-group col-sm-2"  ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.proyecto" ng-value="controller.proyecto.proyecto" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iproy_" class="floating-label">Proyecto</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.actividad" ng-value="controller.proyecto.actividad" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iobra" class="floating-label">Actividad</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.obra" ng-value="controller.proyecto.obra" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="iobra" class="floating-label">Obra</label>
						</div>
						<div class="form-group col-sm-2" ng-if="false">
						  <input type="number" class="inputText" ng-model="controller.proyecto.fuente" ng-value="controller.proyecto.fuente" onblur="this.setAttribute('value', this.value);" ng-maxlength="4" style="text-align: center"/>
						  <label for="campo5" class="floating-label">Fuente</label>
						</div>
					</div>
						
					<div class="form-group" >
			            	<input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="controller.proyectotiponombre" ng-value="controller.proyectotiponombre" 
			            		ng-click="controller.buscarProyectoTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
			            	<span class="label-icon" ng-click="controller.buscarProyectoTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label for="campo3" class="floating-label">* Caracterización Préstamo</label>
					</div>
	
					<div ng-repeat="campo in controller.camposdinamicos" >
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
								<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
													datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="controller.abrirPopupFecha($index)"
													ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
													<span class="label-icon" ng-click="controller.abrirPopupFecha($index)">
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
					
					<div class="form-group" ng-show="controller.unidadejecutoranombre.length>0" >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="controller.entidadnombre" ng-readonly="true"  
			            	 ng-value="controller.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
			            	<label for="campo3" class="floating-label">Organismo Ejecutor</label>
			          	
					</div>
	
					<div class="form-group" >
			            <input type="text" class="inputText" id="iunie" name="iunie" ng-model="controller.unidadejecutoranombre" ng-readonly="true" ng-required="true" 
			            	ng-click="controller.buscarUnidadEjecutora()" ng-value="controller.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
			            <span class="label-icon" ng-click="controller.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label for="campo3" class="floating-label">* Unidad Ejecutora</label>
					</div>
	
					<div class="form-group"  >
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
					
					<div class="form-group" >
							<input  ng-model="controller.proyecto.objetivo"
								class="inputText" id="objetivo"
								ng-value="controller.proyecto.objetivo" onblur="this.setAttribute('value', this.value);">
							<label for="objetivo" class="floating-label">Objetivo</label>
					</div>
					
					<div class="form-group" >
							<input  ng-model="controller.proyecto.objetivoEspecifico"
								class="inputText" 
								ng-value="controller.proyecto.objetivoEspecifico" onblur="this.setAttribute('value', this.value);">
							<label for="objetivo" class="floating-label">Objetivos específicos</label>
					</div>
					
					<div class="form-group" >
							<input  ng-model="controller.proyecto.visionGeneral"
								class="inputText" 
								ng-value="controller.proyecto.visionGeneral" onblur="this.setAttribute('value', this.value);">
							<label for="objetivo" class="floating-label">Visión general</label>
					</div>
					
					<br/>
					
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
					
					
					<div class="panel panel-default" ng-hide="controller.esNuevoDocumento" >
						<div class="panel-heading label-form" style="text-align: center;">Componentes</div>
						<div class="panel-body">
							<div style="width: 95%; float: left">
							<table st-table="controller.displayedComponentes"
							 st-safe-src="controller.componentes" class="table table-striped">
								<thead>
									<tr>
										<th style="display: none;">Id</th>
										<th class="label-form">Nombre</th>
									</tr>
									<tr>
										<th colspan="5"><input st-search="" 
										class="form-control" placeholder="busqueda global ..." type="text"/></th>
									</tr>
								</thead>
								<tbody>
								<tr ng-repeat="row in controller.displayedComponentes">
									<td>{{row.nombre}}</td>
								</tr>
								</tbody>
							</table>
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
				
				<uib-tab ng-click="controller.getPorcentajes();" index="2" heading="Datos Entidad Ejecutora" >
					<div class="panel panel-default">
						<div class="panel-heading label-form" style="text-align: center;">Información Específica del Préstamo en la Entidad Ejecutora</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaElegibilidadUe" is-open="controller.fe_abierto"
											datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="controller.setPorcentaje(5);"
											ng-click="controller.abrirPopupFecha(1009)" ng-value="controller.prestamo.fechaElegibilidadUe" onblur="this.setAttribute('value', this.value);"/>
										<span class="label-icon" ng-click="controller.abrirPopupFecha(1009)">
												<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha de Elegibilidad</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
											<input type="text" class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaCierreOrigianlUe" is-open="controller.fco_abierto"
												datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="controller.setPorcentaje(5);"
												ng-click="controller.abrirPopupFecha(1010)" ng-value="controller.prestamo.fechaCierreOrigianlUe" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="controller.abrirPopupFecha(1010)">
													<i class="glyphicon glyphicon-calendar"></i>
											</span>
										<label class="floating-label">* Fecha de Cierre Original</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
											<input type="text" class="inputText"   uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaCierreActualUe" is-open="controller.fca_abierto"
												datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="controller.setPorcentaje(5);"
												ng-click="controller.abrirPopupFecha(1011)" ng-value="controller.prestamo.fechaCierreActualUe" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="controller.abrirPopupFecha(1011)">
													<i class="glyphicon glyphicon-calendar"></i>
											</span>
										<label  class="floating-label">* Fecha de Cierre Actual</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"  ng-model="controller.prestamo.mesesProrrogaUe" ng-required="true"
										ng-value="controller.prestamo.mesesProrrogaUe" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">* Meses de Prórroga</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"   ng-model="controller.prestamo.plazoEjecucionUe" ng-disabled="true"
										ng-value="controller.prestamo.plazoEjecucionUe" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Plazo de Ejecución %</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input type="number" 
										class="inputText" 
										ng-model="controller.prestamo.montoAsignadoUe" 
										ng-required="true"
										ng-value="controller.prestamo.montoAsignadoUe"
										onblur="this.setAttribute('value', this.value);" 
										ng-change="controller.setPorcentaje(3);"
										ng-blur="controller.ocultarLabel('label_class_6')" 
										id="label_class_6"
										>
										 <label 
											class="money-label" 
											ng-class="controller.label_class_6" 
											ng-click="controller.ocultarLabel('label_class_6')"
										>
											{{controller.prestamo.montoAsignadoUe | number}}
										</label>
										<label class="floating-label">* Monto Asignado</label>
									</div>
								</div>
							</div>
							<div class="row">
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input 
										type="number" 
										class="inputText" 
										ng-model="controller.prestamo.montoAsignadoUeUsd" 
										ng-required="true" 
										ng-value="controller.prestamo.montoAsignadoUeUsd" 
										onblur="this.setAttribute('value', this.value);"
										ng-blur="controller.ocultarLabel('label_class_7')" 
										id="label_class_7"
										/>
										<label 
											class="money-label" 
											ng-class="controller.label_class_7" 
											ng-click="controller.ocultarLabel('label_class_7')"
										>
											{{controller.prestamo.montoAsignadoUeUsd | number}}
										</label>
										<label class="floating-label">* Monto Asignado $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input 
										type="number" 
										class="inputText" 
										ng-model="controller.prestamo.montoAsignadoUeQtz" 
										ng-required="true" 
										ng-value="controller.prestamo.montoAsignadoUeQtz" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="controller.setPorcentaje(4);"
										ng-blur="controller.ocultarLabel('label_class_8')" 
										id="label_class_8"
										>
										<label 
											class="money-label" 
											ng-class="controller.label_class_8" 
											ng-click="controller.ocultarLabel('label_class_8')"
										>
											{{controller.prestamo.montoAsignadoUeQtz | number}}
										</label>
										<label class="floating-label">* Monto Asignado Q</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" 
										class="inputText" 
										ng-model="controller.prestamo.desembolsoAFechaUeUsd" 
										ng-required="true"
										ng-value="controller.prestamo.desembolsoAFechaUeUsd" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="controller.setPorcentaje(3);"
										ng-blur="controller.ocultarLabel('label_class_9')" 
										id="label_class_9"
										>
										<label 
											class="money-label" 
											ng-class="controller.label_class_9" 
											ng-click="controller.ocultarLabel('label_class_9')"
										>
											{{controller.prestamo.desembolsoAFechaUeUsd | number}}
										</label>
										<label class="floating-label">* Desembolso a la Fecha $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="controller.prestamo.desembolsoAFechaUeUsdP" ng-disabled="true"
										ng-value="controller.prestamo.desembolsoAFechaUeUsdP" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Desembolsos a la fecha %</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">							
										<input type="number" 
										class="inputText" 
										ng-model="controller.prestamo.montoPorDesembolsarUeUsd" 
										ng-required="true" 
										ng-value="controller.prestamo.montoPorDesembolsarUeUsd" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="controller.setPorcentaje(4);"
										ng-disabled="true"
										ng-blur="controller.ocultarLabel('label_class_10')" 
										id="label_class_10"
										/>
										<label 
											class="money-label" 
											ng-class="controller.label_class_10" 
											ng-click="controller.ocultarLabel('label_class_10')"
										>
											{{controller.prestamo.montoPorDesembolsarUeUsd | number}}
										</label>
										<label class="floating-label">* Monto por desembolsar $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input type="number" class="inputText" ng-model="controller.prestamo.montoPorDesembolsarUeUsdP" ng-disabled="true" 
										ng-value="controller.prestamo.montoPorDesembolsarUeUsdP" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Monto por desembolsar %</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				
				</uib-tab>
				
				<uib-tab index="3" heading="Datos adicionales" ng-if="controller.mostrarPrestamo" >
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input  type="text" class="inputText"   ng-model="controller.prestamo.numeroAutorizacion" 
							   	onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.numeroAutorizacion">
							   	<label class="floating-label">Número Autorización</label>
							</div>
						</div>
					
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="text" class="inputText"  ng-model="controller.prestamo.destino" 
							   	ng-value="controller.prestamo.destino" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label" >Destino</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="text" class="inputText"  ng-model="controller.prestamo.sectorEconomico" 
							   	ng-value="controller.prestamo.sectorEconomico" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label" >Sector Económico</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaFirma" is-open="controller.ff_abierto"
									datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="controller.abrirPopupFecha(1004)" ng-value="controller.prestamo.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="controller.abrirPopupFecha(1004)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Firma</label>
								
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaCorte" is-open="controller.fc_abierto"
													datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
													ng-click="controller.abrirPopupFecha(1003)" ng-value="controller.prestamo.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="controller.abrirPopupFecha(1003)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Corte</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
				            	<input type="text" class="inputText"    
				            	ng-model="controller.prestamo.tipoAutorizacionNombre" ng-readonly="true" 
				            	ng-click="controller.buscarAutorizacionTipo()"
				            	onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.tipoAutorizacionNombre"			            	/>
				            	<span class="label-icon" ng-click="controller.buscarAutorizacionTipo()"><i class="glyphicon glyphicon-search"></i></span>				          	
					          	<label class="floating-label">Tipo Autorización</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  type="number" class="inputText" ng-model="controller.prestamo.aniosPlazo" max="100" min="0" 
							   	ng-value="controller.prestamo.aniosPlazo" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label">Años Plazo</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.aniosGracia" max="100" min="0" 
							   		ng-value="controller.prestamo.aniosGracia" onblur="this.setAttribute('value', this.value);">
							   	<label  class="floating-label">Años de Gracia</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaAutorizacion" is-open="controller.fa_abierto"
									datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="controller.abrirPopupFecha(1005)" ng-value="controller.prestamo.fechaAutorizacion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="controller.abrirPopupFecha(1005)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Autorización</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.prestamo.fechaFinEjecucion" is-open="controller.ffe_abierto"
									datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="controller.abrirPopupFecha(1006)" ng-value="controller.prestamo.fechaFinEjecucion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="controller.abrirPopupFecha(1006)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha Fin de Ejecución</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
				            	<input type="text" class="inputText" ng-model="controller.prestamo.tipoInteresNombre" ng-readonly="true"
				            	ng-click="controller.buscarInteresTipo()"
				            	onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.tipoInteresNombre"/>
				            	<span class="label-icon" ng-click="controller.buscarInteresTipo()">
				            		<i class="glyphicon glyphicon-search"></i>
				            	</span>
					          	<label class="floating-label">Tipo de Interés</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.porcentajeInteres"  
							   	onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.porcentajeInteres"/>
							   	<label class="floating-label">Porcentaje de Interés</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  class="inputText" type="number"  ng-model="controller.prestamo.periodoEjecucion" max="100" min="0" 
							   	ng-value="controller.prestamo.periodoEjecucion" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label">Período de Ejecución</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.porcentajeComisionCompra"  
							   	onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.porcentajeComisionCompra"/>
							   	<label class="floating-label">Porcentaje Comisión Compra</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.amortizado"  
							   	ng-value="controller.prestamo.amortizado" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Amortizado</label>
							</div>
							
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.porAmortizar" 
							   	ng-value="controller.prestamo.porAmortizar" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Por Amortizar</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.principalAnio" 
							   	ng-value="controller.prestamo.principalAnio" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Principal del Año</label>
							</div>
						</div>
					</div>
					
					<div class="row">						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.interesesAnio"  
							   	ng-value="controller.prestamo.interesesAnio" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Intereses del Año</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.comisionCompromisoAnio"  
							   	ng-value="controller.prestamo.comisionCompromisoAnio" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Comisión Compromiso del Año</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.otrosGastos"  
							   	ng-value="controller.prestamo.otrosGastos" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Otros Gastos</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.principalAcumulado"  
							   	ng-value="controller.prestamo.principalAcumulado" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Principal Acumulado</label>
							</div>
						</div>
					</div>
					
					<div class="row"> 
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="controller.prestamo.interesesAcumulados" 
							   	ng-value="controller.prestamo.interesesAcumulados" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Intereses Acumulados</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="controller.prestamo.comisionCompromisoAcumulado"  
							   	ng-value="controller.prestamo.comisionCompromisoAcumulado" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Comisión Compromiso Acumulado</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="controller.prestamo.otrosCargosAcumulados"  
							   	ng-value="controller.prestamo.otrosCargosAcumulados" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label" >Otros Cargos Acumulados</label>
							</div>
						</div>					
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="controller.prestamo.presupuestoAsignadoFuncionamiento" 
								ng-value="controller.prestamo.presupuestoAsignadoFuncionamiento" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Asignado Funcionamiento</label>
							</div>
						</div>
					
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.presupuestoModificadoFun"  
							   	ng-value="controller.prestamo.presupuestoModificadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Modificado Funcionamiento</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.presupuestoVigenteFun" 
							   	ng-value="controller.prestamo.presupuestoVigenteFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Presupuesto Vigente Funcionamiento</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"   class="inputText" ng-model="controller.prestamo.presupuestoAsignadoInversion"  
							   	ng-value="controller.prestamo.presupuestoAsignadoInversion" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Asignado Inversion</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.presupuestoModificadoInv" 
							   	ng-value="controller.prestamo.presupuestoModificadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Modificado Inversión</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.presupuestoVigenteInv" 
							   	ng-value="controller.prestamo.presupuestoVigenteInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Vigente Inversión</label>
							</div>
						</div>
					</div>		
								
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.presupuestoDevengadoFun" 
							   	ng-value="controller.prestamo.presupuestoDevengadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Devengado Funcionamiento</label>
							</div>
						</div>
					
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number"  class="inputText" ng-model="controller.prestamo.presupuestoDevengadoInv"  
							   	ng-value="controller.prestamo.presupuestoDevengadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Devengado Inversión</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.presupuestoPagadoFun" 
							   	ng-value="controller.prestamo.presupuestoPagadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Presupuesto Pagado Funcionamiento </label>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="controller.prestamo.presupuestoPagadoInv"  
							   		ng-value="controller.prestamo.presupuestoPagadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Pagado Inversión </label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.saldoCuentas"
							   	ng-value="controller.prestamo.saldoCuentas" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Saldo de Cuentas </label>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.desembolsoReal"
							   	ng-value="controller.prestamo.desembolsoReal" onblur="this.setAttribute('value', this.value);"/>  
							   	<label class="floating-label">Desembolso Real GTQ</label>
							</div>
						</div>
					</div>
					
					<div class="row">	
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.desembolsoAFechaUe"
							   	ng-value="controller.prestamo.desembolsoAFechaUe" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Desembolso a la Fecha</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="controller.prestamo.montoPorDesembolsarUe"
							   	ng-value="controller.prestamo.montoPorDesembolsarUe" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Monto por Desembolsar</label>
							</div>
						</div>
					</div>
					
					<div class="form-group">
		            	<input type="text" class="inputText"  
		            	ng-click="controller.buscarEstadoEjecucion()"
		            	ng-model="controller.prestamo.ejecucionEstadoNombre" ng-readonly="true"
		            	onblur="this.setAttribute('value', this.value);" ng-value="controller.prestamo.ejecucionEstadoNombre" />
		            	<span class="label-icon" ng-click="controller.buscarEstadoEjecucion()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label class="floating-label">Estado de Ejecución</label>
					</div>
					
					<div class="form-group">
						<input type="text" ng-model="controller.proyecto.descripcion"
							class="inputText" id="campo2" 
							ng-value="controller.proyecto.descripcion" onblur="this.setAttribute('value', this.value);">
						<label for="campo2" class="floating-label">Descripción</label>
					</div>
				
				</uib-tab>

				<uib-tab index= "4" heading="Acta de constitución" ng-if="false">
					
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
			</uib-tabset>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="form.$valid ? controller.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="controller.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>
</div>

