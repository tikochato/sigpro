var app = angular.module('formularioController', [ 'ngTouch']);

app.controller('formularioController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal) {
		var mi=this;

		$window.document.title = $utilidades.sistema_nombre+' - Formulario';
		i18nService.setCurrentLang('es');
		mi.mostrarcargando=true;
		mi.formularios = [];
		mi.formularios;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.totalFormularios = 0;
		mi.paginaActual = 1;
		mi.formulariotipoid="";
		mi.formulariotiponombre="";
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;


		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp';
			$utilidades.mensaje('primary','No tienes permiso de acceder a esta área');			
		}
		mi.formularioitemtipos =[];
		mi.formularioitemtipo =null;
		mi.mostrarcargandoFormItem=true;
		mi.mostraritem = false;
		mi.paginaActualItems=1;

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
					{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
				    { name: 'codigo', width: 200, displayName: 'Codigo',cellClass: 'grid-align-left' },
				    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
				    { name: 'usuarioCreo', displayName: 'Usuario Creación'},
				    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				    { name: 'formulariotiponomre', displayName: 'Nombre Tipo Formulario', cellClass: 'grid-align-left', enableFiltering: false}
				],
				onRegisterApi: function(gridApi) {
					mi.gridApi = gridApi;
					gridApi.selection.on.rowSelectionChanged($scope,function(row) {
						mi.formularios = row.entity;
					});

					if($routeParams.reiniciar_vista=='rv'){
						mi.guardarEstado();
				    }
				    else{
				    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'formularios', t: (new Date()).getTime()}).then(function(response){
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

		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SFormulario', { accion: 'getFormularioPagina', pagina: pagina, numeroformularioss: $utilidades.elementosPorPagina }).success(
					function(response) {
						mi.formularios = response.formularios;
						mi.gridOptions.data = mi.formularios;
						mi.mostrarcargando = false;
					});
		}

		mi.guardar=function(){
			if(mi.formulario!=null  && mi.formulario.codigo!='' && mi.formulariotipoid != ''){
				var idspropiedad="";
				for (i = 0 ; i<mi.formularioitemtipos.length ; i ++){
					if (i==0){
						idspropiedad = idspropiedad.concat("",mi.formularioitemtipos[i].id);
					}else{
						idspropiedad = idspropiedad.concat(",",mi.formularioitemtipos[i].id);
					}
				}

				$http.post('/SFormulario', {
					accion: 'guardarFormulario',
					esnuevo: mi.esnuevo,
					id: mi.formulario.id,
					codigo: mi.formulario.codigo,
					descripcion: mi.formulario.descripcion,
					formulariotipoid : mi.formulariotipoid,
					items: idspropiedad
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Formulario '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.esnuevo = false;
						mi.formularios.id = response.id;
						mi.cargarTabla();

					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'crear' : 'guardar')+' el Formulario');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.editar = function() {
			if(mi.formularios!=null){
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.cargarTotalPropiedades();
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Formulario que desea editar');
		}


		mi.borrar = function(ev) {
			if(mi.formularios!=null){
				var confirm = $mdDialog.confirm()
			          .title('Confirmación de borrado')
			          .textContent('¿Desea borrar el Formulario "'+mi.formularios.nombre+'"?')
			          .ariaLabel('Confirmación de borrado')
			          .targetEvent(ev)
			          .ok('Borrar')
			          .cancel('Cancelar');

			    $mdDialog.show(confirm).then(function() {
			    	$http.post('/SFormulario', {
						accion: 'borrarProyectoTipo',
						id: mi.formularios.id
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Tipo Proyecto borrado con éxito');
							mi.cargarTabla();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Tipo Proyecto');
					});
			    }, function() {

			    });
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Tipo Proyecto que desea borrar');
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.formularios = null;
			mi.gridApi.selection.clearSelectedRows();
			mi.cargarTotalPropiedades();
		};

		mi.irATabla = function() {
			mi.mostraringreso=false;
		}

		mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'proyectotipos', estado: JSON.stringify(estado), t: (new Date()).getTime() };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
		}

		mi.cambioPagina=function(){
			mi.cargarTabla(mi.paginaActual);
		}

		mi.reiniciarVista=function(){
			if($location.path()=='/proyectotipo/rv')
				$route.reload();
			else
				$location.path('/proyectotipo/rv');
		}

		$http.post('/SProyectoTipo', { accion: 'numeroProyectoTipos' }).success(
				function(response) {
					mi.totalFormularios = response.totalproyectotipos;
					mi.cargarTabla(1);
				}
		);
		//----
		mi.gridOptionsProyectoPropiedad = {
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
						mi.formularioitemtipo = row.entity;
					});
				}
		};

		mi.cargarTablaPropiedades = function(pagina){

			mi.mostrarcargandoFormItem=true;
			$http.post('/SProyectoPropiedad',
					{
						accion: 'getProyectoPropiedadPaginaPorTipoProy',
						pagina: pagina,
						idProyectoTipo:mi.formularios!=null ? mi.formularios.id : null,
						numeroproyectopropiedad: $utilidades.elementosPorPagina }).success(
				function(response) {

					mi.formularioitemtipos = response.proyectopropiedades;
					mi.gridOptionsProyectoPropiedad.data = mi.formularioitemtipos;
					mi.mostrarcargandoFormItem = false;
					mi.mostraritem = true;
				});

		}


		mi.cargarTotalPropiedades = function(){
			$http.post('/SProyectoPropiedad', { accion: 'numeroProyectoPropiedades' }).success(
					function(response) {
						mi.totalProyectopropiedades = response.totalproyectopropiedades;
						mi.cargarTablaPropiedades(mi.paginaActualItems);
					}
			);
		}

		mi.eliminarPropiedad = function(){
			if (mi.formularioitemtipo != null){
				for (i = 0 ; i<mi.formularioitemtipos.length ; i ++){
					if (mi.formularioitemtipos[i].id === mi.formularioitemtipo.id){
						mi.formularioitemtipos.splice (i,1);
						break;
					}
				}
				mi.formularioitemtipo = null;
			}else{
				$utilidades.mensaje('warning','Debe seleccionar la Propiedad que desea eliminar');
			}
		}

		mi.eliminarPropiedad2 = function(row){
			var index = mi.formularioitemtipos.indexOf(row);
	        if (index !== -1) {
	            mi.formularioitemtipos.splice(index, 1);
	        }
		}

		mi.seleccionTabla = function(row){
			if (mi.formularioitemtipo !=null && mi.formularioitemtipo.id == row.id){
				mi.formularioitemtipo = null;
			}else{
				mi.formularioitemtipo = row;
			}
		};
		
		mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarPorFormulario.jsp',
				controller : 'buscarPorFormulario',
				controllerAs : 'modalBuscar',
				backdrop : 'static',
				size : 'md',
				resolve : {
					$servlet : function() {
						return servlet;
					},
					$accionServlet : function() {
						return accionServlet;
					},
					$datosCarga : function() {
						return datosCarga;
					},
					$columnaId : function() {
						return columnaId;
					},
					$columnaNombre : function() {
						return columnaNombre;
					}
				}
			});

			modalInstance.result.then(function(itemSeleccionado) {
				resultado.resolve(itemSeleccionado);
			});
			return resultado.promise;
		};

		
		mi.buscarFormularioTipo = function() {
			var resultado = mi.llamarModalBusqueda('/SFormularioTipo', {
				accion : 'numeroFormularioTipos'
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getFormulariotiposPagina',
					pagina : pagina,
					registros : elementosPorPagina
				};
			},'id','nombre');

			resultado.then(function(itemSeleccionado) {
				mi.formulariotipoid= itemSeleccionado.id;
				mi.formulariotiponombre = itemSeleccionado.nombre;
			});
		};
		
		mi.buscarFormularioItem = function() {
			var resultado = mi.llamarModalBusqueda('/SFormularioItemTipo', {
				accion : 'numeroFormularioItemTipos'
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getFormularioItemtiposPagina',
					pagina : pagina,
					registros : elementosPorPagina
				};
			},'id','nombre');

			resultado.then(function(itemSeleccionado) {
				mi.formularioitemtipos.push(itemSeleccionado);
			});
		};
		
		
		
}]);

app.controller('buscarPorFormulario', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre',buscarPorFormulario ]);

function buscarPorFormulario($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga,$columnaId,$columnaNombre) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post($servlet, $accionServlet).success(function(response) {
		for ( var key in response) {
			mi.totalElementos = response[key];
		}
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : $columnaId,
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Nombre',
			name : $columnaNombre,
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
					mi.seleccionarTipoRiesgo);
		}
	}

	mi.seleccionarTipoRiesgo = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina)).then(
				function(response) {
					if (response.data.success) {

						for ( var key in response.data) {
							if (key != 'success')
								mi.data = response.data[key];
						}
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
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}

