var app = angular.module('actividadController', []);

app.controller('actividadController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal) {
		var mi=this;

		$window.document.title = 'SIGPRO - Actividades';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.actividades = [];
		mi.actividad;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalActividades = 0;
		mi.objetoid = $routeParams.objeto_id;
		mi.objetotipo = $routeParams.objeto_tipo;
		mi.objetoNombre="";
		mi.objetTipoNombre = "";
		mi.paginaActual = 1;
		mi.datotipoid = "";
		mi.datotiponombre = "";
		mi.actividadtipoid = "";
		mi.actividadnombre = "";
		mi.formatofecha = 'dd/MM/yyyy';
		mi.camposdinamicos = {};
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
		mi.filtros = [];

		$http.post('/SObjeto', { accion: 'getObjetoPorId', id: $routeParams.objeto_id, tipo: mi.objetotipo }).success(
				function(response) {
					mi.objetoid = response.id;
					mi.objetoNombre = response.nombre;
					mi.objetoTipoNombre = response.tiponombre;
		});

		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2050, 12, 31),
				minDate : new Date(1990, 1, 1),
				startingDay : 1
		};
		
		mi.ff_opciones = {
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
			    useExternalFiltering: true,
			    useExternalSorting: true,
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left',
						filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.recursoc.filtrar($event,1)"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación', 
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.recursoc.filtrar($event,2)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" ng-keypress="grid.appScope.recursoc.filtrar($event,3)"></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.actividad = row.entity;
						mi.actividad.fechaInicio = moment(mi.actividad.fechaInicio,'DD/MM/YYYY').toDate();
						mi.actividad.fechaFin = moment(mi.actividad.fechaFin,'DD/MM/YYYY').toDate();
						mi.ff_opciones.minDate = mi.actividad.fechaInicio;
					});
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
						if(sortColumns.length==1){
							grid.appScope.actividadc.columnaOrdenada=sortColumns[0].field;
							grid.appScope.actividadc.ordenDireccion = sortColumns[0].sort.direction;
							for(var i = 0; i<sortColumns.length-1; i++)
								sortColumns[i].unsort();
							grid.appScope.actividadc.cargarTabla(grid.appScope.actividadc.paginaActual);
						}
						else if(sortColumns.length>1){
							sortColumns[0].unsort();
						}
						else{
							if(grid.appScope.actividadc.columnaOrdenada!=null){
								grid.appScope.actividadc.columnaOrdenada=null;
								grid.appScope.actividadc.ordenDireccion=null;
							}
						}
							
					} );

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'actividades', t: (new Date()).getTime()}).then(function(response){
					      if(response.data.success && response.data.estado!='')
					    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						  });
				    }
				}
		};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SActividad', { accion: 'getActividadesPaginaPorObjeto', pagina: pagina, numeroactividades: $utilidades.elementosPorPagina, 
				objetoid: $routeParams.objeto_id, tipo: mi.objetotipo,
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
				columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
			}).success(
					function(response) {
						mi.actividades = response.actividades;
						mi.gridOptions.data = mi.actividades;
						mi.mostrarcargando = false;
					});
		}

		mi.guardar=function(valid){
			for (campos in mi.camposdinamicos) {
				if (mi.camposdinamicos[campos].tipo === 'fecha') {
					mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
				}
			}
			if(mi.actividad!=null){
				$http.post('/SActividad', {
					accion: 'guardarActividad',
					esnuevo: mi.esnuevo,
					actividadtipoid : mi.actividadtipoid,
					id: mi.actividad.id,
					objetoid: $routeParams.objeto_id,
					objetotipo: mi.objetotipo,
					nombre: mi.actividad.nombre,
					descripcion: mi.actividad.descripcion,
					fechainicio: moment(mi.actividad.fechaInicio).format('DD/MM/YYYY'),
					fechafin: moment(mi.actividad.fechaFin).format('DD/MM/YYYY'),
					porcentajeavance: mi.actividad.porcentajeavance,
					programa: mi.actividad.programa,
					subprograma: mi.actividad.subprograma,
					proyecto: mi.actividad.proyecto,
					actividad: mi.actividad.actividad,
					obra: mi.actividad.obra,
					datadinamica : JSON.stringify(mi.camposdinamicos)
				}).success(function(response){
					if(response.success){
						mi.actividad.id = response.id;
						$utilidades.mensaje('success','Actividad '+(mi.esnuevo ? 'creada' : 'guardado')+' con éxito');
						mi.cargarTabla();
						mi.esnuevo = false;
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' la Actividad');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.borrar = function(ev) {
			if(mi.actividad!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar la Actividad "'+mi.actividad.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SActividad', {
						accion: 'borrarActividad',
						id: mi.actividad.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Actividad borrada con éxito');
							mi.actividad = null;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar la Actividad');
					});
			    }, function() {

			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Actividad que desea borrar');
		};

		mi.nuevo = function() {
			mi.datotipoid = "";
			mi.datotiponombre = "";
			mi.actividadtipoid = "";
			mi.actividadnombre = "";
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.actividad = null;
			mi.gridApi.selection.clearSelectedRows();
		};

		mi.editar = function() {
			if(mi.actividad!=null){
				mi.mostraringreso = true;
				mi.actividadtipoid = mi.actividad.actividadtipoid;
				mi.esnuevo = false;
				
				var parametros = {
						accion: 'getActividadPropiedadPorTipo',
						idActividad: mi.actividad!='' ? mi.actividad.id : 0,
					    idActividadTipo: mi.actividadtipoid
				}
				$http.post('/SActividadPropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.proyectopropiedades
					for (campos in mi.camposdinamicos) {
						switch (mi.camposdinamicos[campos].tipo){
							case "fecha":
								mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
								break;
							case "entero":
								mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
								break;
						}

					}
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la Actividad que desea editar');
		}

		mi.irATabla = function() {
			mi.mostraringreso=false;
			mi.esNuevo = false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'actividades', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()==('/actividad/'+ mi.objetoid + '/' + mi.objetotipo + '/rv'))
				$route.reload();
			else
				$location.path('/actividad/'+ mi.objetoid + '/' + mi.objetotipo + '/rv');
		}

		mi.abrirPopupFecha = function(index) {
			if(index<1000){
				mi.camposdinamicos[index].isOpen = true;
			}
			else{
				switch(index){
					case 1000: mi.fi_abierto = true; break;
					case 1001: mi.ff_abierto =  true; break;
				}
			}
				
		};
		
		mi.actualizarfechafin =  function(){
			if(mi.actividad.fechaInicio!=''){
				var m = moment(mi.actividad.fechaInicio);
				if(m.isValid()){
					mi.ff_opciones.minDate = m.toDate();
					if(mi.actividad.fechaFin!=null && mi.actividad.fechaFin<mi.actividad.fechaInicio)
						mi.actividad.fechaFin = mi.actividad.fechaInicio;
				}
			}
		}

		$http.post('/SActividad', { accion: 'numeroActividadesPorObjeto',objetoid:$routeParams.objeto_id, tipo: mi.objetotipo }).success(
				function(response) {
					mi.totalActividades = response.totalactividades;
					mi.cargarTabla(1);
		});
		
		
		mi.buscarActividadTipo = function(titulo, mensaje) {

			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarActividadTipo.jsp',
				controller : 'modalBuscarActividadTipo',
				controllerAs : 'modalBuscar',
				backdrop : 'static',
				size : 'md',
				resolve : {
					titulo : function() {
						return titulo;
					},
					mensaje : function() {
						return mensaje;
					}
				}
			});

			modalInstance.result.then(function(selectedItem) {
				mi.actividadtipoid = selectedItem.id;
				mi.actividadtiponombre = selectedItem.nombre;

				var parametros = {
						accion: 'getActividadPropiedadPorTipo',
						idActividad: mi.actividad!=null ? mi.actividad.id : 0,
						idActividadTipo: selectedItem.id
				}

				$http.post('/SActividadPropiedad', parametros).then(function(response){
					mi.camposdinamicos = response.data.actividadpropiedades;
					for (campos in mi.camposdinamicos) {
						if (mi.camposdinamicos[campos].tipo === 'fecha') {
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null; 
						}
					}
				});

			}, function() {
			});
	};

} ]);

app.controller('modalBuscarActividadTipo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'mensaje', modalBuscarActividadTipo ]);

function modalBuscarActividadTipo($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, titulo, mensaje) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post('/SActividadTipo', {
		accion : 'numeroActividadTipos'
	}).success(function(response) {
		mi.totalElementos = response.totalcooperantes;
		mi.elementosPorPagina = mi.totalElementos;
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Nombre Tipo',
			name : 'nombre',
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
					mi.seleccionarEntidad);
		}
	}

	mi.seleccionarEntidad = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarData = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			registros : mi.elementosPorPagina
		};

		mi.mostrarCargando = true;
		$http.post('/SActividadTipo', {accion : 'getActividadtiposPagina'}).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.actividadtipos;
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
			$utilidades.mensaje('warning', 'Debe seleccionar un Tipo');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}
