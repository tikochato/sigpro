/**
 * 
 */

var app = angular.module('sipro', [
	'ngTouch','ngUtilidades'
]);

app.controller('gestionUsuario', [
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
	
	function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal, $dialogoConfirmacion){
		
		$window.document.title = $utilidades.sistema_nombre + ' - Gestión de Usuarios';
		var mi = this;
		mi.sistema_nombre =  $utilidades.sistema_nombre;
		mi.usuariosSeleccionado=false;
	}
]);

