var app = angular.module('porcentajeactividadesController', ['DlhSoft.Kanban.Angular.Components']);

var GanttChartView = DlhSoft.Controls.GanttChartView;
//Query string syntax: ?theme
//Supported themes: Default, Generic-bright, Generic-blue, DlhSoft-gray, Purple-green, Steel-blue, Dark-black, Cyan-green, Blue-navy, Orange-brown, Teal-green, Purple-beige, Gray-blue, Aero.
var queryString = window.location.search;
var theme = queryString ? queryString.substr(1) : null;

app.controller('porcentajeactividadesController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q) {

		var mi=this;
		  
		var KanbanBoard = DlhSoft.Controls.KanbanBoard;
		
		
		$scope.states = {};
		$scope.itemsKanban = null; 
		$scope.mostrarKanban = false;
		
		$http.post('/SPorcentajeActividades', {accion : "getKanban", proyecto_id:$routeParams.proyectoId }).success(
				function(response) {
					var estadoNuevo = { name: 'Nuevo', areNewItemButtonsHidden: true }, 
					estadoEnProgreso = { name: 'En progreso', areNewItemButtonsHidden: true }, 
					estadoTerminado = { name: 'Terminado', isCollapsedByDefaultForGroups: true, areNewItemButtonsHidden: true };
					estadoRetrasado = { name: 'Retrasado', isCollapsedByDefaultForGroups: true, areNewItemButtonsHidden: true };
					
					var colores = ["#ffe033","#9fc6ee","#008000","#ff0000"];
					
					var estados  = [estadoNuevo, estadoEnProgreso, estadoTerminado,estadoRetrasado];
					var items  = response.items;
					
					for (item in items) {
					    items[item].state = estados[items[item].estadoId];
					    items[item].color = colores[items[item].estadoId];
					    items[item].isReadOnly = true;  
					}
					
					$scope.states  = estados;
					$scope.itemsKanban = items;
					$scope.mostrarKanban = true;
					
		});	
		
	}
]);



app.directive('customOnChange', function() {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var onChangeHandler = scope.$eval(attrs.customOnChange);
      element.bind('change', onChangeHandler);
    }
  };
});


