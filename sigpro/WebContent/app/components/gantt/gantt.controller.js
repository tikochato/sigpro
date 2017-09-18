var app = angular.module('ganttController', ['DlhSoft.ProjectData.GanttChart.Directives',
		'DlhSoft.Kanban.Angular.Components','ui.grid.edit', 'ui.grid.rowEdit','ui.bootstrap.contextMenu']);

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
		mi.mostrarcargando = true;
		var date = new Date(), year = date.getFullYear(), month = date.getMonth();	
		mi.objetoId=null;
		mi.objetoTipo=null;
		
		$window.document.title = $utilidades.sistema_nombre+' - Gantt';
		
		var servlet_ = $routeParams.objeto_tipo == 1 ? '/SProyecto' : 'SPrograma';
		var accion_ = $routeParams.objeto_tipo == 1 ? 'obtenerProyectoPorId' : 'obtenerProgramaPorId'
		
		$http.post(servlet_, { accion: accion_, id: $routeParams.objeto_id }).success(
				function(response) {
					mi.proyectoid = response.id;
					mi.proyectoNombre = response.nombre;
					mi.objetoTipoNombre = $routeParams.objeto_tipo == 1 ? "Proyecto" : "Programa";
		});
		
		mi.zoom = 2.0;
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
		};
		
		mi.getStatusColor = function (status) {
		    switch (status) {
		        case 'Completada':
		            return '#b0cfe8';
		        case 'To do':
		            return '#e2e291';
		        case 'Atrasada':
		            return '#fd9496';
		        case 'En progreso':
		            return '#c7e7a5';
		        default:
		            return 'Transparent';
		    }
		};
		
		mi.getOjbetoTipo = function (item) {
		    switch (item.objetoTipo) {
		        case '1':
		            return 'glyphicon glyphicon-record';
		        case '2':
		            return 'glyphicon glyphicon-th';
		        case '3':
		            return 'glyphicon glyphicon-certificate';
		        case '4':
		            return 'glyphicon glyphicon-link';
		        case '5':
		            return 'glyphicon glyphicon-th-list';
		    }
		};
		
		
		mi.getOjbetoTipoNombre = function (item) {
		    switch (item.objetoTipo) {
		        case '1':
		            return 'Proyecto';
		        case '2':
		            return 'Componente';
		        case '3':
		            return 'Producto';
		        case '4':
		            return 'Subproducto';
		        case '5':
		            return 'Actividad';
		    }
		};
		

		
		
		var settings = { 
				areTaskDependencyConstraintsEnabled: true,
				currentTime: new Date(),
				itemHeight: 30,
				isReadOnly:true,
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
		
		
		//
		// Default Columns
		var columns = DlhSoft.Controls.GanttChartView.getDefaultColumns(items, settings);
		
		
		columns.splice(0, 0, {
		    header: 'Id', 
		    width: 50,
		    isReadOnly: true,
		    cellStyle: 'text-align: right;',
		    cellTemplate: function (item) {
		    	//return DlhSoft.Controls.GanttChartView.numberInputColumnTemplateBase(document, 50, function(){ return item.index+1 }, function(value){ item.index=value+1 })
		    	return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document,  function(){ return item.id })
		        //return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return mi.getStatus(item); });
		    }
		});
		
		columns.splice(1, 0, {
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
		    header: 'Tipo', width: 40,
		    cellTemplate: function (item) {
		        var rectangle = document.createElement('div');
		        rectangle.setAttribute('class', mi.getOjbetoTipo(item));
		        rectangle.setAttribute('data-toggle', 'tooltip');
		        rectangle.setAttribute('title', mi.getOjbetoTipoNombre(item));
		        
		        return rectangle;
		    }
		});
		
		
		columns.splice(4, 0, {
		    header: 'Estado', width: 120,
		    cellTemplate: function (item) {
		        return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return mi.getStatus(item); });
		    }
		});
		
		columns.splice(4, 0, {
		    header: '', width: 30,
		    cellTemplate: function (item) {
		        /*var rectangle = document.createElement('div');
		        rectangle.innerHTML = '&nbsp;';
		        rectangle.setAttribute('style', 'background-color: ' + mi.getStatusColor(mi.getStatus(item)));
		        return rectangle;*/
		        var rectangle = document.createElement('div');
		        rectangle.innerHTML = '&nbsp;';
		        rectangle.setAttribute('class', 'glyphicon glyphicon-certificate');
		        rectangle.setAttribute('style', 'color: ' + mi.getStatusColor(mi.getStatus(item)));
		        return rectangle;
		    }
		});
		
		
		
		columns.splice(9,0);
		columns[3].header = 'Nombre';
		columns[3].width = 300;
		columns[5].header = 'Estado';
		columns[5].width = 85;
		columns[6].header = 'Inicio';
		columns[6].width = 85;
		columns[7].header = 'Fin';
		columns[7].width = 85;
		columns[8].header = 'Hito';
		columns[8].width = 60;
		columns[8].isReadOnly = true;
		columns[9].header = 'Completada';
		columns[10].header = 'Responsable';
		columns[10].isReadOnly = true;
		
		//columns.splice(10, 0, { header: 'Duración (d)', width: 80, cellTemplate: DlhSoft.Controls.GanttChartView.getDurationColumnTemplate(64, 8) });
		columns.splice(11, 0, {
		    header: 'Duración (d)', 
		    width: 80,
		    isReadOnly: true,
		    cellStyle: 'text-align: right;',
		    cellTemplate: DlhSoft.Controls.GanttChartView.getDurationColumnTemplate(64, 8)
		});
		
		columns.splice(12 , 0, { header: 'Predecesor', width: 70, cellTemplate: DlhSoft.Controls.GanttChartView.getPredecessorsColumnTemplate(84) });
		
		//columns.push({ header: 'Costo Planificado (Q)', width: 110, cellTemplate: DlhSoft.Controls.GanttChartView.getCostColumnTemplate(84) });
		columns.splice(13, 0, {
		    header: 'Costo Planificado (Q)', 
		    width: 110,
		    isReadOnly: true,
		    cellStyle: 'text-align: right;',
		    cellTemplate: function (item) {
		    	return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return numeral(item.costo).format('$ 0,0') });
	
		    }

		});		
		
		//columns.push({ header: 'Meta Planificada', width: 80, cellTemplate: function (item) { return DlhSoft.Controls.GanttChartView.textInputColumnTemplateBase(document, 64, function () { return item.metaPlanificada; }, function (value) { item.metaPlanificada = value; }); } });
		columns.splice(14, 0, {
		    header: 'Meta Planificada', 
		    width: 80,
		    isReadOnly: true,
		    cellStyle: 'text-align: right;',
		    cellTemplate: function (item) {
		        return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return item.metaPlanificada; });
		    }
		});	
		
		//columns.push({ header: 'Meta Real', width: 80, cellTemplate: function (item) { return DlhSoft.Controls.GanttChartView.textInputColumnTemplateBase(document, 64, function () { return item.metaReal; }, function (value) { item.metaReal = value; }); } });
		columns.splice(15, 0, {
		    header: 'Meta Real', 
		    width: 80,
		    isReadOnly: true,
		    cellStyle: 'text-align: right;',
		    cellTemplate: function (item) {
		        return DlhSoft.Controls.GanttChartView.textColumnTemplateBase(document, function () { return item.metaReal; });
		    }
		});
		
		
		for(var i=0; i<columns.length;i++)
			columns[i].headerClass = 'gantt-chart-header-column';
		
		settings.columns = columns;
		
		settings.itemPropertyChangeHandler = function (item, propertyName, isDirect, isFinal) {
			
		    if (isDirect && isFinal && false){
		    	
		    	if(propertyName=='start' || propertyName=='finish' || propertyName=='content' 
		    		|| propertyName=='completedFinish' ) {
		    		
		    		var parametros = {
		    				accion: 'modificarData',
		    				objetoId: item.objetoId,
		    				objetoTipo:item.objetoTipo,
		    				nombre: item.content,
		    				inicio: moment(item.start).format('DD/MM/YYYY'),
		    				fin: moment(item.finish).format('DD/MM/YYYY'),
		    				completada: item.completedFinish != '' ? true : false
					}
		    	
					$http.post('/SGantt',parametros).success(
						function(response) {
							if (response.success){
								$utilidades.mensaje('success','Item modificado con éxito');
							}else{
								$utilidades.mensaje('danger','Error al guardar item');
							}
					});
		    	}
		    }
		}
		
		$scope.settings = settings;
		
		mi.ganttChartView;
		
		mi.cargarProyecto = function (){
			var formatData = new FormData();
			 
			formatData.append("accion",$routeParams.objeto_tipo == 1 ? 'getProyecto' : 'getPrograma');
			formatData.append($routeParams.objeto_tipo == 1 ? "proyecto_id" : "programa_id",$routeParams.objeto_id);
			
			$http.post('/SGantt', formatData, {
				headers: {'Content-Type': undefined},
				transformRequest: angular.identity
			 }).success(
					function(response) {
						
						var items =  response.items;
						$scope.settings.displayedTime = moment(items[0].start,'DD/MM/YYYY hh:mm:ss').toDate();
						
						for(var i=0; i< items.length; i++){
							if(items[i].start)
								items[i].start = moment(items[i].start,'DD/MM/YYYY hh:mm:ss').toDate();
							if(items[i].finish)
								items[i].finish = moment(items[i].finish,'DD/MM/YYYY hh:mm:ss').toDate();
							if(items[i].identation)
								items[i].indentation = items[i].indentation+0;
							if (items[i].Cost)
								items[i].Cost = Number(items[i].Cost);
							
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
						
						mi.mostrarcargando = false;
			});	
		};
		
		
		mi.cargarProyecto();
		
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
			
			$http.post('/SDownload', { accion: 'exportar', proyectoid:$routeParams.objeto_id,t:moment().unix()
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
		  
		  /*settings.itemPropertyChangeHandler = function (item, propertyName, isDirect, isFinal) {
			    if (isDirect && isFinal){
			    	if(propertyName=='start' || propertyName=='finish' || propertyName=='content' || propertyName=='completedFinish'){
			    		console.log(item.content + '.' + propertyName + ' changed.');
			    		console.log(item);
			    	}
			    }
		}*/
			
		  settings.itemDoubleClickHandler = function (isOnChart, item){
			switch (item.objetoTipo){
				case '1':
					mi.editarPrestamo(item.objetoId,item.index);
					break;
				case '2':
					mi.editarComponente(item.objetoId,item.index,0,false);
					break;
				case '3':
					mi.editarProducto(item.objetoId,item.index,0,false);
					break;
				case '4':
					mi.editarSubproducto(item.objetoId,item.index,0,false);
					break;
				case '5':
					mi.editarActividad(item.objetoId,item.index,item.parent.id,item.parent.objetoTipo);
					break;
			}
		  }
		  
		  
		  
		  
		  settings.mouseHandler = function (eventName, isOnItemsArea, isOnChart, row, column, button, clickCount, e) {
			    if (button == 3 ){
			    	mi.objetoId = row.objetoId;
			    	mi.objetoTipo = row.objetoTipo;
			    }
			};

		  
		  
		  
		  mi.editarPrestamo = function(idprestamo,index) {
				var modalInstance = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'editarPrestamo.jsp',
					controller : 'modalEditarPrestamo',
					controllerAs : 'prestamoc',
					backdrop : 'static',
					size : 'md',
					resolve : {
						idprestamo: function() {
							return idprestamo;
						},
						index: function(){
							return index;
						}
					}
				});

				modalInstance.result.then(function(resultado) {
					if (resultado != undefined){
						$scope.items[index].content = resultado.nombre;
						$utilidades.mensaje('success','Item modificado con éxito');
					}else{
						$utilidades.mensaje('danger', 'Error al guardar el item de prestamo');
					}

				}, function() {
				});
		};
		
		
		mi.editarComponente = function(idcomponente,index,objetoId,esnuevo) {
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'editarComponente.jsp',
				controller : 'modalEditarComponente',
				controllerAs : 'componentec',
				backdrop : 'static',
				size : 'md',
				resolve : {
					idcomponente: function() {
						return idcomponente;
					},
					index: function(){
						return index;
					},
					objetoId : function(){
						return objetoId;
					},
					esnuevo : function(){
						return esnuevo;
					} 
				}
			});

			modalInstance.result.then(function(resultado) {
				if (resultado != undefined){
					$scope.items[index].content = resultado.nombre;
					if (esnuevo){
						mi.cargarProyecto();
					}
					
					$utilidades.mensaje('success','Componente ' + (esnuevo ? 'Creado' : 'modificado') +' con éxito');
				}else{
					$utilidades.mensaje('danger', 'Error al guardar el item de componente');
				}

			}, function() {
			});
		};
		
		mi.editarProducto = function(idproducto,index,componenteId,esnuevo) {
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'editarProducto.jsp',
				controller : 'modalEditarProducto',
				controllerAs : 'productoc',
				backdrop : 'static',
				size : 'md',
				resolve : {
					idproducto: function() {
						return idproducto;
					},
					index: function(){
						return index;
					},
					componenteId: function(){
						return componenteId;
					},
					esnuevo: function(){
						return esnuevo;
					}
				}
			});

			modalInstance.result.then(function(resultado) {
				if (resultado != undefined){
					$scope.items[index].content = resultado.nombre;
					if (esnuevo){
						mi.cargarProyecto();
					}
					$utilidades.mensaje('success','Producto ' + (esnuevo ? 'creado' : 'modificado') + ' con éxito');
				}else{
					$utilidades.mensaje('danger', 'Error al guardar el item de producto');
				}

			}, function() {
			});
		};
		
		mi.editarSubproducto = function(idsubproducto,index,productoId,esnuevo) {
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'editarSubproducto.jsp',
				controller : 'modalEditarSubproducto',
				controllerAs : 'subproductoc',
				backdrop : 'static',
				size : 'md',
				resolve : {
					idsubproducto: function() {
						return idsubproducto;
					},
					index: function(){
						return index;
					},
					productoId: function(){
						return productoId;
					},
					esnuevo: function(){
						return esnuevo;
					}
				}
			});

			modalInstance.result.then(function(resultado) {
				if (resultado != undefined){
					$scope.items[index].content = resultado.nombre;
					if(esnuevo){
						mi.cargarProyecto();
					}
					$utilidades.mensaje('success','Subproducto '+ (esnuevo ? 'Creado' : 'modificado') + ' con éxito');
				}else{
					$utilidades.mensaje('danger', 'Error al guardar el  subproducto');
				}

			}, function() {
			});
		};
		  
		mi.editarActividad = function(idactividad,index,objetoId,objetoTipo) {

				var modalInstance = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'editarActividad.jsp',
					controller : 'modalEditarActividad',
					controllerAs : 'actividadc',
					backdrop : 'static',
					size : 'md',
					resolve : {
						index : function() {
							return index;
						},
						idactividad: function() {
							return idactividad;
						},
						objetoId: function() {
							return objetoId;
						},
						objetoTipo: function() {
							return objetoTipo;
						}
					}
				});

				modalInstance.result.then(function(resultado) {
					if (resultado != undefined){
						$scope.items[index].content = resultado.nombre;
						$scope.items[index].start = moment(resultado.fechaInicio,'DD/MM/YYYY hh:mm:ss').toDate();
						$scope.items[index].finish = mi.sumarDias($scope.items[index].start,resultado.duracion); 
						$scope.items[index].duration = Number(resultado.duracion);
						mi.reconfigurarFechas($scope.items[index],(index +1),Number($scope.items[index].duracion), $scope.items[index].start);
						
						if (objetoId!=null && objetoTipo!=null)
							mi.cargarProyecto();
						$utilidades.mensaje('success','Item ' + (objetoId!=null ? 'creado' : 'modificado') + ' con éxito');
					}else{
						$utilidades.mensaje('danger', 'Error al guardar el item de actividad');
					}
				}, function() {
				});
		};
		
		mi.sumarDias = function(fecha, dias){
			var cnt = 0;
		    var tmpDate = moment(fecha);
		    while (cnt < dias) {
		        tmpDate = tmpDate.add(1,'days');
		        if (tmpDate.weekday() != moment().day("Sunday").weekday() && tmpDate.weekday() != moment().day("Saturday").weekday()) {
		            cnt = cnt + 1;
		        }
		    }
		    tmpDate = moment(tmpDate,'DD/MM/YYYY').toDate();
		    return tmpDate;
		}
		
		mi.reconfigurarFechas = function(objeto,index,duracion,fechaInicial){
			
			for(var i=index; i< $scope.items.length; i++){
				if ($scope.items[i].predecessors != null || $scope.items[i].predecessors != undefined){
					var temp = $scope.items[i].predecessors[0].item;
					if ($scope.items[i].predecessors[0].item.id === objeto.id){
						$scope.items[i].start =  (mi.sumarDias (fechaInicial,duracion));
						duracion = duracion + Number( $scope.items[i].duracion);
						$scope.items[i].finish = (mi.sumarDias (fechaInicial,duracion));
						mi.reconfigurarFechas($scope.items[i],(i +1),duracion,fechaInicial);
					}
				}		
			}
		}
		
		mi.pesoProducto = function(idProyecto) {

			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'pesoProducto.jsp',
				controller : 'modalPesoProducto',
				controllerAs : 'pesoc',
				backdrop : 'static',
				size : 'md',
				resolve : {
					idProyecto : function() {
						return idProyecto;
					}
				}
			});

			modalInstance.result.then(function(resultado) {
				if (resultado != undefined){
					$utilidades.mensaje('success','Los pesos de los productos se cargaron con éxito');
				}else{
					$utilidades.mensaje('danger', 'Error al guardar los pesos');
				}
			}, function() {
			});
	};	
	
	mi.calcularTamanosPantalla = function(){
		//mi.anchoPantalla = Math.floor(document.getElementById("reporte").offsetHeight);
		mi.anchoGantt = Math.floor(document.getElementById("gantt").offsetHeight);
		//mi.anchoPantalla = mi.anchoPantalla - (mi.anchoPantalla * 0.18);
		//mi.anchoGantt = {"height" : + Math.round(mi.anchoGantt) + "px"};
		mi.anchoGantt = Math.round(mi.anchoGantt - 32) + "px";
	}
	
	mi.calcularTamanosPantalla();
	
	angular.element($window).bind('resize', function(){ 
        mi.calcularTamanosPantalla();
        $scope.$digest();
    });
	
	mi.nuevaActividad = function(objetoId, objetoTipo){
		mi.editarActividad(0,0,objetoId,objetoTipo);
	};
	
	var customHtml = '<div style="cursor: pointer; background-color: #f5f5f5; padding: 0px 19px; font-weight: bold;">' +
    'Nuevo </div>';
	
	
	var customItem = {
			enabled: function() {return false},
		    html: customHtml
		 };
	
	$scope.menuOptions = [
		
        ['<div class="glyphicon glyphicon-th"></div><span>&nbsp;Nuevo componente</span>', function () {
            mi.editarComponente (0,0,mi.objetoId,true);
            mi.objetoId = null;
	    	mi.objetoTipo = null;
        }, function() {
            return mi.objetoTipo==1; 
        }],
        
        ['<div class="glyphicon glyphicon-certificate"></div><span>&nbsp;Nuevo producto</span>', function () {
           mi.editarProducto (0,0,mi.objetoId,true);
           mi.objetoId = null;
           mi.objetoTipo = null;
        }, function() {
            return mi.objetoTipo==2; 
        }],
        ['<div class="glyphicon glyphicon-link"></div><span>&nbsp;Nuevo subproducto</span>', function () {
        	mi.editarSubproducto(0,0,mi.objetoId,true);
        	mi.objetoId = null;
	    	mi.objetoTipo = null;
            
        }, function() {
            return mi.objetoTipo==3; 
        }],
        ['<div class="glyphicon glyphicon-th-list"></div><span>&nbsp;Nueva actividad</span>', function () {
        	if (mi.objetoId != null && mi.objetoTipo != null){
        		
        		mi.nuevaActividad (mi.objetoId,mi.objetoTipo);
        	}
        	
        	mi.objetoId = null;
	    	mi.objetoTipo = null;
         }, function() {
             return true; 
         }]
        
        
    ];
	
	}// fin function controller

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

app.controller('modalEditarPrestamo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$uibModal', '$q', 'idprestamo', modalEditarPrestamo ]);

function modalEditarPrestamo($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, idprestamo) {

	var mi = this;
	mi.proyecto = {};
	
	$http.post('/SProyecto',{ accion: 'getProyectoPorId', id:idprestamo,t: (new Date()).getTime()
	  }).then(

	function(response) {
		mi.proyecto = response.data.proyecto;
		
		mi.poryectotipoid = mi.proyecto.proyectotipoid;
		mi.proyectotiponombre=mi.proyecto.proyectotipo;
		mi.unidadejecutoraid=mi.proyecto.unidadejecutoraid;
		mi.unidadejecutoranombre=mi.proyecto.unidadejecutora;
		mi.cooperanteid=mi.proyecto.cooperanteid;
		mi.cooperantenombre=mi.proyecto.cooperante;
	});
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
};
	
	mi.buscarProyectoTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SProyectoTipo', {
			accion : 'numeroProyectoTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getProyectoTipoPagina',
				pagina : pagina,
				numeroproyectotipo : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.poryectotipoid= itemSeleccionado.id;
			mi.proyectotiponombre = itemSeleccionado.nombre;			
		});
	};
	
	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora');

		resultado.then(function(itemSeleccionado) {
			mi.unidadejecutoraid= itemSeleccionado.unidadEjecutora;
			mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;
		});
	};

	mi.buscarCooperante = function(prestamo) {
		var resultado = mi.llamarModalBusqueda('/SCooperante', {
			accion : 'numeroCooperantes', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getCooperantesPagina',
				pagina : pagina,
				numerocooperantes : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			if (prestamo){
				mi.prestamo.cooperanteid= itemSeleccionado.id;
				mi.prestamo.cooperantenombre = itemSeleccionado.nombre;
			}
			else{
				mi.cooperanteid= itemSeleccionado.id;
				mi.cooperantenombre = itemSeleccionado.nombre;
			}
		});
		
		mi.buscarCooperante = function(prestamo) {
			var resultado = mi.llamarModalBusqueda('/SCooperante', {
				accion : 'numeroCooperantes', t:moment().unix()
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getCooperantesPagina',
					pagina : pagina,
					numerocooperantes : elementosPorPagina
				};
			},'id','nombre');

			resultado.then(function(itemSeleccionado) {
				if (prestamo){
					mi.prestamo.cooperanteid= itemSeleccionado.id;
					mi.prestamo.cooperantenombre = itemSeleccionado.nombre;
				}
				else{
					mi.cooperanteid= itemSeleccionado.id;
					mi.cooperantenombre = itemSeleccionado.nombre;
				}
			});
		};
	};
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarModal',
				id: mi.proyecto.id,
				nombre: mi.proyecto.nombre,
				proyectotipoid: mi.poryectotipoid,
				unidadejecutoraid: mi.unidadejecutoraid,
				cooperanteid: mi.cooperanteid,
				esnuevo: false,
				t:moment().unix()
			};
			$http.post('/SProyecto',param_data).then(
				function(response) {
					if (response.data.success) {
						$uibModalInstance.close(response.data.proyecto);
					}else
						$uibModalInstance.close(undefined);
			});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}


app.controller('modalEditarComponente', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$uibModal', '$q', 'idcomponente', 'objetoId','esnuevo', modalEditarComponente ]);

function modalEditarComponente($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, idcomponente,objetoId,esnuevo) {

	var mi = this;
	mi.componente = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.fechaInicio =  "";
	mi.fechaFin = "";
	
	mi.dimensiones = [
		{value:0,nombre:'Seleccione una opción'},
		{value:1,nombre:'Dias',sigla:'d'}
	];
	
	mi.duracionDimension = mi.dimensiones[0];

	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	if (idcomponente!=null && idcomponente!= undefined && idcomponente != ''){
		$http.post('/SComponente',{ accion: 'getComponentePorId', id:idcomponente,t:moment().unix()
		
		  }).then(
	
		function(response) {
			if (response.data.componente!=null){
				mi.componente = response.data.componente;
				mi.unidadejecutoraid= mi.componente.unidadejecutoraid;
				mi.unidadejecutoranombre= mi.componente.unidadejecutoranombre;
				mi.componentetipoid=mi.componente.componentetipoid;
				mi.componentetiponombre=mi.componente.componentetiponombre;
				mi.fechaInicio =  moment(mi.componente.fechaInicio,'DD/MM/YYYY').toDate();
				mi.fechaFin = moment(mi.componente.fechaFin,'DD/MM/YYYY').toDate();
				mi.duracion = mi.componente.duracion;
				mi.duracionDimension = mi.dimensiones[1];
			}
		});
	}
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};


	mi.buscarComponenteTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SComponenteTipo', {
			accion : 'numeroComponenteTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getComponentetiposPagina',
				pagina : pagina,
				numerocomponentetipos : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.componentetipoid = itemSeleccionado.id;
			mi.componentetiponombre = itemSeleccionado.nombre;
		});
	};

	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora');

		resultado.then(function(itemSeleccionado) {
			mi.unidadejecutoraid = itemSeleccionado.unidadEjecutora;
			mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;
		});
	};
	
	mi.abrirPopupFecha = function(index) {
		if(index > 0 && index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		}
	};
	
	mi.cambioDuracion = function(dimension){
		mi.fechaFin = mi.sumarDias(mi.fechaInicio,mi.duracion, dimension.sigla);
	}
	
	mi.sumarDias = function(fecha, dias, dimension){
		if(dimension != undefined && dias != undefined && fecha != ""){
			var cnt = 0;
		    var tmpDate = moment(fecha);
		    while (cnt < (dias -1 )) {
		    	if(dimension=='d'){
		    		tmpDate = tmpDate.add(1,'days');	
		    	}
		        if (tmpDate.weekday() != moment().day("Sunday").weekday() && tmpDate.weekday() != moment().day("Saturday").weekday()) {
		            cnt = cnt + 1;
		        }
		    }
		    tmpDate = moment(tmpDate,'DD/MM/YYYY').toDate();
		    return tmpDate;
		}
	}
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarModal',
				componentetipoid : mi.componentetipoid,
				id: mi.componente.id,
				nombre: mi.componente.nombre,
				unidadejecutoraid:mi.unidadejecutoraid,
				proyectoId:objetoId,
				esnuevo:esnuevo,
				fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'),
				fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
				duaracion: mi.duracion,
				duracionDimension: mi.duracionDimension.value,
				t:moment().unix()
			};
			$http.post('/SComponente',param_data).then(
				function(response) {
					if (response.data.success) {
						$uibModalInstance.close(response.data.componente);
					}else
						$uibModalInstance.close(undefined);
			});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.borrar = function(){
		$http.post('/SComponente', {
			accion: 'borrarComponente',
			id: mi.componente.id
		}).success(function(response){
			if(response.success){
				$uibModalInstance.dismiss('borrar');
			}
			else
				$uibModalInstance.close(undefined);
				
		});
	};
}


app.controller('modalEditarProducto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$uibModal', '$q', 'idproducto', 'componenteId', 'esnuevo',modalEditarProducto ]);

function modalEditarProducto($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, idproducto, componenteId, esnuevo) {

	var mi = this;
	mi.componente = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.fechaInicio =  "";
	mi.fechaFin = "";
	
	mi.dimensiones = [
		{value:0,nombre:'Seleccione una opción'},
		{value:1,nombre:'Dias',sigla:'d'}
	];
	
	mi.duracionDimension = mi.dimensiones[0];
	
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	if (idproducto!=null && idproducto!= undefined && idproducto != ''){
		
		$http.post('/SProducto',{ accion: 'getProductoPorId', id:idproducto,t:moment().unix()
		  }).then(
	
		function(response) {
			if (response.data.producto!=null){
				mi.producto = response.data.producto;
				mi.productoPadreNombre = mi.producto.producto;
				mi.unidadEjecutora = mi.producto.unidadEjectuora;
				mi.unidadEjecutoraNombre = mi.producto.nombreUnidadEjecutora;
				mi.tipo = mi.producto.idProductoTipo;
				mi.tipoNombre = mi.producto.productoTipo;
				mi.fechaInicio =  moment(mi.producto.fechaInicio,'DD/MM/YYYY').toDate();
				mi.fechaFin = moment(mi.producto.fechaFin,'DD/MM/YYYY').toDate();
				mi.duracion = mi.producto.duracion;
				mi.duracionDimension = mi.dimensiones[1];
			}
		});
	}
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};

	mi.buscarTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SProductoTipo', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.tipo = itemSeleccionado.id;
			mi.tipoNombre = itemSeleccionado.nombre;
		});

	};
	
	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora');

		resultado.then(function(itemSeleccionado) {
			mi.unidadEjecutora = itemSeleccionado.unidadEjecutora;
			mi.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
		});
	};
	
	mi.abrirPopupFecha = function(index) {
		if(index > 0 && index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		}
	};
	
	mi.cambioDuracion = function(dimension){
		mi.fechaFin = mi.sumarDias(mi.fechaInicio,mi.duracion, dimension.sigla);
	}
	
	mi.sumarDias = function(fecha, dias, dimension){
		if(dimension != undefined && dias != undefined && fecha != ""){
			var cnt = 0;
		    var tmpDate = moment(fecha);
		    while (cnt < (dias -1 )) {
		    	if(dimension=='d'){
		    		tmpDate = tmpDate.add(1,'days');	
		    	}
		        if (tmpDate.weekday() != moment().day("Sunday").weekday() && tmpDate.weekday() != moment().day("Saturday").weekday()) {
		            cnt = cnt + 1;
		        }
		    }
		    tmpDate = moment(tmpDate,'DD/MM/YYYY').toDate();
		    return tmpDate;
		}
	}
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarModal',
				id: mi.producto.id,
				nombre : mi.producto.nombre,
				tipoproductoid : mi.tipo,
				unidadEjecutora : mi.unidadEjecutora,
				componenteId : componenteId,
				esnuevo:esnuevo,
				fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'),
				fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
				duaracion: mi.duracion,
				duracionDimension: mi.duracionDimension.value,
				t:moment().unix()
			};
			$http.post('/SProducto',param_data).then(
				function(response) {
					if (response.data.success) {
						$uibModalInstance.close(response.data.producto);
					}else
						$uibModalInstance.close(undefined);
			});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.borrar = function(){
		var datos = {
				accion : 'borrar',
				codigo : mi.producto.id
		};
		$http.post('/SProducto', datos).success(
				function(response) {
					if (response.success) {
						$uibModalInstance.dismiss('borrar');
					}
					else
						$uibModalInstance.close(undefined);
				});
	};
}


app.controller('modalEditarSubproducto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$uibModal', '$q', 'idsubproducto','productoId','esnuevo', modalEditarSubproducto ]);

function modalEditarSubproducto($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, idsubproducto,productoId,esnuevo) {

	var mi = this;
	mi.componente = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.fechaInicio =  "";
	mi.fechaFin = "";
	
	mi.dimensiones = [
		{value:0,nombre:'Seleccione una opción'},
		{value:1,nombre:'Dias',sigla:'d'}
	];
	
	mi.duracionDimension = mi.dimensiones[0];
	
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	if (idsubproducto!=null && idsubproducto!= undefined && idsubproducto != ''){
		$http.post('/SSubproducto',{ accion: 'getSubproductoPorId', id:idsubproducto,t:moment().unix()
		  }).then(
	
		function(response) {
			if (response.data.subproducto!=null){
				mi.subproducto = response.data.subproducto;
				mi.tipo = mi.subproducto.subProductoTipoId;
				mi.tipoNombre = mi.subproducto.subProductoTipo;
				
				mi.unidadEjecutora = mi.subproducto.unidadEjecutora;
				mi.unidadEjecutoraNombre = mi.subproducto.nombreUnidadEjecutora;
				mi.fechaInicio =  moment(mi.subproducto.fechaInicio,'DD/MM/YYYY').toDate();
				mi.fechaFin = moment(mi.subproducto.fechaFin,'DD/MM/YYYY').toDate();
				mi.duracion = mi.subproducto.duracion;
				mi.duracionDimension = mi.dimensiones[1];
			}
		});
	}
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};

	mi.buscarTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SSubproductoTipo', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.tipo = itemSeleccionado.id;
			mi.tipoNombre = itemSeleccionado.nombre;
			
			var parametros = { 
				accion: 'getSubproductoPropiedadPorTipo', 
				idsubproducto: mi.subproducto.id,
				idsubproductotipo: itemSeleccionado.id
			}
			$http.post('/SSubproductoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.subproductopropiedades;
				for (campos in mi.camposdinamicos) {
					switch (mi.camposdinamicos[campos].tipo){
						case "fecha":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
							break;
						case "entero":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;
						case "decimal":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;
					}
				}
				
			});
		});

	};
	
	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora');

		resultado.then(function(itemSeleccionado) {
			mi.unidadEjecutora = itemSeleccionado.unidadEjecutora;
			mi.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
		});
	};
	
	mi.abrirPopupFecha = function(index) {
		if(index > 0 && index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		}
	};
	
	mi.cambioDuracion = function(dimension){
		mi.fechaFin = mi.sumarDias(mi.fechaInicio,mi.duracion, dimension.sigla);
	}
	
	mi.sumarDias = function(fecha, dias, dimension){
		if(dimension != undefined && dias != undefined && fecha != ""){
			var cnt = 0;
		    var tmpDate = moment(fecha);
		    while (cnt < (dias -1 )) {
		    	if(dimension=='d'){
		    		tmpDate = tmpDate.add(1,'days');	
		    	}
		        if (tmpDate.weekday() != moment().day("Sunday").weekday() && tmpDate.weekday() != moment().day("Saturday").weekday()) {
		            cnt = cnt + 1;
		        }
		    }
		    tmpDate = moment(tmpDate,'DD/MM/YYYY').toDate();
		    return tmpDate;
		}
	}
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarModal',
				id: mi.subproducto.id,
				nombre : mi.subproducto.nombre,
				tiposubproductoid : mi.tipo,
				unidadEjecutora : mi.unidadEjecutora,
				productoId : productoId,
				esnuevo:esnuevo,
				fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'),
				fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
				duaracion: mi.duracion,
				duracionDimension: mi.duracionDimension.value,
				t:moment().unix()
			};
			$http.post('/SSubproducto',param_data).then(
				function(response) {
					if (response.data.success) {
						$uibModalInstance.close(response.data.subproducto);
					}else
						$uibModalInstance.close(undefined);
			});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.borrar = function() {
		var datos = {
			accion : 'borrar',
			codigo : mi.subproducto.id
		};
		$http.post('/SSubproducto', datos).success(
				function(response) {
					if (response.success) {
						$uibModalInstance.dismiss('borrar');
					}
					else
						$uibModalInstance.close(undefined);
				});
	}
}


app.controller('modalEditarActividad', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q' ,'idactividad','objetoId','objetoTipo',modalEditarActividad ]);

function modalEditarActividad($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q,idactividad,objetoId,objetoTipo) {

	var mi = this;
	mi.actividad = {};
	mi.primeraActividad=false;
	
	mi.dimensiones = [{id:1,nombre:'Dias',sigla:'d'}];
	
	mi.formatofecha = 'dd/MM/yyyy';
	
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};

	mi.ff_opciones = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	mi.abrirPopupFecha = function(index) {
		if(index > 0 && index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1000: mi.fi_abierto = true; break;
			}
		}
	};
	
	if (idactividad!=null && idactividad!= undefined && idactividad != ''){
	
		$http.post('/SActividad',{ accion: 'getActividadPorId', id:idactividad,t:moment().unix()
		  }).then(
	
			function(response) {
				mi.actividad = response.data.actividad;
				mi.actividad.fechaInicio = moment(mi.actividad.fechaInicio,'DD/MM/YYYY').toDate();
				mi.actividad.fechaFin = moment(mi.actividad.fechaFin,'DD/MM/YYYY').toDate();
				mi.ff_opciones.minDate = mi.actividad.fechaInicio;
				mi.duracionDimension = {
						"id": mi.actividad.duracionDimension === 'd' ? 1 : 0,
						"nombre": mi.actividad.duracionDimension,
						"sigla": 'd'
				};
				mi.primeraActividad = mi.actividad.prececesorId == undefined 
					|| mi.actividad.prececesorId == null ? true : false;
				mi.esNuevo = false;
		});
	}else{
		mi.actividad = {};
		mi.esNuevo = true;
	}
	
	mi.cambioDuracion = function(){
		mi.actividad.fechaFin = mi.sumarDias(mi.actividad.fechaInicio,mi.actividad.duracion);
	}
	
	mi.sumarDias = function(fecha, dias){
		var cnt = 0;
	    var tmpDate = moment(fecha);
	    while (cnt < (dias -1 )) {
	        tmpDate = tmpDate.add(1,'days');
	        if (tmpDate.weekday() != moment().day("Sunday").weekday() && tmpDate.weekday() != moment().day("Saturday").weekday()) {
	            cnt = cnt + 1;
	        }
	    }
	    tmpDate = moment(tmpDate,'DD/MM/YYYY').toDate();
	    return tmpDate;
	}
	
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};

	mi.buscarTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SActividadTipo', {
			accion : 'numeroActividadTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getActividadtiposPagina',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.actividad.actividadtipoid = itemSeleccionado.id;
			mi.actividad.actividadtiponombre = itemSeleccionado.nombre;
		});

	};
	
	
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarModal',
				actividadtipoid : mi.actividad.actividadtipoid,
				id: mi.actividad.id,
				nombre: mi.actividad.nombre,
				descripcion: mi.actividad.descripcion,
				fechainicio: moment(mi.actividad.fechaInicio).format('DD/MM/YYYY'),
				fechafin: moment(mi.actividad.fechaFin).format('DD/MM/YYYY'),
				porcentajeavance: mi.actividad.porcentajeavance,
				duracion:mi.actividad.duracion,
				duracionDimension:mi.duracionDimension.sigla,
				objetoId : objetoId,
				objetoTipo: objetoTipo,
				esnuevo : mi.esNuevo,
				t:moment().unix()
			};
			$http.post('/SActividad',param_data).then(
				function(response) {
					if (response.data.success) {
						$uibModalInstance.close(response.data.actividad);
					}else
						$uibModalInstance.close(undefined);
			});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.borrar = function() {
			$http.post('/SActividad', {
				accion: 'borrarActividad',
				id: mi.actividad.id
			}).success(function(response){
				if(response.success){
					$uibModalInstance.dismiss('borrar');
				}
				else
					$uibModalInstance.close(undefined);
			});
		}

}

app.controller('buscarPorProyecto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre',buscarPorProyecto ]);

function buscarPorProyecto($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga,$columnaId,$columnaNombre) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post($servlet, $accionServlet).success(function(response) {
		for ( var key in response) {
			mi.totalElementos = response[key];
		}
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : $columnaId,
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
		}, {
			displayName : 'Nombre',
			name : $columnaNombre,
			cellClass : 'grid-align-left'
		} ],
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect : false,
		modifierKeysToMultiSelect : false,
		noUnselect : false,
		enableFiltering : true,
		enablePaginationControls : false,
		paginationPageSize : 5,
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;
			mi.gridApi.selection.on.rowSelectionChanged($scope,
					mi.seleccionarTipoRiesgo);
		}
	}

	mi.seleccionarTipoRiesgo = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina)).then(
				function(response) {
					if (response.data.success) {

						for ( var key in response.data) {
							if (key != 'success')
								mi.data = response.data[key];
						}
						mi.opcionesGrid.data = mi.data;

						mi.mostrarCargando = false;
					}
				});
	};

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}

app.controller('modalPesoProducto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q' ,'idProyecto' ,'uiGridConstants', 'uiGridGroupingConstants',modalPesoProducto ]);

function modalPesoProducto($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q,idProyecto,uiGridConstants, uiGridGroupingConstants) {

	var mi = this;
	mi.productos = {};
	mi.modificarTabla = false;
	mi.mostrarcargando = true;
	mi.pesoTotal = 0;
	mi.gridOptions = {
		showColumnFooter: true,
		enableCellSelection:true,
		enableCellEdit: true,
		enableCellEditOnFocus: true,
		rowEditWaitInterval: -1,
		expandAllRows : true,
		enableExpandableRowHeader: false,
		showTreeRowHeader: false,
		showTreeExpandNoChildren : false,
		enableColumnMenus: false,
		enableSorting: false,
	    columnDefs: [
	        { name: 'nombre', pinnedLeft:true, enableCellEdit: false, displayName: 'Productos',
	        	
	        },
	        { name: 'peso', width: 70, displayName: 'Peso', type: 'number' 
	        	,aggregationType: uiGridConstants.aggregationTypes.sum
	        }
	    ],
	    onRegisterApi: function( gridApi ) {
	      mi.gridApi = gridApi;
	      
	       
	      
	      mi.gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
	    	  if(mi.modificarTabla){
	    		  mi.pesoTotal = mi.pesoTotal - oldValue + newValue;
	    	  }else{
	    		  //mi.setValor(rowEntity,colDef,oldValue);
	    	  }
	    	  
	    	 
	    	  
	         var gridRows = mi.gridApi.rowEdit.getDirtyRows();
	         var dataRows = gridRows.map( function( gridRow ) { return gridRow.entity; });
	         mi.gridApi.rowEdit.setRowsClean( dataRows );
	      });
	     mi.gridApi.rowEdit.on.saveRow($scope, mi.saveRow);
	  }
	};
	
	
	
	$http.post('/SProducto',{ accion: 'getProductoPorProyecto', idProyecto:idProyecto , t:moment().unix()
	  }).then(

		function(response) {
			var data = response.data.productos;
			mi.pesoTotal = 0;
			for (i in data){
				data[i].peso = data[i].peso === undefined  || data[i].peso === '' ? 0 : Number(data[i].peso);
				mi.pesoTotal = + Number(data[i].peso);
			}
			mi.modificarTabla = mi.pesoTotal === 0;
			
			mi.gridOptions.data = data;
			mi.mostrarcargando = false;
	});
	
	
	mi.ok = function() {
		var p_pesos= "";
		for (i in mi.gridOptions.data){
			p_pesos = p_pesos + (p_pesos.length > 0 ? "~" : "") + mi.gridOptions.data[i].id
				+ "," + mi.gridOptions.data[i].peso;
		}
		var param_data = {
				accion : 'guardarPesoProducto',
				productos :  p_pesos,
				t:moment().unix()
			};
			$http.post('/SProducto',param_data).then(
				function(response) {
					if (response.data.success) {
						$uibModalInstance.close(response);
					}else
						$uibModalInstance.close(undefined);
			});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};


}




