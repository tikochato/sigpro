<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Plan de pagos para {{controller.nombre}}</h3>
</div>
<style>
	.moneda{
		
	}
</style>
<div class="modal-body" id="modal-body">
	<div class="row">
		<div class="col-sm-12">
			<form name="form">
				<div class="form-group">
					<label for="id" class="floating-label">ID {{controller.idObjeto}}</label>
					<br/><br/>
				</div>
				<div class="row">
					
				</div>
				<div class = "row">
					<div class="col-sm-6">
						<div class="form-group">
						   	<input type="text" class="inputText" id="inombre" ng-model="controller.numeroContrato" ng-value="controller.numeroContrato"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true">
						  	<label class="floating-label">* Numero de contrato</label>
						</div>
					</div>
					
					<div class="col-sm-6">
						<div class="form-group">
						   	<input type="text" class="inputText" id="inombre" ng-model="controller.montoContrato" ng-value="controller.montoContrato"   
						     onblur="this.setAttribute('value', this.value);" ng-required="true" ui-number-mask="2">
						  	<label class="floating-label">* Monto del contrato</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group" >
						  <input type="text"  class="inputText" uib-datepicker-popup="{{controller.formatofecha}}" ng-model="controller.fechaPago" is-open="controller.fi_abierto"
						            datepicker-options="controller.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="controller.cambioDuracion()" 
						            ng-click="controller.abrirPopupFecha(1000)" ng-value="controller.fechaInicio" onblur="this.setAttribute('value', this.value);"
						            ng-readonly="!controller.primeraActividad"/>
						            <span class="label-icon" ng-click="controller.abrirPopupFecha(1000)">
						              <i class="glyphicon glyphicon-calendar"></i>
						            </span>
						  <label for="campo.id" class="floating-label">Fecha de pago</label>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
						   	<input type="text" class="inputText" id="inombre" ng-model="controller.montoPago" ng-value="controller.montoPago"   
						     onblur="this.setAttribute('value', this.value);" ui-number-mask="2">
						  	<label class="floating-label">Pago</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
						   	<input type="text" class="inputText" id="inombre" ng-model="controller.descripcion" ng-value="controller.descripcion"   
						     onblur="this.setAttribute('value', this.value);">
						  	<label class="floating-label">Descripción</label>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href
								ng-click="controller.agregarPago()" role="button"
								uib-tooltip="Agregar pago" tooltip-placement="left">
								<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br>
				<div class="row">
					<table
						st-table="controller.planAdquisicionesPagos"
						class="table table-striped table-bordered table-hover table-propiedades">
						<thead >
							<tr>
								<th style="display: none;">ID</th>
								<th style="width: 20%">Fecha</th>
								<th style="width: 20%">Pago</th>
								<th style="width: 40%">Descripcion</th>
								<th style="width: 20%;">Quitar</th>
	
							</tr>
						</thead>
						<tbody>
							<tr st-select-row="row"
								ng-repeat="row in controller.planAdquisicionesPagos">
								<td style="display: none;">{{row.id}}</td>
								<td>{{row.fecha}}</td>
								<td>{{row.pago  | formatoMillones : controller.enMillones}}</td>
								<td>{{row.descripcion}}</td>
								<td>
									<button type="button"
										ng-click="controller.eliminarPago(row)"
										class="btn btn-sm btn-danger">
										<i class="glyphicon glyphicon-minus-sign"> </i>
									</button>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="grid_loading" ng-hide="!controller.mostrarcargando" style="position: absolute; z-index: 1">
		  				<div class="msg">
		      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  				<br /><br />
				  				<b>Cargando, por favor espere...</b>
			  				</span>
						</div>
					</div>
				</div>
			</form>
			<br/>
			<div class="row">
			    <div class="col-sm-12 operation_buttons" align="right">
				    <div class="btn-group">
				        <label class="btn btn-success" ng-click="controller.ok()" 
				        ng-disabled="!form.$valid" uib-tooltip="Guardar"> Guardar</label>
						<label class="btn btn-primary" ng-click="controller.cancel()">Cancelar</label>
			    	</div>
			      
			    </div>
	  		</div>
		</div>
	</div>
</div>