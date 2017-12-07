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
		
		.totalCorrecto{
			border-bottom: 1px solid #398439;
		}
		
		.totalError{
			border-bottom: 1px solid #a94442;
		}
	
	</style>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="prestamoController as prestamoc"
	ng-class=" prestamoc.esTreeview ? 'maincontainer_treeview all_page' : 'maincontainer all_page'" id="title">
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
    <script type="text/ng-template" id="buscarPorPrestamo.jsp">
    		<%@ include file="/app/components/prestamo/buscarPorPrestamo.jsp"%>
  	 </script>
  	  <script type="text/ng-template" id="congelarDescongelar.jsp">
    		<%@ include file="/app/components/prestamo/congelarDescongelar.jsp"%>
  	 </script>
	<shiro:lacksPermission name="24010">
		<span ng-init="prestamoc.redireccionSinPermisos()"></span>
	</shiro:lacksPermission>
	<shiro:hasPermission name="43010">
		<span ng-init="prestamoc.cambioOrden()"></span>
	</shiro:hasPermission>
	<div class="panel panel-default" ng-if="!prestamoc.esTreeview">
	  <div class="panel-heading"><h3>Información General de Préstamos</h3></div>
	</div>
	<div align="center" ng-hide="prestamoc.esColapsado" ng-if="!prestamoc.esTreeview">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
			<shiro:hasPermission name="24040">
				<label class="btn btn-primary" ng-click="prestamoc.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24020">
				<label class="btn btn-primary" ng-click="prestamoc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			</shiro:hasPermission>
			<shiro:hasPermission name="24030">
				<label class="btn btn-danger" ng-click="prestamoc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</shiro:hasPermission>
			</div>
		</div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group" role="group" aria-label="">
					<shiro:hasPermission name="24010">
						<a class="btn btn-default" href ng-click="prestamoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</shiro:hasPermission>
					</div>
		</div>
		<shiro:hasPermission name="24010">
		<div class="col-sm-12" align="center">
			<br/>
			<div id="grid1" ui-grid="prestamoc.gridOpciones" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination
				ui-grid-pagination>
				<div class="grid_loading" ng-hide="!prestamoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
			</div>
			<br/>
			<div class="total-rows">Total de {{  prestamoc.totalPrestamos + (prestamoc.totalPrestamos == 1 ? "Préstamo" : " Préstamos" ) }}</div>
				<ul uib-pagination total-items="prestamoc.totalPrestamos"
						ng-model="prestamoc.paginaActual"
						max-size="prestamoc.numeroMaximoPaginas"
						items-per-page="prestamoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="prestamoc.cambioPagina()"
				></ul>
		</div>

		</shiro:hasPermission>
	</div>
	<div class="row second-main-form" ng-show="prestamoc.esColapsado || prestamoc.esTreeview">
		<div class="page-header">
			<h2 ng-if="prestamoc.esNuevo"><small>Nuevo Préstamo</small></h2>
			<h2 ng-if="!prestamoc.esNuevo"><small>Edición de Préstamo</small></h2>
			</div>
		<div class="operation_buttons">
			<div class="btn-group" ng-hide="prestamoc.esNuevo">
				<label class="btn btn-default" ng-if="!prestamoc.esTreeview" ng-click="prestamoc.botones ? prestamoc.irAPepsArbol(prestamoc.prestamo.id) : ''" uib-tooltip="Ver PEPs en Vista Árbol" tooltip-placement="bottom">
				Vista de Árbol</label>
				<label class="btn btn-default" ng-if="!prestamoc.esTreeview" ng-click="prestamoc.botones ? prestamoc.irAPeps(prestamoc.prestamo.id) : ''" uib-tooltip="Ver PEPs en Vista de Lista" tooltip-placement="bottom">
				Vista de Lista</label>
				<label class="btn btn-default" ng-click="prestamoc.congelarDescongelar()" uib-tooltip="Congelar o descongelar linea base" >
				<span class="glyphicon glyphicon glyphicon-bookmark" aria-hidden="true"></span></label>
			</div>
			<div class="btn-group" style="float: right;">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="prestamoc.mForm.$valid && prestamoc.botones ? prestamoc.guardar(form.$valid) : ''" ng-disabled="!prestamoc.mForm.$valid || !prestamoc.botones" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label ng-if="!prestamoc.esTreeview" class="btn btn-primary" ng-click="prestamoc.botones ? prestamoc.irATabla() : ''" uib-tooltip="Ir a Tabla" tooltip-placement="bottom" ng-disabled="!prestamoc.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="prestamoc.esTreeview" class="btn btn-danger" ng-click=" prestamoc.botones ? prestamoc.t_borrar() : ''" ng-disabled="!(prestamoc.proyecto.id>0) || !prestamoc.botones" uib-tooltip="Borrar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
		<div class="col-sm-12" ng-if="prestamoc.proyecto.projectCargado==1">
				<div class="componente_sigade">Estructura importada desde un archivo de Project</div>
		</div>
		<br>
		<div class="col-sm-12" ng-if="prestamoc.diferenciaCambios != 0 && !prestamo.esNuevo">
				<div class="alert alert-warning" style="text-align: center;">Cambios en techos de componentes de sigade, ajuste los montos</div>
		</div>
		<br>
		<div class="col-sm-12">
			<form name="prestamoc.mForm" style="margin-top: 10px;">
			<uib-tabset active="prestamoc.active">
				<shiro:hasPermission name="43010">
				<uib-tab ng-click="prestamoc.getPorcentajes();" index="0" heading="Préstamo" >
					<div class="form-group">
						<label for="id" class="floating-label id_class">ID {{ prestamoc.prestamo.id }}</label>
						<br/><br/>
					</div>
					
					<div class="row" style="margin-top: 15px;">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="text" class="inputText"   
								ng-model="prestamoc.prestamo.codigoPresupuestario" ng-readonly="true" ng-required="true"
								ng-click="prestamoc.buscarCodigoPresupuestario()"
								onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.codigoPresupuestario" />			            	
								<span class="label-icon" ng-click="prestamoc.buscarCodigoPresupuestario()" tabindex="-1">
									<i class="glyphicon glyphicon-search"></i>
								</span>
								<label class="floating-label">* Código presupuestario</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="text" class="inputText"  ng-model="prestamoc.prestamo.numeroPrestamo" ng-required="true" 
								ng-value="prestamoc.prestamo.numeroPrestamo" onblur="this.setAttribute('value', this.value);">
								<label class="floating-label" >* Número de {{etiquetas.proyecto}}</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
							<input type="text" class="inputText"   ng-model="prestamoc.prestamo.proyectoPrograma" ng-required="true"
							onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.proyectoPrograma"  >
							<label class="floating-label">* Proyecto/Programa</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group" >
								<div id="cooperante" angucomplete-alt placeholder="" pause="100" selected-object="prestamoc.cambioCooperante"
									  local-data="prestamoc.cooperantes" search-fields="nombre" title-field="nombre" field-required="true" 
									  field-label="* Organismo Financiero Internacional"
									  minlength="2" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight"
									  initial-value="prestamoc.prestamo.cooperantenombre" focus-out="prestamoc.blurCooperante()"></div>
							</div>
						</div>
					</div>
					<div class="row" >
						<div class="col-sm-12">
								<div class="form-group">
								   	<input type="text" class="inputText"  ng-model="prestamoc.prestamo.sectorEconomico" 
								   	ng-value="prestamoc.prestamo.sectorEconomico" onblur="this.setAttribute('value', this.value);">
								   	<label class="floating-label" >Sector Económico</label>
								</div>
						</div>
					</div>
						
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaElegibilidadUe" is-open="prestamoc.fe_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="prestamoc.setPorcentaje(5);"
									ng-value="prestamoc.prestamo.fechaElegibilidadUe" onblur="this.setAttribute('value', this.value);" />
								<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1009)" tabindex="-1">
										<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">* Fecha de Elegibilidad</label>
							</div>
						</div>
								
						<div class="col-sm-4">
							<div class="form-group">
								<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaSuscripcion" is-open="prestamoc.fs_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
									ng-value="prestamoc.prestamo.fechaSuscripcion" onblur="this.setAttribute('value', this.value);"
									/>
									<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1008)" tabindex="-1">
										<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">* Fecha de Suscripción</label>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaVigencia" is-open="prestamoc.fv_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
									ng-value="prestamoc.prestamo.fechaVigencia" onblur="this.setAttribute('value', this.value);"
									/>
									<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1012)" tabindex="-1">
										<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">* Fecha de vigencia</label>
							</div>
						</div>
					</div>
					
					
					<div class="row" style="margin-top: 15px;">
								<div class="col-sm-4">
									<div class="form-group">
											<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
												ng-model="prestamoc.prestamo.fechaCierreOrigianlUe" is-open="prestamoc.fco_abierto"
												datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="prestamoc.setPorcentaje(5);"
												ng-value="prestamoc.prestamo.fechaCierreOrigianlUe" onblur="this.setAttribute('value', this.value);"
											/>
											<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1010)" tabindex="-1">
													<i class="glyphicon glyphicon-calendar"></i>
											</span>
										<label class="floating-label">* Fecha de Cierre Original</label>
									</div>
								</div>
								
								<div class="col-sm-4">
									<div class="form-group">
											<input type="text" class="inputText"   uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
												ng-model="prestamoc.prestamo.fechaCierreActualUe" is-open="prestamoc.fca_abierto"
												datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true" ng-change="prestamoc.setPorcentaje(5);"
												ng-value="prestamoc.prestamo.fechaCierreActualUe" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1011)" tabindex="-1"
											ng-readonly="true">
													<i class="glyphicon glyphicon-calendar"></i>
											</span>
										<label  class="floating-label">* Fecha de Cierre Actual</label>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="form-group">
										<input type="text" 
										class="inputText input-money" 
										ng-model="prestamoc.prestamo.mesesProrrogaUe" 
										ng-required="true"
										ng-readonly="true"
										ng-value="prestamoc.prestamo.mesesProrrogaUe"
										onblur="this.setAttribute('value', this.value);" 
										ui-number-mask="2">
										<label class="floating-label">* Meses de Prórroga</label>
									</div>
								</div>
								
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="text" class="inputText input-money"   ng-model="prestamoc.prestamo.plazoEjecucionUe" ng-disabled="true"
										ng-value="prestamoc.prestamo.plazoEjecucionUe" onblur="this.setAttribute('value', this.value);" ui-number-mask="2"/>
										<label class="floating-label">Plazo de Ejecución %</label>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group">
									   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.saldoCuentas"
									   	ng-value="prestamoc.prestamo.saldoCuentas" onblur="this.setAttribute('value', this.value);"/>
									   	<label class="floating-label">Saldo de Cuentas </label>
									</div>
								</div>
							</div>
					
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  
								ng-model="prestamoc.prestamo.tipoMonedaNombre" ng-readonly="true" ng-required="true"
								ng-click="prestamoc.buscarTipoMoneda()"
								onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.tipoMonedaNombre"/>
								<span class="label-icon" ng-click="prestamoc.buscarTipoMoneda()" tabindex="-1">
									<i class="glyphicon glyphicon-search"></i>
								</span>
								<label class="floating-label">* Tipo de Moneda</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group money-input">
								<input type="text" 
								 class="inputText input-money"  
								 ng-model="prestamoc.prestamo.montoContratado" 
								 ng-required="true"
								 ng-value="prestamoc.prestamo.montoContratado"
								 onblur="this.setAttribute('value', this.value);" 
								 ng-change="prestamoc.setPorcentaje(1);" 
								 ui-number-mask="0"
								 >
								<label class="floating-label" >* Monto Contratado</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" 
								 class="inputText input-money"  
								 ng-model="prestamoc.prestamo.montoContratadoUsd" 
								 ng-required="true"
								 ng-value="prestamoc.prestamo.montoContratadoUsd" 
								 onblur="this.setAttribute('value', this.value);" 
								 ng-change="prestamoc.setPorcentaje(2);"
								 ui-number-mask="0"
								 >
								 <label class="floating-label" >* Monto Contratado Equivalente $</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" 
								class="inputText input-money" 
								ng-model="prestamoc.prestamo.montoContratadoQtz" 
								ng-required="true"
								ng-value="prestamoc.prestamo.montoContratadoQtz" 
								onblur="this.setAttribute('value', this.value);"
								ui-number-mask="0"
								>
								<label class="floating-label" >* Monto Contratado Equivalente Q</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input inputText="text" 
								class="inputText input-money" 
								ng-model="prestamoc.prestamo.desembolsoAFechaUsd" 
								ng-required="true"
								ng-value="prestamoc.prestamo.desembolsoAFechaUsd" 
								onblur="this.setAttribute('value', this.value);" 
								ng-change="prestamoc.setPorcentaje(1);"
								ui-number-mask="0"
								>
								<label class="floating-label">* Desembolso a la Fecha $</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText input-money" ng-model="prestamoc.prestamo.desembolsoAFechaUsdP" ng-disabled="true"
								ng-value="prestamoc.prestamo.desembolsoAFechaUsdP" onblur="this.setAttribute('value', this.value);" ui-number-mask="2"/>
								<label class="floating-label">Desembolso a la Fecha %</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" 
								class="inputText input-money" 
								ng-model="prestamoc.prestamo.montoPorDesembolsarUsd" 
								ng-required="true"
								ng-value="prestamoc.prestamo.montoPorDesembolsarUsd" 
								onblur="this.setAttribute('value', this.value);" 
								ng-change="prestamoc.setPorcentaje(2);"
								ng-disabled="true"
								ui-number-mask="0"
								/>
								<label class="floating-label">* Monto por Desembolsar $</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText input-money" ng-model="prestamoc.prestamo.montoPorDesembolsarUsdP" ng-disabled="true"
								ng-value="prestamoc.prestamo.montoPorDesembolsarUsdP" onblur="this.setAttribute('value', this.value);" ui-number-mask="0"/>
								<label class="floating-label">Monto por Desembolsar %</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">    						
								<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}" alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaDecreto" is-open="prestamoc.fd_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
									ng-value="prestamoc.prestamo.fechaDecreto" onblur="this.setAttribute('value', this.value);"
								/>
								<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1007)" tabindex="-1">	
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">* Fecha Decreto</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  type="text" class="inputText"   ng-model="prestamoc.prestamo.numeroAutorizacion" 
							   	onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.numeroAutorizacion">
							   	<label class="floating-label">Número de Decreto</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
								<input type="number" style="text-align: right;"
								 class="inputText "  
								 ng-model="prestamoc.prestamo.porcentajeAvance"
								 ng-value="prestamoc.prestamo.porcentajeAvance"
								 onblur="this.setAttribute('value', this.value);"
								 min="0" max="100">
								<label class="floating-label" >Avance del Préstamo %</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group" >
									<textarea class="inputText" rows="4"  
										ng-model="prestamoc.prestamo.objetivo"
										class="inputText" id="objetivo"
										ng-value="prestamoc.prestamo.objetivo" onblur="this.setAttribute('value', this.value);">
									</textarea>
									<label for="objetivo" class="floating-label">Objetivo del Préstamo</label>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group" >
									<textarea class="inputText" rows="4"  
									    ng-model="prestamoc.prestamo.objetivoEspecifico"
										class="inputText" 
										ng-value="prestamoc.prestamo.objetivoEspecifico" onblur="this.setAttribute('value', this.value);">
									</textarea>
									<label for="objetivo" class="floating-label">Objetivos específicos del Préstamo</label>
							</div>
							</div>
					</div>
					<br>
					<div align="center">
						<h5>Tipo de Préstamo</h5>
						<div style="height: 35px;" >
							<div style="text-align: right;">
								<div class="btn-group" role="group" aria-label="">
									<a class="btn btn-default" href
										ng-click="prestamoc.buscarTiposPrestamo()" role="button"
										uib-tooltip="Asignar un tipo de préstamo" tooltip-placement="left">
										<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
									</a>
								</div>
							</div>
						</div>
						<br/>
						<table st-table="prestamoc.prestamotipos"
							class="table table-striped table-bordered table-hover table-propiedades">
							<thead >
								<tr>
									<th style="width: 60px;">ID</th>
									<th>Nombre</th>
									<th style="width: 30px;">Quitar</th>
			
								</tr>
							</thead>
							<tbody>
								<tr st-select-row="row"
									ng-repeat="row in prestamoc.prestamotipos">
									<td>{{row.id}}</td>
									<td>{{row.nombre}}</td>
									<td>
										<button type="button"
											ng-click="prestamoc.eliminarTiposPrestamo(row)"
											class="btn btn-sm btn-danger">
											<i class="glyphicon glyphicon-minus-sign"> </i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<br>
					<div class="panel panel-default" ng-hide="prestamoc.esNuevoDocumento" >
						<div class="panel-heading label-form" style="text-align: center;">Archivos adjuntos</div>
						<div class="panel-body">
							<div style="width: 95%; float: left">
							<table st-table="prestamoc.displayedCollection" st-safe-src="prestamoc.rowCollection" class="table table-striped">
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
								<tr ng-repeat="row in prestamoc.displayedCollection">
									<td style="display: none;">{{row.id}}</td>
									<td>{{row.nombre}}</td>
									<td>{{row.extension}}</td>
									<td align="center">
										<button type="button"
											ng-click="prestamoc.descargarDocumento(row)"
											uib-tooltip="Descargar documento" tooltip-placement="bottom"
											class="btn btn-default">
											<i class="glyphicon glyphicon-download-alt"> </i>
										</button>
									</td>
									<td align="center">
										<button type="button"
											ng-click="prestamoc.eliminarDocumento(row)"
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
									<label class="btn btn-default" ng-model="prestamoc.adjuntarDocumento"
										ng-click="prestamoc.adjuntarDocumentos();" uib-tooltip="Adjuntar documento" tooltip-placement="bottom">
									<span class="glyphicon glyphicon-plus"></span></label>
								</div>
	        				</div>
						</div>
					</div>
					<div class="row">
						<div class="panel panel-default">
							<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label class="label-form" for="usuarioCreo">Usuario que creo</label>
						  					<p>{{ prestamoc.prestamo.usuarioCreo }}</pl>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label  class="label-form"  for="fechaCreacion">Fecha de creación</label>
						  					<p>{{ prestamoc.prestamo.fechaCreacion }}</p>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group" style="text-align: right">
											<label  class="label-form" for="usuarioActualizo">Usuario que actualizo</label>
						  					<p>{{ prestamoc.prestamo.usuarioActualizo }}</p>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label for="fechaActualizacion"  class="label-form" >Fecha de actualizacion</label>
						  					<p>{{ prestamoc.prestamo.fechaActualizacion }}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>	
				</uib-tab>
				</shiro:hasPermission>
				<shiro:hasPermission name="43010">
				
				<uib-tab ng-click="prestamoc.getPorcentajes();" index="prestamoc.ordenTab+1" heading="Configuraciones Préstamo" ng-if="false">
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">							
										<input type="text" 
										class="inputText input-money" 
										ng-model="prestamoc.prestamo.montoAsignadoUe" 
										ng-required="true"
										ng-value="prestamoc.prestamo.montoAsignadoUe"
										onblur="this.setAttribute('value', this.value);" 
										ui-number-mask="0"
										>
										<label class="floating-label">* Monto Asignado</label>
									</div>
								</div>
							</div>
							<div class="row">
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input 
										type="text" 
										class="inputText input-money" 
										ng-model="prestamoc.prestamo.montoAsignadoUeUsd" 
										ng-required="true" 
										ng-value="prestamoc.prestamo.montoAsignadoUeUsd" 
										onblur="this.setAttribute('value', this.value);"
										ng-change="prestamoc.setPorcentaje(3);"
										ui-number-mask="0"
										/>
										<label class="floating-label">* Monto Asignado $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input 
										type="text" 
										class="inputText input-money" 
										ng-model="prestamoc.prestamo.montoAsignadoUeQtz" 
										ng-required="true" 
										ng-value="prestamoc.prestamo.montoAsignadoUeQtz" 
										onblur="this.setAttribute('value', this.value);" 
										ui-number-mask="2"
										>
										<label class="floating-label">* Monto Asignado Q</label>
									</div>
								</div>
							</div>
							
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">
										<input type="text" 
										class="inputText input-money" 
										ng-model="prestamoc.prestamo.desembolsoAFechaUeUsd" 
										ng-required="true"
										ng-value="prestamoc.prestamo.desembolsoAFechaUeUsd" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="prestamoc.setPorcentaje(3);"
										ui-number-mask="0"
										>
										<label class="floating-label">* Desembolso a la Fecha $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">
										<input type="text" class="inputText input-money" ng-model="prestamoc.prestamo.desembolsoAFechaUeUsdP" ng-disabled="true"
										ng-value="prestamoc.prestamo.desembolsoAFechaUeUsdP" onblur="this.setAttribute('value', this.value);" ui-number-mask="2"/>
										<label class="floating-label">Desembolsos a la fecha %</label>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<div class="form-group">							
										<input type="text" 
										class="inputText input-money" 
										ng-model="prestamoc.prestamo.montoPorDesembolsarUeUsd" 
										ng-required="true" 
										ng-value="prestamoc.prestamo.montoPorDesembolsarUeUsd" 
										onblur="this.setAttribute('value', this.value);" 
										ng-change="prestamoc.setPorcentaje(4);"
										ng-disabled="true"
										ui-number-mask="0"
										/>
										<label class="floating-label">* Monto por desembolsar $</label>
									</div>
								</div>
								
								<div class="col-sm-6">
									<div class="form-group">							
										<input type="text" class="inputText input-money" ng-model="prestamoc.prestamo.montoPorDesembolsarUeUsdP" ng-disabled="true" 
										ng-value="prestamoc.prestamo.montoPorDesembolsarUeUsdP" onblur="this.setAttribute('value', this.value);" ui-number-mask="2"/>
										<label class="floating-label">Monto por desembolsar %</label>
									</div>
								</div>
							</div>
				
				</uib-tab>
				
				<uib-tab index="prestamoc.ordenTab+2" heading="Presupuesto" ng-if="prestamoc.mostrarPrestamo" >
				
					<div class="row" style="margin-top: 15px;">
						<div class="col-sm-12">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="prestamoc.prestamo.presupuestoAsignadoFuncionamiento" 
								ng-value="prestamoc.prestamo.presupuestoAsignadoFuncionamiento" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Asignado Funcionamiento</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-12">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.presupuestoVigenteFun" 
							   	ng-value="prestamoc.prestamo.presupuestoVigenteFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">P. Vigente Funcionamiento</label>
							</div>
						</div>
					</div>
					
					<div class="row" >
						<div class="col-sm-12">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.presupuestoDevengadoFun" 
							   	ng-value="prestamoc.prestamo.presupuestoDevengadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Devengado Funcionamiento</label>
							</div>
						</div>
					</div>
					
					<div ng-if="false" >
				
					<div class="row" style="margin-top: 15px;" >
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="text" class="inputText"  ng-model="prestamoc.prestamo.destino" 
							   	ng-value="prestamoc.prestamo.destino" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label" >Destino</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaFirma" is-open="prestamoc.ff_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="prestamoc.abrirPopupFecha(1004)" ng-value="prestamoc.prestamo.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1004)" tabindex="-1"
								ng-readonly="true">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Firma</label>
								
							</div>
						</div>
					</div>
					<div class="row" ng-show="false">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
											ng-model="prestamoc.prestamo.fechaCorte" is-open="prestamoc.fc_abierto"
													datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
													ng-click="prestamoc.abrirPopupFecha(1003)" ng-value="prestamoc.prestamo.fechaCorte" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1003)" tabindex="-1"
								ng-readonly="true">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Corte</label>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group">
				            	<input type="text" class="inputText"    
				            	ng-model="prestamoc.prestamo.tipoAutorizacionNombre" ng-readonly="true" 
				            	ng-click="prestamoc.buscarAutorizacionTipo()"
				            	onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.tipoAutorizacionNombre"			            	/>
				            	<span class="label-icon" ng-click="prestamoc.buscarAutorizacionTipo()" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>				          	
					          	<label class="floating-label">Tipo Autorización</label>
							</div>
						</div>
					</div>
					
					<div class="row" ng-show="false">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  type="number" class="inputText" ng-model="prestamoc.prestamo.aniosPlazo" max="100" min="0" 
							   	ng-value="prestamoc.prestamo.aniosPlazo" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label">Años Plazo</label>
							</div>
						</div>
							
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.aniosGracia" max="100" min="0" 
							   		ng-value="prestamoc.prestamo.aniosGracia" onblur="this.setAttribute('value', this.value);">
							   	<label  class="floating-label">Años de Gracia</label>
							</div>
						</div>
					</div>
					
					<div class="row" ng-show="false">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaAutorizacion" is-open="prestamoc.fa_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="prestamoc.abrirPopupFecha(1005)" ng-value="prestamoc.prestamo.fechaAutorizacion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1005)" tabindex="-1"
								ng-readonly="true">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha de Autorización</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<input type="text" class="inputText"  uib-datepicker-popup="{{prestamoc.formatofecha}}"  alt-input-formats="{{prestamoc.altformatofecha}}"
									ng-model="prestamoc.prestamo.fechaFinEjecucion" is-open="prestamoc.ffe_abierto"
									datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
									ng-click="prestamoc.abrirPopupFecha(1006)" ng-value="prestamoc.prestamo.fechaFinEjecucion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="prestamoc.abrirPopupFecha(1006)" tabindex="-1"
								ng-readonly="true">
									<i class="glyphicon glyphicon-calendar"></i>
								</span>
								<label class="floating-label">Fecha Fin de Ejecución</label>
							</div>
						</div>
					</div>
					
					<div class="row" ng-show="false">
						<div class="col-sm-6">
							<div class="form-group">
				            	<input type="text" class="inputText" ng-model="prestamoc.prestamo.tipoInteresNombre" ng-readonly="true"
				            	ng-click="prestamoc.buscarInteresTipo()"
				            	onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.tipoInteresNombre" tabindex="-1"/>
				            	<span class="label-icon" ng-click="prestamoc.buscarInteresTipo()" tabindex="-1">
				            		<i class="glyphicon glyphicon-search"></i>
				            	</span>
					          	<label class="floating-label">Tipo de Interés</label>
							</div>
						</div>
						
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="prestamoc.prestamo.porcentajeInteres"  
							   	onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.porcentajeInteres"/>
							   	<label class="floating-label">Porcentaje de Interés</label>
							</div>
						</div>
					</div>
					
					<div class="row" ng-show="false">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input  class="inputText" type="number"  ng-model="prestamoc.prestamo.periodoEjecucion" max="100" min="0" 
							   	ng-value="prestamoc.prestamo.periodoEjecucion" onblur="this.setAttribute('value', this.value);">
							   	<label class="floating-label">Período de Ejecución</label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="prestamoc.prestamo.porcentajeComisionCompra"  
							   	onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.porcentajeComisionCompra"/>
							   	<label class="floating-label">Porc. Comisión Compra</label>
							</div>
						</div>
					</div>
					
					<div class="row" ng-show="false">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.amortizado"  
							   	ng-value="prestamoc.prestamo.amortizado" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Amortizado</label>
							</div>
							
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.porAmortizar" 
							   	ng-value="prestamoc.prestamo.porAmortizar" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Por Amortizar</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="prestamoc.prestamo.principalAnio" 
							   	ng-value="prestamoc.prestamo.principalAnio" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Principal del Año</label>
							</div>
						</div>
					</div>
					
					<div class="row" >						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.interesesAnio"  
							   	ng-value="prestamoc.prestamo.interesesAnio" onblur="this.setAttribute('value', this.value);" />
							   	<label class="floating-label">Intereses del Año</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.comisionCompromisoAnio"  
							   	ng-value="prestamoc.prestamo.comisionCompromisoAnio" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Com. Compromiso del Año</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.otrosGastos"  
							   	ng-value="prestamoc.prestamo.otrosGastos" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Otros Gastos</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.principalAcumulado"  
							   	ng-value="prestamoc.prestamo.principalAcumulado" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">Principal Acumulado</label>
							</div>
						</div>
					</div>
					
					<div class="row" > 
						<div class="col-sm-3" >
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="prestamoc.prestamo.interesesAcumulados" 
							   	ng-value="prestamoc.prestamo.interesesAcumulados" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Intereses Acumulados</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="prestamoc.prestamo.comisionCompromisoAcumulado"  
							   	ng-value="prestamoc.prestamo.comisionCompromisoAcumulado" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Com. Compromiso Acumulado</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"  class="inputText"  ng-model="prestamoc.prestamo.otrosCargosAcumulados"  
							   	ng-value="prestamoc.prestamo.otrosCargosAcumulados" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label" >Otros Cargos Acumulados</label>
							</div>
						</div>					
					</div>
					
					<div class="row">	
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number"   class="inputText" ng-model="prestamoc.prestamo.presupuestoAsignadoInversion"  
							   	ng-value="prestamoc.prestamo.presupuestoAsignadoInversion" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Asignado Inversion</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="prestamoc.prestamo.presupuestoModificadoInv" 
							   	ng-value="prestamoc.prestamo.presupuestoModificadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Modificado Inversión</label>
							</div>
						</div>

						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="prestamoc.prestamo.presupuestoVigenteInv" 
							   	ng-value="prestamoc.prestamo.presupuestoVigenteInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Vigente Inversión</label>
							</div>
						</div>
					</div>		
								
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
							   	<input type="number"  class="inputText" ng-model="prestamoc.prestamo.presupuestoDevengadoInv"  
							   	ng-value="prestamoc.prestamo.presupuestoDevengadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Devengado Inversión</label>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.presupuestoPagadoFun" 
							   	ng-value="prestamoc.prestamo.presupuestoPagadoFun" onblur="this.setAttribute('value', this.value);"/>
							   	<label  class="floating-label">P. Pagado Funcionamiento </label>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText"  ng-model="prestamoc.prestamo.presupuestoPagadoInv"  
							   		ng-value="prestamoc.prestamo.presupuestoPagadoInv" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">P. Pagado Inversión </label>
							</div>
						</div>

						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.desembolsoReal"
							   	ng-value="prestamoc.prestamo.desembolsoReal" onblur="this.setAttribute('value', this.value);"/>  
							   	<label class="floating-label">Desembolso Real GTQ</label>
							</div>
						</div>
					</div>
					
					<div class="row">	
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.desembolsoAFechaUe"
							   	ng-value="prestamoc.prestamo.desembolsoAFechaUe" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Desembolso a la Fecha</label>
							</div>
						</div>
						
						<div class="col-sm-3">
							<div class="form-group">
							   	<input type="number" class="inputText" ng-model="prestamoc.prestamo.montoPorDesembolsarUe"
							   	ng-value="prestamoc.prestamo.montoPorDesembolsarUe" onblur="this.setAttribute('value', this.value);"/>
							   	<label class="floating-label">Monto por Desembolsar</label>
							</div>
						</div>
					</div>
					
					<div class="form-group">
		            	<input type="text" class="inputText"  
		            	ng-click="prestamoc.buscarEstadoEjecucion()"
		            	ng-model="prestamoc.prestamo.ejecucionEstadoNombre" ng-readonly="true"
		            	onblur="this.setAttribute('value', this.value);" ng-value="prestamoc.prestamo.ejecucionEstadoNombre" />
		            	<span class="label-icon" ng-click="prestamoc.buscarEstadoEjecucion()" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
			          	<label class="floating-label">Estado de Ejecución</label>
					</div>
					</div>
				</uib-tab>
				</shiro:hasPermission>
				<uib-tab  index="prestamoc.ordenTab+3" heading="Unidades Ejecutoras" ng-if="prestamoc.mostrarPrestamo">
					<table st-table="prestamoc.displayCollectionUE" st-safe-src="prestamoc.rowCollectionUE" class="table table-striped" style="margin-top: 15px;">
						<thead>
							<tr>
								<th class="label-form" style="text-align: center;">Coordinadora</th>
								<th class="label-form" style="text-align: center;">Organismo ejecutor</th>
								<th class="label-form" style="text-align: center;">Entidad</th>
								<th class="label-form" style="text-align: center;">Fecha Elegibilidad</th>
								<th class="label-form" style="text-align: center;">Fecha Cierre</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="row in prestamoc.rowCollectionUE">
	    						<td>
	    							<input type="checkbox"  ng-model="row.esCoordinador" 
	    							ng-change = "prestamoc.cambiarCoordinador($index)" 
									ng-readonly="prestamoc.m_existenDatos"/>
	    						</td>
								<td class="divisionColumna truncate" style="">
	    							<div style="height: 25px;">
	    								<div style="text-align: left">{{row.nombre}}</div>
	    							</div>
	    						</td>
	    						<td class="divisionColumna truncate" style="">
	    							<div style="height: 25px;">
	    								<div style="text-align: left">{{row.entidad}}</div>
	    							</div>
	    						</td>
	    						<td>
	    							<div class="form-group">    						
										<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}" alt-input-formats="{{prestamoc.altformatofecha}}"
											ng-model="row.fechaElegibilidad" is-open="row.fe_abierto"
											datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
											ng-value="row.fechaElegibilidad" onblur="this.setAttribute('value', this.value);"
											ng-readonly="prestamoc.m_existenDatos"
										/>
										<span class="label-icon" ng-click="prestamoc.abrirPopupFechaFE($index)" tabindex="-1">	
											<i class="glyphicon glyphicon-calendar"></i>
										</span>
									</div>
	    						</td>
	    						<td>
	    							<div class="form-group">    						
										<input type="text" class="inputText" uib-datepicker-popup="{{prestamoc.formatofecha}}" alt-input-formats="{{prestamoc.altformatofecha}}"
											ng-model="row.fechaCierre" is-open="row.fc_abierto"
											datepicker-options="prestamoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
											ng-value="row.fechaCierre" onblur="this.setAttribute('value', this.value);"
											ng-readonly="prestamoc.m_existenDatos"
										/>
										<span class="label-icon" ng-click="prestamoc.abrirPopupFechaFC($index)" tabindex="-1">	
											<i class="glyphicon glyphicon-calendar"></i>
										</span>
									</div>
	    						</td>
							</tr>
							
						</tbody>
					</table>
				</uib-tab>
				<uib-tab  index="prestamoc.ordenTab+4" heading="Componentes" ng-if="prestamoc.mostrarPrestamo">
					<table st-table="prestamoc.displayedCollectionComponentes" st-safe-src="prestamoc.rowCollectionComponentes" class="table table-striped table-hover" style="margin-top: 15px;">
						<thead>
							<tr>
								<th class="label-form" style="text-align: center; ">Nombre</th>
								<th class="label-form" style="text-align: center; width: 100px;">Tipo Moneda</th>
								<th class="label-form" style="text-align: center; width: 155px;">Techo</th>
							</tr>
						</thead>
						<tbody>
							<tr ng-repeat="row in prestamoc.rowCollectionComponentes" >
								<td colspan="3">
									<table style="width: 100%">
										<tr ng-click="prestamoc.seleccionarComponente($index)">
											<td style=" text-align: left;">
												<span class="glyphicon glyphicon-plus-sign" ng-hide="row.mostrar"></span>
												<span class="glyphicon glyphicon-minus-sign" ng-hide="!row.mostrar"></span>
				    							{{row.nombre}}
				    						</td>
				    						<td style="text-align: center; width: 80px;">
				    							{{row.tipoMoneda}}
				    						</td>
				    						<td style="text-align: right; width: 130px;">
				    							{{row.techo | formatoMillonesSinTipo : prestamoc.enMillones}}
				    						</td>
										</tr>
										<tr ng-hide="!row.mostrar">
											<td colspan="3" style="padding: 10px 0px 0px 0px;">
												<div class="form-group" >
													<textarea class="inputText" rows="4"  
														ng-model="row.descripcion"
														class="inputText" 
														ng-value="row.descripcion" onblur="this.setAttribute('value', this.value);"
														ng-maxlength="4000" style=" resize: none;">
													</textarea>
													<label  class="floating-label">Descripción</label>
												</div>
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</tbody>
					</table>
				</uib-tab>
				<uib-tab  index="prestamoc.ordenTab+5" heading="Distribución de Unidad Ejecutora" ng-if="prestamoc.mostrarPrestamo">
					<div style="margin-top: 15px;">
						<div class="row">
							<label class="btn btn-default" ng-click="prestamoc.verHistoria()" uib-tooltip="Ver Historia" ng-hide="true">
							<span class="glyphicon glyphicon glyphicon-book" aria-hidden="true"></span></label>
						</div>
						<div class="row">
							<div class="divTabla">
								<table class="table " >
								 	<tr>
								 		<th rowspan="2" class="label-form" style="vertical-align: middle;"> COMPONENTES</th>
								 		<th colspan="3"  class="label-form" ng-repeat= "organismo in prestamoc.m_organismosEjecutores"
								 			style="text-align: center; vertical-align: middle;">
								 			<div>{{ organismo.nombre }}</div>
								 		</th>
								 		<th rowspan="2" class="label-form" style="text-align: center; vertical-align: middle;">Total Asignado</th>
								 		<th rowspan="2" class="label-form" style="text-align: center; vertical-align: middle;">TECHO</th>
								 	</tr>
								 	<tr>
								 		<th colspan="3" ng-repeat= "organismo in prestamoc.m_organismosEjecutores">
								 			<table style="width: 100%">
								 				<tr>
								 					<td class="label-form" style="min-width: 100px; text-align: center;">Prestamo</td>
								 					<td class="label-form" style="min-width: 100px; text-align: center;">Donación</td>
								 					<td class="label-form" style="min-width: 100px;text-align: center;">Nacional</td>
								 				</tr>
								 			</table>
								 		</th>
								 	</tr>
								 	
								 	<tr ng-repeat="row in m_componentes track by $index " class="filaIngreso"
								 	    ng-click="prestamoc.componenteSeleccionado(row)" ng-class="row.isSelected ? 'st-selected' : ''">
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
															ng-change="prestamoc.actualizarTotalesUE()"
														/>
								 					</td>
								 					<td style="text-align: center;">
								 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
															class="inputText input-money"
															ng-model="ue.donacion"
															ng-value="ue.donacion"
															onblur="this.setAttribute('value', this.value);"
															ui-number-mask="2"
															ng-readonly="prestamoc.m_existenDatos"
															ng-change="prestamoc.actualizarTotalesUE()"
														/>
								 					</td>
								 					<td style="text-align: center; border-right: 1px solid #ddd;">
								 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
															class="inputText input-money"
															ng-model="ue.nacional"
															ng-value="ue.nacional"
															onblur="this.setAttribute('value', this.value);"
															ui-number-mask="2"
															ng-readonly="prestamoc.m_existenDatos"
															ng-change="prestamoc.actualizarTotalesUE()"
														/>
								 					</td>
								 				<tr>
								 			</table>
								 		</td>
								 		<td style="min-width: 100px; text-align: right;"
								 			ng-class="row.totalIngesado==row.techo ? 'totalCorrecto':'totalError'"
								 		 	class="label-form"> {{ row.totalIngesado | formatoMillonesSinTipo : prestamoc.enMillones }} 
								 		 </td>
								 		<td style="width: 155px; text-align: right;"
								 			ng-class="row.totalIngesado==row.techo ? 'totalCorrecto':'totalError'"
								 		 	class="label-form"> {{ row.techo | formatoMillonesSinTipo : prestamoc.enMillones }} 
								 		 </td>
								 		 
								 	</tr>
								 	<tr style="border-top: 3px double #ddd;" >
								 		<td style="min-width: 200px;" class="label-form">
								 			Total Asignado
								 		</td>
								 		<td  colspan="3" ng-repeat = "organismo in prestamoc.m_organismosEjecutores">
								 			<table style="width: 100%;">
								 				<tr>
								 					<td style="text-align: right; width: 100px; padding-right: 5px;" class="label-form">
								 						{{ organismo.totalAsignadoPrestamo | formatoMillonesSinTipo : prestamoc.enMillones }}
								 					</td>
								 					<td style="text-align: right; width: 100px; padding-right: 5px;" class="label-form">
								 						{{ organismo.totalAsignadoDonacion | formatoMillonesSinTipo : prestamoc.enMillones }}
								 					</td>
								 					<td style="text-align: right; border-right: 1px single #ddd; width: 100px; padding-right: 5px;" class="label-form">
								 						{{ organismo.totalAsignadoNacional | formatoMillonesSinTipo : prestamoc.enMillones }}
								 					</td>
								 				<tr>
								 			</table>
								 		</td>
								 	</tr>
								</table>
							</div>
						</div>
						<br/>
						<input type="hidden" ng-model="prestamoc.matriz_valid" name="matriz_valid" ng-required="true" />
						
					</div>
				</uib-tab>
				<uib-tab index="prestamoc.ordenTab+6" heading="Indicadores" ng-if="prestamoc.mostrarPrestamo" ng-click="prestamoc.metasActivo()">
					<shiro:lacksPermission name="17010">
						<span ng-init="prestamoc.redireccionSinPermisos()"></span>
					</shiro:lacksPermission>
					<div ng-if="prestamoc.metasCargadas">
						<%@include file="/app/components/meta/meta.jsp" %>
					</div>
		    	</uib-tab>		    	
				<uib-tab index="prestamoc.ordenTab+7" heading="Riesgos" ng-if="prestamoc.mostrarPrestamo" ng-click="prestamoc.riesgosActivo()" >
					<div ng-if="prestamoc.riesgosCargados">
						<%@include file="/app/components/riesgo/riesgo.jsp" %>
					</div>
				</uib-tab>
			</uib-tabset>
			</form>
		</div>
		<div class="col-sm-12 operation_buttons" align="right" style="margin-top: 15px;">
			<div align="center" class="label-form">Los campos marcados con * son obligatorios y las fechas deben tener formato de dd/mm/aaaa</div>
			<br/>
			<div class="btn-group" ng-disabled="!prestamoc.botones">
				<shiro:hasPermission name="24020">
					<label class="btn btn-success" ng-click="prestamoc.mForm.$valid && prestamoc.botones ? prestamoc.guardar(prestamoc.mForm.$valid) : ''" ng-disabled="!prestamoc.mForm.$valid || !prestamoc.botones" uib-tooltip="Guardar" tooltip-placement="top">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				</shiro:hasPermission>
				<label ng-if="!prestamoc.esTreeview" class="btn btn-primary" ng-click="prestamoc.botones ? prestamoc.irATabla() : ''" uib-tooltip="Ir a Tabla" tooltip-placement="top" ng-disabled="!prestamoc.botones">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
				<label ng-if="prestamoc.esTreeview" class="btn btn-danger" ng-click=" prestamoc.botones ? prestamoc.t_borrar() : ''" ng-disabled="!(prestamoc.proyecto.id>0) || !prestamoc.botones" uib-tooltip="Borrar" tooltip-placement="top">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</div>
		</div>
	</div>
</div>

