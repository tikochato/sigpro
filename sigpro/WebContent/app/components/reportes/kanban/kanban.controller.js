var app = angular.module('kanbanController', ['DlhSoft.Kanban.Angular.Components']);

var GanttChartView = DlhSoft.Controls.GanttChartView;
//Query string syntax: ?theme
//Supported themes: Default, Generic-bright, Generic-blue, DlhSoft-gray, Purple-green, Steel-blue, Dark-black, Cyan-green, Blue-navy, Orange-brown, Teal-green, Purple-beige, Gray-blue, Aero.
var queryString = window.location.search;
var theme = queryString ? queryString.substr(1) : null;

app.controller('kanbanController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q) {

		var mi=this;
		  
		var KanbanBoard = DlhSoft.Controls.KanbanBoard;
		var formatData = new FormData();
		 
		formatData.append("accion",'getKanban');
		formatData.append("proyecto_id",$routeParams.proyectoId);
		$scope.states = {};
		$scope.itemsKanban = null; 
		$scope.mostrarKanban = false;
		
		$http.post('/SGantt', formatData, {
			headers: {'Content-Type': undefined},
			transformRequest: angular.identity
		 }).success(
				function(response) {
					var estado1 = { name: 'Nuevo', areNewItemButtonsHidden: true }, 
					estado2 = { name: 'En progreso', areNewItemButtonsHidden: true }, 
					estado3 = { name: 'Terminado', isCollapsedByDefaultForGroups: true, areNewItemButtonsHidden: true };
					var colores = ["#ffe033","#9fc6ee","#008000"];
					
					var estados  = [estado1, estado2, estado3];
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


