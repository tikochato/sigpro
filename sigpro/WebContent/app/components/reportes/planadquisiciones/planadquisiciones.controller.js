var app = angular.module('planAdquisicionesController', ['ngTouch','ngAnimate','ui.grid.edit', 'ui.grid.rowEdit']);
app.controller('planAdquisicionesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion', '$filter','$uibModal',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion, $filter,$uibModal) {
	var mi = this;
	mi.mostrarcargando = false;
	mi.mostrarDescargar = false;
	mi.mostrarDatos = false;
	mi.enMillones = true;
	mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
	mi.tamanoTotal = mi.tamanoPantalla - 280; 
	mi.estiloCelda = "width:80px;min-width:80px; max-width:80px;text-align: center";
	
	i18nService.setCurrentLang('es');
	
	$scope.divActivo = "";
	mi.activarScroll = function(id){
		$scope.divActivo = id;
    }
	
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
	
	mi.selectedRow = function(row){
		mi.datoSeleccionado = row;
	}
	
	mi.ddlOpciones = [];
	mi.getUnidadMedida = function(){
		$http.post('/SUnidadMedida', {accion: 'getUnidadMedida'}).success(
			function(response){
				mi.ddlOpciones.push({id: 0, value: 'Seleccionar'});
				if(response.success){
					for(x in response.unidadMedida){
						mi.ddlOpciones.push({id: response.unidadMedida[x].id, value: response.unidadMedida[x].nombre});
					}	
				}
		});
	}

	mi.nombreUnidadMedida = function(id){
		for (i=0; i<mi.ddlOpciones.length; i++){
			if(mi.ddlOpciones[i].id == id){
				return mi.ddlOpciones[i].value;
			}
		}
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
           	  if($scope.row.entity.bloquear == true)
           		  return false;
           	  else
           		  return true;
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
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Objeto',
		        	cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.treeNode.children.length > 0}\"><div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>"
		        },
				{ name: 'metodo', enableCellEdit: false,width: 75, displayName: 'Método'},
				{ name: 'unidadMedida', width: 100, displayName: 'Unidad de Medida', editType: 'dropdown', editableCellTemplate: 'ui-grid/dropdownEditor',
					editDropdownOptionsArray: mi.ddlOpciones,
					editDropdownValueLabel: 'value',
					cellTemplate: '<div class="ui-grid-cell-contents">{{grid.appScope.controller.nombreUnidadMedida(row.entity.unidadMedida)}}</div>'
		        },
				{ name: 'cantidad', enableCellEdit: true,width: 75, displayName: 'Cantidad', type: 'number'},
				{ name: 'costo', enableCellEdit: true,width: 75, displayName: 'Costo', type: 'number', cellFilter:'number:2'},
				{ name: 'total', enableCellEdit: true, enableCellEdit: false, width: 125, displayName: 'Total', type: 'number', cellFilter:'number:2'},
				{ name: 'planificadoDocs', enableCellEdit: true, width: 100, displayName: 'Planificado', category: 'PreparacionDocumentos', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realDocs', enableCellEdit: true, enableCellEditOnFocus: true, width: 100, displayName: 'Real', category: 'PreparacionDocumentos', type: 'date', cellFilter: 'date:"dd/MM/yyyy"',
		        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                return "real";
		            }},
				{ name: 'planificadoLanzamiento', enableCellEdit: true, width: 100, displayName: 'Planificado', category: 'LanzamientoEvento', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realLanzamiento', enableCellEdit: true, enableCellEditOnFocus: true, width: 100, displayName: 'Real', category: 'LanzamientoEvento', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                return "real";
		            }},
				{ name: 'planificadoRecepcionEval', enableCellEdit: true, width: 115, displayName: 'Planificado', category: 'RecepcionEvaluacionOfertas', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realRecepcionEval', enableCellEdit: true, enableCellEditOnFocus: true, width: 115, displayName: 'Real', category: 'RecepcionEvaluacionOfertas', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                return "real";
		            }},
				{ name: 'planificadoAdjudica', enableCellEdit: true, width: 100, displayName: 'Planificado', category: 'Adjudicacion', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realAdjudica', enableCellEdit: true, enableCellEditOnFocus: true, width: 100, displayName: 'Real', category: 'Adjudicacion', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                return "real";
		            }},
				{ name: 'planificadoFirma', enableCellEdit: true, width: 100, displayName: 'Planificado', category: 'FirmaContrato', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''},
				{ name: 'realFirma', enableCellEdit: true, enableCellEditOnFocus: true, width: 100, displayName: 'Real', category: 'FirmaContrato', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
		        	cellClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
		                return "real";
		        	}
				},
				{ name: 'nada', enableCellEdit: false, width: 20, displayName: '', pinnedRight:true}
			],
			onRegisterApi: function(gridApi) {
			      mi.gridApi = gridApi;
			      
			      mi.gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
			    	  if(newValue != oldValue){
			    		  rowEntity['ocultarLimpiar'] = false;
			    		  rowEntity['modificado'] = true;
			    		  mi.habilitarPadre(rowEntity['idPredecesor'], rowEntity['objetoTipoPredecesor']);
			    		  
			    		  var index = mi.guardarObjetos.includes(rowEntity['idObjetoTipo']+","+rowEntity['objetoTipo']);
			    		  
			    		  if(!index)
			    			  mi.guardarObjetos.push(rowEntity['idObjetoTipo']+","+rowEntity['objetoTipo']);
			    		  
			    		  if(rowEntity['planAdquisicionId'] > 0){
			    			  index = mi.borrarObjetos.indexOf(rowEntity['planAdquisicionId']);
			    			  mi.borrarObjetos.splice(index, 1);
			    			  rowEntity['ocultarLimpiar'] = false;
			    		  }
			    	  }
			    	  
			    	  if(colDef.name == 'cantidad' || colDef.name == 'precio'){
			    		  if(newValue < 0){
			    			  $utilidades.mensaje('danger','No se permiten números negativos');
			    			  rowEntity[colDef.name] = oldValue;
			    			  return;
			    		  }
			    	  }
			    	  
			    	  rowEntity['total'] = rowEntity['costo'] * rowEntity['cantidad'];
			    	  mi.calcularPadre(rowEntity['idPredecesor'], rowEntity['objetoTipoPredecesor']);
			    	  
			    	  if(Number(rowEntity['total']) > 0){
			    		  mi.habilitarHijo(rowEntity['hijo'], true);
			    	  }
			    	  
			    	  if(colDef.name == 'planificadoLanzamiento'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['planificadoDocs']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Lanzamiento de eventos planificado no debe ser menor que Preparación de documentos planificado');
			    		  }
			    	  } else if(colDef.name == 'planificadoRecepcionEval'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['planificadoLanzamiento']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Recepción y evaluación de ofertas planificado no debe ser menor que Lanzamiento de vento planificado');
			    		  }
			    	  } else if(colDef.name == 'planificadoAdjudica'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['planificadoRecepcionEval']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Adjudicación planificado no debe ser menor que Recepción y evaluación  de ofertas planificado');
			    		  }
			    	  } else if(colDef.name == 'planificadoFirma'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['planificadoAdjudica']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Firma de contrato planificado no debe ser menor que Adjudicación planificado');
			    		  }
			    	  }
			    	  
			    	  if(colDef.name == 'realLanzamiento'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['realDocs']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Lanzamiento de eventos real no debe ser menor que Preparación de documentos real');
			    		  }
			    	  } else if(colDef.name == 'realRecepcionEval'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['realLanzamiento']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Recepción y evaluación de ofertas real no debe ser menor que Lanzamiento de vento real');
			    		  }
			    	  } else if(colDef.name == 'realAdjudica'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['realRecepcionEval']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Adjudicación real no debe ser menor que Recepción y evaluación  de ofertas real');
			    		  }
			    	  } else if(colDef.name == 'realFirma'){
			    		  if (moment(newValue).format('DD/MM/YYYY') < moment(rowEntity['realAdjudica']).format('DD/MM/YYYY')){
			    			  rowEntity[colDef.name] = "";
			    			  $utilidades.mensaje('danger', 'Firma de contrato real no debe ser menor que Adjudicación real');
			    		  }
			    	  }
			      });
			      
			      //mi.gridApi.rowEdit.on.saveRow($scope, mi.saveRow);
			}
	};
	
	mi.habilitarPadre = function(idPredecesor, objetoTipoPredecesor){
		var padre = mi.obtenerEntidad(idPredecesor, objetoTipoPredecesor);
		if (padre != undefined){
			var contador = 0;
			for(i in padre.hijo){
				var hijo = padre.hijo[i].split(',');
				var entidadHijo = mi.obtenerEntidad(hijo[0],hijo[1]);
				if(entidadHijo.bloquear == true || entidadHijo.modificado == true){
					padre.bloquear = true;
					mi.habilitarPadre(padre.idPredecesor, padre.objetoTipoPredecesor);
					break;
				}
				contador++;
			}
			
			if(contador == padre.hijo.length){
				padre.bloquear = false;
				if(padre.idPredecesor > 0){
					mi.habilitarPadre(padre.idPredecesor, padre.objetoTipoPredecesor);
				}
			}
		}
	}
	
	mi.habilitarHijo = function(hijos, bloqueo){
		for(i in hijos){
			var hijo = hijos[i].split(',');
			var entidadHijo = mi.obtenerEntidad(hijo[0],hijo[1]);
			entidadHijo.bloquear = bloqueo;
			mi.habilitarHijo(entidadHijo.hijo, bloqueo);
		}
	}
	
	mi.limpiar = function(rowEntity){
		mi.habilitarHijo(rowEntity.entity.hijo, false);
		if(rowEntity.entity.planAdquisicionId != 0){
			mi.borrarObjetos.push(rowEntity.entity.planAdquisicionId);
		}
		
		var index = mi.guardarObjetos.indexOf(rowEntity.entity['idObjetoTipo']+","+rowEntity.entity['objetoTipo']);
		mi.guardarObjetos.splice(index, 1);
		
		rowEntity.entity.contieneInfoPlan = false;
		rowEntity.entity.ocultarPagos = true;
		rowEntity.entity.ocultarLimpiar = true;
		rowEntity.entity['modificado'] = false;
		rowEntity.entity['metodo'] = 0;
		rowEntity.entity['unidadMedida'] = 0;
		rowEntity.entity['cantidad'] = 0;
		rowEntity.entity['costo'] = 0;
		rowEntity.entity['total'] = 0;
		rowEntity.entity['planificadoDocs'] = '';
		rowEntity.entity['realDocs'] = '';
		rowEntity.entity['planificadoLanzamiento'] = '';
		rowEntity.entity['realLanzamiento'] = '';
		rowEntity.entity['planificadoRecepcionEval'] = '';
		rowEntity.entity['realRecepcionEval'] = '';
		rowEntity.entity['planificadoAdjudica'] = '';
		rowEntity.entity['realAdjudica'] = '';
		rowEntity.entity['planificadoFirma'] = '';
		rowEntity.entity['realFirma'] = '';
		
		var padre = rowEntity.entity.idPredecesor;
		var tipoPadre = rowEntity.entity.objetoTipoPredecesor;
		mi.calcularPadre(padre, tipoPadre);
		mi.habilitarPadre(padre, tipoPadre);
		
		if(rowEntity.entity['idObjetoTipo'] == mi.idPrestamo){
			var entidad = mi.obtenerEntidad(mi.idPrestamo,1);
			$http.post('/SPlanAdquisiciones',{
				accion: 'borrarPlan',
				idPlanAdquisiciones: entidad.planAdquisicionId,
			}).success(function(response){
				
			});
		}
	}
	
	mi.borrarHijos = function(hijos){
		for(p in hijos){
			var hijo = hijos[p].split(',');
			var entidadHijo = mi.obtenerEntidad(hijo[0],hijo[1]);
			
			$http.post('/SPlanAdquisiciones',{
				accion: 'borrarPlan',
				idPlanAdquisiciones: entidadHijo.planAdquisicionId,
			}).success(function(response){
				
			});
			
			if(entidadHijo.hijo.length > 0){
				mi.borrarHijos(entidadHijo.hijo);
			}
		}
	}
	
	mi.guardarPlan = function(){
		var sinError = true;
		
		var h = 0;
		for(h in mi.borrarObjetos){
			$http.post('/SPlanAdquisiciones',{
				accion: 'borrarPlan',
				idObjeto:idObjeto,
				objetoTipo:objetoTipo,
				//idPlanAdquisiciones: mi.borrarObjetos[h],
			}).success(function(response){
				
			});
		}
		
		var p = 0;
		for(p in mi.guardarObjetos){
			var objeto = mi.guardarObjetos[p];
			objeto = objeto.split(',');
			var entidad = mi.obtenerEntidad(objeto[0],objeto[1]);
			$http.post('/SPlanAdquisiciones', {
	            accion: 'guardarPlan',
	            idObjetoTipo: entidad.idObjetoTipo,
	            objetoTipo: entidad.objetoTipo,
	            idPlanAdquisicion: entidad.planAdquisicionId,
	            esnuevo: entidad.planAdquisicionId == 0 ? true : false,
	            unidadMedida: entidad.unidadMedida,
	            cantidad: entidad.cantidad,
	            costo: entidad.costo,
	            total: entidad.total,
	            planificadoDocs: moment(entidad.planificadoDocs).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoDocs).format('DD/MM/YYYY'),
        		realDocs: moment(entidad.realDocs).format('DD/MM/YYYY') == null ? null : moment(entidad.realDocs).format('DD/MM/YYYY'),
	            planificadoLanzamiento: moment(entidad.planificadoLanzamiento).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoLanzamiento).format('DD/MM/YYYY'),
	            realLanzamiento: moment(entidad.realLanzamiento).format('DD/MM/YYYY') == null ? null : moment(entidad.realLanzamiento).format('DD/MM/YYYY'),
	            planificadoRecepcionEval: moment(entidad.planificadoRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoRecepcionEval).format('DD/MM/YYYY'),
	            realRecepcionEval: moment(entidad.realRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(entidad.realRecepcionEval).format('DD/MM/YYYY'),
	    		planificadoAdjudica: moment(entidad.planificadoAdjudica).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoAdjudica).format('DD/MM/YYYY'),
	            realAdjudica: moment(entidad.realAdjudica).format('DD/MM/YYYY') == null ? null : moment(entidad.realAdjudica).format('DD/MM/YYYY'),
	    		planificadoFirma: moment(entidad.planificadoFirma).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoFirma).format('DD/MM/YYYY'),
	            realFirma: moment(entidad.realFirma).format('DD/MM/YYYY') == null ? null : moment(entidad.realFirma).format('DD/MM/YYYY')
	        }).success(function(response){
	        	var entity = mi.obtenerEntidad(response.idObjetoTipo,response.objetoTipo);
	        	entity['planAdquisicionId'] = response.planAdquisicionId;
	        }).error(function(response){
	        	$utilidades.mensaje('danger','Error al guardar el plan para ' + entidad.nombre);
	        	sinError = false;
	        });
		}
		
		/*
		for(h in mi.gridOptions.data){
			var entidad = mi.gridOptions.data[h];
			
			if(entidad.modificado == true){
				var hijos = entidad.hijo;
				if(hijos.length > 0){
					mi.borrarHijos(hijos);
				}
				
				$http.post('/SPlanAdquisiciones', {
		            accion: 'guardarPlan',
		            idObjetoTipo: entidad.idObjetoTipo,
		            objetoTipo: entidad.objetoTipo,
		            idPlanAdquisicion: entidad.planAdquisicionId,
		            esnuevo: entidad.planAdquisicionId == 0 ? true : false,
		            unidadMedida: entidad.unidadMedida,
		            cantidad: entidad.cantidad,
		            costo: entidad.costo,
		            total: entidad.total,
		            planificadoDocs: moment(entidad.planificadoDocs).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoDocs).format('DD/MM/YYYY'),
		            realDocs: moment(entidad.realDocs).format('DD/MM/YYYY') == null ? null : moment(entidad.realDocs).format('DD/MM/YYYY'),
		            planificadoLanzamiento: moment(entidad.planificadoLanzamiento).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoLanzamiento).format('DD/MM/YYYY'),
		            realLanzamiento: moment(entidad.realLanzamiento).format('DD/MM/YYYY') == null ? null : moment(entidad.realLanzamiento).format('DD/MM/YYYY'),
		            planificadoRecepcionEval: moment(entidad.planificadoRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoRecepcionEval).format('DD/MM/YYYY'),
		            realRecepcionEval: moment(entidad.realRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(entidad.realRecepcionEval).format('DD/MM/YYYY'),
		    		planificadoAdjudica: moment(entidad.planificadoAdjudica).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoAdjudica).format('DD/MM/YYYY'),
		            realAdjudica: moment(entidad.realAdjudica).format('DD/MM/YYYY') == null ? null : moment(entidad.realAdjudica).format('DD/MM/YYYY'),
		    		planificadoFirma: moment(entidad.planificadoFirma).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoFirma).format('DD/MM/YYYY'),
		            realFirma: moment(entidad.realFirma).format('DD/MM/YYYY') == null ? null : moment(entidad.realFirma).format('DD/MM/YYYY')
		        }).success(function(response){
		        	entidad['planAdquisicionId'] = response.data.planAdquisicionId;
		        }).error(function(response){
		        	$utilidades.mensaje('danger','Error al guardar el plan para ' + entidad.nombre);
		        	sinError = false;
		        });
			}
		}*/
		
		if(sinError){
			$utilidades.mensaje('success','Plan de adquisiciones guardado exitosamente');
			//mi.generar();
		}
	}
	
	mi.calcularPadre = function(idPredecesor, objetoTipoPredecesor){
		var padre = mi.obtenerEntidad(idPredecesor, objetoTipoPredecesor);
		if(padre != undefined){
			var hijo = padre.hijo;
			var total = 0;
			for(y in hijo){
				var h = mi.obtenerEntidad(hijo[y].split(',')[0],hijo[y].split(',')[1]);
				total += h.total;
			}
			padre.total = total;
			if(padre.idPredecesor > 0 && padre.idPredecesor){
				if(padre.idPredecesor != 0){
					mi.calcularPadre(padre.idPredecesor, padre.objetoTipoPredecesor);
				}
			}
		}
	}
	
	mi.obtenerEntidad = function(id, objetoTipo){
		for (x in mi.data){
			if (id == mi.data[x].idObjetoTipo && objetoTipo == mi.data[x].objetoTipo){
				return mi.data[x];
			}
		}
	}
	
	mi.generar = function(){
		if(mi.prestamo.value > 0){
			mi.mostrarcargando = true;
			mi.idPrestamo = mi.prestamo.value;
			$http.post('/SPlanAdquisiciones',{
				accion: 'generarPlan',
				idPrestamo: mi.idPrestamo,
				informeCompleto: mi.informeCompleto,
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.proyecto);
					mi.exportar = true;
					mi.guardar = true;
					mi.planPagos = true;
					mi.limpiarRow = true;
					mi.mostrarcargando = false;
					mi.mostrarDatos = true;
					mi.borrarObjetos = [];
					mi.guardarObjetos = [];
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
		//uiGridTreeBaseService.toggleRowTreeState(mi.gridApi.grid, row, evt);
	};
	
	mi.claseIcon = function (item) {
	    switch (item.objetoTipo) {
	        case 1:
	            return 'glyphicon glyphicon-record';
	        case 2:
	            return 'glyphicon glyphicon-th';
	        case 3:
	            return 'glyphicon glyphicon-certificate';
	        case 4:
	            return 'glyphicon glyphicon-link';
	        case 5:
	            return 'glyphicon glyphicon-th-list';
	    }
	};
	
	mi.crearArbol = function(datos){
		/*if (datos.length > 0){
			mi.data = datos;
			for(x in mi.data){
				if(mi.data[x].modificado == true){
					var i = x;
					var idPredecesor = mi.data[x].idPredecesor;
					var objetoPredecesor = mi.data[x].objetoTipoPredecesor;
					mi.calcularPadre(idPredecesor, objetoPredecesor);
					x = i;
				}
				
				mi.data[x].costo = mi.data[x].costo != null ? mi.data[x].costo : 0;
				mi.data[x].total = mi.data[x].total != null ? mi.data[x].total : 0;
				mi.data[x].planificadoDocs = mi.data[x].planificadoDocs != null ? moment(mi.data[x].planificadoDocs,'DD/MM/YYYY').toDate() : "";
				mi.data[x].realDocs = mi.data[x].realDocs != null ? moment(mi.data[x].realDocs,'DD/MM/YYYY').toDate() : "";
				mi.data[x].planificadoLanzamiento = mi.data[x].planificadoLanzamiento != null ? moment(mi.data[x].planificadoLanzamiento,'DD/MM/YYYY').toDate() : "";
				mi.data[x].realLanzamiento = mi.data[x].realLanzamiento != null ? moment(mi.data[x].realLanzamiento,'DD/MM/YYYY').toDate() : "";
				mi.data[x].planificadoRecepcionEval = mi.data[x].planificadoRecepcionEval != null ? moment(mi.data[x].planificadoRecepcionEval,'DD/MM/YYYY').toDate() : "";
				mi.data[x].realRecepcionEval = mi.data[x].realRecepcionEval != null ? moment(mi.data[x].realRecepcionEval,'DD/MM/YYYY').toDate() : "";
				mi.data[x].planificadoAdjudica = mi.data[x].planificadoAdjudica != null ? moment(mi.data[x].planificadoAdjudica,'DD/MM/YYYY').toDate() : "";
				mi.data[x].realAdjudica = mi.data[x].realAdjudica != null ? moment(mi.data[x].realAdjudica,'DD/MM/YYYY').toDate() : "";
				mi.data[x].planificadoFirma = mi.data[x].planificadoFirma != null ? moment(mi.data[x].planificadoFirma,'DD/MM/YYYY').toDate() : "";
				mi.data[x].realFirma = mi.data[x].realFirma != null ? moment(mi.data[x].realFirma,'DD/MM/YYYY').toDate() : "";
				
				mi.habilitarPadre(idPredecesor, objetoPredecesor);
			}
		}*/
		
		mi.data = datos;
		var tab = "\t";
		for(x in mi.data){
			mi.data[x].nombre = tab.repeat(mi.data[x].objetoTipo -1) + mi.data[x].nombre;
		}
		
		mi.rowCollectionPrestamo = [];
		mi.rowCollectionPrestamo = mi.data;
		mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
		mi.mostrarDescargar = true;
		//mi.gridOptions.data = mi.data;
		
    	/*$timeout(function(){
    		mi.gridApi.treeBase.expandAllRows();
	   })*/
	}
	
	mi.setValores = function() {
		row = mi.datoSeleccionado;
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'pago.jsp',
			controller : 'modalPago',
			controllerAs : 'controller',
			backdrop : 'static',
			size : 'lg',
			resolve : {
				idObjeto: function() {
					return row.objetoId;
				},
				objetoTipo: function(){
					return row.objetoTipo;
				},
				nombre: function(){
					return row.nombre;
				}
			}
		});

		modalInstance.result.then(function(resultado) {
			$utilidades.mensaje('success','Pagos agregados con éxito');
		}, function() {
		});
	};
	
	mi.getPrestamos();
	mi.getUnidadMedida();
}]);

app.controller('modalPago', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q' ,'idObjeto','objetoTipo','nombre',modalPago ]);

function modalPago($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, idObjeto, objetoTipo, nombre) {

	var mi = this;
	mi.planAdquisicionesPagos = [];
	mi.idObjeto = idObjeto;
	mi.objetoTipo = objetoTipo;
	mi.nombre = nombre;
	mi.formatofecha = 'MMMM';
	mi.enMillones = true;
	mi.mostrarcargando = false;
	
	mi.fechaOptions = {
			formatYear : 'MMM',
		    startingDay: 1,
		    minMode: 'month'
	};

	mi.ff_opciones = {
			formatYear : 'MMM',
		    startingDay: 1,
		    minMode: 'month'
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
	
	mi.cargarPagos = function(){
		mi.mostrarcargando = true;
		$http.post('/SPago',{ accion: 'getPagos', idObjeto:idObjeto,objetoTipo:objetoTipo,t:moment().unix()}).then(
				function(response) {
					if (response.data.success){
						for(x in response.data.pagos){
							response.data.pagos[x].pago = parseFloat(response.data.pagos[x].pago).toFixed(2);
							response.data.pagos[x].fecha = moment(moment(response.data.pagos[x].fechaReal,'DD/MM/YYYY').toDate()).format('MMMM');
						}
						mi.planAdquisicionesPagos = response.data.pagos;
						mi.mostrarcargando = false;
					}
			});	
	}
	
	
	mi.agregarPago = function(){
		mi.planAdquisicionesPagos.push({id:0,fecha: moment(mi.fechaPago).format('MMMM'), fechaReal: moment(mi.fechaPago).format('DD/MM/YYYY'), pago: parseFloat(mi.montoPago).toFixed(2), descripcion: mi.descripcion});
		
		mi.fechaPago = null;
		mi.montoPago = null;
		mi.descripcion = null;
	}
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarPagos',
				idPlanAdquisiciones: idPlanAdquisiciones,
				pagos: JSON.stringify(mi.planAdquisicionesPagos),
				t:moment().unix()
			};
			$http.post('/SPago',param_data).then(
				function(response) {
					if (response.data.success) {
						mi.planAdquisicionesPagos = response.data.pagos;
						$uibModalInstance.close(true);
					}else
						$uibModalInstance.close(false);
			});
	};
	
	mi.eliminarPago = function(row){
		var index = mi.planAdquisicionesPagos.indexOf(row);
        if (index !== -1) {
        	if(row.id != 0){
	        	var param_data = {
	    			accion: 'eliminarPago',
	    			idPago: row.id,
	    			t:moment().unix()
	        	};
	        	$http.post('/SPago',param_data).then(function(response){
	        		if(response.data.success){
	        			mi.planAdquisicionesPagos.splice(index, 1);
	        		}
	        	});
        	}else{
        		mi.planAdquisicionesPagos.splice(index, 1);
        	}
        }
	}

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cargarPagos();
}

app.directive('scrollespejo', ['$window', function($window) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('scroll', function() {
                var elemento = element[0];
                if (elemento.id == scope.divActivo){
      	          if(elemento.id == 'divTablaNombres'){
      	            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
      	          }else if(elemento.id == 'divTablaDatos'){
      	            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
      	            document.getElementById("divCabecerasDatos").scrollLeft = elemento.scrollLeft;
      	          }else{
      	            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
      	          }
      	        }
            });
        }
    };
}])

;