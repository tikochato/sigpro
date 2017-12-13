var app = angular.module('matrizriesgoController', ['smart-table']);


app.controller('matrizriesgoController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {

	var mi=this;
	mi.prestamoId=null;
	mi.objetoTipoNombre = "";
	mi.mostrarTabla = false;
	
		
	$window.document.title = $utilidades.sistema_nombre+' - Matriz Riesgos';
	i18nService.setCurrentLang('es');


	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
	
	$http.post('/SPrestamo', {accion: 'getPrestamos', t: (new Date()).getTime()}).then(
		function(response){
			if(response.data.success){
				mi.lprestamos = response.data.prestamos;
			}	
	});
	
	mi.blurPrestamo=function(){
		if(document.getElementById("prestamo_value").defaultValue!=mi.prestamoNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','prestamo');
		}
	}
	
	mi.cambioPrestamo=function(selected){
		if(selected!== undefined){
			mi.prestamoNombre = selected.originalObject.proyectoPrograma;
			mi.prestamoId = selected.originalObject.id;
			$scope.$broadcast('angucomplete-alt:clearInput', 'pep');
			$scope.$broadcast('angucomplete-alt:clearInput', 'lineaBase');
			mi.getPeps(mi.prestamoId);
		}
		else{
			mi.prestamoNombre="";
			mi.prestamoId=null;
		}
	}
	
	mi.blurPep=function(){
		if(document.getElementById("pep_value").defaultValue!=mi.pepNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','pep');
		}
	}
	
	mi.cambioPep=function(selected){
		if(selected!== undefined){
			mi.pepNombre = selected.originalObject.nombre;
			mi.pepId = selected.originalObject.id;
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
			mi.getLineasBase(mi.pepId);
		}
		else{
			mi.pepNombre="";
			mi.pepId="";
		}
	}
	
	mi.getPeps = function(prestamoId){
		$http.post('/SProyecto',{accion: 'getProyectos', prestamoid: prestamoId}).success(
			function(response) {
				mi.peps = [];
				if (response.success){
					mi.peps = response.entidades;
				}
		});	
	}
	
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	
	mi.cambioLineaBase=function(selected){
		if(selected!== undefined){
			mi.lineaBaseNombre = selected.originalObject.nombre;
			mi.lineaBaseId = selected.originalObject.id;
			mi.cargarMatriz();
		}
		else{
			mi.lineaBaseNombre="";
			mi.lineaBaseId=null;
		}
	};


	mi.getLineasBase = function(proyectoId){
		$http.post('/SProyecto',{accion: 'getLineasBase', proyectoId: proyectoId}).success(
			function(response) {
				mi.lineasBase = [];
				if (response.success){
					mi.lineasBase = response.lineasBase;
				}
		});	
	}
	
	mi.cargarMatriz = function(){
		mi.lista = [];
		mi.riesgos=[];
		mi.mostrarTabla = false;
		 $http.post('/SMatrizRiesgo', { 
			 accion: 'getMatrizRiesgos', 
			 proyectoid:mi.pepId, 
			 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			 t: (new Date()).getTime()})
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
		$http.post('/SMatrizRiesgo', { 
			accion: 'exportarExcel',
			proyectoid:mi.pepId,
			lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			t:moment().unix() })
		.then(
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



		