<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
.panel-body{
	overflow-y: scroll;
	overflow-x: hidden;
	height: 300px; 
	padding: 3px;
}
</style>

<div class="modal-header">
     <h3 class="modal-title">{{controller.nombre}}</h3>
</div>
<div class="modal-body" id="modal-body">
	<div class="row">
		<div class="col-sm-12">
			<div class="row">
				<div class="grid_loading" ng-hide="!controller.mostrarcargando">
				  				<div class="msg">
				      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  				<br /><br />
						  				<b>Cargando, por favor espere...</b>
					  				</span>
								</div>
							</div>
				<div style="padding: 1px;">
					<div class="panel panel-default">
						<div class="panel panel-body">
							<table st-table="controller.displayedItems" st-safe-src="controller.items" class="table table-striped table-bordered table-hover">
								<thead >
									<tr>
										<th style="display: none;">id</th>
										<th style="width: 35%">Nombre</th>
										<th style="text-align: center; width: 12%">Fecha Inicial</th>
										<th style="text-align: center; width: 12%">Fecha Final</th>
										<th style="text-align: center; width: 12%;" st-sort="avance">Avance</th>
										<th style="text-align: center; width: 12%;">Estado</th>
										<th style="text-align: center; width: 17%;">Responsable</th>
									</tr>
								</thead>
								<tbody>
									<tr st-select-row="row"
										ng-repeat="row in controller.displayedItems">
										<td style="display: none;">{{row.id}}</td>
										<td>{{row.nombre}}</td>
										<td style="text-align: center">{{row.fechaInicial}}</td>
										<td style="text-align: center">{{row.fechaFinal}}</td>
										<td style="text-align: right">{{row.avance}}%</td>
										<td style="text-align: center">{{row.estado}}</td>
										<td style="text-align: center">{{row.responsable}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<br/>
			<div class="row">
			    <div class="col-sm-12 operation_buttons" align="right">
				    <div class="btn-group">
						<label class="btn btn-primary" ng-click="controller.cancel()">Cerrar</label>
			    	</div>
			    </div>
	  		</div>
		</div>
	</div>
</div>