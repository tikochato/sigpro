<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="metaController as metac" class="all_page" id="title">
		<script type="text/ng-template" id="metaAvance.jsp">
	    	<%@ include file="/app/components/meta/metaAvance.jsp"%>
		</script>
		<div align="center">
			<div class="operation_buttons" align="right">
				<br/>
				<div class="btn-group btn-group-sm">
			       <shiro:hasPermission name="17040">
			       		<label class="btn btn-default" ng-click="metac.nuevaMeta()" uib-tooltip="Nueva">
						<span class="glyphicon glyphicon-plus"></span></label>
			       </shiro:hasPermission> 
			   	</div>				
    		</div>
    		<shiro:hasPermission name="17010">
    		<div align="center">
    			<div class="grid_loading" ng-hide="!metac.mostrarCargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
							<br />
							<br /> <b>Cargando, por favor espere...</b> 
						</span>
					</div>
				</div>
					
				<table st-table="metac.metasCollection" st-safe-src="metac.metas" class="table">
				<thead>
				<tr>
					<th st-sort="id">ID</th>
					<th st-sort="nombre">Nombre</th>
					<th>Descripción</th>
					<th st-sort="unidadMedidaId">U. Medida</th>
					<th st-sort="datoTipoId">Tipo de Dato</th>
					<th st-sort="metaFinal">Meta Final</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<tr ng-repeat="row in metac.metasCollection" ng-click="metac.metaSeleccionada(row)" ng-class="row.isSelected ? 'st-selected' : ''">
					<td style="vertical-align: middle;">{{row.id | uppercase}}</td>
					<td><input type="text" class="inputText" ng-model="row.nombre" style="width: 100%; text-align: left"></input></td>
					<td><input type="text" class="inputText" ng-model="row.descripcion" style="width: 100%; text-align: left"></input></td>
					<td>
						<select class="inputText" ng-model="row.unidadMedidaId"
							ng-options="unidad as unidad.nombre for unidad in metac.metaunidades track by unidad.id"
							 ng-required="true">
						</select>
					</td>
					<td>
						<select class="inputText" ng-model="row.datoTipoId"
							ng-options="tipo as tipo.nombre for tipo in metac.datoTipos track by tipo.id"
							 ng-required="true">
						</select>
					</td>
					<td><input type="text" class="inputText" ng-model="row.metaFinal" style="width: 100%; text-align: left"></input></td>
					<shiro:hasPermission name="17030">
						<td>
				       		<label class="btn btn-default btn-xs" ng-click="metac.borrarMeta(row)" uib-tooltip="Borrar">
							<span class="glyphicon glyphicon-trash"></span></label>
						</td>
			       </shiro:hasPermission>
				</tr>
				</tbody>
				</table>
			</div>
			<div style="margin-top: 10px;">
				<div class="panel panel-default" >
					<div class="panel-heading label-form" style="text-align: center;">Valores</div>
					<div class="panel-body">
					<div class="form-group col-sm-2" style="text-align: left;">
						<select class="inputText" 
							ng-model="metac.anio" 
							ng-options="opcion for opcion in metac.anios"
							ng-change="metac.getMetasAnio(metac.meta, metac.anio)">
							<option value="">Seleccione un año</option>
						</select>
						<label for="nombre" class="floating-label">* Año</label>
					</div>
					<div align="center">
						<br>
		    			<table class="table table-striped table-hover"  style="height: 100%" ng-show="metac.mostrarValores">
							<thead >
								<tr>
									<th style="text-align: center;" class="label-form"></th>
			         				<th style="text-align: center;" class="label-form">Ene</th>
			         				<th style="text-align: center;" class="label-form">Feb</th>
			         				<th style="text-align: center;" class="label-form">Mar</th>
			         				<th style="text-align: center;" class="label-form">Abr</th>
			         				<th style="text-align: center;" class="label-form">May</th>
			         				<th style="text-align: center;" class="label-form">Jun</th>
			         				<th style="text-align: center;" class="label-form">Jul</th>
			         				<th style="text-align: center;" class="label-form">Ago</th>
			         				<th style="text-align: center;" class="label-form">Sep</th>
			         				<th style="text-align: center;" class="label-form">Oct</th>
			         				<th style="text-align: center;" class="label-form">Nov</th>
									<th style="text-align: center;" class="label-form">Dic</th>
			         				<th style="text-align: center;" class="label-form">Total</th>
			         			</tr>
							</thead>
							<tbody >
								<tr>
						      		<td style="vertical-align: middle;">Planificado</td>
						      		<td><input type="text" class="inputText" ng-model="metac.planificado.enero" ng-change="metac.almacenarPlanificado('enero')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.febrero" ng-change="metac.almacenarPlanificado('febrero')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.marzo" ng-change="metac.almacenarPlanificado('marzo')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.abril" ng-change="metac.almacenarPlanificado('abril')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.mayo" ng-change="metac.almacenarPlanificado('mayo')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.junio" ng-change="metac.almacenarPlanificado('junio')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.julio" ng-change="metac.almacenarPlanificado('julio')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.agosto" ng-change="metac.almacenarPlanificado('agosto')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.septiembre" ng-change="metac.almacenarPlanificado('septiembre')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.octubre" ng-change="metac.almacenarPlanificado('octubre')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.noviembre" ng-change="metac.almacenarPlanificado('noviembre')" style="width: 100%; text-align: right"></input></td>
									<td><input type="text" class="inputText" ng-model="metac.planificado.diciembre" ng-change="metac.almacenarPlanificado('diciembre')" style="width: 100%; text-align: right"></input></td>
									<td style="text-align: right; vertical-align: middle;">{{metac.planificado.total}}</td>
						      	</tr>
						      	<tr ng-click="metac.agregarAvances()">
						      		<td>Real</td>
						      		<td style="text-align: right;">{{metac.real.enero}}</td>
									<td style="text-align: right;">{{metac.real.febrero}}</td>
									<td style="text-align: right;">{{metac.real.marzo}}</td>
									<td style="text-align: right;">{{metac.real.abril}}</td>
									<td style="text-align: right;">{{metac.real.mayo}}</td>
									<td style="text-align: right;">{{metac.real.junio}}</td>
									<td style="text-align: right;">{{metac.real.julio}}</td>
									<td style="text-align: right;">{{metac.real.agosto}}</td>
									<td style="text-align: right;">{{metac.real.septiembre}}</td>
									<td style="text-align: right;">{{metac.real.octubre}}</td>
									<td style="text-align: right;">{{metac.real.noviembre}}</td>
									<td style="text-align: right;">{{metac.real.diciembre}}</td>
									<td style="text-align: right;">{{metac.real.total}}</td>
						      	</tr>
							</tbody>
						</table>
					</div>
					</div>
				</div>
			</div>
			
    		</shiro:hasPermission>
    		
		</div>
	</div>