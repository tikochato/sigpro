<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="administracionTransaccionalController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row">
	    	<div class="col-sm-12">
	    		<div style="width: 100%; height: 15%">
	    			<div class="row">
	    				<div class="panel panel-default">
		  					<div class="panel-heading"><h3>Adminsitración Transaccional</h3></div>
						</div>
					</div>
	    			<br>
	    			<div class="row">
			    		<div class="form-group col-sm-6">
							<select  class="inputText" ng-model="controller.prestamo"
								ng-options="a.text for a in controller.prestamos" ng-change="controller.generar()"></select>
							<label for="prestamo" class="floating-label">Préstamos</label>
						</div>
	    			</div>
	    		</div>
	    		<br>
	    		<div style="width: 100%; height: 85%">
	    			<div class="row">
	    				<div class="form-group col-sm-12">
	    					<table st-table="controller.displayedCollectionObjetos" st-safe-src="controller.rowCollectionObjetos" class="table table-striped">
								<thead>
									<tr>
										<th style="display: none;">Id</th>
										<th class="label-form" style="width: 30%; text-align: center;">Nombre</th>
										<th class="label-form" style="width: 30%; text-align: center;">Tipo Objeto</th>
										<th class="label-form" style="width: 17%; text-align: center;">Usuario creo</th>
										<th class="label-form" style="width: 17%; text-align: center;">Fecha creación</th>
									</tr>
								</thead>
								<tbody>
									<tr ng-repeat="row in controller.displayedCollectionObjetos">
										<td style="display: none;">{{row.id}}</td>
										<td style="text-align: left">{{row.nombre}}</td>
										<td style="text-align: center">{{row.tipoObjeto}}</td>
										<td style="text-align: center">{{row.usuario}}</td>
										<td style="text-align: center">{{row.fecha}}</td>
									</tr>
								</tbody>
							</table>
	    				</div>
	    			</div>
	    			<div class="row">
	    				<div class="form-group col-sm-6">
	    					<canvas id="line" class="chart chart-line" chart-data="data" chart-labels="labels" chart-series="series" chart-options="options">
	    					</canvas>
	    				</div>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	</div>