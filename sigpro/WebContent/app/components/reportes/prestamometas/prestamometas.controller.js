var app = angular.module('prestamometasController', [ 'ui.grid.edit', 'ui.grid.rowEdit', 'ui.grid.pinning', 'ui.grid.autoResize']);


app.controller('prestamometasController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridTreeBaseService','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridTreeBaseService,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	var metaSeleccionada = null;
		
	var AGRUPACION_MES= 1;
	var AGRUPACION_BIMESTRE= 2;
	var AGRUPACION_TRIMESTRE= 3;
	var AGRUPACION_CUATRIMESTRE= 4;
	var AGRUPACION_SEMESTRE= 5;
	var AGRUPACION_ANUAL= 6;
	
	var META_ID_REAL = 1;
	var META_ID_PLANIFICADA = 2;
	var META_ID_LINEABASE= 3;
	var META_ID_FINAL= 4;
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;

	mi.mostrarCargando = false;
	mi.mostrarDescargar = false;
	mi.prestamos = [];
	mi.data = [];	
	
	$window.document.title = $utilidades.sistema_nombre+' - Metas de Préstamo';
	i18nService.setCurrentLang('es');
	
	$http.post('/SMeta', { accion: 'getMetasUnidadesMedida' }).success(
			function(response) {
				mi.unidadesMedida = response.MetasUnidades;
	});
	
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

	$scope.nombreUnidadMedida = function(id){
		if (id != null && id > 0){
			for (i=0; i<mi.unidadesMedida.length; i++){
				if(mi.unidadesMedida[i].id == id){
					return mi.unidadesMedida[i].nombre;
				}
			}
		}
		return "";
	}

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

	mi.formatofecha = 'yyyy';
	
	mi.abrirPopupFechaInicio = function(index) {
		switch(index){
			case 1000: mi.fi_abierto = true; break;
		}
	};	

	mi.abrirPopupFechaFin = function(index) {
		switch(index){
			case 1000: mi.ff_abierto = true; break;
		}
	};	

	mi.fechaOptions = {
			formatYear: 'yyyy',
		    startingDay: 1,
		    minMode: 'year'
	};
		
	mi.cargarTabla = function() {			
			var datos = {
				accion : 'getMetasProducto',
				proyectoid: mi.prestamo.value
			};
		
			mi.mostrarCargando = true;
			mi.mostrarDescargar = true;
			
			$http.post('/SPrestamoMetas', datos).then(function(response) {
				if (response.data.success) {
					mi.data = response.data.proyectometas;
					 for (x in mi.data){
						 mi.data[x].$$treeLevel = mi.data[x].objetoTipo -1;
						 if (mi.data[x].objetoTipo>= 3){
							 mi.data[x] = parsearMetas(mi.data[x]);
							 mi.data[x].getTotalMeta = function(tipoMeta){
								 var anioFin = mi.fechaFin.getFullYear();
								 var totalMeta = parseFloat(0);
								 for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
									for (mes = 0; mes < 12; mes++){
										totalMeta += this[mes+tipoMeta+anioInicio] ? parseFloat(this[mes+tipoMeta+anioInicio]) : 0;
									}
								 }
								 totalMeta = Math.round(totalMeta*100)/100;
								 return totalMeta;
							  };
						 }
						 if(mi.data[x].metaFinal){
							 mi.data[x].metaFinal = Math.round((parseFloat(mi.data[x].metaFinal))*100)/100;
						 }
					 }
					mi.opcionesGrid.data = mi.data;
					mi.mostrarCargando = false;
					
					$timeout(function(){
	                    mi.gridApi.treeBase.expandAllRows();
	                    mi.gridApi.grid.columns[0].hideColumn();
					})
				}
			});
	}
	
	var parsearMetas = function(producto){
		for(var i = 0; i < producto.metasPlanificadas.length; i++){
			var meta = producto.metasPlanificadas[i];
			var fecha = meta.fecha.split("/");
			var mes = parseInt(fecha[1]) - 1;
			if (mi.agrupacion.value == AGRUPACION_BIMESTRE){
				if(mes<=1){
					mes = 1;
				}else if(mes <=3){
					mes = 3;
				}else if(mes <=5){
					mes = 5;
				}else if(mes <=7){
					mes = 7;
				}else if(mes <=9){
					mes = 9;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_TRIMESTRE){
				if(mes<=2){
					mes = 2;
				}else if(mes<=5){
					mes = 5;
				}else if(mes<=8){
					mes = 8;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_CUATRIMESTRE){
				if(mes<=3){
					mes = 3;
				}else if(mes <=7){
					mes = 7;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_SEMESTRE){
				if(mes<=5){
					mes = 5;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_ANUAL){
				mes = 11;
			}
			
			var anio = parseInt(fecha[2]);
			var nombreCelda = mes+'P'+anio;
			var valAnterior = producto[nombreCelda] ? producto[nombreCelda] : 0;
			producto[nombreCelda] = Math.round((valAnterior + parseFloat(meta.valor))*100)/100;
			var nombreCeldaId = 'metaPlanificadaId';
			producto[nombreCeldaId] = meta.id;
		}
		for(var i = 0; i < producto.metasReales.length; i++){
			var meta = producto.metasReales[i];
			var fecha = meta.fecha.split("/");
			var mes = parseInt(fecha[1]) - 1;
			if (mi.agrupacion.value == AGRUPACION_BIMESTRE){
				if(mes<=1){
					mes = 1;
				}else if(mes <=3){
					mes = 3;
				}else if(mes <=5){
					mes = 5;
				}else if(mes <=7){
					mes = 7;
				}else if(mes <=9){
					mes = 9;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_TRIMESTRE){
				if(mes<=2){
					mes = 2;
				}else if(mes<=5){
					mes = 5;
				}else if(mes<=8){
					mes = 8;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_CUATRIMESTRE){
				if(mes<=3){
					mes = 3;
				}else if(mes <=7){
					mes = 7;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_SEMESTRE){
				if(mes<=5){
					mes = 5;
				}else{
					mes = 11;
				}
			}else if (mi.agrupacion.value == AGRUPACION_ANUAL){
				mes = 11;
			}
			
			var anio = parseInt(fecha[2]);
			var nombreCelda = mes+'R'+anio;
			var valAnterior = producto[nombreCelda] ? producto[nombreCelda] : 0;
			producto[nombreCelda] = Math.round((valAnterior + parseFloat(meta.valor))*100)/100;
			var nombreCeldaId = 'metaRealId';
			producto[nombreCeldaId] = meta.id;
		}
		return producto;
	}
	
	var nombreMes = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
		  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
		];
	
	mi.setDefinicionColumnas = function(){
		var anioFin = mi.fechaFin.getFullYear();
		columnDefs = [ 
			{ displayName : 'Producto', name : 'nombreMeta', category: " ", pinnedLeft:true, width: 300, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false, enableColumnMenu: false, 
				cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.treeLevel < 2}\" ><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.pmetasc.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>"
			},
			{ displayName : 'Fecha Inicio', name : 'fechaInicio', category: " ", width: 90, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false, enableColumnMenu: false},
			{ displayName : 'Fecha Fin', name : 'fechaFin', category: " ", width: 90, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false, enableColumnMenu: false},  
			{ displayName : 'Unidad de Medida', category: " ", name : 'unidadDeMedidaId', width: 100, 
				enableCellEdit: true, enableFiltering: false, editableCellTemplate: 'ui-grid/dropdownEditor',
				editDropdownValueLabel: 'nombre', editDropdownOptionsArray: [], enableColumnMenu: false,
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); },
				cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.nombreUnidadMedida(row.entity.unidadDeMedidaId)}}</div>',
				cellClass: function (grid, row, col, rowIndex, colIndex) {
			          return grid.appScope.pmetasc.estiloEditable(grid, row, col,rowIndex, colIndex);
			        }
			}];
		
		if (mi.agrupacion.value == AGRUPACION_MES){ 
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
				for (mes = 0; mes < 12; mes++){
					columnDefs.push({ displayName: 'Planificada', category: nombreMes[mes]+"-"+anioInicio, 
						name: mes+'P'+anioInicio, idMeta: 'metaPlanificadaId', width: 90, 
						tipoMeta: META_ID_PLANIFICADA, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: true, enableFiltering : false, enableColumnMenu: false,
						cellEditableCondition: function($scope) {
							var fechaInicio = $scope.row.entity.fechaInicio.split("/");
							var fechaFin = $scope.row.entity.fechaFin.split("/");
							var mesInicio = parseInt(fechaInicio[1]) - 1;
							var mesFin = parseInt(fechaFin[1]) - 1;
							var anioInicio = parseInt(fechaInicio[2]);
							var anioFin = parseInt(fechaFin[2]);
							var fechaEnIntervalo = false;
							if (anioInicio == anioFin){
								if (this.anio == anioInicio && this.mes >= mesInicio && this.mes <= mesFin){
									fechaEnIntervalo = true;
								}
							}else{
								if(this.anio > anioInicio && this.anio < anioFin){
									fechaEnIntervalo = true; 
								}else if(this.anio == anioInicio && this.mes >= mesInicio){
									fechaEnIntervalo = true;
								}else if(this.anio == anioFin && this.mes <= mesFin){
									fechaEnIntervalo = true;
								}
							}
							return ($scope.row.entity.objetoTipo >= 3 && fechaEnIntervalo 
									&& $scope.row.entity.unidadDeMedidaId != null && $scope.row.entity.unidadDeMedidaId != ""
									); 
							},
						cellClass: function (grid, row, col, rowIndex, colIndex) {
					        return grid.appScope.pmetasc.estiloEditable(grid, row, col,rowIndex, colIndex);
					        }
					});
					columnDefs.push({ displayName: 'Real', category: nombreMes[mes]+"-"+anioInicio, 
						name: mes+'R'+anioInicio, idMeta: 'metaRealId', width: 90,  
						tipoMeta: META_ID_REAL, anio: anioInicio, mes: mes, 
						enableCellEdit: true, enableFiltering : false, enableColumnMenu: false,
						cellEditableCondition: function( $scope ) { 
							var fechaInicio = $scope.row.entity.fechaInicio.split("/");
							var fechaFin = $scope.row.entity.fechaFin.split("/");
							var mesInicio = parseInt(fechaInicio[1]) - 1;
							var mesFin = parseInt(fechaFin[1]) - 1;
							var anioInicio = parseInt(fechaInicio[2]);
							var anioFin = parseInt(fechaFin[2]);
							var fechaEnIntervalo = false;
							if (anioInicio == anioFin){
								if (this.anio == anioInicio && this.mes >= mesInicio && this.mes <= mesFin){
									fechaEnIntervalo = true;
								}
							}else{
								if(this.anio > anioInicio && this.anio < anioFin){
									fechaEnIntervalo = true; 
								}else if(this.anio == anioInicio && this.mes >= mesInicio){
									fechaEnIntervalo = true;
								}else if(this.anio == anioFin && this.mes <= mesFin){
									fechaEnIntervalo = true;
								}
							}
							return ($scope.row.entity.objetoTipo >= 3 && fechaEnIntervalo 
									&& $scope.row.entity.unidadDeMedidaId != null && $scope.row.entity.unidadDeMedidaId != ""
									);  
							},
						cellClass: function (grid, row, col, rowIndex, colIndex) {
					        return grid.appScope.pmetasc.estiloEditable(grid, row, col,rowIndex, colIndex);
					        }
					});
					mi.opcionesGrid.category.push({name: nombreMes[mes]+"-"+anioInicio, visible: true});
				}
			}
		}else if (mi.agrupacion.value == AGRUPACION_BIMESTRE){
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
				for (contador = 1; contador <= 6; contador++){
					var mes = (contador*2)-1;
					columnDefs.push({ displayName: 'Planificada', category: "Bimestre-"+contador+"-"+anioInicio, 
						name: mes+'P'+anioInicio, idMeta: 'metaPlanificadaId', width: 90, cellClass : 'grid-align-center', 
						tipoMeta: META_ID_PLANIFICADA, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'planificado'
					});
					columnDefs.push({ displayName: 'Real', category: "Bimestre-"+contador+"-"+anioInicio, 
						name: mes+'R'+anioInicio, idMeta: 'metaRealId', width: 90, cellClass : 'grid-align-center',
						tipoMeta: META_ID_REAL, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'real'
					});
					mi.opcionesGrid.category.push({name: "Bimestre-"+contador+"-"+anioInicio, visible: true});
				}
			}
		}else if (mi.agrupacion.value == AGRUPACION_TRIMESTRE){
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
				for (contador = 1; contador <= 4; contador++){
					var mes = (contador*3)-1;
					columnDefs.push({ displayName: 'Planificada', category: "Trimestre-"+contador+"-"+anioInicio, 
						name: mes+'P'+anioInicio, idMeta: 'metaPlanificadaId', width: 90, cellClass : 'grid-align-center', 
						tipoMeta: META_ID_PLANIFICADA, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'planificado'
					});
					columnDefs.push({ displayName: 'Real', category: "Trimestre-"+contador+"-"+anioInicio, 
						name: mes+'R'+anioInicio, idMeta: 'metaRealId', width: 90, cellClass : 'grid-align-center',
						tipoMeta: META_ID_REAL, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'real'
					});
					mi.opcionesGrid.category.push({name: "Trimestre-"+contador+"-"+anioInicio, visible: true});
				}
			}
		}else if (mi.agrupacion.value == AGRUPACION_CUATRIMESTRE){
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
				for (contador = 1; contador <= 3; contador++){
					var mes = (contador*4)-1;
					columnDefs.push({ displayName: 'Planificada', category: "Cuatrimestre-"+contador+"-"+anioInicio, 
						name: mes+'P'+anioInicio, idMeta: 'metaPlanificadaId', width: 90, cellClass : 'grid-align-center', 
						tipoMeta: META_ID_PLANIFICADA, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'planificado'
					});
					columnDefs.push({ displayName: 'Real', category: "Cuatrimestre-"+contador+"-"+anioInicio, 
						name: mes+'R'+anioInicio, idMeta: 'metaRealId', width: 90, cellClass : 'grid-align-center',
						tipoMeta: META_ID_REAL, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'real'
					});
					mi.opcionesGrid.category.push({name: "Cuatrimestre-"+contador+"-"+anioInicio, visible: true});
				}
			}
		}else if (mi.agrupacion.value == AGRUPACION_SEMESTRE){ 
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
				for (contador = 1; contador <= 2; contador++){
					var mes = (contador*6)-1;
					columnDefs.push({ displayName: 'Planificada', category: "Semestre-"+contador+"-"+anioInicio, 
						name: mes+'P'+anioInicio, idMeta: 'metaPlanificadaId', width: 90, cellClass : 'grid-align-center', 
						tipoMeta: META_ID_PLANIFICADA, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'planificado'
					});
					columnDefs.push({ displayName: 'Real', category: "Semestre-"+contador+"-"+anioInicio, 
						name: mes+'R'+anioInicio, idMeta: 'metaRealId', width: 90, cellClass : 'grid-align-center',
						tipoMeta: META_ID_REAL, anio: anioInicio, mes: mes, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'real'
					});
					mi.opcionesGrid.category.push({name: "Semestre-"+contador+"-"+anioInicio, visible: true});
				}
			}
		}else if (mi.agrupacion.value == AGRUPACION_ANUAL){ 
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
					columnDefs.push({ displayName: 'Planificada', category: anioInicio, 
						name: '11P'+anioInicio, idMeta: 'metaPlanificadaId', width: 90, cellClass : 'grid-align-center', 
						tipoMeta: META_ID_PLANIFICADA, anio: anioInicio, mes: 11, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'planificado'
					});
					columnDefs.push({ displayName: 'Real', category: anioInicio, 
						name: '11R'+anioInicio, idMeta: 'metaRealId', width: 90, cellClass : 'grid-align-center', 
						tipoMeta: META_ID_REAL, anio: anioInicio, mes: 11, type: 'number',
						enableCellEdit: false, enableFiltering : false, enableColumnMenu: false,
						cellClass: 'real'
					});
					mi.opcionesGrid.category.push({name: anioInicio, visible: true});
			}
		}
		columnDefs.push({ displayName: 'Total Planificado', name: 'getTotalMeta("P")', width: 90, cellClass : 'grid-align-center', 
			tipoMeta: META_ID_LINEABASE, idMeta: 'lineaBaseId', anio: anioFin, mes: 11, type: 'number',
			enableCellEdit: false, enableFiltering : false, enableColumnMenu: false, pinnedRight:true,
			cellClass: 'planificado'
		});
		columnDefs.push({ displayName: 'Total Real', name: 'getTotalMeta("R")', width: 90, cellClass : 'grid-align-center', 
			tipoMeta: META_ID_FINAL, idMeta: 'metaFinalId', anio: anioFin, mes: 11, type: 'number',
			enableCellEdit: false, enableFiltering : false, enableColumnMenu: false, pinnedRight:true,
			cellClass: 'real'
		});
		columnDefs.push({ displayName: 'Meta Final', name: 'metaFinal', width: 90,  
			tipoMeta: META_ID_FINAL, idMeta: 'metaFinalId', anio: anioFin, mes: 11, type: 'number',
			enableCellEdit: true, enableFiltering : false, enableColumnMenu: false, pinnedRight:true,
			cellEditableCondition: function( $scope ) { 
				return ($scope.row.entity.objetoTipo >= 3 && $scope.row.entity.unidadDeMedidaId != null && $scope.row.entity.unidadDeMedidaId != ""); 
				},
			cellClass: function (grid, row, col, rowIndex, colIndex) {
		        return grid.appScope.pmetasc.estiloEditable(grid, row, col,rowIndex, colIndex);
		        }
		});
		
		mi.opcionesGrid.columnDefs = columnDefs;
		mi.opcionesGrid.columnDefs[3].editDropdownOptionsArray = mi.unidadesMedida;
	}
	 	 
	 mi.opcionesGrid = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect: false,
		modifierKeysToMultiSelect: false,
		noUnselect: true,
		showTreeRowHeader: false,
		showTreeExpandNoChildren: false,
        enableCellSelection:true,
		rowEditWaitInterval: 1,
		headerTemplate: '<div role="rowgroup" class="ui-grid-header"> <div class="ui-grid-top-panel"> <div class="ui-grid-header-viewport"> <div class="ui-grid-header-canvas"> <div class="ui-grid-header-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"> <div role="row" class="ui-grid-header-cell-row"> <div class="ui-grid-header-cell ui-grid-clearfix ui-grid-category" ng-repeat="cat in grid.options.category" ng-if="cat.visible &&  (colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }).length > 0"> {{cat.name}} <div class="ui-grid-header-cell ui-grid-clearfix" ng-repeat="col in colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }" ui-grid-header-cell col="col" render-index="$index"> </div> </div><!--!cat.visible && --> <div class="ui-grid-header-cell ui-grid-clearfix" ng-if="col.colDef.category === undefined" ng-repeat="col in colContainer.renderedColumns track by col.uid" ui-grid-header-cell col="col" render-index="$index"> </div></div></div></div></div></div></div>',
	    columnDefs : [ ],
		category: [{name: " ", visible: true}],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;
			
			gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
				if (newValue != oldValue){
					if(colDef.idMeta != null){
				    	if (rowEntity.unidadDeMedidaId != null && rowEntity.unidadDeMedidaId != ""){
				    		if (newValue>=0){
								var fecha = '01/'+(colDef.mes+1)+'/'+colDef.anio;
								
								var meta = {
										id: rowEntity[colDef.idMeta],
										fecha: fecha,
										valor: newValue, 
										tipoMetaId: colDef.tipoMeta,
										unidadMedidaId: rowEntity.unidadDeMedidaId,
										objetoId: rowEntity.id, 
										tipoObjetoId: rowEntity.objetoTipo
								}
								mi.guardarMeta(meta, rowEntity, colDef.idMeta);
				    		}else{
					    		$utilidades.mensaje('danger','No se permiten números negativos');
					    	}
				    	}else{
				    		$utilidades.mensaje('danger','Debe seleccionar una Unidad de Medida');
				    	}
					}
				}
			});
		    
		}
	}
	 
	 mi.generar = function(){
		 mi.opcionesGrid.category= [{name: " ", visible: true}];
			if(mi.prestamo.value > 0){
				if (mi.fechaInicio <= mi.fechaFin){
					if(mi.agrupacion.value > 0){
						mi.setDefinicionColumnas();
						mi.cargarTabla();
					}else{
						$utilidades.mensaje('warning','Debe seleccionar una agrupación');
					}	
				}else{
					$utilidades.mensaje('danger','Error: el año inicial no puede ser mayor al año final');
				}
			}else{
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
			}
		}
		
	mi.guardarMeta = function(meta, rowEntity, col){
		$http.post('/SPrestamoMetas', {
			accion: 'guardarProyectoMeta',
			id: meta.id,
			fecha: meta.fecha,
			valor: meta.valor,
			tipoMetaId: meta.tipoMetaId,
			unidadMedidaId: meta.unidadMedidaId, 
			objetoId: meta.objetoId,
			objetoTipoId: meta.tipoObjetoId
		}).success(function(response){
			if(response.success){
				$utilidades.mensaje('success','Valor guardado con éxito');
				rowEntity[col] = response.metavalor.id;
			}
			else{
				$utilidades.mensaje('danger','Error al guardar el valor');
			}
		});
	 }

	mi.estiloEditable = function (grid, row, col, rowIndex, colIndex) {	    
		var fila = { row: row };
	    if(col.colDef.cellEditableCondition(fila)){
	    	return 'grid-align-center lightblue';
	    }else if(col.colDef.tipoMeta == META_ID_PLANIFICADA){
	    	return 'grid-align-center planificado';
	    }else if(col.colDef.tipoMeta == META_ID_REAL){
	    	return 'grid-align-center real';
	    }
	    return 'grid-align-center';
	}
  
	 mi.exportarExcel = function(){
		 var columnas = [];
		 mi.opcionesGrid.columnDefs.forEach(function(columna) {
			 columnas.push({'displayName': columna.displayName.toString(), "category": columna.category ? columna.category.toString() : ""});
		 });
		 $http.post('/SPrestamoMetas', { 
			 accion: 'exportarExcel', 
			 proyectoid: mi.prestamo.value,
			 fechaInicio: mi.fechaInicio.getFullYear(),
			 fechaFin: mi.fechaFin.getFullYear(),
			 agrupacion: mi.agrupacion.value,
			 filas: JSON.stringify(mi.opcionesGrid.data),
			 columnas: JSON.stringify(columnas),
			 t:moment().unix()
		  } ).then(
				  function successCallback(response) {
					  var anchor = angular.element('<a/>');
					  anchor.attr({
				         href: 'data:application/ms-excel;base64,' + response.data,
				         target: '_blank',
				         download: 'MetasPrestamo.xls'
					  })[0].click();
				  }.bind(this), function errorCallback(response){
					 		
			 	}
		  	);
		};
	
	
}]);



		