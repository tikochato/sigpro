var app = angular.module('formaejemploController', [ 'ngTouch', 'ui.grid.edit' ]);

app
		.controller(
				'formaejemploController',
				[
						'$scope',
						'$http',
						'$interval',
						'$q',
						function($scope, $http, $interval, $q) {

							$scope.entityselected = null;
							$scope.isNew = false;
							$scope.fields = {};

							$http.post('/SFormaEjemplo',
									{ action : 'load' }).success(function(data) {
										$scope.fields = data;
							});

							$scope.today = function() {
								$scope.dt = new Date();
							};
							
							$scope.today();

							$scope.gridOptions = {
								enableRowSelection : true,
								enableRowHeaderSelection : false
							};

							$scope.gridOptions = {
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

							$http.post('/SFormaEjemploGrid')
									.success(
											function(data) {
												$scope.entidades = data.entidades;
												$scope.gridOptions.data = $scope.entidades;
											});

							$scope.gridOptions.multiSelect = false;
							$scope.gridOptions.modifierKeysToMultiSelect = false;
							$scope.gridOptions.noUnselect = true;
							$scope.gridOptions.onRegisterApi = function(gridApi) {
								$scope.gridApi = gridApi;

								gridApi.selection.on
										.rowSelectionChanged(
												$scope,
												function(row) {
													var msg = 'row selected '
															+ row;

													$scope.entityselected = row.entity;
													$scope.gender.selected = $scope.entityselected.gender;
												});

							};

							$scope.open2 = function() {
								$scope.popup2.opened = true;
							};

							$scope.dateOptions = {

								formatYear : 'yy',
								maxDate : new Date(2020, 5, 22),
								minDate : new Date(1900, 1, 1),
								startingDay : 1
							};

							$scope.popup2 = {
								opened : false
							};

							$scope.save = function() {
								if ($scope.isNew) {
									var param_data = {
										action : 'save',
										first_name : $scope.entityselected.nombre,
										last_name : $scope.entityselected.descripcion

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

															$scope.gridOptions.data
																	.push({
																		"id" : response.data.id,

																		"nombre" : $scope.entityselected.nombre,
																		"descripcion" : $scope.entityselected.descripcion

																	});
															$scope.isCollapsed = false;

														}

													});

								} else {
									var param_data = {
										action : 'update',
										emp_no : $scope.entityselected.id,
										first_name : $scope.entityselected.nombre,
										last_name : $scope.entityselected.descripcion

									};
									$http
											.post(
													'/SABCGrid',
													param_data)
											.then(function(response) {
												if (response.data.success) {
													/*
													 * for (var j = 0; j <
													 * $scope.gridOptions.data.length;
													 * j++) { if
													 * (response.data.id ==
													 * $scope.gridOptions.data[j].id) {
													 * $scope.gridOptions.data[j].nombre =
													 * $scope.entityselected.nombre;
													 * $scope.gridOptions.data[j].descripcion =
													 * $scope.descripcion; } }
													 */
													$scope.isCollapsed = false;
												}

											});
								}
							}

							$scope.deleteRow = function() {

								var param_data = {
									action : 'delete',
									emp_no : $scope.entityselected.id
								};
								$http
										.post(
												'/SABCGrid',
												param_data)
										.then(
												function(response) {

													if (response.data.success) {
														if ($scope.entityselected != null) {
															var index = $scope.gridOptions.data
																	.indexOf($scope.entityselected);
															if (index >= 0) {
																$scope.gridOptions.data
																		.splice(
																				index,
																				1);
															}
															$scope.entityselected == null;

														}

													}

												});

							};

							$scope.newRow = function() {

								var formularios = loadform();
								
								$scope.isCollapsed = true;
								$scope.entityselected = null;
								$scope.isNew = true;
							}

							$scope.editRow = function() {
								$scope.isCollapsed = true;
								$scope.isNew = false;
							}

							$scope.cancel = function() {
								$scope.isCollapsed = false;
								$scope.entityselected = null;
							}
							
							var deferred = $q.defer();
							$scope.currDate = new Date();
							$scope.onlyNumbers = /^[0-9]+$/;

							function loadform() {
								$http.post('/SFormaEjemplo',
										{action: 'load'}).success(function(data) {
									$scope.fields = data;
								});
							};

							


						} ]);