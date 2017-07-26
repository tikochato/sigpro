var app = angular.module('informacionPresupuestariaController',['ngAnimate', 'ngTouch', 'ui.grid.edit', 'ui.grid.rowEdit']);

app.controller('adquisicionesController', ['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion){
		var mi = this;
		i18nService.setCurrentLang('es');
		mi.fechaInicio = 2012;
		mi.fechaFin = 2014;
		mi.movimiento = false;
		mi.margen = 0;
		mi.margenTabla = {};
		
		var AGRUPACION_MES= 1;
		var AGRUPACION_BIMESTRE = 2;
		var AGRUPACION_TRIMESTRE = 3;
		var AGRUPACION_CUATRIMESTRE= 4;
		var AGRUPACION_SEMESTRE= 5;
		var AGRUPACION_ANUAL= 6;
		
		var MES_DISPLAY_NAME = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
		var BIMESTRE_DISPLAY_NAME = ['Bimestre 1', 'Bimestre 2','Bimestre 3','Bimestre 4','Bimestre 5','Bimestre 6'];
		var TRIMESTRE_DISPLAY_NAME = ['Trimestre 1', 'Trimestre 2', 'Trimestre 3', 'Trimestre 4'];
		var CUATRIMESTRE_DISPLAY_NAME = ['Cuatrimestre 1', 'Cuatrimestre 2', 'Cuatrimestre 3'];
		var SEMESTRE_DISPLAY_NAME = ['Semestre 1','Semestre 2'];
		var ANUAL_DISPLAY_NAME = ['Anual'];
		
		mi.agrupaciones = [
			{'value' : 0, 'text' : 'Seleccione una opción'},
			{'value' : AGRUPACION_MES, 'text' : 'Mensual'},
			{'value' : AGRUPACION_BIMESTRE, 'text' : 'Bimestre'},
			{'value' : AGRUPACION_TRIMESTRE, 'text' : 'Trimestre'},
			{'value' : AGRUPACION_CUATRIMESTRE, 'text' : 'Cuatrimestre'},
			{'value' : AGRUPACION_SEMESTRE, 'text' : 'Semestre'},
			{'value' : AGRUPACION_ANUAL, 'text' : 'Anual'},
		];	

		mi.agrupacion = mi.agrupaciones[0];
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	    
	    mi.reiniciarVista=function(){
			if($location.path()=='/informacionPresupuestaria/rv')
				$route.reload();
			else
				$location.path('/informacionPresupuestaria/rv');
		}
	    
		mi.formatofecha = 'yyyy';
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.prestamo = mi.prestamos[0];
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		};
		
		mi.fechaOptions = {
				formatYear: 'yyyy',
			    startingDay: 1,
			    minMode: 'year'
		};
		
		mi.getPrestamos = function(){
			mi.gridOptions = mi.gridOptionsPrestamos;
			
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
		
		mi.atras = function(){
				if(mi.margen < 0){
					mi.margen += mi.tamanoCabecera;
					mi.margenTabla = {'margin-left': mi.margen + "px"};	
				}
		}
		
		mi.siguiente = function(){
				if(mi.margen > (-(mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
					mi.margen -= mi.tamanoCabecera;
					mi.margenTabla = {'margin-left': mi.margen + "px"};
				}
		}
		
		mi.generar = function(agrupacion){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(agrupacion != 0){
							mi.totalCabeceras = 0;
							switch (agrupacion) {
							case 1: mi.totalCabeceras = 12; break;
							case 2: mi.totalCabeceras = 6; break;
							case 3: mi.totalCabeceras = 4; break;
							case 4: mi.totalCabeceras = 3; break;
							case 5: mi.totalCabeceras = 2; break;
							case 6: mi.totalCabeceras = 1; break;
							}
							mi.tamanoPantalla = document.getElementById("reporte").offsetWidth - 200;
							mi.totalAnios = Number(mi.fechaFin) - Number(mi.fechaInicio) + 1;
							mi.totalCabecerasAMostrar = $utilidades.getCantidadCabecerasReporte(mi.tamanoPantalla, mi.totalAnios, agrupacion, 90);
							mi.tamanoCelda = $utilidades.getTamanioColumnaReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabecerasAMostrar);
							mi.estiloCelda = "width:"+ mi.tamanoCelda + "px;min-width:"+ mi.tamanoCelda + "px; max-width:"+ mi.tamanoCelda + "px; text-align: center;";
							mi.tamanoTotal = mi.totalCabecerasAMostrar * mi.totalAnios * mi.tamanoCelda;
							mi.tamanoCabecera = mi.totalAnios * mi.tamanoCelda;
							mi.estiloCabecera = "width:"+ mi.tamanoCabecera + "px;min-width:" + mi.tamanoCabecera +"px; max-width:"+ mi.tamanoCabecera + "px; text-align: center;";
							mi.anios = [];
							for(var i = mi.fechaInicio; i <= mi.fechaFin; i++){
								mi.anios.push({ano: i});
							}
							mi.colspan = mi.anios.length;
							mi.aniosfinales = [];
							
							mi.columnas = [];
							if(agrupacion == AGRUPACION_MES){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: MES_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_BIMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: BIMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_TRIMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: TRIMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_CUATRIMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: CUATRIMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_SEMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: SEMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_ANUAL){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: ANUAL_DISPLAY_NAME[i]});
								}
							}
							
							mi.objetoMostrar = [];
							for(var i =0; i < mi.columnas.length; i++){
								mi.objetoMostrar.push(mi.columnas[i]);
							}
							
							mi.aniosfinales = [];
							for(var i = 0; i < mi.columnas.length; i++){
								for(var j = 0; j < mi.anios.length; j++){
									mi.aniosfinales.push({ano: mi.anios[j].ano});
								}
							}
							
							mi.aniosTotal = [];
							for(var j = 0; j < mi.anios.length; j++){
								mi.aniosTotal.push({ano: mi.anios[j].ano});
							}
							
							mi.movimiento = true;
							mi.mostrarDescargar = true;
							
							$http.post('/SInformacionPresupuestaria', 
								{accion: 'generarInforme', idPrestamo: mi.prestamo.value, anoInicial: mi.fechaInicio, anoFinal: mi.fechaFin, agrupacion: agrupacion}
							).success(
								function(response) {
									mi.rowCollectionNombre = response.prestamo;
									mi.displayedCollectionNombre = [].concat(mi.rowCollectionNombre);
									mi.rowCollectionPrestamo = response.prestamo;
									mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
								});
						}
					}else
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}else
					$utilidades.mensaje('warning','Favor de ingresar un año inicial y final válido');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
		
		mi.getPrestamos();
}]);