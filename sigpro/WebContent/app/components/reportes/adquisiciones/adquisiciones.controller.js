var app = angular.module('adquisicionesController',['ngAnimate', 'ngTouch', 'ui.grid.edit', 'ui.grid.rowEdit']);

app.controller('adquisicionesController', ['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion){
		var mi = this;
		mi.gridOptions = {};
		i18nService.setCurrentLang('es');
		mi.mostrarGuardar = false;
		mi.mostrarCongelar = false;
		mi.mostrarCopiar = false;
		mi.modificarTabla = true;
		mi.mostrarDescargar = false;
		mi.totalP = 0;
		mi.autoGuardado = false;
		mi.totales = [];
		mi.columnaNames = [];
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	    
	    mi.reiniciarVista=function(){
			if($location.path()=='/adquisiciones/rv')
				$route.reload();
			else
				$location.path('/adquisiciones/rv');
		}
	    
		mi.formatofecha = 'dd/MM/yyyy';
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.prestamo = mi.prestamos[0];
		
		mi.informes = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.informe = mi.informes[0];
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		};
		
		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2050, 12, 31),
				minDate : new Date(1990, 1, 1),
				startingDay : 1
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
		
		mi.obtenetMesNombre = function(mes, anio){
			switch(mes){
			case 1:
				return "Enero-(" + anio + ")";
			case 2:
				return "Febrero-(" + anio + ")";
			case 3:
				return "Marzo-(" + anio + ")";
			case 4:
				return "Abril-(" + anio + ")";
			case 5:
				return "Mayo-(" + anio + ")";
			case 6:
				return "Junio-(" + anio + ")";
			case 7:
				return "Julio-(" + anio + ")";
			case 8:
				return "Agosto-(" + anio + ")";
			case 9:
				return "Septiembre-(" + anio + ")";
			case 10:
				return "Octubre-(" + anio + ")";
			case 11:
				return "Noviembre-(" + anio + ")";
			case 12:
				return "Diciembre-(" + anio + ")";
			}
		}
		
		mi.generarTabla = function(){
			mi.columnas = [];
			var i = 0;
			var j = 0;
			var mesName = "";
			mi.columnas.push({ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: '',
	        	cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': row.objetoTipo < 5}\"><div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>",
	        	footerCellTemplate: '<div class="ui-grid-cell-contents">Total</div>',
	        });
			
			mi.columnaNames.push("nombre");
			
			var fInicial = moment(mi.fechaInicio).format('DD/MM/YYYY').split('/');
			var fFinal = moment(mi.fechaFin).format('DD/MM/YYYY').split('/');
			var inicio = (Number(fInicial[1]) <= 12 ? Number(fInicial[1]) : 1);
			for(j = Number(fInicial[2]); j <= Number(fFinal[2]); j++){
				var fin = j==Number(fFinal[2]) ? Number(fFinal[1]):12;
				for(i = inicio; i <= fin; i++){
					mesName = mi.obtenerMes(i,j);
					mesDisplayName = mi.obtenetMesNombre(i,j);
					
					mi.totales[mesName] = 0;
					mi.columnaNames.push(mesName);
					mi.columnas.push({ name: mesName, enableCellEdit: false, width: 100, displayName: mesDisplayName, type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>',
			        	categoryDisplayName: j
			        });
				}
				inicio = 1;
			}
			
			mi.columnas.push({ name: 'Total', enableCellEdit: false, width: 150, displayName: 'Total general', type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2',
	        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.totalP | number:2}}</div>'
	        });
			
			mi.columnas.push({ name: 'acumulacionCostos', width: 150, displayName: 'Acumulación de Costos', editType: 'dropdown', editableCellTemplate: 'ui-grid/dropdownEditor',
	        	editDropdownOptionsArray: mi.ddlOpciones, editDropdownValueLabel: 'value', cellFilter: 'mapAcumulacionCosto', editDropdownOptionsArray: mi.ddlOpciones
	        });
			
			mi.columnaNames.push("Total");
			mi.gridOptions.columnDefs = mi.columnas;
		}
		
		mi.getTotal = function(columna){
			return mi.totales[columna.col.name];
		}
		
		mi.generar = function(){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					mi.generarTabla();
					
					$http.post('/SInformePresupuesto',{
						accion: 'generarInforme',
						idPrestamo: mi.prestamo.value,
						tipoInforme: mi.informe.value,
						anio: moment(mi.fechaInicio).format('DD/MM/YYYY')
					}).success(function(response){
						if (response.success){
							mi.crearArbol(response.prestamo);

							for (var i = 0; i < mi.gridOptions.data.length; i++){
								var dato = mi.gridOptions.data[i];
						    	if (dato.objetoTipo == 5){
						    		var rowEntity = mi.obtenerEntidad(dato.idObjetoTipo, dato.objetoTipo);
						    		if (rowEntity.acumulacionCostos == 1){
						    			mi.setInicio(rowEntity);
						    		}else if (rowEntity.acumulacionCostos == 2){
						    			mi.setProrrateo(rowEntity);
						    		}else if (rowEntity.acumulacionCostos == 3){
						    			mi.setFin(rowEntity);
						    		}else if(rowEntity.acumulacionCostos == 0){
						    			mi.setFin(rowEntity);
						    		}
						    	}
						    }
							
							mi.calcularTotales();

							mi.totalPrestamo();
						}else{
							if (response.prestamo[0].tipoInforme == 2){
								$utilidades.mensaje('warning','No existe informe para modificar');
							}
						}
						mi.mostrarDescargar = true;
					})
				}else
					$utilidades.mensaje('warning','Debe seleccionar un año');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
		
		mi.descargar = function(){
			var viewData = { 
				    reporte : [] 
				}

			var obj = {};
			for(x in mi.gridOptions.data){
				for (y in mi.columnaNames){
					obj[mi.columnaNames[y]] = mi.gridOptions.data[x][mi.columnaNames[y]];
				}
				
				viewData.reporte.push(obj);
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
			
			viewData.reporte.push(obj);
			
			$http.post('/SInformePresupuesto',{
				accion: 'exportarExcel',
				data: JSON.stringify(viewData.reporte),
				idPrestamo: mi.prestamo.value,
				anio: moment(mi.fechaInicio).format('DD/MM/YYYY'),
				tipoInforme: mi.informe.value,
				columnas: mi.columnaNames.toString(),
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
				var data = datos;
				data = mi.gethijos(data);
				for(x in data){
					data[x].Total = 0;
					data[x].$$treeLevel = Number(data[x].posicionArbol) - 1;
					if(data[x].objetoTipo == 5 && data[x].acumulacionCostos == 0)
						data[x].acumulacionCostos = 3;
				}
				
				for(x in mi.gridOptions.columnDefs){
					if((x > 0) && (x < mi.gridOptions.columnDefs.length -2)){
						var colNombre = mi.gridOptions.columnDefs[x].name;
						for(x in data){
							data[x][colNombre] = 0;
							data[x]["total" + colNombre] = 0;
						}
					}
				}
				
				mi.gridOptions.data = data;
				
				$timeout(function(){
				     mi.gridApi.treeBase.expandAllRows();
				     mi.totalPrestamo();
			   })
			}
		}
		
		mi.ddlOpciones = [{ id: 1, value: 'Comienzo' },
            { id: 2, value: 'Prorrateo' },
            { id: 3, value: 'Fin' }];
		
		mi.rowTemplate = function(){
			return '<div style="background-color: blue">{{grid.scope.controller.gridOptions.data}}</div>';
		}

		mi.gridOptions = {
				showColumnFooter: true,
	            cellEditableCondition:function($scope){
	            	  if($scope.row.entity.objetoTipo == 5 && $scope.row.entity.hijo == null)
	            		  return true;
	            	  else
	            		  return false;
	            },
				rowEditWaitInterval: -1,
				expandAllRows : true,
				enableExpandableRowHeader: false,
				showTreeRowHeader: false,
				showTreeExpandNoChildren : false,
				enableColumnMenus: false,
				enableSorting: false,
				enableCellEdit: true,
				enableCellEditOnFocus: true,
			    columnDefs: mi.columnas,
			    onRegisterApi: function(gridApi ) {
			      mi.gridApi = gridApi;
			      
			      mi.gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
			    	  if(rowEntity.acumulacionCostos == 1){
			    		  mi.setInicio(rowEntity);
			    	  }else if (rowEntity.acumulacionCostos == 2){
			    		  mi.setProrrateo(rowEntity);
			    	  }else if(rowEntity.acumulacionCostos == 3){
			    		  mi.setFin(rowEntity);
			    	  }
			    	  mi.totalPrestamo();
			      });
			  }
		};
		
		mi.setInicio = function(rowEntity){
			var i = 0;
		  
			for(x in mi.gridOptions.columnDefs){
				if(x > 0 && x < mi.gridOptions.columnDefs.length -2){
					mi.setValor(rowEntity, mi.gridOptions.columnDefs[x].name, 0);
				}
			}
			
			var fInicial = rowEntity.fechaInicio.split('/');
			mi.setValor(rowEntity,mi.obtenerMes(Number(fInicial[1]),Number(fInicial[2])),rowEntity.Costo);
			mi.calcular(rowEntity);
		}
		
		mi.setProrrateo = function(rowEntity){
			var fInicial = rowEntity.fechaInicio.split('/');
			var fFinal = rowEntity.fechaFin.split('/');
			
			var i = 0;
			var j = 0;
			var contador = 0;
			  
			var inicio = (Number(fInicial[1]) <= 12 ? Number(fInicial[1]) : 1);
			for(j = Number(fInicial[2]); j <= Number(fFinal[2]); j++){
				var fin = j==Number(fFinal[2]) ? Number(fFinal[1]):12;
				for(i = inicio; i <= fin; i++){
					contador++;
				}
				inicio = 1;
			}
			  
			var costo = rowEntity.Costo / contador;
			  
			for(x in mi.gridOptions.columnDefs){
				if(x > 0 && x < mi.gridOptions.columnDefs.length -2){
					mi.setValor(rowEntity, mi.gridOptions.columnDefs[x].name, 0);
				}
			}
			
			var inicio = (Number(fInicial[1]) <= 12 ? Number(fInicial[1]) : 1);
			for(j = Number(fInicial[2]); j <= Number(fFinal[2]); j++){
				var fin = j==Number(fFinal[2]) ? Number(fFinal[1]):12;
				for(i = inicio; i <= fin; i++){
					mi.setValor(rowEntity, mi.obtenerMes(i,j), costo);
				}
				inicio = 1;
			}
			mi.calcular(rowEntity);
		}
		
		mi.setFin = function(rowEntity){
			var i = 0;
			
			for(x in mi.gridOptions.columnDefs){
				if(x > 0 && x < mi.gridOptions.columnDefs.length -2){
					mi.setValor(rowEntity, mi.gridOptions.columnDefs[x].name, 0);
				}
			}
			  
			var fFinal = rowEntity.fechaFin.split('/');
			mi.setValor(rowEntity,mi.obtenerMes(Number(fFinal[1]),Number(fFinal[2])),rowEntity.Costo);
			mi.calcular(rowEntity);
		}
		
		mi.saveRow = function(rowEntity){
			mi.guardarRowInforme(rowEntity);
            return true;
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
			mi.rowEntity = mi.gridOptions.data[0];
			mi.totalP = mi.rowEntity.Total;
			for(x in mi.gridOptions.columnDefs){
				if (x > 0 && x < mi.gridOptions.columnDefs.length - 2){
					var columna = mi.gridOptions.columnDefs[x].name;
					mi.totales[columna] = mi.obtenerValor(mi.rowEntity,columna);
				}
			}
		}
		
		mi.calcular = function(rowEntidad){
			mi.calcularPadre(rowEntidad);
		}
		
		mi.calcularTotales = function(){
			var monto = 0;
			for(x in mi.gridOptions.data){
				var rowEntity = mi.obtenerEntidad(mi.gridOptions.data[x].idObjetoTipo, mi.gridOptions.data[x].objetoTipo);
				for(y in mi.gridOptions.columnDefs){
					if(y > 0 && y < mi.gridOptions.columnDefs.length -2){
						monto += mi.obtenerValor(rowEntity, mi.gridOptions.columnDefs[y].name);
					}
				}
				
				rowEntity.Total = monto;
				monto = 0;
			}
		}
		
		mi.calcularPadre = function(rowEntidad){
			if (rowEntidad.idPredecesor != 0){
				var padre = mi.obtenerEntidad(rowEntidad.idPredecesor, rowEntidad.objetoTipoPredecesor);
				var valorFinal = 0;
				if (padre.hijo != null){
					for(y in mi.gridOptions.columnDefs){
						if (y > 0 && y < mi.gridOptions.columnDefs.length -2){
							for(x in padre.hijo){
								var hijo = padre.hijo[x].split(',');
								var valorColumna = mi.obtenerValor(mi.obtenerEntidad(hijo[0], hijo[1]), mi.gridOptions.columnDefs[y].name);
								valorFinal = valorFinal + valorColumna;
							}
							mi.setValor(padre,mi.gridOptions.columnDefs[y].name, valorFinal);
							valorFinal = 0;
							mi.calcularPadre(padre);
						}
					}
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
		
		mi.obtenerMesCabecera = function(mes){
			switch(mes){
			case 1:
				return "Mes1-";
			case 2:
				return "Mes2-";
			case 3:
				return "Mes3-";
			case 4:
				return "Mes4-";
			case 5:
				return "Mes5-";
			case 6:
				return "Mes6-";
			case 7:
				return "Mes7-";
			case 8:
				return "Mes8-";
			case 9:
				return "Mes9-";
			case 10:
				return "Mes10-";
			case 11:
				return "Mes11-";
			case 12:
				return "Mes12-";
			}
		}
		
		mi.obtenerMes = function(mes, anio){
			switch(mes){
			case 1:
				return "Mes1-" + anio;
			case 2:
				return "Mes2-" + anio;
			case 3:
				return "Mes3-" + anio;
			case 4:
				return "Mes4-" + anio;
			case 5:
				return "Mes5-" + anio;
			case 6:
				return "Mes6-" + anio;
			case 7:
				return "Mes7-" + anio;
			case 8:
				return "Mes8-" + anio;
			case 9:
				return "Mes9-" + anio;
			case 10:
				return "Mes10-" + anio;
			case 11:
				return "Mes11-" + anio;
			case 12:
				return "Mes12-" + anio;
			}
		}
		
		mi.obtenerValor = function(rowEntidad, columna){
			return rowEntidad[columna];
		}
		
		mi.setValor = function(rowEntidad, columna, valor){
			rowEntidad[columna] = valor;
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
	
.directive('categoryHeader', function() {
    function link(scope, element, attrs) {

      // create cols as soon as $gridscope is avavilable
      // grids in tabs with lazy loading come later, so we need to 
      // setup a watcher
      scope.$watch('categoryHeader.$gridScope', function(gridScope, oldVal) {
          if (!gridScope) {
              return;
          }
          // setup listener for scroll events to sync categories with table
          var viewPort = scope.categoryHeader.$gridScope.domAccessProvider.grid.$viewport[0];
          var headerContainer = scope.categoryHeader.$gridScope.domAccessProvider.grid.$headerContainer[0];
        
          // watch out, this line usually works, but not always, because under certains conditions
          // headerContainer.clientHeight is 0
          // unclear how to fix this. a workaround is to set a constant value that equals your row height 
          scope.headerRowHeight=  headerContainer.clientHeight;  
         
          angular.element(viewPort).bind("scroll", function() {
            // copy total width to compensate scrollbar width
            $(element).find(".categoryHeaderScroller")
              .width($(headerContainer).find(".ngHeaderScroller").width());
            $(element).find(".ngHeaderContainer")
              .scrollLeft($(this).scrollLeft());
        });
  
        // setup listener for table changes to update categories                
        scope.categoryHeader.$gridScope.$on('ngGridEventColumns', function(event, reorderedColumns) {
          createCategories(event, reorderedColumns);
        });
                });          
      var createCategories = function(event, cols) {
        scope.categories = [];
        var lastDisplayName = "";
        var totalWidth = 0;
        var left = 0;
        angular.forEach(cols, function(col, key) {
          if (!col.visible) {
            return;
          }
          totalWidth += col.width;
          var displayName = (typeof(col.colDef.categoryDisplayName) === "undefined") ?
            "\u00A0" : col.colDef.categoryDisplayName;
          if (displayName !== lastDisplayName) {
            scope.categories.push({
              displayName: lastDisplayName,
              width: totalWidth - col.width,
              left: left
            });
            left += (totalWidth - col.width);
            totalWidth = col.width;
            lastDisplayName = displayName;
          }
        });
        if (totalWidth > 0) {
          scope.categories.push({
            displayName: lastDisplayName,
            width: totalWidth,
            left: left
          });
        }
      };
    }
    return {
      scope: {
        categoryHeader: '='
      },
      restrict: 'EA',
      templateUrl: 'category_header.html',
      link: link
    };
  });