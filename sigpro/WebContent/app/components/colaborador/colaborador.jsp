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
  <shiro:lacksPermission name="4010">
	<p ng-init="colaborador.redireccionSinPermisos()"></p>
  </shiro:lacksPermission>
  
  <div class="panel panel-default">
		    <div class="panel-heading"><h3>Colaboradores</h3></div>
  </div>


  <div class="row" align="center" ng-hide="colaborador.esForma">	
    <div class="col-sm-12 operation_buttons" align="right">
	  <div class="btn-group">
	  <shiro:hasPermission name="4040">
	    <label class="btn btn-primary" ng-click="colaborador.nuevo()" uib-tooltip="Nuevo">
	    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
	  </shiro:hasPermission>
	  <shiro:hasPermission name="4010">
	    <label class="btn btn-primary" ng-click="colaborador.editar()" uib-tooltip="Editar">
	    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
	  </shiro:hasPermission>
	  <shiro:hasPermission name="4030">
	    <label class="btn btn-danger" ng-click="colaborador.borrar()" uib-tooltip="Borrar">
	    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
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
      <br>
      <div class="total-rows">
		  Total de {{  colaborador.totalElementos + (colaborador.totalElementos == 1 ? " Colaborador" : " Colaboradores" ) }}
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

  <div class="second-main-form row" ng-show="colaborador.esForma">

    <div class="page-header">
		<h2 ng-hide="!colaborador.esNuevo"><small>Nuevo Colaborador</small></h2>
		<h2 ng-hide="colaborador.esNuevo"><small>Edición de Colaborador</small></h2>
	</div>
    
    <div class="operation_buttons" align="right">
	  <div class="btn-group">
	    <shiro:hasPermission name="4020">
	      <label class="btn btn-success" ng-click="form.$valid ? colaborador.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
	      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
	    </shiro:hasPermission>
	    <label class="btn btn-primary" ng-click="colaborador.irATabla()" title="Ir a Tabla">
	    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	  </div>
	</div>
	<div class="col-sm-12">
	    <form name="form" class="css-form">
		     	<div class="form-group">
				   <input type="text" name="nombre"  class="inputText" id="nombre" 
				     ng-model="colaborador.colaborador.primerNombre" ng-value="colaborador.colaborador.primerNombre"   
				     onblur="this.setAttribute('value', this.value);" ng-required="true">
				   <label class="floating-label">* Primer Nombre</label>
				</div>
		
		      
		      	<div class="form-group">
				   <input type="text" name="segundonombre"  class="inputText" id="segundonombre"	 
				     ng-model="colaborador.colaborador.segundoNombre" ng-value="colaborador.colaborador.segundoNombre"   
				     onblur="this.setAttribute('value', this.value);" ng-required="false" >
				   <label class="floating-label">Segundo Nombre</label>
				</div>
		      	<div class="form-group">
				   <input type="text" name="primerapellido"  class="inputText" id="primerApellido" 
				     ng-model="colaborador.colaborador.primerApellido" ng-value="colaborador.colaborador.primerApellido"   
				     onblur="this.setAttribute('value', this.value);" ng-required="true" >
				   <label class="floating-label">* Primer Apellido</label>
				</div>
		      	<div class="form-group">
				   <input type="text" name="inombre"  class="inputText" id="inombre" 
				     ng-model="colaborador.colaborador.segundoApellido" ng-value="colaborador.colaborador.segundoApellido"   
				     onblur="this.setAttribute('value', this.value);" ng-required="false" >
				   <label class="floating-label">Segundo Apellido</label>
				</div>
		      	<div class="form-group">
				   <input type="number" name="CUI"  class="inputText" id="CUI" 
				     ng-model="colaborador.colaborador.cui" ng-value="colaborador.colaborador.cui"  ng-maxlength="13"
				     onblur="this.setAttribute('value', this.value);" ng-required="true" >
				   <label class="floating-label">* CUI</label>
				</div>
			  	<div class="form-group" >
				    <input type="text" class="inputText" id="unidadEjecutora" name="unidadEjecutora" ng-model="colaborador.colaborador.unidadejecutoranombre" ng-value="colaborador.colaborador.unidadejecutoranombre" 
						            		ng-click="colaborador.esNuevo? colaborador.buscarUnidadEjecutora(): ''" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
					<span class="label-icon" ng-click="colaborador.esNuevo? colaborador.buscarUnidadEjecutora(): ''"><i class="glyphicon glyphicon-search"></i></span>
					<label for="campo3" class="floating-label">* Nombre Unidad Ejecutora</label>
				</div>
		      	<div class="form-group" ng-hide="colaborador.esNuevo">
				    <input type="text" class="inputText" id="iproyt" name="iproyt" ng-model="colaborador.colaborador.usuario" ng-value="colaborador.colaborador.usuario" 
						            		 onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="false"/>
					<label for="campo3" class="floating-label">Usuario</label>
				</div>
	     
	      <br/>
		  <div class="panel panel-default">
			<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group" style="text-align: right">
							<label class="label-form"for="usuarioCreo">Usuario que creo</label>
									<p>{{ colaborador.colaborador.usuarioCreo }}</p>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label class="label-form" for="fechaCreacion">Fecha de creación</label>
			 						<p>{{ colaborador.colaborador.fechaCreacion }}</p>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group" style="text-align: right">
							<label class="label-form" for="usuarioActualizo">Usuario que actualizo</label>
			 						<p>{{ colaborador.colaborador.usuarioActualizo }}</p>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label class="label-form" for="fechaActualizacion">Fecha de actualizacion</label>
			 						<p>{{ colaborador.colaborador.fechaActualizacion }}</p>
						</div>
					</div>
				</div>
			 </div>
			</div>
	    </form>
    </div>
   <div class="operation_buttons" align="right">
    <div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
    <div class="col-sm-12 operation_buttons" align="right">
	  <div class="btn-group">
	    <shiro:hasPermission name="4020">
	      <label class="btn btn-success" ng-click="form.$valid ? colaborador.guardar() : ''" ng-disabled="!form.$valid" title="Guardar">
	      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
	    </shiro:hasPermission>
	    <label class="btn btn-primary" ng-click="colaborador.cancelar()" title="Ir a Tabla">
	    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	  </div>
	  </div>
	</div>
  </div>
</div>