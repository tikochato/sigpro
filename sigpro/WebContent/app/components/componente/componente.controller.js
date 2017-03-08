var app = angular.module('componenteController', []);

app.controller('componenteController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Componentes';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.componentes = [];
		mi.componente;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalComponentes = 0;
		mi.proyectoid = $routeParams.proyecto_id;
		mi.proyectoNombre="";
		mi.paginaActual = 1;
		mi.datotipoid = "";
		mi.datotiponombre = "";
		mi.componentetipoid = "";
		mi.componentenombre = "";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.proyectoid = $routeParams.proyecto_id;
		mi.formatofecha = 'dd/MM/yyyy';
		mi.camposdinamicos = {};
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		mi.filtros = [];
		mi.orden = null;
		mi.latitud= "";
		mi.longitud = "";
		mi.coordenadas = "";

		$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyecto_id }).success(
				function(response) {
					mi.proyectoid = response.id;
					mi.proyectoNombre = response.nombre;
		});

		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2050, 12, 31),
				minDate : new Date(1990, 1, 1),
				startingDay : 1
		};
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
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
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentec.filtros[\'nombre\']"  ng-keypress="grid.appScope.componentec.filtrar($event)" ></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentec.filtros[\'usuarioCreo\']"  ng-keypress="grid.appScope.componentec.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentec.filtros[\'fechaCreacion\']"  ng-keypress="grid.appScope.componentec.filtrar($event)"  ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.componente = row.entity;
					});

					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.componentec.columnaOrdenada=sortColumns[0].field;
							grid.appScope.componentec.ordenDireccion = sortColumns[0].sort.direction;
							grid.appScope.componentec.cargarTabla(grid.appScope.componentec.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.componentec.columnaOrdenada!=null){
								grid.appScope.componentec.columnaOrdenada=null;
								grid.appScope.componentec.ordenDireccion=null;
							}
						}
					});

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalComponentes();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'componentes', t: (new Date()).getTime()}).then(function(response){
						      if(response.data.success && response.data.estado!=''){
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      }
						      mi.obtenerTotalComponentes();
						  });
				    	  
				    }
				}
		};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SComponente', { accion: 'getComponentesPaginaPorProyecto', pagina: pagina, numerocomponentes: $utilidades.elementosPorPagina,proyectoid: $routeParams.proyecto_id,
				numeroproyecto:  $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'], filtro_snip: mi.filtros['snip'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
			}).success(
					function(response) {
						mi.componentes = response.componentes;
						mi.gridOptions.data = mi.componentes;
						mi.mostrarcargando = false;
					});
		}

		mi.guardar=function(esvalido){
			for (campos in mi.camposdinamicos) {
				if (mi.camposdinamicos[campos].tipo === 'fecha') {
					mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
				}
			}
			if(mi.componente!=null && mi.componente.nombre!='' && mi.unidadejecutoraid!=''){
				$http.post('/SComponente', {
					accion: 'guardarComponente',
					esnuevo: mi.esnuevo,
					componentetipoid : mi.componentetipoid,
					id: mi.componente.id,
					proyectoid: $routeParams.proyecto_id,
					nombre: mi.componente.nombre,
					descripcion: mi.componente.descripcion,
					snip: mi.componente.snip,
					programa: mi.componente.programa,
					subprograma: mi.componente.subprograma,
					proyecto_: mi.componente.proyecto_,
					actividad: mi.componente.actividad,
					obra:mi.componente.obra,
					fuente: mi.componente.fuente,
					esnuevo: mi.esnuevo,
					unidadejecutoraid:mi.unidadejecutoraid,
					longitud: mi.longitud,
					latitud : mi.latitud,
					datadinamica : JSON.stringify(mi.camposdinamicos)
				}).success(function(response){
					if(response.success){
						mi.componente.id = response.id;
						$utilidades.mensaje('success','Componente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.cargarTabla();
						mi.esnuevo = false;
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Componente');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.borrar = function(ev) {
			if(mi.componente!=null && mi.componente.id!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar el Componente "'+mi.componente.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SComponente', {
						accion: 'borrarComponente',
						id: mi.componente.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Cooperante borrado con éxito');
							mi.componente = null;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Cooperante');
					});
			    }, function() {

			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Componente que desea borrar');
		};

		mi.nuevo = function() {
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.unidadejecutoraid="";
			mi.unidadejecutoranombre="";
			mi.componentetipoid="";
			mi.componentetiponombre="";
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.componente = {};
			mi.camposdinamicos = {};
			mi.latitud= "";
			mi.longitud = "";
			mi.coordenadas = "";
			mi.gridApi.selection.clearSelectedRows();

		};

		mi.editar = function() {
			if(mi.componente!=null){
				mi.unidadejecutoraid= mi.componente.unidadejecutoraid;
				mi.unidadejecutoranombre= mi.componente.unidadejecutoranombre;
				mi.componentetipoid=mi.componente.componentetipoid;
				mi.componentetiponombre=mi.componente.componentetiponombre;
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.longitud = mi.componente.longitud;
				mi.latitud = mi.componente.latitud;
				mi.coordenadas = (mi.latitud !=null ?  mi.latitud : '') +
				(mi.latitud!=null ? ', ' : '') + (mi.longitud!=null ? mi.longitud : '');


				var parametros = {
						accion: 'getComponentePropiedadPorTipo',
						idComponente: mi.componente!=null ? mi.componente.id : 0,
						idComponenteTipo: mi.componentetipoid
				}

				$http.post('/SComponentePropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.componentepropiedades;
					for (campos in mi.camposdinamicos) {
						switch (mi.camposdinamicos[campos].tipo){
							case "fecha":
								mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
								break;
							case "entero":
								mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
								break;
							case "decimal":
								mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
								break;
							case "booleano":
								mi.camposdinamicos[campos].valor = mi.camposdinamicos[campos].valor == 'true' ? true : false;
								break;
						}
					}
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Componente que desea editar');
		}

		mi.irATabla = function() {
			mi.mostraringreso=false;
			mi.esnuevo=false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'componentes', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()==('/componente/'+ mi.proyectoid + '/rv'))
				$route.reload();
			else
				$location.path('/componente/'+ mi.proyectoid + '/rv');
		}

		mi.abrirPopupFecha = function(index) {
			mi.camposdinamicos[index].isOpen = true;
		};

		
		mi.irAProductos=function(componenteid){
			if(mi.componente!=null){
				$location.path('/producto/'+componenteid);
			}
		};
		
		mi.irAActividades=function(componenteid){
			if(mi.componente!=null){
				$location.path('/actividad/'+ componenteid +'/2' );
			}
		};
		

		mi.filtrar = function(evt,tipo){
			if(evt.keyCode==13){
				mi.obtenerTotalComponentes();
			}
		}

		mi.obtenerTotalComponentes = function(){
			$http.post('/SComponente', { accion: 'numeroComponentesPorProyecto', proyectoid: $routeParams.proyecto_id,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion']  }).then(
					function(response) {
						mi.totalComponentes = response.data.totalcomponentes;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}

		mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarPorComponente.jsp',
				controller : 'buscarPorComponente',
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


		mi.buscarComponenteTipo = function() {
			var resultado = mi.llamarModalBusqueda('/SComponenteTipo', {
				accion : 'numeroComponenteTipos'
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getComponentetiposPagina',
					pagina : pagina,
					registros : elementosPorPagina
				};
			},'id','nombre');

			resultado.then(function(itemSeleccionado) {
				mi.componentetipoid = itemSeleccionado.id;
				mi.componentetiponombre = itemSeleccionado.nombre;
				var parametros = {
						accion: 'getComponentePropiedadPorTipo',
						idComponente: mi.componente!=null ? mi.componente.id : 0,
						idComponenteTipo: mi.componentetipoid
				}

				$http.post('/SComponentePropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.componentepropiedades
					for (campos in mi.camposdinamicos) {
						switch (mi.camposdinamicos[campos].tipo){
						case "fecha":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
							break;
						case "entero":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
							break;
						case "decimal":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
							break;
						case "booleano":
							mi.camposdinamicos[campos].valor = mi.camposdinamicos[campos].valor == 'true' ? true : false;
							break;
						}
					}
				});
			});
		};

		mi.buscarUnidadEjecutora = function() {
			var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
				accion : 'totalElementos'
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'cargar',
					pagina : pagina,
					registros : elementosPorPagina
				};
			},'unidadEjecutora','nombreUnidadEjecutora');

			resultado.then(function(itemSeleccionado) {
				mi.unidadejecutoraid = itemSeleccionado.unidadEjecutora;
				mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;
			});
		};
		
		
		mi.open = function (posicionlat, posicionlong) {
			mi.geoposicionlat = posicionlat;
			mi.geoposicionlong = posicionlong;

		    var modalInstance = $uibModal.open({
		      animation: true,
		      templateUrl: 'map.html',
		      controller: 'mapCtrl',
		      resolve: {
		        glat: function(){
		        	return $scope.geoposicionlat;
		        },
		        glong: function(){
		        	return $scope.geoposicionlong;
		        }
		      }

		    });

		    modalInstance.result.then(function(coordenadas) {
		    	mi.coordenadas = coordenadas.latitud + ", " + coordenadas.longitud;
		    	mi.latitud= coordenadas.latitud;
				mi.longitud = coordenadas.longitud;

		    }, function() {
			});

		  };
} ]);

app.controller('buscarPorComponente', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre',buscarPorComponente ]);

function buscarPorComponente($uibModalInstance, $scope, $http, $interval,
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
};

app.controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
    function ($scope, $uibModalInstance,$timeout, uiGmapGoogleMapApi, glat, glong) {
	$scope.geoposicionlat = glat != null ? glat : 14.6290845;
	$scope.geoposicionlong = glong != null ? glong : -90.5116158;

	$scope.refreshMap = true;

	uiGmapGoogleMapApi.then(function() {
		$scope.map = { center: { latitude: $scope.geoposicionlat, longitude: $scope.geoposicionlong },
					   zoom: 15,
					   height: 400,
					   width: 200,
					   options: {
						   streetViewControl: false,
						   scrollwheel: true,
						  draggable: true,
						  mapTypeId: google.maps.MapTypeId.SATELLITE
					   },
					   refresh: true
					};
    });



	  $scope.ok = function () {


	   var coordenadas = {};
	   coordenadas.latitud = $scope.geoposicionlat;
	   coordenadas.longitud = $scope.geoposicionlong;

	   $uibModalInstance.close(coordenadas);
	  };

}]);

