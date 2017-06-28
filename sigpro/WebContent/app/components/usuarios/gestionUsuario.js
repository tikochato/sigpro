/**
 * 
 */

var app = angular.module('gestionUsuariosController', [
	'ngTouch','ngUtilidades'
]);

app.controller('gestionUsuariosController', [
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
	'dialogoConfirmacion',
	
	function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal, $dialogoConfirmacion){
		
		var mi=this;
		$window.document.title =$utilidades.sistema_nombre+' - Usuario';
		/** cambiar 
		mi.isCollapsed=false;
		mi.esNuevo = false;
		 fin  cambiar **/
		/*mi.cambio=false;
		mi.changeUsuario=function(input){
			
			mi.cambio=input;
		}*/
		/* usado para cambiar y seleccionar el contexto al cual se va activar */
		mi.colaboradoresCargados=false;
		mi.changeActive=function(input){
			mi.activo=input;
			if(input==2)
				mi.colaboradoresCargados=true;
		}
		
		i18nService.setCurrentLang('es');
		
		/**configuración de usuarios **/
		
		mi.mostrarcargando=true;
		mi.cargandoPermisos=false;
		mi.entityselected = null;
		mi.esNuevo = false;
		mi.paginaActual = 1;
		mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
		mi.elementosPorPagina = $utilidades.elementosPorPagina;
		mi.permisoSelected={id:"",nombre:"", descripcion:""};
		mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:"", colaborador:""};
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
		mi.edicionPermisos=false;
		mi.filtros=[];
		mi.editarElemento = function (event) {
	        var filaId = angular.element(event.toElement).scope().rowRenderIndex;
	        mi.gridApi.selection.selectRow(mi.gridOptions.data[filaId]);
	        mi.editarUsuario();
	    };
		
		mi.gridOptions = {
			enableRowSelection : true,
			enableRowHeaderSelection : false,
			paginationPageSizes : [ 25, 50, 75 ],
			paginationPageSize : 25,
			enableFiltering: true,
			data : [],
			useExternalFiltering: true,
			useExternalSorting: true,
			rowTemplate: '<div ng-dblclick="grid.appScope.usuarioc.editarElemento($event)" ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" class="ui-grid-cell ng-scope ui-grid-disable-selection grid-align-right" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell="" ></div>',
			columnDefs : [ {
				name : 'Usuario',
				cellClass : 'grid-align-left',
				field : 'usuario',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.usuarioc.filtros[\'usuario\']" ng-keypress="grid.appScope.usuarioc.filtrar($event)" style="width:175px;"></input></div>'
			}, {
				name : 'Correo',
				cellClass : 'grid-align-left',
				field : 'email',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.usuarioc.filtros[\'email\']" ng-keypress="grid.appScope.usuarioc.filtrar($event)" style="width:175px;"></input></div>'
			}, {
				name: 'Usuario creo',
				cellClass : 'grid-align-left',
				field: 'usuarioCreo',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.usuarioc.filtros[\'usuario_creo\']" ng-keypress="grid.appScope.usuarioc.filtrar($event)" style="width:175px;"></input></div>'
			}, {
				name : 'Fecha creación',
				cellClass : 'grid-align-left',
				field : 'fechaCreacion',
				filterHeaderTemplate: '<div class="ui-grid-filter-container"><input type="text" style="width: 90%;" ng-model="grid.appScope.usuarioc.filtros[\'fecha_creacion\']" ng-keypress="grid.appScope.usuarioc.filtrar($event)" style="width:175px;"></input></div>'
			},{
				name: 'Usuario actualizo',
				cellClass : 'grid-align-left',
				field: 'usuarioActualizo',
				enableFiltering: false
			},{
				name: 'Fecha actualizacion',
				cellClass : 'grid-align-left',
				field: 'fechaActualizacion',
				enableFiltering: false
			}
			],

		};
		mi.cargarTabla=function(pagina){
			$http.post('/SUsuario',
					{ accion : 'getUsuarios',  pagina: pagina, numeroUsuarios: mi.elementosPorPagina,filtro_usuario: mi.filtros['usuario'],filtro_email: mi.filtros['email'],
				filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'] }).success(function(data) {
					mi.gridOptions.data =  data.usuarios;
					mi.mostrarcargando=false;
					
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


		mi.redireccionSinPermisos=function(){
			$window.location.href = '/main.jsp#!/forbidden';		
		}

		mi.cancelar = function() {
			mi.isCollapsed = false;
			mi.cambioPassword= false;
			mi.mostrarCambioPassword = false;
			mi.tieneColaborador=false;
			mi.edicionPermisos=false;
			mi.cargandoPermisos=false; 
			mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:"", colaborador:""};
		}


		mi.nuevoUsuario=function(){
			mi.claves.password1="";
			mi.claves.password2="";
			mi.permisosAsignados=[];
			mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:"", colaborador:""};
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
										permisos:JSON.stringify(mi.nuevosPermisos)	,
										esnuevo:true
									}
									).success(
										function(data) {
											if(data.success){
												mi.paginaActual=1;
												$utilidades.mensaje('success','Usuario creado exitosamente!');
												mi.cargarTabla(mi.paginaActual);
												mi.nuevosPermisos=[];
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
													mi.nuevosPermisos=[];
													mi.permisosEliminados=[];
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
													 $utilidades.mensaje('success', 'Actualización de permisos exitosa');

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
																		mi.nuevosPermisos=[];
																		mi.permisosEliminados=[];
																		if(mi.usuariosSelected.password!==passwordLocal){
																			$http.post('/SUsuario', {accion: 'cambiarPassword' , usuario: mi.usuariosSelected.usuario,	password:mi.usuariosSelected.password}).success(
																					function(response) {
																						if(response.success){
																							mi.paginaActual=1;
																							$utilidades.mensaje('success','información actualizada exitosamente.');
																							mi.isCollapsed = false;
																							mi.cargarTabla(mi.paginaActual);
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
	    if(mi.colaboradorSeleccionado){
	      mi.asignarColaborador();
	    }

	  };


		mi.eliminarUsuario=function(ev){
			if(mi.usuariosSelected.usuario!==""){
				$dialogoConfirmacion.abrirDialogoConfirmacion($scope
						, "Confirmación de Borrado"
						, '¿Desea borrar al usuario "'+mi.usuariosSelected.usuario+'"?'
						, "Borrar"
						, "Cancelar")
				.result.then(function(data) {
					if(data){
						$http.post('/SUsuario', {
							accion: 'eliminarUsuario',
							usuario: mi.usuariosSelected.usuario
						}).success(function(response){
							if(response.success){
								$utilidades.mensaje('success','Usuario elimiado con éxito');
								mi.cargarTabla(mi.paginaActual);
								mi.usuariosSelected={usuario:"", email:"",password:"", usuarioCreo:"", fechaCreacion:"", usuarioActualizo:"", fechaActualizacion:"", colaborador:""};
							}
							else
								$utilidades.mensaje('danger','Error al eliminar el usuario');
						});
					}
				}, function(){
					
				});
			}else{
			    $utilidades.mensaje('danger','Seleccione un usuario');
			}
		};
		
		function validarEmail(email) {
		    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		    return re.test(email);
		}
		mi.mostrarPermisos=function(){
			mi.edicionPermisos=true;
		};
		mi.editarUsuario=function(){
			if(mi.usuariosSelected.usuario!==""){
				mi.isCollapsed = true;
				mi.esNuevo=false;
				passwordLocal=mi.usuariosSelected.password;
				if(mi.usuariosSelected.colaborador!=null){
					mi.tieneColaborador=true;
				}
				mi.cargandoPermisos= true;
				mi.permisosAsignados=[];
				$http.post('/SUsuario', {
		    		accion:'obtenerPermisos',
		    		usuario: mi.usuariosSelected.usuario
		    	}).then(function(response) {
		    	    mi.permisosAsignados =response.data.permisos;
		    	   mi.cargandoPermisos=false;
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


		mi.buscarPermiso = function(tipo) {
			
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
			    		var parametros={nuevo:mi.esNuevo, usuario:mi.usuariosSelected.usuario, tipo:tipo};
			    		return  parametros;
			    	}
			    }

			});

			modalInstance.result.then(function(resultadoSeleccion) {
				if(resultadoSeleccion.tipo===1){
						mi.cargandoPermisos=true;
						$http.post('/SRol',{accion:'getPermisosPorRol',id:resultadoSeleccion.rol.id}).success(
								function(response){
									mi.permisosAsignados=response.permisos;
									mi.nuevosPermisos=response.ids;
									mi.cargandoPermisos=false;
								}
						);
				
				}else{
					mi.permisosAsignados.push(resultadoSeleccion);
					mi.nuevosPermisos.push(resultadoSeleccion.id);
				}
				
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
		
		mi.filtrar = function(evt){
			if(evt.keyCode==13){
				$http.post('/SUsuario', { accion: 'getTotalUsuarios',	filtro_usuario: mi.filtros['usuario'],filtro_email: mi.filtros['email'],
					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion']  }).success(
						function(response) {
							mi.elementosPorPagina = response.totalUsuarios;
							mi.cargarTabla(mi.paginaActual);
							mi.gridApi.selection.clearSelectedRows();
							mi.usuariosSelected.usuario = "";
				});
			}
		};

		
		
		
		
		
		
		/* fin de configuracion de usuarios */
		function validarEmail(email) {
		    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		    return re.test(email);
		}
		
		
		mi.cargarTabla=function(tab){
			if(tab==1){
				//carga la tabla de usuarios
			}else{
				//carga la tabla de colaboradores
			}
		};
		
		mi.editarItem=function(tab){
			if(tab==1){
				//edita un usuario
			}else{
				//edita un colaborador
			}
		};
		mi.borrarItem=function(tab){
			if(tab==1){
				//borra un usuario
			}else{
				//borra un colaborador
			}
		};
		
		mi.reiniciarTabla=function(tab){
			if(tab==1){
				//reiniciar tabla de usuario
			}else{
				//reiniciar tabla de colaborador
			}
		};
		
		mi.cambiarPagina=function(tab){
			if(tab==1){
				//cambiar Pagina
			}else{
				//reiniciar tabla de colaborador
			}
		}
		
		
		mi.cargarTotalElementos=function(tab){
			if(tab==1){
				//carga el número total de usuarios
			}else{
				//carga el número total de colaboradores
			}
		}
		
		mi.cargarTotalElementos(1);
		
		
		
		
	}
]);

