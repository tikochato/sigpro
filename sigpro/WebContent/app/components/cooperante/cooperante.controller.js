var app = angular.module('cooperanteController', []);

app.controller('cooperanteController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', '$uibModal', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$dialogoConfirmacion) {
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
			mi.filtros = [];
			
			mi.editarElemento = function (event) {
		        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
		        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
		        mi.editar();
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.cooperantec.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
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
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion}).success(
						function(response) {
							mi.cooperantes = response.cooperantes;
							mi.gridOptions.data = mi.cooperantes;
							mi.mostrarcargando = false;
						});
			}
			
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
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
							mi.cooperante.id=response.id;
							mi.cooperante.usuarioCreo=response.usuarioCreo;
							mi.cooperante.fechaCreacion=response.fechaCreacion;
							mi.cooperante.usuarioActualizo=response.usuarioactualizo;
							mi.cooperante.fechaActualizacion=response.fechaactualizacion;
							mi.esnuevo ? mi.esnuevo=!mi.esnuevo: mi.esnuevo=false;
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
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar el Cooperante "'+mi.cooperante.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SCooperante', {
								accion: 'borrarCooperante',
								id: mi.cooperante.id
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Cooperante borrado con éxito');
									mi.cooperante = null;
									mi.cargarTabla();
								}
								else
									$utilidades.mensaje('danger','Error al borrar el Cooperante');
							});
						}
					}, function(){
						
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
					mi.obtenerTotalCooperantes();
					mi.cargarTabla(mi.paginaActual);
					mi.gridApi.selection.clearSelectedRows();
					mi.cooperante = null;
				}
			}
			
			mi.obtenerTotalCooperantes=function(){
				$http.post('/SCooperante', { accion: 'numeroCooperantes',
					filtro_nombre: mi.filtros['nombre'], filtro_codigo: mi.filtros['codigo'],
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion']
				}).success(
				
						function(response) {
							mi.totalCooperantes = response.totalcooperantes;
							mi.cargarTabla(1);
						});
			}
			
			} ]);
