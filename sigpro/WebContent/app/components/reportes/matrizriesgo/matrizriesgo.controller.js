var app = angular.module('matrizriesgoController', ['smart-table']);


app.controller('matrizriesgoController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {

	var mi=this;
	mi.proyectoId=[];
	mi.proyectoId.value = $routeParams.proyectoId;
	mi.proyectoNombre = ""; 
	mi.objetoTipoNombre = "";
	mi.mostrarTabla = false;
	
		
	$window.document.title = $utilidades.sistema_nombre+' - Matriz Riesgos';
	i18nService.setCurrentLang('es');


	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				mi.prestamos.push({'value' : 0, 'text' : 'Seleccione un proyecto'});
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
					
					if ($routeParams.proyectoId!=null && $routeParams.proyectoId != undefined){
						for (x in mi.prestamos){
							if (mi.prestamos[x].value == $routeParams.proyectoId){
								mi.proyectoId = mi.prestamos[x];
								mi.cargarMatriz();
								break;
							}
						}
					}else{
						mi.prestamo = mi.prestamos[0];
					}
				}
		});

	
	mi.cargarMatriz = function(){
			mi.lista = [];
			mi.riesgos=[];
			mi.mostrarTabla = false;
			 $http.post('/SMatrizRiesgo', { accion: 'getMatrizRiesgos', proyectoid:mi.proyectoId.value, t: (new Date()).getTime()})
			 .then(function(response){
				 mi.lista = response.data.matrizriesgos;
				 mi.riesgos = [].concat(mi.lista);
				 if (mi.lista.length > 0){
					 mi.mostrarTabla = true;
				 }else{
					 mi.mostrarTabla = false;
					 if (mi.proyectoId.value>0)
					 $utilidades.mensaje('warning','No se encontraron datos para el '+$rootScope.etiquetas.proyecto);
				 }
			});
		}

	 mi.exportarExcel = function(){
			$http.post('/SMatrizRiesgo', { accion: 'exportarExcel', proyectoid:$routeParams.proyectoId,t:moment().unix(),
				  } ).then(
						  function successCallback(response) {
								var anchor = angular.element('<a/>');
							    anchor.attr({
							         href: 'data:application/ms-excel;base64,' + response.data,
							         target: '_blank',
							         download: 'Matriz_Riesgos.xls'
							     })[0].click();
							  }.bind(this), function errorCallback(response){
							 		
							 	}
						  
					);
		};
}]);



		