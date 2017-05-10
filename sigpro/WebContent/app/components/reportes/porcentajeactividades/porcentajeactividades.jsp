<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="porcentajeactividadesController as porcentajeactividadesc" class="maincontainer all_page" id="title">
	    
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Cartelera de Actividades</h3></div>
		</div>
		<div class="subtitulo">
			{{ porcentajeactividadesc.objetoTipoNombre }} {{ porcentajeactividadesc.proyectoNombre }}
		</div>
		<br/>
		<br/>
		<div class="row">
			<div class="form-group col-sm-3" >
				<select  class="inputText" ng-model="porcentajeactividadesc.tObjeto"
					ng-options="a.text for a in porcentajeactividadesc.tObjetos" ng-change="porcentajeactividadesc.buscarActividades(porcentajeactividadesc.tObjeto.value)"></select>
				<label for="tObjeto" class="floating-label">Tipo Objeto</label>
			</div>
			<div class="form-group col-sm-3" >
				<select class="inputText" ng-model="porcentajeactividades.tColeccionObjeto"
					ng-options="a.text for a in porcentajeactividades.tColeccionObjetos"></select>
					<label for="tObjeto" class="floating-label">Nombre</label>
			</div>
			<div class="form-group col-sm-3" >
				<label class="btn btn-default" ng-click="porcentajeactividades.refrescar();" uib-tooltip="Refrescar" 
					tooltip-placement="bottom">
				<span class="glyphicon glyphicon-refresh"></span></label>
			</div>
		</div>	 
		<div class="row" align="center" >
		<br>
			<div class="kanban-chart" >
	        	<div ds:kanban-board items="itemsKanban"  states="states" ng-if="mostrarKanban"
	                     on-adding-new-item="initializeNewItem(item)" on-editing-item="deleteItem(item)"
	                     edit-item-button-text="'?'" edit-item-button-tool-tip="'Delete item'"
	                     on-item-state-changed="onItemStateChanged(item, state)" >
	    		</div ds:kanban-board>
	    	</div>	
	    </div>
	</div>
