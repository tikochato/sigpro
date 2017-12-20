var app = angular.module('avanceActividadesController',['ngAnimate', 'ngTouch','smart-table','angularjs-dropdown-multiselect']);

app.filter('calculatePercentage', function () {
	  return function (input, resultField, row) {
		  if(row.tipo == 2){
			  return Math.floor(input) + "%";
		  }else
			  return Math.floor(input);
	  };
	});

app.controller('avanceActividadesController',['$scope','$rootScope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$window','$q','$uibModal',
	function($scope, $rootScope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$window, $q,$uibModal){
		var mi = this;
		mi.mostrarCargando = false;
		mi.mostrardiv=false;
		
		mi.pepId=null;
		mi.prestamoId=null;
		
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
		
		mi.totalActividades = 0;
		mi.totalActividadesCompletadas = 0;
		mi.totalActividadesSinIniciar = 0;
		mi.totalActividadesProceso = 0;
		mi.totalActividadesRetrasadas = 0;
		
		mi.totalHitos = 0;
		mi.totalHitosCompletados = 0;
		mi.totalHitosSinIniciar = 0;
		mi.totalHitosRetrasados = 0;
		
		mi.totalProductos = 0;
		mi.totalHitos = 0;
		mi.lineasBase = [];
		
		mi.calcularTamanosPantalla = function(){
			mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
			mi.tamanoSemaforo = mi.tamanoPantalla * 0.05;
			mi.tamanoNombres = (mi.tamanoPantalla - mi.tamanoSemaforo) * 0.20;
			mi.tamanoColPorcentajes = (mi.tamanoPantalla - mi.tamanoNombres - mi.tamanoSemaforo) / 6;
			mi.tamanoColPorcentajesHitos = (mi.tamanoPantalla - mi.tamanoNombres - mi.tamanoSemaforo) / 5;
		}
		
		mi.calcularTamanosPantalla();
		
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
		};
		
		mi.cambioPep=function(selected){
			if(selected!== undefined){
				mi.pepNombre = selected.originalObject.nombre;
				mi.pepId = selected.originalObject.id;
				$scope.$broadcast('angucomplete-alt:clearInput', 'lineaBase');
				mi.getLineasBase(mi.pepId);
			}
			else{
				mi.pepNombre="";
				mi.pepId=null;
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
				mi.validarFecha(mi.fechaCorte);
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
		}
		
		mi.getLineasBase = function(proyectoId){
			$http.post('/SProyecto',{accion: 'getLineasBase', proyectoId: proyectoId}).success(
				function(response) {
					mi.lineasBase = [];
					if (response.success){
						mi.lineasBase = response.lineasBase;
					}
			});	
		}
		
		mi.validarFecha = function(fecha1){
			if(fecha1 != null && mi.pepId != null )
				mi.generar();
		}
				
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
			}
		};
		
		mi.generar = function(){
			if(mi.pepId != 0 ){
				if(mi.fechaCorte != null){
					mi.mostrardiv = false;
					mi.rowCollectionActividades = [];
					mi.rowCollectionHitos = [];
					mi.rowProductos = [];
					mi.displayedCollectionActividades = [];
					mi.displayedCollectionHitos = [];
					mi.displayedProductos = [];
					
					mi.totalActividades = 0;
					mi.totalActividadesCompletadas = 0;
					mi.totalActividadesSinIniciar = 0;
					mi.totalActividadesProceso = 0;
					mi.totalActividadesRetrasadas = 0;
					
					mi.totalHitos = 0;
					mi.totalHitosCompletados = 0;
					mi.totalHitosSinIniciar = 0;
					mi.totalHitosRetrasados = 0;
					
					mi.totalProductos = 0;
					
					mi.mostrarHitos = false;
					mi.mostrarActProyecto = false;
					mi.mostrarCargando = true;
					$http.post('/SAvanceActividades', {
						accion: 'getAvance',
						idPrestamo: mi.pepId,
						lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
						fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY')
					}).success(function(response){
						if (response.success){
							mi.dataPieProyecto = [];
							mi.labelsPieProyecto = [];
							mi.dataBarraProyecto = [];
							
							mi.dataPieHitos = [];
							mi.labelsPieHitos = [];
							mi.dataBarraHitos  = [];
							
							mi.labelsPieProyecto = ["Completadas", "Sin Iniciar", "En Proceso", "Retrasadas"];
							mi.labelsBarProyecto = [moment(mi.fechaCorte).format('YYYY')]
							mi.labelsPieHitos = [moment(mi.fechaCorte).format('YYYY')];
							mi.seriesBarHitos = ['Completadas', 'Sin Iniciar', 'Retrasadas']
							
							mi.totalActividadesCompletadas = 0;
							mi.totalActividadesSinIniciar = 0;
							mi.totalActividadesProceso = 0;
							mi.totalActividadesRetrasadas = 0;
							mi.totalActividadesEsperadas = 0;
							mi.totalActividadesAnioSiguientes = 0;
							
							mi.totalHitosCompletados = 0;
							mi.totalHitosSinIniciar = 0;
							mi.totalHitosRetrasados = 0;
							mi.totalHitosEsperados = 0;
							mi.totalHitosAnioSiguientes = 0;
							
							if(response.actividades != undefined){
								mi.rowCollectionActividades = response.actividades;
								mi.displayedCollectionActividades = [].concat(mi.rowCollectionActividades);
																
								mi.dataPieProyecto = [response.actividades[0].completadas, response.actividades[0].sinIniciar, response.actividades[0].proceso, response.actividades[0].retrasadas];
								
								mi.mostrarActProyecto = true;
							}
							
							mi.totalActividades = response.totalActividades;
							if(response.cantidadesActividades != undefined){
								mi.totalActividadesCompletadas = response.cantidadesActividades[0].completadas;
								mi.totalActividadesSinIniciar = response.cantidadesActividades[0].sinIniciar;
								mi.totalActividadesProceso = response.cantidadesActividades[0].proceso;
								mi.totalActividadesRetrasadas = response.cantidadesActividades[0].retrasadas;
								mi.totalActividadesEsperadas = response.cantidadesActividades[0].esperadasfinanio;
								mi.totalActividadesAnioSiguientes = response.cantidadesActividades[0].aniosiguientes;
								
								mi.dataBarraProyecto = [[mi.totalActividadesCompletadas],[mi.totalActividadesSinIniciar],[mi.totalActividadesProceso],[mi.totalActividadesRetrasadas]];
							}
							
							if(response.hitos != undefined){
								mi.rowCollectionHitos = response.hitos;
								mi.displayedCollectionHitos = [].concat(mi.rowCollectionHitos);
																
								mi.dataPieHitos = [response.hitos[0].completadas, response.hitos[0].sinIniciar, response.hitos[0].retrasadas];
								
								mi.mostrarHitos = true;
							}
							
							mi.totalHitos = response.totalHitos;
							
							if(response.cantidadHitos != undefined){
								mi.totalHitosCompletados = response.cantidadHitos[0].completadas;
								mi.totalHitosSinIniciar = response.cantidadHitos[0].sinIniciar;
								mi.totalHitosRetrasados = response.cantidadHitos[0].retrasadas;
								mi.totalHitosEsperados = response.cantidadHitos[0].esperadasfinanio;
								mi.totalHitosAnioSiguientes = response.cantidadHitos[0].aniosiguientes;
								
								mi.dataBarraHitos = [[mi.totalHitosCompletados], [mi.totalHitosSinIniciar], [mi.totalHitosRetrasados]];
							}

							if(response.productos != undefined){
								mi.rowProductos = response.productos;
								mi.displayedProductos = [].concat(mi.rowProductos);	
							}
							
							mi.totalProductos = response.totalProductos;
							
							mi.mostrarCargando = false;
							mi.mostrardiv=true;
						}else
							mi.mostrarCargando = false;
					});
				}
			}
		}
		
		mi.optionsPieProyecto = {
			legend: {
				display: true,
				position: 'bottom'
			},
			pieceLabel: {
			    render: 'percentage',
			    fontColor: ['green', 'white', 'red'],
			    precision: 2
			  }
		};
		
		mi.charOptions= {
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
	                       labelString: 'Actividades'
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
		
		mi.charOptionsHitos= {
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
	                       labelString: 'Hitos'
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
		
		mi.pieColors = ['#8ecf4c','#88b4df','#ffff4d','#ff4d4d'];
		mi.pieColorsHitos = ['#8ecf4c','#88b4df','#ff4d4d'];
		
		mi.obtenerColor = function(row){
			var style={}
			if(row.completadas >= 0 && row.completadas <= 40){
				style.color='red'
			}else if(row.completadas > 40 && row.completadas <= 60){
				style.color='yellow'
			}else if(row.completadas > 60 && row.completadas <= 100){
				style.color='green'
			}
			return style;
		}
		
		angular.element($window).bind('resize', function(){ 
            mi.calcularTamanosPantalla();
            $scope.$digest();
        });
		
		$scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
		
		mi.mostrarActividades = function(row){
			mi.llamarModalActividades(row);
		}
		
		mi.llamarModalActividades = function(objetoRow) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'objetoavance.jsp',
				controller : 'modalAvance',
				controllerAs : 'controller',
				backdrop : 'static',
				size : 'lg',
				resolve : {
					objetoRow : function(){
						return objetoRow;
					},
					fechaCorte : function(){
						return mi.fechaCorte
					},
					lineaBase: function(){
						return mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null;
					}
				}
			});
			
			modalInstance.result.then(function(itemSeleccionado) {
				resultado.resolve(itemSeleccionado);
			});
			return resultado.promise;
		};
		
		mi.exportarExcel = function(){
			$http.post('/SAvanceActividades', { 
				accion: 'exportarExcel', 	
				idPrestamo: mi.pepId,
				fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY'),
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
				t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'ReporteAvances.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
			};
			
		mi.exportarPdf=function(){
			$http.post('/SAvanceActividades', { 
				accion: 'exportarPdf', 	
				idPrestamo: mi.pepId,
				fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY'),
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
				t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/pdf;base64,' + response.data,
					         target: '_blank',
					         download: 'ReporteAvances.pdf'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		};
}]);

app.controller('modalAvance', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'objetoRow','fechaCorte','$rootScope', 'lineaBase', modalAvance ]);

function modalAvance($uibModalInstance, $scope, $http, $interval,i18nService, $utilidades, $timeout, $log, objetoRow, fechaCorte, $rootScope, lineaBase) {
	var mi = this;	
	
	if(objetoRow.objetoTipo == 1){
		mi.mostrarcargando = true;
		mi.nombre = "Actividades de "+$rootScope.etiquetas.proyecto;
		$http.post('/SAvanceActividades', {
			accion: 'getActividadesProyecto',
			idPrestamo: objetoRow.objetoId,
			fechaCorte: moment(fechaCorte).format('DD/MM/YYYY'),
			lineaBase: lineaBase,
			t:moment().unix()
		}).success(function(response){
			if (response.success){
				mi.items = response.items;
				mi.displayedItems = [].concat(mi.items);	
				mi.mostrarcargando = false;
			}
		});
	}else if(objetoRow.objetoTipo == 10){
		mi.mostrarcargando = true;
		mi.nombre = "Hitos de "+$rootScope.etiquetas.proyecto;
		$http.post('/SAvanceActividades', {
			accion: 'getHitos',
			lineaBase: lineaBase,
			idPrestamo: objetoRow.objetoId,
			fechaCorte: moment(fechaCorte).format('DD/MM/YYYY'),
			t:moment().unix()
		}).success(function(response){
			if (response.success){
				mi.items = response.items;
				mi.displayedItems = [].concat(mi.items);	
				mi.mostrarcargando = false;
			}
		});
	}else if(objetoRow.objetoTipo == 3){
		mi.mostrarcargando = true;
		mi.nombre = "Actividades de producto: " + objetoRow.nombre;
		$http.post('/SAvanceActividades', {
			accion: 'getActividadesProducto',
			lineaBase: lineaBase,
			productoId: objetoRow.objetoId,
			fechaCorte: moment(fechaCorte).format('DD/MM/YYYY'),
			t:moment().unix()
		}).success(function(response){
			if (response.success){
				mi.items = response.items;
				mi.displayedItems = [].concat(mi.items);	
				mi.mostrarcargando = false;
			}
		});
	}
	
	mi.obtenerColor = function(row){
		var style={}
		if(row.avance >= 0 && row.avance <= 40){
			style.color='red'
		}else if(row.avance > 40 && row.avance <= 60){
			style.color='yellow'
		}else if(row.avance > 60 && row.avance <= 100){
			style.color='green'
		}
		return style;
	}
	
	mi.exportarDetalleExcel = function(){
		if(objetoRow.objetoTipo == 1){
			$http.post('/SAvanceActividades', {
				accion: 'exportarDetalleExcel',
				id: objetoRow.objetoId,
				fechaCorte: moment(fechaCorte).format('DD/MM/YYYY'),
				objetoTipo: 1,
				t:moment().unix()
			}).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'DetalleAvancePep.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		}else if(objetoRow.objetoTipo == 10){
			$http.post('/SAvanceActividades', {
				accion: 'exportarDetalleExcel',
				id: objetoRow.objetoId,
				fechaCorte: moment(fechaCorte).format('DD/MM/YYYY'),
				objetoTipo: 10,
				t:moment().unix()
			}).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'DetaleAvanceHitos.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		}else if(objetoRow.objetoTipo == 3){
			$http.post('/SAvanceActividades', {
				accion: 'exportarDetalleExcel',
				id: objetoRow.objetoId,
				fechaCorte: moment(fechaCorte).format('DD/MM/YYYY'),
				objetoTipo: 3,
				t:moment().unix()
			}).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'DetalleAvanceProductos.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
		}
	}
	
	mi.ok = function() {
		
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
};
