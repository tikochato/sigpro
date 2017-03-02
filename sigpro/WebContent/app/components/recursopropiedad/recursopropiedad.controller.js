var app = angular.module('recursopropiedadController', []);

app.controller('recursopropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Recurso Propiedad';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.recursopropiedades = [];
			mi.recursopropiedad;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalRecursoPropiedades = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.tipodatos = [];
			mi.datoTipoSeleccionado;
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			mi.filtros = [];
			
			$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
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
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursopropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.recursopropiedadc.filtrar($event)"></input></div>'
					    },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursopropiedadc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.recursopropiedadc.filtrar($event)"></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.recursopropiedadc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.recursopropiedadc.filtrar($event)"></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.recursopropiedad = row.entity;
							mi.tipodatos.forEach(function(tipo,index){
								if(tipo.id==mi.recursopropiedad.datotipoid)
									mi.datoTipoSeleccionado=tipo;
							});
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.recursopropiedadc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.recursopropiedadc.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.recursopropiedadc.cargarTabla(grid.appScope.recursopropiedadc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.recursopropiedadc.columnaOrdenada!=null){
									grid.appScope.recursopropiedadc.columnaOrdenada=null;
									grid.appScope.recursopropiedadc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalRecursoPropiedades();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'recursopropiedades', t: (new Date()).getTime()}).then(function(response){
					    		  if(response.data.success && response.data.estado!='')
					    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      mi.obtenerTotalRecursoPropiedades();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SRecursoPropiedad', { accion: 'getRecursoPropiedadPagina', pagina: pagina, numerorecursopropiedades: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
					}).success(
						function(response) {
							mi.recursopropiedades = response.recursopropiedades;
							mi.gridOptions.data = mi.recursopropiedades;
							mi.mostrarcargando = false;
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			mi.guardar=function(){
				if(mi.recursopropiedad!=null && mi.recursopropiedad.nombre!=''){
					$http.post('/SRecursoPropiedad', {
						accion: 'guardarRecursoPropiedad',
						esnuevo: mi.esnuevo,
						id: mi.recursopropiedad.id,
						nombre: mi.recursopropiedad.nombre,
						descripcion: mi.recursopropiedad.descripcion,
						datoTipoId: mi.datoTipoSeleccionado.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Recurso '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.recursopropiedad.id = response.id;
							mi.esnuevo = false;
							mi.obtenerTotalRecursoPropiedades();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Recurso');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.recursopropiedad!=null && mi.recursopropiedad.id!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar la Prppiedad Recurso "'+mi.recursopropiedad.nombre+'"?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SRecursoPropiedad', {
							accion: 'borrarRecursoPropiedad',
							id: mi.recursopropiedad.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Propiedad Recurso borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar la Propiedad Recurso');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Recurso que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.recursopropiedad = {};
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.recursopropiedad!=null && mi.recursopropiedad.id!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Recurso que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'recursopropiedad', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/recursopropiedad/rv')
					$route.reload();
				else
					$location.path('/recursopropiedad/rv');
			}
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalRecursoPropiedades();
				}
			}
			mi.obtenerTotalRecursoPropiedades=function(){
				$http.post('/SRecursoPropiedad', { accion: 'numeroRecursoPropiedades', filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(
						function(response) {
							mi.totalRecursoPropiedades = response.totalrecursopropiedades;
							mi.cargarTabla(1);
				});
				
				
				}	
			} 
]);