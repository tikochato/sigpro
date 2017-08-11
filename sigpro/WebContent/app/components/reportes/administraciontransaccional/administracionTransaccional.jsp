<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
		.ui-grid-header-cell-wrapper {
			margin-left: 0px;
			text-align: center;
		}
		.text-center {
			text-align: center;
		}
	</style>
	<div ng-controller="administracionTransaccionalController as controller" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="24010">
			<p ng-init="controller.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="row">
	   		<div style="width: 100%; height: 15%">
	   			<div class="row">
	   				<div class="panel panel-default">
	  					<div class="panel-heading"><h3>Administración Transaccional</h3></div>
					</div>
				</div>
	   			<br>
	   		</div>	
		</div>
		<br>
		<div class="row" align="center" >
	    	<div class="col-sm-12">
	    		<div style="width: 100%; height: 85%; text-align: center">
	    			<div class="row">
	    				<div class="form-group col-sm-12" align="center">
	    					<div id="maingrid" ui-grid="controller.gridOptions" ng-class="'ui-grid-header-cell-wrapper'"
									ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
								<div class="grid_loading" ng-hide="!controller.mostrarcargando">
								  	<div class="msg">
								      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
										  <br /><br />
										  <b>Cargando, por favor espere...</b>
									  </span>
									</div>
							  	</div>
							</div>
							<br>
							<ul uib-pagination total-items="controller.totalPrestamo"
								ng-model="controller.paginaActual"
								max-size="controller.numeroMaximoPaginas"
								items-per-page="controller.elementosPorPagina"
								first-text="Primera"
								last-text="Última"
								next-text="Siguiente"
								previous-text="Anterior"
								class="pagination-sm" boundary-links="true" force-ellipses="true"
								ng-change="actividadc.cambioPagina()"
							></ul>
	    				</div>
	    			</div>
	    		</div>
   				<div style="width: 75%;">
   					<canvas id="bar" class="chart chart-bar" chart-data="controller.data" chart-labels="controller.labels" 
   					chart-options="controller.charOptions" chart-series="controller.series">
   					</canvas>
   				</div>
	    	</div>
	    </div>
	</div>