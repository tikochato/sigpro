var app = angular.module('historia',[]);

app.factory('historia',['$mdDialog','$uibModal', '$http',
	function($mdDialog,$uibModal,$http){
	
	return{
		getHistoria: function($scope, titulo, servlet, id){
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
					}
			    }
			});
		},
		getHistoriaMatriz: function($scope, titulo, servlet, id){
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
					}
			    }
			});
		}
	}
}])

app.controller('historiaController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo','servlet', 'id', historiaController ]);

function historiaController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, titulo, servlet, id) {
	var mi = this;
	
	mi.objetoNombre = titulo;
	mi.mostrarCargando = true;
	mi.posicion = 0;
	mi.totalVersiones = 0;
	mi.versiones = [];
	
	$http.post(servlet, {
		accion: 'getCantidadHistoria',
		id: id,
		t: (new Date()).getTime()
	}).success(
		function(response){
			if(response.success){
				mi.versiones = response.versiones;
				mi.totalVersiones = mi.versiones.length;
				mi.cargarData(mi.versiones[0]);
				mi.desHabilitarBotones();
			}else{
				mi.mostrarCargando = false;
			}
		}
	);
	
	mi.cargarData = function(version){
		$http.post(servlet, {
			accion: 'getHistoria',
			id: id,
			version : version,
			t: (new Date()).getTime()
		}).success(
			function(response){
				if(response.success){
					mi.data = response.historia[0];
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
		mi.cargarData(mi.versiones[0]);
		mi.desHabilitarBotones();
	}
	
	mi.ultimo = function(){
		if(mi.totalVersiones > 0){
			mi.posicion = mi.totalVersiones - 1;
			mi.cargarData(mi.versiones[mi.posicion]);
			mi.desHabilitarBotones();
		}
	}
	
	mi.siguiente = function(){
		if(mi.totalVersiones > 0){
			if(mi.posicion != mi.totalVersiones - 1){
				mi.posicion = mi.posicion+1;
				mi.cargarData(mi.versiones[mi.posicion]);
				mi.desHabilitarBotones();
			}	
		}
	}
	
	mi.atras = function(){
		if(mi.posicion != 0){
			mi.posicion = mi.posicion-1;
			mi.cargarData(mi.versiones[mi.posicion]);
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
}

app.controller('historiaMatrizController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo','servlet', 'id', historiaMatrizController ]);

function historiaMatrizController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, titulo, servlet, id) {
	var mi = this;
	mi.mostrarCargando = false;
	mi.objetoNombre = titulo;
	mi.m_organismosEjecutores = [];
	mi.m_componentes = [];
	mi.posicion = 0;
	mi.versiones = [];
	mi.totalVersiones = 0;
//	$http.post(servlet, {
//		
//	}).success(
//		function(response){
//		if(response.success){
//			
//		}
//	});
	
	mi.m_componentes = [
		{"id":null,"nombre":"Catastro en áreas protegidas","tipoMoneda":null,"techo":17205000.00,"orden":1,"descripcion":null,
		"unidadesEjecutoras":[{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":1.7205E7,"donacion":0.0,"nacional":0.0,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}]},
		{"id":null,"nombre":"Consolidación de la certeza jurídica de las áreas protegidas","tipoMoneda":null,"techo":700000.00,"orden":2,"descripcion":null,
		"unidadesEjecutoras":[{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":700000.0,"donacion":0.0,"nacional":0.0,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}]},
		{"id":null,"nombre":"Soporte técnico, coordinación interinstitucional y uso de la información catastral y registral","tipoMoneda":null,"techo":2145000.00,"orden":3,"descripcion":null,
		"unidadesEjecutoras":[{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":2145000.0,"donacion":0.0,"nacional":0.0,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}]},
		{"id":null,"nombre":"Administración, supervisión y seguimiento del Programa","tipoMoneda":null,"techo":1820000.00,"orden":4,"descripcion":null,
		"unidadesEjecutoras":[{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":1820000.0,"donacion":0.0,"nacional":0.0,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}]},
		{"id":null,"nombre":"Auditoría financera externa","tipoMoneda":null,"techo":130000.00,"orden":5,"descripcion":null,
		"unidadesEjecutoras":[{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":130000.0,"donacion":0.0,"nacional":0.0,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}]},
		{"id":null,"nombre":"Contingencia","tipoMoneda":null,"techo":0.00,"orden":6,"descripcion":null,
		"unidadesEjecutoras":[{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":null,"donacion":null,"nacional":null,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}]}];
	
	
	mi.m_organismosEjecutores = [{"id":0,"nombre":"OBLIGACIONES DEL ESTADO A CARGO DEL TESORO","entidad":"11130018","entidadId":11130018,"ejercicio":2017,"prestamo":null,"donacion":null,"nacional":null,"esCoordinador":0,"fechaElegibilidad":null,"fechaCierre":null}];
	
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
	
	mi.cerrar = function() {
		$uibModalInstance.close(false);
	};
	
	mi.inicio = function(){
		
	}
	
	mi.ultimo = function(){
		if(mi.totalVersiones > 0){
			
		}
	}
	
	mi.siguiente = function(){
		if(mi.totalVersiones > 0){
			
		}
	}
	
	mi.atras = function(){
		if(mi.posicion != 0){
			
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
	
	mi.actualizarTotalesUE();
}
