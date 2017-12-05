var moduloSubproducto = angular.module('moduloSubproducto', [ 'ngTouch',
		'smart-table' ]);

moduloSubproducto.controller('controlSubproducto', [ '$rootScope','$scope', '$routeParams',
		'$route', '$window', '$location', '$mdDialog', '$uibModal', '$http',
		'$interval', 'i18nService', 'Utilidades', '$timeout', '$log', '$q', 'dialogoConfirmacion', 'historia',
		controlSubproducto ]);

function controlSubproducto($rootScope,$scope, $routeParams, $route, $window, $location,
		$mdDialog, $uibModal, $http, $interval, i18nService, $utilidades,
		$timeout, $log, $q, $dialogoConfirmacion, $historia) {
	var mi = this;  
	i18nService.setCurrentLang('es');
	mi.esTreeview = $rootScope.treeview;
	mi.botones = true;
	
	mi.child_adquisiciones = null;
	mi.child_riesgos =  null;
	
	if(!mi.esTreeview)
		$window.document.title = $utilidades.sistema_nombre+' - Subroducto';
	
	mi.productoid = $routeParams.producto_id;
	mi.esForma = false;
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.entidadSeleccionada = -1;
	mi.seleccionada = false;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.productoNombre = "";
	mi.objetoTipoNombre = "";
	mi.entidad='';
	mi.ejercicio = '';

	mi.formatofecha = 'dd/MM/yyyy';
	mi.altformatofecha = ['d!/M!/yyyy'];
	
	mi.acumulacionesCosto = [];
	
	mi.fechaFinPadre;

	mi.dimensiones = [
		{value:1,nombre:'Dias',sigla:'d'}
	];
	
	mi.duracionDimension = mi.dimensiones[0];

	mi.propiedadesValor = [];
	mi.camposdinamicos = {};
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.esNuevo = false;
	
	mi.coordenadas = "";
	
	mi.botones =  true;
	
	mi.adquisicionesCargadas = false;
	mi.riesgos = false;
	
	mi.verHistoria = function(){
		$historia.getHistoria($scope, 'Sub Producto', '/SSubproducto',mi.subproducto.id)
		.result.then(function(data) {
			if (data != ""){
				
			}
		}, function(){
			
		});
	}
	
	mi.objetoTipoNombre = "Producto:";
	$http.post('/SProducto', { accion: 'obtenerProductoPorId', id: mi.productoid, t: (new Date()).getTime()}).success(
		function(response) {
			mi.productoid = response.id;
			mi.prestamoId = response.prestamoId;
			mi.unidadEjecutora = response.unidadEjecutora;
			mi.unidadEjecutoraNombre = response.unidadEjecutoraNombre;
			mi.entidad = response.entidad;
			mi.ejercicio = response.ejercicio;
			mi.entidadnombre = response.entidadNombre;
			mi.productoNombre = response.nombre;
			var fechaInicioPadre = moment(response.fechaInicio, 'DD/MM/YYYY').toDate();
			mi.modificarFechaInicial(fechaInicioPadre);
			mi.congelado = response.congelado;
		});
	
	$http.post('/SAcumulacionCosto', { accion: 'getAcumulacionesCosto', t: (new Date()).getTime()}).success(
			function(response) {
				mi.acumulacionesCosto = response.acumulacionesTipos;
	});
	
	mi.blurCategoria=function(){
		if(document.getElementById("acumulacionCosto_value").defaultValue!=mi.subproducto.acumulacionCostoNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','acumulacionCosto');
		}
	}
	
	mi.cambioAcumulacionCosto=function(selected){
		if(selected!== undefined){
			mi.subproducto.acumulacionCostoNombre = selected.originalObject.nombre;
			mi.subproducto.acumulacionCosto = selected.originalObject.id;
		}
		else{
			mi.subproducto.acumulacionCostoNombre="";
			mi.subproducto.acumulacionCosto="";
		}
	}
	
	mi.modificarFechaInicial = function(fechaPadre){
		mi.fi_opciones.minDate = fechaPadre;
	}
		
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2030, 12, 31),
			minDate : new Date(1970, 1, 1),
			startingDay : 1
	};
	
	mi.fi_opciones = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
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
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion,
			t: (new Date()).getTime()
		};

		mi.mostrarCargando = true;
		$http.post('/SSubproducto', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.subproductos;
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
	    rowTemplate: '<div ng-dblclick="grid.appScope.subproducto.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [ 
			{displayName : 'Id',  width: 60, name : 'id',cellClass : 'grid-align-right',type : 'number',enableFiltering: false, enableSorting: false }, 
			{ displayName : 'Nombre',name : 'nombre',cellClass : 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width:90%;" ng-keypress="grid.appScope.subproducto.filtrar($event,1)" ></input></div>'
			}, 
			{ displayName : 'Descripción', name : 'descripcion', cellClass : 'grid-align-left' },
			{ displayName : 'Tipo', name : 'subProductoTipo', cellClass : 'grid-align-left', enableFiltering: false, enableSorting: false},  
			{ displayName : 'Producto', name : 'producto', cellClass : 'grid-align-left', visible : false },
			{ displayName : 'Subproducto', name : 'subproducto', cellClass : 'grid-align-left', visible : false },
			{ name: 'usuarioCreo', displayName: 'Usuario Creación',cellClass: 'grid-align-left',
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
	
	mi.actualizarCosto = function(costo){
		mi.subproducto.costo = costo;
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
		mi.camposdinamicos={};
		mi.esForma = true;
		mi.entityselected = null;
		mi.esNuevo = true;
		mi.subproducto = null;
		
		mi.tipo = null;
		mi.tipoNombre = "";
		
		mi.unidadEjecutora= mi.prestamoId != null ? mi.unidadEjecutora :  null;
		mi.unidadEjecutoraNombre= mi.prestamoId != null ? mi.unidadEjecutoraNombre : "";

		mi.propiedadesValor = [];
		mi.subproducto = {};
		mi.coordenadas = "";
		
		$utilidades.setFocus(document.getElementById("nombre"));
		
		mi.child_adquisiciones = null;
		mi.child_riesgos = null;
		
		mi.riesgos = undefined;
		mi.adquisicionesCargadas = false;
		
		mi.activeTab = 0;
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

	mi.validarRequiredCosto = function(costo){
		if(costo != null && costo > 0)
			return "* Tipo de acumulación del costo";
		else
			return "Tipo de acumulación del costo";
	}

	mi.borrar = function(ev) {
		if (mi.subproducto!=null && mi.subproducto.id!=null) {
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar "' + mi.subproducto.nombre + '"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					mi.botones =  false;
					var datos = {
							accion : 'borrar',
							codigo : mi.subproducto.id,
							t: (new Date()).getTime()
						};
						$http.post('/SSubproducto', datos).success(
								function(response) {
									if (response.success) {
										$utilidades.mensaje('success',
												'Subroducto borrado con éxito');
										mi.subproducto = null;
										mi.cargarTabla(1);
									} else
										$utilidades.mensaje('danger',
												'Error al borrar el Subproducto');
									mi.botones =  true;
								});
				}
			}, function(){
				
			});
		} else {
			$utilidades.mensaje('warning',
					'Debe seleccionar un Subproducto que desee borrar');
		}
	};

	mi.guardar = function() {
		mi.botones =  false;
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
				renglon: mi.subproducto.renglon,
				ubicacionGeografica: mi.subproducto.ubicacionGeografica,
				producto : ($routeParams.producto_id == undefined ? 0 : $routeParams.producto_id),
				subproductoPadre : mi.subproductoPadre,
				tiposubproductoid : mi.tipo,
				unidadEjecutora : mi.unidadEjecutora,
				ejercicio: mi.ejercicio,
				entidad: mi.entidad,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				longitud: mi.subproducto.longitud,
				costo: mi.subproducto.costo == null ? 0 : mi.subproducto.costo,
				acumulacionCostoId: mi.subproducto.acumulacionCosto == null ? 0 : mi.subproducto.acumulacionCosto,
				fechaInicio: moment(mi.subproducto.fechaInicio).format('DD/MM/YYYY'),
				fechaFin: moment(mi.subproducto.fechaFin).format('DD/MM/YYYY'),
				duaracion: mi.subproducto.duracion,
				duracionDimension: mi.duracionDimension.sigla,
				latitud : mi.subproducto.latitud,
				esnuevo : mi.esNuevo,
				t: (new Date()).getTime()
			};

			$http.post('/SSubproducto', datos).then(
					function(response) {
						if (response.data.success) {
							mi.data = response.data.subproductos;
							mi.opcionesGrid.data = mi.data;
							mi.subproducto.id = response.data.id;
							mi.subproducto.usuarioCreo = response.data.usuarioCreo;
							mi.subproducto.fechaCreacion = response.data.fechaCreacion;
							mi.subproducto.usuarioActualizo = response.data.usuarioactualizo;
							mi.subproducto.fechaActualizacion = response.data.fechaactualizacion;
							if(mi.child_adquisiciones!=null)
								mi.child_adquisiciones.guardar((mi.child_riesgos!=null) ? mi.child_riesgos.guardar : null,'Subproducto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
									'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Subproducto');
							else{
								if(mi.child_riesgos){
									mi.child_riesgos.guardar('Subproducto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
											'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Subproducto');
								}
								else
									$utilidades.mensaje('success','Subproducto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
							}
							if(!mi.esTreeview)
								mi.cargarTabla(mi.paginaActual);
							else{
								if(!mi.esNuevo)
									mi.t_cambiarNombreNodo();
								else
									mi.t_crearNodo(mi.subproducto.id,mi.subproducto.nombre,4,true);
							}
							mi.esNuevo = false;
							
							
						} else {
							$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Subproducto');
						}
						mi.botones =  true;
					});
		
	};

	mi.cancelar = function() {
		mi.esForma = false;
		mi.esNuevo=false;
		mi.child_adquisiciones = null;
		mi.child_riesgos = null;
	};
	
	mi.editar = function() {
		if (mi.subproducto!=null && mi.subproducto.id!=null) {
			mi.esForma = true;
			mi.entityselected = null;
			mi.esNuevo = false;
			mi.tipo = mi.subproducto.idSubproductoTipo; 
			mi.tipoNombre = mi.subproducto.subProductoTipo;
			
			mi.productoid = mi.subproducto.idProducto;
			
			if(mi.subproducto.duracionDimension == 'd'){
				mi.duracionDimension = mi.dimensiones[0];
			}

			mi.subproductoPadre = mi.subproducto.idSubproducto;
			mi.subproductoPadreNombre = mi.subproducto.subproducto;
			
			if(mi.fechaFinPadre != null && !isNaN(mi.fechaFinPadre)){
				mi.subproducto.fechaInicio = mi.sumarDias(mi.fechaFinPadre,2, 'd');
				mi.primerhijo = true;
			}
			$utilidades.setFocus(document.getElementById("nombre"));
			
			mi.coordenadas = (mi.subproducto.latitud !=null ?  mi.subproducto.latitud : '') +
			(mi.subproducto.latitud!=null ? ', ' : '') + (mi.subproducto.longitud!=null ? mi.subproducto.longitud : '');
			
			var parametros = {
					accion: 'getSubproductoPropiedadPorTipo', 
					idsubproducto: mi.subproducto.id,
					idsubproductotipo: mi.tipo,
					t: (new Date()).getTime()
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
				
				mi.adquisicionesCargadas = false;
				mi.riesgos = false;
				
				mi.activeTab = 0;
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
			mi.gridApi.selection.clearSelectedRows();
			mi.subproducto = null;
		}
	};

	mi.obtenerTotalSubproductos = function(){
		$http.post('/SSubproducto', { accion: 'totalElementos', productoid: $routeParams.producto_id,
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'], t: (new Date()).getTime()  }).then(
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

	mi.buscarAcumulacionCosto = function(){
		var resultado = mi.llamarModalBusqueda('Acumulacion Costo','/SAcumulacionCosto', {
			accion : 'numeroAcumulacionCosto' 
		}, function(pagina, elementosPorPagina){
			return{
				accion: 'getAcumulacionCosto',
				pagina: pagina,
				numeroacumulacioncosto : elementosPorPagina
			}
		}, 'id','nombre',false,null);
		
		resultado.then(function(itemSeleccionado){
			mi.subproducto.acumulacionCostoNombre = itemSeleccionado.nombre;
			mi.subproducto.acumulacionCosto = itemSeleccionado.id;
		});
	}
	
	mi.llamarModalBusqueda = function(titulo,servlet, accionServlet, datosCarga, columnaId,columnaNombre, 
			showfilters, entidad) {
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
		var resultado = mi.llamarModalBusqueda('Tipo de subproducto','/SSubproductoTipo', {
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
				accion: 'getSubproductoPropiedadPorTipo', 
				idsubproducto: mi.subproducto.id,
				idsubproductotipo: itemSeleccionado.id,
				t: (new Date()).getTime()
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
		mi.subproducto.fechaFin = mi.sumarDias(mi.subproducto.fechaInicio,mi.subproducto.duracion, dimension.sigla);
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

	mi.buscarSubproducto = function() {

		var resultado = mi.llamarModalBusqueda('Subproducto','/SSubproducto', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre',false,null);

		resultado.then(function(itemSeleccionado) {
			mi.subproductoPadre = itemSeleccionado.id;
			mi.subproductoPadreNombre = itemSeleccionado.nombre;
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
				entidad: entidad,
				ejercicio: ejercicio
			};
		},'unidadEjecutora','nombreUnidadEjecutora',
		true,{entidad: mi.entidad, ejercicio: mi.ejercicio, abreviatura:'', nombre: mi.entidadnombre});

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
	    	if (coordenadas !=null){
		    	mi.coordenadas = coordenadas.latitude + ", " + coordenadas.longitude;
		    	mi.subproducto.latitud= coordenadas.latitude;
				mi.subproducto.longitud = coordenadas.longitude;
	    	}
	    }, function() {
		});
	  };
	  
	  if(mi.esTreeview){
		  if($routeParams.nuevo==1){
			  mi.nuevo();
		  }
		  else{
			  $http.post('/SSubproducto', { accion : 'getSubproductoPorId', id: $routeParams.id, t: (new Date()).getTime() }).then(function(response) {
						if (response.data.success) {
							mi.subproducto = response.data.subproducto;
							if(mi.subproducto.fechaInicio != "")
								mi.subproducto.fechaInicio = moment(mi.subproducto.fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.subproducto.fechaFin != "")
								mi.subproducto.fechaFin = moment(mi.subproducto.fechaFin, 'DD/MM/YYYY').toDate();
							if(mi.subproducto.fechaInicioReal != "")
								mi.subproducto.fechaInicioReal = moment(mi.subproducto.fechaInicioReal, 'DD/MM/YYYY').toDate();
							if(mi.subproducto.fechaFinReal != "")
								mi.subproducto.fechaFinReal = moment(mi.subproducto.fechaFinReal, 'DD/MM/YYYY').toDate();
							mi.editar();
						}
					});
		  }
	  }
	  
	  mi.t_borrar = function(ev) {
			if (mi.subproducto!=null && mi.subproducto.id!=null) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el subproducto "' + mi.subproducto.nombre + '"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var datos = {
								accion : 'borrar',
								codigo : mi.subproducto.id,
								t: (new Date()).getTime()
							};
							$http.post('/SSubproducto', datos).success(
									function(response) {
										if (response.success) {
											
											$utilidades.mensaje('success','Subproducto borrado con éxito');
											mi.subproducto = null;		
											$rootScope.$emit("eliminarNodo", {});
										} else{
											$utilidades.mensaje('danger',
													'Error al borrar el Subproducto');
										}
									});
					}
				}, function(){
					
				});
			} else {
				$utilidades.mensaje('warning',
						'Debe seleccionar el subproducto que desee borrar');
			}
		};
		
		mi.t_cambiarNombreNodo = function(ev){
			$rootScope.$emit("cambiarNombreNodo",mi.subproducto.nombre);
		}
		
		mi.t_crearNodo=function(id,nombre,objeto_tipo,estado){
			$rootScope.$emit("crearNodo",{ id: id, nombre: nombre, objeto_tipo: objeto_tipo, estado: estado })
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

moduloSubproducto.controller('modalBuscarPorSubproducto', [ '$uibModalInstance',
		'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
		'$timeout', '$log',  '$titulo', '$servlet', '$accionServlet', '$datosCarga',
		'$columnaId','$columnaNombre','$showfilters','$entidad',
		modalBuscarPorSubproducto ]);

function modalBuscarPorSubproducto($uibModalInstance, $rootScope,$scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log, $titulo, $servlet, $accionServlet,
		$datosCarga,$columnaId,$columnaNombre , $showfilters,$entidad) {

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
	mi.entidad = '';
	mi.ejercicio = '';
	mi.titulo = $titulo;
	

	if(mi.showfilters){
		mi.entidad = $entidad;
		mi.ejercicio = $entidad.ejercicio;
		var current_year = moment().year();
		for(var i=current_year-$rootScope.catalogo_entidades_anos; i<=current_year; i++)
			mi.ejercicios.push(i);
		mi.ejercicio = (mi.ejercicio == null || mi.ejercicio == "") ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio, t: (new Date()).getTime()}).success(function(response) {
			mi.entidades = response.entidades;
			if(mi.entidades.length>0){
				mi.entidad = (mi.entidad.entidad == null || mi.entidad===undefined || mi.entidad.entidad =="") ? mi.entidades[0] : mi.entidad;
				$accionServlet.ejercicio = mi.ejercicio;
				$accionServlet.t= (new Date()).getTime();
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
		$accionServlet.t=(new Date()).getTime();
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

	mi.cargarTabla = function(pagina, ejercicio, entidad) {
		mi.mostrarCargando = true;
		$servlet.t=(new Date()).getTime();
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina, entidad,ejercicio)).then(
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
		mi.cargarTabla(mi.paginaActual, mi.ejercicio, mi.entidad.entidad);
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
				mi.cargarTabla(1,mi.ejercicio, mi.entidad.entidad);
			});
		}
		
	}

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