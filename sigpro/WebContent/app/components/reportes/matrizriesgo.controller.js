var app = angular.module('matrizriesgoController', []);


app.controller('matrizriesgoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	
	$window.document.title = $utilidades.sistema_nombre+' - Matriz Riesgos';
	i18nService.setCurrentLang('es');
	 
	 
	 $http.post('/SMatrizRiesgo', { accion: 'getMatrizRiesgos', proyectoid:'13', t: (new Date()).getTime()})
	 .then(function(response){
		 mi.lista = response.data.matrizriesgos;
		 mi.riesgos = [].concat(mi.lista);
	});	
	 
	 mi.exportarExcel = function(){
			$http.post('/SMatrizRiesgo', { accion: 'exportarExcel', proyectoid:'13',t:moment().unix(),
				  } ).then(
					function(response) {
						
						
			});
		};
	
	
}]);



		