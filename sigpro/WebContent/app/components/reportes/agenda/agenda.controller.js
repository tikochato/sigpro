var app = angular.module('agendaController', []);


app.controller('agendaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	
	$window.document.title = $utilidades.sistema_nombre+' - Agenda de Actividades';
	i18nService.setCurrentLang('es');
	 
	 
	 $http.post('/SAgenda', { accion: 'getAgenda', proyectoid:'13', t: (new Date()).getTime()})
	 .then(function(response){
		 mi.lista = response.data.agenda;
		 mi.agenda = [].concat(mi.lista);
		 var tab = "\t";
		 for (x in mi.agenda){
			 mi.agenda[x].nombre = tab.repeat(mi.agenda[x].objetoTipo -1) + mi.agenda[x].nombre; 
		 }
	});	
	 
	 mi.exportarExcel = function(){
			$http.post('/SAgenda', { accion: 'exportarExcel', proyectoid:'13',t:moment().unix(), responseType: 'arraybuffer'
				  } ).then(
					function(response, status, headers, config) {
			            var blob = new Blob([response.data], {type: "application/vnd.ms-excel;base64"});
			            var objectUrl = URL.createObjectURL(blob);
			            window.open(objectUrl);
						
						
						
						/*
						var anchor = angular.element('<a/>');
					    anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'Cuotas_COPEP.xls'
					     })[0].click(); */
						

						
			});
		};
	
	
}]);



		