app.controller('adquisicionController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
		var mi=this;
		
		mi.mostrarcargando=true;
		mi.adquisiciones = [];
		mi.adquisicion;
		mi.esnuevo = false;
		mi.camposdinamicos = {};
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
		
		mi.objetoTipo = 0;
		mi.objetoId = 0;
		
		mi.categorias=[];
		mi.tipos=[];
		
		mi.popup_fechas=[false,false,false,false,false,false,false,false,false];
		
		mi.parentController=null;
		
		mi.requerido=false;
		mi.tieneHijos = false;
		mi.actualizarCosto=null;
		mi.inhabilitarFechas=false;
		mi.congelado = 0;
		
		if($scope.$parent.producto){
			$scope.$parent.producto.child_adquisiciones = $scope.adquisicionc;
			mi.parentController = $scope.$parent.producto;
			if($scope.$parent.producto.componenteid){
				mi.objetoTipo = 1;
				mi.objetoId = $scope.$parent.producto.componenteid;
			}
			if($scope.$parent.producto.subcomponenteid){
				mi.objetoTipo = 2;
				mi.objetoId = $scope.$parent.producto.subcomponenteid;
			}
			mi.id = $scope.$parent.producto.producto.id;
			mi.congelado = $scope.$parent.producto.congelado;
			mi.tipo = 3;
			mi.tieneHijos = $scope.$parent.producto.tieneHijos;
			mi.actualizarCosto = $scope.$parent.producto.actualizarCosto;
			mi.fechaElegibilidad = $scope.$parent.producto.producto.fechaElegibilidad;
			mi.fechaCierre = $scope.$parent.producto.producto.fechaCierre;
		}
		if($scope.$parent.subproducto){
			$scope.$parent.subproducto.child_adquisiciones = $scope.adquisicionc;
			mi.parentController = $scope.$parent.subproducto;
			mi.objetoTipo = 3;
			mi.objetoId = $scope.$parent.subproducto.productoid;
			mi.id = $scope.$parent.subproducto.subproducto.id;
			mi.congelado = $scope.$parent.subproducto.congelado;
			mi.tipo = 4;
			mi.tieneHijos = $scope.$parent.subproducto.tieneHijos;
			mi.actualizarCosto = $scope.$parent.subproducto.actualizarCosto;
			mi.fechaElegibilidad = $scope.$parent.subproducto.subproducto.fechaElegibilidad;
			mi.fechaCierre = $scope.$parent.subproducto.subproducto.fechaCierre;
		}
		if($scope.$parent.actividadc){
			$scope.$parent.actividadc.child_adquisiciones = $scope.adquisicionc;
			mi.parentController = $scope.$parent.actividadc;
			mi.objetoTipo = $scope.$parent.actividadc.objetotipo;
			mi.objetoId = $scope.$parent.actividadc.objetoid;
			mi.id = $scope.$parent.actividadc.actividad.id;
			mi.congelado = $scope.$parent.actividadc.congelado;
			mi.tipo = 5;
			mi.tieneHijos = $scope.$parent.actividadc.tieneHijos;
			mi.actualizarCosto = $scope.$parent.actividadc.actualizarCosto;
			mi.fechaElegibilidad = $scope.$parent.actividadc.actividad.fechaElegibilidad;
			mi.fechaCierre = $scope.$parent.actividadc.actividad.fechaCierre;
		}
		
		mi.validarFechas = function(fecha, pos, tipo){
			if(fecha != undefined){
				if(fecha < moment(mi.fechaElegibilidad, 'DD/MM/YYYY)').toDate()){
					$utilidades.mensaje('warning', 'La fecha no puede ser menor a la fecha de Elegibilidad.');
					
					if(pos==1){
						if(tipo==1)
							mi.adquisicion.preparacionDocumentosPlanificada = null;
						else if (tipo==2)
							mi.adquisicion.preparacionDocumentosReal = null;
					}else if(pos==2){
						if(tipo==1)
							mi.adquisicion.lanzamientoEventoPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.lanzamientoEventoReal = null;
					}else if(pos==3){
						if(tipo==1)
							mi.adquisicion.recepcionOfertasPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.recepcionOfertasReal = null;
					}else if(pos==4){
						if(tipo==1)
							mi.adquisicion.adjudicacionPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.adjudicacionReal = null;
					}else if(pos==5){
						if(tipo==1)
							mi.adquisicion.firmaContratoPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.firmaContratoReal = null;
					}	
				}else if(fecha > moment(mi.fechaCierre,'DD/MM/YYYY').toDate()){
					$utilidades.mensaje('warning', 'La fecha no puede ser mayor a la fecha de Cierre.');
					
					if(pos==1){
						if(tipo==1)
							mi.adquisicion.preparacionDocumentosPlanificada = null;
						else if (tipo==2)
							mi.adquisicion.preparacionDocumentosReal = null;
					}else if(pos==2){
						if(tipo==1)
							mi.adquisicion.lanzamientoEventoPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.lanzamientoEventoReal = null;
					}else if(pos==3){
						if(tipo==1)
							mi.adquisicion.recepcionOfertasPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.recepcionOfertasReal = null;
					}else if(pos==4){
						if(tipo==1)
							mi.adquisicion.adjudicacionPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.adjudicacionReal = null;
					}else if(pos==5){
						if(tipo==1)
							mi.adquisicion.firmaContratoPlanificada = null;
						else if(tipo==2)
							mi.adquisicion.firmaContratoReal = null;
					}	
				}else{
					switch(pos){
					case 2:
						if(tipo==1){
							if(fecha < mi.adquisicion.preparacionDocumentosPlanificada){
								mi.adquisicion.lanzamientoEventoPlanificada = null;
								$utilidades.mensaje('warning', 'Fecha de "Lanzamiento de eventos (Planificada)" no debe ser menor que la fecha de "Preparación de documentos (Planificada)".');
							}	
						}else if(tipo==2){
							if(fecha < mi.adquisicion.preparacionDocumentosReal){
								mi.adquisicion.lanzamientoEventoReal = null;
								$utilidades.mensaje('warning', 'Fecha de "Lanzamiento de eventos (Real)" no debe ser menor que la fecha de "Preparación de documentos (Real)".');
							}
						}
						break;
					case 3:
						if(tipo==1){
							if(fecha < mi.adquisicion.lanzamientoEventoPlanificada){
								mi.adquisicion.recepcionOfertasPlanificada = null;
								$utilidades.mensaje('warning', 'Fecha de "Recepción de ofertas (Planificada)" no debe ser menor que la fecha de "Lanzamiento de eventos (Planificada)".');
							}	
						}else if(tipo==2){
							if(fecha < mi.adquisicion.lanzamientoEventoReal){
								mi.adquisicion.recepcionOfertasReal = null;
								$utilidades.mensaje('warning', 'Fecha de "Recepción de ofertas (Real)" no debe ser menor que la fecha de "Lanzamiento de eventos (Real)".');
							}
						}
						break;
					case 4:
						if(tipo==1){
							if(fecha < mi.adquisicion.recepcionOfertasPlanificada){
								mi.adquisicion.adjudicacionPlanificada = null;
								$utilidades.mensaje('warning', 'Fecha de "Adjudicación (Planificada)" no debe ser menor que la fecha de "Recepción de ofertas (Planificada)".');
							}	
						}else if(tipo==2){
							if(fecha < mi.adquisicion.recepcionOfertasReal){
								mi.adquisicion.adjudicacionReal = null;
								$utilidades.mensaje('warning', 'Fecha de "Adjudicación (Real)" no debe ser menor que la fecha de "Recepción de ofertas (Real)".');
							}
						}
						break;
					case 5:
						if(tipo==1){
							if(fecha < mi.adquisicion.adjudicacionPlanificada){
								mi.adquisicion.firmaContratoPlanificada = null;
								$utilidades.mensaje('warning', 'Fecha de "Firma de contrato (Planificada)" no debe ser menor que la fecha de "Adjudicación (Planificada)".');
							}	
						}else if(tipo==2){
							if(fecha < mi.adquisicion.adjudicacionReal){
								mi.adquisicion.firmaContratoReal = null;
								$utilidades.mensaje('warning', 'Fecha de "Firma de contrato (Real)" no debe ser menor que la fecha de "Adjudicación (Real)".');
							}
						}
						break;
					}
				}
			}
		}
		
		mi.actualizarObjetoId=function(){
			switch(mi.tipo){
				case 3: mi.id = $scope.$parent.producto.producto.id; break;
				case 4: mi.id = $scope.$parent.subproducto.subproducto.id; break;
				case 5: mi.id = $scope.$parent.actividadc.actividad.id; break;
			}
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
				mi.adquisicion.tipoId=selected.originalObject.id;
			}
			else{
				mi.adquisicion.tipoNombre="";
				mi.adquisicion.tipoId="";
			}
			mi.actualizaObligatorios();
		}
		
		mi.blurTipo=function(){
			if(document.getElementById("tipo_value").defaultValue!=mi.adquisicion.tipoNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','tipo');
			}
			mi.actualizaObligatorios();
		}
		
		mi.cambioCategoria=function(selected){
			if(selected!== undefined){
				mi.adquisicion.categoriaNombre=selected.originalObject.nombre;
				mi.adquisicion.categoriaId=selected.originalObject.id;
			}
			else{
				mi.adquisicion.categoriaNombre="";
				mi.adquisicion.categoriaId="";
			}
			mi.actualizaObligatorios();
		}
		
		mi.blurCategoria=function(){
			if(document.getElementById("categoria_value").defaultValue!=mi.adquisicion.categoriaNombre){
				$scope.$broadcast('angucomplete-alt:clearInput','categoria');
			}
			mi.actualizaObligatorios();
		}
		
		mi.fechaOptions = {
				formatYear : 'yy',
				//maxDate : new Date(2020, 5, 22),
				//minDate : new Date(1900, 1, 1),
				maxDate: moment(mi.fechaCierre, 'DD/MM/YYYY').toDate(),
				minDate: moment(mi.fechaElegibilidad, 'DD/MM/YYYY').toDate(),
				startingDay : 1
		};
		
		mi.editarElemento = function (event) {
	        mi.editar();
	    };
	    
		mi.cargarAdquisicion = function(pagina){
			$http.post('/SPlanAdquisicion', { accion: 'getPlanAdquisicionPorObjeto',  
				objetoId: mi.id, objetoTipo: mi.tipo, t: (new Date()).getTime()
				}).success(
					function(response) {
						if(response.adquisicion!=null){
							mi.adquisicion = response.adquisicion;
							mi.mostrarcargando = false;
							
							mi.adquisicion.adjudicacionPlanificada = mi.adquisicion.adjudicacionPlanificada!==undefined && mi.adquisicion.adjudicacionPlanificada!='' ? moment(mi.adquisicion.adjudicacionPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.adjudicacionReal = mi.adquisicion.adjudicacionReal!==undefined && mi.adquisicion.adjudicacionReal!='' ? moment(mi.adquisicion.adjudicacionReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.firmaContratoPlanificada = mi.adquisicion.firmaContratoPlanificada!==undefined && mi.adquisicion.firmaContratoPlanificada!='' ? moment(mi.adquisicion.firmaContratoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.firmaContratoReal = mi.adquisicion.firmaContratoReal!==undefined && mi.adquisicion.firmaContratoReal!='' ? moment(mi.adquisicion.firmaContratoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.lanzamientoEventoPlanificada = mi.adquisicion.lanzamientoEventoPlanificada!==undefined && mi.adquisicion.lanzamientoEventoPlanificada!='' ? moment(mi.adquisicion.lanzamientoEventoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.lanzamientoEventoReal = mi.adquisicion.lanzamientoEventoReal!==undefined && mi.adquisicion.lanzamientoEventoReal!='' ? moment(mi.adquisicion.lanzamientoEventoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.preparacionDocumentosPlanificada = mi.adquisicion.preparacionDocumentoPlanificada!==undefined && mi.adquisicion.preparacionDocumentoPlanificada!='' ? moment(mi.adquisicion.preparacionDocumentoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.preparacionDocumentosReal = mi.adquisicion.preparacionDocumentoReal!==undefined && mi.adquisicion.preparacionDocumentoReal!='' ? moment(mi.adquisicion.preparacionDocumentoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.recepcionOfertasPlanificada = mi.adquisicion.recepcionOfertasPlanificada!==undefined && mi.adquisicion.recepcionOfertasPlanificada!='' ? moment(mi.adquisicion.recepcionOfertasPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisicion.recepcionOfertasReal = mi.adquisicion.recepcionOfertasReal!==undefined && mi.adquisicion.recepcionOfertasReal!='' ? moment(mi.adquisicion.recepcionOfertasReal,'DD/MM/YYYY').toDate() : null;
							if(mi.adquisicion.pagos===undefined || mi.adquisicion.pagos==null)
								mi.adquisicion.pagos=[];
							else{
								for(var j=0; j<mi.adquisicion.pagos.length; j++){
									mi.adquisicion.pagos[j].fecha = mi.adquisicion.pagos[j].fechaPago;
									mi.adquisicion.pagos[j].fechaPago = (mi.adquisicion.pagos[j].fechaPago!=null && mi.adquisicion.pagos[j].fechaPago!="") ? moment(mi.adquisicion.pagos[j].fechaPago,'DD/MM/YYYY').toDate() : null;
								}
							}
							
							mi.getInfoNog();
						}
						else{
							mi.nuevo();
						}
					});
		}
		
		mi.cargarAdquisicion();
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		
		mi.guardar=function(call_chain, mensaje_success, mensaje_error){
			mi.actualizarObjetoId();
			if(mi.adquisicion!=null && mi.adquisicion.medidaNombre!=null && mi.adquisicion.medidaNombre!=''){
				$http.post('/SPlanAdquisicion', {
					accion: 'guardarAdquisicion',
					objetoTipo: mi.tipo,
					objetoId: mi.id,
					adjudicacionPlanificada: mi.adquisicion.adjudicacionPlanificada,
					adjudicacionReal: mi.adquisicion.adjudicacionReal,
					cantidad: mi.adquisicion.cantidad,
					categoriaId: mi.adquisicion.categoriaId,
					firmaContratoPlanificada: mi.adquisicion.firmaContratoPlanificada,
					firmaContratoReal: mi.adquisicion.firmaContratoReal,
					id: mi.adquisicion.id,
					lanzamientoEventoPlanificada: mi.adquisicion.lanzamientoEventoPlanificada,
					lanzamientoEventoReal: mi.adquisicion.lanzamientoEventoReal,
					montoContrato: mi.adquisicion.montoContrato != null ? mi.adquisicion.montoContrato : 0,
					nog: mi.adquisicion.nog,
					numeroContrato: mi.adquisicion.numeroContrato,
					precioUnitario: mi.adquisicion.precioUnitario,
					preparacionDocumentosPlanificada: mi.adquisicion.preparacionDocumentosPlanificada,
					preparacionDocumentosReal: mi.adquisicion.preparacionDocumentosReal,
					recepcionOfertasPlanificada: mi.adquisicion.recepcionOfertasPlanificada,
					recepcionOfertasReal: mi.adquisicion.recepcionOfertasReal,
					tipoId: mi.adquisicion.tipoId,
					total: mi.adquisicion.total,
					medidaNombre: mi.adquisicion.medidaNombre,
					pagos: JSON.stringify(mi.adquisicion.pagos),
					tipoRevision: mi.adquisicion.tipoRevision,
					t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						mi.adquisicion.id = response.id;
						if(mi.adquisicion.pagos!=null && mi.adquisicion.pagos!==undefined && mi.adquisicion.pagos.length>0){
							var pagos = 0;
							for(var i=0; i<mi.adquisicion.pagos.length; i++)
								pagos = mi.adquisicion.pagos[i].pago;
							mi.actualizarCosto(pagos);
						}
						else
							mi.actualizarCosto(mi.adquisicion.total);
						if(call_chain!=null)
							call_chain(mensaje_success, mensaje_error);
						else
							$utilidades.mensaje('success',mensaje_success);
					}
					else
						$utilidades.mensaje('danger',mensaje_error);
				});
			}
			else{
				$http.post('/SPlanAdquisicion', {
					accion: 'borrarTodasAdquisiciones',
					objetoId: mi.id,
					objetoTipo: mi.tipo, 
					t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						if(call_chain!=null){
							call_chain(mensaje_success, mensaje_error);
						}
						else
							$utilidades.mensaje('success',mensaje_success);
					}
					else
						$utilidades.mensaje('danger',mensaje_error);
				});
			}
		};

		mi.borrar = function(row) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar la adquisición?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						mi.nuevo();
					}
				}, function(){
					
				});
		};

		mi.nuevo = function() {
			mi.esnuevo = true;
			mi.adquisicion = {
					
			};
			mi.adquisicion.pagos=[];
			$scope.$broadcast('angucomplete-alt:clearInput','categoria');
			$scope.$broadcast('angucomplete-alt:clearInput','tipo');
		};

		mi.abrirPopupFecha = function(index) {
			mi.popup_fechas[index]=true;
		};
		
		mi.agregarPagos = function() {
			var montoContrato = mi.montoContrato();
			if(montoContrato != null && montoContrato > 0){
				var modalInstance = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'pago.jsp',
					controller : 'modalPlanadquisicionPagos',
					controllerAs : 'modalPagos',
					backdrop : 'static',
					size : 'md',
					resolve: {
					    pagos: function(){
					    	return mi.adquisicion.pagos;
					    },
					    totalPagos: function(){
					    	return mi.totalPagos;
					    },
					    montoContrato: function(){
					    	//return mi.adquisicion.montoContrato;
					    	return mi.montoContrato();
					    }, 
					    congelado: function(){
					    	return mi.congelado;
					    }
					  }
				});

				modalInstance.result.then(function() {
				   
				}, function() {
				});

			}else
				$utilidades.mensaje('warning','Para agregar pagos, ingrese Monto del contrato');
		};
		
		mi.montoContrato = function(){
			var totalMonto = 0;
			if(mi.adquisicion.montoContrato != null && mi.adquisicion.montoContrato > 0)
				return mi.adquisicion.montoContrato;
			else{
				for(x in mi.infoNogs){
					totalMonto += mi.infoNogs[x].montoContrato;
				}
				return totalMonto;
			}
		}
		
		mi.actualizaMontos=function(control){
			switch(control){
				case 'total':
					if(mi.adquisicion.total>=0){
						if(mi.adquisicion.cantidad>0)
							mi.adquisicion.precioUnitario = mi.adquisicion.total / mi.adquisicion.cantidad;
						else if(mi.adquisicion.precioUnitario>0)
							mi.adquisicion.cantidad = mi.adquisicion.total / mi.adquisicion.precioUnitario;
					}
					break;
				case 'cantidad':
					if(mi.adquisicion.cantidad>=0){
						if(mi.adquisicion.precioUnitario>=0)
							mi.adquisicion.total = mi.adquisicion.cantidad*mi.adquisicion.precioUnitario;
						else if(mi.adquisicion.total>=0 && mi.adquisicion.cantidad>0)
							mi.adquisicion.precioUnitario = mi.adquisicion.total / mi.adquisicion.cantidad;
					}
					break;
				case 'precio':
					if(mi.adquisicion.precioUnitario>=0){
						if(mi.adquisicion.cantidad>=0)
							mi.adquisicion.total = mi.adquisicion.cantidad*mi.adquisicion.precioUnitario;
						else if(mi.adquisicion.total>=0)
							mi.adquisicion.cantidad = mi.adquisicion.total / mi.adquisicion.precioUnitario;
					}
					break;
						
			}
			mi.actualizaObligatorios();
		}
		
		mi.actualizaObligatorios=function(){
			if(mi.adquisicion.categoriaNombre || 
					mi.adquisicion.tipoNombre ||
					mi.adquisicion.medidaNombre ||
					mi.adquisicion.cantidad ||
					mi.adquisicion.total)
				mi.requerido=true;
			else
				mi.requerido=false;
		}
		
		mi.getInfoNog = function(){
			if(mi.adquisicion.nog != null){
				$http.post('/SPlanAdquisicion', { accion: 'getInfoNog', nog: mi.adquisicion.nog}).success(
					function(response){
						if(response.success){
							if(response.nogInfo.length == 1){
								mi.adquisicion.numeroContrato = response.nogInfo[0].numeroContrato;
								mi.adquisicion.montoContrato = response.nogInfo[0].montoContrato;
								mi.adquisicion.preparacionDocumentosReal = response.nogInfo[0].preparacionDocumentosReal != undefined && response.nogInfo[0].preparacionDocumentosReal !='' ? moment(response.nogInfo[0].preparacionDocumentosReal, 'DD/MM/YYYY').toDate() : null;
								mi.adquisicion.lanzamientoEventoReal = response.nogInfo[0].lanzamientoEventoReal != undefined && response.nogInfo[0].lanzamientoEventoReal != '' ? moment(response.nogInfo[0].lanzamientoEventoReal, 'DD/MM/YYYY').toDate() : null;
								mi.adquisicion.recepcionOfertasReal = response.nogInfo[0].recepcionOfertasReal != undefined && response.nogInfo[0].recepcionOfertasReal != '' ? moment(response.nogInfo[0].recepcionOfertasReal, 'DD/MM/YYYY').toDate() : null;
								mi.adquisicion.adjudicacionReal = response.nogInfo[0].adjudicacionReal != undefined && response.nogInfo[0].adjudicacionReal != '' ? moment(response.nogInfo[0].adjudicacionReal, 'DD/MM/YYYY').toDate() : null;
								mi.adquisicion.firmaContratoReal = response.nogInfo[0].firmaContratoReal != undefined && response.nogInfo[0].firmaContratoReal != '' ? moment(response.nogInfo[0].firmaContratoReal, 'DD/MM/YYYY').toDate() : null;
								mi.inhabilitarFechas=true;
								mi.listaNog = false;
							}else{
								mi.adquisicion.numeroContrato = null;
								mi.adquisicion.montoContrato = null;
								mi.adquisicion.preparacionDocumentosReal = null;
								mi.adquisicion.lanzamientoEventoReal = null;
								mi.adquisicion.recepcionOfertasReal = null;
								mi.adquisicion.adjudicacionReal = null;
								mi.adquisicion.firmaContratoReal = null;
								
								mi.inhabilitarFechas=true;
								
								mi.infoNogs = response.nogInfo;
								mi.displayedInfoNog = [].concat(mi.infoNogs);
								mi.listaNog = true;
							}
						}else{
							$utilidades.mensaje('warning','No existe información para ese NOG');
							mi.adquisicion.nog = null;
						}
					}
				)
			}else{
				mi.infoNogs = null;
				mi.displayedInfoNog = [].concat(mi.infoNogs);
				mi.listaNog = false;
				mi.inhabilitarFechas=false;
			}
		}		
} ]);

app.controller('modalPlanadquisicionPagos', [ '$uibModalInstance',
	'$scope', '$http', '$interval',  'Utilidades',
	'$timeout', '$log','dialogoConfirmacion', 'pagos', 'totalPagos','montoContrato', 'congelado',
	function ($uibModalInstance, $scope, $http, $interval,
		$utilidades, $timeout, $log,$dialogoConfirmacion, pagos, totalPagos, montoContrato, congelado) {
	
		var mi = this;
		
		$scope.pagos = pagos;
		mi.pagos = $scope.pagos;
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
		mi.totalPagos=0;
		mi.montoContrato = montoContrato;
		mi.congelado = congelado;
		
		mi.abrirPopupFecha = function(index, tipo) {
			if(tipo==0){
				mi.pagos[index].isOpen = true;
			}else{
				mi.pagos[index].isOpenValor = true;
			}
			
		};

		mi.fechaOptions = {
				formatYear : 'yy',
				startingDay : 1
		};
		
		mi.guardarFecha = function(row){
			row.fecha = row.fechaPago!=null ? moment(row.fechaPago).format('DD/MM/YYYY') : null;
		}
				
		mi.nuevoPago = function(){
			$scope.pagos.push({  
	               fechaPago: null,
	               pago: null
	            });
		}
		
		mi.borrarPago = function(row){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el pago con fecha '+(row.fecha!=null ? moment(row.fecha).format('DD/MM/YYYY') : '')+'?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					var index = mi.pagos.indexOf(row);
					if (index > -1) {
						mi.pagos.splice(index, 1);
					}
				}
			}, function(){
				
			});
		}
		
		mi.ok = function() {
			$uibModalInstance.close();
		};
		
		$scope.$watch('pagos', function(array) {
		     var total = 0;
		     if (array) {
		         mi.totalPagos = array.reduce(function(total,item) {
		        	 if(total+item.pago <= mi.montoContrato)
		        		 return total + item.pago;
		        	 else{
		        		 $utilidades.mensaje('warning','Los pagos sobrepasan el Monto del contrato');
		        		 $scope.pagos.splice($scope.pagos.length-1, 1);
		        		 return total;
		        	 }
		         },0);
		     } 
		 }, true);
	
	}
]);
