var moduloSubproductoTipo = angular.module('moduloSubproductoTipo', [ 'ngTouch',
		'smart-table' ]);

moduloSubproductoTipo.controller('controlSubproductoTipo', [ '$scope',
		'$routeParams', '$route', '$window', '$location', '$mdDialog',
		'$uibModal', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log',  'dialogoConfirmacion', controlSubproductoTipo ]);

function controlSubproductoTipo($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $dialogoConfirmacion) {
	i18nService.setCurrentLang('es');
	var mi = this;

	$window.document.title = $utilidades.sistema_nombre+' - Subproducto Tipo';

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

	$http.post('/SSubproductoTipo', {
		accion : 'totalElementos'
	}).success(function(response) {
		mi.totalElementos = response.total;
		mi.cargarTabla(1);
	});

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
		$http.post('/SSubproductoTipo', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.subproductoTipos;
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
		rowTemplate: '<div ng-dblclick="grid.appScope.subproductoTipo.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [ 
			{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subproductoTipo.filtros[\'nombre\']" ng-keypress="grid.appScope.subproductoTipo.filtrar($event)"></input></div>'
		    },
		    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
		    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
		    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subproductoTipo.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.subproductoTipo.filtrar($event)"></input></div>'
		    },
		    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subproductoTipo.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.subproductoTipo.filtrar($event)"></input></div>'
		    }
	    ],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			mi.gridApi.selection.on.rowSelectionChanged($scope,
					mi.seleccionarEntidad);
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.subproductoTipo.columnaOrdenada=sortColumns[0].field;
					grid.appScope.subproductoTipo.ordenDireccion = sortColumns[0].sort.direction;
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.subproductoTipo.cargarTabla(grid.appScope.subproductoTipo.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.subproductoTipo.columnaOrdenada!=null){
						grid.appScope.subproductoTipo.columnaOrdenada=null;
						grid.appScope.subproductoTipo.ordenDireccion=null;
					}
				}
					
			} );

			if ($routeParams.reiniciar_vista == 'rv') {
				mi.guardarEstado();
				 mi.obtenerTotalSubproductoTipo();
			} else {
				$http.post('/SEstadoTabla', {action : 'getEstado', grid : 'subproductoTipo', t : (new Date()).getTime()}).then(
						function(response) {
							if (response.data.success&& response.data.estado != '') {
								mi.gridApi.saveState.restore($scope,response.data.estado);
							}
							mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							mi.gridApi.core.on.columnVisibilityChanged($scope,mi.guardarEstado);
							mi.gridApi.core.on.sortChanged($scope,mi.guardarEstado);
							mi.obtenerTotalSubproductoTipo();
						});
			}
		}
	}

	mi.guardarEstado = function() {
		var estado = mi.gridApi.saveState.save();

		var tabla_data = {
			action : 'guardaEstado',
			grid : 'subproductoTipo',
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
		if ($location.path() == '/subproductotipo/rv')
			$route.reload();
		else
			$location.path('/subproductotipo/rv');
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

			var datos = {
				accion : 'tipoPropiedades',
				codigoTipo : mi.entidadSeleccionada.id
			};

			$http.post('/SSubproductoTipo', datos).then(function(response) {
				if (response.data.success) {
					mi.propiedadesTipo = response.data.subproductoTipos;
			
				}
			});

		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar un TIPO DE PRODUCTO');
		}

	};

	mi.borrar = function(ev) {
		if (mi.seleccionada) {
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar "' + mi.entidadSeleccionada.nombre + '"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					var datos = {
							accion : 'borrar',
							codigo : mi.entidadSeleccionada.id
						};
						$http.post('/SSubproductoTipo', datos).success(
								function(response) {
									if (response.success) {
										$utilidades.mensaje('success',
												'Tipo de Subproducto borrado con éxito');
										mi.seleccionada = null;
										mi.cargarTabla(1);
									} else
										$utilidades.mensaje('danger',
												'Error al borrar el Tipo de Subproducto');
								});
				}
			}, function(){
				
			});
		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar un TIPO DE PRODUCTO que desee borrar');
		}
	};

	mi.guardar = function() {
		if (mi.esNuevo) {
			var datos = {
				accion : 'crear',
				nombre : mi.nombre,
				descripcion : mi.descripcion,
				propiedades : JSON.stringify(mi.propiedadesTipo)
			};

			$http.post('/SSubproductoTipo', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.subproductoTipos;
							mi.opcionesGrid.data = mi.data;
							mi.codigo = response.data.id;
							mi.esNuevo=false;
							$utilidades.mensaje('success',
									'El tipo de prodcuto se creo con éxito.');
							mi.obtenerTotalSubproductoTipo();
						} else {
							$utilidades.mensaje('danger',
									'Error la guardar el tipo de subproducto');
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

			$http.post('/SSubproductoTipo', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.subproductoTipos;
							mi.opcionesGrid.data = mi.data;
							mi.esNuevo=false;
							$utilidades.mensaje('success',
									'El tipo de subproducto se guardo con éxito');
							mi.obtenerTotalSubproductoTipo();
						} else {
							$utilidades.mensaje('danger',
									'Error al guardar el tipo de subproducto');
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
			mi.obtenerTotalSubproductoTipo();
			mi.gridApi.selection.clearSelectedRows();
			mi.seleccionada = null;
		}
	};

	
	mi.obtenerTotalSubproductoTipo=function(){
		$http.post('/SSubproductoTipo', { accion: 'totalElementos',objetoid:$routeParams.objeto_id, tipo: mi.objetotipo,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(
				function(response) {
					mi.totalElementos = response.total;
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

moduloSubproductoTipo.controller('modalBuscarPropiedad', [ '$uibModalInstance',
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

	$http.post('/SSubproductoPropiedad', {
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
		$http.post('/SSubproductoPropiedad', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.subproductoPropiedades;
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
			$utilidades.mensaje('warning', 'Debe seleccionar una PROPIEDAD');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}