app.controller('riesgoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog','$uibModal','$q', 'dialogoConfirmacion', 
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog,$uibModal,$q, $dialogoConfirmacion) {
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
		mi.subcomponenteid = "";
		mi.subcomponenteNombre="";
		mi.productoid="";
		mi.productoNombre="";
		mi.colaboradorid = ""
		mi.colaboradorNombre = "";
		mi.camposdinamicos = {};
		mi.formatofecha = 'dd/MM/yyyy';
		mi.altformatofecha = ['d!/M!/yyyy'];
		mi.colaboradorid="";
		mi.colaboradorNombre="";
		mi.proyectoid = "";
		mi.fechaEjecucion = null;
		
		mi.columnaOrdenada=null;
		mi.ordenDireccion = null;
		mi.filtros = [];
		mi.orden = null;
		
		mi.objetoId=null;
		mi.objetoTipo=null;
		mi.parentController=null;
		mi.congelado=0;
		
		if($scope.$parent.prestamoc && $scope.$parent.prestamoc.prestamo){
			$scope.$parent.prestamoc.child_riesgos = $scope.riesgoc;
			mi.objetoId = $scope.$parent.prestamoc.prestamo.id;
			mi.objetoTipo=-1;
			mi.congelado = 0;
			mi.parentController=$scope.$parent.prestamoc;
		}
		else if($scope.$parent.controller && $scope.$parent.controller.proyecto){
			$scope.$parent.controller.child_riesgos = $scope.riesgoc;
			mi.objetoId = $scope.$parent.controller.proyecto.id;
			mi.objetoTipo=0;
			mi.congelado = $scope.$parent.controller.congelado;
			mi.parentController=$scope.$parent.controller;
		}
		else if($scope.$parent.componentec && $scope.$parent.componentec.componente){
			$scope.$parent.componentec.child_riesgos = $scope.riesgoc;
			mi.objetoId = $scope.$parent.componentec.componente.id;
			mi.objetoTipo=1;
			mi.congelado = $scope.$parent.componentec.congelado;
			mi.parentController=$scope.$parent.componentec;
		}
		else if($scope.$parent.subcomponentec && $scope.$parent.subcomponentec.subcomponente){
			$scope.$parent.subcomponentec.child_riesgos = $scope.riesgoc;
			mi.objetoId = $scope.$parent.subcomponentec.subcomponente.id;
			mi.objetoTipo=2;
			mi.congelado = $scope.$parent.subcomponentec.congelado;
			mi.parentController=$scope.$parent.subcomponentec;
		}
		else if( $scope.$parent.producto && $scope.$parent.producto.producto){
			$scope.$parent.producto.child_riesgos = $scope.riesgoc;
			mi.objetoId = $scope.$parent.producto.producto.id;
			mi.objetoTipo=3;
			mi.congelado = $scope.$parent.producto.congelado;
			mi.parentController=$scope.$parent.producto;
		}
		else if($scope.$parent.subproducto && $scope.$parent.subproducto.subproducto){
			$scope.$parent.subproducto.child_riesgos = $scope.riesgoc;
			mi.objetoId = $scope.$parent.subproducto.subproducto.id;
			mi.objetoTipo=4;
			mi.congelado = $scope.$parent.subproducto.congelado;
			mi.parentController=$scope.$parent.subproducto;
		}
		
		mi.actualizarObjetoId=function(){
			switch(mi.objetoTipo){
				case -1: mi.objetoId = $scope.$parent.prestamoc.prestamo.id; break;
				case 0: mi.objetoId = $scope.$parent.controller.proyecto.id; break;
				case 1: mi.objetoId = $scope.$parent.componentec.componente.id; break;
				case 2: mi.objetoId = $scope.$parent.subcomponentec.subcomponente.id; break;
				case 3: mi.objetoId = $scope.$parent.producto.producto.id; break;
				case 4: mi.objetoId = $scope.$parent.subproducto.subproducto.id; break;
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
			$http.post('/SRiesgo', { accion: 'getRiesgosPorObjeto',  
				objetoid: mi.objetoId, objetotipo: mi.objetoTipo, t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.riesgos = response.riesgos;
						mi.mostrarcargando = false;
						for(var i=0; i<mi.riesgos.length; i++)
							mi.riesgos[i].fechaEjecucion = mi.riesgos[i].fechaEjecucion!=null?moment(mi.riesgos[i].fechaEjecucion,'DD/MM/YYYY').toDate():null;
					});
		}
		
		mi.cargarTabla();
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}
		
		mi.guardar=function(mensaje_success, mensaje_error){
			mi.actualizarObjetoId();
			$http.post('/SRiesgo', {
				accion: 'guardarRiesgos',
				riesgos: JSON.stringify(mi.riesgos),
				objetoTipo: mi.objetoTipo,
				objetoId: mi.objetoId,
				t: (new Date()).getTime()
			}).success(function(response){
				if(response.success){
					var sids = response.ids.length>0 ? response.ids.split(",") : [];
					for(var i = 0; i<sids.length; i++)
						mi.riesgos[i].id = parseInt(sids[i]);
					mi.cargarTabla();
					$utilidades.mensaje('success',mensaje_success);
					if(mi.parentController)
						mi.parentController.botones=true;
				}
				else{
					$utilidades.mensaje('danger',mensaje_error);
					if(mi.parentController)
						mi.parentController.botones=true;
				}
			});
		};

		mi.borrar = function(row) {
			$dialogoConfirmacion.abrirDialogoConfirmacion($scope
					, "Confirmación de Borrado"
					, '¿Desea borrar el Riesgo "'+row.nombre+'"?'
					, "Borrar"
					, "Cancelar")
			.result.then(function(data) {
				if(data){
					var index = mi.riesgos.indexOf(row);
			        if (index !== -1) {
			            mi.riesgos.splice(index, 1);
			        }
				}
			}, function(){
				
			});
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
			mi.subcomponenteid = "";
			mi.subcomponenteNombre="";
			mi.productoid="";
			mi.productoNombre="";
			mi.camposdinamicos = {};
			mi.fechaEjecucion=null;
			$utilidades.setFocus(document.getElementById("nombre"));
		};

		mi.editar = function(row) {
			mi.riesgo=row;
			if(mi.riesgo!=null){
				mi.form_valid = null;
				mi.riesgoTipoid = mi.riesgo.riesgotipoid;
				mi.riesgo.fechaEjecucion = mi.riesgo.fechaEjecucion;
				mi.riesgoTipoNombre = mi.riesgo.riesgotiponombre;
				mi.mostraringreso = true;
				mi.esnuevo = false;
				mi.colaboradorNombre = mi.riesgo.colaboradorNombre;
				mi.colaboradorId = mi.riesgo.colaboradorid;
				mi.fechaEjecucion = mi.riesgo.fechaEjecucion;
				mi.ejecutado = mi.riesgo.ejecutado == 1;
				$utilidades.setFocus(document.getElementById("nombre"));
				for (campos in mi.riesgo.camposdinamicos) {
					switch (mi.riesgo.camposdinamicos[campos].tipo){
						case 5:
							mi.riesgo.camposdinamicos[campos].valor = !(mi.riesgo.camposdinamicos[campos].valor instanceof Date) ? moment(mi.riesgo.camposdinamicos[campos].valor,'DD/MM/YYYY').toDate() : mi.riesgo.camposdinamicos[campos].valor ;
							break;
						case 1:
							mi.riesgo.camposdinamicos[campos].valor = (mi.riesgo.camposdinamicos[campos].valor!='' && mi.riesgo.camposdinamicos[campos].valor!=null) ? Number(mi.riesgo.camposdinamicos[campos].valor) : null;
							break;
						case 2:
							mi.riesgo.camposdinamicos[campos].valor = (mi.riesgo.camposdinamicos[campos].valor!='' &&mi.riesgo.camposdinamicos[campos].valor!=null) ? Number(mi.riesgo.camposdinamicos[campos].valor) : null;
							break;
					}
				}
				
			}
		}

		mi.irATabla = function() {
			if(mi.esnuevo){
					if(mi.parentController.mForm.$valid || 
							(mi.parentController.mForm.$error.required.length==1 && mi.parentController.mForm.$error.required[0].$name=='form_valid'))
						mi.riesgos.push({
							id: 0,
							nombre: mi.riesgo.nombre,
							descripcion: mi.riesgo.descripcion == null ? "" : mi.riesgo.descripcion,
							riesgotipoid: mi.riesgoTipoid,
							riesgotiponombre: mi.riesgoTipoNombre,
							impacto: mi.riesgo.impacto,
							probabilidad: mi.riesgo.probabilidad,
							impactoMonto: mi.riesgo.impactoMonto,
							impactoTiempo: mi.riesgo.impactoTiempo,
							gatillo: mi.riesgo.gatillo,
							consecuencia: mi.riesgo.consecuencia,
							solucion: mi.riesgo.solucion,
							riesgosSecundarios: mi.riesgo.riesgosSecundarios,
							ejecutado: mi.ejecutado ? 1 : 0,
							fechaEjecucion:  mi.fechaEjecucion,
							resultado: mi.riesgo.resultado,
							observaciones: mi.riesgo.observaciones,
							colaboradorid: mi.colaboradorid,
							colaboradorNombre: mi.colaboradorNombre,
							usuarioCreo: null,
							usuarioActualizo: null,
							fechaCreacion: null,
							fechaActualizacion: null,
							camposdinamicos: mi.camposdinamicos,
							estado: 1
						});
					mi.mostraringreso=false;
					mi.esnuevo = false;
					mi.form_valid = 1;
				}
				else if(mi.parentController.mForm.$valid || mi.parentController.mForm.$error.required[0].$name=='form_valid'){
					mi.riesgo.colaboradorNombre = mi.colaboradorNombre;
					mi.riesgo.colaboradorid = mi.colaboradorid;
					mi.riesgo.ejecutado = mi.ejecutado ? 1 : 0;
					mi.riesgo.riesgotipoid = mi.riesgoTipoid;
					mi.riesgo.riesgotiponombre = mi.riesgoTipoNombre;
					mi.riesgo.fechaEjecucion = mi.fechaEjecucion;
					mi.mostraringreso=false;
					mi.esnuevo = false;
					mi.form_valid = 1;
				}
				else if(!mi.parentController.mForm.$valid){
					$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');
				}
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