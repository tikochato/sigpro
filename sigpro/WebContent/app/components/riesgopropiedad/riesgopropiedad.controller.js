var app = angular.module('riesgopropiedadController', []);

app.controller('riesgopropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Propiedad Riesgo';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.riesgopropiedades = [];
		mi.riesgopropiedad;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalRiesgoPropiedades = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.tipodatos = [];
		
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		mi.filtros = [];
		
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.riesgopropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.riesgopropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.riesgopropiedadc.filtrar($event)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false,  enableFiltering: false, enableSorting: false },
				    { name: 'usuarioCreo', displayName: 'Usuario Creación',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.riesgopropiedadc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.riesgopropiedadc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.riesgopropiedadc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.riesgopropiedadc.filtrar($event)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.riesgopropiedad = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.riesgopropiedadc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.riesgopropiedadc.ordenDireccion = sortColumns[0].sort.direction;
							for(var i = 0; i<sortColumns.length-1; i++)
								sortColumns[i].unsort();
							grid.appScope.riesgopropiedadc.cargarTabla(grid.appScope.riesgopropiedadc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.riesgopropiedadc.columnaOrdenada!=null){
								grid.appScope.riesgopropiedadc.columnaOrdenada=null;
								grid.appScope.riesgopropiedadc.ordenDireccion=null;
							}
						}
							
					} );

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalRiesgoPropiedades();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'riesgopropiedades', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalRiesgoPropiedades();
						  });
				    }
				}
			};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SRiesgoPropiedad', { accion: 'getRiesgoPropiedadPagina', pagina: pagina, numeroriesgopropiedades: $utilidades.elementosPorPagina,
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
				}).success(
					function(response) {
						mi.riesgopropiedades = response.riesgopropiedades;
						mi.gridOptions.data = mi.riesgopropiedades;
						mi.mostrarcargando = false;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.riesgopropiedad!=null && mi.riesgopropiedad.nombre!=''){
				$http.post('/SRiesgoPropiedad', {
					accion: 'guardarRiesgoPropiedad',
					esnuevo: mi.esnuevo,
					id: mi.riesgopropiedad.id,
					nombre: mi.riesgopropiedad.nombre,
					descripcion: mi.riesgopropiedad.descripcion,
					datoTipoId: mi.riesgopropiedad.datotipoid.id
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Propiedad Riesgo '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.riesgopropiedad.id = response.id;
						mi.esnuevo = false;
						mi.obtenerTotalRiesgoPropiedades();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' la Propiedad Riesgo');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.borrar = function(ev) {
			if(mi.riesgopropiedad!=null && mi.riesgopropiedad.id!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar la Propiedad Riesgo "'+mi.riesgopropiedad.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SRiesgoPropiedad', {
						accion: 'borrarRiesgoPropiedad',
						id: mi.riesgopropiedad.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Propiedad Riesgo borrado con éxito');
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar la Propiedad Riesgo');
					});
			    }, function() {

			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad Riesgo que desea borrar');
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.riesgopropiedad = {};
			mi.gridApi.selection.clearSelectedRows();
			
		};

		mi.editar = function() {
			if(mi.riesgopropiedad!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.riesgopropiedad.datotipoid = {
						"id" : mi.riesgopropiedad.datotipoid,
						"nombre" : mi.riesgopropiedad.datotiponombre
				}
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad Riesgo que desea editar');
		}

		mi.irATabla = function() {
			mi.mostraringreso=false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'riesgopropiedades', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()=='/riesgopropiedad/rv')
				$route.reload();
			else
				$location.path('/riesgopropiedad/rv');
		}
		
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalRiesgoPropiedades();
			}
		};
		
		mi.obtenerTotalRiesgoPropiedades=function(){
			$http.post('/SRiesgoPropiedad', { accion: 'numeroRiesgoPropiedades',objetoid:$routeParams.objeto_id, tipo: mi.objetotipo,
				filtro_nombre: mi.filtros['nombre'],
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(
					function(response) {
						mi.totalRiesgoPropiedades = response.totalriesgopropiedades;
						mi.cargarTabla(1);
			});
		};
		
		
		
		

		/*$http.post('/SRiesgoPropiedad', { accion: 'numeroRiesgoPropiedades' }).success(
				function(response) {
					mi.totalRiesgoPropiedades = response.totalriesgopropiedades;
					mi.cargarTabla(1);
		});*/
		$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
				function(response) {
					mi.tipodatos = response.datoTipos;
		});

	}
]);
