var app = angular.module('gestionAdquisicionesController',['ngTouch','ngAnimate']);
app.controller('gestionAdquisicionesController',['$scope', '$rootScope', '$http','$window', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q',
	function($scope, $rootScope, $http, $window, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q){
	var mi = this;
	mi.tooltipObjetoTipo = [$rootScope.etiquetas.proyecto,"Componente","Subcomponente","Producto","Sub Producto","Actividad"];
	var anioFiscal = new Date();
	mi.anio = anioFiscal.getFullYear();
	mi.enMillones = true;
	mi.data = [];
	mi.dataOriginal=[];
	mi.totales = [];
	mi.scrollPosicion = 0;
	mi.tamanioMinimoColumna = 130;
	mi.tamanioMinimoColumnaMillones = 80;
	mi.grupoMostrado= {"planificado":true};
	mi.estiloAlineacion="text-align: center;";
	
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
			mi.validar(1);
		}
		else{
			mi.lineaBaseNombre="";
			mi.lineaBaseId=null;
		}
	};
	
	mi.getPeps = function(prestamoId){
		$http.post('/SProyecto',{accion: 'getProyectos', prestamoid: prestamoId, t: (new Date()).getTime()}).success(
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
	
	mi.validar = function(noElemento){
		if(mi.pepId > 0)
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
					}else
						mi.generar(mi.agrupacionActual);
				}else{
					$utilidades.mensaje('warning','La fecha inicial es mayor a la fecha final');
				}
			}
		}
		
	}
	
	mi.movimiento = false;
	mi.agrupacionActual = 1
	mi.columnasTotal = 3;
	mi.SiguienteActivo = true;
	mi.AnteriorActivo = false;
	
	var AGRUPACION_MES= 1;
	var AGRUPACION_BIMESTRE = 2;
	var AGRUPACION_TRIMESTRE = 3;
	var AGRUPACION_CUATRIMESTRE= 4;
	var AGRUPACION_SEMESTRE= 5;
	var AGRUPACION_ANUAL= 6;
	
	var MES_DISPLAY_NAME = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
	var MES_DISPLAY_NAME_GRAFICA = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
	var BIMESTRE_DISPLAY_NAME = ['Bimestre 1', 'Bimestre 2','Bimestre 3','Bimestre 4','Bimestre 5','Bimestre 6'];
	var TRIMESTRE_DISPLAY_NAME = ['Trimestre 1', 'Trimestre 2', 'Trimestre 3', 'Trimestre 4'];
	var CUATRIMESTRE_DISPLAY_NAME = ['Cuatrimestre 1', 'Cuatrimestre 2', 'Cuatrimestre 3'];
	var SEMESTRE_DISPLAY_NAME = ['Semestre 1','Semestre 2'];
	var ANUAL_DISPLAY_NAME = ['Anual'];
	
	mi.agrupaciones = [
		{'value' : 0, 'text' : 'Seleccione una opción'},
		{'value' : AGRUPACION_MES, 'text' : 'Mensual'},
		{'value' : AGRUPACION_BIMESTRE, 'text' : 'Bimestre'},
		{'value' : AGRUPACION_TRIMESTRE, 'text' : 'Trimestre'},
		{'value' : AGRUPACION_CUATRIMESTRE, 'text' : 'Cuatrimestre'},
		{'value' : AGRUPACION_SEMESTRE, 'text' : 'Semestre'},
		{'value' : AGRUPACION_ANUAL, 'text' : 'Anual'},
	];	
	
	mi.anterior = function(){
		var elemento = document.getElementById("divTablaDatos");
		var elemento2 = document.getElementById("divTablaDatosTot");
		if(mi.totalCabecerasAMostrar == 0){
			elemento.scrollLeft -= mi.tamanoCelda;
			elemento2.scrollLeft -= mi.tamanoCelda;
			document.getElementById("divCabecerasDatos").scrollLeft -= mi.tamanoCelda;
			mi.SiguienteActivo = true;
		}else{
			if(elemento.scrollLeft > 0){
				elemento.scrollLeft -= mi.tamanoCabecera;
				elemento2.scrollLeft -= mi.tamanoCabecera;
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
		var elemento2 = document.getElementById("divTablaDatosTot");
		if(mi.totalCabecerasAMostrar == 0){
			elemento.scrollLeft += mi.tamanoCelda;
			elemento2.scrollLeft += mi.tamanoCelda;
			document.getElementById("divCabecerasDatos").scrollLeft += mi.tamanoCelda;
			mi.AnteriorActivo = true;
		}else{
			if(elemento.scrollLeft < ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
				elemento.scrollLeft += mi.tamanoCabecera;
				elemento2.scrollLeft += mi.tamanoCabecera;
				document.getElementById("divCabecerasDatos").scrollLeft += mi.tamanoCabecera;
				mi.AnteriorActivo = true;
				if(elemento.scrollLeft >= ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
					mi.SiguienteActivo = false;
				}
			}
		}
		mi.scrollPosicion = elemento.scrollLeft;
	}
	
	mi.verificaSeleccionTipo = function(tipo){
		mi.mostrarCargando = true;
		if(mi.grupoMostrado.planificado){
			mi.estiloAlineacion="text-align: center;";
		}else{
			mi.estiloAlineacion="text-align: right; padding-right:15px;";
		}
		if(!mi.grupoMostrado.planificado){
			if(!tipo==1){
				mi.grupoMostrado.planificado = true;
			}
		}
		mi.calcularTamaniosCeldas();
		mi.mostrarCargando = false;
	}
	
	mi.cambiarAgrupacion = function(agrupacion){
		if(mi.pepId > 0)
		{
			if(agrupacion != 0){
				mi.data = JSON.parse(JSON.stringify(mi.dataOriginal));
				mi.agrupacionActual = agrupacion;
				for (x in mi.data){
					 for(a in mi.data[x].anioPlan){
						 var anio = mi.data[x].anioPlan[a];
						 mi.data[x].anioPlan[a] = mi.agruparMeses(anio);
					 }
				}
				mi.renderizaTabla();
				mi.calcularTotalColumnas();
			}
		}else
			$utilidades.mensaje('warning','Debe de seleccionar un '+$rootScope.etiquetas.proyecto);
	}
	
	mi.agruparMeses = function(anio){
		if(mi.agrupacionActual != AGRUPACION_MES){
			var anioN = {};
			if(mi.agrupacionActual == AGRUPACION_BIMESTRE){
				anioN.mes= [
						{"planificado": anio.mes[0].planificado+anio.mes[1].planificado},
						{"planificado": anio.mes[2].planificado+anio.mes[3].planificado},
						{"planificado": anio.mes[4].planificado+anio.mes[5].planificado},
						{"planificado": anio.mes[6].planificado+anio.mes[7].planificado},
						{"planificado": anio.mes[8].planificado+anio.mes[9].planificado},
						{"planificado": anio.mes[10].planificado+anio.mes[11].planificado}
				];
			}else if(mi.agrupacionActual == AGRUPACION_TRIMESTRE){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado,},
					{"planificado": anio.mes[3].planificado+anio.mes[4].planificado+anio.mes[5].planificado,},
					{"planificado": anio.mes[6].planificado+anio.mes[7].planificado+anio.mes[8].planificado,},
					{"planificado": anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado}
				]
			}else if(mi.agrupacionActual == AGRUPACION_CUATRIMESTRE){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado+anio.mes[3].planificado},
					{"planificado": anio.mes[4].planificado+anio.mes[5].planificado+anio.mes[6].planificado+anio.mes[7].planificado},
					{"planificado": anio.mes[8].planificado+anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado}
				]
			}else if(mi.agrupacionActual == AGRUPACION_SEMESTRE){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado+anio.mes[3].planificado+anio.mes[4].planificado+anio.mes[5].planificado},
					{"planificado": anio.mes[6].planificado+anio.mes[7].planificado+anio.mes[8].planificado+anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado}
				]
			}else if(mi.agrupacionActual == AGRUPACION_ANUAL){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado+anio.mes[3].planificado+anio.mes[4].planificado+anio.mes[5].planificado+anio.mes[6].planificado+anio.mes[7].planificado+anio.mes[8].planificado+anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado},
				]
			}
		}
		return {"anio" : anio.anio, "mes" : anioN != null ? anioN.mes : anio.mes};
	}
	
	mi.agrupacion = mi.agrupaciones[0];
	
	mi.generar = function(agrupacion){
		if(mi.pepId > 0){
			mi.mostrarCargando = true;
			mi.mostrarTablas = false;
			mi.mostrarDescargar = false;
			$http.post('/SGestionAdquisiciones',{
				accion: 'generarGestion',
				proyectoId: mi.pepId,
				fechaInicio: mi.fechaInicio,
				fechaFin: mi.fechaFin,
				lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
				anio: mi.anio
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.proyecto, agrupacion);
				}
			});
		}
	}
	
	mi.crearArbol = function(datos, agrupacion){
		mi.data = datos;
		
		mi.rowCollectionPrestamo = [];
		mi.rowCollectionPrestamo = mi.data;
		mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
		mi.mostrarCargando = false;
		mi.mostrarTablas = true;
		
		mi.anchoPantalla = Math.floor(document.getElementById("reporte").offsetHeight);
		mi.tamanoCargando = (mi.anchoPantalla * 0.75) - 30;
		
		mi.dataOriginal = JSON.parse(JSON.stringify(mi.data));
		mi.data = JSON.parse(JSON.stringify(mi.data));
		mi.totales = [];
		
		for (x in mi.data){
			 var totalFinalPlanificado = 0;
			 var fila = [];
			 var tot = null;
			 for(a in mi.data[x].anioPlan){
				 var totalAnualPlanificado = 0;
				 var anio = mi.data[x].anioPlan[a];
				 for (m in anio.mes){
					totalAnualPlanificado += isNaN(anio.mes[m].planificado) ? 0 : anio.mes[m].planificado;
				 }
				 totalFinalPlanificado += totalAnualPlanificado;
				 tot = {"valor": {"planificado": totalAnualPlanificado}};
				 fila.push(tot);
			 }
			 if(mi.data[x].objetoTipo != null){
				 if(tot == null){
					 tot = {"valor": {"planificado": totalFinalPlanificado}};
					 fila.push(tot);					 
				 }
				 tot = {"anio": fila};
				 mi.totales.push(tot); 
			 }else{
				 tot = {"valor": {"planificado" : null}}
				 fila.push(tot);
				 tot = {"anio": fila};
				 mi.totales.push(tot); 
			 }
		}
		
		mi.calcularTotalColumnas();
		mi.calcularTotalAnual();
		mi.calcularTotalGeneral();
		mi.calcularTotalAcumulado();
		mi.calcularTotalCantidad();
		 
		mi.renderizaTabla();
		mi.mostrarCargando = false;
		mi.mostrarDescargar = true;
		mi.movimiento = true;
		
		$timeout(function(){
			mi.mostrarCargando = false;
		})
		
		mi.cambiarAgrupacion(agrupacion);
	}
	
	mi.calcularTotalColumnas = function(){
		var arregloTotal = [];
		var totalanios = (mi.fechaFin - mi.fechaInicio) + 1;
		mi.sumTotales = [];
		for(var h=0;h<totalanios; h++){
			switch(mi.agrupacionActual){
			case AGRUPACION_MES:
				mi.sumTotales.push({"anio": (mi.fechaInicio + h), "total": [0,0,0,0,0,0,0,0,0,0,0,0]})
				break;
			case AGRUPACION_BIMESTRE:
				mi.sumTotales.push({"anio": (mi.fechaInicio + h), "total": [0,0,0,0,0,0]})
				break;
			case AGRUPACION_TRIMESTRE:
				mi.sumTotales.push({"anio": (mi.fechaInicio + h), "total": [0,0,0,0]})
				break;
			case AGRUPACION_CUATRIMESTRE:
				mi.sumTotales.push({"anio": (mi.fechaInicio + h), "total": [0,0,0]})
				break;
			case AGRUPACION_SEMESTRE:
				mi.sumTotales.push({"anio": (mi.fechaInicio + h), "total": [0,0]})
				break;
			case AGRUPACION_ANUAL:
				mi.sumTotales.push({"anio": (mi.fechaInicio + h), "total": [0]})
				break;
			}
		}
		
		for(p in mi.data){
			for(a in mi.data[p].anioPlan){
				switch(mi.agrupacionActual){
					case AGRUPACION_MES:
						if(mi.sumTotales[a].anio==mi.data[p].anioPlan[a].anio){
							for(var i=0; i<=11; i++){
								var value = mi.data[p].anioPlan[a].mes[i].planificado;
								mi.sumTotales[a].total[i] += value; 	
							}
						}							
						break;
					case AGRUPACION_BIMESTRE:
						if(mi.sumTotales[a].anio==mi.data[p].anioPlan[a].anio){
							for(var i=0; i<=5; i++){
								var value = mi.data[p].anioPlan[a].mes[i].planificado;
								mi.sumTotales[a].total[i] += value; 	
							}
						}
						break;
					case AGRUPACION_TRIMESTRE:
						if(mi.sumTotales[a].anio==mi.data[p].anioPlan[a].anio){
							for(var i=0; i<=3; i++){
								var value = mi.data[p].anioPlan[a].mes[i].planificado;
								mi.sumTotales[a].total[i] += value; 	
							}
						}
						break;
					case AGRUPACION_CUATRIMESTRE:
						if(mi.sumTotales[a].anio==mi.data[p].anioPlan[a].anio){
							for(var i=0; i<=2; i++){
								var value = mi.data[p].anioPlan[a].mes[i].planificado;
								mi.sumTotales[a].total[i] += value; 	
							}
						}
						break;
					case AGRUPACION_SEMESTRE:
						if(mi.sumTotales[a].anio==mi.data[p].anioPlan[a].anio){
							for(var i=0; i<=1; i++){
								var value = mi.data[p].anioPlan[a].mes[i].planificado;
								mi.sumTotales[a].total[i] += value; 	
							}
						}
						break;
					case AGRUPACION_ANUAL:
						if(mi.sumTotales[a].anio==mi.data[p].anioPlan[a].anio){
							for(var i=0; i<1; i++){
								var value = mi.data[p].anioPlan[a].mes[i].planificado;
								mi.sumTotales[a].total[i] += value; 	
							}
						}
						break;
				}
			}
		}			
	}
	
	mi.calcularTotalAnual = function(){
		mi.sumTotalesAnuales = [];
		var totalanios = (mi.fechaFin - mi.fechaInicio) + 1;
		
		for(var i = 0; i < totalanios; i++){
			mi.sumTotalesAnuales.push({"anio" : (mi.fechaInicio + i), "total" : 0});
		}
		
		for(x in mi.data){
			for(y in mi.data[x].anioTotalGestion){
				if(mi.data[x].anioTotalGestion[y].anio == mi.sumTotalesAnuales[y].anio){
					 mi.sumTotalesAnuales[y].total += mi.data[x].anioTotalGestion[y].total[0].planificado; 
				}
			}
		}
	}
	
	mi.calcularTotalAcumulado = function(){
		mi.sumTotalesAcumulados = [];
		//TODO: acumulado
	}
	
	mi.calcularTotalGeneral = function(){
		mi.totalGeneral = 0;
		for(x in mi.data){
			mi.totalGeneral += mi.data[x].total;
		}
	}
	
	mi.calcularTotalAcumulado = function(){
		mi.totalAcumulado = 0;
		for(x in mi.data){
			mi.totalAcumulado += mi.data[x].acumulado;	
		}
	}
	
	mi.calcularTotalCantidad = function(){
		mi.totalCantidad = 0;
		for(x in mi.data){
			mi.totalCantidad += mi.data[x].cantidadAdquisiciones;	
		}
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

	mi.getPlanificado = function(item, indice){
		mes = Math.floor((indice)/mi.aniosTotal.length);
		anio = indice - (mes*mi.aniosTotal.length);
		var valor;
		if(item.anioPlan != null){
			valor = item.anioPlan[anio].mes[mes];
		}
		
		if(valor != undefined && valor.planificado == 0){
			valor.planificado = null;
		}
		
		return valor;
	};
	
	mi.getTotalPlanificado = function(item, indice){
		mes = Math.floor((indice)/mi.aniosTotal.length);
		anio = indice - (mes*mi.aniosTotal.length);
		var valor;
		if(item.anioTotalGestion != null){
			valor = item.anioTotalGestion[anio].total[0];
		}
		
		if(valor != undefined && valor.planificado == 0){
			valor.planificado = null;
		}
		
		return valor;
	};
	
	mi.getTotales = function(itemIndice, indice){
		mes = Math.floor((indice)/mi.aniosTotal.length);
		anio = indice - (mes*mi.aniosTotal.length);
		var item = mi.sumTotales[anio];
		if(item.total != null){
			valor = item.total[mes];
		}
		return valor;
	}
	
	mi.getTotalesAnuales = function(indice){
		mes = Math.floor((indice)/mi.aniosTotal.length);
		anio = indice - (mes*mi.aniosTotal.length);
		
		var item = mi.sumTotalesAnuales[anio];
		if(item.total != null){
			return item.total;
		}
	}
	
	mi.getTotalesAcumulado = function(indice){
		//TODO: acumulado
		return 0;
	}
	
	mi.getTotalColumna = function(indice){
		var val = mi.t[indice];
		return val;
	}
	
	mi.getTamanioColumnaReporte = function(areaReporte, totalAnios, cabecerasAMostrar, columnasTotal){
		var columnasAMostrar = (cabecerasAMostrar * totalAnios);
		var tamanioPropuesto = (areaReporte / columnasAMostrar);
		return Math.floor(tamanioPropuesto);
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
		mi.totalCabecerasAMostrar = $utilidades.getCantidadCabecerasReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabeceras, tamanioMinimo, mi.columnasTotal);
		if(mi.totalCabecerasAMostrar == 0){
			mi.tamanoCelda = tamanioMinimo;
			mi.tamanoTotal = mi.tamanoPantalla - (mi.tamanoCelda * (mi.totalAnios +  mi.columnasTotal));
			if(mi.tamanoTotal<0){mi.tamanoTotal=0;}
		}else{
			mi.tamanoCelda = $utilidades.getTamanioColumnaReporte(mi.tamanoPantalla, mi.totalAnios, mi.totalCabecerasAMostrar,  mi.columnasTotal);
			mi.tamanoTotal = mi.totalCabecerasAMostrar * mi.totalAnios * mi.tamanoCelda;
		}
		mi.estiloCelda = "width:"+ mi.tamanoCelda + "px;min-width:"+ mi.tamanoCelda + "px; max-width:"+ mi.tamanoCelda + "px;";
		mi.tamanoCabecera = mi.totalAnios * mi.tamanoCelda;
		mi.estiloCabecera = "width:"+ mi.tamanoCabecera + "px;min-width:" + mi.tamanoCabecera +"px; max-width:"+ mi.tamanoCabecera + "px; text-align: center;";
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
        mi.calcularTamaniosCeldas();
        $scope.$digest();
      });
    $scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
    
    mi.exportarExcel = function(){
		$http.post('/SGestionAdquisiciones', { 
			accion: 'exportarExcel', 
			agrupacion: mi.agrupacionActual,
			fechaInicio: mi.fechaInicio,
			fechaFin: mi.fechaFin,
			lineaBase: mi.lineaBaseId != null ? "|lb"+mi.lineaBaseId+"|" : null,
			tipoVisualizacion: 0,
			idPrestamo: mi.pepId,
			t:moment().unix()
		}).then(
			function successCallback(response) {
				var anchor = angular.element('<a/>');
				anchor.attr({
					href: 'data:application/ms-excel;base64,' + response.data,
					target: '_blank',
					download: 'GestionDeAdquisiciones.xls'
				})[0].click();
			}.bind(this), function errorCallback(response){ }
		);
	};
}]);