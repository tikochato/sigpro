var app = angular.module('planAdquisicionesController', ['ngTouch','ngAnimate','ui.grid.edit', 'ui.grid.rowEdit','ui.utils.masks']);
app.controller('planAdquisicionesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion', '$filter','$uibModal',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion, $filter,$uibModal) {
	var mi = this;
	var anioFiscal = new Date();
	mi.anio = anioFiscal.getFullYear();
	mi.mostrarBotones = false;
	mi.mostrarGuardando = false;
	mi.mostrarCargando = false;
	mi.enMillones = false;
	mi.fechaSuscripcion = "";
	mi.fechaCierre = "";
	mi.tooltipObjetoTipo = ["Prestamo","Componente","Producto","Sub Producto","Actividad"];
	mi.valoresInicializados = [0,0,0,0,0,"","","","","","","","","",""];
	mi.ddlOpciones = [];
	mi.ddlOpcionesTipos = [];
	mi.ddlcategoriaAdquisiciones = [];
	i18nService.setCurrentLang('es');
	
	mi.calcularTamanosPantalla = function(){
		mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
		mi.tamanoTotal = mi.tamanoPantalla - 300; 
		mi.estiloCelda = "width:80px;min-width:80px; max-width:80px;text-align: center";
	}
	
	$scope.divActivo = "";
	mi.activarScroll = function(id){
		$scope.divActivo = id;
    }
	
	mi.prestamos = [
		{value: 0,text: "Seleccione un préstamo"}
	];	
	mi.prestamo = mi.prestamos[0];
	
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
		function(response) {
			mi.prestamos = [];
			mi.prestamos.push({'value' : 0, 'text' : 'Seleccione un préstamo'});
			if (response.success){
				for (var i = 0; i < response.entidades.length; i++){
					mi.prestamos.push({'value': response.entidades[i].id, 'text': response.entidades[i].nombre});
				}
				
				mi.prestamo = mi.prestamos[0];
			}
		});
	
	mi.selectedRow = function(row){
		mi.datoSeleccionado = row;
	}
	
	$http.post('/SMeta', {accion: 'getMetasUnidadesMedida'}).success(
		function(response){
			mi.ddlOpciones = [];
			mi.ddlOpciones.push({id: 0, value: 'Seleccionar'});
			if(response.success){
				for(x in response.MetasUnidades){
					mi.ddlOpciones.push({id: response.MetasUnidades[x].id, value: response.MetasUnidades[x].nombre});
				}	
			}
	});
	
	mi.ddlOpcionesTiposAdquisicion = [{id: 0, value: "Seleccionar"}];
	
	$http.post('/SCategoriaAdquisicion', {accion: 'getCategoriaAdquisicion'}).success(
		function(response){
			mi.ddlcategoriaAdquisiciones = [];
			mi.ddlcategoriaAdquisiciones = [{id: 0, value: "Seleccionar"}];		
			if(response.success){
				for(x in response.categoriaAdquisicion){
					mi.ddlcategoriaAdquisiciones.push({id: response.categoriaAdquisicion[x].id, value: response.categoriaAdquisicion[x].nombre});
				}
			}
		});
	

	mi.nombreUnidadMedida = function(id){
		for (i=0; i<mi.ddlOpciones.length; i++){
			if(mi.ddlOpciones[i].id == id){
				return mi.ddlOpciones[i].value;
			}
		}
	}
	
	mi.claseIcon = function (item) {
	    switch (item.objetoTipo) {
	        case 1:
	            return 'glyphicon glyphicon-record';
	        case 2:
	            return 'glyphicon glyphicon-th';
	        case 3:
	            return 'glyphicon glyphicon-certificate';
	        case 4:
	            return 'glyphicon glyphicon-link';
	        case 5:
	            return 'glyphicon glyphicon-th-list';
	    }
	};
	
	mi.calcularTotal = function(row){
		row['total'] = row['costo'] * row['cantidad'];
		mi.calcularPadre(row.predecesorId, row.objetoPredecesorTipo);
	}
	
	mi.ocultar = function(row, id, borrar){
        switch (id){
        case 0:
        	row.c0 = !row.c0;
        	break;
        case 1:
        	row.c1 = !row.c1;
        	break;
        case 2:
        	row.c2 = !row.c2;
        	break;
        case 3:
        	row.c3 = !row.c3;
        	break;
        case 4:
        	row.c4 = !row.c4;
        	break;
        case 5:
        	row.c5 = !row.c5;
        	break;
        case 6:
        	if(borrar)
        		row.planificadoDocs = null;
        	row.c6 = !row.c6;
        	break;
        case 7:
        	if(borrar)
        		row.realDocs = null;
        	row.c7 = !row.c7;
        	break;
        case 8:
        	if(borrar)
        		row.planificadoLanzamiento = null;
        	row.c8 = !row.c8;
        	break;
        case 9:
        	if(borrar)
        		row.realLanzamiento = null;
        	row.c9 = !row.c9;
        	break;
        case 10:
        	if(borrar)
        		row.planificadoRecepcionEval = null;
        	row.c10 = !row.c10;
        	break;
        case 11:
        	if(borrar)
        		row.realRecepcionEval = null;
        	row.c11 = !row.c11;
        	break;
        case 12:
        	if(borrar)
        		row.planificadoAdjudica = null;
        	row.c12 = !row.c12;
        	break;
        case 13:
	        if(borrar)
	        	row.realAdjudica = null;
        	row.c13 = !row.c13;
        	break;
        case 14:
        	if(borrar)
        		row.planificadoFirma = null;
        	row.c14 = !row.c14;
        	break;
        case 15:
        	if(borrar)
        		row.realFirma = null;
        	row.c15 = !row.c15;
        	break;
        }
    }
	
	mi.validarFecha = function(row,fecha,tipo){
		switch(fecha){
		case 1:
			if(tipo==1){
				var fechaActual = moment(row.planificadoDocs,'DD/MM/YYYY').toDate();
				if (isNaN(fechaActual)){
					row.planificadoDocs = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
					break;
				}else if(fechaActual < mi.fechaSuscripcion){
					row.planificadoDocs = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.planificadoDocs = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}else{
				var fechaActual = moment(row.realDocs,'DD/MM/YYYY').toDate();
				if (isNaN(fechaActual)){
					row.realDocs = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}else if(fechaActual < mi.fechaSuscripcion){
					row.realDocs = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.realDocs = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}
			break;
		case 2:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoDocs,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoLanzamiento,'DD/MM/YYYY').toDate();
				
				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior.getTime() > fechaActual.getTime()){
							row.planificadoLanzamiento = "";
							$utilidades.mensaje('warning', 'Fecha de "Lanzamiento de eventos planificado" no debe ser menor que la fecha de "Preparación de documentos planificado".');
							break;
						}	
					}else{
						row.planificadoLanzamiento = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Preparación de documentos planificado".');
						break;
					}					
				}else{
					row.planificadoLanzamiento = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.planificadoLanzamiento = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.planificadoLanzamiento = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}else{
				var fechaAnterior = moment(row.realDocs,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realLanzamiento,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.realLanzamiento = "";
							$utilidades.mensaje('warning', 'Fecha de "Lanzamiento de eventos real" no debe ser menor que la fecha de "Preparación de documentos real".');
							break;
						}
					}else{
						row.realLanzamiento = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Preparación de documentos real".');
						break;
					}
					
				}else{
					row.realLanzamiento = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.realLanzamiento = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.realLanzamiento = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}
			break;
		case 3:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoLanzamiento,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoRecepcionEval,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.planificadoRecepcionEval = "";
							$utilidades.mensaje('warning', 'Fecha de "Recepción y evaluación de ofertas planificado" no debe ser menor que la fecha de "Landamiento de eventos planificado".');
							break;
						}
					}else{
						row.planificadoRecepcionEval = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Landamiento de eventos planificado".');
						break;
					}
				}else{
					row.planificadoRecepcionEval = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.planificadoRecepcionEval = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.planificadoRecepcionEval = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}else{
				var fechaAnterior = moment(row.realLanzamiento,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realRecepcionEval,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.realRecepcionEval = "";
							$utilidades.mensaje('warning', 'Fecha de "Recepción y evaluación de ofertas real" no debe ser menor que la fecha de "Landamiento de eventos real".');
							break;
						}
					}else{
						row.realRecepcionEval = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Landamiento de eventos real".');
						break;
					}
				}else{
					row.realRecepcionEval = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.realRecepcionEval = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.realRecepcionEval = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}
			break;
		case 4:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoRecepcionEval,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoAdjudica,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.planificadoAdjudica = "";
							$utilidades.mensaje('warning', 'Fecha de "Adjudicación planificado" no debe ser menor que la fecha de "Recepción y evaluación de ofertas planificado".');
							break;
						}	
					}else{
						row.planificadoAdjudica = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Recepción y evaluación de ofertas planificado".');
						break;
					}
				}else{
					row.planificadoAdjudica = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.planificadoAdjudica = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.planificadoAdjudica = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}else{
				var fechaAnterior = moment(row.realRecepcionEval,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realAdjudica,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.realAdjudica = "";
							$utilidades.mensaje('warning', 'Fecha de "Adjudicación real" no debe ser menor que la fecha de "Recepción y evaluación de ofertas real".');
							break;
						}	
					}else{
						row.realAdjudica = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Recepción y evaluación de ofertas real".');
						break;
					}
				}else{
					row.realAdjudica = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.realAdjudica = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.realAdjudica = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}
			break;
		case 5:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoAdjudica,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoFirma,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.planificadoFirma = "";
							$utilidades.mensaje('warning', 'Fecha de "Firma de contrato planificado" no debe ser menor que la fecha de "Adjudicación planificado".');
							break;
						}
					}else{
						row.planificadoFirma = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Adjudicación planificado".');
						break;
					}
				}else{
					row.planificadoFirma = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.planificadoFirma = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.planificadoFirma = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}else{
				var fechaAnterior = moment(row.planificadoFirma,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realFirma,'DD/MM/YYYY').toDate();
				
				if (!isNaN(fechaActual)){
					if(!isNaN(fechaAnterior)){
						if(fechaAnterior > fechaActual){
							row.realFirma = "";
							$utilidades.mensaje('warning', 'Fecha de "Firma de contrato real" no debe ser menor que la fecha de "Adjudicación real".');
							break;
						}					
					}else{
						row.realFirma = "";
						$utilidades.mensaje('warning', 'Favor de ingresar fecha de "Adjudicación real".');
						break;
					}
				}else{
					row.realFirma = "";
					$utilidades.mensaje('danger', 'Fecha invalida.');
					break;
				}
				
				if(fechaActual < mi.fechaSuscripcion){
					row.realFirma = "";
					$utilidades.mensaje('warning', 'No puede ser menor a la fecha de suscripcion del prestamo "' + moment(mi.fechaSuscripcion).format("DD/MM/YYYY") +'".');
					break;
				}else if(fechaActual > mi.fechaCierre){
					row.realFirma = "";
					$utilidades.mensaje('warning', 'No puede ser mayor a la fecha de cierre del prestamo "' + moment(mi.fechaCierre).format("DD/MM/YYYY") +'".');
					break;
				}
				
				mi.bloquearPadre(row); 
				mi.bloquearHijos(row.hijos);
			}
			break;
		}
	}
	
	mi.bloquearPadre = function(row, bloqueo){
		var predecesorId = row.predecesorId;
		var objetoPredecesorTipo = row.objetoPredecesorTipo;
		
		var entidad = mi.obtenerEntidad(predecesorId,objetoPredecesorTipo);
		if(entidad != null){
			entidad.bloqueado = bloqueo;
			mi.bloquearPadre(entidad, bloqueo);	
		}
	}
	
	mi.desbloquearPadre = function(row, bloqueo){
		var predecesorId = row.predecesorId;
		var objetoPredecesorTipo = row.objetoPredecesorTipo;
		
		var entidad = mi.obtenerEntidad(predecesorId,objetoPredecesorTipo);
		if(entidad != null){
			if(entidad.hijos != null && entidad.hijos.length > 0){
				var hijoBloqueado = true;
				for(x in entidad.hijos){
					var entidadHijo = mi.obtenerEntidad(entidad.hijos[x].split(',')[0],entidad.hijos[x].split(',')[1]);
					if(!entidadHijo.bloqueado){
						if(!mi.hijosBloqueados(entidadHijo))
							hijoBloqueado = false;
						else{
							hijoBloqueado = true;
							break;
						}
					}else{
						hijoBloqueado = true;
						break;
					}
				}
				
				if(!hijoBloqueado){
					entidad.bloqueado = bloqueo;
					mi.desbloquearPadre(entidad, bloqueo);
				}
			}else{
				entidad.bloqueado = bloqueo;
				mi.desbloquearPadre(entidad, bloqueo);
			}
		}
	}
	
	mi.hijosBloqueados = function(entidad){
		if(entidad.hijos != null && entidad.hijos.length > 0){
			for(y in entidad.hijos){
				var entidadHijo = mi.obtenerEntidad(entidad.hijos[y].split(',')[0],entidad.hijos[y].split(',')[1]);
				var hijoBloqueado = true;
				if(!entidadHijo.bloqueado){
					hijoBloqueado = false;
				}else
					return true;
			}
		}
	}
	
	mi.bloquearHijos = function(hijos, bloqueo){
		if(hijos != null){
			for(x in hijos){
				var entidad = mi.obtenerEntidad(hijos[x].split(',')[0],hijos[x].split(',')[1]);
				if(entidad != undefined){
					entidad.bloqueado = bloqueo;
					mi.bloquearHijos(entidad.hijos, bloqueo);
				}
			}
		}
	}
	
	mi.guardarPlan = function(){
		var estructuraGuardar = "";
		mi.mostrarTablas = false;
		mi.mostrarGuardando = true;
		for(h in mi.data){
			var row = mi.data[h];
			estructuraGuardar += row.objetoId + ",";
			estructuraGuardar += row.objetoTipo + ",";
			estructuraGuardar += (row.tipoAdquisicion == null ? "" : row.tipoAdquisicion) + ",";
			estructuraGuardar += (row.categoriaAdquisicion != 0 ? row.categoriaAdquisicion : null) + ",";
			estructuraGuardar += row.idPlanAdquisiciones + ",";
			estructuraGuardar += (row.unidadMedida == null ? "" : row.unidadMedida) + ",";
			estructuraGuardar += row.cantidad + ",";
			estructuraGuardar += row.costo + ",";
			estructuraGuardar += row.total + ",";
			estructuraGuardar += ((row.planificadoDocs == null || row.planificadoDocs == "") ? null : row.planificadoDocs) + ",";
			estructuraGuardar += ((row.realDocs == null || row.realDocs == "") ? null : row.realDocs) + ",";
			estructuraGuardar += ((row.planificadoLanzamiento == null || row.planificadoLanzamiento == "") ? null : row.planificadoLanzamiento) + ",";
			estructuraGuardar += ((row.realLanzamiento == null || row.realLanzamiento == "") ? null : row.realLanzamiento) + ",";
			estructuraGuardar += ((row.planificadoRecepcionEval == null || row.planificadoRecepcionEval == "") ? null : row.planificadoRecepcionEval) + ",";
			estructuraGuardar += ((row.realRecepcionEval == null || row.realRecepcionEval == "") ? null : row.realRecepcionEval) + ",";
			estructuraGuardar += ((row.planificadoAdjudica == null || row.planificadoAdjudica == "") ? null : row.planificadoAdjudica) + ",";
			estructuraGuardar += ((row.realAdjudica == null || row.realAdjudica == "") ? null : row.realAdjudica) + ",";
			estructuraGuardar += ((row.planificadoFirma == null || row.planificadoFirma == "") ? null : row.planificadoFirma) + ",";
			estructuraGuardar += ((row.realFirma == null || row.realFirma == "") ? null : row.realFirma) + ",";
			estructuraGuardar += row.bloqueado;
			estructuraGuardar += "°";
		}
		
		$http.post('/SPlanAdquisiciones', {
			accion: 'guardarPlan',
			data : estructuraGuardar,
			t:moment().unix()
		}).success(function(response){
			if(response.success){
				$utilidades.mensaje('success','Plan de adquisiciones guardado exitosamente');
			}else{
				$utilidades.mensaje('danger','No se pudo guardar correctamente el plan de adquisiciones');
			}
			mi.mostrarGuardando = false;
			mi.mostrarTablas = true;
		})
	}
	
	mi.calcularPadre = function(idPredecesor, objetoTipoPredecesor){
		var padre = mi.obtenerEntidad(idPredecesor, objetoTipoPredecesor);
		if(padre != undefined){
			var hijos = padre.hijos;
			var total = 0;
			for(y in hijos){
				var h = mi.obtenerEntidad(hijos[y].split(',')[0],hijos[y].split(',')[1]);
				total += h.total;
			}
			padre.total = total;
			if(padre.predecesorId > 0 && padre.predecesorId){
				if(padre.predecesorId != 0){
					mi.calcularPadre(padre.predecesorId, padre.objetoPredecesorTipo);
				}
			}
		}
	}
	
	mi.obtenerEntidad = function(id, objetoTipo){
		for (x in mi.data){
			if (id == mi.data[x].objetoId && objetoTipo == mi.data[x].objetoTipo){
				return mi.data[x];
			}
		}
	}
	
	mi.generar = function(){
		if(mi.prestamo.value > 0){
			mi.mostrarCargando = true;
			mi.mostrarTablas = false;
			mi.idPrestamo = mi.prestamo.value;
			$http.post('/SPlanAdquisiciones',{
				accion: 'generarPlan',
				idPrestamo: mi.idPrestamo,
				informeCompleto: mi.informeCompleto,
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.proyecto);
					mi.fechaSuscripcion = moment(response.fechaSuscripcion,'DD/MM/YYYY').toDate();
					mi.fechaCierre = moment(response.fechaCierre,'DD/MM/YYYY').toDate();
					mi.mostrarCargando = false;
					mi.mostrarBotones = true;
					mi.mostrarTablas = true;
					mi.calcularTamanosPantalla();
				}
			});
		}
	}
	
	mi.uncheck = function(event){
		if (mi.checked == event.target.value)
	        mi.checked = false
	}
	
	mi.limpiar = function(){
		if (mi.datoSeleccionado != undefined){
			var param_data = {
	    			accion: 'eliminarPagos',
	    			idObjeto: mi.datoSeleccionado.objetoId,
	    			objetoTipo: mi.datoSeleccionado.objetoTipo,
	    			t:moment().unix()
	        	};
	        	$http.post('/SPago',param_data).then(function(response){
	        		if(response.data.success){
	        			mi.datoSeleccionado.tipoAdquisicion = 0;
	        			mi.datoSeleccionado.unidadMedida = null;
	        			mi.datoSeleccionado.categoriaAdquisicion = 0;
	        			mi.datoSeleccionado.cantidad = 0;
	        			mi.datoSeleccionado.costo = 0;
	        			mi.datoSeleccionado.total = 0;
	        			mi.datoSeleccionado.planificadoDocs = null;
	        			mi.datoSeleccionado.realDocs = null;
	        			mi.datoSeleccionado.planificadoLanzamiento = null;
	        			mi.datoSeleccionado.realLanzamiento = null;
	        			mi.datoSeleccionado.planificadoRecepcionEval = null;
	        			mi.datoSeleccionado.realRecepcionEval = null;
	        			mi.datoSeleccionado.planificadoAdjudica = null;
	        			mi.datoSeleccionado.realAdjudica = null;
	        			mi.datoSeleccionado.planificadoFirma = null;
	        			mi.datoSeleccionado.realFirma = null;
	        			
	        			mi.bloquearHijos(mi.datoSeleccionado.hijos, false);
	        			mi.desbloquearPadre(mi.datoSeleccionado, false);
	        		}
	        	});
		}
	}
	
	mi.exportarExcel = function(){
		var reporte = [];
		
		for(x in mi.gridOptions.data){
			var row = mi.gridOptions.data[x];
			reporte.push({nombre: row.nombre, tipoAdquisicion: row.tipoAdquisicion, planificadoDocs: row.planificadoDocs, realDocs: row.realDocs, planificadoLanzamiento: row.planificadoLanzamiento, realLanzamiento: row.realLanzamiento, planificadoRecepcionEval: row.planificadoRecepcionEval, realRecepcionEval: row.realRecepcionEval, planificadoAdjudica: row.planificadoAdjudica, realAdjudica: row.realAdjudica, planificadoFirma: row.planificadoFirma, realFirma: row.realFirma});
		}
		
		$http.post('/SPlanAdquisiciones',{
			accion: 'exportarExcel',
			data: JSON.stringify(reporte),
			t:moment().unix()
		}).then(
				  function successCallback(response) {
						var anchor = angular.element('<a/>');
					    anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'planAdquisiciones.xls'
					     })[0].click();
					  }.bind(this), function errorCallback(response){
					 		
					 	}
					 );
	}
	
	mi.crearArbol = function(datos){
		mi.data = datos;
		var tab = "\t";
		for(x in mi.data){
			mi.data[x].nombre = tab.repeat(mi.data[x].nivel -1) + mi.data[x].nombre;
		}
		
		mi.rowCollectionPrestamo = [];
		mi.rowCollectionPrestamo = mi.data;
		mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
		mi.mostrarDescargar = true;
	}
	
	mi.agregarPagos = function() {
		row = mi.datoSeleccionado;
		if (row != undefined){
			if(row.bloqueado != true){
				if(!isNaN(moment(row.planificadoAdjudica,'DD/MM/YYYY').toDate())){
					var modalInstance = $uibModal.open({
						animation : 'true',
						ariaLabelledBy : 'modal-title',
						ariaDescribedBy : 'modal-body',
						templateUrl : 'pago.jsp',
						controller : 'modalPago',
						controllerAs : 'controller',
						backdrop : 'static',
						size : 'lg',
						resolve : {
							idObjeto: function() {
								return row.objetoId;
							},
							objetoTipo: function(){
								return row.objetoTipo;
							},
							nombre: function(){
								return row.nombre;
							}
						}
					});

					modalInstance.result.then(function(resultado) {
						$utilidades.mensaje('success','Pagos agregados con éxito.');
					}, function() {
					});
				}else{
					$utilidades.mensaje('warning', 'Debe de ingresar fecha de \"Adjudicación\".');
				}
			}	
		}else
			$utilidades.mensaje('warning', 'Debe de seleccionar algún elemento de la tabla.');
	};
	
}]);

app.controller('modalPago', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log',   '$uibModal', '$q' ,'idObjeto','objetoTipo','nombre',modalPago ]);

function modalPago($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $uibModal, $q, idObjeto, objetoTipo, nombre) {

	var mi = this;
	mi.planAdquisicionesPagos = [];
	mi.idObjeto = idObjeto;
	mi.objetoTipo = objetoTipo;
	mi.nombre = nombre;
	mi.formatofecha = 'MMMM';
	mi.enMillones = false;
	mi.mostrarcargando = false;
	
	mi.fechaOptions = {
			formatYear : 'MMM',
		    startingDay: 1,
		    minMode: 'month',
		    language: 'es'
	};

	mi.ff_opciones = {
			formatYear : 'MMM',
		    startingDay: 1,
		    minMode: 'month',
	    	language: 'es'
	};
	
	mi.abrirPopupFecha = function(index) {
		if(index > 0 && index<1000){
			mi.camposdinamicos[index].isOpen = true;
		}
		else{
			switch(index){
				case 1000: mi.fi_abierto = true; break;
			}
		}
	};
	
	mi.cargarPagos = function(){
		mi.mostrarcargando = true;
		$http.post('/SPago',{ accion: 'getPagos', idObjeto:idObjeto,objetoTipo:objetoTipo,t:moment().unix()}).then(
				function(response) {
					if (response.data.success){
						for(x in response.data.pagos){
							response.data.pagos[x].pago = parseFloat(response.data.pagos[x].pago).toFixed(2);
							response.data.pagos[x].fecha = moment(moment(response.data.pagos[x].fechaReal,'DD/MM/YYYY').toDate()).format('MMMM');
						}
						mi.planAdquisicionesPagos = response.data.pagos;
						mi.mostrarcargando = false;
					}
			});	
	}
	
	
	mi.agregarPago = function(){
		mi.planAdquisicionesPagos.push({id:0,fecha: moment(mi.fechaPago).format('MMMM'), fechaReal: moment(mi.fechaPago).format('DD/MM/YYYY'), pago: parseFloat(mi.montoPago).toFixed(2), descripcion: mi.descripcion});
		
		mi.fechaPago = null;
		mi.montoPago = null;
		mi.descripcion = null;
	}
	
	mi.ok = function() {
		var param_data = {
				accion : 'guardarPagos',
				idObjeto: idObjeto,
				objetoTipo: objetoTipo,
				pagos: JSON.stringify(mi.planAdquisicionesPagos),
				t:moment().unix()
			};
			$http.post('/SPago',param_data).then(
				function(response) {
					if (response.data.success) {
						mi.planAdquisicionesPagos = response.data.pagos;
						$uibModalInstance.close(true);
					}else
						$uibModalInstance.close(false);
			});
	};
	
	mi.eliminarPago = function(row){
		var index = mi.planAdquisicionesPagos.indexOf(row);
        if (index !== -1) {
        	if(row.id != 0){
	        	var param_data = {
	    			accion: 'eliminarPago',
	    			idPago: row.id,
	    			t:moment().unix()
	        	};
	        	$http.post('/SPago',param_data).then(function(response){
	        		if(response.data.success){
	        			mi.planAdquisicionesPagos.splice(index, 1);
	        		}
	        	});
        	}else{
        		mi.planAdquisicionesPagos.splice(index, 1);
        	}
        }
	}

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.cargarPagos();
}

app.directive('focusOnShow', function($timeout) {
    return {
        restrict: 'A',
        link: function($scope, $element, $attr) {
            if ($attr.ngShow){
                $scope.$watch($attr.ngShow, function(newValue){
                    if(newValue){
                        $timeout(function(){
                            $element[0].focus();
                        }, 0);
                    }
                })      
            }
            if ($attr.ngHide){
                $scope.$watch($attr.ngHide, function(newValue){
                    if(!newValue){
                        $timeout(function(){
                            $element[0].focus();
                        }, 0);
                    }
                })      
            }

        }
    };
})

app.directive('nextOnTab', function () {
    return {
        restrict: 'A',
        link: function ($scope, selem, attrs) {
            selem.bind('keydown', function (e) {
                var code = e.keyCode || e.which;
                if (code === 9) {
                    e.preventDefault();
                    var pageElems = document.querySelectorAll('input, select, textarea, td'),
                        elem = e.srcElement
                        focusNext = false,
                        len = pageElems.length;
                    for (var i = 0; i < len; i++) {
                        var pe = pageElems[i];
                        if (focusNext) {
                            if (pe.style.display !== 'none') {
                                pe.focus();
                                break;
                            }
                        } else if (pe === e.srcElement) {
                            focusNext = true;
                        }
                    }
                }
            });
        }
    }
})

app.directive('scrollespejo', ['$window', function($window) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.bind('scroll', function() {
                var elemento = element[0];
                if (elemento.id == scope.divActivo){
      	          if(elemento.id == 'divTablaNombres'){
      	            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
      	          }else if(elemento.id == 'divTablaDatos'){
      	            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
      	            document.getElementById("divCabecerasDatos").scrollLeft = elemento.scrollLeft;
      	          }else{
      	            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
      	          }
      	        }
            });
            angular.element($window).bind('resize', function(){ 
                scope.controller.calcularTamanosPantalla();
                scope.$digest();
              });
            scope.$on('$destroy', function () { window.angular.element($window).off('resize');});
        }
    };
}])
;