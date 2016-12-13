/**
 * 
 */
var moduloUnidadEjecutora = angular.module('moduloUnidadEjecutora', [
    'ngTouch'
]);

moduloUnidadEjecutora.controller('controlUnidadEjecutora', [
	'$scope', '$routeParams', '$uibModal', '$http', '$interval',
	'i18nService', 'Utilidades', '$timeout', '$log', controlUnidadEjecutora
]);

function controlUnidadEjecutora($scope, $routeParams, $uibModal, $http, $interval, i18nService, $utilidades, $timeout, $log) {
    i18nService.setCurrentLang('es');
    var mi = this;

    mi.esForma = false;

    mi.totalElementos = 0;
    mi.paginaActual = 1;
    mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
    mi.elementosPorPagina = $utilidades.elementosPorPagina;

    mi.cambioPagina = function() {
	mi.cargarData(mi.paginaActual);
    }

    $http.post('/SUnidadEjecutora', {
	accion : 'totalElementos'
    }).success(function(response) {
	mi.totalElementos = response.total;
	mi.cargarData(1);
    });

    mi.mostrarCargando = false;
    mi.data = [];
    mi.cargarData = function(pagina) {
	var datos = {
	    accion : 'cargar',
	    pagina : pagina,
	    registros : mi.elementosPorPagina
	};

	mi.mostrarCargando = true;
	$http.post('/SUnidadEjecutora', datos).then(function(response) {
	    if (response.data.success) {

		mi.data = response.data.unidadesEjecutoras;
		mi.opcionesGrid.data = mi.data;

		mi.mostrarCargando = false;
	    }
	});

    };

    mi.entidadSeleccionada = -1;
    mi.seleccionada = false;

    mi.opcionesGrid = {
	data : mi.data,
	columnDefs : [
		{
		    displayName : 'Unidad Ejecutora',
		    name : 'unidadEjecutora',
		    cellClass : 'grid-align-right',
		    type : 'number',
		    width : 150
		}, {
		    displayName : 'Nombre Unidad',
		    name : 'nombreUnidadEjecutora',
		    cellClass : 'grid-align-left'
		}, {
		    displayName : 'Entidad',
		    name : 'entidad',
		    cellClass : 'grid-align-right',
		    type : 'number',
		    width : 150
		}, {
		    displayName : 'Nombre Entidad',
		    name : 'nombreEntidad',
		    cellClass : 'grid-align-left'
		}
	],
	enableRowSelection : true,
	enableRowHeaderSelection : false,
	multiSelect : false,
	modifierKeysToMultiSelect : false,
	noUnselect : false,
	enableFiltering : true,
	enablePaginationControls : false,
	paginationPageSize : $utilidades.elementosPorPagina,
	onRegisterApi : function(gridApi) {
	    mi.gridApi = gridApi;

	    mi.gridApi.selection.on.rowSelectionChanged($scope,
		    mi.seleccionarEntidad);

	    mi.gridApi.colMovable.on.columnPositionChanged($scope,
		    mi.guardarVista);
	    mi.gridApi.colResizable.on.columnSizeChanged($scope,
		    mi.guardarVista);
	    mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarVista);
	    mi.gridApi.core.on.filterChanged($scope, mi.guardarVista);
	    mi.gridApi.core.on.sortChanged($scope, mi.guardarVista);
	}
    }

    mi.guardarVista = function() {
	var state = mi.gridApi.saveState.save();
    };

    mi.restoreState = function() {
	$timeout(function() {
	    mi.state = mi.gridApi.saveState.save();
	    if (mi.state)
		mi.gridApi.saveState.restore($scope, mi.state);
	});
    }

    mi.guardarEstado = function() {
	var estado = mi.state;

	var datos = {
	    accion : 'guardar',
	    grid : 'entidades',
	    estado : JSON.stringify(estado),
	    tiempo : (new Date()).getTime()
	};
	$http.post('/SGuardarEstadoGrid', datos).then(function(response) {
	});
    }

    mi.nuevo = function() {
	mi.esForma = true;
	mi.entityselected = null;
	mi.esNuevo = true;

	mi.codigo = "";
	mi.nombre = "";
	mi.entidad = "";
	mi.nombreEntidad = "";

	mi.limpiarSeleccion();
    }

    mi.limpiarSeleccion = function() {
	mi.gridApi.selection.clearSelectedRows();
	mi.seleccionada = false;
    }

    mi.seleccionarEntidad = function(row) {
	mi.entidadSeleccionada = row.entity;
	mi.seleccionada = row.isSelected;
    };

    mi.editar = function() {
	if (mi.seleccionada) {
	    mi.limpiarSeleccion();

	    mi.esForma = true;
	    mi.entityselected = null;
	    mi.esNuevo = false;
	    
	    mi.codigo = mi.entidadSeleccionada.unidadEjecutora;
	    mi.nombre = mi.entidadSeleccionada.nombreUnidadEjecutora;
	    mi.entidad = mi.entidadSeleccionada.entidad;
	    mi.nombreEntidad = mi.entidadSeleccionada.nombreEntidad;
	} else {
	    $utilidades.mensaje('warning', 'Debe seleccionar una UNIDAD EJECUTORA');
	}

    };

    mi.guardar = function() {
	if (!$utilidades.esNumero(mi.codigo)
		|| !$utilidades.esNumero(mi.entidad)
		|| $utilidades.esCadenaVacia(mi.nombre)
		|| $utilidades.esCadenaVacia(mi.nombreEntidad)) {
	    $utilidades.mensaje('danger',
		    'Debe de llenar todos los campos obligatorios');
	    return;
	}

	if (mi.esNuevo) {
	    var datos = {
		accion : 'crear',
		codigo : mi.codigo,
		nombre : mi.nombre,
		entidad : mi.entidad
	    };

	    $http.post('/SUnidadEjecutora', datos).then(
		    function(response) {
			if (response.data.success) {
			    mi.data = response.data.unidadesEjecutoras;
			    mi.opcionesGrid.data = mi.data;
			    mi.esForma = false;

			    $utilidades.mensaje('success',
				    'Entidad guardada con exito.');
			} else {
			    $utilidades.mensaje('danger',
				    'Entidad ya existe...!!!');
			}

		    });
	} else {
	    var datos = {
		accion : 'actualizar',
		codigo : mi.codigo,
		nombre : mi.nombre,
		entidad : mi.entidad
	    };

	    $http.post('/SUnidadEjecutora', datos).then(
		    function(response) {
			
			if (response.data.success) {
			    mi.data = response.data.unidadesEjecutoras;
			    mi.opcionesGrid.data = mi.data;
			    mi.esForma = false;

			    $utilidades.mensaje('success',
				    'Entidad actualizada con exito.');
			} else {
			    $utilidades.mensaje('danger',
				    'Error al actualizar datos...!!!');
			}
		    });

	}
    };

    mi.cancelar = function() {
	mi.esForma = false;
    };

    mi.buscarEntidad = function(titulo, mensaje) {

	var modalInstance = $uibModal.open({
	    animation : 'true',
	    ariaLabelledBy : 'modal-title',
	    ariaDescribedBy : 'modal-body',
	    templateUrl : 'buscarEntidad.jsp',
	    controller : 'modalBuscarEntidad',
	    controllerAs : 'modalBuscar',
	    backdrop : 'static',
	    size : 'md',
	    resolve : {
		titulo : function() {
		    return titulo;
		},
		mensaje : function() {
		    return mensaje;
		}
	    }

	});

	modalInstance.result.then(function(selectedItem) {
	    mi.entidad = selectedItem.entidad;
	    mi.nombreEntidad = selectedItem.nombre;

	}, function() {
	});
    };

}

moduloUnidadEjecutora.controller('modalBuscarEntidad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log', modalBuscarEntidad
]);

function modalBuscarEntidad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log) {

    var mi = this;

    mi.totalElementos = 0;
    mi.paginaActual = 1;
    mi.numeroMaximoPaginas = 5;
    mi.elementosPorPagina = 9;

    mi.mostrarCargando = false;
    mi.data = [];

    mi.itemSeleccionado = null;
    mi.seleccionado = false;

    $http.post('/SEntidad', {
	accion : 'totalEntidades'
    }).success(function(response) {
	mi.totalElementos = response.total;
	mi.elementosPorPagina = mi.totalElementos;
	mi.cargarData(1);
    });

    mi.opcionesGrid = {
	data : mi.data,
	columnDefs : [
		{
		    displayName : 'Entidad',
		    name : 'entidad',
		    cellClass : 'grid-align-right',
		    type : 'number',
		    width : 150
		}, {
		    displayName : 'Nombre Entidad',
		    name : 'nombre',
		    cellClass : 'grid-align-left'
		}
	],
	enableRowSelection : true,
	enableRowHeaderSelection : false,
	multiSelect : false,
	modifierKeysToMultiSelect : false,
	noUnselect : false,
	enableFiltering : true,
	enablePaginationControls : false,
	paginationPageSize : 5,
	onRegisterApi : function(gridApi) {
	    mi.gridApi = gridApi;

	    mi.gridApi.selection.on.rowSelectionChanged($scope,
		    mi.seleccionarEntidad);
	}
    }

    mi.seleccionarEntidad = function(row) {
	mi.itemSeleccionado = row.entity;
	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
	var datos = {
	    accion : 'cargar',
	    pagina : pagina,
	    registros : mi.elementosPorPagina
	};

	mi.mostrarCargando = true;
	$http.post('/SEntidad', datos).then(function(response) {
	    if (response.data.success) {
		mi.data = response.data.entidades;
		mi.opcionesGrid.data = mi.data;
		mi.mostrarCargando = false;
	    }
	});
    };

    mi.cambioPagina = function() {
	mi.cargarData(mi.paginaActual);
    }

    mi.ok = function() {
	if (mi.seleccionado) {
	    $uibModalInstance.close(mi.itemSeleccionado);
	} else {
	    $utilidades.mensaje('warning', 'Debe seleccionar una ENTIDAD');
	}
    };

    mi.cancel = function() {
	$uibModalInstance.dismiss('cancel');
    };

}
