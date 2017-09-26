//var app = angular.module('metaController', []);

app.controller('metaController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','$mdDialog', 'dialogoConfirmacion', 
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,$mdDialog, $dialogoConfirmacion) {
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
			
			mi.objeto_id = $scope.$parent.objeto_id;
			mi.objeto_tipo = $scope.$parent.objeto_tipo;
						
			mi.nombrePcp = "";
			mi.nombreTipoPcp = "";
			
			mi.anios=[];
			mi.anio = null;			
			
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
				$http.post('/SMeta', { accion: 'getMetasPagina', pagina: 1, numerometas: 1000, 
					id:mi.objeto_id, tipo: mi.objeto_tipo,
					t: (new Date()).getTime()
				}).success(
					function(response) {
						mi.metas = response.Metas;
						mi.mostrarcargando = false;
					});
			}
						
			mi.metaSeleccionada = function(row){
				mi.meta = row;
				mi.mostrarValores = true;
			}
			
			mi.editarElemento = function(row, elemento, valor){
				row[elemento] = valor;
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

			mi.borrar = function(ev) {
				if(mi.meta!=null){
					$dialogoConfirmacion.abrirDialogoConfirmacion($scope
							, "Confirmación de Borrado"
							, '¿Desea borrar la Meta "'+mi.meta.id+'"?'
							, "Borrar"
							, "Cancelar")
					.result.then(function(data) {
						if(data){
							$http.post('/SMeta', {
								accion: 'borrarMeta',
								id: mi.meta.id,
								t: (new Date()).getTime()
							}).success(function(response){
								if(response.success){
									$utilidades.mensaje('success','Meta borrada con éxito');
									mi.meta = null;
									mi.obtenerTotalMetas();
								}
								else
									$utilidades.mensaje('danger','Error al borrar la Meta');
							});
						}
					}, function(){
						
					});
				}
				else
					$utilidades.mensaje('warning','Debe seleccionar la Meta que desea borrar');
			};

			mi.nueva = function() {
				mi.mostraringreso=true;
				mi.esnueva = true;
				mi.meta = {};
				mi.tipoMetaSeleccionado=null;				
				mi.unidadMedidaSeleccionado=null;
				mi.tipoValorSeleccionado = {
						"id" : 3,
						"nombre" : "decimal"
				}
				mi.gridApi.selection.clearSelectedRows();
				$utilidades.setFocus(document.getElementById("nombre"));
			};

			mi.editar = function() {
				if(mi.meta!=null && mi.meta.id!=null){
					mi.mostraringreso = true;
					mi.esnueva = false;
					mi.tipoMetaSeleccionado = {
							"id" : mi.meta.tipoMetaId,
							"nombre" : mi.meta.tipoMetaNombre
					}
					mi.unidadMedidaSeleccionado = {
							"id" : mi.meta.unidadMedidaId,
							"nombre" : mi.meta.unidadMedidaNombre
					}
					mi.tipoValorSeleccionado = {
							"id" : 3,
							"nombre" : "decimal"
					}
					$utilidades.setFocus(document.getElementById("nombre"));
				}
				else{
					$utilidades.mensaje('warning','Debe seleccionar la Meta que desea editar');
				}
			}


//			mi.cargarTabla = function(pagina){
//				mi.mostrarcargando=true;
//				$http.post('/SMeta', { accion: 'getMetasPagina', pagina: pagina, numerometas: $utilidades.elementosPorPagina, 
//					id:mi.objeto_id, tipo: mi.objeto_tipo,
//					filtro_nombre: mi.filtros['nombre'], 
//					filtro_usuario_creo: mi.filtros['usuario_creo'], filtro_fecha_creacion: mi.filtros['fecha_creacion'],
//					columna_ordenada: mi.columnaOrdenada, orden_direccion: mi.ordenDireccion, t: (new Date()).getTime()
//				}).success(
//						function(response) {
//							mi.metas = response.Metas;
//							mi.gridOptions.data = mi.metas;
//							mi.mostrarcargando = false;
//							mi.paginaActual = pagina;
//						});
//			}
			
//			mi.reiniciarVista=function(){
//				if($location.path()=='/meta/'+mi.objeto_id + '/' + mi.objeto_tipo + '/rv')
//					$route.reload();
//				else
//					$location.path('/meta/'+mi.objeto_id + '/' + mi.objeto_tipo + '/rv');
//			}

		}
	]);
