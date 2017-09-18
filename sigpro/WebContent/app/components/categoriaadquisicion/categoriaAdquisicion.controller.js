var app = angular.module('categoriaAdquisicionController',['ngTouch']);

app.controller('categoriaAdquisicionController', ['$scope', '$http', '$interval','Utilidades','i18nService','uiGridConstants','dialogoConfirmacion','$window','$routeParams','$location','uiGridConstants','$route',
	function($scope, $http, $interval,$utilidades,i18nService,uiGridConstants,$dialogoConfirmacion, $window, $routeParams,$location,uiGridConstants,$route){
	var mi = this;
	
	$window.document.title = $utilidades.sistema_nombre+' - Categoría de Adquisición';
	i18nService.setCurrentLang('es');
	mi.mostrarcargando=true;
	mi.mostraringreso=false;
	mi.esnuevo = false;
	mi.objetoTipoNombre = "";
	mi.objetoNombre="";
	mi.paginaActual = 1;
	mi.objetoid = $routeParams.objeto_id;
	mi.objetotipo = $routeParams.objeto_tipo;
	mi.categoriaAdquisiciones = [];
	mi.categoriaAdquisicion;
	mi.totalCategoriaAdquisiciones = 0;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.filtros = [];
	
	mi.reiniciarVista=function(){
		if($location.path()==('/categoriaadquisicion/rv'))
			$route.reload();
		else
			$location.path('/categoriaadquisicion/rv');
	}
	
	mi.nuevo = function() {
		mi.mostraringreso=true;
		mi.esnuevo = true;
		mi.categoriaAdquisicion = {};
		mi.gridApi.selection.clearSelectedRows();
	};
	
	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
        mi.editar();
    };
    
	mi.editar = function() {
		if(mi.categoriaAdquisicion !=null && mi.categoriaAdquisicion.id!=null){
			mi.esnuevo = false;
			mi.mostraringreso = true;
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar la Categoría de adquisición que desea editar');
	}
	
	mi.borrar = function(ev) {
		if(mi.categoriaAdquisicion!=null && mi.categoriaAdquisicion.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar la Categoría de adquisicion "'+mi.categoriaAdquisicion.nombre+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					$http.post('/SCategoriaAdquisicion', {
						accion: 'borrarCategoria',
						id: mi.categoriaAdquisicion.id,
						t: new Date().getTime()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Categoría de adquisición borrada con éxito');
							mi.categoriaAdquisicion = null;
							mi.obtenerTotalcategoriaAdquisicion();
						}
						else
							$utilidades.mensaje('danger','Error al borrar la Categoría de adquisición');
					});
				}
			}, function(){
				
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar la Actividad que desea borrar');
	};

	
	mi.guardar = function(){
		if(mi.categoriaAdquisicion != null && mi.categoriaAdquisicion.nombre != null){
			$http.post('/SCategoriaAdquisicion', {
				accion: 'guardarCategoria',
				esnuevo: mi.esnuevo,
				id: mi.categoriaAdquisicion.id,
				nombre: mi.categoriaAdquisicion.nombre,
				descripcion: mi.categoriaAdquisicion.descripcion,
				t: new Date().getTime()
			}).success(function(response){
				mi.categoriaAdquisicion.usuarioCreo = response.usuarioCreo;
				mi.categoriaAdquisicion.fechaCreacion = response.fechaCreacion;
				mi.categoriaAdquisicion.usuarioActualizo = response.usuarioActualizo;
				mi.categoriaAdquisicion.fechaActualizacion = response.fechaActualizacion;
				$utilidades.mensaje('success','Categoría de adquisición '+(mi.esnuevo ? 'creada' : 'guardada')+' con éxito');
				mi.obtenerTotalcategoriaAdquisicion();
				mi.esnuevo = false;	
			})
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	}
	
	mi.irATabla = function() {
		mi.mostraringreso=false;
		mi.esNuevo = false;
	}

	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'categoriaAdquisicion', estado: JSON.stringify(estado), t: (new Date()).getTime() };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}

	mi.obtenerTotalcategoriaAdquisicion=function(){
		$http.post('/SCategoriaAdquisicion', { accion: 'numeroCategoriaPorObjeto', filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			t: new Date().getTime() }).success(
				function(response) {
					mi.totalCategoriaAdquisiciones = response.totalCategoriaAdquisiciones;
					mi.cargarTabla(1);
		});
	};
	
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SCategoriaAdquisicion', { accion: 'getCategoriaAdquisicionPagina', pagina: pagina, numerocategoriaadquisicion: $utilidades.elementosPorPagina,
			objetoid: $routeParams.objeto_id, tipo: mi.objetotipo,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion,
			t: new Date().getTime()
		}).success(
				function(response) {
					mi.categoriaAdquisiciones = response.categoriaAdquisiciones;
					mi.gridOptions.data = mi.categoriaAdquisiciones;
					mi.mostrarcargando = false;
					mi.paginaActual = pagina;
				});
	}
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalcategoriaAdquisicion();
			mi.gridApi.selection.clearSelectedRows();
			mi.categoriaAdquisiciones = null;
		}
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
		    rowTemplate: '<div ng-dblclick="grid.appScope.controller.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		    columnDefs : [
				{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)"></input></div>'
			    },
			    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
			    { name: 'usuarioCreo', displayName: 'Usuario Creación',
			    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.controller.filtrar($event)"></input></div>'
			    },
			    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
			    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.controller.filtrar($event)"></input></div>'
			    }
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope,function(row) {
					mi.categoriaAdquisicion = row.entity;
					mi.categoriaAdquisicion.fechaInicio = moment(mi.categoriaAdquisicion.fechaInicio,'DD/MM/YYYY').toDate();
					mi.categoriaAdquisicion.fechaFin = moment(mi.categoriaAdquisicion.fechaFin,'DD/MM/YYYY').toDate();
				});

				gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
					if(sortColumns.length==1){
						grid.appScope.controller.columnaOrdenada=sortColumns[0].field;
						grid.appScope.controller.ordenDireccion = sortColumns[0].sort.direction;
						for(var i = 0; i<sortColumns.length-1; i++)
							sortColumns[i].unsort();
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
					mi.obtenerTotalcategoriaAdquisicion();
			    }
			    else{
			    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'categoriaadquisicion', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					      mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						  mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						  mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);

					      mi.obtenerTotalcategoriaAdquisicion();
					  });

			    }
			}
	};


}])

app.directive('showFocus', function($timeout) {
      return function(scope, element, attrs) {
        scope.$watch(attrs.showFocus,
          function (newValue) {
            $timeout(function() {
                element[0].focus();             
            });
          },true);
      };   
    });