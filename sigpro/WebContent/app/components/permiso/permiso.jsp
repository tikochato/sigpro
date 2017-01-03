<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<style type="text/css">

.myGrid {
	width: 100%;
	height: 600px;
}
</style>

	<div ng-controller="permisoController as permisosc" class="maincontainer all_page" id="title">
		<h3>Permisos</h3><br/>
		<div class="row" align="center" ng-hide="permisosc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="crearCooperante">
						<label class="btn btn-primary" ng-click="permisosc.nuevoPermiso()">Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="editarCooperante">
						<label class="btn btn-primary" ng-click="permisosc.editarPermiso()">Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="eliminarCooperante">
						<label class="btn btn-primary" ng-click="permisosc.borrarPermiso()">Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="verCooperante">
    			<div class="col-sm-12" align="center">
				<div id="grid1" ui-grid="permisosc.gridOptions" ui-grid-selection ui-grid-pagination class="permisosc.myGrid"></div>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<shiro:hasPermission name="verCooperante">
		<div class="row" ng-show="permisosc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="permisosc.guardarPermiso()">Guardar</label>
			        <label class="btn btn-danger" ng-click="permisosc.cancel()">Cancelar</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form>
						<div class="form-group">
							<label for="nombre">Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="permisosc.permisoSelected.nombre">
						</div>
						<div class="form-group">
							<label for="Descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="permisosc.permisoSelected.descripcion">
						</div>
						<div class="form-group" ng-repeat="field in permisosc.fields">
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
			        <label class="btn btn-success" ng-click="permisosc.guardarPermiso()">Guardar</label>
			        <label class="btn btn-danger" ng-click="permisosc.cancel()">Cancelar</label>
    			</div>
    		</div>
		</div>
		</shiro:hasPermission>
		

	</div>
