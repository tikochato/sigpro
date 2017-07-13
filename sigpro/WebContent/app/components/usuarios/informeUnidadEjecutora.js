var app = angular.module('informeUnidadController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'informeUnidadController',
 [
	 '$scope',
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
  function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal, $dialogoConfirmacion) {
	var mi=this;
	$window.document.title =$utilidades.sistema_nombre+' - Informe Unidad Ejecutora';
	i18nService.setCurrentLang('es');
	mi.edicion=false;
	console.log("cargado");
   
	mi.usuarioActual={usuario:"", email:""};
	mi.esoculto= true;
	mi.tieneColaborador=false;
	

	
	
	} 
]);
