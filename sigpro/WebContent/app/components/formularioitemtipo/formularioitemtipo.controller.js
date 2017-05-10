var app = angular.module('formularioitemtipoController', []);

app.controller('formularioitemtipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog, $dialogoConfirmacion) {
			var mi=this;

			$window.document.title = $utilidades.sistema_nombre+' - Tipo de Item de Formulario';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.formularioitemtipos = [];
			mi.formularioitemtipo;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalFormularioItmeTipos = 0;
			mi.paginaActual = 1;
			mi.tipodatos = [];
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;

			mi.columnaOrdenada=null;
			mi.ordenDireccion = null;
			mi.filtros = [];
			mi.orden = null;

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
			    rowTemplate: '<div ng-dblclick="grid.appScope.formularioitemtipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			    columnDefs : [
					{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.formularioitemtipoc.filtrar($event,1)"></input></div>'
				    },
				    { name: 'datotiponombre', displayName: 'Tipo de dato', width: 150, cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creo', width: 200, cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.formularioitemtipoc.filtrar($event,2)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', width: 200, cellClass: 'grid-align-left',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.formularioitemtipoc.filtrar($event,3)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false}

				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.formularioitemtipo = row.entity;
					});

					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.formularioitemtipoc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.formularioitemtipoc.ordenDireccion = sortColumns[0].sort.direction;

							grid.appScope.formularioitemtipoc.cargarTabla(grid.appScope.formularioitemtipoc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.formularioitemtipoc.columnaOrdenada!=null){
								grid.appScope.formularioitemtipoc.columnaOrdenada=null;
								grid.appScope.formularioitemtipoc.ordenDireccion=null;
							}
						}

					} );

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'formularioitemtipos', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
						  });
				    }
				}
			};
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SFormularioItemTipo', { accion: 'getFormularioItemtiposPagina', pagina: pagina, numeroformularioitemtipos: $utilidades.elementosPorPagina,
					filtro_nombre: mi.filtros['nombre'],
					filtro_usuario_creo: mi.filtros['usuarioCreo'],
					filtro_fecha_creacion: mi.filtros['fechaCreacion'],
					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion}).success(
						function(response) {
							mi.formularioitemtipos = response.formularioitemtipos;
							mi.gridOptions.data = mi.formularioitemtipos;
							mi.mostrarcargando = false;
						});
			}

			mi.irATabla = function() {
				mi.mostraringreso=false;
			}

			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'formularioitemtipos', estado: JSON.stringify(estado), t: (new Date()).getTime() };
				$http.post('/SEstadoTabla', tabla_data).then(function(response){

				});
			}

			mi.cambioPagina=function(){
				mi.cargarTabla(mi.paginaActual);
			}

			mi.reiniciarVista=function(){
				if($location.path()=='/formularioitemtipo/rv')
					$route.reload();
				else
					$location.path('/formularioitemtipo/rv');
			}

			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.formularioitemtipo = null;
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.guardar=function(){
				if(mi.formularioitemtipo!=null && mi.formularioitemtipo.nombre!='' && mi.formularioitemtipo.datotipo!=null){
					$http.post('/SFormularioItemTipo', {
						accion: 'guardarFormularioItemTipo',
						esnuevo: mi.esnuevo,
						id: mi.formularioitemtipo.id,
						nombre: mi.formularioitemtipo.nombre,
						descripcion: mi.formularioitemtipo.descripcion,
						datotipoid: mi.formularioitemtipo.datotipo.id
					}).success(function(response){
						if(response.success){
							mi.formularioitemtipo.id = response.id;
							mi.formularioitemtipo.usuarioCreo = response.usuarioCreo;
							mi.formularioitemtipo.fechaCreacion = response.fechaCreacion;
							mi.formularioitemtipo.usuarioActualizo = response.usuarioActualizacion;
							mi.formularioitemtipo.fechaActualizacion = response.fechaActualizacion;
							$utilidades.mensaje('success','Tipo de Itme de Formulario '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.esnuevo = false;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Tipo de Item de Formulario');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.editar = function() {
				if(mi.formularioitemtipo!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
					mi.formularioitemtipo.datotipo = {
							"id" : mi.formularioitemtipo.datotipoid,
							"nombre" : mi.formularioitemtipo.datotiponombre
					}
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Tipo de Item de Formulario que desea editar');
			}

			mi.borrar = function(ev) {
				if(mi.formularioitemtipo!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar el Tipo Item de fomrulario "'+mi.formularioitemtipo.nombre+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SFormularioItemTipo', {
								accion: 'borrarFormularioItemTipo',
								id: mi.formularioitemtipo.id
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Tipo de Item de Formulario fue borrado con éxito');
									mi.formularioitemtipo = null;
									mi.cargarTabla();
								}
								else
									$utilidades.mensaje('danger','Error al borrar el Tipo De Item de Formulario');
							});
						}
					}, function(){
						
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Tipo De Item de Formulario que desea borrar');
			};

			mi.filtrar = function(evt,tipo){
				if(evt.keyCode==13){
					switch(tipo){
						case 1: mi.filtros['nombre'] = evt.currentTarget.value; break;
						case 2: mi.filtros['usuarioCreo'] = evt.currentTarget.value; break;
						case 3: mi.filtros['fechaCreacion'] = evt.currentTarget.value; break;

					}
					mi.cargarTabla(mi.paginaActual);
					mi.gridApi.selection.clearSelectedRows();
					mi.formularioitemtipo = null;
				}
			}


			$http.post('/SFormularioItemTipo', { accion: 'numeroFormularioItemTipos' }).success(
				function(response) {
					mi.totalFormularioItmeTipos = response.totalformularioitemtipos;
					mi.cargarTabla(1);
			});

			$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
					function(response) {
						mi.tipodatos = response.datoTipos;
			});

}]);
