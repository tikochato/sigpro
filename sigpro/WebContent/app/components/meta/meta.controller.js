//var app = angular.module('metaController', []);

app.controller('metaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog', '$uibModal', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog, $uibModal, $dialogoConfirmacion) {
			var mi=this;
			
			$window.document.title = $utilidades.sistema_nombre+' - Metas';
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.metas = [];
			mi.meta;
			mi.esnueva = false;
			mi.mostrarValores = false;
			mi.metasunidades = [];
			mi.tipoMetaSeleccionado=null;
			mi.unidadMedidaSeleccionado=null;
			mi.tipoValorSeleccionado=null;
			
			if($scope.$parent.controller){
				mi.objeto_id = $scope.$parent.controller.proyecto.id;
				mi.objeto_tipo = 1;
			}else if($scope.$parent.componentec){
				mi.objeto_id = $scope.$parent.componentec.componente.id;
				mi.objeto_tipo = 2;
			}else if($scope.$parent.producto){
				mi.objeto_id = $scope.$parent.producto.producto.id;
				mi.objeto_tipo = 3;
			}else if($scope.$parent.subproducto){
				mi.objeto_id = $scope.$parent.subproducto.subproducto.id;
				mi.objeto_tipo = 4;
			}else if($scope.$parent.actividadc){
				mi.objeto_id = $scope.$parent.actividadc.actividad.id;
				mi.objeto_tipo = 5;
			}
						
			mi.nombrePcp = "";
			mi.nombreTipoPcp = "";
			
			mi.anios=[];
			mi.anio = null;			
			mi.planificado = null;
			mi.real = null;
			mi.planificadoActual = null;
			
			switch(mi.objeto_tipo){
				case "1": mi.nombreTipoPcp = "Préstamo"; break;
				case "2": mi.nombreTipoPcp = "Componente"; break;
				case "3": mi.nombreTipoPcp = "Producto"; break;
				case "4": mi.nombreTipoPcp = "Subproducto"; break;
				
			}
			
			$http.post('/SMeta', { accion: 'getPcp', id: mi.objeto_id, tipo: mi.objeto_tipo, t: (new Date()).getTime()}).success(
					function(response) {
						mi.nombrePcp = response.nombre;
						mi.fechaInicio = moment(response.fechaInicio, 'DD/MM/YYYY').toDate();
						mi.fechaFin = moment(response.fechaFin, 'DD/MM/YYYY').toDate();
						var anioInicio = moment(response.fechaInicio, 'DD/MM/YYYY').year();
						var anioFin = moment(response.fechaFin, 'DD/MM/YYYY').year();
						for(a = anioInicio; a<=anioFin; a++){
							mi.anios.push(a);
						}
						if(mi.anios.length>0){
							mi.anio=mi.anios[0];
						}
			});
						
			$http.post('/SMeta', { accion: 'getMetasUnidadesMedida', t: (new Date()).getTime() }).success(
					function(response) {
						mi.metaunidades = response.MetasUnidades;
						$http.post('/SDatoTipo', { accion: 'cargarCombo', t: (new Date()).getTime() }).success(
								function(response) {
									mi.datoTipos = response.datoTipos;
									mi.cargarTabla();
						});
			});
			
			
			mi.cargarTabla = function(){
				$http.post('/SMeta', { accion: 'getMetasCompletas', 
					id:mi.objeto_id, tipo: mi.objeto_tipo,
					t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.metas = response.Metas;
						for(x in mi.metas){
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
								mi.metas[x].avance[a].fecha = moment(mi.metas[x].avance[a].fecha,'DD/MM/YYYY').toDate(); 
							}
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
			
			mi.almacenarPlanificado = function(mes){
				for(p in mi.meta.planificado){
					var metaplan = mi.meta.planificado[p]; 
					if(metaplan.ejercicio == mi.anio){
						metaplan[mes] = mi.planificado[mes];
						break;
					}
				}
				mi.meta.planificado.push({"ejercicio":mi.anio, mes:mi.planificado[mes]});
			}
			
			mi.inicializarPlanificadoReal = function(meta){
				var inicial="";
				if(meta.datoTipoId.id == 2 || meta.datoTipoId.id == 3){
					inicial = 0;
				}
				mi.planificado = {
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
				mi.inicializarPlanificadoReal(meta);
				var inicial="";
				if(meta.datoTipoId.id == 2 || meta.datoTipoId.id == 3){
					inicial = 0;
					for(i=0; i<meta.avance.length; i++){
						var fecha = moment(meta.avance[i].fecha).format('DD/MM/YYYY');
						fecha = moment(fecha, 'DD/MM/YYYY');
						var mesA = fecha.month();
						var anioA = fecha.year();					
						if(anioA == anio){
							switch(mesA){
								case 0: mi.real.enero += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 1: mi.real.febrero += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 2: mi.real.marzo += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 3: mi.real.abril += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 4: mi.real.mayo += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 5: mi.real.junio += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 6: mi.real.julio += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 7: mi.real.agosto += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 8: mi.real.septiembre += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 9: mi.real.octubre += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 10: mi.real.noviembre += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
									break;
								case 11: mi.real.diciembre += meta.avance[i].valor!=null ? meta.avance[i].valor : 0;
							}
						}
					}
				}
				for(i=0; i<meta.planificado.length; i++){
					if(meta.planificado[i].ejercicio == anio){
						mi.planificadoActual = meta.planificado[i]; 
						mi.planificado = {
							enero: meta.planificado[i].enero != null ? meta.planificado[i].enero : inicial,
							febrero: meta.planificado[i].febrero != null ? meta.planificado[i].febrero : inicial,
							marzo: meta.planificado[i].marzo != null ? meta.planificado[i].marzo : inicial,
							abril: meta.planificado[i].abril != null ? meta.planificado[i].abril : inicial,
							mayo: meta.planificado[i].mayo != null ? meta.planificado[i].mayo : inicial,
							junio: meta.planificado[i].junio != null ? meta.planificado[i].junio : inicial,
							julio: meta.planificado[i].julio != null ? meta.planificado[i].julio : inicial,
							agosto: meta.planificado[i].agosto != null ? meta.planificado[i].agosto : inicial,
							septiembre: meta.planificado[i].septiembre != null ? meta.planificado[i].septiembre : inicial,
							octubre: meta.planificado[i].octubre != null ? meta.planificado[i].octubre : inicial,
							noviembre: meta.planificado[i].noviembre != null ? meta.planificado[i].noviembre : inicial,
							diciembre: meta.planificado[i].diciembre != null ? meta.planificado[i].diciembre : inicial,
							total: mi.planificado.enero+mi.planificado.febrero+mi.planificado.marzo+mi.planificado.abril+mi.planificado.mayo
								+mi.planificado.junio+mi.planificado.julio+mi.planificado.agosto+mi.planificado.septiembre+mi.planificado.octubre
								+mi.planificado.noviembre+mi.planificado.diciembre
						}
						break;
					}
				}
			}
			
						
			mi.redireccionSinPermisos=function(){
				$window.location.href = '/main.jsp#!/forbidden';		
			}			
			
			mi.guardar=function(){
				if(mi.meta!=null && mi.tipoMetaSeleccionado!=null && mi.unidadMedidaSeleccionado!=null  ){
					$http.post('/SMeta', {
						accion: 'guardarMeta',
						esnueva: mi.esnueva,
						id: mi.meta.id,
						nombre: mi.meta.nombre,
						descripcion: mi.meta.descripcion,
						tipoMetaId:  mi.tipoMetaSeleccionado.id,
						unidadMedidaId: mi.unidadMedidaSeleccionado.id,
						datoTipoId: mi.tipoValorSeleccionado.id,
						objetoTipo:  mi.objeto_tipo,
						objetoId:$routeParams.id,
						t: (new Date()).getTime()						
					}).success(function(response){
						if(response.success){
							mi.meta.id = response.id;
							mi.meta.datoTipoId = response.datoTipoId;
							mi.meta.usuarioCreo = response.usuarioCreo;
							mi.meta.fechaCreacion = response.fechaCreacion;
							mi.meta.usuarioActualizo = response.usuarioactualizo;
							mi.meta.fechaActualizacion = response.fechaactualizacion;
							$utilidades.mensaje('success','Meta '+(mi.esnueva ? 'creada' : 'guardada')+' con éxito');
							mi.esnueva = false;
							mi.obtenerTotalMetas();
						}
						else
							$utilidades.mensaje('danger','Error al '+(mi.esnueva ? 'crear' : 'guardar')+' la Meta');
					});
				}
				else
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
			};

			mi.nuevaMeta = function() {
				mi.metas.push({  
			         "id":"",
			         "nombre":"Nueva Meta",
			         "descripcion":"",
			         "estado":1,
			         "unidadMedidaId":0,
			         "usuarioCreo":"",
			         "fechaCreacion":"",
			         "usuarioActualizo":null,
			         "fechaActualizacion":"",
			         "objetoId":mi.objeto_id,
			         "objetoTipo":mi.objeto_tipo,
			         "datoTipoId":mi.datoTipos[2],
			         "metaFinal":null,
			         "planificado":[  
	
			         ],
			         "avance":[  
	
			         ]
				});
			};

			mi.borrarMeta = function(meta) {
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Meta "'+meta.id+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
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
					    avance: function(){
					    	return mi.meta.avance;
					    },
					    nombreMeta: function(){
					    	return mi.meta.nombre;
					    },
					    anio: function(){
					    	return mi.anio;
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
	'$timeout', '$log','dialogoConfirmacion', 'avance', 'nombreMeta', 'anio',
	function ($uibModalInstance, $scope, $http, $interval,
		i18nService, $utilidades, $timeout, $log,$dialogoConfirmacion, avance, nombreMeta, anio) {
	
		var mi = this;
		
		mi.avance = avance;
		mi.nombreMeta = nombreMeta;
		mi.anio = anio;
		mi.formatofecha = 'dd/MM/yyyy';
		
		//*****************Manejo de fechas ??????
		
		mi.abrirPopupFecha = function(index) {
			mi.avance[index].isOpen = true;
		};

		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2050, 12, 31),
				minDate : new Date(1990, 1, 1),
				startingDay : 1
		};
				
		mi.nuevoAvance = function(){
			mi.avance.push({  
	               "fecha": new Date(),
	               "valor":null,
	               "usuario":"admin"
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