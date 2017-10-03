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
	mi.anioInicial=0;
	mi.anioFinal=0;
	mi.mostrarDescargar = false;
	
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
	
	mi.datosOrigniales;
	
	
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
		
		mi.agrupacion = 1;
		mi.desembolsosGrafica = [];
	}
	
	mi.generarReporte = function (){
		
		mi.inicializarDatos();
		mi.mostrarDescargar = false;
		
		if ( mi.prestamoSeleccionado != null && mi.anio_inicio > 0 &&  mi.anio_inicio.toString().length == 4 ){
			mi.inicializarDatos();
			mi.mostrar=true;
			$http.post('/SDesembolsos', { accion: 'getDesembolsos',
				 proyectoId: mi.prestamoSeleccionado.value,
				 anio_inicial:mi.anio_inicio,
				 anio_final:mi.anio_fin,
				 t: new Date().getTime()
				
				}).success(
		
				function(response) {
					if (response.success){
						mi.datosOrigniales = response;
						mi.asignarSerie2(mi.datosOrigniales.planificado, mi.datosOrigniales.real,mi.datosOrigniales.costos,mi.datosOrigniales.reald,1);
						
						mi.mostrar = true;
						mi.mostrarDescargar = true;
						
						
				}else{
					$utilidades.mensaje('warning','No se encontraron datos para el préstamo');
					mi.mostrar = false;
				}
					
			});	
		}else{
			mi.mostrar = false
		}
	}
	
	mi.asignarSerie2 = function (planificado, real,costo, reald,agrupacion){
		
		var totalReal=0;
		var totalReald=0;
		var totalPlanificado=0;
		var totalVariacion=0;
		var totalCostoPlan=0;
		var variaciones = [];
		var porcentajeVariaciones = [];
		var desembolsoPlanificado = [];
		var desembolsoReal = [];
		var desembolsoReald = [];
		var costoPlan = [];
		mi.tabla=[];
		var totalItems = 0;
		
		switch (agrupacion){
			case 1:
				totalItems = (mi.anio_fin - mi.anio_inicio +1) * 12;
				
				costoPlan.push ("Costo");
				costoPlan.push (...costo.slice());
			
				desembolsoPlanificado.push("Planificado");
				desembolsoPlanificado.push(...planificado.slice());
				
				desembolsoReal.push("Real");
				desembolsoReal.push(...real.slice());
				
				desembolsoReald.push("Real ($)");
				desembolsoReald.push(...reald.slice());
				
				variaciones.push("Variación");
				porcentajeVariaciones.push("Porcentaje");
				
				for (x = 1;x<=totalItems;x++){
					totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
					totalReal = totalReal + desembolsoReal[x];
					totalReald = totalReald + desembolsoReald[x];
					totalCostoPlan= totalCostoPlan + costoPlan[x];
					var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
					var var1 = variacion / desembolsoPlanificado[x];
					var var2 = variacion / desembolsoReal[x];
					var1 = (var1 * 100).toFixed(2);
					var2 = (var2 * 100).toFixed(2);
					var porcentajeVariacion = (variacion / (variacion > 0 ? desembolsoPlanificado[x] : desembolsoReal[x] ) * 100).toFixed(2);
					
					variaciones.push (variacion)
					porcentajeVariaciones.push(isNaN(porcentajeVariacion) ? "0%" :porcentajeVariacion+"%" );
					totalVariacion = totalVariacion + variacion;
				}
				
				costoPlan.push(totalCostoPlan);
				desembolsoPlanificado.push(totalPlanificado);
				desembolsoReal.push(totalReal);
				desembolsoReald.push(totalReald);
				variaciones.push(isNaN((totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2)) ? "0%" :
					(totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2));
				porcentajeVariaciones.push("");
				
				mi.tabla.push(costoPlan);
				mi.tabla.push(desembolsoPlanificado);
				mi.tabla.push(desembolsoReal);
				mi.tabla.push(desembolsoReald);
				mi.tabla.push(variaciones);
				mi.tabla.push(porcentajeVariaciones);
				
				mi.generarEtiquetas(agrupacion,totalItems);
				mi.convertirMillones();
					
				break;
			case 2:
				totalItems = (mi.anio_fin - mi.anio_inicio +1) * 6;
				
				costoPlan.push ("Costo");
				desembolsoPlanificado.push("Planificado");
				desembolsoReal.push("Real");
				desembolsoReald.push("Real ($)");
				variaciones.push("Variación");
				porcentajeVariaciones.push("Porcentaje");
				
				var costoPlanTemp = [];
				costoPlanTemp.push(...costo.slice());
				var desembolsoRTemp =[];
				desembolsoRTemp.push(...real.slice());
				var desembolsoRTempd =[];
				desembolsoRTempd.push(...reald.slice());
				var desembolsoPTemp =[];
				desembolsoPTemp.push(...planificado.slice());
				var costoPlanAgrupado = [];
				var desembolsoRAgrupado =[];
				var desembolsoRAgrupadod =[];
				var desembolsoPAgrupado =[];
				
				for (x=0; x< totalItems ; x++){
					costoPlanAgrupado.push(costoPlanTemp[x * 2] + costoPlanTemp[x * 2 + 1]);
					desembolsoRAgrupado.push(desembolsoRTemp[x * 2] + desembolsoRTemp[x * 2 + 1] );
					desembolsoRAgrupadod.push(desembolsoRTempd[x * 2] + desembolsoRTempd[x * 2 + 1] );
					desembolsoPAgrupado.push(desembolsoPTemp[x * 2] + desembolsoPTemp[x * 2 + 1])
				}
				
				costoPlan.push (...costoPlanAgrupado.slice());			
				desembolsoPlanificado.push(...desembolsoPAgrupado.slice());
				desembolsoReal.push(...desembolsoRAgrupado.slice());
				desembolsoReald.push(...desembolsoRAgrupadod.slice());
				
				for (x = 1;x<=totalItems;x++){
					totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
					totalReal = totalReal + desembolsoReal[x];
					totalReald = totalReald + desembolsoReald[x];
					totalCostoPlan= totalCostoPlan + costoPlan[x];
					var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
					var var1 = variacion / desembolsoPlanificado[x];
					var var2 = variacion / desembolsoReal[x];
					var1 = (var1 * 100).toFixed(2);
					var2 = (var2 * 100).toFixed(2);
					var porcentajeVariacion = (variacion / (variacion > 0 ? desembolsoPlanificado[x] : desembolsoReal[x] ) * 100).toFixed(2);
					
					variaciones.push (variacion)
					porcentajeVariaciones.push(isNaN(porcentajeVariacion) ? "0%" :porcentajeVariacion+"%" );
					totalVariacion = totalVariacion + variacion;
				}
				
				costoPlan.push(totalCostoPlan);
				desembolsoPlanificado.push(totalPlanificado);
				desembolsoReal.push(totalReal);
				desembolsoReald.push(totalReald);
				variaciones.push(isNaN((totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2)) ? "0%" :
					(totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2));
				porcentajeVariaciones.push("");
				
				mi.tabla.push(costoPlan);
				mi.tabla.push(desembolsoPlanificado);
				mi.tabla.push(desembolsoReal);
				mi.tabla.push(desembolsoReald);
				mi.tabla.push(variaciones);
				mi.tabla.push(porcentajeVariaciones);
				
				mi.generarEtiquetas(agrupacion,totalItems);
				mi.convertirMillones();
					
				break;
			case 3:
				totalItems = (mi.anio_fin - mi.anio_inicio +1) * 4;
				
				costoPlan.push ("Costo");
				desembolsoPlanificado.push("Planificado");
				desembolsoReal.push("Real");
				desembolsoReald.push("Real ($)");
				variaciones.push("Variación");
				porcentajeVariaciones.push("Porcentaje");
				
				var costoPlanTemp = [];
				costoPlanTemp.push(...costo.slice());
				var desembolsoRTemp =[];
				desembolsoRTemp.push(...real.slice());
				var desembolsoRTempd =[];
				desembolsoRTempd.push(...reald.slice());
				var desembolsoPTemp =[];
				desembolsoPTemp.push(...planificado.slice());
				var costoPlanAgrupado = [];
				var desembolsoRAgrupado =[];
				var desembolsoRAgrupadod =[];
				var desembolsoPAgrupado =[];
				
				for (x=0; x< totalItems ; x++){
					costoPlanAgrupado.push(costoPlanTemp[x * 3] + costoPlanTemp[x * 3 + 1] + costoPlanTemp[x * 3 + 2]);
					desembolsoRAgrupado.push(desembolsoRTemp[x * 3] + desembolsoRTemp[x * 3 + 1] + desembolsoRTemp[x * 3 + 2] );
					desembolsoRAgrupadod.push(desembolsoRTempd[x * 3] + desembolsoRTempd[x * 3 + 1] + desembolsoRTempd[x * 3 + 2] );
					desembolsoPAgrupado.push(desembolsoPTemp[x * 3] + desembolsoPTemp[x * 3 + 1] + desembolsoPTemp[x * 3 + 2])
				}
				
				costoPlan.push (...costoPlanAgrupado.slice());			
				desembolsoPlanificado.push(...desembolsoPAgrupado.slice());
				desembolsoReal.push(...desembolsoRAgrupado.slice());
				desembolsoReald.push(...desembolsoRAgrupadod.slice());
				
				for (x = 1;x<=totalItems;x++){
					totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
					totalReal = totalReal + desembolsoReal[x];
					totalReald = totalReald + desembolsoReald[x];
					totalCostoPlan= totalCostoPlan + costoPlan[x];
					var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
					var var1 = variacion / desembolsoPlanificado[x];
					var var2 = variacion / desembolsoReal[x];
					var1 = (var1 * 100).toFixed(2);
					var2 = (var2 * 100).toFixed(2);
					var porcentajeVariacion = (variacion / (variacion > 0 ? desembolsoPlanificado[x] : desembolsoReal[x] ) * 100).toFixed(2);
					
					variaciones.push (variacion)
					porcentajeVariaciones.push(isNaN(porcentajeVariacion) ? "0%" :porcentajeVariacion+"%" );
					totalVariacion = totalVariacion + variacion;
				}
				
				costoPlan.push(totalCostoPlan);
				desembolsoPlanificado.push(totalPlanificado);
				desembolsoReal.push(totalReal);
				desembolsoReald.push(totalReald);
				variaciones.push(isNaN((totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2)) ? "0%" :
					(totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2));
				porcentajeVariaciones.push("");
				
				mi.tabla.push(costoPlan);
				mi.tabla.push(desembolsoPlanificado);
				mi.tabla.push(desembolsoReal);
				mi.tabla.push(desembolsoReald);
				mi.tabla.push(variaciones);
				mi.tabla.push(porcentajeVariaciones);
				
				mi.generarEtiquetas(agrupacion,totalItems);
				mi.convertirMillones();
					
				break;
			case 4:
				totalItems = (mi.anio_fin - mi.anio_inicio +1) * 3;
				
				costoPlan.push ("Costo");
				desembolsoPlanificado.push("Planificado");
				desembolsoReal.push("Real");
				desembolsoReald.push("Real ($)");
				variaciones.push("Variación");
				porcentajeVariaciones.push("Porcentaje");
				
				var costoPlanTemp = [];
				costoPlanTemp.push(...costo.slice());
				var desembolsoRTemp =[];
				desembolsoRTemp.push(...real.slice());
				var desembolsoRTempd =[];
				desembolsoRTempd.push(...reald.slice());
				var desembolsoPTemp =[];
				desembolsoPTemp.push(...planificado.slice());
				var costoPlanAgrupado = [];
				var desembolsoRAgrupado =[];
				var desembolsoRAgrupadod =[];
				var desembolsoPAgrupado =[];
				
				for (x=0; x< totalItems ; x++){
					costoPlanAgrupado.push(costoPlanTemp[x * 4] + costoPlanTemp[x * 4 + 1] + costoPlanTemp[x * 4 + 2] + costoPlanTemp[x * 4 + 3]);
					desembolsoRAgrupado.push(desembolsoRTemp[x * 4] + desembolsoRTemp[x * 4 + 1] + desembolsoRTemp[x * 4 + 2] + desembolsoRTemp[x * 4 + 3] );
					desembolsoRAgrupadod.push(desembolsoRTempd[x * 4] + desembolsoRTempd[x * 4 + 1] + desembolsoRTempd[x * 4 + 2] + desembolsoRTempd[x * 4 + 3] );
					desembolsoPAgrupado.push(desembolsoPTemp[x * 4] + desembolsoPTemp[x * 4 + 1] + desembolsoPTemp[x * 4 + 2] + desembolsoPTemp[x * 4 + 3])
				}
				
				costoPlan.push (...costoPlanAgrupado.slice());			
				desembolsoPlanificado.push(...desembolsoPAgrupado.slice());
				desembolsoReal.push(...desembolsoRAgrupado.slice());
				desembolsoReald.push(...desembolsoRAgrupadod.slice());
				
				for (x = 1;x<=totalItems;x++){
					totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
					totalReal = totalReal + desembolsoReal[x];
					totalReald = totalReald + desembolsoReald[x];
					totalCostoPlan= totalCostoPlan + costoPlan[x];
					var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
					var var1 = variacion / desembolsoPlanificado[x];
					var var2 = variacion / desembolsoReal[x];
					var1 = (var1 * 100).toFixed(2);
					var2 = (var2 * 100).toFixed(2);
					var porcentajeVariacion = (variacion / (variacion > 0 ? desembolsoPlanificado[x] : desembolsoReal[x] ) * 100).toFixed(2);
					
					variaciones.push (variacion)
					porcentajeVariaciones.push(isNaN(porcentajeVariacion) ? "0%" :porcentajeVariacion+"%" );
					totalVariacion = totalVariacion + variacion;
				}
				
				costoPlan.push(totalCostoPlan);
				desembolsoPlanificado.push(totalPlanificado);
				desembolsoReal.push(totalReal);
				desembolsoReald.push(totalReald);
				variaciones.push(isNaN((totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2)) ? "0%" :
					(totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2));
				porcentajeVariaciones.push("");
				
				mi.tabla.push(costoPlan);
				mi.tabla.push(desembolsoPlanificado);
				mi.tabla.push(desembolsoReal);
				mi.tabla.push(desembolsoReald);
				mi.tabla.push(variaciones);
				mi.tabla.push(porcentajeVariaciones);
				
				mi.generarEtiquetas(agrupacion,totalItems);
				mi.convertirMillones();
					
				break;
			case 5:
				totalItems = (mi.anio_fin - mi.anio_inicio +1) * 2;
				
				costoPlan.push ("Costo");
				desembolsoPlanificado.push("Planificado");
				desembolsoReal.push("Real");
				desembolsoReald.push("Real ($)");
				variaciones.push("Variación");
				porcentajeVariaciones.push("Porcentaje");
				
				var costoPlanTemp = [];
				costoPlanTemp.push(...costo.slice());
				var desembolsoRTemp =[];
				desembolsoRTemp.push(...real.slice());
				var desembolsoRTempd =[];
				desembolsoRTempd.push(...reald.slice());
				var desembolsoPTemp =[];
				desembolsoPTemp.push(...planificado.slice());
				var costoPlanAgrupado = [];
				var desembolsoRAgrupado =[];
				var desembolsoRAgrupadod =[];
				var desembolsoPAgrupado =[];
				
				for (x=0; x< totalItems ; x++){
					costoPlanAgrupado.push(costoPlanTemp[x * 6] + costoPlanTemp[x * 6 + 1] + costoPlanTemp[x * 6 + 2] + costoPlanTemp[x * 6 + 3] + costoPlanTemp[x * 6 + 4] + costoPlanTemp[x * 6 + 5]);
					desembolsoRAgrupado.push(desembolsoRTemp[x * 6] + desembolsoRTemp[x * 6 + 1] + desembolsoRTemp[x * 6 + 2] + desembolsoRTemp[x * 6 + 3] + desembolsoRTemp[x * 6 + 4] + desembolsoRTemp[x * 6 + 5] );
					desembolsoRAgrupadod.push(desembolsoRTempd[x * 6] + desembolsoRTempd[x * 6 + 1] + desembolsoRTempd[x * 6 + 2] + desembolsoRTempd[x * 6 + 3] + desembolsoRTempd[x * 6 + 4] + desembolsoRTempd[x * 6 + 5] );
					desembolsoPAgrupado.push(desembolsoPTemp[x * 6] + desembolsoPTemp[x * 6 + 1] + desembolsoPTemp[x * 6 + 2] + desembolsoPTemp[x * 6 + 3]+ desembolsoPTemp[x * 6 + 4]+ desembolsoPTemp[x * 6 + 5]);
				}
				
				costoPlan.push (...costoPlanAgrupado.slice());			
				desembolsoPlanificado.push(...desembolsoPAgrupado.slice());
				desembolsoReal.push(...desembolsoRAgrupado.slice());
				desembolsoReald.push(...desembolsoRAgrupadod.slice());
				
				for (x = 1;x<=totalItems;x++){
					totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
					totalReal = totalReal + desembolsoReal[x];
					totalReald = totalReald + desembolsoReald[x];
					totalCostoPlan= totalCostoPlan + costoPlan[x];
					var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
					var var1 = variacion / desembolsoPlanificado[x];
					var var2 = variacion / desembolsoReal[x];
					var1 = (var1 * 100).toFixed(2);
					var2 = (var2 * 100).toFixed(2);
					var porcentajeVariacion = (variacion / (variacion > 0 ? desembolsoPlanificado[x] : desembolsoReal[x] ) * 100).toFixed(2);
					
					variaciones.push (variacion)
					porcentajeVariaciones.push(isNaN(porcentajeVariacion) ? "0%" :porcentajeVariacion+"%" );
					totalVariacion = totalVariacion + variacion;
				}
				
				costoPlan.push(totalCostoPlan);
				desembolsoPlanificado.push(totalPlanificado);
				desembolsoReal.push(totalReal);
				desembolsoReald.push(totalReald);
				variaciones.push(isNaN((totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2)) ? "0%" :
					(totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2));
				porcentajeVariaciones.push("");
				
				mi.tabla.push(costoPlan);
				mi.tabla.push(desembolsoPlanificado);
				mi.tabla.push(desembolsoReal);
				mi.tabla.push(desembolsoReald);
				mi.tabla.push(variaciones);
				mi.tabla.push(porcentajeVariaciones);
				
				mi.generarEtiquetas(agrupacion,totalItems);
				mi.convertirMillones();
					
				break;
			case 6:
				totalItems = (mi.anio_fin - mi.anio_inicio +1) * 1;
				
				costoPlan.push ("Costo");
				desembolsoPlanificado.push("Planificado");
				desembolsoReal.push("Real");
				desembolsoReald.push("Real ($)");
				variaciones.push("Variación");
				porcentajeVariaciones.push("Porcentaje");
				
				var costoPlanTemp = [];
				costoPlanTemp.push(...costo.slice());
				var desembolsoRTemp =[];
				desembolsoRTemp.push(...real.slice());
				var desembolsoRTempd =[];
				desembolsoRTempd.push(...reald.slice());
				var desembolsoPTemp =[];
				desembolsoPTemp.push(...planificado.slice());
				var costoPlanAgrupado = [];
				var desembolsoRAgrupado =[];
				var desembolsoRAgrupadod =[];
				var desembolsoPAgrupado =[];
				
				for (x=0; x< totalItems ; x++){
					costoPlanAgrupado.push(costoPlanTemp[x * 12] + costoPlanTemp[x * 12 + 1] + costoPlanTemp[x * 12 + 2] + costoPlanTemp[x * 12 + 3] + costoPlanTemp[x * 12 + 4] + costoPlanTemp[x * 12 + 5]
					+ costoPlanTemp[x * 12 + 6] + costoPlanTemp[x * 12 + 7] + costoPlanTemp[x * 12 + 8] + costoPlanTemp[x * 12 + 9] + costoPlanTemp[x * 12 + 10] + costoPlanTemp[x * 12 + 11]);
					desembolsoRAgrupado.push(desembolsoRTemp[x * 12] + desembolsoRTemp[x * 12 + 1] + desembolsoRTemp[x * 12 + 2] + desembolsoRTemp[x * 12 + 3] + desembolsoRTemp[x * 12 + 4] + desembolsoRTemp[x * 12 + 5]
					+ desembolsoRTemp[x * 12 +6] + desembolsoRTemp[x * 12 + 7] + desembolsoRTemp[x * 12 + 8] + desembolsoRTemp[x * 12 + 9] + desembolsoRTemp[x * 12 + 10] + desembolsoRTemp[x * 12 + 11]);
					desembolsoRAgrupadod.push(desembolsoRTempd[x * 12] + desembolsoRTempd[x * 12 + 1] + desembolsoRTempd[x * 12 + 2] + desembolsoRTempd[x * 12 + 3] + desembolsoRTempd[x * 12 + 4] + desembolsoRTempd[x * 12 + 5]
					+ desembolsoRTempd[x * 12 +6] + desembolsoRTempd[x * 12 + 7] + desembolsoRTempd[x * 12 + 8] + desembolsoRTempd[x * 12 + 9] + desembolsoRTempd[x * 12 + 10] + desembolsoRTempd[x * 12 + 11]);
					desembolsoPAgrupado.push(desembolsoPTemp[x * 2] + desembolsoPTemp[x * 12 + 1] + desembolsoPTemp[x * 12 + 2] + desembolsoPTemp[x * 12 + 3]+ desembolsoPTemp[x * 12 + 4]+ desembolsoPTemp[x * 12 + 5]
					+ desembolsoPTemp[x * 12 + 6] + desembolsoPTemp[x * 12 + 7] + desembolsoPTemp[x * 12 + 8] + desembolsoPTemp[x * 12 + 9]+ desembolsoPTemp[x * 12 + 10]+ desembolsoPTemp[x * 12 + 11])
				}
				
				costoPlan.push (...costoPlanAgrupado.slice());			
				desembolsoPlanificado.push(...desembolsoPAgrupado.slice());
				desembolsoReal.push(...desembolsoRAgrupado.slice());
				desembolsoReald.push(...desembolsoRAgrupadod.slice());
				
				for (x = 1;x<=totalItems;x++){
					totalPlanificado = totalPlanificado+ desembolsoPlanificado[x];
					totalReal = totalReal + desembolsoReal[x];
					totalReald = totalReald + desembolsoReald[x];
					totalCostoPlan= totalCostoPlan + costoPlan[x];
					var variacion = desembolsoPlanificado[x] - desembolsoReal[x];
					var var1 = variacion / desembolsoPlanificado[x];
					var var2 = variacion / desembolsoReal[x];
					var1 = (var1 * 100).toFixed(2);
					var2 = (var2 * 100).toFixed(2);
					var porcentajeVariacion = (variacion / (variacion > 0 ? desembolsoPlanificado[x] : desembolsoReal[x] ) * 100).toFixed(2);
					
					variaciones.push (variacion)
					porcentajeVariaciones.push(isNaN(porcentajeVariacion) ? "0%" :porcentajeVariacion+"%" );
					totalVariacion = totalVariacion + variacion;
				}
				
				costoPlan.push(totalCostoPlan);
				desembolsoPlanificado.push(totalPlanificado);
				desembolsoReal.push(totalReal);
				desembolsoReald.push(totalReald);
				variaciones.push(isNaN((totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2)) ? "0%" :
					(totalVariacion /(totalVariacion > 0 ? totalPlanificado : totalReal)*100).toFixed(2));
				porcentajeVariaciones.push("");
				
				mi.tabla.push(costoPlan);
				mi.tabla.push(desembolsoPlanificado);
				mi.tabla.push(desembolsoReal);
				mi.tabla.push(desembolsoReald);
				mi.tabla.push(variaciones);
				mi.tabla.push(porcentajeVariaciones);
				
				mi.generarEtiquetas(agrupacion,totalItems);
				mi.convertirMillones();
				break;
		}
		
		mi.tabla[4][totalItems+1] = mi.tabla[1][totalItems+1] - mi.tabla[2][totalItems+1];
		 
		
		mi.tabla[5][[totalItems+1]] = (mi.tabla[4][totalItems+1] != 0 ? 
				(mi.tabla[4][totalItems+1] / 
						(mi.tabla[4][totalItems+1]  > 0 ? mi.tabla[1][totalItems+1] : 
							mi.tabla[2][totalItems+1] ) * 100 ).toFixed(2) : "0") + "%";
		
		
	}
	
	mi.generarEtiquetas = function(agrupacion,totalItems){
		mi.etiqutas = [];
		mi.columnas = [];
		switch (agrupacion){
			case 1:
				if (totalItems == 12){
					mi.etiqutas = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
						"Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre",];
					mi.columnas.push ("Mes");
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}else if (totalItems > 12){
					var totalAnios = mi.anio_fin - mi.anio_inicio +1;
					mi.columnas.push ("Mes");
					for (x = mi.anio_inicio;x<=  mi.anio_fin;x++){
						
						mi.etiqutas.push ("Ene-" + x);
						mi.etiqutas.push ("Feb-" + x);
						mi.etiqutas.push ("Mar-" + x);
						mi.etiqutas.push ("Abr-" + x);
						mi.etiqutas.push ("May-" + x);
						mi.etiqutas.push ("Jun-" + x);
						mi.etiqutas.push ("Jul-" + x);
						mi.etiqutas.push ("Ago-" + x);
						mi.etiqutas.push ("Sep-" + x);
						mi.etiqutas.push ("Oct-" + x);
						mi.etiqutas.push ("Nov-" + x);
						mi.etiqutas.push ("Dic-" + x);
					}
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}
				break;
			case 2:
				if (totalItems == 6){
					mi.etiqutas = ["1", "2", "3", "4", "5", "6"];
					mi.columnas.push ("Semestre");
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}else if (totalItems > 6){
					var totalAnios = mi.anio_fin - mi.anio_inicio +1;
					mi.columnas.push ("Semestre");
					for (x = mi.anio_inicio;x<=  mi.anio_fin;x++){
						
						mi.etiqutas.push ("1-" + x);
						mi.etiqutas.push ("2-" + x);
						mi.etiqutas.push ("3-" + x);
						mi.etiqutas.push ("4-" + x);
						mi.etiqutas.push ("5-" + x);
						mi.etiqutas.push ("6-" + x);
					}
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}
			break;
			case 3:
				if (totalItems == 4){
					mi.etiqutas = ["1", "2", "3", "4"];
					mi.columnas.push ("Trimestre");
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Trimestre");
				}else if (totalItems > 4){
					var totalAnios = mi.anio_fin - mi.anio_inicio +1;
					mi.columnas.push ("Mes");
					for (x = mi.anio_inicio;x<=  mi.anio_fin;x++){
						
						mi.etiqutas.push ("1-" + x);
						mi.etiqutas.push ("2-" + x);
						mi.etiqutas.push ("3-" + x);
						mi.etiqutas.push ("4-" + x);
					}
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}
			break;
			case 4:
				if (totalItems == 3){
					mi.etiqutas = ["1", "2", "3"];
					mi.columnas.push ("Cuatrimestre");
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}else if (totalItems > 3){
					var totalAnios = mi.anio_fin - mi.anio_inicio +1;
					mi.columnas.push ("Cuatrimestre");
					for (x = mi.anio_inicio;x<=  mi.anio_fin;x++){
						
						mi.etiqutas.push ("1-" + x);
						mi.etiqutas.push ("2-" + x);
						mi.etiqutas.push ("3-" + x);
					}
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}
			break;
			case 5:
				if (totalItems == 2){
					mi.etiqutas = ["1", "2"];
					mi.columnas.push ("Semestre");
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}else if (totalItems > 2){
					var totalAnios = mi.anio_fin - mi.anio_inicio +1;
					mi.columnas.push ("Semestre");
					for (x = mi.anio_inicio;x<=  mi.anio_fin;x++){
						
						mi.etiqutas.push ("1-" + x);
						mi.etiqutas.push ("2-" + x);
					}
					mi.columnas.push(...mi.etiqutas);
					mi.columnas.push("Total");
				}
			break;
			case 6:
				
				for (x = mi.anio_inicio; x <= mi.anio_fin; x++){
					mi.etiqutas.push(x);
				}
				mi.columnas.push ("Año");
				mi.columnas.push(...mi.etiqutas);
				mi.columnas.push("Total");
				
			break;
		}
	};
	
	mi.agruparDatos = function(agrupacion){
		mi.asignarSerie2(mi.datosOrigniales.planificado, mi.datosOrigniales.real,mi.datosOrigniales.costos,mi.datosOrigniales.reald,agrupacion);
		
	};
	
	
	
	
	
	
	mi.abrirPopupFecha = function(index) {
		switch(index){
			case 1: mi.ef_abierto = true; break;
		}
	};
	
	
	
	 mi.formato1 = function (value) {
		 if (!isNaN(value))
			 return numeral(value).format('0,0.00')
		 else return value;
	 }
	 
	 mi.clase = function (value){
		 switch (value){
		 case 1: return "planificado";
		 case 2: return "real2";
		 default: return "";
		 }
	 }
	 
	 mi.esNumero = function(value){
		 return !isNaN(value);
	 }
	 
	 mi.NombrexAxis = function(value){
		 switch (value){
		 	case 1: mi.yAxisNombre="Mes"; break;
		 	case 2: mi.yAxisNombre="Bimestre"; break;
		 	case 3: mi.yAxisNombre="Semestre"; break;
		 	case 4: mi.yAxisNombre="Trimestre"; break;
		 	case 5: mi.yAxisNombre="Cuatrimestre"; break;
		 	case 1: mi.yAxisNombre="Semestre"; break;
		 	case 1: mi.yAxisNombre="Año"; break;
		 }
	 }
	 
	 
	 mi.convertirMillones = function(){
		 mi.desembolsosGrafica = [];
		 mi.desembolsosGrafica[0] = [];
		 mi.desembolsosGrafica[1] = [];
		
		 mi.desembolsosGrafica[0].push(...mi.tabla[1].slice(1,mi.tabla[1].length -1));
		 mi.desembolsosGrafica[1].push(...mi.tabla[2].slice(1,mi.tabla[2].length -1));
		 
		 for (y = 0; y<mi.datosOrigniales.planificado.length ; y++){
			 if (mi.enMillones){
				 mi.desembolsosGrafica[0][y] = mi.desembolsosGrafica[0][y] / 1000000;
				 mi.desembolsosGrafica[1][y] = mi.desembolsosGrafica[1][y] / 1000000;
			 }else
				 break;
		 }  
	 }
	 

		mi.exportarExcel = function(){
			 $http.post('/SDesembolsos', { 
				 accion: 'exportarExcel',
				 agrupacion: mi.agrupacion,
				 costo: JSON.stringify(mi.tabla[0].slice(0)),
				 planificado: JSON.stringify(mi.tabla[1].slice(0)),
				 real: JSON.stringify(mi.tabla[2].slice(0)),
				 realdolares: JSON.stringify(mi.tabla[3].slice(0)),
				 variacion: JSON.stringify(mi.tabla[4].slice(0)),
				 porcentaje:  JSON.stringify(mi.tabla[5].slice(0)),
				 headers: JSON.stringify(mi.columnas),
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'Desembolsos.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
			};
		
		mi.exportarPdf=function(){
			 $http.post('/SDesembolsos', { 
				 accion: 'exportarPdf', 
				 agrupacion: mi.agrupacion,
				 costo: JSON.stringify(mi.tabla[0].slice(0)),
				 planificado: JSON.stringify(mi.tabla[1].slice(0)),
				 real: JSON.stringify(mi.tabla[2].slice(0)),
				 realdolares: JSON.stringify(mi.tabla[3].slice(0)),
				 variacion: JSON.stringify(mi.tabla[4].slice(0)),
				 porcentaje:  JSON.stringify(mi.tabla[5].slice(0)),
				 headers: JSON.stringify(mi.columnas),
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/pdf;base64,' + response.data,
					         target: '_blank',
					         download: 'Desembolsos.pdf'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		};
		
		mi.validarParametros = function(){
			if(mi.anio_inicio != null && mi.anio_inicio.toString().length == 4 && 
					mi.anio_fin != null && mi.anio_fin.toString().length == 4)
			{

				if (mi.anio_inicio <= mi.anio_fin){
					
					mi.generarReporte();
				}else{
					$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}
			}
		}
	 
}]);
