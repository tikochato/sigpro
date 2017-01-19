var app = angular.module('recursopropiedadController', []);

app.controller('recursopropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
			var mi=this;
			
			$window.document.title = 'SIGPRO - Recurso Propiedad';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.recursopropiedades = [];
			mi.recursopropiedad;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalRecursoPropiedades = 0;
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
							mi.recursopropiedad = row.entity;
						});
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'recursopropiedades', t: (new Date()).getTime()}).then(function(response){
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
				$http.post('/SRecursoPropiedad', { accion: 'getRecursoPropiedadPagina', pagina: pagina, numerorecursopropiedades: $utilidades.elementosPorPagina }).success(
						function(response) {
							mi.recursopropiedades = response.recursopropiedades;
							mi.gridOptions.data = mi.recursopropiedades;
							mi.mostrarcargando = false;
						});
			}
			
			mi.guardar=function(){
				if(mi.recursopropiedad!=null && mi.recursopropiedad.nombre!=''){
					$http.post('/SRecursoPropiedad', {
						accion: 'guardarRecursoPropiedad',
						esnuevo: mi.esnuevo,
						id: mi.recursopropiedad.id,
						nombre: mi.recursopropiedad.nombre,
						descripcion: mi.recursopropiedad.descripcion,
						datoTipoId: mi.recursopropiedad.datotipoid
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Recurso '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.recursopropiedad.id = response.id;
							mi.esnuevo = false;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Recurso');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.borrar = function(ev) {
				if(mi.recursopropiedad!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar la Prppiedad Recurso "'+mi.recursopropiedad.nombre+'"?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SRecursoPropiedad', {
							accion: 'borrarRecursoPropiedad',
							id: mi.recursopropiedad.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Propiedad Recurso borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar la Propiedad Recurso');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Recurso que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.recursopropiedad = null;
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.recursopropiedad!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Propiedad Recurso que desea editar');
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'recursopropiedad', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/recursopropiedad/rv')
					$route.reload();
				else
					$location.path('/recursopropiedad/rv');
			}
			
			$http.post('/SRecursoPropiedad', { accion: 'numeroRecursoPropiedades' }).success(
					function(response) {
						mi.totalRecursoPropiedades = response.totalrecursopropiedades;
						mi.cargarTabla(1);
			});
			$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});
			
		} ]);