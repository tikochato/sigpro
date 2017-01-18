var app = angular.module('recursotipoController', [ 'ngTouch']);

app.controller('recursotipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal) {
		var mi=this;
		
		$window.document.title = 'SIGPRO - Tipo Recurso';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.recursotipos = [];
		mi.recursotipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalRecursotipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		
		mi.recursopropiedades =[];
		mi.componentepropiedad =null;
		mi.mostrarcargandoRecursoProp=true;
		mi.mostrarPropiedadRecurso = false;
		mi.paginaActualPropiedades=1;
		
		mi.gridOptions = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: $utilidades.elementosPorPagina,
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación'},
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.recursotipo = row.entity;
					});
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'componenteTipos', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
						  });
				    }
				}
		};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SRecursoTipo', { accion: 'getRecursotiposPagina', pagina: pagina, numerorecursotipos: $utilidades.elementosPorPagina }).success(
					function(response) {
						mi.recursotipos = response.recursotipos;
						mi.gridOptions.data = mi.recursotipos;
						mi.mostrarcargando = false;
					});
		}
		
		mi.guardar=function(){
			if(mi.recursotipo!=null  && mi.recursotipo.nombre!=''){
				var idspropiedad="";
				for (i = 0 ; i<mi.recursopropiedades.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.recursopropiedades[i].id); 
					}else{
						idspropiedad = idspropiedad.concat(",",mi.recursopropiedades[i].id);
					}
				}
				
				$http.post('/SRecursoTipo', {
					accion: 'guardarRecursotipo',
					esnuevo: mi.esnuevo,
					id: mi.recursotipo.id,
					nombre: mi.recursotipo.nombre,
					descripcion: mi.recursotipo.descripcion,
					propiedades: idspropiedad.length > 0 ? idspropiedad : null
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo de Recurso '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.recursotipo.id = response.id;
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo de Recurso');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		mi.editar = function() {
			if(mi.recursotipo!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.cargarTotalPropiedades();
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Recurso que desea editar');
		}
		
		mi.borrar = function(ev) {
			if(mi.recursotipo!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar el tipo de recurso "'+mi.recursotipo.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SRecursoTipo', {
						accion: 'borrarRecursoTipo',
						id: mi.recursotipo.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Tipo de Recurso borrado con éxito');
							mi.recursotipo = null;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Tipo de Recurso');
					});
			    }, function() {
			    
			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Recurso que desea borrar');
		};
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.recursotipo = null;
			mi.gridApi.selection.clearSelectedRows();
			mi.cargarTotalPropiedades();
		};
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'recursotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/recursotipo/rv')
				$route.reload();
			else
				$location.path('/recursotipo/rv');
		}
		
		$http.post('/SRecursoTipo', { accion: 'numeroRecursoTipos' }).success(
				function(response) {
					mi.totalRecursotipos = response.totalrecursotipos;
					mi.cargarTabla(1);
				}
		);
		//----
		
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
			$http.post('/SRecursoPropiedad', 
					{ 
						accion: 'getRecursoPropiedadPaginaPorTipo',
						pagina: pagina,
						idComponenteTipo:mi.recursotipo!=null ? mi.recursotipo.id : null, 
						numerocomponentepropiedad: $utilidades.elementosPorPagina }).success(
				function(response) {
					
					mi.recursopropiedades = response.recursopropiedades;
					mi.gridOptionscomponentePropiedad.data = mi.recursopropiedades;
					mi.mostrarcargandoRecursoProp = false;
					mi.mostrarPropiedad = true;
				});
			
		}
		
		mi.cargarTotalPropiedades = function(){
			$http.post('/SRecursoPropiedad', { accion: 'numeroRecursoPropiedades' }).success(
					function(response) {
						mi.totalComponentepropiedades = response.totalrecursopropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualPropiedades);
					}
			);
		}
		
		mi.eliminarPropiedad = function(){
			if (mi.componentepropiedad != null){
				for (i = 0 ; i<mi.recursopropiedades.length ; i ++){
					if (mi.recursopropiedades[i].id === mi.componentepropiedad.id){
						mi.recursopropiedades.splice (i,1);
						break;
					}
				}
				mi.componentepropiedad = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}
		
		mi.eliminarPropiedad2 = function(row){
			var index = mi.recursopropiedades.indexOf(row);
	        if (index !== -1) {
	            mi.recursopropiedades.splice(index, 1);
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
			var modalInstance = $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : 'buscarrecursopropiedad.jsp',
			    controller : 'modalBuscarRecursoPropiedad',
			    controllerAs : 'modalBuscar',
			    backdrop : 'static',
			    size : 'md',
			    resolve : {
					idspropiedad : function() {
						var idspropiedad = "";
						var propiedadTemp;
						for (i = 0, len =mi.recursopropiedades.length;  i < len; i++) {
				    		if (i == 0){
				    			idspropiedad = idspropiedad.concat("",mi.recursopropiedades[i].id);
				    		}else{
				    			idspropiedad = idspropiedad.concat(",",mi.recursopropiedades[i].id);
				    		}
				    	}
					    return idspropiedad;
					}
			    }

			});
			
			modalInstance.result.then(function(selectedItem) {
				mi.recursopropiedades.push(selectedItem);
				
			}, function() {
			});
		}
} ]);

app.controller('modalBuscarRecursoPropiedad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','idspropiedad', modalBuscarRecursoPropiedad
]);

function modalBuscarRecursoPropiedad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log,idspropiedad) {
	
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
    $http.post('/SRecursoPropiedad', {
    	accion : 'numerorecursoPropiedadesDisponibles'
        }).success(function(response) {
    	mi.totalElementos = response.totalrecursopropiedades;
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
			    mi.seleccionarRecursoPropiedad);
		}
    }
    
    mi.seleccionarRecursoPropiedad = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
    	var datos = {
    	    accion : 'getRecursoPropiedadesTotalDisponibles',
    	    pagina : pagina,
    	    idspropiedades: idspropiedad,
    	    registros : mi.elementosPorPagina
    	};

    	mi.mostrarCargando = true;
    	$http.post('/SRecursoPropiedad', datos).then(function(response) {
    	    if (response.data.success) {
    	    	mi.data = response.data.recursopropiedades;
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
