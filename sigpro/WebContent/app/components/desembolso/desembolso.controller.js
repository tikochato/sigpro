app.controller('desembolsoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', 'dialogoConfirmacion',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Desembolso';
			i18nService.setCurrentLang('es');
			
			mi.desembolsos = [];
			mi.desembolso;
			mi.esnuevo = false;
			mi.desembolsotipoid;
			mi.desembolsonombre;
			mi.objetoTipoNombre=""
			mi.proyectoid = $scope.$parent.controller.proyecto.id;
			mi.fecha = new Date();
			mi.formatofecha = 'dd/MM/yyyy';
			
			mi.mostrarcargando=false;
			
			
			mi.opcionesFecha = {
				    formatYear: 'yyyy',
				    maxDate: new Date(2050, 12, 31),
				    minDate : new Date(1990, 1, 1),
				    startingDay: 1
				  };
			
			 mi.popup = {
					 abierto: false
			 };
			 
			 $scope.$parent.controller.child_desembolso = $scope.desembolsoc;
			
			mi.cargarTabla = function(){
				mi.mostrarcargando=true;
				$http.post('/SDesembolso', { accion: 'getDesembolsosPorProyecto', 
					proyectoid: mi.proyectoid, t: (new Date()).getTime()
					}).success(
						function(response) {
							mi.desembolsos = response.desembolsos;
							for(x in mi.desembolsos){
								mi.desembolsos[x].fecha = moment(mi.desembolsos[x].fecha,'DD/MM/YYYY').toDate();
							}
							
							mi.tipo_moneda_nombre = response.tipoMonedaNombre;
							mi.tipo_moneda = response.tipoMonedaId;
							mi.mostrarcargando = false;
						});
			};
			
			mi.cargarTabla();
			
			mi.guardar=function(mensaje_success, mensaje_error,call_chain){
				if(mi.desembolsos!=null && mi.proyectoid!=''){
					var desembolsos = '';
					for(var i=0; i<mi.desembolsos.length; i++){
						desembolsos = desembolsos + "," + JSON.stringify(mi.desembolsos[i]);
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
				mi.desembolsos.push({
					id: -1,
					monto: 0,
					fecha: null,
					tipo_moneda: mi.tipo_moneda
				});
			};
			
			mi.mostrarCalendar = function(index) {
			    mi.desembolsos[index].c_abierto = true;
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
					mi.desembolsos[selectedItem.posicion].tipo_moneda=selectedItem.id;
					mi.desembolsos[selectedItem.posicion].tipo_moneda_nombre=selectedItem.nombre;
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
						var index = mi.desembolsos.indexOf(row);
				        if (index !== -1) {
				            mi.desembolsos.splice(index, 1);
				        }
					}
				}, function(){

				});
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