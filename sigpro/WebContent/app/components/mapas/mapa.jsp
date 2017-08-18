<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style type="text/css">
	
		.divMapa{
			height: calc(80% - 50px);
			width: 100%;
		}
		
		
	</style>	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="mapaController as mapac" class="maincontainer all_page" id="title">
		
	
	    
	    <script type="text/ng-template" id="modalInfo.html">
        	<div class="modal-header">
            	<h3 class="modal-title" id="modal-title">Datos de {{infoc.objeto.nombreOjetoTipo}}</h3>
        	</div>
        	<div class="modal-body" id="modal-body">

				
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="id">Id</label>
					</div>
					<div class="col-sm-6">
						 {{infoc.objeto.id }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Nombre</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.objeto.nombre }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Usuario que creo</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.objeto.usuarioCreo }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Fecha de creación</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.objeto.fechaCreacion }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Usuario que actualizo</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.objeto.usuarioactualizo }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Fecha de actualizacion</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.objeto.fechaactualizacion }}
					</div>
				</div>
				

				</div>

        	</div>
        	<div class="modal-footer">
            	<button class="btn btn-primary" type="button" ng-click="infoc.ok()">Aceptar</button>
        	</div>
    </script>
    
    <div class="panel panel-default">
	  <div class="panel-heading"><h3>Mapa</h3></div>
	</div>
	    
		<div class="row" style="width: 100%;">
					<div class="form-group col-sm-4" align="left">
						<select  class="inputText" ng-model="mapac.prestamo"
							ng-options="a.text for a in mapac.prestamos"
							ng-change="mapac.cargar()"></select>
					</div>
		</div>
		<div class="row">
	    <div class="operation_buttons" align="right" style="width: 97%">
	    	 <div class="checkbox" ng-hide="!mapac.mostrar">
			    <label>
			      <input type="checkbox" ng-model="mostrarTodo" ng-click="mostrar(0)">
			      Todos
			    </label>
			    <label>
			      <input type="checkbox" ng-model="mostrarProyectos" ng-click="mostrar(1)">
			      Proyecto
			    </label>
			    <label>
			      <input type="checkbox" ng-model="mostrarComponentes" ng-click="mostrar(2)">
			      Componentes
			    </label>
			    <label>
			      <input type="checkbox" ng-model="mostrarProductos" ng-click="mostrar(3)">
			      Productos
			    </label>
			    <label>
			      <input type="checkbox" ng-model="mostrarSubproductos" ng-click="mostrar(4)">
			      Subproductos
			    </label>
			    <label>
			      <input type="checkbox" ng-model="mostrarActividades" ng-click="mostrar(5)">
			      Actividades
			    </label>
			  </div>
	    	 
	    </div>
	    </div>
        <div class=" modal-body row divMapa"  >
        	  <div class=" divMapa" >
	        	<ui-gmap-google-map id="mainmap"  center="map.center" zoom="map.zoom" options="map.options" events="map.events"
	        	ng-if="mapac.mostrar"  >
	      			
					<div ng-repeat="marca in marcas track by marca.id">
						<div ng-switch on ="marca.objetoTipoId">
							<div ng-switch-when="1">
								<ui-gmap-marker ng-if="mostrarTodo || mostrarProyectos" 
									 idkey="marca.id" coords="marca.posicion" icon = "marca.icon"
									 click="abrirInformacion(marca.objetoId,marca.objetoTipoId)" 
									 options="{title:marca.nombre}"  
									 >
								 </ui-gmap-marker>
							 </div>
							 <div ng-switch-when="2">
								<ui-gmap-marker ng-if="mostrarTodo || mostrarComponentes" 
									 idkey="marca.id" coords="marca.posicion" icon = "marca.icon"
									 click="abrirInformacion(marca.objetoId,marca.objetoTipoId)" 
									 options="{title:marca.nombre}"  
									 >
								 </ui-gmap-marker>
							 </div>
							 <div ng-switch-when="3">
								<ui-gmap-marker ng-if="mostrarTodo || mostrarProductos" 
									 idkey="marca.id" coords="marca.posicion" icon = "marca.icon"
									 click="abrirInformacion(marca.objetoId,marca.objetoTipoId)" 
									 options="{title:marca.nombre}"  
									 >
								 </ui-gmap-marker>
							 </div>
							 <div ng-switch-when="4">
								<ui-gmap-marker ng-if="mostrarTodo || mostrarSubproductos" 
									 idkey="marca.id" coords="marca.posicion" icon = "marca.icon"
									 click="abrirInformacion(marca.objetoId,marca.objetoTipoId)" 
									 options="{title:marca.nombre}"  
									 >
								 </ui-gmap-marker>
							 </div>
							 <div ng-switch-when="5">
								<ui-gmap-marker ng-if="mostrarTodo || mostrarActividades" 
									 idkey="marca.id" coords="marca.posicion" icon = "marca.icon"
									 click="abrirInformacion(marca.objetoId,marca.objetoTipoId)" 
									 options="{title:marca.nombre}"  
									 >
								 </ui-gmap-marker>
							 </div>
						</div>
					</div>
				</ui-gmap-google-map>
			</div>
		</div>
		<br>
	</div>
