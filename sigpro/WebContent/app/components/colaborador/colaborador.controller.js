var moduloColaborador = angular.module('moduloColaborador', [ 'ngTouch']);

moduloColaborador.controller('controlColaborador', [ '$scope', '$routeParams','$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', 'dialogoConfirmacion','$q',controlColaborador ]);

function controlColaborador($scope, $routeParams, $route, $window, $location, $mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $dialogoConfirmacion, $q) {
	i18nService.setCurrentLang('es');
	var mi = this;

	$window.document.title = $utilidades.sistema_nombre+' - Colaborador';

	mi.esForma = false;
	mi.entidad="";
	mi.ejercicio="";
	mi.unidadejecutoranombre="";
	mi.entidadnombre="";
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.columnaOrdenada=null;
	mi.ordenDireccion=null;
	mi.unidadesejecutoras = [];
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
				mi.paginaActual = pagina;
			}
		});

	};
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
	mi.colaborador = null;
	mi.seleccionada = false;
	
	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.opcionesGrid.data[filaId]);
        mi.editar();
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
	    rowTemplate: '<div ng-dblclick="grid.appScope.colaborador.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
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
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.usuarioValido = false;
		mi.gridApi.selection.clearSelectedRows();
	}

	

	mi.editar = function() {
		if (mi.colaborador!=null && mi.colaborador.id!=null) {
			mi.esForma = true;
			mi.esNuevo = false;
			mi.unidadejecutoraid=mi.colaborador.unidadejecutoraid;
			mi.unidadejecutoranombre=mi.colaborador.unidadejecutora;
			mi.entidadnombre = mi.colaborador.entidadnombre;
			mi.ejercicio = mi.colaborador.ejercicio;
			mi.entidad = mi.colaborador.entidadentidad;
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
				ejercicio: mi.ejercicio,
				entidadid: mi.entidad,
				unidadejecutoraid: mi.colaborador.unidadejecutoraid,
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
				ejercicio: mi.ejercicio,
				entidadid: mi.entidad,
				unidadejecutoraid : mi.colaborador.unidadejecutoraid,
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
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar al colaborador "'+mi.colaborador.primerNombre+' '+mi.colaborador.segundoNombre+' '+mi.colaborador.primerApellido+' '+mi.colaborador.segundoApellido+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
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
				}
			}, function(){
				
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
			mi.obtenerTotalColaboradores();
			//mi.cargarData(mi.paginaActual);
			mi.gridApi.selection.clearSelectedRows();
			mi.colaborador = null;
		}
	}
	
	mi.obtenerTotalColaboradores=function(){
		$http.post('/SColaborador', {
			accion : 'totalElementos',
			filtro_pnombre: mi.filtros['pnombre'],
			filtro_snombre: mi.filtros['snombre'],
			filtro_papellido: mi.filtros['papellido'],
			filtro_sapellido: mi.filtros['sapellido'],
			filtro_cui: mi.filtros['cui'],
			filtro_unidad_ejecutora: mi.filtros['unidad_ejecutora']
		}).success(function(response) {
			mi.totalElementos = response.total;
			mi.cargarData(mi.paginaActual);
		});
	}

	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('Unidades Ejecutoras','/SUnidadEjecutora', {
			accion : 'totalElementos',
			ejercicio: mi.ejercicio,
			entidad: mi.entidad
		}, function(pagina, elementosPorPagina,ejercicio,entidad) {
			return {
				accion : 'cargar',
				pagina : pagina,
				ejercicio: ejercicio,
				entidad: entidad,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora', true, {entidad: mi.entidad, ejercicio: mi.ejercicio, abreviatura:'', nombre: mi.entidadnombre});

		resultado.then(function(itemSeleccionado) {
			mi.ejercicio = itemSeleccionado.ejercicio;
			mi.entidad = itemSeleccionado.entidad;
			mi.colaborador.unidadejecutoraid= itemSeleccionado.unidadEjecutora;
			mi.colaborador.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.colaborador.entidadnombre = itemSeleccionado.nombreEntidad;
		});
	};
	
	mi.llamarModalBusqueda = function(titulo,servlet, accionServlet, datosCarga,columnaId,columnaNombre, showfilters,entidad) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarUnidadEjecutora.jsp',
			controller : 'modalBuscarUnidadEjecutora',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$titulo: function(){
					return titulo;
				},
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				},
				$showfilters: function(){
					return showfilters;
				},
				$entidad: function(){
					return entidad;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
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
		'Utilidades', '$timeout', '$log','$titulo', '$servlet', '$accionServlet', '$datosCarga',
		'$columnaId','$columnaNombre','$showfilters','$entidad','$rootScope', modalBuscarUnidadEjecutora ]);

function modalBuscarUnidadEjecutora($uibModalInstance, $scope, $http,
		$interval, i18nService, $utilidades, $timeout, $log,$titulo, $servlet, $accionServlet, $datosCarga,
		$columnaId,$columnaNombre,$showfilters,$entidad,$rootScope) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	mi.showfilters = $showfilters;
	mi.mostrarCargando = false;
	mi.data = [];
	mi.ejercicios = [];
	mi.entidades = [];
	mi.titulo = $titulo;
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
	if(mi.showfilters){
		var current_year = moment().year();
		mi.entidad = $entidad;
		mi.ejercicio = $entidad.ejercicio;
		for(var i=current_year-$rootScope.catalogo_entidades_anos; i<=current_year; i++)
			mi.ejercicios.push(i);
		mi.ejercicio = (mi.ejercicio == "") ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio}).success(function(response) {
			mi.entidades = response.entidades;
			if(mi.entidades.length>0){
				mi.entidad = (mi.entidad===undefined) ? mi.entidades[0] : mi.entidad;
				$accionServlet.ejercicio = mi.ejercicio;
				$accionServlet.entidad = mi.entidad.entidad;
				$http.post($servlet, $accionServlet).success(function(response) {
					for ( var key in response) {
						mi.totalElementos = response[key];
					}
					mi.cargarData(1,mi.ejercicio,mi.entidad.entidad);
				});
			}
			
		});
	}
	else{
		$http.post($servlet, $accionServlet).success(function(response) {
			for ( var key in response) {
				mi.totalElementos = response[key];
			}
			mi.cargarData(1,0,0);
		});
	}

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

	mi.cargarData = function(pagina,ejercicio, entidad) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina,ejercicio, entidad)).then(
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

