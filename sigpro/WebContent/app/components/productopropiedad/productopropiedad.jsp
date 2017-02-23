<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlProductoPropiedad as productoPropiedad" class="maincontainer all_page">
	<shiro:lacksPermission name="22010">
		<p ng-init="productoPropiedad.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
  <h3>{{ productoPropiedad.esForma ? (productoPropiedad.esNuevo ? "Nueva Propiedad de Producto" : "Editar Propiedad de Producto") : "Propiedad de Producto" }}</h3>

  <br />

  <div align="center" ng-hide="productoPropiedad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="22040">
        	<label class="btn btn-primary" ng-click="productoPropiedad.nuevo()">Nueva</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="22010">
        	<label class="btn btn-primary" ng-click="productoPropiedad.editar()">Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="22030">
        	<label class="btn btn-primary" ng-click="productoPropiedad.borrar()">Borrar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="22010">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="productoPropiedad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
	  <br/>
      <div id="grid1" ui-grid="productoPropiedad.opcionesGrid" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!productoPropiedad.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <br/>
      <div class="total-rows">Total de {{  productoPropiedad.totalElementos + (productoPropiedad.totalElementos == 1 ? " Propiedad de Producto" : " Propiedades de Producto" ) }}</div>
      <ul uib-pagination 
      	total-items="productoPropiedad.totalElementos" 
      	ng-model="productoPropiedad.paginaActual" 
      	max-size="productoPropiedad.numeroMaximoPaginas" 
      	items-per-page="productoPropiedad.elementosPorPagina" 
      	first-text="Primero"
        last-text="Último" 
        next-text="Siguiente" 
        previous-text="Anterior" 
        class="pagination-sm" 
        boundary-links="true" 
        force-ellipses="true" 
        ng-change="productoPropiedad.cambioPagina()"
      ></ul>
    </div>
    </shiro:hasPermission>
   
  </div>

  <div class="row main-form" ng-show="productoPropiedad.esForma">
	  <h4 ng-hide="!productoPropiedad.esNuevo">Nueva propiedad de producto</h4>
	  <h4 ng-hide="productoPropiedad.esNuevo">Edición de propiedad de producto</h4>

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
      	<shiro:hasPermission name="22020">
        	<label class="btn btn-success" ng-click="form.$valid ? productoPropiedad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="productoPropiedad.cancelar()">Ir a Tabla</label>
      </div>
    </div>
    
    <div>
	    <form name="form" class="css-form" novalidate>
	
	      <div class="row col-sm-12">
		      <div class="form-group ">
		        <label>Id</label>
		        <p class="form-control-static">{{ productoPropiedad.codigo }}</p>
		      </div>
	      </div>

	      <div class="row">
		      <div class="form-group">
		        <label for="campo1">* Nombre</label> 
		        <input type="text" class="form-control" placeholder="Nombre" ng-model="productoPropiedad.nombre" ng-required="true" />
		      </div>

		      <div class="form-group">
		        <label for="campo2">* Tipo</label>     			
    			<select class="form-control" ng-model="productoPropiedad.datoTipoSeleccionado" ng-options="tipo as tipo.nombre for tipo in productoPropiedad.datoTipos track by tipo.id" ng-required="true"
    			ng-readonly="true" ng-disabled="!productoPropiedad.esNuevo" ng-required="true">
					<option disabled selected value> -- Seleccione Tipo -- </option>
				</select>
    		 </div>
		
		      <div class="form-group">
		        <label for="campo3">Descripción</label> 
		        <input type="text" class="form-control" placeholder="Descripción" ng-model="productoPropiedad.descripcion"/>
		      </div>
		      <div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ productoPropiedad.entidadSeleccionada.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ productoPropiedad.entidadSeleccionada.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ productoPropiedad.entidadSeleccionada.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ productoPropiedad.entidadSeleccionada.fechaActualizacion }} </p>
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
      <shiro:hasPermission name="22020">
        	<label class="btn btn-success" ng-click="form.$valid ? productoPropiedad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="productoPropiedad.cancelar()">Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>