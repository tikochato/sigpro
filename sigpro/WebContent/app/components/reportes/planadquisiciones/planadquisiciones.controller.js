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
			cellEditableCondition:function($scope){
          	  if($scope.row.entity.tipo == 1)
          		  return true;
          	  else
          		  return false;
			},
			rowEditWaitInterval: -1,
			enableExpandableRowHeader: false,
			showTreeRowHeader: false,
			showTreeExpandNoChildren : false,
			enableColumnMenus: false,
			enableSorting: false,
			enableCellEdit: true,
			enableCellEditOnFocus: true,
			headerTemplate: '<div role="rowgroup" class="ui-grid-header"> <div class="ui-grid-top-panel"> <div class="ui-grid-header-viewport"> <div class="ui-grid-header-canvas"> <div class="ui-grid-header-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"> <div role="row" class="ui-grid-header-cell-row"> <div class="ui-grid-header-cell ui-grid-clearfix" ng-if="col.colDef.category === undefined" ng-repeat="col in colContainer.renderedColumns track by col.uid" ui-grid-header-cell col="col" render-index="$index"> </div> <div style="width:100px" align="center" class="ui-grid-header-cell ui-grid-clearfix ui-grid-category" ng-repeat="cat in grid.appScope.controller.categorias" ng-if="cat.visible && (colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }).length > 0"> <span ng-if="cat.showCatName === true"> {{cat.displayName}} </span> <br ng-if="cat.showCatName !== true" /> <div class="ui-grid-header-cell ui-grid-clearfix" ng-repeat="col in colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }" ui-grid-header-cell col="col" render-index="$index"> </div> </div><!--!cat.visible && --> </div> </div> </div> </div> </div> </div> ',
			category: mi.categorias,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Componente',
		        	cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.treeLevel < 2}\"><div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>",
		        },
				{ name: 'metodo', enableCellEdit: false,width: 150, displayName: 'Método'},
				{ name: 'planificadoDocs', enableCellEdit: false, width: 150, displayName: 'Planificado', category: 'PreparacionDocumentos', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realDocs', enableCellEdit: true, enableCellEditOnFocus: true, width: 150, displayName: 'Real', category: 'PreparacionDocumentos', type: 'date', cellFilter: 'date:"dd/MM/yyyy"'},
				{ name: 'planificadoLanzamiento', enableCellEdit: false, width: 150, displayName: 'Planificado', category: 'LanzamientoEvento', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realLanzamiento', enableCellEdit: true, enableCellEditOnFocus: true, width: 150, displayName: 'Real', category: 'LanzamientoEvento', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'planificadoRecepcionEval', enableCellEdit: false, width: 150, displayName: 'Planificado', category: 'RecepcionEvaluacionOfertas', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realRecepcionEval', enableCellEdit: true, enableCellEditOnFocus: true, width: 150, displayName: 'Real', category: 'RecepcionEvaluacionOfertas', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'planificadoAdjudica', enableCellEdit: false, width: 150, displayName: 'Planificado', category: 'Adjudicacion', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realAdjudica', enableCellEdit: true, enableCellEditOnFocus: true, width: 150, displayName: 'Real', category: 'Adjudicacion', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'planificadoFirma', enableCellEdit: false, width: 150, displayName: 'Planificado', category: 'FirmaContrato', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realFirma', enableCellEdit: true, enableCellEditOnFocus: true, width: 150, displayName: 'Real', category: 'FirmaContrato', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
			],
			onRegisterApi: function(gridApi) {
			      mi.gridApi = gridApi;
			      
			      mi.gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){				      
			    	  mi.saveRow(rowEntity);
			      });
			      
			      mi.gridApi.rowEdit.on.saveRow($scope, mi.saveRow);
			}
	};
	
	mi.saveRow = function(rowEntity){
		$http.post('/SPlanAdquisiciones', {
            accion: 'guardarPlan',
            id: rowEntity.idObjetoTipo,
            planificadoDocs: moment(rowEntity.planificadoDocs).format('DD/MM/YYYY') == null ? null : moment(rowEntity.planificadoDocs).format('DD/MM/YYYY'),
            realDocs: moment(rowEntity.realDocs).format('DD/MM/YYYY') == null ? null : moment(rowEntity.realDocs).format('DD/MM/YYYY'),
            planificadoLanzamiento: moment(rowEntity.planificadoLanzamiento).format('DD/MM/YYYY') == null ? null : moment(rowEntity.planificadoLanzamiento).format('DD/MM/YYYY'),
            realLanzamiento: moment(rowEntity.realLanzamiento).format('DD/MM/YYYY') == null ? null : moment(rowEntity.realLanzamiento).format('DD/MM/YYYY'),
            planificadoRecepcionEval: moment(rowEntity.planificadoRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(rowEntity.planificadoRecepcionEval).format('DD/MM/YYYY'),
            realRecepcionEval: moment(rowEntity.realRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(rowEntity.realRecepcionEval).format('DD/MM/YYYY'),
    		planificadoAdjudica: moment(rowEntity.planificadoAdjudica).format('DD/MM/YYYY') == null ? null : moment(rowEntity.planificadoAdjudica).format('DD/MM/YYYY'),
            realAdjudica: moment(rowEntity.realAdjudica).format('DD/MM/YYYY') == null ? null : moment(rowEntity.realAdjudica).format('DD/MM/YYYY'),
    		planificadoFirma: moment(rowEntity.planificadoFirma).format('DD/MM/YYYY') == null ? null : moment(rowEntity.planificadoFirma).format('DD/MM/YYYY'),
            realFirma: moment(rowEntity.realFirma).format('DD/MM/YYYY') == null ? null : moment(rowEntity.realFirma).format('DD/MM/YYYY')
        }).success(function(response){
        	
                
        }).error(function(response){
        	
        });
	}
	
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
				
				data[x].planificadoDocs = (data[x].planificadoDocs!='') ? moment(data[x].planificadoDocs,'DD/MM/YYYY').toDate() : null;
				data[x].realDocs = (data[x].realDocs!='') ? moment(data[x].realDocs,'DD/MM/YYYY').toDate() : null;
				data[x].planificadoLanzamiento = (data[x].planificadoLanzamiento!='') ? moment(data[x].planificadoLanzamiento,'DD/MM/YYYY').toDate() : null;
				data[x].realLanzamiento = (data[x].realLanzamiento!='') ? moment(data[x].realLanzamiento,'DD/MM/YYYY').toDate() : null;
				data[x].planificadoRecepcionEval = (data[x].planificadoRecepcionEval!='') ? moment(data[x].planificadoRecepcionEval,'DD/MM/YYYY').toDate() : null;
				data[x].realRecepcionEval = (data[x].realRecepcionEval!='') ? moment(data[x].realRecepcionEval,'DD/MM/YYYY').toDate() : null;
				data[x].planificadoAdjudica = (data[x].planificadoAdjudica!='') ? moment(data[x].planificadoAdjudica,'DD/MM/YYYY').toDate() : null;
				data[x].realAdjudica = (data[x].realAdjudica!='') ? moment(data[x].realAdjudica,'DD/MM/YYYY').toDate() : null;
				data[x].planificadoFirma = (data[x].planificadoFirma!='') ? moment(data[x].planificadoFirma,'DD/MM/YYYY').toDate() : null;
				data[x].realFirma = (data[x].realFirma!='') ? moment(data[x].realFirma,'DD/MM/YYYY').toDate() : null;
			}	
		}
		
		mi.gridOptions.data = data;
		
    	$timeout(function(){
		     mi.gridApi.treeBase.expandAllRows();
	   })
	}
	
	mi.getPrestamos();
}]);
