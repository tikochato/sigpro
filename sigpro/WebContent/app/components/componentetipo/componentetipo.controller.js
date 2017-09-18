var app = angular.module('componentetipoController', [ 'ngTouch']);

app.controller('componentetipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipo Componente';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.componentetipos = [];
		mi.componentetipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalComponentetipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		
		mi.componentepropiedades =[];
		mi.componentepropiedad =null;
		mi.mostrarcargandoCompProp=true;
		mi.mostrarPropiedadComponente = false;
		mi.paginaActualPropiedades=1;
		
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.componentetipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' ,
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentetipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.componentetipoc.filtrar($event)" ></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentetipoc.filtros[\'usuarioCreo\']" ng-keypress="grid.appScope.componentetipoc.filtrar($event)" ></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentetipoc.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.componentetipoc.filtrar($event)"  ></input></div>'	
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.componentetipo = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.componentetipoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.componentetipoc.ordenDireccion = sortColumns[0].sort.direction;

							grid.appScope.componentetipoc.cargarTabla(grid.appScope.componentetipoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.componentetipoc.columnaOrdenada!=null){
								grid.appScope.componentetipoc.columnaOrdenada=null;
								grid.appScope.componentetipoc.ordenDireccion=null;
							}
						}
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalComponenteTipos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'componenteTipos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!=''){
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				    		  }
				    		  mi.obtenerTotalComponenteTipos();
						  });
				    }
				}
		};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SComponenteTipo', { accion: 'getComponentetiposPagina', pagina: pagina, numerocomponentetipos: $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime() }).success(
					function(response) {
						mi.componentetipos = response.componentetipos;
						mi.gridOptions.data = mi.componentetipos;
						mi.mostrarcargando = false;
						mi.paginaActual = pagina;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.componentetipo!=null && mi.componentetipo.nombre!=null){
				var idspropiedad="";
				for (i = 0 ; i<mi.componentepropiedades.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.componentepropiedades[i].id); 
					}else{
						idspropiedad = idspropiedad.concat(",",mi.componentepropiedades[i].id);
					}
				}
				
				$http.post('/SComponenteTipo', {
					accion: 'guardarComponentetipo',
					esnuevo: mi.esnuevo,
					id: mi.componentetipo.id,
					nombre: mi.componentetipo.nombre,
					descripcion: mi.componentetipo.descripcion,
					propiedades: idspropiedad.length > 0 ? idspropiedad : null, t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo Componente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.componentetipo.id = response.id;
						mi.componentetipo.usuarioCreo=response.usuarioCreo;
						mi.componentetipo.fechaCreacion=response.fechaCreacion;
						mi.componentetipo.usuarioActualizo=response.usuarioactualizo;
						mi.componentetipo.fechaActualizacion=response.fechaactualizacion;
						mi.obtenerTotalComponenteTipos();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo Componente');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		mi.editar = function() {
			if(mi.componentetipo!=null && mi.componentetipo.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.cargarTotalPropiedades();
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Componente que desea editar');
		}
		
		mi.borrar = function(ev) {
			if(mi.componentetipo!=null && mi.componentetipo.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el tipo de componente "'+mi.componentetipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SComponenteTipo', {
							accion: 'borrarComponenteTipo',
							id: mi.componentetipo.id, t: (new Date()).getTime()
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo Componente borrado con éxito');
								mi.componentetipo = null;
								mi.obtenerTotalComponenteTipos();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo Componente');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Componente que desea borrar');
		};
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.componentetipo = {};
			mi.gridApi.selection.clearSelectedRows();
			mi.cargarTotalPropiedades();
		};
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
			mi.esnuevo=false;
		}
		
		
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'componentetipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/componentetipo/rv')
				$route.reload();
			else
				$location.path('/componentetipo/rv');
		}
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalComponenteTipos();
				mi.gridApi.selection.clearSelectedRows();
				mi.componentetipo = null;
			}
		}

		mi.obtenerTotalComponenteTipos = function(){
			$http.post('/SComponenteTipo', { accion: 'numeroComponenteTipos',
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime()  }).then(
					function(response) {
						mi.totalComponentetipos = response.data.totalcomponentetipos;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}
		
		mi.gridOptionscomponentePropiedad = {
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
						mi.componentepropiedad = row.entity;
					});
				}
		};
		
		mi.cargarTablaPropiedades = function(pagina){
			mi.mostrarcargandoProyProp=true;
			$http.post('/SComponentePropiedad', 
					{ 
						accion: 'getComponentePropiedadPaginaPorTipo',
						pagina: pagina,
						idComponenteTipo:mi.componentetipo!=null ? mi.componentetipo.id : null, 
						numerocomponentepropiedad: $utilidades.elementosPorPagina, t: (new Date()).getTime() }).success(
				function(response) {
					
					mi.componentepropiedades = response.componentepropiedades;
					mi.gridOptionscomponentePropiedad.data = mi.componentepropiedades;
					mi.mostrarcargandoCompProp = false;
					mi.mostrarPropiedad = true;
				});
			
		}
		
		mi.cargarTotalPropiedades = function(){
			$http.post('/SComponentePropiedad', { accion: 'numeroComponentePropiedades', t: (new Date()).getTime() }).success(
					function(response) {
						mi.totalComponentepropiedades = response.totalcomponentepropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualPropiedades);
					}
			);
		}
		
		mi.eliminarPropiedad = function(){
			if (mi.componentepropiedad != null){
				for (i = 0 ; i<mi.componentepropiedades.length ; i ++){
					if (mi.componentepropiedades[i].id === mi.componentepropiedad.id){
						mi.componentepropiedades.splice (i,1);
						break;
					}
				}
				mi.componentepropiedad = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}
		
		mi.eliminarPropiedad2 = function(row){
			var index = mi.componentepropiedades.indexOf(row);
	        if (index !== -1) {
	            mi.componentepropiedades.splice(index, 1);
	        }
		}
		
		mi.seleccionTabla = function(row){
			if (mi.componentepropiedad !=null && mi.componentepropiedad.id == row.id){
				mi.componentepropiedad = null;
			}else{
				mi.componentepropiedad = row;
			}
		}
		
		mi.buscarPropiedad = function(titulo, mensaje) {
			titulo = 'Propiedades de Componente';
			var modalInstance = $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : 'buscarcomponentepropiedad.jsp',
			    controller : 'modalBuscarComponentePropiedad',
			    controllerAs : 'modalBuscar',
			    backdrop : 'static',
			    size : 'md',
			    resolve : {
					idspropiedad : function() {
						var idspropiedad = "";
						var propiedadTemp;
						for (i = 0, len =mi.componentepropiedades.length;  i < len; i++) {
				    		if (i == 0){
				    			idspropiedad = idspropiedad.concat("",mi.componentepropiedades[i].id);
				    		}else{
				    			idspropiedad = idspropiedad.concat(",",mi.componentepropiedades[i].id);
				    		}
				    	}
					    return idspropiedad;
					},
					$titulo : function(){
						return titulo;
					}
			    }

			});
			
			modalInstance.result.then(function(selectedItem) {
				mi.componentepropiedades.push(selectedItem);
				
			}, function() {
			});
		}
} ]);

app.controller('modalBuscarComponentePropiedad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','idspropiedad','$titulo', modalBuscarComponentePropiedad
]);

function modalBuscarComponentePropiedad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log,idspropiedad, $titulo) {
	
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	mi.titulo = $titulo;
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
    $http.post('/SComponentePropiedad', {
    	accion : 'numeroComponentePropiedadesDisponibles', t: (new Date()).getTime()
        }).success(function(response) {
    	mi.totalElementos = response.totalcomponentepropiedades;
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
		paginationPageSize : mi.elementosPorPagina,
		onRegisterApi : function(gridApi) {
		    mi.gridApi = gridApi;
		    mi.gridApi.selection.on.rowSelectionChanged($scope,
			    mi.seleccionarComponentePropiedad);
		}
    }
    
    mi.seleccionarComponentePropiedad = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
    	var datos = {
    	    accion : 'getComponentePropiedadPagina',
    	    pagina : pagina,
    	    numerocomponentepropiedades: mi.elementosPorPagina,
    	    registros : mi.elementosPorPagina, t: (new Date()).getTime()
    	};

    	mi.mostrarCargando = true;
    	$http.post('/SComponentePropiedad', datos).then(function(response) {
    	    if (response.data.success) {
    	    	mi.data = response.data.componentepropiedades;
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
};

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
