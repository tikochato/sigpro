var app = angular.module('informacionPresupuestariaController',['ngAnimate', 'ngTouch', 'ui.grid.edit', 'ui.grid.rowEdit', 'smart-table']);

app.controller('adquisicionesController', ['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion){
		var mi = this;
		i18nService.setCurrentLang('es');
		mi.fechaInicio = "";
		mi.fechaFin = "";
		mi.movimiento = false;
		mi.mostrarDescargar = false;
		mi.mostrarCargando = false;
		mi.SiguienteActivo = true;
		mi.AnteriorActivo = false;
		mi.enMillones = true;
		mi.agrupacionActual = 1
		mi.limiteAnios = 5;
		mi.tamanioMinimoColumna = 125;
		mi.tamanioMinimoColumnaMillones = 75;
		mi.grupoMostrado= {"planificado":true,"real":true};
		mi.estiloAlineacion="text-align: center;";
		mi.data = [];
		mi.totales = [];
		
		var AGRUPACION_MES= 1;
		var AGRUPACION_BIMESTRE = 2;
		var AGRUPACION_TRIMESTRE = 3;
		var AGRUPACION_CUATRIMESTRE= 4;
		var AGRUPACION_SEMESTRE= 5;
		var AGRUPACION_ANUAL= 6;
		
		var MES_NAME = ['mes1','mes2','mes3','mes4','mes5','mes6','mes7','mes8','mes9','mes10','mes11','mes12'];
		var MES_DISPLAY_NAME = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
		var BIMESTRE_NAME = ['Bimestre1', 'Bimestre2', 'Bimestre3', 'Bimestre4', 'Bimestre5', 'Bimestre6'];
		var BIMESTRE_DISPLAY_NAME = ['Bimestre 1', 'Bimestre 2','Bimestre 3','Bimestre 4','Bimestre 5','Bimestre 6'];
		var TRIMESTRE_NAME = ['Trimestre1', 'Trimestre2','Trimestre3','Trimestre4'];
		var TRIMESTRE_DISPLAY_NAME = ['Trimestre 1', 'Trimestre 2', 'Trimestre 3', 'Trimestre 4'];
		var CUATRIMESTRE_NAME = ['Cuatrimestre1','Cuatrimestre2','Cuatrimestre3'];
		var CUATRIMESTRE_DISPLAY_NAME = ['Cuatrimestre 1', 'Cuatrimestre 2', 'Cuatrimestre 3'];
		var SEMESTRE_NAME = ['Semestre1','Semestre2'];
		var SEMESTRE_DISPLAY_NAME = ['Semestre 1','Semestre 2'];
		var ANUAL_DISPLAY_NAME = ['Anual'];
		
		
		$scope.divActivo = "";
		mi.activarScroll = function(id){
			$scope.divActivo = id;
	    }
				
		mi.agrupaciones = [
			{'value' : 0, 'text' : 'Seleccione una opción'},
			{'value' : AGRUPACION_MES, 'text' : 'Mensual'},
			{'value' : AGRUPACION_BIMESTRE, 'text' : 'Bimestre'},
			{'value' : AGRUPACION_TRIMESTRE, 'text' : 'Trimestre'},
			{'value' : AGRUPACION_CUATRIMESTRE, 'text' : 'Cuatrimestre'},
			{'value' : AGRUPACION_SEMESTRE, 'text' : 'Semestre'},
			{'value' : AGRUPACION_ANUAL, 'text' : 'Anual'},
		];	

		mi.agrupacion = mi.agrupaciones[0];
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
	    
	    mi.reiniciarVista=function(){
			if($location.path()=='/informacionPresupuestaria/rv')
				$route.reload();
			else
				$location.path('/informacionPresupuestaria/rv');
		}
	    
		mi.formatofecha = 'yyyy';
		
		mi.prestamos = [
			{'value' : 0, 'text' : 'Seleccionar una opción'},
		];
		
		mi.prestamo = mi.prestamos[0];
		
		mi.abrirPopupFecha = function(index) {
			switch(index){
				case 1000: mi.fi_abierto = true; break;
				case 1001: mi.ff_abierto = true; break;
			}
		};
		
		mi.fechaOptions = {
				formatYear: 'yyyy',
			    startingDay: 1,
			    minMode: 'year'
		};
		
		$http.post('/SProyecto',{accion: 'getProyectos'}).success(
			function(response) {
				mi.prestamos = [];
				mi.prestamos.push({'value' : 0, 'text' : 'Seleccione una opción'});
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
					mi.prestamo = mi.prestamos[0];
				}
		});
				
		mi.anterior = function(){
			var elemento = document.getElementById("divTablaDatos");
			if(mi.totalCabecerasAMostrar == 0){
				elemento.scrollLeft -= mi.tamanoCelda;
				document.getElementById("divCabecerasDatos").scrollLeft -= mi.tamanoCelda;
				mi.SiguienteActivo = true;
			}else{
			if(elemento.scrollLeft > 0){
				elemento.scrollLeft -= mi.tamanoCabecera;
				document.getElementById("divCabecerasDatos").scrollLeft -= mi.tamanoCabecera;
				mi.SiguienteActivo = true;
				if(elemento.scrollLeft <= 0){
					mi.AnteriorActivo = false;
				}
			}
		}
		}
		
		mi.siguiente = function(){
			var elemento = document.getElementById("divTablaDatos");
			if(mi.totalCabecerasAMostrar == 0){
				elemento.scrollLeft += mi.tamanoCelda;
				document.getElementById("divCabecerasDatos").scrollLeft += mi.tamanoCelda;
				mi.AnteriorActivo = true;
			}else{
			if(elemento.scrollLeft < ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
				elemento.scrollLeft += mi.tamanoCabecera;
				document.getElementById("divCabecerasDatos").scrollLeft += mi.tamanoCabecera;
				mi.AnteriorActivo = true;
				if(elemento.scrollLeft >= ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
					mi.SiguienteActivo = false;
				}
			}
		}
		}
		
		mi.validar = function(noElemento){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null && mi.fechaInicio.toString().length == 4 && 
						mi.fechaFin != null && mi.fechaFin.toString().length == 4)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(noElemento && noElemento == 2 && (mi.fechaFin - mi.fechaInicio)>mi.limiteAnios){ //fechaInicio
							mi.fechaInicio = mi.fechaFin - mi.limiteAnios;
							$utilidades.mensaje('warning','La diferencia de años no puede ser mayor a '+mi.limiteAnios);
						}else if(noElemento && noElemento == 3 && (mi.fechaFin - mi.fechaInicio)>mi.limiteAnios){ //fechaFin
							mi.fechaFin = mi.fechaInicio + mi.limiteAnios;
							$utilidades.mensaje('warning','La diferencia de años no puede ser mayor a '+mi.limiteAnios);
						}
						mi.generar(mi.agrupacionActual);
					}else{
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
					}
				}
			}
		}
		
		mi.verificaSeleccionTipo = function(tipo){
			mi.mostrarCargando = true;
			if(mi.grupoMostrado.planificado && mi.grupoMostrado.real){
				mi.estiloAlineacion="text-align: center;";
			}else{
				mi.estiloAlineacion="text-align: right; padding-right:15px;";
			}
			if(!mi.grupoMostrado.planificado && !mi.grupoMostrado.real){
				if(tipo==1){
					mi.grupoMostrado.real = true;
				}else{
					mi.grupoMostrado.planificado = true;
				}
			}
			mi.calcularTamaniosCeldas();
			mi.mostrarCargando = false;
		}
				
		mi.calcularTamaniosCeldas = function(){
			var tamanioMinimo = mi.tamanioMinimoColumna;
			if(mi.enMillones){
				tamanioMinimo = mi.tamanioMinimoColumnaMillones;
			}
			if(mi.grupoMostrado.planificado && mi.grupoMostrado.real){
				tamanioMinimo = tamanioMinimo * 2;
			}
			mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth) - 200;
			mi.totalAnios = Number(mi.fechaFin) - Number(mi.fechaInicio) + 1;
			mi.totalCabecerasAMostrar = $utilidades.getCantidadCabecerasReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabeceras, tamanioMinimo);
			if(mi.totalCabecerasAMostrar == 0){
				mi.tamanoCelda = tamanioMinimo;
				mi.tamanoTotal = mi.tamanoPantalla - (mi.tamanoCelda * (mi.totalAnios + 1));
				if(mi.tamanoTotal<0){mi.tamanoTotal=0;}
			}else{
				mi.tamanoCelda = $utilidades.getTamanioColumnaReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabecerasAMostrar);
				mi.tamanoTotal = mi.totalCabecerasAMostrar * mi.totalAnios * mi.tamanoCelda;
			}
			mi.estiloCelda = "width:"+ mi.tamanoCelda + "px;min-width:"+ mi.tamanoCelda + "px; max-width:"+ mi.tamanoCelda + "px;";
			mi.tamanoCabecera = mi.totalAnios * mi.tamanoCelda;
			mi.estiloCabecera = "width:"+ mi.tamanoCabecera + "px;min-width:" + mi.tamanoCabecera +"px; max-width:"+ mi.tamanoCabecera + "px; text-align: center;";
		}
		
		mi.cargarTabla = function(agrupacion) {			
			var datos = {
				accion : 'generarInforme',
				idPrestamo: mi.prestamo.value,
				anoInicial: mi.fechaInicio,
				anoFinal: mi.fechaFin
			};
		
			mi.mostrarCargando = true;
			mi.mostrarDescargar = false;
			
			$http.post('/SInformacionPresupuestaria', datos).then(function(response) {
				if (response.data.success) {
					mi.data = response.data.prestamo;
					var tab = "\t";
					 for (x in mi.data){
						 mi.data[x].nombre = tab.repeat(mi.data[x].objeto_tipo -1) + mi.data[x].nombre; 
					 }
					mi.renderizaTabla();
					mi.mostrarCargando = false;
					mi.mostrarDescargar = true;
					mi.movimiento = true;
					
					$timeout(function(){
						mi.mostrarCargando = false;
					})
				}
			});
	}
		
		mi.generar = function(agrupacion){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(agrupacion != 0){
							mi.cargarTabla();
							mi.agrupacionActual = agrupacion;
							
						}
					}else
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}else
					$utilidades.mensaje('warning','Favor de ingresar un año inicial y final válido');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
		
		mi.renderizaTabla = function(){
			mi.totalCabeceras = 1;
			switch (mi.agrupacionActual) {
			case 1: mi.totalCabeceras = 12; break;
			case 2: mi.totalCabeceras = 6; break;
			case 3: mi.totalCabeceras = 4; break;
			case 4: mi.totalCabeceras = 3; break;
			case 5: mi.totalCabeceras = 2; break;
			case 6: mi.totalCabeceras = 1; break;
			}
			
			mi.calcularTamaniosCeldas();
			
			mi.anios = [];
			for(var i = mi.fechaInicio; i <= mi.fechaFin; i++){
				mi.anios.push({anio: i});
			}
			mi.colspan = mi.anios.length;
			mi.aniosfinales = [];
			
			mi.columnas = [];
			if(mi.agrupacionActual == AGRUPACION_MES){
				for(var i = 0; i < mi.totalCabeceras; i++){
					mi.columnas.push({nombreMes: MES_DISPLAY_NAME[i]});
				}
			}else if(mi.agrupacionActual == AGRUPACION_BIMESTRE){
				for(var i = 0; i < mi.totalCabeceras; i++){
					mi.columnas.push({nombreMes: BIMESTRE_DISPLAY_NAME[i]});
				}
			}else if(mi.agrupacionActual == AGRUPACION_TRIMESTRE){
				for(var i = 0; i < mi.totalCabeceras; i++){
					mi.columnas.push({nombreMes: TRIMESTRE_DISPLAY_NAME[i]});
				}
			}else if(mi.agrupacionActual == AGRUPACION_CUATRIMESTRE){
				for(var i = 0; i < mi.totalCabeceras; i++){
					mi.columnas.push({nombreMes: CUATRIMESTRE_DISPLAY_NAME[i]});
				}
			}else if(mi.agrupacionActual == AGRUPACION_SEMESTRE){
				for(var i = 0; i < mi.totalCabeceras; i++){
					mi.columnas.push({nombreMes: SEMESTRE_DISPLAY_NAME[i]});
				}
			}else if(mi.agrupacionActual == AGRUPACION_ANUAL){
				for(var i = 0; i < mi.totalCabeceras; i++){
					mi.columnas.push({nombreMes: ANUAL_DISPLAY_NAME[i]});
				}
			}
			
			mi.objetoMostrar = [];
			for(var i =0; i < mi.columnas.length; i++){
				mi.objetoMostrar.push(mi.columnas[i]);
			}
			
			mi.aniosfinales = [];
			for(var i = 0; i < mi.columnas.length; i++){
				for(var j = 0; j < mi.anios.length; j++){
					mi.aniosfinales.push({anio: mi.anios[j].anio, nombre: mi.columnas[i].nombreMes.toLowerCase()});
				}
			}
			
			mi.aniosTotal = [];
			for(var j = 0; j < mi.anios.length; j++){
				mi.aniosTotal.push({anio: mi.anios[j].anio});
			}
			
			var tamanio = mi.columnas.length * mi.aniosTotal.length; 
			mi.columnastotales = new Array(tamanio);
			mi.totales = new Array(mi.data.length);
			
			var elemento = document.getElementById("divTablaDatos");
			if(elemento.scrollLeft >= ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
				mi.SiguienteActivo = false;
			}else{
				mi.SiguienteActivo = true;
			}
		}
		
		mi.getPlanificado=function(itemIndice, indice){
			mes = Math.floor((indice)/mi.aniosTotal.length);
			anio = indice - (mes*mi.aniosTotal.length);
			var item = mi.data[itemIndice];
			var valor = Object.values(item.anios[anio])[mes]; 
			/*
			if (totales[1]){
		    	console.log("vacio");
		    }else{
		    	console.log("crear");
		        totales[1] = new Array(3);
		        totales[1][1] = "hola";
		    }
			/*
			if(mi.totales[itemIndice]){
				var total = mi.totales[itemIndice];
				if (total[anio]){
					total[anio] += valor;
				}else{
					total[anio] = valor;
				} 
			}else{
				//mi.totales[itemIndice] = [anio.toString() : valor ];
			}
			
			if(mi.totales[indice]){
			      var total = mi.totales[indice];
			      if (total[anio]){
			        total[anio] += valor;
			      }else{
			        total[anio] = valor;
			      } 
			    }else{
			      var a= [];
			      a[anio] = valor;
			      mi.totales[indice] = a;
			    }
			    */
			
			return valor;
		};
}]);

app.directive('scrollespejo', ['$window', function($window) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('scroll', function() {
                var elemento = element[0];
                if (elemento.id == scope.divActivo){
      	          if(elemento.id == 'divTablaNombres'){
      	            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
      	            document.getElementById("divTotales").scrollTop = elemento.scrollTop ;
      	          }else if(elemento.id == 'divTablaDatos'){
      	            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
      	            document.getElementById("divTotales").scrollTop = elemento.scrollTop ;
      	          }else{
      	            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
      	            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
      	          }
      	        }
            });
            angular.element($window).bind('resize', function(){ 
                scope.controller.calcularTamaniosCeldas();
                scope.$digest();
              });
            scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
        }
    };
}])

;