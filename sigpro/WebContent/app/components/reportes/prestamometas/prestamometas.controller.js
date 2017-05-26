var app = angular.module('prestamometasController', []);


app.controller('prestamometasController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi=this;
	mi.proyectoid = "";
	mi.proyectoNombre = "";
	mi.objetoTipoNombre = "";
	var posActualEditando = -1;
	var metaSeleccionada = null;
	mi.padreActual = -1;
	mi.objetoTipo = - 1;
	
	
	$window.document.title = $utilidades.sistema_nombre+' - Metas de Préstamo';
	i18nService.setCurrentLang('es');
	 
	$http.post('/SProyecto', { accion: 'obtenerProyectoPorId', id: $routeParams.proyectoId }).success(
			function(response) {
				mi.proyectoid = response.id;
				mi.proyectoNombre = response.nombre;
				mi.objetoTipoNombre = "Proyecto";
	});
	 
	 $http.post('/SProyecto', { accion: 'getProyectoMetas', proyectoid:$routeParams.proyectoId, t: (new Date()).getTime()})
	 .then(function(response){
		 mi.lista = response.data.proyectometas;
		 mi.prestamometas = [].concat(mi.lista);
		 var tab = "\t";
		 for (x in mi.prestamometas){
			 mi.prestamometas[x].nombre = tab.repeat(mi.prestamometas[x].objetoTipo -1) + mi.prestamometas[x].nombre;
			 mi.prestamometas[x].editarMetaReal = false;
			 mi.prestamometas[x].editarMetaAnualReal = false;
		 }
	});	
	 
	mi.acordion = function(padre, objetoTipo){
		if (mi.padreActual == padre && mi.objetoTipo == objetoTipo){
			mi.padreActual = -1;
			mi.objetoTipo = - 1;
		}else{
			mi.padreActual = padre;
			mi.objetoTipo = objetoTipo;
		}
	}
	 
	 mi.editarMetaReal = function(x){
		 if (x.unidadDeMedida!= ""){
			 var pos = mi.prestamometas.indexOf(x);
			 if (posActualEditando != -1){
				 mi.prestamometas[posActualEditando].editarMetaReal = false;
			 }
			 posActualEditando = pos;
			 mi.prestamometas[pos].editarMetaReal = !mi.prestamometas[pos].editarMetaReal;
			 metaSeleccionada = {
					 metaRealId: mi.prestamometas[pos].metaRealId,
					 metaReal: mi.prestamometas[pos].metaReal,
					 datoTipoId: mi.prestamometas[pos].datoTipoId
			 }
		 }
	 }
	 
	 mi.cancelaEditarMetaReal = function(x){
		 mi.prestamometas[posActualEditando].editarMetaReal = false;
		 mi.prestamometas[posActualEditando].metaReal = metaSeleccionada.metaReal;
		 metaSeleccionada = null;
	 }
	 
	 mi.guardarMetaReal = function($event){
	    var keyCode = $event.which || $event.keyCode;
	    if (keyCode === 13 && metaSeleccionada != null 
	    		&& mi.prestamometas[posActualEditando].metaReal!='' 
	    		&& mi.prestamometas[posActualEditando].metaReal!=null) {
	    	switch(parseInt(metaSeleccionada.datoTipoId)){
			case 1: //texto
				metaSeleccionada.valorString = mi.prestamometas[posActualEditando].metaReal;
				break;
			case 2: //entero
				metaSeleccionada.valorEntero = mi.prestamometas[posActualEditando].metaReal;
				break;
			case 3: //decimal
				metaSeleccionada.valorDecimal = mi.prestamometas[posActualEditando].metaReal;
				break;
			case 4: //booleano

				break;
			case 5: //fecha
				metaSeleccionada.valorTiempo = mi.prestamometas[posActualEditando].metaReal;
				break;
			default: 
				mi.metavalor.valorString = null;
	    	}
	    	metaSeleccionada.id = metaSeleccionada.MetaRealId;
	    	guardarMeta(metaSeleccionada);
	    }
	  };
	  
	  mi.editarMetaAnualReal = function(x){
			 if (x.unidadDeMedida!= ""){
				 var pos = mi.prestamometas.indexOf(x);
				 if (posActualEditando != -1){
					 mi.prestamometas[posActualEditando].editarMetaAnualReal = false;
				 }
				 posActualEditando = pos;
				 mi.prestamometas[pos].editarMetaAnualReal = !mi.prestamometas[pos].editarMetaAnualReal;
				 metaSeleccionada = {
						 metaAnualRealId: mi.prestamometas[pos].metaAnualRealId,
						 metaAnualReal: mi.prestamometas[pos].metaAnualReal,
						 datoTipoId: mi.prestamometas[pos].datoTipoId
				 }
			 }
		 }
		 
		 mi.cancelaEditarMetaAnualReal = function(x){
			 mi.prestamometas[posActualEditando].editarMetaAnualReal = false;
			 mi.prestamometas[posActualEditando].metaAnualReal = metaSeleccionada.metaAnualReal;
			 metaSeleccionada = null;
		 }
		 
		 mi.guardarMetaAnualReal = function($event){
		    var keyCode = $event.which || $event.keyCode;
		    if (keyCode === 13 && metaSeleccionada != null 
		    		&& mi.prestamometas[posActualEditando].metaAnualReal!='' 
		    		&& mi.prestamometas[posActualEditando].metaAnualReal!=null) {
		    	switch(parseInt(metaSeleccionada.datoTipoId)){
				case 1: //texto
					metaSeleccionada.valorString = mi.prestamometas[posActualEditando].metaAnualReal;
					break;
				case 2: //entero
					metaSeleccionada.valorEntero = mi.prestamometas[posActualEditando].metaAnualReal;
					break;
				case 3: //decimal
					metaSeleccionada.valorDecimal = mi.prestamometas[posActualEditando].metaAnualReal;
					break;
				case 4: //booleano

					break;
				case 5: //fecha
					metaSeleccionada.valorTiempo = mi.prestamometas[posActualEditando].metaAnualReal;
					break;
				default: 
					mi.metavalor.valorString = null;
		    	}
		    	metaSeleccionada.id = metaSeleccionada.metaAnualRealId;
		    	guardarMeta(metaSeleccionada);
		    }
		  };
	  
	 guardarMeta = function(Meta){
		 $http.post('/SMetaValor', {
				accion: 'guardarMetaValor',
				esnuevo: true,
				metaid: Meta.id,
				fecha: null,
				valorEntero: Meta.valorEntero,
				valorString: Meta.valorString,
				valorDecimal: Meta.valorDecimal,
				valorTiempo: Meta.valorTiempo
				
			}).success(function(response){
				if(response.success){
					metaSeleccionada.metaReal = mi.prestamometas[posActualEditando].metaReal;
					metaSeleccionada.metaAnualReal = mi.prestamometas[posActualEditando].metaAnualReal;
					mi.prestamometas[posActualEditando].editarMetaReal = false;
					mi.prestamometas[posActualEditando].editarMetaAnualReal = false;
					$utilidades.mensaje('success','Valor guardado con éxito');
				}
				else
					$utilidades.mensaje('danger','Error al guardar el valor');
			});
	 }
	  
	 mi.exportarExcel = function(){
			$http.post('/SProyecto', { accion: 'exportarExcel', proyectoid:$routeParams.proyectoId,t:moment().unix()
				  } ).then(
						  function successCallback(response) {
								var anchor = angular.element('<a/>');
							    anchor.attr({
							         href: 'data:application/ms-excel;base64,' + response.data,
							         target: '_blank',
							         download: 'MetasPrestamo.xls'
							     })[0].click();
							  }.bind(this), function errorCallback(response){
							 		
							 	}
							 );
		};
	
	
}]);



		