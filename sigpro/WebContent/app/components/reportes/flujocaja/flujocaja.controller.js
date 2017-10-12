var app = angular.module('flujocajaController', [ 'smart-table', 'vs-repeat']);


app.controller('flujocajaController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {

	var mi = this;
	mi.fechaCorte = null;
	mi.movimiento = false;
	mi.mostrarDescargar = false;
	mi.mostrarCargando = false;
	mi.SiguienteActivo = true;
	mi.AnteriorActivo = false;
	mi.enMillones = true;
	mi.agrupacionActual = 1
	mi.columnasTotal = 0;
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

	mi.formatofecha = 'dd/MM/yyyy';

	mi.abrirPopupFecha = function() {
		mi.isOpen = true; 
	};

	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};

	$http.post('/SProyecto',{accion: 'getProyectos',
		t:moment().unix()}).success(
			function(response) {
				mi.prestamos = [];
				mi.prestamos.push({'value' : 0, 'text' : 'Seleccione un '+$rootScope.etiquetas.proyecto});
				if (response.success){
					for (var i = 0; i < response.entidades.length; i++){
						mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
					}
					mi.prestamo = mi.prestamos[0];
				}
			});

	mi.anterior = function(){
		var elemento = document.getElementById("divTablaDatos");
		var elementoTotales = document.getElementById("cuerpoTotalesDatos");
		if(mi.totalCabecerasAMostrar == 0){
			elemento.scrollLeft -= mi.tamanoCelda;
			elementoTotales.scrollLeft -= mi.tamanoCelda;
			document.getElementById("divCabecerasDatos").scrollLeft -= mi.tamanoCelda;
			mi.SiguienteActivo = true;
		}else{
			if(elemento.scrollLeft > 0){
				elemento.scrollLeft -= mi.tamanoCabecera;
				elementoTotales.scrollLeft -= mi.tamanoCabecera;
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
		var elementoTotales = document.getElementById("cuerpoTotalesDatos");
		if(mi.totalCabecerasAMostrar == 0){
			elemento.scrollLeft += mi.tamanoCelda;
			elementoTotales.scrollLeft += mi.tamanoCelda;
			document.getElementById("divCabecerasDatos").scrollLeft += mi.tamanoCelda;
			mi.AnteriorActivo = true;
		}else{
			if(elemento.scrollLeft < ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
				elemento.scrollLeft += mi.tamanoCabecera;
				elementoTotales.scrollLeft += mi.tamanoCabecera;
				document.getElementById("divCabecerasDatos").scrollLeft += mi.tamanoCabecera;
				mi.AnteriorActivo = true;
				if(elemento.scrollLeft >= ((mi.tamanoCabecera * (mi.totalCabeceras - mi.totalCabecerasAMostrar)))){
					mi.SiguienteActivo = false;
				}
			}
		}
		mi.scrollPosicion = elemento.scrollLeft;
	}

	mi.validar = function(){
		if(mi.prestamo.value > 0)
		{
			if(mi.fechaCorte != null)
			{
				mi.generar(mi.agrupacionActual);
			}
		}
	}

	mi.calcularTamaniosCeldas = function(){
		var tamanioMinimo = mi.tamanioMinimoColumna;
		if(mi.enMillones){
			tamanioMinimo = mi.tamanioMinimoColumnaMillones;
		}
		mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth) - 300;
		mi.totalAnios = 1;
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
				fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY'),
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
				anioN.mes= [
						{"planificado": anio.mes[0].planificado+anio.mes[1].planificado, "real": anio.mes[0].real+anio.mes[1].real},
						{"planificado": anio.mes[2].planificado+anio.mes[3].planificado, "real": anio.mes[2].real+anio.mes[3].real},
						{"planificado": anio.mes[4].planificado+anio.mes[5].planificado, "real": anio.mes[4].real+anio.mes[5].real},
						{"planificado": anio.mes[6].planificado+anio.mes[7].planificado, "real": anio.mes[6].real+anio.mes[7].real},
						{"planificado": anio.mes[8].planificado+anio.mes[9].planificado, "real": anio.mes[8].real+anio.mes[9].real},
						{"planificado": anio.mes[10].planificado+anio.mes[11].planificado, "real": anio.mes[10].real+anio.mes[11].real}
				];
			}else if(mi.agrupacionActual == AGRUPACION_TRIMESTRE){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado, "real": anio.mes[0].real+anio.mes[1].real+anio.mes[2].real},
					{"planificado": anio.mes[3].planificado+anio.mes[4].planificado+anio.mes[5].planificado, "real": anio.mes[3].real+anio.mes[4].real+anio.mes[5].real},
					{"planificado": anio.mes[6].planificado+anio.mes[7].planificado+anio.mes[8].planificado, "real": anio.mes[6].real+anio.mes[7].real+anio.mes[8].real},
					{"planificado": anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado, "real": anio.mes[9].real+anio.mes[10].real+anio.mes[11].real}
				]
			}else if(mi.agrupacionActual == AGRUPACION_CUATRIMESTRE){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado+anio.mes[3].planificado
					, "real": anio.mes[0].real+anio.mes[1].real+anio.mes[2].real+anio.mes[3].real},
					{"planificado": anio.mes[4].planificado+anio.mes[5].planificado+anio.mes[6].planificado+anio.mes[7].planificado
					, "real": anio.mes[4].real+anio.mes[5].real+anio.mes[6].real+anio.mes[7].real},
					{"planificado": anio.mes[8].planificado+anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado
					, "real": anio.mes[8].real+anio.mes[9].real+anio.mes[10].real+anio.mes[11].real}
				]
			}else if(mi.agrupacionActual == AGRUPACION_SEMESTRE){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado+anio.mes[3].planificado+anio.mes[4].planificado+anio.mes[5].planificado
						,"real": anio.mes[0].real+anio.mes[1].real+anio.mes[2].real+anio.mes[3].real+anio.mes[4].real+anio.mes[5].real},
					{"planificado": anio.mes[6].planificado+anio.mes[7].planificado+anio.mes[8].planificado+anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado
						,"real": anio.mes[6].real+anio.mes[7].real+anio.mes[8].real+anio.mes[9].real+anio.mes[10].real+anio.mes[11].real},
				]
			}else if(mi.agrupacionActual == AGRUPACION_ANUAL){
				anioN.mes= [
					{"planificado": anio.mes[0].planificado+anio.mes[1].planificado+anio.mes[2].planificado+anio.mes[3].planificado+anio.mes[4].planificado+anio.mes[5].planificado+anio.mes[6].planificado+anio.mes[7].planificado+anio.mes[8].planificado+anio.mes[9].planificado+anio.mes[10].planificado+anio.mes[11].planificado
						,"real": anio.mes[0].real+anio.mes[1].real+anio.mes[2].real+anio.mes[3].real+anio.mes[4].real+anio.mes[5].real+anio.mes[6].real+anio.mes[7].real+anio.mes[8].real+anio.mes[9].real+anio.mes[10].real+anio.mes[11].real},
					]
			}
			anio = anioN;
		}
		return anio;
	}
	
	mi.agruparResumenTotales = function(){
		if(mi.agrupacionActual != AGRUPACION_MES){
			for(m=0; m<12;m++){
				mi.resumenTotales.filaDesembolsos[m] = mi.resumenTotales.filaDesembolsos[m]!=null ? mi.resumenTotales.filaDesembolsos[m] : 0;
				mi.resumenTotales.filaDesembolsosReal[m] = mi.resumenTotales.filaDesembolsosReal[m]!=null ? mi.resumenTotales.filaDesembolsosReal[m] : 0;
			}
			if(mi.agrupacionActual == AGRUPACION_BIMESTRE){
				var fila = mi.resumenTotales.filaPlanificado;
				mi.resumenTotales.filaPlanificado = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaPlanificadoAcumulado;
				mi.resumenTotales.filaPlanificadoAcumulado = [fila[1], fila[3], fila[5], fila[7], fila[9], fila[11]];
				var fila = mi.resumenTotales.filaEjecutado;
				mi.resumenTotales.filaEjecutado = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutadoAcumulado;
				mi.resumenTotales.filaEjecutadoAcumulado = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacion;
				mi.resumenTotales.filaVariacion = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacionPorcentaje;
				mi.resumenTotales.filaVariacionPorcentaje = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsos;
				mi.resumenTotales.filaDesembolsos = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsosReal;
				mi.resumenTotales.filaDesembolsosReal = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaSaldo;
				mi.resumenTotales.filaSaldo = [fila[0]+fila[1], fila[2]+fila[3], fila[4]+fila[5], fila[6]+fila[7], fila[8]+fila[9], fila[10]+fila[11]];
			}else if(mi.agrupacionActual == AGRUPACION_TRIMESTRE){
				var fila = mi.resumenTotales.filaPlanificado;
				mi.resumenTotales.filaPlanificado = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaPlanificadoAcumulado;
				mi.resumenTotales.filaPlanificadoAcumulado = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutado;
				mi.resumenTotales.filaEjecutado = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutadoAcumulado;
				mi.resumenTotales.filaEjecutadoAcumulado = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacion;
				mi.resumenTotales.filaVariacion = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacionPorcentaje;
				mi.resumenTotales.filaVariacionPorcentaje = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsos;
				mi.resumenTotales.filaDesembolsos = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsosReal;
				mi.resumenTotales.filaDesembolsosReal = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaSaldo;
				mi.resumenTotales.filaSaldo = [fila[0]+fila[1]+fila[2], fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8], fila[9]+fila[10]+fila[11]];
			}else if(mi.agrupacionActual == AGRUPACION_CUATRIMESTRE){
				var fila = mi.resumenTotales.filaPlanificado;
				mi.resumenTotales.filaPlanificado = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaPlanificadoAcumulado;
				mi.resumenTotales.filaPlanificadoAcumulado = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutado;
				mi.resumenTotales.filaEjecutado = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutadoAcumulado;
				mi.resumenTotales.filaEjecutadoAcumulado = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacion;
				mi.resumenTotales.filaVariacion = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacionPorcentaje;
				mi.resumenTotales.filaVariacionPorcentaje = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsos;
				mi.resumenTotales.filaDesembolsos = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsosReal;
				mi.resumenTotales.filaDesembolsosReal = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaSaldo;
				mi.resumenTotales.filaSaldo = [fila[0]+fila[1]+fila[2]+fila[3], fila[4]+fila[5]+fila[6]+fila[7], fila[8]+fila[9]+fila[10]+fila[11]];
			}else if(mi.agrupacionActual == AGRUPACION_SEMESTRE){
				var fila = mi.resumenTotales.filaPlanificado;
				mi.resumenTotales.filaPlanificado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaPlanificadoAcumulado;
				mi.resumenTotales.filaPlanificadoAcumulado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutado;
				mi.resumenTotales.filaEjecutado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutadoAcumulado;
				mi.resumenTotales.filaEjecutadoAcumulado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacion;
				mi.resumenTotales.filaVariacion = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacionPorcentaje;
				mi.resumenTotales.filaVariacionPorcentaje = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsos;
				mi.resumenTotales.filaDesembolsos = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsosReal;
				mi.resumenTotales.filaDesembolsosReal = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaSaldo;
				mi.resumenTotales.filaSaldo = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5], fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
			}else if(mi.agrupacionActual == AGRUPACION_ANUAL){
				var fila = mi.resumenTotales.filaPlanificado;
				mi.resumenTotales.filaPlanificado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaPlanificadoAcumulado;
				mi.resumenTotales.filaPlanificadoAcumulado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutado;
				mi.resumenTotales.filaEjecutado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaEjecutadoAcumulado;
				mi.resumenTotales.filaEjecutadoAcumulado = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacion;
				mi.resumenTotales.filaVariacion = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaVariacionPorcentaje;
				mi.resumenTotales.filaVariacionPorcentaje = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsos;
				mi.resumenTotales.filaDesembolsos = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaDesembolsosReal;
				mi.resumenTotales.filaDesembolsosReal = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
				var fila = mi.resumenTotales.filaSaldo;
				mi.resumenTotales.filaSaldo = [fila[0]+fila[1]+fila[2]+fila[3]+fila[4]+fila[5]+fila[6]+fila[7]+fila[8]+fila[9]+fila[10]+fila[11]];
			}
		}
		return anio;
	}

	mi.cambiarAgrupacion = function(agrupacion){
		if(mi.prestamo.value > 0)
		{
			if(mi.fechaCorte != null)
			{
					if(agrupacion != 0){
						mi.data = JSON.parse(JSON.stringify(mi.dataOriginal));
						mi.resumenTotales = JSON.parse(JSON.stringify(mi.resumenTotalesOriginal));
						mi.agrupacionActual = agrupacion;
						for (x in mi.data){
							for(a in mi.data[x].anios){
								var anio = mi.data[x].anios[a];
								mi.data[x].anios[a] = mi.agruparMeses(anio);
							}
						}	
						mi.agruparResumenTotales();
						mi.renderizaTabla();
					}
			}else
				$utilidades.mensaje('warning','Favor de ingresar una fecha válida');
		}else
			$utilidades.mensaje('warning','Debe de seleccionar un '+$rootScope.etiquetas.proyecto);
	}

	mi.generar = function(agrupacion){
		if(mi.prestamo.value > 0)
		{
			if(mi.fechaCorte != null)
			{
				if(agrupacion != 0){
					mi.agrupacionActual = agrupacion;
					mi.cargarTabla();

				}
			}else
				$utilidades.mensaje('warning','Favor de ingresar una fecha válida');
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
		var fecha = moment(mi.fechaCorte).format('DD/MM/YYYY')
		var anio = moment(fecha, 'DD/MM/YYYY').year();
		mi.anios.push({anio: anio});
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
		var valor = item.anios[anio].mes[mes]!=null ? item.anios[anio].mes[mes] : 0;
		return valor;
	};

	mi.exportarExcel = function(){
		$http.post('/SFlujoCaja', { 
			accion: 'exportarExcel', 
			proyectoid: mi.prestamo.value,
			fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY'),
			agrupacion: mi.agrupacionActual,
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
		$http.post('/SFlujoCaja', { 
			accion: 'exportarPdf',
			proyectoid: mi.prestamo.value,
			fechaCorte: moment(mi.fechaCorte).format('DD/MM/YYYY'),
			agrupacion: mi.agrupacionActual,
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

app.directive('scrollespejoflujo', ['$window', function($window) {
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {
			angular.element($window).bind('resize', function(){ 
				scope.flujoc.calcularTamaniosCeldas();
				scope.$digest();
			});
			scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
		}
	};
}])

;
