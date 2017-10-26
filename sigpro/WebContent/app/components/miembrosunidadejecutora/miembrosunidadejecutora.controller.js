var app = angular.module('miembrosunidadejecutoraController', [ 'ngTouch','smart-table']);

app.controller('miembrosunidadejecutoraController',['$rootScope','$scope','$http','$interval','i18nService','Utilidades','documentoAdjunto','$routeParams','$window','$location','$route','uiGridConstants',
	'$mdDialog','$uibModal','$q','$filter', 'dialogoConfirmacion', 
	function($rootScope,$scope, $http, $interval,i18nService,$utilidades,$documentoAdjunto,$routeParams,
			$window,$location,$route,uiGridConstants,$mdDialog,$uibModal,$q,$filter, $dialogoConfirmacion) {

	
	var mi = this;
	mi.miembros = [];
	mi.colaboradores = [];
	mi.roles = [];
	mi.proyectoId = $routeParams.proyectoId;
	mi.proyectoNombre;
	mi.unidadEjecutoraNombre;
	
	mi.tamanoPantalla = Math.floor(document.getElementById("miemborsue").offsetWidth);
	mi.tamanoTotal = mi.tamanoPantalla - 300; 
	
	
	
	$scope.divActivo = "";
	mi.activarScroll = function(id){
		$scope.divActivo = id;
    }
	
	
	$http.post('/SProyecto', {
		accion: 'getProyectoPorId',
		id: mi.proyectoId,
		t:moment().unix()
	}).success(function(response){
		if(response.success){
			 mi.proyectoNombre = response.proyecto.nombre;
			 mi.unidadEjecutoraNombre = response.proyecto.unidadejecutora;
		}
		else
			$utilidades.mensaje('warning','No se encontraron datos para este proyecto');
	});
	
	
	
	$http.post('/SMiembrosUnidadEjecutora', {
		accion: 'getMiembros',
		proyectoId: mi.proyectoId,
		t:moment().unix()
	}).success(function(response){
		if(response.success){
			 mi.rowCollection = response.miembros;
			 for (x in mi.rowCollection){
				 mi.rowCollection[x].rolunidad = [];
				 mi.rowCollection[x].rolunidad.id =  mi.rowCollection[x].rolUnidadEjecutoraId;
				 mi.rowCollection[x].rolunidad.nombre = mi.rowCollection[x].rolUnidadEjecotoraNombre;
			 }
	         mi.displayedCollection = [].concat(mi.rowCollection);
	         mi.colaboradores = response.colaboradores;
	         mi.roles = response.roles;
		}
		else
			$utilidades.mensaje('warning','No se encontraron datos para este proyecto');
	});
	
	mi.seleccionarColaborador = function(pos){
		for (x in mi.colaboradores){
			if (mi.colaboradores[x].id == mi.rowCollection[pos].colaboradorId){
				mi.rowCollection[pos].email = mi.colaboradores[x].email;
				mi.rowCollection[pos].colaboradorNombre = mi.colaboradores[x].nombre;
				break;
			}
		}
	}
	
	mi.seleccionarRol = function(pos){
		for (x in mi.roles){
			if (mi.roles[x].id == mi.rowCollection[pos].rolunidad.id){
				mi.rowCollection[pos].rolUnidadEjecutoraId = mi.roles[x].id;
				mi.rowCollection[pos].rolUnidadEjecotoraNombre = mi.roles[x].nombre;
				break;
			}
		}
	}
	
	
	
	mi.guardar = function(valido){
		if (valido){
			
		
		var param_data = {
				accion : 'guardarMiembros',
				proyectoId: mi.proyectoId,
				miembros: JSON.stringify(mi.rowCollection),
				t:moment().unix()
		};
		$http.post('/SMiembrosUnidadEjecutora',param_data).then(
			function(response) {
				if(response.data.success){
					for (x in mi.rowCollection)
						mi.rowCollection[x].guardado = true;
					$utilidades.mensaje('success',"Miembros guardados con exito");
				}else{
					$utilidades.mensaje('danger','Error al guardar los miembros');
				}
			}
		);
		}
	};
	
	mi.eliminarMiembro = function(row){
		var index = mi.rowCollection.indexOf(row);
        if (index !== -1) {
            mi.rowCollection.splice(index, 1);
        }
	};
	
	mi.nuevoMiembro = function (){
		mi.rowCollection.push({  
			 id :null,
			 colaboradorId:null,
			 colaboradorNombre: "",
			 proyectoId: null,
			 estado: 1,
			 rolUnidadEjecutoraId: null,
			 rolUnidadEjecotoraNombre: "",
			 email: "",
			 guardado: false
		});
	}
}
]);