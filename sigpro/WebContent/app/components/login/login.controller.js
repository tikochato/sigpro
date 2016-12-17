/**
 * 
 */

var app = angular.module('loginController', [
	'ngTouch'
]);

app.controller('loginController', [
	'$scope','$http',function($scope,$http){
		var mi = this;
		mi.username = "";
		mi.password = "";
		$scope.showerror = false;
		mi.login=function(){
			if(this.username!='' && this.password!=''){
				var data = { username: this.username, password: this.password};
				$http.post('/SLogin', data).then(function(response){
					console.log(response);
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

