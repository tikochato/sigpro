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
  	<div class="col-sm-12 page-header" style="font-weight: bold;">Avances de Meta</div>
  	<div class="col-sm-12" style="font-weight: bold;">"{{modalAvances.nombreMeta}}"</div>
  </div>
  <div>
  	<div class="operation_buttons" align="right">
		<br/>
		<div class="btn-group btn-group-sm">
	       <shiro:hasPermission name="17040">
	       		<label class="btn btn-default" ng-click="modalAvances.nuevoAvance()" uib-tooltip="Nuevo Avance">
				<span class="glyphicon glyphicon-plus"></span></label>
	       </shiro:hasPermission> 
	   	</div>				
	</div>
  </div>
	<div class="row" style="height:calc(90vh - 250px); overflow-y:scroll;">
		<div class="col-sm-12">
			<table st-table="modalAvances.avanceCollection" st-safe-src="modalAvances.avance" class="table">
				<thead>
				<tr>
					<th st-sort="fecha">Fecha</th>
					<th st-sort="valor">Valor</th>
					<th st-sort="usuario">Usuario Creación</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<tr class="filaIngreso" ng-repeat="row in modalAvances.avanceCollection">
					<td>
						<div class="form-group">
							<input type="text" class="inputText" uib-datepicker-popup="{{modalAvances.formatofecha}}" ng-model="row.fechaControl" is-open="row.isOpen"
								datepicker-options="modalAvances.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="modalAvances.abrirPopupFecha($index,0)"
								ng-change="modalAvances.guardarFecha(row)" ng-readonly="true"/>
							<span class="label-icon" ng-click="modalAvances.abrirPopupFecha($index,0)">
								<i class="glyphicon glyphicon-calendar"></i>
							</span>
						</div>
					</td>
					<td>
						<div ng-switch="modalAvances.datoTipo">
							<div ng-switch-when="texto" >
								<input type="text" class="inputText" ng-model="row.valorString" style="width: 100%; text-align: left"></input>
							</div>
							<div ng-switch-when="entero" class="form-group" >
								<input type="text" class="inputText" ng-model="row.valorEntero" style="width: 100%; text-align: rigth" ui-number-mask="0"></input>
							</div>
							<div ng-switch-when="decimal" class="form-group" >
								<input type="text" class="inputText" ng-model="row.valorDecimal" style="width: 100%; text-align: rigth" ui-number-mask="2"></input>
							</div>
							<div ng-switch-when="booleano" class="form-group" >
								<input type="checkbox" ng-model="row.valorString" />
							</div>
							<div ng-switch-when="fecha" class="form-group" >
								<input type="text" class="inputText" uib-datepicker-popup="{{modalAvances.formatofecha}}" ng-model="row.valorTiempoControl" is-open="row.isOpenValor"
									datepicker-options="modalAvances.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="modalAvances.abrirPopupFecha($index,1)"
									ng-change="modalAvances.guardarFecha(row)" ng-readonly="true"/>
									<span class="label-icon" ng-click="modalAvances.abrirPopupFecha($index,1)">
										<i class="glyphicon glyphicon-calendar"></i>
									</span>
							</div>
						</div>
					</td>
					<td style="vertical-align: middle;">{{row.usuario}}</td>
					<shiro:hasPermission name="17030">
						<td>
				       		<label class="btn btn-default btn-xs" ng-click="modalAvances.borrarAvance(row)" uib-tooltip="Borrar">
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
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="modalAvances.ok()">&nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
	    	</div>
	      
	    </div>
  </div>
</div>
