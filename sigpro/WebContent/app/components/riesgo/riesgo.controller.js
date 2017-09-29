var app = angular.module('riesgoController', ['smart-table']);

app.controller('riesgoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
		var mi=this;
		
		mi.mostrarcargando=true;
		mi.riesgos = [];
		mi.riesgo;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.riesgoTipoid = "";
		mi.riesgoTipoNombre="";
		mi.componenteid = "";
		mi.componenteNombre="";
		mi.productoid="";
		mi.productoNombre="";
		mi.colaboradorid = ""
		mi.colaboradorNombre = "";
		mi.camposdinamicos = {};
		mi.formatofecha = 'dd/MM/yyyy';
		mi.colaboradorid="";
		mi.colaboradorNombre="";
		mi.proyectoid = "";
		mi.objetoid = $routeParams.objeto_id;
		mi.objetotipo = $routeParams.objeto_tipo;
		mi.objetoNombre = "";
		mi.objetoTipoNombre="";
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		mi.filtros = [];
		mi.orden = null;
		mi.probabilidades = [{valor:1, nombre:"Bajo"},{valor:2,nombre:"Medio"},{valor:3,nombre:"Alto"}];
		
		mi.fechaOptions = {
				formatYear : 'yy',
				maxDate : new Date(2020, 5, 22),
				minDate : new Date(1900, 1, 1),
				startingDay : 1
		};
		
		$http.post('/SObjeto', { accion: 'getObjetoPorId', id: $routeParams.objeto_id, tipo: mi.objetotipo, t: (new Date()).getTime()}).success(
				function(response) {
					mi.objetoid = response.id;
					mi.objetoNombre = response.nombre;
					mi.objetoTipoNombre = response.tiponombre;
		});
		
		mi.editarElemento = function (event) {
	        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
	        mi.editar();
	    };
	    
		mi.cargarTabla = function(pagina){
			mi.mostrarcargando=true;
			$http.post('/SRiesgo', { accion: 'getRiesgosPorObjeto',  
				objetoid:$routeParams.objeto_id,objetotipo:$routeParams.objeto_tipo, t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.riesgos = response.riesgos;
						mi.mostrarcargando = false;
						for(var i=0; i<mi.riesgos.length; i++)
							mi.riesgos[i].fechaEjecucion = moment(mi.riesgos[i].fechaEjecucion,'DD/MM/YYYY').toDate();
					});
		}
		
		mi.cargarTabla();
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		mi.guardar=function(){
			
			for (campos in mi.camposdinamicos) {
				if (mi.camposdinamicos[campos].tipo === 'fecha') {
					mi.camposdinamicos[campos].valor_f = moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY')
				}
			}
			
			if(mi.riesgo!=null && mi.riesgo.nombre!='' && mi.riesgoTipoid!='' && mi.probabilidad!=null){
				$http.post('/SRiesgo', {
					accion: 'guardarRiesgo',
					esnuevo: mi.esnuevo,
					id: mi.riesgo.id,
					objetoid: mi.objetoid,
					objetotipo: mi.objetotipo,
					riesgotipoid : mi.riesgoTipoid,
					nombre: mi.riesgo.nombre,
					descripcion: mi.riesgo.descripcion,
					objetoId: $routeParams.objeto_id,
					impacto: mi.riesgo.impacto,
					puntuacionimpoacto: mi.riesgo.puntuacionImpacto,
					impactoproyectado: mi.riesgo.impactoProyectado,
					probabilidad: mi.probabilidad!=null ? mi.probabilidad.valor : null,
					gatillossintomas: mi.riesgo.gatillosSintomas,
					respuesta: mi.riesgo.respuesta,
					colaboradorid: mi.colaboradorid,
					riesgossecundarios: mi.riesgo.riesgosSecundarios,
					ejecutado: mi.ejecutado == true ? 1 : 0 ,
					fechaejecucion: moment(mi.riesgo.fechaEjecucion).format('DD/MM/YYYY'),
					objetoTipo:  $routeParams.objeto_tipo,
					datadinamica : JSON.stringify(mi.camposdinamicos),
					t: (new Date()).getTime()
				}).success(function(response){
					if(response.success){
						mi.esnuevo = false;
						mi.riesgo.id = response.id;
						$utilidades.mensaje('success','Riesgo '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.riesgo.usuarioCreo = response.usuarioCreo;
						mi.riesgo.fechaCreacion = response.fechaCreacion;
						mi.riesgo.usuarioActualizo = response.usuarioactualizo;
						mi.riesgo.fechaActualizacion = response.fechaactualizacion;
						mi.cargarTabla();
					}
					else
						$utilidades.mensaje('danger','Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Riesgo');
				});
			}
			else
				$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
		};

		mi.borrar = function(ev) {
			if(mi.riesgo!=null && mi.riesgo.id!=null){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar el Riesgo "'+mi.riesgo.nombre+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SRiesgo', {
							accion: 'borrarRiesgo',
							id: mi.riesgo.id,
							t: (new Date()).getTime()
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Riesgo borrado con éxito');
								mi.riesgo = null;
								mi.cargarTabla();
							}
							else
								$utilidades.mensaje('danger','Error al borrar el Riesgo');
						});
					}
				}, function(){
					
				});
			}
			else
				$utilidades.mensaje('warning','Debe seleccionar el Riesgo que desea borrar');
		};

		mi.nuevo = function() {
			mi.mostraringreso=true;
			mi.esnuevo = true;
			mi.colaboradorNombre="";
			mi.colaboradorid="";
			mi.riesgo = {};
			mi.riesgoTipoid = "";
			mi.riesgoTipoNombre="";
			mi.componenteid = "";
			mi.componenteNombre="";
			mi.productoid="";
			mi.productoNombre="";
			mi.camposdinamicos = {};
			mi.probabilidad=null; 
			mi.riesgo.fechaEjecucion=null;
			$utilidades.setFocus(document.getElementById("nombre"));
		};

		mi.editar = function(row) {
			mi.riesgo=row;
			if(mi.riesgo!=null){
				mi.riesgoTipoid = mi.riesgo.riesgotipoid;
				mi.riesgo.fechaEjecucion = mi.riesgo.fechaEjecucion;
				mi.riesgoTipoNombre = mi.riesgo.riesgotiponombre;
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.colaboradorNombre = mi.riesgo.colaboradorNombre;
				mi.colaboradorId = mi.riesgo.colaboradorId;
				$utilidades.setFocus(document.getElementById("nombre"));
				if (mi.riesgo.probabilidad !=null && mi.riesgo.probabilidad > 0){
					mi.probabilidad = {
							"valor" : mi.riesgo.probabilidad,
							"nombre" : mi.probabilidades[mi.riesgo.probabilidad - 1].nombre
					}
				}else {
					mi.probabilidad = {};
				}
				mi.ejecutado = mi.riesgo.ejecutado == 1;
				$utilidades.setFocus(document.getElementById("nombre"));
				var parametros = { 
						accion: 'getRiesgoPropiedadPorTipo', 
						idRiesgo: mi.riesgo.id,
						idRiesgoTipo: mi.riesgoTipoid,
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
				
			}
		}

		mi.irATabla = function() {
			if(mi.esnuevo){
				mi.riesgos.push({
					colaboradorNombre: mi.colaboradorNombre,
					colaboradorid: mi.colaboradorid,
					descripcion: mi.riesgo.descripcion,
					ejecutado: mi.ejecutado ? 1 : 0,
					estado: 1,
					fechaActualizacion: null,
					fechaCreacion: null,
					fechaEjecucion:  mi.riesgo.fechaEjecucion,
					gatillosSintomas: mi.riesgo.gatillosSintomas,
					id: mi.riesgo.id,
					impacto: mi.riesgo.impacto,
					impactoProyectado: mi.riesgo.impactoProyectado,
					nombre: mi.riesgo.nombre,
					probabilidad: mi.riesgo.probabilidad,
					puntuacionImpacto: mi.riesgo.probabilidadImpacto,
					respuesta: mi.riesgo.respuesta,
					riesgosSecundarios: mi.riesgo.riesgosSecundarios,
					riesgotipoid: mi.riesgoTipoid,
					riesgotiponombre: mi.riesgoTipoNombre,
					usuarioActualizo: null,
					usuarioCreo: null,
				});
			}
			mi.mostraringreso=false;
			mi.esnuevo = false;
		}
	
		
		mi.abrirPopupFecha = function(index) {
			
			if(index<1000){
				mi.camposdinamicos[index].isOpen = true;
			}
			else{
				switch(index){
					case 1000: mi.fe_abierto = true; break;
				}
			}
		};
		
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				mi.obtenerTotalRiesgos();
				mi.gridApi.selection.clearSelectedRows();
				mi.riesgo = null;
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
			mi.riesgoTipoid = itemSeleccionado.id;
			mi.riesgoTipoNombre = itemSeleccionado.nombre;
			
			var parametros = { 
					accion: 'getRiesgoPropiedadPorTipo', 
					idRiesgo: mi.riesgo!=null ? mi.riesgo.id : 0,
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
			$utilidades.mensaje('warning', 'Debe seleccionar una ENTIDAD');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
}