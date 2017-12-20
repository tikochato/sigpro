var app = angular.module('matrizraciController', ['vs-repeat']);


app.controller('matrizraciController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
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
	mi.sinColaboradores = false;
	mi.mostrarExport = false;
	
	$window.document.title = $utilidades.sistema_nombre+' - Matriz RACI';
	i18nService.setCurrentLang('es');
	 
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
	}
	
	mi.cambioPep=function(selected){
		if(selected!== undefined){
			mi.pepNombre = selected.originalObject.nombre;
			mi.pepId = selected.originalObject.id;
			$scope.$broadcast('angucomplete-alt:clearInput', 'lineaBase');
			mi.getLineasBase(mi.pepId);		
		}
		else{
			mi.pepNombre="";
			mi.pepId="";
		}
	}
	
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	
	mi.cambioLineaBase=function(selected){
		if(selected!== undefined){
			mi.lineaBaseNombre = selected.originalObject.nombre;
			mi.lineaBaseId = selected.originalObject.id;
			mi.generarMatriz();
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
	 
	 mi.generarMatriz = function (){
		 mi.mostrarTabla = false;
		 mi.mostrarExport = false;
		 
		 if (mi.pepId > 0){
		
			 mi.mostrarcargando = true;
			 
			 mi.inicializarVariables();
				$http.post('/SMatrizRACI', { 
					accion: 'getMatriz', 
					lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
					idPrestamo: mi.pepId 
				}).success(
					function(response) {
						mi.colaboradores = response.colaboradores;
						mi.matrizAsignacion = response.matriz;
						mi.construirMatriz();
						mi.motrarTabla = true;
						mi.mostrarcargando = false;
						mi.sinColaboradores = response.sinColaboradores;
						mi.mostrarExport = true;
						
				});	
		 }
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
			  item[0] = {rol: mi.matrizAsignacion[x].objetoNombre,id:-1,objetoId:-1,objetoTipo:mi.matrizAsignacion[x].objetoTipo,nivel:mi.matrizAsignacion[x].nivel};
			  for (y in mi.colaboradores){
				  if (mi.matrizAsignacion[x].idR == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'R',rolId:1,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo, nivel:mi.matrizAsignacion[x].nivel};
				  } else if (mi.matrizAsignacion[x].idA == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'A',rolId:2,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo, nivel:mi.matrizAsignacion[x].nivel};
				  }else if (mi.matrizAsignacion[x].idC == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'C',rolId:3,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo, nivel:mi.matrizAsignacion[x].nivel};
				  }else if (mi.matrizAsignacion[x].idI == mi.colaboradores[y].id ){
					  item[Number(y)+1] = {rol:'I',rolId:4,id:mi.colaboradores[y].id,objetoId:mi.matrizAsignacion[x].objetoId,objetoTipo:mi.matrizAsignacion[x].objetoTipo, nivel:mi.matrizAsignacion[x].nivel};
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
		  		return (mi.sinColaboradores ? "no_rotate" : "rotate") + " cabecerath1";
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
		        case 0:
		            return 'glyphicon glyphicon-record';
		        case 1:
		            return 'glyphicon glyphicon-th';
		        case 2:
		            return 'glyphicon glyphicon-equalizer';
		        case 3:
		            return 'glyphicon glyphicon-certificate';
		        case 4:
		            return 'glyphicon glyphicon-link';
		        case 5:
		            return 'glyphicon glyphicon-time';
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
					},
					lineaBase : function(){
						return mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null;
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
				 idPrestamo: mi.pepId,
				 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
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
			 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			 idPrestamo: mi.pepId, 
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
	'$timeout', '$log', '$objetoId',  '$objetoTipo', '$rol', 'lineaBase', modalInformacion ]);

function modalInformacion($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $objetoId, $objetoTipo, $rol, lineaBase) {

	var mi = this;
	mi.informacion={};
	

	$http.post('/SMatrizRACI', {
		accion : 'getInformacionTarea',
		objetoId: $objetoId,
		lineaBase: lineaBase,
		objetoTipo:$objetoTipo,
		rol: $rol
		
	}).success(function(response) {
		mi.informacion = response.informacion;
	});
	
	 mi.ok = function () {
		 $uibModalInstance.dismiss('cancel');
	 };


}




		