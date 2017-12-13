<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
		.centrado {
			text-align: center; 
		}
		
		.derecha {
			text-align: right; 
		}
		
		.nombreFormat {    
		    margin: 0 0 2px;
		    color: #333;
		    word-break: none;
		    word-wrap: break-word;
		    background-color: transparent;
		    border: none;
		    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
		    padding: 0px;
		    line-height: normal;	
		    overflow: initial;	
		    font-size: 13px;     
		}
		
		.textoRiesgo {
		  max-width: 300px;
		  white-space: nowrap;
		  overflow: hidden;
		  text-overflow: ellipsis;
		}		
		
		.numeroRiesgo {
		  max-width: 50px;
		  white-space: nowrap;
		  overflow: hidden;
		  text-overflow: ellipsis;
		}
		
		.montoRiesgo {
		  max-width: 115px;
		  white-space: nowrap;
		  overflow: hidden;
		  text-overflow: ellipsis;
		}
		
	    .table-striped>tbody>tr:nth-child(odd)>td {
    		background-color: #f3f3f3;
		}
		
		.buscar-control {
		    display: block;
		    width: 100%;
		    padding: 3px 8px;
		    font-size: 13px;
		    line-height: 1.42857143;
		    color: #555;
		    background-color: #fff;
		    background-image: none;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
		    box-shadow: inset 0 1px 1px rgba(0,0,0,0.075);
		    -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
		    -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
		    transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
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
					<div class="col-sm-8" align="right" ng-hide="!matrizriesgoc.mostrarTabla">
						<div class="btn-group" style="padding-left: 20px;">
							<label class="btn btn-default" ng-click="matrizriesgoc.exportarExcel()" uib-tooltip="Exportar a Excel">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							<label class="btn btn-default" ng-click="matrizriesgoc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
							<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
						</div>
					</div>
    			<br><br><br><br>
	    	</div>			
		
		<div style="float: left; float: left; position: absolute; margin-top: 60px; margin-left: -30px; z-index: 100;" ng-click="matrizriesgoc.Buscar()" ng-hide="!matrizriesgoc.mostrarTabla">
			<span class="glyphicon glyphicon-search" uib-tooltip="Buscar" tooltip-placement="top"></span>
		</div>
		<div class="row" align="center" style="overflow-x: auto;">
			<br>
			<div class="col-sm-12 " ng-hide="!matrizriesgoc.mostrarTabla">
				<table st-table="matrizriesgoc.riesgos" st-safe-src="matrizriesgoc.lista" class="table table-striped">
					<thead>
						<tr>
							<th class="label-form centrado">Id</th>
							<th st-sort="nombre" class="label-form centrado">Riesgo</th>
							<th st-sort="tipoNombre" class="label-form centrado">Tipo de Riesgo</th>
							<th st-sort="objetoTipoNombre" class="label-form centrado">Nivel</th>
							<th st-sort="impacto" class="label-form centrado">Calificación de Riesgo</th>
							<th st-sort="probabilidad" class="label-form centrado">Probabilidad de Ocurrencia</th>
							<th st-sort="calificacion" class="label-form centrado">Calificación Total</th>
							<th st-sort="impactoTiempo" class="label-form centrado">Impacto en Tiempo</th>
							<th st-sort="contingenciaTiempo" class="label-form centrado">Contingencia en Tiempo</th>
							<th st-sort="impactoMonto" class="label-form centrado">Impacto en Monto (Q)</th>
							<th st-sort="contingenciaMonto" class="label-form centrado">Contingencia en Monto (Q)</th>
							<th class="label-form centrado">Evento Iniciador</th>
							<th class="label-form centrado">Consecuencias</th>
							<th class="label-form centrado">Riesgos Secundarios</th>
							<th class="label-form centrado">Solución</th>
							<th st-sort="colaboradorNombre" class="label-form centrado">Responsable</th>
							<th st-sort="ejecutado" class="label-form centrado">¿Ha sido ejecutado?</th>
							<th st-sort="fechaEjecucion" class="label-form centrado">Fecha de Ejecución</th>
							<th class="label-form centrado">Resultado</th>
							<th class="label-form centrado">Observaciones</th>
						</tr>
						<tr ng-show="matrizriesgoc.mostrarBuscar">
							<th colspan="20" class="label-form"><input st-search="" class="buscar-control" placeholder="Buscar" type="text"/></th>
						</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in matrizriesgoc.riesgos">

						<td><p class="nombreFormat">{{row.idRiesgo}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.nombre}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.tipoNombre}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.objetoTipoNombre}}</p></td>
						<td><p class="nombreFormat numeroRiesgo derecha">{{row.impacto}}</p></td>
						<td><p class="nombreFormat numeroRiesgo derecha">{{row.probabilidad}}</p></td>
						<td><p class="nombreFormat numeroRiesgo derecha">{{row.calificacion}} </p></td>
						<td><p class="nombreFormat numeroRiesgo derecha">{{row.impactoTiempo}}</p></td>
						<td><p class="nombreFormat numeroRiesgo derecha">{{row.contingenciaTiempo}}</p></td>
						<td><p class="nombreFormat montoRiesgo derecha">{{row.impactoMonto| formatoMillones : 0}}</p></td>
						<td><p class="nombreFormat montoRiesgo derecha">{{row.contingenciaMonto| formatoMillones : 0}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.gatillos}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.consecuencia}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.riesgosSecundarios}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.solucion}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.colaboradorNombre}}</p></td>
						<td><p class="nombreFormat numeroRiesgo derecha">{{ row.ejecutado == 1 ? 'Si' : 'No' }}</p></td>
						<td><p class="nombreFormat montoRiesgo derecha">{{row.fechaEjecucion}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.resultado}}</p></td>
						<td><p class="nombreFormat textoRiesgo">{{row.observaciones}}</p></td>
					</tr>
				</tbody>
			</table>
			<br/>
		</div>
	</div>
</div>