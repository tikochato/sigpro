var app = angular.module('planejecucionController', []);


app.controller('planejecucionController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	mi.proyectoid = "";
	mi.proyectoNombre = "";
	mi.objetoTipoNombre = "";
	mi.formatofecha = 'yyyy';
	
	mi.fechaOptions = {
			datepickerMode:"year",
			  minMode:"year",
	};
	
	$window.document.title = $utilidades.sistema_nombre+' - Plan de Ejecución';
	i18nService.setCurrentLang('es');
	 
	$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyectoId }).success(
			function(response) {
				mi.proyectoid = response.id;
				mi.proyectoNombre = response.nombre;
				mi.objetoTipoNombre = "Proyecto";
	});
	 
	$http.post('/SPrestamo', { accion: 'getPrestamo', objetoId:$routeParams.proyectoId,
		objetoTipo:1,
		t: (new Date()).getTime()})
	
	 .then(function(response){
		 mi.prestamo = response.data.prestamo;
		 mi.setPorcentaje(1);
		 mi.setPorcentaje(2);
		 mi.setPorcentaje(3);
		 mi.setPorcentaje(4);
		 mi.setPorcentaje(5);
	});	
	 
	 mi.exportarExcel = function(){
			$http.post('/SAgenda', { accion: 'exportarExcel', proyectoid:$routeParams.proyectoId,t:moment().unix()
				  } ).then(
						  function successCallback(response) {
								var anchor = angular.element('<a/>');
							    anchor.attr({
							         href: 'data:application/ms-excel;base64,' + response.data,
							         target: '_blank',
							         download: 'Agenda.xls'
							     })[0].click();
							  }.bind(this), function errorCallback(response){
							 		
							 	}
							 );
		};
		
		
	
	mi.setPorcentaje = function(tipo){
			var n = 0;
			if (tipo==1)
			{
				if(mi.prestamo.desembolsoAFechaUsd != undefined && mi.prestamo.montoContratado != undefined){
					n = (mi.prestamo.desembolsoAFechaUsd / mi.prestamo.montoContratado) * 100;
					mi.prestamo.desembolsoAFechaUsdP = Number(n.toFixed(2));
				}
			}else if (tipo==2){
				if(mi.prestamo.montoContratadoUsd != undefined && mi.prestamo.montoPorDesembolsarUsd != undefined){
					n = (mi.prestamo.montoPorDesembolsarUsd / mi.prestamo.montoContratadoUsd) * 100;
					mi.prestamo.montoPorDesembolsarUsdP = Number(n.toFixed(2));
				}
			}else if (tipo==3){
				if(mi.prestamo.desembolsoAFechaUeUsd != undefined && mi.prestamo.montoAsignadoUe != undefined){
					n = (mi.prestamo.desembolsoAFechaUeUsd / mi.prestamo.montoAsignadoUe) * 100;
					mi.prestamo.desembolsoAFechaUeUsdP = Number(n.toFixed(2));
				}
			}else if(tipo==4){
				if(mi.prestamo.montoAsignadoUeUsd != undefined && mi.prestamo.montoPorDesembolsarUeUsd != undefined){
					n = (mi.prestamo.montoPorDesembolsarUeUsd / mi.prestamo.montoAsignadoUeUsd) * 100;
					mi.prestamo.montoPorDesembolsarUeUsdP = Number(n.toFixed(2));
				}
			}else if(tipo==5){
				if(mi.prestamo.fechaCierreActualUe != undefined && mi.prestamo.fechaElegibilidadUe != undefined){
					
					var fechaCierreTemp = moment(mi.prestamo.fechaCierreActualUe,'DD/MM/YYYY').toDate().getTime();
					var fechaElegibilidadTemp = moment(mi.prestamo.fechaElegibilidadUe,'DD/MM/YYYY').toDate().getTime();
					var hoy = new moment(moment(new Date()).format('DD/MM/YYYY'),'DD/MM/YYYY').toDate().getTime();
					
					var dif1 = fechaCierreTemp - fechaElegibilidadTemp;
					var dif2 = hoy - fechaElegibilidadTemp;
					n = (dif2 / dif1) * 100;
					if (isNaN(n))
						n = 0.00;
					mi.prestamo.plazoEjecucionUe = Number(n.toFixed(2));	
				}	
			}
	};
	
	mi.abrirPopupFecha = function(index) {
		switch(index){
			case 1: mi.ef_abierto = true; break;
		}
	

	};
	
	
}]);



		