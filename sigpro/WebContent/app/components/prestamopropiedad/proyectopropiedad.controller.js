var app = angular.module('proyectopropiedadController', []);

app.controller('proyectopropiedadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
		var mi=this;
		
		$window.document.title = $utilidades.sistema_nombre+' - Propiedad Préstamo';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.proyectopropiedades = [];
		mi.proyectopropiedad;
		mi.tipoDatoSeleccionado = 0;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalProyectoPropiedades = 0;
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
			    rowTemplate: '<div ng-dblclick="grid.appScope.proyectopropiedadc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [ 
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.proyectopropiedadc.filtros[\'nombre\']" ng-keypress="grid.appScope.proyectopropiedadc.filtrar($event)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'datotiponombre', displayName: 'Tipo dato', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.proyectopropiedadc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.proyectopropiedadc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.proyectopropiedadc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.proyectopropiedadc.filtrar($event)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.proyectopropiedad = row.entity;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.proyectopropiedadc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.proyectopropiedadc.ordenDireccion = sortColumns[0].sort.direction;

							if(grid.appScope.proyectopropiedadc.columnaOrdenada=="datotiponombre"){
								grid.appScope.proyectopropiedadc.columnaOrdenada="datoTipo";
							}
							grid.appScope.proyectopropiedadc.cargarTabla(grid.appScope.proyectopropiedadc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.proyectopropiedadc.columnaOrdenada!=null){
								grid.appScope.proyectopropiedadc.columnaOrdenada=null;
								grid.appScope.proyectopropiedadc.ordenDireccion=null;
							}
						}
					} );
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalProyectoPropiedades();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyctopropiedades', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!='')
				    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalProyectoPropiedades();
						  });
				    }
				}
			};
		
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SProyectoPropiedad', { accion: 'getProyectoPropiedadPagina', pagina: pagina, numeroproyectopropiedad: $utilidades.elementosPorPagina,
			filtro_nombre: mi.filtros['nombre'], 
			filtro_usuario_creo: mi.filtros['usuario_creo'],
		    filtro_fecha_creacion: mi.filtros['fecha_creacion'],
		    columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, 
		    t: (new Date()).getTime()}).success(
					function(response) {
						mi.proyectopropiedades = response.proyectopropiedades;
						mi.gridOptions.data = mi.proyectopropiedades;
						mi.mostrarcargando = false;
						mi.paginaActual = pagina;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.proyectopropiedad!=null && mi.tipoDatoSeleccionado!=null){
				$http.post('/SProyectoPropiedad', {
					accion: 'guardarProyectoPropiedad',
					esnuevo: mi.esnuevo,
					id: mi.proyectopropiedad.id,
					nombre: mi.proyectopropiedad.nombre,
					descripcion: mi.proyectopropiedad.descripcion,
					datoTipoId: mi.tipoDatoSeleccionado.id,
					t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Propiedad de Préstamo '+(mi.esnuevo ? 'creada' : 'guardada')+' con éxito');
						mi.proyectopropiedad.id = response.id;
						mi.proyectopropiedad.usuarioCreo = response.usuarioCreo;
						mi.proyectopropiedad.fechaCreacion = response.fechaCreacion;
						mi.proyectopropiedad.usuarioActualizo = response.usuarioactualizo;
						mi.proyectopropiedad.fechaActualizacion = response.fechaactualizacion;
						mi.esnuevo = false;
						mi.obtenerTotalProyectoPropiedades();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' la Propiedad de Préstamo');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};
	
		mi.borrar = function(ev) {
			if(mi.proyectopropiedad!=null && mi.proyectopropiedad.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar la Propiedad de Préstamo "'+mi.proyectopropiedad.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SProyectoPropiedad', {
							accion: 'borrarProyectoPropiedad',
							id: mi.proyectopropiedad.id,
							t: (new Date()).getTime()
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Propiedad de Préstamo borrado con éxito');
								mi.proyectopropiedad = null;
								mi.obtenerTotalProyectoPropiedades();
							}
							else
								$utilidades.mensaje('danger','Error al borrar la Propiedad de Préstamo');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad de Préstamo que desea borrar');
		};
	
		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.proyectopropiedad = {};
			mi.tipoDatoSeleccionado=null;
			mi.gridApi.selection.clearSelectedRows();
			$utilidades.setFocus(document.getElementById("nombre"));
		};
	
		mi.editar = function() {
			if(mi.proyectopropiedad!=null && mi.proyectopropiedad.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.tipoDatoSeleccionado = {
						"id" : mi.proyectopropiedad.datotipoid,
						"nombre" : mi.proyectopropiedad.datotiponombre
				}
				$utilidades.setFocus(document.getElementById("nombre"));
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad de Préstamo que desea editar');
		}
	
		mi.irATabla = function() {
			mi.mostraringreso=false;
		}
		
		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'proyctopropiedades', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
			$http.post('/SEstadoTabla', tabla_data).then(function(response){
				
			});
		}
		
		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}
		
		mi.reiniciarVista=function(){
			if($location.path()=='/prestamopropiedad/rv')
				$route.reload();
			else
				$location.path('/prestamopropiedad/rv');
		}
		
		mi.obtenerTotalProyectoPropiedades = function() { 
			$http.post('/SProyectoPropiedad', { accion: 'numeroProyectoPropiedades',filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'],
			    filtro_fecha_creacion: mi.filtros['fecha_creacion'], 
			    t: (new Date()).getTime() }).success(
					function(response) {
						mi.totalProyectoPropiedades = response.totalproyectopropiedades;
						mi.cargarTabla(1);
			});
		}
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalProyectoPropiedades();
				mi.gridApi.selection.clearSelectedRows();
				mi.proyectopropiedad = null;
			}
		}
		
		$http.post('/SDatoTipo', { accion: 'cargarCombo', t: (new Date()).getTime() }).success(
				function(response) {
					mi.tipodatos = response.datoTipos;
		});
		
	} 
]);

