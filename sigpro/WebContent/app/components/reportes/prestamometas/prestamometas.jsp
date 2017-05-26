<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
		pre {    
		    margin: 0 0 0px;
		    font-size: medium;
		    line-height: normal;
		    color: #333;
		    word-break: none;
		    word-wrap: break-word;
		    background-color: transparent;
		    border: none;
		    border-radius: none;
		    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
		    padding: 0px;
		    line-height: normal;
		     
		}
		.centrado{
			text-align: center;
		}
		.inputText{
			height: 21px; 
		}
	</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="prestamometasController as pmetasc" class="maincontainer all_page" id="title">
	    
  	    <shiro:lacksPermission name="30010">
			<p ng-init="pmetasc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Metas de Préstamo</h3></div>
		</div>
		<div class="subtitulo">
			{{ pmetasc.proyectoNombre }}
		</div>
		
		<div class="row" align="center" >
		
			<div class="operation_buttons" align="right">
					<div class="btn-group">
						<label class="btn btn-primary"  ng-click="pmetasc.exportarExcel()" uib-tooltip="Exportar">
						<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span> Exportar</label>
					</div>
			</div>
			<div class="col-sm-12 ">
				<table st-table="pmetasc.prestamometas" st-safe-src="pmetasc.lista" class="table table-condensed table-hover">
					<thead> 
						<tr>
							<th class="label-form">Producto</th>
							<th class="label-form">Unidad de Medida</th>
							<th class="label-form centrado">Meta Planificada</th>
							<th class="label-form centrado">Meta Real</th>
							<th class="label-form centrado">Meta Anual Planificada</th>
							<th class="label-form centrado">Meta Anual Real</th>
							<th class="label-form centrado">Línea Base</th>
							<th class="label-form centrado">Meta Final</th>
						</tr>
					</thead>
					<tbody>
					<tr ng-repeat="row in pmetasc.prestamometas" padre="{{row.objetoPadre}}" ng-click="pmetasc.acordion(row.id, row.objetoTipo)" ng-hide="row.objetoPadre == pmetasc.padreActual && row.objetoTipo > pmetasc.objetoTipo" index="{{$index}}">
						
						<td>
							<pre>{{row.nombre}}</pre>
						</td>
						<td>{{row.unidadDeMedida}}</td>
						<td class="centrado">{{row.metaPlanificada}}</td>
						<td class="centrado">
							<div ng-show="!row.editarMetaReal" ng-click="pmetasc.editarMetaReal(row)"  uib-tooltip="{{row.metaRealFecha}}">{{row.metaReal}}</div>
							<input ng-show="row.editarMetaReal" type="text"  class="inputText centrado" 
								ng-model="row.metaReal" ng-value="row.metaReal" ng-blur="pmetasc.cancelaEditarMetaReal(row)"
								ng-keypress="pmetasc.guardarMetaReal($event)">
						</td>	
						<td class="centrado">{{row.metaAnualPlanificada}}</td>
						<td class="centrado">
							<div ng-show="!row.editarMetaAnualReal" ng-click="pmetasc.editarMetaAnualReal(row)"  uib-tooltip="{{row.metaAnualRealFecha}}">{{row.metaAnualReal}}</div>
							<input ng-show="row.editarMetaAnualReal" type="text"  class="inputText centrado" 
								ng-model="row.metaAnualReal" ng-value="row.metaAnualReal" ng-blur="pmetasc.cancelaEditarMetaAnualReal(row)"
								ng-keypress="pmetasc.guardarMetaAnualReal($event)">
						</td>
						<td class="centrado">{{row.lineaBase}}</td>
						<td class="centrado">{{row.metaFinal}}</td>
					</tr>
					</tbody>
				</table>
				
				<br/>
				
				
			</div>
		  
	</div>
</div>
