<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
		.centrado {
			text-align: center; vertical-align: top;
		}
	</style>
	
	<div ng-controller="matrizriesgoController as matrizriesgoc" class="maincontainer all_page" id="title">

  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
	  		<div class="panel-heading"><h3>Matriz de Riesgos</h3></div>
		</div>
		
		<br>
	    	<div class="row">
	    		<div class="form-group col-sm-6" align="left">
					<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="matrizriesgoc.cambioPrestamo"
						  local-data="matrizriesgoc.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
						  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
						  initial-value="matrizriesgoc.prestamoNombre" focus-out="matrizriesgoc.blurPrestamo()" input-name="prestamo"></div>
					<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
	    	</div>
	    	<div class="row">
	    		<div class="form-group col-sm-6" align="left">
					<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="matrizriesgoc.cambioPep"
						  local-data="matrizriesgoc.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
						  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
						  initial-value="matrizriesgoc.pepNombre" focus-out="matrizriesgoc.blurPep()" input-name="pep" disable-input="matrizriesgoc.prestamoId==null"></div>
					<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
				</div>
	    	</div>
	    	<div class="row">		    	
		    		<div class="form-group col-sm-4" align="left">
						<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="matrizriesgoc.cambioLineaBase"
							  local-data="matrizriesgoc.lineasBase" search-fields="nombre" title-field="nombre" 
							  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="matrizriesgoc.lineaBaseNombre" 
							  focus-out="matrizriesgoc.blurLineaBase()" input-name="lineaBase"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>					
					<div class="col-sm-6" align="right" ng-hide="!matrizriesgoc.mostrarTabla">
						<div class="col-sm-12">
							<div class="btn-group" style="padding-left: 20px;">
								<label class="btn btn-default" ng-click="matrizriesgoc.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!matrizriesgoc.mostrarDescargar">
								<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
								<label class="btn btn-default" ng-click="matrizriesgoc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							</div>
						</div>
					</div>
    			<br><br><br><br>
	    	</div>			
		
		<div class="row" align="center" >
			<br>
			<div class="col-sm-12 " ng-hide="!matrizriesgoc.mostrarTabla">
				<table st-table="matrizriesgoc.riesgos" st-safe-src="matrizriesgoc.lista" class="table table-striped">
					<thead>
						<tr>
							<th class="label-form centrado">Id</th>
							<th st-sort="nombre" class="label-form centrado">Riesgo</th>
							<th class="label-form centrado">Tipo de Riesgo</th>
							<th class="label-form centrado">Nivel</th>
							<th class="label-form centrado">Calificación de Riesgo</th>
							<th class="label-form centrado">Probabilidadd de Ocurrencia</th>
							<th class="label-form centrado">Calificación</th>
							<th class="label-form centrado">Impacto en Tiempo</th>
							<th class="label-form centrado">Contingencia en Tiempo</th>
							<th class="label-form centrado">Impacto en Monto (Q)</th>
							<th class="label-form centrado">Contingencia en Monto (Q)</th>
							<th class="label-form centrado">Evento Iniciador</th>
							<th class="label-form centrado">Consecuencias</th>
							<th class="label-form centrado">Riesgos Secundarios</th>
							<th class="label-form centrado">Solución</th>
							<th class="label-form centrado">Responsable</th>
							<th class="label-form centrado">¿Ha sido ejecutado?</th>
							<th class="label-form centrado">Fecha de Ejecución</th>
							<th class="label-form centrado">Resultado</th>
							<th class="label-form centrado">Observaciones</th>
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