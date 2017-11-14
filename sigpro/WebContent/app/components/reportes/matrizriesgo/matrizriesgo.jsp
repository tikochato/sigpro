<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="matrizriesgoController as matrizriesgoc" class="maincontainer all_page" id="title">

  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
	  		<div class="panel-heading"><h3>Matriz de Riesgos</h3></div>
		</div>
		
		
		<div class="row">
			<div class="form-group col-sm-6">
				<select  class="inputText" ng-model="matrizriesgoc.proyectoId"
					ng-options="a.text for a in matrizriesgoc.prestamos"
					ng-change="matrizriesgoc.cargarMatriz()"></select>
			</div>
		</div>
			
		
		<div class="row" align="center" >
			<br>
			<div class="col-sm-12 " ng-hide="!matrizriesgoc.mostrarTabla">
			<div class="operation_buttons" align="right">
					<div class="btn-group">		
						<label class="btn btn-default" ng-click="matrizriesgoc.exportarExcel()" uib-tooltip="Exportar" >
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
					</div>
			</div>
			</div>
			<div class="col-sm-12 " ng-hide="!matrizriesgoc.mostrarTabla">
				<table st-table="matrizriesgoc.riesgos" st-safe-src="matrizriesgoc.lista" class="table table-striped"
				ng-hide="!matrizriesgoc.mostrarTabla" >
					<thead>
						<tr>
							<th class="label-form">Id</th>
							<th st-sort="nombre" class="label-form">Riesgo</th>
							<th class="label-form">Categoría</th>
							<th class="label-form">Nivel</th>
							<th class="label-form">Impacto</th>
							<th class="label-form">Probabilidad</th>
							<th class="label-form">Calificación</th>
							<th class="label-form">Impacto en Tiempo</th>
							<th class="label-form">Contingencia en Tiempo</th>
							<th class="label-form">Impacto en Monto (Q)</th>
							<th class="label-form">Contingencia en Monto (Q)</th>
							<th class="label-form">Evento Iniciador</th>
							<th class="label-form">Consecuencias</th>
							<th class="label-form">Riesgos Secundarios</th>
							<th class="label-form">Solución</th>
							<th class="label-form">Responsable</th>
							<th class="label-form">¿Ha sido ejecutado?</th>
							<th class="label-form">Fecha de Ejecución</th>
							<th class="label-form">Resultado</th>
							<th class="label-form">Observaciones</th>
						</tr>
						<tr>
							<th colspan="20"><input st-search="" class="form-control" placeholder="Buscar" type="text"/></th>
						</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in matrizriesgoc.riesgos">

						<td>{{row.idRiesgo}}</td>
						<td>{{row.nombre}}</td>
						<td>{{row.tipoNombre}}</td>
						<td>{{row.objetoTipoNombre}}</td>
						<td>{{row.impacto}}</td>
						<td>{{row.probabilidad}}</td>
						<td>{{(row.impacto * row.probabilidad).toFixed(2)}} </td>
						<td>{{row.impactoTiempo}}</td>
						<td>{{(row.impacto * row.probabilidad * row.impactoTiempo).toFixed(2)}}</td>
						<td>{{row.impactoMonto}}</td>
						<td>{{(row.impacto * row.probabilidad * row.impactoMonto).toFixed(2)}}</td>
						<td>{{row.gatillo}}</td>
						<td>{{row.consecuencias}}</td>
						<td>{{row.riesgosSecundarios}}</td>
						<td>{{row.solucion}}</td>
						<td>{{row.colaboradorNombre}}</td>
						<td>{{ row.ejecutado == 1 ? 'Si' : 'No' }}</td>
						<td>{{row.fechaEjecucion}}</td>
						<td>{{row.resultado}}</td>
						<td>{{row.observaciones}}</td>
					</tr>
					</tbody>
				</table>
				<br/>
			</div>

	</div>
</div>