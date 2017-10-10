<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlUnidadEjecutora as unidad" class="maincontainer all_page">

  <script type="text/ng-template" id="buscarEntidad.jsp">
    <%@ include file="/app/components/unidadejecutora/buscarEntidad.jsp"%>
  </script>
	<shiro:lacksPermission name="33010">
		<p ng-init="unidad.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
	
  <div class="panel panel-default">
	  <div class="panel-heading"><h3>Unidad Ejecutora</h3></div>
	</div>

  <div class="row" align="center" ng-hide="unidad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="33040">
	        <label class="btn btn-primary" ng-click="unidad.nuevo()" uib-tooltip="Nuevo">
	        <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
      	</shiro:hasPermission> 
      	<shiro:hasPermission name="33020">
	        <label class="btn btn-primary" ng-click="unidad.editar()"  uib-tooltip="Editar">
			<span class="glyphicon glyphicon-pencil"></span> Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="33030">
				<label class="btn btn-danger" ng-click="controller.borrar()" title="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="33010">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="unidad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
	  <br>
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

  <div class="row second-main-form" ng-show="unidad.esForma">

	<div class="page-header">
		<h2 ng-hide="!unidad.esNuevo"><small>Nueva unidad ejecutora</small></h2>
		<h2 ng-hide="unidad.esNuevo"><small>Edición de unidad ejecutora</small></h2>
	</div>
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="33020">
        	<label class="btn btn-success" ng-click="form.$valid ? unidad.guardar() : ''" ng-disabled="!form.$valid"  uib-tooltip="Guardar">
			<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
      	</shiro:hasPermission> 
        <label class="btn btn-primary" ng-click="unidad.cancelar()" uib-tooltip="Ir a Tabla">
		<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
      </div>
    </div>

   <div class="col-sm-12">

      <form name="form">
	        
	        <div class="form-group"  >
				<input type="number" class="inputText" name="campo1" id="campo1"  ng-model="unidad.codigo" 
					ng-required="true" ng-readonly="!unidad.esNuevo"
					ng-value="unidad.codigo" onblur="this.setAttribute('value', this.value);">
			     <label class="floating-label">* Unidad Ejecutora</label>
			</div>
        
		    <div class="form-group">
	          <input type="text" class="inputText" ng-model="unidad.nombre" ng-required="true" 
	          		ng-value="unidad.nombre" onblur="this.setAttribute('value', this.value);"/>
	          <label class="floating-label">* Nombre Unidad Ejecutora</label>
	        </div>
		
		    <div class="form-group" ng-required="true">
	            <input type="text" class="inputText" id="campo3" name="campo3" ng-model="unidad.nombreEntidad" 
	            ng-readonly="true" ng-required="true" ng-click="unidad.buscarEntidad()"
	            ng-value="unidad.nombreEntidad" onblur="this.setAttribute('value', this.value);" />
	            <span class="label-icon" ng-click="unidad.buscarEntidad()"><i class="glyphicon glyphicon-search"></i></span>
	            <label class="floating-label">* Entidad</label>
	        </div>
	        <br/>
	        <div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p> {{ unidad.unidad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label  class="label-form" for="fechaCreacion">Fecha de creación</label>
									<p id="fechaCreacion"> {{ unidad.unidad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ unidad.unidad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label  class="label-form" for="fechaActualizacion">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ unidad.unidad.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
	  </form>

    </div>
    <div align="center" class="label-form">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <shiro:hasPermission name="33020">
        	<label class="btn btn-success" ng-click="form.$valid ? unidad.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
			<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="unidad.cancelar()" uib-tooltip="Ir a Tabla">
		<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>