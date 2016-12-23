/**
 * 
 */
var moduloProductoPropiedad = angular.module('moduloProductoPropiedad', [ 'ngTouch' ]);

moduloProductoPropiedad.controller('controlProductoPropiedad', [ '$scope',
		'$routeParams', '$route', '$window', '$location', '$mdDialog',
		'$uibModal', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', controlProductoPropiedad ]);

function controlProductoPropiedad($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log) {
	i18nService.setCurrentLang('es');
	var mi = this;

	$window.document.title = 'SIGPRO - Producto Propiedad';

	mi.esForma = false;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	
	mi.datoTipos = [];
	mi.datoTipoSeleccionado = null;
	
	$http.post('/SDatoTipo', {
		accion : 'cargarCombo'
	}).success(function(response) {
		mi.datoTipos = response.datoTipos;
	});
	

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	$http.post('/SProductoPropiedad', {
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
		$http.post('/SProductoPropiedad', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.productoPropiedades;
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
			displayName : 'Nombre',
			name : 'nombre',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'Id Tipo',
			name : 'idTipo',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			visible : false
		}, {
			displayName : 'Tipo',
			name : 'tipo',
			cellClass : 'grid-align-left'
		}, {
			displayName : 'Descripción',
			name : 'descripcion',
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
					grid : 'productoPropiedad',
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
			grid : 'productoPropiedad',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/productoPropiedad/rv')
			$route.reload();
		else
			$location.path('/productoPropiedad/rv');
	}

	mi.nuevo = function() {
		mi.limpiarSeleccion();

		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;

		mi.codigo = "";
		mi.nombre = "";
		mi.descripcion = "";
		
		mi.datoTipoSeleccionado = null;
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
			mi.nombre = mi.entidadSeleccionada.nombre;
			mi.descripcion = mi.entidadSeleccionada.descripcion;

			mi.datoTipoSeleccionado = {
					"id" : mi.entidadSeleccionada.idTipo,
					"nombre" : mi.entidadSeleccionada.tipo
			}

		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar una PROPIEDAD DE PRODUCTO');
		}

	};

	mi.borrar = function(ev) {
		if (mi.seleccionada) {
			var confirm = $mdDialog.confirm().title('Confirmación de borrado')
					.textContent(
							'¿Desea borrar "' + mi.entidadSeleccionada.nombre
									+ '"?')
					.ariaLabel('Confirmación de borrado').targetEvent(ev).ok(
							'Borrar').cancel('Cancelar');

			$mdDialog.show(confirm).then(mi.borrarConfirmado,
					mi.borrarNoConfirmado);

		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar una PROPIEDAD DE PRODUCTO que desee borrar');
		}
	};

	mi.borrarConfirmado = function() {
		var datos = {
			accion : 'borrar',
			codigo : mi.entidadSeleccionada.id
		};
		$http.post('/SProductoPropiedad', datos).success(
				function(response) {
					if (response.success) {
						$utilidades.mensaje('success',
								'Propiedad de Producto borrado con éxito');
						mi.cargarData(1);
					} else
						$utilidades.mensaje('danger',
								'Error al borrar el Tipo de Producto');
				});
	};

	mi.borrarNoConfirmado = function() {

	};

	mi.guardar = function() {
		
		if ($utilidades.esCadenaVacia(mi.nombre)
				|| $utilidades.esCadenaVacia(mi.descripcion)
				|| !$utilidades.esNumero(mi.datoTipoSeleccionado.id)) {
			$utilidades.mensaje('danger',
					'Debe de llenar todos los campos obligatorios');
			return;
		}

		if (mi.esNuevo) {
			var datos = {
				accion : 'crear',
				nombre : mi.nombre,
				descripcion : mi.descripcion,
				tipo : mi.datoTipoSeleccionado.id,
				usuario : 'temporal'
			};
			
			$http.post('/SProductoPropiedad', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.productoPropiedades;
							mi.opcionesGrid.data = mi.data;
							mi.esForma = false;

							$utilidades.mensaje('success',
									'Propiedad de Producto guardado con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Propiedad de Producto ya existe...!!!');
						}

					});
		} else {
			var datos = {
				accion : 'actualizar',
				codigo : mi.codigo,
				nombre : mi.nombre,
				descripcion : mi.descripcion,
				tipo : mi.datoTipoSeleccionado.id,
				usuario : 'temporal'
			};

			$http.post('/SProductoPropiedad', datos).then(
					function(response) {

						if (response.data.success) {
							mi.data = response.data.productoPropiedades;
							mi.opcionesGrid.data = mi.data;
							mi.esForma = false;

							$utilidades.mensaje('success',
									'Propiedad de Producto actualizado con exito.');
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

}