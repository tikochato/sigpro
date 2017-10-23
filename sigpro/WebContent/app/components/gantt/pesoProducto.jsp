<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Peso</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
	
	<div ui-grid="pesoc.gridOptions" 
		ui-grid-grouping ui-grid-edit ui-grid-row-edit ui-grid-resize-columns 
		ui-grid-selection ui-grid-cellNav ui-grid-pinning
		class="grid">
		<div class="grid_loading" ng-hide="!pesoc.mostrarcargando">
		  	<div class="msg">
		      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
				  <br /><br />
				  <b>Cargando, por favor espere...</b>
			  </span>
			</div>
		  </div>
	</div>
		
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="pesoc.pesoTotal == 100 ? pesoc.ok() : ''" 
			        ng-disabled="pesoc.pesoTotal != 100" uib-tooltip="Guardar"> Guardar</label>
					<label class="btn btn-primary" ng-click="pesoc.cancel()">Cancelar</label>
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>