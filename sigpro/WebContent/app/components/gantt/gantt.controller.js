var app = angular.module('ganttController', ['DlhSoft.ProjectData.GanttChart.Directives','DlhSoft.Kanban.Angular.Components']);

var GanttChartView = DlhSoft.Controls.GanttChartView;
//Query string syntax: ?theme
//Supported themes: Default, Generic-bright, Generic-blue, DlhSoft-gray, Purple-green, Steel-blue, Dark-black, Cyan-green, Blue-navy, Orange-brown, Teal-green, Purple-beige, Gray-blue, Aero.
var queryString = window.location.search;
var theme = queryString ? queryString.substr(1) : null;

app.controller('ganttController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q) {

		var mi=this;
		mi.proyectoid = "";
		mi.proyectoNombre = "";
		mi.objetoTipoNombre = "";
		var date = new Date(), year = date.getFullYear(), month = date.getMonth();

		$window.document.title = $utilidades.sistema_nombre+' - Gantt';
		
		$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyectoId }).success(
				function(response) {
					mi.proyectoid = response.id;
					mi.proyectoNombre = response.nombre;
					mi.objetoTipoNombre = "Proyecto";
		});
		
		mi.zoom = 2.5;
		var date = new Date(), year = date.getFullYear(), month = date.getMonth();
		var items=[];
		
		mi.nombreArchivo="";
		
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
				currentTime: new Date(),
				itemHeight: 30,
				barCornerRadius: 8,
				months: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
				daysOfWeek: ['D','L','M','M','J','V','S'],
				dateFormatter: function (date){  return moment(date).format("DD/MM/YYYY");  },
				dateTimeFormatter: function (dateTime) { return moment(dateTime).format("DD/MM/YYYY");  },
				isMouseWheelZoomEnabled: false,
				horizontalGridLines: '#e0e0e0',
				itemTemplate: function (item) {
				    var document = item.ganttChartView.ownerDocument;
				    var toolTip = document.createElementNS('http://www.w3.org/2000/svg', 'title');
				    var toolTipContent = item.content +'\n • ' + (!item.isMilestone ? 'Inicio: ' : 'Fecha: ')  + moment(item.start).format("DD/MM/YYYY");
				    if (!item.isMilestone)
				        toolTipContent += '\n • ' + 'Fin: ' + moment(item.finish).format("DD/MM/YYYY");
				    toolTipContent += (item.parent!=null) ? '\n • ' + 'Padre: '+ item.parent.content : '';
				    toolTip.appendChild(document.createTextNode(toolTipContent));
				    return toolTip;
				},
				scales:[{ scaleType: 'NonworkingTime', isHeaderVisible: false, isHighlightingVisible: true, highlightingStyle: 'stroke-width: 0; fill: #f8f8f8' },
			    		{ scaleType: 'Months', headerTextFormat: 'Month', headerStyle: 'padding: 7px 5px; border-right: solid 1px White; border-bottom: solid 1px White; color: gray', isSeparatorVisible: true, separatorStyle: 'stroke: #c8bfe7; stroke-width: 1px' },
			    		{ scaleType: 'Days', headerTextFormat: 'Day', headerStyle: 'padding: 7px 5px; border-right: solid 1px White; border-bottom: solid 1px White; color: gray', isSeparatorVisible: false, separatorStyle: 'stroke: #c8bfe7; stroke-width: 0.25px' }]
			    
		};
		
		settings.assignableResources = ['Resource 1', 'Resource 2', 'Resource 3', 'Material 1', 'Material 2'];
		// Default Columns
		var columns = DlhSoft.Controls.GanttChartView.getDefaultColumns(items, settings);
		
		columns.splice(0, 0, {
		    header: 'Orden', 
		    width: 50,
		    isReadOnly: true,
		    cellStyle: 'text-align: right;',
		    cellTemplate: function (item) {
		    	//return DlhSoft.Controls.GanttChartView.numberInputColumnTemplateBase(document, 50, function(){ return item.index+1 }, function(value){ item.index=value+1 })
		    	return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document,  function(){ return item.index+1 })
		        //return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return mi.getStatus(item); });
		    }
		});
		
		columns.splice(2, 0, {
		    header: 'Estado', width: 120,
		    cellTemplate: function (item) {
		        return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return mi.getStatus(item); });
		    }
		});
		
		columns.splice(2, 0, {
		    header: '', width: 30,
		    cellTemplate: function (item) {
		        var rectangle = document.createElement('div');
		        rectangle.innerHTML = '&nbsp;';
		        rectangle.setAttribute('style', 'background-color: ' + mi.getStatusColor(mi.getStatus(item)));
		        return rectangle;
		    }
		});
		
		columns.splice(9,1);
		
		columns[1].header = 'Nombre';
		columns[1].width = 300;
		columns[4].header = 'Inicio';
		columns[5].header = 'Fin';
		columns[6].header = 'Hito';
		columns[6].isReadOnly = true;
		columns[7].header = 'Completada';
		columns[8].header = 'Responsable';
		
		for(var i=0; i<columns.length;i++)
			columns[i].headerClass = 'gantt-chart-header-column';
		
		settings.columns = columns;
		
		settings.itemPropertyChangeHandler = function (item, propertyName, isDirect, isFinal) {
		    if (isDirect && isFinal){
		    	if(propertyName=='start' || propertyName=='finish' || propertyName=='content' || propertyName=='completedFinish'){
		    		console.log(item.content + '.' + propertyName + ' changed.');
		    		console.log(item);
		    	}
		    }
		}
		
		$scope.settings = settings;
		
		mi.ganttChartView;
		var formatData = new FormData();
		 
		formatData.append("accion",'getProyecto');
		formatData.append("proyecto_id",$routeParams.proyectoId);
		
		$http.post('/SGantt', formatData, {
			headers: {'Content-Type': undefined},
			transformRequest: angular.identity
		 }).success(
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
					mi.ganttChartView = document.getElementById('ganttChartView');
					
					
					var predecesores = response.predecesores;
					
					for (var predecesor in predecesores){
						for(var i=0; i< items.length; i++){
							if (items[i].id === predecesores[predecesor].id){
								for(var j=0; j< items.length; j++){
									if (items[j].id == predecesores[predecesor].idPredecesor){
										items[i].predecessors = [{ item: items[j] }];
										break;
									}
								}
								break;
							}
						}
					}
					
		});	
		
		mi.zoomAcercar = function(){
			mi.zoom =(mi.zoom<1) ? mi.zoom + 0.05 :  mi.zoom + 1;
			mi.ganttChartView.setHourWidth(mi.zoom);
		};
		
		mi.zoomAlejar = function(){
			mi.zoom = (mi.zoom<1) ? mi.zoom - 0.05 :  mi.zoom - 1;
			if(mi.zoom<0.05){
				mi.zoom=0.05;
				$utilidades.mensaje('warning','No puede alejar mas la vista de la gráfica');
			}
			else
				mi.ganttChartView.setHourWidth(mi.zoom);
		};
		
		mi.cargar=function(){
			if (mi.archivos!=null && mi.arhivos != ''){
			
			var formatData = new FormData();
			formatData.append("file",mi.archivos);  
			formatData.append("accion",'importar');
			$http.post('/SGantt',formatData, {
					headers: {'Content-Type': undefined},
					transformRequest: angular.identity
				 } ).then(
			
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
					mi.ganttChartView = document.getElementById('ganttChartView');
				}
			);
			}else{
				$utilidades.mensaje('danger','Debe seleccionar un archivo');
			}
		};
		
		mi.exportar=function(){
			var formatData = new FormData();
			 
			
			
			$http.post('/SDownload', { accion: 'exportar', proyectoid:$routeParams.proyectoId,t:moment().unix()
			  }).then(
					 function successCallback(response) {
							var anchor = angular.element('<a/>');
						    anchor.attr({
						         href: 'data:application/ms-project;base64,' + response.data,
						         target: '_blank',
						         download: 'Programa.mpx'
						     })[0].click();
						  }.bind(this), function errorCallback(response){
						 		
						 	}
			);
		};
		
		
		 $scope.cargarArchivo = function(event){
		         mi.archivos = event.target.files[0];      
		         mi.nombreArchivo = mi.archivos.name;
		       
		  };
	   
		
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


