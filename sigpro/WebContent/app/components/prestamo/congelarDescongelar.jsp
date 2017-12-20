<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	.divTablacg{
	    width: 100%;
	    max-height: 237px;
	    overflow-y: auto;
	    overflow-x: hidden;
	}
</style>
<div class="modal-header">
    <h3 class="modal-title">Congelar o Descongelar PEPs</h3>
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
	
	<div class="divTablacg">
		<table st-table="modalcc.peps"
			class="table table-striped  table-hover ">
			<thead >
				<tr>
					<th style="width: 60px;">ID</th>
					<th>Nombre</th>
					<th style="width: 30px;">Congelar / Descongelar</th>
					<th style="width: 30px;">Permiso Editar</th>
				</tr>
			</thead>
			<tbody>
				<tr st-select-row="row"
					ng-repeat="row in modalcc.peps">
					<td>{{row.id}}</td>
					<td>{{row.nombre}}</td>
					<td style="text-align: center;">
						<input type="checkbox" ng-model="row.congeladoTemp" ng-change="modalcc.cambioCongelado($index)"/>
					</td>
					<td style="text-align: center;">
						<input type="checkbox" ng-model="row.permisoEditarCongelar" ng-disabled="!row.congeladoTemp"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<br/>
	<div class="row main-form">
		<form name="form" style="margin-top: 10px;">
			<div class="col-sm-12" ng-if="false">
				<div class="form-group">
					<input type="checkbox"  ng-model="modalcc.crearLineaBase" ng-change = "modalcc.cambioCheck()"> 
 					<label class="floating-label">Crear nueva linea base</label>   						
				</div>
				
				<div class="form-group" ng-if="modalcc.crearLineaBase && false" >
				      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="modalcc.nombre"
				       ng-value="modalcc.nombre" 
				      onblur="this.setAttribute('value', this.value);" ng-required="true" >
				      <label class="floating-label">* Nombre</label>
				</div>
			</div>
		</form>
	</div>
	<br />
	<div class="row">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid  ? modalcc.ok() : '' "
				 ng-disabled = "!form.$valid">
					&nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label> 
					<label class="btn btn-primary" ng-click="modalcc.cancel()">Cancelar</label>
			</div>

		</div>
	</div>
</div>