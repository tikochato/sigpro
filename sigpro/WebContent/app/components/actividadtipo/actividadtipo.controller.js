var app = angular.module('actividadtipoController', [ 'ngTouch']);

app.controller('actividadtipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipos de Actividad';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.actividadtipos = [];
		mi.actividadtipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalActividadtipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp';
			$utilidades.mensaje('primary','No tienes permiso de acceder a esta área');			
		}
		mi.actividadpropiedades =[];
		mi.actividadpropiedad =null;
		mi.mostrarcargandoActProp=true;
		mi.mostrarPropiedadActividad = false;
		mi.paginaActualPropiedades=1;
		
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.actividadtipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    useExternalSorting: true,
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left', 
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.actividadtipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.actividadtipoc.filtrar($event)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.actividadtipoc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.actividadtipoc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', 
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.actividadtipoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.actividadtipoc.filtrar($event)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.actividadtipo = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.actividadtipoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.actividadtipoc.ordenDireccion = sortColumns[0].sort.direction;
							for(var i = 0; i<sortColumns.length-1; i++)
								sortColumns[i].unsort();
							grid.appScope.actividadtipoc.cargarTabla(grid.appScope.actividadtipoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.actividadtipoc.columnaOrdenada!=null){
								grid.appScope.actividadtipoc.columnaOrdenada=null;
								grid.appScope.actividadtipoc.ordenDireccion=null;
							}
						}
							
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalActividadPropiedades();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'actividadTipos', t: (new Date()).getTime()}).then(function(response){
						      if(response.data.success && response.data.estado!=''){
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      }
						      mi.obtenerTotalActividadPropiedades();
						  });
				    }
				}
		};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SActividadTipo', { accion: 'getActividadtiposPagina', pagina: pagina, numeroactividadtipos: $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
			}).success(
					function(response) {
						mi.actividadtipos = response.actividadtipos;
						mi.gridOptions.data = mi.actividadtipos;
						mi.mostrarcargando = false;
					});
		}
		
		mi.guardar=function(){
			if(mi.actividadtipo!=null  && mi.actividadtipo.nombre!=''){
				var idspropiedad="";
				for (i = 0 ; i<mi.actividadpropiedades.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.actividadpropiedades[i].id); 
					}else{
						idspropiedad = idspropiedad.concat(",",mi.actividadpropiedades[i].id);
					}
				}
				
				$http.post('/SActividadTipo', {
					accion: 'guardarActividadtipo',
					esnuevo: mi.esnuevo,
					id: mi.actividadtipo.id,
					nombre: mi.actividadtipo.nombre,
					descripcion: mi.actividadtipo.descripcion,
					propiedades: idspropiedad.length > 0 ? idspropiedad : null
				}).success(function(response){
					if(response.success){
						mi.actividadtipo.id = response.id;
						mi.actividadtipo.usuarioCreo =response.usuarioCreo;
						mi.actividadtipo.fechaCreacion=response.fechaCreacion;
						mi.actividadtipo.usuarioActualizo=response.usuarioactualizo;
						mi.actividadtipo.fechaActualizacion=response.fechaactualizacion;
						$utilidades.mensaje('success','Tipo de Actividad '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo de Actividad');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		mi.editar = function() {
			if(mi.actividadtipo!=null && mi.actividadtipo.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.cargarTotalPropiedades();
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Actividad que desea editar');
		}
		
		mi.borrar = function(ev) {
			if(mi.actividadtipo!=null && mi.actividadtipo.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el tipo de actividad "'+mi.actividadtipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SActividadTipo', {
							accion: 'borrarActividadTipo',
							id: mi.actividadtipo.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo de Actividad borrado con éxito');
								mi.actividadtipo = null;
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo de Actividad');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Actividad que desea borrar');
		};
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.actividadtipo = {};
			mi.gridApi.selection.clearSelectedRows();
			mi.cargarTotalPropiedades();
		};
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'actividadtipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/actividadtipo/rv')
				$route.reload();
			else
				$location.path('/actividadtipo/rv');
		}
		
		mi.filtrar = function(evt,tipo){
			if(evt.keyCode==13){
				mi.obtenerTotalActividadPropiedades();
				mi.gridApi.selection.clearSelectedRows();
				mi.actividadtipo = null;
			}
		}
		
		mi.obtenerTotalActividadPropiedades=function(){
			$http.post('/SActividadTipo', { accion: 'numeroActividadTipos' }).success(
				function(response) {
					mi.totalActividadtipos = response.totalactividadtipos;
					mi.cargarTabla(1);
				}
			);
		}
		//----
		
		mi.gridOptionsactividadPropiedad = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: 10,
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'datotiponombre', displayName: 'Tipo Dato'}
				    
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.actividadpropiedad = row.entity;
					});
				}
		};
		
		mi.cargarTablaPropiedades = function(pagina){
			mi.mostrarcargandoProyProp=true;
			$http.post('/SActividadPropiedad', 
					{ 
						accion: 'getActividadPropiedadPaginaPorTipo',
						pagina: pagina,
						idActividadTipo:mi.actividadtipo!=null ? mi.actividadtipo.id : null, 
						numeroactividadpropiedad: $utilidades.elementosPorPagina }).success(
				function(response) {
					
					mi.actividadpropiedades = response.actividadpropiedades;
					mi.gridOptionsactividadPropiedad.data = mi.actividadpropiedades;
					mi.mostrarcargandoActProp = false;
					mi.mostrarPropiedad = true;
				});
			
		}
		
		mi.cargarTotalPropiedades = function(){
			$http.post('/SActividadPropiedad', { accion: 'numeroActividadPropiedades' }).success(
					function(response) {
						mi.totalActividadpropiedades = response.totalactividadpropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualPropiedades);
					}
			);
		}
		
		mi.eliminarPropiedad = function(){
			if (mi.actividadpropiedad != null){
				for (i = 0 ; i<mi.actividadpropiedades.length ; i ++){
					if (mi.actividadpropiedades[i].id === mi.actividadpropiedad.id){
						mi.actividadpropiedades.splice (i,1);
						break;
					}
				}
				mi.actividadpropiedad = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}
		
		mi.eliminarPropiedad2 = function(row){
			var index = mi.actividadpropiedades.indexOf(row);
	        if (index !== -1) {
	            mi.actividadpropiedades.splice(index, 1);
	        }
		}
		
		mi.seleccionTabla = function(row){
			if (mi.actividadpropiedad !=null && mi.actividadpropiedad.id == row.id){
				mi.actividadpropiedad = null;
			}else{
				mi.actividadpropiedad = row;
			}
		}
		
		mi.buscarPropiedad = function(titulo, mensaje) {
			var modalInstance = $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : 'buscaractividadpropiedad.jsp',
			    controller : 'modalBuscarActividadPropiedad',
			    controllerAs : 'modalBuscar',
			    backdrop : 'static',
			    size : 'md',
			    resolve : {
					idspropiedad : function() {
						var idspropiedad = "";
						var propiedadTemp;
						for (i = 0, len =mi.actividadpropiedades.length;  i < len; i++) {
				    		if (i == 0){
				    			idspropiedad = idspropiedad.concat("",mi.actividadpropiedades[i].id);
				    		}else{
				    			idspropiedad = idspropiedad.concat(",",mi.actividadpropiedades[i].id);
				    		}
				    	}
					    return idspropiedad;
					}
			    }

			});
			
			modalInstance.result.then(function(selectedItem) {
				mi.actividadpropiedades.push(selectedItem);
				
			}, function() {
			});
		}
} ]);

app.controller('modalBuscarActividadPropiedad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','idspropiedad', modalBuscarActividadPropiedad
]);

function modalBuscarActividadPropiedad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log,idspropiedad) {
	
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
    $http.post('/SActividadPropiedad', {
    	accion : 'numeroactividadoPropiedadesDisponibles'
        }).success(function(response) {
    	mi.totalElementos = response.totalactividadpropiedades;
    	mi.elementosPorPagina = mi.totalElementos;
    	mi.cargarData(1);
    });
    
    mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [
			{displayName : 'Id', name : 'id', cellClass : 'grid-align-right', type : 'number', width : 100
			}, { displayName : 'Nombre', name : 'nombre', cellClass : 'grid-align-left'}
		],
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect : false,
		modifierKeysToMultiSelect : false,
		noUnselect : false,
		enableFiltering : true,
		enablePaginationControls : false,
		paginationPageSize : 5,
		onRegisterApi : function(gridApi) {
		    mi.gridApi = gridApi;
		    mi.gridApi.selection.on.rowSelectionChanged($scope,
			    mi.seleccionarActividadPropiedad);
		}
    }
    
    mi.seleccionarActividadPropiedad = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
    	var datos = {
    	    accion : 'getActividadPropiedadesTotalDisponibles',
    	    pagina : pagina,
    	    idspropiedades: idspropiedad,
    	    registros : mi.elementosPorPagina
    	};

    	mi.mostrarCargando = true;
    	$http.post('/SActividadPropiedad', datos).then(function(response) {
    	    if (response.data.success) {
    	    	mi.data = response.data.actividadpropiedades;
    	    	mi.opcionesGrid.data = mi.data;
    			mi.mostrarCargando = false;
    	    }
    	});
    	
     };
     
     mi.cambioPagina = function() {
    	mi.cargarData(mi.paginaActual);
      }

     mi.ok = function() {
    	if (mi.seleccionado) {
    	    $uibModalInstance.close(mi.itemSeleccionado);
    	} else {
    	    $utilidades.mensaje('warning', 'Debe seleccionar una Propiedad');
    	}
     };

     mi.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
     };
}
