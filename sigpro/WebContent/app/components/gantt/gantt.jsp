<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="ganttController as ganttc" class="maincontainer all_page" id="title">
	    
	    <h3>Gantt</h3>
	    <div class="row" align="center" >
		    <div class="operation_buttons" align="right">
		    <form>
		    	
				<div class="btn-group">
					<label class="btn btn-primary" ng-click="ganttc.exportar()"><span class="glyphicon glyphicon glyphicon-import" aria-hidden="true"></span></label>
					<label class="btn btn-primary" ng-click="ganttc.exportar()"><span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
					<label class="btn btn-primary" ng-click="ganttc.zoomAcercar()"><span class="glyphicon glyphicon-zoom-in" aria-hidden="true"></span></label>
					<label class="btn btn-primary" ng-click="ganttc.zoomAlejar()"><span class="glyphicon glyphicon-zoom-out" aria-hidden="true"></span></label>
				</div>
				
			</form>
			</div>
		</div>
		 <div class="row" align="center" >
		 
		    <div class="gantt-chart">
				<div ds:gantt-chart id="ganttChartView" items="items" settings="settings" auto-refresh="{{ true }}" style="min-height: 400px"></div>
	    	</div>
			<br/>
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
