<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlProductoPropiedad as productoPropiedad" class="maincontainer all_page">
	<shiro:lacksPermission name="22010">
		<p ng-init="productoPropiedad.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
  
  <div class="panel panel-default">
	  <div class="panel-heading"><h3>{{ productoPropiedad.esForma ? (productoPropiedad.esNuevo ? "Nueva Propiedad de Producto" : "Editar Propiedad de Producto") : "Propiedad de Producto" }}</h3></div>
	</div>

  <br />

  <div align="center" ng-hide="productoPropiedad.esForma">
		<br>
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="22040">
        	<label class="btn btn-primary" ng-click="productoPropiedad.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="22010">
        	<label class="btn btn-primary" ng-click="productoPropiedad.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="22030">
        	<label class="btn btn-danger" ng-click="productoPropiedad.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
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

  <div class="row second-main-form" ng-show="productoPropiedad.esForma">
	  <div class="page-header">
			<h2 ng-hide="!productoPropiedad.esNuevo"><small>Nueva propiedad de producto</small></h2>
	  		<h2 ng-hide="productoPropiedad.esNuevo"><small>Edición de propiedad de producto</small></h2>
	  	</div>

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
      	<shiro:hasPermission name="22020">
        	<label class="btn btn-success" ng-click="form.$valid ? productoPropiedad.guardar() : ''" ng-disabled="!form.$valid"  uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label> 
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="productoPropiedad.cancelar()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
      </div>
    </div>
    
    <div class="col-sm-12">
	    <form name="form" class="css-form" novalidate>
	
		      <div class="form-group ">
		        <label class="floating-label">ID {{ productoPropiedad.codigo }}</label>
		        <br/><br/>
		      </div>

		      <div class="form-group">
		        <input type="text" class="inputText" ng-model="productoPropiedad.nombre"  value="{{productoPropiedad.nombre}}" onblur="this.setAttribute('value', this.value);" ng-required="true" />
		        <label for="campo1" class="floating-label">* Nombre</label> 
		      </div>

		      <div class="form-group">   			
    			<select class="inputText" ng-model="productoPropiedad.datoTipoSeleccionado" ng-options="tipo as tipo.nombre for tipo in productoPropiedad.datoTipos track by tipo.id" ng-required="true"
    			ng-readonly="true" ng-disabled="!productoPropiedad.esNuevo" ng-required="true">
					<option disabled selected value> -- Seleccione Tipo -- </option>
				</select>
		        <label for="campo2" class="floating-label">* Tipo</label>  
    		 </div>
		
		      <div class="form-group">
		        <input type="text" class="inputText" ng-model="productoPropiedad.descripcion" value="{{productoPropiedad.descripcion}}" onblur="this.setAttribute('value', this.value);"/>
		        <label for="campo3" class="floating-label">Descripción</label> 
		      </div>
		      <div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p class=""> {{ productoPropiedad.entidadSeleccionada.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ productoPropiedad.entidadSeleccionada.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" id="usuarioCreo">{{ productoPropiedad.entidadSeleccionada.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" id="usuarioCreo">{{ productoPropiedad.entidadSeleccionada.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
		      
	      
	    </form>
    </div>
  
    <div class="col-sm-12 label-form" align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      <shiro:hasPermission name="22020">
        	<label class="btn btn-success" ng-click="form.$valid ? productoPropiedad.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
      	</shiro:hasPermission>
        <label class="btn btn-primary" ng-click="productoPropiedad.cancelar()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
      </div>
    </div>
  </div>

</div>