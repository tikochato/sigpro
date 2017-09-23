<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="metasController as metac" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="17010">
			<p ng-init="metac.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Metas de {{ metac.nombreTipoPcp }}</h3></div>
		</div>
		<div class="subtitulo">
			{{metac.nombrePcp}}
		</div>
				
		<div align="center" ng-hide="metac.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="17040">
			       		<label class="btn btn-primary" ng-click="metac.nueva()" title="Nuevo" click="setFocusToTextBox()">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="17030">
			       		<label class="btn btn-danger" ng-click="metac.borrar()" title="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
			        
			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="17010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="metac.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
						<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="metac.gridOptions" class="grid" 
				 	ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-auto-resize ui-grid-edit		
					style="height: 250px;">
					<div class="grid_loading" ng-hide="!metac.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  metac.totalMetas + (metac.totalMetas == 1 ? " Meta" : " Metas" ) }}</div>
			</div>
			
			<div class="valores" ng-show="metac.mostrarValores">
					<br>
				<div class="form-group col-sm-2" style="text-align: left;">
					<select class="inputText" 
						ng-model="metac.anio" 
						ng-options="opcion for opcion in metac.anios" 
						ng-readonly="true" ng-required="true" >
						<option value="">Seleccione un año</option>
					</select>
					<label for="nombre" class="floating-label">* Año</label>
				</div>
				<div class="col-sm-12" align="center">
					<br>
	    			<table class="table table-striped"  style="height: 100%">
						<thead >
							<tr>
								<th style="text-align: center;" class="label-form">Tipo</th>
		         				<th style="text-align: center;" class="label-form">Enero</th>
		         				<th style="text-align: center;" class="label-form">Febrero</th>
		         				<th style="text-align: center;" class="label-form">Marzo</th>
		         				<th style="text-align: center;" class="label-form">Abril</th>
		         				<th style="text-align: center;" class="label-form">Mayo</th>
		         				<th style="text-align: center;" class="label-form">Junio</th>
		         				<th style="text-align: center;" class="label-form">Julio</th>
		         				<th style="text-align: center;" class="label-form">Agosto</th>
		         				<th style="text-align: center;" class="label-form">Septiembre</th>
		         				<th style="text-align: center;" class="label-form">Octubre</th>
		         				<th style="text-align: center;" class="label-form">Noviembre</th>
								<th style="text-align: center;" class="label-form">Diciembre</th>
		         				<th style="text-align: center;" class="label-form">Total</th>
		         			</tr>
						</thead>
						<tbody >
							<tr>
					      		<td nowrap> <p class="nombreFormat">Planificado</p> </td>
					      		<td nowrap> <p class="nombreFormat">1</p> </td>
								<td nowrap> <p class="nombreFormat">2</p> </td>
								<td nowrap> <p class="nombreFormat">3</p> </td>
								<td nowrap> <p class="nombreFormat">4</p> </td>
								<td nowrap> <p class="nombreFormat">5</p> </td>
								<td nowrap> <p class="nombreFormat">6</p> </td>
								<td nowrap> <p class="nombreFormat">7</p> </td>
								<td nowrap> <p class="nombreFormat">8</p> </td>
								<td nowrap> <p class="nombreFormat">9</p> </td>
								<td nowrap> <p class="nombreFormat">10</p> </td>
								<td nowrap> <p class="nombreFormat">11</p> </td>
								<td nowrap> <p class="nombreFormat">12</p> </td>
								<td nowrap> <p class="nombreFormat">tot</p> </td>
					      	</tr>
					      	<tr>
					      		<td nowrap> <p class="nombreFormat">Real</p> </td>
					      		<td nowrap> <p class="nombreFormat">1</p> </td>
								<td nowrap> <p class="nombreFormat">2</p> </td>
								<td nowrap> <p class="nombreFormat">3</p> </td>
								<td nowrap> <p class="nombreFormat">4</p> </td>
								<td nowrap> <p class="nombreFormat">5</p> </td>
								<td nowrap> <p class="nombreFormat">6</p> </td>
								<td nowrap> <p class="nombreFormat">7</p> </td>
								<td nowrap> <p class="nombreFormat">8</p> </td>
								<td nowrap> <p class="nombreFormat">9</p> </td>
								<td nowrap> <p class="nombreFormat">10</p> </td>
								<td nowrap> <p class="nombreFormat">11</p> </td>
								<td nowrap> <p class="nombreFormat">12</p> </td>
								<td nowrap> <p class="nombreFormat">tot</p> </td>
					      	</tr>
						</tbody>
					</table>
				</div>
			</div>
			
    		</shiro:hasPermission>
    		
		</div>
	</div>
