var app = angular.module('metaController', []);

app.controller('metaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Metas';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.metas = [];
			mi.meta;
			mi.mostraringreso=false;
			mi.esnueva = false;
			mi.totalMetas = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.metatipos = [];
			mi.metasunidades = [];
			
			mi.nombrePcp = "";
			mi.nombreTipoPcp = "";
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			
			mi.filtros = [];
			
			switch($routeParams.tipo){
				case "1": mi.nombreTipoPcp = "Proyecto"; break;
				case "2": mi.nombreTipoPcp = "Componente"; break;
				case "3": mi.nombreTipoPcp = "Producto"; break;
				case "4": mi.nombreTipoPcp = "Subproducto"; break;
				
			}
			
			$http.post('/SMeta', { accion: 'getPcp', id: $routeParams.id, tipo: $routeParams.tipo }).success(
					function(response) {
						mi.nombrePcp = response.nombre;
			});
			
			$http.post('/SMeta', { accion: 'getMetasTipos' }).success(
					function(response) {
						mi.metatipos = response.MetasTipos;
			});
			
			$http.post('/SMeta', { accion: 'getMetasUnidadesMedida' }).success(
					function(response) {
						mi.metaunidades = response.MetasUnidades;
			});
			
			$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
					function(response) {
						mi.datoTipos = response.datoTipos;
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.metac.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
						{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
							filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.metac.filtros[\'nombre\']" ng-keypress="grid.appScope.metac.filtrar($event)"></input></div>'
						},
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'unidadMedidaNombre', displayName: 'Unidad Medida', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'tipoMetaNombre', displayName: 'Tipo Meta', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.metac.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.metac.filtrar($event)"></input></div>'
					    },
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.metac.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.metac.filtrar($event)"></input></div>'
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.meta = row.entity;
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.metac.columnaOrdenada=sortColumns[0].field;
								grid.appScope.metac.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.metac.cargarTabla(grid.appScope.metac.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.metac.columnaOrdenada!=null){
									grid.appScope.metac.columnaOrdenada=null;
									grid.appScope.metac.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalMetas();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'metas', t: (new Date()).getTime()}).then(function(response){
					    	  if(response.data.success && response.data.estado!='')
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							  mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							  mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							  mi.obtenerTotalMetas();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SMeta', { accion: 'getMetasPagina', pagina: pagina, numerometas: $utilidades.elementosPorPagina, 
					id: $routeParams.id, tipo: $routeParams.tipo,
					filtro_nombre: mi.filtros['nombre'], 
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion 
				}).success(
						function(response) {
							mi.metas = response.Metas;
							mi.gridOptions.data = mi.metas;
							mi.mostrarcargando = false;
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			mi.guardar=function(){
				if(mi.meta!=null && mi.meta.nombre!='' && mi.meta.tipoMetaId>0 && mi.meta.unidadMedidaId>0 && mi.meta.datoTipoId>0 ){
					$http.post('/SMeta', {
						accion: 'guardarMeta',
						esnueva: mi.esnueva,
						id: mi.meta.id,
						nombre: mi.meta.nombre,
						descripcion: mi.meta.descripcion,
						tipoMetaId:  mi.meta.tipoMetaId,
						unidadMedidaId: mi.meta.unidadMedidaId,
						datoTipoId: mi.meta.datoTipoId,
						objetoTipo:  $routeParams.tipo,
						objetoId:$routeParams.id
						
					}).success(function(response){
						if(response.success){
							mi.meta.id = response.id;
							mi.meta.usuarioCreo = response.usuarioCreo;
							mi.meta.fechaCreacion = response.fechaCreacion;
							mi.meta.usuarioActualizo = response.usuarioactualizo;
							mi.meta.fechaActualizacion = response.fechaactualizacion;
							$utilidades.mensaje('success','Meta '+(mi.esnueva ? 'creada' : 'guardada')+' con éxito');
							mi.obtenerTotalMetas();
							mi.esnueva = false;
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnueva ? 'crear' : 'guardar')+' la Meta');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.meta!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Meta "'+mi.meta.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SMeta', {
								accion: 'borrarMeta',
								id: mi.meta.id
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Meta borrada con éxito');
									mi.meta = null;
									mi.obtenerTotalMetas();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la Meta');
							});
						}
					}, function(){
						
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Meta que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnueva = true;
				mi.meta = {};
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.meta!=null && mi.meta.id!=null){
					mi.mostraringreso = true;
					mi.esnueva = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Meta que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'metas', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/meta/'+ $routeParams.id + '/' + $routeParams.tipo + '/rv')
					$route.reload();
				else
					$location.path('/meta/'+ $routeParams.id + '/' + $routeParams.tipo + '/rv');
			}
						
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalMetas();
					mi.gridApi.selection.clearSelectedRows();
					mi.meta = null;
				}
			}
			
			mi.irAMetaValores=function(){
				if(mi.meta.id!=null){
					$location.path('/metavalor/'+ mi.meta.id +'/'+ mi.meta.datoTipoId );
				}
			}
			
			mi.obtenerTotalMetas =  function() {
				$http.post('/SMeta', { accion: 'numeroMetas', id: $routeParams.id, tipo: $routeParams.tipo  }).success(
						function(response) {
							mi.totalMetas = response.totalmetas;
							mi.cargarTabla(1);
						});
				
				}
			}

]);