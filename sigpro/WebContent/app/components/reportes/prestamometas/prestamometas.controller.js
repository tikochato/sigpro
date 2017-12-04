var app = angular.module('prestamometasController', [ 'smart-table']);


app.controller('prestamometasController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	
	var mi = this;
	mi.fechaInicio = "";
	mi.fechaFin = "";
	mi.movimiento = false;
	mi.mostrarDescargar = false;
	mi.mostrarCargando = false;
	mi.SiguienteActivo = true;
	mi.AnteriorActivo = false;
	mi.enMillones = true;
	mi.agrupacionActual = 1
	mi.columnasTotal = 3;
	mi.limiteAnios = 5;
	mi.tamanioMinimoColumna = 125;
	mi.tamanioMinimoColumnaMillones = 60;
	mi.grupoMostrado= {"planificado":true,"real":true};
	mi.estiloAlineacion="text-align: center;";
	mi.porcentajeCeldaValor = "width: 45%; float: left;";
	mi.porcentajeCeldaPipe = "width: 10%; float: left;";
	mi.data = [];
	mi.dataOriginal = [];
	mi.totales = [];
	mi.scrollPosicion = 0;
	mi.prestamoId=null;
	
	mi.VALOR_PLANIFICADO= 0;
	mi.VALOR_REAL= 1;
	
	var AGRUPACION_MES= 1;
	var AGRUPACION_BIMESTRE = 2;
	var AGRUPACION_TRIMESTRE = 3;
	var AGRUPACION_CUATRIMESTRE= 4;
	var AGRUPACION_SEMESTRE= 5;
	var AGRUPACION_ANUAL= 6;
	
	var MES_DISPLAY_NAME = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
	var MES_DISPLAY_NAME_MIN = ['enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio', 'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'];
	var BIMESTRE_DISPLAY_NAME = ['Bimestre 1', 'Bimestre 2','Bimestre 3','Bimestre 4','Bimestre 5','Bimestre 6'];
	var TRIMESTRE_DISPLAY_NAME = ['Trimestre 1', 'Trimestre 2', 'Trimestre 3', 'Trimestre 4'];
	var CUATRIMESTRE_DISPLAY_NAME = ['Cuatrimestre 1', 'Cuatrimestre 2', 'Cuatrimestre 3'];
	var SEMESTRE_DISPLAY_NAME = ['Semestre 1','Semestre 2'];
	var ANUAL_DISPLAY_NAME = ['Anual'];
	
	
	$scope.divActivo = "";
		
	mi.iconoObjetoTipo = {
			10: "glyphicon glyphicon-scale",
			0: "glyphicon glyphicon-record",
		    1: "glyphicon glyphicon-th",
		    2: "glyphicon glyphicon-equalizer",
		    3: "glyphicon glyphicon-certificate",
		    4: "glyphicon glyphicon-link",
		    5: "glyphicon glyphicon-time",
		};
		
		mi.tooltipObjetoTipo = {
			10: "Meta",
		    0: "Proyecto",
		    1: "Componente",
		    2: "Subcomponente",
		    3: "Producto",
		    4: "Subproducto",
		    5: "Actividad",
		};
		
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
			if($location.path()=='/prestamometas/rv')
				$route.reload();
			else
				$location.path('/prestamometas/rv');
		}
	    
	$window.document.title = $utilidades.sistema_nombre+' - Avance de Metas';
	i18nService.setCurrentLang('es');
		
	mi.formatofecha = 'yyyy';
	
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
				$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
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
		
		mi.blurLineaBase=function(){
			if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
			}
		};
		
		mi.cambioLineaBase=function(selected){
			if(selected!== undefined){
				mi.lineaBaseNombre = selected.originalObject.nombre;
				mi.lineaBaseId = selected.originalObject.id;
				mi.validar(1);
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
			mi.scrollPosicion = elemento.scrollLeft;
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
			mi.scrollPosicion = elemento.scrollLeft;
		}
		mi.exportarPdf=function(){
			 var tipoVisualizacion = 0;
			 if (mi.grupoMostrado.planificado && mi.grupoMostrado.real){
				 tipoVisualizacion = 2;
			 }else if(mi.grupoMostrado.real){
				 tipoVisualizacion = 1;
			 }
			$http.post('/SPrestamoMetas', { 
				accion: 'exportarPdf',
				proyectoid: mi.pepId,
				fechaInicio: mi.fechaInicio,
				fechaFin: mi.fechaFin,
				agrupacion: mi.agrupacionActual,
				tipoVisualizacion: tipoVisualizacion,
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
				t:moment().unix()
			  } ).then(
					  function successCallback(response) {
							var anchor = angular.element('<a/>');
						    anchor.attr({
						         href: 'data:application/pdf;base64,' + response.data,
						         target: '_blank',
						         download: 'AvanceDeMetas.pdf'
						     })[0].click();
						  }.bind(this), function errorCallback(response){
						 	}
						 );
		};
		
		mi.validar = function(noElemento){
			if(mi.pepId != null && mi.pepId > 0)
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
			mi.movimiento = false;
			if(mi.grupoMostrado.planificado && mi.grupoMostrado.real){
				mi.estiloAlineacion="text-align: center;";
				mi.porcentajeCeldaValor = "width: 45%; float: left;";
				mi.porcentajeCeldaPipe = "width: 10%; float: left;";
			}else{
				mi.estiloAlineacion="text-align: right; padding-right:15px;";
				mi.porcentajeCeldaValor = "";
				mi.porcentajeCeldaPipe = "";
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
			mi.movimiento = true;
		}
				
		mi.calcularTamaniosCeldas = function(){
			var tamanioMinimo = mi.tamanioMinimoColumna;
			if(mi.enMillones){
				tamanioMinimo = mi.tamanioMinimoColumnaMillones;
			}
			if(mi.grupoMostrado.planificado && mi.grupoMostrado.real){
				tamanioMinimo = tamanioMinimo * 2;
			}
			mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth) - 400;
			mi.totalAnios = Number(mi.fechaFin) - Number(mi.fechaInicio) + 1;
			mi.totalCabecerasAMostrar = $utilidades.getCantidadCabecerasReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabeceras, tamanioMinimo, mi.columnasTotal);
			if(mi.totalCabecerasAMostrar == 0){
				mi.tamanoCelda = tamanioMinimo;
				mi.tamanoTotal = mi.tamanoPantalla - (mi.tamanoCelda * (mi.totalAnios + mi.columnasTotal));
				if(mi.tamanoTotal<0){mi.tamanoTotal=0;}
			}else{
				mi.tamanoCelda = $utilidades.getTamanioColumnaReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabecerasAMostrar, mi.columnasTotal);
				mi.tamanoTotal = mi.totalCabecerasAMostrar * mi.totalAnios * mi.tamanoCelda;
			}
			mi.estiloCelda = "width:"+ mi.tamanoCelda + "px;min-width:"+ mi.tamanoCelda + "px; max-width:"+ mi.tamanoCelda + "px;";
			mi.tamanoCabecera = mi.totalAnios * mi.tamanoCelda;
			mi.estiloCabecera = "width:"+ mi.tamanoCabecera + "px;min-width:" + mi.tamanoCabecera +"px; max-width:"+ mi.tamanoCabecera + "px; text-align: center;";
		}
		
		mi.cargarTabla = function() {			
			var datos = {
				accion : 'getMetasProducto',
				idPrestamo: mi.pepId,
				anioInicial: mi.fechaInicio,
				anioFinal: mi.fechaFin,
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null
			};
		
			mi.mostrarCargando = true;
			mi.mostrarDescargar = false;
			mi.movimiento = false;
			
			$http.post('/SPrestamoMetas', datos)
			.then(function(response) {
				if (response.data.success) {
					mi.dataOriginal = JSON.parse(JSON.stringify(response.data.prestamo));
					mi.data = JSON.parse(JSON.stringify(response.data.prestamo));
					mi.totales = [];
					 for (x in mi.data){
						 var totalFinal = {"planificado": null, "real": null};
						 var fila = [];
						 if(mi.data[x].objeto_tipo == 10){
							 totalFinal = {"planificado": 0, "real": 0};
							 for(a in mi.data[x].anios){
								 var totalAnual = {"planificado": 0, "real": 0};
								 var anio = mi.data[x].anios[a];
								 for (m in anio){
									 if(m != "anio"){
										 totalAnual.planificado += anio[m][mi.VALOR_PLANIFICADO];
										 totalAnual.real += anio[m][mi.VALOR_REAL];
									 }
									 
								 }
								 totalFinal.planificado += totalAnual.planificado;
								 totalFinal.real += totalAnual.real;
								 totalAnual.planificado = parseFloat(totalAnual.planificado).toFixed(2);
								 totalAnual.real = parseFloat(totalAnual.real).toFixed(2);
								 var tot = {"valor": totalAnual};
								 fila.push(tot);
								 mi.data[x].anios[a] = mi.agruparMeses(anio);
								 totalFinal.planificado = parseFloat(totalFinal.planificado).toFixed(2);
								 totalFinal.real = parseFloat(totalFinal.real).toFixed(2);
							 }
						 }else{
							 for(a in mi.data[x].anios){
								 var totalAnual = {"planificado": null, "real": null};
								 var tot = {"valor": totalAnual};
								 fila.push(tot);
							 }
						 }
						 var tot = {"valor": totalFinal};
						 fila.push(tot);
						 var tot = {"anio": fila};
						 mi.totales.push(tot);
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
		
		mi.agruparMeses = function(anio){
			if(mi.agrupacionActual != AGRUPACION_MES){
				var anioN = {};
				if(mi.agrupacionActual == AGRUPACION_BIMESTRE){
					anioN = {
							"bimestre1" : [anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_REAL]],
							"bimestre2" : [anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_REAL]],
							"bimestre3" : [anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_REAL]],
							"bimestre4" : [anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_REAL]],
							"bimestre5" : [anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_REAL]],
							"bimestre6" : [anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_REAL]],
					}
				}else if(mi.agrupacionActual == AGRUPACION_TRIMESTRE){
					anioN = {
							"trimestre1" : [anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_REAL]],
							"trimestre2" : [anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_REAL]],
							"trimestre3" : [anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_REAL]],
							"trimestre4" : [anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_PLANIFICADO],anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_REAL]]
					}
				}else if(mi.agrupacionActual == AGRUPACION_CUATRIMESTRE){
					anioN = {
							"cuatrimestre1" : [anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_PLANIFICADO]
												,anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_REAL]],
							"cuatrimestre2" : [anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_PLANIFICADO]
												,anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_REAL]],
							"cuatrimestre3" : [anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_PLANIFICADO]
												,anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_REAL]]
					}
				}else if(mi.agrupacionActual == AGRUPACION_SEMESTRE){
					anioN = {
							"semestre1" : [anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_PLANIFICADO]
											,anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_REAL]],
							"semestre2" : [anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_PLANIFICADO]
											,anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_REAL]],
					}
				}else if(mi.agrupacionActual == AGRUPACION_ANUAL){
					anioN = {
							"anual1" : [anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_PLANIFICADO]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_PLANIFICADO]
							,anio[MES_DISPLAY_NAME_MIN[0]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[1]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[2]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[3]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[4]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[5]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[6]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[7]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[8]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[9]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[10]][mi.VALOR_REAL]+anio[MES_DISPLAY_NAME_MIN[11]][mi.VALOR_REAL]],
					}
				}
				anio = anioN;
			}
			return anio;
		}
		
		mi.cambiarAgrupacion = function(agrupacion){
			if(mi.pepId > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(agrupacion != 0){
							mi.data = JSON.parse(JSON.stringify(mi.dataOriginal));
							mi.agrupacionActual = agrupacion;
							for (x in mi.data){
								 if(mi.data[x].objeto_tipo == 10){
									 for(a in mi.data[x].anios){
										 var anio = mi.data[x].anios[a];
										 mi.data[x].anios[a] = mi.agruparMeses(anio);
									 }
								 }
							 }
							mi.renderizaTabla();
						}
					}else
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}else
					$utilidades.mensaje('warning','Favor de ingresar un año inicial y final válido');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un '+$rootScope.etiquetas.proyecto);
		}
		
		mi.generar = function(agrupacion){
			if(mi.pepId > 0)
			{
				if(mi.fechaInicio != null && mi.fechaFin != null)
				{
					if (mi.fechaFin >= mi.fechaInicio){
						if(agrupacion != 0){
							mi.agrupacionActual = agrupacion;
							mi.cargarTabla();
							
						}
					}else
						$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}else
					$utilidades.mensaje('warning','Favor de ingresar un año inicial y final válido');
			}else
				$utilidades.mensaje('warning','Debe de seleccionar un '+$rootScope.etiquetas.proyecto);
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

			mi.anios = [];
			for(var i = mi.fechaInicio; i <= mi.fechaFin; i++){
				mi.anios.push({anio: i});
			}
			mi.colspan = mi.anios.length;
			mi.aniosfinales = [];
			
			mi.aniosTotal = [];
			for(var j = 0; j < mi.anios.length; j++){
				mi.aniosTotal.push({anio: mi.anios[j].anio});
			}
			
			mi.calcularTamaniosCeldas();
			
			
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
			
			var tamanio = mi.columnas.length * mi.aniosTotal.length; 
			mi.columnastotales = new Array(tamanio);
			
			var elemento = document.getElementById("divTablaDatos");
			if(elemento.scrollLeft >= ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
				mi.SiguienteActivo = false;
			}else{
				mi.SiguienteActivo = true;
			}
		}
		
		mi.getPlanificado=function(itemIndice, indice, tipoMeta){
			mes = Math.floor((indice)/mi.aniosTotal.length);
			anio = indice - (mes*mi.aniosTotal.length);
			var item = mi.data[itemIndice];
			var valor = Object.values(item.anios[anio])[mes];
			if(valor[tipoMeta]==null && mi.data[itemIndice].objeto_tipo==10){
				return 0;
			}
			return valor[tipoMeta];
		};
		
		angular.element($window).bind('resize', function(){ 
            mi.calcularTamaniosCeldas();
            $scope.$digest();
          });
        $scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
		
		mi.exportarExcel = function(){
			 var tipoVisualizacion = 0;
			 if (mi.grupoMostrado.planificado && mi.grupoMostrado.real){
				 tipoVisualizacion = 2;
			 }else if(mi.grupoMostrado.real){
				 tipoVisualizacion = 1;
			 }
			 $http.post('/SPrestamoMetas', { 
				 accion: 'exportarExcel', 
				 proyectoid: mi.pepId,
				 fechaInicio: mi.fechaInicio,
				 fechaFin: mi.fechaFin,
				 agrupacion: mi.agrupacionActual,
				 tipoVisualizacion: tipoVisualizacion,
				 lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
				 t:moment().unix()
			  } ).then(
					  function successCallback(response) {
						  var anchor = angular.element('<a/>');
						  anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'AvanceDeMetas.xls'
						  })[0].click();
					  }.bind(this), function errorCallback(response){
				 	}
			  	);
			};
		
		
}]);

		