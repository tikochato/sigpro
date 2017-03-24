var app = angular.module('mapaController', [ 'ngTouch' ]);

app.controller('mapaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','uiGmapGoogleMapApi',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,uiGmapGoogleMapApi) {

	$scope.geoposicionlat =  14.6290845;
	$scope.geoposicionlong = -90.5116158;
	
	$scope.mostrarTodo = true;
	$scope.mostrarObjetoId = 0;
	$scope.titulo="";
	$scope.marcas = {};
	
	$scope.mostrarTodo=true;
	$scope.mostrarProyectos=true;
	$scope.mostrarComponentes=true;
	$scope.mostrarProductos = true;
	$scope.mostrarSubproductos = true;
	$scope.mostrarActividades = true;
	$scope.proyectoid = $routeParams.proyecto_id;
	$scope.accionServlet = $scope.proyectoid!=null ? 'getMarcasPorProyecto' : 'getMarcasProyecto';
	
	uiGmapGoogleMapApi.then(function() {
		$scope.map = { center: { latitude: $scope.geoposicionlat, longitude: $scope.geoposicionlong },
		   zoom: 15,
		   height: 400,
		   width: 200,
		   options: {
			   streetViewControl: false,
			   scrollwheel: true,
			  draggable: true,
			  mapTypeId: google.maps.MapTypeId.SATELLITE
		   },
		   events:{
			   click: function (map,evtName,evt) {
				   $scope.posicion = {latitude: evt[0].latLng.lat()+"", longitude: evt[0].latLng.lng()+""} ;
				   $scope.$evalAsync();
			   }
		   },
		   refresh: true
		};
    });
	
	$http.post('/SMapa', { accion: $scope.accionServlet, proyectoId:$routeParams.proyecto_id}).success(
			function(response) {
				$scope.marcas = response.marcas;
	});
	
	 $scope.mostrar = function (objetoId) {
		 
		 switch (objetoId){
		 	case 0:
		 		$scope.mostrarProyectos=$scope.mostrarTodo;
		 		$scope.mostrarComponentes=$scope.mostrarTodo;
		 		$scope.mostrarProductos = $scope.mostrarTodo;
		 		$scope.mostrarSubproductos = $scope.mostrarTodo;
		 		$scope.mostrarActividades = $scope.mostrarTodo;
		 		break;
		 	case 1:
		 		$scope.mostrarTodo = $scope.mostrarProyectos && $scope.mostrarComponentes && $scope.mostrarProductos
				&& $scope.mostrarSubproductos && $scope.mostrarActividades ? true : false;
		 		break;
		 	case 2:
		 		$scope.mostrarTodo = $scope.mostrarProyectos && $scope.mostrarComponentes && $scope.mostrarProductos
				&& $scope.mostrarSubproductos && $scope.mostrarActividades ? true : false;
		 		break;
		 	case 3:
		 		$scope.mostrarTodo = $scope.mostrarProyectos && $scope.mostrarComponentes && $scope.mostrarProductos
				&& $scope.mostrarSubproductos && $scope.mostrarActividades ? true : false;
		 		break;
		 	case 4:
		 		$scope.mostrarTodo = $scope.mostrarProyectos && $scope.mostrarComponentes && $scope.mostrarProductos
				&& $scope.mostrarSubproductos && $scope.mostrarActividades ? true : false;
		 		break;
		 	case 5:
		 		$scope.mostrarTodo = $scope.mostrarProyectos && $scope.mostrarComponentes && $scope.mostrarProductos
				&& $scope.mostrarSubproductos && $scope.mostrarActividades ? true : false;
		 		break;		
		 }
	  };
	  
	  $scope.mostrarInformaicon = function(objetoId , ObjetoTipo){
		  console.log("aaa");
		  console.log(objetoId);
		  console.log(ObjetoTipo);
	  }
	  
	  
	  
	  $scope.abrirInformacion = function (objetoId , objetoTipo) {
		    var modalInstance = $uibModal.open({
		      
		      ariaLabelledBy: 'Información',
		      ariaDescribedBy: 'modal-body',
		      templateUrl: 'modalInfo.html',
		      controller: 'modalInformacion',
		      controllerAs: 'infoc',
		      
		      resolve : {
		    	    $objetoId : function() {
						return objetoId;
					},
					$objetoTipo : function() {
						return objetoTipo;
					}
				}
		    });	    
	 };
	
}]);


app.controller('modalInformacion', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$objetoId', '$objetoTipo',  modalInformacion ]);

function modalInformacion($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $objetoId, $objetoTipo) {

	var mi = this;
	mi.objeto={};
	

	$http.post('/SMapa', {
		accion : 'getObjeto',
		objetoId: $objetoId,
		objetoTipo: $objetoTipo
	}).success(function(response) {
		mi.objeto = response.objeto;
	});
	
	 mi.ok = function () {
		 $uibModalInstance.dismiss('cancel');
	 };


	

	

	

}
