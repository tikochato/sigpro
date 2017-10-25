var app = angular.module('rolunidadejecutoraController', []);

app.controller('rolunidadejecutoraController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Roles Unidad Ejecutora';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.mostraringreso=false;
			mi.esnueva = false;
			mi.totalroles = 0;
			mi.paginaActual = 1;
			mi.rolPredeterminado = false;
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.rolc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
						{ name: 'nombre', width: 300, displayName: 'Nombre',cellClass: 'grid-align-left',
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.rolc.filtros[\'nombre\']"  ng-keypress="grid.appScope.rolc.filtrar($event,1)"></input></div>'
						},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.rolc.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.rolc.filtrar($event,2)"></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.rolc.filtros[\'fecha_creacion\']"  ng-keypress="grid.appScope.rolc.filtrar($event,3)"></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.rol = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.rolc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.rolc.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.rolc.cargarTabla(grid.appScope.rolc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.rolc.columnaOrdenada!=null){
									grid.appScope.rolc.columnaOrdenada=null;
									grid.appScope.rolc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalRoles();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'rolesunidadejecutora', t: (new Date()).getTime()}).then(function(response){
					    		  if(response.data.success && response.data.estado!='')
					    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							      mi.obtenerTotalRoles();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SRolUnidadEjecutora', { accion: 'getRolesUnidadEjecutora', pagina: pagina, numerorolunidadejecutora: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
				}).success(
				
						function(response) {
							mi.roles = response.rolesUnidadEjecutora;
							mi.gridOptions.data = mi.roles;
							mi.mostrarcargando = false;
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			
			mi.guardar=function(){
				if(mi.rol!=null && mi.rol.nombre!=null){
					$http.post('/SRolUnidadEjecutora', {
						accion: 'guardar',
						esnueva: mi.esnueva,
						id: mi.rol.id,
						nombre: mi.rol.nombre,
						rolPredeterminado: mi.rolPredeterminado ? 1 : 0
						
					}).success(function(response){
						if(response.success){
							mi.rol.id = response.id;
							mi.rol.usuarioCreo = response.usuarioCreo;
							mi.rol.fechaCreacion = response.fechaCreacion;
							mi.rol.usuarioActualizo = response.usuarioactualizo;
							mi.rol.fechaActualizacion = response.fechaactualizacion;
							mi.esnueva = false;
							$utilidades.mensaje('success','Rol '+(mi.esnueva ? 'creado' : 'guardado')+' con éxito');
							mi.obtenerTotalRoles();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnueva ? 'crear' : 'guardar')+' el rol');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.rol!=null && mi.rol.id!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar el rol "'+mi.rol.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SRolUnidadEjecutora', {
								accion: 'borrar',
								id: mi.rol.id
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Rol borrado con éxito');
									mi.rol = null;
									mi.obtenerTotalRoles();
								}
								else
									$utilidades.mensaje('danger','Error al borrar el rol');
							});
						}
					}, function(){
						
					});	
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el rol que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnueva = true;
				mi.rol = {};
				mi.rolPredeterminado = false;
				mi.gridApi.selection.clearSelectedRows();
				$utilidades.setFocus(document.getElementById("nombre"));
			};

			mi.editar = function() {
				if(mi.rol!=null && mi.rol.id!=null){
					mi.rolPredeterminado = mi.rol.rolPredeterminado == 1;
					mi.mostraringreso = true;
					mi.esnueva = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el rol que desea editar');
				$utilidades.setFocus(document.getElementById("nombre"));
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
				mi.esnueva=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'rolesunidadejecutora', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/rolunidadejecutora/rv')
					$route.reload();
				else
					$location.path('/rolunidadejecutora/rv');
			}
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalRoles();
					mi.gridApi.selection.clearSelectedRows();
					mi.rol = null;
				}
			}
			
			mi.obtenerTotalRoles = function(){
				$http.post('/SRolUnidadEjecutora', { accion: 'numeroRolesUnidadEjecutora', filtro_nombre:mi.filtros["nombre"] , filtro_usuario_creo:mi.filtros["usuario_creo"] 
				, filtro_fecha_creacion:mi.filtros["fecha_creacion"],t: (new Date()).getTime()}).success(
				
						function(response) {
							mi.totalroles = response.totalrolesunidadejecutora;
							mi.cargarTabla(1);
						});
				
				} 
			}
]);
