<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style type="text/css">

.myGrid {
	width: 100%;
	height: 600px;
}
</style>

	<div ng-controller="formaejemploController" class="maincontainer all_page" id="title">
		<h3>Formulario de Ejemplo</h3><br/>
		<div class="row" align="center" ng-hide="isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-primary" ng-click="newRow()">Nuevo</label>
			        <label class="btn btn-primary" ng-click="editRow()">Editar</label>
			        <label class="btn btn-primary" ng-click="deleteRow()">Borrar</label>
    			</div>
    		</div>
    		<div class="col-sm-12" align="center">
				<div id="grid1" ui-grid="gridOptions" ui-grid-selection ui-grid-pagination class="myGrid"></div>
			</div>
		</div>
		<div class="row" ng-show="isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="save()">Guardar</label>
			        <label class="btn btn-danger" ng-click="cancel()">Cancelar</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form>
						<div class="form-group">
							<label for="campo1">Campo estatico</label>
    						<input type="text" class="form-control" id="campo1" placeholder="campo1">
						</div>
						<div class="form-group">
							<label for="campo2">Otro campo estatico</label>
    						<input type="text" class="form-control" id="campo2" placeholder="campo2">
						</div>
						<div class="form-group" ng-repeat="field in fields">
							<label for="field.id">{{ field.label }}</label>
							<div ng-switch="field.type">
								<input ng-switch-when="string" type="text" id="{{ 'field_'+field.id }}" ng-model="field.value" class="form-control" />
								<input ng-switch-when="entero" type="text" id="{{ 'field_'+field.id }}" numbers-Only ng-model="field.value" class="form-control" />
								<input ng-switch-when="decimal" type="number" id="{{ 'field_'+field.id }}" ng-model="field.value" class="form-control" />
								<input ng-switch-when="boolean" type="checkbox" id="{{ 'field_'+field.id }}" ng-model="field.value" />
								<p ng-switch-when="date" class="input-group">
									<input type="text" id="{{ 'field_'+field.id }}" class="form-control" uib-datepicker-popup ng-model="field.value" is-open="field.isOpen"
														datepicker-options="dateOptions" close-text="Cerrar" /><span
														class="input-group-btn">
														<button type="button" class="btn btn-default"
															ng-click="open($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</button>
													</span>
								</p>
								<select ng-switch-when="select" id="{{ 'field_'+field.id }}" class="form-control" ng-model="x.value">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in field.options"
														value="{{number.value}}">{{number.label}}</option>
								</select>
							</div>
						</div>
				</form>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="save()">Guardar</label>
			        <label class="btn btn-danger" ng-click="cancel()">Cancelar</label>
    			</div>
    		</div>
		</div>

	</div>
