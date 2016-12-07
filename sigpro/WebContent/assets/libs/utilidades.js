/**
 * 
 */

'use strict';

var app = angular.module('ngUtilidades', ['ngFlash']);

app.provider('Utilidades', function () {
	
	var elementosPorPagina=20;
	var numeroMaximoPaginas=5;
    
	this.$get = ['$rootScope', 'Flash', function ($rootScope, $alertas) {
        var dataFactory = {};
        
        dataFactory.mensaje = function (tipo,texto){
        	return $alertas.create(tipo,texto,5000, {container: 'alertas'});
        };
        

        return dataFactory;
    }];
});
