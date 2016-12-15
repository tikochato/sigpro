<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div ng-controller="controlUnidadEjecutora as unidad" class="maincontainer all_page">

  <script type="text/ng-template" id="buscarEntidad.jsp">
    <%@ include file="/app/components/unidadejecutora/buscarEntidad.jsp"%>
  </script>

  <h3>{{ unidad.esForma ? (unidad.esNuevo ? "Nueva Unidad Ejecutora" : "Editar Unidad Ejecutora") : "Unidad Ejecutora" }}</h3>

  <br />

  <div class="row" align="center" ng-hide="unidad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-primary" ng-click="unidad.nuevo()">Nueva</label> <label class="btn btn-primary" ng-click="unidad.editar()">Editar</label>
      </div>
    </div>
    <div class="col-sm-12" align="center">
      <div style="height: 35px;">
		<div style="text-align: right;">
			<div class="btn-group" role="group" aria-label="">
				<a class="btn btn-default" href ng-click="unidad.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
			</div>
		</div>
	  </div>
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
  </div>

  <div class="row" ng-show="unidad.esForma">

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid ? unidad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
        <label class="btn btn-danger" ng-click="unidad.cancelar()">Cancelar</label>
      </div>

    </div>

    <div>

      <form name="form" class="css-form" novalidate>

		<div class="row">
	        <div class="form-group col-sm-2" ng-class="{ 'has-error' : form.campo1.$invalid }">
	          <label for="campo1">* Unidad Ejecutora:</label> 
	          <input type="number" class="form-control" id="campo1" name="campo1" placeholder="Unidad Ejecutora" ng-model="unidad.codigo" ng-readonly="!unidad.esNuevo" required />
	        </div>
        </div>

		<div class="row">
	        <div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo2.$invalid }">
	          <label for="campo2">* Nombre Unidad Ejecutora:</label> 
	          <input type="text" class="form-control" id="campo2" name="campo2" placeholder="Nombre Unidad Ejecutora" ng-model="unidad.nombre" required />
	        </div>
	
	        <div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo3.$invalid }">
	          <label for="campo3">* Entidad:</label>
	          <div class="input-group">
	            <input type="hidden" class="form-control" ng-model="unidad.entidad" /> 
	            <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Nombre Entidad" ng-model="unidad.nombreEntidad" ng-readonly="true" required/>
	            <span class="input-group-addon" ng-click="unidad.buscarEntidad()"><i class="glyphicon glyphicon-search"></i></span>
	          </div>
	        </div>
		</div>
      </form>

    </div>
    <div align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-success" ng-click="form.$valid ? unidad.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
        <label class="btn btn-danger" ng-click="unidad.cancelar()">Cancelar</label>
      </div>
    </div>
  </div>

</div>