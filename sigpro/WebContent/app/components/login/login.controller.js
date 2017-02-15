/**
 * 
 */

var app = angular.module('sipro', [
	'ngTouch','ngUtilidades'
]);

app.controller('loginController', [
	'$scope','$http','Utilidades','$window',function($scope,$http,$utilidades,$window){
		
		$window.document.title = $utilidades.sistema_nombre + ' - Actividades';
		var mi = this;
		mi.sistema_nombre =  $utilidades.sistema_nombre;
		mi.username = "";
		mi.password = "";
		$scope.showerror = false;
		mi.login=function(){
			if(this.username!='' && this.password!=''){
				var data = { username: this.username, password: this.password};
				$http.post('/SLogin', data).then(function(response){
					if(response.data.success==true)
					   	window.location.href = '/main.jsp';
					 else
					    $scope.showerror = true;
				 	}, function errorCallback(response){
				 		$scope.showerror = true;
				 	}
				 );
			 }
		 }
	}
]);

