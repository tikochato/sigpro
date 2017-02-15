var app = angular.module('ganttController', ['DlhSoft.ProjectData.GanttChart.Directives']);

app.controller('ganttController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document) {

		var mi=this;
		var date = new Date(), year = date.getFullYear(), month = date.getMonth();

		$window.document.title = 'SIGPRO - Gantt';
		
		var items = [{ content: 'Task 1', isExpanded: false },
            { content: 'Task 1.1', indentation: 1, start: new Date(year, month, 2, 8, 0, 0), finish: new Date(year, month, 4, 16, 0, 0) },
            { content: 'Task 1.2', indentation: 1, start: new Date(year, month, 3, 8, 0, 0), finish: new Date(year, month, 5, 12, 0, 0) },
            { content: 'Task 2', isExpanded: true },
            { content: 'Task 2.1', indentation: 1, start: new Date(year, month, 2, 8, 0, 0), finish: new Date(year, month, 8, 16, 0, 0), completedFinish: new Date(year, month, 5, 16, 0, 0), assignmentsContent: 'Resource 1, Resource 2 [50%]' },
            { content: 'Task 2.2', indentation: 1 },
            { content: 'Task 2.2.1', indentation: 2, start: new Date(year, month, 11, 8, 0, 0), finish: new Date(year, month, 14, 16, 0, 0), completedFinish: new Date(year, month, 14, 16, 0, 0), assignmentsContent: 'Resource 2' },
            { content: 'Task 2.2.2', indentation: 2, start: new Date(year, month, 12, 12, 0, 0), finish: new Date(year, month, 14, 16, 0, 0), assignmentsContent: 'Resource 2' },
            { content: 'Task 3', indentation: 1, start: new Date(year, month, 15, 16, 0, 0), isMilestone: true }];
		
		items[3].predecessors = [{ item: items[0], dependencyType: 'SS' }];
		items[7].predecessors = [{ item: items[6], lag: 2 * 60 * 60 * 1000 }];
		items[8].predecessors = [{ item: items[4] }, { item: items[5] }];
		for (var i = 4; i <= 16; i++)
		   items.push({ content: 'Task ' + i, indentation: i >= 8 && i % 3 == 2 ? 0 : 1, start: new Date(year, month, 2 + (i <= 8 ? (i - 4) * 3 : i - 8), 8, 0, 0), finish: new Date(year, month, 2 + (i <= 8 ? (i - 4) * 3 + (i > 8 ? 6 : 1) : i - 2), 16, 0, 0) });
		items[9].finish.setDate(items[9].finish.getDate() + 2);
		items[9].assignmentsContent = 'Resource 1';
		items[10].predecessors = [{ item: items[9] }];
		
		//Prepare control settings.
		var settings = {
		   // Optionally, hide data grid or set grid and chart widths, set read only settings, and/or disable virtualization.
		   // isGridVisible: false,
		   // gridWidth: '30%',
		   // chartWidth: '70%',
		   // isGridReadOnly: true,
		   // isChartReadOnly: true,
		   // isVirtualizing: false,
		
		   // Optionally, preseve task effort when start value changes in the grid.
		   // isTaskEffortPreservedWhenStartChangesInGrid: true,
		
		   // Optionally, set the scrollable timeline to present.
		   // timelineStart: new Date(year, month, 1),
		   // timelineFinish: new Date(year + 2, month, 1),
		
		   // Optionally, set application target, interaction mode, and theme and/or custom styles.
		   // target: 'Phone', // Supported values: Standard, Phone.
		   // interaction: 'TouchEnabled', // Supported values: Standard, TouchEnabled.
		   // theme: 'Aero', // Supported values: Modern, ModernBordered, Aero.
		   // border: 'Gray',
		   // gridLines: 'LightGray',
		   // standardBarStyle: 'stroke: Green; fill: LightGreen',
		   // standardCompletedBarStyle: 'stroke: DarkGreen; fill: DarkGreen',
		   // dependencyLineStyle: 'stroke: Green; fill: none; marker-end: url(#ArrowMarker)',
		
		   // Optionally, display alternative row background.
		   // alternativeItemStyle: 'background-color: #f9f9f9', alternativeChartItemStyle: 'fill: #f9f9f9',
		
		   // Optionally, set item template used when displaying task bar tool tips in the chart area.
		   // itemTemplate: function (item) {
		   //     var toolTip = document.createElementNS('http://www.w3.org/2000/svg', 'title');
		   //     var toolTipContent = item.content + ' • ' + 'Start: ' + item.start.toLocaleString();
		   //     if (!item.isMilestone)
		   //         toolTipContent += ' • ' + 'Finish: ' + item.finish.toLocaleString();
		   //     toolTip.appendChild(document.createTextNode(toolTipContent));
		   //     return toolTip;
		   // },
		
		   // Set the current time value to automatically scroll to a specific chart coordinate, and display a vertical bar highlighter at the specified point.
		   currentTime: new Date(year, month, 2, 12, 0, 0)
		};
		
		// Prepare the columns collection.
		var columns = DlhSoft.Controls.GanttChartView.getDefaultColumns(items, settings);
		var indexOffset = columns[0].isSelection ? 1 : 0;

		// Apply the customized columns collection.
		settings.columns = columns;

		var ganttChartView = $document[0].getElementById('ganttChartView');
		DlhSoft.Controls.GanttChartView.initialize(ganttChartView, items, settings);
		
	}
]);
