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
	mi.mostrarcargando = false;
	
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
		 mi.mostrarcargando = true;
		 mi.inicializarVariables();
			$http.post('/SMatrizRACI', { accion: 'getMatriz', 
				idPrestamo: mi.prestamoSeleccionado!= null ? mi.prestamoSeleccionado.value : 0 }).success(
			
				function(response) {
					mi.colaboradores = response.colaboradores;
					mi.matrizAsignacion = response.matriz;
					mi.construirMatriz();
					mi.motrarTabla = true;
					mi.mostrarcargando = false;
					
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
			  
			  
				 
			  item[0] = {rol: mi.matrizAsignacion[x].objetoNombre,id:-1,objetoId:-1,objetoTipo:mi.matrizAsignacion[x].objetoTipo};
			  for (y in mi.colaboradores){
				  if (mi.matrizAsignacion[x].idR == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'R',rolId:1,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo};
				  } else if (mi.matrizAsignacion[x].idA == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'A',rolId:2,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo};
				  }else if (mi.matrizAsignacion[x].idC == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'C',rolId:3,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo};
				  }else if (mi.matrizAsignacion[x].idI == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'I',rolId:4,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo};
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
		  		return "rotate cabecerath1";
		  	else 
		  		return "label-form thTarea";
			 
			 
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
		  if (value.rolId == -1)
			  return "claseNombre"
			  
	  };
	  
	  mi.claseIcon = function (item,index) {
		   
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
			  mi.abrirInformacion(valor.objetoId,valor.objetoTipo, valor.rol);
	  }
	  
	  mi.abrirInformacion = function (objetoId , objetoTipo, rol) {
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
	 
	 mi.exportarExcel = function(){
			$http.post('/SMatrizRACI', { 
				 accion: 'exportarExcel', 
				 idPrestamo: mi.prestamoSeleccionado.value, 
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'MatrizRACI.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
			};
			
	 mi.exportarPdf=function(){
		 $http.post('/SMatrizRACI', { 
			 accion: 'exportarPdf', 
			 idPrestamo: mi.prestamoSeleccionado.value, 
			 t:moment().unix()
		  } ).then(
				  function successCallback(response) {
					  var anchor = angular.element('<a/>');
					  anchor.attr({
				         href: 'data:application/pdf;base64,' + response.data,
				         target: '_blank',
				         download: 'MatrizRACI.pdf'
					  })[0].click();
				  }.bind(this), function errorCallback(response){
			 	}
		  	);
	 };
		
}]);

app.controller('modalInformacion', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$objetoId',  '$objetoTipo', '$rol',  modalInformacion ]);

function modalInformacion($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $objetoId, $objetoTipo, $rol) {

	var mi = this;
	mi.informacion={};
	

	$http.post('/SMatrizRACI', {
		accion : 'getInformacionTarea',
		objetoId: $objetoId,
		objetoTipo:$objetoTipo,
		rol: $rol
		
	}).success(function(response) {
		mi.informacion = response.informacion;
	});
	
	 mi.ok = function () {
		 $uibModalInstance.dismiss('cancel');
	 };


}




		