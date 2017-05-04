<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div ng-controller="controlEntidad as entidad" class="maincontainer all_page">
	<shiro:lacksPermission name="10010">
			<p ng-init="entidad.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
	<div class="panel panel-default">
	<div class="panel-heading">
  		<h3>{{ entidad.esForma ? (entidad.esNuevo ? "Nueva Entidad" : "Editar Entidad") : "Entidades" }}</h3>
  		</div>
  	</div>

  <br />

  <div class="row" align="center" ng-hide="entidad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="1040">
      		<label class="btn btn-primary" ng-click="entidad.nueva()"><span class="glyphicon glyphicon-plus" uib-tooltip="Nuevo"></span>Nueva</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="1010">
        	<label class="btn btn-primary" ng-click="entidad.editar()"><span class="glyphicon glyphicon-pencil" uib-tooltip="Editar"></span>Editar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="10010">
    	  <div class="col-sm-12" align="center">
	      <div style="height: 35px;">
			<div style="text-align: right;">

				<div class="btn-group" role="group" aria-label="">
					<a class="btn btn-default" href ng-click="entidad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
				</div>
			</div>
		  </div>
		  <br/>
	      <div id="grid1" ui-grid="entidad.entidades_gridOptions" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
	        <div class="grid_loading" ng-hide="!entidad.mostrarCargando">
	          <div class="msg">
	            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
	          </div>
	        </div>
	      </div>
	      <ul uib-pagination
	        total-items="entidad.totalEntidades"
	        ng-model="entidad.paginaActual"
	        max-size="entidad.numeroMaximoPaginas"
	        items-per-page="entidad.elementosPorPagina"
	        first-text="Primero"
	        last-text="Último"
	        next-text="Siguiente"
	        previous-text="Anterior"
	        class="pagination-sm"
	        boundary-links="true"
	        force-ellipses="true"
	        ng-change="entidad.cambioPagina()"
	      ></ul>
	    </div>
	</shiro:hasPermission>

  </div>

  <div class="row second-main-form" ng-show="entidad.esForma">

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
      	<shiro:hasPermission name="1020">
        	<label class="btn btn-success" ng-click="form.$valid ? entidad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
        </shiro:hasPermission>
        <label class="btn btn-primary" ng-click="entidad.cancelar()">Ir a Tabla</label>
      </div>

    </div>

    <div>

      <form name="form" class="css-form" novalidate>

			<div class="row">
		        <div class="form-group col-sm-12">
		          <input type="number" class="inputText"  ng-model="entidad.entidad.entidad" 
		          ng-readonly="!entidad.esNuevo" ng-required="true" 
		          value="{{entidad.entidad.entidad}}"   
     				onblur="this.setAttribute('value', this.value);"/>
		          <label class="floating-label">* Entidad</label>
		        </div>
		    </div>

			<div class="row">
				<div class="form-group col-sm-12" >
				  <input type="text" class="inputText"   ng-model="entidad.entidad.nombre" 
				  ng-readonly="!entidad.esNuevo" ng-required="true" 
				  value="{{entidad.entidad.nombre}}"   
     				onblur="this.setAttribute('value', this.value);"/>
				  <label class="floating-label">* Nombre Entidad</label>
				</div>
			</div>
			<div class="row">
		        <div class="form-group col-sm-12">
		          <input type="text" class="inputText" ng-model="entidad.entidad.abreviatura"
		          value="{{entidad.entidad.abreviatura}}"   
     				onblur="this.setAttribute('value', this.value);">
		          <label class="floating-label">Abreviatura</label>
		        </div>
			</div>
      </form>

    </div>
    <div align="center" class="label-form">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="1020">      		
        	<label class="btn btn-success" ng-click="form.$valid ? entidad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label>
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="entidad.cancelar()">Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>
