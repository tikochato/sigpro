<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
		.filaIngreso  > td{
			padding: 0px !important;
			padding-left: 8px !important;
			padding-right: 8px !important;
			vertical-align: initial !important; 
		}
	</style>
<div class="modal-body" id="modal-body" style="height: 90vh;">
<div class="row">
  	<div class="col-sm-12 page-header" style="font-weight: bold;">Pagos Planificado</div>
  </div>
  <div>
  	<div class="col-sm-5">
		<div class="form-group">
			<input type="text" class="inputText input-money" ng-model="pagoc.techo" ui-number-mask="2" 
			ng-value="pagoc.techo" onblur="this.setAttribute('value', this.value);" ng-readonly="true"/>
				<label class="floating-label" >Monto del contrato (Q)</label>
		</div>
	</div>
  	<div class="operation_buttons" align="right">
	<br/>
		<div class="btn-group btn-group-sm">	       
      		<label class="btn btn-default" uib-tooltip="Nuevo Pago" ng-click="pagoc.congelado?'':pagoc.nuevoPago()" ng-disabled="pagoc.congelado" tooltip-placement="left">
			<span class="glyphicon glyphicon-plus"></span></label>
	   	</div>				
	</div>
  </div>
	<div class="row" style="height:calc(90vh - 250px); overflow-y:scroll;">
		<div class="col-sm-12">
		<form name="formPagos"> 
			<table st-table="pagoc.pagosCollection" st-safe-src="pagoc.pagos" class="table">
				<thead>
				<tr>
					<th st-sort="fecha">Fecha</th>
					<th st-sort="valor">Monto (Q)</th>
					<th width="1%"></th>
				</tr>
				</thead>
				<tbody>
				<tr class="filaIngreso" ng-repeat="row in pagoc.pagosCollection">
					<td>
						<div class="form-group">
							<input type="text" class="inputText" uib-datepicker-popup="{{pagoc.formatofecha}}" alt-input-formats="{{pagoc.altformatofecha}}"
										 	ng-model="row.fechaPago" is-open="row.isOpen"
								datepicker-options="pagoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
								ng-change="pagoc.guardarFecha(row)" ng-readonly="pagoc.congelado" ng-required="true"/>
							<span class="label-icon" ng-click="pagoc.congelado?'':pagoc.abrirPopupFecha($index,0)" tabindex="-1">
								<i class="glyphicon glyphicon-calendar"></i>
							</span>
						</div>
					</td>
					<td>
						<div class="form-group" >
							<input type="text" class="inputText input-money" ng-model="row.pago" 
							ng-readonly="pagoc.congelado" style="width: 100%; text-align: rigth" 
							ui-number-mask="2" ng-required="true"></input>
						</div>
					</td>
					
						<td>
				       		<label class="btn btn-default btn-xs" uib-tooltip="Borrar" ng-click="pagoc.congelado?'':pagoc.borrarPago(row)" ng-disabled="pagoc.congelado" tooltip-placement="left">
							<span class="glyphicon glyphicon-trash"></span></label>
						</td>
			       
				</tr>
				</tbody>
			</table>
			</form>
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-sm-6">
			Total: {{ pagoc.totalPagos | formatoMillones : false }}
		</div>
	    <div class="col-sm-6 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success"  ng-click="formPagos.$valid ?  pagoc.ok() : ''" 
		        ng-disabled="!formPagos.$valid">Cerrar</label>
	    	</div>
	      
	    </div>
  </div>
</div>
