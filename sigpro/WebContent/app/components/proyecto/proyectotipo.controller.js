var app = angular.module('proyectotipoController', [ 'ngTouch']);

app.controller('proyectotipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants) {
		var mi=this;
		
		$window.document.title = 'SIGPRO - Tipo Proyecto';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.proyectotipos = [];
		mi.proyectotipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalProcetotipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		
		
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
						mi.cooperante = row.entity;
					});
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyectoTipos', t: (new Date()).getTime()}).then(function(response){
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
			$http.post('/SProyectoTipo', { accion: 'getProyectoTipoPagina', pagina: pagina, numerocooperantes: $utilidades.elementosPorPagina }).success(
					function(response) {
						mi.proyectotipos = response.poryectotipos;
						mi.gridOptions.data = mi.proyectotipos;
						mi.mostrarcargando = false;
					});
		}
		
		mi.guardar=function(){
			if(mi.proyectotipo!=null  && mi.proyectotipo.nombre!=''){
				$http.post('/SProyectoTipo', {
					accion: 'guardarProyectotipo',
					esnuevo: mi.esnuevo,
					id: mi.proyectotipo.id,
					nombre: mi.proyectotipo.nombre,
					descripcion: mi.proyectotipo.descripcion
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo Proyecto '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo Proyecto');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		
		
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.proyectotipo = null;
			mi.gridApi.selection.clearSelectedRows();
		};
		
		
	
		
	
		
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'proyectotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/proyectotipo/rv')
				$route.reload();
			else
				$location.path('/proyectotipo/rv');
		}
		
		$http.post('/SProyectoTipo', { accion: 'numeroProyectoTipos' }).success(
				function(response) {
					mi.totalProyectotipos = response.totalproyectotipos;
					mi.cargarTabla(1);
				});
		
} ]);