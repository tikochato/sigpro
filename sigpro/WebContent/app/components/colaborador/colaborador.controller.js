/**
 * 
 */
var moduloColaborador = angular.module('moduloColaborador', [ 'ngTouch' ,'ui.bootstrap.contextMenu']);

moduloColaborador.controller('controlColaborador', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log',
		controlColaborador ]);

function controlColaborador($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log) {
	i18nService.setCurrentLang('es');
	var mi = this;

	$window.document.title = $utilidades.sistema_nombre+' - Colaborador';

	
	mi.esForma = false;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.columnaOrdenada=null;
	mi.ordenDireccion=null;

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	mi.filtros = [];
	mi.mostrarCargando = true;
	mi.data = [];
	
	mi.cargarData = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			filtro_pnombre: mi.filtros['pnombre'],
			filtro_snombre: mi.filtros['snombre'],
			filtro_papellido: mi.filtros['papellido'],
			filtro_sapellido: mi.filtros['sapellido'],
			filtro_cui: mi.filtros['cui'],
			filtro_unidad_ejecutora: mi.filtros['unidad_ejecutora'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion,
			registros : mi.elementosPorPagina
		};

		mi.mostrarCargando = true;
		$http.post('/SColaborador', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.colaboradores;
				mi.opcionesGrid.data = mi.data;

				mi.mostrarCargando = false;
			}
		});

	};
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
	mi.colaborador = null;
	mi.seleccionada = false;
	mi.menuOptions = [
        ['<span class="glyphicon glyphicon-pencil"> Editar', function ($itemScope, $event, modelValue, text, $li) {
      	  mi.editar();
        }],
        null,
        ['<span class="glyphicon glyphicon-trash text-danger"><font style="color: black;"> Borrar</font>', function ($itemScope, $li) {
      	  mi.borrar();
        }]
    ];
	
	mi.contextMenu = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.opcionesGrid.data[filaId]);
    };

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [{
			displayName : 'Primer Nombre',
			name : 'primerNombre',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.colaborador.filtros[\'pnombre\']" ng-keypress="grid.appScope.colaborador.filtrar($event)"></input></div>'
		}, {
			displayName : 'Segundo Nombre',
			name : 'segundoNombre',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.colaborador.filtros[\'snombre\']" ng-keypress="grid.appScope.colaborador.filtrar($event)"></input></div>'
		}, {
			displayName : 'Primer Apellido',
			name : 'primerApellido',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.colaborador.filtros[\'papellido\']" ng-keypress="grid.appScope.colaborador.filtrar($event)"></input></div>'
		}, {
			displayName : 'Segundo Apellido',
			name : 'segundoApellido',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.colaborador.filtros[\'sapellido\']" ng-keypress="grid.appScope.colaborador.filtrar($event)"></input></div>'
		}, {
			displayName : 'CUI',
			name : 'cui',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150,
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.colaborador.filtros[\'cui\']" ng-keypress="grid.appScope.colaborador.filtrar($event)"></input></div>'
		},{
			displayName : 'Nombre Unidad Ejecutora',
			name : 'nombreUnidadEjecutora',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.colaborador.filtros[\'unidad_ejecutora\']" ng-keypress="grid.appScope.colaborador.filtrar($event)"></input></div>'
		}],
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
	    rowTemplate: '<div context-menu="grid.appScope.colaborador.menuOptions" right-click="grid.appScope.colaborador.contextMenu($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			mi.gridApi.selection.on.rowSelectionChanged($scope,function(row){
				mi.colaborador = row.entity;
			});
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.colaborador.columnaOrdenada=sortColumns[0].field;
					grid.appScope.colaborador.ordenDireccion = sortColumns[0].sort.direction;
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.colaborador.cargarData(grid.appScope.colaborador.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.colaborador.columnaOrdenada!=null){
						grid.appScope.colaborador.columnaOrdenada=null;
						grid.appScope.colaborador.ordenDireccion=null;
					}
				}
					
			} );

			if ($routeParams.reiniciar_vista == 'rv') {
				mi.guardarEstado();
				mi.obtenerTotalColaboradores();
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
							}{

								mi.gridApi.colMovable.on.columnPositionChanged(
										$scope, mi.guardarEstado);
								mi.gridApi.colResizable.on.columnSizeChanged(
										$scope, mi.guardarEstado);
								mi.gridApi.core.on.columnVisibilityChanged($scope,
										mi.guardarEstado);
							}
							mi.obtenerTotalColaboradores();
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
			mi.obtenerTotalColaboradores();
		});
	}

	mi.reiniciarVista = function() {
		if ($location.path() == '/colaborador/rv')
			$route.reload();
		else
			$location.path('/colaborador/rv');
	}

	mi.nuevo = function() {
		
		mi.esForma = true;
		mi.colaborador = {};
		mi.esNuevo = true;

		mi.usuarioValido = false;
		mi.gridApi.selection.clearSelectedRows();
	}

	

	mi.editar = function() {
		if (mi.colaborador!=null && mi.colaborador.id!=null) {
			mi.esForma = true;
			mi.esNuevo = false;

			mi.validarUsuario();

		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un Conlabordor');
		}

	};

	mi.guardar = function() {
		if (mi.esNuevo) {
			var datos = {
				accion : 'crear',
				primerNombre : mi.colaborador.primerNombre,
				segundoNombre : mi.colaborador.segundoNombre,
				primerApellido : mi.colaborador.primerApellido,
				segundoApellido : mi.colaborador.segundoApellido,
				cui : mi.colaborador.cui,
				unidadEjecutora : mi.colaborador.unidadEjecutora,
				usuario : mi.colaborador.usuario
			};

			$http.post('/SColaborador', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.colaboradores;
							mi.colaborador.usuarioCreo =response.data.colaborador.usuarioCreo;
							mi.colaborador.fechaCreacion= response.data.colaborador.fechaCreacion;
							mi.colaborador.usuarioActualizo=response.data.colaborador.usuarioActualizo;
							mi.colaborador.fechaActualizacion=response.data.colaborador.fechaActualizacion;
							mi.opcionesGrid.data = mi.data;

							$utilidades.mensaje('success',
									'Colaborador guardado con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Error al guardar al Colaborador.');
						}

					});
		} else {
			var datos = {
				accion : 'actualizar',
				id : mi.colaborador.id,
				primerNombre : mi.colaborador.primerNombre,
				segundoNombre : mi.colaborador.segundoNombre,
				primerApellido : mi.colaborador.primerApellido,
				segundoApellido : mi.colaborador.segundoApellido,
				cui : mi.colaborador.cui,
				unidadEjecutora : mi.colaborador.unidadEjecutora,
				usuario : mi.colaborador.usuario
			};

			$http.post('/SColaborador', datos).then(
					function(response) {

						if (response.data.success) {
							mi.data = response.data.colaboradores;
							mi.opcionesGrid.data = mi.data;
							mi.colaborador.usuarioCreo =response.data.colaborador.usuarioCreo;
							mi.colaborador.fechaCreacion= response.data.colaborador.fechaCreacion;
							mi.colaborador.usuarioActualizo=response.data.colaborador.usuarioActualizo;
							mi.colaborador.fechaActualizacion=response.data.colaborador.fechaActualizacion;

							$utilidades.mensaje('success',
									'Colaborador actualizado con exito.');
						} else {
							$utilidades.mensaje('danger',
									'Error al actualizar datos.');
						}
					});

		}
	};
	
	mi.borrar = function(ev) {
		if(mi.colaborador!=null && mi.colaborador.id!=null){
			var confirm = $mdDialog.confirm()
		          .title('Confirmación de borrado')
		          .textContent('¿Desea borrar al colaborador "'+mi.colaborador.primerNombre+' '+mi.colaborador.segundoNombre+' '+mi.colaborador.primerApellido+' '+mi.colaborador.segundoApellido+'"?')
		          .ariaLabel('Confirmación de borrado')
		          .targetEvent(ev)
		          .ok('Borrar')
		          .cancel('Cancelar');

		    $mdDialog.show(confirm).then(function() {
		    	$http.post('/SColaborador', {
					accion: 'borrar',
					id: mi.colaborador.id
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Colaborador borrado con éxito');
						mi.colaborador = null;
						mi.obtenerTotalColaboradores();
					}
					else
						$utilidades.mensaje('danger','Error al borrar al Colaborador');
				});
		    }, function() {
		    
		    });
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar al Colaborador que desea borrar');
	};

	mi.cancelar = function() {
		mi.esForma = false;
		mi.esNuevo= false;
	};
	
	mi.filtrar = function(evt,tipo){
		if(evt.keyCode==13){
			mi.cargarData(mi.paginaActual);
		}
	}
	
	mi.obtenerTotalColaboradores=function(){
		$http.post('/SColaborador', {
			accion : 'totalElementos'
		}).success(function(response) {
			mi.totalElementos = response.total;
			mi.cargarData(1);
		});
	}

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
			mi.colaborador.unidadEjecutora = selectedItem.unidadEjecutora;
			mi.colaborador.nombreUnidadEjecutora = selectedItem.nombreUnidadEjecutora;

		}, function() {
		});
	};

	mi.usuarioValido = false;

	mi.validarUsuario = function() {
		$http.post('/SColaborador', {
			accion : 'validarUsuario',
			usuario : mi.colaborador.usuario
		}).then(function(response) {
			mi.usuarioValido = response.data.success;
		});
	}

	mi.usuarioCambio = function() {
		mi.usuarioValido = false;
	}
	
	mi.buscarUsuario=function(){
		var modalInstance = $uibModal.open({
		    animation : 'true',
		    ariaLabelledBy : 'modal-title',
		    ariaDescribedBy : 'modal-body',
		    templateUrl : 'buscarUsuario.jsp',
		    controller : 'modalBuscarUsuario',
		    controllerAs : 'modalBuscar',
		    backdrop : 'static',
		    size : 'md',
		    resolve : {
		    	infoUsuario: function(){
		    		var parametros={ usuario:""};
		    		return  parametros;
		    	}
		    }

		});
		
		modalInstance.result.then(function(data) {
			mi.colaborador.usuario=data.usuario;
			mi.usuarioValido=data.success;
		}, function() {
		});
	};

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

moduloColaborador.controller('modalBuscarUsuario', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','infoUsuario',modalBuscarUsuario
]);
function modalBuscarUsuario($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, infoUsuario) {
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
   
   
    mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [
			{
				displayName : 'Usuario', 
				name : 'usuario', 
				cellClass : 'grid-align-right', 
				type : 'text',width : 150
			
			}, 
			{ 
				displayName : 'Correo', 
				name : 'email', 
				cellClass : 'grid-align-left'
			}
			
		],
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
			    mi.seleccionarPermiso);
		}
    }
    var datos = {
			accion : 'getUsuariosDisponibles',
			pagina :1,
			registros : 100
		};

		mi.mostrarCargando = true;
		$http.post('/SUsuario', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.usuarios;
				mi.opcionesGrid.data = mi.data;

				mi.mostrarCargando = false;
			}
		});
    mi.seleccionarPermiso = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };


     mi.ok = function() {
    	if (mi.seleccionado) {
    	    $uibModalInstance.close({usuario:mi.itemSeleccionado.usuario, success:true});
    	} else {
    	    $utilidades.mensaje('warning', 'Debe seleccionar un colaborador');
    	}
     };
	

     mi.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
     };
}

