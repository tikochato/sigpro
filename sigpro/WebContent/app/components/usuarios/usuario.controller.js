var app = angular.module('usuarioController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'usuarioController',
 [
  '$scope',
  '$http',
  '$interval',
  '$q',
  'i18nService',
  'Utilidades',
  '$routeParams',
  'uiGridConstants',
  '$mdDialog',
  '$window',
  '$location',
  '$route',
  '$q',
  '$uibModal',
  function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal) {
	var mi=this;
	$window.document.title = 'SIGPRO - Usuarios';
	mi.colaboradorSeleccionado =false;
	i18nService.setCurrentLang('es');
	mi.mostrarcargando=true;
	mi.entityselected = null;
	mi.esNuevo = false;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.permisoSelected={id:"",nombre:"", descripcion:""};	
	mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};
	mi.claves={password1:"", password2:""};
	mi.nuevosPermisos=[];
	mi.permisosEliminados=[];
	var usuarioMail ="";
	mi.permisosAsignados=[];
	mi.colaborador={};
	mi.mensajeActualizado={mensaje:"buscar colaborador"};
	mi.cambioPassword= false;
	mi.mostrarCambioPassword = false;
	var passwordLocal="";
	mi.tieneColaborador=false;
	mi.gridOptions = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		paginationPageSizes : [ 25, 50, 75 ],
		paginationPageSize : 25,
		data : [],
		columnDefs : [ {
			name : 'Usuario',
			field : 'usuario'
		}, {
			name : 'Correo',
			field : 'email'
		}, {
			name : 'Fecha Creación',
			field : 'fechaCreacion'
		}, {
			name: 'Usuario creó',
			field: 'usuarioCreo'
		},{
			name: 'Usuario Actualizó',
			field: 'usuarioActualizo'
		}
		],

	};
	mi.cargarTabla=function(pagina){
		$http.post('/SUsuario',
				{ accion : 'getUsuarios',  pagina: pagina, numeroUsuarios: $utilidades.elementosPorPagina  }).success(function(data) {
				mi.gridOptions.data =  data.usuarios;
				mi.mostrarcargando=false;
				mi.isCollapsed = false;
		});
	};
	
	mi.gridOptions.multiSelect = false;
	mi.gridOptions.modifierKeysToMultiSelect = false;
	mi.gridOptions.noUnselect = true;
	mi.gridOptions.onRegisterApi = function(gridApi) {
		mi.gridApi = gridApi;
		gridApi.selection.on
		.rowSelectionChanged(
			$scope,
			function(row) {
			var msg = 'row selected '
			+ row;
			mi.usuariosSelected = row.entity;
			usuarioMail= mi.usuariosSelected.email;
		});
		if($routeParams.reiniciar_vista=='rv'){
			mi.guardarEstado();
	    }
	    else{
	    	  $http.post('/SEstadoTabla', { action: 'getEstado', grid:'usuarios', t: (new Date()).getTime()}).then(function(response){
		      if(response.data.success && response.data.estado!='')
		    	  mi.gridApi.saveState.restore( $scope, response.data.estado);
		    	  mi.gridApi.colMovable.on.columnPositionChanged($scope, mi.guardarEstado);
			      mi.gridApi.colResizable.on.columnSizeChanged($scope, mi.guardarEstado);
			      mi.gridApi.core.on.columnVisibilityChanged($scope, mi.guardarEstado);
			      mi.gridApi.core.on.sortChanged($scope, mi.guardarEstado);
			  });
	    }
	};

   
						
						
	

	

	mi.cancelar = function() {
		mi.isCollapsed = false;
		mi.cambioPassword= false;
		mi.mostrarCambioPassword = false;
		mi.tieneColaborador=false;
	}
				
	
	mi.nuevoUsuario=function(){
		mi.claves.password1="";
		mi.claves.password2="";
		mi.permisosAsignados=[];
		mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};
		mi.isCollapsed = true;
		mi.entityselected = null;
		mi.esNuevo = true;
	};
	
	mi.guardarUsuario=function(){
		if(mi.esNuevo){
			if(mi.claves.password1!=="" && mi.claves.password2!=="" && mi.usuariosSelected.usuario!=="" && mi.usuariosSelected.email!==""){
				if(validarEmail(mi.usuariosSelected.email)){
					if(mi.claves.password1===mi.claves.password2){
						mi.usuariosSelected.password= mi.claves.password1;
						$http.post('/SUsuario',
								{
									accion: 'guardarUsuario',
									usuario:mi.usuariosSelected.usuario,
									email:mi.usuariosSelected.email,
									password:mi.usuariosSelected.password,
									permisos:JSON.stringify(mi.nuevosPermisos),
									esnuevo:true
								}).success(
									function(data) {
										if(data.success){
											mi.paginaActual=1;
											$utilidades.mensaje('success','Usuario creado exitosamente!');
											mi.cargarTabla(mi.paginaActual);
											mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};
										}
							});
					}else{
						$utilidades.mensaje('danger','No coinciden la contraseña y su confirmación.');
					}
				}else{
					$utilidades.mensaje('danger','correo electrónico no válido.');
				}
				
			}else{
				$utilidades.mensaje('danger','Los campos no deben de quedar vacios.');
			}
		}else{
			if(mi.usuariosSelected.email!==""){
				if(validarEmail(mi.usuariosSelected.email)){
					if(usuarioMail===mi.usuariosSelected.email && passwordLocal=== mi.usuariosSelected.password){
						if(mi.nuevosPermisos.length==0 && mi.permisosEliminados.length==0){
							$utilidades.mensaje('danger','No se ha realizado ningún cambio.');
							
						}else{
							$http.post('/SUsuario',
									{
										accion: 'actualizarPermisos',
										usuario:mi.usuariosSelected.usuario,
										permisosNuevos:JSON.stringify(mi.nuevosPermisos),
										permisosEliminados:JSON.stringify(mi.permisosEliminados)
									}).success(
										function(data) {
											if(data.success){
												mi.cargarTabla(mi.paginaActual);
												if(mi.usuariosSelected.password!==passwordLocal){
													$http.post('/SUsuario', {accion: 'cambiarPassword' , usuario: mi.usuariosSelected.usuario,	password:mi.usuariosSelected.password}).success(
															function(response) {
																if(response.success){
																	 $utilidades.mensaje('success', 'cambio de contraseña Exitoso.');
																}else{
																	$utilidades.mensaje('danger', 'No se pudo cambiar la contraseña.');
																}
													});
												}
												mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};
												
											}
								});
						}
						
					}else{
						$http.post('/SUsuario',
								{
									accion: 'guardarUsuario',
									usuario:mi.usuariosSelected.usuario,
									email:mi.usuariosSelected.email,
									esnuevo:false
								}).success(
									function(data) {
										if(data.success){
											if(mi.nuevosPermisos.length==0 && mi.permisosEliminados.length==0){
												mi.isCollapsed = false;
												mi.cargarTabla(mi.paginaActual);
												if(mi.usuariosSelected.password!==passwordLocal){
													$http.post('/SUsuario', {accion: 'cambiarPassword' , usuario: mi.usuariosSelected.usuario,	password:mi.usuariosSelected.password}).success(
															function(response) {
																if(response.success){
																	mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};
																	 $utilidades.mensaje('success', 'actualizacion de datos exitosa.');
																}else{
																	$utilidades.mensaje('danger', 'No se pudo cambiar la contraseña.');
																}
													});
												}
												
											}else{
												$http.post('/SUsuario',
														{
															accion: 'actualizarPermisos',
															usuario:mi.usuariosSelected.usuario,
															permisosNuevos:JSON.stringify(mi.nuevosPermisos),
															permisosEliminados:JSON.stringify(mi.permisosEliminados)
														}).success(
															function(data) {
																if(data.success){
																	
																	if(mi.usuariosSelected.password!==passwordLocal){
																		$http.post('/SUsuario', {accion: 'cambiarPassword' , usuario: mi.usuariosSelected.usuario,	password:mi.usuariosSelected.password}).success(
																				function(response) {
																					if(response.success){
																						mi.paginaActual=1;
																						$utilidades.mensaje('success','información actualizada exitosamente.');
																						mi.isCollapsed = false;
																						mi.cargarTabla(mi.paginaActual);
																						mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};																						
																					}else{
																						$utilidades.mensaje('danger', 'No se pudo cambiar la contraseña.');
																					}
																		});
																	}else{
																		$utilidades.mensaje('success','información actualizada exitosamente.');
																	}
																	
																}
													});
												
											}
										}else {
											$utilidades.mensaje('danger','No se pudo realizar cambios.');
										}
										
							});
						
					}
				}else{
					$utilidades.mensaje('danger','correo electrónico no válido.');
				}
				
			}else{
				$utilidades.mensaje('danger','Los campos no deben de quedar vacios.');
			}
		}
	};
	
	
	
	mi.eliminarUsuario=function(ev){
		if(mi.usuariosSelected.usuario!==""){
			var confirm = $mdDialog.confirm()
	          .title('Confirmación de borrado')
	          .textContent('¿Desea borrar al usuario "'+mi.usuariosSelected.usuario+'"?')
	          .ariaLabel('Confirmación de borrado')
	          .targetEvent(ev)
	          .ok('Borrar')
	          .cancel('Cancelar');
			$mdDialog.show(confirm).then(function() {
		    	$http.post('/SUsuario', {
					accion: 'eliminarUsuario',
					usuario: mi.usuariosSelected.usuario
				}).success(function(response){
					if(response.success){
						$utilidades.mensaje('success','Usuario elimiado con éxito');
						mi.cargarTabla(mi.paginaActual);
						mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:""};
					}
					else
						$utilidades.mensaje('danger','Error al eliminar el usuario');
				});
		    }, function() {
		    
		    });
		}else{
		    $utilidades.mensaje('danger','Seleccione un usuario');
		}
	};
	function validarEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(email);
	}
	
	mi.editarUsuario=function(){
		if(mi.usuariosSelected.usuario!==""){
			mi.isCollapsed = true;
			mi.esNuevo=false;
			passwordLocal=mi.usuariosSelected.password;
			if(mi.usuariosSelected.colaborador!=null){
				mi.tieneColaborador=true;
			}
			$http.post('/SUsuario', {
	    		accion:'obtenerPermisos',
	    		usuario: mi.usuariosSelected.usuario
	    	}).then(function(response) {
	    	    mi.permisosAsignados =response.data.permisos;
	    	});
		}else{
			$utilidades.mensaje('danger','Seleccione un usuario');
		}
		
	};
	
	
	mi.reiniciarVista=function(){
		if($location.path()=='/usuarios/rv')
			$route.reload();
		else
			$location.path('/usuarios/rv');
	};
	
	mi.cambiarPagina=function(){
		mi.cargarTabla(mi.paginaActual);
	};
	
	mi.guardarEstado=function(){
		var estado = mi.gridApi.saveState.save();
		var tabla_data = { action: 'guardaEstado', grid:'usuarios', estado: JSON.stringify(estado), t: (new Date()).getTime() }; 
		$http.post('/SEstadoTabla', tabla_data).then(function(response){
			
		});
	}
	

	mi.buscarPermiso = function(titulo, mensaje) {
		var modalInstance = $uibModal.open({
		    animation : 'true',
		    ariaLabelledBy : 'modal-title',
		    ariaDescribedBy : 'modal-body',
		    templateUrl : 'buscarPermiso.jsp',
		    controller : 'modalBuscarPermiso',
		    controllerAs : 'modalBuscar',
		    backdrop : 'static',
		    size : 'md',
		    resolve : {
		    	infoPermisos: function(){
		    		var parametros={nuevo:mi.esNuevo, usuario:mi.usuariosSelected.usuario};
		    		return  parametros;
		    	}
		    }

		});
		
		modalInstance.result.then(function(permisoSeleccionado) {
			mi.permisosAsignados.push(permisoSeleccionado);
			mi.nuevosPermisos.push(permisoSeleccionado.id);
		}, function() {
		});
	}
	
	mi.eliminarPermiso= function(permiso){
		var indice = mi.permisosAsignados.indexOf(permiso);
		if (indice !== -1) {
	       mi.permisosAsignados.splice(indice, 1);
	       mi.nuevosPermisos.splice(indice,1);
	    }
		if(!mi.esNuevo){
			mi.permisosEliminados.push(permiso.idPermiso);
		}
	};
	
	mi.buscarColaborador=function(){
		var modalInstance = $uibModal.open({
		    animation : 'true',
		    ariaLabelledBy : 'modal-title',
		    ariaDescribedBy : 'modal-body',
		    templateUrl : 'buscarColaborador.jsp',
		    controller : 'modalBuscarColaborador',
		    controllerAs : 'modalBuscar',
		    backdrop : 'static',
		    size : 'md',
		    resolve : {
		    	infoUsuario: function(){
		    		var parametros={ usuario:mi.usuariosSelected.usuario};
		    		return  parametros;
		    	}
		    }

		});
		
		modalInstance.result.then(function(data) {
			 mi.colaboradorSeleccionado=true;
			mi.colaborador=data;
			mi.mensajeActualizado.mensaje=data.primerApellido+ ", "+data.primerNombre;
		}, function() {
		});
	};
	
	mi.asignarColaborador= function(){
		if(mi.colaboradorSeleccionado){
			var datos = {
					accion : 'actualizar',
					codigo : mi.colaborador.id,
					primerNombre :  mi.colaborador.primerNombre,
					segundoNombre :  mi.colaborador.segundoNombre,
					primerApellido :  mi.colaborador.primerApellido,
					segundoApellido :  mi.colaborador.segundoApellido,
					cui :  mi.colaborador.cui,
					unidadEjecutora :  mi.colaborador.unidadEjecutora,
					usuario : mi.usuariosSelected.usuario
				};

				$http.post('/SColaborador', datos).then(
						function(response) {
							if (response.data.success) {
								$utilidades.mensaje('success',
										'Colaborador asignado con exito.');
							} else {
								$utilidades.mensaje('danger',
										'Error al actualizar datos...!!!');
							}
						});
		}else{
			$utilidades.mensaje('danger',
			'Seleccione un colaborador.');
		}
		
	}
	
	$http.post('/SUsuario', { accion: 'getTotalUsuarios' }).success(
			function(response) {
				mi.totalUsuarios = response.totalUsuarios;
				mi.cargarTabla(mi.paginaActual);
	});
} ]);

app.controller('modalBuscarPermiso', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','infoPermisos', modalBuscarPermiso
]);

function modalBuscarPermiso($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, infoPermisos) {
	
	var mi = this;
	if(infoPermisos.nuevo){
		mi.mostrarCargando = true;
    	$http.post('/SPermiso', {
    		accion:'getPermisos'
    	}).then(function(response) {
    	    if (response.data.success) {
    	    	mi.data = response.data.permisos;
    	    	mi.opcionesGrid.data = mi.data;
    			mi.mostrarCargando = false;
    	    }
    	});
	}else{
		 $http.post('/SUsuario', {
		    	accion : 'getPermisosDisponibles',
		    	usuario:infoPermisos.usuario
		        }).success(function(response) {
		        	mi.data = response.permisos;
	    	    	mi.opcionesGrid.data = mi.data;
	    			mi.mostrarCargando = false;
		    });
	}
	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
   
   
    mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [
			{
				displayName : 'Permiso', 
				name : 'nombre', 
				cellClass : 'grid-align-right', 
				type : 'text', width : 150
			
			}, 
			{ 
				displayName : 'Descripción', 
				name : 'descripcion', 
				cellClass : 'grid-align-left'
			}
		],
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
			    mi.seleccionarPermiso);
		}
    }
    
    mi.seleccionarPermiso = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };


     mi.ok = function() {
    	if (mi.seleccionado) {
    	    $uibModalInstance.close(mi.itemSeleccionado);
    	} else {
    	    $utilidades.mensaje('warning', 'Debe seleccionar un permiso');
    	}
     };

     mi.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
     };
}





app.controller('modalBuscarColaborador', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','infoUsuario',modalBuscarColaborador
]);
function modalBuscarColaborador($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, infoUsuario) {
	var mi = this;

	mi.totalElementos = 0;
	mi.paginaActual = 1;
	mi.numeroMaximoPaginas = 5;
	mi.elementosPorPagina = 9;
	
	mi.mostrarCargando = false;
	mi.data = [];
	
	mi.itemSeleccionado = null;
	mi.seleccionado = false;
	
   
   
    mi.opcionesGrid = {
		data : mi.data,
		columnDefs : [
			{
				displayName : 'Primer nombre', 
				name : 'primerNombre', 
				cellClass : 'grid-align-right', 
				type : 'text'
			
			}, 
			{ 
				displayName : 'Segundo nombre', 
				name : 'segundoNombre', 
				cellClass : 'grid-align-left'
			}
			, 
			{ 
				displayName : 'Primer apellido', 
				name : 'primerApellido', 
				cellClass : 'grid-align-left'
			}
			, 
			{ 
				displayName : 'Segundo apellido', 
				name : 'segundoApellido', 
				cellClass : 'grid-align-left'
			}
			, {
				displayName : 'CUI',
				name : 'cui',
				cellClass : 'grid-align-right',
				type : 'number'
			}
			, {
				displayName : 'Unidad Ejecutora',
				name : 'nombreUnidadEjecutora',
				cellClass : 'grid-align-left'
			}
		],
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
			    mi.seleccionarPermiso);
		}
    }
    var datos = {
			accion : 'cargar',
			pagina :1,
			registros : 100
		};

		mi.mostrarCargando = true;
		$http.post('/SColaborador', datos).then(function(response) {
			if (response.data.success) {

				mi.data = response.data.unidadesEjecutoras;
				mi.opcionesGrid.data = mi.data;

				mi.mostrarCargando = false;
			}
		});
    mi.seleccionarPermiso = function(row) {
    	mi.itemSeleccionado = row.entity;
    	mi.seleccionado = row.isSelected;
    };


     mi.ok = function() {
    	if (mi.seleccionado) {
    	    $uibModalInstance.close(mi.itemSeleccionado);
    	} else {
    	    $utilidades.mensaje('warning', 'Debe seleccionar un colaborador');
    	}
     };
	

     mi.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
     };
}
