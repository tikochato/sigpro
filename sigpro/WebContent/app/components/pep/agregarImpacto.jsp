<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
    <h3 class="modal-title">Impacto</h3>
</div>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-12">
	    <form name="formImpacto">
	    	<div class="form-group" >
	            	<input type="text" class="inputText" ng-model="modalc.impacto.entidadNombre" ng-value="modalc.impacto.entidadNombre" 
	            		ng-click="modalc.buscarEntidad()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
	            	<span class="label-icon" ng-click="modalc.buscarEntidad()"><i class="glyphicon glyphicon-search"></i></span>
	          	<label  class="floating-label">* Organización</label>
			</div>
			
			<div class="form-group">
				<input type="text"  class="inputText" ng-model="modalc.impacto.impacto" ng-value="modalc.impacto.impacto" onblur="this.setAttribute('value', this.value);" ng-required="true" >
				<label  class="floating-label">* Impacto y participación de la organización</label>
			</div>
			
			
	    </form>
    </div>
    </div>
    <br/>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click=" formImpacto.$valid ? modalc.ok() : ''" ng-disabled="!formImpacto.$valid"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="modalc.cancel()">Cancelar</label>
	    	</div>
	    </div>
  </div>
</div>