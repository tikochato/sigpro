var app = angular.module('agendaController', []);


app.controller('agendaController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope,$rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	mi.proyectoid = "";
	mi.proyectoNombre = "";
	mi.objetoTipoNombre = "";
	mi.mostrarcargando = true;
	mi.proyectoId = [];
	mi.proyectoId.value = $routeParams.proyectoId;
	mi.mostrarTabla = false; 
	
	$window.document.title = $utilidades.sistema_nombre+' - Agenda de Actividades';
	i18nService.setCurrentLang('es');
	 
	
	
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				mi.prestamos.push({'value' : 0, 'text' : 'Seleccione un '+$rootScope.etiquetas.proyecto});
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
					
					if ($routeParams.proyectoId!=null && $routeParams.proyectoId != undefined){
						for (x in mi.prestamos){
							if (mi.prestamos[x].value == $routeParams.proyectoId){
								mi.proyectoId = mi.prestamos[x];
								mi.cargarAgenda();
								break;
							}
						}
					}else{
						mi.prestamo = mi.prestamos[0];
					}
				}
		});
	 
	mi.cargarAgenda = function (){
		mi.agenda = [];
		mi.lista = [];
		 mi.mostrarTabla=false;
		 $http.post('/SAgenda', { accion: 'getAgenda', proyectoid:mi.proyectoId.value, t: (new Date()).getTime()})
		 .then(function(response){
			 mi.lista = response.data.agenda;
			 mi.agenda = [].concat(mi.lista);
			 var tab = "\t";
			 for (x in mi.agenda){
				 mi.agenda[x].nombre = tab.repeat(mi.agenda[x].nivel) + mi.agenda[x].nombre; 
			 }
			 mi.mostrarcargando = false;
			 if(mi.lista.length > 0)
				 mi.mostrarTabla=true;
		});	
	};
	 
	 mi.exportarExcel = function(){
			$http.post('/SAgenda', { accion: 'exportarExcel', proyectoid:$routeParams.proyectoId,t:moment().unix()
				  } ).then(
						  function successCallback(response) {
								var anchor = angular.element('<a/>');
							    anchor.attr({
							         href: 'data:application/ms-excel;base64,' + response.data,
							         target: '_blank',
							         download: 'Agenda.xls'
							     })[0].click();
							  }.bind(this), function errorCallback(response){
							 		
							 	}
							 );
		};
		
		 mi.claseIcon = function (objetoTipo) {
			   
			    switch (objetoTipo) {
			        case 0:
			            return 'glyphicon glyphicon-record';
			        case 1:
			            return 'glyphicon glyphicon-th';
			        case 2:
			            return 'glyphicon glyphicon-equalizer';
			        case 3:
			            return 'glyphicon glyphicon-certificate';
			        case 4:
			            return 'glyphicon glyphicon-link';
			        case 5:
			            return 'glyphicon glyphicon-time';
			    }
			   
			};
	
	
}]);



		