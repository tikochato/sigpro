var app = angular.module('componentepropiedadController', []);

app.controller('componentepropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
			var mi=this;
			
			$window.document.title = 'SIGPRO - Componente Propiedad';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.componentepropiedades = [];
			mi.componentepropiedad;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalComponentePropiedades = 0;
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.tipodatos = [];
			
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
					    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación'},
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.componentepropiedad = row.entity;
						});
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'componentepropiedades', t: (new Date()).getTime()}).then(function(response){
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
				$http.post('/SComponentePropiedad', { accion: 'getComponentePropiedadPagina', pagina: pagina, numerocomponentepropiedades: $utilidades.elementosPorPagina }).success(
						function(response) {
							mi.componentepropiedades = response.componentepropiedades;
							mi.gridOptions.data = mi.componentepropiedades;
							mi.mostrarcargando = false;
						});
			}
			
			mi.guardar=function(){
				if(mi.componentepropiedad!=null && mi.componentepropiedad.nombre!=''){
					$http.post('/SComponentePropiedad', {
						accion: 'guardarComponentePropiedad',
						esnuevo: mi.esnuevo,
						id: mi.componentepropiedad.id,
						nombre: mi.componentepropiedad.nombre,
						descripcion: mi.componentepropiedad.descripcion,
						datoTipoId: mi.datotipoid
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Componente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.componentepropiedad.id = response.id;
							mi.esnuevo = false;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Componente');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.componentepropiedad!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar la Prppiedad Componente "'+mi.componentepropiedad.nombre+'"?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SComponentePropiedad', {
							accion: 'borrarComponentePropiedad',
							id: mi.componentepropiedad.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Propiedad Componente borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar la Propiedad Componente');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Componente que desea borrar');
			};

			mi.nuevo = function() {
				mi.datotipoid = "";
				mi.datotiponombre = "";
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.componentepropiedad = null;
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.componentepropiedad!=null){
					mi.datotipoid = mi.componentepropiedad.datotipoid;
					mi.datotiponombre = mi.componentepropiedad.datotiponombre;
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Componente que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'componentepropiedad', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/componentepropiedad/rv')
					$route.reload();
				else
					$location.path('/componentepropiedad/rv');
			}
			
			$http.post('/SComponentePropiedad', { accion: 'numeroComponentePropiedades' }).success(
					function(response) {
						mi.totalComponentePropiedades = response.totalcomponentepropiedades;
						mi.cargarTabla(1);
			});
			$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
		} ]);