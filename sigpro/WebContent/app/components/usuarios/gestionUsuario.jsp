<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/ng-template" id="buscarPermiso.jsp">
   	<%@ include file="/app/components/usuarios/buscarPermiso.jsp"%>
</script>
<script type="text/ng-template" id="cambiarPassword.jsp">
   	<%@ include file="/app/components/usuarios/cambiarPassword.jsp"%>
</script>
<script type="text/ng-template" id="buscarColaborador.jsp">
   	<%@ include file="/app/components/usuarios/buscarColaborador.jsp"%>
</script>
	<div ng-controller="gestionUsuariosController as usuarioc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="34010">
			<p ng-init="usuarioc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Usuarios</h3></div>
		</div>

		<div class="row" align="center" ng-hide="usuarioc.isCollapsed">
		<br>
		
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="34040">
						<label class="btn btn-primary" ng-click="" uib-tooltip="Nuevo">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34010">
						<label class="btn btn-primary" ng-click="" uib-tooltip="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34030">
						<label class="btn btn-danger" ng-click="" uib-tooltip="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		<div class="col-sm-12" align="center">
    			<uib-tabset active="active">
				    <uib-tab index="0" heading="Usuarios" select="usuarioc.changeActive(1)">
					    <br>
					    <div style="height: 35px;">
							<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
								<a class="btn btn-default" href ng-click="usuarioc.reiniciarVista(1)" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
							</div>
							</div>
						</div>
						<br>
						<div id="grid1" ui-grid="usuarioc.gridOptions" ui-grid-save-state
								ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="usuarioc.myGrid">
								<div class="grid_loading" ng-hide="!usuarioc.mostrarcargando">
						  	<div class="msg">
						      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
								  <br /><br />
								  <b>Cargando, por favor espere...</b>
							  </span>
							</div>
						  </div>
						</div>
						<ul uib-pagination total-items="usuarioc.totalUsuarios"
								ng-model="usuarioc.paginaActualUsuarios"
								max-size="usuarioc.numeroMaximoPaginas"
								items-per-page="usuarioc.elementosPorPagina"
								first-text="Primero"
								last-text="Último"
								next-text="Siguiente"
								previous-text="Anterior"
								class="pagination-sm" boundary-links="true" force-ellipses="true"
								ng-change="usuarioc.cambiarPagina(1)"
						>
						</ul>
	    						
				    </uib-tab>				   
				    <uib-tab index="1" heading="Colaboradores" select="usuarioc.changeActive(2)" >
				    <br>
				     <div style="height: 35px;">
						<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="usuarioc.reiniciarVista(2)" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
						</div>
						</div>
					</div>
					<br> 
				    </uib-tab>
				  </uib-tabset>
    			
			</div>
    		

		</div>
		
		<!-- inicio de edición -->

		


	</div>
