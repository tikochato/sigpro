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
	        
	        <div class = "row">
				<div class="col-sm-6">
					<div class="form-group">
						<select class="inputText" ng-model="subproductoc.duracionDimension"
							ng-options="dim as dim.nombre for dim in subproductoc.dimensiones track by dim.value"
							 ng-required="true">
						</select>
						<label for="nombre" class="floating-label">* Dimension</label>
					</div>
				</div>
				
				<div class="col-sm-6">
					<div class="form-group">
					   <input class="inputText"  type="number"
					     ng-model="subproductoc.duracion" ng-value="subproductoc.duracion"   
					     onblur="this.setAttribute('value', this.value);"  min="1" max="500" ng-required="true" 
					     ng-readonly="subproductoc.duracionDimension.value != 0 ? false : true"
					     ng-change="subproductoc.fechaInicio != null && subproductoc.duracionDimension.value != 0 ? subproductoc.cambioDuracion(subproductoc.duracionDimension) : ''">
					   <label class="floating-label">* Duración</label>
					</div>	
				</div>
				
				<div class="col-sm-6">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{subproductoc.formatofecha}}" alt-input-formats="{{subproductoc.altformatofecha}}"
							  	ng-model="subproductoc.fechaInicio" is-open="subproductoc.fi_abierto"
					            datepicker-options="subproductoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="subproductoc.cambioDuracion(subproductoc.duracionDimension);" ng-required="true"  
					            ng-click="subproductoc.abrirPopupFecha(1000)" ng-value="subproductoc.fechaInicio" onblur="this.setAttribute('value', this.value);">
					            <span class="label-icon" ng-click="subproductoc.abrirPopupFecha(1000)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
					  <label for="campo.id" class="floating-label">* Fecha de Inicio</label>
					</div>
				</div>
			
				<div class="col-sm-6">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{subproductoc.formatofecha}}" alt-input-formats="{{subproductoc.altformatofecha}}"
							  	ng-model="subproductoc.fechaFin" is-open="subproductoc.ff_abierto"
					            datepicker-options="subproductoc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true" ng-click=""
					            ng-value="subproductoc.fechaFin" onblur="this.setAttribute('value', this.value);"
					            ng-readonly="true"/>
					            <span class="label-icon" ng-click="subproductoc.abrirPopupFecha(1001)">
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
			        <label class="btn btn-success" ng-click="form.$valid ? subproductoc.ok() : ''" ng-disabled="!form.$valid"> Guardar</label>
					<label class="btn btn-primary" ng-click="subproductoc.cancel()">Cancelar</label>
					<label class="btn btn-danger" ng-click="subproductoc.borrar()" ng-disabled="subproductoc.esnuevo">Borrar</label>
					
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>