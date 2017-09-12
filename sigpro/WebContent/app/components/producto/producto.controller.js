var moduloProducto = angular.module('moduloProducto', [ 'ngTouch',
		'smart-table']);

moduloProducto.controller('controlProducto', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', '$q', 'dialogoConfirmacion', 
		controlProducto ]);

function controlProducto($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $q, $dialogoConfirmacion) {
	var mi = this;  
	i18nService.setCurrentLang('es');
	$window.document.title = $utilidades.sistema_nombre+' - Producto';
	    
	mi.componenteid = $routeParams.componente_id;
	mi.esForma = false;
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.entidadSeleccionada = -1;
	mi.seleccionada = false;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;

	mi.propiedadesValor = [];
	mi.camposdinamicos = {};
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.esNuevo = false;
	mi.coordenadas = "";
	mi.objetoTipoNombre = "";
	
	mi.dimensiones = [
		{value:0,nombre:'Seleccione una opción'},
		{value:1,nombre:'Dias',sigla:'d'}
	];
	
	mi.duracionDimension = mi.dimensiones[0];
	
	$http.post('/SComponente', { accion: 'obtenerComponentePorId', id: $routeParams.componente_id }).success(
			function(response) {
				mi.componenteid = response.id;
				mi.componenteNombre = response.nombre;
				mi.objetoTipoNombre = "Componente";
				var fechaInicioPadre = moment(response.fechaInicio, 'DD/MM/YYYY').toDate();
				mi.modificarFechaInicial(fechaInicioPadre);
	});
	
	mi.modificarFechaInicial = function(fechaPadre){
		mi.fi_opciones.minDate = fechaPadre;
	}
	
	mi.fi_opciones = {
			formatYear : 'yy',
			maxDate : new Date(2020, 5, 22),
			minDate : new Date(1900, 1, 1),
			startingDay : 1
	}
	
	mi.ff_opciones = {
			formatYear : 'yy',
			maxDate : new Date(2020, 5, 22),
			minDate : new Date(1900, 1, 1),
			startingDay : 1
	}
	
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	mi.formatofecha = 'dd/MM/yyyy';

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
			componenteid : $routeParams.componente_id,
			numeroproyecto:  $utilidades.elementosPorPagina, 
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], 
			filtro_fecha_creacion: mi.filtros['fechaCreacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
		};

		mi.mostrarCargando = true;
		$http.post('/SProducto', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.productos;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
				
				for(x in mi.data){
					if(mi.data[x].fechaInicio != "")
						mi.data[x].fechaInicio = moment(mi.data[x].fechaInicio, 'DD/MM/YYYY').toDate();
					if(mi.data[x].fechaFin != "")
						mi.data[x].fechaFin = moment(mi.data[x].fechaFin, 'DD/MM/YYYY').toDate();
				}
			}
		});
	};


	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.opcionesGrid.data[filaId]);
        mi.editar();
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
	    rowTemplate: '<div ng-dblclick="grid.appScope.producto.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
	    columnDefs : [ 
			{displayName : 'Id',  width: 100, name : 'id',cellClass : 'grid-align-right',type : 'number',width : 150 , enableFiltering: false}, 
			{ displayName : 'Nombre',name : 'nombre',cellClass : 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"  style="width: 90%;" ng-keypress="grid.appScope.producto.filtrar($event,1)" ></input></div>'
			}, 
			{ displayName : 'Descripción', name : 'descripcion', cellClass : 'grid-align-left', enableFiltering: false },
			{ displayName : 'Tipo', name : 'productoTipo', cellClass : 'grid-align-left', enableFiltering: false, enableSorting: false},  
			{ displayName : 'Componente', name : 'componente', cellClass : 'grid-align-left', visible : false },
			{ displayName : 'Producto', name : 'producto', cellClass : 'grid-align-left', visible : false },
			{ name: 'usuarioCreo', displayName: 'Usuario Creación',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"  style="width: 90%;" ng-keypress="grid.appScope.producto.filtrar($event,2)" ></input></div>'
			},
		    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"  style="width: 90%;" ng-keypress="grid.appScope.producto.filtrar($event,3)"  ></input></div>'
		    }
		],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.producto = row.entity;
			});
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.producto.columnaOrdenada=sortColumns[0].field;
					grid.appScope.producto.ordenDireccion = sortColumns[0].sort.direction;
					
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.producto.cargarTabla(grid.appScope.producto.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.producto.columnaOrdenada!=null){
						grid.appScope.producto.columnaOrdenada=null;
						grid.appScope.producto.ordenDireccion=null;
					}
				}
			} );

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
				mi.obtenerTotalProductos();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'producto', t: (new Date()).getTime()}).then(function(response){
				      if(response.data.success && response.data.estado!='')
				    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
				      mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
					  mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
					  mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
					  mi.obtenerTotalProductos();
				  });
		    	  
		    }
		}
	}

	mi.guardarEstado = function() {
		var estado = mi.gridApi.saveState.save();

		var tabla_data = {
			action : 'guardaEstado',
			grid : 'producto',
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
		mi.producto = null;
		
		mi.tipo = null;
		mi.tipoNombre = "";

		mi.unidadEjecutora = null;
		mi.unidadEjecutoraNombre = "";

		mi.propiedadesValor = [];
		mi.producto = {};
		
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
		if (mi.producto!=null && mi.producto.id!=null) {
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar "' + mi.producto.nombre + '"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					var datos = {
							accion : 'borrar',
							codigo : mi.producto.id
						};
						$http.post('/SProducto', datos).success(
								function(response) {
									if (response.success) {
										$utilidades.mensaje('success','Tipo de Producto borrado con éxito');
										mi.producto = null;
										mi.obtenerTotalProductos();			
									} else{
										$utilidades.mensaje('danger',
												'Error al borrar el Tipo de Producto');
									}
								});
				}
			}, function(){
				
			});
		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar un PRODUCTO que desee borrar');
		}
	};

	mi.guardar = function() {
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
			}
		}
			var datos = {
				accion : 'guardar',
				id: mi.producto.id,
				nombre : mi.producto.nombre,
				descripcion : mi.producto.descripcion,
				snip: mi.producto.snip,
				programa : mi.producto.programa,
				subprograma : mi.producto.subprograma,
				proyecto_ : mi.producto.proyecto_,
				actividad: mi.producto.actividad,
				obra: mi.producto.obra,
				renglon: mi.producto.renglon,
				ubicacionGeografica: mi.producto.ubicacionGeografica,
				componente : $routeParams.componente_id,
				productoPadre : mi.productoPadre,
				tipoproductoid : mi.tipo,
				unidadEjecutora : mi.unidadEjecutora,
				longitud: mi.producto.longitud,
				latitud : mi.producto.latitud,
				costo: mi.producto.costo == null ? 0 : mi.producto.costo,
				acumulacionCosto: mi.producto.acumulacionCostoId == null ? 0 : mi.producto.acumulacionCostoId,
				fechaInicio: moment(mi.producto.fechaInicio).format('DD/MM/YYYY'),
				fechaFin: moment(mi.producto.fechaFin).format('DD/MM/YYYY'),
				duaracion: mi.producto.duracion,
				duracionDimension: mi.duracionDimension.sigla,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				esnuevo : mi.esNuevo
			};

			$http.post('/SProducto', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.productos;
							mi.opcionesGrid.data = mi.data;
							$utilidades.mensaje('success','Producto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
							mi.esNuevo = false;
							mi.producto.id = response.data.id;
							mi.producto.usuarioCreo = response.data.usuarioCreo;
							mi.producto.fechaCreacion = response.data.fechaCreacion;
							mi.producto.usuarioactualizo = response.data.usuarioactualizo;
							mi.producto.fechaactualizacion = response.data.fechaactualizacion;
							mi.obtenerTotalProductos();
						} else {
							$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Producto');
						}
					});
		
	};

	mi.cancelar = function() {
		mi.esForma = false;
		mi.esNuevo=false;
	};
	
	mi.editar = function() {
		if (mi.producto!=null && mi.producto.id!=null) {
			mi.esForma = true;
			mi.entityselected = null;
			mi.esNuevo = false;
			mi.tipo = mi.producto.idProductoTipo;
			mi.tipoNombre = mi.producto.productoTipo;
			
			if(mi.producto.duracionDimension == 'd'){
				mi.duracionDimension = mi.dimensiones[1];
			}else{
				mi.duracionDimension = mi.dimensiones[0];
			}

			mi.productoPadre = mi.producto.idProducto;
			mi.productoPadreNombre = mi.producto.producto;
			
			mi.unidadEjecutora = mi.producto.unidadEjectuora;
			mi.unidadEjecutoraNombre = mi.producto.nombreUnidadEjecutora;
			
			mi.coordenadas = (mi.producto.latitud !=null ?  mi.producto.latitud : '') +
			(mi.producto.latitud!=null ? ', ' : '') + (mi.producto.longitud!=null ? mi.producto.longitud : '');
			
			var parametros = {
					accion: 'getProductoPropiedadPorTipo', 
					idproducto: mi.producto.id,
					idproductotipo: mi.tipo
			}
			$http.post('/SProductoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.productopropiedades
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
			$utilidades.mensaje('warning', 'Debe seleccionar un PRODUCTO');
		}

	};
	
	mi.reiniciarVista = function() {
		if($location.path()==('/producto/'+ $routeParams.componente_id + '/rv'))
			$route.reload();
		else
			$location.path('/producto/'+ $routeParams.componente_id + '/rv');
		
	}
	
	mi.filtrar = function(evt,tipo){
		if(evt.keyCode==13){
			switch(tipo){
				case 1: mi.filtros['nombre'] = evt.currentTarget.value; break;
				case 2: mi.filtros['usuarioCreo'] = evt.currentTarget.value; break;
				case 3: mi.filtros['fechaCreacion'] = evt.currentTarget.value; break;
			}
			mi.obtenerTotalProductos();
			mi.gridApi.selection.clearSelectedRows();
			mi.producto = null;
		}
	};

	mi.obtenerTotalProductos = function(){
		$http.post('/SProducto', { accion: 'totalElementos',componenteid : $routeParams.componente_id,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion']  }).then(
				function(response) {
					mi.totalElementos = response.data.total;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};
	
	mi.irASubproductos=function(){
		if(mi.producto.id!=null){
			$location.path('/subproducto/'+ mi.producto.id );
		}
	};
	
	mi.irAActividades=function(){
		if(mi.producto.id!=null){
			$location.path('/actividad/'+ mi.producto.id +'/3' );
		}
	};
	
	mi.irARiesgos=function(){
		if(mi.producto.id!=null){
			$location.path('/riesgo/'+ mi.producto.id +'/3' );
		}
	};
	
	mi.irAMetas=function(){
		if(mi.producto.id!=null){
			$location.path('/meta/'+ mi.producto.id +'/3' );
		}
	};
	
	mi.buscarAcumulacionCosto = function(){
		var resultado = mi.llamarModalBusqueda('/SAcumulacionCosto', {
			accion : 'numeroAcumulacionCosto' 
		}, function(pagina, elementosPorPagina){
			return{
				accion: 'getAcumulacionCosto',
				pagina: pagina,
				numeroacumulacioncosto : elementosPorPagina
			}
		}, 'id','nombre');
		
		resultado.then(function(itemSeleccionado){
			mi.producto.acumulacionCostoNombre = itemSeleccionado.nombre;
			mi.producto.acumulacionCostoId = itemSeleccionado.id;
		});
	}

	mi.llamarModalBusqueda = function(servlet, datosTotal, datosCarga, columnaId,columnaNombre) {
		var resultado = $q.defer();

		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProducto.jsp',
			controller : 'modalBuscarPorProducto',
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
		var resultado = mi.llamarModalBusqueda('/SProductoTipo', {
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
				accion: 'getProductoPropiedadPorTipo', 
				idproducto: mi.producto.id,
				idproductotipo: itemSeleccionado.id
			}
			$http.post('/SProductoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.productopropiedades;
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
		if(index > 0 && index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		}
	};
	
	mi.cambioDuracion = function(dimension){
		mi.producto.fechaFin = mi.sumarDias(mi.producto.fechaInicio,mi.producto.duracion, dimension.sigla);
	}
	
	mi.sumarDias = function(fecha, dias, dimension){
		if(dimension != undefined && dias != undefined && fecha != ""){
			var cnt = 0;
		    var tmpDate = moment(fecha);
		    while (cnt < (dias -1 )) {
		    	if(dimension=='d'){
		    		tmpDate = tmpDate.add(1,'days');	
		    	}
		        if (tmpDate.weekday() != moment().day("Sunday").weekday() && tmpDate.weekday() != moment().day("Saturday").weekday()) {
		            cnt = cnt + 1;
		        }
		    }
		    tmpDate = moment(tmpDate,'DD/MM/YYYY').toDate();
		    return tmpDate;
		}
	}

	mi.buscarProducto = function() {

		var resultado = mi.llamarModalBusqueda('/SProducto', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.productoPadre = itemSeleccionado.id;
			mi.productoPadreNombre = itemSeleccionado.nombre;
		});

	};
	
	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('Unidad Ejecutora','/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina,
				entidad:entidad,
				
					
			};
		},'unidadEjecutora','nombreUnidadEjecutora',false);

		resultado.then(function(itemSeleccionado) {
			mi.unidadEjecutora = itemSeleccionado.unidadEjecutora;
			mi.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.ejercicio = itemSeleccionado.ejercicio;
			mi.entidad = itemSeleccionado.entidad;
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
		    	mi.producto.latitud= coordenadas.latitude;
				mi.producto.longitud = coordenadas.longitude;
	    	}
	    }, function() {
		});
	  };
	  
}

moduloProducto.controller('modalBuscarPorProducto', [ '$uibModalInstance',
		'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', '$servlet', '$datosTotal', '$datosCarga',
		'$columnaId','$columnaNombre',
		modalBuscarPorProducto ]);

function modalBuscarPorProducto($uibModalInstance, $scope, $http, $interval,
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

moduloProducto.controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
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