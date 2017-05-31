<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
     <h3 class="modal-title">Actividad</h3>
 </div>
<div class="modal-body" id="modal-body">
	<div class="row">
	<div class="col-sm-12">
		<form name="form">
			<uib-tabset active="active">
				<uib-tab index="0" heading="Requeridos">
				
					<div class="form-group">
						<label for="id" class="floating-label">ID {{actividadc.actividad.id }}</label>
						<br/><br/>
					</div>
					<div class="form-group">
		 						<div class="form-group">
						   <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="actividadc.actividad.nombre" ng-value="actividadc.actividad.nombre"  onblur="this.setAttribute('value', this.value);" ng-required="true" >
						   <label class="floating-label">* Nombre</label>
						</div>
					</div>
					<div class="form-group" >
					    <input type="text" class="inputText"  ng-model="actividadc.actividad.actividadtiponombre" ng-value="actividadc.actividad.actividadtiponombre" 
						ng-click="actividadc.buscarTipo()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
						<span class="label-icon" ng-click="actividadc.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
						<label for="campo3" class="floating-label">* Tipo de Actividad</label>
					</div>
					
					<div class = "row">
						<div class="col-sm-6">
							<div class="form-group">
								<select class="inputText" ng-model="actividadc.duracionDimension"
									ng-options="dim as dim.nombre for dim in actividadc.dimensiones track by dim.id"
									 ng-required="true">
									<option value="">--Seleccione una opción--</option>
								</select>
								<label for="nombre" class="floating-label">* Dimension</label>
							</div>
						</div>
						<div class="col-sm-6">
						<div class="form-group">
						   <input class="inputText"  type="number"
						     ng-model="actividadc.actividad.duracion" ng-value="actividadc.actividad.duracion"   
						     onblur="this.setAttribute('value', this.value);"  min="1" max="100" ng-required="true" 
						     ng-change="actividadc.cambioDuracion()">
						   <label class="floating-label">*Duración</label>
						</div>	
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="actividadc.actividad.fechaInicio" is-open="actividadc.fi_abierto"
							            datepicker-options="actividadc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="actividadc.cambioDuracion()" ng-required="true"  
							            ng-click="actividadc.abrirPopupFecha(1000)" ng-value="actividadc.actividad.fechaInicio" onblur="this.setAttribute('value', this.value);"
							            ng-readonly="!actividadc.primeraActividad"/>
							            <span class="label-icon" ng-click="actividadc.abrirPopupFecha(1000)">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">*Fecha de Inicio</label>
							</div>
						</div>
						
						<div class="col-sm-6">
						
							<div class="form-group" >
							  <input type="text"  class="inputText" uib-datepicker-popup="{{actividadc.formatofecha}}" ng-model="actividadc.actividad.fechaFin" is-open="actividadc.ff_abierto"
							            datepicker-options="actividadc.ff_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true"  ng-click="actividadc.abrirPopupFecha(-1)"
							            ng-value="actividadc.actividad.fechaFin" onblur="this.setAttribute('value', this.value);"
							            ng-readonly="true"/>
							            <span class="label-icon" ng-click="actividadc.abrirPopupFecha(-1)">
							              <i class="glyphicon glyphicon-calendar"></i>
							            </span>
							  <label for="campo.id" class="floating-label">* Fecha de Fin</label>
							</div>
						</div>
					</div>
					<div class="form-group">
					   <input type="number" name="iavance"  class="inputText" id="inombre" 
					     ng-model="actividadc.actividad.porcentajeavance" ng-value="actividadc.actividad.porcentajeavance"   
					     onblur="this.setAttribute('value', this.value);"  min="0" max="100" ng-required="true" >
					   <label class="floating-label">* Avance %</label>
					</div>	
				</uib-tab>
				<uib-tab index="1" heading="Opcionales">
					<div class="form-group">
   						<input type="text" class="inputText" id="descripcion" ng-model="actividadc.actividad.descripcion"  ng-value="proyectopropiedadc.proyectopropiedad.descripcion" onblur="this.setAttribute('value', this.value);" >
						<label for="descripcion" class="floating-label">Descripción</label>
					</div>		
				</uib-tab>
			</uib-tabset>
			
		</form>
		<br/>
		<div class="row">
		    <div class="col-sm-12 operation_buttons" align="right">
			    <div class="btn-group">
			        <label class="btn btn-success" ng-click="form.$valid ? actividadc.ok() : ''" 
			        ng-disabled="!form.$valid" title="Guardar" uib-tooltip="Guardar"> Guardar</label>
					<label class="btn btn-primary" ng-click="actividadc.cancel()">Cancelar</label>
		    	</div>
		      
		    </div>
  		</div>
	</div>
	</div>
</div>