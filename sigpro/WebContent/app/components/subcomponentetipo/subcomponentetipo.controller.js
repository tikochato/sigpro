var app = angular.module('subcomponentetipoController', [ 'ngTouch']);

app.controller('subcomponentetipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipo Subcomponente';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.subcomponentetipos = [];
		mi.subcomponentetipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalSubComponentetipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		
		mi.subcomponentepropiedades =[];
		mi.subcomponentepropiedad =null;
		mi.mostrarcargandoCompProp=true;
		mi.mostrarPropiedadSubComponente = false;
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.subcomponentetipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' ,
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentetipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.subcomponentetipoc.filtrar($event)" ></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentetipoc.filtros[\'usuarioCreo\']" ng-keypress="grid.appScope.subcomponentetipoc.filtrar($event)" ></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentetipoc.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.subcomponentetipoc.filtrar($event)"  ></input></div>'	
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.subcomponentetipo = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.subcomponentetipoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.subcomponentetipoc.ordenDireccion = sortColumns[0].sort.direction;

							grid.appScope.subcomponentetipoc.cargarTabla(grid.appScope.subcomponentetipoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.subcomponentetipoc.columnaOrdenada!=null){
								grid.appScope.subcomponentetipoc.columnaOrdenada=null;
								grid.appScope.subcomponentetipoc.ordenDireccion=null;
							}
						}
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalSubComponenteTipos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'subcomponenteTipos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!=''){
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				    		  }
				    		  mi.obtenerTotalSubComponenteTipos();
						  });
				    }
				}
		};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SSubComponenteTipo', { accion: 'getSubComponentetiposPagina', pagina: pagina, numerosubcomponentetipos: $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime() }).success(
					function(response) {
						mi.subcomponentetipos = response.subcomponentetipos;
						mi.gridOptions.data = mi.subcomponentetipos;
						mi.mostrarcargando = false;
						mi.paginaActual = pagina;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.subcomponentetipo!=null && mi.subcomponentetipo.nombre!=null){
				var idspropiedad="";
				for (i = 0 ; i<mi.subcomponentepropiedades.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.subcomponentepropiedades[i].id); 
					}else{
						idspropiedad = idspropiedad.concat(",",mi.subcomponentepropiedades[i].id);
					}
				}
				
				$http.post('/SSubComponenteTipo', {
					accion: 'guardarSubComponentetipo',
					esnuevo: mi.esnuevo,
					id: mi.subcomponentetipo.id,
					nombre: mi.subcomponentetipo.nombre,
					descripcion: mi.subcomponentetipo.descripcion,
					propiedades: idspropiedad.length > 0 ? idspropiedad : null, t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo Subcomponente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.subcomponentetipo.id = response.id;
						mi.subcomponentetipo.usuarioCreo=response.usuarioCreo;
						mi.subcomponentetipo.fechaCreacion=response.fechaCreacion;
						mi.subcomponentetipo.usuarioActualizo=response.usuarioactualizo;
						mi.subcomponentetipo.fechaActualizacion=response.fechaactualizacion;
						mi.obtenerTotalSubComponenteTipos();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo Subcomponente');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		mi.editar = function() {
			if(mi.subcomponentetipo!=null && mi.subcomponentetipo.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.cargarTotalPropiedades();
				$utilidades.setFocus(document.getElementById("nombre"));
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Subcomponente que desea editar');
		}
		
		mi.borrar = function(ev) {
			if(mi.subcomponentetipo!=null && mi.subcomponentetipo.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el tipo de subcomponente "'+mi.subcomponentetipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SSubComponenteTipo', {
							accion: 'borrarSubComponenteTipo',
							id: mi.subcomponentetipo.id, t: (new Date()).getTime()
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo Subcomponente borrado con éxito');
								mi.subcomponentetipo = null;
								mi.obtenerTotalSubComponenteTipos();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo Subcomponente');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Subcomponente que desea borrar');
		};
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.subcomponentetipo = {};
			mi.gridApi.selection.clearSelectedRows();
			mi.cargarTotalPropiedades();
			
			$utilidades.setFocus(document.getElementById("nombre"));
		};
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
			mi.esnuevo=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'subcomponentetipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/subcomponentetipo/rv')
				$route.reload();
			else
				$location.path('/subcomponentetipo/rv');
		}
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalSubComponenteTipos();
				mi.gridApi.selection.clearSelectedRows();
				mi.subcomponentetipo = null;
			}
		}

		mi.obtenerTotalSubComponenteTipos = function(){
			$http.post('/SSubComponenteTipo', { accion: 'numeroSubComponenteTipos',
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime()  }).then(
					function(response) {
						mi.totalSubComponentetipos = response.data.totalsubcomponentetipos;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}
		
		mi.gridOptionssubcomponentePropiedad = {
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
						mi.subcomponentepropiedad = row.entity;
					});
				}
		};
		
		mi.cargarTablaPropiedades = function(pagina){
			mi.mostrarcargandoProyProp=true;
			$http.post('/SSubComponentePropiedad', 
					{ 
						accion: 'getSubComponentePropiedadPaginaPorTipo',
						pagina: pagina,
						idSubComponenteTipo:mi.subcomponentetipo!=null ? mi.subcomponentetipo.id : null, 
						numerosubcomponentepropiedad: $utilidades.elementosPorPagina, t: (new Date()).getTime() }).success(
				function(response) {
					
					mi.subcomponentepropiedades = response.subcomponentepropiedades;
					mi.gridOptionssubcomponentePropiedad.data = mi.subcomponentepropiedades;
					mi.mostrarcargandoCompProp = false;
					mi.mostrarPropiedad = true;
				});
			
		}
		
		mi.cargarTotalPropiedades = function(){
			$http.post('/SSubComponentePropiedad', { accion: 'numeroSubComponentePropiedades', t: (new Date()).getTime() }).success(
					function(response) {
						mi.totalSubComponentepropiedades = response.totalsubcomponentepropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualPropiedades);
					}
			);
		}
		
		mi.eliminarPropiedad = function(){
			if (mi.subcomponentepropiedad != null){
				for (i = 0 ; i<mi.subcomponentepropiedades.length ; i ++){
					if (mi.subcomponentepropiedades[i].id === mi.subcomponentepropiedad.id){
						mi.subcomponentepropiedades.splice (i,1);
						break;
					}
				}
				mi.subcomponentepropiedad = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}
		
		mi.eliminarPropiedad2 = function(row){
			var index = mi.subcomponentepropiedades.indexOf(row);
	        if (index !== -1) {
	            mi.subcomponentepropiedades.splice(index, 1);
	        }
		}
		
		mi.seleccionTabla = function(row){
			if (mi.subcomponentepropiedad !=null && mi.subcomponentepropiedad.id == row.id){
				mi.subcomponentepropiedad = null;
			}else{
				mi.subcomponentepropiedad = row;
			}
		}
		
		mi.buscarPropiedad = function(titulo, mensaje) {
			var modalInstance = $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : 'buscarsubcomponentepropiedad.jsp',
			    controller : 'modalBuscarSubComponentePropiedad',
			    controllerAs : 'modalBuscar',
			    backdrop : 'static',
			    size : 'md',
			    resolve : {
					idspropiedad : function() {
						var idspropiedad = "";
						var propiedadTemp;
						for (i = 0, len =mi.subcomponentepropiedades.length;  i < len; i++) {
				    		if (i == 0){
				    			idspropiedad = idspropiedad.concat("",mi.subcomponentepropiedades[i].id);
				    		}else{
				    			idspropiedad = idspropiedad.concat(",",mi.subcomponentepropiedades[i].id);
				    		}
				    	}
					    return idspropiedad;
					}
			    }

			});
			
			modalInstance.result.then(function(selectedItem) {
				mi.subcomponentepropiedades.push(selectedItem);
				
			}, function() {
			});
		}
} ]);

app.controller('modalBuscarSubComponentePropiedad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','idspropiedad', modalBuscarSubComponentePropiedad
]);

function modalBuscarSubComponentePropiedad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log,idspropiedad) {
	
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
    $http.post('/SSubComponentePropiedad', {
    	accion : 'numeroSubComponentePropiedadesDisponibles', t: (new Date()).getTime()
        }).success(function(response) {
    	mi.totalElementos = response.totalsubcomponentepropiedades;
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
			    mi.seleccionarSubComponentePropiedad);
		}
    }
    
    mi.seleccionarSubComponentePropiedad = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
    	var datos = {
    	    accion : 'getSubComponentePropiedadPagina',
    	    pagina : pagina,
    	    numerosubcomponentepropiedades: mi.elementosPorPagina,
    	    registros : mi.elementosPorPagina, t: (new Date()).getTime()
    	};

    	mi.mostrarCargando = true;
    	$http.post('/SSubComponentePropiedad', datos).then(function(response) {
    	    if (response.data.success) {
    	    	mi.data = response.data.subcomponentepropiedades;
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