<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-10">
      <div id="grid1" ui-grid="modalBuscar.opcionesGrid" ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!modalBuscar.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <div align="center" ng-show="modalBuscar.totalElementos > modalBuscar.elementosPorPagina">
        <ul uib-pagination total-items="modalBuscar.totalElementos" ng-model="modalBuscar.paginaActual" max-size="modalBuscar.numeroMaximoPaginas" items-per-page="modalBuscar.elementosPorPagina"
          first-text="Primero" last-text="Último" next-text="Siguiente" previous-text="Anterior" class="pagination-sm" boundary-links="true" force-ellipses="true"
          ng-change="modalBuscar.cambioPagina()"
        ></ul>
      </div>
    </div>
    <div class="col-sm-2">
      <div class="row">
        <button class="btn btn-primary" type="button" ng-click="modalBuscar.ok()" style="width: 85%">Ok</button>
      </div>
      <div class="row">
        <button class="btn btn-primary" type="button" ng-click="modalBuscar.cancel()" style="width: 85%">Cancelar</button>
      </div>
    </div>
  </div>
</div>