<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="matrizriesgoController as matrizriesgoc" class="maincontainer all_page" id="title">

  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Matriz de Riesgos</h3><br/>
		<h4>{{ riesgoc.proyectoNombre }}</h4><br/>
		<div class="row" align="center" >

			<div class="operation_buttons" align="left">
					<div class="btn-group">
						<label class="btn btn-primary" ng-click="matrizriesgoc.exportarExcel()" uib-tooltip="Exportar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true">&nbsp;Exportar</span></label>
					</div>
			</div>
			<div class="col-sm-12 ">
				<table st-table="matrizriesgoc.riesgos" st-safe-src="matrizriesgoc.lista" class="table table-condensed table-hover" >
					<thead>
						<tr>
							<th>Riesgo</th>
							<th>Nivel</th>
							<th st-sort="nombre">Descripción</th>
							<th>Categoría</th>
							<th>Impacto proyectado</th>
							<th>Impacto</th>
							<th>Puntuación de impacto</th>
							<th>Probabilidad</th>
							<th>Punteo de prioridad</th>
							<th>Gatillos / Sintomas</th>
							<th>Respuesta</th>
							<th>Responsable</th>
							<th>Riesgos secundarios</th>
							<th>¿Ha sido ejecutado?</th>
							<th>Fecha de Ejecución</th>

						</tr>
						<tr>
							<th colspan="7"><input st-search="" class="form-control" placeholder="Buscar" type="text"/></th>
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