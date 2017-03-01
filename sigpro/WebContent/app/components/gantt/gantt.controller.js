var app = angular.module('ganttController', ['DlhSoft.ProjectData.GanttChart.Directives']);

var GanttChartView = DlhSoft.Controls.GanttChartView;
//Query string syntax: ?theme
//Supported themes: Default, Generic-bright, Generic-blue, DlhSoft-gray, Purple-green, Steel-blue, Dark-black, Cyan-green, Blue-navy, Orange-brown, Teal-green, Purple-beige, Gray-blue, Aero.
var queryString = window.location.search;
var theme = queryString ? queryString.substr(1) : null;

app.controller('ganttController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout) {

		var mi=this;
		var date = new Date(), year = date.getFullYear(), month = date.getMonth();

		$window.document.title = $utilidades.sistema_nombre+' - Gantt';
		
		
		
		var date = new Date(), year = date.getFullYear(), month = date.getMonth();
		var items=[];
		
		mi.getStatus = function (item) {
		    if (item.hasChildren || item.isMilestone)
		        return '';
		    if (item.completedFinish >= item.finish)
		        return 'Completada';
		    var now = settings.currentTime;
		    if (item.completedFinish < now)
		        return 'Atrasada';
		    if (item.completedFinish > item.start)
		        return 'En progreso';
		    return 'To do';
		}
		
		mi.getStatusColor = function (status) {
		    switch (status) {
		        case 'Completada':
		            return 'Green';
		        case 'To do':
		            return 'Gray';
		        case 'Atrasada':
		            return 'Red';
		        case 'En progreso':
		            return 'Orange';
		        default:
		            return 'Transparent';
		    }
		}
		var settings = { 
				areTaskDependencyConstraintsEnabled: true,
				currentTime: new Date()
		};
		// Default Columns
		var columns = DlhSoft.Controls.GanttChartView.getDefaultColumns(items, settings);
		
		columns.splice(1, 0, {
		    header: 'Estado', width: 120,
		    cellTemplate: function (item) {
		        return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return mi.getStatus(item); });
		    }
		});
		
		columns.splice(1, 0, {
		    header: '', width: 30,
		    cellTemplate: function (item) {
		        var rectangle = document.createElement('div');
		        rectangle.innerHTML = '&nbsp;';
		        rectangle.setAttribute('style', 'background-color: ' + mi.getStatusColor(mi.getStatus(item)));
		        return rectangle;
		    }
		});
		
		columns.splice(7,1);
		
		columns[0].header = '';
		columns[0].width = 300;
		columns[3].header = 'Inicio';
		columns[4].header = 'Fin';
		columns[5].header = 'Hito';
		columns[5].isReadOnly = true;
		columns[6].header = 'Completada';
		
		settings.columns = columns;
		$scope.settings = settings;
		
		var ganttChartView;
		
		$http.post('/SGantt', { accion: 'getProyecto', proyecto_id: $routeParams.proyectoId }).success(
				function(response) {
					var items = response.items;
					$scope.settings.displayedTime = moment(items[0].start,'DD/MM/YYYY hh:mm:ss').toDate();
					
					for(var i=0; i< items.length; i++){
						if(items[i].start)
							items[i].start = moment(items[i].start,'DD/MM/YYYY hh:mm:ss').toDate();
						if(items[i].finish)
							items[i].finish = moment(items[i].finish,'DD/MM/YYYY hh:mm:ss').toDate();
						if(items[i].identation)
							items[i].indentation = Number(items[i].indentation);
						items[i].expandend = items[i].expanded=='true' ? true : false;
						items[i].isMilestone = items[i].isMilestone=='true' ? true : false;
					}
					$scope.items = items;
					$scope.settings.timelineStart =items[0].start;
					ganttChartView = document.getElementById('ganttChartView');
					
		});	
		
		mi.cargar=function(){
			$http.post('/SGantt', { accion: 'cargar',t:moment().unix(), nombre: 'Cronograma.mpp', } ).then(
				function(response) {
					var items = response.data.items;
					$scope.settings.displayedTime = moment(items[0].start,'DD/MM/YYYY hh:mm:ss').toDate();
					
					for(var i=0; i< items.length; i++){
						if(items[i].start)
							items[i].start = moment(items[i].start,'DD/MM/YYYY hh:mm:ss').toDate();
						if(items[i].finish)
							items[i].finish = moment(items[i].finish,'DD/MM/YYYY hh:mm:ss').toDate();
						if(items[i].identation)
							items[i].indentation = Number(items[i].indentation);
						items[i].expandend = items[i].expanded=='true' ? true : false;
						items[i].isMilestone = items[i].isMilestone=='true' ? true : false;
					}
					$scope.items = items;
					$scope.settings.timelineStart =items[0].start;
					ganttChartView = document.getElementById('ganttChartView');
				}
			);
		};
		
		mi.exportar=function(){
			$http.post('/SGantt', { accion: 'exportar',t:moment().unix(), proyecto_id: $routeParams.proyectoId }).then(
				function(response) {
					
				}
			);
		};
		
	}
]);
