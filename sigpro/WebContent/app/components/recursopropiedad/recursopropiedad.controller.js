var app = angular.module('recursopropiedadController', []);

app.controller('recursopropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Propiedades de Recurso';
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
			
			$http.post('/SDatoTipo', { accion: 'cargarCombo', t: (new Date()).getTime() }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.recursopropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
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
				$http.post('/SRecursoPropiedad', { accion: 'getRecursoPropiedadPagina', pagina: pagina, numerorecursopropiedad: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
					}).success(
						function(response) {
							mi.recursopropiedades = response.recursopropiedades;
							mi.gridOptions.data = mi.recursopropiedades;
							mi.mostrarcargando = false;
							mi.paginaActual = pagina;
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
						datoTipoId: mi.datoTipoSeleccionado.id,
						t: (new Date()).getTime()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad de Recurso '+(mi.esnuevo ? 'creada' : 'guardada')+' con éxito');
							mi.recursopropiedad.id = response.id;
							mi.recursopropiedad.usuarioCreo = response.usuarioCreo;
							mi.recursopropiedad.fechaCreacion = response.fechaCreacion;
							mi.recursopropiedad.usuarioActualizo = response.usuarioactualizo;
							mi.recursopropiedad.fechaActualizacion = response.fechaactualizacion;
							
							mi.esnuevo = false;
							mi.obtenerTotalRecursoPropiedades();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' la Propiedad del Recurso');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.recursopropiedad!=null && mi.recursopropiedad.id!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Propiedad del Recurso "'+mi.recursopropiedad.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SRecursoPropiedad', {
								accion: 'borrarRecursoPropiedad',
								id: mi.recursopropiedad.id,
								t: (new Date()).getTime()
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Propiedad del Recurso borrada con éxito');
									mi.recursopropiedad = null;
									mi.obtenerTotalRecursoPropiedades();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la Propiedad del Recurso');
							});
						}
					}, function(){
						
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad del Recurso que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.recursopropiedad = {};
				mi.datoTipoSeleccionado = null;
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.recursopropiedad!=null && mi.recursopropiedad.id!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad del Recurso que desea editar');
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
					mi.gridApi.selection.clearSelectedRows();
					mi.recursopropiedad = null;
				}
			}
			mi.obtenerTotalRecursoPropiedades=function(){
				$http.post('/SRecursoPropiedad', { accion: 'numeroRecursoPropiedades', filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'], t: (new Date()).getTime() }).success(
						function(response) {
							mi.totalRecursoPropiedades = response.totalrecursopropiedades;
							mi.cargarTabla(1);
				});
				
				
				}	
			} 
]);