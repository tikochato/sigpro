var app = angular.module('proyectoController', [ 'ngTouch' ]);

app.controller('proyectoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$uibModal','$q',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$uibModal,$q) {

	var mi = this;
	i18nService.setCurrentLang('es');
	mi.proyecto = null;
	mi.esNuevo = false;
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

	mi.fechaOptions = {
			formatYear : 'yy',
			maxDate : new Date(2020, 5, 22),
			minDate : new Date(1900, 1, 1),
			startingDay : 1
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
		columnDefs : [
			{ name: 'id', width: 60, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name: 'nombre',  displayName: 'Nombre',cellClass: 'grid-align-left' },
			{ name : 'snip', width: 60, displayName : 'SNIP', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
			{ name : 'proyectotipo',   displayName : 'Tipo proyecto' ,cellClass: 'grid-align-left' },
			{ name : 'unidadejecutora',   displayName : 'Unidad Ejecutora' ,cellClass: 'grid-align-left' },
			{ name : 'cooperante',  width:200, displayName : 'Cooperante' ,cellClass: 'grid-align-left' },
			{ name: 'usuariocrea', width: 120, displayName: 'Usuario Creación'},
		    { name: 'fechacrea', width: 100, displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}

		],
		onRegisterApi: function(gridApi) {
			mi.gridApi = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope,function(row) {
				mi.proyecto = row.entity;
			});

			if($routeParams.reiniciar_vista=='rv'){
				mi.guardarEstado();
		    }
		    else{
		    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'proyceto', t: (new Date()).getTime()}).then(function(response){
			      if(response.data.success && response.data.estado!='')
			    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
			    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
				      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
				      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
				  });
		    }
		}
	};


	mi.cargarTabla = function(pagina){
		mi.mostrarcargando=true;
		$http.post('/SProyecto', { accion: 'getProyectoPagina', pagina: pagina,
			numeroproyecto:  $utilidades.elementosPorPagina }).success(
				function(response) {
					mi.entidades = response.proyectos;
					mi.gridOpciones.data = mi.entidades;
					mi.mostrarcargando = false;
				});
	}


	mi.guardar = function(){
		for (campos in mi.camposdinamicos) {
			if (mi.camposdinamicos[campos].tipo === 'fecha') {
				mi.camposdinamicos[campos].valor = moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY')
			}
		}
		if(mi.proyecto!=null &&  mi.proyecto.nombre!='' && mi.proyecto.snip!=''
			&& mi.poryectotipoid!='' && mi.unidadejecutoraid !=''
			&& mi.cooperanteid!=''){
			var param_data = {
				accion : 'guardar',
				id: mi.proyecto.id,
				nombre: mi.proyecto.nombre,
				snip: mi.proyecto.snip,
				descripcion:mi.proyecto.descripcion,
				proyectotipoid: mi.poryectotipoid,
				unidadejecutoraid: mi.unidadejecutoraid,
				cooperanteid: mi.cooperanteid,
				esnuevo: mi.esNuevo,
				datadinamica : JSON.stringify(mi.camposdinamicos)
			};
			$http.post('/SProyecto',param_data).then(
				function(response) {
					if (response.data.success) {
						mi.proyecto.id = response.data.id;
						$utilidades.mensaje('success','Proyecto '+(mi.esNuevo ? 'creado' : 'guardado')+' con éxito');
						mi.cargarTabla();
						mi.esNuevo = false;
					}else
						$utilidades.mensaje('danger','Error al '+(mi.esNuevo ? 'creado' : 'guardado')+' el Proyecto');
			});

		}else
			$utilidades.mensaje('warning','Debe de llenar todos los campos obligatorios');


	 }

	mi.borrar = function() {
		if(mi.proyecto !=null){
			$http.post('/SProyecto', {
				accion: 'borrarProyecto',
				id: mi.proyecto.id
			}).success(function(response){
				if(response.success){
					$utilidades.mensaje('success','Proyecto borrado con éxito');
					mi.cargarTabla();
				}
				else
					$utilidades.mensaje('danger','Error al borrar el Proyecto');
			});
		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Proyecto que desea borrar');
	};

	mi.nuevo = function (){
		mi.poryectotipoid = "";
		mi.proyectotiponombre="";
		mi.unidadejecutoraid="";
		mi.unidadejecutoranombre="";
		mi.cooperanteid="";
		mi.cooperantenombre="";
		mi.esColapsado = true;
		mi.proyecto = null;
		mi.esNuevo = true;
		mi.camposdinamicos = {};
		mi.gridApi.selection.clearSelectedRows();

	};

	mi.editar = function() {
		if(mi.proyecto!=null){
			mi.poryectotipoid = mi.proyecto.proyectotipoid;
			mi.proyectotiponombre=mi.proyecto.proyectotipo;
			mi.unidadejecutoraid=mi.proyecto.unidadejecutoraid;
			mi.unidadejecutoranombre=mi.proyecto.unidadejecutora;
			mi.cooperanteid=mi.proyecto.cooperanteid;
			mi.cooperantenombre=mi.proyecto.cooperante;
			mi.esColapsado = true;
			mi.esNuevo = false;

			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.proyecto.id:0,
				    idProyectoTipo: mi.poryectotipoid
			}
			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades
				for (campos in mi.camposdinamicos) {
					switch (mi.camposdinamicos[campos].tipo){
						case "fecha":
							mi.camposdinamicos[campos].valor = moment(mi.camposdinamicos[campos].valor).format('DD/MM/YYYY')
							break;
						case "entero":
							mi.camposdinamicos[campos].valor = Number(mi.camposdinamicos[campos].valor);
							break;

					}

				}
			});

		}
		else
			$utilidades.mensaje('warning','Debe seleccionar el Proyecto que desea editar');
	}

	mi.irATabla = function() {
		mi.esColapsado=false;
	}

	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'proyecto', estado: JSON.stringify(estado), t: (new Date()).getTime() };
		$http.post('/SEstadoTabla', tabla_data).then(function(response){

		});
	}

	mi.cambioPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	}

	mi.reiniciarVista=function(){
		if($location.path()=='/proyecto/rv')
			$route.reload();
		else
			$location.path('/proyecto/rv');
	}

	mi.abrirPopupFecha = function(index) {
		mi.camposdinamicos[index].isOpen = true;
	};

	$http.post('/SProyecto', { accion: 'numeroProyectos' }).success(
			function(response) {
				mi.totalProyectos = response.totalproyectos;
				mi.cargarTabla(1);
	});

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
			$location.path('/riesgo/'+  proyectoid );
		}
	};
	
	mi.irAHitos=function(proyectoid){
		if(mi.proyecto!=null){
			$location.path('/hito/'+ proyectoid );
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
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.poryectotipoid= itemSeleccionado.id;
			mi.proyectotiponombre = itemSeleccionado.nombre;

			var parametros = {
					accion: 'getProyectoPropiedadPorTipo',
					idProyecto: mi.proyecto!=''?mi.poryectotipoid.id:0,
					idProyectoTipo: itemSeleccionado.id
			}

			$http.post('/SProyectoPropiedad', parametros).then(function(response){
				mi.camposdinamicos = response.data.proyectopropiedades
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

	mi.buscarCooperante = function() {
		var resultado = mi.llamarModalBusqueda('/SCooperante', {
			accion : 'numeroCooperantes'
		}, function(pagina, elementosPorPagina) {
			return {
				accion : 'getCooperantesPagina',
				pagina : pagina,
				registros : elementosPorPagina
			};
		},'id','nombre');

		resultado.then(function(itemSeleccionado) {
			mi.cooperanteid= itemSeleccionado.id;
			mi.cooperantenombre = itemSeleccionado.nombre;

		});
	};
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
