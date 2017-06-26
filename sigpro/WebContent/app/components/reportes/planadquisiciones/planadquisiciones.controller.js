var app = angular.module('planAdquisicionesController', ['ngTouch','ngAnimate','ui.grid.edit', 'ui.grid.rowEdit']);
app.controller('planAdquisicionesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion) {
	var mi = this;
	
	i18nService.setCurrentLang('es');
	
	mi.anio = new Date().getFullYear();
	
	mi.prestamos = [
		{value: 0,text: "Seleccione una opción"}
	];
	
	mi.prestamo = mi.prestamos[0];
	
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
	
	mi.categorias = [
		{name: 'PreparacionDocumentos', displayName: 'Preparación de documentos',visible: true, showCatName: true},
		{name: 'LanzamientoEvento', displayName: 'Lanzamiento de evento', visible: true, showCatName: true},
		{name: 'RecepcionEvaluacionOfertas', displayName: 'Recepción y evaluación de ofertas', visible: true, showCatName: true},
		{name: 'Adjudicacion', displayName: 'Adjudicación', visible: true, showCatName: true},
		{name: 'FirmaContrato', displayName: 'Firma contrato', visible: true, showCatName: true}
	];
	
	mi.gridOptions = {
			expandAllRows : true,
			enableExpandableRowHeader: false,
			showTreeRowHeader: false,
			showTreeExpandNoChildren : false,
			enableColumnMenus: false,
			enableSorting: false,
			enableCellEditOnFocus: true,
			headerTemplate: '<div role="rowgroup" class="ui-grid-header"> <div class="ui-grid-top-panel"> <div class="ui-grid-header-viewport"> <div class="ui-grid-header-canvas"> <div class="ui-grid-header-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"> <div role="row" class="ui-grid-header-cell-row"> <div class="ui-grid-header-cell ui-grid-clearfix" ng-if="col.colDef.category === undefined" ng-repeat="col in colContainer.renderedColumns track by col.uid" ui-grid-header-cell col="col" render-index="$index"> </div> <div style="width:100px" align="center" class="ui-grid-header-cell ui-grid-clearfix ui-grid-category" ng-repeat="cat in grid.appScope.controller.categorias" ng-if="cat.visible && (colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }).length > 0"> <span ng-if="cat.showCatName === true"> {{cat.displayName}} </span> <br ng-if="cat.showCatName !== true" /> <div class="ui-grid-header-cell ui-grid-clearfix" ng-repeat="col in colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }" ui-grid-header-cell col="col" render-index="$index"> </div> </div><!--!cat.visible && --> </div> </div> </div> </div> </div> </div> ',
			category: mi.categorias,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Componente',
		        	cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': grid.appScope.controller.objetoTipo < 5}\"><div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>",
		        },
				{ name: 'metodo', enableCellEdit: false,width: 100, displayName: 'Método'},
				{ name: 'planificadoDocs', enableCellEdit: false, width: 100, displayName: 'Planificado', category: 'PreparacionDocumentos'},
				{ name: 'realDocs', enableCellEdit: false, width: 100, displayName: 'Real', category: 'PreparacionDocumentos'},
				{ name: 'planificadoLanzamiento', enableCellEdit: false, width: 100, displayName: 'Planificado', category: 'LanzamientoEvento'},
				{ name: 'realLanzamiento', enableCellEdit: false, width: 150, displayName: 'Real', category: 'LanzamientoEvento'},
				{ name: 'planificadoRecepcionEval', enableCellEdit: false, width: 200, displayName: 'Planificado', category: 'RecepcionEvaluacionOfertas'},
				{ name: 'realRecepcionEval', enableCellEdit: false, width: 150, displayName: 'Real', category: 'RecepcionEvaluacionOfertas'},
				{ name: 'planificadoAdjudica', enableCellEdit: false, width: 200, displayName: 'Planificado', category: 'Adjudicacion'},
				{ name: 'realAdjudica', enableCellEdit: false, width: 100, displayName: 'Real', category: 'Adjudicacion'},
				{ name: 'planificadoFirma', enableCellEdit: false, width: 200, displayName: 'Planificado', category: 'FirmaContrato'},
				{ name: 'realFirma', enableCellEdit: false, width: 100, displayName: 'Real', category: 'FirmaContrato'}
			],
			onRegisterApi: function(gridApi) {
			      mi.gridApi = gridApi;
			}
	};
	
	mi.generar = function(){
		if(mi.prestamo.value > 0){
			$http.post('/SPlanAdquisiciones',{
				accion: 'generarPlan',
				idPrestamo: mi.prestamo.value,
				informeCompleto: mi.informeCompleto,
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.componentes);
					mi.exportar = true;
				}
			});
		}
	}
	
	mi.exportarExcel = function(){
		var reporte = [];
		
		for(x in mi.gridOptions.data){
			var row = mi.gridOptions.data[x];
			reporte.push({nombre: row.nombre, metodo: row.metodo, planificadoDocs: row.planificadoDocs, realDocs: row.realDocs, planificadoLanzamiento: row.planificadoLanzamiento, realLanzamiento: row.realLanzamiento, planificadoRecepcionEval: row.planificadoRecepcionEval, realRecepcionEval: row.realRecepcionEval, planificadoAdjudica: row.planificadoAdjudica, realAdjudica: row.realAdjudica, planificadoFirma: row.planificadoFirma, realFirma: row.realFirma});
		}
		
		$http.post('/SPlanAdquisiciones',{
			accion: 'exportarExcel',
			data: JSON.stringify(reporte),
			t:moment().unix()
		}).then(
				  function successCallback(response) {
						var anchor = angular.element('<a/>');
					    anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'planAdquisiciones.xls'
					     })[0].click();
					  }.bind(this), function errorCallback(response){
					 		
					 	}
					 );
	}
	
	mi.toggleRow = function(row, evt) {
		uiGridTreeBaseService.toggleRowTreeState(mi.gridApi.grid, row, evt);
	};
	
	mi.crearArbol = function(datos){
		if (datos.length > 0){
			var data = datos;
			
			for(x in data){
				data[x].$$treeLevel = Number(data[x].posicionArbol) - 1;
				if(data[x].objetoTipo == 5)
					data[x].metodo = 0;
			}
		}
		
		mi.gridOptions.data = data;
		
    	$timeout(function(){
		     mi.gridApi.treeBase.expandAllRows();
	   })
	}
	
	mi.getPrestamos();
}]);
