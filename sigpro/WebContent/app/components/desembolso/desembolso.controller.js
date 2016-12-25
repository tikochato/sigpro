var app = angular.module('desembolsoController', []);

app.controller('desembolsoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal) {
			var mi=this;
			
			$window.document.title = 'SIGPRO - Desembolso';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.desembolsos = [];
			mi.desembolso;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			mi.totalDesembolsos = 0;
			mi.paginaActual = 1;
			mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
			mi.elementosPorPagina = $utilidades.elementosPorPagina;
			mi.desembolsotipoid;
			mi.desembolsonombre;
			mi.proyectoid;
			mi.fecha = new Date();
			
			mi.gridOptions = {
				enableRowSelection : true,
				enableRowHeaderSelection : false,
				multiSelect: false,
				modifierKeysToMultiSelect: false,
				noUnselect: true,
				enableFiltering: true,
				enablePaginationControls: false,
			    paginationPageSize: $utilidades.elementosPorPagina,
				columnDefs : [ 
					{ name: 'id', width: 65, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'fecha', width: 100, displayName: 'Fecha', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
					{ name: 'monto', width: 100, displayName: 'Monto', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
					{ name: 'proyecto', width: 200, displayName: 'Proyecto',cellClass: 'grid-align-left' },
					{ name: 'desembolsotipo', width: 200, displayName: 'Tipo Desembolso',cellClass: 'grid-align-left' },
					{ name: 'tipocambio', width: 100, displayName: 'Tipo Cambio', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'usuarioCreo', displayName: 'Usuario Creación'},
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.desembolso = row.entity;
					});
					
					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'desembolsos', t: (new Date()).getTime()}).then(function(response){
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
			
			mi.opcionesFecha = {
				    formatYear: 'yyyy',
				    maxDate: new Date(2020, 5, 22),
				    minDate : new Date(1900, 1, 1),
				    startingDay: 1
				  };
			
			 mi.popup = {
					 abierto: false
			 };
			
			mi.cargarTabla = function(pagina){
				mi.mostrarcargando=true;
				$http.post('/SDesembolso', { accion: 'getDesembolsosPagina', pagina: pagina, numerodesembolsos: $utilidades.elementosPorPagina }).success(
						function(response) {
							mi.desembolsos = response.desembolsos;
							mi.gridOptions.data = mi.desembolsos;
							mi.desembolso = null;
							mi.mostrarcargando = false;
						});
			};
			
			mi.guardar=function(){
				if(mi.desembolso!=null && mi.desembolso.fecha!='' && mi.desembolso.monto!=''
					&& mi.desembolso.tipocambio!='' && mi.proyectoid!='' && mi.desembolsotipoid!=''){
					$http.post('/SDesembolso', {
						accion: 'guardarDesembolso',
						esnuevo: mi.esnuevo,
						id: mi.desembolso.id,
						fecha: moment(mi.fecha).format('DD/MM/YYYY'),
						monto: mi.desembolso.monto,
						tipocambio : mi.desembolso.tipocambio,
						proyectoid : 1,
						desembolsotipoid : mi.desembolsotipoid
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Desembolso '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
							mi.desembolso.id = response.id;
							mi.esnuevo = false;
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Desembolso');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};
			
			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.desembolso = null;
				mi.gridApi.selection.clearSelectedRows();
			};
			
			mi.borrar = function(ev) {
				if(mi.desembolso!=null){
					var confirm = $mdDialog.confirm()
				          .title('Confirmación de borrado')
				          .textContent('¿Desea borrar el Desembolso ?')
				          .ariaLabel('Confirmación de borrado')
				          .targetEvent(ev)
				          .ok('Borrar')
				          .cancel('Cancelar');
	
				    $mdDialog.show(confirm).then(function() {
				    	$http.post('/SDesembolso', {
							accion: 'borrarDesembolso',
							id: mi.desembolso.id
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Desembolso borrado con éxito');
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Desembolso');
						});
				    }, function() {
				    
				    });
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Desembolso que desea borrar');
			};
			
			mi.editar = function() {
				if(mi.desembolso!=null){
					mi.fecha = moment(mi.desembolso.fecha, 'DD/MM/YYYY').toDate();
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar el Desembolso que desea editar');
			};

			
			$http.post('/SDesembolso', { accion: 'numeroDesembolsos' }).success(
					function(response) {
						mi.totalDesembolsos = response.totaldesembolsos;
						mi.cargarTabla(1);
			});
			
			mi.mostrarCalendar = function() {
			    mi.popup.abierto = true;
			  };
			  
			mi.irATabla = function() {
				mi.mostraringreso=false;
			};
			
			mi.guardarEstado=function(){
				var estado = mi.gridApi.saveState.save();
				var tabla_data = { action: 'guardaEstado', grid:'desembolso', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
				$http.post('/SEstadoTabla', tabla_data).then(function(response){
					
				});
			};
			
			mi.reiniciarVista=function(){
				if($location.path()=='/desembolso/rv')
					$route.reload();
				else
					$location.path('/desembolso/rv');
			}
			
			mi.buscarTipoDesembolso = function(titulo, mensaje) {
				var instanciaModal = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'buscarDesembolsoTipo.jsp',
					controller : 'modalBuscarDesembolsoTipo',
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

				instanciaModal.result.then(function(selectedItem) {
					
					mi.desembolsotipoid = selectedItem.id;
					mi.desembolsonombre = selectedItem.nombre;

				}, function() {
				});
			};

			
} ]);


app.controller('modalBuscarDesembolsoTipo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'mensaje', modalBuscarDesembolsoTipo ]);

function modalBuscarDesembolsoTipo($uibModalInstance, $scope, $http, $interval,
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
	
	$http.post('/SDesembolsoTipo', {
		accion : 'numeroDesembolsoTipo'
	}).success(function(response) {
		mi.totalElementos = response.total;
		mi.elementosPorPagina = mi.totalElementos;
		mi.cargarData(1);
	});
	
	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID', name : 'id', cellClass : 'grid-align-right',
		}, {
			displayName : 'Tipo desembolso',
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
					mi.seleccionarDesembolsoTipo);
		}
	}
	
	mi.seleccionarDesembolsoTipo = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};
	
	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post('/SDesembolsoTipo', {accion : 'getDesembolsotiposPagina'}).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.desembolsotipos;
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
			$utilidades.mensaje('warning', 'Debe seleccionar una ENTIDAD');
		}
	};
	
	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};

}