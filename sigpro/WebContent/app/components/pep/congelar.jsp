<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
		
</style>
<div class="modal-header">
    <h3 class="modal-title">{{ modalcc.tipoLineaBase == 1 ? 'Linea Base' :'Congelar PEP'}}</h3>
</div>
<div class="modal-body" id="modal-body">
	<div class="grid_loading" ng-hide="!modalcc.mostrarcargando" style="position:relative; z-index:4;   ">
			  	<div class="msg" style="height: 120px!important;">
			      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  <br /><br />
					  <b>Cargando, por favor espere...</b>
				  </span>
				</div>
	</div>
	<div class="row main-form">
		<form name="form" style="margin-top: 10px;">
			<div class="col-sm-12" ng-if="modalcc.tipoLineaBase == 1">
			
			   <div class="form-group" >
			   		<div class="btn-group">
				        <label class="btn btn-default" ng-model="modalcc.nuevaLineaBase" uib-btn-radio="1" ng-change="modalcc.selectNuevo(true)">Nueva Linea Base</label>
				        <label class="btn btn-default" ng-model="modalcc.nuevaLineaBase" uib-btn-radio="2" ng-change="modalcc.selectNuevo(false)">Editar Linea Base</label>
				    </div>
			   </div>
			   <div class="form-group" ng-if="modalcc.nuevaLineaBase == 1" >
				      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="modalcc.nombre"
				       ng-value="modalcc.nombre" 
				      onblur="this.setAttribute('value', this.value);" ng-required="true" >
				      <label class="floating-label">* Nombre</label>
				</div>
				<div class="form-group" ng-if="modalcc.nuevaLineaBase == 2">
						<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" 
							  selected-object="modalcc.cambioLineaBase" local-data="modalcc.lineasBase" 
							  search-fields="nombre" title-field="nombre" field-required="true" 
							  field-label="* Linea Base" minlength="1" 
							  input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="modalcc.lineaBaseNombre" 
							  focus-out="modalcc.blurLineaBase()" input-name="lineaBase"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
				
			</div>
			<div class="col-sm-12" ng-if="modalcc.tipoLineaBase == 2">
				<div class="form-group col-sm-6" >
						<div id= "mes" angucomplete-alt placeholder="" pause="100" 
							  selected-object="modalcc.cambioMes" local-data="modalcc.mes" 
							  search-fields="nombre" title-field="nombre" field-required="true" 
							  field-label="* Mes" minlength="1" 
							  input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="modalcc.mesNombre" 
							  focus-out="modalcc.blurMes()" input-name="mes"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
				<div class="form-group col-sm-6" style="padding: 10px 2px;">
					<input type="number"  class="inputText" ng-model="modalcc.anio" maxlength="4" minlength="4"  
						ng-value="modalcc.anio" onblur="this.setAttribute('value', this.value);"	
						ng-required="true"  min="{{ modalcc.minYear }}" max="{{ modalcc.maxYear }}"/>
					  	<label  class="floating-label">*Año</label>
				</div>
			</div>
		</form>
	</div>
	<br />
	<div class="row">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid && !modalcc.bloquearBoton  ? modalcc.ok() : '' "
				 ng-disabled = "!form.$valid || modalcc.bloquearBoton">
					&nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label> 
					<label class="btn btn-primary" ng-click="!modalcc.bloquearBoton ? modalcc.cancel() : ''" 
					ng-disabled = "modalcc.bloquearBoton">Cancelar</label>
			</div>

		</div>
	</div>
</div>