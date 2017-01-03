var app = angular.module('permisoController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'permisoController',
 [
  '$scope',
  '$http',
  '$interval',
  '$q',
  'Utilidades',
  'uiGridConstants',
  '$mdDialog',
  function($scope, $http, $interval, $q,$utilidades,uiGridConstants,$mdDialog) {
	var mi=this;
	mi.entityselected = null;
	mi.isNew = false;
	mi.fields = {};
	mi.numeroMaximoPaginas = $utilidades.numeroMaximoPaginas;
	mi.elementosPorPagina = $utilidades.elementosPorPagina;
	mi.permisoSelected={id:"",nombre:"", descripcion:""};
	/*$http.post('/SPermiso',
	{ action : 'load' }).success(function(data) {
	mi.fields = data;
	});*/

	mi.today = function() {
		mi.dt = new Date();
	};
							
	mi.today();
							/*mi.gridOptions = {
								enableRowSelection : true,
								enableRowHeaderSelection : false
							};*/

	mi.gridOptions = {
		enableRowSelection : true,
		enableRowHeaderSelection : false,
		paginationPageSizes : [ 25, 50, 75 ],
		paginationPageSize : 25,
		data : [],
		columnDefs : [ {
			name : 'ID',
			field : 'id'
		}, {
			name : 'Nombre',
			field : 'nombre'
		}, {
			name : 'Descripcion',
			field : 'descripcion'
		}

		],

	};
	$http.post('/SPermiso',
			{ accion : 'getPermisos' }).success(function(data) {
			mi.gridOptions.data =  data.permisos;
	});
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
			mi.permisoSelected = row.entity;
			//mi.gender.selected = mi.entityselected.gender;
		});

	};

    mi.open2 = function() {
    	mi.popup2.opened = true;
	};

	mi.dateOptions = {
		formatYear : 'yy',
		maxDate : new Date(2020, 5, 22),
		minDate : new Date(1900, 1, 1),
		startingDay : 1
	};

	mi.popup2 = {
 	  opened : false
	};
						
						
	mi.deleteRow = function() {

		var param_data = {
			accion : 'delete',
			emp_no : mi.entityselected.id
		};
		$http.post(
			'/SABCGrid',
			param_data).then(
			 function(response) {
				if (response.data.success) {
					if (mi.entityselected != null) {
						var index = mi.gridOptions.data.indexOf(mi.entityselected);
						if (index >= 0) {
							mi.gridOptions.data.splice(index,1);
						}
						mi.entityselected == null;

					}
				}

		});

	};

	mi.newRow = function() {
		var formularios = loadform();						
		mi.isCollapsed = true;
		mi.entityselected = null;
		mi.isNew = true;
	}

    mi.editRow = function() {
		mi.isCollapsed = true;
		mi.isNew = false;
	}

	mi.cancel = function() {
		mi.isCollapsed = false;
		mi.entityselected = null;
	}
							
	var deferred = $q.defer();
	mi.currDate = new Date();
	mi.onlyNumbers = /^[0-9]+$/;
	function loadform() {
		$http.post('/SPermiso',
			{accion: 'getPermisos'}).success(
				function(data) {
					mi.fields = data;
		});
	};

	mi.nuevoPermiso=function(){
		var formularios = loadform();						
		mi.isCollapsed = true;
		mi.entityselected = null;
		mi.isNew = true;
		mi.permisoSelected.nombre="",
		mi.permisoSelected.descripcion="";
	};					
	mi.guardarPermiso=function(){		
		if(mi.permisoSelected.nombre!="" && mi.permisoSelected.descripcion!=""){
			if(mi.isNew){
				$http.post('/SPermiso',
					{
						accion: 'guardarPermiso',
						nombre:mi.permisoSelected.nombre,
						descripcion:mi.permisoSelected.descripcion
					}).success(
						function(data) {
							if(data.success){
								console.log(data);
								mi.gridOptions.data
								.push({
									"id" : data.data,
									"nombre" : mi.permisoSelected.nombre,
									"descripcion" : mi.permisoSelected.descripcion
								});
								mi.isCollapsed = false;
							}
				});
			}else{
				$http.post('/SPermiso',
						{
							accion: 'editarPermiso',
							id:mi.permisoSelected.id,
							nombre:mi.permisoSelected.nombre,
							descripcion:mi.permisoSelected.descripcion
						}).success(
							function(data) {
								if(data.success){
									/*mi.gridOptions.data
									.push({
										"id" : data.data,
										"nombre" : mi.permisoSelected.nombre,
										"descripcion" : mi.permisoSelected.descripcion
									});*/
									loadform();
									mi.isCollapsed = false;
								}
					});
			}
		}else{
			$utilidades.mensaje('danger','Llene los campos');
		}
	};
	mi.borrarPermiso=function(ev){
		mi.isCollapsed = false;
		if(mi.permisoSelected.id!=""){
			var confirm = $mdDialog.confirm()
	          .title('Confirmación de borrado')
	          .textContent('¿Desea borrar el permiso "'+mi.permisoSelected.nombre+'"?')
	          .ariaLabel('Confirmación de borrado')
	          .targetEvent(ev)
	          .ok('Borrar')
	          .cancel('Cancelar');
			$mdDialog.show(confirm).then(function() {
		    	$http.post('/SPermiso', {
					accion: 'eliminarPermiso',
					id: mi.permisoSelected.id
				}).success(function(response){
					console.log(response);
					if(response.success){
						$utilidades.mensaje('success','Permiso borrado con éxito');
						loadform();
						mi.permisoSelected={id:"",nombre:"", descripcion:""};
					}
					else
						$utilidades.mensaje('danger','Error al borrar el Permiso');
				});
		    }, function() {
		    
		    });
		}else{
			console.log(mi.permisoSelected);
		    $utilidades.mensaje('danger','Seleccione un permiso');
		}
	};
	
	
	mi.editarPermiso=function(){
		if(mi.permisoSelected.id!=""){
			mi.isCollapsed = true;
			mi.isNew=false;
		}else{
			$utilidades.mensaje('danger','Seleccione un permiso');
		}
	};
} ]);