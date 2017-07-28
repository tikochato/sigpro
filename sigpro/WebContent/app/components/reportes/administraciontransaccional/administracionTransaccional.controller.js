var app = angular.module('administracionTransaccionalController',['ngTouch']);
app.controller('administracionTransaccionalController',['$scope', '$http', '$interval','Utilidades','i18nService',
	function($scope, $http, $interval, $utilidades,i18nService){
		var mi = this;
		mi.paginaActual = 1;
		mi.totalPrestamo = 0;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.prestamo = mi.prestamos[0];
		
		$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				mi.prestamos.push({'value' : 0, 'text' : 'Seleccione una opción'});
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
					mi.prestamo = mi.prestamos[0];
				}
			});
		
		mi.generar = function(){
			if(mi.prestamo.value != 0){
				mi.mostrarcargando = true;
				$http.post('/SAdministracionTransaccional', {accion: 'getComponentes', proyectoId: mi.prestamo.value}).success(
					function(response){
						mi.gridOptions.data = response.componentes;
						mi.mostrarcargando = false;
						mi.totalPrestamo = response.componentes.length;
					});
			}
		}
		
		mi.gridOptions = {
			enableRowSelection : true,
			enableRowHeaderSelection : false,
			enableColumnMenus: false,
			enablePaginationControls: true,
		    enablePaginationControls: false,
		    paginationPageSize: 50,
			useExternalFiltering: true,
			enableSorting: false,
			headerCellClass: 'text-center',
			columnDefs : [
				{name: 'nombre', displayName: 'Nombre', width: 300, cellClass: 'grid-align-left'},
				{name: 'nombre_objeto_tipo', displayName: 'Tipo objeto'},
				{name: 'usuario_creo', displayName: 'Usuario creó'},
				{name: 'usuario_actualizo', displayName: 'Usuario actualizó'},
				{name: 'fecha_creacion', displayName: 'Fecha creación'},
				{name: 'fecha_modificacion', displayName: 'Fecha modificación'},
				{name: 'estado', displayName: 'Estado'}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi = gridApi;
			}
		}
		
		mi.gridOptions.data = [];
}]);