var app = angular.module('desembolsoController', []);

app.controller('desembolsoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Desembolso';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.desembolsos = [];
			mi.desembolso;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalDesembolsos = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.desembolsotipoid;
			mi.desembolsonombre;
			mi.proyectoid = $routeParams.pproyectoid;
			mi.fecha = new Date();
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;

			mi.filtros = [];
			mi.orden = null;
			
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
					{ name: 'id', width: 65, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'fecha',  displayName: 'Fecha', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.desembolsoc.filtros[\'fecha\']"  ng-keypress="grid.appScope.desembolsoc.filtrar($event)" ></input></div>'
					},
					{ name: 'monto', width: 100, displayName: 'Monto', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'desembolsotipo', width: 200, displayName: 'Tipo Desembolso',cellClass: 'grid-align-left',enableFiltering: false, enableSorting: false },
					{ name: 'tipocambio', width: 100, displayName: 'Tipo Cambio', cellClass: 'grid-align-right', type: 'number', enableFiltering: false, enableSorting: false },
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.desembolsoc.filtros[\'usuarioCreo\']"  ng-keypress="grid.appScope.desembolsoc.filtrar($event)" ></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.desembolsoc.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.desembolsoc.filtrar($event)" ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.desembolso = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.desembolsoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.desembolsoc.ordenDireccion = sortColumns[0].sort.direction;
							grid.appScope.desembolsoc.cargarTabla(grid.appScope.desembolsoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.desembolsoc.columnaOrdenada!=null){
								grid.appScope.desembolsoc.columnaOrdenada=null;
								grid.appScope.desembolsoc.ordenDireccion=null;
							}
						}
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalDesembolsos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'desembolsos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!=''){
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				    		  }
				    		  mi.obtenerTotalDesembolsos();
						  });
				    }
				}
			};
			
			mi.opcionesFecha = {
				    formatYear: 'yyyy',
				    maxDate: new Date(2050, 12, 31),
				    minDate : new Date(1990, 1, 1),
				    startingDay: 1
				  };
			
			 mi.popup = {
					 abierto: false
			 };
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SDesembolso', { accion: 'getDesembolsosPaginaPorProyecto', pagina: pagina, numerodesembolsos: $utilidades.elementosPorPagina,
					proyectoid: $routeParams.proyecto_id,numeroproyecto:  $utilidades.elementosPorPagina,
					filtro_fecha: mi.filtros['fecha'], filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
					}).success(
				
						function(response) {
							mi.desembolsos = response.desembolsos;
							mi.gridOptions.data = mi.desembolsos;
							mi.mostrarcargando = false;
						});
			};
			
			mi.guardar=function(){
				if(mi.desembolso!=null && mi.proyectoid!=''){
					$http.post('/SDesembolso', {
						accion: 'guardarDesembolso',
						esnuevo: mi.esnuevo,
						id: mi.desembolso.id,
						fecha: moment(mi.fecha).format('DD/MM/YYYY'),
						monto: mi.desembolso.monto,
						tipocambio : mi.desembolso.tipocambio,
						proyectoid : 1,
						desembolsotipoid : mi.desembolso.desembolsotipoid
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Desembolso '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.desembolso.id = response.id;
							mi.esnuevo = false;
							mi.obtenerTotalDesembolsos();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Desembolso');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};
			
			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.desembolso = {};
				mi.desembolsotipoid="";
				mi.desembolsonombre="";
				mi.gridApi.selection.clearSelectedRows();
			};
			
			mi.borrar = function(ev) {
				if(mi.desembolso!=null && mi.desembolso.id!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar el Desembolso ?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SDesembolso', {
							accion: 'borrarDesembolso',
							id: mi.desembolso.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Desembolso borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Desembolso');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Desembolso que desea borrar');
			};
			
			mi.editar = function() {
				if(mi.desembolso!=null && mi.desembolso.id!=null){
					mi.fecha = moment(mi.desembolso.fecha, 'DD/MM/YYYY').toDate();
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Desembolso que desea editar');
			};

			$http.post('/SProyecto', { accion: 'obtenerProyectoPorId',id: $routeParams.proyecto_id }).success(
					function(response) {
						mi.proyectonombre = response.nombre;
			});
			
			mi.mostrarCalendar = function() {
			    mi.popup.abierto = true;
			  };
			  
			mi.irATabla = function() {
				mi.mostraringreso=false;
				mi.esnuevo=false;
			};
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalDesembolsos();
				}
			};

			mi.obtenerTotalDesembolsos = function(){
				$http.post('/SDesembolso', { accion: 'numeroDesembolsosPorProyecto', proyectoid:$routeParams.proyecto_id,
					filtro_fecha: mi.filtros['fecha'],
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion']  }).then(
						function(response) {
							mi.totalDesembolsos = response.data.totaldesembolsos;
							mi.paginaActual = 1;
							mi.cargarTabla(mi.paginaActual);
				});
			};
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'desembolso', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			};
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/desembolso/rv')
					$route.reload();
				else
					$location.path('/desembolso/rv');
			}
			
			mi.buscarTipoDesembolso = function(titulo, mensaje) {
				var instanciaModal = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'buscarDesembolsoTipo.jsp',
					controller : 'modalBuscarDesembolsoTipo',
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

				instanciaModal.result.then(function(selectedItem) {
					
					mi.desembolso.desembolsotipoid = selectedItem.id;
					mi.desembolso.desembolsotipo = selectedItem.nombre;

				}, function() {
				});
			};

			
} ]);


app.controller('modalBuscarDesembolsoTipo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'mensaje', modalBuscarDesembolsoTipo ]);

function modalBuscarDesembolsoTipo($uibModalInstance, $scope, $http, $interval,
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
	
	$http.post('/SDesembolsoTipo', {
		accion : 'numeroDesembolsoTipo'
	}).success(function(response) {
		mi.totalElementos = response.total;
		mi.elementosPorPagina = mi.totalElementos;
		mi.cargarData(1);
	});
	
	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID', width: 100, name : 'id', cellClass : 'grid-align-right',
		}, {
			displayName : 'Tipo desembolso',
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
					mi.seleccionarDesembolsoTipo);
		}
	}
	
	mi.seleccionarDesembolsoTipo = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};
	
	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post('/SDesembolsoTipo', {accion : 'getDesembolsotiposPagina'}).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.desembolsotipos;
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
			$utilidades.mensaje('warning', 'Debe seleccionar una ENTIDAD');
		}
	};
	
	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}