var docsAdjuntos = angular.module('documentosadjuntosController', [ 'ngTouch']);
/*
docsAdjuntos.controller('documentosadjuntosController',['$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$q','$uibModal', '$document',
	function($scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$q,$uibModal,$document) {
		var mi=this;
		
		i18nService.setCurrentLang('es');
		
		mi.displayObjeto = function(objetoSeleccionado){
			if(objetoSeleccionado === 0){
				mi.proyectoHide = false;
				mi.componenteHide = false;
				mi.productoHide = false;
				mi.subProductoHide = false;
				mi.actividadHide = false;
				mi.subirArchivo = false;
				mi.esRequeridoProyecto = true;
				mi.esRequeridoComponente = true;
				mi.esRequeridoProducto = true;
				mi.esRequeridoSubProducto = true;
				mi.esRequeridoActividad = true;
				$scope.limpiarCampos(true);
			}else if(objetoSeleccionado === 1){
				mi.proyectoHide = true;
				mi.componenteHide = false;
				mi.productoHide = false;
				mi.subProductoHide = false;
				mi.actividadHide = false;
				mi.subirArchivo = true;
				mi.esRequeridoProyecto = false;
				mi.esRequeridoComponente = true;
				mi.esRequeridoProducto = true;
				mi.esRequeridoSubProducto = true;
				mi.esRequeridoActividad = true;
				$scope.limpiarCampos(true);
			}else if(objetoSeleccionado === 2){
				mi.proyectoHide = false;
				mi.componenteHide = true;
				mi.productoHide = false;
				mi.subProductoHide = false;
				mi.actividadHide = false;
				mi.subirArchivo = true;
				mi.esRequeridoProyecto = true;
				mi.esRequeridoComponente = false;
				mi.esRequeridoProducto = true;
				mi.esRequeridoSubProducto = true;
				mi.esRequeridoActividad = true;
				$scope.limpiarCampos(true);
			}else if(objetoSeleccionado === 3){
				mi.proyectoHide = false;
				mi.componenteHide = false;
				mi.productoHide = true;
				mi.subProductoHide = false;
				mi.actividadHide = false;
				mi.esRequeridoProyecto = true;
				mi.esRequeridoComponente = true;
				mi.esRequeridoProducto = false;
				mi.esRequeridoSubProducto = true;
				mi.esRequeridoActividad = true;
				mi.subirArchivo = true;
				$scope.limpiarCampos(true);
			}else if(objetoSeleccionado === 4){
				mi.proyectoHide = false;
				mi.componenteHide = false;
				mi.productoHide = false;
				mi.subProductoHide = true;
				mi.actividadHide = false;
				mi.subirArchivo = true;
				mi.esRequeridoProyecto = true;
				mi.esRequeridoComponente = true;
				mi.esRequeridoProducto = true;
				mi.esRequeridoSubProducto = false;
				mi.esRequeridoActividad = true;
				$scope.limpiarCampos(true);
			}else if(objetoSeleccionado === 5){
				mi.proyectoHide = false;
				mi.componenteHide = false;
				mi.productoHide = false;
				mi.subProductoHide = false;
				mi.actividadHide = true;
				mi.subirArchivo = true;
				mi.esRequeridoProyecto = true;
				mi.esRequeridoComponente = true;
				mi.esRequeridoProducto = true;
				mi.esRequeridoSubProducto = true;
				mi.esRequeridoActividad = false;
				$scope.limpiarCampos(true);
			}
		}
		
		mi.cargarDocumentos = function(valorSeleccionado){
			var formatData = new FormData();
			formatData.append("accion","getDocumentos");
			formatData.append("idObjeto", valorSeleccionado);
			formatData.append("idTipoObjeto", (mi.idProyecto == "" ? (mi.idComponente == "" ? (mi.idProducto == "" ? (mi.idSubProducto == "" ? (mi.idActividad) : mi.idSubProducto) : mi.idProducto) : mi.idComponente) : mi.idProyecto));
			$http.post('/SDocumentosAdjuntos', formatData, {
				headers: {'Content-Type': undefined},
				transformRequest: angular.identity,
			}).then(function(response) {
				if (response.data.success) {
					mi.tdocumentos=[];
					var documentos = response.data.documentos;
					for (var i = 0; i < documentos.length; i++){
						mi.tdocumentos.push(
							{
								'id' : documentos[i].id,
								'extension' : documentos[i].extension,
								'nombre' : documentos[i].nombre,
								'descripcion' : documentos[i].descripcion,
							}
						)
					}	
				}else{

				}
			});
		}
		
		mi.mostrarcargando=true;
		mi.mostraringreso=false;
		mi.esnuevo = false;
		mi.isCollapsed = true;
		mi.paginaActual = 1;
		mi.tdocumentos=[];
		mi.DocumentosEliminados=[];
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		
		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp';
			$utilidades.mensaje('primary','No tienes permiso de acceder a esta área');			
		};
		
		$scope.objetos = [
			{value: 0,text: "Seleccione una Opción"},
			{value: 1,text: "Proyecto"},
			{value: 2,text: 'Componente'},
			{value: 3,text: 'Producto'},
			{value: 4,text: 'Sub producto'},
			{value: 5,text: 'Actividad'}
		];
		
		$scope.objeto = $scope.objetos[0];
		
		$scope.cargarDocumento=function(event){
			$scope.documentos = event.target.files[0];      
			$scope.nombreDocumento = $scope.documentos.name;
		}
		
		$scope.buscar=function(selectedValue){
				var modalInstance = $uibModal.open({
				    animation : 'true',
				    ariaLabelledBy : 'modal-title',
				    ariaDescribedBy : 'modal-body',
				    templateUrl : 'busqueda.jsp',
				    backdrop : 'static',
				    size : 'md',
				    scope: $scope,
				    templateUrl : 'busqueda.jsp',
				    controller: 'ModalDialogController',
				    controllerAs:'modalBuscar',
				    resolve : {
				    	tipoObjeto : function() {
							return selectedValue;
						},
						proyectoId: function(){
							return null;
						}
				    }

				});
			
			modalInstance.result.then(function(data) {
					if (selectedValue === 1){
						mi.proyecto = data.nombre;
						mi.idProyecto = data.id;
					}else if (selectedValue === 2){
						mi.componente = data.nombre;
						mi.idComponente = data.id;
					}else if (selectedValue === 3){
						mi.producto = data.nombre;
						mi.idProducto = data.id;
					}else if (selectedValue === 4){
						mi.subproducto = data.nombre;
						mi.idSubProducto = data.id;
					}else if (selectedValue === 5){
						mi.actividad = data.nombre;
						mi.idActividad = data.id;
					}
					
					mi.cargarDocumentos(selectedValue);
			}, function() {
			});
		};
		
		$scope.limpiarCampos = function(limpiarTabla){
			mi.proyecto = "";
			mi.idProyecto = "";
			mi.componente = "";
			mi.idComponente = "";
			mi.producto = "";
			mi.idProducto = "";
			mi.subproducto = "";
			mi.idSubProducto = "";
			if (limpiarTabla === true){
				mi.tdocumentos = [];
			}				
		}
		
		mi.agregarDocumento=function(selectedValue, descripcion){
			if ($scope.documentos!=null && $scope.documentos != '' && descripcion != undefined && descripcion != ""){
				var formatData = new FormData();
				formatData.append("file",$scope.documentos);
				formatData.append("accion",'agregarDocumento');
				formatData.append("descripcion", descripcion)
				formatData.append("idObjeto", selectedValue.value);
				formatData.append("idTipoObjeto", (mi.idProyecto == "" ? (mi.idComponente == "" ? (mi.idProducto == "" ? (mi.idSubProducto == "" ? (mi.idActividad) : mi.idSubProducto) : mi.idProducto) : mi.idComponente) : mi.idProyecto));
				formatData.append("esNuevo",true);
				$http.post('/SDocumentosAdjuntos', formatData, {
					headers: {'Content-Type': undefined},
					transformRequest: angular.identity,
				}).then(
						function(response) {
							if (response.data.success) {
								var nombredoc = mi.descripcion.replace(new RegExp(" ", 'g'),"_") + '.' + selectedValue.text.toLowerCase();
								mi.tdocumentos.push(
									{
										'id' : response.data.id,
										'extension' : response.data.extension_archivo,
										'nombre' : response.data.nombre,
										'descripcion' : mi.descripcion,
									}
								)
								mi.extension = null;
								mi.nombre='';
								mi.descripcion='';
								document.getElementById("pickfile").value = "";
							}else{
								if(response.data.existe_archivo)
									$utilidades.mensaje('danger','El archivo que desea subir ya existe');
								else
									$utilidades.mensaje('danger','Error al guardar el documento adjunto');
							}
				});
			}else{
				//mensaje de asignación de archivo
			}
		};
		
		String.prototype.replaceAll = function(search, replacement) {
		    var target = this;
		    return target.replace(new RegExp(search, 'g'), replacement);
		};
		
		mi.descargarDocumento= function(documento){
			var url = "/SDocumentosAdjuntos?accion=getDescarga&id="+documento.id;
			window.location.href = url;
		}
		
		mi.eliminarDocumento= function(documento, selectedValue){
			$http.post('/SDocumentosAdjuntos?accion=eliminarDocumento&id='+documento.id)
			.then(function successCAllback(response){
				if (response.data.success){
					var indice = mi.tdocumentos.indexOf(documento);
					if (indice !== -1) {
				       mi.tdocumentos.splice(indice, 1);		       
				    }
					mi.tdocumentos = [];
					mi.cargarDocumentos(selectedValue);
				}
			});
		};
	}
 ]);
/*
docsAdjuntos.controller('ModalDialogController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'tipoObjeto', ModalDialogController ]);

function ModalDialogController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, tipoObjeto) {
	var mi = this;
	
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;

	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	if (tipoObjeto === 1){
		var formatData = new FormData();//Utilizarlo para enviar id's de para cargar de objetos
		
		$http.post('/SBusqueda', {
			accion : 'documentosAdjuntosProyectos'
		}).success(function(response) {
			if (response.success) {
				mi.data = response.proyectos;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});	
	}else if(tipoObjeto === 2){
		$http.post('/SBusqueda', {
			accion : 'documentosAdjuntosComponentes'
		}).success(function(response) {
			if (response.success) {
				mi.data = response.componentes;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});
	}else if(tipoObjeto === 3){
		$http.post('/SBusqueda', {
			accion : 'documentosAdjuntosProductos'
		}).success(function(response) {
			if (response.success) {
				mi.data = response.productos;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});
	}else if(tipoObjeto === 4){
		$http.post('/SBusqueda', {
			accion : 'documentosAdjuntosSubProductos'
		}).success(function(response) {
			if (response.success) {
				mi.data = response.subproductos;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});
	}else if(tipoObjeto === 5){
		$http.post('/SBusqueda', {
			accion : 'documentosAdjuntosActividades'
		}).success(function(response) {
			if (response.success) {
				mi.data = response.actividades;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});
	}
	
	mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [ {
			displayName : 'Id',
			name : 'id',
			cellClass : 'grid-align-right',
			type : 'number',
			width : 80
		}, {
			displayName : 'Nombre',
			name : 'nombre',
			cellClass : 'grid-align-left'
		}],
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
					mi.seleccionarEntidad);
		}
	}

	mi.seleccionarEntidad = function(row) {
		mi.itemSeleccionado = row.entity;
		mi.seleccionado = row.isSelected;
	};

	mi.cargarTabla = function(pagina) {
		var datos = {
			accion : 'cargar',
			pagina : pagina,
			registros : mi.elementosPorPagina
		};

		mi.mostrarCargando = true;
		$http.post('/SSubproductoPropiedad', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.subproductoPropiedades;
				mi.opcionesGrid.data = mi.data;
				mi.mostrarCargando = false;
			}
		});
	};

	mi.cambioPagina = function() {
		mi.cargarTabla(mi.paginaActual);
	}

	mi.ok = function() {
		if (mi.seleccionado) {
			$uibModalInstance.close(mi.itemSeleccionado);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una PROPIEDAD');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
};*/