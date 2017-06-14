<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="programaController as programac"
	class="maincontainer all_page" id="title">

	<script type="text/ng-template" id="buscarPorPrograma.jsp">
    		<%@ include file="/app/components/programa/buscarPorPrograma.jsp"%>
  	 </script>
  	 <shiro:lacksPermission name="37010">
		<p ng-init="programac.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>

	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Programas</h3></div>
	</div>
	
	<div class="row" align="center" ng-hide="programac.esColapsado">
	<br />
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="37040">
				<label class="btn btn-primary" ng-click="programac.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37010">
				<label class="btn btn-primary" ng-click="programac.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37030">
				<label class="btn btn-danger" ng-click="programac.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="37010">
		<div class="col-sm-12" align="center">
			<div style="height: 35px;">
				<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
					<shiro:hasPermission name="37010">
						<a class="btn btn-default" href ng-click="programac.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</shiro:hasPermission>
					</div>
				</div>
			</div>
			<br/>
			<div id="grid1" ui-grid="programac.gridOpciones" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination
				ui-grid-pagination>
				<div class="grid_loading" ng-hide="!programac.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  programac.totalProgramas + (programac.totalProgramas == 1 ? " Programa" : " Programas" ) }}</div>
				<ul uib-pagination total-items="programac.totalProgramas"
						ng-model="programac.paginaActual"
						max-size="programac.numeroMaximoPaginas"
						items-per-page="programac.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="programac.cambioPagina()"
				></ul>
		</div>

		</shiro:hasPermission>


	</div>
	<div class="row second-main-form" ng-show="programac.esColapsado">
		<div class="page-header">
			<h2 ng-hide="!programac.esNuevo"><small>Nuevo Programa</small></h2>
			<h2 ng-hide="programac.esNuevo"><small>Edición de programa</small></h2>
			</div>
		
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="form">
				<uib-tabset active="active">
					<uib-tab index="0" heading="Datos del programa">
						<div class="form-group">
							<label for="id" class="floating-label">ID {{ programac.programa.id }}</label>
		  					<br/><br/>
						</div>
						<div class="form-group">
							<input type="text"  class="inputText" ng-model="programac.programa.nombre" ng-value="programac.programa.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" >
							<label  class="floating-label">* Nombre</label>
						</div>
						<div class="form-group" >
				            	<input type="text" class="inputText" ng-model="programac.programatiponombre" ng-value="programac.programatiponombre" 
				            		ng-click="programac.buscarProgramaTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
				            	<span class="label-icon" ng-click="programac.buscarProgramaTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	<label  class="floating-label">* Tipo Programa</label>
						</div>
		
						<div ng-repeat="campo in programac.camposdinamicos">
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
											<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
																datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="programac.abrirPopupFecha($index)"
																ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
																<span class="label-icon" ng-click="programac.abrirPopupFecha($index)">
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
						
						<div class="form-group">
							<input type="text" ng-model="programac.programa.descripcion"
								class="inputText" id="campo2" 
								ng-value="programac.programa.descripcion" onblur="this.setAttribute('value', this.value);">
							<label for="campo2" class="floating-label">Descripción</label>
						</div>
						<br />
						<div align="center">
							<h5 class="label-form">Proyectos </h5>
							<div style="height: 35px; width: 75%">
								<div style="text-align: right;">
									<div class="btn-group" role="group" aria-label="">
										<a class="btn btn-default" href
											ng-click="programac.buscarProyecto()" role="button"
											uib-tooltip="Asignar un nuevo proyecto" tooltip-placement="left">
											<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
										</a>
									</div>
								</div>
							</div>
							<br/>
							<table style="width: 75%;"
							st-table="programac.proyectos"
							class="table table-striped  table-bordered">
							<thead >
								<tr>
									<th class="label-form">ID</th>
									<th class="label-form">Nombre</th>
									<th class="label-form">Descripicon</th>
									<th  class="label-form" style="width: 30px;">Quitar</th>
		
								</tr>
							</thead>
							<tbody>
								<tr st-select-row="row"
									ng-repeat="row in programac.proyectos">
									<td>{{row.id}}</td>
									<td>{{row.nombre}}</td>
									<td>{{row.descripcion}}</td>
									<td>
										<button type="button"
											ng-click="programac.eliminarProyecto(row)"
											class="btn btn-sm btn-danger">
											<i class="glyphicon glyphicon-minus-sign"> </i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
						</div>
						<br/>
						<div class="panel panel-default" ng-hide="programac.esNuevoDocumento">
							<div class="panel-heading label-form" style="text-align: center;">Archivos adjuntos</div>
							<div class="panel-body">
								<div style="width: 95%; float: left">
								<table st-table="programac.displayedCollection" st-safe-src="programac.rowCollection" class="table table-striped">
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
									<tr ng-repeat="row in programac.displayedCollection">
										<td style="display: none;">{{row.id}}</td>
										<td>{{row.nombre}}</td>
										<td>{{row.extension}}</td>
										<td align="center">
											<button type="button"
												ng-click="programac.descargarDocumento(row)"
												uib-tooltip="Descargar documento" tooltip-placement="bottom"
												class="btn btn-default">
												<i class="glyphicon glyphicon-download-alt"> </i>
											</button>
										</td>
										<td align="center">
											<button type="button"
												ng-click="programac.eliminarDocumento(row)"
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
										<label class="btn btn-default" ng-model="programac.adjuntarDocumento"
											ng-click="programac.adjuntarDocumentos();" uib-tooltip="Adjuntar documento" tooltip-placement="bottom">
										<span class="glyphicon glyphicon-plus"></span></label>
									</div>
		        				</div>
							</div>
						</div>
						<br/>
						<div class="panel panel-default">
							<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label  class="label-form">Usuario que creo</label>
						  					<p class="">{{ programac.programa.usuarioCreo }}</pl>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label  class="label-form">Fecha de creación</label>
						  					<p class="">{{ programac.programa.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label  class="label-form">Usuario que actualizo</label>
						  					<p class="">{{ programac.programa.usuarioactualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label  class="label-form">Fecha de actualizacion</label>
						  					<p class="">{{ programac.programa.fechaactualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						
					</uib-tab>
					
					<uib-tab ng-click="programac.getPorcentajes();" index="1" heading="Datos del préstamo">
					<div class="panel panel-default">
						<div class="panel-heading label-form" style="text-align: center;">Información General del Préstamo</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-5">
									<div class="form-group">
										<input  type="number" class="inputText" ng-model="programac.prestamo.codigoPresupuestario" ng-required="true"
										ng-value="programac.prestamo.codigoPresupuestario" onblur="this.setAttribute('value', this.value);">
										<label class="floating-label" >* Código presupuestario</label>
									</div>
								</div>
								<div class="col-sm-5">
									<div class="form-group">
										<input type="text" class="inputText"  ng-model="programac.prestamo.numeroPrestamo" ng-required="true"
										ng-value="programac.prestamo.numeroPrestamo" onblur="this.setAttribute('value', this.value);">
										<label class="floating-label" >* Número de prestamo</label>
									</div>
								</div>
								<div class="col-sm-2" align="right" >
								<label class="btn btn-default" ng-click="programac.cargaSigade()" uib-tooltip="Cargar datos de SIGADE" tooltip-placement="bottom">
								<span class="glyphicon glyphicon-search"></span></label>
								</div>


							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
									<input type="text" class="inputText"   ng-model="programac.prestamo.proyectoPrograma" ng-required="true"
									onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.proyectoPrograma"  >
									<label class="floating-label">* Proyecto/Programa</label>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-12">
									<div class="form-group">
										<input type="text" class="inputText"
										ng-model="programac.prestamo.unidadEjecutoraNombre" ng-readonly="true" ng-required="true"
										ng-click="programac.buscarUnidadEjecutoraPrestamo()"
										onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.unidadEjecutoraNombre" />
										<span class="label-icon" ng-click="programac.buscarUnidadEjecutoraPrestamo()">
											<i class="glyphicon glyphicon-search"></i>
										</span>
										<label class="floating-label">* Unidad Ejecutora</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<div class="form-group" >
										<input type="text" class="inputText" id="icoope" name="icoope" ng-model="programac.prestamo.cooperantenombre" ng-readonly="true" ng-required="true"
											ng-click="programac.buscarCooperante(true)" ng-value="programac.prestamo.cooperantenombre" onblur="this.setAttribute('value', this.value);"/>
										<span class="label-icon" ng-click="programac.buscarCooperante(true)"><i class="glyphicon glyphicon-search"></i></span>
										<label for="campo3" class="floating-label">* Cooperante</label>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-4">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaDecreto" is-open="programac.fd_abierto"
											datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
											ng-click="programac.abrirPopupFecha(1007)" ng-value="programac.prestamo.fechaDecreto" onblur="this.setAttribute('value', this.value);"/>
										<span class="label-icon" ng-click="programac.abrirPopupFecha(1007)">
											<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha Decreto</label>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaSuscripcion" is-open="programac.fs_abierto"
											datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
											ng-click="programac.abrirPopupFecha(1008)" ng-value="programac.prestamo.fechaSuscripcion" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="programac.abrirPopupFecha(1008)">
												<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha de Suscripción</label>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaVigencia" is-open="programac.fv_abierto"
											datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
											ng-click="programac.abrirPopupFecha(1012)" ng-value="programac.prestamo.fechaVigencia" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="programac.abrirPopupFecha(1012)">
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
										ng-model="programac.prestamo.tipoMonedaNombre" ng-readonly="true" ng-required="true"
										ng-click="programac.buscarTipoMoneda()"
										onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.tipoMonedaNombre"/>
										<span class="label-icon" ng-click="programac.buscarTipoMoneda()">
											<i class="glyphicon glyphicon-search"></i>
										</span>
										<label class="floating-label">* Tipo de Moneda</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"  ng-model="programac.prestamo.montoContratado" ng-required="true"
										ng-value="programac.prestamo.montoContratado" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(1);">
										<label class="floating-label" >* Monto Contratado</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"  ng-model="programac.prestamo.montoContratadoUsd" ng-required="true"
										ng-value="programac.prestamo.montoContratadoUsd" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(2);">
										<label class="floating-label" >* Monto Contratado $</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"  ng-model="programac.prestamo.montoContratadoQtz" ng-required="true"
										ng-value="programac.prestamo.montoContratadoQtz" onblur="this.setAttribute('value', this.value);">
										<label class="floating-label" >* Monto Contratado Q</label>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.desembolsoAFechaUsd" ng-required="true"
										ng-value="programac.prestamo.desembolsoAFechaUsd" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(1);"/>
										<label class="floating-label">* Desembolso a la Fecha $</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.desembolsoAFechaUsdP" ng-disabled="true"
										ng-value="programac.prestamo.desembolsoAFechaUsdP" onblur="this.setAttribute('value', this.value);" />
										<label class="floating-label">Desembolso a la Fecha %</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoPorDesembolsarUsd" ng-required="true"
										ng-value="programac.prestamo.montoPorDesembolsarUsd" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(2);"/>
										<label class="floating-label">* Monto por Desembolsar $</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoPorDesembolsarUsdP" ng-disabled="true"
										ng-value="programac.prestamo.montoPorDesembolsarUsdP" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Monto por Desembolsar %</label>
									</div>
								</div>
							</div>
						</div>
					</div>

					
				</uib-tab>
				
				<uib-tab ng-click="programac.getPorcentajes();" index="2" heading="Datos Entidad Ejecutroa">
					<div class="panel panel-default">
						<div class="panel-heading label-form" style="text-align: center;">Información Específica del Préstamo en la Entidad Ejecutora</div>
						<div class="panel-body">
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaElegibilidadUe" is-open="programac.fe_abierto"
											datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="programac.setPorcentaje(5);"
											ng-click="programac.abrirPopupFecha(1009)" ng-value="programac.prestamo.fechaElegibilidadUe" onblur="this.setAttribute('value', this.value);"/>
										<span class="label-icon" ng-click="programac.abrirPopupFecha(1009)">
												<i class="glyphicon glyphicon-calendar"></i>
										</span>
										<label class="floating-label">* Fecha de Elegibilidad</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
											<input type="text" class="inputText" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaCierreOrigianlUe" is-open="programac.fco_abierto"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="programac.setPorcentaje(5);"
												ng-click="programac.abrirPopupFecha(1010)" ng-value="programac.prestamo.fechaCierreOrigianlUe" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="programac.abrirPopupFecha(1010)">
													<i class="glyphicon glyphicon-calendar"></i>
											</span>
										<label class="floating-label">* Fecha de Cierre Original</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
											<input type="text" class="inputText"   uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaCierreActualUe" is-open="programac.fca_abierto"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="programac.setPorcentaje(5);"
												ng-click="programac.abrirPopupFecha(1011)" ng-value="programac.prestamo.fechaCierreActualUe" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="programac.abrirPopupFecha(1011)">
													<i class="glyphicon glyphicon-calendar"></i>
											</span>
										<label  class="floating-label">* Fecha de Cierre Actual</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"  ng-model="programac.prestamo.mesesProrrogaUe" ng-required="true"
										ng-value="programac.prestamo.mesesProrrogaUe" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">* Meses de Prórroga</label>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText"   ng-model="programac.prestamo.plazoEjecucionUe" ng-disabled="true"
										ng-value="programac.prestamo.plazoEjecucionUe" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Plazo de Ejecución %</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoAsignadoUe" ng-required="true"
										ng-value="programac.prestamo.montoAsignadoUe" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(3);"/>
										<label class="floating-label">* Monto Asignado</label>
									</div>
								</div>
							</div>
							<div class="row">

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoAsignadoUeUsd" ng-required="true"
										ng-value="programac.prestamo.montoAsignadoUeUsd" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">* Monto Asignado $</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoAsignadoUeQtz" ng-required="true"
										ng-value="programac.prestamo.montoAsignadoUeQtz" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(4);"/>
										<label class="floating-label">* Monto Asignado Q</label>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.desembolsoAFechaUeUsd" ng-required="true"
										ng-value="programac.prestamo.desembolsoAFechaUeUsd" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(3);"/>
										<label class="floating-label">* Desembolso a la Fecha $</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.desembolsoAFechaUeUsdP" ng-disabled="true"
										ng-value="programac.prestamo.desembolsoAFechaUeUsdP" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Desembolsos a la fecha %</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoPorDesembolsarUeUsd" ng-required="true"
										ng-value="programac.prestamo.montoPorDesembolsarUeUsd" onblur="this.setAttribute('value', this.value);" ng-change="programac.setPorcentaje(4);"/>
										<label class="floating-label">* Monto por desembolsar $</label>
									</div>
								</div>

								<div class="col-sm-6">
									<div class="form-group">
										<input type="number" class="inputText" ng-model="programac.prestamo.montoPorDesembolsarUeUsdP" ng-disabled="true"
										ng-value="programac.prestamo.montoPorDesembolsarUeUsdP" onblur="this.setAttribute('value', this.value);"/>
										<label class="floating-label">Monto por desembolsar %</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				
				</uib-tab>

				<uib-tab index="3" heading="Datos adicionales" >
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input  type="text" class="inputText"   ng-model="programac.prestamo.numeroAutorizacion"
							   	onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.numeroAutorizacion">
							   	<label class="floating-label">Número Autorización</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="text" class="inputText"  ng-model="programac.prestamo.destino"
							   	ng-value="programac.prestamo.destino" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label" >Destino</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="text" class="inputText"  ng-model="programac.prestamo.sectorEconomico"
							   	ng-value="programac.prestamo.sectorEconomico" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label" >Sector Económico</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaFirma" is-open="programac.ff_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="programac.abrirPopupFecha(1004)" ng-value="programac.prestamo.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="programac.abrirPopupFecha(1004)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Firma</label>

							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaCorte" is-open="programac.fc_abierto"
													datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
													ng-click="programac.abrirPopupFecha(1003)" ng-value="programac.prestamo.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="programac.abrirPopupFecha(1003)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Corte</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
				            	<input type="text" class="inputText"
				            	ng-model="programac.prestamo.tipoAutorizacionNombre" ng-readonly="true"
				            	ng-click="programac.buscarAutorizacionTipo()"
				            	onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.tipoAutorizacionNombre"			            	/>
				            	<span class="label-icon" ng-click="programac.buscarAutorizacionTipo()"><i class="glyphicon glyphicon-search"></i></span>
					          	<label class="floating-label">Tipo Autorización</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  type="number" class="inputText" ng-model="programac.prestamo.aniosPlazo" max="100" min="0"
							   	ng-value="programac.prestamo.aniosPlazo" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label">Años Plazo</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.aniosGracia" max="100" min="0"
							   		ng-value="programac.prestamo.aniosGracia" onblur="this.setAttribute('value', this.value);">
							   	<label  class="floating-label">Años de Gracia</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaAutorizacion" is-open="programac.fa_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="programac.abrirPopupFecha(1005)" ng-value="programac.prestamo.fechaAutorizacion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="programac.abrirPopupFecha(1005)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Autorización</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaFinEjecucion" is-open="programac.ffe_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="programac.abrirPopupFecha(1006)" ng-value="programac.prestamo.fechaFinEjecucion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="programac.abrirPopupFecha(1006)">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha Fin de Ejecución</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
				            	<input type="text" class="inputText" ng-model="programac.prestamo.tipoInteresNombre" ng-readonly="true"
				            	ng-click="programac.buscarInteresTipo()"
				            	onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.tipoInteresNombre"/>
				            	<span class="label-icon" ng-click="programac.buscarInteresTipo()">
				            		<i class="glyphicon glyphicon-search"></i>
				            	</span>
					          	<label class="floating-label">Tipo de Interés</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.porcentajeInteres"
							   	onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.porcentajeInteres"/>
							   	<label class="floating-label">Porcentaje de Interés</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  class="inputText" type="number"  ng-model="programac.prestamo.periodoEjecucion" max="100" min="0"
							   	ng-value="programac.prestamo.periodoEjecucion" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label">Período de Ejecución</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.porcentajeComisionCompra"
							   	onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.porcentajeComisionCompra"/>
							   	<label class="floating-label">Porcentaje Comisión Compra</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.amortizado"
							   	ng-value="programac.prestamo.amortizado" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Amortizado</label>
							</div>

						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.porAmortizar"
							   	ng-value="programac.prestamo.porAmortizar" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Por Amortizar</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.principalAnio"
							   	ng-value="programac.prestamo.principalAnio" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Principal del Año</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.interesesAnio"
							   	ng-value="programac.prestamo.interesesAnio" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Intereses del Año</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.comisionCompromisoAnio"
							   	ng-value="programac.prestamo.comisionCompromisoAnio" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Comisión Compromiso del Año</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.otrosGastos"
							   	ng-value="programac.prestamo.otrosGastos" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Otros Gastos</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.principalAcumulado"
							   	ng-value="programac.prestamo.principalAcumulado" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Principal Acumulado</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="programac.prestamo.interesesAcumulados"
							   	ng-value="programac.prestamo.interesesAcumulados" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Intereses Acumulados</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="programac.prestamo.comisionCompromisoAcumulado"
							   	ng-value="programac.prestamo.comisionCompromisoAcumulado" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Comisión Compromiso Acumulado</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="programac.prestamo.otrosCargosAcumulados"
							   	ng-value="programac.prestamo.otrosCargosAcumulados" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label" >Otros Cargos Acumulados</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="programac.prestamo.presupuestoAsignadoFuncionamiento"
								ng-value="programac.prestamo.presupuestoAsignadoFuncionamiento" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Asignado Funcionamiento</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.presupuestoModificadoFun"
							   	ng-value="programac.prestamo.presupuestoModificadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Modificado Funcionamiento</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.presupuestoVigenteFun"
							   	ng-value="programac.prestamo.presupuestoVigenteFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Presupuesto Vigente Funcionamiento</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"   class="inputText" ng-model="programac.prestamo.presupuestoAsignadoInversion"
							   	ng-value="programac.prestamo.presupuestoAsignadoInversion" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Asignado Inversion</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.presupuestoModificadoInv"
							   	ng-value="programac.prestamo.presupuestoModificadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Modificado Inversión</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.presupuestoVigenteInv"
							   	ng-value="programac.prestamo.presupuestoVigenteInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Vigente Inversión</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.presupuestoDevengadoFun"
							   	ng-value="programac.prestamo.presupuestoDevengadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Devengado Funcionamiento</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number"  class="inputText" ng-model="programac.prestamo.presupuestoDevengadoInv"
							   	ng-value="programac.prestamo.presupuestoDevengadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Devengado Inversión</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.presupuestoPagadoFun"
							   	ng-value="programac.prestamo.presupuestoPagadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Presupuesto Pagado Funcionamiento </label>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="programac.prestamo.presupuestoPagadoInv"
							   		ng-value="programac.prestamo.presupuestoPagadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Presupuesto Pagado Inversión </label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.saldoCuentas"
							   	ng-value="programac.prestamo.saldoCuentas" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Saldo de Cuentas </label>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.desembolsoReal"
							   	ng-value="programac.prestamo.desembolsoReal" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Desembolso Real GTQ</label>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.desembolsoAFechaUe"
							   	ng-value="programac.prestamo.desembolsoAFechaUe" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Desembolso a la Fecha</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="programac.prestamo.montoPorDesembolsarUe"
							   	ng-value="programac.prestamo.montoPorDesembolsarUe" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Monto por Desembolsar</label>
							</div>
						</div>
					</div>

					<div class="form-group">
		            	<input type="text" class="inputText"
		            	ng-click="programac.buscarEstadoEjecucion()"
		            	ng-model="programac.prestamo.ejecucionEstadoNombre" ng-readonly="true"
		            	onblur="this.setAttribute('value', this.value);" ng-value="programac.prestamo.ejecucionEstadoNombre" />
		            	<span class="label-icon" ng-click="programac.buscarEstadoEjecucion()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label class="floating-label">Estado de Ejecución</label>
					</div>

					<div class="form-group">
						<input type="text" ng-model="programac.proyecto.descripcion"
							class="inputText" id="campo2"
							ng-value="programac.proyecto.descripcion" onblur="this.setAttribute('value', this.value);">
						<label for="campo2" class="floating-label">Descripción</label>
					</div>

				</uib-tab>
					
				</uib-tabset>	
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>