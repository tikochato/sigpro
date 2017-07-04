var app = angular.module('informacionPresupuestariaController',['ngAnimate', 'ngTouch', 'ui.grid.edit', 'ui.grid.rowEdit']);

app.controller('adquisicionesController', ['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion){
		var mi = this;
		mi.gridOptions = {};
		mi.mostrarcargando=false;
		i18nService.setCurrentLang('es');
		mi.modificarTabla = true;
		mi.mostrarDescargar = false;
		mi.totalP = 0;
		mi.totales = [];
		mi.columnaNames = [];
		mi.informeCompleto = false;
		mi.categorias = [];
		mi.menuItems = [];
		mi.columnasDinamicas =[];
		
		var AGRUPACION_MES= 1;
		var AGRUPACION_BIMESTRE = 2;
		var AGRUPACION_TRIMESTRE = 3;
		var AGRUPACION_CUATRIMESTRE= 4;
		var AGRUPACION_SEMESTRE= 5;
		var AGRUPACION_ANUAL= 6;
		
		var MES_NAME = ['Mes1','Mes2','Mes3','Mes4','Mes5','Mes6','Mes7','Mes8','Mes9','Mes10','Mes11','Mes12'];
		var MES_DISPLAY_NAME = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
		var BIMESTRE_NAME = ['Bimestre1', 'Bimestre2', 'Bimestre3', 'Bimestre4', 'Bimestre5', 'Bimestre6'];
		var BIMESTRE_DISPLAY_NAME = ['Bimestre 1', 'Bimestre 2','Bimestre 3','Bimestre 4','Bimestre 5','Bimestre 6'];
		var TRIMESTRE_NAME = ['Trimestre1', 'Trimestre2','Trimestre3','Trimestre4'];
		var TRIMESTRE_DISPLAY_NAME = ['Trimestre 1', 'Trimestre 2', 'Trimestre 3', 'Trimestre 4'];
		var CUATRIMESTRE_NAME = ['Cuatrimestre1','Cuatrimestre2','Cuatrimestre3'];
		var CUATRIMESTRE_DISPLAY_NAME = ['Cuatrimestre 1', 'Cuatrimestre 2', 'Cuatrimestre 3'];
		var SEMESTRE_NAME = ['Semestre1','Semestre2'];
		var SEMESTRE_DISPLAY_NAME = ['Semestre 1','Semestre 2'];
		
		mi.agrupaciones = [
			{'value' : 0, 'text' : 'Seleccione una opción'},
			{'value' : AGRUPACION_MES, 'text' : 'Mes'},
			{'value' : AGRUPACION_BIMESTRE, 'text' : 'Bimestre'},
			{'value' : AGRUPACION_TRIMESTRE, 'text' : 'Trimestre'},
			{'value' : AGRUPACION_CUATRIMESTRE, 'text' : 'Cuatrimestre'},
			{'value' : AGRUPACION_SEMESTRE, 'text' : 'Semestre'},
			{'value' : AGRUPACION_ANUAL, 'text' : 'Anual'},
		];	

		mi.agrupacion = mi.agrupaciones[0];
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	    
	    mi.reiniciarVista=function(){
			if($location.path()=='/informacionPresupuestaria/rv')
				$route.reload();
			else
				$location.path('/informacionPresupuestaria/rv');
		}
	    
		mi.formatofecha = 'yyyy';
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.prestamo = mi.prestamos[0];
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		};
		
		mi.fechaOptions = {
				formatYear: 'yyyy',
			    startingDay: 1,
			    minMode: 'year'
		};
		
		mi.getPrestamos = function(){
			$http.post('/SProyecto',{accion: 'getProyectos'}).success(
				function(response) {
					mi.prestamos = [];
					mi.prestamos.push({'value' : 0, 'text' : 'Seleccione una opción'});
					if (response.success){
						for (var i = 0; i < response.entidades.length; i++){
							mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
						}
						mi.prestamo = mi.prestamos[0];
					}
				});
		}
		
		mi.generarTabla = function(){
			mi.columnas = [];
			mi.columnaNames = [];
			mi.cabeceras = [];
			mi.categorias = [];
			mi.columnasDinamicas = [];
			
			var fecha = new Date();
			var m = fecha.getMonth() +1;
			var y = fecha.getFullYear();
			
			var i = 0;
			var j = 0;
			var mesName = "";
			mi.columnas.push({ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: '', enableColumnMenu: false,
	        	cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.hijo.length != 0}\"><div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>",
	        	footerCellTemplate: '<div class="ui-grid-cell-contents">Total</div>',
	        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
	        		if(row.entity.hijo.length == 0)
	        			return "actividad";
	        		else
	        			return "padre";
	            }
	        });
			
			mi.columnaNames.push("nombre");
			mi.cabeceras.push("nombre");
			
			if(mi.agrupacion.value == AGRUPACION_MES){
				var fInicial = moment(mi.fechaInicio).format('YYYY');
				var fFinal = moment(mi.fechaFin).format('YYYY');
				
				for(j = fInicial; j <= fFinal; j++){
					mi.categorias.push({name: j.toString(), visible: true, showCatName: true});
					
					for(i = 1; i <= 12; i++){
						mesName = MES_NAME[i-1];
						
						mesDisplayName = MES_DISPLAY_NAME[i-1];
						
						mi.totales[mesName + "-" + j + "-P"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-P");
						mi.columnasDinamicas.push(mesName+ "-" + j + "-P");
						mi.cabeceras.push(mesDisplayName);
						mi.columnas.push({ name: mesName + "-" + j + "-P", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "planificado";
				            }
				        });
						
						mi.totales[mesName + "-" + j + "-R"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-R");
						mi.columnasDinamicas.push(mesName + "-" + j + "-R");
						mi.cabeceras.push(mesDisplayName);
						mi.columnas.push({ name: mesName + "-" + j + "-R", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "real";
				            }
				        });
					}
				}
			}else if (mi.agrupacion.value == AGRUPACION_BIMESTRE){
				var fInicial = moment(mi.fechaInicio).format('YYYY');
				var fFinal = moment(mi.fechaFin).format('YYYY');
				
				for(j = fInicial; j <= fFinal; j++){
					mi.categorias.push({name: j.toString(), visible: true, showCatName: true});
					
					for(i = 1; i <= 6; i++){
						mesName = BIMESTRE_NAME[i-1];
						
						mesDisplayName = BIMESTRE_DISPLAY_NAME[i-1];
						
						mi.totales[mesName + "-" + j + "-P"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-P");
						mi.columnasDinamicas.push(mesName+ "-" + j + "-P");
						mi.cabeceras.push(mesDisplayName);
						
						mi.columnas.push({ name: mesName + "-" + j + "-P", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "planificado";
				            }
				        });
						
						mi.totales[mesName + "-" + j + "-R"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-R");
						mi.columnasDinamicas.push(mesName + "-" + j + "-R");
						mi.cabeceras.push(mesDisplayName);
						mi.columnas.push({ name: mesName + "-" + j + "-R", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "real";
				            }
				        });
					}
				}
			}else if (mi.agrupacion.value == AGRUPACION_TRIMESTRE){
				var fInicial = moment(mi.fechaInicio).format('YYYY');
				var fFinal = moment(mi.fechaFin).format('YYYY');
				
				for(j = fInicial; j <= fFinal; j++){
					mi.categorias.push({name: j.toString(), visible: true, showCatName: true});
					
					for(i = 1; i <= 4; i++){
						mesName = TRIMESTRE_NAME[i-1];
						
						mesDisplayName = TRIMESTRE_DISPLAY_NAME[i-1];
						
						mi.totales[mesName + "-" + j + "-P"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-P");
						mi.columnasDinamicas.push(mesName+ "-" + j + "-P");
						mi.cabeceras.push(mesDisplayName);
						
						mi.columnas.push({ name: mesName + "-" + j + "-P", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "planificado";
				            }
				        });
						
						mi.totales[mesName + "-" + j + "-R"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-R");
						mi.columnasDinamicas.push(mesName + "-" + j + "-R");
						mi.cabeceras.push(mesDisplayName);
						mi.columnas.push({ name: mesName + "-" + j + "-R", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "real";
				            }
				        });
					}
				}
			}else if (mi.agrupacion.value == AGRUPACION_CUATRIMESTRE){
				var fInicial = moment(mi.fechaInicio).format('YYYY');
				var fFinal = moment(mi.fechaFin).format('YYYY');
				
				for(j = fInicial; j <= fFinal; j++){
					mi.categorias.push({name: j.toString(), visible: true, showCatName: true});
					
					for(i = 1; i <= 3; i++){
						mesName = CUATRIMESTRE_NAME[i-1];
						
						mesDisplayName = CUATRIMESTRE_DISPLAY_NAME[i-1];
						
						mi.totales[mesName + "-" + j + "-P"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-P");
						mi.columnasDinamicas.push(mesName+ "-" + j + "-P");
						mi.cabeceras.push(mesDisplayName);
						
						mi.columnas.push({ name: mesName + "-" + j + "-P", enableCellEdit: false, width: 130, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "planificado";
				            }
				        });
						
						mi.totales[mesName + "-" + j + "-R"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-R");
						mi.columnasDinamicas.push(mesName + "-" + j + "-R");
						mi.cabeceras.push(mesDisplayName);
						mi.columnas.push({ name: mesName + "-" + j + "-R", enableCellEdit: false, width: 130, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "real";
				            }
				        });
					}
				}
			}else if (mi.agrupacion.value == AGRUPACION_SEMESTRE){
				var fInicial = moment(mi.fechaInicio).format('YYYY');
				var fFinal = moment(mi.fechaFin).format('YYYY');
				
				for(j = fInicial; j <= fFinal; j++){
					mi.categorias.push({name: j.toString(), visible: true, showCatName: true});
					
					for(i = 1; i <= 2; i++){
						mesName = SEMESTRE_NAME[i-1];
						
						mesDisplayName = SEMESTRE_DISPLAY_NAME[i-1];
						
						mi.totales[mesName + "-" + j + "-P"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-P");
						mi.columnasDinamicas.push(mesName+ "-" + j + "-P");
						mi.cabeceras.push(mesDisplayName);
						
						mi.columnas.push({ name: mesName + "-" + j + "-P", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "planificado";
				            }
				        });
						
						mi.totales[mesName + "-" + j + "-R"] = 0;
						mi.columnaNames.push(mesName + "-" + j + "-R");
						mi.columnasDinamicas.push(mesName + "-" + j + "-R");
						mi.cabeceras.push(mesDisplayName);
						mi.columnas.push({ name: mesName + "-" + j + "-R", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
				        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
				        	menuItems: [
				        		{
				        			title: 'Mostrar/Ocultar',
				        			action: mi.mostrarOcultarColumna
				        		}
				        	],
				        	category: j.toString(),
				        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				                return "real";
				            }
				        });
					}
				}
			}else if (mi.agrupacion.value == AGRUPACION_ANUAL){
				var fInicial = moment(mi.fechaInicio).format('YYYY');
				var fFinal = moment(mi.fechaFin).format('YYYY');
				
				for(j = fInicial; j <= fFinal; j++){
					mesName = "Anio";
					
					mesDisplayName = "Año " + j;
					
					mi.totales[mesName + "-" + j + "-P"] = 0;
					mi.columnaNames.push(mesName + "-" + j + "-P");
					mi.columnasDinamicas.push(mesName+ "-" + j + "-P");
					mi.cabeceras.push(mesDisplayName);
					
					mi.columnas.push({ name: mesName + "-" + j + "-P", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
			        	menuItems: [
			        		{
			        			title: 'Mostrar/Ocultar',
			        			action: mi.mostrarOcultarColumna
			        		}
			        	],
			        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                return "planificado";
			            }
			        });
					
					mi.totales[mesName + "-" + j + "-R"] = 0;
					mi.columnaNames.push(mesName + "-" + j + "-R");
					mi.columnasDinamicas.push(mesName + "-" + j + "-R");
					mi.cabeceras.push(mesDisplayName);
					mi.columnas.push({ name: mesName + "-" + j + "-R", enableCellEdit: false, width: 120, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
			        	menuItems: [
			        		{
			        			title: 'Mostrar/Ocultar',
			        			action: mi.mostrarOcultarColumna
			        		}
			        	],
			        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			                return "real";
			            }
			        });
				}
			}
			
			mi.columnas.push({ name: 'Total-P', enableCellEdit: false, width: 150, displayName: 'Total general', type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2',
	        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.totalP | number:2}}</div>', pinnedRight:true, enableHiding: false, enableGrouping: false, enablePinning: false,
	        	menuItems: [
	        		{
	        			title: 'Mostrar/Ocultar',
	        			action: mi.mostrarOcultarColumna
	        		}
	        	],
	        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
	                return "planificadoTotal";
	            }
	        });
			
			mi.columnas.push({ name: 'Total-R', enableCellEdit: false, width: 150, displayName: 'Total general', type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2',
	        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.totalR | number:2}}</div>', pinnedRight:true, enableHiding: false, enableGrouping: false, enablePinning: false,
	        	menuItems: [
	        		{
	        			title: 'Mostrar/Ocultar',
	        			action: mi.mostrarOcultarColumna
	        		}
	        	],
	        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
	                return "realTotal";
	            }
	        });
			
			mi.columnaNames.push("Total-P");
			mi.cabeceras.push("Total-P");
			mi.columnaNames.push("Total-R");
			mi.cabeceras.push("Total-R");
			mi.gridOptions.columnDefs = mi.columnas;
			
			mi.mostrarPlanificado();
		}
		
		mi.mostrarReal = function(){
			var currentTime = new Date();
			var month = currentTime.getMonth() + 1;
			var year = currentTime.getFullYear();
			
			for(x in mi.gridOptions.columnDefs){
				if(mi.gridOptions.columnDefs[x].name.includes("-P")){
					mi.gridOptions.columnDefs[x].visible = false;
					mi.gridOptions.columnDefs[x].ocultar = true;
				}else{
					mi.gridOptions.columnDefs[x].visible = true;
					mi.gridOptions.columnDefs[x].ocultar = false;
				}
			}
			
			mi.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
		}
		
		mi.mostrarPlanificado = function(){
			var currentTime = new Date();
			var month = currentTime.getMonth() + 1;
			var year = currentTime.getFullYear();
			
			for(x in mi.gridOptions.columnDefs){
				if(mi.gridOptions.columnDefs[x].name.includes("-R")){
					mi.gridOptions.columnDefs[x].visible = false;
					mi.gridOptions.columnDefs[x].ocultar = true;
				}else{
					mi.gridOptions.columnDefs[x].visible = true;
					mi.gridOptions.columnDefs[x].ocultar = false;
				}
			}
			
			mi.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
		}
		
		mi.mostrarCombinado = function(){
			for(x in mi.gridOptions.columnDefs){
				mi.gridOptions.columnDefs[x].visible = true;
				mi.gridOptions.columnDefs[x].ocultar = false;
			}
			
			mi.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
		}
		
		mi.getTotal = function(columna){
			return mi.totales[columna.col.name];
		}
		
		mi.mostrarOcultarColumna = function(col){
			var columna = this.context.col.name;
			var columna2 = "";
			var bandera = true;
			
			if (columna.includes("-P")){
				columna2 = columna.replace('-P','-R');
			}else if (columna.includes("-R")){
				columna2 = columna.replace('-R','-P');
			}
				
			for(x in mi.gridOptions.columnDefs){
				if(mi.gridOptions.columnDefs[x].name == columna){
					if(mi.gridOptions.columnDefs[x].ocultar){
						mi.gridOptions.columnDefs[x].visible = false;
						mi.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
						bandera = false;
						break;
					}else
						break;
				}
			}
			
			if (bandera){
				for(x in mi.gridOptions.columnDefs){
					if(mi.gridOptions.columnDefs[x].name == columna2){
						mi.gridOptions.columnDefs[x].visible = true;
						mi.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
					}
				}
			}
			
			mi.calcularTotales();
			mi.totalPrestamo();
		}
		
		mi.generar = function(){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(mi.agrupacion.value != 0){
							$scope.active = 0;
							mi.mostrarcargando=true;
							mi.generarTabla();
							
							$http.post('/SInformePresupuesto',{
								accion: 'generarInforme',
								idPrestamo: mi.prestamo.value,
								informeCompleto: mi.informeCompleto,
								columnaNames: mi.columnasDinamicas.toString(),
								agrupacion: mi.agrupacion.value,
								anio: moment(mi.fechaInicio).format('DD/MM/YYYY')
							}).success(function(response){
								if (response.success){
									mi.crearArbol(response.prestamo);
									mi.data = response.prestamo;								
									mi.mostrarcargando=false;
								}else{
									if (response.prestamo[0].tipoInforme == 2){
										$utilidades.mensaje('warning','No existe informe para modificar');
									}
								}
								mi.mostrarDescargar = true;
							})
						}else
							$utilidades.mensaje('warning','Debe seleccionar la agrupación');
					}else
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}else
					$utilidades.mensaje('warning','Debe seleccionar un año');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
		
		mi.exportarExcel = function(){
		    var reporte = []; 

			var obj = {};
			for(x in mi.gridOptions.data){
				for (y in mi.columnaNames){
					obj[mi.columnaNames[y]] = mi.gridOptions.data[x][mi.columnaNames[y]];
				}
				
				reporte.push(obj);
				obj = {};
			}
			
			for(y in mi.columnaNames){
				if(y == 0)
					obj[mi.columnaNames[y]] = "Total";
				else if (y == mi.columnaNames.length-1)
					obj[mi.columnaNames[y]] = mi.totalP;
				else
					obj[mi.columnaNames[y]] = mi.totales[mi.columnaNames[y]];
			}
			
			reporte.push(obj);
			
			$http.post('/SInformePresupuesto',{
				accion: 'exportarExcel',
				data: JSON.stringify(reporte),
				idPrestamo: mi.prestamo.value,
				anio: moment(mi.fechaInicio).format('DD/MM/YYYY'),
				columnas: mi.columnaNames.toString(),
				cabeceras : mi.cabeceras.toString(),
				t:moment().unix()
			}).then(
			  function successCallback(response) {
					var anchor = angular.element('<a/>');
				    anchor.attr({
				         href: 'data:application/ms-excel;base64,' + response.data,
				         target: '_blank',
				         download: 'Informe.xls'
				     })[0].click();
				  }.bind(this), function errorCallback(response){
				 		
				 	}
				 );
		}
		
		mi.crearArbol = function(datos){
			if (datos.length > 0){
				mi.gridOptions.data = datos;
				
				$timeout(function(){
				     mi.gridApi.treeBase.expandAllRows();
				     mi.totalPrestamo();
				     mi.calcularTotales();
			   })
			}
		}
		
		mi.ddlOpciones = [{ id: 1, value: 'Comienzo' },
            { id: 2, value: 'Prorrateo' },
            { id: 3, value: 'Fin' }];
		
		mi.gridOptions = {
				showColumnFooter: true,
	            cellEditableCondition:function($scope){
	            	  if($scope.row.entity.objetoTipo == 5 && $scope.row.entity.hijo.length == 0)
	            		  return true;
	            	  else
	            		  return false;
	            },
	            headerTemplate: '<div role="rowgroup" class="ui-grid-header"> <div class="ui-grid-top-panel"> <div class="ui-grid-header-viewport"> <div class="ui-grid-header-canvas"> <div align="center" class="ui-grid-header-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"> <div role="row" class="ui-grid-header-cell-row"><div align="center" class="ui-grid-header-cell ui-grid-clearfix ui-grid-category" ng-repeat="cat in grid.appScope.controller.categorias" ng-if="cat.visible && (colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }).length > 0"> <span ng-if="cat.showCatName === true"> {{cat.name}} </span> <br ng-if="cat.showCatName !== true" />  <div class="ui-grid-header-cell ui-grid-clearfix" ng-repeat="col in colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }" ui-grid-header-cell col="col" render-index="$index"> </div> </div><!--!cat.visible && --> <div class="ui-grid-header-cell ui-grid-clearfix" ng-if="col.colDef.category === undefined" ng-repeat="col in colContainer.renderedColumns track by col.uid" ui-grid-header-cell col="col" render-index="$index"> </div> </div> </div> </div> </div> </div> </div>',
	            category: mi.categorias,
				rowEditWaitInterval: -1,
				expandAllRows : true,
				enableExpandableRowHeader: false,
				showTreeRowHeader: false,
				showTreeExpandNoChildren : false,
				enableSorting: false,
			    columnDefs: mi.columnas,
			    onRegisterApi: function(gridApi ) {
			      mi.gridApi = gridApi;
			  }
		};
		
		mi.toggleRow = function(row, evt) {
			uiGridTreeBaseService.toggleRowTreeState(mi.gridApi.grid, row, evt);
		};
		
		mi.totalPrestamo = function(){
			mi.rowEntity = mi.gridOptions.data[0];
			mi.totalP = 0;
			mi.totalR = 0;
			var columna = "";
			for(x in mi.columnasDinamicas){
				if(mi.columnasDinamicas[x].includes("-P")==true){
					columna = mi.columnasDinamicas[x];
					mi.totalP += mi.obtenerValor(mi.rowEntity,columna);
				}else if(mi.columnasDinamicas[x].includes("-R")==true){
					columna = mi.columnasDinamicas[x];
					mi.totalR += mi.obtenerValor(mi.rowEntity,columna);
				}
			}
			
			for(x in mi.columnasDinamicas){
				if(mi.columnasDinamicas[x].includes("-P")==true){
					columna = mi.columnasDinamicas[x];
					mi.totales[columna] = mi.rowEntity[columna];
				}else if(mi.columnasDinamicas[x].includes("-R")==true){
					columna = mi.columnasDinamicas[x];
					mi.totales[columna] = mi.rowEntity[columna];
				}
			}
		}
		
		mi.calcularTotales = function(){
			var montoPlanificado = 0;
			var montoReal = 0;
			
			for(x in mi.gridOptions.data){
				var rowEntity = mi.obtenerEntidad(mi.gridOptions.data[x].idObjetoTipo, mi.gridOptions.data[x].objetoTipo);
				
				for(y in mi.columnasDinamicas){
					if(mi.columnasDinamicas[y].includes("-P")==true){
						montoPlanificado += mi.obtenerValor(rowEntity, mi.columnasDinamicas[y]);
					}else if(mi.columnasDinamicas[y].includes("-R")==true){
						montoReal+= mi.obtenerValor(rowEntity, mi.columnasDinamicas[y]);
					}
				}
				
				rowEntity["Total-P"] = montoPlanificado;
				rowEntity["Total-R"] = montoReal;
				montoPlanificado = 0;
				montoReal = 0;
			}
		}
		
		mi.obtenerEntidad = function(id, objetoTipo){
			for (x in mi.data){
				if (id == mi.data[x].idObjetoTipo && objetoTipo == mi.data[x].objetoTipo){
					return mi.data[x];
				}
			}
		}
		
		mi.obtenerValor = function(rowEntidad, columna){
			return rowEntidad[columna];
		}
		
		mi.getPrestamos();
}])

.filter('mapAcumulacionCosto', function() {
	  var genderHash = {
	    1: 'Comienzo',
	    2: 'Prorrateo',
	    3: 'Fin'
	  };
	 
	  return function(input) {
	    if (!input){
	      return '';
	    } else {
	      return genderHash[input];
	    }
	  };
	})