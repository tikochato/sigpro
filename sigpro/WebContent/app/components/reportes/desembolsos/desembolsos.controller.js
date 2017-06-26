var app = angular.module('desembolsosController', []);

app.controller('desembolsosController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
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
	
	mi.desembolsos= [];
	mi.lista = [];
	mi.anios = [];
	mi.anio = "";
	mi.columnas=[];
	
	
	mi.fechaOptions = {
			datepickerMode:"year",
			  minMode:"year",
	};
	
	$window.document.title = $utilidades.sistema_nombre+' - Desembolsos';
	i18nService.setCurrentLang('es');
	
	mi.etiqutas = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio","Agosto","Septiembre",
		"Octubre","Noviembre","Diciembre"];
	mi.series = ['Planificado', 'Real'];
	mi.radarColors = ['#b1cad7','#FDB45C']
	mi.datasetOverride = [{ yAxisID: 'y-axis-1' }
	];
	
	mi.options = {
			
			legend: {
				display: true,
				position: 'right'
			},
			    scales: {
			      yAxes: [
			        {
			          id: 'y-axis-1',
			          type: 'linear',
			          display: true,
			          position: 'left',
			          ticks: {
	                        callback: function (value) {
	                            return numeral(value).format('$ 0,0')
	                        }
	                    }
			        }
			      ]
			    }
			  };
	
	
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
		
		mi.desembolsos= [];
		mi.lista = [];
		mi.anios = [];
		mi.anio = "";
		mi.columnas=[];
	}
	
	mi.generarReporte = function (){
		mi.inicializarDatos();
		$http.post('/SDesembolsos', { accion: 'getDesembolsos'
			, proyectoId: mi.prestamoSeleccionado.value,ejercicioFiscal:2013,proyectoId:mi.prestamoSeleccionado.value }).success(
	
			function(response) {
				if (response.success){
					mi.lista = response.lista;
					var anios_temp = [];
					for (x in mi.lista){
						anios_temp.push(mi.lista[x].anio);
					}
					anios_temp= anios_temp.sort();
					
					for (x in anios_temp){
						var item = [];
						item.id = anios_temp[x];
						item.nombre = anios_temp[x];
						mi.anios.push(item);
					}
					
					mi.anioSeleccionado = mi.anios!=null && mi.anios != undefined && mi.anios.length > 0 ?  mi.anios[0].id : undefined;
					mi.mostrar = true;
					mi.asignarSerie();
			}else{
				$utilidades.mensaje('warning','No se encontraron datos para el préstamo');
			}
				
		});	
	}
	
	mi.asignarSerie = function(){
		if (mi.mostrar && mi.desembolsos!=null){
			mi.anio = [];
			mi.anio.id = mi.anioSeleccionado;
			mi.anio.nombre = mi.anioSeleccionado;
			mi.tabla=[];
			for (x in mi.lista){
				if (mi.lista[x].anio === mi.anio.id){
					mi.desembolsos = mi.lista[x].desembolsos;
					break;
				}
			}
			
			var totalReal=0;
			var totalPlanificado=0;
			var totalVariacion=0;
			var variaciones = [];
			var desembolsoPlanificado = mi.desembolsos[0].slice();
			var desembolsoReal = mi.desembolsos[1].slice();
			for (x = 0;x<12;x++){
				totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
				totalReal = totalReal + desembolsoReal[x];
				var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
				variaciones.push (variacion)
				totalVariacion = totalVariacion + variacion;
			}
			
			desembolsoPlanificado.push("Planificado");
			desembolsoPlanificado.push(totalPlanificado);
			desembolsoReal.push("Real");
			desembolsoReal.push(totalReal);
			variaciones.push("Variacion");
			
			variaciones.push(totalVariacion);
			
			mi.tabla.push(desembolsoPlanificado);
			mi.tabla.push(desembolsoReal);
			mi.tabla.push(variaciones);
			
			mi.columnas=[];
			mi.columnas.push ("Mes");
			mi.columnas.push(...mi.etiqutas);
			mi.columnas.push("Total");
		}	
	}
	
	
	
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
	
	 mi.formato1 = function (value) {
         return numeral(value).format('0,0.00')
	 }
}]);
