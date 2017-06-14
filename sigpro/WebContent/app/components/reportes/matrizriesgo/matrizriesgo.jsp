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
		<div class="subtitulo">
			{{ matrizriesgoc.objetoTipoNombre }} {{ matrizriesgoc.proyectoNombre }}
		</div>
			
		
		<div class="row" align="center" >
			<br>
			<div class="operation_buttons" align="right">
					<div class="btn-group">
						<label class="btn btn-primary" ng-click="matrizriesgoc.exportarExcel()" uib-tooltip="Exportar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span> Exportar</label>
					</div>
			</div>
			<div class="col-sm-12 ">
				<table st-table="matrizriesgoc.riesgos" st-safe-src="matrizriesgoc.lista" class="table table-condensed table-hover" >
					<thead>
						<tr>
							<th class="label-form">Riesgo</th>
							<th class="label-form">Nivel</th>
							<th st-sort="nombre" class="label-form">Descripción</th>
							<th class="label-form">Categoría</th>
							<th class="label-form">Impacto proyectado</th>
							<th class="label-form">Impacto</th>
							<th class="label-form">Puntuación de impacto</th>
							<th class="label-form">Probabilidad</th>
							<th class="label-form">Punteo de prioridad</th>
							<th class="label-form">Gatillos / Sintomas</th>
							<th class="label-form">Respuesta</th>
							<th class="label-form">Responsable</th>
							<th class="label-form">Riesgos secundarios</th>
							<th class="label-form">¿Ha sido ejecutado?</th>
							<th class="label-form">Fecha de Ejecución</th>

						</tr>
						<tr>
							<th colspan="15"><input st-search="" class="form-control" placeholder="Buscar" type="text"/></th>
						</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in matrizriesgoc.riesgos">

						<td>{{row.idRiesgo}}</td>
						<td>{{row.objetoTipoNombre}}</td>
						<td>{{row.nombre}}</td>
						<td>{{row.tipoNombre}}</td>
						<td>{{row.impactoProyectado}}</td>
						<td>{{row.impacto}}</td>
						<td>{{row.puntuacionImpacto}}</td>
						<td>
						<ANY ng-switch="row.probabilidad" >
							<ANY ng-switch-when="1">
								Bajo
							</ANY>
							<ANY ng-switch-when="2">
								Medio
							</ANY>
							<ANY ng-switch-when="3">
								Alto
							</ANY>
						</ANY>
						</td>
						<td>{{ row.punteoPrioridad }} </td>
						<td>{{row.gatillosSintomas}}</td>
						<td>{{row.respuesta}}</td>
						<td>{{row.colaboradorNombre}}</td>
						<td>{{row.riesgosSecundarios}}</td>
						<td>{{ row.ejecutado == 1 ? 'Si' : 'No' }}</td>
						<td>{{row.fechaEjecucion}}</td>
					</tr>
					</tbody>
				</table>
				<br/>
			</div>

	</div>
</div>