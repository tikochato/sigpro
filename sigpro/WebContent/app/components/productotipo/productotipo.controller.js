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

	$window.document.title = $utilidades.sistema_nombre+' - Producto Tipo';

	mi.esForma = false;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;

	mi.propiedadesTipo = [];
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];

	mi.cambioPagina = function() {
		mi.cargarTabla(mi.paginaActual);
	}

	
	mi.mostrarCargando = true;
	mi.data = [];
	mi.cargarTabla = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			registros : mi.elementosPorPagina,
			filtro_nombre: mi.filtros['nombre'], 
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
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
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect : false,
		modifierKeysToMultiSelect : false,
		noUnselect : false,
		enableFiltering : true,
		enablePaginationControls : false,
		paginationPageSize : $utilidades.elementosPorPagina,
		useExternalFiltering: true,
	    useExternalSorting: true,
		data : mi.data,
		columnDefs : [ 
			{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.productoTipo.filtros[\'nombre\']" ng-keypress="grid.appScope.productoTipo.filtrar($event)"></input></div>'
		    },
		    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
		    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
		    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.productoTipo.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.productoTipo.filtrar($event)"></input></div>'
		    },
		    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.productoTipo.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.productoTipo.filtrar($event)"></input></div>'
		    }
	    ],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			mi.gridApi.selection.on.rowSelectionChanged($scope,
					mi.seleccionarEntidad);
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.productoTipo.columnaOrdenada=sortColumns[0].field;
					grid.appScope.productoTipo.ordenDireccion = sortColumns[0].sort.direction;
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.productoTipo.cargarTabla(grid.appScope.productoTipo.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.productoTipo.columnaOrdenada!=null){
						grid.appScope.productoTipo.columnaOrdenada=null;
						grid.appScope.productoTipo.ordenDireccion=null;
					}
				}
					
			} );

			if ($routeParams.reiniciar_vista == 'rv') {
				mi.guardarEstado();
				 mi.obtenerTotalProductoTipo();
			} else {
				$http.post('/SEstadoTabla', {action : 'getEstado', grid : 'productoTipo', t : (new Date()).getTime()}).then(
						function(response) {
							if (response.data.success&& response.data.estado != '') {
								mi.gridApi.saveState.restore($scope,response.data.estado);
							}
							mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							mi.gridApi.core.on.columnVisibilityChanged($scope,mi.guardarEstado);
							mi.gridApi.core.on.sortChanged($scope,mi.guardarEstado);
							mi.obtenerTotalProductoTipo();
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
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
	mi.reiniciarVista = function() {
		if ($location.path() == '/productotipo/rv')
			$route.reload();
		else
			$location.path('/productotipo/rv');
	}

	mi.nuevo = function() {
		mi.limpiarSeleccion();

		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;

		mi.codigo = "";
		mi.nombre = "";
		mi.descripcion = "";
		mi.entidadSeleccionada.usuarioCreo = "";
		mi.entidadSeleccionada.fechaCreacion = "";
		mi.entidadSeleccionada.usuairoActulizo = "";
		mi.entidadSeleccionada.fechaActualizacion = "";
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

			var datos = {
				accion : 'tipoPropiedades',
				codigoTipo : mi.entidadSeleccionada.id
			};

			$http.post('/SProductoTipo', datos).then(function(response) {
				if (response.data.success) {
					mi.propiedadesTipo = response.data.productoTipos;
			
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
						mi.obtenerTotalProductoTipo();
					} else
						$utilidades.mensaje('danger',
								'Error al borrar el Tipo de Producto');
				});
	};

	mi.borrarNoConfirmado = function() {

	};

	mi.guardar = function() {
		
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
							mi.codigo = response.data.id;
							mi.entidadSeleccionada.usuarioCreo = response.data.usuarioCreo;
							mi.entidadSeleccionada.fechaCreacion = response.data.fechaCreacion;
							mi.entidadSeleccionada.usuairoActulizo = response.data.usuarioactualizo;
							mi.entidadSeleccionada.fechaActualizacion = response.data.fechaactualizacion;
							
							mi.esNuevo=false;
							$utilidades.mensaje('success',
									'El tipo de producto se creo con éxito.');
							mi.obtenerTotalProductoTipo();
						} else {
							$utilidades.mensaje('danger',
									'Error la guardar el tipo de producto');
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
							mi.entidadSeleccionada.usuarioCreo = response.data.usuarioCreo;
							mi.entidadSeleccionada.fechaCreacion = response.data.fechaCreacion;
							mi.entidadSeleccionada.usuairoActulizo = response.data.usuarioactualizo;
							mi.entidadSeleccionada.fechaActualizacion = response.data.fechaactualizacion;
							
							mi.esNuevo=false;
							$utilidades.mensaje('success',
									'El tipo de producto se guardo con éxito');
							mi.obtenerTotalProductoTipo();
						} else {
							$utilidades.mensaje('danger',
									'Error al guardar el tipo de producto');
						}
					});

		}
	};

	mi.cancelar = function() {
		mi.esForma = false;
		mi.esNuevo=false;
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
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalProductoTipo();
		}
	};

	
	mi.obtenerTotalProductoTipo = function(){
		$http.post('/SProductoTipo', { accion: 'totalElementos',objetoid:$routeParams.objeto_id, tipo: mi.objetotipo,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(
				function(response) {
					mi.totalElementos = response.total;
					mi.paginaActual = 1;
					mi.cargarTabla(1);
		});
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
					descripcion: selectedItem.descripcion,
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
		mi.cargarTabla(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'Id',
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 80
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

	mi.cargarTabla = function(pagina) {
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
		mi.cargarTabla(mi.paginaActual);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una Propiedad');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}