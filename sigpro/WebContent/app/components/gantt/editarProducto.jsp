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
	        
	        <div class = "row">
				<div class="col-sm-6">
					<div class="form-group">
						<select class="inputText" ng-model="productoc.duracionDimension"
							ng-options="dim as dim.nombre for dim in productoc.dimensiones track by dim.value"
							 ng-required="true">
						</select>
						<label for="nombre" class="floating-label">* Dimension</label>
					</div>
				</div>
				
				<div class="col-sm-6">
					<div class="form-group">
					   <input class="inputText"  type="number"
					     ng-model="productoc.duracion" ng-value="productoc.duracion"   
					     onblur="this.setAttribute('value', this.value);"  min="1" max="500" ng-required="true" 
					     ng-readonly="productoc.duracionDimension.value != 0 ? false : true"
					     ng-change="productoc.fechaInicio != null && productoc.duracionDimension.value != 0 ? productoc.cambioDuracion(productoc.duracionDimension) : ''">
					   <label class="floating-label">* Duración</label>
					</div>	
				</div>
				
				<div class="col-sm-6">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{productoc.formatofecha}}" alt-input-formats="{{productoc.altformatofecha}}"
							  	ng-model="productoc.fechaInicio" is-open="productoc.fi_abierto"
					            datepicker-options="productoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="productoc.cambioDuracion(productoc.duracionDimension);" ng-required="true"  
					            ng-click="productoc.abrirPopupFecha(1000)" ng-value="productoc.fechaInicio" onblur="this.setAttribute('value', this.value);">
					            <span class="label-icon" ng-click="productoc.abrirPopupFecha(1000)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
					  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
					</div>
				</div>
			
				<div class="col-sm-6">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{productoc.formatofecha}}" alt-input-formats="{{productoc.altformatofecha}}"
							  	ng-model="productoc.fechaFin" is-open="productoc.ff_abierto"
					            datepicker-options="productoc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true" ng-click=""
					            ng-value="productoc.fechaFin" onblur="this.setAttribute('value', this.value);"
					            ng-readonly="true"/>
					            <span class="label-icon" ng-click="productoc.abrirPopupFecha(1001)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
					  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
					</div>
				</div>
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