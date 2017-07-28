var app = angular.module('administracionTransaccionalController',['ngTouch']);
app.controller('administracionTransaccionalController',['$scope', '$http', '$interval','Utilidades','i18nService',
	function($scope, $http, $interval, $utilidades,i18nService){
		var mi = this;
		
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
				$http.post('/SAdministracionTransaccional', {accion: 'getComponentes', proyectoId: mi.prestamo.value}).success(
					function(response){
						mi.rowCollectionObjetos = [];
						mi.rowCollectionObjetos = response.componentes;
						mi.displayedCollectionObjetos = [].concat(mi.rowCollectionObjetos);
					});
			}
		}
}]);