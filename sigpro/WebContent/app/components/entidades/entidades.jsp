<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div ng-controller="controlEntidad as entidad" class="maincontainer all_page">

  <h3>{{ entidad.esForma ? (entidad.esNuevo ? "Nueva Entidad" : "Editar Entidad") : "Entidades" }}</h3>

  <br />

  <div class="row" align="center" ng-hide="entidad.esForma">
    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-primary" ng-click="entidad.nuevo()">Nueva</label> 
        <label class="btn btn-primary" ng-click="entidad.editar()">Editar</label>
      </div>
    </div>
    <div class="col-sm-12" align="center">
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
  </div>

  <div class="row" ng-show="entidad.esForma">

    <div class="col-sm-12 operation_buttons" align="right">

      <div class="btn-group">
        <label class="btn btn-success" ng-click="entidad.guardar()">Guardar</label> 
        <label class="btn btn-danger" ng-click="entidad.cancelar()">Cancelar</label>
      </div>

    </div>

    <div class="col-sm-12">

      <form class="css-form" novalidate>

        <div class="form-group">
          <label for="campo1">* Entidad</label> 
          <input type="number" class="form-control" id="campo1" placeholder="entidad" ng-model="entidad.entidad" ng-readonly="!entidad.esNuevo" style="width: 200px;" required />
        </div>

        <div class="form-group">
          <label for="campo2">* Nombre Entidad</label> 
          <input type="text" class="form-control" id="campo2" placeholder="nombre entidad" ng-model="entidad.nombre" ng-readonly="!entidad.esNuevo" required />
        </div>

        <div class="form-group">
          <label for="campo3">Abreviatura</label> 
          <input type="text" class="form-control" id="campo3" placeholder="abreviatura" ng-model="entidad.abreviatura" style="width: 200px;">
        </div>

      </form>

    </div>
    <div align="center">Los campos marcados con * son obligatorios</div>

    <div class="col-sm-12 operation_buttons" align="right">
      <div class="btn-group">
        <label class="btn btn-success" ng-click="entidad.guardar()">Guardar</label> 
        <label class="btn btn-danger" ng-click="entidad.cancelar()">Cancelar</label>
      </div>
    </div>
  </div>

</div>