var app = angular.module('historia',[]);

app.factory('historia',['$mdDialog','$uibModal', '$http',
	function($mdDialog,$uibModal,$http){
	
	return{
		getHistoria: function($scope, titulo, servlet, id, objeto_tipo, adquisiciones, riesgos, desembolsos, metas){
			return $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : '/app/components/historia/historia.jsp',
			    controller: 'historiaController',
			    controllerAs: 'modalh',
			    size : 'lg',
			    backdrop : 'static',
			    scope: $scope,
			    resolve : {
			    	titulo : function() {
						return titulo;
					},
					servlet : function(){
						return servlet;
					},
					id : function(){
						return id;
					},
					objeto_tipo : function(){
						return objeto_tipo;
					},
					adquisiciones: function(){
						return adquisiciones;
					},
					riesgos: function(){
						return riesgos; 
					},
					desembolsos: function(){
						return desembolsos;
					},
					metas: function(){
						return metas;
					}
			    }
			});
		},
		getHistoriaMatriz: function($scope, titulo, servlet, id, codigoPresupuestario){
			return $uibModal.open({
				animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : '/app/components/historia/historia_matriz.jsp',
			    controller: 'historiaMatrizController',
			    controllerAs: 'modalh',
			    size : 'lg',
			    backdrop : 'static',
			    scope: $scope,
			    resolve : {
			    	titulo : function() {
						return titulo;
					},
					servlet : function(){
						return servlet;
					},
					id : function(){
						return id;
					},
					codigo_presupuestario : function(){
						return codigoPresupuestario;
					}
			    }
			});
		}
	}
}])

app.controller('historiaController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo','servlet', 'id', 'objeto_tipo', 'adquisiciones', 'riesgos', 'desembolsos', 'metas', historiaController ]);

function historiaController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, 
		titulo, servlet, id, objeto_tipo, adquisiciones, riesgos, desembolsos, metas) {
	var mi = this;
	
	mi.objetoNombre = titulo;
	mi.mostrarCargando = true;
	mi.posicion = 0;
	mi.totalVersiones = 0;
	mi.versiones = [];
	mi.adquisiciones = adquisiciones;
	mi.riesgos = riesgos;
	mi.desembolsos = desembolsos;
	mi.metas = metas;
	mi.id = id;
	mi.objetoTipo = objeto_tipo;
	mi.servletGeneral = servlet;
	mi.servlet = servlet;
	
	mi.adquisicionesClick = function(){
		mi.posicion = 0;
		mi.tipo = 1;
		mi.mostrarCargando = true;
		mi.servlet = '/SPlanAdquisicion';
		$http.post(mi.servlet, {
			accion: 'getCantidadHistoria',
			id: mi.id,
			objetoTipo : mi.objetoTipo,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.versiones = response.versiones;
					mi.totalVersiones = mi.versiones.length;
					mi.cargarData(mi.versiones[0], mi.tipo);
					mi.desHabilitarBotones();
				}
			}
		);
	}
	
	mi.riesgosClick = function(){
		mi.posicion = 0;
		mi.tipo = 2;
		mi.mostrarCargando = true;
		mi.servlet = '/SRiesgo';
		$http.post(mi.servlet, {
			accion: 'getCantidadHistoria',
			id: mi.id,
			objetoTipo : mi.objetoTipo,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.versiones = response.versiones;
					mi.totalVersiones = mi.versiones.length;
					mi.cargarData(mi.versiones[0], mi.tipo);
					mi.desHabilitarBotones();
				}
			}
		);
	}
	
	mi.desembolsosClick = function(){
		mi.posicion = 0;
		mi.tipo = 2;
		mi.mostrarCargando = true;
		mi.servlet = '/SDesembolso';
		$http.post(mi.servlet, {
			accion: 'getCantidadHistoria',
			id: mi.id,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.versiones = response.versiones;
					mi.totalVersiones = mi.versiones.length;
					mi.cargarData(mi.versiones[0], mi.tipo);
					mi.desHabilitarBotones();
				}
			}
		);
	}
	
	mi.metasClick = function(){
		mi.posicion = 0;
		mi.tipo = 2;
		mi.mostrarCargando = true;
		mi.servlet = '/SMeta';
		$http.post(mi.servlet, {
			accion: 'getCantidadHistoria',
			id: mi.id,
			objetoTipo : mi.objetoTipo,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.versiones = response.versiones;
					mi.totalVersiones = mi.versiones.length;
					mi.cargarData(mi.versiones[0], mi.tipo);
					mi.desHabilitarBotones();
				}
			}
		);
	}
	
	mi.generalClick = function(){
		mi.posicion = 0;
		mi.tipo = 1;
		mi.mostrarCargando = true;
		mi.servlet = mi.servletGeneral;
		$http.post(servlet, {
			accion: 'getCantidadHistoria',
			id: id,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.versiones = response.versiones;
					mi.totalVersiones = mi.versiones.length;
					mi.cargarData(mi.versiones[0], mi.tipo);					
					mi.desHabilitarBotones();
				}else{
					mi.mostrarCargando = false;
				}
			}
		);
	}
	
	mi.cargarData = function(version, tipo){
		mi.data = [];
		$http.post(mi.servlet, {
			accion: 'getHistoria',
			id: id,
			objetoTipo : mi.objetoTipo,
			version : version,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					if(tipo==2){
						mi.cabeceras = [];
						for(x in response.historia[0]){
							mi.cabeceras.push(response.historia[0][x].nombre);
						}
					}
					mi.data = tipo != 2 ? response.historia[0] : response.historia;
					mi.displayedItems = [].concat(mi.data);
					mi.mostrarCargando = false;
				}else{
					mi.mostrarCargando = false;
				}
			}
		);
	}
	
	mi.cerrar = function() {
		$uibModalInstance.close(false);
	};
	
	mi.inicio = function(){
		mi.posicion = 0;
		mi.cargarData(mi.versiones[0], mi.tipo);
		mi.desHabilitarBotones();
	}
	
	mi.ultimo = function(){
		if(mi.totalVersiones > 0){
			mi.posicion = mi.totalVersiones - 1;
			mi.cargarData(mi.versiones[mi.posicion], mi.tipo);
			mi.desHabilitarBotones();
		}
	}
	
	mi.siguiente = function(){
		if(mi.totalVersiones > 0){
			if(mi.posicion != mi.totalVersiones - 1){
				mi.posicion = mi.posicion+1;
				mi.cargarData(mi.versiones[mi.posicion], mi.tipo);
				mi.desHabilitarBotones();
			}	
		}
	}
	
	mi.atras = function(){
		if(mi.posicion != 0){
			mi.posicion = mi.posicion-1;
			mi.cargarData(mi.versiones[mi.posicion], mi.tipo);
			mi.desHabilitarBotones();
		}
	}
	
	mi.desHabilitarBotones = function(){
		if(mi.posicion == 0)
			mi.disabledInicio=true;
		else
			mi.disabledInicio=false;
		
		if(mi.posicion == mi.totalVersiones -1)
			mi.disabledFin = true;
		else
			mi.disabledFin = false;
		
		if(mi.totalVersiones == 1){
			mi.disabledInicio=true;
			mi.disabledFin = true;
		}
	}
	
	mi.generalClick();
}

app.controller('historiaMatrizController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo','servlet', 'id', 'codigo_presupuestario', historiaMatrizController ]);

function historiaMatrizController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, titulo, servlet, id, codigoPresupuestario) {
	var mi = this;
	mi.mostrarCargando = true;
	mi.objetoNombre = titulo;
	mi.m_organismosEjecutores = [];
	mi.m_componentes = [];
	mi.posicion = 0;
	mi.versiones = [];
	mi.totalVersiones = 0;

	$http.post(servlet, {
		accion: 'getVersionesMatriz',
		prestamoId: id,
		codigoPresupuestario: codigoPresupuestario,
		t: (new Date()).getTime()
	}).success(
		function(response){
		if(response.success){
			mi.versiones = response.versiones;
			mi.totalVersiones = mi.versiones.length;
			if(mi.totalVersiones > 0){
				mi.cargarData(mi.versiones[0]);
				mi.desHabilitarBotones();
			}else
				mi.mostrarCargando = false;
		}
	});
	
	mi.actualizarTotalesUE = function(){
		for (x in mi.m_componentes){
			var  totalUnidades = 0;
			var totalAsignado = 0;
			for (j in mi.m_componentes[x].unidadesEjecutoras){
				totalUnidades = totalUnidades + mi.m_componentes[x].unidadesEjecutoras[j].prestamo;
			}
			totalAsignado = totalUnidades;
			mi.totalIngresado = mi.totalIngresado + totalUnidades;
			mi.matriz_valid = mi.matriz_valid==1 &&  totalUnidades == mi.m_componentes[x].techo ? 1 : null;
 
			mi.m_componentes[x].totalIngesado = totalAsignado;
		}
	}
	
	mi.cargarData = function(version){
		$http.post(servlet, {
			accion: 'getHistoriaMatriz',
			version: version,
			prestamoId: id,
			codigoPresupuestario: codigoPresupuestario,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.m_organismosEjecutores = response.unidadesEjecutoras;
					mi.m_componentes = response.componentes;
					mi.actualizarTotalesUE();
					mi.mostrarCargando = false;
				}else{
					mi.mostrarCargando = false;
				}
			}
		);
	}
	
	mi.cerrar = function() {
		$uibModalInstance.close(false);
	};
	
	mi.inicio = function(){
		mi.inicializar();
		mi.mostrarCargando = true;
		mi.posicion = 0;
		mi.cargarData(mi.versiones[0]);
		mi.desHabilitarBotones();
	}
	
	mi.ultimo = function(){
		if(mi.totalVersiones > 0){
			mi.inicializar();
			mi.mostrarCargando = true;
			mi.posicion = mi.totalVersiones - 1;
			mi.cargarData(mi.versiones[mi.posicion]);
			mi.desHabilitarBotones();
		}
	}
	
	mi.siguiente = function(){
		if(mi.totalVersiones > 0){
			if(mi.posicion != mi.totalVersiones - 1){
				mi.inicializar();
				mi.mostrarCargando = true;
				mi.posicion = mi.posicion+1;
				mi.cargarData(mi.versiones[mi.posicion]);
				mi.desHabilitarBotones();
			}
		}
	}
	
	mi.atras = function(){
		if(mi.posicion != 0){
			mi.inicializar();
			mi.mostrarCargando = true;
			mi.posicion = mi.posicion-1;
			mi.cargarData(mi.versiones[mi.posicion]);
			mi.desHabilitarBotones();
		}
	}
	
	mi.desHabilitarBotones = function(){
		if(mi.totalVersiones == 0){
			mi.disabledInicio = true;
			mi.disabledFin = true;
		}else{
			if(mi.posicion == 0)
				mi.disabledInicio=true;
			else
				mi.disabledInicio=false;
			
			if(mi.posicion == mi.totalVersiones -1)
				mi.disabledFin = true;
			else
				mi.disabledFin = false;
			
			if(mi.totalVersiones == 1){
				mi.disabledInicio=true;
				mi.disabledFin = true;
			}	
		}
	}
	
	mi.inicializar = function(){
		mi.m_componentes = [];
	}
}
