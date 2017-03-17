<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlSubproductoPropiedad as subproductoPropiedad" class="maincontainer all_page">
	<shiro:lacksPermission name="41010">
		<p ng-init="subproductoPropiedad.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
  <h3>{{ subproductoPropiedad.esForma ? (subproductoPropiedad.esNuevo ? "Nueva Propiedad de Subproducto" : "Editar Propiedad de Subproducto") : "Propiedad de Subproducto" }}</h3>

  <br />

  <div align="center" ng-hide="subproductoPropiedad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="41040">
        	<label class="btn btn-primary" ng-click="subproductoPropiedad.nuevo()">Nueva</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="41010">
        	<label class="btn btn-primary" ng-click="subproductoPropiedad.editar()">Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="41030">
        	<label class="btn btn-primary" ng-click="subproductoPropiedad.borrar()">Borrar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="41010">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="subproductoPropiedad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
	  <br/>
      <div id="grid1" ui-grid="subproductoPropiedad.opcionesGrid" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!subproductoPropiedad.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <br/>
      <div class="total-rows">Total de {{  subproductoPropiedad.totalElementos + (subproductoPropiedad.totalElementos == 1 ? " Propiedad de Subproducto" : " Propiedades de Subproducto" ) }}</div>
      <ul uib-pagination 
      	total-items="subproductoPropiedad.totalElementos" 
      	ng-model="subproductoPropiedad.paginaActual" 
      	max-size="subproductoPropiedad.numeroMaximoPaginas" 
      	items-per-page="subproductoPropiedad.elementosPorPagina" 
      	first-text="Primero"
        last-text="Último" 
        next-text="Siguiente" 
        previous-text="Anterior" 
        class="pagination-sm" 
        boundary-links="true" 
        force-ellipses="true" 
        ng-change="subproductoPropiedad.cambioPagina()"
      ></ul>
    </div>
    </shiro:hasPermission>
   
  </div>

  <div class="row main-form" ng-show="subproductoPropiedad.esForma">
	  <h4 ng-hide="!subproductoPropiedad.esNuevo">Nueva propiedad de subproducto</h4>
	  <h4 ng-hide="subproductoPropiedad.esNuevo">Edición de propiedad de subproducto</h4>

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
      	<shiro:hasPermission name="41020">
        	<label class="btn btn-success" ng-click="form.$valid ? subproductoPropiedad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="subproductoPropiedad.cancelar()">Ir a Tabla</label>
      </div>
    </div>
    
    <div>
	    <form name="form" class="css-form" novalidate>
	
	      <div class="row col-sm-12">
		      <div class="form-group ">
		        <label>Id</label>
		        <p class="form-control-static">{{ subproductoPropiedad.codigo }}</p>
		      </div>
	      </div>

	      <div class="row">
		      <div class="form-group">
		        <label for="campo1">* Nombre</label> 
		        <input type="text" class="form-control" placeholder="Nombre" ng-model="subproductoPropiedad.nombre" ng-required="true" />
		      </div>

		      <div class="form-group">
		        <label for="campo2">* Tipo</label>     			
    			<select class="form-control" ng-model="subproductoPropiedad.datoTipoSeleccionado" ng-options="tipo as tipo.nombre for tipo in subproductoPropiedad.datoTipos track by tipo.id" ng-required="true"
    			ng-readonly="true" ng-disabled="!subproductoPropiedad.esNuevo" ng-required="true">
					<option disabled selected value> -- Seleccione Tipo -- </option>
				</select>
    		 </div>
		
		      <div class="form-group">
		        <label for="campo3">Descripción</label> 
		        <input type="text" class="form-control" placeholder="Descripción" ng-model="subproductoPropiedad.descripcion"/>
		      </div>
		      <div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ subproductoPropiedad.entidadSeleccionada.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ subproductoPropiedad.entidadSeleccionada.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ subproductoPropiedad.entidadSeleccionada.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ subproductoPropiedad.entidadSeleccionada.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
		      
	      </div>
	      
	    </form>
    </div>
  
    <div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      <shiro:hasPermission name="41020">
        	<label class="btn btn-success" ng-click="form.$valid ? subproductoPropiedad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="subproductoPropiedad.cancelar()">Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>