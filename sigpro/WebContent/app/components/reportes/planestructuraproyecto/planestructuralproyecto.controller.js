var app = angular.module('planEstructuralProyectoController',['ngTouch','ngAnimate','ui.utils.masks','vs-repeat']);
app.controller('planEstructuralProyectoController',['$scope', '$rootScope', '$http', '$window', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion', '$filter','$uibModal',
	function($scope, $rootScope, $http, $window, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion, $filter,$uibModal){
	var mi = this;
	
	mi.mostrarCargando = false;
	mi.enMillones = false;
	mi.tooltipObjetoTipo = [$rootScope.etiquetas.proyecto,"Componente","Subcomponente","Producto","Sub Producto","Actividad"];
	
	mi.calcularTamanosPantalla = function(){
		mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
		mi.tamanoTotal = mi.tamanoPantalla; 
		mi.estiloCelda = "width:80px;min-width:80px; max-width:80px;text-align: center";
	}
	
	mi.claseIcon = function (item) {
	    switch (item.objetoTipo) {
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
	
	$scope.divActivo = "";
	mi.activarScroll = function(id){
		$scope.divActivo = id;
    }
	
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
	
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	
	mi.cambioLineaBase=function(selected){
		if(selected!== undefined){
			mi.lineaBaseNombre = selected.originalObject.nombre;
			mi.lineaBaseId = selected.originalObject.id;
			mi.generar();
		}
		else{
			mi.lineaBaseNombre="";
			mi.lineaBaseId=null;
		}
	};
	
	mi.generar = function(){
		if(mi.pepId > 0){
			mi.mostrarCargando = true;
			mi.mostrarTablas = false;
			$http.post('/SPlanEstructuralProyecto',{
				accion: 'generarPlan',
				proyectoId: mi.pepId,
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.proyecto);
					mi.fechaSuscripcion = moment(response.fechaSuscripcion,'DD/MM/YYYY').toDate();
					mi.fechaCierre = moment(response.fechaCierre,'DD/MM/YYYY').toDate();
					mi.cooperanteId = response.cooperanteId;
					mi.mostrarCargando = false;
					mi.mostrarBotones = true;
					mi.mostrarTablas = true;
					mi.calcularTamanosPantalla();
				}
			});
		}
	}
	
	mi.crearArbol = function(datos){
		mi.data = datos;
		
		mi.rowCollectionPrestamo = [];
		mi.rowCollectionPrestamo = mi.data;
		mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
		mi.mostrarDescargar = true;
	}
	
	mi.exportarExcel = function(){
		$http.post('/SPlanEstructuralProyecto', { 
			 accion: 'exportarExcel', 
			 proyectoId: mi.pepId, 
			 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			 t:moment().unix()
		  } ).then(
				  function successCallback(response) {
					  var anchor = angular.element('<a/>');
					  anchor.attr({
				         href: 'data:application/ms-excel;base64,' + response.data,
				         target: '_blank',
				         download: 'PlanEstructuraPréstamo.xls'
					  })[0].click();
				  }.bind(this), function errorCallback(response){
			 	}
		  	);
		};
	
}])