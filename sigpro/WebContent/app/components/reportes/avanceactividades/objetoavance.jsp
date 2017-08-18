<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Actividades de {{controller.nombre}}</h3>
</div>
<div class="modal-body" id="modal-body">
	<div class="row">
		<div class="col-sm-12">
			<form name="form">
				<div class="row" style="height: 400px;">
					<table st-table="controller.items" class="table table-striped table-bordered table-hover table-propiedades">
						<thead >
							<tr>
								<th style="display: none;">id</th>
								<th style="width: 20%">Nombre</th>
								<th style="width: 20%">Fecha Inicial</th>
								<th style="width: 20%">Fecha Final</th>
								<th style="width: 20%;">Avance</th>
								<th style="width: 20%;">Responsable</th>
							</tr>
						</thead>
						<tbody style="height: 400px;">
							<tr st-select-row="row"
								ng-repeat="row in controller.items">
								<td style="display: none;">{{row.id}}</td>
								<td>{{row.nombre}}</td>
								<td>{{row.fechaInicial}}</td>
								<td>{{row.fechaFinal}}</td>
								<td>{{row.avance}}</td>
								<td>{{row.responsable}}</td>
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
		</div>
	</div>
</div>