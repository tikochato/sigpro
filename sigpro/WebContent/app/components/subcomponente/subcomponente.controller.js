var app = angular.module('subcomponenteController', ['smart-table']);

app.controller('subcomponenteController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q', 'dialogoConfirmacion','historia','pagoplanificado', 
	function($scope,$rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion, $historia,$pagoplanificado) {
		var mi=this;
		
		mi.esTreeview = $rootScope.treeview;
		mi.botones = true;
		
		if(!mi.esTreeview)
			$window.document.title = $utilidades.sistema_nombre+' - Subcomponentes';
		
		mi.acumulacionesCosto = [];
		
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.subcomponentes = [];
		mi.subcomponente;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalSubComponentes = 0;
		mi.componenteNombre="";
		mi.objetoTipoNombre = ""
		mi.paginaActual = 1;
		mi.datotipoid = "";
		mi.datotiponombre = "";
		mi.subcomponentetipoid = "";
		mi.subcomponentenombre = "";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.componenteid = $routeParams.componente_id;
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
		mi.camposdinamicos = {};
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		mi.filtros = [];
		mi.orden = null;
		mi.coordenadas = "";
		mi.entidad='';
		mi.ejercicio = '';
		
		mi.riesgos=undefined;
		mi.active=0;
		mi.child_riesgos=null;
		mi.pagos = [];

		mi.dimensiones = [
			{value:1,nombre:'Dias',sigla:'d'}
		];
		
		mi.validarRequiredCosto = function(costo){
			if(costo != null && costo > 0)
				return "* Tipo de acumulación del costo";
			else
				return "Tipo de acumulación del costo";
		}
		
		mi.duracionDimension = mi.dimensiones[0];
		
		$http.post('/SComponente', { accion: 'obtenerComponentePorId', id: $routeParams.componente_id, t: (new Date()).getTime() }).success(
				function(response) {
					mi.componenteid = response.id;
					mi.componenteNombre = response.nombre;
					mi.objetoTipoNombre = "Componente";
					mi.prestamoId = response.prestamoId;
					mi.unidadejecutoraid = response.unidadEjecutora;
					mi.unidadejecutoranombre = response.unidadEjecutoraNombre;
					mi.entidad = response.entidad;
					mi.ejercicio = response.ejercicio;
					mi.entidadnombre = response.entidadNombre;
					mi.congelado = response.congelado;
		});
		
		$http.post('/SAcumulacionCosto', { accion: 'getAcumulacionesCosto', t: (new Date()).getTime()}).success(
				function(response) {
					mi.acumulacionesCosto = response.acumulacionesTipos;
		});
		
		mi.blurCategoria=function(){
			if(document.getElementById("acumulacionCosto_value").defaultValue!=mi.subcomponente.acumulacionCostoNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','acumulacionCosto');
			}
		}
		
		mi.cambioAcumulacionCosto=function(selected){
			if(selected!== undefined){
				mi.subcomponente.acumulacionCostoNombre = selected.originalObject.nombre;
				mi.subcomponente.acumulacionCostoId = selected.originalObject.id;
				
				if(mi.subcomponente.acumulacionCostoId == 2){
					mi.subcomponente.costo = null;
					mi.bloquearCosto = true;
				}else{
					mi.subcomponente.costo = null;
					mi.bloquearCosto = false;
				}
			}
			else{
				mi.subcomponente.acumulacionCostoNombre="";
				mi.subcomponente.acumulacionCostoId="";
			}
		}
		
		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2050, 12, 31),
				minDate : new Date(1990, 1, 1),
				startingDay : 1
		};
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		
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
			    useExternalFiltering: true,
			    useExternalSorting: true,
			    rowTemplate: '<div ng-dblclick="grid.appScope.subcomponentec.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentec.filtros[\'nombre\']"  ng-keypress="grid.appScope.subcomponentec.filtrar($event)" ></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentec.filtros[\'usuarioCreo\']"  ng-keypress="grid.appScope.subcomponentec.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.subcomponentec.filtros[\'fechaCreacion\']"  ng-keypress="grid.appScope.subcomponentec.filtrar($event)"  ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.subcomponente = row.entity;
					});

					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.subcomponentec.columnaOrdenada=sortColumns[0].field;
							grid.appScope.subcomponentec.ordenDireccion = sortColumns[0].sort.direction;
							grid.appScope.subcomponentec.cargarTabla(grid.appScope.subcomponentec.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.subcomponentec.columnaOrdenada!=null){
								grid.appScope.subcomponentec.columnaOrdenada=null;
								grid.appScope.subcomponentec.ordenDireccion=null;
							}
						}
					});

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalSubComponentes();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'subcomponentes', t: (new Date()).getTime()}).then(function(response){
						      if(response.data.success && response.data.estado!=''){
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
						    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      }
						      mi.obtenerTotalSubComponentes();
						  });
				    	  
				    }
				}
		};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SSubComponente', { accion: 'getSubComponentesPaginaPorComponente', pagina: pagina, numerosubcomponentes: $utilidades.elementosPorPagina,componenteid: $routeParams.componente_id,
				filtro_nombre: mi.filtros['nombre'], filtro_snip: mi.filtros['snip'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion,t: (new Date()).getTime()
			}).success(
					function(response) {
						mi.subcomponentes = response.subcomponentes;
						
						for(x in mi.subcomponentes){
							if(mi.subcomponentes[x].fechaInicio != "")
								mi.subcomponentes[x].fechaInicio = moment(mi.subcomponentes[x].fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.subcomponentes[x].fechaFin != "")
								mi.subcomponentes[x].fechaFin = moment(mi.subcomponentes[x].fechaFin, 'DD/MM/YYYY').toDate();
							mi.subcomponentes[x].inversionNueva = mi.subcomponentes[x].inversionNueva == 1;
						}

						mi.gridOptions.data = mi.subcomponentes;
						mi.mostrarcargando = false;
						mi.paginaActual = pagina;
					});
		}

		mi.guardar=function(esvalido){
			mi.botones = false;
			for (campos in mi.camposdinamicos) {
				if (mi.camposdinamicos[campos].tipo === 'fecha') {
					mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
				}
			}
			
				$http.post('/SSubComponente', {
					accion: 'guardarSubComponente',
					esnuevo: mi.esnuevo,
					subcomponentetipoid : mi.subcomponentetipoid,
					id: mi.subcomponente.id,
					componenteid: $routeParams.componente_id,
					nombre: mi.subcomponente.nombre,
					descripcion: mi.subcomponente.descripcion,
					snip: mi.subcomponente.snip,
					programa: mi.subcomponente.programa,
					subprograma: mi.subcomponente.subprograma,
					proyecto_: mi.subcomponente.proyecto_,
					actividad: mi.subcomponente.actividad,
					obra:mi.subcomponente.obra,
					renglon: mi.subcomponente.renglon,
					ubicacionGeografica: mi.subcomponente.ubicacionGeografica,
					esnuevo: mi.esnuevo,
					ejercicio: mi.ejercicio,
					entidad: mi.entidad,
					unidadejecutoraid: mi.unidadejecutoraid === "" ? null : mi.unidadejecutoraid, 
					longitud: mi.subcomponente.longitud,
					latitud : mi.subcomponente.latitud,
					costo: mi.subcomponente.costo == null ? null : mi.subcomponente.costo,
					acumulacionCosto: mi.subcomponente.acumulacionCostoId == null ? null : mi.subcomponente.acumulacionCostoId,
					fechaInicio: moment(mi.subcomponente.fechaInicio).format('DD/MM/YYYY'),
					fechaFin: moment(mi.subcomponente.fechaFin).format('DD/MM/YYYY'),
					duaracion: mi.subcomponente.duracion,
					duracionDimension: mi.duracionDimension.sigla,
					inversionNueva: mi.subcomponente.inversionNueva ? 1 : 0,
					pagosPlanificados: JSON.stringify(mi.pagos),
					datadinamica : JSON.stringify(mi.camposdinamicos),
				}).success(function(response){
					if(response.success){
						mi.subcomponente.id = response.id;
						mi.subcomponente.usuarioCreo=response.usuarioCreo;
						mi.subcomponente.fechaCreacion=response.fechaCreacion;
						mi.subcomponente.usuarioActualizo=response.usuarioactualizo;
						mi.subcomponente.fechaActualizacion=response.fechaactualizacion;
						if(!mi.esTreeview)
							mi.obtenerTotalSubComponentes();
						else{
							if(!mi.esnuevo)
								mi.t_cambiarNombreNodo();
							else
								mi.t_crearNodo(mi.subcomponente.id,mi.subcomponente.nombre,2,true);
						}
						if(mi.child_riesgos!=null){
							ret = mi.child_riesgos.guardar('Subcomponente '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
									'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Subcomponente');
						}
						else
							$utilidades.mensaje('success','Subcomponente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						
						mi.getAsignado();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Subcomponente');
					mi.botones = true;
				});
			
		};

		mi.borrar = function(ev) {
			if(mi.subcomponente!=null && mi.subcomponente.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Subcomponente "'+mi.subcomponente.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					mi.botones = false;
					if(data){
						$http.post('/SSubComponente', {
							accion: 'borrarSubComponente',
							id: mi.subcomponente.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Subcomponente borrado con éxito');
								mi.subcomponente = null;
								mi.obtenerTotalSubComponentes();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Subcomponente');
							mi.botones = true;
						});
					}
				}, function(){
					
				});	
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Subcomponente que desea borrar');
		};

		mi.nuevo = function() {
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.unidadejecutoraid= mi.prestamoId != null ? mi.unidadejecutoraid :  "";
			mi.unidadejecutoranombre= mi.prestamoId != null ? mi.unidadejecutoranombre : "";
			mi.subcomponentetipoid="";
			mi.subcomponentetiponombre="";
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.subcomponente = {};
			mi.camposdinamicos = {};
			mi.duracionDimension = '';
			mi.coordenadas = "";
			mi.duracionDimension = mi.dimensiones[0];
			if(!mi.esTreeview)
				mi.gridApi.selection.clearSelectedRows();
			$utilidades.setFocus(document.getElementById("nombre"));
			mi.active=0;
			mi.riesgos=undefined;
		};

		mi.editar = function() {
			if(mi.subcomponente!=null){
				mi.subcomponentetipoid=mi.subcomponente.subcomponentetipoid;
				mi.subcomponentetiponombre=mi.subcomponente.subcomponentetiponombre;
				
				if(mi.subcomponente.duracionDimension == 'd'){
					mi.duracionDimension = mi.dimensiones[0];
				}else{
					mi.duracionDimension = mi.dimensiones[0];
				}
				
				if(mi.subcomponente.acumulacionCostoId==2){
					mi.bloquearCosto = true;
				}else{
					mi.bloquearCosto = false;
				}
				
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.coordenadas = (mi.subcomponente.latitud !=null ?  mi.subcomponente.latitud : '') +
				(mi.subcomponente.latitud!=null ? ', ' : '') + (mi.subcomponente.longitud!=null ? mi.subcomponente.longitud : '');


				var parametros = {
						accion: 'getSubComponentePropiedadPorTipo',
						idSubComponente: mi.subcomponente!=null ? mi.subcomponente.id : 0,
						idSubComponenteTipo: mi.subcomponentetipoid
						,t: (new Date()).getTime()
				}

				$http.post('/SSubComponentePropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.subcomponentepropiedades;
					mi.riesgos=undefined;
					mi.active=0;
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
					mi.getAsignado();
					$utilidades.setFocus(document.getElementById("nombre"));
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Subcomponente que desea editar');
		}

		mi.irATabla = function() {
			mi.child_riesgos = null;
			mi.mostraringreso=false;
			mi.esnuevo=false;
			mi.sobrepaso = false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'subcomponentes', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()==('/subcomponente/'+ mi.componenteid + '/rv'))
				$route.reload();
			else
				$location.path('/subcomponente/'+ mi.componenteid + '/rv');
		}

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
			mi.subcomponente.fechaFin = mi.sumarDias(mi.subcomponente.fechaInicio,mi.subcomponente.duracion, dimension.sigla);
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
		
		mi.irAProductos=function(subcomponenteid){
			if(mi.subcomponente!=null){
				$location.path('/producto/'+subcomponenteid+'/2');
			}
		};
		
		mi.irAActividades=function(subcomponenteid){
			if(mi.subcomponente!=null){
				$location.path('/actividad/'+ subcomponenteid +'/2' );
			}
		};
		
		mi.irARiesgos=function(subcomponenteid){
			if(mi.subcomponente!=null){
				$location.path('/riesgo/'+ subcomponenteid +'/2' );
			}
		};
		

		mi.filtrar = function(evt,tipo){
			if(evt.keyCode==13){
				mi.obtenerTotalSubComponentes();
				mi.gridApi.selection.clearSelectedRows();
				mi.subcomponente = null;
			}
		}

		mi.obtenerTotalSubComponentes = function(){
			$http.post('/SSubComponente', { accion: 'numeroSubComponentesPorComponente', componenteid: $routeParams.componente_id,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],t: (new Date()).getTime()  }).then(
					function(response) {
						mi.totalSubComponentes = response.data.totalsubcomponentes;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}
		
		mi.buscarAcumulacionCosto = function(){
			var resultado = mi.llamarModalBusquedaAcumulacion('Tipo de acumulación del costo','/SAcumulacionCosto', {
				accion : 'numeroAcumulacionCosto' 
			}, function(pagina, elementosPorPagina){
				return{
					accion: 'getAcumulacionCosto',
					pagina: pagina,
					numeroacumulacioncosto : elementosPorPagina
				}
			}, 'id','nombre',false,null);
			
			resultado.then(function(itemSeleccionado){
				mi.subcomponente.acumulacionCostoNombre = itemSeleccionado.nombre;
				mi.subcomponente.acumulacionCostoId = itemSeleccionado.id;
			});
		}
		
		mi.llamarModalBusquedaAcumulacion = function(titulo,servlet, accionServlet, datosCarga,columnaId,
				columnaNombre, showfilters,entidad) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarAcumulacionCosto.jsp',
				controller : 'buscarPorSubComponente',
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

		mi.llamarModalBusqueda = function(titulo,servlet, accionServlet, datosCarga,columnaId,
				columnaNombre, showfilters,entidad) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarPorSubComponente.jsp',
				controller : 'buscarPorSubComponente',
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

		mi.verHistoria = function(){
			$historia.getHistoria($scope, 'Sub Componente', '/SSubComponente',mi.subcomponente.id, 2, false, true, false, false)
			.result.then(function(data) {
				if (data != ""){
					
				}
			}, function(){
				
			});
		}
		
		mi.buscarSubComponenteTipo = function() {
			var resultado = mi.llamarModalBusqueda('Tipos de Subcomponente','/SSubComponenteTipo', {
				accion : 'numeroSubComponenteTipos'
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getSubComponentetiposPagina',
					pagina : pagina,
					numerosubcomponentetipos : elementosPorPagina
				};
			},'id','nombre',false, null);

			resultado.then(function(itemSeleccionado) {
				mi.subcomponentetipoid = itemSeleccionado.id;
				mi.subcomponentetiponombre = itemSeleccionado.nombre;
				var parametros = {
						accion: 'getSubComponentePropiedadPorTipo',
						idSubComponente: mi.subcomponente!=null ? mi.subcomponente.id : 0,
						idSubComponenteTipo: mi.subcomponentetipoid
						,t: (new Date()).getTime()
				}

				$http.post('/SSubComponentePropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.subcomponentepropiedades
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
			},'unidadEjecutora','nombreUnidadEjecutora',true,{entidad: mi.entidad, ejercicio: mi.ejercicio, abreviatura:'', nombre: mi.entidadnombre});

			resultado.then(function(itemSeleccionado) {
				mi.unidadejecutoraid = itemSeleccionado.unidadEjecutora;
				mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;
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
			    	mi.subcomponente.latitud= coordenadas.latitude;
					mi.subcomponente.longitud = coordenadas.longitude;
		    	}else{
		    		mi.coordenadas = null;
		    		mi.subcomponente.latitud = null;
		    		mi.subcomponente.longitud = null;
		    	}
		    }, function() {
			});
		  };
		  
		  if(mi.esTreeview){
			  if($routeParams.nuevo==1){
				  mi.nuevo();
			  }
			  else{
				  $http.post('/SSubComponente', { accion : 'getSubComponentePorId', id: $routeParams.id, t: (new Date()).getTime() }).then(function(response) {
						if (response.data.success) {
							mi.subcomponente = response.data.subcomponente;
							if(mi.subcomponente.fechaInicio != "")
								mi.subcomponente.fechaInicio = moment(mi.subcomponente.fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.subcomponente.fechaFin != "")
								mi.subcomponente.fechaFin = moment(mi.subcomponente.fechaFin, 'DD/MM/YYYY').toDate();
							if(mi.subcomponente.fechaInicioReal != "")
								mi.subcomponente.fechaInicioReal = moment(mi.subcomponente.fechaInicioReal, 'DD/MM/YYYY').toDate();
							if(mi.subcomponente.fechaFinReal != "")
								mi.subcomponente.fechaFinReal = moment(mi.subcomponente.fechaFinReal, 'DD/MM/YYYY').toDate();
							mi.subcomponente.inversionNueva = mi.subcomponente.inversionNueva == 1;
							mi.editar();
						}
					});
			  }
		  }
		  
		  mi.t_borrar = function(ev) {
				if (mi.subcomponente!=null && mi.subcomponente.id!=null) {
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar el Subcomponente "' + mi.subcomponente.nombre + '"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							var datos = {
									accion : 'borrarSubComponente',
									id : mi.subcomponente.id,
									t: (new Date()).getTime()
								};
								$http.post('/SSubComponente', datos).success(
										function(response) {
											if (response.success) {
												
												$utilidades.mensaje('success','Subcomponente borrado con éxito');
												mi.subcomponente = null;		
												$rootScope.$emit("eliminarNodo", {});
											} else{
												$utilidades.mensaje('danger',
														'Error al borrar el Subcomponente');
											}
										});
						}
					}, function(){
						
					});
				} else {
					$utilidades.mensaje('warning',
							'Debe seleccionar el Subcomponente que desee borrar');
				}
			};
			
			mi.t_cambiarNombreNodo = function(ev){
				$rootScope.$emit("cambiarNombreNodo",mi.subcomponente.nombre);
			}
			
			mi.t_crearNodo=function(id,nombre,objeto_tipo,estado){
				$rootScope.$emit("crearNodo",{ id: id, nombre: nombre, objeto_tipo: objeto_tipo, estado: estado })
			};
			
			mi.agregarPagos = function() {
				$pagoplanificado.getPagoPlanificado($scope, mi.subcomponente.id,2, 
				function(objetoId, objetoTipo){
					return{
						accion: 'getPagos',
						objetoId: objetoId,
						objetoTipo : objetoTipo
					}
				}, mi.subcomponente.fechaInicio,mi.subcomponente.fechaFin)
				.result.then(function(data) {
					mi.pagos=data;
					mi.subcomponente.costo = 0;
					for (x in mi.pagos){
						mi.subcomponente.costo += mi.pagos[x].pago;
					}
				}, function(){
				});
			};
			
			mi.getAsignado = function(){
				if(mi.subcomponente.programa != null){
					$http.post('/SSubComponente', {
						accion: 'getValidacionAsignado',
						id: mi.subcomponente.id,
						programa: mi.subcomponente.programa,
						subprograma: mi.subcomponente.subprograma,
						proyecto: mi.subcomponente.proyecto,
						actividad: mi.subcomponente.actividad,
						obra: mi.subcomponente.obra,
						renglon: mi.subcomponente.renglon,
						geografico: mi.subcomponente.ubicacionGeografica,
						t: new Date().getTime()
					}).success(function(response){
						if(response.success){
							mi.asignado = response.asignado;
							mi.sobrepaso = response.sobrepaso;
						}
					});
				}
			}
			
			mi.validarAsignado = function(){
				if(mi.subcomponente.costo != null){
					if(mi.subcomponente.programa != null){
						if(mi.subcomponente.costo <= mi.asignado)
							mi.sobrepaso = false;
						else
							mi.sobrepaso = true;	
					}
				}
			}
} ]);

app.controller('buscarPorSubComponente', [ '$uibModalInstance',
	'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$titulo','$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre','$showfilters','$entidad',buscarPorSubComponente ]);

function buscarPorSubComponente($uibModalInstance, $rootScope, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $titulo,$servlet,$accionServlet,$datosCarga
	,$columnaId,$columnaNombre,$showfilters,$entidad) {

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
		mi.ejercicio = mi.ejercicio == null || mi.ejercicio=="" ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio}).success(function(response) {
			mi.entidades = response.entidades;
			if(mi.entidades.length>0){
				mi.entidad = (mi.entidad.entidad == null || mi.entidad===undefined || mi.entidad.entidad =="") ? mi.entidades[0] : mi.entidad;
				
				$accionServlet.ejercicio = mi.ejercicio;
				$accionServlet.entidad = mi.entidad.entidad;
				$http.post($servlet, $accionServlet).success(function(response) {
					for ( var key in response) {
						mi.totalElementos = response[key];
					}
					mi.cargarData(1,mi.ejercicio,mi.entidad.entidad);
				});
			}
			
		});
	}
	else{
		$http.post($servlet, $accionServlet).success(function(response) {
			for ( var key in response) {
				mi.totalElementos = response[key];
			}
			mi.cargarData(1,0, 0);
		});
	}

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

	mi.cargarData = function(pagina, ejercicio, entidad) {
		mi.mostrarCargando = true;
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
		mi.cargarData(mi.paginaActual, mi.ejercicio, mi.entidad.entidad);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};
	
	mi.borrar = function(){
		$uibModalInstance.close("");	
	}

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cambioEjercicio= function(){
		mi.cargarData(1,mi.ejercicio, mi.entidad.entidad);
	}
	
	mi.cambioEntidad= function(selected){
		if(selected!==undefined){
			mi.entidad = selected.originalObject;
			$http.post('/SUnidadEjecutora', {accion:"totalElementos", ejercicio: mi.entidad.ejercicio,entidad: mi.entidad.entidad}).success(function(response) {
				for ( var key in response) {
					mi.totalElementos = response[key];
				}
				mi.cargarData(1,mi.ejercicio, mi.entidad.entidad);
			});
		}
	}
};

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
	  
	  $scope.borrar = function () {
		  $uibModalInstance.close(null);
	  };
}]);

