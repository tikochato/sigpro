<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Préstamo</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
		<form name="form">
				
					<div class="form-group">
						<label for="id" class="floating-label">ID {{ prestamoc.proyecto.id }}</label>
						<br/><br/>
					</div>
					<div class="form-group">
				      <input type="text" name="inombre"  class="inputText" id="inombre" 
				      ng-model="prestamoc.proyecto.nombre" ng-value="prestamoc.proyecto.nombre" 
				      onblur="this.setAttribute('value', this.value);" ng-required="true" >
				      <label class="floating-label">* Nombre</label>
					</div>	
					<div class="form-group" >
		            	<input type="text" class="inputText"  ng-model="prestamoc.proyectotiponombre" ng-value="prestamoc.proyectotiponombre" 
		            		ng-click="prestamoc.buscarProyectoTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
		            	<span class="label-icon" ng-click="prestamoc.buscarProyectoTipo()"><i class="glyphicon glyphicon-search"></i></span>
		          		<label  class="floating-label">* Categoría Préstamo</label>
		          	</div>
		          	<div class="form-group">
			            <input type="text" class="inputText"  ng-model="prestamoc.unidadejecutoranombre" ng-readonly="true" ng-required="true" 
			            	ng-click="prestamoc.buscarUnidadEjecutora()" ng-value="prestamoc.unidadejecutoranombre" onblur="this.setAttribute('value', this.value);"/>
			            <span class="label-icon" ng-click="prestamoc.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          	<label  class="floating-label">* Unidad Ejecutora</label>
					</div>
					<div class="form-group" >
		            	<input type="text" class="inputText"  ng-model="prestamoc.cooperantenombre" ng-readonly="true" ng-required="true" 
		            		ng-click="prestamoc.buscarCooperante()" ng-value="prestamoc.cooperantenombre" onblur="this.setAttribute('value', this.value);"/>
		            	<span class="label-icon" ng-click="prestamoc.buscarCooperante()"><i class="glyphicon glyphicon-search"></i></span>
		          		<label  class="floating-label">* Cooperante</label>
					</div>
								
				
			
		</form>
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="prestamoc.ok()"> Guardar</label>
					<label class="btn btn-primary" ng-click="prestamoc.cancel()">Cancelar</label>
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>