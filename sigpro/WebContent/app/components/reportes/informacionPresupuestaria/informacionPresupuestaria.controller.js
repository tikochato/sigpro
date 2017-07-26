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
		
		var AGRUPACION_MES= 1;
		var AGRUPACION_BIMESTRE = 2;
		var AGRUPACION_TRIMESTRE = 3;
		var AGRUPACION_CUATRIMESTRE= 4;
		var AGRUPACION_SEMESTRE= 5;
		var AGRUPACION_ANUAL= 6;
		
		var MES_DISPLAY_NAME = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
		var BIMESTRE_DISPLAY_NAME = ['Bimestre 1', 'Bimestre 2','Bimestre 3','Bimestre 4','Bimestre 5','Bimestre 6'];
		var TRIMESTRE_DISPLAY_NAME = ['Trimestre 1', 'Trimestre 2', 'Trimestre 3', 'Trimestre 4'];
		var CUATRIMESTRE_DISPLAY_NAME = ['Cuatrimestre 1', 'Cuatrimestre 2', 'Cuatrimestre 3'];
		var SEMESTRE_DISPLAY_NAME = ['Semestre 1','Semestre 2'];
		var ANUAL_DISPLAY_NAME = ['Anual'];
		
		//**TODO: quitar generación dinamica de informacion
		//////////////////////**************************************************
		
		$scope.divActivo = "";
		mi.activarScroll = function(id){
			$scope.divActivo = id;
	    }
		
		var nameList = ['Pierre', 'Pol', 'Jacques', 'Robert', 'Elisa', 'Dupont', 'Germain', 'Delcourt', 'Erick', 'Menez'];
        
        $scope.rowCollection = [];
        $scope.datosTabla = [];
        
        function createRandomItem() {
            var
                firstName = Math.floor(Math.random() * 1000000000),
                lastName = Math.floor(Math.random() * 1000000000),
                age = Math.floor(Math.random() * 1000000000),
                email = Math.floor(Math.random() * 1000000000),
                balance = Math.floor(Math.random() * 1000000000),
            	nombre = nameList[Math.floor(Math.random() * 9)];

            return {
                1: lastName,
                2: age,
                3: email,
                4: balance,
                5: lastName,
                6: age,
                7: email,
                8: balance,
                9: lastName,
                10: age,
                11: email,
                12: balance,
                13: lastName,
                14: age,
                15: email,
                16: balance,
                17: lastName,
                18: age,
                19: email,
                20: balance,
                21: lastName,
                22: age,
                23: email,
                24: balance,
                25: lastName,
                26: age,
                27: email,
                28: balance,
                29: lastName,
                30: age,
                31: email,
                32: balance,
                33: lastName,
                34: age,
                35: email,
                36: email,
                nombre: nombre
            };
        }
        
        $scope.columns=['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36'];
                
        //**************************************************////////////////////
		
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
		
		mi.generar = function(agrupacion){
			if(mi.prestamo.value > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(agrupacion != 0){
							//**TODO: mostrar Cargando
							//mi.mostrarCargando = true;
							mi.agrupacionActual = agrupacion;
							mi.totalCabeceras = 1;
							switch (agrupacion) {
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
								mi.anios.push({ano: i});
							}
							mi.colspan = mi.anios.length;
							mi.aniosfinales = [];
							
							mi.columnas = [];
							if(agrupacion == AGRUPACION_MES){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: MES_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_BIMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: BIMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_TRIMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: TRIMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_CUATRIMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: CUATRIMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_SEMESTRE){
								for(var i = 0; i < mi.totalCabeceras; i++){
									mi.columnas.push({nombreMes: SEMESTRE_DISPLAY_NAME[i]});
								}
							}else if(agrupacion == AGRUPACION_ANUAL){
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
									mi.aniosfinales.push({ano: mi.anios[j].ano});
								}
							}
							
							mi.aniosTotal = [];
							for(var j = 0; j < mi.anios.length; j++){
								mi.aniosTotal.push({ano: mi.anios[j].ano});
							}
							
							
							//**TODO: quitar generación dinamica de informacion							
							//////////////////****************************
							
							mi.mostrarDescargar = true;
							mi.movimiento = true;
							$scope.rowCollection = [];
							for(var i=0;i<50;i++){
								$scope.rowCollection.push(createRandomItem());
						    }
							$scope.datosTabla = [].concat($scope.rowCollection);
							//****************************/////////////////////
							
							var elemento = document.getElementById("divTablaDatos");
							if(elemento.scrollLeft >= ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
								mi.SiguienteActivo = false;
							}else{
								mi.SiguienteActivo = true;
							}
						}
					}else
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}else
					$utilidades.mensaje('warning','Favor de ingresar un año inicial y final válido');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
		}
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