var app = angular.module('prestamoController', [ 'ngTouch','smart-table',  'ui.bootstrap.contextMenu']);

app.controller('prestamoController',['$rootScope','$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','$filter', 'dialogoConfirmacion','historia', 
	function($rootScope,$scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,$filter, $dialogoConfirmacion, $historia) {

	var mi = this;
	i18nService.setCurrentLang('es');

	mi.esTreeview = $rootScope.treeview;
	mi.botones = true;
	
	if(!mi.esTreeview)
		$window.document.title = 'Préstamos';
		
	mi.rowCollection = [];
	mi.prestamo = null;
	mi.esNuevo = false;
	mi.esNuevoDocumento = true;
	mi.campos = {};
	mi.esColapsado = false;
	mi.esNuevoDocumento = true;
	mi.mostrarcargando= (mi.esTreeview) ? false : true;
	mi.paginaActual = 1;
	mi.cooperantes = [];
	mi.prestamotipos = [];
	mi.unidadesejecutoras = [];
	mi.poryectotipoid = "";
	mi.prestamotiponombre="";
	mi.unidadejecutoraid="";
	mi.entidad="";
	mi.ejercicio="";
	mi.unidadejecutoranombre="";
	mi.entidadnombre="";
	mi.cooperanteid="";
	mi.cooperantenombre="";
	mi.camposdinamicos = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.altformatofecha = ['d!/M!/yyyy'];
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.totalPrestamos = 0;
	mi.mostrarPrestamo = true;
	mi.ordenTab=0;
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.prestamo = [];
	mi.componentes = [];	
	mi.metasCargadas = false;
	mi.riesgosCargados = false;
	mi.child_metas = null;
	mi.child_riesgos = null;
	
	mi.prestamo.desembolsoAFechaUsdP = "";
	mi.prestamo.montoPorDesembolsarUsdP = "";
	mi.prestamo.desembolsoAFechaUeUsdP = "";
	mi.prestamo.montoPorDesembolsarUeUsdP = "";
	mi.prestamo.plazoEjecucionUe = "";
	
	mi.coordenadas = "";
	
	mi.impactos =[];
	mi.miembros = [];
	
	mi.active = 0;
	
	mi.child_desembolso = null;
	mi.child_riesgos = null;
	mi.ingresoValidoMatriz = true;

	mi.m_organismosEjecutores = [];
	$scope.m_componentes = [];
	mi.totalIngresado  = 0;
	mi.diferenciaCambios = 0;
	
	mi.matriz_valid = 1;
	
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	mi.buscarTiposPrestamo = function(){
		var idPrestamoTipos = "";
		for(x in mi.prestamotipos){
			idPrestamoTipos = idPrestamoTipos + (x > 0 ? "," : "") + mi.prestamotipos[x].id;
		}
		var resultado = mi.llamarModalBusqueda('Tipos de préstmo','/SPrestamoTipo', {
			accion : 'numeroPrestamoTipos'	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getPrestamoTipoPagina',
				pagina : pagina,
				numeroresponsablerol : elementosPorPagina,
				idPrestamoTipos : idPrestamoTipos
			};
		},'id','nombre', null, null);

		resultado.then(function(itemSeleccionado) {
			var tipoPrestamo = {};
			tipoPrestamo = { id: itemSeleccionado.id, nombre: itemSeleccionado.nombre };
			mi.prestamotipos.push(tipoPrestamo);
		});
	}
	
	mi.eliminarTiposPrestamo= function(tipo){
		var indice = mi.prestamotipos.indexOf(tipo);
		if (indice !== -1) {
	       mi.prestamotipos.splice(indice, 1);
	    }
	};

	mi.editarElemento = function (event) {
		var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOpciones.data[filaId]);
        mi.editar();
    };
    
    mi.verHistoria = function(){
		$historia.getHistoriaMatriz($scope, 'Matriz Préstamo', '/SPrestamo', mi.prestamo.id, mi.prestamo.codigoPresupuestario)
		.result.then(function(data) {
			if (data != ""){
				
			}
		}, function(){
			
		});
	}
    
	mi.gridOpciones = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect: false,
		modifierKeysToMultiSelect: false,
		noUnselect: true,
		enableFiltering: true,
		enablePaginationControls: false,
	    paginationPageSize: $utilidades.elementosPorPagina,
	    useExternalFiltering: true,
	    useExternalSorting: true,
	    rowTemplate: '<div ng-dblclick="grid.appScope.prestamoc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [
			{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'proyectoPrograma',  displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamoc.filtros[\'nombre\']" ng-keypress="grid.appScope.prestamoc.filtrar($event)" style="width:175px;"></input></div>'
			},
			{ name : 'codigoPresupuestario',    displayName : 'Código presupuestario',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamoc.filtros[\'codigo_presupuestario\']" ng-keypress="grid.appScope.prestamoc.filtrar($event)" style="width:175px;"></input></div>',
			},
			{ name : 'numeroPrestamo',    displayName : 'Número de Préstamo' ,cellClass: 'grid-align-left', 
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamoc.filtros[\'numero_prestamo\']" ng-keypress="grid.appScope.prestamoc.filtrar($event)" style="width:175px;"></input></div>',
			},
			{ name: 'usuarioCreo', width: 120, displayName: 'Usuario Creación',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.prestamoc.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.prestamoc.filtrar($event)" style="width:90px;"></input></div>'
			},
		    { name: 'fechaCreacion', width: 100, displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.prestamoc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.prestamoc.filtrar($event)" style="width:80px;" ></input></div>'
		    }
		],
		onRegisterApi: function(gridApi) {
			mi.gridApi = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.prestamo = row.entity;
			});

			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.prestamoc.columnaOrdenada=sortColumns[0].field;
					grid.appScope.prestamoc.ordenDireccion = sortColumns[0].sort.direction;

					grid.appScope.prestamoc.cargarTabla(grid.appScope.prestamoc.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.prestamoc.columnaOrdenada!=null){
						grid.appScope.prestamoc.columnaOrdenada=null;
						grid.appScope.prestamoc.ordenDireccion=null;
					}
				}
			} );

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
				mi.obtenerTotalPrestamos();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyceto', t: (new Date()).getTime()}).then(function(response){
		    		  if(response.data.success && response.data.estado!='')
		    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
			    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
				      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
				      mi.obtenerTotalPrestamos();
				  });
		    }
		}
	};

	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';
	}
	
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SPrestamo', { accion: 'getPrestamosPagina', pagina: pagina,
			elementosPorPagina:  $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'],
			filtro_codigo_presupuestario: mi.filtros['codigo_presupuestario'], filtro_numero_prestamo: mi.filtros['numero_prestamo'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t:moment().unix()
			}).success(
				function(response) {
					for(x in response.prestamos){
						response.prestamos[x].fechaDecreto = response.prestamos[x].fechaDecreto != null ? moment(response.prestamos[x].fechaDecreto,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaSuscripcion = response.prestamos[x].fechaSuscripcion != null ? moment(response.prestamos[x].fechaSuscripcion,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaElegibilidadUe = response.prestamos[x].fechaElegibilidadUe != null ? moment(response.prestamos[x].fechaElegibilidadUe,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaCierreOrigianlUe = response.prestamos[x].fechaCierreOrigianlUe != null ? moment(response.prestamos[x].fechaCierreOrigianlUe,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaCierreActualUe = response.prestamos[x].fechaCierreActualUe != null ? moment(response.prestamos[x].fechaCierreActualUe,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaVigencia = response.prestamos[x].fechaVigencia != null ? moment(response.prestamos[x].fechaVigencia,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaCorte = response.prestamos[x].fechaCorte != null ? moment(response.prestamos[x].fechaCorte,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaFirma = response.prestamos[x].fechaFirma != null ? moment(response.prestamos[x].fechaFirma,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaAutorizacion = response.prestamos[x].fechaAutorizacion != null ? moment(response.prestamos[x].fechaAutorizacion,'DD/MM/YYYY').toDate() : undefined;
						response.prestamos[x].fechaFinEjecucion = response.prestamos[x].fechaFinEjecucion != null ? moment(response.prestamos[x].fechaFinEjecucion,'DD/MM/YYYY').toDate() : undefined;
					}
					
					mi.prestamos = response.prestamos;
					mi.gridOpciones.data = mi.prestamos;
					mi.mostrarcargando = false;
				});
	};
	
	mi.cambioCooperante=function(selected){
		if(selected!== undefined){
			mi.cooperantenombre=selected.originalObject.nombre;
			mi.cooperanteid=selected.originalObject.id;
			mi.prestamo.cooperanteid = mi.cooperanteid;
		}
		else{
			mi.cooperantenombre="";
			mi.cooperanteid="";
			mi.prestamo.cooperanteid = null;
		}
	}
	
	mi.blurCooperante=function(){
		if(document.getElementById("cooperante_value").defaultValue!=mi.cooperantenombre){
			$scope.$broadcast('angucomplete-alt:clearInput');
		}
	}
	
	mi.getPorcentajes = function(){
		mi.setPorcentaje(1);
		mi.setPorcentaje(2);
		mi.setPorcentaje(3);
		mi.setPorcentaje(4);
		mi.setPorcentaje(5);
	}
	
	mi.setPorcentaje = function(tipo){
		var n = 0;
		if (tipo==1)
		{
			if(mi.prestamo.desembolsoAFechaUsd !== undefined && mi.prestamo.montoContratado !== undefined){
				n = (mi.prestamo.desembolsoAFechaUsd / mi.prestamo.montoContratado) * 100;
				mi.prestamo.desembolsoAFechaUsdP = Number(n.toFixed(2));
				mi.prestamo.montoPorDesembolsarUsd= ((1 - (mi.prestamo.desembolsoAFechaUsdP/100) ) *  mi.prestamo.montoContratado);
				mi.prestamo.montoPorDesembolsarUsd= Number(mi.prestamo.montoPorDesembolsarUsd.toFixed(2));
				mi.prestamo.montoPorDesembolsarUsdP= 100 - mi.prestamo.desembolsoAFechaUsdP;
				if(isNaN(mi.prestamo.montoPorDesembolsarUsdP))
					mi.prestamo.montoPorDesembolsarUsdP = null;
			}
		}else if (tipo==2){
			if(mi.prestamo.montoContratadoUsd !== undefined && mi.prestamo.montoPorDesembolsarUsd !== undefined){
				n = (mi.prestamo.montoPorDesembolsarUsd / mi.prestamo.montoContratadoUsd) * 100;
				mi.prestamo.montoPorDesembolsarUsdP = Number(n.toFixed(2));
				if(isNaN(mi.prestamo.montoPorDesembolsarUsdP))
					mi.prestamo.montoPorDesembolsarUsdP = null;
			}
		}else if (tipo==3){
			if(mi.prestamo.desembolsoAFechaUeUsd !== undefined && mi.prestamo.montoAsignadoUe !== undefined){
				n = (mi.prestamo.desembolsoAFechaUeUsd / mi.prestamo.montoAsignadoUeUsd) * 100;
				mi.prestamo.desembolsoAFechaUeUsdP = Number(n.toFixed(2));
				mi.prestamo.montoPorDesembolsarUeUsd = ((1.00 - (mi.prestamo.desembolsoAFechaUeUsdP/100.00) ) *  (mi.prestamo.montoAsignadoUeUsd*1.00));
				mi.prestamo.montoPorDesembolsarUeUsdP= 100.00 - mi.prestamo.desembolsoAFechaUeUsdP;
				if(isNaN(mi.prestamo.desembolsoAFechaUeUsdP))
					mi.prestamo.desembolsoAFechaUeUsdP = null;
				if(isNaN(mi.prestamo.montoPorDesembolsarUeUsd))
					mi.prestamo.montoPorDesembolsarUeUsd = null;
				if(isNaN(mi.prestamo.montoPorDesembolsarUeUsdP))
					mi.prestamo.montoPorDesembolsarUeUsdP = null;
			}
		}else if(tipo==4){
			if(mi.prestamo.montoAsignadoUeUsd !== undefined && mi.prestamo.montoPorDesembolsarUeUsd !== undefined){
				n = (mi.prestamo.montoPorDesembolsarUeUsd / mi.prestamo.montoAsignadoUeUsd) * 100;
				mi.prestamo.montoPorDesembolsarUeUsdP = Number(n.toFixed(2));
				if(isNaN(mi.prestamo.montoPorDesembolsarUeUsdP))
					mi.prestamo.montoPorDesembolsarUeUsdP = null;
			}
		}else if(tipo==5){
			if(mi.prestamo.fechaCierreActualUe !== undefined && mi.prestamo.fechaElegibilidadUe !== undefined){
				var fechaInicio = moment(mi.prestamo.fechaElegibilidadUe).format('DD/MM/YYYY').split('/');
				var fechaFinal = moment(mi.prestamo.fechaCierreActualUe).format('DD/MM/YYYY').split('/');
				var ffechaInicio = Date.UTC(fechaInicio[2],fechaInicio[1]-1,fechaInicio[0]);
				var ffechaFinal = Date.UTC(fechaFinal[2],fechaFinal[1]-1,fechaFinal[0]);
				
				var hoy = new Date();
				var fechaActual = hoy.today().split('/');
				var ffechaActual = Date.UTC(fechaActual[2],fechaActual[1]-1,fechaActual[0]);
				
				var dif1 = ffechaFinal - ffechaInicio;
				var dif2 = ffechaActual - ffechaInicio;
				n = (dif1>0) ? (dif2 / dif1) * 100.00 : 0.00;
				if (isNaN(n))
					n = 0.00;
				mi.prestamo.plazoEjecucionUe = Number(n.toFixed(2));
				mi.prestamo.mesesProrrogaUe = moment(mi.prestamo.fechaCierreActualUe).diff(mi.prestamo.fechaCierreOrigianlUe,'months',true);
				if(isNaN(mi.prestamo.plazoEjecucionUe))
					mi.prestamo.plazoEjecucionUe = null;
				if(isNaN(mi.prestamo.mesesProrrogaUe))
					mi.prestamo.mesesProrrogaUe = null;
			}
			
		}
	};

	Date.prototype.today = function () { 
	    return ((this.getDate() < 10)?"0":"") + this.getDate() +"/"+(((this.getMonth()+1) < 10)?"0":"") + (this.getMonth()+1) +"/"+ this.getFullYear();
	}
	
	mi.guardar = function(esvalido){
		if (mi.prestamo!=null && mi.prestamo.codigoPresupuestario !=null && mi.prestamo.codigoPresupuestario != ''){
			var idPrestamoTipos = "";
			for(x in mi.prestamotipos){
				idPrestamoTipos = idPrestamoTipos + (x > 0 ? "," : "") + mi.prestamotipos[x].id;
			}
			var param_data = {
					accion: "guardarPrestamo",
					esNuevo: mi.esNuevo,
					prestamoId: mi.prestamo.id,
					codigoPresupuestario: mi.prestamo.codigoPresupuestario,
					numeroPrestamo: mi.prestamo.numeroPrestamo,
					proyetoPrograma: mi.prestamo.proyectoPrograma,
					unidadEjecutora: mi.unidadejecutoraid,
					entidad: mi.entidad, 
					ejercicio: mi.ejercicio,
					cooperanteUeId: mi.prestamo.cooperanteid,
					fechaDecreto: moment(mi.prestamo.fechaDecreto).format('DD/MM/YYYY'),
					fechaSuscripcion: moment(mi.prestamo.fechaSuscripcion).format('DD/MM/YYYY'),
					fechaVigencia: moment(mi.prestamo.fechaVigencia).format('DD/MM/YYYY'),
					tipoMonedaId: mi.prestamo.tipoMonedaId,
					montoContratado: mi.prestamo.montoContratado,
					montoContratadoUsd: mi.prestamo.montoContratadoUsd,
					montoContratadoQtz: mi.prestamo.montoContratadoQtz,
					desembolsoAFechaUsd: mi.prestamo.desembolsoAFechaUsd,
					montoPorDesembolsarUsd: mi.prestamo.montoPorDesembolsarUsd,
					fechaElegibilidad: moment(mi.prestamo.fechaElegibilidadUe).format('DD/MM/YYYY'),
					fechaCierreOriginal:  moment(mi.prestamo.fechaCierreOrigianlUe).format('DD/MM/YYYY'),
					fechaCierreActual: moment(mi.prestamo.fechaCierreActualUe).format('DD/MM/YYYY'),
					mesesProrroga: mi.prestamo.mesesProrrogaUe,
					montoAisignadoUe: mi.prestamo.montoAsignadoUe,
					desembolsoAFechaUe: mi.prestamo.desembolsoAFechaUe,
					montoPorDesembolsarUe: mi.prestamo.montoPorDesembolsarUe,
					montoAsignadoUeUsd: mi.prestamo.montoAsignadoUeUsd,
					montoAsignadoUeQtz: mi.prestamo.montoAsignadoUeQtz,
					desembolsoAFechaUeUsd: mi.prestamo.desembolsoAFechaUeUsd,
					montoPorDesembolsarUeUsd: mi.prestamo.montoPorDesembolsarUeUsd,
					destino : mi.prestamo.destino,
					sectorEconomico: mi.prestamo.sectorEconomico,
					fechaFimra: mi.prestamo.fechaFirma != undefined ? moment(mi.prestamo.fechaFirma).format('DD/MM/YYYY') : undefined,
					tipoAutorizacionId : mi.prestamo.tipoAutorizacionId,
					numeroAutorizacion: mi.prestamo.numeroAutorizacion,
					fechaAutorizacion: mi.prestamo.fechaAutorizacion != undefined ? moment(mi.prestamo.fechaAutorizacion).format('DD/MM/YYYY') : undefined,
					aniosPlazo: mi.prestamo.aniosPlazo != undefined ? mi.prestamo.aniosPlazo : undefined,
					aniosGracia: mi.prestamo.aniosGracia,
					fechaFinEjecucion: mi.prestamo.fechaFinEjecucion != undefined ? moment(mi.prestamo.fechaFinEjecucion).format('DD/MM/YYYY') : undefined,
					periodoEjecucion: mi.prestamo.periodoEjecucion != "" ? mi.prestamo.periodoEjecucion : undefined,
					tipoInteresId: mi.prestamo.tipoInteresId,
					porcentajeInteres: mi.prestamo.porcentajeInteres,
					porcentajeComisionCompra: mi.prestamo.porcentajeComisionCompra,
					amortizado: mi.prestamo.amortizado,
					porAmortizar: mi.prestamo.porAmortizar,
					principalAnio: mi.prestamo.principalAnio,
					interesesAnio : mi.prestamo.interesesAnio,
					comisionCompromisoAnio: mi.prestamo.comisionCompromisoAnio,
					otrosGastos: mi.prestamo.otrosGastos,
					principalAcumulado: mi.prestamo.principalAcumulado,
					interesesAcumulados: mi.prestamo.interesesAcumulados,
					comisionCompromisoAcumulado: mi.prestamo.comisionCompromisoAcumulado,
					otrosCargosAcumulados: mi.prestamo.otrosCargosAcumulados,
					presupuestoAsignadoFuncionamiento: mi.prestamo.presupuestoAsignadoFuncionamiento,
					presupuestoAsignadoInversion: mi.prestamo.presupuestoAsignadoInversion,
					presupuestoModificadoFuncionamiento: mi.prestamo.presupuestoModificadoFun,
					presupuestoModificadoInversion: mi.prestamo.presupuestoModificadoInv,
					presupuestoVigenteFuncionamiento: mi.prestamo.presupuestoVigenteFun,
					presupuestoVigenteInversion: mi.prestamo.presupuestoVigenteInv,
					presupuestoDevengadoFunconamiento:mi.prestamo.presupuestoDevengadoFun,
					presupuestoDevengadoInversion:mi.prestamo.presupuestoDevengadoInv,
					presupuestoPagadoFuncionamiento: mi.prestamo.presupuestoPagadoFun,
					presupuestoPagadoInversion: mi.prestamo.presupuestoPagadoInv,
					saldoCuentas: mi.prestamo.saldoCuentas,
					desembolsoReal: mi.prestamo.desembolsoReal,
					ejecucionEstadoId: mi.prestamo.ejecucionEstadoId != undefined ? mi.prestamo.ejecucionEstadoId : undefined,
					fechaCorte : mi.prestamo.fechaCorte != undefined ? moment(mi.prestamo.fechaCorte).format('DD/MM/YYYY') : undefined,
					objetivo : mi.prestamo.objetivo,
					objetivoEspecifico : mi.prestamo.objetivoEspecifico,
					idPrestamoTipos: idPrestamoTipos,
					porcentajeAvance: mi.prestamo.porcentajeAvance,
					t:moment().unix()
				};
				
			
				$http.post('/SPrestamo',param_data).then(
					function(response) {
						if (response.data.success) {
							mi.diferenciaCambios = 0;
							mi.prestamo.usuarioCreo = response.data.usuarioCreo;
							mi.prestamo.fechaCreacion = response.data.fechaCreacion;
							mi.prestamo.fechaActualizacion = response.data.fechaActualizacion;
							mi.prestamo.usuarioActualizo = response.data.usuarioActualizo;
							mi.prestamo.id = response.data.id;
							
							if(mi.esTreeview){
								mi.t_cambiarNombreNodo();
							}
							else{
								mi.obtenerTotalPrestamos();
							}
							
							if (mi.matriz_valid && mi.totalIngresado  > 0 && $scope.m_componentes.length > 0 ){
								for(c=0; c<$scope.m_componentes.length; c++){
									$scope.m_componentes[c].descripcion = mi.rowCollectionComponentes[c]!=null ? mi.rowCollectionComponentes[c].descripcion : null;
								}
								var parametros = {
										accion: 'guardarMatriz',
										estructura: JSON.stringify($scope.m_componentes),
										componentes: JSON.stringify(mi.rowCollectionComponentes),
										unidadesEjecutoras : JSON.stringify(mi.rowCollectionUE),
										prestamoId: mi.prestamo.id,
										existenDatos: mi.m_existenDatos ? 'true' : 'false',
									    t:moment().unix()
								};
					
								$http.post('/SPrestamo', parametros).then(function(response){
									if (response.data.success){
										//mi.m_organismosEjecutores = response.data.unidadesEjecutoras;
										//$scope.m_componentes = response.data.componentes;
										//mi.diferenciaCambios = true;
										mi.m_existenDatos = true; 

										if(mi.child_metas!=null || mi.child_riesgos!=null){
											if(mi.child_metas)
												mi.child_metas.guardar(null, (mi.child_riesgos!=null) ?  mi.child_riesgos.guardar : null,'Préstamo '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
														'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Préstamo');
											else if(mi.child_riesgos)
												mi.child_riesgos.guardar('Préstamo '+(mi.esNuevo ? 'creado' : 'guardado')+' con Éxito',
														'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Préstamo');
										}else{
											$utilidades.mensaje('success','Préstamo '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
											
											mi.esNuevoDocumento = false;
										}
										mi.botones=true;
										mi.esNuevo=false;
									}else{
										$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'crear' : 'guardar')+' la matriz del préstamo');
										mi.botones=true;
									}
								});
							}else{
								if(mi.child_metas!=null)
									mi.child_metas.guardar(null, null,'Préstamo '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito',
											'Error al '+(mi.esNuevo ? 'crear' : 'guardar')+' el préstamo');
								else{
									$utilidades.mensaje('success','Préstamo '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
								}
								mi.botones=true;
							}
							mi.esNuevo=false;
							
						}else{
							$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'crear' : 'guardar')+' el préstamo');
							mi.botones=true;
						}
				});
		}
		else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	 }

	mi.borrar = function(ev) {
		if(mi.prestamo !=null && mi.prestamo.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el préstamo "'+mi.prestamo.proyectoPrograma+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					$http.post('/SPrestamo', {
						accion: 'borrarPrestamo',
						prestamoId: mi.prestamo.id,
						t:moment().unix()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Préstamo borrado con éxito');
							mi.prestamo = null;
							mi.obtenerTotalPrestamos();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el préstamo');
					});
				}
			}, function(){
				
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el '+$rootScope.etiquetas.proyecto+' que desea borrar');
	};

	mi.nuevo = function (){
		mi.esNuevoDocumento = true;		
		mi.poryectotipoid = "";
		mi.prestamotiponombre="";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.cooperanteid="";
		mi.cooperantenombre="";
		mi.esColapsado = true;
		mi.prestamo = {};
		mi.esNuevo = true;
		mi.camposdinamicos = {};
		mi.coordenadas = "";
		mi.gridApi.selection.clearSelectedRows();
		mi.prestamo = [];
		$scope.active = 0;
		mi.metasCargadas = false;
		mi.riesgosCargados = false;
		
		mi.rowCollectionComponentes = [];
		mi.displayedCollectionComponentes = [];
		mi.rowCollectionUE = [];
		mi.displayCollectionUE = [];
		mi.m_organismosEjecutores = [];
		$scope.m_componentes = [];
		mi.totalIngresado  = 0;
		
		mi.active = 0;
		mi.child_metas = null;
		mi.matriz_valid = 1;
	};

	mi.editar = function() {
		if(mi.prestamo!=null && mi.prestamo.id!=null){
			mi.esNuevoDocumento = false;
			mi.esColapsado = true;
			mi.esNuevo = false;
			mi.esNuevoDocumento = false;
			mi.active = 0;
			mi.child_metas = null;
			
			$http.post('/SPrestamo',{
				accion: 'obtenerTipos',
				prestamoId: mi.prestamo.id
			}).then(function(response){
				if(response.data.success){
					mi.prestamotipos = response.data.prestamoTipos;
				}
			})
			
			$http.post('/SPrestamo', {
				accion: 'getComponentesSigade',
				codigoPresupuestario : mi.prestamo.codigoPresupuestario
			}).then(function(response){
				if(response.data.success){
					mi.componentes = response.data.componentes;
					mi.rowCollectionComponentes = mi.componentes;
					mi.displayedCollectionComponentes = [].concat(mi.rowCollectionComponentes);
				}
			})
			
			$http.post('/SPrestamo',{
				accion: 'getUnidadesEjecutoras',
				codigoPresupuestario : mi.prestamo.codigoPresupuestario,
				proyectoId : mi.prestamo.id
			}).then(function(response){
				mi.unidadesEjecutoras = response.data.unidadesEjecutoras;
				mi.rowCollectionUE = mi.unidadesEjecutoras;
				for(ue=0; ue<mi.rowCollectionUE.length; ue++){
					mi.rowCollectionUE[ue].fechaElegibilidad = mi.rowCollectionUE[ue].fechaElegibilidad != '' ? moment(mi.rowCollectionUE[ue].fechaElegibilidad, 'DD/MM/YYYY').toDate() : null;
					mi.rowCollectionUE[ue].fechaCierre = mi.rowCollectionUE[ue].fechaCierre != '' ? moment(mi.rowCollectionUE[ue].fechaCierre, 'DD/MM/YYYY').toDate() : null;
					mi.rowCollectionUE[ue].esCoordinador = mi.rowCollectionUE[ue].esCoordinador==1;
				}
				mi.displayCollectionUE = [].concat(mi.rowCollectionUE);

			})
			
			mi.cargarMatriz();
			
			mi.getPorcentajes();
			
			mi.getDocumentosAdjuntos( mi.prestamo.id,-1);
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el préstamo que desea editar');
	}
	
	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}
	mi.cambioOrden=function(){
		mi.ordenTab++;
	}
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'prestamo', estado: JSON.stringify(estado) };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}
	
	mi.actualizarTotalesUE = function(){
		var totalPrestamo = 0;
		var totalDonacion = 0;
		var totalNacional = 0;
		
		for (x in mi.m_organismosEjecutores){
			mi.m_organismosEjecutores[x].totalAsignadoPrestamo = 0;
			mi.m_organismosEjecutores[x].totalAsignadoDonacion = 0;
			mi.m_organismosEjecutores[x].totalAsignadoNacional = 0;
		}
		
		for(c=0; c<$scope.m_componentes.length; c++){
			for(ue=0; ue<$scope.m_componentes[c].unidadesEjecutoras.length; ue++){
				mi.m_organismosEjecutores[ue].totalAsignadoPrestamo += $scope.m_componentes[c].unidadesEjecutoras[ue].prestamo;
				mi.m_organismosEjecutores[ue].totalAsignadoDonacion += $scope.m_componentes[c].unidadesEjecutoras[ue].donacion;
				mi.m_organismosEjecutores[ue].totalAsignadoNacional+= $scope.m_componentes[c].unidadesEjecutoras[ue].nacional;
			}
		}
	}
	
	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}

	mi.reiniciarVista=function(){
		if($location.path()=='/prestamo/rv')
			$route.reload();
		else
			$location.path('/prestamo/rv');
	}

	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalPrestamos();
			mi.gridApi.selection.clearSelectedRows();
			mi.prestamo = null;
		}
	};

	mi.obtenerTotalPrestamos = function(){
		$http.post('/SPrestamo', { accion: 'numeroPrestamos', 
			filtro_nombre: mi.filtros['nombre'], filtro_codigo_presupuestario: mi.filtros['codigo_presupuestario'], 
			filtro_numero_prestamo: mi.filtros['numero_prestamo'], filtro_usuario_creo: mi.filtros['usuario_creo'], 
			filtro_fecha_creacion: mi.filtros['fecha_creacion'], t:moment().unix()  } ).then(
				function(response) {
					mi.totalPrestamos = response.data.totalprestamos;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};

	mi.llamarModalBusqueda = function(titulo,servlet, accionServlet, datosCarga,columnaId,columnaNombre, showfilters,entidad) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorPrestamo.jsp',
			controller : 'buscarPorPrestamo',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$titulo: function(){
					return titulo;
				},
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				},
				$showfilters: function(){
					return showfilters;
				},
				$entidad: function(){
					return entidad;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};

	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('Unidades Ejecutoras','/SUnidadEjecutora', {
			accion : 'totalElementos',
			ejercicio: mi.ejercicio,
			entidad: mi.entidad ,t:moment().unix()
		}, function(pagina, elementosPorPagina,ejercicio,entidad) {
			return {
				accion : 'cargar',
				pagina : pagina,
				ejercicio: ejercicio,
				entidad: entidad,
				registros : elementosPorPagina,
				t:moment().unix()
			};
		},'unidadEjecutora','nombreUnidadEjecutora', true, {entidad: mi.entidad, ejercicio: mi.ejercicio, abreviatura:'', nombre: mi.entidadnombre});

		resultado.then(function(itemSeleccionado) {
			mi.ejercicio = itemSeleccionado.ejercicio;
			mi.entidad = itemSeleccionado.entidad;
			mi.unidadejecutoraid= itemSeleccionado.unidadEjecutora;
			mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.entidadnombre = itemSeleccionado.nombreEntidad;
		});
	};

	mi.buscarCooperante = function(prestamo) {
		var resultado = mi.llamarModalBusqueda('Cooperantes','/SCooperante', {
			accion : 'numeroCooperantes', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getCooperantesPagina',
				pagina : pagina,
				numerocooperantes : elementosPorPagina,
				t:moment().unix()
			};
		},'id','nombre', false, null);

		resultado.then(function(itemSeleccionado) {
			
				mi.prestamo.cooperanteid= itemSeleccionado.id;
				mi.prestamo.cooperantenombre = itemSeleccionado.siglas + " - " + itemSeleccionado.nombre;
			
				mi.cooperanteid= itemSeleccionado.id;
				mi.cooperantenombre = itemSeleccionado.siglas + " - " + itemSeleccionado.nombre;
			

		});
	};
	
	mi.abrirPopupFecha = function(index) {
		if(index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1003: mi.fc_abierto = true; break;
				case 1004: mi.ff_abierto = true; break;
				case 1005: mi.fa_abierto = true; break;
				case 1006: mi.ffe_abierto = true; break;
				case 1007: mi.fd_abierto = true; break;
				case 1008: mi.fs_abierto = true; break;
				case 1009: mi.fe_abierto = true; break;
				case 1010: mi.fco_abierto = true; break;
				case 1011: mi.fca_abierto = true; break;
				case 1012: mi.fv_abierto = true; break;
			}
		}

	};
	
	
	
	mi.buscarUnidadEjecutoraPrestamo = function() {	
		var resultado = mi.llamarModalBusqueda('Unidades Ejecutoras','/SUnidadEjecutora', {
			accion : 'totalElementos',
			ejercicio: mi.ejercicio,
			entidad: mi.entidad,
			t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina,
				t:moment().unix()
			};
		},'unidadEjecutora','nombreUnidadEjecutora', true,{entidad: mi.entidad, ejercicio: mi.ejercicio, abreviatura:'', nombre: mi.entidadnombre});

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.prestamo.unidadEjecutora = itemSeleccionado.unidadEjecutora;
			mi.prestamo.entidad = itemSeleccionado.entidad;
			mi.prestamo.ejercicio = itemSeleccionado.ejercicio;
			mi.entidadnombre = itemSeleccionado.nombreEntidad;
		});
	};
	
	mi.buscarAutorizacionTipo = function() {
		
		var resultado = mi.llamarModalBusqueda('Tipos de Autorizaciones','/SAutorizacionTipo', {
			accion : 'numeroAutorizacionTipo' ,t:moment().unix()	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getAutorizacionTipoPagin',
				pagina : pagina,
				numeroautorizaciontipo : elementosPorPagina,
				t:moment().unix()
			};
		},'id','nombre', false, null);

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoAutorizacionNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoAutorizacionId = itemSeleccionado.id;
		});
	};
	
	mi.buscarInteresTipo = function() {
		
		var resultado = mi.llamarModalBusqueda('Tipos de Interes','/SInteresTipo', {
			accion : 'numeroInteresTipo' ,t:moment().unix()	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getAutorizacionTipoPagin',
				pagina : pagina,
				numerointerestipo : elementosPorPagina,
				t:moment().unix()
			};
		},'id','nombre', false, null);

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoInteresNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoInteresId = itemSeleccionado.id;
		});
	};
	
	mi.buscarTipoMoneda = function() {
		
		var resultado = mi.llamarModalBusqueda('Tipos de Moneda','/STipoMoneda', {
			accion : 'numeroTipoMonedas',t:moment().unix()	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getTipoMonedaPagina',
				pagina : pagina,
				numerotipomoneda : elementosPorPagina
				,t:moment().unix()
			};
		},'id','nombre', false, null);

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoMonedaNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoMonedaId = itemSeleccionado.id;
		});
	};
	
	mi.buscarEstadoEjecucion = function() {
		
		var resultado = mi.llamarModalBusqueda('Estado de Ejecución','/SEjecucionEstado', {
			accion : 'numeroEjecucionEstado' ,t:moment().unix()	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getEjecucionEstadoPagina',
				pagina : pagina,
				numeroejecucionestado : elementosPorPagina
				,t:moment().unix()
			};
		},'id','nombre', false, null);

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.ejecucionEstadoNombre = itemSeleccionado.nombre;
			mi.prestamo.ejecucionEstadoId = itemSeleccionado.id;
		});
	};

	mi.open = function (posicionlat, posicionlong) {
		$scope.geoposicionlat = posicionlat;
		$scope.geoposicionlong = posicionlong;

	    var modalInstance = $uibModal.open({
	      animation: true,
	      templateUrl: 'map.html',
	      controller: 'mapCtrl',
	      resolve: {
	        glat: function(){
	        	return $scope.geoposicionlat;
	        },
	        glong: function(){
	        	return $scope.geoposicionlong;
	        }
	      }

	    });

	    modalInstance.result.then(function(coordenadas) {
	    	if (coordenadas !=undefined){
		    	mi.coordenadas = coordenadas.latitude + ", " + coordenadas.longitude;
		    	mi.prestamo.latitud= coordenadas.latitude;
				mi.prestamo.longitud = coordenadas.longitude;
	    	}else{
	    		mi.coordenadas = "";
		    	mi.prestamo.latitud= null
				mi.prestamo.longitud = null;
	    	}
	    }, function() {
	    	
		});
	  };
	  
	  mi.cargaSigade = function(){
			var parametros = {
					accion: 'getdatos',
					codigoPresupuestario:mi.prestamo.codigoPresupuestario,
				    t:moment().unix()
			}
			$http.post('/SDataSigade', parametros).then(function(response){
				mi.prestamo={};
				if (response.data.success){
					mi.prestamo = response.data.prestamo;
					mi.prestamo.codigoPresupuestario = Number(response.data.prestamo.codigoPresupuestario);
					mi.prestamo.fechaDecreto = moment(response.data.prestamo.fechaDecreto,'DD/MM/YYYY').toDate()
					mi.prestamo.fechaSuscripcion = moment(response.data.prestamo.fechaSuscripcion,'DD/MM/YYYY').toDate();
					mi.prestamo.fechaVigencia = moment(response.data.prestamo.fechaVigencia,'DD/MM/YYYY').toDate();
					mi.prestamo.nombre = mi.prestamo.nombre == null || mi.prestamo.nombre == undefined || mi.prestamo.nombre == '' ?
							mi.prestamo.proyectoPrograma : mi.prestamo.nombre;
					mi.cooperanteid = mi.prestamo.cooperanteid;
					
					mi.getPorcentajes();
				}else{
					$utilidades.mensaje('warning', 'No se encontraron datos con los parámetros ingresados');
				}
				
			});
			
		};
		
		mi.buscarDirecotorProyecto = function() {
			var resultado = mi.llamarModalBusqueda('Colaboradores','/SColaborador', {
				accion : 'totalElementos', t:moment().unix()
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'cargar',
					pagina : pagina,
					numerocooperantes : elementosPorPagina
					,t:moment().unix()
				};
			},'id','nombreCompleto', false, null);

			resultado.then(function(itemSeleccionado) {
				mi.directorProyectoNombre = itemSeleccionado.primerNombre +
						(itemSeleccionado.segundoNombre!=null ? ' ' + itemSeleccionado.segundoNombre : '') +
						' ' + itemSeleccionado.primerApellido +
						(itemSeleccionado.segundoApellido!=null ? ' ' + itemSeleccionado.segundoApellido + ' ' : '');
				mi.directorProyectoId = itemSeleccionado.id;
			});
		};
		
		mi.agregarImpacto = function() {

			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'agregarImpacto.jsp',
				controller : 'modalAgregarImpacto',
				controllerAs : 'modalc',
				backdrop : 'static',
				size : 'md',
				
			});

			modalInstance.result.then(function(resultado) {
				if (resultado != undefined){
					mi.impactos.push(resultado);
				}else{
					$utilidades.mensaje('danger', 'Error al agregar impacto');
				}
			}, function() {
			});
		};
		
		mi.quitarImpacto = function(row){
			var index = mi.impactos.indexOf(row);
	        if (index !== -1) {
	            mi.impactos.splice(index, 1);
	        }
		}
		
		mi.agregarMiembro = function() {
			var resultado = mi.llamarModalBusqueda('Colaboradores','/SColaborador', {
				accion : 'totalElementos', t:moment().unix()
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'cargar',
					pagina : pagina,
					numerocooperantes : elementosPorPagina
					,t:moment().unix()
				};
			},'id','nombreCompleto',false, null);

			resultado.then(function(itemSeleccionado) {
				if (itemSeleccionado != undefined ){
					var miembro = {id:itemSeleccionado.id,
							nombre: itemSeleccionado.primerNombre +
							(itemSeleccionado.segundoNombre!=null ? ' ' + itemSeleccionado.segundoNombre : '') +
							' ' + itemSeleccionado.primerApellido +
							(itemSeleccionado.segundoApellido!=null ? ' ' + itemSeleccionado.segundoApellido + ' ' : '')
					}
					mi.miembros.push(miembro);
				}else{
					$utilidades.mensaje('danger', 'Error al agregar miembro');
				}
			});
		};
		
		mi.quitarMiembro = function(row){
			var index = mi.miembros.indexOf(row);
	        if (index !== -1) {
	            mi.miembros.splice(index, 1);
	        }
		};
		
		
		mi.buscarCodigoPresupuestario = function() {	
			var resultado = mi.llamarModalBusqueda('Código Presupuestario','/SDataSigade', {
				accion : 'totalElementos',t:moment().unix()	
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getcodigos',
					pagina : pagina,
					registros : elementosPorPagina ,t:moment().unix()
				};
			},'codigopresupuestario','numeroprestamo', false, null);

			resultado.then(function(itemSeleccionado) {
				if (itemSeleccionado!=null && itemSeleccionado != undefined){
					mi.prestamo.codigoPresupuestario = Number(itemSeleccionado.codigopresupuestario);
					mi.cargaSigade();
					mi.cargarMatriz();
					
					$http.post('/SPrestamo', {
						accion: 'getComponentesSigade',
						codigoPresupuestario : mi.prestamo.codigoPresupuestario
					}).then(function(response){
						if(response.data.success){
							mi.componentes = response.data.componentes;
							mi.rowCollectionComponentes = mi.componentes;
							mi.displayedCollectionComponentes = [].concat(mi.rowCollectionComponentes);
						}
					})
					
					$http.post('/SPrestamo',{
						accion: 'getUnidadesEjecutoras',
						codigoPresupuestario : mi.prestamo.codigoPresupuestario,
						proyectoId : $routeParams.id
					}).then(function(response){
						mi.unidadesEjecutoras = response.data.unidadesEjecutoras;
						mi.rowCollectionUE = mi.unidadesEjecutoras;
						if(mi.rowCollectionUE.length>0){
							mi.rowCollectionUE[0].esCoordinador=true;
						}
						mi.displayCollectionUE = [].concat(mi.rowCollectionUE);

					})
				}
				
			});
		};
		
		if(mi.esTreeview){
			  $http.post('/SProyecto', { accion : 'getProyectoPorId', id: $routeParams.id, t: (new Date()).getTime() }).then(function(response) {
						if (response.data.success) {
							mi.prestamo = response.data.proyecto;
							if(mi.prestamo.fechaInicio != "")
								mi.prestamo.fechaInicio = moment(mi.prestamo.fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.prestamo.fechaFin != "")
								mi.prestamo.fechaFin = moment(mi.prestamo.fechaFin, 'DD/MM/YYYY').toDate();
							mi.editar();
						}
					});
		  }
		
		mi.t_borrar = function(ev) {
			if (mi.prestamo!=null && mi.prestamo.id!=null) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el '+$rootScope.etiquetas.proyecto+' "' + mi.prestamo.nombre + '"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var datos = {
								accion : 'borrarProyecto',
								id : mi.prestamo.id,
								t: (new Date()).getTime()
							};
							$http.post('/SProyecto', datos).success(
									function(response) {
										if (response.success) {
											
											$utilidades.mensaje('success',$rootScope.etiquetas.proyecto+' borrado con éxito');
											mi.prestamo = null;		
											$rootScope.$emit("eliminarNodo", {});
										} else{
											$utilidades.mensaje('danger',
													'Error al borrar el '+$rootScope.etiquetas.proyecto);
										}
									});
					}
				}, function(){
					
				});
			} else {
				$utilidades.mensaje('warning',
						'Debe seleccionar el '+$rootScope.etiquetas.proyecto+' que desee borrar');
			}
		};
		
		mi.t_cambiarNombreNodo = function(ev){
			$rootScope.$emit("cambiarNombreNodo",mi.prestamo.nombre);
			$rootScope.$emit("recargarArbol",mi.prestamo.id);
		}
		
		mi.irAPeps=function(prestamoid){
			if(mi.prestamo!=null){
				$location.path('/pep/'+ prestamoid );
			}
		};
		
		mi.irAPepsArbol=function(prestamoid){
			if(mi.prestamo!=null){
				$window.location='/main_treeview.jsp#!/'+ prestamoid;
			}
		};
		
		
		mi.cargarMatriz = function(){
			mi.matriz_valid = null;
			mi.diferenciaCambios = 0;
			var parametros = {
					accion: 'obtenerMatriz',
					prestamoId: mi.prestamo.id,
					codigoPresupuestario:mi.prestamo.codigoPresupuestario,
				    t:moment().unix()
			};

			$http.post('/SPrestamo', parametros).then(function(response){

				if (response.data.success){
					mi.m_organismosEjecutores = response.data.unidadesEjecutoras;
					$scope.m_componentes = response.data.componentes;
					mi.m_existenDatos = response.data.existenDatos;
					mi.metasCargadas = false;
					mi.riesgosCargados = false;
					mi.activeTab = 0;
					mi.diferenciaCambios = response.data.diferencia;
					mi.actualizarTotalesUE();
				}else{
					$utilidades.mensaje('warning', 'No se encontraron datos con los parámetros ingresados');
				}
			});

		};
		
		mi.guardarMatriz = function(){
			if (mi.matriz_valid && !mi.m_existenDatos && $scope.m_componentes.length > 0 ){
				var parametros = {
						accion: 'guardarMatriz',
						estructura: JSON.stringify($scope.m_componentes),
						prestamoId: mi.prestamo.id,
					    t:moment().unix()
				};
	
				$http.post('/SPrestamo', parametros).then(function(response){
	
					if (response.data.success){
						mi.m_organismosEjecutores = response.data.unidadesEjecutoras;
						$scope.m_componentes = response.data.componentes;
						mi.m_existenDatos = true; 
						return true;
					}else{
						return false;
					}
	
				});
			}
		};
		
		mi.componenteSeleccionado = function(row){
			if(mi.rowAnterior){
				if(row != mi.rowAnterior){
					mi.rowAnterior.isSelected=false;
				}else{
					return;
				}
			}
			row.isSelected = true;
			mi.rowAnterior = row;
		};

		mi.metasActivo = function(){
			if(!mi.metasCargadas){
				mi.metasCargadas = true;
			}
		}
		
		mi.riesgosActivo = function(){
			if(!mi.riesgosCargados){
				mi.riesgosCargados = true;
			}
		}
		
		
		$scope.$watch('m_componentes', function(componentes,componentesOld) {
			mi.matriz_valid = 1;
			mi.totalIngresado  = 0;
		     for (x in componentes){
		    	 componentes[x].totalAsignadoPrestamo = 0;
		    	 var  totalUnidades = 0;
		    	 var totalAsignado = 0;
		    	 for (j in componentes[x].unidadesEjecutoras){
		    		 totalUnidades = totalUnidades +  componentes[x].unidadesEjecutoras[j].prestamo;
		    	 }
		    	 totalAsignado = totalUnidades;
		    	 mi.totalIngresado = mi.totalIngresado + totalUnidades;
		    	 mi.matriz_valid = mi.matriz_valid==1 &&  totalUnidades == componentes[x].techo ? 1 : null;
		    	 
		    	 $scope.m_componentes[x].totalIngesado = totalAsignado;
		     }
		 },true);
		
		mi.adjuntarDocumentos = function(){
			$documentoAdjunto.getModalDocumento($scope, mi.prestamo.id,-1)
			.result.then(function(data) {
				if (data != ""){
					mi.rowCollection = [];
					mi.rowCollection = data;
			        mi.displayedCollection = [].concat(mi.rowCollection);
				}
			}, function(){
				
			});
		}

		mi.getDocumentosAdjuntos = function(objetoId, tipoObjetoId){
			mi.rowCollection = [];
			var formatData = new FormData();
			formatData.append("accion","getDocumentos");
			formatData.append("idObjeto", objetoId);
			formatData.append("idTipoObjeto", tipoObjetoId);
			formatData.append("t", moment().unix());
			
			
			$http.post('/SDocumentosAdjuntos', formatData, {
				headers: {'Content-Type': undefined},
				transformRequest: angular.identity,
			}).then(function(response) {
				if (response.data.success) {
					 mi.rowCollection = response.data.documentos;
			         mi.displayedCollection = [].concat(mi.rowCollection);
				}
			});
		}
		
		mi.descargarDocumento= function(row){
			var url = "/SDocumentosAdjuntos?accion=getDescarga&id="+row.id;
			window.location.href = url;
		}
		
		mi.eliminarDocumento= function(row){
			$http.post('/SDocumentosAdjuntos?accion=eliminarDocumento&id='+row.id)
			.then(function successCAllback(response){
				if (response.data.success){
					var indice = mi.rowCollection.indexOf(row);
					if (indice !== -1) {
				       mi.rowCollection.splice(indice, 1);		       
				    }
					mi.rowCollection = [];
					mi.getDocumentosAdjuntos( mi.prestamo.id,-1);
				}
			});
		};
		
		mi.cambiarCoordinador = function (pos){
			for (x in mi.rowCollectionUE){
				mi.rowCollectionUE[x].esCoordinador = false;
			}
			
			mi.rowCollectionUE[pos].esCoordinador = true;
			
		};
		
		mi.abrirPopupFechaFE = function(index) {
			mi.rowCollectionUE[index].fe_abierto = true;
		}
		
		mi.abrirPopupFechaFC = function(index) {
			mi.rowCollectionUE[index].fc_abierto = true;
		};
		
		mi.seleccionarComponente = function(index){
			
			mi.rowCollectionComponentes[index].mostrar = !mi.rowCollectionComponentes[index].mostrar; 
		}
		
		mi.congelarDescongelar = function () {
			  var modalInstance = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'congelarDescongelar.jsp',
					controller : 'modalCongelarDescongelar',
					controllerAs : 'modalcc',
					backdrop : 'static',
					size : 'lg',
					resolve: {
				        prestamoid: function(){
				        	return mi.prestamo.id;
				        }
				     }
				});
			  
			  
			  
			  modalInstance.result.then(function(resultado) {
					if (resultado != undefined && resultado ){
						$utilidades.mensaje('success', 'Se realizados con éxtio');
					}else{
						$utilidades.mensaje('danger', 'Error al congelar o descongelar');
					}
				}, function() {
				});
		  };
} ]);

app.controller('buscarPorPrestamo', [ '$uibModalInstance',
	'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$titulo', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre','$showfilters','$entidad',buscarPorPrestamo ]);

function buscarPorPrestamo($uibModalInstance, $rootScope,$scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $titulo,$servlet,$accionServlet,$datosCarga,$columnaId,$columnaNombre,$showfilters,$entidad) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
	mi.showfilters = $showfilters;
	mi.ejercicios = [];
	mi.prestamos = [];
	mi.titulo = $titulo;
	
	if(mi.showfilters){
		var current_year = moment().year();
		mi.entidad = $entidad;
		mi.ejercicio = $entidad.ejercicio;
		for(var i=current_year-$rootScope.catalogo_entidades_anos; i<=current_year; i++)
			mi.ejercicios.push(i);
		mi.ejercicio = (mi.ejercicio == "" || mi.ejercicio == null) ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio,t:moment().unix()}).success(function(response) {
			mi.prestamos = response.entidades;
			if(mi.prestamos.length>0){
				mi.entidad = (mi.entidad.entidad == null || mi.entidad===undefined || mi.entidad.entidad== "") ? mi.prestamos[0] : mi.entidad;
				$accionServlet.ejercicio = mi.ejercicio;
				$accionServlet.entidad = mi.entidad.entidad;
				$http.post($servlet, $accionServlet).success(function(response) {
					for ( var key in response) {
						mi.totalElementos = response[key];
					}
					mi.cargarData(1,mi.ejercicio,mi.entidad.entidad);
				});
			}
			
		});
	}
	else{
		$http.post($servlet, $accionServlet).success(function(response) {
			for ( var key in response) {
				mi.totalElementos = response[key];
			}
			mi.cargarData(1,0,0);
		});
	}

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : $columnaId,
			cellClass : 'grid-align-right',
			type : 'number',
			width : 100
		}, {
			displayName : 'Nombre',
			name : $columnaNombre,
			cellClass : 'grid-align-left'
		} ],
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		multiSelect : false,
		modifierKeysToMultiSelect : false,
		noUnselect : false,
		enableFiltering : true,
		enablePaginationControls : false,
		paginationPageSize : 5,
		onRegisterApi : function(gridApi) {
			mi.gridApi = gridApi;
			mi.gridApi.selection.on.rowSelectionChanged($scope,
					mi.seleccionarTipoRiesgo);
		}
	}

	mi.seleccionarTipoRiesgo = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarData = function(pagina,ejercicio, entidad) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina,ejercicio, entidad)).then(
				function(response) {
					if (response.data.success) {
						for ( var key in response.data) {
							if (key != 'success')
								mi.data = response.data[key];
						}
						mi.opcionesGrid.data = mi.data;

						mi.mostrarCargando = false;
					}
				});
	};

	mi.cambioPagina = function() {
		mi.cargarData(mi.paginaActual, mi.ejercicio, mi.entidad!= undefined ? mi.entidad.entidad : null);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cambioEjercicio= function(){
		mi.cargarData(1,mi.ejercicio, mi.entidad.entidad);
	}
	
	mi.cambioEntidad=function(selected){
		if(selected!==undefined){
			mi.entidad = selected.originalObject;
			$http.post('/SUnidadEjecutora', {accion:"totalElementos", ejercicio: mi.entidad.ejercicio,entidad: mi.entidad.entidad,t:moment().unix()}).success(function(response) {
				for ( var key in response) {
					mi.totalElementos = response[key];
				}
				mi.cargarData(1,mi.ejercicio,mi.entidad.entidad);
			});
		}
	};
	
	
	
}

app.controller('modalAgregarImpacto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q' ,modalAgregarImpacto ]);

function modalAgregarImpacto($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q) {

	var mi = this;
	mi.impacto = {};
	
	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorPrestamo.jsp',
			controller : 'buscarPorPrestamo',
			controllerAs : 'modalBuscar',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$servlet : function() {
					return servlet;
				},
				$accionServlet : function() {
					return accionServlet;
				},
				$datosCarga : function() {
					return datosCarga;
				},
				$columnaId : function() {
					return columnaId;
				},
				$columnaNombre : function() {
					return columnaNombre;
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};

	mi.buscarEntidad = function() {
		var resultado = mi.llamarModalBusqueda('/SEntidad', {
			accion : 'totalEntidades',t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
				,t:moment().unix()
			};
		},'entidad','nombre');
		resultado.then(function(itemSeleccionado) {
			mi.impacto.entidadNombre = itemSeleccionado.nombre;
			mi.impacto.entidadId = itemSeleccionado.entidad;
		});
	};
	
	mi.ok = function() {
		$uibModalInstance.close(mi.impacto);
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
};

app.controller('modalCongelarDescongelar', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q', 'prestamoid' ,modalCongelarDescongelar ]);

function modalCongelarDescongelar($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, prestamoid) {

	var mi = this;
	mi.peps = [];
	mi.mostrarcargando=true;
	mi.crearLineaBase = false;
	$http.post('/SProyecto', { 
		accion: 'getProyectos', 
		prestamoid: prestamoid,
		t: (new Date()).getTime() }).success(
			function(response) {
				mi.mostrarcargando=false;
				mi.peps = response.entidades;
				for (x in mi.peps)
					mi.peps[x].congeladoTemp = mi.peps[x].congelado==1;
	});
	
	mi.ok = function() {
		var peps = [];
		for (x in mi.peps){
			var pep = {};
			pep.id = mi.peps[x].id;
			pep.congelado = mi.peps[x].congeladoTemp;
			peps.push(pep);
		}
		
		mi.mostrarcargando=true;
		$http.post('/SPrestamo', { 
			accion: 'congelarDescongelar', 
			lineaBase: mi.crearLineaBase,
			nombre: mi.nombre,
			peps : JSON.stringify(peps),
			t: (new Date()).getTime() }).success(
				function(response) {
					console.log(response.success);
					mi.mostrarcargando=true;
					$uibModalInstance.close(response.success);
		});
		
		
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cambioCheck = function (){
		mi.nombre = '';
	}
};