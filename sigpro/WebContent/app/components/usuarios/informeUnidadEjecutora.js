var app = angular.module('informeUnidadController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'informeUnidadController',
 [
	 '$scope',
	  '$rootScope',
	  '$http',
	  '$interval',
	  '$q',
	  'i18nService',
	  'Utilidades',
	  '$routeParams',
	  'uiGridConstants',
	  '$mdDialog',
	  '$window',
	  '$location',
	  '$route',
	  '$q',
	  '$uibModal',
	  'dialogoConfirmacion', 
  function($scope, $rootScope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal, $dialogoConfirmacion) {
	var mi=this;
	$window.document.title =$utilidades.sistema_nombre+' - Informe Unidad Ejecutora';
	i18nService.setCurrentLang('es');
	mi.edicion=false;
	mi.unidadEjecutora={id:0,nombre:""};	
	mi.usuarioActual={usuario:"", email:""};
	mi.mostrarCargandoUno=false;
	mi.mostrarCargandoDos=false;
	mi.roles={administrador:"",dcp:"",cooperante:"",unidadEjecutora:"",planificador:""};
	mi.prestamos=[];
	mi.buscarUnidadEjecutora = function(titulo, mensaje) {
		mi.roles={administrador:"",dcp:"",cooperante:"",unidadEjecutora:"",planificador:""};
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarUnidadEjecutora.jsp',
			controller : 'modalBuscarUnidadEjecutora',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md'
		});

		modalInstance.result.then(function(selectedItem) {
			mi.unidadEjecutora.nombre = selectedItem.nombreUnidadEjecutora;
			mi.unidadEjecutora.id = selectedItem.unidadEjecutora;
			cargarPrestamos();

		}, function() {
		});
	};
	
	function cargarPrestamos(){
		mi.mostrarCargandouno = true;
		var datos={
				accion:"getPrestamosPorElemento",
				tipo:4,
				id:mi.unidadEjecutora.id
		}
		$http.post('/SUsuario', datos).then(function(response) {
			if (response.data.success) {
				mi.mostrarCargandouno = false;
				mi.prestamos=response.data.prestamos;
			}
		});
	}
	mi.verUsuarios=function(index,row){
		mi.roles={administrador:"",dcp:"",cooperante:"",unidadEjecutora:"",planificador:""};
		var datos={
				accion:"getUsuarioPorPrestamo",
				proyecto:row.id
		}
		$http.post('/SUsuario', datos).then(function(response) {
			if (response.data.success) {
				var carga =response.data.usuarios;
				for(var i=0;i<carga.length;i++){
					if(carga[i]["id"]==2){
						mi.roles.administrador=carga[i].usuario;
					}else if(carga[i]["id"]==3){
						mi.roles.dcp=carga[i].usuario;
					}else if(carga[i]["id"]==4){
						mi.roles.unidadEjecutora=carga[i].usuario;
					}else if(carga[i]["id"]==5){
						mi.roles.planificador=carga[i].usuario;
					}
					else if(carga[i]["id"]==6){
						mi.roles.cooperante=carga[i].usuario;
					}
				}
			}
		});
	}
	
	
	
	} 
]);

app.controller('modalBuscarUnidadEjecutora', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log', modalBuscarUnidadEjecutora ]);

function modalBuscarUnidadEjecutora($uibModalInstance, $scope, $http,
	$interval, i18nService, $utilidades, $timeout, $log) {

var mi = this;

mi.totalElementos = 0;
mi.paginaActual = 1;
mi.numeroMaximoPaginas = 5;
mi.elementosPorPagina = 9;

mi.mostrarCargando = false;
mi.data = [];

mi.itemSeleccionado = null;
mi.seleccionado = false;

$http.post('/SUnidadEjecutora', {
	accion : 'totalEntidades'
}).success(function(response) {
	mi.totalElementos = response.total;
	mi.elementosPorPagina = mi.totalElementos;
	mi.cargarData(1);
});

mi.opcionesGrid = {
	data : mi.data,
	columnDefs : [ {
		displayName : 'Unidad Ejecutora',
		name : 'unidadEjecutora',
		cellClass : 'grid-align-right',
		type : 'number',
		width: 120
	}, {
		displayName : 'Nombre Unidad',
		name : 'nombreUnidadEjecutora',
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
	$http.post('/SUnidadEjecutora', datos).then(function(response) {
		if (response.data.success) {
			mi.data = response.data.unidadesEjecutoras;
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
		$utilidades.mensaje('warning',
				'Debe seleccionar una UNIDAD EJECUTORA');
	}
};

mi.cancel = function() {
	$uibModalInstance.dismiss('cancel');
};

}
