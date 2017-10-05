var app = angular.module('planejecucionController', []);


app.controller('planejecucionController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	mi.proyectoid = "";
	mi.proyectoNombre = "";
	mi.objetoTipoNombre = "";
	mi.formatofecha = 'yyyy';
	mi.mostrar = false;
	mi.tabla = {};
	mi.anioFiscal = "";
	mi.mesReportado = "";
	mi.prestamo = {};
	mi.mostrarCargando = false;
	mi.mostrarExport = false;
	
	mi.fechaOptions = {
			datepickerMode:"year",
			  minMode:"year",
	};
	
	mi.radarColors = ['#88b4df','#8ecf4c'];
	 
	$window.document.title = $utilidades.sistema_nombre+' - Plan de Ejecución';
	i18nService.setCurrentLang('es');
	
	mi.radarOptions = {
			legend: {
				display: true,
				position: 'bottom'
			}
	};
	
	
	 mi.etiquetas =["Ejecución Física", "Plazo de Ejecución", "Ejecución Financiera"];
	 mi.series = ["Planificado", "Real"];

	  mi.dataRadar = [
		    [0, 0, 0],  //planificado
		    [0, 0, 0]  //real
		  ];
	  
	
	 
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
				}
	});
	
	mi.inicializarDatos = function (){
		mi.proyectoid = "";
		mi.proyectoNombre = "";
		mi.objetoTipoNombre = "";
		mi.mostrar = false;
		mi.tabla = {};
		mi.anioFiscal = "";
		mi.mesReportado = "";
		mi.prestamo = {};
	}
	
	mi.generarReporte = function (){
		mi.mostrarExport = false;
		mi.mostrar=false;
		if (mi.prestamoSeleccionado !=null && mi.prestamoSeleccionado != undefined){
			mi.inicializarDatos();
			$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: mi.prestamoSeleccionado.value,t:moment().unix() }).success(
				function(response) {
					mi.proyectoid = response.id;
					mi.proyectoNombre = response.nombre;
					mi.objetoTipoNombre = "Proyecto";
					
					$http.post('/SPrestamo', { accion: 'getPrestamo', objetoId:mi.prestamoSeleccionado.value,
						objetoTipo:1,
						t: (new Date()).getTime()})
					 .then(function(response){
						 if (response.data.success && response.data.prestamo != null 
								 && response.data.prestamo != undefined){
						 mi.prestamo = response.data.prestamo;
						 //mi.tabla.push({'etiqueta1',})
						 mi.setPorcentaje(1);
						 mi.setPorcentaje(2);
						 mi.setPorcentaje(3);
						 mi.setPorcentaje(4);
						 mi.setPorcentaje(5);
						 mi.mostrarCargando = true;
						 $http.post('/SPlanEjecucion', { accion: 'getDatosPlan', proyectoId:mi.proyectoid,
								t: (new Date()).getTime()})
							 .then(function(response){
								 var fecha_actual = moment(response.data.fecha,'DD/MM/YYYY').toDate();
								 mi.dataRadar[1][0] = Number(response.data.ejecucionFisicaR);
								 mi.dataRadar[0][0] = Number(response.data.ejecucionFisicaP);
								 mi.dataRadar[0][2] = Number(response.data.ejecucionFinancieraP);
								 mi.dataRadar[0][1] = Number(response.data.plazoEjecucionP);
								 mi.mesReportado = mi.obtenerMes(Number(moment(fecha_actual).format('MM')));
								 mi.anioFiscal = Number(moment(fecha_actual).format('YYYY'));
								 mi.mostrar = true; 
								 mi.mostrarCargando = false;
								 mi.mostrarExport = true;
							});
						 
						 }else{
							 $utilidades.mensaje('warning','No se encontro datos del prestamo');
						 }
					});		
			});	
		}
	}
	

	mi.exportarExcel = function(){

		
		
		$http.post('/SPlanEjecucion', { 
			accion: 'exportarExcel', 
			id: mi.prestamoSeleccionado.value,
			ejecucionFisicaPlan : mi.dataRadar[0][0],
			plazoEjecucionPlan: mi.dataRadar[0][1],
			ejecucionFinancieraPlan : mi.dataRadar[0][2],
			ejecucionFisicaReal : mi.dataRadar[1][0],
			plazoEjecucionReal: mi.dataRadar[1][1],
			ejecucionFinancieraReal : mi.dataRadar[1][2],
			
			t:moment().unix()
		  } ).then(
				  function successCallback(response) {
					  var anchor = angular.element('<a/>');
					  anchor.attr({
				         href: 'data:application/ms-excel;base64,' + response.data,
				         target: '_blank',
				         download: 'Plan_De_Ejecucion.xls'
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
					mi.dataRadar[1][2] = mi.prestamo.desembolsoAFechaUsdP;
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
					if (isNaN(n) || dif1 == 0)
						n = 0.00;
					mi.prestamo.plazoEjecucionUe = Number(n.toFixed(2));	
					mi.dataRadar[1][1] = mi.prestamo.plazoEjecucionUe.toFixed(0);
				}	
			}
	};
	
	mi.abrirPopupFecha = function(index) {
		switch(index){
			case 1: mi.ef_abierto = true; break;
		}
	};
	
	mi.obtenerMes= function (mes){
		switch (mes){
			case 1: return "Enero";
			case 2: return "Febrero";
			case 3: return "Marzo";
			case 4: return "Abril";
			case 5: return "Mayo";
			case 6: return "Junio";
			case 7: return "Julio";
			case 8: return "Agosto";
			case 9: return "Septiembre";
			case 10: return "Octubre";
			case 11: return "Noviembre";
			case 12: return "Diciembre";
		
		}
	}
	
	mi.formatoMoneda = function (value) {
		 if (!isNaN(value))
			 return numeral(value).format('0,0.00')
		 else return value;
	 }
	
}]);



		