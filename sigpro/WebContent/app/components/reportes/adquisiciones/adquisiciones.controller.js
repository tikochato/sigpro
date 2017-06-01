var app = angular.module('adquisicionesController',['ngAnimate', 'ngTouch', 'ui.grid.edit', 'ui.grid.rowEdit']);

app.controller('adquisicionesController', ['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion){
		var mi = this;
		i18nService.setCurrentLang('es');
		mi.mostrarCongelar = false;
		mi.mostrarCopiar = false;
		mi.modificarTabla = true;
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	    
	    mi.reiniciarVista=function(){
			if($location.path()=='/adquisiciones/rv')
				$route.reload();
			else
				$location.path('/adquisiciones/rv');
		}		
		
		mi.formatofecha = 'yyyy';
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.prestamo = mi.prestamos[0];
		
		mi.informes = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
			{'value' : 1, 'text' : 'Ejecucion Financiera Base'},
			{'value' : 2, 'text' : 'Ejecucion Financiera Modificada'},
		];
		
		mi.informe = mi.informes[0];
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
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
		
		mi.generar = function(){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null)
				{
					if(mi.informe.value > 0){
						$http.post('/SReporte',{
							accion: 'generarInforme',
							idPrestamo: mi.prestamo.value,
							tipoInforme: mi.informe.value,
							anio: moment(mi.fechaInicio).format('DD/MM/YYYY')
						}).success(function(response){
							if (response.success){
								mi.crearArbol(response.prestamo);
							}
						})
					}else
						$utilidades.mensaje('warning','Debe seleccionar tipo de informe a generar');
				}else
					$utilidades.mensaje('warning','Debe seleccionar un año');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
		
		mi.congelar = function(ev){
			if(mi.gridOptions.data.length > 0){			
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Congelado"
						, '¿Desea congelar el informe "'+mi.prestamo.text+'"?'
						, "Congelar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){						
						$http.post('/SReporte', {
							accion: 'congelarInforme',
							id: mi.gridOptions.data[0].id,
							t:moment().unix()
						}).success(function(response){
							if(response.success){
								mi.mostrarCongelar = false;
								mi.modificarTabla = false;
								mi.mostrarCopiar = true;
								$utilidades.mensaje('success','Informe congelado');
							}
							else
								$utilidades.mensaje('danger','Error al congelar el informe');
						});
					}
				}, function(){
					
				});
			
			}else
				$utilidades.mensaje('warning','Debe de cargar un informe de préstamo para poder congelarlo');
		}
		
		mi.crearArbol = function(datos){
			if (datos.length > 0){
				var data = datos;
				data = mi.gethijos(data);
				for(x in data){
					data[x].$$treeLevel = Number(data[x].posicionArbol) - 1;
				}
				mi.gridOptions.data = data;
				
				if(mi.gridOptions.data[0].estado == 1){
					mi.mostrarCongelar = true;
					mi.mostrarCopiar = false;
					mi.modificarTabla = true;
				}else if (mi.gridOptions.data[0].estado == 2){
					mi.mostrarCongelar = false;
					mi.modificarTabla = false;
					mi.mostrarCopiar = true;
				}
				
				$timeout(function(){
				     mi.gridApi.treeBase.expandAllRows();
				     mi.totalP = 0;
			   })
			}
		}
		
		mi.rowTemplate = function(){
			return '<div style="background-color: blue">{{grid.scope.controller.gridOptions.data}}</div>';
		}

		mi.gridOptions = {
				showColumnFooter: true,
				enableCellEdit: true,
				rowEditWaitInterval: 1,
				expandAllRows : true,
				enableExpandableRowHeader: false,
				showTreeRowHeader: false,
				showTreeExpandNoChildren : false,
			    columnDefs: [
			        { name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: '', 
			        	cellTemplate: "<div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>"
			        },
			        { name: 'Mes1', width: 100, displayName: 'Enero', type: 'number', 
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes1}}</div>'
			        },
			        { name: 'Mes2', width: 100, displayName: 'Febrero', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes2}}</div>'
			        },
			        { name: 'Mes3',  width: 100, displayName: 'Marzo', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes3}}</div>'
			        },
			        { name: 'Mes4',  width: 100, displayName: 'Abril', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes4}}</div>'
			        },
			        { name: 'Mes5',  width: 100, displayName: 'Mayo', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes5}}</div>'
			        },
			        { name: 'Mes6',  width: 100, displayName: 'Junio', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes6}}</div>'
			        },
			        { name: 'Mes7',  width: 100, displayName: 'Julio', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes7}}</div>'
			        },
			        { name: 'Mes8',  width: 100, displayName: 'Agosto', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes8}}</div>'
			        },
			        { name: 'Mes9',  width: 100, displayName: 'Septiembre', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes9}}</div>'
			        },
			        { name: 'Mes10',  width: 100, displayName: 'Octubre', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes10}}</div>'
			        },
			        { name: 'Mes11',  width: 100, displayName: 'Noviembre', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes11}}</div>'
			        },
			        { name: 'Mes12',  width: 100, displayName: 'Diciembre', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.gridOptions.data[0].Mes12}}</div>'
			        },
			        { name: 'Total',  enableCellEdit: false, width: 150, displayName: 'Total general', type: 'number',
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.totalP}}</div>'
			        }
			    ],
			    onRegisterApi: function( gridApi ) {
			      mi.gridApi = gridApi;
			      
			      mi.gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
			    	  if(mi.modificarTabla == true){
				    	  if (newValue != oldValue){
				    		  if (mi.getSumaHijos(rowEntity, colDef) == true){
				    			  mi.calcular(rowEntity, colDef, newValue);
				    			  rowEntity.Total = rowEntity.Mes1 + rowEntity.Mes2 + rowEntity.Mes3 + rowEntity.Mes4 + rowEntity.Mes5 + rowEntity.Mes6 + rowEntity.Mes7 + rowEntity.Mes8 + rowEntity.Mes9 + rowEntity.Mes10 + rowEntity.Mes11 + rowEntity.Mes12 ;
				    			  var deferred = $q.defer();
				    			  mi.gridApi.rowEdit.setSavePromise(rowEntity, deferred.resolve());
				    		  }else{
				    			  mi.setValor(rowEntity,colDef,oldValue);
				    			  var deferred = $q.defer();
				    			  mi.gridApi.rowEdit.setSavePromise(rowEntity, deferred.resolve());
				    		  }
				    	  }
			    	  }else{
			    		  mi.setValor(rowEntity,colDef,oldValue);
		    			  var deferred = $q.defer();
		    			  mi.gridApi.rowEdit.setSavePromise(rowEntity, deferred.resolve());
			    	  }
			      });
			      
			     mi.gridApi.rowEdit.on.saveRow($scope, mi.saveRow);
			  }
		};
		
		mi.saveRow = function(rowEntity){
            var promise = mi.guardarRowInforme(rowEntity);
            mi.gridApi.rowEdit.setSavePromise(rowEntity, promise);
		}
		
		mi.getSumaHijos = function(rowEntity, colDef){
			var sumaValores = 0;
			for(x in rowEntity.hijo){
				var hijo = rowEntity.hijo[x].split(',');
				sumaValores += mi.obtenerValor(mi.obtenerEntidad(hijo[0], hijo[1]), colDef);
			}
			
			if (sumaValores > 0)
				return false;
			else
				return true;
		}
      
        mi.guardarRowInforme = function(rowEntity){
            var defered = $q.defer();
            var promise = defered.promise;
                   
             $http.post('/SReporte', {
                    accion: 'guardarInformePrestamo',
					tipoInforme: mi.informe.value,
					anio: moment(mi.fechaInicio).format('DD/MM/YYYY'),
                    id: rowEntity.id,
                    idObjetoTipo: rowEntity.idObjetoTipo,
                    objetoTipo: rowEntity.objetoTipo,
                    nombre: rowEntity.nombre,
                    mes1: rowEntity.Mes1,
                    mes2: rowEntity.Mes2,
                    mes3: rowEntity.Mes3,
                    mes4: rowEntity.Mes4,
                    mes5: rowEntity.Mes5,
                    mes6: rowEntity.Mes6,
                    mes7: rowEntity.Mes7,
                    mes8: rowEntity.Mes8,
                    mes9: rowEntity.Mes9,
                    mes10: rowEntity.Mes10,
                    mes11: rowEntity.Mes11,
                    mes12: rowEntity.Mes12,
                    total: rowEntity.Total
                }).success(function(response){
                    if(response.success){
                        defered.resolve();
                    }
                    else
                        defered.reject()
                });
             return promise;
         }
        
		
		mi.setColorRow = function(grid){
			if (grid.objetoTipo == 1)
				return 'blue';
		}
		
		mi.gethijos = function(data){
			var nuevo = [];
			for (x in data){
				if(data[x].idPredecesor === 0)
				{
					nuevo.push(data[x]);
				}else{
					nuevo.push(data[x]);
					nuevo = mi.obtenerHijo(nuevo,data[x]);
				}
		    }
			return nuevo;
		}
		
		mi.obtenerHijo = function(lista, dato){
			for (x in lista){
				if (dato.idPredecesor === lista[x].idObjetoTipo && dato.objetoTipoPredecesor === lista[x].objetoTipo){
					var listaInterna = lista[x].hijo == null ? [] : lista[x].hijo;
					listaInterna.push(dato.idObjetoTipo+","+dato.objetoTipo);
		            lista[x].hijo = listaInterna;
		            return lista;
				}
			}
		}

		mi.toggleRow = function(row, evt) {
			uiGridTreeBaseService.toggleRowTreeState(mi.gridApi.grid, row, evt);
		};
		
		mi.totalPrestamo = function(){
			mi.totalP = mi.gridOptions.data[0].Mes1 + mi.gridOptions.data[0].Mes2 + mi.gridOptions.data[0].Mes3 + 
				mi.gridOptions.data[0].Mes4 + mi.gridOptions.data[0].Mes5 + mi.gridOptions.data[0].Mes6 + 
				mi.gridOptions.data[0].Mes7 + mi.gridOptions.data[0].Mes8 + mi.gridOptions.data[0].Mes9 + 
				mi.gridOptions.data[0].Mes10 + mi.gridOptions.data[0].Mes11 + mi.gridOptions.data[0].Mes12;
		}
		
		mi.calcular = function(rowEntidad, colDef, valor){
			mi.calcularPadre(rowEntidad, colDef, valor);
			mi.totalPrestamo();
		}
		
		mi.calcularPadre = function(rowEntidad, colDef){
			if (rowEntidad.idPredecesor != 0){
				var padre = mi.obtenerEntidad(rowEntidad.idPredecesor, rowEntidad.objetoTipoPredecesor);
				var valorFinal = 0;
				if (padre.hijo != null){
					for(x in padre.hijo){
						var hijo = padre.hijo[x].split(',');
						var valorColumna = mi.obtenerValor(mi.obtenerEntidad(hijo[0], hijo[1]), colDef);
						valorFinal = valorFinal + valorColumna;
					}
					mi.setValor(padre,colDef, valorFinal);
					padre.Total = padre.Mes1 + padre.Mes2 + padre.Mes3 + padre.Mes4 + padre.Mes5 + padre.Mes6 + padre.Mes7 + padre.Mes8 + padre.Mes9 + padre.Mes10 + padre.Mes11 + padre.Mes12 ;
					mi.saveRow(padre);
					mi.calcularPadre(padre, colDef);
				}
			}
		}
		
		mi.obtenerEntidad = function(id, objetoTipo){
			for (x in mi.gridOptions.data){
				if (id == mi.gridOptions.data[x].idObjetoTipo && objetoTipo == mi.gridOptions.data[x].objetoTipo){
					return mi.gridOptions.data[x];
				}
			}
		}
		
		mi.obtenerValor = function(rowEntidad, columna){
			switch (columna.name){
			case "Mes1":
				return rowEntidad.Mes1;
			case "Mes2":
				return rowEntidad.Mes2;
			case "Mes3":
				return rowEntidad.Mes3;
			case "Mes4":
				return rowEntidad.Mes4;
			case "Mes5":
				return rowEntidad.Mes5;
			case "Mes6":
				return rowEntidad.Mes6;
			case "Mes7":
				return rowEntidad.Mes7;
			case "Mes8":
				return rowEntidad.Mes8;
			case "Mes9":
				return rowEntidad.Mes9;
			case "Mes10":
				return rowEntidad.Mes10;
			case "Mes11":
				return rowEntidad.Mes11;
			case "Mes12":
				return rowEntidad.Mes12;
			}
		}
		
		mi.setValor = function(rowEntidad, columna, valor){
			switch (columna.name){
			case "Mes1":
				rowEntidad.Mes1 = valor;
				break;
			case "Mes2":
				rowEntidad.Mes2 = valor;
				break;
			case "Mes3":
				rowEntidad.Mes3 = valor;
				break;
			case "Mes4":
				rowEntidad.Mes4 = valor;
				break;
			case "Mes5":
				rowEntidad.Mes5 = valor;
				break;
			case "Mes6":
				rowEntidad.Mes6 = valor;
				break;
			case "Mes7":
				rowEntidad.Mes7 = valor;
				break;
			case "Mes8":
				rowEntidad.Mes8 = valor;
				break;
			case "Mes9":
				rowEntidad.Mes9 = valor;
				break;
			case "Mes10":
				rowEntidad.Mes10 = valor;
				break;
			case "Mes11":
				rowEntidad.Mes11 = valor;
				break;
			case "Mes12":
				rowEntidad.Mes12 = valor;
				break;
			}
		}
		
		mi.getPrestamos();
}]);