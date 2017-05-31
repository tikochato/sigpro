var app = angular.module('responsabletipoController', []);

app.controller('responsabletipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','dialogoConfirmacion',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
	var mi = this;
	
	mi.totalResponsablesTipos = 0;
	mi.mostrarcargando=true;
	mi.responsableTipo = null;
	mi.paginaActual = 1;
	mi.esnuevo = false;
	mi.filtros = [];
	mi.entidades = [];
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	
	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}
	
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'responsableTipo', estado: JSON.stringify(estado) };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}
	
	mi.reiniciarVista=function(){
		if($location.path()=='/responsabletipo/rv')
			$route.reload();
		else
			$location.path('/responsabletipo/rv');
	}
	
	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}
	
	mi.nuevo = function(){
		mi.esNuevo = true;
		mi.responsableTipo = {};
		mi.esColapsado = true;
		mi.gridApi.selection.clearSelectedRows();
	}
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalResponsablesTipos();
			mi.gridApi.selection.clearSelectedRows();
			mi.responsableTipo = null;
		}
	};
	
	mi.guardar = function(esvalido){
		if(mi.responsableTipo != null){
			$http.post('/SResponsableTipo', 
			{
				accion: 'guardar',
				id: mi.responsableTipo.id,
				nombre: mi.responsableTipo.nombre,
				esnuevo : mi.esNuevo,
				descripcion: mi.responsableTipo.descripcion,
			}).then(function(response){
				if (response.data.success)
				{
					mi.responsableTipo.id = response.data.id;
					mi.responsableTipo.usuarioCreo = response.data.usuarioCreo;
					mi.responsableTipo.fechaCreacion = response.data.fechaCreacion;
					mi.responsableTipo.usuarioactualizo = response.data.usuarioactualizo;
					mi.responsableTipo.fechaactualizacion = response.data.fechaactualizacion;
					$utilidades.mensaje('success','Tipo responsable '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
					mi.obtenerTotalResponsablesTipos();
					mi.esNuevo = false;
				}
			})
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	}
	
	mi.editar = function(){
		if(mi.responsableTipo!= null && mi.responsableTipo.id!= null){
			mi.esNuevo = false;
			mi.esColapsado = true;
		}else
			$utilidades.mensaje('warning','Debe seleccionar el tipo de responsable que desea editar');
	}
	
	mi.borrar = function(ev) {
		if(mi.responsableTipo !=null && mi.responsableTipo.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el tipo de Responsable "'+mi.responsableTipo.nombre+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					$http.post('/SResponsableTipo', {
						accion: 'borrarResponsableTipo',
						id: mi.responsableTipo.id,
						t:moment().unix()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Tipo responsable borrado con éxito');
							mi.responsableTipo = null;
							mi.obtenerTotalResponsablesTipos();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el tipo de responsable');
					});
				}
			}, function(){
				
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Préstamo que desea borrar');
	};
	
	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOpciones.data[filaId]);
        mi.editar();
    };
	
	mi.gridOpciones = {
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
				{ name: 'nombre', width: 200 ,displayName: 'Nombre',cellClass: 'grid-align-left',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
				},
				{ name: 'descripcion', displayName: 'Descripcion', cellClass: 'grid-align-left', enableFiltering: false},
				{ name: 'usuarioCreo', displayName: 'Usuario Creación',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.controller.filtrar($event)" style="width:90px;"></input></div>'
				},
			    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:80px;" ></input></div>'
			    }
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope,function(row) {
					mi.responsableTipo = row.entity;
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
					mi.obtenerTotalResponsablesTipos();
			    }
			    else{
			    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'responsableTipo', t: (new Date()).getTime()}).then(function(response){
			    		  if(response.data.success && response.data.estado!='')
			    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
				    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
					      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
					      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
					      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
					      mi.obtenerTotalResponsablesTipos();
					  });
			    }
			}
		};
	
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';
	}
	mi.obtenerTotalResponsablesTipos = function(){
		$http.post('/SResponsableTipo', { accion: 'numeroResponsableTipoFiltro',t:moment().unix(),
			filtro_nombre: mi.filtros['nombre'],
			filtro_descripcion: mi.filtros['descripcion'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  } ).then(
				function(response) {
					mi.totalResponsablesTipos = response.data.totalResposnablesTipos;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};
	
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SResponsableTipo', { accion: 'getResponsableTipoPaginafiltro', pagina: pagina,
			numeroresponsabletipo:  $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'], filtro_descripcion: mi.filtros['descripcion'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t:moment().unix()
			}).success(
				function(response) {
					mi.entidades = response.responsablestipos;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
	};
}]);