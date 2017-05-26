var app = angular.module('proyectoController', [ 'ngTouch','smart-table',  'ui.bootstrap.contextMenu']);

app.controller('proyectoController',['$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','$filter', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,$filter, $dialogoConfirmacion) {

	var mi = this;
	i18nService.setCurrentLang('es');

	$window.document.title = $utilidades.sistema_nombre+' - Préstamos';
		
	mi.rowCollection = [];
	mi.proyecto = null;
	mi.esNuevo = false;
	mi.esNuevoDocumento = true;
	mi.campos = {};
	mi.esColapsado = false;
	mi.mostrarcargando=true;
	mi.paginaActual = 1;
	mi.cooperantes = [];
	mi.proyectotipos = [];
	mi.unidadesejecutoras = [];
	mi.poryectotipoid = "";
	mi.proyectotiponombre="";
	mi.unidadejecutoraid="";
	mi.unidadejecutoranombre="";
	mi.cooperanteid="";
	mi.cooperantenombre="";
	mi.camposdinamicos = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.totalProyectos = 0;
	mi.mostrarPrestamo = true;

	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.prestamo = [];
	
	mi.prestamo.desembolsoAFechaUsdP = "";
	mi.prestamo.montoPorDesembolsarUsdP = "";
	mi.prestamo.desembolsoAFechaUeUsdP = "";
	mi.prestamo.montoPorDesembolsarUeUsdP = "";
	mi.prestamo.plazoEjecucionUe = "";
	
	mi.coordenadas = "";

	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};


	mi.editarElemento = function (event) {
        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
        mi.gridApi.selection.selectRow(mi.gridOpciones.data[filaId]);
        mi.editar();
    };
    
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
	    rowTemplate: '<div ng-dblclick="grid.appScope.controller.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [
			{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre',  displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'nombre\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:175px;"></input></div>'
			},
			{ name : 'proyectotipo',    displayName : 'Tipo préstamo' ,cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false },
			{ name : 'unidadejecutora',    displayName : 'Unidad Ejecutora' ,cellClass: 'grid-align-left', enableFiltering: false , enableSorting: false },
			{ name : 'cooperante',   displayName : 'Cooperante' ,cellClass: 'grid-align-left',  enableFiltering: false , enableSorting: false },
			{ name: 'usuarioCreo', width: 120, displayName: 'Usuario Creación',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.controller.filtrar($event)" style="width:90px;"></input></div>'
			},
		    { name: 'fechaCreacion', width: 100, displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.controller.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.controller.filtrar($event)" style="width:80px;" ></input></div>'
		    }
		],
		onRegisterApi: function(gridApi) {
			mi.gridApi = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.proyecto = row.entity;
			});

			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.controller.columnaOrdenada=sortColumns[0].field;
					grid.appScope.controller.ordenDireccion = sortColumns[0].sort.direction;

					grid.appScope.controller.cargarTabla(grid.appScope.controller.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.controller.columnaOrdenada!=null){
						grid.appScope.controller.columnaOrdenada=null;
						grid.appScope.controller.ordenDireccion=null;
					}
				}
			} );

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
				mi.obtenerTotalProyectos();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyceto', t: (new Date()).getTime()}).then(function(response){
		    		  if(response.data.success && response.data.estado!='')
		    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
			    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
				      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
				      mi.obtenerTotalProyectos();
				  });
		    }
		}
	};

	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';
	}
	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SProyecto', { accion: 'getProyectoPagina', pagina: pagina,
			numeroproyecto:  $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t:moment().unix()
			}).success(
				function(response) {
					mi.entidades = response.proyectos;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
	};
	
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
			if(mi.prestamo.desembolsoAFechaUsd != undefined && mi.prestamo.montoContratado != undefined){
				n = (mi.prestamo.desembolsoAFechaUsd / mi.prestamo.montoContratado) * 100;
				mi.prestamo.desembolsoAFechaUsdP = Number(n.toFixed(2));
			}
		}else if (tipo==2){
			if(mi.prestamo.montoContratadoUsd != undefined && mi.prestamo.montoPorDesembolsarUsd != undefined){
				n = (mi.prestamo.montoPorDesembolsarUsd / mi.prestamo.montoContratadoUsd) * 100;
				mi.prestamo.montoPorDesembolsarUsdP = Number(n.toFixed(2));
			}
		}else if (tipo==3){
			if(mi.prestamo.desembolsoAFechaUeUsd != undefined && mi.prestamo.montoAsignadoUe != undefined){
				n = (mi.prestamo.desembolsoAFechaUeUsd / mi.prestamo.montoAsignadoUe) * 100;
				mi.prestamo.desembolsoAFechaUeUsdP = Number(n.toFixed(2));
			}
		}else if(tipo==4){
			if(mi.prestamo.montoAsignadoUeUsd != undefined && mi.prestamo.montoPorDesembolsarUeUsd != undefined){
				n = (mi.prestamo.montoPorDesembolsarUeUsd / mi.prestamo.montoAsignadoUeUsd) * 100;
				mi.prestamo.montoPorDesembolsarUeUsdP = Number(n.toFixed(2));
			}
		}else if(tipo==5){
			if(mi.prestamo.fechaCierreActualUe != undefined && mi.prestamo.fechaElegibilidadUe != undefined){
				var fechaInicio = moment(mi.prestamo.fechaElegibilidadUe).format('DD/MM/YYYY').split('/');
				var fechaFinal = moment(mi.prestamo.fechaCierreActualUe).format('DD/MM/YYYY').split('/');
				var ffechaInicio = Date.UTC(fechaInicio[2],fechaInicio[1]-1,fechaInicio[0]);
				var ffechaFinal = Date.UTC(fechaFinal[2],fechaFinal[1]-1,fechaFinal[0]);
				
				var hoy = new Date();
				var fechaActual = hoy.today().split('/');
				var ffechaActual = Date.UTC(fechaActual[2],fechaActual[1]-1,fechaActual[0]);
				
				var dif1 = ffechaFinal - ffechaInicio;
				var dif2 = ffechaActual - ffechaInicio;
				n = (dif2 / dif1) * 100;
				if (isNaN(n))
					n = 0.00;
				mi.prestamo.plazoEjecucionUe = Number(n.toFixed(2));
			}
			
		}
	};

	Date.prototype.today = function () { 
	    return ((this.getDate() < 10)?"0":"") + this.getDate() +"/"+(((this.getMonth()+1) < 10)?"0":"") + (this.getMonth()+1) +"/"+ this.getFullYear();
	}
	
	mi.guardar = function(esvalido){
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
			}
		}
		if(mi.proyecto!=null && mi.proyecto.nombre!=null){
			var param_data = {
				accion : 'guardar',
				id: mi.proyecto.id,
				nombre: mi.proyecto.nombre,
				snip: mi.proyecto.snip,
				objetivo: mi.proyecto.objetivo,
				descripcion:mi.proyecto.descripcion,
				proyectotipoid: mi.poryectotipoid,
				unidadejecutoraid: mi.unidadejecutoraid,
				cooperanteid: mi.cooperanteid,
				programa: mi.proyecto.programa,
				subprograma: mi.proyecto.subprograma,
				proyecto_: mi.proyecto.proyecto,
				obra:mi.proyecto.obra,
				actividad: mi.proyecto.actividad,
				fuente: mi.proyecto.fuente,
				esnuevo: mi.esNuevo,
				longitud: mi.proyecto.longitud,
				latitud : mi.proyecto.latitud,
				datadinamica : JSON.stringify(mi.camposdinamicos),

				codigoPresupuestario: mi.prestamo.codigoPresupuestario,
				numeroPrestamo: mi.prestamo.numeroPrestamo,
				proyetoPrograma: mi.prestamo.proyectoPrograma,
				unidadEjecutora: mi.prestamo.unidadEjecutora,
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
				t:moment().unix()
			};
			$http.post('/SProyecto',param_data).then(
				function(response) {
					if (response.data.success) {
						mi.proyecto.id = response.data.id;
						mi.proyecto.usuarioCreo = response.data.usuarioCreo;
						mi.proyecto.fechaCreacion = response.data.fechaCreacion;
						mi.proyecto.usuarioactualizo = response.data.usuarioactualizo;
						mi.proyecto.fechaactualizacion = response.data.fechaactualizacion;
						$utilidades.mensaje('success','Préstamo '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
						mi.obtenerTotalProyectos();
						mi.esNuevo = false;
					}else
						$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Préstamo');
			});

			mi.esNuevoDocumento = false;
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	 }

	mi.borrar = function(ev) {
		if(mi.proyecto !=null && mi.proyecto.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el Préstamo "'+mi.proyecto.nombre+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					$http.post('/SProyecto', {
						accion: 'borrarProyecto',
						id: mi.proyecto.id,
						t:moment().unix()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Préstamo borrado con éxito');
							mi.proyecto = null;
							mi.obtenerTotalProyectos();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Préstamo');
					});
				}
			}, function(){
				
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Préstamo que desea borrar');
	};

	mi.nuevo = function (){
		mi.esNuevoDocumento = true;		
		mi.poryectotipoid = "";
		mi.proyectotiponombre="";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.cooperanteid="";
		mi.cooperantenombre="";
		mi.esColapsado = true;
		mi.proyecto = {};
		mi.esNuevo = true;
		mi.camposdinamicos = {};
		mi.coordenadas = "";
		mi.gridApi.selection.clearSelectedRows();
		mi.prestamo = [];
		$scope.active = 0;
	};

	mi.editar = function() {
		if(mi.proyecto!=null && mi.proyecto.id!=null){
			mi.esNuevoDocumento = false;
			mi.poryectotipoid = mi.proyecto.proyectotipoid;
			mi.proyectotiponombre=mi.proyecto.proyectotipo;
			mi.unidadejecutoraid=mi.proyecto.unidadejecutoraid;
			mi.unidadejecutoranombre=mi.proyecto.unidadejecutora;
			mi.cooperanteid=mi.proyecto.cooperanteid;
			mi.cooperantenombre=mi.proyecto.cooperante;
			mi.esColapsado = true;
			mi.esNuevo = false;
			mi.coordenadas = (mi.proyecto.latitud !=null ?  mi.proyecto.latitud : '') +
			(mi.proyecto.latitud!=null ? ', ' : '') + (mi.proyecto.longitud!=null ? mi.proyecto.longitud : '');

			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.proyecto.id:0,
				    idProyectoTipo: mi.poryectotipoid,
				    t:moment().unix()
			}
			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades
				for (campos in mi.camposdinamicos) {
					switch (mi.camposdinamicos[campos].tipo){
					case "fecha":
						mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
						break;
					case "entero":
						mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
						break;
					case "decimal":
						mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
						break;
					case "booleano":
						mi.camposdinamicos[campos].valor = mi.camposdinamicos[campos].valor == 'true' ? true : false;
						break;
					}
				}
			});
			
			parametros = {
					accion: 'getPrestamo',
					objetoId: mi.proyecto.id,
				    objetoTipo : 1,
				    t:moment().unix()
			}
			
			$http.post('/SPrestamo', parametros).then(function(response){
				if (response.data.prestamo!=null){
					mi.prestamo = response.data.prestamo;
					mi.prestamo.fechaCorte = mi.prestamo.fechaCorte != undefined ?  moment(mi.prestamo.fechaCorte,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaFirma = mi.prestamo.fechaFirma != undefined ? moment (mi.prestamo.fechaFirma,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaAutorizacion = mi.prestamo.fechaAutorizacion != undefined ? moment(mi.prestamo.fechaAutorizacion,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaFinEjecucion = mi.prestamo.fechaFinEjecucion != undefined ? moment (mi.prestamo.fechaFinEjecucion,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaDecreto = mi.prestamo.fechaDecreto != undefined ? moment (mi.prestamo.fechaDecreto,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaSuscripcion = mi.prestamo.fechaSuscripcion != undefined ? moment(mi.prestamo.fechaSuscripcion,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaElegibilidadUe = mi.prestamo.fechaElegibilidadUe != undefined ? moment(mi.prestamo.fechaElegibilidadUe,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaCierreOrigianlUe = mi.prestamo.fechaCierreOrigianlUe != undefined ? moment (mi.prestamo.fechaCierreOrigianlUe,'DD/MM/YYYY').toDate() : undefined; 
					mi.prestamo.fechaCierreActualUe = mi.prestamo.fechaCierreActualUe != undefined ? moment (mi.prestamo.fechaCierreActualUe,'DD/MM/YYYY').toDate() : undefined;
					mi.prestamo.fechaVigencia = mi.prestamo.fechaVigencia != undefined ? moment(mi.prestamo.fechaVigencia,'DD/MM/YYYY').toDate() : undefined;
				}
				
			});
			
			parametros = {
					accion: 'obtenerProyectosPorPrograma',
					idPrograma: mi.proyecto!=''? mi.proyecto.id:0,
				    t:moment().unix()
			}
			
			$http.post('/SProyecto', parametros).then(function(response){
				mi.proyectos = response.data.proyectos;
				
			});

			mi.getDocumentosAdjuntos(1, mi.proyecto.id);
			$scope.active = 0;
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Préstamo que desea editar');
	}

	mi.adjuntarDocumentos = function(){
		$documentoAdjunto.getModalDocumento($scope, 1, mi.proyecto.id)
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
				mi.getDocumentosAdjuntos(1, mi.proyecto.id);
			}
		});
	};

	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}

	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'proyceto', estado: JSON.stringify(estado) };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
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
			mi.obtenerTotalProyectos();
			mi.gridApi.selection.clearSelectedRows();
			mi.proyecto = null;
		}
	};

	mi.obtenerTotalProyectos = function(){
		$http.post('/SProyecto', { accion: 'numeroProyectos',t:moment().unix(),
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  } ).then(
				function(response) {
					mi.totalProyectos = response.data.totalproyectos;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};

	mi.irADesembolsos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/desembolso/'+proyectoid);
		}

	};

	mi.irAComponentes=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/componente/'+ proyectoid );
		}
	};

	mi.irARiesgos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/riesgo/' + proyectoid + '/1' );
		}
	};

	mi.irAHitos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/hito/'+ proyectoid );
		}
	};

	mi.irAActividades=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/actividad/'+ proyectoid +'/1' );
		}
	};

	mi.irAGantt=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/gantt/'+ proyectoid );
		}
	};
	mi.irAMapa=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/mapa/'+ proyectoid );
		}
	};
	mi.irAKanban=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/porcentajeactividades/'+ proyectoid );
		}
	};

	mi.irAAgenda=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/agenda/'+ proyectoid );
		}
	};
	
	mi.irAMatrizRiesgos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/matrizriesgo/'+ proyectoid );
		}
	};

	mi.irAPrestamoMetas=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/prestamometas/'+ proyectoid );
		}
	};

	mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyecto',
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

	mi.llamarModalBusquedaUnidadEjec = function(servlet, accionServlet, datosCarga) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorProyecto.jsp',
			controller : 'buscarPorProyectoUnidadEjec',
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
				}
			}
		});

		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};

	mi.buscarProyectoTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SProyectoTipo', {
			accion : 'numeroProyectoTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getProyectoTipoPagina',
				pagina : pagina,
				numeroproyectotipo : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.poryectotipoid= itemSeleccionado.id;
			mi.proyectotiponombre = itemSeleccionado.nombre;

			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.poryectotipoid.id:0,
					idProyectoTipo: itemSeleccionado.id,
					t:moment().unix()
			}

			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades;
				for (campos in mi.camposdinamicos) {
					switch (mi.camposdinamicos[campos].tipo){
						case "fecha":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? moment(mi.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : null;
							break;
						case "entero":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
							break;
						case "decimal":
							mi.camposdinamicos[campos].valor = (mi.camposdinamicos[campos].valor!='') ? Number(mi.camposdinamicos[campos].valor) : null;
							break;
						case "booleano":
							mi.camposdinamicos[campos].valor = mi.camposdinamicos[campos].valor == 'true' ? true : false;
							break;
					}
				}
			});
		});
	};

	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora');

		resultado.then(function(itemSeleccionado) {
			mi.unidadejecutoraid= itemSeleccionado.unidadEjecutora;
			mi.unidadejecutoranombre = itemSeleccionado.nombreUnidadEjecutora;

		});
	};

	mi.buscarCooperante = function(prestamo) {
		var resultado = mi.llamarModalBusqueda('/SCooperante', {
			accion : 'numeroCooperantes', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getCooperantesPagina',
				pagina : pagina,
				numerocooperantes : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			if (prestamo){
				mi.prestamo.cooperanteid= itemSeleccionado.id;
				mi.prestamo.cooperantenombre = itemSeleccionado.nombre;
			}
			else{
				mi.cooperanteid= itemSeleccionado.id;
				mi.cooperantenombre = itemSeleccionado.nombre;
			}

		});
	};
	
	mi.llamarModalArchivo = function() {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'cargarArchivo.jsp',
			controller : 'cargararchivoController',
			controllerAs : 'cargararchivoc',
			backdrop : 'static',
			size : 'md',
		});

		modalInstance.result.then(function(respuesta) {
			resultado.resolve(respuesta);
		});
		return resultado.promise;
	};
	
	mi.cargarArchivo = function() {
		var resultado = mi.llamarModalArchivo();

		resultado.then(function(resultado) {
			mi.mostrarcargando=false;
			if (resultado.data.success){
				mi.obtenerTotalProyectos();
				$utilidades.mensaje('success','Préstamo creado con éxito');
			}else{
				$utilidades.mensaje('danger','Error al crear el Préstamo');
			}
			
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
		var resultado = mi.llamarModalBusqueda('/SUnidadEjecutora', {
			accion : 'totalElementos'	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'unidadEjecutora','nombreUnidadEjecutora');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.prestamo.unidadEjecutora = itemSeleccionado.unidadEjecutora;
		});
	};
	
	mi.buscarAutorizacionTipo = function() {
		
		var resultado = mi.llamarModalBusqueda('/SAutorizacionTipo', {
			accion : 'numeroAutorizacionTipo'	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getAutorizacionTipoPagin',
				pagina : pagina,
				numeroautorizaciontipo : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoAutorizacionNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoAutorizacionId = itemSeleccionado.id;
		});
	};
	
	mi.buscarInteresTipo = function() {
		
		var resultado = mi.llamarModalBusqueda('/SInteresTipo', {
			accion : 'numeroInteresTipo'	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getAutorizacionTipoPagin',
				pagina : pagina,
				numerointerestipo : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoInteresNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoInteresId = itemSeleccionado.id;
		});
	};
	
	mi.buscarTipoMoneda = function() {
		
		var resultado = mi.llamarModalBusqueda('/STipoMoneda', {
			accion : 'numeroTipoMonedas'	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getTipoMnedaPagina',
				pagina : pagina,
				numerotipomoneda : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoMonedaNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoMonedaId = itemSeleccionado.id;
		});
	};
	
	mi.buscarEstadoEjecucion = function() {
		
		var resultado = mi.llamarModalBusqueda('/SEjecucionEstado', {
			accion : 'numeroEjecucionEstado'	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getEjecucionEstadoPagina',
				pagina : pagina,
				numeroejecucionestado : elementosPorPagina
			};
		},'id','nombre');

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
	    	if (coordenadas !=null){
		    	mi.coordenadas = coordenadas.latitude + ", " + coordenadas.longitude;
		    	mi.proyecto.latitud= coordenadas.latitude;
				mi.proyecto.longitud = coordenadas.longitude;
	    	}
	    }, function() {
		});
	  };
	  
	  mi.cargaSigade = function(){
			var parametros = {
					accion: 'getdatos',
					noPrestamo: mi.prestamo.numeroPrestamo,
					codigoPresupuestario:mi.prestamo.codigoPresupuestario,
				    t:moment().unix()
			}
			$http.post('/SDataSigade', parametros).then(function(response){
				mi.prestamo={};
				if (response.data.success){
					mi.prestamo.codigoPresupuestario = Number(response.data.prestamo.codigoPresupuestario);
					mi.prestamo.numeroPrestamo = response.data.prestamo.numeroPrestamo;
					mi.prestamo.proyectoPrograma = response.data.prestamo.proyectoPrograma;
					mi.prestamo.cooperantenombre = response.data.prestamo.cooperantenombre;
					mi.prestamo.fechaDecreto = moment(response.data.prestamo.fechaDecreto,'DD/MM/YYYY').toDate()
					mi.prestamo.fechaSuscripcion = moment(response.data.prestamo.fechaSuscripcion,'DD/MM/YYYY').toDate();
					mi.prestamo.fechaVigencia = moment(response.data.prestamo.fechaVigencia,'DD/MM/YYYY').toDate();
					mi.prestamo.tipoMonedaNombre = response.data.prestamo.tipoMonedaNombre;
					mi.prestamo.tipoMonedaId= response.data.prestamo.tipoMonedaId;
					mi.prestamo.montoContratado = response.data.prestamo.montoContratado;
					mi.prestamo.montoContratadoUsd = response.data.prestamo.montoContratadoUsd;
					mi.prestamo.montoContratadoQtz = response.data.prestamo.montoContratadoQtz;
					mi.prestamo.desembolsoAFechaUsd = response.data.prestamo.desembolsoAFechaUsd;
					mi.prestamo.montoPorDesembolsarUsd =  response.data.prestamo.montoPorDesembolsarUsd;
					mi.getPorcentajes();
				}else{
					$utilidades.mensaje('warning', 'No se encontraron datos con los parámetros ingresados');
				}
				
			});
			
		}
	  
	  
} ]);

app.controller('buscarPorProyecto', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre',buscarPorProyecto ]);

function buscarPorProyecto($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga,$columnaId,$columnaNombre) {

	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];

	mi.itemSeleccionado = null;
	mi.seleccionado = false;

	$http.post($servlet, $accionServlet).success(function(response) {
		for ( var key in response) {
			mi.totalElementos = response[key];
		}
		mi.cargarData(1);
	});

	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'ID',
			name : $columnaId,
			cellClass : 'grid-align-right',
			type : 'number',
			width : 70
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

	mi.cargarData = function(pagina) {
		mi.mostrarCargando = true;
		$http.post($servlet, $datosCarga(pagina, mi.elementosPorPagina)).then(
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
		mi.cargarData(mi.paginaActual);
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
}

app.controller('mapCtrl',[ '$scope','$uibModalInstance','$timeout', 'uiGmapGoogleMapApi','glat','glong',
    function ($scope, $uibModalInstance,$timeout, uiGmapGoogleMapApi, glat, glong) {
	$scope.geoposicionlat = glat != null ? glat : 14.6290845;
	$scope.geoposicionlong = glong != null ? glong : -90.5116158;
	$scope.posicion = (glat !=null && glong !=null ) ? {latitude: glat, longitude: glong} : null;
	$scope.refreshMap = true;

	uiGmapGoogleMapApi.then(function() {
		$scope.map = { center: { latitude: $scope.geoposicionlat, longitude: $scope.geoposicionlong },
		   zoom: 15,
		   height: 400,
		   width: 200,
		   options: {
			   streetViewControl: false,
			   scrollwheel: true,
			  draggable: true,
			  mapTypeId: google.maps.MapTypeId.SATELLITE
		   },
		   events:{
			   click: function (map,evtName,evt) {
				   $scope.posicion = {latitude: evt[0].latLng.lat()+"", longitude: evt[0].latLng.lng()+""} ;
				   $scope.$evalAsync();
			   }
		   },
		   refresh: true
		};
    });

	  $scope.ok = function () {
		  $uibModalInstance.close($scope.posicion);
	  };
}]);

app.directive('rightClick', function($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.rightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });
    };
});

app.controller('cargararchivoController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log','$q', cargararchivoController ]);

function cargararchivoController($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log,$q) {

	var mi = this;
	mi.mostrar = true;
	mi.nombreArchivo="";
	mi.mostrarcargando=false;
	
	$scope.cargarArchivo = function(event){
		var resultado = $q.defer();
	     mi.archivos = event.files[0];      
	     mi.nombreArchivo = mi.archivos.name;
	     resultado.resolve(event.files[0]);
	     document.getElementById("nombreArchivo").value = mi.nombreArchivo;
	     return resultado.promise;
	};

	mi.ok = function() {
		if (mi.nombreArchivo != '') {
			mi.cargar();
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un archivo');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cargar=function(){
		if (mi.archivos!=null && mi.arhivos != ''){
			mi.mostrarcargando=true;
			var formatData = new FormData();
			formatData.append("file",mi.archivos);  
			formatData.append("accion",'importar');
			$http.post('/SGantt',formatData, {
					headers: {'Content-Type': undefined},
					transformRequest: angular.identity
				 } ).then(
			
				function(response) {
					mi.mostrarcargando=false;
					$uibModalInstance.close(response);
				}
			);
		}else{
			$utilidades.mensaje('danger','Debe seleccionar un archivo');
		}
	};
}
