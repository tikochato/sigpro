<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal-body" id="modal-body" style="height: 90vh;">
<div class="row">
  	<div class="col-sm-12" style="font-weight: bold;">Avances de Meta</div>
  	<div class="col-sm-12" style="font-weight: bold;">"{{modalAvances.nombreMeta}}"</div>
  </div>
  <br/>
  <div>
  	<div class="operation_buttons" align="right">
		<br/>
		<div class="btn-group btn-group-sm">
	       <shiro:hasPermission name="17040">
	       		<label class="btn btn-default" ng-click="modalAvances.nuevoAvance()" uib-tooltip="Nuevo">
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
				<tr ng-repeat="row in modalAvances.avanceCollection">
					<td>
						<div class="form-group">
							<input type="text" class="inputText" uib-datepicker-popup="{{modalAvances.formatofecha}}" ng-model="row.fechaControl" is-open="row.isOpen"
								datepicker-options="modalAvances.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="modalAvances.abrirPopupFecha($index)"
								ng-change="modalAvances.guardarFecha(row)" ng-readonly="true"/>
							<span class="label-icon" ng-click="modalAvances.abrirPopupFecha($index)">
								<i class="glyphicon glyphicon-calendar"></i>
							</span>
						</div>
					</td>
					<td><input type="text" class="inputText" ng-model="row.valor" style="width: 100%; text-align: right"></td>
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
		        <label class="btn btn-success" ng-click="modalAvances.ok()">Cerrar</label>
	    	</div>
	      
	    </div>
  </div>
</div>
