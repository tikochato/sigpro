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
	    			<div ng-hide="!modalh.mostrarCargando" style="width: 100%; margin-left: 10px; z-index: 1">
	    				<div class="grid_loading" ng-hide="!modalh.mostrarCargando">
							<div class="msg">
								<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
									<br />
									<br /> <b>Cargando, por favor espere...</b> 
								</span>
							</div>
						</div>
					</div>
					<div style="overflow: auto; min-height: 300px; max-height: 300px; z-index: -1">
						<table class="table" ng-hide="modalh.mostrarCargando">
						 	<tr>
						 		<th rowspan="2" class="label-form" style="vertical-align: middle;">COMPONENTES</th>
						 		<th colspan="3"  class="label-form" ng-repeat= "organismo in modalh.m_organismosEjecutores"
						 			style="text-align: center; vertical-align: middle;">
						 			<div>{{ organismo.nombre }}</div>
						 		</th>
						 		<th rowspan="2" class="label-form" style="text-align: center; vertical-align: middle;">Total Asignado</th>
						 		<th rowspan="2" class="label-form" style="text-align: center; vertical-align: middle;">TECHO</th>
						 	</tr>
						 	<tr>
						 		<th colspan="3" ng-repeat= "organismo in modalh.m_organismosEjecutores">
						 			<table style="width: 100%">
						 				<tr>
						 					<td class="label-form" style="min-width: 100px; text-align: center;">Prestamo</td>
						 					<td class="label-form" style="min-width: 100px; text-align: center;">Donación</td>
						 					<td class="label-form" style="min-width: 100px;text-align: center;">Nacional</td>
						 				</tr>
						 			</table>
						 		</th>
						 	</tr>
								 	
						 	<tr ng-repeat="row in modalh.m_componentes track by $index " class="filaIngreso" ng-class="row.isSelected ? 'st-selected' : ''">
						 		<td style="min-width: 200px;" class="label-form"> {{ row.nombre }} </td>
						 		<td  colspan="3" ng-repeat = "ue in row.unidadesEjecutoras track by $index">
						 			<table style="width: 100%;">
						 				<tr>
						 					<td style="text-align: center;">
						 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
													class="inputText input-money"
													ng-model="ue.prestamo"
													ng-value="ue.prestamo"
													onblur="this.setAttribute('value', this.value);"
													ui-number-mask="2"
													ng-readonly="true"
												/>
						 					</td>
						 					<td style="text-align: center;">
						 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
													class="inputText input-money"
													ng-model="ue.donacion"
													ng-value="ue.donacion"
													onblur="this.setAttribute('value', this.value);"
													ui-number-mask="2"
													ng-readonly="true"
												/>
						 					</td>
						 					<td style="text-align: center; border-right: 1px solid #ddd;">
						 						<input  inputText="text" style="width: 100px; margin-right: 5px;"
													class="inputText input-money"
													ng-model="ue.nacional"
													ng-value="ue.nacional"
													onblur="this.setAttribute('value', this.value);"
													ui-number-mask="2"
													ng-readonly="true"
												/>
						 					</td>
						 				<tr>
						 			</table>
						 		</td>
						 		<td style="min-width: 100px; text-align: right;"
						 			ng-class="row.totalIngesado==row.techo ? 'totalCorrecto':'totalError'"
						 		 	class="label-form"> {{ row.totalIngesado | formatoMillonesSinTipo : modalh.enMillones }} 
						 		 </td>
						 		<td style="width: 155px; text-align: right;"
						 			ng-class="row.totalIngesado==row.techo ? 'totalCorrecto':'totalError'"
						 		 	class="label-form"> {{ row.techo | formatoMillonesSinTipo : modalh.enMillones }} 
						 		 </td>
						 		 
						 	</tr>
						 	<tr style="border-top: 3px double #ddd;" ng-show="false">
						 		<td style="min-width: 200px;" class="label-form">
						 			Total Asignado
						 		</td>
						 		<td  colspan="3" ng-repeat = "organismo in modalh.m_organismosEjecutores">
						 			<table style="width: 100%;">
						 				<tr>
						 					<td style="text-align: center;">
						 						{{ organismo.totalAsignadoPrestamo | formatoMillonesSinTipo : modalh.enMillones }}
						 					</td>
						 					<td style="text-align: center;">
						 						{{ organismo.totalAsignadoDonacion | formatoMillonesSinTipo : modalh.enMillones }}
						 					</td>
						 					<td style="text-align: center; border-right: 1px single #ddd;">
						 						{{ organismo.totalAsignadoNacional | formatoMillonesSinTipo : modalh.enMillones }}
						 					</td>
						 				<tr>
						 			</table>
						 		</td>
						 	</tr>
						</table>
					</div>
	        		<div align="right" style="font-size: 12px; z-index: -1">
	        			<b>Total de versiones: {{modalh.posicion + 1}} de {{modalh.totalFechas}}</b>
	        		</div> 
	    		</div>
	    		<div class="row" align="center" style="z-index: -1" ng-hide="modalh.totalFechas == 0">
	    			<label class="btn btn-default" ng-click="modalh.disabledInicio != true ? modalh.inicio() : ''" uib-tooltip="Primero" tooltip-placement="bottom" ng-disabled="modalh.mostrarCargando || modalh.disabledInicio">
					<span class="glyphicon glyphicon-fast-backward"></span></label>
	    			<label class="btn btn-default" ng-click="modalh.disabledInicio != true ? modalh.atras() : ''" uib-tooltip="Atrás" tooltip-placement="bottom" ng-disabled="modalh.mostrarCargando || modalh.disabledInicio">
					<span class="glyphicon glyphicon-backward"></span></label>
					<label class="btn btn-default" ng-click="modalh.disabledFin != true ? modalh.siguiente() : ''" uib-tooltip="Siguiente" tooltip-placement="bottom" ng-disabled="modalh.mostrarCargando || modalh.disabledFin">
					<span class="glyphicon glyphicon-forward"></span></label>
	    			<label class="btn btn-default" ng-click="modalh.disabledFin != true ? modalh.ultimo() : ''" uib-tooltip="Último" tooltip-placement="bottom" ng-disabled="modalh.mostrarCargando || modalh.disabledFin">
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