<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="kanbanController as kanbanc" class="maincontainer all_page" id="title">
	    
	    <h3>Reporte de Actividades</h3>
	    <br/>
	    <div class="row" align="center" >
		    
		</div>
		 
		<div class="row" align="center" >
			<div class="kanban-chart" >
	        	<div ds:kanban-board items="itemsKanban"  states="states" ng-if="mostrarKanban"
	                     on-adding-new-item="initializeNewItem(item)" on-editing-item="deleteItem(item)"
	                     edit-item-button-text="'?'" edit-item-button-tool-tip="'Delete item'"
	                     on-item-state-changed="onItemStateChanged(item, state)" >
	    		</div ds:kanban-board>
	    	</div>	
	    </div>
	</div>
