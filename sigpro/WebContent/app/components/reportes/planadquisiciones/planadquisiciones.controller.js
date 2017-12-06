var app = angular.module('planAdquisicionesController', ['ngTouch','ngAnimate','ui.utils.masks','vs-repeat']);
app.controller('planAdquisicionesController',['$scope', '$rootScope', '$http', '$window', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion', '$filter','$uibModal',
	function($scope, $rootScope, $http, $window, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion, $filter,$uibModal) {
	var mi = this;
	var anioFiscal = new Date();
	mi.anio = anioFiscal.getFullYear();
	mi.mostrarBotones = false;
	mi.mostrarGuardando = false;
	mi.mostrarCargando = false;
	mi.enMillones = false;
	mi.fechaSuscripcion = "";
	mi.fechaCierre = "";
	mi.tooltipObjetoTipo = [$rootScope.etiquetas.proyecto,"Componente","Subcomponente","Producto","Sub Producto","Actividad"];
	mi.valoresInicializados = [0,0,0,0,0,"","","","","","","","","",""];
	mi.ddlOpciones = [];
	mi.ddlOpcionesTipos = [];
	mi.ddlcategoriaAdquisiciones = [];
	i18nService.setCurrentLang('es');
	
	mi.calcularTamanosPantalla = function(){
		mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
		mi.tamanoTotal = mi.tamanoPantalla - 300; 
		mi.estiloCelda = "width:80px;min-width:80px; max-width:80px;text-align: center";
	}
	
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
	
	mi.getLineasBase = function(proyectoId){
		$http.post('/SProyecto',{accion: 'getLineasBase', proyectoId: proyectoId}).success(
			function(response) {
				mi.lineasBase = [];
				if (response.success){
					mi.lineasBase = response.lineasBase;
				}
		});	
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
	
	mi.selectedRow = function(row){
		mi.datoSeleccionado = row;
	}
	
	$http.post('/SMeta', {accion: 'getMetasUnidadesMedida'}).success(
		function(response){
			mi.ddlOpciones = [];
			mi.ddlOpciones.push({id: 0, value: 'Seleccionar'});
			if(response.success){
				for(x in response.MetasUnidades){
					mi.ddlOpciones.push({id: response.MetasUnidades[x].id, value: response.MetasUnidades[x].nombre});
				}	
			}
	});
	
	mi.ddlOpcionesTiposAdquisicion = [{id: 0, value: "Seleccionar"}];
	
	mi.obtenerTipoAdquisicion = function(){
		$http.post('/STipoAdquisicion', {accion: 'getTipoAdquisicionPaginaPorCooperante', idCooperante: mi.cooperanteId}).success(
			function(response){
				mi.ddlOpcionesTiposAdquisicion = [];
				mi.ddlOpcionesTiposAdquisicion = [{id: 0, value: "Seleccionar"}];
				if(response.success){
					for(x in response.cooperanteTipoAdquisiciones){
						mi.ddlOpcionesTiposAdquisicion.push({id: response.cooperanteTipoAdquisiciones[x].id, value: response.cooperanteTipoAdquisiciones[x].nombre});
					}
				}
			}
		);
	}
	
	$http.post('/SCategoriaAdquisicion', {accion: 'getCategoriaAdquisicion'}).success(
		function(response){
			mi.ddlcategoriaAdquisiciones = [];
			mi.ddlcategoriaAdquisiciones = [{id: 0, value: "Seleccionar"}];		
			if(response.success){
				for(x in response.categoriaAdquisicion){
					mi.ddlcategoriaAdquisiciones.push({id: response.categoriaAdquisicion[x].id, value: response.categoriaAdquisicion[x].nombre});
				}
			}
		});
	

	mi.nombreUnidadMedida = function(id){
		for (i=0; i<mi.ddlOpciones.length; i++){
			if(mi.ddlOpciones[i].id == id){
				return mi.ddlOpciones[i].value;
			}
		}
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
	
	angular.element($window).bind('resize', function(){ 
        mi.calcularTamanosPantalla();
        $scope.$digest();
      });
    $scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
	
	mi.calcularTotal = function(row){
		row['total'] = row['costo'] * row['cantidad'];
		mi.calcularPadre(row.predecesorId, row.objetoPredecesorTipo);
	}
	
	mi.obtenerEntidad = function(id, objetoTipo){
		for (x in mi.data){
			if (id == mi.data[x].objetoId && objetoTipo == mi.data[x].objetoTipo){
				return mi.data[x];
			}
		}
	}
	
	mi.generar = function(){
		if(mi.pepId > 0){
			mi.mostrarCargando = true;
			mi.mostrarTablas = false;
			$http.post('/SPlanAdquisiciones',{
				accion: 'generarPlan',
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
				proyectoId: mi.pepId
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.proyecto);
					mi.fechaSuscripcion = moment(response.fechaSuscripcion,'DD/MM/YYYY').toDate();
					mi.fechaCierre = moment(response.fechaCierre,'DD/MM/YYYY').toDate();
					mi.cooperanteId = response.cooperanteId;
					mi.obtenerTipoAdquisicion();
					mi.mostrarCargando = false;
					mi.mostrarBotones = true;
					mi.mostrarTablas = true;
					mi.calcularTamanosPantalla();
				}
			});
		}
	}
	
	mi.exportarExcel = function(){
		$http.post('/SPlanAdquisiciones', { 
			 accion: 'exportarExcel', 
			 proyectoId: mi.pepId,
			 informeCompleto: mi.informeCompleto,
			 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			 t:moment().unix()
		  } ).then(
				  function successCallback(response) {
					  var anchor = angular.element('<a/>');
					  anchor.attr({
				         href: 'data:application/ms-excel;base64,' + response.data,
				         target: '_blank',
				         download: 'PlanAdquisiciones.xls'
					  })[0].click();
				  }.bind(this), function errorCallback(response){
			 	}
		  	);
		};
	
	mi.exportarPdf=function(){
		$http.post('/SPlanAdquisiciones', { 
			 accion: 'exportarPdf', 
			 idPrestamo: mi.idPrestamo,
			 informeCompleto: mi.informeCompleto,	
			 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			 t:moment().unix()
		  } ).then(
				  function successCallback(response) {
					  var anchor = angular.element('<a/>');
					  anchor.attr({
				         href: 'data:application/pdf;base64,' + response.data,
				         target: '_blank',
				         download: 'PlanAdquisiciones.pdf'
					  })[0].click();
				  }.bind(this), function errorCallback(response){
			 	}
		  	);
	};
	
	mi.crearArbol = function(datos){
		mi.data = datos;
		
		mi.rowCollectionPrestamo = [];
		mi.rowCollectionPrestamo = mi.data;
		mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
		mi.mostrarDescargar = true;
	}
	
}]);
