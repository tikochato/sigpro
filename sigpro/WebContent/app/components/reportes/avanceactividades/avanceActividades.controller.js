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
		mi.totalActividadesCompletadas = 0;
		mi.totalActividadesSinIniciar = 0;
		mi.totalActividadesProceso = 0;
		mi.totalActividadesRetrasadas = 0;
		
		mi.totalHitos = 0;
		mi.totalHitosCompletados = 0;
		mi.totalHitosSinIniciar = 0;
		mi.totalHitosRetrasados = 0;
		
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
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
			}
		};
		
		mi.generar = function(){
			if(mi.prestamo.value != 0){
				if(mi.fechaCorte != null){
					mi.rowCollectionActividades = [];
					mi.rowCollectionHitos = [];
					mi.rowCollectionProductos = [];
					mi.displayedCollectionActividades = [];
					mi.displayedCollectionHitos = [];
					mi.displayedCollectionProductos = [];
					
					mi.totalActividades = 0;
					mi.totalActividadesCompletadas = 0;
					mi.totalActividadesSinIniciar = 0;
					mi.totalActividadesProceso = 0;
					mi.totalActividadesRetrasadas = 0;
					
					mi.totalHitos = 0;
					mi.totalHitosCompletados = 0;
					mi.totalHitosSinIniciar = 0;
					mi.totalHitosRetrasados = 0;
					
					mi.totalProductos = 0;
					
					mi.mostrarcargando = true;
					$http.post('/SAvanceActividades', {
						accion: 'getAvance',
						idPrestamo: mi.prestamo.value,
						fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY')
					}).success(function(response){
						if (response.success){
							if(response.actividades != undefined){
								mi.rowCollectionActividades = response.actividades;
								mi.displayedCollectionActividades = [].concat(mi.rowCollectionActividades);	
							}
							
							mi.totalActividades = response.totalActividades;
							if(response.cantidadesActividades != undefined){
								mi.totalActividadesCompletadas = response.cantidadesActividades[0].completadas;
								mi.totalActividadesSinIniciar = response.cantidadesActividades[0].sinIniciar;
								mi.totalActividadesProceso = response.cantidadesActividades[0].proceso;
								mi.totalActividadesRetrasadas = response.cantidadesActividades[0].retrasadas;
							}
							
							if(response.hitos != undefined){
								mi.rowCollectionHitos = response.hitos;
								mi.displayedCollectionHitos = [].concat(mi.rowCollectionHitos);
							}
							
							mi.totalHitos = response.totalHitos;
							
							if(response.cantidadHitos != undefined){
								mi.totalHitosCompletados = response.cantidadHitos[0].completadas;
								mi.totalHitosSinIniciar = response.cantidadHitos[0].sinIniciar;
								mi.totalHitosRetrasados = response.cantidadHitos[0].retrasadas
							}

							if(response.productos != undefined){
								mi.rowCollectionProductos = response.productos;
								mi.displayedCollectionProductos = [].concat(mi.rowCollectionProductos);	
							}
							
							mi.totalProductos = response.totalProductos;
							
							mi.mostrarcargando = false;
						}else
							mi.mostrarcargando = false;
					});
				}
			}
		}
}]);