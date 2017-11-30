//var app = angular.module('metaController', []);

app.controller('metaController',['$scope','$rootScope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog', '$uibModal', 'dialogoConfirmacion', 
		function($scope,$rootScope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog, $uibModal, $dialogoConfirmacion) {
			var mi=this;
			
			mi.titulo= "Meta";
			mi.tituloP= "Metas";
			
			$window.document.title = $utilidades.sistema_nombre+' - '+mi.titulo;
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.metas = [];
			mi.metasBorradas = [];
			mi.meta;
			mi.esnueva = false;
			mi.mostrarValores = false;
			mi.metasunidades = [];
			mi.tipoMetaSeleccionado=null;
			mi.unidadMedidaSeleccionado=null;
			mi.tipoValorSeleccionado=null;
					
			mi.nombrePcp = "";
			mi.nombreTipoPcp = "";
			mi.objeto = null;
			
			mi.anio = null;			
			mi.planificado = null;
			mi.real = null;
			mi.planificadoActual = null;

			mi.formatofecha = 'dd/MM/yyyy';
			mi.altformatofecha = ['d!/M!/yyyy'];
			mi.congelado = 0;
						
			mi.inicializarControlador = function(){
				mi.anios=[];
				mi.obtenerDatosPadre();
				mi.fechaInicio = mi.objeto.fechaInicio;
				mi.fechaFin = mi.objeto.fechaFin;
				var anioInicio = moment(mi.objeto.fechaInicio).year();
				var anioFin = moment(mi.objeto.fechaFin).year();
				for(a = anioInicio; a<=anioFin; a++){
					mi.anios.push(a);
				}
				if(mi.anios.length>0){
					mi.anio=mi.anios[0];
				}
				
				switch(mi.objeto_tipo){
				case "0": mi.nombreTipoPcp = $rootScope.etiquetas.proyecto; break;
				case "1": mi.nombreTipoPcp = "Componente"; break;
				case "2": mi.nombreTipoPcp = "Subcomponente"; break;
				case "3": mi.nombreTipoPcp = "Producto"; break;
				case "4": mi.nombreTipoPcp = "Subproducto"; break;
				
				}
				
				$http.post('/SMeta', { accion: 'getMetasUnidadesMedida', t: (new Date()).getTime() }).success(
						function(response) {
							mi.metaunidades = response.MetasUnidades;
							$http.post('/SDatoTipo', { accion: 'cargarCombo', t: (new Date()).getTime() }).success(
									function(response) {
										mi.datoTipos = response.datoTipos;
										mi.cargarTabla();
							});
				});
				
			}
			
			mi.obtenerDatosPadre = function(){
				var objeto;
				if($scope.$parent.prestamoc){
					objeto = $scope.$parent.prestamoc.prestamo;
					mi.objeto_tipo = -1;
					$scope.$parent.prestamoc.child_metas = $scope.metac;
					objeto.fechaInicio=objeto.fechaDecreto!=null ? objeto.fechaDecreto : new Date();
					objeto.fechaFin=new Date();
					mi.titulo = "Indicador"
					mi.tituloP = "Indicadores"
				}else if($scope.$parent.controller){
					objeto = $scope.$parent.controller.proyecto;
					mi.objeto_tipo = 0;
					mi.congelado = $scope.$parent.controller.congelado;
					$scope.$parent.controller.child_metas = $scope.metac;
				}else if($scope.$parent.componentec){
					objeto = $scope.$parent.componentec.componente;
					mi.objeto_tipo = 1;
					mi.congelado = $scope.$parent.componentec.congelado;
					$scope.$parent.componentec.child_metas = $scope.metac;
				}else if($scope.$parent.subcomponentec){
					objeto = $scope.$parent.subcomponentec.subcomponente;
					mi.objeto_tipo = 2;
					mi.congelado = $scope.$parent.subcomponentec.congelado;
					$scope.$parent.subcomponentec.child_metas = $scope.metac;
				}else if($scope.$parent.producto){
					objeto = $scope.$parent.producto.producto;
					mi.objeto_tipo = 3;
					mi.congelado = $scope.$parent.producto.congelado;
					$scope.$parent.producto.child_metas = $scope.metac;
				}else if($scope.$parent.subproducto){
					objeto = $scope.$parent.subproducto.subproducto;
					mi.objeto_tipo = 4;
					mi.congelado = $scope.$parent.subproducto.congelado;
					$scope.$parent.subproducto.child_metas = $scope.metac;
				}else if($scope.$parent.actividadc){
					objeto = $scope.$parent.actividadc.actividad;
					mi.objeto_tipo = 5;
					mi.congelado = $scope.$parent.actividadc.congelado;
					$scope.$parent.actividadc.child_metas = $scope.metac;
				}
								
				mi.objeto = objeto;
				mi.objeto_id = objeto.id;
			}
			
			mi.inicializarControlador();
			
			mi.cargarTabla = function(){
				$http.post('/SMeta', { accion: 'getMetasCompletas', 
					id:mi.objeto_id, tipo: mi.objeto_tipo,
					t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.metas = response.Metas;
						for(x in mi.metas){
							if(mi.metas[x].metaFinalTiempo!=null){
								mi.metas[x].fechaControl = moment(mi.metas[x].metaFinalTiempo,'DD/MM/YYYY').toDate();
							}
							if(mi.metas[x].datoTipoId==4){
								mi.metas[x].metaFinalString = 'true' == mi.metas[x].metaFinalString;
							}
							for(um in mi.metaunidades){
								if(mi.metaunidades[um].id === mi.metas[x].unidadMedidaId){
									mi.metas[x].unidadMedidaId = mi.metaunidades[um];
									break;
								}
							}
							for(dt in mi.datoTipos){
								if(mi.datoTipos[dt].id === mi.metas[x].datoTipoId){
									mi.metas[x].datoTipoId = mi.datoTipos[dt];
									break;
								}
							}
							for(a in mi.metas[x].avance){
								if(mi.metas[x].datoTipoId==4){
									mi.metas[x].avance[a].valorString = 'true' == mi.metas[x].avance[a].valorString;
								}
								mi.metas[x].avance[a].fechaControl = moment(mi.metas[x].avance[a].fecha,'DD/MM/YYYY').toDate();
								mi.metas[x].avance[a].valorTiempoControl = moment(mi.metas[x].avance[a].valorTiempo,'DD/MM/YYYY').toDate();
							}
						}
						if(mi.metas!=null && mi.metas.length>0){
							mi.metas[0].isSelected=true;
							mi.metaSeleccionada(mi.metas[0]);
						}
						mi.mostrarcargando = false;
					});
			}
						
			mi.metaSeleccionada = function(row){
				if(mi.rowAnterior){
					if(row != mi.rowAnterior){
						mi.rowAnterior.isSelected=false;
					}else{
						return;
					}
				}
				row.isSelected = true;
				mi.rowAnterior = row;
				mi.meta = row;
				mi.mostrarValores = true;
				mi.getMetasAnio(mi.meta, mi.anio);	
			}
						
			mi.abrirPopupFecha = function(meta) {
				switch(meta){
					case 1001: mi.planificado.isOpenEnero=true; break;
					case 1002: mi.planificado.isOpenFebrero=true; break;
					case 1003: mi.planificado.isOpenMarzo=true; break;
					case 1004: mi.planificado.isOpenAbril=true; break;
					case 1005: mi.planificado.isOpenMayo=true; break;
					case 1006: mi.planificado.isOpenJunio=true; break;
					case 1007: mi.planificado.isOpenJulio=true; break;
					case 1008: mi.planificado.isOpenAgosto=true; break;
					case 1009: mi.planificado.isOpenSeptiembre=true; break;
					case 1010: mi.planificado.isOpenOctubre=true; break;
					case 1011: mi.planificado.isOpenNoviembre=true; break;
					case 1012: mi.planificado.isOpenDiciembre=true; break;
					default: meta.isOpen = true; break;
				}
			};

			mi.fechaOptions = {
					formatYear : 'yy',
					maxDate : new Date(2050, 12, 31),
					minDate : new Date(1990, 1, 1),
					startingDay : 1
			};
			
			mi.guardarFechaMetaFinal = function(row){
				row.metaFinalTiempo = moment(row.fechaControl).format('DD/MM/YYYY');
			}
			
			mi.almacenarPlanificado = function(mes){
				var fila = null;
				for(p in mi.meta.planificado){
					var metaplan = mi.meta.planificado[p]; 
					if(metaplan.ejercicio == mi.anio){
						fila = metaplan;
						break;
					}
				}
				if(fila==null){
					fila={
							ejercicio: mi.anio,
							eneroString: null,
							febreroString: null,
							marzoString: null,
							abrilString: null,
							mayoString: null,
							junioString: null,
							julioString: null,
							agostoString: null,
							septiembreString: null,
							octubreString: null,
							noviembreString: null,
							diciembreString: null,
							eneroEntero: null,
							febreroEntero: null,
							marzoEntero: null,
							abrilEntero: null,
							mayoEntero: null,
							junioEntero: null,
							julioEntero: null,
							agostoEntero: null,
							septiembreEntero: null,
							octubreEntero: null,
							noviembreEntero: null,
							diciembreEntero: null,
							eneroDecimal: null,
							febreroDecimal: null,
							marzoDecimal: null,
							abrilDecimal: null,
							mayoDecimal: null,
							junioDecimal: null,
							julioDecimal: null,
							agostoDecimal: null,
							septiembreDecimal: null,
							octubreDecimal: null,
							noviembreDecimal: null,
							diciembreDecimal: null,
							eneroTiempo: null,
							febreroTiempo: null,
							marzoTiempo: null,
							abrilTiempo: null,
							mayoTiempo: null,
							junioTiempo: null,
							julioTiempo: null,
							agostoTiempo: null,
							septiembreTiempo: null,
							octubreTiempo: null,
							noviembreTiempo: null,
							diciembreTiempo: null
					}
					mi.meta.planificado.push(fila);
				}
				if(mi.meta.datoTipoId.id == 5){
					fila[mes]=moment(mi.planificado[mes]).format('DD/MM/YYYY');
				}else{
					fila[mes]=mi.planificado[mes];
				}
				mi.totalPlanificado();
			}
			
			mi.inicializarPlanificadoReal = function(meta){
				var inicial="";
				if(meta!=null && meta!=undefined && (meta.datoTipoId.id == 2 || meta.datoTipoId.id == 3)){
					inicial = 0;
				}
				mi.planificado = {
						eneroString: inicial,
						febreroString: inicial,
						marzoString: inicial,
						abrilString: inicial,
						mayoString: inicial,
						junioString: inicial,
						julioString: inicial,
						agostoString: inicial,
						septiembreString: inicial,
						octubreString: inicial,
						noviembreString: inicial,
						diciembreString: inicial,
						eneroEntero: inicial,
						febreroEntero: inicial,
						marzoEntero: inicial,
						abrilEntero: inicial,
						mayoEntero: inicial,
						junioEntero: inicial,
						julioEntero: inicial,
						agostoEntero: inicial,
						septiembreEntero: inicial,
						octubreEntero: inicial,
						noviembreEntero: inicial,
						diciembreEntero: inicial,
						eneroDecimal: inicial,
						febreroDecimal: inicial,
						marzoDecimal: inicial,
						abrilDecimal: inicial,
						mayoDecimal: inicial,
						junioDecimal: inicial,
						julioDecimal: inicial,
						agostoDecimal: inicial,
						septiembreDecimal: inicial,
						octubreDecimal: inicial,
						noviembreDecimal: inicial,
						diciembreDecimal: inicial,
						eneroTiempo: inicial,
						febreroTiempo: inicial,
						marzoTiempo: inicial,
						abrilTiempo: inicial,
						mayoTiempo: inicial,
						junioTiempo: inicial,
						julioTiempo: inicial,
						agostoTiempo: inicial,
						septiembreTiempo: inicial,
						octubreTiempo: inicial,
						noviembreTiempo: inicial,
						diciembreTiempo: inicial,
						total: inicial
				}
				mi.real = {
						enero: inicial,
						febrero: inicial,
						marzo: inicial,
						abril: inicial,
						mayo: inicial,
						junio: inicial,
						julio: inicial,
						agosto: inicial,
						septiembre: inicial,
						octubre: inicial,
						noviembre: inicial,
						diciembre: inicial,
						total: inicial
				}
			}
						
			mi.getMetasAnio = function(meta, anio){
				if(meta!=null && meta!=undefined){
					mi.inicializarPlanificadoReal(meta);
					var inicial="";
					if(meta.datoTipoId.id == 2){
						inicial = 0;
						for(i=0; i<meta.avance.length; i++){
							var fecha = moment(meta.avance[i].fecha, 'DD/MM/YYYY');
							var mesA = fecha.month();
							var anioA = fecha.year();					
							if(anioA == anio){
								switch(mesA){
									case 0: mi.real.enero += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 1: mi.real.febrero += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
									break;
									case 2: mi.real.marzo += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 3: mi.real.abril += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 4: mi.real.mayo += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 5: mi.real.junio += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 6: mi.real.julio += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 7: mi.real.agosto += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 8: mi.real.septiembre += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 9: mi.real.octubre += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 10: mi.real.noviembre += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
										break;
									case 11: mi.real.diciembre += meta.avance[i].valorEntero!=null ? parseInt(meta.avance[i].valorEntero) : 0;
								}
								mi.real.total = parseInt(mi.real.enero)+parseInt(mi.real.febrero)+parseInt(mi.real.marzo)+parseInt(mi.real.abril)+parseInt(mi.real.mayo)+parseInt(mi.real.junio)+parseInt(mi.real.julio)+parseInt(mi.real.agosto)+parseInt(mi.real.septiembre)+parseInt(mi.real.octubre)+parseInt(mi.real.noviembre)+parseInt(mi.real.diciembre);
							}
						}
					}else if(meta.datoTipoId.id == 3){
						inicial = 0;
						for(i=0; i<meta.avance.length; i++){
							var fecha = moment(meta.avance[i].fecha, 'DD/MM/YYYY');
							var mesA = fecha.month();
							var anioA = fecha.year();					
							if(anioA == anio){
								switch(mesA){
								case 0: mi.real.enero += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 1: mi.real.febrero += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 2: mi.real.marzo += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 3: mi.real.abril += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 4: mi.real.mayo += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 5: mi.real.junio += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 6: mi.real.julio += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 7: mi.real.agosto += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 8: mi.real.septiembre += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 9: mi.real.octubre += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 10: mi.real.noviembre += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								break;
							case 11: mi.real.diciembre += meta.avance[i].valorDecimal!=null ? parseFloat(meta.avance[i].valorDecimal) : parseFloat(0);
								}
								mi.real.total = (parseFloat(mi.real.enero)+parseFloat(mi.real.febrero)+parseFloat(mi.real.marzo)+parseFloat(mi.real.abril)+parseFloat(mi.real.mayo)+parseFloat(mi.real.junio)+parseFloat(mi.real.julio)+parseFloat(mi.real.agosto)+parseFloat(mi.real.septiembre)+parseFloat(mi.real.octubre)+parseFloat(mi.real.noviembre)+parseFloat(mi.real.diciembre)).toFixed(2);
							}
						}
					}
					for(i=0; i<meta.planificado.length; i++){
						if(meta.planificado[i].ejercicio == anio){
							mi.planificadoActual = meta.planificado[i]; 
							mi.planificado = {
								eneroString: meta.planificado[i].eneroString != null ? meta.planificado[i].eneroString : inicial,
								febreroString: meta.planificado[i].febreroString != null ? meta.planificado[i].febreroString : inicial,
								marzoString: meta.planificado[i].marzoString != null ? meta.planificado[i].marzoString : inicial,
								abrilString: meta.planificado[i].abrilString != null ? meta.planificado[i].abrilString : inicial,
								mayoString: meta.planificado[i].mayoString != null ? meta.planificado[i].mayoString : inicial,
								junioString: meta.planificado[i].junioString != null ? meta.planificado[i].junioString : inicial,
								julioString: meta.planificado[i].julioString != null ? meta.planificado[i].julioString : inicial,
								agostoString: meta.planificado[i].agostoString != null ? meta.planificado[i].agostoString : inicial,
								septiembreString: meta.planificado[i].septiembreString != null ? meta.planificado[i].septiembreString : inicial,
								octubreString: meta.planificado[i].octubreString != null ? meta.planificado[i].octubreString : inicial,
								noviembreString: meta.planificado[i].noviembreString != null ? meta.planificado[i].noviembreString : inicial,
								diciembreString: meta.planificado[i].diciembreString != null ? meta.planificado[i].diciembreString : inicial,
								eneroEntero: meta.planificado[i].eneroEntero != null ? meta.planificado[i].eneroEntero : inicial,
								febreroEntero: meta.planificado[i].febreroEntero != null ? meta.planificado[i].febreroEntero : inicial,
								marzoEntero: meta.planificado[i].marzoEntero != null ? meta.planificado[i].marzoEntero : inicial,
								abrilEntero: meta.planificado[i].abrilEntero != null ? meta.planificado[i].abrilEntero : inicial,
								mayoEntero: meta.planificado[i].mayoEntero != null ? meta.planificado[i].mayoEntero : inicial,
								junioEntero: meta.planificado[i].junioEntero != null ? meta.planificado[i].junioEntero : inicial,
								julioEntero: meta.planificado[i].julioEntero != null ? meta.planificado[i].julioEntero : inicial,
								agostoEntero: meta.planificado[i].agostoEntero != null ? meta.planificado[i].agostoEntero : inicial,
								septiembreEntero: meta.planificado[i].septiembreEntero != null ? meta.planificado[i].septiembreEntero : inicial,
								octubreEntero: meta.planificado[i].octubreEntero != null ? meta.planificado[i].octubreEntero : inicial,
								noviembreEntero: meta.planificado[i].noviembreEntero != null ? meta.planificado[i].noviembreEntero : inicial,
								diciembreEntero: meta.planificado[i].diciembreEntero != null ? meta.planificado[i].diciembreEntero : inicial,
								eneroDecimal: meta.planificado[i].eneroDecimal != null ? meta.planificado[i].eneroDecimal : inicial,
								febreroDecimal: meta.planificado[i].febreroDecimal != null ? meta.planificado[i].febreroDecimal : inicial,
								marzoDecimal: meta.planificado[i].marzoDecimal != null ? meta.planificado[i].marzoDecimal : inicial,
								abrilDecimal: meta.planificado[i].abrilDecimal != null ? meta.planificado[i].abrilDecimal : inicial,
								mayoDecimal: meta.planificado[i].mayoDecimal != null ? meta.planificado[i].mayoDecimal : inicial,
								junioDecimal: meta.planificado[i].junioDecimal != null ? meta.planificado[i].junioDecimal : inicial,
								julioDecimal: meta.planificado[i].julioDecimal != null ? meta.planificado[i].julioDecimal : inicial,
								agostoDecimal: meta.planificado[i].agostoDecimal != null ? meta.planificado[i].agostoDecimal : inicial,
								septiembreDecimal: meta.planificado[i].septiembreDecimal != null ? meta.planificado[i].septiembreDecimal : inicial,
								octubreDecimal: meta.planificado[i].octubreDecimal != null ? meta.planificado[i].octubreDecimal : inicial,
								noviembreDecimal: meta.planificado[i].noviembreDecimal != null ? meta.planificado[i].noviembreDecimal : inicial,
								diciembreDecimal: meta.planificado[i].diciembreDecimal != null ? meta.planificado[i].diciembreDecimal : inicial,
								eneroTiempo: (meta.planificado[i].eneroTiempo != null && meta.planificado[i].eneroTiempo != "") ? moment(meta.planificado[i].eneroTiempo,'DD/MM/YYYY').toDate() : null,
								febreroTiempo: (meta.planificado[i].febreroTiempo != null && meta.planificado[i].febreroTiempo != "") ? moment(meta.planificado[i].febreroTiempo,'DD/MM/YYYY').toDate() : null,
								marzoTiempo: (meta.planificado[i].marzoTiempo != null && meta.planificado[i].marzoTiempo != "") ? moment(meta.planificado[i].marzoTiempo,'DD/MM/YYYY').toDate() : null,
								abrilTiempo: (meta.planificado[i].abrilTiempo != null && meta.planificado[i].abrilTiempo != "")? moment(meta.planificado[i].abrilTiempo,'DD/MM/YYYY').toDate() : null,
								mayoTiempo: (meta.planificado[i].mayoTiempo != null && meta.planificado[i].mayoTiempo != "") ? moment(meta.planificado[i].mayoTiempo,'DD/MM/YYYY').toDate() : null,
								junioTiempo: (meta.planificado[i].junioTiempo != null && meta.planificado[i].junioTiempo != "") ? moment(meta.planificado[i].junioTiempo,'DD/MM/YYYY').toDate() : null,
								julioTiempo: (meta.planificado[i].julioTiempo != null && meta.planificado[i].julioTiempo != "") ? moment(meta.planificado[i].julioTiempo,'DD/MM/YYYY').toDate() : null,
								agostoTiempo: (meta.planificado[i].agostoTiempo != null && meta.planificado[i].agostoTiempo != "") ? moment(meta.planificado[i].agostoTiempo,'DD/MM/YYYY').toDate() : null,
								septiembreTiempo: (meta.planificado[i].septiembreTiempo != null && meta.planificado[i].septiembreTiempo != "") ? moment(meta.planificado[i].septiembreTiempo,'DD/MM/YYYY').toDate() : null,
								octubreTiempo: (meta.planificado[i].octubreTiempo != null && meta.planificado[i].octubreTiempo != "") ? moment(meta.planificado[i].octubreTiempo,'DD/MM/YYYY').toDate() : null,
								noviembreTiempo: (meta.planificado[i].noviembreTiempo != null && meta.planificado[i].noviembreTiempo != "") ? moment(meta.planificado[i].noviembreTiempo,'DD/MM/YYYY').toDate() : null,
								diciembreTiempo: (meta.planificado[i].diciembreTiempo != null && meta.planificado[i].diciembreTiempo != "") ? moment(meta.planificado[i].diciembreTiempo,'DD/MM/YYYY').toDate() : null,
								total: inicial
							}
							if(mi.meta.datoTipoId!=null && mi.meta.datoTipoId.id==4){
								mi.planificado.eneroString= 'true' == meta.planificado[i].eneroString;
								mi.planificado.febreroString= 'true' == meta.planificado[i].febreroString;
								mi.planificado.marzoString= 'true' == meta.planificado[i].marzoString;
								mi.planificado.abrilString= 'true' == meta.planificado[i].abrilString;
								mi.planificado.mayoString= 'true' == meta.planificado[i].mayoString;
								mi.planificado.junioString= 'true' == meta.planificado[i].junioString;
								mi.planificado.julioString= 'true' == meta.planificado[i].julioString;
								mi.planificado.agostoString= 'true' == meta.planificado[i].agostoString;
								mi.planificado.septiembreString= 'true' == meta.planificado[i].septiembreString;
								mi.planificado.octubreString= 'true' == meta.planificado[i].octubreString;
								mi.planificado.noviembreString= 'true' == meta.planificado[i].noviembreString;
								mi.planificado.diciembreString= 'true' == meta.planificado[i].diciembreString;
							}
							break;
						}
					}
					mi.totalPlanificado();
				}
			}
			
			mi.totalPlanificado = function(){
				if(mi.meta.datoTipoId.id == 2){
					mi.planificado.total = mi.planificado.eneroEntero+mi.planificado.febreroEntero+mi.planificado.marzoEntero+mi.planificado.abrilEntero+mi.planificado.mayoEntero+mi.planificado.junioEntero+mi.planificado.julioEntero+mi.planificado.agostoEntero+mi.planificado.septiembreEntero+mi.planificado.octubreEntero+mi.planificado.noviembreEntero+mi.planificado.diciembreEntero;
				}else if(mi.meta.datoTipoId.id == 3){
					mi.planificado.total = parseFloat(mi.planificado.eneroDecimal+mi.planificado.febreroDecimal+mi.planificado.marzoDecimal+mi.planificado.abrilDecimal+mi.planificado.mayoDecimal+mi.planificado.junioDecimal+mi.planificado.julioDecimal+mi.planificado.agostoDecimal+mi.planificado.septiembreDecimal+mi.planificado.octubreDecimal+mi.planificado.noviembreDecimal+mi.planificado.diciembreDecimal).toFixed(2);
				}
			}
						
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}			
			
			mi.guardar = function(child_scope,call_chain, mensaje, mensaje_error){	
				if(mi.metas!=null && mi.metas.length>0){
					if(mi.objeto_id===null || mi.objeto_id==undefined){
						mi.obtenerDatosPadre();
					}
					var metasArreglo = mi.metas.concat(mi.metasBorradas);
					
					var metasJson = JSON.stringify(metasArreglo);
					
					$http.post('/SMeta', {
						accion: 'guardarMetasCompletas',
						metas: metasJson,
						objeto_id: mi.objeto_id,
						objeto_tipo: mi.objeto_tipo,
						t: (new Date()).getTime()
					}).success(function(response){
						if(response.success){
							if(child_scope!=null)
								child_scope(call_chain,mensaje, mensaje_error);
							else{
								$utilidades.mensaje('success',mensaje);
								mi.inicializarControlador();
							}
						}
						else
							$utilidades.mensaje('danger',mensaje_error);
					});
				}
				else{
					if(child_scope!=null)
						child_scope(call_chain,mensaje, mensaje_error);
					else{
						$utilidades.mensaje('success',mensaje);
					}
				}
			}
			
			mi.nuevaMeta = function() {
				mi.metas.push({  
			         "id":null,
			         "nombre": null,
			         "descripcion":"",
			         "estado":1,
			         "unidadMedidaId": null,
			         "usuarioCreo":"",
			         "fechaCreacion":"",
			         "usuarioActualizo":null,
			         "fechaActualizacion":"",
			         "objetoId":mi.objeto_id,
			         "objetoTipo":mi.objeto_tipo,
			         "datoTipoId":mi.datoTipos[1],
			         "metaFinalString":null,
			         "metaFinalEntero":null,
			         "metaFinalDecimal":null,
			         "metaFinalTiempo":null,
			         "fechaControl":null,
			         "planificado":[  
	
			         ],
			         "avance":[  
	
			         ]
				});
			};

			mi.borrarMeta = function(meta) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar '+mi.titulo+' "'+ (meta.nombre != null ? meta.nombre : '') +'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							if(meta.id!=null){
								meta.estado=0;
								mi.metasBorradas.push(meta);
							}
							var index = mi.metas.indexOf(meta);
							if (index > -1) {
								mi.metas.splice(index, 1);
								mi.mostrarValores = false;
							}
						}
					}, function(){
						
					});
			};
			
			mi.agregarAvances = function() {
				var modalInstance = $uibModal.open({
					animation : 'true',
					ariaLabelledBy : 'modal-title',
					ariaDescribedBy : 'modal-body',
					templateUrl : 'metaAvance.jsp',
					controller : 'modalMetaAvances',
					controllerAs : 'modalAvances',
					backdrop : 'static',
					size : 'md',
					resolve: {
						titulo: function(){
							return mi.titulo;
						},
					    avance: function(){
					    	return mi.meta.avance;
					    },
					    nombreMeta: function(){
					    	return mi.meta.nombre;
					    },
					    datoTipo: function(){
					    	return mi.meta.datoTipoId.nombre;
					    },
					    anio: function(){
					    	return mi.anio;
					    },
					    fechaInicio: function(){
					    	return mi.fechaInicio;
					    }
					  }
				});

				modalInstance.result.then(function() {
					mi.getMetasAnio(mi.meta, mi.anio);
				}, function() {
				});

			};

		}
	]);


app.controller('modalMetaAvances', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log','dialogoConfirmacion', 'titulo', 'avance', 'nombreMeta', 'datoTipo', 'anio', 'fechaInicio',
	function ($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log,$dialogoConfirmacion, titulo, avance, nombreMeta, datoTipo, anio, fechaInicio) {
	
		var mi = this;
		
		mi.titulo = titulo;
		mi.avance = avance;
		mi.nombreMeta = nombreMeta;
		mi.datoTipo = datoTipo;
		mi.anio = anio;
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
				
		mi.abrirPopupFecha = function(index, tipo) {
			if(tipo==0){
				mi.avance[index].isOpen = true;
			}else{
				mi.avance[index].isOpenValor = true;
			}
			
		};

		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(),
				minDate : fechaInicio,
				startingDay : 1
		};
		
		mi.guardarFecha = function(row){
			row.fecha = moment(row.fechaControl).format('DD/MM/YYYY');
			row.valorTiempo = row.valorTiempoControl!=null ? moment(row.valorTiempoControl).format('DD/MM/YYYY') : null;
		}
		
		mi.validarFecha = function(row){
			if (row.fechaControl && (row.fechaControl.getTime()<fechaInicio.getTime() || row.fechaControl.getTime()>new Date())){
				return true;
			}
			return false;
		}
				
		mi.nuevoAvance = function(){
			mi.avance.push({  
	               "fecha": moment().format('DD/MM/YYYY'),
	               "fechaControl": new Date(),
	               "valorString":null,
	               "valorEntero":null,
	               "valorDecimal":null,
	               "valorTiempo":null,
	               "usuario":null
	            });
		}
		
		mi.borrarAvance = function(row){
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el avance con fecha '+row.fecha+'?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					var index = mi.avance.indexOf(row);
					if (index > -1) {
						mi.avance.splice(index, 1);
					}
				}
			}, function(){
				
			});
		}
		
		mi.ok = function() {
			$uibModalInstance.close();
		};
	
	}
]);