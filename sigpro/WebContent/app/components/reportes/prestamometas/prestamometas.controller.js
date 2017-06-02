var app = angular.module('prestamometasController', [ 'ui.grid.edit', 'ui.grid.rowEdit',]);


app.controller('prestamometasController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridTreeBaseService','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridTreeBaseService,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	var metaSeleccionada = null;
	
	var META_ID_UNIDADMEDIDA = -1;
	var META_ID_TIPODATO = 0;
	var META_ID_REAL = 1;
	var META_ID_ANUALPLANIFICADA = 2;
	var META_ID_LINEABASE= 3;
	var META_ID_FINAL= 4;
	
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	
	
	$window.document.title = $utilidades.sistema_nombre+' - Metas de Préstamo';
	i18nService.setCurrentLang('es');
	 
	$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyectoId }).success(
			function(response) {
				mi.proyectoid = response.id;
				mi.proyectoNombre = response.nombre;
				mi.objetoTipoNombre = "Proyecto";
	});
	

	$http.post('/SMeta', { accion: 'getMetasUnidadesMedida' }).success(
			function(response) {
				mi.unidadesMedida = response.MetasUnidades;
				mi.opcionesGrid.columnDefs[2].editDropdownOptionsArray = mi.unidadesMedida;
	});
	

	$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
			function(response) {
				mi.datoTipos = response.datoTipos;
				mi.opcionesGrid.columnDefs[3].editDropdownOptionsArray = mi.datoTipos;
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
	
	$scope.nombreDatoTipo = function(id){
		if (id != null && id > 0){
			for (i=0; i<mi.datoTipos.length; i++){
				if(mi.datoTipos[i].id == id){
					return mi.datoTipos[i].nombre;
				}
			}
		}
		return "";
	}
			 
	mi.mostrarCargando = true;
	mi.data = [];
	mi.cargarTabla = function() {
		var datos = {
			accion : 'getProyectoMetas',
			proyectoid: $routeParams.proyectoId
		};
	
		mi.mostrarCargando = true;
		
		$http.post('/SProyecto', datos).then(function(response) {
			if (response.data.success) {
				mi.data = response.data.proyectometas;
				 for (x in mi.data){
					 mi.data[x].$$treeLevel = mi.data[x].objetoTipo -1;
				 }
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
				
				$timeout(function(){
                    mi.gridApi.treeBase.expandAllRows();
                    mi.gridApi.grid.columns[0].hideColumn();
				})
			}
		});
	};
	 	 
	 mi.opcionesGrid = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect: false,
		modifierKeysToMultiSelect: false,
		noUnselect: true,
		enableFiltering: true,
		useExternalSorting: true,
		showTreeRowHeader: false,
		showTreeExpandNoChildren: false,
		rowEditWaitInterval: 5000,
	    data : mi.data,
	    columnDefs : [ 
			{ displayName : 'Producto', name : 'nombre', width: 400, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false, 
				cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.treeLevel < 2}\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.pmetasc.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>"
			},
			{ displayName : 'Fecha Fin', name : 'metaFecha', width: 200, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false},  
			{ displayName : 'Unidad de Medida', tipoMetaId: META_ID_UNIDADMEDIDA, name : 'unidadDeMedidaId', width: 200, cellClass : 'grid-align-left', 
				enableCellEdit: true, enableFiltering: false, editableCellTemplate: 'ui-grid/dropdownEditor',
				editDropdownValueLabel: 'nombre', editDropdownOptionsArray: [],
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); },
				cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.nombreUnidadMedida(row.entity.unidadDeMedidaId)}}</div>'
			},  
			{ displayName : 'Tipo de Dato', tipoMetaId: META_ID_TIPODATO, name : 'datoTipoId', width: 200, cellClass : 'grid-align-left', 
				enableCellEdit: true, enableFiltering: false, editableCellTemplate: 'ui-grid/dropdownEditor',
				editDropdownValueLabel: 'nombre', editDropdownOptionsArray: [],
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); },
				cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.nombreDatoTipo(row.entity.datoTipoId)}}</div>'
			},  
			{ displayName : 'Meta Real', tipoMetaId: META_ID_REAL, name : 'metaReal', cellClass : 'grid-align-center', 
				enableCellEdit: true, enableFiltering : false,
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
			},
			{ displayName : 'Meta Anual Planificada', tipoMetaId: META_ID_ANUALPLANIFICADA, name : 'metaAnualPlanificada', cellClass : 'grid-align-center', 
				enableCellEdit: true, enableFiltering : false,
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
			},
			{ displayName : 'Línea Base', tipoMetaId: META_ID_LINEABASE, name : 'lineaBase', cellClass : 'grid-align-center', 
				enableCellEdit: true, enableFiltering : false,
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
			},
			{ displayName : 'Meta Final', tipoMetaId: META_ID_FINAL, name : 'metaFinal', cellClass : 'grid-align-center', 
				enableCellEdit: true, enableFiltering : false,
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
			}
		],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.meta = row.entity;
			});
			
			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.pmetasc.columnaOrdenada=sortColumns[0].field;
					grid.appScope.pmetasc.ordenDireccion = sortColumns[0].sort.direction;
					
					for(var i = 0; i<sortColumns.length-1; i++)
						sortColumns[i].unsort();
					grid.appScope.pmetasc.cargarTabla();
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.pmetasc.columnaOrdenada!=null){
						grid.appScope.pmetasc.columnaOrdenada=null;
						grid.appScope.pmetasc.ordenDireccion=null;
					}
				}
			} );

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
				mi.cargarTabla();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyectometas', t: (new Date()).getTime()}).then(function(response){
				      if(response.data.success && response.data.estado!='')
				    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
					  mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
					  mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
					  mi.cargarTabla();
				  });
		    	  
		    }

		    gridApi.rowEdit.on.saveRow($scope, function( rowEntity ) {
		    	    var promise = mi.guardarMeta(rowEntity);
		            mi.gridApi.rowEdit.setSavePromise(rowEntity, promise);
		    	  });
		}
	}
	 
	 mi.toggleRow = function(row, evt) {
         uiGridTreeBaseService.toggleRowTreeState(mi.gridApi.grid, row, evt);
     };
     	 
	 mi.guardarEstado=function(){
			var estado = mi.gridApi.saveState.save();
			var tabla_data = { action: 'guardaEstado', grid:'proyectometas', estado: JSON.stringify(estado) };
			$http.post('/SEstadoTabla', tabla_data).then(function(response){

			});
	 }

	mi.guardarMeta = function(rowEntity){
		var defered = $q.defer();
        var promise = defered.promise;
        
    	if (rowEntity.datoTipoId != "" && rowEntity.unidadDeMedidaId != ""
    		&& rowEntity.datoTipoId != null && rowEntity.unidadDeMedidaId != null){
	        
			$http.post('/SProyecto', {
				accion: 'guardarProyectoMeta',
				id: rowEntity.id,
				objetoTipo: rowEntity.objetoTipo,
				unidadDeMedidaId: rowEntity.unidadDeMedidaId,
				datoTipoId: rowEntity.datoTipoId,
				metaFecha: rowEntity.metaFecha,
				metaRealId: rowEntity.metaRealId,
				metaReal: rowEntity.metaReal,
				metaAnualPlanificadaId: rowEntity.metaAnualPlanificadaId,
				metaAnualPlanificada: rowEntity.metaAnualPlanificada,
				lineaBaseId: rowEntity.lineaBaseId,
				lineaBase: rowEntity.lineaBase,
				metaFinalId: rowEntity.metaFinalId,
				metaFinal: rowEntity.metaFinal
			}).success(function(response){
					if(response.success){
						rowEntity.metaRealId = response.proyectometa.metaRealId;
						rowEntity.metaAnualPlanificadaId = response.proyectometa.metaAnualPlanificadaId;
						rowEntity.lineaBaseId = response.proyectometa.lineaBaseId;
						rowEntity.metaFinalId = response.proyectometa.metaFinalId;
						$utilidades.mensaje('success','Valor guardado con éxito');
						defered.resolve();
					}
					else{
						$utilidades.mensaje('danger','Error al guardar el valor');
	                	defered.reject()
					}
				});
    	}else{
    		$utilidades.mensaje('danger','Debe seleccionar una Unidad de Medida y un Tipo de Dato');
        	defered.reject()
    	}
		 return promise;
	 }
	  
	 mi.exportarExcel = function(){
			$http.post('/SProyecto', { accion: 'exportarExcel', proyectoid:$routeParams.proyectoId,t:moment().unix()
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



		