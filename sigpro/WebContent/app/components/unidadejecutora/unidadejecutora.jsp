<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlUnidadEjecutora as unidad" class="maincontainer all_page">

  <script type="text/ng-template" id="buscarEntidad.jsp">
    <%@ include file="/app/components/unidadejecutora/buscarEntidad.jsp"%>
  </script>

  <h3>{{ unidad.esForma ? (unidad.esNuevo ? "Nueva Unidad Ejecutora" : "Editar Unidad Ejecutora") : "Unidad Ejecutora" }}</h3>

  <br />

  <div class="row" align="center" ng-hide="unidad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="crearEntidad">
        <label class="btn btn-primary" ng-click="unidad.nuevo()">Nueva</label>
      	</shiro:hasPermission> 
      	<shiro:hasPermission name="editarEntidad">
        <label class="btn btn-primary" ng-click="unidad.editar()">Editar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="verEntidad">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="unidad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
      <div id="grid1" ui-grid="unidad.opcionesGrid" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!unidad.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <ul uib-pagination total-items="unidad.totalElementos" ng-model="unidad.paginaActual" max-size="unidad.numeroMaximoPaginas" items-per-page="unidad.elementosPorPagina" first-text="Primero"
        last-text="Último" next-text="Siguiente" previous-text="Anterior" class="pagination-sm" boundary-links="true" force-ellipses="true" ng-change="unidad.cambioPagina()"
      ></ul>
    </div>
    </shiro:hasPermission>
   
  </div>

  <div class="row main-form" ng-show="unidad.esForma">

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid ? unidad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
        <label class="btn btn-primary" ng-click="unidad.cancelar()">Ir a Tabla</label>
      </div>
    </div>

    <div>

      <form name="form">

		<div class="row">
	        <div class="form-group">
	          <label>* Unidad Ejecutora:</label> 
	          <input type="number" class="form-control" id="campo1" name="campo1" placeholder="Unidad Ejecutora" ng-model="unidad.codigo" ng-readonly="!unidad.esNuevo" ng-required="true" />
	        </div>
        </div>

		<div class="row">
	        <div class="form-group">
	          <label for="campo2">* Nombre Unidad Ejecutora:</label> 
	          <input type="text" class="form-control" placeholder="Nombre Unidad Ejecutora" ng-model="unidad.nombre" ng-required="true" />
	        </div>
	
	        <div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo3.$invalid }">
	          <label>* Entidad:</label>
	          <div class="input-group">
	            <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Nombre Entidad" ng-model="unidad.nombreEntidad" ng-readonly="true" ng-required="true"/>
	            <span class="input-group-addon" ng-click="unidad.buscarEntidad()"><i class="glyphicon glyphicon-search"></i></span>
	          </div>
	        </div>
	        <div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ unidad.unidad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ unidad.unidad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ unidad.unidad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ unidad.unidad.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
		</div>
      </form>

    </div>
    <div align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid ? unidad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
        <label class="btn btn-primary" ng-click="unidad.cancelar()">Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>