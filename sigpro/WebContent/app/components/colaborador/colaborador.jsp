<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div ng-controller="controlColaborador as colaborador" class="maincontainer all_page">

  <script type="text/ng-template" id="buscarUnidadEjecutora.jsp">
    <%@ include file="/app/components/colaborador/buscarUnidadEjecutora.jsp"%>
  </script>
   <script type="text/ng-template" id="buscarUsuario.jsp">
    <%@ include file="/app/components/colaborador/buscarUsuario.jsp"%>
  </script>

  <h3>{{ colaborador.esForma ? (colaborador.esNuevo ? "Nuevo Colaborador" : "Editar Colaborador") : "Colaborador" }}</h3>

  <br />

  <div align="center" ng-hide="colaborador.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
      	<shiro:hasPermission name="4040">
        	<label class="btn btn-primary" ng-click="colaborador.nuevo()">Nuevo</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="4010">
        	<label class="btn btn-primary" ng-click="colaborador.editar()">Editar</label>
      	</shiro:hasPermission>
      	<shiro:hasPermission name="4030">
      			<label class="btn btn-primary" ng-click="colaborador.borrar()">Borrar</label>
      	</shiro:hasPermission>
      </div>
    </div>
    <shiro:hasPermission name="4010">
     <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="colaborador.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
	  <br/>
      <div id="grid1" ui-grid="colaborador.opcionesGrid" ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!colaborador.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <ul uib-pagination 
      	total-items="colaborador.totalElementos" 
      	ng-model="colaborador.paginaActual" 
      	max-size="colaborador.numeroMaximoPaginas" 
      	items-per-page="colaborador.elementosPorPagina" 
      	first-text="Primero"
        last-text="Último" 
        next-text="Siguiente" 
        previous-text="Anterior" 
        class="pagination-sm" 
        boundary-links="true" 
        force-ellipses="true" 
        ng-change="colaborador.cambioPagina()"
      ></ul>
    </div>
    </shiro:hasPermission>
   
  </div>

  <div class="main-form" ng-show="colaborador.esForma">

    <div class="operation_buttons" align="right">

      <div class="btn-group">
      	<shiro:hasPermission name="4020">
      	<label class="btn btn-success" ng-click="form.$valid && colaborador.usuarioValido ? colaborador.guardar() : ''" ng-disabled="!form.$valid || !colaborador.usuarioValido">Guardar</label>
      	</shiro:hasPermission>         
        <label class="btn btn-primary" ng-click="colaborador.cancelar()">Ir a Tabla</label>
      </div>

    </div>
    
    <div>
	    <form name="form" class="css-form">
	      
		      <div class="form-group" >
		        <label>* Primer Nombre</label> 
		        <input type="text" class="form-control" placeholder="Primer Nombre" ng-model="colaborador.colaborador.primerNombre" ng-required="true" />
		      </div>
		
		      <div class="form-group">
		        <label>Segundo Nombre</label> 
		        <input type="text" class="form-control" placeholder="Segundo Nombre" ng-model="colaborador.colaborador.segundoNombre" />
		      </div>
		
		      <div class="form-group" >
		        <label>* Primer Apellido</label> 
		        <input type="text" class="form-control" placeholder="Primer Apellido" ng-model="colaborador.colaborador.primerApellido" ng-required="true" />
		      </div>
		
		      <div class="form-group" >
		        <label>Segundo Apellido</label> 
		        <input type="text" class="form-control" placeholder="Segundo Apellido" ng-model="colaborador.colaborador.segundoApellido" />
		      </div>
	      
	
	      
		      <div class="form-group" >
		        <label>* CUI</label> 
		        <input type="number" class="form-control"  placeholder="CUI" ng-model="colaborador.colaborador.cui" ng-maxlength="13" ng-required="true" />
		      </div>
	      
	      
	      
		      <div class="form-group" >
				  <label>* Nombre Unidad Ejecutora</label> 
				  <div class="input-group">
				    <input type="text" class="form-control" placeholder="Nombre Unidad Ejecutora" ng-model="colaborador.colaborador.nombreUnidadEjecutora" ng-disabled="true"  ng-required="true"/>
				    <span class="input-group-addon" ng-click="colaborador.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
				  </div>
			  </div>
	      
	
	      
		      <div class="form-group" >
		        <label for="campo6">* Usuario</label> 
		        <div class="input-group">
		          <input type="text" class="form-control" placeholder="Usuario" ng-model="colaborador.colaborador.usuario"  ng-disabled="true" ng-required="true"/>
		          <span class="input-group-addon" ng-click="colaborador.buscarUsuario()" uib-tooltip="Validar Usuario" ><i class="glyphicon glyphicon-search"></i></span>
		        </div>
		      </div>
	     
	      <br/>
		  <div class="panel panel-default">
			<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group" style="text-align: right">
							<label for="usuarioCreo">Usuario que creo</label>
									<p class="form-control-static">{{ colaborador.colaborador.usuarioCreo }}</p>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="fechaCreacion">Fecha de creación</label>
			 						<p class="form-control-static">{{ colaborador.colaborador.fechaCreacion }}</p>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group" style="text-align: right">
							<label for="usuarioActualizo">Usuario que actualizo</label>
			 						<p class="form-control-static">{{ colaborador.colaborador.usuarioActualizo }}</p>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label for="fechaActualizacion">Fecha de actualizacion</label>
			 						<p class="form-control-static">{{ colaborador.colaborador.fechaActualizacion }}</p>
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
      	<shiro:hasPermission name="4020">
      		
        <label class="btn btn-success" ng-click="form.$valid && colaborador.usuarioValido ? colaborador.guardar() : '' " ng-disabled="form.$invalid || !colaborador.usuarioValido">Guardar</label>
      	</shiro:hasPermission> 
        <label class="btn btn-primary" ng-click="colaborador.cancelar()">Ir a Tabla</label>
      </div>
    </div>
  </div>
</div>