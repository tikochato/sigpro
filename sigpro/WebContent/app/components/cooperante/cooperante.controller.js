var app = angular.module('cooperanteController', [ 'ngTouch']);

app.controller('cooperanteController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams) {
			var mi=this;
			
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.cooperantes = [];
			mi.cooperante;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalCooperantes = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			
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
						{ name: 'codigo', width: 150, displayName: 'Código', cellClass: 'grid-align-right' },
					    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación'},
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
					]
				};
			
			mi.gridOptions.onRegisterApi = function(gridApi) {
				mi.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope,function(row) {
					mi.cooperante = row.entity;
				});
			};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SCooperante', { accion: 'getCooperantesPagin', pagina: pagina, numerocooperantes: $utilidades.elementosPorPagina }).success(
						function(response) {
							mi.cooperantes = response.cooperantes;
							mi.gridOptions.data = mi.cooperantes;
							mi.mostrarcargando = false;
						});
			}
			
			mi.cargarTabla(mi.paginaActual);

			
			mi.guardar=function(){
				$http.post('/SCooperante', {
					accion: 'guardarCooperante',
					esnuevo: mi.esnuevo,
					id: mi.cooperante.id,
					codigo: mi.cooperante.codigo,
					nombre: mi.cooperante.nombre,
					descripcion: mi.cooperante.descripcion
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Cooperante '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Cooperante');
				});
			};

			mi.borrar = function() {
				if(mi.cooperante!=null){
					$http.post('/SCooperante', {
						accion: 'borrarCooperante',
						id: mi.cooperante.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Cooperante borrado con éxito');
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Cooperante');
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Cooperante que desea borrar');
			};

			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.cooperante = null;
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.cooperante!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Cooperante que desea editar');
			}

			mi.cancelar = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'cooperantes', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			if($routeParams.reset_grid=='rv'){
				mi.guardarEstado();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'cooperantes', t: (new Date()).getTime()}).then(function(response){
			    	  if(response.data.success && response.data.estado!='')
			    		  mi.gridApi.saveState.restore( $scope, response.data.estado);
			    	  $scope.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
				      $scope.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
				      $scope.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      $scope.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
				  });
		    }
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
		} ]);