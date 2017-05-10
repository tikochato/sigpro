var app = angular.module('programapropiedadController', []);

app.controller('programapropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Propiedad Programa';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.programapropiedades = [];
		mi.programapropiedad;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalProgramaPropiedades = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.tipodatos = [];
		
		mi.filtros=[];
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.programapropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programapropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.programapropiedadc.filtrar($event)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programapropiedadc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.programapropiedadc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programapropiedadc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.programapropiedadc.filtrar($event)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.programapropiedad = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.programapropiedadc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.programapropiedadc.ordenDireccion = sortColumns[0].sort.direction;

							grid.appScope.programapropiedadc.cargarTabla(grid.appScope.programapropiedadc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.programapropiedadc.columnaOrdenada!=null){
								grid.appScope.programapropiedadc.columnaOrdenada=null;
								grid.appScope.programapropiedadc.ordenDireccion=null;
							}
						}
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalProgramaPropiedades();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'programapropiedades', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!='')
				    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalProgramaPropiedades();
						  });
				    }
				}
			};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SProgramaPropiedad', { accion: 'getProgramaPropiedadPagina', pagina: pagina, numeroprogramapropiedades: $utilidades.elementosPorPagina,
			filtro_nombre: mi.filtros['nombre'], 
			filtro_usuario_creo: mi.filtros['usuario_creo'],
		    filtro_fecha_creacion: mi.filtros['fecha_creacion'],
		    columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion }).success(
					function(response) {
						mi.programapropiedades = response.programapropiedades;
						mi.gridOptions.data = mi.programapropiedades;
						mi.mostrarcargando = false;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.programapropiedad!=null && mi.programapropiedad.datotipoid!=null){
				$http.post('/SProgramaPropiedad', {
					accion: 'guardarProgramaPropiedad',
					esnuevo: mi.esnuevo,
					id: mi.programapropiedad.id,
					nombre: mi.programapropiedad.nombre,
					descripcion: mi.programapropiedad.descripcion,
					datoTipoId: mi.programapropiedad.datotipoid.id
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Propiedad de Programa '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.programapropiedad.id = response.id;
						mi.programapropiedad.usuarioCreo = response.usuarioCreo;
						mi.programapropiedad.fechaCreacion = response.fechaCreacion;
						mi.programapropiedad.usuarioActualizo = response.usuarioactualizo;
						mi.programapropiedad.fechaActualizacion = response.fechaactualizacion;
						mi.esnuevo = false;
						mi.obtenerTotalProgramaPropiedades();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad de Programa');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
	
		mi.borrar = function(ev) {
			if(mi.programapropiedad!=null && mi.programapropiedad.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar la Propiedad de Programa "'+mi.programapropiedad.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SProgramaPropiedad', {
							accion: 'borrarProgramaPropiedad',
							id: mi.programapropiedad.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Propiedad de Programa borrado con éxito');
								mi.programapropiedad = null;
								mi.obtenerTotalProgramaPropiedades();
							}
							else
								$utilidades.mensaje('danger','Error al borrar la Propiedad de Programa');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad de Programa que desea borrar');
		};
	
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.programapropiedad = {};
			mi.gridApi.selection.clearSelectedRows();
		};
	
		mi.editar = function() {
			if(mi.programapropiedad!=null && mi.programapropiedad.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.programapropiedad.datotipoid = {
						"id" : mi.programapropiedad.datotipoid,
						"nombre" : mi.programapropiedad.datotiponombre
				}

			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad de Programa que desea editar');
		}
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'programapropiedades', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/programapropiedad/rv')
				$route.reload();
			else
				$location.path('/programapropiedad/rv');
		}
		
		mi.obtenerTotalProgramaPropiedades = function() { 
			$http.post('/SProgramaPropiedad', { accion: 'numeroProgramaPropiedades',filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'],
			    filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(
					function(response) {
						mi.totalProgramaPropiedades = response.totalprogramapropiedades;
						mi.cargarTabla(1);
			});
		}
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalProgramaPropiedades();
				mi.gridApi.selection.clearSelectedRows();
				mi.programapropiedad = null;
			}
		}
		
		$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
				function(response) {
					mi.tipodatos = response.datoTipos;
		});
		
	} 
]);