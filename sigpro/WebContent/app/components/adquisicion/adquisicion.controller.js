app.controller('adquisicionController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
		var mi=this;
		
		mi.mostrarcargando=true;
		mi.adquisiciones = [];
		mi.adquisicion;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.camposdinamicos = {};
		mi.formatofecha = 'dd/MM/yyyy';
		
		mi.objetoTipo = 0;
		mi.objetoId = 0;
		
		mi.categorias=[];
		mi.tipos=[];
		
		mi.popup_fechas=[false,false,false,false,false,false,false,false,false];
		
		mi.parentController=null;
		
		if($scope.$parent.producto){
			$scope.$parent.producto.child_adquisicion = $scope.adquisicionc;
			mi.parentController = $scope.$parent.producto;
			mi.objetoTipo = 3;
			mi.objetoId = $scope.$parent.producto.producto.id;
		}
		if($scope.$parent.subproductoc){
			$scope.$parent.subproductoc.child_adquisicion = $scope.adquisicionc;
			mi.parentController = $scope.$parent.subproductoc;
			mi.objetoTipo = 4;
			mi.objetoId = $scope.$parent.subproductoc.subproducto.id;
		}
		if($scope.$parent.actividadc){
			$scope.$parent.actividadc.child_adquisicion = $scope.adquisicionc;
			mi.parentController = $scope.$parent.actividadc;
			mi.objetoTipo = 5;
			mi.objetoId = $scope.$parent.actividadc.actividad.id;
		}
		
		$http.post('/STipoAdquisicion', { accion: 'getTipoAdquisicionPorObjeto', objetoId: mi.objetoId, objetoTipo: mi.objetoTipo, t: (new Date()).getTime()}).success(
				function(response) {
					mi.tipos = response.tipoAdquisiciones;
		});
		
		$http.post('/SCategoriaAdquisicion', { accion: 'getCategoriasAdquisicion', t: (new Date()).getTime()}).success(
				function(response) {
					mi.categorias = response.categoriasAdquisicion;
		});
		
		mi.cambioTipo=function(selected){
			if(selected!== undefined){
				mi.adquisicion.tipoNombre=selected.originalObject.nombre;
				mi.adquisicioin.tipoId=selected.originalObject.id;
			}
			else{
				mi.adquisicion.tipoNombre="";
				mi.adquisicion.tipoId="";
			}
		}
		
		mi.blurTipo=function(){
			if(document.getElementById("tipo_value").defaultValue!=mi.adquisicion.tipoNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','tipo');
			}
		}
		
		mi.cambioCategoria=function(selected){
			if(selected!== undefined){
				mi.adquisicion.categoriaNombre=selected.originalObject.nombre;
				mi.adquisicioin.categoriaId=selected.originalObject.id;
			}
			else{
				mi.adquisicion.categoriaNombre="";
				mi.adquisicion.categoriaId="";
			}
		}
		
		mi.blurCategoria=function(){
			if(document.getElementById("categoria_value").defaultValue!=mi.adquisicion.categoriaNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','categoria');
			}
		}
		
		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2020, 5, 22),
				minDate : new Date(1900, 1, 1),
				startingDay : 1
		};
		
		mi.editarElemento = function (event) {
	        mi.editar();
	    };
	    
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SPlanAdquisicion', { accion: 'getPlanAdquisicionPorObjeto',  
				objetoid: mi.objetoId, objetotipo: mi.objetoTipo, t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.adquisiciones = response.adquisiciones;
						mi.mostrarcargando = false;
						for(var i=0; i<mi.adquisiciones.length; i++){
							mi.adquisiciones[i].adjudicacionPlanificada = mi.adquisiciones[i].adjudicacionPlanificada!=null ? moment(mi.adquisiciones[i].adjudicacionPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].adjudicacionReal = mi.adquisiciones[i].adjudicacionReal!=null ? moment(mi.adquisiciones[i].adjudicacionReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].firmaContratoPlanificada = mi.adquisiciones[i].firmaContratoPlanificada!=null ? moment(mi.adquisiciones[i].firmaContratoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].firmaContratoReal = mi.adquisiciones[i].firmaContratoReal!=null ? moment(mi.adquisiciones[i].firmaContratoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].lanzamientoEventoPlanificada = mi.adquisiciones[i].lanzamientoEventoPlanificada!=null ? moment(mi.adquisiciones[i].lanzamientoEventoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].lanzamientoEventoReal = mi.adquisiciones[i].lanzamientoEventoReal!=null ? moment(mi.adquisiciones[i].lanzamientoEventoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].preparacionDocumentosPlanificada = mi.adquisiciones[i].preparacionDocumentosPlanificada!=null ? moment(mi.adquisiciones[i].preparacionDocumentosPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].preparacionDocumentosReal = mi.adquisiciones[i].preparacionDocumentosReal!=null ? moment(mi.adquisiciones[i].preparacionDocumentosReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].recepcionOfertasPlanificada = mi.adquisiciones[i].recepcionOfertasPlanificada!=null ? moment(mi.adquisiciones[i].recepcionOfertasPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].recepcionOfertasReal = mi.adquisiciones[i].recepcionOfertasReal!=null ? moment(mi.adquisiciones[i].recepcionOfertasReal,'DD/MM/YYYY').toDate() : null;
						}
					});
		}
		
		mi.cargarTabla();
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		
		mi.guardar=function(mensaje_success, mensaje_error){
			
			if(mi.adquisiciones.length>0){
				$http.post('/SPlanAdquisicion', {
					accion: 'guardarRiesgos',
					adquisiciones: JSON.stringify(mi.adquisiciones),
					objetoTipo: mi.objetoTipo,
					objetoId: mi.objetoId,
					t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						var sids = response.ids.length>0 ? response.ids.split(",") : [];
						for(var i = 0; i<sids.length; i++)
							mi.adquisiciones[i].id = parseInt(sids[i]);
						mi.cargarTabla();
						$utilidades.mensaje('success',mensaje_success);
					}
					else
						$utilidades.mensaje('danger',mensaje_error);
				});
			}
			else
				$utilidades.mensaje('success',mensaje_success);
		};

		mi.borrar = function(index) {
			mi.adquisicion = mi.adquisiciones[index];
			if(mi.adquisicion!=null && index >-1){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Riesgo "'+mi.adquisicion.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						var index = mi.adquisiciones.indexOf(row);
				        if (index !== -1) {
				            mi.adquisiciones.splice(index, 1);
				        }
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar la adquisición que desea borrar');
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.adquisicion = { 
			};
		};

		mi.editar = function(row) {
			mi.adquisicion=row;
			if(mi.adquisicion!=null){
				mi.form_valid = null;
				mi.mostraringreso = true;
				mi.esnuevo = false;
				$utilidades.setFocus(document.getElementById("descripcion"));
				for (campos in mi.adquisicion.camposdinamicos) {
					switch (mi.adquisicion.camposdinamicos[campos].tipo){
						case 5:
							mi.adquisicion.camposdinamicos[campos].valor = !(mi.adquisicion.camposdinamicos[campos].valor instanceof Date) ? moment(mi.adquisicion.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : mi.adquisicion.camposdinamicos[campos].valor ;
							break;
						case 1:
							mi.adquisicion.camposdinamicos[campos].valor = (mi.adquisicion.camposdinamicos[campos].valor!='' && mi.adquisicion.camposdinamicos[campos].valor!=null) ? Number(mi.adquisicion.camposdinamicos[campos].valor) : null;
							break;
						case 2:
							mi.adquisicion.camposdinamicos[campos].valor = (mi.adquisicion.camposdinamicos[campos].valor!='' &&mi.adquisicion.camposdinamicos[campos].valor!=null) ? Number(mi.adquisicion.camposdinamicos[campos].valor) : null;
							break;
					}
				}
				
			}
		}

		mi.irATabla = function() {
			if(mi.esnuevo){
					console.log(mi.parentController.mForm.$valid);
					if(mi.parentController.mForm.$valid || 
							(mi.parentController.mForm.$error.required.length==1 && mi.parentController.mForm.$error.required[0].$name=='form_valid'))
						mi.adquisiciones.push({
							tipoNombre: mi.adquisicion.tipoNombre,
							tipoId: mi.adquisicion.tipoId,
							categoriaNombre: mi.adquisicion.categoriaNombre,
							categoriaId: mi.adquisicion.categoriaId,
							medidaNombre: mi.adquisicion.medidaNombre,
							cantidad: mi.adquisicion.cantidad,
							precio: mi.adquisicion.precio,
							total: mi.adquisicion.total,
							nog: mi.adquisicion.nog,
							numeroContrato: mi.adquisicion.numeroContrato,
							montoContrato: mi.adquisicion.montoContrato,
							preparacionDocumentoPlanificado: moment(mi.adquisicion.preparacionDocumentoPlanifiado).format('DD/MM/YYY'),
							preparacionDocumentoReal: moment(mi.adquisicion.preparacionDocumentoReal).format('DD/MM/YYYY'),
							lanzamientoEventoPlanificado:moment(mi.adquisicion.lanzamientoEventoPlanificado).format('DD/MM/YYYY'),
							lanzamientoEventoReal:moment(mi.adquisicion.lanzamientoEventoReal).format('DD/MM/YYYY'),
							recepcionOfertasPlanificado:moment(mi.adquisicion.recepcionOfertasPlanificado).format('DD/MM/YYYY'),
							recepcionOfertasReal:moment(mi.adquisicion.recepcionOfertasReal).format('DD/MM/YYYY'),
							adjudicacionPlanificado:moment(mi.adquisicion.adjudicacionPlanificado).format('DD/MM/YYYY'),
							adjudicacionReal:moment(mi.adquisicion.adjudicacionReal).format('DD/MM/YYYY'),
							firmaContratoPlanificado:moment(mi.adquisicion.firmaContratoPlanificado).format('DD/MM/YYYY'),
							firmaContratoReal: mi.adquisicion.moment(mi.adquisicion.firmaContratoReal).format('DD/MM/YYYY'),
							pagos: mi.adquisicion.pagos
						});
					mi.mostraringreso=false;
					mi.esnuevo = false;
					mi.form_valid = 1;
				}
				else if(mi.parentController.mForm.$valid || mi.parentController.mForm.$error.required[0].$name=='form_valid'){
					mi.mostraringreso=false;
					mi.esnuevo = false;
					mi.form_valid = 1;
				}
				else if(!mi.parentController.mForm.$valid){
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
				}
		}
	
		
		mi.abrirPopupFecha = function(index) {
			mi.popup_fechas[index]=true;
		};
		
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalRiesgos();
				mi.gridApi.selection.clearSelectedRows();
				mi.adquisicion = null;
			}
		};
		
		
		mi.llamarModalBusqueda = function(servlet, accionServlet, datosCarga,columnaId,columnaNombre) {
			var resultado = $q.defer();
			var modalInstance = $uibModal.open({
				animation : 'true',
				ariaLabelledBy : 'modal-title',
				ariaDescribedBy : 'modal-body',
				templateUrl : 'buscarPorRiesgo.jsp',
				controller : 'buscarPorRiesgo',
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
	
	
	
	
	mi.buscarRiesgoTipo = function() {
		var resultado = mi.llamarModalBusqueda('/SRiesgoTipo', {
			accion : 'numeroComponenteTipos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getRiesgotiposPagina',
				pagina : pagina,
				numeroriesgostipo : elementosPorPagina
			};
		},'id','nombre');
		
		
		resultado.then(function(itemSeleccionado) {
			mi.adquisicionTipoid = itemSeleccionado.id;
			mi.adquisicionTipoNombre = itemSeleccionado.nombre;
			
			var parametros = { 
					accion: 'getRiesgoPropiedadPorTipo', 
					idRiesgo: mi.adquisicion!=null ? mi.adquisicion.id : 0,
					idRiesgoTipo: itemSeleccionado.id,
					t: (new Date()).getTime()
			}
			
			$http.post('/SRiesgoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.componentepropiedades;
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
	
	mi.buscarColaborador = function() {
		var resultado = mi.llamarModalBusqueda('/SColaborador', {
			accion : 'totalElementos'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'cargar',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombreCompleto');
		
		
		resultado.then(function(itemSeleccionado) {
			mi.colaboradorid = itemSeleccionado.id;
			mi.colaboradorNombre = itemSeleccionado.nombreCompleto;
			
		});
	};
	
			
} ]);

app.controller('buscarPorRiesgo', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$servlet', '$accionServlet', '$datosCarga','$columnaId','$columnaNombre',buscarPorRiesgo ]);

function buscarPorRiesgo($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $servlet,$accionServlet,$datosCarga, $columnaId,$columnaNombre) {
	
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

	mi.seleccionarTipoAdquisicion = function(row) {
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
			$utilidades.mensaje('warning', 'Debe seleccionar una ENTIDAD');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}