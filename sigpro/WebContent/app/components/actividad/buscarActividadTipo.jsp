<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-body" id="modal-body">
  <div class="row">
  	<div class="col-sm-12" style="font-weight: bold;">Asignación Matríz RACI</div>
  </div>
  <br/>
  <div class="row" ng-show="modalBuscar.showfilters">
  	<div class="col-sm-12">
  		<select class="inputText" ng-model="modalBuscar.ejercicio" ng-change="modalBuscar.cambioEjercicio()"
  		ng-options="ejercicio as ejercicio for ejercicio in modalBuscar.ejercicios">
	    </select><br>
    </div>
  </div>
  <div class="row" ng-if="modalBuscar.mostrarRoles">
    	<div class="col-sm-12">
			<div class="form-group">
					<select class="inputText" ng-model="modalBuscar.rolAsignado"
						ng-options="rol as rol.nombre for rol in modalBuscar.roles track by rol.id"
						ng-required="true">
						<option value="">Seleccione una opción</option>
					</select>
				    <label for="nombre" class="floating-label">* Rol</label>
			</div>
			</div>
	</div>
  <br/>
  <div class="row" ng-show="modalBuscar.showfilters">
  	<div class="col-sm-12">
	  	<div angucomplete-alt id="ex1"
			  placeholder="Buscar Entidades"
			  pause="100"
			  selected-object="modalBuscar.cambioEntidad"
			  local-data="modalBuscar.entidades"
			  search-fields="nombre"
			  title-field="nombre"
			  minlength="3"
			  input-class="form-control form-control-small"
			  match-class="angucomplete-highlight"
			  initial-value="modalBuscar.entidad.nombre">
		</div>
  	</div>
  </div>
  <br/>
  <div class="row">
    <div class="col-sm-12">
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
    </div>
    <br/>
    
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="modalBuscar.ok()"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="modalBuscar.cancel()">Cancelar</label>
	    	</div>
	      
	    </div>
  </div>
</div>