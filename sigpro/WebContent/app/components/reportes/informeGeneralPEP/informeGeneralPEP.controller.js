var app = angular.module('informeGeneralPEPController', []);


app.controller('informeGeneralPEPController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
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
	 
	$window.document.title = $utilidades.sistema_nombre+' - Informe General del Préstamo';
	i18nService.setCurrentLang('es');
	
	mi.radarOptions = {
			legend: {
				display: true,
				position: 'bottom'
			}
	};
	
	
	 mi.etiquetas =[ "Plazo de Ejecución","Ejecución Financiera", "Ejecución Física" ];
	 mi.series = ["Planificado", "Real"];

	  mi.dataRadar = [
		    [0, 0, 0],  //planificado
		    [0, 0, 0]  //real
		  ];
	  
	  mi.lprestamos = [];
		
	$http.post('/SPrestamo', {accion: 'getPrestamos', t: (new Date()).getTime()}).then(
		function(response){
			if(response.data.success){
				mi.lprestamos = response.data.prestamos;
			}	
	});
	
	mi.blurPrestamo=function(){
		if(document.getElementById("prestamo_value").defaultValue!=mi.prestamoNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','prestamo');
		}
	}
	
	mi.cambioPrestamo=function(selected){
		if(selected!== undefined){
			mi.prestamoNombre = selected.originalObject.proyectoPrograma;
			mi.prestamoId = selected.originalObject.id;
			$scope.$broadcast('angucomplete-alt:clearInput', 'pep');
			$scope.$broadcast('angucomplete-alt:clearInput', 'lineaBase');
			mi.getPeps(mi.prestamoId);
		}
		else{
			mi.prestamoNombre="";
			mi.prestamoId=null;
		}
	}
	
	mi.blurPep=function(){
		if(document.getElementById("pep_value").defaultValue!=mi.pepNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','pep');
		}
	}
	
	mi.cambioPep=function(selected){
		if(selected!== undefined){
			mi.pepNombre = selected.originalObject.nombre;
			mi.pepId = selected.originalObject.id;
			$scope.$broadcast('angucomplete-alt:clearInput', 'lineaBase');
			mi.getLineasBase(mi.pepId);
		}
		else{
			mi.pepNombre="";
			mi.pepId="";
		}
	};
	
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	
	mi.cambioLineaBase=function(selected){
		if(selected!== undefined){
			mi.lineaBaseNombre = selected.originalObject.nombre;
			mi.lineaBaseId = selected.originalObject.id;
			mi.generarReporte();
		}
		else{
			mi.lineaBaseNombre="";
			mi.lineaBaseId=null;
		}
	};
	
	mi.getPeps = function(prestamoId){
		$http.post('/SProyecto',{accion: 'getProyectos', prestamoid: prestamoId}).success(
			function(response) {
				mi.peps = [];
				if (response.success){
					mi.peps = response.entidades;
				}
		});	
	};
	
	mi.getLineasBase = function(proyectoId){
		$http.post('/SProyecto',{accion: 'getLineasBase', proyectoId: proyectoId}).success(
			function(response) {
				mi.lineasBase = [];
				if (response.success){
					mi.lineasBase = response.lineasBase;
				}
		});	
	}
			
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
		mi.dataRadar = [
		    [0, 0, 0],  //planificado
		    [0, 0, 0]  //real
		  ];
			mi.inicializarDatos();
				$http.post('/SPrestamo', {
					accion: 'getPrestamoPorPEP', 
					proyectoId: mi.pepId,
					lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
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
					 $http.post('/SInformeGeneralPEP', { accion: 'getDatosPlan', proyectoId: mi.pepId,
							t: (new Date()).getTime()})
						 .then(function(response){
							 var fecha_actual = moment(response.data.fecha,'DD/MM/YYYY').toDate();
							 
							 mi.dataRadar[0][0] = Number(response.data.plazoEjecucionP).toFixed(2);
							 mi.dataRadar[0][1] = Number(response.data.ejecucionFinancieraP).toFixed(2);
							 mi.dataRadar[0][2] = Number(response.data.ejecucionFisicaP).toFixed(2);
							 
							 
							 mi.dataRadar[1][0] = Number(response.data.plazoEjecucionR).toFixed(2);
							 mi.dataRadar[1][1] = Number(response.data.ejecucionFinancieraR).toFixed(2);
							 mi.dataRadar[1][2] = Number(response.data.ejecucionFisicaR).toFixed(2);
							 
							 mi.mesReportado = mi.obtenerMes(Number(moment(fecha_actual).format('MM')));
							 mi.anioFiscal = Number(moment(fecha_actual).format('YYYY'));
							 mi.mostrar = true; 
							 mi.mostrarCargando = false;
							 mi.mostrarExport = true;
						});
					 
					 }else{
						 $utilidades.mensaje('warning','No se encontro datos del ' + $rootScope.etiquetas.proyecto);
					 }
				});		
	}
	

	mi.exportarExcel = function(){
		$http.post('/SInformeGeneralPEP', { 
			accion: 'exportarExcel', 
			id: mi.pepId,
			plazoEjecucionPlan: mi.dataRadar[0][0],
			ejecucionFinancieraPlan : mi.dataRadar[0][1],
			ejecucionFisicaPlan : mi.dataRadar[0][2],			
			plazoEjecucionReal: mi.dataRadar[1][0],
			ejecucionFinancieraReal : mi.dataRadar[1][1],
			ejecucionFisicaReal : mi.dataRadar[1][2],
			lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
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
		
		mi.exportarJasper = function(){
			var anchor = angular.element('<a/>');
			  anchor.attr({
		         href: '/app/components/reportes/jasper/reporte.jsp?reporte=1&proyecto='+mi.pepId
			  })[0].click();
		}
		
	
	mi.setPorcentaje = function(tipo){
			var n = 0;
			if (tipo==1)
			{
				if(mi.prestamo.desembolsoAFechaUsd != undefined && mi.prestamo.montoContratado != undefined){
					n = (mi.prestamo.desembolsoAFechaUsd / mi.prestamo.montoContratado) * 100;
					mi.prestamo.desembolsoAFechaUsdP = Number(n.toFixed(2));
					//mi.dataRadar[1][2] = Number(((mi.prestamo.desembolsadoAFecha / mi.prestamo.montoContratadoEntidadUsd) * 100).toFixed(2));
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
						
					//mi.dataRadar[1][1] = mi.prestamo.plazoEjecucionPEP.toFixed(2);
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



		