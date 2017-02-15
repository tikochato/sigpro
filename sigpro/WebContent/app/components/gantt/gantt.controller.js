var app = angular.module('ganttController', ['DlhSoft.ProjectData.GanttChart.Directives']);

app.controller('ganttController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document) {

		var mi=this;
		var year = 2017;
		var month = 1;
		$window.document.title = 'SIGPRO - Gantt';
		
		var items = [{ content: "Task 1", isExpanded: false },
            { content: "Task 1.1", indentation: 1, start: new Date(year, month, 2, 8, 0, 0), finish: new Date(year, month, 4, 16, 0, 0) },
            { content: "Task 1.2", indentation: 1, start: new Date(year, month, 3, 8, 0, 0), finish: new Date(year, month, 5, 12, 0, 0) },
            { content: "Task 2", isExpanded: true },
            { content: "Task 2.1", indentation: 1, start: new Date(year, month, 2, 8, 0, 0), finish: new Date(year, month, 8, 16, 0, 0), 
              completedFinish: new Date(year, month, 5, 16, 0, 0), assignmentsContent: "Resource 1, Resource 2 [50%]" },
            { content: "Task 2.2", indentation: 1 },
            { content: "Task 2.2.1", indentation: 2, start: new Date(year, month, 11, 8, 0, 0), finish: new Date(year, month, 12, 16, 0, 0), 
              completedFinish: new Date(year, month, 12, 16, 0, 0), assignmentsContent: "Resource 2" },
            { content: "Task 2.2.2", indentation: 2, start: new Date(year, month, 12, 12, 0, 0), finish: new Date(year, month, 14, 16, 0, 0), 
              assignmentsContent: "Resource 2" },
            { content: "Task 3", indentation: 1, start: new Date(year, month, 15, 16, 0, 0), isMilestone: true }];
		
		
		items[3].predecessors = [{ item: items[0], dependencyType: "SS" }];
		items[7].predecessors = [{ item: items[6], lag: 2 * 60 * 60 * 1000 }];
		items[8].predecessors = [{ item: items[4] }, { item: items[5] }];
		
		var settings = {
		        // Auto-scheduling is initially turned on.
		        areTaskDependencyConstraintsEnabled: true,
		        // Other settings that you can enable and customize as needed in your application.
		        // isGridVisible: false,
		        gridWidth: '30%',
		        chartWidth: '90%',
		        // isGridReadOnly: true,
		        // isChartReadOnly: true,
		        // isVirtualizing: false,
		        // isTaskEffortPreservedWhenStartChangesInGrid: true,
		        // border: 'Gray',
		        // gridLines: 'LightGray',
		        // standardBarStyle: 'stroke: Green; fill: LightGreen',
		        // standardCompletedBarStyle: 'stroke: DarkGreen; fill: DarkGreen',
		        // dependencyLineStyle: 'stroke: Green; fill: none; marker-end: url(#ArrowMarker)',
		        // alternativeItemStyle: 'background-color: #f9f9f9', alternativeChartItemStyle: 'fill: #f9f9f9',
		        // itemTemplate: (item) => {
		        //     var toolTip = document.createElementNS('http://www.w3.org/2000/svg', 'title');
		        //     var toolTipContent = item.content + ' • ' + 'Start: ' + item.start.toLocaleString();
		        //     if (!item.isMilestone)
		        //         toolTipContent += ' • ' + 'Finish: ' + item.finish.toLocaleString();
		        //     toolTip.appendChild(document.createTextNode(toolTipContent));
		        //     return toolTip;
		        // },
		        currentTime: new Date(year, month - 1, 12) // Display the current time vertical line of the chart at the project start date.
		    };
		var ganttChartView = $document[0].getElementById('ganttChartView');
		DlhSoft.Controls.GanttChartView.initialize(ganttChartView, items, settings);
		
	}
]);
