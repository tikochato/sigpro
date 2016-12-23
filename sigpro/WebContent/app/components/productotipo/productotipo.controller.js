var moduloProductoTipo = angular.module('moduloProductoTipo', [ 'ngTouch',
		'smart-table' ]);

moduloProductoTipo.controller('controlProductoTipo', [ '$scope',
		'$routeParams', '$route', '$window', '$location', '$mdDialog',
		'$uibModal', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', controlProductoTipo ]);

function controlProductoTipo($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log) {
	i18nService.setCurrentLang('es');
	var mi = this;

	$window.document.title = 'SIGPRO - Producto Tipo';

	mi.esForma = false;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;

	mi.propiedadesTipo = [];

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	$http.post('/SProductoTipo', {
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
		$http.post('/SProductoTipo', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.productoTipos;
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
					grid : 'productoTipo',
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
			grid : 'productoTipo',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/productoTipo/rv')
			$route.reload();
		else
			$location.path('/productoTipo/rv');
	}

	mi.nuevo = function() {
		mi.limpiarSeleccion();

		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;

		mi.codigo = "";
		mi.nombre = "";
		mi.descripcion = "";

		mi.propiedadesTipo = [];

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

			mi.mostrarCargando = true;

			var datos = {
				accion : 'tipoPropiedades',
				codigoTipo : mi.entidadSeleccionada.id
			};

			$http.post('/SProductoTipo', datos).then(function(response) {
				if (response.data.success) {
					mi.propiedadesTipo = response.data.productoTipos;
					mi.mostrarCargando = false;
				}
			});

		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar un TIPO DE PRODUCTO');
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
					'Debe seleccionar un TIPO DE PRODUCTO que desee borrar');
		}
	};

	mi.borrarConfirmado = function() {
		var datos = {
			accion : 'borrar',
			codigo : mi.entidadSeleccionada.id
		};
		$http.post('/SProductoTipo', datos).success(
				function(response) {
					if (response.success) {
						$utilidades.mensaje('success',
								'Tipo de Producto borrado con éxito');
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
				|| $utilidades.esCadenaVacia(mi.descripcion)) {
			$utilidades.mensaje('danger',
					'Debe de llenar todos los campos obligatorios');
			return;
		}

		if (mi.esNuevo) {
			var datos = {
				accion : 'crear',
				nombre : mi.nombre,
				descripcion : mi.descripcion,
				usuario : 'temporal',
				propiedades : JSON.stringify(mi.propiedadesTipo)
			};

			$http.post('/SProductoTipo', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.productoTipos;
							mi.opcionesGrid.data = mi.data;
							mi.esForma = false;

							$utilidades.mensaje('success',
									'Tipo de Producto guardado con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Tipo de Producto ya existe...!!!');
						}

					});
		} else {
			var datos = {
				accion : 'actualizar',
				codigo : mi.codigo,
				nombre : mi.nombre,
				descripcion : mi.descripcion,
				usuario : 'temporal',
				propiedades : JSON.stringify(mi.propiedadesTipo)
			};

			$http.post('/SProductoTipo', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.productoTipos;
							mi.opcionesGrid.data = mi.data;
							mi.esForma = false;

							$utilidades.mensaje('success',
									'Tipo de Producto actualizado con exito.');
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

	mi.eliminarPropiedad = function(index) {
		for (var i = 0; i < mi.propiedadesTipo.length; i++) {
			if (mi.propiedadesTipo[i].estado === "E") {
				index++;
				continue;
			}

			if (i == index) {
				if (mi.propiedadesTipo[index].estado === "C") {
					mi.propiedadesTipo[index].estado = "E";
				} else if (mi.propiedadesTipo[index].estado === "N") {
					mi.propiedadesTipo.splice(index, 1)
				}
			}
		}
	};

	mi.agregarPropiedad = function() {
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPropiedad.jsp',
			controller : 'modalBuscarPropiedad',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md'
		});

		modalInstance.result.then(function(selectedItem) {
			var codigo = mi.codigo

			if (!$utilidades.esNumero(codigo)) {
				codigo = -1;
			}

			if (mi.verificarPropiedad(codigo, selectedItem.id)) {
				mi.propiedadesTipo.push({
					idTipo : codigo,
					tipo : mi.nombre,
					idPropiedad : selectedItem.id,
					propiedad : selectedItem.nombre,
					idPropiedadTipo : selectedItem.idTipo,
					propiedadTipo : selectedItem.tipo,
					estado : 'N'
				});
			} else {
				$utilidades.mensaje('warning',
						'Propiedad agregada anteriormente');
			}
		}, function() {
		});

	};

	mi.verificarPropiedad = function(codigoTipo, codigoPropiedad) {
		for (var i = 0; i < mi.propiedadesTipo.length; i++) {
			if (mi.propiedadesTipo[i].idTipo === codigoTipo
					&& mi.propiedadesTipo[i].idPropiedad === codigoPropiedad) {
				return false;
			}
		}

		return true;
	}

}

moduloProductoTipo.controller('modalBuscarPropiedad', [ '$uibModalInstance',
		'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', modalBuscarPropiedad ]);

function modalBuscarPropiedad($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post('/SProductoPropiedad', {
		accion : 'totalElementos'
	}).success(function(response) {
		mi.totalElementos = response.total;
		mi.cargarData(1);
	});

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
		$http.post('/SProductoPropiedad', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.productoPropiedades;
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
			$utilidades.mensaje('warning', 'Debe seleccionar una PROPIEDAD');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}