var app = angular.module('cargatrabajoController', ['ngTouch','smart-table','ivh.treeview']);




app.controller('cargatrabajoController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location','$route','uiGridConstants','$mdDialog','$uibModal', '$document','$timeout','$q','$filter',
	function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$document,$timeout,$q,$filter,ivhTreeviewMgr) {
	var mi = this;
	i18nService.setCurrentLang('es');
	
    mi.idTotal = 0;
    mi.responsableTotal = "Total";
    mi.actividadesAtrasadasTotal = 0;
	mi.actividadesAlertaTotal = 0;
	mi.actividadesACumplirTotal = 0; 
	mi.actividadesCompletadas  = 0;
    mi.exportar = false;
    mi.grafica = true;
    mi.idPrestamo = 0;
    mi.estructuraProyecto=[];
    mi.objetosSeleccionados=[];
    mi.datosTabla = [];
    mi.mostrar = false;
    
    
    mi.pieColors = ['#febbbc','#eaeab0','#daefc4','#c4dbee'];
    
    	mi.optionsPie = {
			
			legend: {
				display: true,
				position: 'bottom'
			}
			  };

    mi.redireccionSinPermisos=function(){
		$window.location.href = '/main.jsp#!/forbidden';		
	}
    
    mi.reiniciarVista=function(){
		if($location.path()=='/cargatrabajo/rv')
			$route.reload();
		else
			$location.path('/cargatrabajo/rv');
	}
    
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
	
	
	
	$scope.toggle = function () {
	      mi.type = mi.type === 'polarArea' ?
	        'pie' : 'polarArea';
	};
	
	mi.charOptions= {
		legend: { display: true, position: 'top' }
	}
	
	mi.generar = function(){
		
		if (mi.tObjeto.value == 1 || mi.prestamo.value != 0){
			mi.grafica = true;
			mi.idPrestamo = mi.prestamo.value;
			mi.mostrar = false;
			
			$http.post('/SCargaTrabajo', {accion: 'getEstructrua', idPrestamo :mi.prestamo.value}).success(
					function(response){
						var estructura = response.estructura;
						
						mi.objetosSeleccionados.push({
							objetoId: estructura.objetoId,
							objetoTipo: estructura.objetoTipo
						})
						
						if (estructura.children!=null || estructura.children!= undefined)
							mi.agregarhijos(estructura.children);
						
						var idPrestamos = "";
						var idComponentes = "";
						var idProductos = "";
						var idSubproductos = "";
						
						for (x in mi.objetosSeleccionados){
							switch (mi.objetosSeleccionados[x].objetoTipo){
								case 1: idPrestamos = idPrestamos + (idPrestamos.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
								break;
								case 2: idComponentes = idComponentes + (idComponentes.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
								break;
								case 3: idProductos = idProductos + (idProductos.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
								break;
								case 4: idSubproductos = idSubproductos + (idSubproductos.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
								break;
							} 
						}
						
						$http.post('/SCargaTrabajo', 
								{
									accion: 'getCargaTrabajoPrestamo', 
									idPrestamos:idPrestamos,
									idComponentes:idComponentes,
									idProductos:idProductos,
									idSubproductos:idSubproductos
									
								}).success(function(response){
									if(response.success){
										mi.rowCollection = [];
										mi.rowCollection = response.cargatrabajo;
								        mi.displayedCollection = [].concat(mi.rowCollection);
								        mi.actividadesAtrasadasTotal = 0;
										mi.actividadesAlertaTotal = 0;
										mi.actividadesACumplirTotal = 0; 
										mi.actividadesCompletadas  = 0;
								        for (x in mi.rowCollection){
								        	mi.actividadesAtrasadasTotal = mi.actividadesAtrasadasTotal + mi.rowCollection[x].actividadesAtrasadas;
											mi.actividadesAlertaTotal = mi.actividadesAlertaTotal + mi.rowCollection[x].actividadesAlerta;
											mi.actividadesACumplirTotal = mi.actividadesACumplirTotal +  mi.rowCollection[x].actividadesACumplir; 
											mi.actividadesCompletadas = mi.actividadesCompletadas + mi.rowCollection[x].actividadesCompletadas;
								        }
								        
								        
								    	
								    	 mi.labels = ["Actividades retrasadas", "Actividades en alerta", "Actividades a cumplir","Actividades completadas"];
								    	 mi.data = [mi.actividadesAtrasadasTotal, mi.actividadesAlertaTotal,
								    		 mi.actividadesACumplirTotal,mi.actividadesCompletadas];
								    	 
								    	 mi.mostrar = true;
									}
								});
			});
			
			
			
			
		}
	};
	
	mi.agregarhijos = function (hijos){
		for (x in hijos){
			
			mi.objetosSeleccionados.push(
					{
						objetoId: hijos[x].objetoId,
						objetoTipo: hijos[x].objetoTipo
					}
			);
		
			if (hijos[x].children!=null || hijos[x].children!= undefined)
				mi.agregarhijos(hijos[x].children);
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
	
	// ------------
	mi.getEstructura = function (){
		mi.generar();
		
		
	}
	
	mi.llamarModal = function(idproyecto) {
		var resultado = $q.defer();
		var modalInstance = $uibModal.open({
			animation : 'true',
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'estructuraproyecto.jsp',
			controller : 'modalEstructura',
			controllerAs : 'estructura',
			backdrop : 'static',
			size : 'md',
			resolve : {
				$idproyecto : function() {
					return idproyecto;
				}				
			}
		});
		
		modalInstance.result.then(function(itemSeleccionado) {
			resultado.resolve(itemSeleccionado);
		});
		return resultado.promise;
	};
	
	mi.filtrarEstrucrura = function(titulo, mensaje){
		var resultado = mi.llamarModal(mi.prestamo.value); 
		mi.mostrar = false;
		resultado.then(function(itemSeleccionados) {
			mi.objetosSeleccionados = itemSeleccionados;
			var idPrestamos = "";
			var idComponentes = "";
			var idProductos = "";
			var idSubproductos = "";
			
			for (x in mi.objetosSeleccionados){
				switch (mi.objetosSeleccionados[x].objetoTipo){
					case 1: idPrestamos = idPrestamos + (idPrestamos.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
					break;
					case 2: idComponentes = idComponentes + (idComponentes.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
					break;
					case 3: idProductos = idProductos + (idProductos.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
					break;
					case 4: idSubproductos = idSubproductos + (idSubproductos.length > 0 ? "," : "") + mi.objetosSeleccionados[x].objetoId; 
					break;
				} 
			}
			
			$http.post('/SCargaTrabajo', 
					{
						accion: 'getCargaTrabajoPrestamo', 
						idPrestamos:idPrestamos,
						idComponentes:idComponentes,
						idProductos:idProductos,
						idSubproductos:idSubproductos
						
					}).success(function(response){
						if(response.success){
							mi.rowCollection = [];
							mi.rowCollection = response.cargatrabajo;
					        mi.displayedCollection = [].concat(mi.rowCollection);
					        mi.actividadesAtrasadasTotal = 0;
							mi.actividadesAlertaTotal = 0;
							mi.actividadesACumplirTotal = 0; 
							mi.actividadesCompletadas  = 0;
					        for (x in mi.rowCollection){
					        	mi.actividadesAtrasadasTotal = mi.actividadesAtrasadasTotal + mi.rowCollection[x].actividadesAtrasadas;
								mi.actividadesAlertaTotal = mi.actividadesAlertaTotal + mi.rowCollection[x].actividadesAlerta;
								mi.actividadesACumplirTotal = mi.actividadesACumplirTotal +  mi.rowCollection[x].actividadesACumplir; 
								mi.actividadesCompletadas = mi.actividadesCompletadas + mi.rowCollection[x].actividadesCompletadas;
					        }
					        
					        
					    	
					    	 mi.labels = ["Actividades retrasadas", "Actividades en alerta", "Actividades a cumplir","Actividades completadas"];
					    	 mi.data = [mi.actividadesAtrasadasTotal, mi.actividadesAlertaTotal,
					    		 mi.actividadesACumplirTotal,mi.actividadesCompletadas];
					    	 mi.mostrar = true;
						}
					});
			
			
		});
	};
	
	
	
	
	
	
	
}]);



app.controller('modalEstructura', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', '$idproyecto',modalEstructura ]);

function modalEstructura($uibModalInstance, $scope, $http, $interval,
	i18nService, $utilidades, $timeout, $log, $idproyecto) {

	var mi = this;
	
	mi.seleccionados = [];
	
	$http.post('/SCargaTrabajo', {accion: 'getEstructrua', idPrestamo :$idproyecto}).success(
			function(response){
				mi.estructuraProyecto = response.estructura;
				
	});

	

	mi.ok = function() {
		mi.seleccionados = [];
		
		mi.customOpts = {
			  useCheckboxes: false,
			  onToggle: mi.awesomeCallback,
			  twistieCollapsedTpl: '<span class="glyphicon glyphicon-chevron-right"></span>',
			  twistieExpandedTpl: '<span class="glyphicon glyphicon-chevron-down"></span>',
			  
			};
		
		if (mi.estructuraProyecto.selected)
			mi.seleccionados.push({objetoId: mi.estructuraProyecto.objetoId, objetoTipo: mi.estructuraProyecto.objetoTipo});
		
		if (mi.estructuraProyecto.children!=null || mi.estructuraProyecto.children!= undefined)
			mi.agregarhijos(mi.estructuraProyecto.children);
		
		if (mi.seleccionados.length > 0) {
			$uibModalInstance.close(mi.seleccionados);
		} else {
			$utilidades.mensaje('warning', 'Debe seleccionar una fila');
		}
	};

	mi.cancel = function() {
		$uibModalInstance.dismiss('cancel');
	};
	
	mi.agregarhijos = function (hijos){
		for (x in hijos){
			if (hijos[x].selected)
				mi.seleccionados.push(
						{
							objetoId: hijos[x].objetoId,
							objetoTipo: hijos[x].objetoTipo
						}
				);
			
			if (hijos[x].children!=null || hijos[x].children!= undefined)
				mi.agregarhijos(hijos[x].children);
		}
	}
};