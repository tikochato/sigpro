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
  	<div class="col-sm-12 page-header" style="font-weight: bold;">Pagos de la Adquisición</div>
  </div>
  <div>
  	<div class="operation_buttons" align="right">
		<br/>
		<div class="btn-group btn-group-sm">
	       <shiro:hasPermission name="17040">
	       		<label class="btn btn-default" ng-click="modalPagos.nuevoPago()" uib-tooltip="Nuevo Pago" tooltip-placement="left">
				<span class="glyphicon glyphicon-plus"></span></label>
	       </shiro:hasPermission> 
	   	</div>				
	</div>
  </div>
	<div class="row" style="height:calc(90vh - 250px); overflow-y:scroll;">
		<div class="col-sm-12">
			<table st-table="modalPagos.pagosCollection" st-safe-src="modalPagos.pagos" class="table">
				<thead>
				<tr>
					<th st-sort="fecha">Fecha</th>
					<th st-sort="valor">Monto</th>
					<th width="1%"></th>
				</tr>
				</thead>
				<tbody>
				<tr class="filaIngreso" ng-repeat="row in modalPagos.pagosCollection">
					<td>
						<div class="form-group">
							<input type="text" class="inputText" uib-datepicker-popup="{{modalPagos.formatofecha}}" ng-model="row.fechaPago" is-open="row.isOpen"
								datepicker-options="modalPagos.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
								ng-change="modalPagos.guardarFecha(row)"/>
							<span class="label-icon" ng-click="modalPagos.abrirPopupFecha($index,0)">
								<i class="glyphicon glyphicon-calendar"></i>
							</span>
						</div>
					</td>
					<td>
						<div class="form-group" >
							<input type="text" class="inputText input-money" ng-model="row.pago" style="width: 100%; text-align: rigth" ui-number-mask="2"></input>
						</div>
					</td>
					<shiro:hasPermission name="17030">
						<td>
				       		<label class="btn btn-default btn-xs" ng-click="modalPagos.borrarPago(row)" uib-tooltip="Borrar" tooltip-placement="left">
							<span class="glyphicon glyphicon-trash"></span></label>
						</td>
			       </shiro:hasPermission>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-sm-6">
			Total: {{ modalPagos.totalPagos | currency:" ":2 }}
		</div>
	    <div class="col-sm-6 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="modalPagos.ok()">Cerrar</label>
	    	</div>
	      
	    </div>
  </div>
</div>
