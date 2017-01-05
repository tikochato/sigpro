var app = angular.module('riesgopropiedadController', []);

app.controller('riesgopropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
		var mi=this;
		
		$window.document.title = 'SIGPRO - Propiedad Riesgo';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.riesgopropiedades = [];
		mi.riesgopropiedad;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalRiesgoPropiedades = 0;
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
						mi.riesgopropiedad = row.entity;
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
			$http.post('/SRiesgoPropiedad', { accion: 'getRiesgoPropiedadPagina', pagina: pagina, numeroriesgopropiedades: $utilidades.elementosPorPagina }).success(
					function(response) {
						mi.riesgopropiedades = response.riesgopropiedades;
						mi.gridOptions.data = mi.riesgopropiedades;
						mi.mostrarcargando = false;
					});
		}
		
		mi.guardar=function(){
			if(mi.riesgopropiedad!=null && mi.riesgopropiedad.nombre!=''){
				$http.post('/SRiesgoPropiedad', {
					accion: 'guardarRiesgoPropiedad',
					esnuevo: mi.esnuevo,
					id: mi.riesgopropiedad.id,
					nombre: mi.riesgopropiedad.nombre,
					descripcion: mi.riesgopropiedad.descripcion,
					datoTipoId: mi.riesgopropiedad.datotipoid
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Propiedad Riesgo '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.riesgopropiedad.id = response.id;
						mi.esnuevo = false;
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Riesgo');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
	
		mi.borrar = function(ev) {
			if(mi.riesgopropiedad!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar la Propiedad Riesgo "'+mi.riesgopropiedad.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');
	
			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SRiesgoPropiedad', {
						accion: 'borrarRiesgoPropiedad',
						id: mi.riesgopropiedad.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Riesgo borrado con éxito');
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar la Riesgo Componente');
					});
			    }, function() {
			    
			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad Riesgo que desea borrar');
		};
	
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.riesgopropiedad = null;
			mi.gridApi.selection.clearSelectedRows();
		};
	
		mi.editar = function() {
			if(mi.riesgopropiedad!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad Riesgo que desea editar');
		}
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'riesgopropiedad', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/riesgopropiedad/rv')
				$route.reload();
			else
				$location.path('/riesgopropiedad/rv');
		}
		
		$http.post('/SRiesgoPropiedad', { accion: 'numeroRiesgoPropiedades' }).success(
				function(response) {
					mi.totalRiesgoPropiedades = response.totalriesgopropiedades;
					mi.cargarTabla(1);
		});
		$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
				function(response) {
					mi.tipodatos = response.datoTipos;
		});
		
	} 
]);