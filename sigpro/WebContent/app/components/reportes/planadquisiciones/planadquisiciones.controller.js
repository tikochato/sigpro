var app = angular.module('planAdquisicionesController', ['ngTouch','ngAnimate','ui.grid.edit', 'ui.grid.rowEdit','ui.utils.masks']);
app.controller('planAdquisicionesController',['$scope', '$http', '$interval', 'uiGridTreeViewConstants','Utilidades','i18nService','uiGridConstants','$timeout', 'uiGridTreeBaseService', '$q','dialogoConfirmacion', '$filter','$uibModal',
	function($scope, $http, $interval, uiGridTreeViewConstants,$utilidades,i18nService,uiGridConstants,$timeout, uiGridTreeBaseService, $q, $dialogoConfirmacion, $filter,$uibModal) {
	var mi = this;
	mi.mostrarBotones = false;
	mi.mostrarcargando = false;
	mi.enMillones = true;
	mi.tooltipObjetoTipo = ["Prestamo","Componente","Producto","Sub Producto","Actividad"];
	mi.valoresInicializados = [0,0,0,0,0,"","","","","","","","","",""];
	mi.ddlOpciones = [];
	mi.ddlOpcionesTipos = [];
	mi.ddlOpcionesMetodo = [];
	i18nService.setCurrentLang('es');
	
	mi.calcularTamanosPantalla = function(){
		mi.tamanoPantalla = Math.floor(document.getElementById("reporte").offsetWidth);
		mi.tamanoTotal = mi.tamanoPantalla - 280; 
		mi.estiloCelda = "width:80px;min-width:80px; max-width:80px;text-align: center";
	}
	
	$scope.divActivo = "";
	mi.activarScroll = function(id){
		$scope.divActivo = id;
    }
	
	mi.prestamos = [
		{value: 0,text: "Seleccione una opción"}
	];	
	mi.prestamo = mi.prestamos[0];
	
	$http.post('/SProyecto',{accion: 'getProyectos'}).success(
		function(response) {
			mi.prestamos = [];
			mi.prestamos.push({'value' : 0, 'text' : 'Seleccione una opción'});
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
	
	mi.ddlOpcionesTipos = [{id: 0, value: "Seleccionar"}];
	mi.ddlOpcionesMetodo = [{id: 0, value: "Seleccionar"}];

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
	}
	
	mi.ocultar = function(row, id){
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
        	row.c6 = !row.c6;
        	break;
        case 7:
        	row.c7 = !row.c7;
        	break;
        case 8:
        	row.c8 = !row.c8;
        	break;
        case 9:
        	row.c9 = !row.c9;
        	break;
        case 10:
        	row.c10 = !row.c10;
        	break;
        case 11:
        	row.c11 = !row.c11;
        	break;
        case 12:
        	row.c12 = !row.c12;
        	break;
        case 13:
        	row.c13 = !row.c13;
        	break;
        case 14:
        	row.c14 = !row.c14;
        	break;
        case 15:
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
				}
			}else{
				var fechaActual = moment(row.realDocs,'DD/MM/YYYY').toDate();
				if (isNaN(fechaActual)){
					row.realDocs = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}
			}
			break;
		case 2:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoDocs,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoLanzamiento,'DD/MM/YYYY').toDate();
				
				if (!isNaN(fechaActual)){
					if(fechaAnterior.getTime() > fechaActual.getTime()){
						row.planificadoLanzamiento = "";
						$utilidades.mensaje('warning', 'Fecha de "Lanzamiento de eventos planificado" no debe ser menor que la fecha de "Preparación de documentos planificado"');					
					}
				}else{
					row.planificadoLanzamiento = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}					
			}else{
				var fechaAnterior = moment(row.realDocs,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realLanzamiento,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.realLanzamiento = "";
						$utilidades.mensaje('warning', 'Fecha de "Lanzamiento de eventos real" no debe ser menor que la fecha de "Preparación de documentos real"');
					}
				}else{
					row.realLanzamiento = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}					
			}
			break;
		case 3:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoLanzamiento,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoRecepcionEval,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.planificadoRecepcionEval = "";
						$utilidades.mensaje('warning', 'Fecha de "Recepción y evaluación de ofertas planificado" no debe ser menor que la fecha de "Landamiento de eventos planificado"');					
					}
				}else{
					row.planificadoRecepcionEval = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}					
			}else{
				var fechaAnterior = moment(row.realLanzamiento,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realRecepcionEval,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.realRecepcionEval = "";
						$utilidades.mensaje('warning', 'Fecha de "Recepción y evaluación de ofertas real" no debe ser menor que la fecha de "Landamiento de eventos real"');					
					}
				}else{
					row.realRecepcionEval = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}					
			}
			break;
		case 4:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoRecepcionEval,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoAdjudica,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.planificadoAdjudica = "";
						$utilidades.mensaje('warning', 'Fecha de "Adjudicación planificado" no debe ser menor que la fecha de "Recepción y evaluación de ofertas planificado"');					
					}
				}else{
					row.planificadoAdjudica = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}					
			}else{
				var fechaAnterior = moment(row.realRecepcionEval,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realAdjudica,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.realAdjudica = "";
						$utilidades.mensaje('warning', 'Fecha de "Adjudicación real" no debe ser menor que la fecha de "Recepción y evaluación de ofertas real"');					
					}
				}else{
					row.realAdjudica = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}
			}
			break;
		case 5:
			if(tipo==1){
				var fechaAnterior = moment(row.planificadoAdjudica,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.planificadoFirma,'DD/MM/YYYY').toDate();

				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.planificadoFirma = "";
						$utilidades.mensaje('warning', 'Fecha de "Firma de contrato planificado" no debe ser menor que la fecha de "Adjudicación planificado"');					
					}
				}else{
					row.planificadoFirma = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}
			}else{
				var fechaAnterior = moment(row.planificadoFirma,'DD/MM/YYYY').toDate();
				var fechaActual = moment(row.realFirma,'DD/MM/YYYY').toDate();
				
				if (!isNaN(fechaActual)){
					if(fechaAnterior > fechaActual){
						row.realFirma = "";
						$utilidades.mensaje('warning', 'Fecha de "Firma de contrato real" no debe ser menor que la fecha de "Adjudicación real"');					
					}
				}else{
					row.realFirma = "";
					$utilidades.mensaje('danger', 'Fecha invalida');
				}
			}
			break;
		}
	}
	
	mi.bloquearPadre = function(row){
		var predecesorId = row.predecesorId;
		var objetoPredecesorTipo = row.objetoPredecesorTipo;
		
		var entidad = mi.obtenerEntidad(predecesorId,objetoPredecesorTipo);
		if(entidad != undefined){
			entidad.bloqueado = true;
			mi.bloquearPadre(entidad);
		}
	}
	
	mi.desbloquearPadre = function(row){
		var predecesorId = row.predecesorId;
		var objetoPredecesorTipo = row.objetoPredecesorTipo;
		
		if(predecesorId != 0 && objetoPredecesorTipo != 0){
			var entidad = mi.obtenerEntidad(row.objetoId,row.objetoTipo);
			var entidadPadre = mi.obtenerEntidad(predecesorId,objetoPredecesorTipo);
			if((entidad.metodo == mi.valoresInicializados[0] || entidad.metodo == null) && (entidad.unidadMedida == mi.valoresInicializados[1] || entidad.unidadMedida == null) && (entidad.cantidad == mi.valoresInicializados[2] || entidad.cantidad == null) &&
					(entidad.costo == mi.valoresInicializados[3] || entidad.costo == null) && (entidad.total == mi.valoresInicializados[4] || entidad.total == null) && (entidad.planificadoDocs == mi.valoresInicializados[5] || entidad.planificadoDocs == null) && 
					(entidad.realDocs == mi.valoresInicializados[6] || entidad.realDocs == null) && (entidad.planificadoLanzamiento == mi.valoresInicializados[7] || entidad.planificadoLanzamiento == null) && 
					(entidad.realLanzamiento == mi.valoresInicializados[8] || entidad.realLanzamiento == null) && (entidad.planificadoRecepcionEval == mi.valoresInicializados[9] || entidad.planificadoRecepcionEval == null) && 
					(entidad.realRecepcionEval == mi.valoresInicializados[10] || entidad.realRecepcionEval == null) && (entidad.planificadoAdjudica == mi.valoresInicializados[11] || entidad.planificadoAdjudica == null) && 
					(entidad.realAdjudica == mi.valoresInicializados[12] || entidad.realAdjudica == null) && (entidad.planificadoFirma == mi.valoresInicializados[13] || entidad.planificadoFirma == null) && 
					(entidad.realFirma == mi.valoresInicializados[14] || entidad.realFirma == null)){
				
				entidadPadre.bloqueado = false;
			}
		}
	}
	
	mi.guardarPlan = function(){
		for(p in mi.data){
			var entidad = mi.data[p];
			$http.post('/SPlanAdquisiciones', {
	            accion: 'guardarPlan',
	            objetoId: entidad.objetoId,
	            objetoTipo: entidad.objetoTipo,
	            metodo: entidad.metodo,
	            tipoAdquisicion: entidad.tipoAdquisicion,
	            idPlanAdquisiciones: entidad.idPlanAdquisiciones,
	            unidadMedida: entidad.unidadMedida,
	            cantidad: entidad.cantidad,
	            costo: entidad.costo,
	            total: entidad.total,
	            planificadoDocs: moment(entidad.planificadoDocs).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoDocs).format('DD/MM/YYYY'),
        		realDocs: moment(entidad.realDocs).format('DD/MM/YYYY') == null ? null : moment(entidad.realDocs).format('DD/MM/YYYY'),
	            planificadoLanzamiento: moment(entidad.planificadoLanzamiento).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoLanzamiento).format('DD/MM/YYYY'),
	            realLanzamiento: moment(entidad.realLanzamiento).format('DD/MM/YYYY') == null ? null : moment(entidad.realLanzamiento).format('DD/MM/YYYY'),
	            planificadoRecepcionEval: moment(entidad.planificadoRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoRecepcionEval).format('DD/MM/YYYY'),
	            realRecepcionEval: moment(entidad.realRecepcionEval).format('DD/MM/YYYY') == null ? null : moment(entidad.realRecepcionEval).format('DD/MM/YYYY'),
	    		planificadoAdjudica: moment(entidad.planificadoAdjudica).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoAdjudica).format('DD/MM/YYYY'),
	            realAdjudica: moment(entidad.realAdjudica).format('DD/MM/YYYY') == null ? null : moment(entidad.realAdjudica).format('DD/MM/YYYY'),
	    		planificadoFirma: moment(entidad.planificadoFirma).format('DD/MM/YYYY') == null ? null : moment(entidad.planificadoFirma).format('DD/MM/YYYY'),
	            realFirma: moment(entidad.realFirma).format('DD/MM/YYYY') == null ? null : moment(entidad.realFirma).format('DD/MM/YYYY')
	        }).success(function(response){
	        	
	        }).error(function(response){
	        	
	        });
		}
		$utilidades.mensaje('success','Plan de adquisiciones guardado exitosamente');
	}
	
	mi.calcularPadre = function(idPredecesor, objetoTipoPredecesor){
		var padre = mi.obtenerEntidad(idPredecesor, objetoTipoPredecesor);
		if(padre != undefined){
			var hijo = padre.hijo;
			var total = 0;
			for(y in hijo){
				var h = mi.obtenerEntidad(hijo[y].split(',')[0],hijo[y].split(',')[1]);
				total += h.total;
			}
			padre.total = total;
			if(padre.idPredecesor > 0 && padre.idPredecesor){
				if(padre.idPredecesor != 0){
					mi.calcularPadre(padre.idPredecesor, padre.objetoTipoPredecesor);
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
			mi.mostrarcargando = true;
			mi.idPrestamo = mi.prestamo.value;
			$http.post('/SPlanAdquisiciones',{
				accion: 'generarPlan',
				idPrestamo: mi.idPrestamo,
				informeCompleto: mi.informeCompleto,
			}).success(function(response){
				if(response.success){
					mi.crearArbol(response.proyecto);
					mi.mostrarcargando = false;
					mi.mostrarBotones = true;
					mi.calcularTamanosPantalla();
				}
			});
		}
	}
	
	mi.limpiar = function(){
		mi.datoSeleccionado.metodo = 0;
		mi.datoSeleccionado.unidadMedida = null;
		mi.datoSeleccionado.tipoAdquisicion = 0;
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
	}
	
	mi.exportarExcel = function(){
		var reporte = [];
		
		for(x in mi.gridOptions.data){
			var row = mi.gridOptions.data[x];
			reporte.push({nombre: row.nombre, metodo: row.metodo, planificadoDocs: row.planificadoDocs, realDocs: row.realDocs, planificadoLanzamiento: row.planificadoLanzamiento, realLanzamiento: row.realLanzamiento, planificadoRecepcionEval: row.planificadoRecepcionEval, realRecepcionEval: row.realRecepcionEval, planificadoAdjudica: row.planificadoAdjudica, realAdjudica: row.realAdjudica, planificadoFirma: row.planificadoFirma, realFirma: row.realFirma});
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
			mi.data[x].nombre = tab.repeat(mi.data[x].objetoTipo -1) + mi.data[x].nombre;
		}
		
		mi.rowCollectionPrestamo = [];
		mi.rowCollectionPrestamo = mi.data;
		mi.displayedCollectionPrestamo = [].concat(mi.rowCollectionPrestamo);
		mi.mostrarDescargar = true;
	}
	
	mi.agregarPagos = function() {
		row = mi.datoSeleccionado;
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
			$utilidades.mensaje('success','Pagos agregados con éxito');
		}, function() {
		});
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
	mi.enMillones = true;
	mi.mostrarcargando = false;
	
	mi.fechaOptions = {
			formatYear : 'MMM',
		    startingDay: 1,
		    minMode: 'month'
	};

	mi.ff_opciones = {
			formatYear : 'MMM',
		    startingDay: 1,
		    minMode: 'month'
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
				idPlanAdquisiciones: idPlanAdquisiciones,
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
        }
    };
}])
;