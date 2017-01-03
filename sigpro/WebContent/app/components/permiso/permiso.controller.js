var app = angular.module('permisoController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'permisoController',
 [
  '$scope',
  '$http',
  '$interval',
  '$q',
  function($scope, $http, $interval, $q) {
	var mi=this;
	mi.entityselected = null;
	mi.isNew = false;
	mi.fields = {};

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
			name : 'Id',
			field : 'id'
		}, {
			name : 'Nombres',
			field : 'nombre'
		}, {
			name : 'Descripcion',
			field : 'descripcion'
		}

		],

	};
	$http.post('/SPermiso',
			{ action : 'getPermisos' }).success(function(data) {
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
    		console.log(row);
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
							
							/*
							mi.save = function() {
								if (mi.isNew) {
									var param_data = {
										action : 'save',
										first_name : mi.entityselected.nombre,
										last_name : mi.entityselected.descripcion

									};
									$http
											.post(
													'/SABCGrid',
													param_data)
											.then(
													function(response) {
														if (response.data.success) {
															var today = new Date();
															var dd = today
																	.getDate();
															var mm = today
																	.getMonth() + 1;

															var yyyy = today
																	.getFullYear();
															if (dd < 10) {
																dd = '0' + dd
															}
															if (mm < 10) {
																mm = '0' + mm
															}
															var today = dd
																	+ '/' + mm
																	+ '/'
																	+ yyyy;

															mi.gridOptions.data
																	.push({
																		"id" : response.data.id,

																		"nombre" : mi.entityselected.nombre,
																		"descripcion" : mi.entityselected.descripcion

																	});
															mi.isCollapsed = false;

														}

													});

								} else {
									var param_data = {
										action : 'update',
										emp_no : mi.entityselected.id,
										first_name : mi.entityselected.nombre,
										last_name : mi.entityselected.descripcion

									};
									$http
											.post(
													'/SABCGrid',
													param_data)
											.then(function(response) {
												if (response.data.success) {
													
													mi.isCollapsed = false;
												}

											});
								}
							}*/

	mi.deleteRow = function() {

		var param_data = {
			action : 'delete',
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
			{action: 'getPermisos'}).success(
				function(data) {
					mi.fields = data;
		});
	};

							


} ]);