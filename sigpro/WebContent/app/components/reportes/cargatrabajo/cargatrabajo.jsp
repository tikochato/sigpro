<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style type="text/css">
	.cabecera {
		position: absolute;
	    margin-top: -47px;
	    flex-shrink: 0;
	    overflow-x: hidden;
	    width: 96%;
	}
	
	.cabecerath1{
		margin: 0px auto;
    	width: 96px;
    	overflow-x: hidden;
	}
	.divTabla{
	    width: 100%;
	    height: 200px;
	    overflow-y: auto;
	    
	}
			</style>
	<div ng-controller="cargatrabajoController as controller" class="maincontainer all_page" id="title">
		<script type="text/ng-template" id="estructuraproyecto.jsp">
    		<%@ include file="/app/components/reportes/cargatrabajo/estructuraproyecto.jsp"%>
  	    </script>
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Carga de Trabajo</h3></div>
		</div>
	    <br>
	    <div class="row"  align="center">
	    	<div class="col-sm-12">
	    		<div class="row" style="height: 300px; width: 100%;">
				    <div style="height: 50%; width: 50%; float: left">
				    	<div class="row">
							<div class="form-group col-sm-6" >
								<select  class="inputText" ng-model="controller.prestamo"
									ng-options="a.text for a in controller.prestamos" 
									ng-change="controller.getEstructura()"></select>
								<label for="tObjeto" class="floating-label">Préstamos</label>
						   </div>
						</div>
			    	</div>
			    	<br/><br/><br/>
			    	<div style=" width: 100%; ">
				    	<div class="operation_buttons" align="right" ng-if="controller.mostrar">
				    		<div class="btn-group">
										<label class="btn btn-default" ng-model="desembolsosc.enMillones" 
										uib-btn-radio="true"  uib-tooltip="Filtrar actividades" role="button" 
										tabindex="0" aria-invalid="false" ng-click="controller.filtrarEstrucrura()">
										<span class="glyphicon glyphicon-filter" aria-hidden="true"></span></label>
										
							</div>
							
						</div>
						<div class="divTabla">
		    			<table st-table="controller.displayedCollection" st-safe-src="controller.rowCollection" class="table table-striped"
		    			ng-if="controller.mostrar">
							<thead  class="cabecera">
								<tr>
									<th style="display: none;">Id</th>
									<th class="label-form" style="width: 43%">Responsable</th>
									<th class="label-form" style="width: 15%; text-align: center;">Actividades retrasadas</th>
									<th class="label-form" style="width: 15%; text-align: center;">Actividades en alerta</th>
									<th class="label-form" style="width: 15%; text-align: center;">Actividades a cumplir</th>
									<th class="label-form" style="width: 15%; text-align: center;">Actividades completadas</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="row in controller.displayedCollection" >
									<td style="display: none;">{{row.id}}</td>
									<td style="width: 40%">{{row.responsable}}</td>
									<td style="text-align: center; width: 15%;">{{row.actividadesAtrasadas}}</td>
									<td style="text-align: center; width: 15%;">{{row.actividadesAlerta}}</td>
									<td style="text-align: center; width: 15%;">{{row.actividadesACumplir}}</td>
									<td style="text-align: center; width: 15%;">{{row.actividadesCompletadas}}</td>
								</tr>
								<tr>
									<td style="display: none;">{{controller.idTotal}}</td>
									<td style="font-weight: bold">{{controller.responsableTotal}}</td>
									<td style="text-align: center; font-weight: bold">{{controller.actividadesAtrasadasTotal}}</td>
									<td style="text-align: center; font-weight: bold">{{controller.actividadesAlertaTotal}}</td>
									<td style="text-align: center; font-weight: bold">{{controller.actividadesACumplirTotal}}</td>
									<td style="text-align: center; font-weight: bold">{{controller.actividadesCompletadas}}</td>
								</tr>
							</tbody>
						</table>
						</div>
			    	</div>
		    	</div>
			    <br>
		    	<br>
		    	<div  style="width: 60%" ng-if="controller.mostrar">
		    		<canvas id="pie" class="chart chart-pie" 
				  	chart-data="controller.data" chart-labels="controller.labels" chart-options="controller.optionsPie"
				  	chart-colors = "controller.pieColors">
					</canvas>	
		    	</div>
	    </div>
	    
	</div>    
</div>