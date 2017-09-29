var app = angular.module('planAdquisicionesController',['ngTouch','ngAnimate']);
app.controller('planAdquisicionesController', [ '$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q){
		var mi = this;
		mi.tooltipObjetoTipo = ["Prestamo","Componente","Producto","Sub Producto","Actividad"];
		
		mi.calcularTamanosPantalla = function(){
			mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
			mi.tamanoTotal = mi.tamanoPantalla - 300; 
			mi.estiloCelda = "width:80px;min-width:80px; max-width:80px;text-align: center";
		}
		
		$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				mi.prestamos.push({'value' : 0, 'text' : 'Seleccione un préstamo'});
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
					
					mi.prestamo = mi.prestamos[0];
				}
			});
		
		mi.generar = function(){
			if(mi.prestamo.value > 0){
				mi.mostrarCargando = true;
				mi.mostrarTablas = false;
				mi.idPrestamo = mi.prestamo.value;
				$http.post('/SPlanAdquisiciones',{
					accion: 'generarPlan',
					idPrestamo: mi.idPrestamo
				}).success(function(response){
					if(response.success){
						mi.crearArbol(response.proyecto);
						mi.calcularTamanosPantalla();
					}
				});
			}
		}
		
		mi.crearArbol = function(datos){
			mi.data = datos;
			
			mi.rowCollectionPrestamo = [];
			mi.rowCollectionPrestamo = mi.data;
			mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
			mi.mostrarCargando = false;
			mi.mostrarTablas = true;
		}
		
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
}]);