<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.panel-body {
    overflow-y: scroll;
    overflow-x:hidden;
    height: 300px;
}

</style>
<div class="modal-header">
            	<h3 class="modal-title" id="modal-title">Actividades</h3>
        	</div>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-12">
    <div class="row">
    	<div style="padding: 1px;" >
    		<div class="panel panel-default" >
				<div class="panel-body">
				<table st-table="estructura.estructuraProyecto" class="table table-striped">
					<thead>
					<tr>
						<th>Nombre</th>
						<th>Estado</th>
						<th>Fecha Inicio</th>
						<th>Fecha Fin</th>
					</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in estructura.estructuraProyecto">
						<td>
							<div style="height: 25px;">
								<p><span ng-class="estructura.claseIcon(row);" style="margin-left: {{row.objetoTipo-1}}em" uib-tooltip="{{controller.tooltipObjetoTipo[row.objetoTipo-1]}}"></span>{{row.nombre}}</p>
							</div>
						</td>
						<td>{{row.nombreEstado}}</td>
						<td>{{row.fechaInicio}}</td>
						<td>{{row.fechaFin}}</td>
					</tr>
					</tbody>
				</table>
					
				</div>
			</div>
    	</div>
	</div>
    </div>
  </div>
    <br/>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="estructura.ok()"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="estructura.cancel()">Cancelar</label>
	    	</div>
	      
	    </div>
  </div>
</div>