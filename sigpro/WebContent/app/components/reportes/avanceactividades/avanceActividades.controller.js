var app = angular.module('avanceActividadesController',['ngAnimate', 'ngTouch','smart-table']);

app.filter('calculatePercentage', function () {
	  return function (input, resultField, row) {
		  if(row.tipo == 2){
			  return Math.floor(input) + "%";
		  }else
			  return Math.floor(input);
	  };
	});

app.controller('avanceActividadesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$window',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$window){
		var mi = this;
		mi.mostrarCargando = false;
		mi.mostrardiv=false;
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
		
		mi.calcularTamanosPantalla = function(){
			mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
			mi.tamanoSemaforo = mi.tamanoPantalla * 0.05;
			mi.tamanoNombres = (mi.tamanoPantalla - mi.tamanoSemaforo) * 0.35;
			mi.tamanoColPorcentajes = (mi.tamanoPantalla - mi.tamanoNombres - mi.tamanoSemaforo) / 4;
		}
		
		mi.calcularTamanosPantalla();
		
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
					mi.mostrardiv = false;
					mi.rowCollectionActividades = [];
					mi.rowCollectionHitos = [];
					mi.rowProductos = [];
					mi.displayedCollectionActividades = [];
					mi.displayedCollectionHitos = [];
					mi.displayedProductos = [];
					
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
					
					mi.mostrarCargando = true;
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
								mi.rowProductos = response.productos;
								mi.displayedProductos = [].concat(mi.rowProductos);	
							}
							
							mi.totalProductos = response.totalProductos;
							
							mi.mostrarCargando = false;
							mi.mostrardiv=true;
						}else
							mi.mostrarCargando = false;
					});
				}
			}
		}
		
		mi.obtenerColor = function(row){
			var style={}
			if(row.completadas >= 0 && row.completadas <= 40){
				style.color='red'
			}else if(row.completadas > 40 && row.completadas <= 60){
				style.color='yellow'
			}else if(row.completadas > 60 && row.completadas <= 100){
				style.color='green'
			}
			return style;
		}
		
		angular.element($window).bind('resize', function(){ 
            mi.calcularTamanosPantalla();
            $scope.$digest();
        });
}]);