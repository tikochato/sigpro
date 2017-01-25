var app = angular.module('formularioitemtipoController', []);

app.controller('formularioitemtipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
			var mi=this;
			
			$window.document.title = 'SIGPRO - Tipo Item de Formulario';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.formularioitemtipos = [];
			mi.formularioitemtipo;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalFormularioItmeTipos = 0;
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
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false}
				    
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.formularioitemtipo = row.entity;
					});
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'formularioitemtipos', t: (new Date()).getTime()}).then(function(response){
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
				$http.post('/SFormularioItemTipo', { accion: 'getFormularioItemtiposPagina', pagina: pagina, numeroformularioitemtipos: $utilidades.elementosPorPagina }).success(
						function(response) {
							mi.formularioitemtipos = response.guardarFormularioItemTipo;
							mi.gridOptions.data = mi.formularioitemtipos;
							mi.mostrarcargando = false;
						});
			}
			
			
			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'formularioitemtipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/formularioitemtipo/rv')
					$route.reload();
				else
					$location.path('/formularioitemtipo/rv');
			}
			
			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.formularioitemtipo = null;
				mi.gridApi.selection.clearSelectedRows();
			};
			
			mi.guardar=function(){
				if(mi.formularioitemtipo!=null && mi.formularioitemtipo.nombre!='' && mi.formularioitemtipo.datotipo!=null){
					$http.post('/SFormularioItemTipo', {
						accion: 'guardarFormularioItemTipo',
						esnuevo: mi.esnuevo,
						id: mi.formularioitemtipo.id,
						nombre: mi.formularioitemtipo.nombre,
						descripcion: mi.formularioitemtipo.descripcion,
						datotipoid: mi.formularioitemtipo.datotipo.id
					}).success(function(response){
						if(response.success){
							mi.formularioitemtipo.id = response.id;
							$utilidades.mensaje('success','Tipo de Itme de Formulario '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.esnuevo = false;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Tipo de Item de Formulario');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};
			
			mi.editar = function() {
				if(mi.formularioitemtipo!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Tipo de Item de Formulario que desea editar');
			}
			
			mi.borrar = function(ev) {
				if(mi.formularioitemtipo!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar el Tipo Item de fomrulario "'+mi.formularioitemtipo.nombre+'"?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SFormularioItemTipo', {
							accion: 'borrarFormularioItemTipo',
							id: mi.formularioitemtipo.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo de Item de Formulario fue borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo De Item de Formulario');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Tipo De Item de Formulario que desea borrar');
			};

			
			$http.post('/SFormularioItemTipo', { accion: 'numeroFormularioItemTipos' }).success(
				function(response) {
					mi.totalFormularioItmeTipos = response.totalformularioitemtipos;
					mi.cargarTabla(1);
			});
			
}]);