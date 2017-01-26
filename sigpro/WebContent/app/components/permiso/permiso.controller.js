var app = angular.module('permisoController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'permisoController',
 [
  '$scope',
  '$http',
  '$interval',
  '$q',
  'i18nService',
  'Utilidades',
  '$routeParams',
  'uiGridConstants',
  '$mdDialog',
  '$window',
  '$location',
  '$route',
  function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route) {
	var mi=this;
	$window.document.title = 'SIGPRO - Permisos';
	i18nService.setCurrentLang('es');
	mi.mostrarcargando=true;
	mi.entityselected = null;
	mi.esNuevo = false;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.permisoSelected={id:"",nombre:"", descripcion:""};	
	mi.gridOptions = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		paginationPageSizes : [ 25, 50, 75 ],
		paginationPageSize : 25,
		data : [],
		columnDefs : [ {
			name : 'ID',
			field : 'id'
		}, {
			name : 'Nombre',
			field : 'nombre'
		}, {
			name : 'Descripcion',
			field : 'descripcion'
		}

		],

	};
	mi.cargarTabla=function(pagina){
		$http.post('/SPermiso',
				{ accion : 'getPermisosPagina',  pagina: pagina, numeroPermisos: $utilidades.elementosPorPagina  }).success(function(data) {
				mi.gridOptions.data =  data.permisos;
				mi.mostrarcargando=false;
		});
	};
	
	mi.gridOptions.multiSelect = false;
	mi.gridOptions.modifierKeysToMultiSelect = false;
	mi.gridOptions.noUnselect = true;
	mi.gridOptions.onRegisterApi = function(gridApi) {
		mi.gridApi = gridApi;
		gridApi.selection.on
		.rowSelectionChanged(
			$scope,
			function(row) {
			var msg = 'row selected '
			+ row;
			mi.permisoSelected = row.entity;
		});
		if($routeParams.reiniciar_vista=='rv'){
			mi.guardarEstado();
	    }
	    else{
	    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'permisos', t: (new Date()).getTime()}).then(function(response){
		      if(response.data.success && response.data.estado!='')
		    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
		    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
			      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
			      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
			      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
			  });
	    }
	};

   
						
						
	

	

	mi.cancelar = function() {
		mi.isCollapsed = false;
	}
	
	mi.nuevoPermiso=function(){
		var formularios = mi.cargarTabla(mi.paginaActual);						
		mi.isCollapsed = true;
		mi.entityselected = null;
		mi.esNuevo = true;
		mi.permisoSelected = {id:"",nombre:"", descripcion:""};
	};					
	mi.guardarPermiso=function(){		
		if(mi.permisoSelected.nombre!=="" && mi.permisoSelected.descripcion!==""){
			var idSend=0;
			if(!mi.esNuevo){
				idSend=mi.permisoSelected.id;
			}
			$http.post('/SPermiso',
					{
						accion: 'guardarPermiso',
						nombre:mi.permisoSelected.nombre,
						descripcion:mi.permisoSelected.descripcion,
						esnuevo:mi.esNuevo,
						id:idSend
					}).success(
						function(data) {
							if(data.success){
								if(mi.esNuevo){
									mi.gridOptions.data
									.push({
										"id" : data.data,
										"nombre" : mi.permisoSelected.nombre,
										"descripcion" : mi.permisoSelected.descripcion
									});
									mi.isCollapsed = false;
								}else{
									mi.cargarTabla(mi.paginaactual);
									mi.isCollapsed = false;
								}
								
							}
				});
			/*if(mi.esNuevo){
				$http.post('/SPermiso',
					{
						accion: 'guardarPermiso',
						nombre:mi.permisoSelected.nombre,
						descripcion:mi.permisoSelected.descripcion,
						esnuevo:mi.esNuevo,
						id:0
					}).success(
						function(data) {
							if(data.success){
								if(mi.esNuevo){
									mi.gridOptions.data
									.push({
										"id" : data.data,
										"nombre" : mi.permisoSelected.nombre,
										"descripcion" : mi.permisoSelected.descripcion
									});
									mi.isCollapsed = false;
								}else{
									mi.cargarTabla(mi.paginaactual);
									mi.isCollapsed = false;
								}
								
							}
				});
			}else{
				$http.post('/SPermiso',
						{
							accion: 'guardarPermiso',
							id:mi.permisoSelected.id,
							nombre:mi.permisoSelected.nombre,
							descripcion:mi.permisoSelected.descripcion,
							esnuevo:mi.esNuevo,
						}).success(
							function(data) {
								if(data.success){
									mi.cargarTabla(mi.paginaactual);
									mi.isCollapsed = false;
								}
					});
			}*/
		}else{
			$utilidades.mensaje('danger','Llene los campos');
		}
	};
	mi.borrarPermiso=function(ev){
		mi.isCollapsed = false;
		if(mi.permisoSelected.id!==""){
			var confirm = $mdDialog.confirm()
	          .title('Confirmación de borrado')
	          .textContent('¿Desea borrar el permiso "'+mi.permisoSelected.nombre+'"?')
	          .ariaLabel('Confirmación de borrado')
	          .targetEvent(ev)
	          .ok('Borrar')
	          .cancel('Cancelar');
			$mdDialog.show(confirm).then(function() {
		    	$http.post('/SPermiso', {
					accion: 'eliminarPermiso',
					id: mi.permisoSelected.id
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Permiso borrado con éxito');
						mi.cargarTabla(mi.paginaActual);
						mi.permisoSelected={id:"",nombre:"", descripcion:""};
					}
					else
						$utilidades.mensaje('danger','Error al borrar el Permiso');
				});
		    }, function() {
		    
		    });
		}else{
		    $utilidades.mensaje('danger','Seleccione un permiso');
		}
	};
	
	
	mi.editarPermiso=function(){
		if(mi.permisoSelected.id!==""){
			mi.isCollapsed = true;
			mi.esNuevo=false;
		}else{
			$utilidades.mensaje('danger','Seleccione un permiso');
		}
	};
	
	mi.reiniciarVista=function(){
		if($location.path()=='/permisos/rv')
			$route.reload();
		else
			$location.path('/permisos/rv');
	};
	
	mi.cambiarPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	};
	
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'permisos', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
		$http.post('/SEstadoTabla', tabla_data).then(function(response){
			
		});
	}
	
	$http.post('/SPermiso', { accion: 'getTotalPermisos' }).success(
			function(response) {
				mi.totalPermisos = response.totalPermisos;
				mi.cargarTabla(mi.paginaActual);
	});
} ]);