var app = angular.module('informacionPresupuestariaController',['ngAnimate', 'ngTouch', 'ui.grid.edit', 'ui.grid.rowEdit']);

app.controller('adquisicionesController', ['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion){
		var mi = this;
		mi.gridOptions = {};
		mi.mostrarcargando=false;
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
		mi.informeCompleto = false;
		mi.categorias = [];
		mi.menuItems = [];
		mi.columnasDinamicas =[];
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	    
	    mi.reiniciarVista=function(){
			if($location.path()=='/informacionPresupuestaria/rv')
				$route.reload();
			else
				$location.path('/informacionPresupuestaria/rv');
		}
	    
		mi.formatofecha = 'dd/MM/yyyy';
		
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
		
		mi.obtenerMesNombre = function(mes){
			switch(mes){
			case 1:
				return "Enero";
			case 2:
				return "Febrero";
			case 3:
				return "Marzo";
			case 4:
				return "Abril";
			case 5:
				return "Mayo";
			case 6:
				return "Junio";
			case 7:
				return "Julio";
			case 8:
				return "Agosto";
			case 9:
				return "Septiembre";
			case 10:
				return "Octubre";
			case 11:
				return "Noviembre";
			case 12:
				return "Diciembre";
			}
		}
		
		mi.generarTabla = function(){
			mi.columnas = [];
			mi.columnaNames = [];
			mi.cabeceras = [];
			mi.categorias = [];
			
			var fecha = new Date();
			var m = fecha.getMonth() +1;
			var y = fecha.getFullYear();
			
			var i = 0;
			var j = 0;
			var mesName = "";
			mi.columnas.push({ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: '', enableColumnMenu: false,
	        	cellTemplate: "<div class=\"ui-grid-cell-contents\" ng-class=\"{'ui-grid-tree-padre': grid.appScope.controller.objetoTipo < 5}\"><div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><div style=\"float:left;\" class=\"ui-grid-tree-base-row-header-buttons\" ng-class=\"{'ui-grid-tree-base-header': row.treeLevel > -1 }\" ng-click=\"grid.appScope.controller.toggleRow(row,evt)\"><i ng-class=\"{'ui-grid-icon-down-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'ui-grid-icon-right-dir': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed', 'ui-grid-icon-blank': ( ( !grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) && !( row.treeNode.children && row.treeNode.children.length > 0 ) )}\" ng-style=\"{'padding-left': grid.options.treeIndent * row.treeLevel + 'px'}\"></i> &nbsp;</div>{{COL_FIELD CUSTOM_FILTERS}}</div>",
	        	footerCellTemplate: '<div class="ui-grid-cell-contents">Total</div>',
	        });
			
			mi.columnaNames.push("nombre");
			mi.cabeceras.push("nombre");
			
			var fInicial = moment(mi.fechaInicio).format('DD/MM/YYYY').split('/');
			var fFinal = moment(mi.fechaFin).format('DD/MM/YYYY').split('/');
			var inicio = (Number(fInicial[1]) <= 12 ? Number(fInicial[1]) : 1);
			for(j = Number(fInicial[2]); j <= Number(fFinal[2]); j++){
				mi.categorias.push({name: j.toString(), visible: true, showCatName: true});
				var fin = j==Number(fFinal[2]) ? Number(fFinal[1]):12;
				for(i = inicio; i <= fin; i++){
					mesName = mi.obtenerMes(i,j);
					
					mesDisplayName = mi.obtenerMesNombre(i);
					
					mi.totales[mesName] = 0;
					mi.columnaNames.push(mesName+"-P");
					mi.columnasDinamicas.push(mesName+"-P");
					mi.cabeceras.push(mesDisplayName + "(P)");
					mi.columnas.push({ name: mesName+"-P", enableCellEdit: false, width: 120, displayName: mesDisplayName + "(P)", type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
			        	menuItems: [
			        		{
			        			title: 'Mostrar/Ocultar',
			        			action: mi.mostrarOcultarColumna
			        		}
			        	],
			        	category: j.toString()
			        });
					
					mi.columnaNames.push(mesName+"-R");
					mi.columnasDinamicas.push(mesName+"-R");
					mi.cabeceras.push(mesDisplayName + "(R)");
					mi.columnas.push({ name: mesName+"-R", enableCellEdit: false, width: 120, displayName: mesDisplayName + "(R)", type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2', 
			        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.getTotal(this) | number:2}}</div>', enableHiding: false, enableGrouping: false, enablePinning: false,
			        	menuItems: [
			        		{
			        			title: 'Mostrar/Ocultar',
			        			action: mi.mostrarOcultarColumna
			        		}
			        	],
			        	category: j.toString()
			        });
				}
				inicio = 1;
			}
			
			mi.columnas.push({ name: 'Total', enableCellEdit: false, width: 150, displayName: 'Total general', type: 'number', cellFilter:'number:2', footerCellFilter : 'number : 2',
	        	footerCellTemplate: '<div class="ui-grid-cell-contents"> {{grid.appScope.controller.totalP | number:2}}</div>', enableColumnMenu: false, pinnedRight:true
	        });
			
			mi.columnas.push({ name: 'acumulacionCostos', enableCellEdit: true, enableCellEditOnFocus: true, width: 150, displayName: 'Acumulación de Costos', editType: 'dropdown', editableCellTemplate: 'ui-grid/dropdownEditor',
	        	editDropdownOptionsArray: mi.ddlOpciones, editDropdownValueLabel: 'value', cellFilter: 'mapAcumulacionCosto', editDropdownOptionsArray: mi.ddlOpciones, enableColumnMenu: false, pinnedRight:true
	        });
			
			mi.columnaNames.push("Total");
			mi.cabeceras.push("Total");
			mi.gridOptions.columnDefs = mi.columnas;
			
			mi.ocultarColumnas();
		}
		
		mi.ocultarColumnas = function(){
			var currentTime = new Date();
			var month = currentTime.getMonth() + 1;
			var year = currentTime.getFullYear();
			
			for(x in mi.gridOptions.columnDefs){
				if (x > 0 && x < mi.gridOptions.columnDefs.length -2){
					if(Number(mi.gridOptions.columnDefs[x].category) < year){
						if(mi.gridOptions.columnDefs[x].name.includes("-P")){
							mi.gridOptions.columnDefs[x].visible = false;
							mi.gridOptions.columnDefs[x].ocultar = true;
						}else{
							mi.gridOptions.columnDefs[x].visible = true;
							mi.gridOptions.columnDefs[x].ocultar = false;
						}
					}else{
						var numMesColumna = mi.gridOptions.columnDefs[x].name.split('-')[0].replace('Mes','');
						if(numMesColumna < month){
							if(mi.gridOptions.columnDefs[x].name.includes("-P")){
								mi.gridOptions.columnDefs[x].visible = false;
								mi.gridOptions.columnDefs[x].ocultar = true;
							}else{
								mi.gridOptions.columnDefs[x].visible = true;
								mi.gridOptions.columnDefs[x].ocultar = false;
							}
						}else{
							if(mi.gridOptions.columnDefs[x].name.includes("-R")){
								mi.gridOptions.columnDefs[x].visible = false;
								mi.gridOptions.columnDefs[x].ocultar = true;
							}else{
								mi.gridOptions.columnDefs[x].visible = true;
								mi.gridOptions.columnDefs[x].ocultar = false;
							}
						}
					}
				}
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
					mi.mostrarcargando=true;
					mi.generarTabla();
					
					$http.post('/SInformePresupuesto',{
						accion: 'generarInforme',
						idPrestamo: mi.prestamo.value,
						informeCompleto: mi.informeCompleto,
						columnaNames: mi.columnasDinamicas.toString(),
						anio: moment(mi.fechaInicio).format('DD/MM/YYYY')
					}).success(function(response){
						if (response.success){
							mi.crearArbol(response.prestamo);
							mi.data = response.prestamo;
							//for (var i = 0; i < mi.gridOptions.data.length; i++){
							for (var i = 0; i < mi.data.length; i++){
								var dato = mi.data[i];
						    	if (dato.objetoTipo == 5 && dato.hijo.length == 0){
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
							
							mi.mostrarcargando=false;
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
				var data = datos;
				//data = mi.gethijos(data);
				
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
			var mesP = mi.obtenerMes(Number(fInicial[1]),Number(fInicial[2]))+"-P";
			var mesR = mi.obtenerMes(Number(fInicial[1]),Number(fInicial[2]))+"-R";
			mi.setValor(rowEntity,mesP,rowEntity.Costo);
			mi.setValor(rowEntity,mesR,rowEntity.CostoReal);
			mi.calcular2(rowEntity,mesP,mesR);
			
			mi.calcularTotales();
			mi.totalPrestamo();
		}
		
		mi.setProrrateo = function(rowEntity){			
			var FI = rowEntity.fechaInicio.split('/');
			var FF = rowEntity.fechaFin.split('/');
			var diasPorcentual = [];
			var contador = 0;
			if(FI[1] != FF[1]){
				var fInicial = moment(rowEntity.fechaInicio,'DD/MM/YYYY');
				var fFinal = moment(rowEntity.fechaFin,'DD/MM/YYYY');
				var d = 0;
				var diasTotales = fFinal.diff(fInicial,'d');

				var inicio = (Number(FI[1]) <= 12 ? Number(FI[1]) : 1);
				for(j = Number(FF[2]); j <= Number(FF[2]); j++){
					var fin = j==Number(FF[2]) ? Number(FF[1]):12;
					for(i = inicio; i <= fin; i++){
						if (diasPorcentual.length == 0){
							diasPorcentual.push(mi.getDiasDelMes(FI[1],FI[2]) - FI[0]);
						}else{
							d = mi.getDiasDelMes(i,j);
							diasPorcentual.push(d);
						}
					}
					inicio = 1;
				}
				
				diasPorcentual[diasPorcentual.length-1] = FF[0];
				
				for(i = 0; i<diasPorcentual.length;i++){
					diasPorcentual[i] = diasPorcentual[i]/diasTotales;
				}
				
				for(x in mi.gridOptions.columnDefs){
					if(x > 0 && x < mi.gridOptions.columnDefs.length -2){
						mi.setValor(rowEntity, mi.gridOptions.columnDefs[x].name+"-P", 0);
						mi.setValor(rowEntity, mi.gridOptions.columnDefs[x].name+"-R", 0);
					}
				}
				
				var mesP = "";
				var mesR = "";
				
				var inicio = (Number(FI[1]) <= 12 ? Number(FI[1]) : 1);
				for(j = Number(FF[2]); j <= Number(FF[2]); j++){
					var fin = j==Number(FF[2]) ? Number(FF[1]):12;
					for(i = inicio; i <= fin; i++){
						mesP = mi.obtenerMes(i,j)+"-P";
						mesR = mi.obtenerMes(i,j)+"-R";
						
						mi.setValor(rowEntity, mesP, rowEntity.Costo * diasPorcentual[contador]);
						mi.setValor(rowEntity, mesR, rowEntity.CostoReal * diasPorcentual[contador]);
						mi.calcular2(rowEntity, mesP, mesR);
						contador++;
					}
					inicio = 1;
				}
				
				//mi.calcular(rowEntity);
				
				mi.calcularTotales();
				mi.totalPrestamo();
			}			
		}
		
		mi.setFin = function(rowEntity){
			var i = 0;
			
			for(x in mi.gridOptions.columnDefs){
				if(x > 0 && x < mi.gridOptions.columnDefs.length -2){
					mi.setValor(rowEntity, mi.gridOptions.columnDefs[x].name, 0);
				}
			}
			  
			var fFinal = rowEntity.fechaFin.split('/');
			var mesP = mi.obtenerMes(Number(fFinal[1]),Number(fFinal[2]))+"-P";
			var mesR = mi.obtenerMes(Number(fFinal[1]),Number(fFinal[2]))+"-R";
			mi.setValor(rowEntity,mesP,rowEntity.Costo);
			mi.setValor(rowEntity,mesR,rowEntity.CostoReal);
			//mi.calcular(rowEntity);
			mi.calcular2(rowEntity,mesP,mesR);
			
			mi.calcularTotales();
			mi.totalPrestamo();
		}
		
		mi.getDiasDelMes = function(mes,ano)
		{
			if( (mes == 1) || (mes == 3) || (mes == 5) || (mes == 7) || (mes == 8) || (mes == 10) || (mes == 12) ) 
		        return 31;
		    else if( (mes == 4) || (mes == 6) || (mes == 9) || (mes == 11) ) 
		        return 30;
		    else if( mes == 2 )
		    {
		        if( (ano % 4 == 0) && (ano % 100 != 0) || (ano % 400 == 0) )
		            return 29;
		        else
		            return 28;
		    }   
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
		
		mi.calcularTotales = function(){
			var monto = 0;
			for(x in mi.gridOptions.data){
				var rowEntity = mi.obtenerEntidad(mi.gridOptions.data[x].idObjetoTipo, mi.gridOptions.data[x].objetoTipo);
				for(y in mi.gridOptions.columnDefs){
					if(y > 0 && y < mi.gridOptions.columnDefs.length -2){
						if(mi.gridOptions.columnDefs[y].visible==true){
							monto += mi.obtenerValor(rowEntity, mi.gridOptions.columnDefs[y].name);
						}
					}
				}
				
				rowEntity.Total = monto;
				monto = 0;
			}
		}
		
		mi.calcular2 = function(rowEntidad,mesP,mesR){
			var padre = mi.obtenerEntidad(rowEntidad.idPredecesor, rowEntidad.objetoTipoPredecesor);
			var valorActualPadreMesP = mi.obtenerValor(padre, mesP);
			var valorActualActividadP = mi.obtenerValor(rowEntidad, mesP);
			var valorActualPadreMesR = mi.obtenerValor(padre, mesR);
			var valorActualActividadR = mi.obtenerValor(rowEntidad, mesR);
			
			mi.setValor(padre,mesP, valorActualPadreMesP + valorActualActividadP);
			mi.setValor(padre,mesR, valorActualPadreMesR + valorActualActividadR);
			if (padre.idPredecesor > 0)
				mi.calcular2(padre,mesP,mesR);
			
			/*for(y in mi.gridOptions.columnDefs){
				if (y > 0 && y < mi.gridOptions.columnDefs.length -2){
					var valorActualPadre = mi.obtenerValor(padre, mi.gridOptions.columnDefs[y].name);
					var valorActualActividad = mi.obtenerValor(rowEntidad, mi.gridOptions.columnDefs[y].name);
					mi.setValor(padre,mi.gridOptions.columnDefs[y].name, valorActualPadre + valorActualActividad);
					if (padre.idPredecesor > 0)
						mi.calcular2(padre);
				}
			}*/
		}
		
		
		
		
		mi.calcular = function(rowEntidad){
			mi.calcularPadre(rowEntidad);
		}
		
		mi.calcularPadre = function(rowEntidad){
			if (rowEntidad.idPredecesor != 0){
				var padre = mi.obtenerEntidad(rowEntidad.idPredecesor, rowEntidad.objetoTipoPredecesor);
				var valorFinal = 0;
				if (padre.hijo != null){
					for(y in mi.gridOptions.columnDefs){
						if (y > 0 && y < mi.gridOptions.columnDefs.length -2){
							for(x in padre.hijo){
								if(padre.hijo[x] != null){
									var hijo = padre.hijo[x].split(',');
									var entity = mi.obtenerEntidad(hijo[0], hijo[1]);
									var valorColumna = mi.obtenerValor(entity, mi.gridOptions.columnDefs[y].name);
									valorFinal = valorFinal + valorColumna;
								}
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
			for (x in mi.data){
				if (id == mi.data[x].idObjetoTipo && objetoTipo == mi.data[x].objetoTipo){
					return mi.data[x];
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