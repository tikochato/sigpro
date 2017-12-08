<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
    <h3 class="modal-title">Historia de {{modalh.objetoNombre}}</h3>
</div>
<style>
	.table-striped>tbody>tr:nth-child(odd)>td, {
   		background-color: #cdcdcd;
	}
	.atributo{
		font-weight: bold;
	}
</style>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-12">
	    <form name="formImpacto">
	    	<div class="form-group" >
	    		<div class="row">
	    			<div ng-hide="!modalh.mostrarCargando" style="width: 100%; margin-left: 10px">
	    				<div class="grid_loading" ng-hide="!modalh.mostrarCargando">
							<div class="msg">
								<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
									<br />
									<br /> <b>Cargando, por favor espere...</b> 
								</span>
							</div>
						</div>
					</div>
					<uib-tabset active="modalh.active">
						<uib-tab ng-click="modalh.generalClick();" index="0" heading="General" >
							<div style="overflow: auto; min-height: 300px; max-height: 300px;">						
								<table st-table="modalh.displayedItems" st-safe-src="modalh.data" class="table table-striped table-bordered table-hover">
					        		<tbody>
					        			<tr st-select-row="row" ng-repeat="row in modalh.displayedItems">
					        				<td style="width: 50%" class="atributo">
					        					{{row.nombre}}
					        				</td style="width: 50%">
					        				<td>
					        					{{row.valor}}
					        				</td>	        				
					        			</tr>
					        		</tbody>
			        			</table>
							</div>
						</uib-tab>
						<uib-tab ng-click="modalh.adquisicionesClick();" index="1" heading="Adquisiciones" ng-hide="!modalh.adquisiciones">
							<div style="overflow: auto; min-height: 300px; max-height: 300px;">
								<table st-table="modalh.displayedItems" st-safe-src="modalh.data" class="table table-striped table-bordered table-hover">
					        		<tbody>
					        			<tr st-select-row="row" ng-repeat="row in modalh.displayedItems">
					        				<td style="width: 50%" class="atributo">
					        					{{row.nombre}}
					        				</td style="width: 50%">
					        				<td>
					        					{{row.valor}}
					        				</td>	        				
					        			</tr>
					        		</tbody>
			        			</table>
							</div>
        				</uib-tab>
        				<uib-tab ng-click="modalh.riesgosClick();" index="2" heading="Riesgos" ng-hide="!modalh.riesgos">
        					<div style="overflow: auto; min-height: 300px; max-height: 300px;">
        						<table st-table="modalh.displayedItems" st-safe-src="modalh.data" class="table table-striped table-bordered table-hover">
        							<thead>
        								<tr>
        									<th style="text-align: center;" st-select-row="row" ng-repeat="row in modalh.cabeceras">{{row}}</th>
        								</tr>
        							</thead>
					        		<tbody>
					        			<tr st-select-row="row" ng-repeat="row in modalh.displayedItems">
					        				<td ng-repeat="item in row">
					        					{{item.valor}}
					        				</td>	        				
					        			</tr>
					        		</tbody>
			        			</table>
        					</div>
        				</uib-tab>
        				<uib-tab ng-click="modalh.desembolsosClick();" index="3" heading="Desembolsos" ng-hide="!modalh.desembolsos">
        					<div style="overflow: auto; min-height: 300px; max-height: 300px;">
        					</div>
        				</uib-tab>
        				<uib-tab ng-click="modalh.metasClick();" index="4" heading="Metas" ng-hide="!modalh.metas">
        					<div style="overflow: auto; min-height: 300px; max-height: 300px;">
        					</div>
        				</uib-tab>
        			</uib-tabset>
	        		<div align="right" style="font-size: 12px; z-index: -1">
	        			<b>Total de versiones: {{modalh.totalVersiones != 0 ? modalh.posicion + 1 : 0}} de {{modalh.totalVersiones}}</b>
	        		</div> 
	    		</div>
	    		<div class="row" align="center" ng-hide="modalh.mostrarCargando || modalh.totalVersiones == 0">
	    			<label class="btn btn-default" ng-click="modalh.disabledInicio != true ? modalh.inicio() : ''" uib-tooltip="Primero" tooltip-placement="bottom" ng-disabled="modalh.disabledInicio">
					<span class="glyphicon glyphicon-fast-backward"></span></label>
	    			<label class="btn btn-default" ng-click="modalh.disabledInicio != true ? modalh.atras() : ''" uib-tooltip="Atrás" tooltip-placement="bottom" ng-disabled="modalh.disabledInicio">
					<span class="glyphicon glyphicon-backward"></span></label>
					<label class="btn btn-default" ng-click="modalh.disabledFin != true ? modalh.siguiente() : ''" uib-tooltip="Siguiente" tooltip-placement="bottom" ng-disabled="modalh.disabledFin">
					<span class="glyphicon glyphicon-forward"></span></label>
	    			<label class="btn btn-default" ng-click="modalh.disabledFin != true ? modalh.ultimo() : ''" uib-tooltip="Último" tooltip-placement="bottom" ng-disabled="modalh.disabledFin">
					<span class="glyphicon glyphicon-fast-forward"></span></label>
	    		</div>	        	
			</div>
	    </form>
    </div>
    </div>
    <br/>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">		        
				<label class="btn btn-primary" ng-click="modalh.cerrar()">Cerrar</label>
	    	</div>
	    </div>
  </div>
</div>