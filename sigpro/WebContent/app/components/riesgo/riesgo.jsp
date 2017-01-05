<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="riesgoController as riesgoc" class="maincontainer all_page" id="title">
	    <script type="text/ng-template" id="buscarRiesgoTipo.jsp">
    		<%@ include file="/app/components/riesgo/buscarRiesgoTipo.jsp"%>
  	    </script>
		<h3>Riesgos</h3><br/>
		<h4>{{ riesgoc.proyectoNombre }}</h4><br/>
		<div class="row" align="center" ng-if="!riesgoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="crearCooperante">
			       		<label class="btn btn-primary" ng-click="riesgoc.nuevo()">Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="editarCooperante"><label class="btn btn-primary" ng-click="riesgoc.editar()">Editar</label></shiro:hasPermission>
			       <shiro:hasPermission name="eliminarCooperante">
			       		<label class="btn btn-primary" ng-click="riesgoc.borrar()">Borrar</label>
			       </shiro:hasPermission>			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="verCooperante">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="riesgoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="riesgoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!riesgoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="riesgoc.totalRiesgos" 
						ng-model="riesgoc.paginaActual" 
						max-size="riesgoc.numeroMaximoPaginas" 
						items-per-page="riesgoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="riesgoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
		</div>
		<div class="row" ng-if="riesgoc.mostraringreso">
			<h4 ng-hide="!riesgoc.esnuevo">Nuevo riesgo</h4>
			<h4 ng-hide="riesgoc.esnuevo">Edición de riesgo</h4>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="riesgoc.guardar()">Guardar</label>
			        <label class="btn btn-primary" ng-click="riesgoc.irATabla()">Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form>
						<div class="form-group">
							<label for="id">ID</label>
    						<label class="form-control" id="id">{{ riesgoc.riesgo.id }}</label>
						</div>
						<div class="form-group">
							<label for="nombre">* Nombre</label>
    						<input type="text" class="form-control" id="nombre" placeholder="Nombre" ng-model="riesgoc.riesgo.nombre">
						</div>
						
						<div class="form-group">
							<label for="campo3">* Tipo Riesgo:</label>
				          	<div class="input-group">
				            	<input type="hidden" class="form-control" ng-model="riesgoc.riesgoTipoid" /> 
				            	<input type="text" class="form-control" id="irietipo" name="irietipo" placeholder="Nombre Tipo Riesgo" ng-model="riesgoc.riesgoTipoNombre" ng-readonly="true" required/>
				            	<span class="input-group-addon" ng-click="riesgoc.buscarRiesgoTipo()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label for="campo3">* Componente:</label>
				          	<div class="input-group">
				            	<input type="hidden" class="form-control" ng-model="riesgoc.componenteid" /> 
				            	<input type="text" class="form-control" id="icomp" name="icomp" placeholder="Nombre Componente" ng-model="riesgoc.componenteNombre" ng-readonly="true" required/>
				            	<span class="input-group-addon" ng-click="riesgoc.buscarComponente()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label for="campo3">* Producto:</label>
				          	<div class="input-group">
				            	<input type="hidden" class="form-control" ng-model="riesgoc.productoid" /> 
				            	<input type="text" class="form-control" id="iprod" name="iprod" placeholder="Nombre Producto" ng-model="riesgoc.productoNombre" ng-readonly="true" required/>
				            	<span class="input-group-addon" ng-click="riesgoc.buscarProducto()"><i class="glyphicon glyphicon-search"></i></span>
				          	</div>
						</div>
						
						<div class="form-group">
							<label for="descripcion">Descripción</label>
    						<input type="text" class="form-control" id="descripcion" placeholder="Descripción" ng-model="riesgoc.riesgo.descripcion">
						</div>
						<div class="form-group">
							<label for="usuarioCreo">Usuario que creo</label>
    						<label class="form-control" id="usuarioCreo">{{ riesgoc.riesgo.usuarioCreo }}</label>
						</div>
						<div class="form-group">
							<label for="fechaCreacion">Fecha de creación</label>
    						<label class="form-control" id="fechaCreacion">{{ riesgoc.riesgo.fechaCreacion }}</label>
						</div>
						<div class="form-group">
							<label for="usuarioActualizo">Usuario que actualizo</label>
    						<label class="form-control" id="usuarioCreo">{{ riesgoc.riesgo.usuarioActualizo }}</label>
						</div>
						<div class="form-group">
							<label for="fechaActualizacion">Fecha de actualizacion</label>
    						<label class="form-control" id="usuarioCreo">{{ riesgoc.riesgo.fechaActualizacion }}</label>
						</div>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
				        <label class="btn btn-success" ng-click="riesgoc.guardar()">Guardar</label>
				        <label class="btn btn-primary" ng-click="riesgoc.irATabla()">Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
