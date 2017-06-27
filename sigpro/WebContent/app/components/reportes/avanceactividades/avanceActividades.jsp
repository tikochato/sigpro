<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	
	<div ng-controller="avanceActividadesController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
		  <div class="panel-heading"><h3>Avance de Actividades</h3></div>
		</div>
	    <br>
	    
	    <div class="row">
	    	<div class="col-sm-12">
		    	<div style="height: 100%; width: 100%; height: 15%">
		    		<div class="row">
			    		<div class="form-group col-sm-3">
							<select  class="inputText" ng-model="controller.prestamo"
								ng-options="a.text for a in controller.prestamos"></select>
							<label for="prestamo" class="floating-label">Préstamos</label>
						</div>
						<div class="form-group col-sm-3" >
							<label class="btn btn-default" ng-click="controller.generar()" uib-tooltip="Generar informe" 
								tooltip-placement="bottom">
								<span class="glyphicon glyphicon-new-window"></span>
							</label>
						</div>
		    		</div>
		    	</div>
	    	
		    	<div style="height: 100%; width: 100%; height: 85%">
		    		<div style="height: 5%; width: 100%">
						<div><h4><b>Actividades</b></h4></div>
					</div>
					<div id="grid1" ui-grid="controller.gridOptions1" style="width: 85%; height: 300px"
						ui-grid-edit 
						ui-grid-row-edit 
						ui-grid-resize-columns 
						ui-grid-cellNav 
						ui-grid-pinning
						class="grid">
						<div class="grid_loading" ng-hide="!controller.mostrarcargando" style="position: absolute; z-index: 1">
			  				<div class="msg">
			      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  				<br /><br />
					  				<b>Cargando, por favor espere...</b>
				  				</span>
							</div>
						</div>
					</div>
					<div style="height: 5%; width: 100%">
						<div><h4><b>Hitos</b></h4></div>
					</div>
					<div id="grid2" ui-grid="controller.gridOptions2" style="width: 85%; height: 300px"
						ui-grid-edit 
						ui-grid-row-edit 
						ui-grid-resize-columns 
						ui-grid-cellNav 
						ui-grid-pinning
						class="grid">
						<div class="grid_loading" ng-hide="!controller.mostrarcargando" style="position: absolute; z-index: 1">
			  				<div class="msg">
			      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  				<br /><br />
					  				<b>Cargando, por favor espere...</b>
				  				</span>
							</div>
						</div>
					</div>
					<div style="height: 5%; width: 100%">
						<div><h4><b>Productos</b></h4></div>
					</div>
					<div id="grid3" ui-grid="controller.gridOptions3" style="width: 85%; height: 300px"
						ui-grid-edit 
						ui-grid-row-edit 
						ui-grid-resize-columns 
						ui-grid-cellNav 
						ui-grid-pinning
						class="grid">
						<div class="grid_loading" ng-hide="!controller.mostrarcargando" style="position: absolute; z-index: 1">
			  				<div class="msg">
			      				<span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  				<br /><br />
					  				<b>Cargando, por favor espere...</b>
				  				</span>
							</div>
						</div>
					</div>
		    	</div>
	    	</div>
	    </div>
	</div>