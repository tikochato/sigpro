/**
 * 
 */

var app = angular.module('gestionUsuariosController', [
	'ngTouch','ngUtilidades'
]);

app.controller('gestionUsuariosController', [
	'$scope',
	'$http',
	'$interval',
	'$q',
	'i18nService',
	'Utilidades',
	'$routeParams',
	'uiGridConstants',
	'$mdDialog',
	'$window',
	'$location',
	'$route',
	'$q',
	'$uibModal',
	'dialogoConfirmacion',
	
	function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal, $dialogoConfirmacion){
		
		var mi=this;
		$window.document.title =$utilidades.sistema_nombre+' - Usuario';
		/** cambiar **/
		mi.isCollapsed=false;
		mi.esNuevo = false;
		/** fin  cambiar **/
		mi.cambio=false;
		mi.changeUsuario=function(input){
			
			mi.cambio=input;
		}
		/* usado para cambiar y seleccionar el contexto al cual se va activar */
		mi.colaboradoresCargados=false;
		mi.changeActive=function(input){
			mi.activo=input;
			if(input==2)
				mi.colaboradoresCargados=true;
		}
		
		i18nService.setCurrentLang('es');
		//configuración de tabla 1 -  usuarios
		
		
		
		
		
		
		
		
		
				
		function validarEmail(email) {
		    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		    return re.test(email);
		}
		
		
		mi.cargarTabla=function(tab){
			if(tab==1){
				//carga la tabla de usuarios
			}else{
				//carga la tabla de colaboradores
			}
		};
		
		mi.editarItem=function(tab){
			if(tab==1){
				//edita un usuario
			}else{
				//edita un colaborador
			}
		};
		mi.borrarItem=function(tab){
			if(tab==1){
				//borra un usuario
			}else{
				//borra un colaborador
			}
		};
		
		mi.reiniciarTabla=function(tab){
			if(tab==1){
				//reiniciar tabla de usuario
			}else{
				//reiniciar tabla de colaborador
			}
		};
		
		mi.cambiarPagina=function(tab){
			if(tab==1){
				//cambiar Pagina
			}else{
				//reiniciar tabla de colaborador
			}
		}
	}
]);

