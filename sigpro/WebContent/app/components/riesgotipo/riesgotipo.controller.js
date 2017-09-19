var app = angular.module('riesgotipoController', [ 'ngTouch']);

app.controller('riesgotipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipo Riesgo';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.riesgotipos = [];
		mi.riesgotipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalRiesgotipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.filtros=[];
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
		mi.riesgopropiedades =[];
		mi.riesgopropiedad =null;
		mi.mostrarcargandoRieProp=true;
		mi.mostrarPropiedadRiesgo = false;
		mi.paginaActualPropiedades=1;
		
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.riesgotipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.riesgotipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.riesgotipoc.filtrar($event)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.riesgotipoc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.riesgotipoc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.riesgotipoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.riesgotipoc.filtrar($event)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.riesgotipo = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.riesgotipoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.riesgotipoc.ordenDireccion = sortColumns[0].sort.direction;
							for(var i = 0; i<sortColumns.length-1; i++)
								sortColumns[i].unsort();
							grid.appScope.riesgotipoc.cargarTabla(grid.appScope.riesgotipoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.riesgotipoc.columnaOrdenada!=null){
								grid.appScope.riesgotipoc.columnaOrdenada=null;
								grid.appScope.riesgotipoc.ordenDireccion=null;
							}
						}
							
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalTipoRiesgos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'riesgoTipos', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalTipoRiesgos();
						  });
				    }
				}
		};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SRiesgoTipo', { accion: 'getRiesgotiposPagina', pagina: pagina, numeroriesgostipo: $utilidades.elementosPorPagina,
				objetoid: $routeParams.objeto_id, tipo: mi.objetotipo, filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
			}).success(
					function(response) {
						mi.riesgotipos = response.riesgotipos;
						mi.gridOptions.data = mi.riesgotipos;
						mi.mostrarcargando = false;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.riesgotipo!=null  && mi.riesgotipo.nombre!=''){
				var idspropiedad="";
				for (i = 0 ; i<mi.riesgopropiedades.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.riesgopropiedades[i].id); 
					}else{
						idspropiedad = idspropiedad.concat(",",mi.riesgopropiedades[i].id);
					}
				}
				
				$http.post('/SRiesgoTipo', {
					accion: 'guardarRiesgotipo',
					t: (new Date()).getTime(),
					esnuevo: mi.esnuevo,
					id: mi.riesgotipo.id,
					nombre: mi.riesgotipo.nombre,
					descripcion: mi.riesgotipo.descripcion,
					propiedades: idspropiedad.length > 0 ? idspropiedad : null
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo Riesgo '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.riesgotipo.id = response.id;
						mi.riesgotipo.usuarioCreo = response.usuarioCreo;
						mi.riesgotipo.fechaCreacion = response.fechaCreacion;
						mi.riesgotipo.usuarioActualizo = response.usuarioactualizo;
						mi.riesgotipo.fechaActualizacion = response.fechaactualizacion;
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo Riesgo');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		mi.editar = function() {
			if(mi.riesgotipo!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				$utilidades.setFocus(document.getElementById("nombre"));
				mi.cargarTotalPropiedades();
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Riesgo que desea editar');
		}
		
		mi.borrar = function(ev) {
			if(mi.riesgotipo!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el tipo de riesgo "'+mi.riesgotipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SRiesgoTipo', {
							accion: 'borrarRiesgoTipo',
							id: mi.riesgotipo.id,
							t: (new Date()).getTime()
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo Riesgo borrado con éxito');
								mi.riesgotipo = null;
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo Riesgo');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Riesgo que desea borrar');
		};
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.riesgotipo = null;
			mi.gridApi.selection.clearSelectedRows();
			$utilidades.setFocus(document.getElementById("nombre"));
			mi.cargarTotalPropiedades();
		};
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'riesgotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/riesgotipo/rv')
				$route.reload();
			else
				$location.path('/riesgotipo/rv');
		}
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalTipoRiesgos();
				mi.gridApi.selection.clearSelectedRows();
				mi.riesgotipo = null;
			}
		}
		
		mi.obtenerTotalTipoRiesgos = function(){
			$http.post('/SRiesgoTipo', { accion: 'numeroRiesgoTipos',
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'], t: (new Date()).getTime() }).then(
					function(response) {
						mi.totalRiesgotipos = response.data.totalriesgos;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}
		
		//----
		
		mi.gridOptionsriesgoPropiedad = {
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
						mi.riesgopropiedad = row.entity;
					});
				}
		};
		
		mi.cargarTablaPropiedades = function(pagina){
			mi.mostrarcargandoProyProp=true;
			$http.post('/SRiesgoPropiedad', 
					{ 
						accion: 'getRiesgoPropiedadPaginaPorTipo',
						pagina: pagina,
						t: (new Date()).getTime(),
						idRiesgoTipo:mi.riesgotipo!=null ? mi.riesgotipo.id : null, 
						numeroriesgopropiedad: $utilidades.elementosPorPagina }).success(
				function(response) {
					mi.riesgopropiedades = response.riesgopropiedades;
					mi.gridOptionsriesgoPropiedad.data = mi.riesgopropiedades;
					mi.mostrarcargandoRieProp = false;
					mi.mostrarPropiedad = true;
				});
			
		}
		
		mi.cargarTotalPropiedades = function(){
			$http.post('/SComponentePropiedad', { accion: 'numeroComponentePropiedades', t: (new Date()).getTime() }).success(
					function(response) {
						mi.totalRiesgopropiedades = response.totalriesgopropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualPropiedades);
					}
			);
		}
		
		mi.eliminarPropiedad = function(){
			if (mi.riesgopropiedad != null){
				for (i = 0 ; i<mi.riesgopropiedades.length ; i ++){
					if (mi.riesgopropiedades[i].id === mi.riesgopropiedad.id){
						mi.riesgopropiedades.splice (i,1);
						break;
					}
				}
				mi.riesgopropiedad = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}
		
		mi.eliminarPropiedad2 = function(row){
			var index = mi.riesgopropiedades.indexOf(row);
	        if (index !== -1) {
	            mi.riesgopropiedades.splice(index, 1);
	        }
		}
		
		mi.seleccionTabla = function(row){
			if (mi.riesgopropiedad !=null && mi.riesgopropiedad.id == row.id){
				mi.riesgopropiedad = null;
			}else{
				mi.riesgopropiedad = row;
			}
		}
		
		mi.buscarPropiedad = function(titulo, mensaje) {
			var modalInstance = $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : 'buscarriesgopropiedad.jsp',
			    controller : 'modalBuscarRiesgoPropiedad',
			    controllerAs : 'modalBuscar',
			    backdrop : 'static',
			    size : 'md',
			    resolve : {
					idspropiedad : function() {
						var idspropiedad = "";
						var propiedadTemp;
						for (i = 0, len =mi.riesgopropiedades.length;  i < len; i++) {
				    		if (i == 0){
				    			idspropiedad = idspropiedad.concat("",mi.riesgopropiedades[i].id);
				    		}else{
				    			idspropiedad = idspropiedad.concat(",",mi.riesgopropiedades[i].id);
				    		}
				    	}
					    return idspropiedad;
					}
			    }

			});
			
			modalInstance.result.then(function(selectedItem) {
				mi.riesgopropiedades.push(selectedItem);
				
			}, function() {
			});
		}
} ]);

app.controller('modalBuscarRiesgoPropiedad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','idspropiedad', modalBuscarRiesgoPropiedad
]);

function modalBuscarRiesgoPropiedad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log,idspropiedad) {
	
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
    $http.post('/SRiesgoPropiedad', {
    	accion : 'numeroriesgoPropiedadesDisponibles', t: (new Date()).getTime()
        }).success(function(response) {
    	mi.totalElementos = response.totalriesgopropiedades;
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
			    mi.seleccionarComponentePropiedad);
		}
    }
    
    mi.seleccionarComponentePropiedad = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
    	var datos = {
    	    accion : 'getRiesgoPropiedadesTotalDisponibles',
    	    pagina : pagina,
    	    idspropiedades: idspropiedad,
    	    numerocomponentepropiedad : mi.elementosPorPagina,
    	    t: (new Date()).getTime()
    	};

    	mi.mostrarCargando = true;
    	$http.post('/SRiesgoPropiedad', datos).then(function(response) {
    	    if (response.data.success) {
    	    	mi.data = response.data.riesgopropiedades;
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
