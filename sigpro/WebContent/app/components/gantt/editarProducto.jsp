<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Producto</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
		<form name="form">
			<div class="form-group">
				<label  class="floating-label">ID {{ productoc.producto.id }}</label>
				<br/><br/>
			</div>
			
			<div class="form-group">
				<input type="text" class="inputText" ng-model="productoc.producto.nombre" ng-value="productoc.producto.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" />
				<label class="floating-label">* Nombre</label> 
			</div>
			
			<div class="form-group" >
	          <input type="text" class="inputText" ng-model="productoc.tipoNombre" ng-value="productoc.tipoNombre" 
	          	ng-click="productoc.buscarTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
	           <span class="label-icon" ng-click="productoc.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
	          <label class="floating-label">* Tipo</label>
	        </div>
	        
	        <div class="form-group">
	            <input type="text" class="inputText" ng-model="productoc.unidadEjecutoraNombre" ng-value="productoc.unidadEjecutoraNombre" 
	            	ng-click="productoc.buscarUnidadEjecutora()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
	            <span class="label-icon" ng-click="productoc.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
	          <label  class="floating-label">* Unidad Ejecutora</label>
	        </div>
				
			
			
		</form>
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? productoc.ok() : ''" ng-disabled="!form.$valid"> Guardar</label>
					<label class="btn btn-primary" ng-click="productoc.cancel()">Cancelar</label>
					<label class="btn btn-danger" ng-click="productoc.borrar()" ng-disabled="productoc.esnuevo">Borrar</label>
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>