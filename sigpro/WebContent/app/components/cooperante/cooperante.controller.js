var app = angular.module('cooperanteController', []);

app.controller('cooperanteController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Cooperantes';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.cooperantes = [];
			mi.cooperante;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalCooperantes = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp';
				$utilidades.mensaje('primary','No tienes permiso de acceder a esta área');			
			}
			mi.filtros = [];
			
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
						{ name: 'codigo', width: 150, displayName: 'Código', 
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-model="grid.appScope.cooperantec.filtros[\'codigo\']" ng-keypress="grid.appScope.cooperantec.filtrar($event)"></input></div>'
						},
					    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-model="grid.appScope.cooperantec.filtros[\'nombre\']" ng-keypress="grid.appScope.cooperantec.filtrar($event)"></input></div>'
					    },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-model="grid.appScope.cooperantec.filtros[\'usuarioCreo\']" ng-keypress="grid.appScope.cooperantec.filtrar($event)"></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-model="grid.appScope.cooperantec.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.cooperantec.filtrar($event)"></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.cooperante = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.cooperantec.columnaOrdenada=sortColumns[0].field;
								grid.appScope.cooperantec.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.cooperantec.cargarTabla(grid.appScope.cooperantec.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.cooperantec.columnaOrdenada!=null){
									grid.appScope.cooperantec.columnaOrdenada=null;
									grid.appScope.cooperantec.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							 mi.obtenerTotalCooperantes();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'cooperantes', t: (new Date()).getTime()}).then(function(response){
							      if(response.data.success && response.data.estado!=''){
							    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
							    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
								      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
								      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      }
							      mi.obtenerTotalCooperantes();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SCooperante', { accion: 'getCooperantesPagina', pagina: pagina, numerocooperantes: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'],  filtro_codigo: mi.filtros['codigo'],
					filtro_usuario_creo: mi.filtros['usuarioCcreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion}).success(
						function(response) {
							mi.cooperantes = response.cooperantes;
							mi.gridOptions.data = mi.cooperantes;
							mi.mostrarcargando = false;
						});
			}
			
			mi.guardar=function(){
				if(mi.cooperante!=null && mi.cooperante.nombre!=null){
					$http.post('/SCooperante', {
						accion: 'guardarCooperante',
						esnuevo: mi.esnuevo,
						id: mi.cooperante.id,
						codigo: mi.cooperante.codigo,
						nombre: mi.cooperante.nombre,
						descripcion: mi.cooperante.descripcion
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Cooperante '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Cooperante');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.cooperante!=null && mi.cooperante.id!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar el Cooperante "'+mi.cooperante.nombre+'"?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SCooperante', {
							accion: 'borrarCooperante',
							id: mi.cooperante.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Cooperante borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Cooperante');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Cooperante que desea borrar');
			};

			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.cooperante = {};
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.cooperante!=null && mi.cooperante.id!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Cooperante que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
				mi.esnuevo=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'cooperantes', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/cooperante/rv')
					$route.reload();
				else
					$location.path('/cooperante/rv');
			}
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.cargarTabla(mi.paginaActual);
				}
			}
			
			mi.obtenerTotalCooperantes=function(){
				$http.post('/SCooperante', { accion: 'numeroCooperantes',
					filtro_nombre: mi.filtros['nombre'], filtro_codigo: mi.filtros['codigo'],
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']
				}).success(
				
						function(response) {
							mi.totalCooperantes = response.totalcooperantes;
							mi.cargarTabla(1);
						});
			}
			
			} ]);
