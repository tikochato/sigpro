var app = angular.module('mapaController', [ 'ngTouch','angularjs-dropdown-multiselect' ]);

app.controller('mapaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','uiGmapGoogleMapApi',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,uiGmapGoogleMapApi) {

	var mi = this;
	
	$scope.geoposicionlat =  14.6290845;
	$scope.geoposicionlong = -90.5116158;

	$scope.mostrarTodo = true;
	$scope.mostrarObjetoId = 0;
	$scope.marcas = {};

	$scope.mostrarTodo=true;
	$scope.mostrarProyectos=true;
	$scope.mostrarComponentes=true;
	$scope.mostrarProductos = true;
	$scope.mostrarSubproductos = true;
	$scope.mostrarActividades = true;
	$scope.proyectoid = $routeParams.proyecto_id;
	
	
	$scope.accionServlet = $scope.proyectoid!=null ? 'getMarcasPorProyecto' : 'getMarcasProyecto';
	$scope.mostrarControles = $scope.proyectoid!=null;
	mi.mostrar = false;
	
	
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
		function(response) {
			mi.prestamos = [];
			mi.prestamos.push({'value' : 0, 'text' : 'Seleccione un proyecto'});
			if (response.success){
				for (var i = 0; i < response.entidades.length; i++){
					mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
				}
				
				if ($scope.proyectoid !=null && $scope.proyectoid != undefined){
					for (x in mi.prestamos){
						if (mi.prestamos[x].value == $scope.proyectoid){
							mi.prestamo = mi.prestamos[x];
							mi.cargar();
							break;
						}
						
					}
					
				}else{
					mi.prestamo = mi.prestamos[0];
				}
				
				
			}
	});

	mi.cargarMapa = function(){
		
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
		
	}
	

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
		 		if ($scope.mostrarTodo){
		 			mi.transclusionModel = [mi.transclusionData[0],mi.transclusionData[1],mi.transclusionData[2],mi.transclusionData[3]];
		 		}else{
		 			mi.transclusionModel = [];
		 		}
		 		for (x in $scope.marcas){
		 			$scope.marcas[x].mostrar = true;
				 }
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
				&& $scope.mostrarSubproductos && $scope.mostrarActividades;
		 		if ($scope.mostrarActividades)
		 			mi.transclusionModel = [mi.transclusionData[0],mi.transclusionData[1],mi.transclusionData[2],mi.transclusionData[3]];
		 		else
		 			mi.transclusionModel = [];
		 		for (x in $scope.marcas){
					 if ($scope.marcas[x].objetoTipoId==5 )
						 $scope.marcas[x].mostrar = true;
				 }
		 		
		 		
		 		break;
		 }
	  };

	  $scope.mostrarInformaicon = function(objetoId , ObjetoTipo){

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
	 
	 mi.cargar = function(){
		 if (mi.prestamo!=null && mi.prestamo.value > 0){
		 $http.post('/SMapa', { accion: 'getMarcasPorProyecto', proyectoId:mi.prestamo.value}).success(
					function(response) {
						$scope.marcas = response.marcas;
						for (x in $scope.marcas){
							if ($scope.marcas[x].objetoTipoId == 1){
								$scope.geoposicionlat =  $scope.marcas[x].posicion.latitude;
								$scope.geoposicionlong = $scope.marcas[x].posicion.longitude;
							}
						}
						mi.mostrar=true;
						mi.cargarMapa();
			});
		 } 
	 };
	 
	 
	 mi.transclusionData = [  { id: 1, label: 'Sin iniciar' }, { id: 2, label: 'En proceso' }, { id: 3, label: 'Retrasadas' },{ id: 4, label: 'Completadas' }]; 
	 mi.transclusionSettings = { dynamicTitle: false, showCheckAll: false, showUncheckAll:false,};
	 mi.transclusionModel = [mi.transclusionData[0],mi.transclusionData[1],mi.transclusionData[2],mi.transclusionData[3]];

	 
	 mi.selectActividad = {
			 onItemSelect: function(item) {
				 for (x in $scope.marcas){
					 if ($scope.marcas[x].objetoTipoId==5 && $scope.marcas[x].idEstado == item.id)
						 $scope.marcas[x].mostrar = true;
				 }
				 $scope.mostrarActividades = true;
				 if (mi.transclusionModel.length == 4)
					 mi.getMostrarTodo();
			 },
			 onItemDeselect:function(item) {
				 for (x in $scope.marcas){
					 if ($scope.marcas[x].objetoTipoId==5 && $scope.marcas[x].idEstado == item.id)
						 $scope.marcas[x].mostrar = false;
				 }
				 $scope.mostrarTodo = false;
				 
				 if (mi.transclusionModel.length == 0){
					 $scope.mostrarActividades = false;
				 }
				 
			 }
	 };
	 
	 mi.getMostrarTodo = function(){
		 $scope.mostrarTodo = $scope.mostrarProyectos && $scope.mostrarComponentes && $scope.mostrarProductos
			&& $scope.mostrarSubproductos && $scope.mostrarActividades && mi.transclusionModel.length == 4 ;
	 }

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
