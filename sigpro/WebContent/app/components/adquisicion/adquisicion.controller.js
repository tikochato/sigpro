app.controller('adquisicionController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
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
			$scope.$parent.producto.child_adquisiciones = $scope.adquisicionc;
			mi.parentController = $scope.$parent.producto;
			mi.objetoTipo = 3;
			mi.objetoId = $scope.$parent.producto.producto.id;
		}
		if($scope.$parent.subproductoc){
			$scope.$parent.subproductoc.child_adquisiciones = $scope.adquisicionc;
			mi.parentController = $scope.$parent.subproductoc;
			mi.objetoTipo = 4;
			mi.objetoId = $scope.$parent.subproductoc.subproducto.id;
		}
		if($scope.$parent.actividadc){
			$scope.$parent.actividadc.child_adquisiciones = $scope.adquisicionc;
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
				mi.adquisicion.tipoId=selected.originalObject.id;
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
				mi.adquisicion.categoriaId=selected.originalObject.id;
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
				objetoId: mi.objetoId, objetoTipo: mi.objetoTipo, t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.adquisiciones = response.adquisiciones;
						mi.mostrarcargando = false;
						for(var i=0; i<mi.adquisiciones.length; i++){
							mi.adquisiciones[i].adjudicacionPlanificada = mi.adquisiciones[i].adjudicacionPlanificada!="" ? moment(mi.adquisiciones[i].adjudicacionPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].adjudicacionReal = mi.adquisiciones[i].adjudicacionReal!="" ? moment(mi.adquisiciones[i].adjudicacionReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].firmaContratoPlanificada = mi.adquisiciones[i].firmaContratoPlanificada!="" ? moment(mi.adquisiciones[i].firmaContratoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].firmaContratoReal = mi.adquisiciones[i].firmaContratoReal!="" ? moment(mi.adquisiciones[i].firmaContratoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].lanzamientoEventoPlanificada = mi.adquisiciones[i].lanzamientoEventoPlanificada!="" ? moment(mi.adquisiciones[i].lanzamientoEventoPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].lanzamientoEventoReal = mi.adquisiciones[i].lanzamientoEventoReal!="" ? moment(mi.adquisiciones[i].lanzamientoEventoReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].preparacionDocumentosPlanificada = mi.adquisiciones[i].preparacionDocumentosPlanificada!="" ? moment(mi.adquisiciones[i].preparacionDocumentosPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].preparacionDocumentosReal = mi.adquisiciones[i].preparacionDocumentosReal!="" ? moment(mi.adquisiciones[i].preparacionDocumentosReal,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].recepcionOfertasPlanificada = mi.adquisiciones[i].recepcionOfertasPlanificada!="" ? moment(mi.adquisiciones[i].recepcionOfertasPlanificada,'DD/MM/YYYY').toDate() : null;
							mi.adquisiciones[i].recepcionOfertasReal = mi.adquisiciones[i].recepcionOfertasReal!="" ? moment(mi.adquisiciones[i].recepcionOfertasReal,'DD/MM/YYYY').toDate() : null;
							if(mi.adquisiciones[i].pagos===undefined || mi.adquisiciones[i].pagos==null)
								mi.adquisiciones[i].pagos=[];
							else{
								for(var j=0; j<mi.adquisiciones[i].pagos.length; j++){
									mi.adquisiciones[i].pagos[j].fecha = mi.adquisiciones[i].pagos[j].fechaPago;
									mi.adquisiciones[i].pagos[j].fechaPago = (mi.adquisiciones[i].pagos[j].fechaPago!=null && mi.adquisiciones[i].pagos[j].fechaPago!="") ? moment(mi.adquisiciones[i].pagos[j].fechaPago,'DD/MM/YYYY').toDate() : null;
								}
							}
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
					accion: 'guardarAdquisiciones',
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

		mi.borrar = function(row) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar la adquisición "'+mi.adquisicion.categoriaNombre+'"?'
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
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.adquisicion = {
					
			};
			mi.adquisicion.pagos=[];
			$scope.$broadcast('angucomplete-alt:clearInput','categoria');
			$scope.$broadcast('angucomplete-alt:clearInput','tipo');
		};

		mi.editar = function(row) {
			mi.adquisicion=row;
			if(mi.adquisicion!=null){
				mi.form_valid = null;
				mi.mostraringreso = true;
				mi.esnuevo = false;
				$scope.$broadcast('angucomplete-alt:changeInput','categoria', mi.adquisicion.categoriaNombre);
				$scope.$broadcast('angucomplete-alt:changeInput','tipo', mi.adquisicion.tipoNombre);
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
					if(mi.parentController.mForm.$valid || 
							(mi.parentController.mForm.$error.required.length==1 && mi.parentController.mForm.$error.required[0].$name=='form_valid'))
						mi.adquisiciones.push({
							id: -1,
							tipoNombre: mi.adquisicion.tipoNombre,
							tipoId: mi.adquisicion.tipoId,
							categoriaNombre: mi.adquisicion.categoriaNombre,
							categoriaId: mi.adquisicion.categoriaId,
							medidaNombre: mi.adquisicion.medidaNombre,
							cantidad: mi.adquisicion.cantidad,
							precioUnitario: mi.adquisicion.precioUnitario,
							total: mi.adquisicion.total,
							nog: mi.adquisicion.nog,
							numeroContrato: mi.adquisicion.numeroContrato,
							montoContrato: mi.adquisicion.montoContrato,
							preparacionDocumentoPlanificado: mi.adquisicion.preparacionDocumentoPlanificado!=null ? moment(mi.adquisicion.preparacionDocumentoPlanifiado).format('DD/MM/YYY') : null,
							preparacionDocumentoReal: mi.adquisicion.preparacionDocumentoReal!=null ? moment(mi.adquisicion.preparacionDocumentoReal).format('DD/MM/YYYY') : null,
							lanzamientoEventoPlanificado: mi.adquisicion.lanzamientoEventoPlanificado!=null ? moment(mi.adquisicion.lanzamientoEventoPlanificado).format('DD/MM/YYYY') : null,
							lanzamientoEventoReal:mi.adquisicion.lanzamientoEventoReal!=null ? moment(mi.adquisicion.lanzamientoEventoReal).format('DD/MM/YYYY') : null,
							recepcionOfertasPlanificado: mi.adquisicion.recepcionOfertasPlanificado!=null ? moment(mi.adquisicion.recepcionOfertasPlanificado).format('DD/MM/YYYY') : null,
							recepcionOfertasReal: mi.adquisicion.recepcionOfertasReal!=null ? moment(mi.adquisicion.recepcionOfertasReal).format('DD/MM/YYYY') : null,
							adjudicacionPlanificado: mi.adquisicion.adjudicacionPlanificado!=null ? moment(mi.adquisicion.adjudicacionPlanificado).format('DD/MM/YYYY') : null,
							adjudicacionReal: mi.adquisicion.adjudicacionReal!=null ? moment(mi.adquisicion.adjudicacionReal).format('DD/MM/YYYY') : null,
							firmaContratoPlanificado: mi.adquisicion.firmaContratoPlanificado!=null ? moment(mi.adquisicion.firmaContratoPlanificado).format('DD/MM/YYYY') :null,
							firmaContratoReal:  mi.adquisicion.firmaContratoReal!=null ? moment(mi.adquisicion.firmaContratoReal).format('DD/MM/YYYY') : null,
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
		
		mi.agregarPagos = function(row) {
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
				    	return row.pagos;
				    }
				  }
			});

			modalInstance.result.then(function() {
			   
			}, function() {
			});

		};
		
			
} ]);

app.controller('modalPlanadquisicionPagos', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log','dialogoConfirmacion', 'pagos', 
	function ($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log,$dialogoConfirmacion, pagos, totalPagos) {
	
		var mi = this;
		
		$scope.pagos = pagos;
		mi.pagos = $scope.pagos;
		mi.formatofecha = 'dd/MM/yyyy';
		mi.totalPagos=0;
				
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
		             return total + item.pago;
		         },0);
		     } 
		 }, true);
	
	}
]);
