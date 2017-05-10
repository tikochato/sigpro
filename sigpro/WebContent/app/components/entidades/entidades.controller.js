/**
 * 
 */
var moduloEntidad = angular.module('moduloEntidad', [ 'ngTouch' ]);

moduloEntidad.controller('controlEntidad', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', 'dialogoConfirmacion', 
		controlEntidad ]);

function controlEntidad($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $dialogoConfirmacion) {
	i18nService.setCurrentLang('es');
	var mi = this;
	
	$window.document.title = $utilidades.sistema_nombre+' - Entidad';

	mi.totalEntidades = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.filtros=[];
	
	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	mi.obtenerTotalEntidades= function(){ $http.post('/SEntidad', {
			accion : 'totalEntidades',
			filtro_entidad: mi.filtros['entidad'],
			filtro_nombre: mi.filtros['nombre'],
			filtro_abreviatura: mi.filtros['abreviatura']
		}).success(function(response) {
			mi.totalEntidades = response.total;
			mi.cargarData(1);
		});
	}

	mi.mostrarCargando = true;
	mi.data = [];
	mi.cargarData = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			registros : mi.elementosPorPagina,
			filtro_entidad: mi.filtros['entidad'],
			filtro_nombre: mi.filtros['nombre'],
			filtro_abreviatura: mi.filtros['abreviatura'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
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
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
	mi.entidad = -1;
	mi.seleccionada = false;
	

	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.entidades_gridOptions.data[filaId]);
        mi.editar();
    };

	mi.entidades_gridOptions = {
		data : mi.data,
		rowTemplate: '<div ng-dblclick="grid.appScope.entidad.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [ {
			name : 'Entidad',
			field : 'entidad',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.entidad.filtros[\'entidad\']" ng-keypress="grid.appScope.entidad.filtrar($event)"></input></div>'
		}, {
			name : 'Nombre Entidad',
			field : 'nombre',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.entidad.filtros[\'nombre\']" ng-keypress="grid.appScope.entidad.filtrar($event)"></input></div>'
		}, {
			name : 'Siglas',
			field : 'abreviatura',
			width : 150,
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.entidad.filtros[\'abreviatura\']" ng-keypress="grid.appScope.entidad.filtrar($event)"></input></div>'
		} ],
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
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			mi.gridApi.selection.on.rowSelectionChanged($scope,
					function(row){
				mi.entidad = row.entity;
			});
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.entidad.columnaOrdenada=sortColumns[0].field;
					grid.appScope.entidad.ordenDireccion = sortColumns[0].sort.direction;
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.entidad.cargarData(grid.appScope.entidad.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.entidad.columnaOrdenada!=null){
						grid.appScope.entidad.columnaOrdenada=null;
						grid.appScope.entidad.ordenDireccion=null;
					}
				}
					
			} );

			if ($routeParams.reiniciar_vista == 'rv') {
				mi.guardarEstado();
				mi.obtenerTotalEntidades();
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
							
							mi.obtenerTotalEntidades();
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

	mi.nueva = function() {
		mi.esForma = true;
		mi.entityselected = {};
		mi.esNuevo = true;

		mi.entidad = {};

		mi.gridApi.selection.clearSelectedRows();
	}

	mi.editar = function() {
		if (mi.entidad!= null && mi.entidad.entidad!=null) {
			mi.esForma = true;
			mi.entityselected = null;
			mi.esNuevo = false;

		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una Entidad');
		}

	};

	mi.guardar = function() {
		if (mi.esNuevo && mi.entidad.nombre!='') {
			var datos = {
				accion : 'crear',
				entidad : mi.entidad.entidad,
				nombre : mi.entidad.nombre,
				abreviatura : mi.entidad.abreviatura
			};

			$http.post('/SEntidad', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.entidades;
							mi.entidades_gridOptions.data = mi.data;
							$utilidades.mensaje('success',
									'Entidad guardada con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Entidad ya existe.');
						}

					});
		} else {
			var datos = {
				accion : 'actualizar',
				entidad : mi.entidad.entidad,
				abreviatura : mi.entidad.abreviatura
			};

			$http.post('/SEntidad', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.entidades;
							mi.entidades_gridOptions.data = mi.data;
							$utilidades.mensaje('success',
									'Entidad actualizada con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Error al actualizar datos.');
						}
					});

		}
	};

	mi.cancelar = function() {
		mi.esForma = false;
	};
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalEntidades();
			mi.gridApi.selection.clearSelectedRows();
			mi.entidad = null;
		}
	};

}
