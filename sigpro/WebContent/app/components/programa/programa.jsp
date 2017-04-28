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

	<h3>Programas</h3>
	<br />
	<div class="row" align="center" ng-hide="programac.esColapsado">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="37040">
				<label class="btn btn-primary" ng-click="programac.nuevo()">Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37010">
				<label class="btn btn-primary" ng-click="programac.editar()">Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="37030">
				<label class="btn btn-primary" ng-click="programac.borrar()">Borrar</label>
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
	<div class="row main-form" ng-show="programac.esColapsado">
		<h4 ng-hide="!programac.esNuevo">Nuevo Programa</h4>
		<h4 ng-hide="programac.esNuevo">Edición de programa</h4>
		
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()">Ir a Tabla</label>
			</div>
		</div>
		<div class="col-sm-12">
			<form name="form">
				<uib-tabset active="active">
					<uib-tab index="0" heading="Datos del programa">
						<div class="form-group">
							<label for="id">ID</label>
		  					<p class="form-control-static">{{ programac.programa.id }}</p>
						</div>
						<div class="form-group">
							<label >* Nombre</label>
							<input type="text"  ng-model="programac.programa.nombre"
								class="form-control" placeholder="Nombre" ng-required="true" >
						</div>
						<div class="form-group" >
							<label >* Tipo Programa</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control" placeholder="Nombre Tipo Programa" ng-model="programac.programatiponombre" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="programac.buscarProgramaTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
		
						<div class="form-group" ng-repeat="campo in programac.camposdinamicos">
							<label for="campo.id">{{ campo.label }}</label>
							<div ng-switch="campo.tipo">
								<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" placeholder="{{campo.label}}" />
								<input ng-switch-when="entero" type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" placeholder="{{campo.label}}"  />
								<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" placeholder="{{campo.label}}" />
								<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
								<p ng-switch-when="fecha" class="input-group">
									<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"/>
														<span class="input-group-btn">
															<button type="button" class="btn btn-default"
																ng-click="programac.abrirPopupFecha($index)">
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
						
						<div class="form-group">
							<label for="campo2">Descripción</label>
							<input type="text" ng-model="programac.programa.descripcion"
								class="form-control" id="campo2" placeholder="Descripción">
						</div>
						<br />
						<div align="center">
							<h5>Proyectos </h5>
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
									<th>ID</th>
									<th>Nombre</th>
									<th>Descripicon</th>
									<th style="width: 30px;">Quitar</th>
		
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
						<div class="panel panel-default">
							<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label >Usuario que creo</label>
						  					<p class="form-control-static">{{ programac.programa.usuarioCreo }}</pl>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label >Fecha de creación</label>
						  					<p class="form-control-static">{{ programac.programa.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label >Usuario que actualizo</label>
						  					<p class="form-control-static">{{ programac.programa.usuarioactualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label >Fecha de actualizacion</label>
						  					<p class="form-control-static">{{ programac.programa.fechaactualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</uib-tab>
					<uib-tab index="1" heading="Datos del préstamo" ng-if="programac.mostrarPrestamo">
					<!--       Préstamo    -->
					
						<div class="form-group">
							<label>Fecha de Corte</label>
    						<p class="input-group">
							<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaCorte" is-open="programac.fc_abierto"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Corte"   />
												<span class="input-group-btn">
												<button type="button" class="btn btn-default"
													ng-click="programac.abrirPopupFecha(1003)">
													<i class="glyphicon glyphicon-calendar"></i>
												</button>
											</span>
							</p>
						</div>
						<div class="form-group">
						    <label >Código presupuestario</label>
							<input class="form-control" type="number"  ng-model="programac.prestamo.codigoPresupuestario"  >
						</div>
						
						<div class="form-group">
							<label >Número de prestamo</label>
						   	<input class="form-control" type="text"  ng-model="programac.prestamo.numeroPrestamo"  >
						</div>
						
						<div class="form-group">
							<label >Destino</label>
						   	<input class="form-control" type="text"  ng-model="programac.prestamo.destino" >
						</div>
						
						<div class="form-group">
							<label >Sector Económico</label>
						   	<input class="form-control" type="text"  ng-model="programac.prestamo.sectorEconomico" >
						</div>
						
						<div class="form-group">
							<label for="campo3">* Unidad Ejecutora</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control"  placeholder="Nombre Unidad Ejecutora" 
				            	ng-model="programac.prestamo.unidadejecutoranombre" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="programac.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label>Fecha de Firma</label>
    						<p class="input-group">
							<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaFirma" is-open="programac.ff_abierto"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Corte"  />
												<span class="input-group-btn">
												<button type="button" class="btn btn-default"
													ng-click="programac.abrirPopupFecha(1004)">
													<i class="glyphicon glyphicon-calendar"></i>
												</button>
											</span>
							</p>
						</div>
						
						<div class="form-group">
							<label for="campo3">Tipo Autorización</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control"  placeholder="Nombre Tipo Autorización" 
				            	ng-model="programac.prestamo.nombreAutorizacionTipo" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="programac.buscarAutorizacionTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label>Número Autorización</label>
						   	<input class="form-control" type="text"  ng-model="programac.prestamo.numeroAutorizacion" >
						</div>
						
						<div class="form-group">
							<label>Fecha de Autorización</label>
    						<p class="input-group">
							<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaAutorizacion" is-open="programac.fa_abierto"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Autorización"  />
												<span class="input-group-btn">
												<button type="button" class="btn btn-default"
													ng-click="programac.abrirPopupFecha(1005)">
													<i class="glyphicon glyphicon-calendar"></i>
												</button>
											</span>
							</p>
						</div>
						
						<div class="form-group">
							<label>Años Plazo</label>
						   	<input class="form-control" type="number"  ng-model="programac.prestamo.aniosPlazo" max="100" min="0" >
						</div>
						
						<div class="form-group">
							<label>Años de Gracia</label>
						   	<input class="form-control" type="number"  ng-model="programac.prestamo.aniosGracia" max="100" min="0" >
						</div>
						
						<div class="form-group">
							<label>Fecha Fin de Ejecución</label>
    						<p class="input-group">
							<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaFinEjecucion" is-open="programac.ffe_abierto"
												datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha Fin de Ejecución"  />
												<span class="input-group-btn">
												<button type="button" class="btn btn-default"
													ng-click="programac.abrirPopupFecha(1006)">
													<i class="glyphicon glyphicon-calendar"></i>
												</button>
											</span>
							</p>
						</div>
						
						<div class="form-group">
							<label>Período de Ejecución</label>
						   	<input class="form-control" type="number"  ng-model="programac.prestamo.periodoEjecucion" max="100" min="0" >
						</div>
						
						<div class="form-group">
							<label for="campo3">Tipo de Inerés</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control"  placeholder="Tipo de interés" 
				            	ng-model="programac.prestamo.nombreTipoInteres" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="programac.buscarInteresTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label>Porcentaje de Interés</label>
						   	<input type="number"  ng-model="programac.prestamo.porcentajeInteres" class="form-control" placeholder="Porcentaje de interés" />
						</div>
						
						<div class="form-group">
							<label>Porcentaje Comisión Compra</label>
						   	<input type="number"  ng-model="programac.prestamo.porcentajeComisionCompra" class="form-control" placeholder="Porcentaje de Comisión Compra" />
						</div>
						
						<div class="form-group">
							<label for="campo3">Tipo de Moneda</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control"  placeholder="Tipo de Moneda" 
				            	ng-model="programac.prestamo.nombreTipoMoneda" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="programac.buscarTipoMoneda()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label>Monto Contratado</label>
						   	<input type="number"  ng-model="programac.prestamo.montoContratado" class="form-control" placeholder="Porcentaje de Comisión Compra" />
						</div>
						
						<div class="form-group">
							<label>Amortizado</label>
						   	<input type="number"  ng-model="programac.prestamo.amortizado" class="form-control" placeholder="Amortizado" />
						</div>
						
						<div class="form-group">
							<label>Por Amortizar</label>
						   	<input type="number"  ng-model="programac.prestamo.porAmortizar" class="form-control" placeholder="Por Amortizado" />
						</div>
						
						<div class="form-group">
							<label>Principal del Año</label>
						   	<input type="number"  ng-model="programac.prestamo.principalAnio" class="form-control" placeholder="Principal del Año" />
						</div>
						
						<div class="form-group">
							<label>Intereses del Año</label>
						   	<input type="number"  ng-model="programac.prestamo.interesesAnio" class="form-control" placeholder="Interes del Año" />
						</div>
						
						<div class="form-group">
							<label>Comisión Compromiso del Año</label>
						   	<input type="number"  ng-model="programac.prestamo.comisionCompromisoAnio" class="form-control" placeholder="Intereses del Año" />
						</div>
						
						<div class="form-group">
							<label>Otros Gastos</label>
						   	<input type="number"  ng-model="programac.prestamo.otrosGastos" class="form-control" placeholder="Otros Gastos" />
						</div>
						
						<div class="form-group">
							<label>Principal Acumulado</label>
						   	<input type="number"  ng-model="programac.prestamo.principalAcumulado" class="form-control" placeholder="Principal Acumulado" />
						</div>
						
						<div class="form-group">
							<label>Intereses Acumulados</label>
						   	<input type="number"  ng-model="programac.prestamo.interesesAcumulados" class="form-control" placeholder="Intereses Acumulados" />
						</div>
						
						<div class="form-group">
							<label>Comisión Compromiso Acumulado</label>
						   	<input type="number"  ng-model="programac.prestamo.comisionCompromisoAcumulado" class="form-control" placeholder="Intereses Acumulados" />
						</div>
						
						<div class="form-group">
							<label>Otros Cargos Acumulados</label>
						   	<input type="number"  ng-model="programac.prestamo.otrosCargosAcumulados" class="form-control" placeholder="Intereses Acumulados" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Asignado Funcionamiento</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoAsignadoFuncionamiento" class="form-control" placeholder="Presupuesto Asignado Funcionamiento" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Asignado Inversion</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoAsignadoInversion" class="form-control" placeholder="Presupuesto Asignado Inversión" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Modificado Funcionamiento</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoModificadoFuncionamiento" class="form-control" placeholder="Presupuesto Modificado Funcionamiento" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Modificado Inversión</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoModificadoInversion" class="form-control" placeholder="Presupuesto Modificado Inversión" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Vigente Funcionamiento</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoVigenteFuncionamiento" class="form-control" placeholder="Presupuesto VigenteFuncionamiento" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Vigente Inversión</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoVigenteInversion" class="form-control" placeholder="Presupuesto Vignete Inversión" />
						</div>
						
						
						<div class="form-group">
							<label>Presupuesto Devengado Funcionamiento</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoDevengadoFuncionamiento" class="form-control" placeholder="Presupuesto Devengado Funcionamiento" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Devengado Inversión</label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoDevengadoInversion" class="form-control" placeholder="Presupuesto Devengado Inversion" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Pagado Funcionamiento </label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoPagadoFuncionamiento" class="form-control" placeholder="Presupuesto Pagado Funcionamiento" />
						</div>
						
						<div class="form-group">
							<label>Presupuesto Pagado Inversión </label>
						   	<input type="number"  ng-model="programac.prestamo.presupuestoPagadoInversion" class="form-control" placeholder="Presupuesto Pagado Inversión" />
						</div>
						
						<div class="form-group">
							<label>Saldo de Cuentas </label>
						   	<input type="number"  ng-model="programac.prestamo.saldoCuentas" class="form-control" placeholder="Saldo de Cuentas" />
						</div>
						
						<div class="form-group">
							<label>Desembolso Real GTQ</label>
						   	<input type="number"  ng-model="programac.prestamo.desembolsoReal" class="form-control" placeholder="Desembolso Real" />
						</div>
						
						
						
						<div class="form-group">
							<label for="campo3">Estado de Ejecución</label>
				          	<div class="input-group">
				            	<input type="text" class="form-control"  placeholder="Estado de Ejecución" 
				            	ng-model="programac.prestamo.ejecucionEstadoNombre" ng-readonly="true" ng-required="true"/>
				            	<span class="input-group-addon" ng-click="programac.buscarEstadoEjecucion()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label >Proyecto/Programa</label>
						   	<input class="form-control" type="text"  ng-model="programac.prestamo.proyectoPrograma" >
						</div>
						
						<div class="form-group">
							<label>Fecha Decreto</label>
    						<p class="input-group">
								<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaDecreto" is-open="programac.fd_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha Fin de Decreto"  />
									<span class="input-group-btn">
									<button type="button" class="btn btn-default"
										ng-click="programac.abrirPopupFecha(1007)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
						
						<div class="form-group">
							<label>Fecha de Suscripción</label>
    						<p class="input-group">
								<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaSuscripcion" is-open="programac.fs_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Suscripción"  />
									<span class="input-group-btn">
									<button type="button" class="btn btn-default"
										ng-click="programac.abrirPopupFecha(1008)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
						
						<div class="form-group">
							<label>Fecha de Elegibilidad</label>
    						<p class="input-group">
								<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaElegibilidadUe" is-open="programac.fe_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Suscripción"  />
									<span class="input-group-btn">
									<button type="button" class="btn btn-default"
										ng-click="programac.abrirPopupFecha(1009)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
						
						<div class="form-group">
							<label>Fecha de Cierre Original</label>
    						<p class="input-group">
								<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaCierreOrigianlUe" is-open="programac.fco_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Suscripción"  />
									<span class="input-group-btn">
									<button type="button" class="btn btn-default"
										ng-click="programac.abrirPopupFecha(1010)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
						
						<div class="form-group">
							<label>Fecha de Cierre Actual</label>
    						<p class="input-group">
								<input type="text" class="form-control" uib-datepicker-popup="{{programac.formatofecha}}" ng-model="programac.prestamo.fechaCierreActualUe" is-open="programac.fca_abierto"
									datepicker-options="programac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" placeholder="Fecha de Suscripción"  />
									<span class="input-group-btn">
									<button type="button" class="btn btn-default"
										ng-click="programac.abrirPopupFecha(1011)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
						
						
						<div class="form-group">
							<label>Meses de Prórroga</label>
						   	<input type="number"  ng-model="programac.prestamo.mesesProrrogaUe" class="form-control" placeholder="Meses de Prorroga" />
						</div>
						
						<div class="form-group">
							<label>Plazo de Ejecución</label>
						   	<input type="number"  ng-model="programac.prestamo.plazoEjecucionUe" class="form-control" placeholder="Plazo de Ejecución" />
						</div>
						
						<div class="form-group">
							<label>Monto Asignado</label>
						   	<input type="number"  ng-model="programac.prestamo.montoAsignadoUe" class="form-control" placeholder="Monto Asignado " />
						</div>
						
						<div class="form-group">
							<label>Desembolso a la Fecha</label>
						   	<input type="number"  ng-model="programac.prestamo.desembolsoAFechaUe" class="form-control" placeholder="Desembolso a la Fecha" />
						</div>
						
						<div class="form-group">
							<label>Monto por Desembolsar</label>
						   	<input type="number"  ng-model="programac.prestamo.montoPorDesembolsarUe" class="form-control" placeholder="Monto por Desembolsar" />
						</div>
						
						
						
								
						
					</uib-tab>
				</uib-tabset>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="37020">
					<label class="btn btn-success" ng-click="form.$valid ? programac.guardar(form.$valid) : ''" ng-disabled="!form.$valid">Guardar</label>
				</shiro:hasPermission>
				<label class="btn btn-primary" ng-click="programac.irATabla()">Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>