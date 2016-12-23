<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlProductoPropiedad as productoPropiedad" class="maincontainer all_page">

  <h3>{{ productoPropiedad.esForma ? (productoPropiedad.esNuevo ? "Nueva Propiedad de Producto" : "Editar Propiedad de Producto") : "Propiedad de Producto" }}</h3>

  <br />

  <div align="center" ng-hide="productoPropiedad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="crearPropiedadProducto">
        	<label class="btn btn-primary" ng-click="productoPropiedad.nuevo()">Nueva</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="editarPropiedadProducto">
        	<label class="btn btn-primary" ng-click="productoPropiedad.editar()">Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="eliminarPropiedadProducto">
        	<label class="btn btn-primary" ng-click="productoPropiedad.borrar()">Borrar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="verTipoProducto">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="productoPropiedad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
      <div id="grid1" ui-grid="productoPropiedad.opcionesGrid" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!productoPropiedad.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
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

  <div ng-show="productoPropiedad.esForma">

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid ? productoPropiedad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
        <label class="btn btn-danger" ng-click="productoPropiedad.cancelar()">Cancelar</label>
      </div>

    </div>
    
    <div>
	    <form name="form" class="css-form" novalidate>
	
	      <div class="row">
		      <div class="form-group col-sm-3" ng-show="!productoPropiedad.esNuevo">
		        <label for="campo0">ID:</label>
		        <input type="text" class="form-control" id="campo0" name="campo0" placeholder="ID" ng-model="productoPropiedad.codigo" ng-readonly="true" />
		      </div>
	      </div>

	      <div class="row">
		      <div class="form-group col-sm-10" ng-class="{ 'has-error' : form.campo1.$invalid }">
		        <label for="campo1">* Nombre:</label> 
		        <input type="text" class="form-control" id="campo1" name="campo1" placeholder="Nombre" ng-model="productoPropiedad.nombre" required />
		      </div>

		      <div class="form-group col-sm-2" ng-class="{ 'has-error' : form.campo2.$invalid }">
		        <label for="campo2">* Tipo:</label>     			
    			<select class="form-control" id="campo2" name="campo2" ng-model="productoPropiedad.datoTipoSeleccionado" ng-options="tipo as tipo.nombre for tipo in productoPropiedad.datoTipos track by tipo.id" required>
					<option disabled selected value> -- Seleccione Tipo -- </option>
				</select>
    		 </div>
		
		      <div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo3.$invalid }">
		        <label for="campo3">* Descripción:</label> 
		        <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Descripción" ng-model="productoPropiedad.descripcion" required />
		      </div>
		      
		      
	      </div>
	      
	    </form>
    </div>
  
    <div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid ? productoPropiedad.guardar() : '' " ng-disabled="!form.$valid">Guardar</label> 
        <label class="btn btn-danger" ng-click="productoPropiedad.cancelar()">Cancelar</label>
      </div>
    </div>
  </div>

</div>