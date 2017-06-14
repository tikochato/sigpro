var app = angular.module('prestamometasController', [ 'ui.grid.edit', 'ui.grid.rowEdit', 'ui.grid.pinning']);


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

	mi.mostrarCargando = false;
	mi.prestamos = [];
	mi.data = [];	
	
	$window.document.title = $utilidades.sistema_nombre+' - Metas de Préstamo';
	i18nService.setCurrentLang('es');
	
	$http.post('/SMeta', { accion: 'getMetasUnidadesMedida' }).success(
			function(response) {
				mi.unidadesMedida = response.MetasUnidades;
	});

	$http.post('/SDatoTipo', { accion: 'cargarCombo' }).success(
			function(response) {
				mi.datoTipos = response.datoTipos;
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

	mi.agrupaciones = [
		{'value' : 1, 'text' : 'Mes'},
		//m{'value' : 2, 'text' : 'Trimestre'},
		{'value' : 3, 'text' : 'Cuatrimestre'},
		{'value' : 4, 'text' : 'Anual'},
	];

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
			
			$http.post('/SPrestamoMetas', datos).then(function(response) {
				if (response.data.success) {
					mi.data = response.data.proyectometas;
					 for (x in mi.data){
						 mi.data[x].$$treeLevel = mi.data[x].objetoTipo -1;
						 if (mi.data[x].objetoTipo>= 3){
							 mi.data[x] = parsearMetas(mi.data[x]);
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
			var anio = parseInt(fecha[2]);
			var nombreCelda = mes+'P'+anio;
			producto[nombreCelda] = meta.valor;
			producto[mes+'R'+anio+'Id'] = meta.fecha;
		}
		for(var i = 0; i < producto.metasReales.length; i++){
			var meta = producto.metasReales[i];
			var fecha = meta.fecha.split("/");
			var mes = parseInt(fecha[1]) - 1;
			var anio = parseInt(fecha[2]);
			var nombreCelda = mes+'R'+anio;
			producto[nombreCelda] = meta.valor;
			producto[mes+'R'+anio+'Id'] = meta.fecha;
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
				cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.treeLevel < 2}\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.pmetasc.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>"
			},
			{ displayName : 'Fecha Inicio', name : 'fechaInicio', category: " ", width: 100, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false, enableColumnMenu: false},
			{ displayName : 'Fecha Fin', name : 'fechaFin', category: " ", width: 100, cellClass : 'grid-align-left', enableCellEdit: false, enableFiltering: false, enableColumnMenu: false},  
			{ displayName : 'Unidad de Medida', category: " ", tipoMetaId: META_ID_UNIDADMEDIDA, name : 'unidadDeMedidaId', width: 100, cellClass : 'grid-align-left', 
				enableCellEdit: true, enableFiltering: false, editableCellTemplate: 'ui-grid/dropdownEditor',
				editDropdownValueLabel: 'nombre', editDropdownOptionsArray: [], enableColumnMenu: false,
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); },
				cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.nombreUnidadMedida(row.entity.unidadDeMedidaId)}}</div>'
			},  
			{ displayName : 'Tipo de Dato', category: " ", tipoMetaId: META_ID_TIPODATO, name : 'datoTipoId', width: 100, cellClass : 'grid-align-left', 
				enableCellEdit: true, enableFiltering: false, enableColumnMenu: false, editableCellTemplate: 'ui-grid/dropdownEditor',
				editDropdownValueLabel: 'nombre', editDropdownOptionsArray: [],
				cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); },
				cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.nombreDatoTipo(row.entity.datoTipoId)}}</div>'
			}];
		
		if (mi.agrupacion == 1){
			for (anioInicio = mi.fechaInicio.getFullYear(); anioInicio<=(anioFin); anioInicio++){
				for (mes = 0; mes < 12; mes++){
					columnDefs.push({ displayName: 'Planificada', category: nombreMes[mes]+"-"+anioInicio, name: mes+'P'+anioInicio, width: 100, cellClass : 'grid-align-center', 
						tipoMeta: 'P', anio: anioInicio, mes: mes, idMeta: function( $scope ) { return ($scope.row.entity[mes+'P'+anioInicio+'Id']); },  
						enableCellEdit: true, enableFiltering : false, enableColumnMenu: false,
						cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
					});
					columnDefs.push({ displayName: 'Real', category: nombreMes[mes]+"-"+anioInicio, name: mes+'R'+anioInicio, idMeta: function( $scope ) { return ($scope.row.entity[mes+'R'+anioInicio+'Id']); },  width: 100, cellClass : 'grid-align-center', 
						tipoMeta: 'R', anio: anioInicio, mes: mes, idMeta: function( $scope ) { return ($scope.row.entity[mes+'R'+anioInicio+'Id']); },  
						enableCellEdit: true, enableFiltering : false, enableColumnMenu: false,
						cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
					});
					mi.opcionesGrid.category.push({name: nombreMes[mes]+"-"+anioInicio, visible: true});
				}
			}
		}
		columnDefs.push({ displayName: 'Linea Base', name: 'lineaBase', width: 100, cellClass : 'grid-align-center', 
			tipoMeta: 'L',idMeta: function( $scope ) { return ($scope.row.entity[mes+'P'+anioInicio+'Id']); },  
			enableCellEdit: true, enableFiltering : false, enableColumnMenu: false,
			cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
		});
		columnDefs.push({ displayName: 'Meta Final', name: 'metaFinal', width: 100, cellClass : 'grid-align-center', 
			tipoMeta: 'F', idMeta: function( $scope ) { return ($scope.row.entity[mes+'P'+anioInicio+'Id']); },  
			enableCellEdit: true, enableFiltering : false, enableColumnMenu: false,
			cellEditableCondition: function( $scope ) { return ($scope.row.entity.objetoTipo >= 3); }
		});
		
		mi.opcionesGrid.columnDefs = columnDefs;
		mi.opcionesGrid.columnDefs[3].editDropdownOptionsArray = mi.unidadesMedida;
		mi.opcionesGrid.columnDefs[4].editDropdownOptionsArray = mi.datoTipos;
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
		rowEditWaitInterval: 5000,
		headerTemplate: '<div role="rowgroup" class="ui-grid-header"> <div class="ui-grid-top-panel"> <div class="ui-grid-header-viewport"> <div class="ui-grid-header-canvas"> <div class="ui-grid-header-cell-wrapper" ng-style="colContainer.headerCellWrapperStyle()"> <div role="row" class="ui-grid-header-cell-row"> <div class="ui-grid-header-cell ui-grid-clearfix ui-grid-category" ng-repeat="cat in grid.options.category" ng-if="cat.visible &&  (colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }).length > 0"> {{cat.name}} <div class="ui-grid-header-cell ui-grid-clearfix" ng-repeat="col in colContainer.renderedColumns | filter:{ colDef:{category: cat.name} }" ui-grid-header-cell col="col" render-index="$index"> </div> </div><!--!cat.visible && --> <div class="ui-grid-header-cell ui-grid-clearfix" ng-if="col.colDef.category === undefined" ng-repeat="col in colContainer.renderedColumns track by col.uid" ui-grid-header-cell col="col" render-index="$index"> </div></div></div></div></div></div></div>',
	    columnDefs : [ ],
		category: [{name: " ", visible: true}],
		
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;

			/*
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.meta = row.entity;
			});
			*/
			
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
			
			gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
				if (newValue != oldValue){
					console.log(rowEntity);
					console.log(colDef);
					console.log(colDef.idMeta);
				}
			});
		    
			gridApi.rowEdit.on.saveRow($scope, function( rowEntity ) {
		    	    var promise = mi.guardarMeta(rowEntity);
		            mi.gridApi.rowEdit.setSavePromise(rowEntity, promise);
		    	  });
		}
	}
	 
	 mi.generar = function(){
			if(mi.prestamo.value > 0){
				if (mi.fechaInicio <= mi.fechaFin){
					if(mi.agrupacion > 0){
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
		
	mi.guardarMeta = function(rowEntity){
		var defered = $q.defer();
        var promise = defered.promise;
        
    	if (rowEntity.datoTipoId != "" && rowEntity.unidadDeMedidaId != ""
    		&& rowEntity.datoTipoId != null && rowEntity.unidadDeMedidaId != null){
	        
			$http.post('/SPrestamoMetas', {
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
			$http.post('/SPrestamoMetas', { accion: 'exportarExcel', proyectoid:$routeParams.proyectoId,t:moment().unix()
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



		