var app = angular.module('tipoAdquisicionController',['ngTouch']);

app.controller('tipoAdquisicionController', ['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion){
		var mi = this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Tipo de Adquisición';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.paginaActual = 1;
		mi.tipoAdquisicion;
		mi.tipoAdquisiciones = [];
		mi.filtros = [];
		
		mi.cambioPagina = function() {
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()==('/tipoadquisicion' + '/rv'))
				$route.reload();
			else
				$location.path('/tipoadquisicion' + '/rv');
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'tipoadquisicion', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}
		
		mi.obtenerTotalTipoAdquisicion = function(){
			$http.post('/STipoAdquisicion', { accion: 'numeroTipoAdquisicion', 
				filtro_cooperante: mi.filtros['cooperante'],
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime() }).then(
					function(response) {
						mi.totalTipoAdquisicion = response.data.totalTipoAdquisicion;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/STipoAdquisicion', { accion: 'getTipoAdquisicionPagina', pagina: pagina, numeroTipoAdquisicion: $utilidades.elementosPorPagina
				,filtro_cooperante: mi.filtros['cooperante']
				,filtro_nombre: mi.filtros['nombre'] 
				,filtro_usuario_creo: mi.filtros['usuarioCreo']
			    ,filtro_fecha_creacion: mi.filtros['fechaCreacion']
			    ,columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
			    }).success(
			
					function(response) {
						mi.tipoAdquisiciones = response.tipoAdquisicion;
						mi.gridOptions.data = mi.tipoAdquisiciones;
						mi.mostrarcargando = false;
					});
		}
		
		mi.gridOptions = {
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
					{ name: 'cooperanteNombre', width: 200, displayName: 'Cooperante', cellClass: 'grid-align-left'
						,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'cooperante\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
					},
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left'
				    	,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
				    },
				    { name: 'usuarioCreo', displayName: 'Usuario Creación' 
				    	,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuarioCreo\']" ng-keypress="grid.appScope.controller.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fechaCreacion\']" ng-keypress="grid.appScope.controller.filtrar($event)" ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					
					gridApi.selection.on.rowSelectionChanged($scope,
							mi.seleccionarEntidad);
					
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
								
					});
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalTipoAdquisicion();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'programaTipos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!='')
				    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalTipoAdquisicion();
						  });
				    }
				}
		};
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalTipoAdquisicion();
				mi.gridApi.selection.clearSelectedRows();
				mi.seleccionada = null;
			}
		};
		
		mi.editarElemento = function (event) {
	        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
	        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
	        mi.editar();
	    };
		
		mi.editar = function(){
			if(mi.seleccionada){
				mi.mostraringreso = true;
				mi.esNuevo = false;	
				$utilidades.setFocus(document.getElementById("icoope"));
			}else{
				$utilidades.mensaje('warning',
				'Debe seleccionar un TIPO DE ADQUISICION que desee editar');
			}
		}
		
		mi.borrar = function(){
			if(mi.seleccionada){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar "' + mi.tipoAdquisicion.nombre + '"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var datos = {
								accion : 'borrarTipoAdquisicion',
								tipoAdquisicionId : mi.tipoAdquisicion.id,
								t: (new Date()).getTime()

							};
							$http.post('/STipoAdquisicion', datos).success(
									function(response) {
										if (response.success) {
											$utilidades.mensaje('success',
													'Tipo de Adquisicion borrado con éxito');
											mi.seleccionada = null;
											mi.obtenerTotalTipoAdquisicion();
										} else
											$utilidades.mensaje('danger',
													'Error al bSorrar el Tipo de Adquisición');
									});
					}
				}, function(){
					
				});
			} else {
				$utilidades.mensaje('warning',
						'Debe seleccionar un TIPO DE ADQUISICION que desee borrar');
			}
		}
		
		mi.irATabla = function(){
			mi.seleccionada=false;
			mi.mostraringreso = false;
		}
		
		mi.nuevo = function(){
			mi.limpiarSeleccion();
			mi.mostraringreso = true;
			mi.esNuevo = true;
			$utilidades.setFocus(document.getElementById("icoope"));
			mi.tipoAdquisicion = {};
			mi.tipoAdquisicion.cooperanteCodigo = 0;
			mi.tipoAdquisicion.cooperanteNombre = '';
		}
		
		mi.limpiarSeleccion = function() {
			mi.gridApi.selection.clearSelectedRows();
			mi.seleccionada = false;
		}
		
		mi.seleccionarEntidad = function(row) {
			mi.tipoAdquisicion = row.entity;
			mi.seleccionada = row.isSelected;
		};
		
		mi.buscarCooperante = function(prestamo) {
			var resultado = mi.llamarModalBusqueda('/SCooperante', {
				accion : 'numeroCooperantes', t:moment().unix()
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getCooperantesPagina',
					pagina : pagina,
					numerocooperantes : elementosPorPagina
				};
			},'id','nombre');

			resultado.then(function(itemSeleccionado) {
				mi.tipoAdquisicion.cooperanteCodigo= itemSeleccionado.codigo;
				mi.tipoAdquisicion.cooperanteNombre = itemSeleccionado.siglas != null ? itemSeleccionado.siglas + " - " + itemSeleccionado.nombre : itemSeleccionado.nombre;
			});
		};
		
		mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga, columnaId, columnaNombre) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarCooperante.jsp',
				controller : 'modalBuscar',
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
					$columnaId : function(){
						return columnaId;
					},
					$columnaNombre : function(){
						return columnaNombre;
					}
				}
			});

			modalInstance.result.then(function(itemSeleccionado) {
				resultado.resolve(itemSeleccionado);
			});
			return resultado.promise;
		};
		
		mi.guardar = function(){
			mi.acciones = "";

			$http.post('/STipoAdquisicion', {
				accion: 'guardarTipoAdquisicion',
				idTipoAdquisicion : mi.tipoAdquisicion.id,
				cooperanteCodigo : mi.tipoAdquisicion.cooperanteCodigo,
				nombreTipoAdquisicion : mi.tipoAdquisicion.nombre,
				esNuevo : mi.esNuevo,
				convenioCDirecta: mi.tipoAdquisicion.esConvenioCdirecta == true ? 1 : 0,
				t: (new Date()).getTime()
			}).success(function(response){
				if(response.success){
					mi.tipoAdquisicion.usuarioCreo = response.usuarioCreo;
					mi.tipoAdquisicion.fechaCreacion = response.fechaCreacion;
					mi.tipoAdquisicion.usuarioActualizo = response.usuarioactualizo;
					mi.tipoAdquisicion.fechaActualizacion = response.fechaactualizacion;
					mi.tipoAdquisicion.id=response.id;
					mi.esNuevo=false;
					$utilidades.mensaje('success', 'Tipo de adquisicion guardado con éxito');
					mi.obtenerTotalTipoAdquisicion();
				}
			})
		}
}]);

app.controller('modalBuscar', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga','$columnaId','$columnaNombre',modalBuscar]);

function modalBuscar($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga,$columnaId,$columnaNombre) {

	var mi = this;
	
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	mi.mostrarCargando = false;
	mi.data = [];
	$accionServlet.t=(new Date()).getTime();

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
			mi.gridApi.selection.on.rowSelectionChanged($scope,	mi.seleccionarCooperante);
		}
	}

	mi.seleccionarCooperante = function(row) {
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
		if (mi.seleccionado && ((mi.rolAsignado != null && mi.rolAsignado != undefined) || !mi.mostrarRoles  )) {
			if (mi.mostrarRoles){
				mi.itemSeleccionado.rol = mi.rolAsignado.id;
				mi.itemSeleccionado.nombrerol = mi.rolAsignado.nombre;
			}
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}