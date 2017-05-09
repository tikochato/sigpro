/**
 * 
 */
var moduloUnidadEjecutora = angular.module('moduloUnidadEjecutora',
		[ 'ngTouch' ]);

moduloUnidadEjecutora.controller('controlUnidadEjecutora', [ '$scope',
		'$routeParams', '$route', '$window', '$location', '$mdDialog',
		'$uibModal', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', controlUnidadEjecutora ]);

function controlUnidadEjecutora($scope, $routeParams, $route, $window,
		$location, $mdDialog, $uibModal, $http, $interval, i18nService,
		$utilidades, $timeout, $log) {
	i18nService.setCurrentLang('es');
	var mi = this;
	
	$window.document.title = $utilidades.sistema_nombre+' - Unidad Ejecutora';

	mi.esForma = false;
	mi.esNuevo = false;
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

	mi.mostrarCargando = true;
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
	
	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.opcionesGrid.data[filaId]);
        mi.editar();
    };
    
	mi.opcionesGrid = {
		data : mi.data,
		rowTemplate: '<div ng-dblclick="grid.appScope.unidad.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [ {
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
		} ],
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

			if ($routeParams.reiniciar_vista == 'rv') {
				mi.guardarEstado();
			} else {
				$http.post('/SEstadoTabla', {
					action : 'getEstado',
					grid : 'unidadEjecutora',
					t : (new Date()).getTime()
				}).then(
						function(response) {

							if (response.data.success
									&& response.data.estado != '') {
								mi.gridApi.saveState.restore($scope,
										response.data.estado);
							}

							mi.gridApi.colMovable.on.columnPositionChanged(
									$scope, mi.guardarEstado);
							mi.gridApi.colResizable.on.columnSizeChanged(
									$scope, mi.guardarEstado);
							mi.gridApi.core.on.columnVisibilityChanged($scope,
									mi.guardarEstado);
							mi.gridApi.core.on.sortChanged($scope,
									mi.guardarEstado);
						});
			}
		}
	}

	mi.guardarEstado = function() {
		var estado = mi.gridApi.saveState.save();

		var tabla_data = {
			action : 'guardaEstado',
			grid : 'unidadEjecutora',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/unidadejecutora/rv')
			$route.reload();
		else
			$location.path('/unidadejecutora/rv');
	}
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
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
			$utilidades.mensaje('warning',
					'Debe seleccionar una UNIDAD EJECUTORA');
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

moduloUnidadEjecutora.controller('modalBuscarEntidad', [ '$uibModalInstance',
		'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', 'titulo', 'mensaje', modalBuscarEntidad ]);

function modalBuscarEntidad($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log, titulo, mensaje) {
	
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
		columnDefs : [ {
			displayName : 'Entidad',
			name : 'entidad',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150
		}, {
			displayName : 'Nombre Entidad',
			name : 'nombre',
			cellClass : 'grid-align-left'
		} ],
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
