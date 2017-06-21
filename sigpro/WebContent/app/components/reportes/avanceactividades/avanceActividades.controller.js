var app = angular.module('avanceActividadesController',['ngAnimate', 'ngTouch']);

app.controller('avanceActividadesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants){
		var mi = this;
		
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
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Actividades', enableColumnMenu: false },
				{ name: 'sinIniciar', width: 200, displayName: 'Sin Iniciar', enableColumnMenu: false},
				{ name: 'proceso', width: 200, displayName: 'En proceso', enableColumnMenu: false},
				{ name: 'completadas', width: 200, displayName: 'Completadas', enableColumnMenu: false},
				{ name: 'retrasadas', width: 200, displayName: 'Retrasadas', enableColumnMenu: false}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi1 = gridApi;
			}
		}
		
		mi.gridOptions2 = {
			enableSorting: false,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Hitos', enableColumnMenu: false },
				{ name: 'sinIniciar', width: 200, displayName: 'Sin Iniciar', enableColumnMenu: false},
				{ name: 'proceso', width: 200, displayName: 'En proceso', enableColumnMenu: false},
				{ name: 'completadas', width: 200, displayName: 'Completadas', enableColumnMenu: false},
				{ name: 'retrasadas', width: 200, displayName: 'Retrasadas', enableColumnMenu: false}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi2 = gridApi;
			}
		}
		
		mi.gridOptions3 = {
			enableSorting: false,
			columnDefs: [
				{ name: 'nombre', pinnedLeft:true, enableCellEdit: false, width: 300, displayName: 'Productos', enableColumnMenu: false },
				{ name: 'sinIniciar', width: 200, displayName: 'Sin Iniciar', enableColumnMenu: false},
				{ name: 'proceso', width: 200, displayName: 'En proceso', enableColumnMenu: false},
				{ name: 'completadas', width: 200, displayName: 'Completadas', enableColumnMenu: false},
				{ name: 'retrasadas', width: 200, displayName: 'Retrasadas', enableColumnMenu: false}
			],
			onRegisterApi: function(gridApi) {
				mi.gridApi3 = gridApi;
			}
		}
		
		mi.generar = function(){
			$http.post('/SAvanceActividades', {
				accion: 'getAvance',
				idPrestamo: mi.prestamo.value
			});
		}
}]);