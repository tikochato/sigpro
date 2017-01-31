<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-12">
      <div id="grid1" ui-grid="modalBuscarUnidadMedida.opcionesGrid" ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination>
        <div class="grid_loading" ng-hide="!modalBuscarUnidadMedida.mostrarCargando">
          <div class="msg">
            <span><i class="fa fa-spinner fa-spin fa-4x"></i> <br /> <br /> <b>Cargando, por favor espere...</b> </span>
          </div>
        </div>
      </div>
      <div align="center" ng-show="modalBuscarUnidadMedida.totalElementos > modalBuscarUnidadMedida.elementosPorPagina">
        <ul uib-pagination total-items="modalBuscarUnidadMedida.totalElementos" ng-model="modalBuscarUnidadMedida.paginaActual" max-size="modalBuscarUnidadMedida.numeroMaximoPaginas" items-per-page="modalBuscarUnidadMedida.elementosPorPagina"
          first-text="Primero" last-text="Último" next-text="Siguiente" previous-text="Anterior" class="pagination-sm" boundary-links="true" force-ellipses="true"
          ng-change="modalBuscarUnidadMedida.cambioPagina()"
        ></ul>
      </div>
    </div>
    </div>
    <br/>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="modalBuscarUnidadMedida.ok()"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="modalBuscarUnidadMedida.cancel()">Cancelar</label>
	    	</div>
	      
	    </div>
  </div>
</div>