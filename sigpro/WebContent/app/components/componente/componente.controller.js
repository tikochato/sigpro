var app = angular.module('componenteController', ['smart-table']);

app.controller('componenteController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q', 'dialogoConfirmacion','historia','pagoplanificado', 
	function($scope,$rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion, $historia, $pagoplanificado) {
		var mi=this;
		
		mi.esTreeview = $rootScope.treeview;
		mi.botones = true;
		
		if(!mi.esTreeview)
			$window.document.title = $utilidades.sistema_nombre+' - Componentes';
		
		mi.acumulacionesCosto = [];
		
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.componentes = [];
		mi.componente;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalComponentes = 0;
		mi.proyectoid = $routeParams.proyecto_id;
		mi.proyectoNombre="";
		mi.objetoTipoNombre = ""
		mi.paginaActual = 1;
		mi.datotipoid = "";
		mi.datotiponombre = "";
		mi.componentetipoid = "";
		mi.componentenombre = "";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
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
		mi.esDeSigade = false;
		
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
		
		$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyecto_id, t: (new Date()).getTime() }).success(
				function(response) {
					mi.proyectoid = response.id;
					mi.proyectoNombre = response.nombre;
					mi.objetoTipoNombre = "Proyecto";
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
			if(document.getElementById("acumulacionCosto_value").defaultValue!=mi.componente.acumulacionCostoNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','acumulacionCosto');
			}
		}
		
		mi.cambioAcumulacionCosto=function(selected){
			if(selected!== undefined){
				mi.componente.acumulacionCostoNombre = selected.originalObject.nombre;
				mi.componente.acumulacionCostoId = selected.originalObject.id;
				
				if(mi.componente.acumulacionCostoId == 2){
					mi.componente.costo = null;
					mi.bloquearCosto = true;
				}else{
					mi.componente.costo = null;
					mi.bloquearCosto = false;
				}
			}
			else{
				mi.componente.acumulacionCostoNombre="";
				mi.componente.acumulacionCostoId="";
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.componentec.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.componentec.filtros[\'nombre\']"  ng-keypress="grid.appScope.componentec.filtrar($event)" ></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',cellClass: 'grid-align-left',
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
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion,t: (new Date()).getTime()
			}).success(
					function(response) {
						mi.componentes = response.componentes;
						mi.esDeSigade = response.esDeSigade;
						for(x in mi.componentes){
							if(mi.componentes[x].fechaInicio != "")
								mi.componentes[x].fechaInicio = moment(mi.componentes[x].fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.componentes[x].fechaFin != "")
								mi.componentes[x].fechaFin = moment(mi.componentes[x].fechaFin, 'DD/MM/YYYY').toDate();
							mi.componentes[x].inversionNueva = mi.componentes[x].inversionNueva == 1;
						}

						mi.gridOptions.data = mi.componentes;
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
					renglon: mi.componente.renglon,
					ubicacionGeografica: mi.componente.ubicacionGeografica,
					esnuevo: mi.esnuevo,
					ejercicio: mi.ejercicio,
					entidad: mi.entidad,
					unidadejecutoraid:mi.unidadejecutoraid === "" ? null : mi.unidadejecutoraid,
					longitud: mi.componente.longitud,
					latitud : mi.componente.latitud,
					costo: mi.componente.costo == null ? null : mi.componente.costo,
					costoTecho: mi.componente.costoTecho != null ?  mi.componente.costoTecho : null,
					acumulacionCosto: mi.componente.acumulacionCostoId == null ? null : mi.componente.acumulacionCostoId,
					fechaInicio: moment(mi.componente.fechaInicio).format('DD/MM/YYYY'),
					fechaFin: moment(mi.componente.fechaFin).format('DD/MM/YYYY'),
					duaracion: mi.componente.duracion,
					duracionDimension: mi.duracionDimension.sigla,
					esDeSigade: 0,
					inversionNueva: mi.componente.inversionNueva ? 1 : 0,
					pagosPlanificados: JSON.stringify(mi.pagos),
					datadinamica : JSON.stringify(mi.camposdinamicos),
				}).success(function(response){
					if(response.success){
						mi.componente.id = response.id;
						mi.componente.usuarioCreo=response.usuarioCreo;
						mi.componente.fechaCreacion=response.fechaCreacion;
						mi.componente.usuarioActualizo=response.usuarioactualizo;
						mi.componente.fechaActualizacion=response.fechaactualizacion;
						if(!mi.esTreeview)
							mi.obtenerTotalComponentes();
						else{
							if(!mi.esnuevo)
								mi.t_cambiarNombreNodo();
							else
								mi.t_crearNodo(mi.componente.id,mi.componente.nombre,1,true);
						}
						if(mi.child_riesgos!=null){
							ret = mi.child_riesgos.guardar('Componente '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
									'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Componente');
						}
						else
							$utilidades.mensaje('success','Componente '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						
						mi.getAsignado();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Componente');
					mi.botones = true;
				});
			
		};

		mi.borrar = function(ev) {
			if(mi.componente!=null && mi.componente.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Componente "'+mi.componente.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					mi.botones = false;
					if(data){
						$http.post('/SComponente', {
							accion: 'borrarComponente',
							id: mi.componente.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Cooperante borrado con éxito');
								mi.componente = null;
								mi.obtenerTotalComponentes();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Cooperante');
							mi.botones = true;
						});
					}
				}, function(){
					
				});	
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Componente que desea borrar');
		};

		mi.nuevo = function() {
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.unidadejecutoraid= mi.prestamoId != null ? mi.unidadejecutoraid :  "";
			mi.unidadejecutoranombre= mi.prestamoId != null ? mi.unidadejecutoranombre : "";
			mi.componentetipoid="";
			mi.componentetiponombre="";
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.componente = {};
			mi.camposdinamicos = {};
			mi.duracionDimension = '';
			mi.coordenadas = "";
			mi.duracionDimension = mi.dimensiones[0];
			if(!mi.esTreeview)
				mi.gridApi.selection.clearSelectedRows();
			$utilidades.setFocus(document.getElementById("nombre"));
			mi.active=0;
		};

		mi.editar = function() {
			if(mi.componente!=null){
				mi.unidadejecutoraid= mi.componente.unidadejecutoraid;
				mi.unidadejecutoranombre= mi.componente.unidadejecutoranombre;
				mi.componentetipoid=mi.componente.componentetipoid;
				mi.componentetiponombre=mi.componente.componentetiponombre;
				mi.ejercicio = mi.componente.ejercicio;
				mi.entidad = mi.componente.entidadentidad;
				mi.entidadnombre = mi.componente.entidadnombre;
				
				if(mi.componente.duracionDimension == 'd'){
					mi.duracionDimension = mi.dimensiones[0];
				}else{
					mi.duracionDimension = mi.dimensiones[0];
				}
				
				if(mi.componente.acumulacionCostoId==2){
					mi.bloquearCosto = true;
				}else{
					mi.bloquearCosto = false;
				}
				
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.coordenadas = (mi.componente.latitud !=null ?  mi.componente.latitud : '') +
				(mi.componente.latitud!=null ? ', ' : '') + (mi.componente.longitud!=null ? mi.componente.longitud : '');


				var parametros = {
						accion: 'getComponentePropiedadPorTipo',
						idComponente: mi.componente!=null ? mi.componente.id : 0,
						idComponenteTipo: mi.componentetipoid
						,t: (new Date()).getTime()
				}

				$http.post('/SComponentePropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.componentepropiedades;
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
					$utilidades.setFocus(document.getElementById("nombre"));
					mi.getAsignado();
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
			mi.componente.fechaFin = mi.sumarDias(mi.componente.fechaInicio,mi.componente.duracion, dimension.sigla);
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
		
		mi.irASubComponente=function(componenteid){
			if(mi.componente!=null){
				$location.path('/subcomponente/'+componenteid);
			}
		};
		
		mi.irAProductos=function(componenteid){
			if(mi.componente!=null){
				$location.path('/producto/'+componenteid+'/1');
			}
		};
		
		mi.irAActividades=function(componenteid){
			if(mi.componente!=null){
				$location.path('/actividad/'+ componenteid +'/1' );
			}
		};
		
		mi.irARiesgos=function(componenteid){
			if(mi.componente!=null){
				$location.path('/riesgo/'+ componenteid +'/1' );
			}
		};
		

		mi.filtrar = function(evt,tipo){
			if(evt.keyCode==13){
				mi.obtenerTotalComponentes();
				mi.gridApi.selection.clearSelectedRows();
				mi.componente = null;
			}
		}

		mi.obtenerTotalComponentes = function(){
			$http.post('/SComponente', { accion: 'numeroComponentesPorProyecto', proyectoid: $routeParams.proyecto_id,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'],t: (new Date()).getTime()  }).then(
					function(response) {
						mi.totalComponentes = response.data.totalcomponentes;
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
				mi.componente.acumulacionCostoNombre = itemSeleccionado.nombre;
				mi.componente.acumulacionCostoId = itemSeleccionado.id;
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
				controller : 'buscarPorComponente',
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
				templateUrl : 'buscarPorComponente.jsp',
				controller : 'buscarPorComponente',
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


		mi.buscarComponenteTipo = function() {
			var resultado = mi.llamarModalBusqueda('Tipos de Componente','/SComponenteTipo', {
				accion : 'numeroComponenteTipos'
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getComponentetiposPagina',
					pagina : pagina,
					numerocomponentetipos : elementosPorPagina
				};
			},'id','nombre',false, null);

			resultado.then(function(itemSeleccionado) {
				mi.componentetipoid = itemSeleccionado.id;
				mi.componentetiponombre = itemSeleccionado.nombre;
				var parametros = {
						accion: 'getComponentePropiedadPorTipo',
						idComponente: mi.componente!=null ? mi.componente.id : 0,
						idComponenteTipo: mi.componentetipoid
						,t: (new Date()).getTime()
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
			    	mi.componente.latitud= coordenadas.latitude;
					mi.componente.longitud = coordenadas.longitude;
		    	}else{
		    		mi.coordenadas = null;
		    		mi.componente.latitud = null;
		    		mi.componente.longitud = null;
		    	}
		    }, function() {
			});
		  };
		  
		  mi.verHistoria = function(){
				$historia.getHistoria($scope, 'Componente', '/SComponente',mi.componente.id, 1, false, true, false, false)
				.result.then(function(data) {
					if (data != ""){
						
					}
				}, function(){
					
				});
			}
		  
		  if(mi.esTreeview){
			  if($routeParams.nuevo==1){
				  mi.nuevo();
			  }
			  else{
				  $http.post('/SComponente', { accion : 'getComponentePorId', id: $routeParams.id, t: (new Date()).getTime() }).then(function(response) {
						if (response.data.success) {
							mi.componente = response.data.componente;
							mi.componente.inversionNueva = mi.componente.inversionNueva == 1;
							if(mi.componente.fechaInicio != "")
								mi.componente.fechaInicio = moment(mi.componente.fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.componente.fechaFin != "")
								mi.componente.fechaFin = moment(mi.componente.fechaFin, 'DD/MM/YYYY').toDate();
							if(mi.componente.fechaInicioReal != "")
								mi.componente.fechaInicioReal = moment(mi.componente.fechaInicioReal, 'DD/MM/YYYY').toDate();
							if(mi.componente.fechaFinReal != "")
								mi.componente.fechaFinReal = moment(mi.componente.fechaFinReal, 'DD/MM/YYYY').toDate();
							mi.editar();
						}
					});
			  }
		  }
		  
		  mi.t_borrar = function(ev) {
				if (mi.componente!=null && mi.componente.id!=null) {
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar el componente "' + mi.componente.nombre + '"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							var datos = {
									accion : 'borrarComponente',
									id : mi.componente.id,
									t: (new Date()).getTime()
								};
								$http.post('/SComponente', datos).success(
										function(response) {
											if (response.success) {
												
												$utilidades.mensaje('success','Componente borrado con éxito');
												mi.componente = null;		
												$rootScope.$emit("eliminarNodo", {});
											} else{
												$utilidades.mensaje('danger',
														'Error al borrar el Componente');
											}
										});
						}
					}, function(){
						
					});
				} else {
					$utilidades.mensaje('warning',
							'Debe seleccionar el componente que desee borrar');
				}
			};
			
			mi.t_cambiarNombreNodo = function(ev){
				$rootScope.$emit("cambiarNombreNodo",mi.componente.nombre);
			}
			
			mi.t_crearNodo=function(id,nombre,objeto_tipo,estado){
				$rootScope.$emit("crearNodo",{ id: id, nombre: nombre, objeto_tipo: objeto_tipo, estado: estado })
			};
			
			mi.agregarPagos = function() {
				$pagoplanificado.getPagoPlanificado($scope, mi.componente.id,1, 
				function(objetoId, objetoTipo){
					return{
						accion: 'getPagos',
						objetoId: objetoId,
						objetoTipo : objetoTipo
					}
				}, mi.componente.fechaInicio,mi.componente.fechaFin)
				.result.then(function(data) {
					mi.pagos=data;
					mi.componente.costo = 0;
					for (x in mi.pagos){
						mi.componente.costo += mi.pagos[x].pago;
					}
				}, function(){
				});
			};
			
		
		mi.getAsignado = function(){
			if(mi.componente.programa != null){
				$http.post('/SComponente', {
					accion: 'getValidacionAsignado',
					id: mi.componente.id,
					programa: mi.componente.programa,
					subprograma: mi.componente.subprograma,
					proyecto: mi.componente.proyecto,
					actividad: mi.componente.actividad,
					obra: mi.componente.obra,
					renglon: mi.componente.renglon,
					geografico: mi.componente.ubicacionGeografica,
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
			if(mi.componente.costo != null){
				if(mi.componente.programa != null){
					if(mi.componente.costo <= mi.asignado)
						mi.sobrepaso = false;
					else
						mi.sobrepaso = true;
				}
			}
		}
} ]);

app.controller('buscarPorComponente', [ '$uibModalInstance',
	'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$titulo','$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre','$showfilters','$entidad',buscarPorComponente ]);

function buscarPorComponente($uibModalInstance, $rootScope, $scope, $http, $interval,
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

