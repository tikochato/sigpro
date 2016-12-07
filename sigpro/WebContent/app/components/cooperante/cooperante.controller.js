var app = angular.module('cooperanteController', [ 'ngTouch' ]);

app.controller('cooperanteController',['$scope','$http','$interval','i18nService',
		function($scope, $http, $interval,i18nService) {
			var mi=this;
			
			i18nService.setCurrentLang('es');
			mi.mostrarcargando=true;
			mi.cooperantes = [];
			mi.cooperante;
			mi.mostraringreso=false;
			mi.esnuevo = false;
			
			mi.gridOptions = {
					enableRowSelection : true,
					enableRowHeaderSelection : false,
					multiSelect: false,
					modifierKeysToMultiSelect: false,
					noUnselect: true,
					enableFiltering: true,
					data: [],
					columnDefs : [ 
						{ name: 'id', width: 100, displayName: 'ID', cellClass: 'grid-align-right', type: 'number', enableFiltering: false },
						{ name: 'codigo', width: 150, displayName: 'Código', cellClass: 'grid-align-right' },
					    { name: 'nombre', width: 200, displayName: 'Nombre',cellClass: 'grid-align-left' },
					    { name: 'descripcion', displayName: 'Descripción', cellClass: 'grid-align-left', enableFiltering: false},
					    { name: 'usuarioCreo', displayName: 'Usuario Creación'},
					    { name: 'fechaCreacion', displayName: 'Fecha Creación', cellClass: 'grid-align-right', type: 'date', cellFilter: 'date:\'dd/MM/yyyy\''}
					]
				};
			
			mi.gridOptions.onRegisterApi = function(gridApi) {
				mi.gridApi = gridApi;
				gridApi.selection.on.rowSelectionChanged($scope,function(row) {
					mi.cooperante = row.entity;
				});
			};
			
			mi.cargarTabla = function(){
				mi.mostrarcargando=true;
				$http.post('/SCooperante', { accion: 'getCooperantes' }).success(
						function(response) {
							mi.cooperantes = response.cooperantes;
							mi.gridOptions.data = mi.cooperantes;
							mi.mostrarcargando = false;
						});
			}
			
			mi.cargarTabla();

			
			mi.guardar=function(){
				$http.post('/SCooperante', {
					accion: 'guardarCooperante',
					esnuevo: mi.esnuevo,
					id: mi.cooperante.id,
					codigo: mi.cooperante.codigo,
					nombre: mi.cooperante.nombre,
					descripcion: mi.cooperante.descripcion
				}).success(function(response){
					if(response.success){
						alert('Cooperante '+(mi.esnuevo ? 'creado' : 'guardado')+' con éxito');
						mi.cargarTabla();
					}
					else
						alert('Error al '+(mi.esnuevo ? 'creado' : 'guardado')+' el Cooperante')
				});
			};

			mi.borrar = function() {
				if(mi.cooperante!=null){
					$http.post('/SCooperante', {
						accion: 'borrarCooperante',
						id: mi.cooperante.id
					}).success(function(response){
						if(response.success){
							alert('Cooperante borrado con éxito');
							mi.cargarTabla();
						}
						else
							alert('Error al borrar el Cooperante');
					});
				}
				else
					alert('Debe seleccionar el Cooperante que desea borrar');
			};

			mi.nuevo = function() {
				mi.mostraringreso=true;
				mi.esnuevo = true;
				mi.cooperante = null;
				mi.gridApi.selection.clearSelectedRows();
			};

			mi.editar = function() {
				if(mi.cooperante!=null){
					mi.mostraringreso = true;
					mi.esnuevo = false;
				}
				else
					alert('Debe seleccionar el Cooperante que desea editar');
			}

			mi.cancelar = function() {
				mi.mostraringreso=false;
			}
			
			
			
		} ]);