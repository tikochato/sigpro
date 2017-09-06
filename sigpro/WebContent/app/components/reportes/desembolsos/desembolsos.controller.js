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
	mi.desembolsosOriginal = [];
	mi.desembolsosGrafica=[];
	mi.lista = [];
	mi.anios = [];
	mi.anio = "";
	mi.columnas=[];
	mi.agrupaciones=[];
	mi.agrupacion=1;
	mi.yAxisNombre="";
	  
	mi.enMillones = true;
	mi.enMillonesAux = true; 
	
	
	mi.fechaOptions = {
			datepickerMode:"year",
			  minMode:"year",
	};
	
	$window.document.title = $utilidades.sistema_nombre+' - Desembolsos';
	i18nService.setCurrentLang('es');
	
	mi.etiqutas = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio","Agosto","Septiembre",
		"Octubre","Noviembre","Diciembre"];
	mi.series = ['Planificado', 'Real'];
	mi.radarColors = ['#88b4df','#8ecf4c'];
	mi.datasetOverride = [{ yAxisID: 'y-axis-1' }
	];
	
	mi.agrupaciones = [{id:1,nombre:"Mensual"},{id:2,nombre:"Bimestral"},{id:3,nombre:"Trimsestral"},
		{id:4,nombre:"Cuatrimestral"},{id:5,nombre:"Semestral"},{id:6,nombre:"Anual"}];
	
	mi.optionsMillones = {
			
			legend: {
				display: true,
				position: 'bottom'
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
			        	    	 if (mi.enMillones)
			        	    		 value = value / 1000000;
			        	    	 return numeral(value).format('$ 0,0')
	                        }
	                   },
	                   scaleLabel: {
	                       display: true,
	                       labelString: 'Monto'
	                     }
			        	
			        }
			      ],
			      xAxes: [{
			    	  scaleLabel: {
	                       display: true,
	                       labelString: mi.yAxisNombre
	                     }
			      }
			      ]
			    }
			  };
	
	
mi.options = {
			
			legend: {
				display: true,
				position: 'bottom'
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
	                   },
	                   scaleLabel: {
	                       display: true,
	                       labelString: 'Monto'
	                     }
			        	
			        }
			      ],
			      xAxes: [{
			    	  scaleLabel: {
	                       display: true,
	                       labelString: mi.yAxisNombre
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
		//mi.agrupacion = mi.agruapacion == null || mi.agrupacion == undefined ? 1 : mi.agrupacion;
		mi.agrupacion = 1;
		mi.desembolsosGrafica = [];
	}
	
	mi.generarReporte = function (){
		mi.inicializarDatos();
		
		if ( mi.prestamoSeleccionado != null && mi.anioSeleccionado > 0 &&  mi.anioSeleccionado.toString().length == 4 ){
			mi.inicializarDatos();
			mi.mostrar=true;
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
						
						//mi.anioSeleccionado = mi.anios!=null && mi.anios != undefined && mi.anios.length > 0 ?  mi.anios[0].id : undefined;
						//mi.agrupacion = mi.agrupaciones[0].id;
						mi.mostrar = true;
						mi.asignarSerie(mi.agrupacion);
						
				}else{
					$utilidades.mensaje('warning','No se encontraron datos para el préstamo');
					mi.mostrar = false;
				}
					
			});	
		}else{
			mi.mostrar = false
		}
	}
	
	mi.asignarSerie = function(valor){
	
		mi.columnas=[];
		mi.etiqutas = [];
		var totalItems=0;
		mi.agrupacion = valor;
		switch (mi.agrupacion){
			case 1:
				mi.etiqutas = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
					"Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre",];
				mi.columnas.push ("Mes");
				mi.columnas.push(...mi.etiqutas);
				totalItems=12;
				mi.options.scales.xAxes[0].scaleLabel.labelString="Meses";
				break;
			case 2:
				mi.etiqutas = ["1", "2", "3", "4", "5", "6"];
				mi.columnas.push ("Bimestre");
				mi.columnas.push(...mi.etiqutas);
				totalItems=6;
				mi.options.scales.xAxes[0].scaleLabel.labelString="Bimestres";
				break;
			case 3:
				mi.etiqutas = ["1", "2", "3", "4"];
				mi.columnas.push ("Trimestre");
				mi.columnas.push(...mi.etiqutas);
				mi.options.scales.xAxes[0].scaleLabel.labelString="Trimestres";
				totalItems=4;
				break;
			case 4:
				mi.etiqutas = ["1", "2", "3"];
				mi.columnas.push ("Cuatrimestre");
				mi.columnas.push(...mi.etiqutas);
				mi.options.scales.xAxes[0].scaleLabel.labelString="Cuatrimestres";
				totalItems=3;
				break;
			case 5:
				mi.etiqutas = ["1", "2"];
				mi.columnas.push ("Semestre");
				mi.columnas.push(...mi.etiqutas);
				totalItems=2;
				mi.options.scales.xAxes[0].scaleLabel.labelString="Semestres";
				break;
			case 6:
				totalItems=mi.anios.length;
				for (x in mi.anios){
					mi.etiqutas.push(mi.anios[x].nombre);
				}
				mi.columnas.push ("Año");
				mi.columnas.push(...mi.etiqutas)
				mi.options.scales.xAxes[0].scaleLabel.labelString="Años";
				break;
		}
		mi.columnas.push("Total");
		
		
		if (mi.mostrar && mi.desembolsos!=null){
			mi.anio = [];
			mi.anio.id = mi.anioSeleccionado;
			mi.anio.nombre = mi.anioSeleccionado;
			mi.tabla=[];
			if (mi.agrupacion < 6){
				for (x in mi.lista){
					if (mi.lista[x].anio === mi.anio.id){
						mi.desembolsos = mi.lista[x].desembolsos;
						break;
					}
				}
			}else{
				mi.desembolsos = [];
				mi.desembolsos[0] = [];
				mi.desembolsos[1] = [];
				for (x in mi.lista){
					var des_temp = mi.lista[x].desembolsos
					var des_temp_p=des_temp[0];
					var des_temp_r=des_temp[1];
					var des_anual_r = 0;
					var des_anual_p = 0;
					for (y = 0;y<12;y++){
						des_anual_p = des_anual_p + des_temp_p[y];
						des_anual_r = des_anual_r + des_temp_r[y];
					}
					mi.desembolsos[0].push(des_anual_p);
					mi.desembolsos[1].push(des_anual_r);
				}
			}
			mi.agrupar();
			
			
			var totalReal=0;
			var totalPlanificado=0;
			var totalVariacion=0;
			var variaciones = [];
			var desembolsoPlanificado = [];
			var desembolsoReal = [];
			
			desembolsoPlanificado.push("Planificado");
			desembolsoPlanificado.push(...mi.desembolsos[0].slice());
			
			desembolsoReal.push("Real");
			desembolsoReal.push(...mi.desembolsos[1].slice());
			
			variaciones.push("Variación");
			
			for (x = 1;x<=totalItems;x++){
				totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
				totalReal = totalReal + desembolsoReal[x];
				var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
				variaciones.push (variacion)
				totalVariacion = totalVariacion + variacion;
			}
			
			desembolsoPlanificado.push(totalPlanificado);
			desembolsoReal.push(totalReal);
			variaciones.push(totalVariacion);
			
			mi.tabla.push(desembolsoPlanificado);
			mi.tabla.push(desembolsoReal);
			mi.tabla.push(variaciones);
			
			mi.convertirMillones();
		}	
	}
	
	mi.agrupar = function() {
		if (mi.agrupacion < 6){
			
			var desembolsoPlanificado = [];
			var desembolsoReal = [];
			
			desembolsoPlanificado.push(...mi.desembolsos[0].slice());
			desembolsoReal.push(...mi.desembolsos[1].slice());
			
			var agrupacionFinalPlanificado = [];
			var agrupacionFinalReal = [];
			
			
			var maximo = 1;
			var totalGrupoPlanificado = 0;
			var totalGrupoReal = 0;
			for (x in desembolsoPlanificado){
				totalGrupoPlanificado = totalGrupoPlanificado + desembolsoPlanificado[x];
				totalGrupoReal = totalGrupoReal +  desembolsoReal[x];
				if (maximo == mi.agrupacion){
					agrupacionFinalPlanificado.push(totalGrupoPlanificado);
					agrupacionFinalReal.push(totalGrupoReal);
					maximo = 1;
					totalGrupoPlanificado = 0;
					totalGrupoReal = 0;
				}else
					maximo++;		
			}
			
			mi.desembolsos = [];
			mi.desembolsos.push(agrupacionFinalPlanificado);
			mi.desembolsos.push(agrupacionFinalReal);
			mi.desembolsosOriginal=[]
			mi.desembolsosOriginal.push(...mi.desembolsos);
			
			 mi.convertirMillones();
			 
		
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
		 if (!isNaN(value))
			 return numeral(value).format('0,0.00')
		 else return value;
	 }
	 
	 mi.clase = function (value){
		 switch (value){
		 case 0: return "planificado";
		 case 1: return "real2";
		 default: return "";
		 }
	 }
	 
	 mi.esNumero = function(value){
		 return !isNaN(value);
	 }
	 
	 mi.NombrexAxis = function(value){
		 switch (value){
		 	case 1: mi.yAxisNombre="Mensual"; break;
		 	case 2: mi.yAxisNombre="Bimestral"; break;
		 	case 3: mi.yAxisNombre="Semestral"; break;
		 	case 4: mi.yAxisNombre="Trimestral"; break;
		 	case 5: mi.yAxisNombre="Cuatrimesral"; break;
		 	case 1: mi.yAxisNombre="Semestral"; break;
		 	case 1: mi.yAxisNombre="Anual"; break;
		 }
	 }
	 
	 
	 mi.convertirMillones = function(){
		 mi.desembolsosGrafica = [];
		
		 mi.desembolsosGrafica = JSON.parse(JSON.stringify(mi.desembolsosOriginal));
		 
		 
			 for (x in mi.desembolsos){
				 
				 for (y = 0; y<12 ; y++){
					 if (mi.enMillones){
						 mi.desembolsosGrafica[x][y] = mi.desembolsosGrafica[x][y] / 1000000;
					 }else
						 break;
				 }
			 }
		  
	 }
	 
}]);
