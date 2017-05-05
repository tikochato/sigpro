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
	$window.document.title = $utilidades.sistema_nombre+' - Permisos';
	i18nService.setCurrentLang('es');
	mi.mostrarcargando=true;
	mi.entityselected = null;
	mi.esNuevo = false;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.permisoSelected={id:"",nombre:"", descripcion:""};
	mi.filtros=[];
	
	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
        mi.editarPermiso();
    };
	
	mi.gridOptions = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		paginationPageSizes : [ 25, 50, 75 ],
		paginationPageSize : 25,
		enableFiltering: true,
		useExternalFiltering: true,
		useExternalSorting: true,
		data : [],
		rowTemplate: '<div ng-dblclick="grid.appScope.permisosc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [ {
			name : 'ID',
			field : 'id',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.permisosc.filtros[\'id\']" ng-keypress="grid.appScope.permisosc.filtrar($event)" style="width:175px;"></input></div>'
		}, {
			name : 'Nombre',
			cellClass : 'grid-align-left',
			field : 'nombre',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.permisosc.filtros[\'nombre\']" ng-keypress="grid.appScope.permisosc.filtrar($event)" style="width:175px;"></input></div>'
		}, {
			name : 'Descripcion',
			field : 'descripcion',
			cellClass : 'grid-align-left',
			enableFiltering: false
		}, {
			name : 'Usuario creo',
			field : 'usuarioCreo',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.permisosc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.permisosc.filtrar($event)" style="width:175px;"></input></div>'
		}, {
			name : 'Fecha creacion',
			field : 'fechaCreacion',
			cellClass : 'grid-align-left',
			filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.permisosc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.permisosc.filtrar($event)" style="width:175px;"></input></div>'
		}, {
			name : 'Usuario actualizo',
			field : 'usuarioActualizo',
			cellClass : 'grid-align-left',
			enableFiltering: false
		}, {
			name : 'Fecha actualizacion',
			field : 'fechaActualizacion',
			cellClass : 'grid-align-left',
			enableFiltering: false
		}
		

		],

	};
	mi.cargarTabla=function(pagina){
		$http.post('/SPermiso',
				{ accion : 'getPermisosPagina',  pagina: pagina, numeroPermisos: mi.elementosPorPagina ,filtro_id: mi.filtros['id'],filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  }).success(function(data) {
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

   
						
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}		
	

	

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
			$http.post('/SPermiso',
					{
						accion: 'guardarPermiso',
						nombre:mi.permisoSelected.nombre,
						descripcion:mi.permisoSelected.descripcion,
						esnuevo:mi.esNuevo,
						id:mi.permisoSelected.id
					}).success(
						function(data) {
							if(data.success){
								if(mi.esNuevo){
									mi.paginaActual=1;
									mi.cargarTabla(mi.paginaActual);
									$utilidades.mensaje('success','Permiso agregado exitosamente');
								}else{
									mi.paginaActual=1;
									mi.cargarTabla(mi.paginaActual);
									$utilidades.mensaje('success','Permiso actualizado exitosamente');
								}
								mi.permisoSelected.usuarioCreo = data.usuarioCreo;
								mi.permisoSelected.fechaCreacion = data.fechaCreacion;
								mi.permisoSelected.usuarioActualizo = data.usuarioactualizo;
								mi.permisoSelected.fechaActualizacion = data.fechaactualizacion;
								
							}else{
								$utilidades.mensaje('danger','No se pudieron aplicar los cambios');
							}
							
				});
			
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
				mi.elementosPorPagina = response.totalPermisos;
				mi.cargarTabla(mi.paginaActual);
	});
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			$http.post('/SUsuario', { accion: 'getTotalPermisos',	filtro_id: mi.filtros['id'],filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  }).success(
					function(response) {
						mi.elementosPorPagina = response.totalPermisos;
						mi.cargarTabla(mi.paginaActual);
			});
		}
	};
} ]);