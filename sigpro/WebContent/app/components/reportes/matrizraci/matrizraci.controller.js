var app = angular.module('matrizraciController', []);


app.controller('matrizraciController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	mi.proyectoid = "";
	mi.proyectoNombre = "";
	mi.objetoTipoNombre = "";
	mi.colaboradores = [];
	mi.matrizAsignacion = [];
	mi.matrizRaci = [];
	mi.encabezadoMatriz = [];
	mi.mostrarTabla = false;
	
	$window.document.title = $utilidades.sistema_nombre+' - Matriz RACI';
	i18nService.setCurrentLang('es');
	 
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
				}
	});
	 
	
	 
	 
	 mi.generarMatriz = function (){
		 mi.inicializarVariables();
			$http.post('/SMatrizRACI', { accion: 'getMatriz', idPrestamo: mi.prestamoSeleccionado.value }).success(
				function(response) {
					mi.colaboradores = response.colaboradores;
					mi.matrizAsignacion = response.matriz;
					mi.construirMatriz();
					mi.mostrarTabla = true;
			});	
	  };
	  
	  
	  mi.inicializarVariables = function(){
		  mi.proyectoid = "";
			mi.proyectoNombre = "";
			mi.objetoTipoNombre = "";
			mi.colaboradores = [];
			mi.matrizAsignacion = [];
			mi.matrizRaci = [];
			mi.encabezadoMatriz = [];
			mi.mostrarTabla = false;
	  }
	  
	  
	  mi.construirMatriz = function(){
		  
		  mi.encabezadoMatriz.push({id:-1,nombre:'Tareas'});
		  for (x in mi.colaboradores){
			  mi.encabezadoMatriz.push(mi.colaboradores[x]);
		  }
		  
		  
		  for (x in mi.matrizAsignacion ){
			  var item = [];
			  var tab = "\t";
			  mi.matrizAsignacion[x].objetoNombre= tab.repeat(mi.matrizAsignacion[x].nivel -1) + mi.matrizAsignacion[x].objetoNombre;
			  
				 
			  item[0] = {rol: mi.matrizAsignacion[x].objetoNombre,id:-1,objetoId:-1,objetoTipo:mi.matrizAsignacion[x].objetoTipo};
			  for (y in mi.colaboradores){
				  if (mi.matrizAsignacion[x].idR == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'R',rolId:1,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId};
				  } else if (mi.matrizAsignacion[x].idA == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'A',rolId:2,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId};
				  }else if (mi.matrizAsignacion[x].idC == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'C',rolId:3,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId};
				  }else if (mi.matrizAsignacion[x].idI == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'I',rolId:4,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId};
				  }
				  else{
					  item[Number(y)+1] = '';
				  }
				  
			  }
			  mi.matrizRaci.push(item);
		  }
		  
	  }
	  
	  mi.claseHeader = function (value){
		  	if (value > 0)
		  		return "rotate";
		  	else 
		  		return "label-form";
			 
			 
	  };
	  
	  
	  mi.claseBody = function (value){
		  switch (value.rolId){
		  case 1:
			  return "classRolR"; break;
		  case 2:
			  return "classRolA"; break;
		  case 3:
			  return "classRolC"; break;
		  case 4:
			  return "classRolI"; break;
		  }   
	  };
	  
	  mi.claseIcon = function (item) {
		    switch (item[0].objetoTipo) {
		        case 1:
		            return 'glyphicon glyphicon-record';
		        case 2:
		            return 'glyphicon glyphicon-th';
		        case 3:
		            return 'glyphicon glyphicon-certificate';
		        case 4:
		            return 'glyphicon glyphicon-link';
		        case 5:
		            return 'glyphicon glyphicon-th-list';
		    }
		};
	  
	  
	  mi.mostrarColaborador = function(valor){
		  if ( valor != "" && valor.objetoId != null)
			  mi.abrirInformacion(valor.objetoId, valor.rol);
	  }
	  
	  mi.abrirInformacion = function (objetoId , rol) {
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
					$rol : function() {
						return rol;
					}
				}
		    });	    
	 };
	 
	 mi.getNumber = function(num) {
		    return new Array(num);   
		}
		
}]);

app.controller('modalInformacion', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$objetoId', '$rol',  modalInformacion ]);

function modalInformacion($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $objetoId, $rol) {

	var mi = this;
	mi.informacion={};
	

	$http.post('/SMatrizRACI', {
		accion : 'getInformacionTarea',
		actividadId: $objetoId,
		rol: $rol
		
	}).success(function(response) {
		mi.informacion = response.informacion;
	});
	
	 mi.ok = function () {
		 $uibModalInstance.dismiss('cancel');
	 };


}




		