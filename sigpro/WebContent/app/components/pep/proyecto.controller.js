var app = angular.module('proyectoController', [ 'ngTouch','smart-table',  'ui.bootstrap.contextMenu']);

app.controller('proyectoController',['$rootScope','$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q','$filter', 'dialogoConfirmacion', 'historia',
	function($rootScope,$scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,$filter, $dialogoConfirmacion, $historia) {

	var mi = this;
	i18nService.setCurrentLang('es');

	mi.esTreeview = $rootScope.treeview;
	mi.botones = true;
	
	if(!mi.esTreeview)
		$window.document.title = $utilidades.sistema_nombre+' - '+$rootScope.etiquetas.proyecto+'s';
		
	mi.rowCollection = [];
	mi.prestamoid = $routeParams.prestamo_id;
	mi.proyecto = null;
	mi.esNuevo = false;
	mi.esNuevoDocumento = true;
	mi.campos = {};
	mi.esColapsado = false;
	mi.mostrarcargando= (mi.esTreeview) ? false : true;
	mi.paginaActual = 1;
	mi.cooperantes = [];
	mi.proyectotipos = [];
	mi.poryectotipoid = "";
	mi.proyectotiponombre="";
	mi.unidadejecutoraid="";
	mi.entidad="";
	mi.ejercicio="";
	mi.unidadejecutoranombre="";
	mi.entidadnombre="";
	mi.camposdinamicos = {};
	mi.formatofecha = 'dd/MM/yyyy';
	mi.altformatofecha = ['d!/M!/yyyy'];
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.totalProyectos = 0;
	mi.mostrarPrestamo = true;
	mi.ordenTab=0;
	mi.columnaOrdenada=null;
	mi.ordenDireccion = null;
	mi.filtros = [];
	mi.orden = null;
	mi.prestamo = [];
	mi.fechaInicioTemp = '';
	mi.fechaFinalTemp = '';
	mi.fechaInicioRealTemp = '';
	mi.fechaFinalRealTemp = '';
	
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
	
	mi.prestamoNombre = "";
	mi.objetoTipoNombre = "";
	mi.m_organismosEjecutores = [];
	mi.m_componentes = [];
	
	mi.congelado = 0;
	
	$http.post('/SPrestamo', { accion: 'obtenerPrestamoPorId', id: mi.prestamoid, t: (new Date()).getTime() }).success(
		function(response) {
			if(response.success){
				mi.prestamoNombre = response.nombre;
				mi.objetoTipoNombre = "Préstamo";	
				mi.prestamoid=response.id;
				mi.codigoPresupuestario = response.codigoPresupuestario;
				mi.fechaCierreActualUe = response.fechaCierreActualUe;				
			}
	});
	
	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};

	mi.calcularCostoFecha = function(proyectoId){
		$http.post('SProyecto',{
			accion: 'calcularCostoFecha',
			proyectoId: proyectoId
		}).then(function(response) {
			if(response.data.success)
				$utilidades.mensaje('success','Costos y fechas calculados exitosamente');
			else
				$utilidades.mensaje('danger','Error al calcular costos y fechas');
		});
	}
	
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
			{ name : 'proyectotipo',    displayName : 'Caracterización '+$rootScope.etiquetas.proyecto ,cellClass: 'grid-align-left', enableFiltering: false, enableSorting: false },
			{ name : 'unidadejecutora',    displayName : 'Unidad ejecutora' ,cellClass: 'grid-align-left', enableFiltering: false , enableSorting: false },
			{ name: 'usuarioCreo', width: 120, displayName: 'Usuario Creación',cellClass: 'grid-align-left',
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
				mi.congelado = row.entity.congelado;
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
		
		$http.post('/SProyecto', { accion: 'getProyectoPagina', pagina: pagina, prestamoId: mi.prestamoid,
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
	
	Date.prototype.today = function () { 
	    return ((this.getDate() < 10)?"0":"") + this.getDate() +"/"+(((this.getMonth()+1) < 10)?"0":"") + (this.getMonth()+1) +"/"+ this.getFullYear();
	}
	
	mi.guardar = function(esvalido){
		mi.botones=false;
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor_f = mi.camposdinamicos[campos].valor!=null ? moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY') : "";
			}
		}

		var listaImpactos = "";
		for (impacto in mi.impactos){
			listaImpactos = listaImpactos + (listaImpactos.length>0 ? "~" : "")+
			mi.impactos[impacto].entidadId + "," + mi.impactos[impacto].impacto; 
		}
		var miembros = "";
		for (m in mi.miembros){
			miembros = miembros + (miembros.length > 0 ? "," : "") + mi.miembros[m].id; 
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
				ejercicio: mi.ejercicio,
				entidadid: mi.entidad,
				unidadejecutoraid: mi.unidadejecutoraid,
				programa: mi.proyecto.programa,
				subprograma: mi.proyecto.subprograma,
				proyecto_: mi.proyecto.proyecto,
				obra:mi.proyecto.obra,
				actividad: mi.proyecto.actividad,
				fuente: mi.proyecto.fuente,
				esnuevo: mi.esNuevo,
				longitud: mi.proyecto.longitud,
				latitud : mi.proyecto.latitud,
				directorProyecto: mi.directorProyectoId,
				impactos : listaImpactos,
				miembros: miembros,
				objetoivoEspecifico: mi.proyecto.objetivoEspecifico,
				visionGeneral : mi.proyecto.visionGeneral,
				datadinamica : JSON.stringify(mi.camposdinamicos),
				ejecucionFisicaReal: mi.proyecto.ejecucionFisicaReal,
				proyectoClase: $rootScope.etiquetas.id,
				projectCargado: mi.proyecto.projectCargado,
				prestamoId: mi.prestamoid,
				observaciones : mi.proyecto.observaciones,
				porcentajeAvance: mi.proyecto.porcentajeAvance,
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
						
						if(mi.esTreeview){
							mi.t_cambiarNombreNodo();
						}
						else
							mi.obtenerTotalProyectos();
						if(mi.child_desembolso!=null || mi.child_riesgos!=null){
							if(mi.child_desembolso)
								ret = mi.child_desembolso.guardar($rootScope.etiquetas.proyecto+' '+(mi.esNuevo ? 'creado' : 'guardado')+' con Éxito',
										'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el '+$rootScope.etiquetas.proyecto,
										mi.child_riesgos!=null ? mi.child_riesgos.guardar :  null);
							else if(mi.child_riesgos)
								ret = mi.child_riesgos.guardar($rootScope.etiquetas.proyecto+' '+(mi.esNuevo ? 'creado' : 'guardado')+' con Éxito',
										'Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el '+$rootScope.etiquetas.proyecto);
						}
						else{
							$utilidades.mensaje('success',$rootScope.etiquetas.proyecto+' '+(mi.esNuevo ? 'creado' : 'guardado')+' con Éxito');
							mi.botones=true;
						}
						
						mi.esNuevo = false;
					}else
						$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el '+$rootScope.etiquetas.proyecto);
			});

			mi.esNuevoDocumento = false;
		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
	 }

	mi.borrar = function(ev) {
		if(mi.proyecto !=null && mi.proyecto.id!=null){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el '+$rootScope.etiquetas.proyecto+' "'+mi.proyecto.nombre+'"?'
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
							$utilidades.mensaje('success',$rootScope.etiquetas.proyecto+' borrado con éxito');
							mi.proyecto = null;
							mi.obtenerTotalProyectos();
						}
						else
							$utilidades.mensaje('danger','Error al borrar el '+$rootScope.etiquetas.proyecto);
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
		mi.proyectotiponombre="";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
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
			mi.entidadnombre = mi.proyecto.entidadnombre;
			mi.ejercicio = mi.proyecto.ejercicio;
			mi.entidad = mi.proyecto.entidadentidad;
			mi.directorProyectoNombre = mi.proyecto.directorProyectoNmbre;
			mi.directorProyectoId = mi.proyecto.directorProyectoId;
			mi.esColapsado = true;
			mi.esNuevo = false;
			mi.coordenadas = (mi.proyecto.latitud !=null ?  mi.proyecto.latitud : '') +
			(mi.proyecto.latitud!=null ? ', ' : '') + (mi.proyecto.longitud!=null ? mi.proyecto.longitud : '');
			mi.impactos =[];
			mi.miembros = [];
			if (mi.fechaInicioTemp == null || mi.fechaInicioTemp == ''){
				mi.fechaInicioTemp = mi.proyecto.fechaInicio;
				mi.fechaFinalTemp = mi.proyecto.fechaFin;
			}
			
			if(mi.fechaInicioRealTemp == null || mi.fechaInicioRealTemp==''){
				mi.fechaInicioRealTemp = mi.proyecto.fechaInicioReal;
				mi.fechaFinalRealTemp = mi.proyecto.fechaFinReal;
				
				if((mi.fechaInicioRealTemp != null && mi.fechaInicioRealTemp != '') && (mi.fechaFinalRealTemp != null && mi.fechaFinalRealTemp != '')){
					mi.duracionReal = moment(mi.fechaFinalRealTemp,'DD/MM/YYYY').toDate() - moment(mi.fechaInicioRealTemp,'DD/MM/YYYY').toDate();
					mi.duracionReal = Number(mi.duracionReal / (1000*60*60*24))+1;
				}
			}else{
				if((mi.fechaInicioRealTemp != null && mi.fechaInicioRealTemp != '') && (mi.fechaFinalRealTemp != null && mi.fechaFinalRealTemp != '')){
					mi.duracionReal = moment(mi.fechaFinalRealTemp,'DD/MM/YYYY').toDate() - moment(mi.fechaInicioRealTemp,'DD/MM/YYYY').toDate();
					mi.duracionReal = Number(mi.duracionReal / (1000*60*60*24))+1;
				}
			}
			
			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.proyecto.id:0,
				    idProyectoTipo: mi.poryectotipoid,
				    t:moment().unix()
			}
			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades;
				mi.desembolsos = undefined;
				mi.riesgos = undefined;
				mi.active = 0;
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
					accion: 'obtenerProyectosPorPrograma',
					idPrograma: mi.proyecto!=''? mi.proyecto.id:0,
				    t:moment().unix()
			}
			
			$http.post('/SProyecto', parametros).then(function(response){
				mi.proyectos = response.data.proyectos;
				
			});
			
			parametros = {
					accion: 'getMiembrosPorProyect',
					proyectoId: mi.proyecto!=''? mi.proyecto.id:0,
				    t:moment().unix()
			}
			$http.post('/SProyectoMiembro', parametros).then(function(response){
				mi.miembros  = response.data.miembros;
				
			});
			
			parametros = {
					accion: 'getImpactosPorProyect',
					proyectoId: mi.proyecto!=''? mi.proyecto.id:0,
				    t:moment().unix()
			}
			$http.post('/SProyectoImpacto', parametros).then(function(response){
				mi.impactos  = response.data.impactos;
				
			});
			
			parametros = {
					accion: 'obtenerMatriz',
					proyectoId: mi.proyecto!=''? mi.proyecto.id:0,
				    t:moment().unix()
			}
			
			$http.post('/SProyecto', parametros).then(function(response){
				mi.m_organismosEjecutores = response.data.unidadesEjecutoras;
				mi.m_componentes = response.data.componentes;
				mi.m_existenDatos = response.data.existenDatos;
				
			});
			
			if(mi.prestamoid != null){
				$http.post('/SProyecto', { accion: 'obtenerMontoTechos', id: mi.proyecto.id }).then(
					function(response){
					if(response.data.success){
						mi.montoTechos = response.data.techoPep;	
						
						if(mi.proyecto.costo > mi.montoTechos)
							mi.sobrepaso = true;
						else
							mi.sobrepaso = false;
						
						$http.post('/SDataSigade', { accion: 'getMontoDesembolsosUE', codPrep: mi.codigoPresupuestario, ejercicio: mi.proyecto.ejercicio, entidad: mi.proyecto.entidadentidad, ue: mi.proyecto.unidadejecutoraid}).then(
							function(response){
								if(response.data.success){
									mi.montoDesembolsadoUE = response.data.montoDesembolsadoUE;
									
									mi.montoPorDesembolsar = mi.montoTechos - mi.montoDesembolsadoUE;
									
									$http.post('/SDataSigade', { accion: 'getMontoDesembolsosUEALaFecha', codPrep: mi.codigoPresupuestario, entidad: mi.proyecto.entidadentidad, ue: mi.proyecto.unidadejecutoraid}).then(
										function(response){
											if(response.data.success){
												mi.desembolsoAFechaUsd = response.data.montoDesembolsadoUEALaFecha;
											}
										}
											
									)
								}
							}
						);
					}
				})
			}
			mi.getDocumentosAdjuntos( mi.proyecto.id,0);
			$scope.active = 0;
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el '+$rootScope.etiquetas.proyecto+' que desea editar');
	}

	mi.adjuntarDocumentos = function(){
		$documentoAdjunto.getModalDocumento($scope, mi.proyecto.id,0)
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
				mi.getDocumentosAdjuntos( mi.proyecto.id,0);
			}
		});
	};

	mi.irATabla = function() {
		mi.esColapsado=false;
		mi.esNuevo = false;
	}
	mi.cambioOrden=function(){
		mi.ordenTab++;
	}
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'proyceto', estado: JSON.stringify(estado) };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}
	
	mi.ocultarLabel=function(input){
		if(mi[input]=="money-label-hidden"){
			mi[input]="money-label";
			
		}else{
			mi[input]="money-label-hidden";
			var data_input = $window.document.getElementById(input);
		    data_input.focus();
		}
		
	};
	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}

	mi.reiniciarVista=function(){
		if($location.path()=='/pep/'+ mi.prestamoid + '/rv')
			$route.reload();
		else
			$location.path('/pep/'+ mi.prestamoid + '/rv');
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
			filtro_nombre: mi.filtros['nombre'], prestamoId: mi.prestamoid,
			filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  } ).then(
				function(response) {
					mi.totalProyectos = response.data.totalproyectos;
					mi.paginaActual = 1;
					mi.cargarTabla(mi.paginaActual);
		});
	};

	mi.irAComponentes=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/componente/'+ proyectoid );
		}
	};

	mi.irAHitos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/hito/'+ proyectoid );
		}
	};

	mi.irAActividades=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/actividad/'+ proyectoid +'/0' );
		}
	};

	mi.irAGantt=function(proyectoid){
		if(mi.proyecto!=null){
			if(mi.esTreeview)
				$window.location='/main.jsp#!/gantt/'+ proyectoid + '/0';
			else
				$location.path('/gantt/'+ proyectoid + '/0' );
		}
	};
	mi.irAMapa=function(proyectoid){
		if(mi.proyecto!=null){
			if(mi.esTreeview)
				$window.location='/main.jsp#!/mapa/'+ proyectoid ;
			else
				$location.path('/mapa/'+proyectoid);
		}
	};
	mi.irAKanban=function(proyectoid){
		if(mi.proyecto!=null){
			if(mi.esTreeview)
				$window.location='/main.jsp#!/porcentajeactividades/'+ proyectoid;
			else
				$location.path('/porcentajeactividades/'+ proyectoid );
		}
	};

	mi.irAAgenda=function(proyectoid){
		if(mi.proyecto!=null){
			if(mi.esTreeview)
				$window.location='/main.jsp#!/agenda/'+ proyectoid;
			else
				$location.path('/agenda/'+ proyectoid);
		}
	};
	
	mi.irAMatrizRiesgos=function(proyectoid){
		if(mi.proyecto!=null){
			if(mi.esTreeview)
				$window.location='/main.jsp#!/matrizriesgo/'+ proyectoid;
			else
				$location.path('/matrizriesgo/'+ proyectoid );
		}
	};
	mi.irAMiembrosUnidadEjecutora = function(proyectoid){
		if(mi.proyecto!=null){
			if(mi.esTreeview)
				$window.location='/main.jsp#!/miembrosunidadejecutora/'+ proyectoid;
			else
				$location.path('/miembrosunidadejecutora/'+ proyectoid);
		}
	};

	mi.llamarModalBusqueda = function(titulo,servlet, accionServlet, datosCarga,columnaId,columnaNombre, showfilters,entidad) {
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


	mi.buscarProyectoTipo = function() {
		var resultado = mi.llamarModalBusqueda('Tipos de '+$rootScope.etiquetas.proyecto,'/SProyectoTipo', {
			accion : 'numeroProyectoTipos',t:moment().unix()
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getProyectoTipoPagina',
				pagina : pagina,
				numeroproyectotipo : elementosPorPagina,
				t:moment().unix()
			};
		},'id','nombre',false, null);

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
	
	mi.llamarModalArchivo = function(proyectoId, completadoSigade, prestamoId) {
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
			resolve : {
				$proyectoId : function() {
					return proyectoId;
				},
				$completadoSigade : function() {
					return completadoSigade;
				},
				$prestamoId : function() {
					return prestamoId;
				}
				
				
			}
		});

		modalInstance.result.then(function(respuesta) {
			resultado.resolve(respuesta);
		});
		return resultado.promise;
	};
	
	mi.cargarArchivo = function() {
		var resultado = mi.llamarModalArchivo( 0, 1,mi.prestamoid);

		resultado.then(function(resultado) {
			mi.mostrarcargando=false;
			if (resultado.data.success){
				mi.obtenerTotalProyectos();
				$utilidades.mensaje('success',$rootScope.etiquetas.proyecto+' creado con éxito');
			}else{
				$utilidades.mensaje('danger','Error al crear el '+$rootScope.etiquetas.proyecto);
			}
			
		});
	};
	
	mi.completarConArchivo = function() {
		var resultado = mi.llamarModalArchivo(mi.proyecto!=null && mi.proyecto != undefined ? mi.proyecto.id : 0, 
				mi.proyecto!=null && mi.proyecto != undefined ? mi.proyecto.projectCargado: 1,0);

		resultado.then(function(resultado) {
			mi.mostrarcargando=false;
			if (resultado.data.success){
				mi.proyecto.projectCargado = 1;
				mi.obtenerTotalProyectos();
				$utilidades.mensaje('success',$rootScope.etiquetas.proyecto+' completado con éxito');
			}else{
				$utilidades.mensaje('danger','Error al crear el '+$rootScope.etiquetas.proyecto);
			}
			if(mi.esTreeview){
				$rootScope.$emit("recargarArbol",{});
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
		    	mi.proyecto.latitud= coordenadas.latitude;
				mi.proyecto.longitud = coordenadas.longitude;
	    	}else{
	    		mi.coordenadas = "";
		    	mi.proyecto.latitud= null
				mi.proyecto.longitud = null;
	    	}
	    }, function() {
	    	
		});
	  };
	  
	  mi.generarReporte = function () {
		  var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'generarReporte.jsp',
				controller : 'modalGenerarReporte',
				controllerAs : 'modalrc',
				backdrop : 'static',
				size : 'md',
				resolve: {
			        proyectoid: function(){
			        	return mi.proyecto.id;
			        }
			     }
			});
		  
		  
		  
		  modalInstance.result.then(function(resultado) {
				if (resultado != undefined){
					mi.exportarJasper(resultado.fechaCorte, resultado.lineaBase);
				}else{
					$utilidades.mensaje('danger', 'Error al generar el reporte');
				}
			}, function() {
			});
	  };
	  
	  mi.congelar = function (tipoLineaBase) {
		  var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'congelar.jsp',
				controller : 'modalCongelar',
				controllerAs : 'modalcc',
				backdrop : 'static',
				size : 'md',
				resolve: {
			        proyectoid: function(){
			        	return mi.proyecto.id;
			        },
			        tipoLineaBase: function(){
			        	return tipoLineaBase;
			        }
			     }
			});
		  
		  modalInstance.result.then(function(resultado) {
				if (resultado != undefined && resultado  && resultado.success){
					mi.proyecto.congelado = 1;
					mi.congelado = 1;
					$utilidades.mensaje('success', 'Se generaron los datos correctamente');
				}else{
					$utilidades.mensaje('danger', 'Error al guardar' + 
							(resultado != undefined && resultado.mensaje != undefined ? ' : ' + resultado.mensaje : ''));
				}
			}, function() {
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
		
		if(mi.esTreeview){
			  $http.post('/SProyecto', { accion : 'getProyectoPorId', id: $routeParams.id, t: (new Date()).getTime() }).then(function(response) {
						if (response.data.success) {
							mi.proyecto = response.data.proyecto;
							mi.fechaInicioTemp = mi.proyecto.fechaInicio;
							mi.fechaFinalTemp = mi.proyecto.fechaFin;
							mi.fechaInicioRealTemp = mi.proyecto.fechaInicioReal;
							mi.fechaFinalRealTemp = mi.proyecto.fechaFinReal;
							mi.congelado = mi.proyecto.congelado;
							if(mi.proyecto.fechaInicio != null && mi.proyecto.fechaInicio != "")
								mi.proyecto.fechaInicio = moment(mi.proyecto.fechaInicio, 'DD/MM/YYYY').toDate();
							if(mi.proyecto.fechaFin != null && mi.proyecto.fechaFin != "")
								mi.proyecto.fechaFin = moment(mi.proyecto.fechaFin, 'DD/MM/YYYY').toDate();
							if(mi.proyecto.fechaInicioReal != null && mi.proyecto.fechaInicioReal != "")
								mi.proyecto.fechaInicioReal = moment(mi.proyecto.fechaInicioReal, 'DD/MM/YYYY').toDate();
							if(mi.proyecto.fechaFinReal != null && mi.proyecto.fechaFinReal != "")
								mi.proyecto.fechaFinReal = moment(mi.proyecto.fechaFinReal, 'DD/MM/YYYY').toDate();
							
							if((mi.proyecto.fechaInicioReal !=null && mi.proyecto.fechaInicioReal != "") && (mi.proyecto.fechaFinReal !=null && mi.proyecto.fechaFinReal != "")){
								mi.duracionReal = mi.proyecto.fechaFinReal - mi.proyecto.fechaInicioReal;
								mi.duracionReal = Number(mi.duracionReal / (1000*60*60*24))+1;
							}
							mi.editar();
							
						}
					});
		  }
		
		mi.t_borrar = function(ev) {
			if (mi.proyecto!=null && mi.proyecto.id!=null) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el '+$rootScope.etiquetas.proyecto+' "' + mi.proyecto.nombre + '"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var datos = {
								accion : 'borrarProyecto',
								id : mi.proyecto.id,
								t: (new Date()).getTime()
							};
							$http.post('/SProyecto', datos).success(
									function(response) {
										if (response.success) {
											
											$utilidades.mensaje('success',$rootScope.etiquetas.proyecto+' borrado con éxito');
											mi.proyecto = null;		
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
			$rootScope.$emit("cambiarNombreNodo",mi.proyecto.nombre);
			$rootScope.$emit("recargarArbol",mi.proyecto.id);
		}
		
		mi.exportarJasper = function(fechaCorte, lineaBase){
			var anchor = angular.element('<a/>');
			  anchor.attr({
		         href: '/app/components/reportes/jasper/reporte.jsp?reporte=0&proyecto='+mi.proyecto.id+'&fecha='+fechaCorte.getTime()+(lineaBase!=null?'&lb='+lineaBase:'')
			  })[0].click();
		}
		
		mi.exportar=function(){
			var formatData = new FormData();
			
			$http.post('/SGantt', { accion: 'exportar', proyecto_id:mi.proyecto.id,t:moment().unix()
			  }).then(
					 function successCallback(response) {
							var anchor = angular.element('<a/>');
						    anchor.attr({
						         href: 'data:application/xml,' + response.data,
						         target: '_blank',
						         download: mi.proyecto.nombre + '.xml'
						     })[0].click();
						  }.bind(this), function errorCallback(response){
						 		
						 	}
			);
		};
		
		mi.verHistoria = function(){
			$historia.getHistoria($scope, 'Pep', '/SProyecto',mi.proyecto.id, 0, false, true, true, false)
			.result.then(function(data) {
				if (data != ""){
					
				}
			}, function(){
				
			});
		}
} ]);

app.controller('buscarPorProyecto', [ '$uibModalInstance',
	'$rootScope','$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$titulo', '$servlet', '$accionServlet', '$datosCarga',
	'$columnaId','$columnaNombre','$showfilters','$entidad',buscarPorProyecto ]);

function buscarPorProyecto($uibModalInstance, $rootScope,$scope, $http, $interval,
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
	mi.entidades = [];
	mi.titulo = $titulo;
	
	if(mi.showfilters){
		var current_year = moment().year();
		mi.entidad = $entidad;
		mi.ejercicio = $entidad.ejercicio;
		for(var i=current_year-$rootScope.catalogo_entidades_anos; i<=current_year; i++)
			mi.ejercicios.push(i);
		mi.ejercicio = (mi.ejercicio == "" || mi.ejercicio == null) ? current_year : mi.ejercicio;
		$http.post('SEntidad', { accion: 'entidadesporejercicio', ejercicio: mi.ejercicio,t:moment().unix()}).success(function(response) {
			mi.entidades = response.entidades;
			if(mi.entidades.length>0){
				mi.entidad = (mi.entidad.entidad == null || mi.entidad===undefined || mi.entidad.entidad== "") ? mi.entidades[0] : mi.entidad;
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
	  
	  $scope.cancel = function () {
		  $uibModalInstance.close(undefined);
		  
	  };
}]);

app.controller('modalGenerarReporte', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q', 'proyectoid' ,modalGenerarReporte ]);

function modalGenerarReporte($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, proyectoid) {

	var mi = this;
	mi.formatofecha = 'dd/MM/yyyy';
	mi.altformatofecha = ['d!/M!/yyyy'];
	mi.observacionesAbierto = false;
		
	$http.post('/SProyecto', { accion: 'getPepDetalle', id: proyectoid, t: (new Date()).getTime() }).success(
			function(response) {
				mi.fechaCorte = new Date();
				if(response.success){
					if(response.detalle){
						mi.observaciones = response.detalle.observaciones;
						mi.alertivos = response.detalle.alertivos;
						mi.elaborado = response.detalle.elaborado;
						mi.aprobado = response.detalle.aprobado;
						mi.autoridad = response.detalle.autoridad;
					}
				}
		});
	
	$http.post('/SProyecto',{accion: 'getLineasBase', proyectoId: proyectoid}).success(
		function(response) {
			mi.lineasBase = [];
			if (response.success){
				mi.lineasBase = response.lineasBase;
			}
	});	
		
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	
	mi.cambioLineaBase=function(selected){
		if(selected!== undefined){
			mi.lineaBaseNombre = selected.originalObject.nombre;
			mi.lineaBaseId = selected.originalObject.id;
		}
		else{
			mi.lineaBaseNombre="";
			mi.lineaBaseId=null;
		}
	};
		
	mi.fi_opciones = {
			formatYear : 'yy',
			maxDate : new Date(2050, 12, 31),
			minDate : new Date(1990, 1, 1),
			startingDay : 1
	};
	
	mi.abrirPopupFecha = function(index) {
		mi.fi_abierto = true;
	};
	
	mi.ok = function() {
		$http.post('/SProyecto', { 
			accion: 'guardarPepDetalle', 
			id: proyectoid, 
			observaciones: mi.observaciones,
			alertivos: mi.alertivos,
			elaborado: mi.elaborado,
			aprobado: mi.aprobado, 
			autoridad: mi.autoridad,
			t: (new Date()).getTime() }).success(
				function(response) {
					if(response.success){
						console.log(response.detalle);
					}
				});
		if(mi.lineaBaseNombre!=null && mi.lineaBaseNombre!=""){
			var resultado = {
					fechaCorte : mi.fechaCorte, 
					lineaBase : mi.lineaBaseId
			}
			$uibModalInstance.close(resultado);
		}else{
			$utilidades.mensaje('warning', 'Debe seleccionar una línea Base');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
};

app.controller('modalCongelar', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q', 'proyectoid','tipoLineaBase' ,modalCongelar ]);

function modalCongelar($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, proyectoid,tipoLineaBase) {

	var mi = this;
	mi.mostrarcargando=false;
	mi.lineasBase = [];
	mi.nuevaLineaBase = 1;
	mi.bloquerBoton = false;
	mi.tipoLineaBase = tipoLineaBase;
	mi.maxYear=0;
	mi.minYear =0;
	
	mi.mes = [{nombre:'Enero',id:1},{nombre:'Febrero',id:2},{nombre:'Marzo',id:3},
		{nombre:'Abril',id:4},{nombre:'Mayo',id:5},{nombre:'Junio',id:6},
		{nombre:'Julio',id:7},{nombre:'Agosto',id:8},{nombre:'Septiembre',id:9},
		{nombre:'Octubre',id:10},{nombre:'Noviembre',id:11},{nombre:'Diciembre',id:12}]
	
	mi.ok = function() {
		mi.mostrarcargando=true;
		mi.bloquearBoton = true;
		$http.post('/SProyecto', { 
			accion: 'congelar', 
			id: proyectoid, 
			lineaBaseId: mi.lineaBaseId,
			nuevo : mi.nuevaLineaBase,
			nombre: mi.nombre,
			tipoLineaBase:mi.tipoLineaBase,
			mes:mi.mesNombre,
			anio:mi.anio,
			t: (new Date()).getTime() }).success(
				function(response) {
					console.log(response.success);
					mi.mostrarcargando=true;
					mi.bloquearBoton = false;
					$uibModalInstance.close(response);
				});
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cambioLineaBase=function(selected){
		if(selected!== undefined){
			mi.lineaBaseNombre = selected.originalObject.nombre;
			mi.lineaBaseId = selected.originalObject.id;
		}
		else{
			mi.lineaBaseNombre="";
			mi.lineaBaseId=null;
		}
	};
	
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	
	
	$http.post('/SProyecto',{accion: 'getLineasBasePorTipo', proyectoId: proyectoid, tipoLineaBase:1}).success(
		function(response) {
			mi.lineasBase = [];
			if (response.success){
				mi.lineasBase = response.lineasBase;
				mi.maxYear = response.anioActual;
				mi.minYear = response.anioActual-1;
				mi.anio = mi.maxYear;
			}
	});	
	
	mi.selectNuevo = function(nuevo){
		mi.nombre = '';
		mi.lineaBaseNombre="";
		mi.lineaBaseId=null;
	}
	
	mi.cambioMes=function(selected){
		if(selected!== undefined){
			mi.mesNombre = selected.originalObject.nombre;
			mi.mesId = selected.originalObject.id;
		}
		else{
			mi.mesNombre="";
			mi.mesId=null;
		}
	};
	
	mi.blurLineaBase=function(){
		if(document.getElementById("lineaBase_value").defaultValue!=mi.lineaBaseNombre){
			$scope.$broadcast('angucomplete-alt:clearInput','lineaBase');
		}
	};
	

	
}

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
	'$timeout', '$log','$q','$proyectoId','$completadoSigade','$prestamoId', cargararchivoController ]);

function cargararchivoController($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log,$q,$proyectoId,$completadoSigade,$prestamoId) {

	var mi = this;
	mi.mostrar = true;
	mi.nombreArchivo="";
	mi.mostrarcargando=false;
	mi.multiproyecto = false;
	mi.bloquearBotones = false;
	mi.yaCompletadosigade = $completadoSigade == 1 || $completadoSigade == undefined;
	mi.proyectoId = $proyectoId;
	
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
			mi.mostrarcargando=true;
			mi.cargar();
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar un archivo');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.seleccionCompletarSigade = function(){
		if (mi.completarsigade)
			mi.multiproyecto = false;
	}
	
	mi.seleccionMultiProyecto = function (){
		if (mi.multiproyecto)
			mi.completarsigade = false;
	}
	
	mi.cargar=function(){
		mi.bloquearBotones = true;
		if (mi.archivos!=null && mi.arhivos != ''){
			mi.mostrarcargando=true;
			var formatData = new FormData();
			formatData.append("file",mi.archivos);  
			formatData.append("accion",'importar');
			formatData.append("multiproyecto",mi.multiproyecto ? 1 : 0);
			formatData.append("marcarCargado",mi.proyectoId > 0  ? 1 : 0);
			formatData.append("proyecto_id",$proyectoId);
			formatData.append("prestamoId", $prestamoId);
			formatData.append("t",moment().unix());
			
			$http.post('/SGantt',formatData, {
					headers: {'Content-Type': undefined},
					transformRequest: angular.identity
				 } ).then(
			
				function(response) {
					mi.mostrarcargando=false;
					mi.bloquearBotones = false;
					
					$uibModalInstance.close(response);
				}
			);
		}else{
			$utilidades.mensaje('danger','Debe seleccionar un archivo');
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
}


