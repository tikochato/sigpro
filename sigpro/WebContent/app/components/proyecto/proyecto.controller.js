var app = angular.module('proyectoController', [ 'ngTouch' ]);

app.controller('proyectoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants) {
					
	var mi = this;
	i18nService.setCurrentLang('es');
	mi.entidadseleccionada = null;
	mi.esNuevo = false;
	mi.campos = {};
	mi.esColapsado = false;
	mi.mostrarcargando=true;
	mi.paginaActual = 1;
	mi.cooperantes = [];
	mi.proyectotipos = [];
	mi.unidadesejecutoras = [];

	mi.gridOpciones = {
		enableRowSelection : true,
		enableRowHeaderSelection : false
	};

	mi.gridOpciones = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect: false,
		modifierKeysToMultiSelect: false,
		noUnselect: true,
		enableFiltering: true,
		enablePaginationControls: false,
	    paginationPageSize: $utilidades.elementosPorPagina,
		columnDefs : [ 
			{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre',  displayName: 'Nombre',cellClass: 'grid-align-left' },
			{ name : 'snip', width: 60, displayName : 'SNIP', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name : 'proyectotipo',   displayName : 'Tipo proyecto' ,cellClass: 'grid-align-left' },
			{ name : 'unidadejecutora',   displayName : 'Unidad Ejecutora' ,cellClass: 'grid-align-left' },
			{ name : 'cooperante',  width:200, displayName : 'Cooperante' ,cellClass: 'grid-align-left' },
			{ name: 'usuariocrea', width: 120, displayName: 'Usuario Creación'},
		    { name: 'fechacrea', width: 100, displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
			
		],
		onRegisterApi: function(gridApi) {
			mi.gridApi = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.entidadseleccionada = row.entity;
			});
			
			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyceto', t: (new Date()).getTime()}).then(function(response){
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
		$http.post('/SProyecto', { accion: 'getProyectoPagin', pagina: pagina, numerocooperantes: $utilidades.elementosPorPagina }).success(
				function(response) {
					mi.entidades = response.entidades;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
	}
	
	
	mi.guardar = function(){
		
		if(mi.entidadseleccionada!=null &&  mi.entidadseleccionada.nombre!='' && mi.entidadseleccionada.snip!=''
			&& mi.entidadseleccionada.proyectotipoid!='' && mi.entidadseleccionada.unidadejecutoraid
			&& mi.entidadseleccionada.cooperanteid){
			var param_data = {
				accion : 'guardar',
				esnuevo: mi.esNuevo,
				datos : JSON.stringify(mi.entidadseleccionada)
			};
			$http.post('/SProyecto',param_data).then(
				function(response) {
					if (response.data.success) {
						$utilidades.mensaje('success','Proyecto '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.cargarTabla();
					}else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Proyecto');
			}); 
			
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		
		
	 }
	
	mi.borrar = function() {
		if(mi.entidadseleccionada !=null){
			$http.post('/SProyecto', {
				accion: 'borrarProyecto',
				id: mi.entidadseleccionada.id
			}).success(function(response){
				if(response.success){
					$utilidades.mensaje('success','Proyecto borrado con éxito');
					mi.cargarTabla();
				}
				else
					$utilidades.mensaje('danger','Error al borrar el Proyecto');
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Proyecto que desea borrar');
	};
	
	mi.nuevo = function (){ 
		 
		mi.esColapsado = true;
		mi.entidadseleccionada = null;
		mi.esNuevo = true;
		mi.gridApi.selection.clearSelectedRows();
		mi.cargarCompos();
	};
	
	mi.editar = function() {
		if(mi.entidadseleccionada!=null){
			mi.cargarCompos();
			mi.esColapsado = true;
			mi.esNuevo = false;
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Cooperante que desea editar');
	}
	
	mi.irATabla = function() {
		mi.esColapsado=false;
	}
	
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'proyecto', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
		$http.post('/SEstadoTabla', tabla_data).then(function(response){
			
		});
	}
	
	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}
	
	mi.reiniciarVista=function(){
		if($location.path()=='/proyecto/rv')
			$route.reload();
		else
			$location.path('/proyecto/rv');
	}
	
	$http.post('/SCooperante', { accion: 'numeroProyectos' }).success(
			function(response) {
				mi.totalProyectos = response.totalproyectos;
				mi.cargarTabla(1);
	});
	
	mi.cargarCompos = function(){
		$http.post('/SProyecto', { accion: 'cargar_cooperantes' }).success(
			function(response) {
				mi.cooperantes = response.entidades;
		});
		
		$http.post('/SProyecto', { accion: 'cargar_proyectotipos' }).success(
				function(response) {
					mi.proyectotipos = response.entidades;
		});
		
		$http.post('/SProyecto', { accion: 'cargar_unidadesejecturoas' }).success(
				function(response) {
					mi.unidadesejecutoras = response.entidades;
		});
	}
	
	
} ]);