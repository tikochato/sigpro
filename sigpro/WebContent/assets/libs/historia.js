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