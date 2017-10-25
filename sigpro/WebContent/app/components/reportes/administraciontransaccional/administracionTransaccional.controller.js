var app = angular.module('administracionTransaccionalController',['ngTouch','smart-table']);
app.controller('administracionTransaccionalController',['$scope', '$http', '$interval','Utilidades','i18nService','$window',
	function($scope, $http, $interval, $utilidades,i18nService,$window){
		var mi = this;
		
		mi.mostrarGrafica = false;
		mi.nombres = function(row){
			if(row.nivel == 0)
				return "etiquetaNombre";
		}
		
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		};
		
		mi.validarFecha = function(fecha1, fecha2){
			if(fecha1 != null && fecha2 != null)
				if(fecha2 > fecha1)
					mi.generar();
				else
					$utilidades.mensaje('warning','La fecha inicial es mayor que la fecha final');
		}
		
		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2050, 12, 31),
				minDate : new Date(1990, 1, 1),
				startingDay : 1
		};
		
		mi.charOptions= {
			scales: {
				legend: {
					display: true,
					position: 'bottom'
				},
				yAxes: [
					{	
						id: 'y-axis-1',
						type: 'linear',
						display: true,
						position: 'left',
						ticks: {
							callback: function (value) {
								return numeral(value).format('0')
                        	}
	                   },
	                   scaleLabel: {
	                       display: true,
	                       labelString: 'Cantidad de transacciones'
	                   }
			        	
			        }
				],
				xAxes: [{
			    	  scaleLabel: {
	                       display: true,
	                       labelString: 'Estado'
	                     }
			      }
			      ]
			}
		};
		
		mi.generar = function(){
			mi.mostrarcargando = true;
			mi.mostrarTablas = true;
			mi.mostrarGrafica = false;
			mi.displayedDatos = [];
			mi.datos = [];
			mi.totalCreados = mi.totalActualizados = mi.totalEliminados = 0;
			$http.post('/SAdministracionTransaccional', {accion: 'getTransacciones', fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'), fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'), t: new Date().getTime()}).success(
					function(response){
						mi.rowDatos = response.usuarios;
						mi.displayedDatos = [].concat(mi.rowDatos);
						
						mi.totalCreados = 0;
						mi.totalActualizados = 0;
						mi.totalEliminados = 0;
						for(x in mi.rowDatos){
							mi.totalCreados += mi.rowDatos[x].transacciones.creados;
							mi.totalActualizados += mi.rowDatos[x].transacciones.actualizados;
							mi.totalEliminados += mi.rowDatos[x].transacciones.eliminados;
						}
						
						mi.generarGrafica(response.creados[0].anios, response.actualizados[0].anios, response.eliminados[0].anios);
						
						mi.series = ['Datos'];
						mi.datos = [mi.totalCreados,mi.totalActualizados,mi.totalEliminados];
						mi.labels = [];
						mi.labels= ['Creados', 'Actualizados', 'Eliminados']
						mi.data = mi.datos;
						
						mi.mostrarcargando = false;
						mi.mostrarGrafica = true;
					});
		}
		
		mi.totalesPorUsuario = function(row){
			mi.datos = [row.creados, row.actualizados, row.eliminados];
			mi.data = mi.datos;
		}
		
		mi.totalesGenerales = function(){
			mi.datos = [mi.totalCreados,mi.totalActualizados,mi.totalEliminados];
			mi.data = mi.datos;
		}
		
		mi.calcularTamanosPantalla = function(){
			mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
			mi.tamanoCelda = (mi.tamanoPantalla -300) / 4;
			mi.anchoPantalla = Math.floor(document.getElementById("reporte").offsetHeight);
		}
		
		angular.element($window).bind('resize', function(){ 
            mi.calcularTamanosPantalla();
            $scope.$digest();
        });		
		$scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
		
		mi.calcularTamanosPantalla();
		
		mi.exportarExcel = function(){
			$http.post('/SAdministracionTransaccional', { 
				 accion: 'exportarExcel', 
				 fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'), 
				 fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'AdministracionTransaccional.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
			};
			
			mi.descargarExcelDetalle = function(row){
				$http.post('/SAdministracionTransaccional', {
					accion: 'exportarExcelDetalle',
					proyectoId: row.objeto_id,
					proyectoNombre: row.nombre,
					fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'), 
					fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
					t:moment().unix()
				}).then(
						  function successCallback(response) {
							  var anchor = angular.element('<a/>');
							  anchor.attr({
						         href: 'data:application/ms-excel;base64,' + response.data,
						         target: '_blank',
						         download: 'AdministracionTransaccional_' + row.nombre +'_' + moment(mi.fechaInicio).format('DD/MM/YYYY') + '_al_' + moment(mi.fechaFin).format('DD/MM/YYYY') + '_.xls'
							  })[0].click();
						  }.bind(this), function errorCallback(response){
					 	}
				  	);
			}
			
		mi.exportarPdfDetalle=function(row){
			$http.post('/SAdministracionTransaccional', { 
				 accion: 'exportarPdfDetalle', 
				 fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'), 
				 fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
				 proyectoId: row.objeto_id,
				 proyectoNombre: row.nombre,
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/pdf;base64,' + response.data,
					         target: '_blank',
					         download: 'AdministracionTransaccional_' + row.nombre +'_' + moment(mi.fechaInicio).format('DD/MM/YYYY') + '_al_' + moment(mi.fechaFin).format('DD/MM/YYYY') + '_.pdf'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		};
			
		mi.exportarPdf=function(){
			$http.post('/SAdministracionTransaccional', { 
				 accion: 'exportarPdf', 
				 fechaInicio: moment(mi.fechaInicio).format('DD/MM/YYYY'), 
				 fechaFin: moment(mi.fechaFin).format('DD/MM/YYYY'),
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/pdf;base64,' + response.data,
					         target: '_blank',
					         download: 'AdministracionTransaccional.pdf'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		};
		
		mi.arregloMeses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sept", "Oct", "Nov", "Dic"]; 
		mi.radarColors = ['#8ecf4c', '#88b4df', '#ff3333'];
		
		mi.generarGrafica = function(arregloCreadas, arregloActualizadas, arregloEliminadas){
			mi.labelsMeses = [];
			mi.seriesLineales = [];
			mi.dataLineales = [];
			mi.seriesLineales = ['Creados', 'Actualizados', 'Eliminados'];
			
			mi.dataLinealesCreados = [];
			mi.dataLinealesActualizadas = [];
			mi.dataLinealesEliminadas = [];
			
			for(var i=0; i<arregloCreadas.length; i++){
				for(var j=0; j<=11; j++){
					mi.labelsMeses.push(mi.arregloMeses[j] + "-" + arregloCreadas[i].anio);
				}
			}
			
			for(var i=0;i<arregloCreadas.length; i++){
				for(var j=0; j<=11; j++){
					mi.dataLinealesCreados.push(arregloCreadas[i].mes[j]);
					mi.dataLinealesActualizadas.push(arregloActualizadas[i].mes[j]);
					mi.dataLinealesEliminadas.push(arregloEliminadas[i].mes[j]);
				}
			}
			
			mi.dataLineales.push(mi.dataLinealesCreados);
			mi.dataLineales.push(mi.dataLinealesActualizadas);
			mi.dataLineales.push(mi.dataLinealesEliminadas);
		}
		
		mi.charOptionsLineales= {
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
									return numeral(value).format('0')
	                        	}
		                   },
		                   scaleLabel: {
		                       display: true,
		                       labelString: 'Cantidad de transacciones'
		                   }
				        	
				        }
					],
					xAxes: [{
				    	  scaleLabel: {
		                       display: true,
		                       labelString: 'Meses'
		                     }
				      }
				      ]
				}
			};
		
}]);

app.directive('stFilteredCollection', function () {
	  return {
	    require: '^stTable',
	    link: function (scope, element, attr, ctrl) {
	      scope.$watch(ctrl.getFilteredCollection, function(val) {
	        scope.filteredCollection = val;
	        
	        scope.controller.totalCreados = 0;
	        scope.controller.totalActualizados = 0;
	        scope.controller.totalEliminados = 0;
	        
	        for(x in val){
	        	scope.controller.totalCreados = scope.controller.totalCreados + val[x].creados;
	        	scope.controller.totalActualizados = scope.controller.totalActualizados + val[x].actualizados;
	        	scope.controller.totalEliminados = scope.controller.totalEliminados + val[x].eliminados;
	        }
	      })
	    }
	  }
	});