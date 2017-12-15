var app = angular.module('pagoplanificado',[]);

app.factory('pagoplanificado',['$mdDialog','$uibModal', '$http',
	function($mdDialog,$uibModal,$http){
	
	return{
		getPagoPlanificado: function($scope, objetoId, objetoTipo, datosCarga, fechaFin, fechaInicio){
			return $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : '/app/components/pago_planificado/pago_planificado.jsp',
			    controller : 'modalPagos',
				controllerAs : 'pagoc',
				backdrop : 'static',
				size : 'md',
				resolve : {
					$objetoId : function() {
						return objetoId;
					},
					$objetoTipo : function() {
						return objetoTipo;
					},
					$datosCarga : function() {
						return datosCarga;
					},
					$fechaInicio : function() {
						return fechaFin;
					},
					$fechaFin : function() {
						return fechaInicio;
					}
			    }
			});
		}
		
	}
}])

app.controller('modalPagos', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log','dialogoConfirmacion', '$objetoId', '$objetoTipo', '$datosCarga', '$fechaInicio',
	'$fechaFin',modalPagos ]);

function modalPagos($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $dialogoConfirmacion, $objetoId,$objetoTipo,$datosCarga,$fechaInicio, 
	$fechaFin) {

	$scope.pagos = [];
	var mi = this;
	mi.pagos = $scope.pagos;
	mi.formatofecha = 'dd/MM/yyyy';
	mi.altformatofecha = ['d!/M!/yyyy'];
	mi.totalPagos=0;
	
	mi.abrirPopupFecha = function(index, tipo) {
		if(tipo==0){
			mi.pagos[index].isOpen = true;
		}else{
			mi.pagos[index].isOpenValor = true;
		}
		
	};

	mi.fechaOptions = {
			formatYear : 'yy',
			startingDay : 1,
			maxDate:  $fechaFin, 
			minDate: $fechaInicio
	};
	
	
	$http.post('SPagoPlanificado', {accion:'getPagos',objetoId: $objetoId, objetoTipo: $objetoTipo}).success(
		function(response) {
			$scope.pagos = response.pagos;
			for (x in $scope.pagos){
				$scope.pagos[x].fechaPago = moment($scope.pagos[x].fechaPago,'DD/MM/YYYY').toDate() 
			}
			mi.pagos = $scope.pagos;
	});

	
	
	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.guardarFecha = function(row){
		row.fecha = row.fechaPago!=null ? moment(row.fechaPago).format('DD/MM/YYYY') : null;
	}
			
	mi.nuevoPago = function(){
		$scope.pagos.push({  
               fechaPago: null,
               pago: null
            });
	}
	
	mi.borrarPago = function(row){
		$dialogoConfirmacion.abrirDialogoConfirmacion($scope
				, "Confirmación de Borrado"
				, '¿Desea borrar el pago ?'
				, "Borrar"
				, "Cancelar")
		.result.then(function(data) {
			if(data){
				var index = mi.pagos.indexOf(row);
				if (index > -1) {
					$http.post('SPagoPlanificado', {accion:'borrarPago',idPago:row.id}).success(
							function(response) {
								mi.pagos.splice(index, 1);
					});
					
					
				}
			}
		}, function(){
			
		});
	}
	
	mi.ok = function() {
		var pagosTemp = [];
		
		for(x in $scope.pagos){
			var pagoTemp = {fechaPago: moment($scope.pagos[x].fechaPago).format('DD/MM/YYYY'),  pago:$scope.pagos[x].pago};
			pagosTemp.push(pagoTemp)
		}
		$uibModalInstance.close(pagosTemp);
	};
	
	$scope.$watch('pagos', function(array) {
	     var total = 0;
	     if (array) {
	         mi.totalPagos = array.reduce(function(total,item) {
        		 return total + item.pago;
	         },0);
	     } 
	 }, true);
	
}
