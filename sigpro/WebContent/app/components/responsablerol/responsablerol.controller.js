var app = angular.module('responsablerolController',[]);

app.controller('responsablerolController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','dialogoConfirmacion',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
	var mi = this;
	
	mi.totalResponsablesrol = 0;
	mi.mostrarcargando=true;
	mi.responsableRol = null;
	mi.esnuevo = false;
	mi.paginaActual = 1;
	mi.filtros = [];
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	
	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.responsableRol = [];
		mi.esNuevo = false;
	}
	
	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}
	
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'responsableRol', estado: JSON.stringify(estado) };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}
	
	mi.reiniciarVista=function(){
		if($location.path()=='/responsablerol/rv')
			$route.reload();
		else
			$location.path('/responsablerol/rv');
	}
	
	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}
	
	mi.nuevo = function(){
		mi.esNuevo = true;
		mi.responsableRol = {};
		mi.esColapsado = true;
		mi.gridApi.selection.clearSelectedRows();
		$utilidades.setFocus(document.getElementById("nombre"));
	}
	
	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalResponsablesRoles();
			mi.gridApi.selection.clearSelectedRows();
			mi.responsableRol = null;
		}
	};
	
	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOpciones.data[filaId]);
        mi.editar();
    };
    
    mi.editar = function(){
		if(mi.responsableRol!= null && mi.responsableRol.id!= null){
			mi.esNuevo = false;
			mi.esColapsado = true;
			$utilidades.setFocus(document.getElementById("nombre"));
		}else
			$utilidades.mensaje('warning','Debe seleccionar el tipo de responsable que desea editar');
	}
    
    mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOpciones.data[filaId]);
        mi.editar();
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
		    useExternalFiltering: true,
		    useExternalSorting: true,
		    rowTemplate: '<div ng-dblclick="grid.appScope.controller.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			columnDefs : [
				{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				{ name: 'nombre', width: 200 ,displayName: 'Nombre',cellClass: 'grid-align-left',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
				},
				{ name: 'descripcion', displayName: 'Descripcion', cellClass: 'grid-align-left', enableFiltering: false},
				{ name: 'responsableTipoNombre', width: 200 ,displayName: 'Tipo Responsable',cellClass: 'grid-align-left',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
				},
				{ name: 'usuarioCreo', displayName: 'Usuario Creación',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.controller.filtrar($event)" style="width:90px;"></input></div>'
				},
			    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
					filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:80px;" ></input></div>'
			    }
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope,function(row) {
					mi.responsableRol = row.entity;
				});

				gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
					if(sortColumns.length==1){
						grid.appScope.controller.columnaOrdenada=sortColumns[0].field;
						grid.appScope.controller.ordenDireccion = sortColumns[0].sort.direction;

						grid.appScope.controller.cargarTabla(grid.appScope.controller.paginaActual);
					}
					else if(sortColumns.length>1){
						sortColumns[0].unsort();
					}
					else{
						if(grid.appScope.controller.columnaOrdenada!=null){
							grid.appScope.controller.columnaOrdenada=null;
							grid.appScope.controller.ordenDireccion=null;
						}
					}
				} );

				if($routeParams.reiniciar_vista=='rv'){
					mi.guardarEstado();
					mi.obtenerTotalResponsablesRoles();
			    }
			    else{
			    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'responsableRol', t: (new Date()).getTime()}).then(function(response){
			    		  if(response.data.success && response.data.estado!='')
			    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
				    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
					      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
					      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
					      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
					      mi.obtenerTotalResponsablesRoles();
					  });
			    }
			}
		};
    
    mi.obtenerTotalResponsablesRoles = function(){
		$http.post('/SResponsableRol', { accion: 'numeroResponsableRolFiltro',t:moment().unix(),
			filtro_nombre: mi.filtros['nombre'],
			filtro_descripcion: mi.filtros['descripcion'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  } ).then(
				function(response) {
					mi.totalResponsablesrol = response.data.totalResposnablesRoles;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};
	
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SResponsableRol', { accion: 'getResponsableRolPaginafiltro', pagina: pagina,
			numeroresponsabletipo:  $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'], filtro_descripcion: mi.filtros['descripcion'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t:moment().unix()
			}).success(
				function(response) {
					mi.entidades = response.responsablesroles;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
	};
	
	mi.buscarResponsableTipo = function(){
		var resultado = mi.llamarModalBusqueda('/SResponsableTipo', 
				{accion : 'numeroResponsableTipo'
		}, function(pagina, elementosPorPagina){
			return {
				accion: 'getResponsableTipoPagina',
				pagina: pagina,
				numeroResponsableTipo : elementosPorPagina
			};
		}, 'id', 'nombre');
		
		resultado.then(function(itemSeleccionado){
			mi.responsableRol.responsableTipoId = itemSeleccionado.id;
			mi.responsableRol.responsableTipoNombre = itemSeleccionado.nombre;
		})
	}
	
	mi.guardar = function(esvalido){
		if(mi.responsableRol != null){
			$http.post('/SResponsableRol', 
			{
				accion: 'guardar',
				id: mi.responsableRol.id,
				nombre: mi.responsableRol.nombre,
				esnuevo : mi.esNuevo,
				descripcion: mi.responsableRol.descripcion,
				responsableTipo : mi.responsableRol.responsableTipoId
			}).then(function(response){
				if (response.data.success)
				{
					mi.responsableRol.id = response.data.id;
					mi.responsableRol.usuarioCreo = response.data.usuarioCreo;
					mi.responsableRol.fechaCreacion = response.data.fechaCreacion;
					mi.responsableRol.usuarioactualizo = response.data.usuarioactualizo;
					mi.responsableRol.fechaactualizacion = response.data.fechaactualizacion;
					$utilidades.mensaje('success','Tipo responsable '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
					mi.obtenerTotalResponsablesRoles();
					mi.esNuevo = false;
				}
			})
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	}
	
	mi.borrar = function(ev) {
		if(mi.responsableRol !=null && mi.responsableRol.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el Responsable rol "'+mi.responsableRol.nombre+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					$http.post('/SResponsableRol', {
						accion: 'borrarResponsableRol',
						id: mi.responsableRol.id,
						t:moment().unix()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Tipo responsable borrado con éxito');
							mi.responsableRol = null;
							mi.obtenerTotalResponsablesRoles();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el tipo de responsable');
					});
				}
			}, function(){
				
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Préstamo que desea borrar');
	};
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'busquedaporResponsableTipo.jsp',
			controller : 'busquedaporResponsableTipo',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};
}]);

app.controller('busquedaporResponsableTipo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre',busquedaporResponsableTipo ]);

function busquedaporResponsableTipo($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga,$columnaId,$columnaNombre) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post($servlet, $accionServlet).success(function(response) {
		for ( var key in response) {
			mi.totalElementos = response[key];
		}
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : $columnaId,
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Nombre',
			name : $columnaNombre,
			cellClass : 'grid-align-left'
		} ],
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect : false,
		modifierKeysToMultiSelect : false,
		noUnselect : false,
		enableFiltering : true,
		enablePaginationControls : false,
		paginationPageSize : 5,
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;
			mi.gridApi.selection.on.rowSelectionChanged($scope,
					mi.seleccionarTipoRiesgo);
		}
	}

	mi.seleccionarTipoRiesgo = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina)).then(
				function(response) {
					if (response.data.success) {

						for ( var key in response.data) {
							if (key != 'success')
								mi.data = response.data[key];
						}
						mi.opcionesGrid.data = mi.data;

						mi.mostrarCargando = false;
					}
				});
	};

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}