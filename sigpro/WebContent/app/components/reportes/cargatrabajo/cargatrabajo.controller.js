var app = angular.module('cargatrabajoController', ['ngTouch','smart-table']);
app.controller('cargatrabajoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter) {
	var mi = this;
	i18nService.setCurrentLang('es');
	
    mi.idTotal = 0;
    mi.responsableTotal = "Total";
    mi.actividadesAtrasadasTotal = 0;
    mi.actividadesProcesoTotal = 0;
    mi.exportar = false;

    mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
    
    mi.reiniciarVista=function(){
		if($location.path()=='/cargatrabajo/rv')
			$route.reload();
		else
			$location.path('/cargatrabajo/rv');
	}
    
	mi.tObjetos = [
		{value: 0,text: "Seleccione una Opción"},
		{value: 1,text: 'Préstamo'},
		{value: 2,text: 'Componente'},
		{value: 3,text: 'Producto'},
		{value: 4,text: 'Sub Producto'}
	];
	
	mi.tObjeto = mi.tObjetos[0];

	mi.obtenerMes = function(){
		var d = new Date();
		var month = new Array();
		month[0] = "Enero";
		month[1] = "Febrero";
		month[2] = "Marzo";
		month[3] = "Abril";
		month[4] = "Mayo";
		month[5] = "Junio";
		month[6] = "Julio";
		month[7] = "Agosto";
		month[8] = "Septiembre";
		month[9] = "Octubre";
		month[10] = "Noviembre";
		month[11] = "Diciembre";
		return month[d.getMonth()];
	}
	
	mi.mesActual = "";
	mi.mesActual = mi.obtenerMes();
	
	mi.reset = function(){
		mi.entidades = [
			{value: 0, text: 'Seleccione una opción'}
		]
		
		mi.unidadesEjecutoras = [
			{value: 0, text: "Seleccione una opción"}
		]
		
		mi.prestamos = [
			{value: 0,text: "Seleccione una opción"}
		];
		
		mi.componentes = [
			{value: 0,text: "Seleccione una opción"}
		];
		
		mi.productos = [
			{value: 0,text: "Seleccione una opción"}
		];
		
		mi.subProductos = [
			{value: 0,text: "Seleccione una opción"}
		];
		
		mi.entidad = mi.entidades[0];
		mi.unidadEjecutora = mi.unidadesEjecutoras[0];
		mi.prestamo = mi.prestamos[0];
		mi.componente = mi.componentes[0];
		mi.producto = mi.productos[0];
		mi.subProducto = mi.subProductos[0];
	}
	
	mi.displayObjeto = function(objetoSeleccionado){
		mi.mostrarcargando=false;
		if(objetoSeleccionado === 0){
			mi.entidadHide = false;
			mi.unidadEjecutoraHide = false;
			mi.prestamoHide = false;
			mi.componenteHide = false;
			mi.productoHide = false;
			mi.subProductoHide = false;
			mi.reset();
		}else if(objetoSeleccionado === 1){
			mi.entidadHide = true;
			mi.unidadEjecutoraHide = true;
			mi.prestamoHide = true;
			mi.componenteHide = false;
			mi.productoHide = false;
			mi.subProductoHide = false;
			mi.reset();
			mi.getEntidades();
		}else if(objetoSeleccionado === 2){
			mi.entidadHide = true;
			mi.unidadEjecutoraHide = true;
			mi.prestamoHide = true;
			mi.componenteHide = true;
			mi.productoHide = false;
			mi.subProductoHide = false;
			mi.reset();
			mi.getEntidades();
		}else if (objetoSeleccionado === 3){
			mi.entidadHide = true;
			mi.unidadEjecutoraHide = true;
			mi.prestamoHide = true;
			mi.componenteHide = true;
			mi.productoHide = true;
			mi.subProductoHide = false;
			mi.reset();
			mi.getEntidades();
		}else if (objetoSeleccionado === 4){
			mi.entidadHide = true;
			mi.unidadEjecutoraHide = true;
			mi.prestamoHide = true;
			mi.componenteHide = true;
			mi.productoHide = true;
			mi.subProductoHide = true;
			mi.reset();
			mi.getEntidades();
		}
	}
	
	mi.getEntidades = function(){
		$http.post('/SEntidad', {accion: 'cargar'}).success(
				function(response){
					mi.entidades = [];
					mi.entidades.push({'value' : 0, 'text' : 'Seleccione una opción'});
					if(response.success){
						for(var i=0; i< response.entidades.length; i++){
							mi.entidades.push({'value' : response.entidades[i].entidad, 'text' : response.entidades[i].nombre});
						}
						
						mi.entidad = mi.entidades[0];
					}
				}
		)
	}
	
	mi.getUnidadesEjecutoras = function(entidadId){
		$http.post('/SUnidadEjecutora', {accion: 'cargarPorEntidad', entidadId : entidadId}).success(
				function(response){
					mi.unidadesEjecutoras = [];
					mi.unidadesEjecutoras.push({'value' : 0, 'text' : 'Seleccione una opción'});
					if (response.success){
						for(var i = 0; i< response.unidadesEjecutoras.length; i++){
							mi.unidadesEjecutoras.push({'value' : response.unidadesEjecutoras[i].unidadEjecutora, 'text' : response.unidadesEjecutoras[i].nombreUnidadEjecutora});
						}
						
						mi.unidadEjecutora = mi.unidadesEjecutoras[0];
					}
				});
	}
	
	mi.getPrestamos = function(unidadEjecutoraId){
		$http.post('/SProyecto',{accion: 'getProyectosPorUnidadEjecutora', unidadEjecutoraId : unidadEjecutoraId}).success(
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
	}
	
	mi.getComponentes = function(prestamoId){
		$http.post('/SComponente',{accion: 'getComponentesPaginaPorProyecto', proyectoid: prestamoId}).success(
				function(response) {
					mi.componentes = [];
					mi.componentes.push({'value' : 0, 'text' : 'Seleccione una opción'});
					if (response.success){
						for (var i = 0; i < response.componentes.length; i++){
							mi.componentes.push({'value': response.componentes[i].id, 'text': response.componentes[i].nombre});
						}
						
						mi.componente = mi.componentes[0];
					}
				});
	}
	
	mi.getProductos = function(componenteId){
		mi.productos = [];
		mi.productos.push({'value' : 0, 'text' : 'Seleccione una opción'});
		mi.producto = mi.productos[0];

		if ((mi.tObjeto.value == 3 || mi.tObjeto.value == 4) && componenteId != 0){
			$http.post('/SProducto', {accion: 'listarProductos', componenteid:componenteId}).success(
					function(response){
						
						if (response.success){
							for (var i = 0; i < response.productos.length; i++){
								mi.productos.push({'value': response.productos[i].id, 'text': response.productos[i].nombre});
							}
							
							mi.producto = mi.productos[0];
						}
					});
		}
	}
	
	mi.getSubProductos = function(productoId){
		mi.subProductos = [];
		mi.subProductos.push({'value' : 0, 'text' : 'Seleccione una opción'});
		mi.subProducto = mi.subProductos[0];
		
		if(mi.tObjeto.value == 4 && productoId != 0){
			$http.post('/SSubproducto', {accion: 'cargar', componenteid: productoId}).success(
					function(response){
						if(response.success){
							for (var i = 0; i < response.subproductos.length; i++){
								mi.subProductos.push({'value': response.subproductos[i].id, 'text': response.subproductos[i].nombre});
							}
							
							mi.subProducto = mi.subProductos[0];
						}
					});
		}
	}
	
	mi.refrescar = function(){
		if (mi.tObjeto.value == 1 || mi.prestamo.value != 0){
			$http.post('/SReporte', 
			{
				accion: 'getCargaTrabajoPrestamo', 
				objetoTipo: mi.tObjeto.value,
				idPrestamo : mi.prestamo.value,
				idComponente : mi.componente.value,
				idProducto : mi.producto.value,
				idSubProducto : mi.subProducto.value
			}).success(function(response){
				if(response.success){
					mi.cargaTrabajo = [], obj_c_processed = [];
					
					var pos = 0;
					for(x in response.actividadesAtrasadas){
						var id = response.actividadesAtrasadas[x].id
						var responsable = response.actividadesAtrasadas[x].responsable;
						if(mi.existeResponsable(id) == -1){
							mi.cargaTrabajo.push({id: id, responsable: responsable, actividadesAtrasadas: 1, actividadesProceso: 0});
						}else{
							for(y in mi.cargaTrabajo){
								if(id == mi.cargaTrabajo[y].id)
									mi.cargaTrabajo[y].actividadesAtrasadas += 1;
							}
						}
					}
					
					for(x in response.actividadesProceso){
						var id = response.actividadesProceso[x].id
						var responsable = response.actividadesProceso[x].responsable;
						if(mi.existeResponsable(response.actividadesProceso[x].id) == -1)
							mi.cargaTrabajo.push({id: id, responsable: responsable, actividadesAtrasadas: 0, actividadesProceso: 1});
						else{
							for(y in mi.cargaTrabajo){
								if(id == mi.cargaTrabajo[y].id)
									mi.cargaTrabajo[y].actividadesProceso += 1;
							}
						}
					}
					
					mi.rowCollection = [];
					mi.rowCollection = mi.cargaTrabajo;
			        mi.displayedCollection = [].concat(mi.rowCollection);
			        
			        var totalAtrasadas = 0;
			        var totalProceso = 0;
			        
			        for (var i=0; i<mi.cargaTrabajo.length; i++){
			        	totalAtrasadas += Number(mi.cargaTrabajo[i].actividadesAtrasadas);
			        	totalProceso += Number(mi.cargaTrabajo[i].actividadesProceso);
			        }
			        
			        mi.idTotal = 0;
			        mi.responsableTotal = "Total";
			        mi.actividadesAtrasadasTotal = totalAtrasadas;
			        mi.actividadesProcesoTotal = totalProceso;
			        
			        mi.exportar = true;
				}
			});
		}
	}
	
	mi.existeResponsable = function(id){
		for(x in mi.cargaTrabajo){
			if (mi.cargaTrabajo[x].id == id)
				return x;
		}
		return -1;
	}
	
	mi.exportarExcel = function(){
		$http.post('/SReporte',{
			accion: 'exportarExcel',
			tipoReporte: 'cargaTrabajo',
			data: JSON.stringify(mi.cargaTrabajo),
			totales: "Total,"+mi.actividadesAtrasadasTotal+ ","+mi.actividadesProcesoTotal,
			mes: mi.mesActual,
			t:moment().unix()
		}).then(
				  function successCallback(response) {
						var anchor = angular.element('<a/>');
					    anchor.attr({
					         href: 'data:application/ms-excel;base64,' + response.data,
					         target: '_blank',
					         download: 'cargaTrabajo.xls'
					     })[0].click();
					  }.bind(this), function errorCallback(response){
					 		
					 	}
					 );
	}
	
	mi.reset();
}]);