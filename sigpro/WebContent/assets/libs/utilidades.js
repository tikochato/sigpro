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
				
				dataFactory.getCantidadCabecerasReporte = function(areaReporte, totalAnios, totalCabeceras, tamanioMinimoColumna){
					switch (totalCabeceras) {
					case 1: totalCabeceras = 12; break;
					case 2: totalCabeceras = 6; break;
					case 3: totalCabeceras = 4; break;
					case 4: totalCabeceras = 3; break;
					case 5: totalCabeceras = 2; break;
					case 6: totalCabeceras = 1; break;
					}
                    
					while (totalCabeceras>1){
                        var columnasAMostrar = (totalCabeceras * totalAnios) + totalAnios + 1;
                        var tamanioColumna = (areaReporte / columnasAMostrar);
                        if (tamanioColumna > tamanioMinimoColumna){
                            return totalCabeceras;
                        }else{
                            totalCabeceras--;
                        }
                    }
                    return totalCabeceras;
                }
               
                dataFactory.getTamanioColumnaReporte = function(areaReporte, totalAnios, cabecerasAMostrar){
                    var columnasAMostrar = (cabecerasAMostrar * totalAnios) + totalAnios + 1;
                    var tamanioPropuesto = (areaReporte / columnasAMostrar);
                    return Math.floor(tamanioPropuesto);
                }

				return dataFactory;
			} ];
});