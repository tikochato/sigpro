var app = angular.module('administracionTransaccionalController',['ngTouch','smart-table']);
app.controller('administracionTransaccionalController',['$scope', '$http', '$interval','Utilidades','i18nService','$window',
	function($scope, $http, $interval, $utilidades,i18nService,$window){
		var mi = this;
		
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
		
		mi.mostrarcargando = true;
		mi.mostrarTablas = false;
		mi.monstrarDiv = true;
		$http.post('/SAdministracionTransaccional', {accion: 'getComponentes'}).success(
			function(response){
				mi.rowDatos = response.usuarios;
				mi.displayedDatos = [].concat(mi.rowDatos);
				
				mi.totalCreados = 0;
				mi.totalActualizados = 0;
				mi.totalEliminados = 0;
				for(x in mi.rowDatos){
					mi.totalCreados += mi.rowDatos[x].creados;
					mi.totalActualizados += mi.rowDatos[x].actualizados;
					mi.totalEliminados += mi.rowDatos[x].eliminados;
				}
				
				mi.series = ['Datos'];
				mi.datos = [mi.totalCreados,mi.totalActualizados,mi.totalEliminados];
				mi.labels = [];
				mi.labels= ['Creados', 'Actualizados', 'Eliminados']
				mi.data = mi.datos;
				
				mi.mostrarcargando = false;
				mi.mostrarTablas = true;
			});
		
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
			mi.tamanoCelda = mi.tamanoPantalla / 4;
			mi.anchoPantalla = Math.floor(document.getElementById("reporte").offsetHeight);
		}
		
		angular.element($window).bind('resize', function(){ 
            mi.calcularTamanosPantalla();
            $scope.$digest();
        });
		
		mi.calcularTamanosPantalla();
}]);