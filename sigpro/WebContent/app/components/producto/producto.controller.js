var moduloProducto = angular.module('moduloProducto', [ 'ngTouch',
		'smart-table', 'ui.grid.pinning', 'ui.grid.autoResize']);

moduloProducto.controller('controlProducto', [ '$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http', '$rootScope',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', '$q', 'uiGridTreeBaseService','uiGridConstants', 'dialogoConfirmacion', 'historia', 
		controlProducto ]);

function controlProducto($scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $rootScope, $interval, i18nService, $utilidades,
		$timeout, $log, $q, uiGridTreeBaseService,uiGridConstants, $dialogoConfirmacion, $historia) {
	var mi = this;  
	i18nService.setCurrentLang('es');
	
	mi.esTreeview = $rootScope.treeview;
	mi.botones = true;
	
	if(!mi.esTreeview)
		$window.document.title = $utilidades.sistema_nombre+' - Producto';
	mi.child_metas = null;
	mi.child_adquisiciones = null;
	mi.child_riesgos = null;
	
	mi.objetoId = $routeParams.objeto_id;
	mi.objetoTipo = $routeParams.objeto_tipo;
	
	mi.esForma = false;
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.entidadSeleccionada = -1;
	mi.seleccionada = false;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	
	mi.propiedadesValor = [];
	mi.camposdinamicos = {};
	
	mi.acumulacionesCosto = [];
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.esNuevo = false;
	mi.coordenadas = "";
	mi.objetoTipoNombre = "";
	mi.entidad='';
	mi.ejercicio = '';
	
	mi.metasCargadas = false;
	mi.adquisicionesCargadas = false;
	mi.riesgos = false;
	
	mi.dimensiones = [
		{value:1,nombre:'Dias',sigla:'d'}
	];
	
	mi.duracionDimension = mi.dimensiones[0];
	
	mi.verHistoria = function(){
		$historia.getHistoria($scope, 'Producto', '/SProducto',mi.producto.id)
		.result.then(function(data) {
			if (data != ""){
				
			}
		}, function(){
			
		});
	}
	
	if(mi.objetoTipo==1){
		mi.objetoTipoNombre = "Componente";
		$http.post('/SComponente', { accion: 'obtenerComponentePorId', id: mi.objetoId, t: (new Date()).getTime()}).success(
			function(response) {
				mi.componenteid = response.id;
				mi.prestamoId = response.prestamoId;
				mi.unidadEjecutora = response.unidadEjecutora;
				mi.unidadEjecutoraNombre = response.unidadEjecutoraNombre;
				mi.entidad = response.entidad;
				mi.ejercicio = response.ejercicio;
				mi.entidadnombre = response.entidadNombre;
				mi.objetoNombre = response.nombre;
				var fechaInicioPadre = moment(response.fechaInicio, 'DD/MM/YYYY').toDate();
				mi.modificarFechaInicial(fechaInicioPadre);
				mi.congelado = response.congelado;
			});
	}else if(mi.objetoTipo==2){
		mi.objetoTipoNombre = "Subcomponente";
		$http.post('/SSubComponente', { accion: 'obtenerSubComponentePorId', id: mi.objetoId, t: (new Date()).getTime()}).success(
				function(response) {
					mi.subcomponenteid = response.id;
					mi.objetoNombre = response.nombre;
					mi.prestamoId = response.prestamoId;
					mi.unidadEjecutora = response.unidadEjecutora;
					mi.unidadEjecutoraNombre = response.unidadEjecutoraNombre;
					mi.entidad = response.entidad;
					mi.ejercicio = response.ejercicio;
					mi.entidadnombre = response.entidadNombre;
					mi.objetoNombre = response.nombre;
					var fechaInicioPadre = moment(response.fechaInicio, 'DD/MM/YYYY').toDate();
					mi.modificarFechaInicial(fechaInicioPadre);
					mi.congelado = response.congelado;
				});
	}
	
	$http.post('/SAcumulacionCosto', { accion: 'getAcumulacionesCosto', t: (new Date()).getTime()}).success(
			function(response) {
				mi.acumulacionesCosto = response.acumulacionesTipos;
	});
	
	mi.blurCategoria=function(){
		if(document.getElementById("acumulacionCosto_value").defaultValue!=mi.producto.acumulacionCostoNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','acumulacionCosto');
		}
	}
	
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
	mi.altformatofecha = ['d!/M!/yyyy'];

	mi.cambioPagina = function() {
		mi.cargarTabla(mi.paginaActual);
	}

	
	mi.mostrarCargando = (mi.esTreeview) ? false : true;
	mi.data = [];
	mi.cargarTabla = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			registros : mi.elementosPorPagina,
			componenteid : mi.componenteid,
			subcomponenteid : mi.subcomponenteid,
			numeroproyecto:  $utilidades.elementosPorPagina, 
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], 
			filtro_fecha_creacion: mi.filtros['fechaCreacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, 
			t: (new Date()).getTime()
		};

		mi.mostrarCargando = true;
		$http.post('/SProducto', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.productos;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
				mi.paginaActual = pagina;
				
				for(x in mi.data){
					if(mi.data[x].fechaInicio != "")
						mi.data[x].fechaInicio = moment(mi.data[x].fechaInicio, 'DD/MM/YYYY').toDate();
					if(mi.data[x].fechaFin != "")
						mi.data[x].fechaFin = moment(mi.data[x].fechaFin, 'DD/MM/YYYY').toDate();
				}
			}
		});
	};


	mi.actualizarCosto = function(costo){
		mi.producto.costo = costo;
	}

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
			{ displayName : mi.objetoTipoNombre, name : mi.objetoTipoNombre.toLowerCase(), cellClass : 'grid-align-left', visible : false },
			{ displayName : 'Producto', name : 'producto', cellClass : 'grid-align-left', visible : false },
			{ name: 'usuarioCreo', displayName: 'Usuario Creación',cellClass: 'grid-align-left',
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
	
	mi.cambioAcumulacionCosto=function(selected){
		if(selected!== undefined){
			mi.producto.acumulacionCostoNombre = selected.originalObject.nombre;
			mi.producto.acumulacionCostoId = selected.originalObject.id;
		}
		else{
			mi.producto.acumulacionCostoNombre="";
			mi.producto.acumulacionCostoId="";
		}
	}

	mi.guardarEstado = function() {
		var estado = mi.gridApi.saveState.save();

		var tabla_data = {
			action : 'guardaEstado',
			grid : 'producto',
			estado : JSON.stringify(estado),
			t: (new Date()).getTime()
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
		
		mi.unidadEjecutora= mi.prestamoId != null ? mi.unidadEjecutora :  null;
		mi.unidadEjecutoraNombre= mi.prestamoId != null ? mi.unidadEjecutoraNombre : "";

		mi.propiedadesValor = [];
		mi.producto = {};
		
		mi.coordenadas = "";
		

		mi.duracionDimension = mi.dimensiones[0];
		
		for (campos in mi.camposdinamicos) {
			mi.camposdinamicos[campos].valor = null;
		}
		mi.metasCargadas = false;
		mi.adquisicionesCargadas = false;
		mi.riesgos = undefined;
		
		mi.activeTab = 0;
		$utilidades.setFocus(document.getElementById("nombre"));
	}

	mi.limpiarSeleccion = function() {
		if(!mi.esTreeview)
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
					, '¿Desea borrar el producto "' + mi.producto.nombre + '"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					mi.botones = false;
					var datos = {
							accion : 'borrar',
							codigo : mi.producto.id,
							t: (new Date()).getTime()
						};
						$http.post('/SProducto', datos).success(
								function(response) {
									if (response.success) {
										$utilidades.mensaje('success','Producto borrado con éxito');
										mi.producto = null;
										mi.obtenerTotalProductos();			
									} else{
										$utilidades.mensaje('danger',
												'Error al borrar el Producto');
									}
									mi.botones = true;
								});
				}
			}, function(){
				
			});
		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar el producto que desee borrar');
		}
	};

	mi.guardar = function() {
		mi.botones = false;
		if(mi.duracionDimension.sigla!=null && mi.duracionDimension.sigla!=''){
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
				componente : mi.componenteid,
				subcomponente : mi.subcomponenteid,
				productoPadre : mi.productoPadre,
				tipoproductoid : mi.tipo,
				unidadEjecutora : mi.unidadEjecutora,
				ejercicio: mi.ejercicio,
				entidad: mi.entidad,
				longitud: mi.producto.longitud,
				latitud : mi.producto.latitud,
				costo: mi.producto.costo,
				acumulacionCosto: mi.producto.acumulacionCostoId,
				fechaInicio: moment(mi.producto.fechaInicio).format('DD/MM/YYYY'),
				fechaFin: moment(mi.producto.fechaFin).format('DD/MM/YYYY'),
				duaracion: mi.producto.duracion,
				duracionDimension: mi.duracionDimension.sigla,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				esnuevo : mi.esNuevo,
				t: (new Date()).getTime()
			};

			$http.post('/SProducto', datos).then(
					function(response) {
						if (response.data.success) {
							mi.producto.id = response.data.id;
							mi.producto.usuarioCreo = response.data.usuarioCreo;
							mi.producto.fechaCreacion = response.data.fechaCreacion;
							mi.producto.usuarioactualizo = response.data.usuarioactualizo;
							mi.producto.fechaactualizacion = response.data.fechaactualizacion;
							if(mi.child_metas!=null)
								mi.child_metas.guardar((mi.child_adquisiciones!=null) ? mi.child_adquisiciones.guardar : null, (mi.child_riesgos!=null) ?  mi.child_riesgos.guardar : null,'Producto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
										'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Producto');
							else{
								if(mi.child_adquisiciones!=null)
									mi.child_adquisiciones.guardar((mi.child_riesgos!=null) ? mi.child_riesgos.guardar : null,'Producto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
										'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Producto');
								else{
									if(mi.child_riesgos!=null)
										mi.child_riesgos.guardar('Producto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
												'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Producto');
									else
										$utilidades.mensaje('success','Producto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
								}
							}
							if(!mi.esTreeview)
								mi.obtenerTotalProductos();
							else{
								if(!mi.esNuevo)
									mi.t_cambiarNombreNodo();
								else
									mi.t_crearNodo(mi.producto.id,mi.producto.nombre,3,true);
							}
							mi.esNuevo = false;
						} else {
							$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'crear' : 'guardar')+' el Producto');
						}
						mi.botones = true;
					});
		} else {
			$utilidades.mensaje('danger','Debe seleccionar una dimensión válida');
		}
	};

	mi.cancelar = function() {
		mi.esForma = false;
		mi.esNuevo=false;
		mi.child_metas = null;
		mi.child_adquisiciones = null;
		mi.child_riesgos = null;
	};
	
	mi.editar = function() {
		if (mi.producto!=null && mi.producto.id!=null) {
			mi.esForma = true;
			mi.entityselected = null;
			mi.esNuevo = false;
			mi.tipo = mi.producto.idProductoTipo;
			mi.tipoNombre = mi.producto.productoTipo;
		
			
			if(mi.producto.duracionDimension == 'd'){
				mi.duracionDimension = mi.dimensiones[0];
			}
			
			mi.componenteid = mi.producto.idComponente;
			mi.subcomponenteid = mi.producto.idSubComponente;

			mi.productoPadre = mi.producto.idProducto;
			mi.productoPadreNombre = mi.producto.producto;
			
			mi.coordenadas = (mi.producto.latitud !=null ?  mi.producto.latitud : '') +
			(mi.producto.latitud!=null ? ', ' : '') + (mi.producto.longitud!=null ? mi.producto.longitud : '');
			
			var parametros = {
					accion: 'getProductoPropiedadPorTipo', 
					idproducto: mi.producto.id,
					idproductotipo: mi.tipo,
					t: (new Date()).getTime()
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
				mi.metasCargadas = false;
				mi.adquisicionesCargadas = false;
				mi.riesgos = false;
				
				mi.activeTab = 0;
				$utilidades.setFocus(document.getElementById("nombre"));
			});
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar el producto que desee editar');
		}

	};
	
	mi.reiniciarVista = function() {
		if($location.path()==('/producto/'+ mi.objetoId + '/' + mi.objetoTipo + '/rv'))
			$route.reload();
		else
			$location.path('/producto/'+ mi.objetoId + '/' + mi.objetoTipo + '/rv');
		
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
		$http.post('/SProducto', { accion: 'totalElementos',componenteid : mi.componenteid, subcomponenteid : mi.subcomponenteid, 
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime() }).then(
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
			$location.path('/metas/'+ mi.producto.id +'/3' );
		}
	};
	
	mi.buscarAcumulacionCosto = function(){
		var resultado = mi.llamarModalBusqueda('Acumulación Costo','/SAcumulacionCosto', {
			accion : 'numeroAcumulacionCosto' 
		}, function(pagina, elementosPorPagina){
			return{
				accion: 'getAcumulacionCosto',
				pagina: pagina,
				numeroacumulacioncosto : elementosPorPagina
			}
		}, 'id','nombre', false,null);
		
		resultado.then(function(itemSeleccionado){
			mi.producto.acumulacionCostoNombre = itemSeleccionado.nombre;
			mi.producto.acumulacionCostoId = itemSeleccionado.id;
		});
	}
	
	mi.validarRequiredCosto = function(costo){
		if(costo != null && costo > 0)
			return "* Tipo de acumulación del costo";
		else
			return "Tipo de acumulación del costo";
	}

	mi.llamarModalBusqueda = function(titulo, servlet, accionServlet,  datosCarga, columnaId,columnaNombre, showfilters, entidad) {
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
				$titulo : function() {
					return titulo;
				},
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
				},
				$showfilters: function(){
					return showfilters;
				},
				$entidad: function(){
					return entidad;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});

		return resultado.promise;

	};

	mi.buscarTipo = function() {
		var resultado = mi.llamarModalBusqueda('Tipos de Producto','/SProductoTipo', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre',false, null);

		resultado.then(function(itemSeleccionado) {
			mi.tipo = itemSeleccionado.id;
			mi.tipoNombre = itemSeleccionado.nombre;
			
			var parametros = { 
				accion: 'getProductoPropiedadPorTipo', 
				idproducto: mi.producto.id,
				idproductotipo: itemSeleccionado.id, 
				t: (new Date()).getTime()
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
		if(mi.producto.fechaInicio!==undefined)
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

		var resultado = mi.llamarModalBusqueda('Productos','/SProducto', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre', false, null);

		resultado.then(function(itemSeleccionado) {
			mi.productoPadre = itemSeleccionado.id;
			mi.productoPadreNombre = itemSeleccionado.nombre;
		});

	};
	
	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('Unidades Ejecutoras','/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina,entidad, ejercicio) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina,
				entidad:entidad,
				ejercicio: ejercicio
			};
		},'unidadEjecutora','nombreUnidadEjecutora',true,{entidad: mi.entidad, ejercicio: mi.ejercicio, abreviatura:'', nombre: mi.entidadnombre});

		resultado.then(function(itemSeleccionado) {
			mi.unidadEjecutora = itemSeleccionado.unidadEjecutora;
			mi.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.entidad = itemSeleccionado.entidad;
			mi.ejercicio = itemSeleccionado.ejercicio;
			mi.entidadnombre = itemSeleccionado.nombreEntidad;
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
	    	if (coordenadas !=null && coordenadas != ""){
		    	mi.coordenadas = coordenadas.latitude + ", " + coordenadas.longitude;
		    	mi.producto.latitud= coordenadas.latitude;
				mi.producto.longitud = coordenadas.longitude;
	    	}else{
	    		mi.coordenadas = null;
	    		mi.producto.latitud = null;
	    		mi.producto.longitud = null;
	    	}
	    }, function() {
		});
	  };
	  
	  if(mi.esTreeview){
		  if($routeParams.nuevo==1){
			  mi.nuevo();
		  }
		  else{
			  $http.post('/SProducto', { accion : 'getProductoPorId', id: $routeParams.id, t: (new Date()).getTime() }).then(function(response) {
						if (response.data.success) {
							mi.producto = response.data.producto;
							if(mi.producto.fechaInicio != "")
								mi.producto.fechaInicio = moment(mi.producto.fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.producto.fechaFin != "")
								mi.producto.fechaFin = moment(mi.producto.fechaFin, 'DD/MM/YYYY').toDate();
							if(mi.producto.fechaInicioReal != "")
								mi.producto.fechaInicioReal = moment(mi.producto.fechaInicioReal, 'DD/MM/YYYY').toDate();
							if(mi.producto.fechaFinReal != "")
								mi.producto.fechaFinReal = moment(mi.producto.fechaFinReal, 'DD/MM/YYYY').toDate();
							mi.editar();
						}
					});
		  }
	  }
	  
	  mi.t_borrar = function(ev) {
			if (mi.producto!=null && mi.producto.id!=null) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el producto "' + mi.producto.nombre + '"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var datos = {
								accion : 'borrar',
								codigo : mi.producto.id,
								t: (new Date()).getTime()
							};
							$http.post('/SProducto', datos).success(
									function(response) {
										if (response.success) {
											
											$utilidades.mensaje('success','Producto borrado con éxito');
											mi.producto = null;		
											$rootScope.$emit("eliminarNodo", {});
										} else{
											$utilidades.mensaje('danger',
													'Error al borrar el Producto');
										}
									});
					}
				}, function(){
					
				});
			} else {
				$utilidades.mensaje('warning',
						'Debe seleccionar el producto que desee borrar');
			}
		};
		
		mi.t_cambiarNombreNodo = function(ev){
			$rootScope.$emit("cambiarNombreNodo",mi.producto.nombre);
		}
		
		mi.t_crearNodo=function(id,nombre,objeto_tipo,estado){
			$rootScope.$emit("crearNodo",{ id: id, nombre: nombre, objeto_tipo: objeto_tipo, estado: estado })
		}

		mi.metasActivo = function(){
			if(!mi.metasCargadas){
				mi.metasCargadas = true;
			}
		}
		
		mi.adquisicionesActivo = function(){
			if(!mi.adquisicionesCargadas){
				mi.adquisicionesCargadas = true;
			}
		}
		
		mi.riesgosActivo = function(){
			if(!mi.riesgos){
				mi.riesgos = true;
			}
		}
	  
}

moduloProducto.controller('modalBuscarPorProducto', [ '$uibModalInstance',
		'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log', '$titulo','$servlet', '$accionServlet', '$datosCarga',
		'$columnaId','$columnaNombre', '$showfilters', '$entidad',
		modalBuscarPorProducto ]);

function modalBuscarPorProducto($uibModalInstance, $rootScope,$scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log, $titulo, $servlet, $accionServlet,
		$datosCarga,$columnaId,$columnaNombre, $showfilters, $entidad) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	mi.showfilters = $showfilters;
	mi.ejercicios = [];
	mi.entidades = [];
	mi.titulo = $titulo;
	
	if(mi.showfilters){
		var current_year = moment().year();
		mi.entidad = $entidad;
		mi.ejercicio = $entidad.ejercicio;
		for(var i=current_year-$rootScope.catalogo_entidades_anos; i<=current_year; i++)
			mi.ejercicios.push(i);
		
		mi.ejercicio = ( mi.ejercicio == null || mi.ejercicio == "") ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio, t: (new Date()).getTime()}).success(function(response) {
			mi.entidades = response.entidades;
			if(mi.entidades.length>0){
				mi.entidad = (mi.entidad.entidad == null || mi.entidad===undefined || mi.entidad.entidad =="" ) ? mi.entidades[0] : mi.entidad;
				$accionServlet.ejercicio = mi.ejercicio;
				$accionServlet.entidad = mi.entidad.entidad;
				$http.post($servlet, $accionServlet).success(function(response) {
					for ( var key in response) {
						mi.totalElementos = response[key];
					}
					mi.cargarTabla(1,mi.ejercicio,mi.entidad.entidad);
				});
			}
		});
		
	}else{

		$http.post($servlet, $accionServlet).success(function(response) {
			for ( var key in response) {
				mi.totalElementos = response[key];
			}
			mi.cargarTabla(1,0,0);
		});
	
	}

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

	mi.cargarTabla = function(pagina,ejercicio, entidad) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina, entidad,ejercicio, (new Date()).getTime())).then(
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
		var entidad=null;
		if(mi.entidad){
			entidad = mi.entidad.entidad
		}
		mi.cargarTabla(mi.paginaActual, mi.ejercicio, entidad);
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
	
	mi.cambioEjercicio= function(){	
		mi.cargarTabla(1,mi.ejercicio, mi.entidad.entidad);
	}
	
	mi.cambioEntidad= function(selected){
		if(selected!==undefined){
			mi.entidad = selected.originalObject;
			$http.post('/SUnidadEjecutora', {accion:"totalElementos", ejercicio: mi.entidad.ejercicio,entidad: mi.entidad.entidad, t: (new Date()).getTime()}).success(function(response) {
				for ( var key in response) {
					mi.totalElementos = response[key];
				}
				mi.cargarTabla(1,mi.ejercicio,mi.entidad.entidad);
			});
		}
	}

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

	  $scope.borrar = function () {
		  $uibModalInstance.close(null);
	  };
}]);

