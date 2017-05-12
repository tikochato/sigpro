var app = angular.module('hitoController', []);

app.controller('hitoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Hitos';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.hitos = [];
		mi.hito;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.hitotipoid ="";
		mi.hitotipoNombre="";
		mi.totalHitos = 0;
		mi.paginaActual = 1;
		mi.proyectoid = $routeParams.proyecto_id;
		mi.proyectoNombre="";
		mi.objetoTipoNombre="";
		mi.formatofecha = 'dd/MM/yyyy';
		mi.hitodatotipoid = "";
		mi.hitoresultado="";
		mi.hitoresultadocomentario;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		mi.filtros = [];
		mi.orden = null;


		$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyecto_id }).success(
				function(response) {
					mi.proyectoid = response.id;
					mi.proyectoNombre = response.nombre;
					mi.objetoTipoNombre = "Proyecto";
		});

		mi.editarElemento = function (event) {
	        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
	        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
	        mi.editar();
	    };
	    
		mi.gridOptions = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: $utilidades.elementosPorPagina,
			    rowTemplate: '<div ng-dblclick="grid.appScope.hitoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.hitoc.filtrar($event,1)" ></input></div>'				    
					},
				    { name: 'hitotiponombre', width: 200, displayName: 'Tipo Hito',cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false  },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.hitoc.filtrar($event,2)" ></input></div>'
					},
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.hitoc.filtrar($event,3)" ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.hito = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.hitoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.hitoc.ordenDireccion = sortColumns[0].sort.direction;
							grid.appScope.hitoc.cargarTabla(grid.appScope.hitoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.hitoc.columnaOrdenada!=null){
								grid.appScope.hitoc.columnaOrdenada=null;
								grid.appScope.hitoc.ordenDireccion=null;
							}
						}
					} );

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						 mi.obtenerTotalHitos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'hitos', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
						      
						      mi.obtenerTotalHitos();
						  });
				    }
				}
		};

		mi.fechaOptions = {
			    formatYear: 'yy',
			    maxDate: new Date(2020, 5, 22),
			    minDate: new Date(1990,1,1),
			    startingDay: 1
		};

		mi.popupfecha = function() {
		    mi.popupfecha.abierto = false;
		};

		mi.popupfecharesultado = function() {
		    mi.popupfecharesultado.abierto = false;
		};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SHito', { accion: 'getHitosPaginaPorProyecto', pagina: pagina, numerohitos: $utilidades.elementosPorPagina, proyectoid:$routeParams.proyecto_id,
				numeroproyecto:  $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion}).success(
					function(response) {
						mi.hitos = response.hitos;
						mi.gridOptions.data = mi.hitos;
						mi.mostrarcargando = false;
					});
		}

		mi.guardar=function(){
			if(mi.hito!=null && mi.hito.fecha!='' && mi.hito.nombre!=''){
				$http.post('/SHito', {
					accion: 'guardarHito',
					esnuevo: mi.esnuevo,
					id: mi.hito.id,
					proyectoid: mi.proyectoid,
					nombre: mi.hito.nombre,
					hitotipoid: mi.hitotipoid,
					fecha:moment(mi.fecha).format('DD/MM/YYYY'),
					resultado: (mi.hitodatotipoid==5 ? (mi.hitoresultado!='' ? moment(mi.hitoresultado).format('DD/MM/YYYY'):null) : mi.hitoresultado),
					comentario : mi.hitoresultadocomentario,
					datotipoid: mi.hitodatotipoid,
					descripcion:  mi.hito.descripcion
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Hito '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.hito.id = response.id;
						mi.hito.usuarioCreo = response.usuarioCreo;
						mi.hito.fechaCreacion = response.fechaCreacion;
						mi.hito.usuarioActualizo = response.usuarioActualizo;
						mi.hito.fechaActualizacion = response.fechaActualizacion;
						mi.obtenerTotalHitos();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Hito');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.borrar = function(ev) {
			if(mi.hito!=null && mi.hito.id){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Hito "'+mi.hito.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SHito', {
							accion: 'borrarHito',
							id: mi.hito.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Hito borrado con éxito');
								mi.hito = null;
								mi.obtenerTotalHitos();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Hito');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Hito que desea borrar');
		};
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.hito = {};
			mi.fecha = null;
			mi.hitotipoid ="";
			mi.hitotipoNombre="";
			mi.hitoresultado = null;
			mi.hitoresultadocomentario = null;
			mi.gridApi.selection.clearSelectedRows();
			
			
		};

		mi.editar = function() {
			if(mi.hito!=null && mi.hito.id !=null){
				mi.fecha = moment(mi.hito.fecha, 'DD/MM/YYYY').toDate();
				mi.hitotipoid = mi.hito.hitotipoid;
				mi.hitotipoNombre= mi.hito.hitotiponombre;
				mi.hitoresultadocomentario = mi.hito.comentario;
				mi.hitodatotipoid = mi.hito.datotipoid;
				mi.mostraringreso = true;
				switch(mi.hitodatotipoid){
					case 3: 
						mi.hitoresultado = Number(mi.hito.resultado);
						break;
					case 4:
						mi.hitoresultado = mi.hito.resultado=='true' ? true : false;
						break;
					case 5:
						mi.hitoresultado = (mi.hitodatotipoid==5 ? moment(mi.hito.resultado, 'DD/MM/YYYY').toDate() : mi.hito.resultado);
						break;
					default:
						mi.hitoresultado = mi.hito.resultado;
				}
				mi.esnuevo = false;
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Hito que desea editar');
		}

		mi.irATabla = function() {
			mi.mostraringreso=false;
			mi.esnuevo=false;
		}
		
		mi.filtrar = function(evt,tipo){
			if(evt.keyCode==13){
				switch(tipo){
					case 1: mi.filtros['nombre'] = evt.currentTarget.value; break;
					case 2: mi.filtros['usuarioCreo'] = evt.currentTarget.value; break;
					case 3: mi.filtros['fechaCreacion'] = evt.currentTarget.value; break;
				}
				mi.obtenerTotalHitos();
				mi.gridApi.selection.clearSelectedRows();
				mi.hito = null;
			}
		}

		mi.obtenerTotalHitos = function(){
			$http.post('/SHito', { accion: 'numeroHitosPorProyecto', proyectoid:$routeParams.proyecto_id,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion']  }).then(
					function(response) {
						mi.totalProyectos = response.data.totalhitos;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'hitos', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()==('/hito/'+ mi.proyectoid + '/rv'))
				$route.reload();
			else
				$location.path('/hito/'+ mi.proyectoid + '/rv');
		}

		mi.abirpopup = function() {
			 mi.popupfecha.abierto = true;
		};

		mi.abirpopupreultado = function() {
			 mi.popupfecharesultado.abierto = true;
		};

		
		mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarHitoTipo.jsp',
				controller : 'buscarHitoTipo',
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
					}
				}
			});

			modalInstance.result.then(function(itemSeleccionado) {
				resultado.resolve(itemSeleccionado);
			});
			return resultado.promise;
	};

	mi.buscarHitoTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SHitoTipo', {
			accion : 'numeroHitoTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getHitoTiposPagina',
				pagina : pagina,
				numerohitotipos : elementosPorPagina
			};
		});

		resultado.then(function(itemSeleccionado) {
			mi.hitotipoid = itemSeleccionado.id;
			mi.hitotipoNombre = itemSeleccionado.nombre;
			mi.hitodatotipoid = itemSeleccionado.idTipo;
			console.log(mi.hitodatotipoid);
		});
	};

} ]);

app.controller('buscarHitoTipo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga', buscarHitoTipo ]);

function buscarHitoTipo($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga) {

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
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Nombre',
			name : 'nombre',
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
			$utilidades.mensaje('warning', 'Debe seleccionar un Tipo Hito');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}
