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
      	<shiro:hasPermission name="crearColaborador">
        	<label class="btn btn-primary" ng-click="colaborador.nuevo()">Nuevo</label> 
      	</shiro:hasPermission>
      	<shiro:hasPermission name="editarColaborador">
        	<label class="btn btn-primary" ng-click="colaborador.editar()">Editar</label>
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

  <div ng-show="colaborador.esForma">

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid && colaborador.usuarioValido ? colaborador.guardar() : ''" ng-disabled="!form.$valid || !colaborador.usuarioValido">Guardar</label> 
        <label class="btn btn-primary" ng-click="colaborador.cancelar()">Ir a Tabla</label>
      </div>

    </div>
    
    <div>
	    <form name="form" class="css-form">
	
	      <div class="row">
		      <div class="form-group col-sm-12" ng-show="!colaborador.esNuevo">
		        <label for="campo0">ID:</label> 
		      	<p class="form-control-static" id="campo0" name="campo0">{{colaborador.codigo}} </p>
		      </div>
	      </div>

	      <div class="row">
		      <div class="form-group col-sm-3" >
		        <label for="campo1">* Primer Nombre:</label> 
		        <input type="text" class="form-control" id="campo1" name="campo1" placeholder="Primer Nombre" ng-model="colaborador.primerNombre" ng-required="true" />
		      </div>
		
		      <div class="form-group col-sm-3">
		        <label for="campo2">Segundo Nombre:</label> 
		        <input type="text" class="form-control" id="campo2" name="campo2" placeholder="Segundo Nombre" ng-model="colaborador.segundoNombre" />
		      </div>
		
		      <div class="form-group col-sm-3" >
		        <label for="campo3">* Primer Apellido:</label> 
		        <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Primer Apellido" ng-model="colaborador.primerApellido" ng-required="true" />
		      </div>
		
		      <div class="form-group col-sm-3" >
		        <label for="campo4">Segundo Apellido:</label> 
		        <input type="text" class="form-control" id="campo4" name="campo4" placeholder="Segundo Apellido" ng-model="colaborador.segundoApellido" />
		      </div>
	      </div>
	
	      <div class="row">
		      <div class="form-group col-sm-12" >
		        <label for="campo5">* CUI:</label> 
		        <input type="number" id="campo5" name="campo5" class="form-control"  placeholder="CUI" ng-model="colaborador.cui" ng-maxlength="13" ng-required="true" />
		      </div>
	      </div>
	      
	      <div class="row">
		      <div class="form-group col-sm-12" >
				  <label for="campo6">* Nombre Unidad Ejecutora:</label> 
				  <div class="input-group">
				    <input type="hidden" class="form-control" ng-model="colaborador.unidadEjecutora" /> 
				    <input type="text" id="campo6" name="campo6" class="form-control" placeholder="Nombre Unidad Ejecutora" ng-model="colaborador.nombreUnidadEjecutora" ng-disabled="true"  ng-required="true"/>
				    <span class="input-group-addon" ng-click="colaborador.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
				  </div>
			  </div>
	      </div>
	
	      <div class="row">
		      <div class="form-group col-sm-12" >
		        <label for="campo6">* Usuario:</label> 
		        <div class="input-group">
		          <input type="text" id="campo7" name="campo7" class="form-control" placeholder="Usuario" ng-model="colaborador.usuario"  ng-disabled="true" ng-required="true"/>
		          <span class="input-group-addon" ng-click="colaborador.buscarUsuario()" uib-tooltip="Validar Usuario" ><i class="glyphicon glyphicon-search"></i></span>
		        </div>
		      </div>
	      </div>
	    </form>
    </div>
  
    <div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid && colaborador.usuarioValido ? colaborador.guardar() : '' " ng-disabled="form.$invalid || !colaborador.usuarioValido">Guardar</label> 
        <label class="btn btn-primary" ng-click="colaborador.cancelar()">Ir a Tabla</label>
      </div>
    </div>
  </div>
</div>