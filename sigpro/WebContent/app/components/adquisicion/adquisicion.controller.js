var app = angular.module('adquisicionController',['ngTouch','ngAnimate','ui.grid.edit', 'ui.grid.rowEdit']);
app.controller('adquisicionController',['$scope', '$http', '$interval','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion', '$filter','$uibModal','$window','$routeParams','$location','$route',
	function($scope, $http, $interval,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q,dialogoConfirmacion, $filter,$uibModal, $window,$routeParams, $location,$route){
	var mi = this;
	
	$window.document.title = $utilidades.sistema_nombre+' - Adquisiciones';
	i18nService.setCurrentLang('es');
	mi.adquisiciones = [];
	mi.adquisicion;
	mi.totalAdquisiciones = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.objetoid = $routeParams.objeto_id;
	mi.objetotipo = $routeParams.objeto_tipo;
	mi.filtros = [];
	
	$http.post('/SObjeto', { accion: 'getObjetoPorId', id: $routeParams.objeto_id, tipo: mi.objetotipo, t: new Date().getTime()}).success(
			function(response) {
				mi.objetoid = response.id;
				mi.objetoNombre = response.nombre;
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
		    rowTemplate: '<div ng-dblclick="grid.appScope.controller.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		    columnDefs : [
		    	{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
		    	{ name: 'tipoAdquisicionNombre', width: 200, displayName: 'Tipo de Adquisición', cellClass: 'grid-align-left', enableFiltering: false },
				{ name: 'unidadMedida', width: 200, displayName: 'Unidad Medida',cellClass: 'grid-align-left',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'unidadMedida\']" ng-keypress="grid.appScope.controller.filtrar($event)"></input></div>'
				},
				{ name: 'categoriaAdquisicionNombre', width: 200, displayName: 'Categoría Adquisición',cellClass: 'grid-align-left', enableFiltering: false },
				{ name: 'monto', width: 100, displayName: 'Monto total', cellClass: 'grid-align-right', type: 'number', cellFilter: 'currency:"Q" : 2', enableFiltering: false },
				{ name: 'usuarioCreo', cellClass: 'grid-align-left', displayName: 'Usuario Creación',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuarioCreo\']"  ng-keypress="grid.appScope.controller.filtrar($event)" ></input></div>'
			    },
			    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
			    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.controller.filtrar($event)" ></input></div>'
			    }
		    ],
		    onRegisterApi: function(gridApi) {
		    	mi.gridApi = gridApi;
		    	gridApi.selection.on.rowSelectionChanged($scope,function(row) {
					mi.adquisicion = row.entity;
				});
		    	
		    	gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
					if(sortColumns.length==1){
						grid.appScope.controller.columnaOrdenada=sortColumns[0].field;
						grid.appScope.controller.ordenDireccion = sortColumns[0].sort.direction;
						grid.appScope.controller.cargarTabla(grid.appScope.controller.paginaActual);
					}
					else if(sortColumns.length>1){
						sortColumns[0].unsort();
					}
					else{
						if(grid.appScope.controller.columnaOrdenada!=null){
							grid.appScope.controller.columnaOrdenada=null;
							grid.appScope.controller.ordenDireccion=null;
						}
					}
				} );
		    	
		    	if($routeParams.reiniciar_vista=='rv'){
					mi.guardarEstado();
					mi.obtenerTotalAdquisiciones();
			    }
			    else{
			    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'adquisiciones', t: (new Date()).getTime()}).then(function(response){
			    		  if(response.data.success && response.data.estado!=''){
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
			    		  }
			    		  mi.obtenerTotalAdquisiciones();
					  });
			    }
		    }
	}
	
	mi.obtenerTotalAdquisiciones = function(){
		$http.post('/SPlanAdquisiciones', { accion: 'totalAdquisicionesPorObjeto',objetoid:$routeParams.objeto_id, objetoTipo: $routeParams.objeto_tipo,
			filtro_unidad_medida: mi.filtros['unidadMedida'], filtro_usuario_creo: mi.filtros['usuarioCreo'], 
			filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: new Date().getTime() }).success(
				function(response) {
					mi.totalAdquisiciones = response.totalAdquisiciones;
					if(mi.totalAdquisiciones > 0)
						mi.cargarTabla(1);
		});
	}
	
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SPlanAdquisiciones', { accion: 'getMetasPagina', pagina: pagina, numerometas: $utilidades.elementosPorPagina, 
			id: $routeParams.id, tipo: $routeParams.tipo,
			filtro_unidad_medida: mi.filtros['unidadMedida'], filtro_usuario_creo: mi.filtros['usuarioCreo'], 
			filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: new Date().getTime() }).success(
				function(response) {
					mi.adquisiciones = response.adquisiciones;
					mi.gridOptions.data = mi.adquisiciones;
					mi.mostrarcargando = false;
					mi.paginaActual = pagina;
				});
	}
	
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'adquisiciones', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
		$http.post('/SEstadoTabla', tabla_data).then(function(response){
			
		});
	};
	
	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}
	
	mi.reiniciarVista=function(){
		if($location.path()=='/adquisicion/'+$routeParams.objeto_id+'/'+$routeParams.objeto_tipo)
			$route.reload();
		else
			$location.path('/adquisicion/'+$routeParams.objeto_id+'/'+$routeParams.objeto_tipo);
	}
	
	mi.nuevo = function(){
		mi.mostraringreso = true;
		mi.esnuevo=true;
	}
	
	mi.editar = function(){
		mi.mostraringreso = true;
		mi.esnuevo=false;
	}
	
	mi.irATabla = function(){
		mi.mostraringreso = false;
	}
	
	
}]);