var app = angular.module('programatipoController', [ 'ngTouch']);

app.controller('programatipoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal, $dialogoConfirmacion) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Tipo Programa';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.programatipos = [];
		mi.programatipo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalProgramatipos = 0;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		
		mi.filtros = [];
		mi.orden = null;


		mi.programapropiedades =[];
		mi.programapropiedad =null;
		mi.mostrarcargandoProgProp=true;
		mi.mostrarPropiedad = false;
		mi.paginaActualPropiedades=1;

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
			    rowTemplate: '<div ng-dblclick="grid.appScope.programatipoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left'
				    	,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programatipoc.filtros[\'nombre\']" ng-keypress="grid.appScope.programatipoc.filtrar($event)" style="width:175px;"></input></div>'
				    },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación' 
				    	,filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programatipoc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.programatipoc.filtrar($event)"></input></div>'
				    },
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				    	filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programatipoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.programatipoc.filtrar($event)" ></input></div>'
				    }
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.programatipo = row.entity;
					});
					
					
					gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
							if(sortColumns.length==1){
								grid.appScope.programatipoc.columnaOrdenada=sortColumns[0].field;
								grid.appScope.programatipoc.ordenDireccion = sortColumns[0].sort.direction;
								grid.appScope.programatipoc.cargarTabla(grid.appScope.programatipoc.paginaActual);
							}
							else if(sortColumns.length>1){
								sortColumns[0].unsort();
							}
							else{
								if(grid.appScope.programatipoc.columnaOrdenada!=null){
									grid.appScope.programatipoc.columnaOrdenada=null;
									grid.appScope.programatipoc.ordenDireccion=null;
								}
							}
								
						} );
					

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
						mi.obtenerTotalProgramatipos();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'programaTipos', t: (new Date()).getTime()}).then(function(response){
				    		  if(response.data.success && response.data.estado!='')
				    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
					    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
						      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
						      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
						      mi.obtenerTotalProgramatipos();
						  });
				    }
				}
		};

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SProgramaTipo', { accion: 'getProgramaTipoPagina', pagina: pagina, numeroprogramatipo: $utilidades.elementosPorPagina
				,filtro_nombre: mi.filtros['nombre'] 
				,filtro_usuario_creo: mi.filtros['usuario_creo']
			    ,filtro_fecha_creacion: mi.filtros['fecha_creacion']
			    ,columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion
			    }).success(
			
					function(response) {
						mi.programatipos = response.porgramatipos;
						mi.gridOptions.data = mi.programatipos;
						mi.mostrarcargando = false;
					});
		}
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			if(mi.programatipo!=null  && mi.programatipo.nombre!=''){
				var idspropiedad="";
				for (i = 0 ; i<mi.programapropiedades.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.programapropiedades[i].id);
					}else{
						idspropiedad = idspropiedad.concat(",",mi.programapropiedades[i].id);
					}
				}

				$http.post('/SProgramaTipo', {
					accion: 'guardarProgramatipo',
					esnuevo: mi.esnuevo,
					id: mi.programatipo.id,
					nombre: mi.programatipo.nombre,
					descripcion: mi.programatipo.descripcion,
					propiedades: idspropiedad
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Tipo de Programa'+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.programatipo.id = response.id;
						mi.programatipo.usuarioCreo = response.usuarioCreo;
						mi.programatipo.fechaCreacion = response.fechaCreacion;
						mi.programatipo.usuarioActualizo = response.usuarioactualizo;
						mi.programatipo.fechaActualizacion = response.fechaactualizacion;
						
						mi.obtenerTotalProgramatipos();

					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Tipo de Programa');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.editar = function() {
			if(mi.programatipo!=null && mi.programatipo.id!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.cargarTotalPropiedades();
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Programa que desea editar');
		}


		mi.borrar = function(ev) {
			if(mi.programatipo!=null && mi.programatipo.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el tipo de programa "'+mi.programatipo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SProgramaTipo', {
							accion: 'borrarProgramaTipo',
							id: mi.programatipo.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Tipo de Programa borrado con éxito');
								mi.programatipo = null;
								mi.obtenerTotalProgramatipos();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Tipo de Programa');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo de Programa que desea borrar');
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.programatipo = {};
			mi.gridApi.selection.clearSelectedRows();
			mi.cargarTotalPropiedades();
		};

		mi.irATabla = function() {
			mi.mostraringreso=false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'programatipos', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()=='/programatipo/rv')
				$route.reload();
			else
				$location.path('/programatipo/rv');
		}

		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalProgramatipos();
				mi.gridApi.selection.clearSelectedRows();
				mi.programatipo = null;
			}
		}
		
		mi.obtenerTotalProgramatipos = function(){
			$http.post('/SProgramaTipo', { accion: 'numeroProgramaTipos',
				filtro_nombre: mi.filtros['nombre'], 
				filtro_usuario_creo: mi.filtros['usuarioCreo'], filtro_fecha_creacion: mi.filtros['fechaCreacion'] }).then(
					function(response) {
						mi.totalProgramatipos = response.data.totalprogramatipos;
						mi.paginaActual = 1;
						mi.cargarTabla(mi.paginaActual);
			});
		}

		mi.gridOptionsProgramaPropiedad = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: 10,
				columnDefs : [
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'datotiponombre', displayName: 'Tipo Dato'}

				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.programapropiedad = row.entity;
					});
				}
		};

		mi.cargarTablaPropiedades = function(pagina){

			mi.mostrarcargandoProgProp=true;
			$http.post('/SProgramaPropiedad',
					{
						accion: 'getProgramaPropiedadPaginaPorTipoProg',
						pagina: pagina,
						idProgramaTipo:mi.programatipo!=null ? mi.programatipo.id : null,
						numeroprogramapropiedad: $utilidades.elementosPorPagina }).success(
				function(response) {

					mi.programapropiedades = response.programapropiedades;
					mi.gridOptionsProgramaPropiedad.data = mi.programapropiedades;
					mi.mostrarcargandoProgProp = false;
					mi.mostrarPropiedad = true;
				});

		}


		mi.cargarTotalPropiedades = function(){
			$http.post('/SProgramaPropiedad', { accion: 'numeroProgramaPropiedades' }).success(
					function(response) {
						mi.totalProgramapropiedades = response.totalprogramapropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualPropiedades);
					}
			);
		}

		mi.eliminarPropiedad = function(){
			if (mi.programapropiedad != null){
				for (i = 0 ; i<mi.programapropiedades.length ; i ++){
					if (mi.programapropiedades[i].id === mi.programapropiedad.id){
						mi.programapropiedades.splice (i,1);
						break;
					}
				}
				mi.programapropiedad = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}

		mi.eliminarPropiedad2 = function(row){
			var index = mi.programapropiedades.indexOf(row);
	        if (index !== -1) {
	            mi.programapropiedades.splice(index, 1);
	        }
		}

		mi.seleccionTabla = function(row){
			if (mi.programapropiedad !=null && mi.programapropiedad.id == row.id){
				mi.programapropiedad = null;
			}else{
				mi.programapropiedad = row;
			}
		}

		mi.buscarPropiedad = function(titulo, mensaje) {
			var modalInstance = $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : 'buscarpropiedad.jsp',
			    controller : 'modalBuscarPropiedad',
			    controllerAs : 'modalBuscar',
			    backdrop : 'static',
			    size : 'md',
			    resolve : {
					idspropiedad : function() {
						var idspropiedad = "";
						var propiedadTemp;
						for (i = 0, len = mi.programapropiedades.length;  i < len; i++) {
				    		if (i == 0){
				    			idspropiedad = idspropiedad.concat("",mi.programapropiedades[i].id);
				    		}else{
				    			idspropiedad = idspropiedad.concat(",",mi.programapropiedades[i].id);
				    		}
				    	}
					    return idspropiedad;
					}
			    }

			});

			modalInstance.result.then(function(selectedItem) {
				mi.programapropiedades.push(selectedItem);

			}, function() {
			});
		}
} ]);

app.controller('modalBuscarPropiedad', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','idspropiedad', modalBuscarPropiedad
]);

function modalBuscarPropiedad($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log,idspropiedad) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

    $http.post('/SProgramaPropiedad', {
    	accion : 'numeroProgramaPropiedadesDisponibles'
        }).success(function(response) {
    	mi.totalElementos = response.totalprogramapropiedades;
    	mi.elementosPorPagina = mi.totalElementos;
    	mi.cargarData(1);
    });

    mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [
			{displayName : 'Id', name : 'id', cellClass : 'grid-align-right', type : 'number', width : 100
			}, { displayName : 'Nombre', name : 'nombre', cellClass : 'grid-align-left'}
		],
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
			    mi.seleccionarProgramaPropiedad);
		}
    }

    mi.seleccionarProgramaPropiedad = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };

    mi.cargarData = function(pagina) {
    	var datos = {
    	    accion : 'getProgramaPropiedadesTotalDisponibles',
    	    pagina : pagina,
    	    idspropiedades: idspropiedad,
    	    registros : mi.elementosPorPagina
    	};

    	mi.mostrarCargando = true;
    	$http.post('/SProgramaPropiedad', datos).then(function(response) {
    	    if (response.data.success) {

    	    	mi.data = response.data.programapropiedades;
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
    	    $utilidades.mensaje('warning', 'Debe seleccionar una Propiedad');
    	}
     };

     mi.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
     };
}
