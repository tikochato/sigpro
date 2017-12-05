<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<style>
  	    .cuerpoTablaDatos {
		    overflow-y: scroll;
		    overflow-x: hidden;
		    text-align: center;
		}
		
		.theadDatos {
			flex-shrink: 0; overflow-x: hidden;
		}
		
		.divTabla{
			float: left;
			max-height: 300px;
			overflow-y:hidden;
			overflow-x:hidden;
		}

 	</style>
	
	<div ng-controller="adquisicionController as adquisicionc" class="maincontainer_treeview all_page" id="title">
	    <script type="text/ng-template" id="pago.jsp">
    		<%@ include file="/app/components/adquisicion/pago.jsp"%>
  	    </script>
  	    
  	    <br/>
		<div class="row">
			<div class="page-header">
				<h2 ng-if="adquisicionc.esnuevo"><small>Nueva adquisición</small></h2>
				<h2 ng-if="!adquisicionc.esnuevo"><small>Edición de adquisición</small></h2>
			</div>
			<div class="col-sm-12" align="right">
				<div class="btn-group">
					<label class="btn btn-default btn-sm" ng-click="adquisicionc.agregarPagos()" uib-tooltip="Pagos" tooltip-placement="left">
					<span class="glyphicon glyphicon-piggy-bank"></span></label>
					<label class="btn btn-default btn-sm" ng-click="adquisicionc.borrar()" uib-tooltip="Borrar" tooltip-placement="left">
					<span class="glyphicon glyphicon-trash"></span></label>
				</div>
			</div>
			<div class="col-sm-12">
						<div class="form-group" ng-show="!adquisicionc.esnuevo">
							<label for="id" class="floating-label id_class">ID {{ adquisicionc.adquisicion.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
				            	<div id="categoria" angucomplete-alt placeholder="" pause="100" selected-object="adquisicionc.cambioCategoria"
											  local-data="adquisicionc.categorias" search-fields="nombre" title-field="nombre" field-required="adquisicionc.requerido" field-label="* Categoría"
											  minlength="1" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight" disable-input="adquisicionc.congelado"
											  initial-value="adquisicionc.adquisicion.categoriaNombre" focus-out="adquisicionc.blurCategoria()" input-name="categoria"></div>
						</div>
						<div class="form-group">
				            	<div id="tipo" angucomplete-alt placeholder="" pause="100" selected-object="adquisicionc.cambioTipo"
											  local-data="adquisicionc.tipos" search-fields="nombre" title-field="nombre" field-required="adquisicionc.requerido" field-label="* Tipo"
											  minlength="1" input-class="form-control form-control-small field-angucomplete" match-class="angucomplete-highlight" disable-input="adquisicionc.congelado"
											  initial-value="adquisicionc.adquisicion.tipoNombre" focus-out="adquisicionc.blurTipo()" input-name="tipo"></div>
						</div>
						<div class="row">
							<div class="col-sm-3">
								<div class="form-group">
										<input type="text" class="inputText" ng-model="adquisicionc.adquisicion.medidaNombre" 
						            	ng-required="adquisicionc.requerido" ng-readonly="adquisicionc.congelado"
						            	ng-value="adquisicionc.adquisicion.medidaNombre" onblur="this.setAttribute('value', this.value);" ng-change="adquisicionc.actualizaObligatorios()"/>
						            	<label class="floating-label">* Medida</label>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
										<input type="text" class="inputText input-money" ng-model="adquisicionc.adquisicion.cantidad" ng-required="adquisicionc.requerido" ui-number-mask="0"  ng-readonly="adquisicionc.congelado"
										ng-value="adquisicionc.adquisicion.cantidad" onblur="this.setAttribute('value', this.value);" ng-change="adquisicionc.actualizaMontos('cantidad')" />
										<label class="floating-label" >* Cantidad</label>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
										<input type="text" class="inputText input-money" ng-model="adquisicionc.adquisicion.precioUnitario" ui-number-mask="2"  ng-readonly="adquisicionc.congelado"
										ng-value="adquisicionc.adquisicion.precioUnitario" onblur="this.setAttribute('value', this.value); " ng-change="adquisicionc.actualizaMontos('precio')"/>
										<label class="floating-label" >Precio (Q)</label>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
										<input type="text" class="inputText input-money" ng-model="adquisicionc.adquisicion.total" ng-required="adquisicionc.requerido" ui-number-mask="2"  ng-readonly="adquisicionc.congelado"
										ng-value="adquisicionc.adquisicion.total" onblur="this.setAttribute('value', this.value);" ng-change="adquisicionc.actualizaMontos('total')"/>
										<label class="floating-label" >* Total (Q)</label>
								</div>
							</div>
						</div>
						<br/>
						<div class="row">
    							<div class="col-sm-3">
    								<input type="radio" ng-model="adquisicionc.adquisicion.tipoRevision"  ng-disabled="adquisicionc.congelado"
    								value="1" />
									<label class="label-form">Revisión ex-ante</label>
    							</div>
    							<div class="col-sm-3">
    								<input type="radio" ng-model="adquisicionc.adquisicion.tipoRevision" ng-disabled="adquisicionc.congelado"
    								value="2" />
									<label class="label-form">Revisión ex-post</label>
    							</div>
    						</div>
						<br/>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group">
									<input type="number" class="inputText" ng-model="adquisicionc.adquisicion.nog" style="text-align: right;"  ng-readonly="adquisicionc.congelado"
									ng-value="adquisicionc.adquisicion.nog" onblur="this.setAttribute('value', this.value);" ng-blur="adquisicionc.getInfoNog();"/>
										<label class="floating-label" >NOG (Número de Orden Guatecompra)</label>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<input type="text" class="inputText" ng-model="adquisicionc.adquisicion.numeroContrato" style="text-align: left;"  ng-readonly="adquisicionc.congelado"
									ng-value="adquisicionc.adquisicion.numeroContrato" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"
									ng-disabled="adquisicionc.adquisicion.montoContrato != null"/>
										<label class="floating-label" >Número de contrato</label>
								</div>
							</div>
							<div class="col-sm-3">
								<div class="form-group">
									<input type="text" class="inputText input-money" ng-model="adquisicionc.adquisicion.montoContrato" ui-number-mask="2"  ng-readonly="adquisicionc.congelado"
									ng-value="adquisicionc.adquisicion.montoContrato" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"
									ng-disabled="adquisicionc.adquisicion.montoContrato != null"/>
										<label class="floating-label" >Monto del contrato (Q)</label>
								</div>
							</div>
						</div>
							<div class="row">
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.preparacionDocumentosPlanificada" is-open="adquisicionc.popup_fechas[0]"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.preparacionDocumentosPlanificada, 1, 1);"
											ng-value="adquisicionc.adquisicion.preparacionDocumentoPlanificada" onblur="this.setAttribute('value', this.value);"  ng-readonly="adquisicionc.congelado"/>
											<span class="label-icon" ng-click="adquisicionc.congelado?'':adquisicionc.abrirPopupFecha(0)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Preparación de documentos (Planificada)</label>
									</div>
    							</div>
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.preparacionDocumentosReal" is-open="adquisicionc.popup_fechas[1]"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
											ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.preparacionDocumentosReal, 1, 2);"
											ng-value="adquisicionc.adquisicion.preparacionDocumentosReal" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"/>
											<span class="label-icon" ng-click="adquisicionc.abrirPopupFecha(1)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Preparación de documentos (Real)</label>
									</div>
    							</div>
    						</div>
							<div class="row">
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.lanzamientoEventoPlanificada" is-open="adquisicionc.popup_fechas[2]" ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.lanzamientoEventoPlanificada, 2, 1);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  
											ng-readonly="adquisicionc.congelado" ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.lanzamientoEventoPlanificada, 2, 1);"
											ng-value="adquisicionc.adquisicion.lanzamientoEventoPlanificada" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="adquisicionc.congelado?'':adquisicionc.abrirPopupFecha(2)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Lanzamiento de evento (Planificada)</label>
									</div>
    							</div>
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.lanzamientoEventoReal" is-open="adquisicionc.popup_fechas[3]"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" 
											clear-text="Borrar" ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.lanzamientoEventoReal, 2, 2);"
											ng-value="adquisicionc.adquisicion.lanzamientoEventoReal" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"/>
											<span class="label-icon" ng-click="adquisicionc.abrirPopupFecha(3)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Lanzamiento de evento (Real)</label>
									</div>
    							</div>
    						</div>
							<div class="row">
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.recepcionOfertasPlanificada" is-open="adquisicionc.popup_fechas[4]"
										 	ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.recepcionOfertasPlanificada, 3, 1);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-readonly="adquisicionc.congelado"
											ng-value="adquisicionc.adquisicion.recepcionOfertasPlanificada" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="adquisicionc.congelado?'':adquisicionc.abrirPopupFecha(4)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Recepción de ofertas (Planificada)</label>
									</div>
    							</div>
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.recepcionOfertasReal" is-open="adquisicionc.popup_fechas[5]"
										 	ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.recepcionOfertasReal, 3, 2);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
											ng-value="adquisicionc.adquisicion.recepcionOfertasReal" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"/>
											<span class="label-icon" ng-click="adquisicionc.abrirPopupFecha(5)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Recepción de ofertas (Real)</label>
									</div>
    							</div>
    						</div>
							<div class="row">
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.adjudicacionPlanificada" is-open="adquisicionc.popup_fechas[6]"
										 	ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.adjudicacionPlanificada, 4, 1);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-readonly="adquisicionc.congelado"
											ng-value="adquisicionc.adquisicion.adjudicacionPlanificada" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="adquisicionc.congelado?'':adquisicionc.abrirPopupFecha(6)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Adjudicación (Planificada)</label>
									</div>
    							</div>
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.adjudicacionReal" is-open="adquisicionc.popup_fechas[7]"
										 	ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.adjudicacionReal, 4, 2);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
											ng-value="adquisicionc.adquisicion.adjudicacionReal" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"/>
											<span class="label-icon" ng-click="adquisicionc.abrirPopupFecha(7)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Adjudicación (Real)</label>
									</div>
    							</div>
    						</div>
							<div class="row">
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.firmaContratoPlanificada" is-open="adquisicionc.popup_fechas[8]"
										 	ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.firmaContratoPlanificada, 5, 1);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-readonly="adquisicionc.congelado"
											ng-value="adquisicionc.adquisicion.firmaContratoPlanificada" onblur="this.setAttribute('value', this.value);"/>
											<span class="label-icon" ng-click="adquisicionc.congelado?'':adquisicionc.abrirPopupFecha(8)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Firma contrato (Planificada)</label>
									</div>
    							</div>
    							<div class="col-sm-6">
    								<div class="form-group">
										<input type="text" class="inputText" uib-datepicker-popup="{{adquisicionc.formatofecha}}" alt-input-formats="{{adquisicionc.altformatofecha}}"
										 	ng-model="adquisicionc.adquisicion.firmaContratoReal" is-open="adquisicionc.popup_fechas[9]"
										 	ng-blur="adquisicionc.validarFechas(adquisicionc.adquisicion.firmaContratoReal, 5, 2);"
											datepicker-options="adquisicionc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
											ng-value="adquisicionc.adquisicion.firmaContratoReal" onblur="this.setAttribute('value', this.value);" ng-disabled="adquisicionc.inhabilitarFechas"/>
											<span class="label-icon" ng-click="adquisicionc.abrirPopupFecha(9)" tabindex="-1">
												<i class="glyphicon glyphicon-calendar"></i>
											</span>
											<label class="floating-label">Firma contrato (Real)</label>
									</div>
    							</div>
    						</div>
    						<br/>
    						<div class="row" ng-if="adquisicionc.listaNog">
    							<div class="col-sm-12" style="text-align: center">
    								<Label class="label-form">Información general del NOG (Número de Orden Guatecompra)</Label>
    							</div>
    							<br/>
    							<div class="divTabla">
	    							<table st-table="adquisicionc.displayedInfoNog" st-safe-src="adquisicionc.infoNogs" class="table table-striped smart-table">
										<thead class="theadDatos">
											<tr>
												<th class="label-form" style="text-align: center; width: 14%;">No. Contrato</th>
												<th class="label-form" style="text-align: center; width: 14%;">Monto</th>
												<th class="label-form" style="text-align: center; width: 14%">Prep. de doctos (Real)</th>
												<th class="label-form" style="text-align: center; width: 14%">Lanzamiento de evento (Real)</th>
												<th class="label-form" style="text-align: center; width: 14%;">Recepción de ofertas (Real)</th>
												<th class="label-form" style="text-align: center; width: 14%;">Adjudicación (Real)</th>
												<th class="label-form" style="text-align: center; width: 14%;">Firma contrato (Real)</th>
											</tr>
										</thead>
										<tbody class="cuerpoTablaDatos">
											<tr st-select-row="row" ng-repeat="row in adquisicionc.displayedInfoNog">
												<td style="text-align: right">{{row.numeroContrato}}</td>
												<td style="text-align: right">{{row.montoContrato | formatoMillones : false}}</td>
												<td style="text-align: center">{{row.preparacionDocumentosReal}}</td>
												<td style="text-align: center">{{row.lanzamientoEventoReal}}</td>
												<td style="text-align: center">{{row.recepcionOfertasReal}}</td>
												<td style="text-align: center">{{row.adjudicacionReal}}</td>
												<td style="text-align: center">{{row.firmaContratoReal}}</td>
											</tr>
										</tbody>
									</table>
								</div>
    						</div>
			</div>
		</div>
	</div>
