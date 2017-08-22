<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>

.leyendaTexto {
		    text-align: right;
		}
		
		.leyendaTexto li {
		    display: inline-block;
		    position: relative;
		    padding: 1px 8px 1px 15px;
		    font-size: smaller;
		}
		
		.leyendaTexto li span {
		    position: absolute;
		    left: 0;
		    width: 12px;
		    height: 12px;
		    border-radius: 4px;
		}

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
						<th></th>
						<th>Fecha Inicio</th>
						<th>Fecha Fin</th>
					</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in estructura.estructuraProyecto">
						<td>
							<div style="height: 25px;">
								<div><span ng-class="estructura.claseIcon(row);" style="margin-left: {{row.objetoTipo-1}}em" uib-tooltip="{{controller.tooltipObjetoTipo[row.objetoTipo-1]}}" >
								</span>{{row.nombre}}
								</div>
							</div>
						</td>
						<td style="width: {{estructura.tamanoSemaforo}}px"><span ng-style="estructura.obtenerColor(row);" class="glyphicon glyphicon-certificate" ng-if="row.objetoTipo == 5"></span></td>
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
  
  
  <div class="row">
			<div class="col-sm-3"></div>
			
			<div class="col-sm-8">
	    		<ol class="leyendaTexto" >
					<li ><span style="background-color: #fd9496;"></span>Atrasadas</li>
			        <li ><span style="background-color: #e2e291;"></span>En alerta</li>
			        <li ><span style="background-color: #c7e7a5;"></span>A cumplir</li>
			        <li ><span style="background-color: #b0cfe8;"></span>Completadas</li>
				</ol>
			</div>
   	</div>
  
    <br/>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
				<label class="btn btn-primary" ng-click="estructura.cancel()">Cancelar</label>
	    	</div>
	      
	    </div>
  </div>
</div>