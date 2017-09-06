<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Subproducto</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
		<form name="form">
			<div class="form-group">
				<label for="id" class="floating-label">ID {{subproductoc.subproducto.id}}</label>
				<br/><br/> 
			</div>
		
		
			<div class="form-group">
				<input type="text" class="inputText" ng-model="subproductoc.subproducto.nombre" ng-required="true" 
				ng-value="subproductoc.subproducto.nombre" onblur="this.setAttribute('value', this.value);"/>
				<label class="floating-label">* Nombre</label>
			</div>
			
			<div class="form-group" >
	            <input type="text" class="inputText" ng-model="subproductoc.tipoNombre" ng-readonly="true" ng-required="true" 
	            	ng-value="subproductoc.tipoNombre" onblur="this.setAttribute('value', this.value);" 
	            	ng-click="subproductoc.buscarTipo()"/>
	            <span class="label-icon" ng-click="subproductoc.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
	            <label class="floating-label">* Tipo</label>
	        </div>
	        
	        <div class="form-group">
	            <input type="text" class="inputText" ng-model="subproductoc.unidadEjecutoraNombre" ng-readonly="true" ng-required="true" 
	            	ng-value="subproductoc.unidadEjecutoraNombre" onblur="this.setAttribute('value', this.value);" 
	            	ng-click="subproductoc.buscarUnidadEjecutora()"/>
	            <span class="label-icon" ng-click="subproductoc.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
	          <label class="floating-label">* Unidad Ejecutora</label>
	        </div>
				
			
			
		</form>
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? subproductoc.ok() : ''" ng-disabled="!form.$valid"> Guardar</label>
					<label class="btn btn-primary" ng-click="subproductoc.cancel()">Cancelar</label>
					<label class="btn btn-danger" ng-click="subproductoc.borrar()" ng-disabled="subproductoc.esnuevo">Borrar</label>
					productoc
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>