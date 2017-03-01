var app = angular.module('formulariotipoController', [ 'ngTouch']);

app.controller('formulariotipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipo Formulario';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.formulariotipos = [];
		mi.formulariotipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalFormulariotipos = 0;
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
						mi.formulariotipo = row.entity;
					});
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'formulariotipos', t: (new Date()).getTime()}).then(function(response){
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
			$http.post('/SFormularioTipo', { accion: 'getFormulariotiposPagina', pagina: pagina, numeroformulariotipos: $utilidades.elementosPorPagina }).success(
					function(response) {
						mi.formulariotipos = response.formulariotipos;
						mi.gridOptions.data = mi.formulariotipos;
						mi.mostrarcargando = false;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.formulariotipo!=null  && mi.formulariotipo.nombre!=''){
				
				
				$http.post('/SFormularioTipo', {
					accion: 'guardarFormulariotipo',
					esnuevo: mi.esnuevo,
					id: mi.formulariotipo.id,
					nombre: mi.formulariotipo.nombre,
					descripcion: mi.formulariotipo.descripcion,
					
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo Formulario '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.formulariotipo.id = response.id;
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo Formulario');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
		
		mi.editar = function() {
			if(mi.formulariotipo!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de RFormulario que desea editar');
		}
		
		mi.borrar = function(ev) {
			if(mi.formulariotipo!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar el tipo de formulario "'+mi.formulariotipo.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SFormularioTipo', {
						accion: 'borrarFormularioTipo',
						id: mi.formulariotipo.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Tipo Formulario borrado con éxito');
							mi.formulariotipo = null;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Tipo Formulario');
					});
			    }, function() {
			    
			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Formulario que desea borrar');
		};
		
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.formulariotipo = null;
			mi.gridApi.selection.clearSelectedRows();
		};
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'formulariotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/formulariotipo/rv')
				$route.reload();
			else
				$location.path('/formulariotipo/rv');
		}
		
		$http.post('/SFormularioTipo', { accion: 'numeroFormularioTipos' }).success(
				function(response) {
					mi.totalFormulariotipos = response.totalformulariotipos;
					mi.cargarTabla(1);
				}
		);	
} ]);