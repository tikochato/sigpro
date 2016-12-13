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

				return dataFactory;
			} ];
});
