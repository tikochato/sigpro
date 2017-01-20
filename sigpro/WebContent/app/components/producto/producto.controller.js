var moduloProducto = angular.module('moduloProducto', [ 'ngTouch',
		'smart-table' ]);

moduloProducto.controller('controlProducto', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', '$q',
		controlProducto ]);

function controlProducto($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $q) {
	  
	i18nService.setCurrentLang('es');
	
	var mi = this;

	$window.document.title = 'SIGPRO - Producto';

	mi.esForma = false;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;

	mi.propiedadesValor = [];
	
	mi.camposdinamicos = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2020, 5, 22),
			minDate : new Date(1900, 1, 1),
			startingDay : 1
	};

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	$http.post('/SProducto', {
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
		$http.post('/SProducto', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.productos;
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
		}, {
			displayName : 'Id tipo',
			name : 'idProductoTipo',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			visible : false
		}, {
			displayName : 'Tipo',
			name : 'productoTipo',
			cellClass : 'grid-align-left',
			visible : false
		}, {
			displayName : 'Id componente',
			name : 'idComponente',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			visible : false
		}, {
			displayName : 'Componente',
			name : 'componente',
			cellClass : 'grid-align-left',
			visible : false
		}, {
			displayName : 'Id Producto',
			name : 'idProducto',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			visible : false
		}, {
			displayName : 'Producto',
			name : 'producto',
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
					grid : 'producto',
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
			grid : 'producto',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/producto/rv')
			$route.reload();
		else
			$location.path('/producto/rv');
	}

	mi.nuevo = function() {
		mi.limpiarSeleccion();

		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;

		mi.codigo = "";
		mi.nombre = "";
		mi.descripcion = "";

		mi.tipo = null;
		mi.tipoNombre = "";

		mi.componente = null;
		mi.componenteNombre = "";

		mi.productoPadre = null;
		mi.productoPadreNombre = "";

		mi.propiedadesValor = [];

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

			mi.tipo = mi.entidadSeleccionada.idProductoTipo;
			mi.tipoNombre = mi.entidadSeleccionada.productoTipo;

			mi.componente = mi.entidadSeleccionada.idComponente;
			mi.componenteNombre = mi.entidadSeleccionada.componente;

			mi.productoPadre = mi.entidadSeleccionada.idProducto;
			mi.productoPadreNombre = mi.entidadSeleccionada.producto;

		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un PRODUCTO');
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
					'Debe seleccionar un PRODUCTO que desee borrar');
		}
	};

	mi.borrarConfirmado = function() {
		var datos = {
			accion : 'borrar',
			codigo : mi.entidadSeleccionada.id
		};
		$http.post('/SProducto', datos).success(
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
				componente : mi.componente,
				productoPadre : mi.productoPadre,
				tipo : mi.tipo,
				propiedades : JSON.stringify(mi.propiedadesValor),
				actividades : JSON.stringify(''),
				usuario : 'temporal'
			};

			$http.post('/SProducto', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.productos;
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
				componente : mi.componente,
				productoPadre : mi.productoPadre,
				tipo : mi.tipo,
				propiedades : JSON.stringify(mi.propiedadesValor),
				actividades : JSON.stringify(''),
				usuario : 'temporal'
			};

			$http.post('/SProducto', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.productos;
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

	mi.editarPropiedadValor = function(index) {

	};

	mi.llamarModalBusqueda = function(servlet, datosTotal, datosCarga) {
		var resultado = $q.defer();

		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProducto.jsp',
			controller : 'modalBuscarPorProducto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$datosTotal : function() {
					return datosTotal;
				},
				$datosCarga : function() {
					return datosCarga;
				}
			}

		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});

		return resultado.promise;

	};

	mi.buscarTipo = function() {

		var resultado = mi.llamarModalBusqueda('/SProductoTipo', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		});

		resultado.then(function(itemSeleccionado) {
			mi.tipo = itemSeleccionado.id;
			mi.tipoNombre = itemSeleccionado.nombre;
			
			var parametros = { 
				accion: 'getProductoPropiedadPorTipo', 
				idproducto: mi.codigo,
				idproductotipo: itemSeleccionado.id
			}
			$http.post('/SProductoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.productopropiedades
			});
		});

	};

	mi.cambiarTipo = function() {

	};
	
	mi.abrirPopupFecha = function(index) {
		mi.camposdinamicos[index].isOpen = true;
	};

	mi.buscarComponente = function() {

		var resultado = mi.llamarModalBusqueda('/SComponente', {
			accion : 'numeroComponentes'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getComponentesPagina',
				pagina : pagina,
				numerocomponentes : elementosPorPagina
			};
		});

		resultado.then(function(itemSeleccionado) {
			mi.componente = itemSeleccionado.id;
			mi.componenteNombre = itemSeleccionado.nombre;
		});

	};

	mi.buscarProducto = function() {

		var resultado = mi.llamarModalBusqueda('/SProducto', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		});

		resultado.then(function(itemSeleccionado) {
			mi.productoPadre = itemSeleccionado.id;
			mi.productoPadreNombre = itemSeleccionado.nombre;
		});

	};

}

moduloProducto.controller('modalBuscarPorProducto', [ '$uibModalInstance',
		'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', '$servlet', '$datosTotal', '$datosCarga',
		modalBuscarPorProducto ]);

function modalBuscarPorProducto($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log, $servlet, $datosTotal,
		$datosCarga) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post($servlet, $datosTotal).success(function(response) {
		for ( var key in response) {
			mi.totalElementos = response[key];
		}
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
					mi.seleccionarItem);
		}
	}

	mi.seleccionarItem = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina)).then(
				function(response) {
					if (response.data.success) {

						for ( var key in response.data) {
							if (key != 'success')
								mi.data = response.data[key];
						}

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
			$utilidades.mensaje('warning', 'Debe seleccionar un elemento');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}