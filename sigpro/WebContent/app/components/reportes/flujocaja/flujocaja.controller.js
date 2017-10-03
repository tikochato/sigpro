var app = angular.module('flujocajaController', [ 'smart-table']);


app.controller('flujocajaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {

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
	mi.columnasTotal = 1;
	mi.limiteAnios = 5;
	mi.tamanioMinimoColumna = 125;
	mi.tamanioMinimoColumnaMillones = 60;
	mi.estiloAlineacion="text-align: right;";
	mi.data = [];
	mi.dataOriginal = [];
	mi.totales = [];
	mi.resumenTotalesOriginal = [];
	mi.resumenTotales = [];		
	mi.scrollPosicion = 0;
	
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
	mi.activarScroll = function(id){
		$scope.divActivo = id;
	}

	mi.iconoObjetoTipo = {
			1: "glyphicon glyphicon-record",
			2: "glyphicon glyphicon-th",
			3: "glyphicon glyphicon-certificate",
			4: "glyphicon glyphicon-link",
			5: "glyphicon glyphicon-th-list",
	};

	mi.tooltipObjetoTipo = {
			1: "Proyecto",
			2: "Componente",
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
		if($location.path()=='/flujocaja/rv')
			$route.reload();
		else
			$location.path('/flujocaja/rv');
	}

	$window.document.title = $utilidades.sistema_nombre+' - Flujo de Caja';
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

	mi.calcularTamaniosCeldas = function(){
		var tamanioMinimo = mi.tamanioMinimoColumna;
		if(mi.enMillones){
			tamanioMinimo = mi.tamanioMinimoColumnaMillones;
		}
		mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth) - 300;
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
		mi.tamanioNombre = (mi.tamanoPantalla+200) -(((mi.totalCabecerasAMostrar*mi.totalAnios)+1) * mi.tamanoCelda);
	}

	mi.cargarTabla = function() {			
		var datos = {
				accion : 'getFlujoCaja',
				idPrestamo: mi.prestamo.value,
				anioInicial: mi.fechaInicio,
				anioFinal: mi.fechaFin,
				t: (new Date()).getTime()
		};

		mi.mostrarCargando = true;
		mi.mostrarDescargar = false;
		mi.movimiento = false;

		$http.post('/SFlujoCaja', datos)
		.then(function(response) {
			if (response.data.success) {
				mi.dataOriginal = JSON.parse(JSON.stringify(response.data.prestamo));
				mi.data = JSON.parse(JSON.stringify(response.data.prestamo));
				mi.resumenTotalesOriginal = JSON.parse(JSON.stringify(response.data.totales));
				mi.resumenTotales = JSON.parse(JSON.stringify(response.data.totales));
				mi.totales = [];
				for (x in mi.data){
					if(mi.data[x].objeto_tipo==1){
						
					}
					 var totalFinalPlanificado = 0;
					 var totalFinalReal = 0;
					 var fila = [];
					 for(a in mi.data[x].anios){
						 var totalAnualPlanificado = 0;
						 var totalAnualReal = 0;
						 var anio = mi.data[x].anios[a];
						 for (m in anio.mes){
							totalAnualPlanificado += isNaN(anio.mes[m].planificado) ? 0 : anio.mes[m].planificado;
							totalAnualReal += isNaN(anio.mes[m].real) ? 0 : anio.mes[m].real;
						 }
						 totalFinalPlanificado += totalAnualPlanificado;
						 totalFinalReal += totalAnualReal;
						 var tot = {"valor": {"planificado": totalAnualPlanificado, "real": totalAnualReal}};
						 fila.push(tot);
					 }
					 var tot = {"valor": {"planificado": totalFinalPlanificado, "real": totalFinalReal}};
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
		if(mi.prestamo.value > 0)
		{
			if(mi.fechaInicio != null && mi.fechaFin != null)
			{
				if (mi.fechaFin >= mi.fechaInicio){
					if(agrupacion != 0){
						mi.data = JSON.parse(JSON.stringify(mi.dataOriginal));
						mi.agrupacionActual = agrupacion;
						for (x in mi.data){
							if(mi.data[x].objeto_tipo == 0){
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
			$utilidades.mensaje('warning','Debe de seleccionar un préstamo');
	}

	mi.generar = function(agrupacion){
		if(mi.prestamo.value > 0)
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

	mi.getPlanificado=function(itemIndice, indice){
		mes = Math.floor((indice)/mi.aniosTotal.length);
		anio = indice - (mes*mi.aniosTotal.length);
		var item = mi.data[itemIndice];
		var valor = item.anios[anio].mes[mes];
		return valor;
	};

	mi.exportarExcel = function(){
		var tipoVisualizacion = 0;
		if (mi.grupoMostrado.planificado && mi.grupoMostrado.real){
			tipoVisualizacion = 2;
		}else if(mi.grupoMostrado.real){
			tipoVisualizacion = 1;
		}
		$http.post('/SFlujoCaja', { 
			accion: 'exportarExcel', 
			proyectoid: mi.prestamo.value,
			fechaInicio: mi.fechaInicio,
			fechaFin: mi.fechaFin,
			agrupacion: mi.agrupacionActual,
			tipoVisualizacion: tipoVisualizacion,
			t:moment().unix()
		} ).then(
				function successCallback(response) {
					var anchor = angular.element('<a/>');
					anchor.attr({
						href: 'data:application/ms-excel;base64,' + response.data,
						target: '_blank',
						download: 'FlujoCaja.xls'
					})[0].click();
				}.bind(this), function errorCallback(response){
				}
		);
	};

	mi.exportarPdf=function(){
		var tipoVisualizacion = 0;
		if (mi.grupoMostrado.planificado && mi.grupoMostrado.real){
			tipoVisualizacion = 2;
		}else if(mi.grupoMostrado.real){
			tipoVisualizacion = 1;
		}
		$http.post('/SFlujoCaja', { 
			accion: 'exportarPdf',
			proyectoid: mi.prestamo.value,
			fechaInicio: mi.fechaInicio,
			fechaFin: mi.fechaFin,
			agrupacion: mi.agrupacionActual,
			tipoVisualizacion: tipoVisualizacion,
			t:moment().unix()
		} ).then(
				function successCallback(response) {
					var anchor = angular.element('<a/>');
					anchor.attr({
						href: 'data:application/pdf;base64,' + response.data,
						target: '_blank',
						download: 'FlujoCaja.pdf'
					})[0].click();
				}.bind(this), function errorCallback(response){
				}
		);
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
						if(Math.abs(scope.controller.scrollPosicion-element[0].scrollLeft)<scope.controller.tamanoCelda){//bloquear scroll horizontal
							element[0].scrollLeft = scope.controller.scrollPosicion;
						}
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
