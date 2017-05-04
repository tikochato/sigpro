var moduloSubproducto = angular.module('moduloSubproducto', [ 'ngTouch',
		'smart-table' ]);

moduloSubproducto.controller('controlSubproducto', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', '$q',
		controlSubproducto ]);

function controlSubproducto($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $q) {
	var mi = this;  
	i18nService.setCurrentLang('es');
	$window.document.title = $utilidades.sistema_nombre+' - Subroducto';
	mi.productoid = $routeParams.producto_id;
	mi.esForma = false;
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.entidadSeleccionada = -1;
	mi.seleccionada = false;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.productoid = "";
	mi.productoNombre = "";
	mi.objetoTipoNombre = "";

	mi.propiedadesValor = [];
	mi.camposdinamicos = {};
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.esNuevo = false;
	
	mi.coordenadas = "";
	
	$http.post('/SSubproducto', { accion: 'obtenerSubproductoPorId', id: $routeParams.subproducto_id }).success(
			function(response) {
				mi.subproductoid = response.id;
				mi.subproductoNombre = response.nombre;
	});
	
	$http.post('/SProducto', { accion: 'obtenerProductoPorId', id: $routeParams.producto_id }).success(
			function(response) {
				mi.productoid = response.id;
				mi.productoNombre = response.nombre;
				mi.objetoTipoNombre = "Producto";
	});
	
	mi.formatofecha = 'dd/MM/yyyy';
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2030, 12, 31),
			minDate : new Date(1970, 1, 1),
			startingDay : 1
	};

	mi.cambioPagina = function() {
		mi.cargarTabla(mi.paginaActual);
	}

	
	mi.mostrarCargando = true;
	mi.data = [];
	
	mi.cargarTabla = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			registros : mi.elementosPorPagina,
			productoid : $routeParams.producto_id,
			numeroproyecto:  $utilidades.elementosPorPagina, 
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], 
			filtro_fecha_creacion: mi.filtros['fechaCreacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
		};

		mi.mostrarCargando = true;
		$http.post('/SSubproducto', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.subproductos;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});
	};

	mi.opcionesGrid = {
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
	    data : mi.data,
		columnDefs : [ 
			{displayName : 'Id',  width: 60, name : 'id',cellClass : 'grid-align-right',type : 'number',enableFiltering: false, enableSorting: false }, 
			{ displayName : 'Nombre',name : 'nombre',cellClass : 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-keypress="grid.appScope.subproducto.filtrar($event,1)" ></input></div>'
			}, 
			{ displayName : 'Descripción', name : 'descripcion', cellClass : 'grid-align-left' },
			{ displayName : 'Tipo', name : 'subproductoTipo', cellClass : 'grid-align-left', enableFiltering: false, enableSorting: false},  
			{ displayName : 'Producto', name : 'producto', cellClass : 'grid-align-left', visible : false },
			{ displayName : 'Subproducto', name : 'subproducto', cellClass : 'grid-align-left', visible : false },
			{ name: 'usuarioCreo', displayName: 'Usuario Creación',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-keypress="grid.appScope.subproducto.filtrar($event,2)" ></input></div>'
			},
		    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-keypress="grid.appScope.subproducto.filtrar($event,3)"  ></input></div>'
		    }
		],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.subproducto = row.entity;
			});
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.subproducto.columnaOrdenada=sortColumns[0].field;
					grid.appScope.subproducto.ordenDireccion = sortColumns[0].sort.direction;
					
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.subproducto.cargarTabla(grid.appScope.subproducto.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.subproducto.columnaOrdenada!=null){
						grid.appScope.subproducto.columnaOrdenada=null;
						grid.appScope.subproducto.ordenDireccion=null;
					}
				}
			} );

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
				mi.obtenerTotalSubproductos();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'subproducto', t: (new Date()).getTime()}).then(function(response){
				      if(response.data.success && response.data.estado!='')
				    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
				      mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
					  mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
					  mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      
					  mi.obtenerTotalSubproductos();
				  });
		    	  
		    }
		}
	}

	mi.guardarEstado = function() {
		var estado = mi.gridApi.saveState.save();

		var tabla_data = {
			action : 'guardaEstado',
			grid : 'subproducto',
			estado : JSON.stringify(estado),
			t : (new Date()).getTime()
		};
		$http.post('/SEstadoTabla', tabla_data).then(function(response) {

		});
	};
	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
	mi.nuevo = function() {
		mi.limpiarSeleccion();

		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;
		mi.subproducto = null;
		
		mi.tipo = null;
		mi.tipoNombre = "";

		mi.unidadEjecutora = null;
		mi.unidadEjecutoraNombre = "";

		mi.propiedadesValor = [];
		mi.subproducto = {};
		mi.coordenadas = "";

	}

	mi.limpiarSeleccion = function() {
		mi.gridApi.selection.clearSelectedRows();
		mi.seleccionada = false;
	}

	mi.seleccionarEntidad = function(row) {
		mi.entidadSeleccionada = row.entity;
		mi.seleccionada = row.isSelected;
	};

	

	mi.borrar = function(ev) {
		if (mi.subproducto!=null && mi.subproducto.id!=null) {
			var confirm = $mdDialog.confirm().title('Confirmación de borrado')
					.textContent(
							'¿Desea borrar "' + mi.subproducto.nombre
									+ '"?')
					.ariaLabel('Confirmación de borrado').targetEvent(ev).ok(
							'Borrar').cancel('Cancelar');

			$mdDialog.show(confirm).then(mi.borrarConfirmado,
					mi.borrarNoConfirmado);

		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar un Subproducto que desee borrar');
		}
	};

	mi.borrarConfirmado = function() {
		var datos = {
			accion : 'borrar',
			codigo : mi.subproducto.id
		};
		$http.post('/SSubproducto', datos).success(
				function(response) {
					if (response.success) {
						$utilidades.mensaje('success',
								'Subroducto borrado con éxito');
						mi.cargarTabla(1);
					} else
						$utilidades.mensaje('danger',
								'Error al borrar el Subproducto');
				});
	};

	mi.borrarNoConfirmado = function() {

	};

	mi.guardar = function() {
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
			}
		}
			var datos = {
				accion : 'guardar',
				id: mi.subproducto.id,
				nombre : mi.subproducto.nombre,
				descripcion : mi.subproducto.descripcion,
				snip: mi.subproducto.snip,
				programa : mi.subproducto.programa,
				subprograma : mi.subproducto.subprograma,
				proyecto_ : mi.subproducto.proyecto_,
				actividad: mi.subproducto.actividad,
				obra: mi.subproducto.obra,
				fuente: mi.subproducto.fuente,
				producto : ($routeParams.producto_id == undefined ? 0 : $routeParams.producto_id),
				subproductoPadre : mi.subproductoPadre,
				tiposubproductoid : mi.tipo,
				unidadEjecutora : mi.unidadEjecutora,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				longitud: mi.subproducto.longitud,
				latitud : mi.subproducto.latitud,
				esnuevo : mi.esNuevo
			};

			$http.post('/SSubproducto', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.subproductos;
							mi.opcionesGrid.data = mi.data;
							$utilidades.mensaje('success','Subproducto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
							mi.esNuevo = false;
							mi.subproducto.id = response.data.id;
							mi.subproducto.usuarioCreo = response.data.usuarioCreo;
							mi.subproducto.fechaCreacion = response.data.fechaCreacion;
							mi.subproducto.usuarioActualizo = response.data.usuarioactualizo;
							mi.subproducto.fechaActualizacion = response.data.fechaactualizacion;
							mi.cargarTabla(mi.paginaActual);
							
						} else {
							$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Subproducto');
						}
					});
		
	};

	mi.cancelar = function() {
		mi.esForma = false;
		mi.esNuevo=false;
	};
	
	mi.editar = function() {
		if (mi.subproducto!=null && mi.subproducto.id!=null) {
			mi.esForma = true;
			mi.entityselected = null;
			mi.esNuevo = false;
			mi.tipo = mi.subproducto.idSubproductoTipo;
			mi.tipoNombre = mi.subproducto.subproductoTipo;

			mi.subproductoPadre = mi.subproducto.idSubproducto;
			mi.subproductoPadreNombre = mi.subproducto.subproducto;
			
			mi.unidadEjecutora = mi.subproducto.unidadEjectuora;
			mi.unidadEjecutoraNombre = mi.subproducto.nombreUnidadEjecutora;
			mi.coordenadas = (mi.subproducto.latitud !=null ?  mi.subproducto.latitud : '') +
			(mi.subproducto.latitud!=null ? ', ' : '') + (mi.subproducto.longitud!=null ? mi.subproducto.longitud : '');
			
			var parametros = {
					accion: 'getSubproductoPropiedadPorTipo', 
					idsubproducto: mi.subproducto.id,
					idsubproductotipo: mi.tipo
			}
			$http.post('/SSubproductoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.subproductopropiedades
				for (campos in mi.camposdinamicos) {
					switch (mi.camposdinamicos[campos].tipo){
						case "fecha":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
							break;
						case "entero":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;
						case "decimal":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;
					}
				}
			});
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un subproducto');
		}

	};
	
	mi.reiniciarVista = function() {
		if($location.path()==('/subproducto/'+ $routeParams.producto_id + '/rv'))
			$route.reload();
		else
			$location.path('/subproducto/'+ $routeParams.producto_id + '/rv');
		
	}
	
	mi.filtrar = function(evt,tipo){
		if(evt.keyCode==13){
			switch(tipo){
				case 1: mi.filtros['nombre'] = evt.currentTarget.value; break;
				case 2: mi.filtros['usuarioCreo'] = evt.currentTarget.value; break;
				case 3: mi.filtros['fechaCreacion'] = evt.currentTarget.value; break;
			}
			mi.obtenerTotalSubproductos();
		}
	};

	mi.obtenerTotalSubproductos = function(){
		$http.post('/SSubproducto', { accion: 'totalElementos', productoid: $routeParams.producto_id,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion']  }).then(
				function(response) {
					mi.totalElementos = response.data.total;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};
	
	mi.irAActividades=function(){
		if(mi.subproducto.id!=null){
			$location.path('/actividad/'+ mi.subproducto.id +'/4' );
		}
	};
	
	mi.irARiesgos=function(){
		if(mi.subproducto.id!=null){
			$location.path('/riesgo/'+ mi.subproducto.id +'/4' );
		}
	};

	mi.llamarModalBusqueda = function(servlet, datosTotal, datosCarga, columnaId,columnaNombre) {
		var resultado = $q.defer();

		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorSubproducto.jsp',
			controller : 'modalBuscarPorSubproducto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$datosTotal : function() {
					return datosTotal;
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

	mi.buscarTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SSubproductoTipo', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.tipo = itemSeleccionado.id;
			mi.tipoNombre = itemSeleccionado.nombre;
			
			var parametros = { 
				accion: 'getSubproductoPropiedadPorTipo', 
				idsubproducto: mi.subproducto.id,
				idsubproductotipo: itemSeleccionado.id
			}
			$http.post('/SSubproductoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.subproductopropiedades;
				for (campos in mi.camposdinamicos) {
					switch (mi.camposdinamicos[campos].tipo){
						case "fecha":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
							break;
						case "entero":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;
						case "decimal":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;
					}
				}
				
			});
		});

	};

	mi.cambiarTipo = function() {

	};
	
	mi.abrirPopupFecha = function(index) {
		mi.camposdinamicos[index].isOpen = true;
	};

	mi.buscarSubproducto = function() {

		var resultado = mi.llamarModalBusqueda('/SSubproducto', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.subproductoPadre = itemSeleccionado.id;
			mi.subproductoPadreNombre = itemSeleccionado.nombre;
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
			mi.unidadEjecutora = itemSeleccionado.unidadEjecutora;
			mi.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
		});
	};
	
	mi.open = function (posicionlat, posicionlong) {
		$scope.geoposicionlat = posicionlat;
		$scope.geoposicionlong = posicionlong;

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
	    	if (coordenadas !=null){
		    	mi.coordenadas = coordenadas.latitude + ", " + coordenadas.longitude;
		    	mi.subproducto.latitud= coordenadas.latitude;
				mi.subproducto.longitud = coordenadas.longitude;
	    	}
	    }, function() {
		});
	  };
	  
}

moduloSubproducto.controller('modalBuscarPorSubproducto', [ '$uibModalInstance',
		'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', '$servlet', '$datosTotal', '$datosCarga',
		'$columnaId','$columnaNombre',
		modalBuscarPorSubproducto ]);

function modalBuscarPorSubproducto($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log, $servlet, $datosTotal,
		$datosCarga,$columnaId,$columnaNombre) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post($servlet, $datosTotal).success(function(response) {
		for ( var key in response) {
			mi.totalElementos = response[key];
		}
		mi.cargarTabla(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'Id',
			name : $columnaId,
			cellClass : 'grid-align-right',
			type : 'number',
			width : 150
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
					mi.seleccionarItem);
		}
	}

	mi.seleccionarItem = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarTabla = function(pagina) {
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
		mi.cargarTabla(mi.paginaActual);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un elemento');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

};

moduloSubproducto.controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
    function ($scope, $uibModalInstance,$timeout, uiGmapGoogleMapApi, glat, glong) {
	$scope.geoposicionlat = glat != null ? glat : 14.6290845;
	$scope.geoposicionlong = glong != null ? glong : -90.5116158;
	$scope.posicion = (glat !=null && glong !=null ) ? {latitude: glat, longitude: glong} : null;
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
		   events:{
			   click: function (map,evtName,evt) {
				   $scope.posicion = {latitude: evt[0].latLng.lat()+"", longitude: evt[0].latLng.lng()+""} ;
				   $scope.$evalAsync();
			   }
		   },
		   refresh: true
		};
    });

	  $scope.ok = function () {
		  $uibModalInstance.close($scope.posicion);
	  };
}]);