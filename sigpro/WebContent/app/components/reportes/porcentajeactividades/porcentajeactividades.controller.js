var app = angular.module('porcentajeactividadesController', ['DlhSoft.Kanban.Angular.Components']);

var GanttChartView = DlhSoft.Controls.GanttChartView;
var queryString = window.location.search;
var theme = queryString ? queryString.substr(1) : null;

app.controller('porcentajeactividadesController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q) {

		var mi=this;
		mi.proyectoid = "";
		mi.proyectoNombre = "";
		mi.objetoTipoNombre = "";
		mi.componentes = [];
		mi.productos = [];
		mi.mostrarcargando=false;
		
		var KanbanBoard = DlhSoft.Controls.KanbanBoard;
		
		mi.tObjetos = [
			{value: 1,text: "Todo"},
			{value: 2,text: 'Componente'},
			{value: 3,text: 'Producto'}
		];
		
		mi.productos = [
			{value: 0,text: "Seleccione una Opción"}
		];
		
		mi.tObjeto = mi.tObjetos[0];
		mi.producto = mi.productos[0];
		
		mi.displayObjeto = function(objetoSeleccionado){
			mi.mostrarcargando=false;
			if(objetoSeleccionado === 0){
				mi.componenteHide = false;
				mi.productoHide = false;
			}else if(objetoSeleccionado === 1){
				mi.componenteHide = false;
				mi.productoHide = false;
				mi.resetKanban();
			}else if(objetoSeleccionado === 2){
				mi.componenteHide = true;
				mi.productoHide = false;
				mi.resetKanban();
				mi.getComponentes();
			}else if (objetoSeleccionado === 3){
				mi.componenteHide = true;
				mi.productoHide = true;
				mi.resetKanban();
				mi.getComponentes();
			}
		}
		
		mi.getComponentes = function(){
			$http.post('/SComponente',{accion: 'getComponentesPaginaPorProyecto', proyectoid: $routeParams.proyectoId}).success(
					function(response) {
						mi.componentes = [];
						mi.componentes.push({'value' : 0, 'text' : 'Seleccione una opción'});
						if (response.success){
							for (var i = 0; i < response.componentes.length; i++){
								mi.componentes.push({'value': response.componentes[i].id, 'text': response.componentes[i].nombre});
							}
							
							mi.componente = mi.componentes[0];
						}
					});
		}
		
		mi.getProductos = function(componenteId){
			mi.productos = [];
			mi.productos.push({'value' : 0, 'text' : 'Seleccione una opción'});
			mi.producto = mi.productos[0];
			mi.resetKanban();
			
			if (mi.tObjeto.value == 3 && componenteId != 0){
				$http.post('/SProducto', {accion: 'listarProductos', componenteid:componenteId}).success(
						function(response){
							
							if (response.success){
								for (var i = 0; i < response.productos.length; i++){
									mi.productos.push({'value': response.productos[i].id, 'text': response.productos[i].nombre});
								}
								
								mi.producto = mi.productos[0];
							}
						});
			}
		}
		
		mi.refrescar = function(){
			mi.resetKanban();
			switch(mi.tObjeto.value){
			case 1:
				mi.getActividades();
				break;
			case 2:
				mi.getActividades();
				break;
			case 3:
				mi.getActividades();
				break;
			case 4:
				break;
			case 5:
				break;
			}
		};
		
		mi.resetKanban = function(){
			$scope.states = {};
			$scope.itemsKanban = null; 
			$scope.mostrarKanban = false;
		};
		
		$scope.states = {};
		$scope.itemsKanban = null; 
		$scope.mostrarKanban = false;
		
		mi.getActividades = function(){
			mi.mostrarcargando=true;
			var param_data = {};
			var ejecutar = false;
			if(mi.tObjeto.value == 1){
				param_data = {
					accion : "getKanban", 
					proyecto_id:$routeParams.proyectoId
				}
				ejecutar = true;
			}if(mi.tObjeto.value == 2 && mi.componente.value != 0){
				param_data = {
					accion : "getKanbanComponente", 
					componente_id : mi.componente.value
				}
				ejecutar = true;
			} else if(mi.tObjeto.value == 3 && mi.componente.value != 0 && mi.producto.value != 0){
				param_data = {
					accion : "getKanbanProducto", 
					producto_id : mi.producto.value
				}
				ejecutar = true;
			}
			
			if(ejecutar){
				$http.post('/SPorcentajeActividades', param_data).success(
						function(response) {
							var estadoNuevo = { name: 'Nuevo', areNewItemButtonsHidden: true }, 
							estadoEnProgreso = { name: 'En progreso', areNewItemButtonsHidden: true }, 
							estadoTerminado = { name: 'Terminado', isCollapsedByDefaultForGroups: true, areNewItemButtonsHidden: true };
							estadoRetrasado = { name: 'Retrasado', isCollapsedByDefaultForGroups: true, areNewItemButtonsHidden: true };
							
							var colores = ["#ffe033","#9fc6ee","#008000","#ff0000"];
							
							var estados  = [estadoNuevo, estadoEnProgreso, estadoTerminado,estadoRetrasado];
							var items  = response.items;
							
							for (item in items) {
							    items[item].state = estados[items[item].estadoId];
							    items[item].color = colores[items[item].estadoId];
							    items[item].isReadOnly = true;  
							}
							
							$scope.states  = estados;
							$scope.itemsKanban = items;
							$scope.mostrarKanban = true;
					});	
				mi.mostrarcargando=false;
			}
		}
	}
]);

app.directive('customOnChange', function() {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var onChangeHandler = scope.$eval(attrs.customOnChange);
      element.bind('change', onChangeHandler);
    }
  };
});


