var app = angular.module('metavalorController', []);

app.controller('metavalorController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Valores de Meta';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.metavalores = [];
			mi.metavalor;
			mi.metavalorId = $routeParams.metaid;
			mi.datoTipoNombre;
			mi.datoTipoId = $routeParams.datotipoid;
			mi.mostraringreso=false;
			mi.nuevo = false;
			mi.totalMetaValores = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.formatofecha = 'dd/MM/yyyy';
			mi.altformatofecha = ['d!/M!/yyyy'];
			
			mi.nombreMeta = "";
			
			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			
			mi.filtros = [];
			
			$http.post('/SMeta', { accion: 'obtenerMetaPorId', id: $routeParams.metaid,t: (new Date()).getTime() }).success(
					function(response) {
						mi.nombreMeta = response.nombre;
			});
			
			$http.post('/SDatoTipo', { accion: 'getDatoTipoPorId', id: $routeParams.datotipoid,t: (new Date()).getTime() }).success(
					function(response) {
						mi.datoTipoNombre = response.nombre;
			});
						
			mi.editarElemento = function (event) {
		        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
		        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
		        mi.editar();
		    };
			
		    mi.abrirPopupFecha = function(index) {
				if(index==1000){
					mi.fc_abierto = true;
				}

			};

			mi.fechaOptions = {
					formatYear : 'yy',
					maxDate : new Date(2050, 12, 31),
					minDate : new Date(1990, 1, 1),
					startingDay : 1
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
				    rowTemplate: '<div ng-dblclick="grid.appScope.metavc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
					columnDefs : [ 
						{ name: 'fecha', width: 400, displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'', enableFiltering: false
					    },
					    { name: 'valor', displayName: 'Valor',cellClass: 'grid-align-center',  enableFiltering: false
						},
						{ name: 'usuario', width: 400,  displayName: 'Usuario Creación',  enableFiltering: false
					    }
					],
					onRegisterApi: function(gridApi) {
						mi.gridApi = gridApi;
						gridApi.selection.on.rowSelectionChanged($scope,function(row) {
							mi.metavalor = row.entity;
							mi.metavalor.fecha = moment(mi.metavalor.fecha,'DD/MM/YYYY').toDate();
						});
						
						gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.metavc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.metavc.ordenDireccion = sortColumns[0].sort.direction;
								for(var i = 0; i<sortColumns.length-1; i++)
									sortColumns[i].unsort();
								grid.appScope.metavc.cargarTabla(grid.appScope.metavc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.metavc.columnaOrdenada!=null){
									grid.appScope.metavc.columnaOrdenada=null;
									grid.appScope.metavc.ordenDireccion=null;
								}
							}
								
						} );
						
						if($routeParams.reiniciar_vista=='rv'){
							mi.guardarEstado();
							mi.obtenerTotalMetaValores();
					    }
					    else{
					    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'metavalores', t: (new Date()).getTime()}).then(function(response){
					    	  if(response.data.success && response.data.estado!='')
						    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
							  mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
							  mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
							  mi.obtenerTotalMetaValores();
							  });
					    }
					}
				};
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SMetaValor', { accion: 'getMetaValoresTabla', 
					metaid: $routeParams.metaid, datotipoid: $routeParams.datotipoid ,t: (new Date()).getTime()
					, pagina:mi.paginaActual, totalValores:$utilidades.elementosPorPagina
				}).success(
						function(response) {
							mi.metavalores = response.MetaValores;
							mi.gridOptions.data = mi.metavalores;
							for (x in mi.metavalores){
								switch (mi.metavalores[x].datoTipo){
									case 2: mi.metavalores[x].valor = Number(mi.metavalores[x].valor);
									case 3: mi.metavalores[x].valor = Number(mi.metavalores[x].valor);
								}
							}
							mi.mostrarcargando = false;
							
						});
			}
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			
			
			mi.guardar=function(){
				if(mi.metavalor!=null && mi.metavalor.valor!='' && mi.metavalor.valor!=null ){
					var valorTemp = mi.metavalor.valor;
					if (mi.datoTipoId == 5)
						valorTemp = moment(valorTemp).format('DD/MM/YYYY');
						
					$http.post('/SMetaValor', {
						accion: 'guardarMetaValor',
						esnuevo: mi.nuevo,
						metaid: mi.metavalorId,
						fecha: moment(mi.metavalor.fecha).format('DD/MM/YYYY'),
						valorEntero: mi.metavalor.valorEntero,
						valorString: mi.metavalor.valorString,
						valorDecimal: mi.metavalor.valorDecimal,
						valorTiempo: mi.metavalor.valorTiempo,
						datotipoid: mi.datoTipoId,
						valor : valorTemp
						
						
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Valor '+(mi.nuevo ? 'creado' : 'guardado')+' con éxito');
							mi.obtenerTotalMetaValores();
							mi.nuevo = false;
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.nuevo ? 'crear' : 'guardar')+' el valor');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};


			mi.nuevoValor = function() {
				mi.mostraringreso=true;
				mi.nuevo = true;
				mi.metavalor = {};
				mi.gridApi.selection.clearSelectedRows();
			};
			
			mi.borrar = function(ev) {
				if(mi.metavalor!=null ){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar el valor de meta ?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SMetaValor', {
								accion: 'borrarMetaValor',
								metaid: mi.metavalorId,
								fecha: moment(mi.metavalor.fecha).format('DD/MM/YYYY')
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Valor de la meta borrado con éxito');
									mi.metavalor=null;
									mi.obtenerTotalMetaValores();
								}
								else
									$utilidades.mensaje('danger','Error al borrar el valor de la meta');
							});
						}
					}, function(){
						
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Hito que desea borrar');
			};

			mi.editar = function() {
				if(mi.metavalor!=null && mi.metavalor.metaid!=null){
					mi.mostraringreso = true;
					mi.nuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Valor que desea editar');
				
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'metavalores', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			}
			
			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}
			
			mi.reiniciarVista=function(){
				if($location.path()=='/metavalor/'+ $routeParams.metaid + '/' + $routeParams.datotipoid + '/rv')
					$route.reload();
				else
					$location.path('/metavalor/'+ $routeParams.metaid + '/' + $routeParams.datotipoid + '/rv');
			}
						
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					mi.obtenerTotalMetaValores();
					mi.gridApi.selection.clearSelectedRows();
					mi.metavalor = null;
				}
			}
			
			mi.obtenerTotalMetaValores =  function() {
				$http.post('/SMetaValor', { accion: 'numeroMetaValores', metaid: $routeParams.metaid ,t: (new Date()).getTime()}).success(
						function(response) {
							mi.totalMetaValores = response.totalMetaValores;
							mi.cargarTabla(1);
						});
				
				};
				
			mi.popupfecharesultado = function() {
			    mi.popupfecharesultado.abierto = false;
			};
			
			mi.abirpopupreultado = function() {
				 mi.popupfecharesultado.abierto = true;
			};		
		}
	]);
