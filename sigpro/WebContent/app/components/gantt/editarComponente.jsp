<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Componente</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
		<form name="form">
				
			<div class="form-group">
				<label for="id" class="floating-label">ID {{ componentec.componente.id }}</label>
				<br/><br/>
			</div>
				
			<div class="form-group">
			   <input type="text" class="inputText" 
			     ng-model="componentec.componente.nombre" ng-value="componentec.componente.nombre"   
			     onblur="this.setAttribute('value', this.value);" ng-required="true" >
			   <label class="floating-label">* Nombre</label>
			</div>
			
			<div class="form-group">
	            <input type="text" class="inputText"  ng-model="componentec.unidadejecutoranombre" ng-readonly="true" ng-required="true" 
	            	ng-click="componentec.buscarUnidadEjecutora()" ng-value="componentec.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
	            <span class="label-icon" ng-click="componentec.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
	          	<label for="campo3" class="floating-label">* Unidad Ejecutora</label>
			</div>
			<div class="form-group">
	            <input type="text" class="inputText"  ng-model="componentec.componentetiponombre" ng-readonly="true" ng-required="true" 
	            	ng-click="componentec.buscarComponenteTipo()" ng-value="componentec.componentetiponombre" onblur="this.setAttribute('value', this.value);"/>
	            <span class="label-icon" ng-click="componentec.buscarComponenteTipo()"><i class="glyphicon glyphicon-search"></i></span>
	          	<label for="campo3" class="floating-label">* Tipo de Componente</label>
			</div>
			
		</form>
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? componentec.ok() : ''" ng-disabled="!form.$valid"> Guardar</label>
					<label class="btn btn-primary" ng-click="componentec.cancel()">Cancelar</label>
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>