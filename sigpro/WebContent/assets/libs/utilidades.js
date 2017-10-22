'use strict';

var app = angular.module('ngUtilidades', [ 'ngFlash' ]);

app.provider('Utilidades', function() {

	this.$get = [
			'$rootScope',
			'Flash',
			function($rootScope, $alertas) {
				var dataFactory = {};

				dataFactory.elementosPorPagina = 20;
				dataFactory.numeroMaximoPaginas = 5;
				dataFactory.sistema_nombre = "SIPRO";
				
				dataFactory.mensaje = function(tipo, texto) {
					return $alertas.create(tipo, texto, 5000, {
						container : 'alertas'
					});
				};

				dataFactory.esNumero = function(valor) {
					return !isNaN(parseFloat(valor)) && isFinite(valor);
				}

				dataFactory.esCadenaVacia = function(cadena) {
					return cadena == null || cadena == "";
				}

				dataFactory.esCorreoValido = function(correo) {
					return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/
							.test(correo);
				}

				dataFactory.getCantidadCabecerasReporte = function(areaReporte, totalAnios, totalCabeceras, tamanioMinimoColumna, columnasTotal){
					while (totalCabeceras>0){
						var columnasAMostrar = (totalCabeceras * totalAnios) + totalAnios + columnasTotal;
						var tamanioColumna = (areaReporte / columnasAMostrar);
						if (tamanioColumna > tamanioMinimoColumna){
							return totalCabeceras;
						}else{
							totalCabeceras--;
						}
					}
					return totalCabeceras;
				}
				
				dataFactory.getTamanioColumnaReporte = function(areaReporte, totalAnios, cabecerasAMostrar, columnasTotal){
					var columnasAMostrar = (cabecerasAMostrar * totalAnios) + totalAnios + columnasTotal;
					var tamanioPropuesto = (areaReporte / columnasAMostrar);
                    return Math.floor(tamanioPropuesto);
				}
				
				dataFactory.setFocus = function(elemento){
					if(elemento !== undefined && elemento !== null){
						setTimeout(function(){elemento.focus();}, 100);
					}
				}

				return dataFactory;
			} ];
})

app.filter('formatoMillones', function() {
    return function(numero, millones) {
    	if(numero != null){
	        if(millones){
	        	var res = ((numero/1000000).toFixed(2));
	        	return ('Q '+res.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
	        }
	        return ('Q '+ (Number(numero).toFixed(2)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    	}
    };
});

app.filter('formatoMillonesDolares', function() {
    return function(numero, millones) {
    	if(numero != null){
	        if(millones){
	        	var res = ((numero/1000000).toFixed(2));
	        	return ('$ '+res.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
	        }
	        return ('$ '+ (Number(numero).toFixed(2)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    	}
    };
})

app.filter('formatoMillonesSinTipo', function() {
    return function(numero, millones) {
    	if(numero != null){
	        if(millones){
	        	var res = ((numero/1000000).toFixed(2));
	        	return (res.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
	        }
	        return ((Number(numero).toFixed(2)).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","));
    	}
    };
})
