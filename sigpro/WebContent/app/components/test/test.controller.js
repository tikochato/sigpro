var app = angular.module('testController', []);

app.controller('testController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog) {
			var mi=this;
			
			$window.document.title = 'SIGPRO - Test';
			i18nService.setCurrentLang('es');
			
			
			mi.irAMeta=function(id, tipo){
				$location.path('/meta/1/1');
			};
			
		} ]);