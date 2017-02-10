var app = angular.module('usuarioInfoController', [ 'ngTouch', 'ui.grid.edit' ]);

app.controller(
 'usuarioInfoController',
 [
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
  function($scope, $http, $interval, $q,i18nService,$utilidades,$routeParams,uiGridConstants,$mdDialog, $window, $location, $route,$q,$uibModal) {
	var mi=this;
	$window.document.title = 'SIGPRO - Usuario';
	i18nService.setCurrentLang('es');
	mi.edicion=false;
	
   
	mi.usuarioActual={usuario:"", email:""};
	mi.esoculto= true;
	mi.tieneColaborador=false;
	
	mi.editar=function(){
		mi.esoculto=!mi.esoculto;
	};
	function validarEmail(email) {
	    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	    return re.test(email);
	}
	mi.guardarUsuario=function(){
		console.log("he");
		console.log(usuarioMail);
		if(mi.usuarioActual.email!==""){
			if(validarEmail(mi.usuarioActual.email)){
				if(usuarioMail!==mi.usuarioActual.email){
					$http.post('/SUsuario',
							{
								accion: 'editarUsuario',
								usuario:mi.usuarioActual.usuario,
								email:mi.usuarioActual.email
							}).success(
								function(data) {
									console.log(data);
									if(data.success){
										mi.esoculto=!mi.esoculto;
										$utilidades.mensaje('success','Cambios realizados correctamente.');
									}else {
										$utilidades.mensaje('danger','No se pudo realizar cambios.');
									}
									
						});
					
				}else{
					mi.esoculto=!mi.esoculto;
				}
			}else{
				$utilidades.mensaje('danger','correo electrónico no válido.');
			}
			
		}else{
			$utilidades.mensaje('danger','Los campos no deben de quedar vacios.');
		}
	};
	
	
	
	mi.cambiarPassword=function(){
		var modalInstance = $uibModal.open({
		    animation : 'true',
		    ariaLabelledBy : 'modal-title',
		    ariaDescribedBy : 'modal-body',
		    templateUrl : 'cambiarPassword.jsp',
		    controller : 'modalPassword',
		    controllerAs : 'modalPassword',
		    backdrop : 'static',
		    size : 'md',
		    resolve : {
		    	infoUsuario: function(){
		    		var parametros={ usuario:mi.usuarioActual.usuario};
		    		return  parametros;
		    	}
		    }

		});
		
		modalInstance.result.then(function(password) {
			console.log(password);
			if(password.password!==""){
				$http.post('/SUsuario', {accion: 'cambiarPassword' , usuario: password.usuario,	password:password.password}).success(
						function(response) {
							if(response.success){
								 $utilidades.mensaje('success', 'cambio de contraseña Exitoso.');
								 mi.esoculto=!mi.esoculto;
							}else{
								$utilidades.mensaje('danger', 'No se pudo cambiar la contraseña.');
							}
				});
			}
			
		}, function() {
		});
		
		
	};
	$http.post('/SUsuario', {accion: 'usuarioActual' }).success(
			function(response) {
				if(response.success){
					 mi.usuarioActual=response.usuario;
					 usuarioMail  =response.usuario.email;
					 if(response.usuario.pnombre!=null){
						 mi.tieneColaborador=true;
					 }
				}else{
					$utilidades.mensaje('danger', 'No se pudo cambiar la contraseña.');
				}
	});
	
	
} ]);




app.controller('modalPassword', [
	'$uibModalInstance', '$scope', '$http', '$interval', 'i18nService',
	'Utilidades', '$timeout', '$log','infoUsuario',modalPassword
]);
function modalPassword($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, infoUsuario) {
	
	var mi = this;
	mi.password={password1:"", password2:""};
	
     mi.ok = function() {
    	if (mi.password.password1!=="" && mi.password.password2!=="") {
    		if(mi.password.password1 === mi.password.password2){
    			$uibModalInstance.close({usuario:infoUsuario.usuario, password: mi.password.password1});
    		}else{
    			$uibModalInstance.close({usuario:infoUsuario.usuario, password: ""});
    			$utilidades.mensaje('danger', 'La contraseña y su confirmación no coinciden.');
    		}
    	} else {
    		$uibModalInstance.close({usuario:infoUsuario.usuario, password: ""});
    	    $utilidades.mensaje('danger', 'La contraseña no debe quedar vacia.');
    	}
     };

     mi.cancel = function() {
    	$uibModalInstance.dismiss('cancel');
     };
}
