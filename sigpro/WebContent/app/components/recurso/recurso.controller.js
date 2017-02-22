var app = angular.module('recursoController', []);

app.controller('recursoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Recursos';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.recursos = [];
		mi.recurso;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalRecursos = 0;
		mi.paginaActual = 1;
		mi.datotipoid = "";
		mi.datotiponombre = "";
		mi.recursotipoid = "";
		mi.recursonombre = "";
		mi.formatofecha = 'dd/MM/yyyy';
		mi.camposdinamicos = {};
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp';
			$utilidades.mensaje('primary','No tienes permiso de acceder a esta área');
			
		}
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
		mi.filtros = [];
		
		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2030, 12, 31),
				minDate : new Date(1950, 1, 1),
				startingDay : 1
		};

		mi.gridOptions = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: $utilidades.elementosPorPagina,
			    useExternalFiltering: true,
			    useExternalSorting: true,
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursoc.filtros[\'nombre\']" ng-keypress="grid.appScope.recursoc.filtrar($event)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursoc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.recursoc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.recursoc.filtrar($event)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.recurso = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.recursoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.recursoc.ordenDireccion = sortColumns[0].sort.direction;
							for(var i = 0; i<sortColumns.length-1; i++)
								sortColumns[i].unsort();
							grid.appScope.recursoc.cargarTabla(grid.appScope.recursoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.recursoc.columnaOrdenada!=null){
								grid.appScope.recursoc.columnaOrdenada=null;
								grid.appScope.recursoc.ordenDireccion=null;
							}
						}
							
					} );

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalRecursos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'recursos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!='')
				    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalRecursos();
						  });
				    }
				}
		};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SRecurso', { accion: 'getRecursosPagina', 
				pagina: pagina, numerorecursos: $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
			}).success(
					function(response) {
						mi.recursos = response.recursos;
						mi.gridOptions.data = mi.recursos;
						mi.mostrarcargando = false;
					});
		}

		mi.guardar=function(){
			for (campos in mi.camposdinamicos) {
				if (mi.camposdinamicos[campos].tipo === 'fecha') {
					mi.camposdinamicos[campos].valor = moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY');
				}
			}
			if(mi.recurso!=null && mi.recurso.id!=null){
				$http.post('/SRecurso', {
					accion: 'guardarRecurso',
					esnuevo: mi.esnuevo,
					recursotipoid : mi.recurso.recursotipoid,
					unidadmedida: mi.recurso.medidaid,
					id: mi.recurso.id,
					nombre: mi.recurso.nombre,
					descripcion: mi.recurso.descripcion,
					datadinamica : JSON.stringify(mi.camposdinamicos)
				}).success(function(response){
					if(response.success){
						mi.recurso.id = response.id;
						$utilidades.mensaje('success','Recurso '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.obtenerTotalRecursos();
						mi.esnuevo = false;
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Recurso');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.borrar = function(ev) {
			if(mi.recurso!=null && mi.recurso.id!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar el Recurso "'+mi.recurso.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SRecurso', {
						accion: 'borrarRecurso',
						id: mi.recurso.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Recurso borrado con éxito');
							mi.recurso = {};
							mi.obtenerTotalRecursos();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Recurso');
					});
			    }, function() {

			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Recurso que desea borrar');
		};

		mi.nuevo = function() {
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.recursotipoid = "";
			mi.recursonombre = "";
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.recurso = {};
			mi.gridApi.selection.clearSelectedRows();
		};

		mi.editar = function() {
			if(mi.recurso!=null && mi.recurso.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Recurso que desea editar');
		}

		mi.irATabla = function() {
			mi.mostraringreso=false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'recursos', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()==('/recurso/'+ mi.objetoTipo + '/' + mi.objetoId +'/rv'))
				$route.reload();
			else
				$location.path('/recurso/'+ mi.objetoTipo + '/' + mi.objetoId + '/rv');
		}

		mi.abrirPopupFecha = function(index) {
			mi.camposdinamicos[index].isOpen = true;
		};
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalRecursos();
			}
		}

		mi.obtenerTotalRecursos = function(){
			$http.post('/SRecurso', { accion: 'numeroRecursos',
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']
				}).success(
					function(response) {
						mi.totalRecursos = response.totalrecursos;
						mi.cargarTabla(1);
			});
		}

		mi.buscarRecursoTipo = function(titulo, mensaje) {

			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarRecursoTipo.jsp',
				controller : 'modalBuscarRecursoTipo',
				controllerAs : 'modalBuscar',
				backdrop : 'static',
				size : 'md',
				resolve : {
					titulo : function() {
						return titulo;
					},
					mensaje : function() {
						return mensaje;
					}
				}
			});

			modalInstance.result.then(function(selectedItem) {
				mi.recurso.recursotipoid = selectedItem.id;
				mi.recurso.recursotiponombre = selectedItem.nombre;

			}, function() {
			});
			
			
	};
	
	mi.buscarUnidadMedida = function(titulo, mensaje) {

			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : '/app/components/recurso/buscarRecursoUnidadMedida.jsp',
				controller : 'modalBuscarUnidadMedida',
				controllerAs : 'modalBuscarUnidadMedida',
				backdrop : 'static',
				size : 'md',
				resolve : {
					titulo : function() {
						return titulo;
					},
					mensaje : function() {
						return mensaje;
					}
				}
			});

				modalInstance.result.then(function(selectedItem) {
					mi.recurso.medidaid = selectedItem.id;
					mi.recurso.medidanombre = selectedItem.nombre;
		
				}, function() {
				});
		
		
		};

} ]);

app.controller('modalBuscarRecursoTipo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'mensaje', modalBuscarRecursoTipo ]);

function modalBuscarRecursoTipo($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, titulo, mensaje) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post('/SRecursoTipo', {
		accion : 'numeroRecursoTipos'
	}).success(function(response) {
		mi.totalElementos = response.totalrecursostipos;
		mi.elementosPorPagina = mi.totalElementos;
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Nombre Tipo',
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
		$http.post('/SRecursoTipo', {accion : 'getRecursotiposPagina'}).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.recursotipos;
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
			$utilidades.mensaje('warning', 'Debe seleccionar un Tipo');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}

app.controller('modalBuscarUnidadMedida', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'mensaje', modalBuscarUnidadMedida ]);

function modalBuscarUnidadMedida($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, titulo, mensaje) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.medidas = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post('/SRecursoUnidadMedida', {
		accion : 'numeroRecursoUnidadMedidas'
	}).success(function(response) {
		mi.totalElementos = response.totalRecursoUnidadMedidas;
		mi.elementosPorPagina = mi.totalElementos;
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.medidas,
		columnDefs : [ {
			displayName : 'ID',
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Unidad',
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
		$http.post('/SRecursoUnidadMedida', {accion : 'getRecursoUnidadMedidasPagina'}).then(function(response) {
			if (response.data.success) {
				mi.medidas = response.data.RecursoUnidadMedidas;
				mi.opcionesGrid.data = mi.medidas;
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
			$utilidades.mensaje('warning', 'Debe seleccionar una Unidad');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}
