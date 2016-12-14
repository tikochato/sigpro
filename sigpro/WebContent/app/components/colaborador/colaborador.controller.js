/**
 * 
 */
var moduloColaborador = angular.module('moduloColaborador', [ 'ngTouch' ]);

moduloColaborador.controller('controlColaborador', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log',
		controlColaborador ]);

function controlColaborador($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log) {
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

	$http.post('/SColaborador', {
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
		$http.post('/SColaborador', datos).then(function(response) {
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
		columnDefs : [ {
			displayName : 'Id',
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			visible : false
		}, {
			displayName : 'Primer Nombre',
			name : 'primerNombre',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'Segundo Nombre',
			name : 'segundoNombre',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'Primer Apellido',
			name : 'primerApellido',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'Segundo Apellido',
			name : 'segundoApellido',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'CUI',
			name : 'cui',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150
		}, {
			displayName : 'Unidad Ejecutora',
			name : 'unidadEjecutora',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			visible : false
		}, {
			displayName : 'Nombre Unidad Ejecutora',
			name : 'nombreUnidadEjecutora',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'Usuario',
			name : 'usuario',
			cellClass : 'grid-align-left',
			visible : false
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
					grid : 'colaborador',
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
			grid : 'colaborador',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/colaborador/rv')
			$route.reload();
		else
			$location.path('/colaborador/rv');
	}

	mi.nuevo = function() {
		mi.limpiarSeleccion();

		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;

		mi.codigo = "";
		mi.primerNombre = "";
		mi.segundoNombre = "";
		mi.primerApellido = "";
		mi.segundoApellido = "";
		mi.cui = "";
		mi.unidadEjecutora = "";
		mi.nombreUnidadEjecutora = "";
		mi.usuario = "";

		mi.usuarioValido = false;
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

			mi.codigo = mi.entidadSeleccionada.id;
			mi.primerNombre = mi.entidadSeleccionada.primerNombre;
			mi.segundoNombre = mi.entidadSeleccionada.segundoNombre;
			mi.primerApellido = mi.entidadSeleccionada.primerApellido;
			mi.segundoApellido = mi.entidadSeleccionada.segundoApellido;
			mi.cui = mi.entidadSeleccionada.cui;
			mi.unidadEjecutora = mi.entidadSeleccionada.unidadEjecutora;
			mi.nombreUnidadEjecutora = mi.entidadSeleccionada.nombreUnidadEjecutora
			mi.usuario = mi.entidadSeleccionada.usuario;

			mi.validarUsuario();

		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un COLABORADOR');
		}

	};

	mi.guardar = function() {
		if (!$utilidades.esNumero(mi.cui)
				|| $utilidades.esCadenaVacia(mi.primerNombre)
				|| $utilidades.esCadenaVacia(mi.primerApellido)
				|| $utilidades.esCadenaVacia(mi.nombreUnidadEjecutora)
				|| $utilidades.esCadenaVacia(mi.usuario)) {
			$utilidades.mensaje('danger',
					'Debe de llenar todos los campos obligatorios');
			return;
		}

		if (mi.esNuevo) {
			var datos = {
				accion : 'crear',
				primerNombre : mi.primerNombre,
				segundoNombre : mi.segundoNombre,
				primerApellido : mi.primerApellido,
				segundoApellido : mi.segundoApellido,
				cui : mi.cui,
				unidadEjecutora : mi.unidadEjecutora,
				usuario : mi.usuario
			};

			$http.post('/SColaborador', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.unidadesEjecutoras;
							mi.opcionesGrid.data = mi.data;
							mi.esForma = false;

							$utilidades.mensaje('success',
									'Colaborador guardado con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Colaborador ya existe...!!!');
						}

					});
		} else {
			var datos = {
				accion : 'actualizar',
				codigo : mi.codigo,
				primerNombre : mi.primerNombre,
				segundoNombre : mi.segundoNombre,
				primerApellido : mi.primerApellido,
				segundoApellido : mi.segundoApellido,
				cui : mi.cui,
				unidadEjecutora : mi.unidadEjecutora,
				usuario : mi.usuario
			};

			$log.info(datos);

			$http.post('/SColaborador', datos).then(
					function(response) {

						if (response.data.success) {
							mi.data = response.data.unidadesEjecutoras;
							mi.opcionesGrid.data = mi.data;
							mi.esForma = false;

							$utilidades.mensaje('success',
									'Colaborador actualizado con exito.');
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

	mi.buscarUnidadEjecutora = function(titulo, mensaje) {

		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarUnidadEjecutora.jsp',
			controller : 'modalBuscarUnidadEjecutora',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md'
		});

		modalInstance.result.then(function(selectedItem) {
			mi.unidadEjecutora = selectedItem.unidadEjecutora;
			mi.nombreUnidadEjecutora = selectedItem.nombreUnidadEjecutora;

		}, function() {
		});
	};

	mi.usuarioValido = false;

	mi.validarUsuario = function() {
		$http.post('/SColaborador', {
			accion : 'validarUsuario',
			usuario : mi.usuario
		}).then(function(response) {
			mi.usuarioValido = response.data.success;
		});
	}

	mi.usuarioCambio = function() {
		mi.usuarioValido = false;
	}

}

moduloColaborador.controller('modalBuscarUnidadEjecutora', [
		'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
		'Utilidades', '$timeout', '$log', modalBuscarUnidadEjecutora ]);

function modalBuscarUnidadEjecutora($uibModalInstance, $scope, $http,
		$interval, i18nService, $utilidades, $timeout, $log) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post('/SUnidadEjecutora', {
		accion : 'totalEntidades'
	}).success(function(response) {
		mi.totalElementos = response.total;
		mi.elementosPorPagina = mi.totalElementos;
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
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
		$http.post('/SUnidadEjecutora', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.unidadesEjecutoras;
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
			$utilidades.mensaje('warning',
					'Debe seleccionar una UNIDAD EJECUTORA');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}
