var app = angular.module('prestamotipoController', [ 'ngTouch']);

app.controller('prestamotipoController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion', 
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Tipo de Préstamo';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.prestamotipos = [];
		mi.prestamotipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalPestamotipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
		mi.filtros = [];
		mi.orden = null;

		mi.mostrarcargandoProyProp=true;
		
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.prestamotipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left'
				    	,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamotipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.prestamotipoc.filtrar($event)" style="width:175px;"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación' 
				    	,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamotipoc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.prestamotipoc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamotipoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.prestamotipoc.filtrar($event)" ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.prestamotipo = row.entity;
					});
					
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.prestamotipoc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.prestamotipoc.ordenDireccion = sortColumns[0].sort.direction;
								grid.appScope.prestamotipoc.cargarTabla(grid.appScope.prestamotipoc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.prestamotipoc.columnaOrdenada!=null){
									grid.appScope.prestamotipoc.columnaOrdenada=null;
									grid.appScope.prestamotipoc.ordenDireccion=null;
								}
							}
								
						} );
					

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalprestamotipos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'prestamoTipos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!='')
				    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalprestamotipos();
						  });
				    }
				}
		};
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SPrestamoTipo', { accion: 'getPrestamoTipoPagina', pagina: pagina, numeroprestamotipo: $utilidades.elementosPorPagina
				,filtro_nombre: mi.filtros['nombre'] 
				,filtro_usuario_creo: mi.filtros['usuario_creo']
			    ,filtro_fecha_creacion: mi.filtros['fecha_creacion']
			    ,columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
			    }).success(
			
					function(response) {
						mi.prestamotipos = response.poryectotipos;
						mi.gridOptions.data = mi.prestamotipos;
						mi.mostrarcargando = false;
					});
		}

		mi.guardar=function(){
			if(mi.prestamotipo!=null  && mi.prestamotipo.nombre!=''){
				$http.post('/SPrestamoTipo', {
					accion: 'guardarPrestamotipo',
					esnuevo: mi.esnuevo,
					id: mi.prestamotipo.id,
					nombre: mi.prestamotipo.nombre,
					descripcion: mi.prestamotipo.descripcion,
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo de préstamo ' + (mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.prestamotipo.id = response.id;
						mi.prestamotipo.usuarioCreo=response.usuarioCreo;
						mi.prestamotipo.fechaCreacion=response.fechaCreacion;
						mi.prestamotipo.usuarioActualizo=response.usuarioactualizo;
						mi.prestamotipo.fechaActualizacion=response.fechaactualizacion;
						
						mi.obtenerTotalprestamotipos();

					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo de préstamo');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.editar = function() {
			if(mi.prestamotipo!=null && mi.prestamotipo.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				$utilidades.setFocus(document.getElementById("nombre"));
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de préstamo que desea editar');
		}


		mi.borrar = function(ev) {
			if(mi.prestamotipo!=null && mi.prestamotipo.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el tipo de préstamo "'+mi.prestamotipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SPrestamoTipo', {
							accion: 'borrarPrestamoTipo',
							id: mi.prestamotipo.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo de préstamo borrado con éxito');
								mi.prestamotipo = null;
								mi.obtenerTotalprestamotipos();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo préstmoa');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de préstamo que desea borrar');
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.prestamotipo = {};
			mi.gridApi.selection.clearSelectedRows();
			$utilidades.setFocus(document.getElementById("nombre"));
		};

		mi.irATabla = function() {
			mi.mostraringreso=false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'prestamotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()=='/prestamotipo/rv')
				$route.reload();
			else
				$location.path('/prestamotipo/rv');
		}

		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalprestamotipos();
				mi.gridApi.selection.clearSelectedRows();
				mi.prestamotipo = null;
			}
		}
		
		mi.obtenerTotalprestamotipos = function(){
			$http.post('/SPrestamoTipo', { accion: 'numeroPrestamoTipos',
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime() }).then(
					function(response) {
						mi.totalPrestamotipos = response.data.totalprestamotipos;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}
} ]);
