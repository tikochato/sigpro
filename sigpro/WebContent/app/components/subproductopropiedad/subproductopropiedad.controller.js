/**
 * 
 */
var moduloSubproductoPropiedad = angular.module('moduloSubproductoPropiedad', [ 'ngTouch' ]);

moduloSubproductoPropiedad.controller('controlSubproductoPropiedad', [ '$scope',
		'$routeParams', '$route', '$window', '$location', '$mdDialog',
		'$uibModal', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log',  'dialogoConfirmacion', controlSubproductoPropiedad ]);

function controlSubproductoPropiedad($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $dialogoConfirmacion) {
	i18nService.setCurrentLang('es');
	var mi = this;

	$window.document.title = $utilidades.sistema_nombre+' - Subproducto Propiedad';

	mi.esForma = false;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	
	mi.datoTipos = [];
	mi.datoTipoSeleccionado = null;
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	
	$http.post('/SDatoTipo', {accion : 'cargarCombo'}).success(function(response) {
		mi.datoTipos = response.datoTipos;
	});
	

	mi.cambioPagina = function() {
		mi.cargarTabla(mi.paginaActual);
	}

	$http.post('/SSubproductoPropiedad', {accion : 'totalElementos'}).success(function(response) {
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
		$http.post('/SSubproductoPropiedad', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.subproductoPropiedades;
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
		rowTemplate: '<div ng-dblclick="grid.appScope.subproductoPropiedad.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [
			{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
		    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subproductoPropiedad.filtros[\'nombre\']" ng-keypress="grid.appScope.subproductoPropiedad.filtrar($event)"></input></div>'
		    },
		    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
		    {displayName : 'Tipo', name : 'tipo', cellClass : 'grid-align-left', enableFiltering: false, enableSorting: false},
		    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
		    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subproductoPropiedad.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.subproductoPropiedad.filtrar($event)"></input></div>'
		    },
		    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subproductoPropiedad.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.subproductoPropiedad.filtrar($event)"></input></div>'
		    }
		 ],		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			mi.gridApi.selection.on.rowSelectionChanged($scope,
					mi.seleccionarEntidad);
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.subproductoPropiedad.columnaOrdenada=sortColumns[0].field;
					grid.appScope.subproductoPropiedad.ordenDireccion = sortColumns[0].sort.direction;
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.subproductoPropiedad.cargarTabla(grid.appScope.subproductoPropiedad.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.subproductoPropiedad.columnaOrdenada!=null){
						grid.appScope.subproductoPropiedad.columnaOrdenada=null;
						grid.appScope.subproductoPropiedad.ordenDireccion=null;
					}
				}
					
			} );

			if ($routeParams.reiniciar_vista == 'rv') {
				mi.guardarEstado();
				 mi.obtenerTotalSubproductoPropiedades();
			} else {
				$http.post('/SEstadoTabla', { action : 'getEstado', grid : 'subproductoPropiedad', t : (new Date()).getTime()}).then(
						function(response) {
							if (response.data.success && response.data.estado != '') {
								mi.gridApi.saveState.restore($scope, response.data.estado);
							}
							mi.gridApi.colMovable.on.columnPositionChanged( $scope, mi.guardarEstado);
							mi.gridApi.colResizable.on.columnSizeChanged( $scope, mi.guardarEstado);
							mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
							mi.obtenerTotalSubproductoPropiedades();
						});
			}
		}
	}

	mi.guardarEstado = function() {
		var estado = mi.gridApi.saveState.save();

		var tabla_data = {
			action : 'guardaEstado',
			grid : 'subproductoPropiedad',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	}
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
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
					'Debe seleccionar una propiedad de subproducto');
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
						$http.post('/SSubproductoPropiedad', datos).success(
								function(response) {
									if (response.success) {
										$utilidades.mensaje('success',
												'Propiedad de subproducto borrado con éxito');
										mi.seleccionada = null;
										mi.cargarTabla(1);
									} else
										$utilidades.mensaje('danger',
												'Error al borrar la propiedad de subproducto');
								});
				}
			}, function(){
				
			});
		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar la prpiead de subproducto que desee borrar');
		}
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
			
			$http.post('/SSubproductoPropiedad', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.subproductoPropiedades;
							mi.opcionesGrid.data = mi.data;
							mi.esNuevo = false;
							mi.obtenerTotalSubproductoPropiedades();
							mi.codigo = response.data.id;
							$utilidades.mensaje('success',
									'Propiedad de subproducto guardado con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Propiedad de subproducto ya existe.');
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

			$http.post('/SSubproductoPropiedad', datos).then(
					function(response) {

						if (response.data.success) {
							mi.data = response.data.subproductoPropiedades;
							mi.opcionesGrid.data = mi.data;
							mi.esNuevo = false;
							mi.obtenerTotalSubproductoPropiedades();

							$utilidades.mensaje('success',
									'Propiedad de subproducto actualizado con exito.');
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
	
	mi.reiniciarVista = function() {
		if ($location.path() == '/subproductopropiedad/rv')
			$route.reload();
		else
			$location.path('/subproductopropiedad/rv');
	}
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalSubproductoPropiedades();
			mi.gridApi.selection.clearSelectedRows();
			mi.seleccionada = null;
		}
	};

	
	mi.obtenerTotalSubproductoPropiedades=function(){
		$http.post('/SSubproductoPropiedad', { accion: 'totalElementos',objetoid:$routeParams.objeto_id, tipo: mi.objetotipo,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(
				function(response) {
					mi.totalElementos = response.total;
					mi.cargarTabla(1);
		});
	};

}