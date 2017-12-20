app.controller('desembolsoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', 'dialogoConfirmacion',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Desembolso';
			i18nService.setCurrentLang('es');
			
			$scope.desembolsos = [];
			mi.desembolso;
			mi.esnuevo = false;
			mi.desembolsotipoid;
			mi.desembolsonombre;
			mi.objetoTipoNombre=""
			mi.proyectoid = $scope.$parent.controller.proyecto.id;
			mi.montoPorDesembolsar = $scope.$parent.controller.montoPorDesembolsar;
			mi.fechaCierreActual = $scope.$parent.controller.fechaCierreActualUe;
			mi.desembolsoAFechaUsd = $scope.$parent.controller.desembolsoAFechaUsd;
			mi.totalDesembolsos = 0;
			mi.fecha = new Date();
			mi.formatofecha = 'dd/MM/yyyy';
			mi.altformatofecha = ['d!/M!/yyyy'];
			mi.desembolsosValidos=true;
			mi.desembolsosRealesOpen = false;
			
			mi.desembolsosReales = [];
			mi.totalRealUsd = 0;
			mi.totalRealGtq = 0;
			
			mi.mostrarcargando=false;
			mi.congelado = 0;
			
			mi.opcionesFecha = {
				    formatYear: 'yyyy',
				    maxDate: moment(mi.fechaCierreActual,'DD/MM/YYYY').toDate(),
				    startingDay: 1
				  };
			
			 mi.popup = {
					 abierto: false
			 };
			 
			$scope.$parent.controller.child_desembolso = $scope.desembolsoc;
			mi.congelado = $scope.$parent.controller.congelado;
			
			mi.cargarTabla = function(){
				mi.mostrarcargando=true;
				$http.post('/SDesembolso', { accion: 'getDesembolsosPorProyecto', 
					proyectoid: mi.proyectoid, t: (new Date()).getTime()
					}).success(
						function(response) {
							$scope.desembolsos = response.desembolsos;
							for(x in $scope.desembolsos){
								$scope.desembolsos[x].fecha = moment($scope.desembolsos[x].fecha,'DD/MM/YYYY').toDate();
							}
							mi.tipo_moneda_nombre = response.tipoMonedaNombre;
							mi.tipo_moneda = response.tipoMonedaId;
							mi.opcionesFecha.minDate = moment(response.fechaActual, 'DD/MM/YYYY').toDate();
							mi.mostrarcargando = false;
						});
				
				mi.mostrarcargando=true;
				$http.post('/SDesembolso', { accion: 'getDesembolsosReales', 
					proyectoid: mi.proyectoid, t: (new Date()).getTime()
					}).success(
						function(response) {
							mi.desembolsosReales = response.desembolsos;
							mi.totalRealUsd = 0;
							mi.totalRealGtq = 0;
							for (x in mi.desembolsosReales){
								mi.totalRealUsd = mi.totalRealUsd + mi.desembolsosReales[x].desembolsosMesUsd;
								mi.totalRealGtq = mi.totalRealGtq + mi.desembolsosReales[x].desembolsosMesGtq;	
							}
						});
			};
			
			mi.cargarTabla();
			
			mi.guardar=function(mensaje_success, mensaje_error,call_chain){
				if($scope.desembolsos!=null && mi.proyectoid!=''){
					var desembolsos = '';
					for(var i=0; i<$scope.desembolsos.length; i++){
						desembolsos = desembolsos + "," + JSON.stringify($scope.desembolsos[i]);
					}
					$http.post('/SDesembolso', {
						accion: 'guardarDesembolsos',
						desembolsos: desembolsos.substring(1),
						proyectoid: mi.proyectoid,
						t: (new Date()).getTime()
					}).success(function(response){
						if(response.success){
							if(call_chain!=null)
								call_chain(mensaje_success, mensaje_error);
							else{
								$utilidades.mensaje('success',mensaje_success);
								if($scope.$parent.controller)
									$scope.$parent.controller.botones=true;
							}
						}
						else{
							$utilidades.mensaje('danger',mensaje_error);
							if($scope.$parent.controller)
								$scope.$parent.controller.botones=true;
						}
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};
			
			mi.nuevo = function() {
				$scope.desembolsos.push({
					id: -1,
					monto: 0,
					fecha: null,
					tipo_moneda: mi.tipo_moneda
				});
			};
			
			mi.mostrarCalendar = function(index) {
			    $scope.desembolsos[index].c_abierto = true;
			  };
			  
			mi.irATabla = function() {
				mi.mostraringreso=false;
				mi.esnuevo=false;
			};
			
			mi.filtrar = function(evt){
				if(evt.keyCode==13){
					
				}
			};

			mi.buscarTipoMoneda = function(titulo, mensaje, posicion) {
				var instanciaModal = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'buscarTipoMoneda.jsp',
					controller : 'modalBuscarTipoMoneda',
					controllerAs : 'modalBuscar',
					backdrop : 'static',
					size : 'md',
					resolve : {
						titulo : function() {
							return titulo;
						},
						mensaje : function() {
							return mensaje;
						},
						posicion: function() {
							return posicion;
						}
					}
				});

				instanciaModal.result.then(function(selectedItem) {
					$scope.desembolsos[selectedItem.posicion].tipo_moneda=selectedItem.id;
					$scope.desembolsos[selectedItem.posicion].tipo_moneda_nombre=selectedItem.nombre;
				}, function() {
				});
			};
			
			mi.borrar = function(row) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Desembolso?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var index = $scope.desembolsos.indexOf(row);
				        if (index !== -1) {
				            $scope.desembolsos.splice(index, 1);
				        }
					}
				}, function(){

				});
		    }
			
			$scope.$watch('desembolsos', function() {
			    var total = 0;
			        mi.totalDesembolsos = $scope.desembolsos.reduce(function(total,item) {
			       	 	 	return total + item.monto;
			        },0);
			}, true);
			
			mi.validarMonto = function(row){
				var total = 0;
				for(x in $scope.desembolsos){
					total += $scope.desembolsos[x].monto;
					if(total >= mi.montoPorDesembolsar){
						if(mi.desembolsosValidos == true){
							$utilidades.mensaje('warning','Los desembolsos sobrepasan el Monto por desembolsar');
							mi.desembolsosValidos = false;
						}
						return true;
					}
				}
				
				return false;
			}
			
			for(x in $scope.desembolsos){
				total += $scope.desembolsos[x].monto;
				if(total >= mi.montoPorDesembolsar){
					if(mi.desembolsosValidos == true){
						$utilidades.mensaje('warning','Los desembolsos sobrepasan el Monto por desembolsar');
						mi.desembolsosValidos = false;
					}
					return true;
				}
			}
} ]);



app.controller('modalBuscarTipoMoneda', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'mensaje','posicion', modalBuscarTipoMoneda ]);

function modalBuscarTipoMoneda($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, titulo, mensaje, posicion) {

	var mi = this;
	
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	mi.posicion = posicion;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
	mi.mostrarCargando = true;
	$http.post('/STipoMoneda', {accion : 'getTipoMonedas', t: new Date().getTime()}).then(function(response) {
		if (response.data.success) {
			mi.data = response.data.tipoMonedas;
			mi.opcionesGrid.data = mi.data;
			mi.mostrarCargando = false;
		}
	});
	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID', width: 100, name : 'id', cellClass : 'grid-align-right',
		}, {
			displayName : 'Tipo Moneda',
			name : 'nombre',
			cellClass : 'grid-align-left'
		} , {
			displayName : 'Símbolo',
			name : 'simbolo',
			cellClass : 'grid-align-left'
		}],
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
	
	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}
	
	mi.ok = function() {
		if (mi.seleccionado) {
			mi.itemSeleccionado.posicion = mi.posicion;
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un Tipo de Moneda');
		}
	};
	
	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}