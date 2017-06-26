var app = angular.module('avanceActividadesController',['ngAnimate', 'ngTouch']);

app.filter('calculatePercentage', function () {
	  return function (input, resultField, row) {
		  if(row.tipo == 2){
			  return Math.floor(input) + "%";
		  }else
			  return Math.floor(input);
	  };
	});

app.controller('avanceActividadesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants){
		var mi = this;
		mi.mostrarcargando = false;
		mi.totalActividades = 0;
		mi.totalProductos = 0;
		mi.totalHitos = 0;
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
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
		
		mi.getPrestamos();
		
		mi.gridOptions1 = {
			enableSorting: false,
			showColumnFooter: true,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Actividades', enableColumnMenu: false, 
					footerCellTemplate: '<div class="ui-grid-cell-contents">Total de Actividades: {{grid.appScope.controller.totalActividades}}</div>',
				},
				{ name: 'sinIniciar', width: 200, displayName: 'Sin Iniciar', enableColumnMenu: false,         
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre == "Porcentaje") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre == "Porcentaje"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre == "Porcentaje"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'proceso', width: 200, displayName: 'En proceso', enableColumnMenu: false, 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre == "Porcentaje") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre == "Porcentaje"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre == "Porcentaje"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'completadas', width: 200, displayName: 'Completadas', enableColumnMenu: false, 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre == "Porcentaje") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre == "Porcentaje"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre == "Porcentaje"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'retrasadas', width: 200, displayName: 'Retrasadas', enableColumnMenu: false, 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre == "Porcentaje") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre == "Porcentaje"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre == "Porcentaje"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi1 = gridApi;
			}
		}
		
		mi.gridOptions2 = {
			enableSorting: false,
			showColumnFooter: true,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Productos', enableColumnMenu: false, 
					footerCellTemplate: '<div class="ui-grid-cell-contents">Total de Hitos: {{grid.appScope.controller.totalHitos}}</div>',
				},
				{ name: 'sinIniciar', width: 200, displayName: 'Hitos sin Iniciar', enableColumnMenu: false, type: 'number',          
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre != "Cantidad") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre != "Cantidad"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre != "Cantidad"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'completadas', width: 200, displayName: 'Hitos completados', enableColumnMenu: false, type: 'number', 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre != "Cantidad") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre != "Cantidad"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre != "Cantidad"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'retrasadas', width: 200, displayName: 'Hitos retrasados', enableColumnMenu: false, type: 'number', 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40 && row.entity.nombre != "Cantidad") {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60 && row.entity.nombre != "Cantidad"){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100 && row.entity.nombre != "Cantidad"){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi2 = gridApi;
			}
		}
		
		mi.gridOptions3 = {
			enableSorting: false,
			showColumnFooter: true,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Productos', enableColumnMenu: false, 
					footerCellTemplate: '<div class="ui-grid-cell-contents">Total de Productos: {{grid.appScope.controller.totalProductos}}</div>',
				},
				{ name: 'sinIniciar', width: 200, displayName: 'Actividades sin Iniciar', enableColumnMenu: false, type: 'number',          
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40) {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'proceso', width: 200, displayName: 'Actividades en proceso', enableColumnMenu: false, type: 'number', 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40) {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'completadas', width: 200, displayName: 'Actividades completadas', enableColumnMenu: false, type: 'number', 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40) {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				},
				{ name: 'retrasadas', width: 200, displayName: 'Actividades retrasadas', enableColumnMenu: false, type: 'number', 
					cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
						if (Number(grid.getCellValue(row,col)) >= 0 && Number(grid.getCellValue(row,col)) <= 40) {
							return 'red';
			            } else if (Number(grid.getCellValue(row,col)) >= 41 && Number(grid.getCellValue(row,col)) <= 60){
			            	return  'yellow';
			            } else if(Number(grid.getCellValue(row,col)) >= 61 && Number(grid.getCellValue(row,col)) <= 100){
			            	return 'green';
			            }
			        },
			        cellFilter: 'calculatePercentage:"actualScore":row.entity'
				}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi3 = gridApi;
			}
		}
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
			}
		};
		
		mi.generar = function(){
			if(mi.prestamo.value != 0){
				if(mi.fechaCorte != null){
					mi.mostrarcargando = true;
					mi.gridOptions1.data = [];
					mi.gridOptions2.data = [];
					mi.gridOptions3.data = [];
					$http.post('/SAvanceActividades', {
						accion: 'getAvance',
						idPrestamo: mi.prestamo.value,
						fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY')
					}).success(function(response){
						if (response.success){
							mi.gridOptions1.data = response.actividades;
							mi.totalActividades = response.totalActividades;
							mi.gridOptions2.data = response.hitos;
							mi.totalHitos = response.totalHitos;
							mi.gridOptions3.data = response.productos;
							mi.totalProductos = response.totalProductos;
							mi.mostrarcargando = false;
						}else
							mi.mostrarcargando = false;
					});
				}else
					$utilidades.mensaje('warning','Debe seleccionar una fecha de corte');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
}]);