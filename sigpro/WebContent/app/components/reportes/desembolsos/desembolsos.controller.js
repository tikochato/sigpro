var app = angular.module('desembolsosController', []);

app.controller('desembolsosController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	mi.proyectoid = "";
	mi.proyectoNombre = "";
	mi.objetoTipoNombre = "";
	mi.formatofecha = 'yyyy';
	mi.mostrar = false;
	mi.tabla = {};
	mi.anioFiscal = "";
	mi.mesReportado = "";
	mi.prestamo = {};
	
	mi.fechaOptions = {
			datepickerMode:"year",
			  minMode:"year",
	};
	
	$window.document.title = $utilidades.sistema_nombre+' - Desembolsos';
	i18nService.setCurrentLang('es');
	
	
	
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
				}
	});
	
	mi.inicializarDatos = function (){
		
	}
	
	mi.generarReporte = function (){
		mi.inicializarDatos();
		$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: mi.prestamoSeleccionado.value }).success(
			function(response) {
			
				
				
		});	
	}
	
	
		
	
	
	mi.abrirPopupFecha = function(index) {
		switch(index){
			case 1: mi.ef_abierto = true; break;
		}
	};
	
	mi.obtenerMes= function (mes){
		switch (mes){
			case 1: return "Enero";
			case 2: return "Febrero";
			case 3: return "Marzo";
			case 4: return "Abril";
			case 5: return "Mayo";
			case 6: return "Junio";
			case 7: return "Julio";
			case 8: return "Agosto";
			case 9: return "Septiembre";
			case 10: return "Octubre";
			case 11: return "Noviembre";
			case 12: return "Diciembre";
		
		}
	}
	
}]);
