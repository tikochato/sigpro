var app = angular.module('subcomponentepropiedadController', []);

app.controller('subcomponentepropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Subcomponente Propiedad';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.subcomponentepropiedades = [];
			mi.subcomponentepropiedad;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalSubComponentePropiedades = 0;
			
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.tipodatos = [];
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			mi.filtros = [];
			mi.orden = null;
			
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.subcomponentepropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				    useExternalSorting: true,
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left', 
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentepropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.subcomponentepropiedadc.filtrar($event)"></input></div>',
					    },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentepropiedadc.filtros[\'usuarioCreo\']" ng-keypress="grid.appScope.subcomponentepropiedadc.filtrar($event)" ></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentepropiedadc.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.subcomponentepropiedadc.filtrar($event)" style="width:80px;" ></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.subcomponentepropiedad = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.subcomponentepropiedadc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.subcomponentepropiedadc.ordenDireccion = sortColumns[0].sort.direction;
								
								grid.appScope.subcomponentepropiedadc.cargarTabla(grid.appScope.subcomponentepropiedadc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.subcomponentepropiedadc.columnaOrdenada!=null){
									grid.appScope.subcomponentepropiedadc.columnaOrdenada=null;
									grid.appScope.subcomponentepropiedadc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalSubComponentePropiedades();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'subcomponentepropiedades', t: (new Date()).getTime()}).then(function(response){
							      if(response.data.success && response.data.estado!=''){
							    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
							    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
								      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
								      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      }
							      mi.obtenerTotalSubComponentePropiedades();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SSubComponentePropiedad', { accion: 'getSubComponentePropiedadPagina', pagina: pagina, 
					numerosubcomponentepropiedades: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()		
					}).success(
						function(response) {
							mi.subcomponentepropiedades = response.subcomponentepropiedades;
							mi.gridOptions.data = mi.subcomponentepropiedades;
							mi.mostrarcargando = false;
							mi.paginaActual = pagina;
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			
			mi.guardar=function(){
				if(mi.subcomponentepropiedad!=null && mi.subcomponentepropiedad.nombre!=null){
					$http.post('/SSubComponentePropiedad', {
						accion: 'guardarSubComponentePropiedad',
						esnuevo: mi.esnuevo,
						id: mi.subcomponentepropiedad.id,
						nombre: mi.subcomponentepropiedad.nombre,
						descripcion: mi.subcomponentepropiedad.descripcion,
						datoTipoId: mi.datotipo.id, t: (new Date()).getTime()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Subcomponente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.subcomponentepropiedad.id = response.id;
							mi.subcomponentepropiedad.usuarioCreo=response.usuarioCreo;
							mi.subcomponentepropiedad.fechaCreacion=response.fechaCreacion;
							mi.subcomponentepropiedad.usuarioActualizo=response.usuarioactualizo;
							mi.subcomponentepropiedad.fechaActualizacion=response.fechaactualizacion;
							mi.esnuevo = false;
							mi.obtenerTotalSubComponentePropiedades();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Subcomponente');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.subcomponentepropiedad!=null && mi.subcomponentepropiedad.id!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Propiedad Subcomponente "'+mi.subcomponentepropiedad.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SSubComponentePropiedad', {
								accion: 'borrarSubComponentePropiedad',
								id: mi.subcomponentepropiedad.id, t: (new Date()).getTime()
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Propiedad Subcomponente borrado con éxito');
									mi.subcomponentepropiedad = null;
									mi.cargarTabla();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la Propiedad Subcomponente');
							});
						}
					}, function(){
						
					});		
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Subcomponente que desea borrar');
			};

			mi.nuevo = function() {
				mi.datotipo = null;
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.subcomponentepropiedad = {};
				mi.gridApi.selection.clearSelectedRows();
				$utilidades.setFocus(document.getElementById("nombre"));
			};

			mi.editar = function() {
				if(mi.subcomponentepropiedad!=null && mi.subcomponentepropiedad.id!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
					mi.datotipo = {
							"id" : mi.subcomponentepropiedad.datotipoid,
							"nombre" : mi.subcomponentepropiedad.datotiponombre
					}
					
					$utilidades.setFocus(document.getElementById("nombre"));
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Subcomponente que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'subcomponentepropiedades', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/subcomponentepropiedad/rv')
					$route.reload();
				else
					$location.path('/subcomponentepropiedad/rv');
			}
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalSubComponentePropiedades();
					mi.gridApi.selection.clearSelectedRows();
					mi.subcomponentepropiedad = null;
				}
			}

			mi.obtenerTotalSubComponentePropiedades = function(){
				$http.post('/SSubComponentePropiedad', { accion: 'numeroSubComponentePropiedades',
					filtro_nombre: mi.filtros['nombre'],
					filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime()  }).then(
						function(response) {
							mi.totalSubComponentePropiedades = response.data.totalsubcomponentepropiedades;
							mi.paginaActual = 1;
							mi.cargarTabla(mi.paginaActual);
				});
			}
			
			
			$http.post('/SDatoTipo', { accion: 'cargarCombo', t: (new Date()).getTime() }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
		} ]);