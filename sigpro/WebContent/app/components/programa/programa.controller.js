var app = angular.module('programaController', [ 'ngTouch' ,'ui.bootstrap.contextMenu']);

app.controller('programaController',['$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {

	var mi = this;
	i18nService.setCurrentLang('es');
	
	$window.document.title = $utilidades.sistema_nombre+' - Programas';
	
	mi.programa = null;
	mi.esNuevo = false;
	mi.campos = {};
	mi.esColapsado = false;
	mi.mostrarcargando=true;
	mi.esNuevoDocumento = true;
	mi.paginaActual = 1;
	mi.programatipoid = "";
	mi.programatiponombre="";
	mi.camposdinamicos = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.totalProgramas = 0;
	mi.mostrarPrestamo = true;
	mi.unidadejecutoraid="";
	mi.entidad="";
	mi.ejercicio="";
	mi.unidadejecutoranombre="";
	mi.entidadnombre="";

	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;

	mi.filtros = [];
	mi.orden = null;
	
	mi.proyectos =[];

	mi.prestamo = [];
	
	mi.prestamo.desembolsoAFechaUsdP = "";
	mi.prestamo.montoPorDesembolsarUsdP = "";
	mi.prestamo.desembolsoAFechaUeUsdP = "";
	mi.prestamo.montoPorDesembolsarUeUsdP = "";
	mi.prestamo.plazoEjecucionUe = "";
	
	mi.proyecto =null;
	mi.mostrarcargandoProyecto=true;
	mi.mostrarProyecto = false;
	mi.paginaActualProyectos=1;

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
    
    
    mi.adjuntarDocumentos = function(){
		$documentoAdjunto.getModalDocumento($scope, 6, mi.programa.id)
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
		mi.displayedCollection = [];
		mi.esNuevoDocumento = true;
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
		         if (mi.rowCollection.length>0)
		        	 mi.esNuevoDocumento = false;
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
			       mi.rowCollection.splice(indice, 6);		       
			    }
				mi.rowCollection = [];
				mi.getDocumentosAdjuntos(6, mi.programa.id);
			}
		});
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
	    rowTemplate: '<div ng-dblclick="grid.appScope.programac.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
		columnDefs : [
			{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre',  displayName: 'Nombre',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programac.filtros[\'nombre\']" ng-keypress="grid.appScope.programac.filtrar($event)" ></input></div>'
			},
			{ name : 'programatipo',    displayName : 'Tipo programa' ,cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false },
			{ name: 'usuarioCreo',  displayName: 'Usuario Creación',cellClass: 'grid-align-left',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text"style="width: 90%;" ng-model="grid.appScope.programac.filtros[\'usuario_creo\']"  ng-keypress="grid.appScope.programac.filtrar($event)" ></input></div>'
			},
		    { name: 'fechaCreacion',  displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\'',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.programac.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.programac.filtrar($event)"  ></input></div>'
		    }
		],
		onRegisterApi: function(gridApi) {
			mi.gridApi = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.programa = row.entity;
			});

			gridApi.core.on.sortChanged( $scope, function ( grid, sortColumns ) {
				if(sortColumns.length==1){
					grid.appScope.programac.columnaOrdenada=sortColumns[0].field;
					grid.appScope.programac.ordenDireccion = sortColumns[0].sort.direction;

					grid.appScope.programac.cargarTabla(grid.appScope.programac.paginaActual);
				}
				else if(sortColumns.length>1){
					sortColumns[0].unsort();
				}
				else{
					if(grid.appScope.programac.columnaOrdenada!=null){
						grid.appScope.programac.columnaOrdenada=null;
						grid.appScope.programac.ordenDireccion=null;
					}
				}
			} );

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
				mi.obtenerTotalProgramas();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'programa', t: (new Date()).getTime()}).then(function(response){
		    		  if(response.data.success && response.data.estado!='')
		    			  mi.gridApi.saveState.restore( $scope, response.data.estado);
			    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
				      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
				      mi.obtenerTotalProgramas();
				  });
		    }
		}
	};


	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SPrograma', { accion: 'getProgramaPagina', pagina: pagina,
			numeroprograma:  $utilidades.elementosPorPagina, filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
			columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t:moment().unix()
			}).success(
				function(response) {
					mi.entidades = response.programas;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
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
				mi.prestamo.montoPorDesembolsarUsdP= 100- mi.prestamo.desembolsoAFechaUsdP;
				
			}
		}else if (tipo==2){
			if(mi.prestamo.montoContratadoUsd !== undefined && mi.prestamo.montoPorDesembolsarUsd !== undefined){
				n = (mi.prestamo.montoPorDesembolsarUsd / mi.prestamo.montoContratadoUsd) * 100;
				mi.prestamo.montoPorDesembolsarUsdP = Number(n.toFixed(2));
				
			}
		}else if (tipo==3){
			if(mi.prestamo.desembolsoAFechaUeUsd !== undefined && mi.prestamo.montoAsignadoUe !== undefined){
				n = (mi.prestamo.desembolsoAFechaUeUsd / mi.prestamo.montoAsignadoUeUsd) * 100;
				mi.prestamo.desembolsoAFechaUeUsdP = Number(n.toFixed(2));
				mi.prestamo.montoPorDesembolsarUeUsd = ((1.00 - (mi.prestamo.desembolsoAFechaUeUsdP/100.00) ) *  (mi.prestamo.montoAsignadoUeUsd*1.00));
				mi.prestamo.montoPorDesembolsarUeUsdP= 100.00 - mi.prestamo.desembolsoAFechaUeUsdP;
			}
		}else if(tipo==4){
			if(mi.prestamo.montoAsignadoUeUsd !== undefined && mi.prestamo.montoPorDesembolsarUeUsd !== undefined){
				n = (mi.prestamo.montoPorDesembolsarUeUsd / mi.prestamo.montoAsignadoUeUsd) * 100;
				mi.prestamo.montoPorDesembolsarUeUsdP = Number(n.toFixed(2));
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

	mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}

	mi.guardar = function(esvalido){
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
			}
		}
		
		var idsproyectos="";
		for (i = 0 ; i<mi.proyectos.length ; i ++){
			if (i==0){
				idsproyectos = idsproyectos.concat("",mi.proyectos[i].id);
			}else{
				idsproyectos = idsproyectos.concat(",",mi.proyectos[i].id);
			}
		}
		
		if(mi.programa!=null && mi.programa.nombre!=null){
			var param_data = {
				accion : 'guardar',
				id: mi.programa.id,
				nombre: mi.programa.nombre,
				descripcion:mi.programa.descripcion,
				programatipoid: mi.programatipoid,
				esnuevo: mi.esNuevo,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				idsproyectos:idsproyectos,
				objetoTipo:6,
				t:moment().unix()
			};
			$http.post('/SPrograma',param_data).then(
				function(response) {
					if (response.data.success) {
						mi.programa.id = response.data.id;
						mi.programa.usuarioCreo = response.data.usuarioCreo;
						mi.programa.fechaCreacion = response.data.fechaCreacion;
						mi.programa.usuarioactualizo = response.data.usuarioactualizo;
						mi.programa.fechaactualizacion = response.data.fechaactualizacion;
						mi.obtenerTotalProgramas();
						
						if (mi.prestamo!=null && mi.prestamo.codigoPresupuestario !=null && mi.prestamo.codigoPresupuestario != ''){
							var param_data = {
									accion: "gurdarPrestamo",
									objetoId: mi.programa.id,
									objetoTipo: 6,
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
									t:moment().unix()
								};
								
							
								$http.post('/SPrestamo',param_data).then(
										function(response) {
											if (response.data.success) {
												$utilidades.mensaje('success','Programa '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
											}else
												$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Programa');
								});
						} 
						mi.esNuevo = false;
						
					}else
						$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'crearo' : 'guardar')+' el Programa');
			});
			mi.esNuevoDocumento = false;
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	 }

	mi.borrar = function(ev) {
		if(mi.programa !=null && mi.programa.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el programa "'+mi.programa.nombre+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					$http.post('/SPrograma', {
						accion: 'borrarPrograma',
						id: mi.programa.id,
						t:moment().unix()
					}).success(function(response){
						if(response.success){
							$utilidades.mensaje('success','Programa borrado con éxito');
							mi.programa = null;
							mi.obtenerTotalProgramas();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el Programa');
					});
				}
			}, function(){
				
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Programa que desea borrar');
	};

	mi.nuevo = function (){
		mi.programatipoid = "";
		mi.programatiponombre="";
		mi.esColapsado = true;
		mi.programa = {};
		mi.prestamo = {};
		mi.esNuevo = true;
		mi.camposdinamicos = {};
		mi.proyectos =[];
		mi.gridApi.selection.clearSelectedRows();
		mi.prestamo = [];
		mi.rowCollection = [];
		mi.displayedCollection = [];
		mi.esNuevoDocumento = true;
		$scope.active = 0;
	};

	mi.editar = function() {
		if(mi.programa!=null && mi.programa.id!=null){
			mi.programatipoid = mi.programa.programatipoid;
			mi.programatiponombre=mi.programa.programatipo;
			mi.esColapsado = true;
			mi.esNuevo = false;
			mi.prestamo = {};
			
			
			
			var parametros = {
					accion: 'getProgramaPropiedadPorTipo',
					idPrograma: mi.programa!=''? mi.programa.id:0,
				    idProgramaTipo: mi.programatipoid,
				    t:moment().unix()
			}
			$http.post('/SProgramaPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.programapropiedades
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
			
			if (mi.programa.prestamo!=null && mi.programa.prestamo!= undefined && mi.programa.prestamo!= ''){
				mi.prestamo = JSON.parse( JSON.stringify( mi.programa.prestamo ) ); 
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
				mi.unidadejecutoraid=mi.prestamo.unidadejecutoraid;
				mi.unidadejecutoranombre=mi.prestamo.unidadejecutora;
				mi.entidad = mi.prestamo.entidadentidad;
				mi.entidadnombre = mi.prestamo.entidadnombre;
				mi.ejercicio = mi.prestamo.ejercicio;
			}
			
			
			
			parametros = {
					accion: 'getPrestamo',
					objetoId: mi.programa.id,
				    objetoTipo : 6,
				    t:moment().unix()
			}
			
			parametros = {
					accion: 'obtenerProyectosPorPrograma',
					idPrograma: mi.programa!=''? mi.programa.id:0,
				    t:moment().unix()
			}
			
			$http.post('/SProyecto', parametros).then(function(response){
				mi.proyectos = response.data.proyectos;
				
			});
			$scope.active = 0;
			mi.getDocumentosAdjuntos(6, mi.programa.id);
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Programa que desea editar');
	}

	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}

	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'programa', estado: JSON.stringify(estado) };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}

	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}

	mi.reiniciarVista=function(){
		if($location.path()=='/programa/rv')
			$route.reload();
		else
			$location.path('/programa/rv');
	}

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
	
	mi.buscarCooperante = function(prestamo) {
		var resultado = mi.llamarModalBusqueda('Cooperante','/SCooperante', {
			accion : 'numeroCooperantes', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getCooperantesPagina',
				pagina : pagina,
				numerocooperantes : elementosPorPagina
				, t:moment().unix()
			};
		},'id','nombre',false,null);

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
	
	mi.buscarUnidadEjecutoraPrestamo = function() {	
		var resultado = mi.llamarModalBusqueda('Unidad Ejecutora','/SUnidadEjecutora', {
			accion : 'totalElementos' , t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
				, t:moment().unix()
			};
		},'unidadEjecutora','nombreUnidadEjecutora',false,null);

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.unidadEjecutoraNombre = itemSeleccionado.nombreUnidadEjecutora;
			mi.prestamo.unidadEjecutora = itemSeleccionado.unidadEjecutora;
		});
	};

	mi.filtrar = function(evt){
		if(evt.keyCode==13){
			mi.obtenerTotalProgramas();
			mi.gridApi.selection.clearSelectedRows();
			mi.programa = null;
		}
	};

	mi.obtenerTotalProgramas = function(){
		$http.post('/SPrograma', { accion: 'numeroProgramas',t:moment().unix(),
			filtro_nombre: mi.filtros['nombre'],
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  } ).then(
				function(response) {
					mi.totalProgramas = response.data.totalprogramas;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};

	mi.irAProyectos=function(programaid){
		if(mi.programa!=null){
			$location.path('/proyecto/'+ programaid );
		}
	};
	
	mi.eliminarProyecto = function(row){
		var index = mi.proyectos.indexOf(row);
        if (index !== -1) {
            mi.proyectos.splice(index, 1);
        }
	}

	mi.llamarModalBusqueda = function(titulo,servlet, accionServlet, datosCarga,columnaId,columnaNombre, showfilters,entidad) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'buscarPorPrograma.jsp',
			controller : 'buscarPorPrograma',
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

	mi.buscarProgramaTipo = function() {
		var resultado = mi.llamarModalBusqueda('Tipo programa','/SProgramaTipo', {
			accion : 'numeroProgramaTipos', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getProgramaTipoPagina',
				pagina : pagina,
				numeroprogramatipo : elementosPorPagina
				, t:moment().unix()
			};
		},'id','nombre',false,null);

		resultado.then(function(itemSeleccionado) {
			mi.programatipoid= itemSeleccionado.id;
			mi.programatiponombre = itemSeleccionado.nombre;

			var parametros = {
					accion: 'getProgramaPropiedadPorTipo',
					idPrograma: mi.programa!=''? mi.programa.id:0,
					idProgramaTipo: itemSeleccionado.id,
					t:moment().unix()
			}

			$http.post('/SProgramaPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.programapropiedades;
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
	
	
	mi.buscarProyecto = function() {
		var idsproyectos = "";
		var proyectoTemp;
		for (i = 0, len =mi.proyectos.length;  i < len; i++) {
    		if (i == 0){
    			idsproyectos = idsproyectos.concat("",mi.proyectos[i].id);
    		}else{
    			idsproyectos = idsproyectos.concat(",",mi.proyectos[i].id);
    		}
    	}
	    
	    
		var resultado = mi.llamarModalBusqueda('Proyecto','/SProyecto', {
			accion : 'numeroProyectosDisponibles',
			idsproyectos:idsproyectos	
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getProyectoPaginaDisponibles',
				pagina : pagina,
				numeroproyecto : elementosPorPagina,
				idsproyectos:idsproyectos
			};
		},'id','nombre',false,null);

		resultado.then(function(itemSeleccionado) {
			mi.proyectos.push(itemSeleccionado);
		});
	};
	
	mi.buscarUnidadEjecutora = function() {
		var resultado = mi.llamarModalBusqueda('Unidades Ejecutoras','/SUnidadEjecutora', {
			accion : 'totalElementos',
			ejercicio: mi.ejercicio,
			entidad: mi.entidad
			, t:moment().unix()
		}, function(pagina, elementosPorPagina,ejercicio,entidad) {
			return {
				accion : 'cargar',
				pagina : pagina,
				ejercicio: ejercicio,
				entidad: entidad,
				registros : elementosPorPagina
				, t:moment().unix()
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
	
	mi.buscarAutorizacionTipo = function() {
		
		var resultado = mi.llamarModalBusqueda('Tipo Autorización','/SAutorizacionTipo', {
			accion : 'numeroAutorizacionTipo'	 , t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getAutorizacionTipoPagin',
				pagina : pagina,
				numeroautorizaciontipo : elementosPorPagina
				, t:moment().unix()
			};
		},'id','nombre',false,null);

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoAutorizacionNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoAutorizacionId = itemSeleccionado.id;
		});
	};
	
	mi.buscarInteresTipo = function() {
		
		var resultado = mi.llamarModalBusqueda('Tipo Interes','/SInteresTipo', {
			accion : 'numeroInteresTipo' , t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getAutorizacionTipoPagin',
				pagina : pagina,
				numerointerestipo : elementosPorPagina
				, t:moment().unix()
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoInteresNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoInteresId = itemSeleccionado.id;
		});
	};
	
	
	
	mi.buscarTipoMoneda = function() {
		
		var resultado = mi.llamarModalBusqueda('Tipo Moneda','/STipoMoneda', {
			accion : 'numeroTipoMonedas' , t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getTipoMonedaPagina',
				pagina : pagina,
				numerotipomoneda : elementosPorPagina
				, t:moment().unix()
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.tipoMonedaNombre = itemSeleccionado.nombre;
			mi.prestamo.tipoMonedaId = itemSeleccionado.id;
		});
	};
	
	mi.buscarEstadoEjecucion = function() {
		
		var resultado = mi.llamarModalBusqueda('Estado de ejecución','/SEjecucionEstado', {
			accion : 'numeroEjecucionEstado', t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getEjecucionEstadoPagina',
				pagina : pagina,
				numeroejecucionestado : elementosPorPagina
				, t:moment().unix()
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.prestamo.ejecucionEstadoNombre = itemSeleccionado.nombre;
			mi.prestamo.ejecucionEstadoId = itemSeleccionado.id;
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
				
				mi.cooperanteid = mi.prestamo.cooperanteid;
				
				mi.getPorcentajes();
			}else{
				$utilidades.mensaje('warning', 'No se encontraron datos con los parámetros ingresados');
			}
			
		});
		
	};
	 
		
		mi.buscarCodigoPresupuestario = function() {	
			var resultado = mi.llamarModalBusqueda('Código Presupuestario','/SDataSigade', {
				accion : 'totalElementos'	, t:moment().unix()
			}, function(pagina, elementosPorPagina) {
				return {
					accion : 'getcodigos',
					pagina : pagina,
					registros : elementosPorPagina
					, t:moment().unix()
				};
			},'codigopresupuestario','numeroprestamo', false, null);

			resultado.then(function(itemSeleccionado) {
				if (itemSeleccionado!=null && itemSeleccionado != undefined){
					mi.prestamo.codigoPresupuestario = Number(itemSeleccionado.codigopresupuestario);
					mi.cargaSigade();
				}
				
			});
		};
	

	
} ]);

app.controller('buscarPorPrograma', [ '$uibModalInstance',
	'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$titulo', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre','$showfilters','$entidad',buscarPorPrograma ]);

function buscarPorPrograma($uibModalInstance, $rootScope,$scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log, $titulo,$servlet,$accionServlet,$datosCarga,$columnaId
		,$columnaNombre,$showfilters,$entidad) {
	
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
	mi.entidades = [];
	mi.titulo = $titulo;
	
	if(mi.showfilters){
		var current_year = moment().year();
		mi.entidad = $entidad;
		mi.ejercicio = $entidad.ejercicio;
		for(var i=current_year-$rootScope.catalogo_entidades_anos; i<=current_year; i++)
			mi.ejercicios.push(i);
		mi.ejercicio = (mi.ejercicio == "") ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio}).success(function(response) {
			mi.entidades = response.entidades;
			if(mi.entidades.length>0){
				mi.entidad = (mi.entidad===undefined) ? mi.entidades[0] : mi.entidad;
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
			$http.post('/SUnidadEjecutora', {accion:"totalElementos", ejercicio: mi.entidad.ejercicio,entidad: mi.entidad.entidad}).success(function(response) {
				for ( var key in response) {
					mi.totalElementos = response[key];
				}
				mi.cargarData(1,mi.ejercicio,mi.entidad.entidad);
			});
		}
	};	
};

app.directive('showFocus', function($timeout) {
    return function(scope, element, attrs) {
      scope.$watch(attrs.showFocus,
        function (newValue) {
          $timeout(function() {
              element[0].focus();             
          });
        },true);
    };   
  });