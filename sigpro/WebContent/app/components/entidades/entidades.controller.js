/**
 * 
 */
var moduloEntidad = angular.module('moduloEntidad', [ 'ngTouch' ]);

moduloEntidad.controller('controlEntidad', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log',
		controlEntidad ]);

function controlEntidad($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log) {
	i18nService.setCurrentLang('es');
	var mi = this;
	
	$window.document.title = 'SIGPRO - Entidad';

	mi.totalEntidades = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	$http.post('/SEntidad', {
		accion : 'totalEntidades'
	}).success(function(response) {
		mi.totalEntidades = response.total;
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
		$http.post('/SEntidad', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.entidades;
				mi.entidades_gridOptions.data = mi.data;

				mi.mostrarCargando = false;
			}
		});

	};

	mi.entidadSeleccionada = -1;
	mi.seleccionada = false;

	mi.entidades_gridOptions = {
		data : mi.data,
		columnDefs : [ {
			name : 'Entidad',
			field : 'entidad',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150
		}, {
			name : 'Nombre Entidad',
			field : 'nombre',
			cellClass : 'grid-align-left'
		}, {
			name : 'Siglas',
			field : 'abreviatura',
			width : 150
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
					grid : 'entidad',
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
			grid : 'entidad',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/entidad/rv')
			$route.reload();
		else
			$location.path('/entidad/rv');
	}

	mi.nuevo = function() {
		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;

		mi.entidad = "";
		mi.nombre = "";
		mi.abreviatura = "";

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

			mi.entidad = mi.entidadSeleccionada.entidad;
			mi.nombre = mi.entidadSeleccionada.nombre;
			mi.abreviatura = mi.entidadSeleccionada.abreviatura;
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una ENTIDAD');
		}

	};

	mi.guardar = function() {
		if (mi.esNuevo) {
			if (!$utilidades.esNumero(mi.entidad)
					|| $utilidades.esCadenaVacia(mi.nombre)) {
				$utilidades.mensaje('danger',
						'Debe de llenar todos los campos obligatorios');
				return;
			}

			var datos = {
				accion : 'crear',
				entidad : mi.entidad,
				nombre : mi.nombre,
				abreviatura : mi.abreviatura
			};

			$http.post('/SEntidad', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.entidades;
							mi.entidades_gridOptions.data = mi.data;
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
				entidad : mi.entidad,
				abreviatura : mi.abreviatura
			};

			$http.post('/SEntidad', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.entidades;
							mi.entidades_gridOptions.data = mi.data;
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

	mi.mostrarMensaje = function(titulo, mensaje) {

		$uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'mensajeEntidades.jsp',
			controller : 'modalMensajeEntidades',
			controllerAs : 'modalMess',
			backdrop : 'static',
			size : 'sm',
			resolve : {
				titulo : function() {
					return titulo;
				},
				mensaje : function() {
					return mensaje;
				}
			}

		});
	};

}
