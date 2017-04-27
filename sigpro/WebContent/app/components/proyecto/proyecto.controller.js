var app = angular.module('proyectoController', [ 'ngTouch','smart-table' ]);

app.controller('proyectoController',['$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,$filter) {

	var mi = this;
	i18nService.setCurrentLang('es');

	$window.document.title = $utilidades.sistema_nombre+' - Proyectos';

	mi.rowCollection = [];
	mi.proyecto = null;
	mi.esNuevo = false;
	mi.esNuevoDocumento = true;
	mi.campos = {};
	mi.esColapsado = false;
	mi.mostrarcargando=true;
	mi.paginaActual = 1;
	mi.cooperantes = [];
	mi.proyectotipos = [];
	mi.unidadesejecutoras = [];
	mi.poryectotipoid = "";
	mi.proyectotiponombre="";
	mi.unidadejecutoraid="";
	mi.unidadejecutoranombre="";
	mi.cooperanteid="";
	mi.cooperantenombre="";
	mi.camposdinamicos = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.totalProyectos = 0;

	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;

	mi.coordenadas = "";

	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
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
		columnDefs : [
			{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre',  displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
			},
			{ name : 'proyectotipo',    displayName : 'Tipo proyecto' ,cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false },
			{ name : 'unidadejecutora',    displayName : 'Unidad Ejecutora' ,cellClass: 'grid-align-left', enableFiltering: false , enableSorting: false },
			{ name : 'cooperante',   displayName : 'Cooperante' ,cellClass: 'grid-align-left',  enableFiltering: false , enableSorting: false },
			{ name: 'usuarioCreo', width: 120, displayName: 'Usuario Creación',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.controller.filtrar($event)" style="width:90px;"></input></div>'
			},
		    { name: 'fechaCreacion', width: 100, displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:80px;" ></input></div>'
		    }
		],
		onRegisterApi: function(gridApi) {
			mi.gridApi = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.proyecto = row.entity;
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
				mi.obtenerTotalProyectos();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyceto', t: (new Date()).getTime()}).then(function(response){
		    		  if(response.data.success && response.data.estado!='')
		    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
			    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
				      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
				      mi.obtenerTotalProyectos();
				  });
		    }
		}
	};

	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';
	}
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SProyecto', { accion: 'getProyectoPagina', pagina: pagina,
			numeroproyecto:  $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t:moment().unix()
			}).success(
				function(response) {
					mi.entidades = response.proyectos;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
	}


	mi.guardar = function(esvalido){
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
			}
		}
		if(mi.proyecto!=null && mi.proyecto.nombre!=null){
			var param_data = {
				accion : 'guardar',
				id: mi.proyecto.id,
				nombre: mi.proyecto.nombre,
				snip: mi.proyecto.snip,
				descripcion:mi.proyecto.descripcion,
				proyectotipoid: mi.poryectotipoid,
				unidadejecutoraid: mi.unidadejecutoraid,
				cooperanteid: mi.cooperanteid,
				programa: mi.proyecto.programa,
				subprograma: mi.proyecto.subprograma,
				proyecto_: mi.proyecto.proyecto,
				obra:mi.proyecto.obra,
				actividad: mi.proyecto.actividad,
				fuente: mi.proyecto.fuente,
				esnuevo: mi.esNuevo,
				longitud: mi.proyecto.longitud,
				latitud : mi.proyecto.latitud,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				t:moment().unix()
			};
			$http.post('/SProyecto',param_data).then(
				function(response) {
					if (response.data.success) {
						mi.proyecto.id = response.data.id;
						$utilidades.mensaje('success','Proyecto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
						mi.obtenerTotalProyectos();
						mi.esNuevo = false;
					}else
						$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Proyecto');
			});
			
			mi.esNuevoDocumento = false;
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	 }

	mi.borrar = function(ev) {
		if(mi.proyecto !=null && mi.proyecto.id!=null){

			var confirm = $mdDialog.confirm()
	        .title('Confirmación de borrado')
	        .textContent('¿Desea borrar el Proyecto "'+mi.proyecto.nombre+'"?')
	        .ariaLabel('Confirmación de borrado')
	        .targetEvent(ev)
	        .ok('Borrar')
	        .cancel('Cancelar');

			$mdDialog.show(confirm).then(function() {
				$http.post('/SProyecto', {
					accion: 'borrarProyecto',
					id: mi.proyecto.id,
					t:moment().unix()
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Proyecto borrado con éxito');
						mi.proyecto = null;
						mi.obtenerTotalProyectos();
					}
					else
						$utilidades.mensaje('danger','Error al borrar el Proyecto');
				});
			}, function() {

		    });
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Proyecto que desea borrar');
	};

	mi.nuevo = function (){
		mi.esNuevoDocumento = true;
		mi.poryectotipoid = "";
		mi.proyectotiponombre="";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.cooperanteid="";
		mi.cooperantenombre="";
		mi.esColapsado = true;
		mi.proyecto = {};
		mi.esNuevo = true;
		mi.camposdinamicos = {};
		mi.coordenadas = "";
		mi.gridApi.selection.clearSelectedRows();
	};

	mi.editar = function() {
		if(mi.proyecto!=null && mi.proyecto.id!=null){
			mi.esNuevoDocumento = false;
			mi.poryectotipoid = mi.proyecto.proyectotipoid;
			mi.proyectotiponombre=mi.proyecto.proyectotipo;
			mi.unidadejecutoraid=mi.proyecto.unidadejecutoraid;
			mi.unidadejecutoranombre=mi.proyecto.unidadejecutora;
			mi.cooperanteid=mi.proyecto.cooperanteid;
			mi.cooperantenombre=mi.proyecto.cooperante;
			mi.esColapsado = true;
			mi.esNuevo = false;
			mi.coordenadas = (mi.proyecto.latitud !=null ?  mi.proyecto.latitud : '') +
			(mi.proyecto.latitud!=null ? ', ' : '') + (mi.proyecto.longitud!=null ? mi.proyecto.longitud : '');
			
			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.proyecto.id:0,
				    idProyectoTipo: mi.poryectotipoid,
				    t:moment().unix()
			}
			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades
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
			
			mi.getDocumentosAdjuntos(1, mi.proyecto.id);
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Proyecto que desea editar');
	}
	
	mi.adjuntarDocumentos = function(){
		$documentoAdjunto.getModalDocumento($scope, 1, mi.proyecto.id)
		.result.then(function(data) {
			mi.getDocumentosAdjuntos(1, mi.proyecto.id);
		}, function(){
			
		});
	}

	mi.getDocumentosAdjuntos = function(objetoId, tipoObjetoId){
		mi.rowCollection = [];
		var formatData = new FormData();
		formatData.append("accion","getDocumentos");
		formatData.append("idObjeto", objetoId);
		formatData.append("idTipoObjeto", tipoObjetoId);
		$http.post('/SDocumentosAdjuntos', formatData, {
			headers: {'Content-Type': undefined},
			transformRequest: angular.identity,
		}).then(function(response) {
			if (response.data.success) {
				 mi.rowCollection = response.data.documentos;
		         mi.displayedCollection = [].concat(mi.rowCollection);
			}
		});
	}
	
	mi.descargarDocumento= function(row){
		var url = "/SDocumentosAdjuntos?accion=getDescarga&id="+row.id;
		window.location.href = url;
	}
	
	mi.eliminarDocumento= function(row){
		$http.post('/SDocumentosAdjuntos?accion=eliminarDocumento&id='+row.id)
		.then(function successCAllback(response){
			if (response.data.success){
				var indice = mi.rowCollection.indexOf(row);
				if (indice !== -1) {
			       mi.rowCollection.splice(indice, 1);		       
			    }
				mi.rowCollection = [];
				mi.getDocumentosAdjuntos(1, mi.proyecto.id);
			}
		});
	};

	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}

	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'proyceto', estado: JSON.stringify(estado) };
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

	mi.abrirPopupFecha = function(index) {
		mi.camposdinamicos[index].isOpen = true;
	};

	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalProyectos();
		}
	};

	mi.obtenerTotalProyectos = function(){
		$http.post('/SProyecto', { accion: 'numeroProyectos',t:moment().unix(),
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  } ).then(
				function(response) {
					mi.totalProyectos = response.data.totalproyectos;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};

	mi.irADesembolsos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/desembolso/'+proyectoid);
		}

	};

	mi.irAComponentes=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/componente/'+ proyectoid );
		}
	};

	mi.irARiesgos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/riesgo/' );
		}
	};

	mi.irAHitos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/hito/'+ proyectoid );
		}
	};
	mi.irAActividades=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/actividad/'+ proyectoid +'/1' );
		}
	};

	mi.irAGantt=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/gantt/'+ proyectoid );
		}
	};

	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
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

	mi.llamarModalBusquedaUnidadEjec = function(servlet, accionServlet, datosCarga) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyectoUnidadEjec',
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

	mi.buscarProyectoTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SProyectoTipo', {
			accion : 'numeroProyectoTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getProyectoTipoPagina',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.poryectotipoid= itemSeleccionado.id;
			mi.proyectotiponombre = itemSeleccionado.nombre;

			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.poryectotipoid.id:0,
					idProyectoTipo: itemSeleccionado.id,
					t:moment().unix()
			}

			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades;
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
			mi.unidadejecutoraid= itemSeleccionado.unidadEjecutora;
			mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;

		});
	};

	mi.buscarCooperante = function() {
		var resultado = mi.llamarModalBusqueda('/SCooperante', {
			accion : 'numeroCooperantes', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getCooperantesPagina',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.cooperanteid= itemSeleccionado.id;
			mi.cooperantenombre = itemSeleccionado.nombre;

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
		    	mi.proyecto.latitud= coordenadas.latitude;
				mi.proyecto.longitud = coordenadas.longitude;
	    	}
	    }, function() {
		});
	  };
} ]);

app.controller('buscarPorProyecto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre',buscarPorProyecto ]);

function buscarPorProyecto($uibModalInstance, $scope, $http, $interval,
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

app.controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
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