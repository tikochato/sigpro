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
    overflow-x:auto;
    height: 300px;
    
}

.etiqueta {
    
    
    
    
    color: rgba(0,0,0,0.38);
    font-weight: bold;
    font-size: 11px;
}

</style>
<div class="modal-header">
            	<h3 class="modal-title" id="modal-title">Actividades</h3>
        	</div>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-12">
    <label class="etiqueta">Responsables</label>
    <div class = "row" style="padding: 0px 0px 10px 10px; width: 100%">
    	<div ng-dropdown-multiselect="" options="estructura.responsables" translation-texts="estructura.buttonText" 
    	selected-model="estructura.model" extra-settings="estructura.settings"
    	events="estructura.selectColaborador" style="width: 100%"> 
    	</div>
    </div>
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
						<th>Fecha Inicio Real</th>
						<th>Fecha Fin Real</th>
					</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in estructura.estructuraProyecto">
						<td>
							<div style="height: 25px;">
								<div><span ng-class="estructura.claseIcon(row);" uib-tooltip="{{controller.tooltipObjetoTipo[row.objetoTipo-1]}}" >
								</span>{{row.nombre}}
								</div>
							</div>
						</td>
						<td style="width: {{estructura.tamanoSemaforo}}px"><span ng-style="estructura.obtenerColor(row);" class="glyphicon glyphicon-certificate" ng-if="row.objetoTipo == 5"></span></td>
						<td>{{row.fechaInicio}}</td>
						<td>{{row.fechaFin}}</td>
						<td style="text-align: center;">{{row.fechaInicioReal}}</td>
						<td style="text-align: center;">{{row.fechaFinReal}}</td>
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
		    	<label class="btn btn-default" ng-click="estructura.exportarExcel()" uib-tooltip="Exportar a Excel">
		    		<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span>
		    	</label>
				<label class="btn btn-primary" ng-click="estructura.cancel()">Cerrar</label>
				
	    	</div>
	      
	    </div>
  </div>
</div>