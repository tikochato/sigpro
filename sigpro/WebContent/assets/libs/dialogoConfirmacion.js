var app = angular.module('dialogoConfirmacion',[]);

app.factory('dialogoConfirmacion',['$mdDialog','$uibModal', '$http',
	function($mdDialog,$uibModal,$http){
	
	return{
		abrirDialogoConfirmacion: function($scope, titulo, textoCuerpo, textoBotonOk, textoBotonCancelar){
			return $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : '/app/components/dialogoconfirmacion/dialogoConfirmacion.jsp',
			    controller: 'dialogoConfirmacionController',
			    controllerAs: 'dialogoCtrl',
			    size : 'md',
			    backdrop : 'static',
			    scope: $scope,
			    resolve : {
			    	titulo : function() {
						return titulo;
					},
					textoCuerpo: function(){
						return textoCuerpo;
					},
					textoBotonOk: function(){
						return textoBotonOk;
					},
					textoBotonCancelar: function(){
						return textoBotonCancelar;
					}
			    }

			});
		},
		
		
	}
}])

app.controller('dialogoConfirmacionController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'titulo', 'textoCuerpo', 'textoBotonOk', 'textoBotonCancelar', dialogoConfirmacionController ]);

function dialogoConfirmacionController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, titulo, textoCuerpo, textoBotonOk, textoBotonCancelar) {
	var mi = this;
	
	mi.titulo = titulo;
	mi.textoCuerpo = textoCuerpo;
	mi.textoBotonOk = textoBotonOk;
	mi.textoBotonCancelar = textoBotonCancelar;
		
	mi.aceptar = function(){
		$uibModalInstance.close(true);
	}
	
	mi.cancelar = function() {
		$uibModalInstance.close(false);
	};
	
}