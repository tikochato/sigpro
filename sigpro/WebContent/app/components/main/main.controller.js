/**
 *
 */
var app = angular.module('sigpro',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate',
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps','ng.deviceDetector','ui.grid.grouping','ui.grid.autoResize','ngFlash','ngUtilidades','ngAria','ngMaterial','ngMessages']);

app.config(['$routeProvider', '$locationProvider','FlashProvider', function ($routeProvider, $locationProvider,FlashProvider) {
	   $locationProvider.hashPrefix('!');
	   //$locationProvider.html5Mode(true);
	   $routeProvider
	   		/*.when('/main',{
        		templateUrl : '',
        		resolve:{
        			main: function main(){
        				window.location.href = '/main.jsp';
        			}
        		}
        	})*/
		    .when('/formaejemplo',{
            	template: '<div load-on-demand="\'formaejemploController\'" class="all_page"></div>'
            })
            .when('/cooperante/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'cooperanteController\'" class="all_page"></div>'
            })
            .when('/proyecto/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectoController\'" class="all_page"></div>'
            })
            .when('/entidad',{
            	template: '<div load-on-demand="\'moduloEntidad\'" class="all_page"></div>'
            })
            .when('/unidadEjecutora',{
            	template: '<div load-on-demand="\'moduloUnidadEjecutora\'" class="all_page"></div>'
            })
            .when('/proyectotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectotipoController\'" class="all_page"></div>'
            })
            

            /*.when('/salir',{
            	templateUrl : '<div></div>',
            	resolve:{
            		logout: function logout($http){
            			$http.post('/SLogout', '').then(function(response){
	        				    if(response.data.success)
	        				    	window.location.href = '/login.jsp';
	        			 	}, function errorCallback(response){

	        			 	}
	        			 );
            			return true;
            		}
            	}
            });*/
            ;
	   FlashProvider.setTimeout(5000);
	   FlashProvider.setShowClose(true);
}]);

app.config(['$loadOnDemandProvider', function ($loadOnDemandProvider) {
	   var modules = [
	       {
	    	   name: 'formaejemploController',
	    	   script: '/app/components/formaejemplo/formaejemplo.controller.js',
	    	   template: '/app/components/formaejemplo/formaejemplo.jsp'
	       },
	       {
	    	   name: 'cooperanteController',
	    	   script: '/app/components/cooperante/cooperante.controller.js',
	    	   template: '/app/components/cooperante/cooperante.jsp'
	       },
	       {
	    	   name: 'proyectoController',
	    	   script: '/app/components/proyecto/proyecto.controller.js',
	    	   template: '/app/components/proyecto/proyecto.jsp'
	       }, {
	    	   name: 'moduloEntidad',
	    	   script: '/app/components/entidades/entidades.controller.js',
	    	   template: '/app/components/entidades/entidades.jsp'
	       }, {
	    	   name: 'moduloUnidadEjecutora',
	    	   script: '/app/components/unidadejecutora/unidadejecutora.controller.js',
	    	   template: '/app/components/unidadejecutora/unidadejecutora.jsp'
	       },
	       {
	    	   name: 'proyectotipoController',
	    	   script: '/app/components/proyecto/proyectotipo.controller.js',
	    	   template: '/app/components/proyecto/proyectotipo.jsp'
	       }
	   ];
	   $loadOnDemandProvider.config(modules);
}]);

app.config(['uiGmapGoogleMapApiProvider',function(uiGmapGoogleMapApiProvider) {
    uiGmapGoogleMapApiProvider.configure({
        key: 'AIzaSyBPq-t4dJ1GV1kdtXoVZfG7PtfEAHrhr00',
        v: '3.', //defaults to latest 3.X anyhow
        libraries: 'weather,geometry,visualization'
    });
}]);

app.controller('MainController',['$scope','$document','deviceDetector','$rootScope','$location','$window',
   function($scope,$document,deviceDetector,$rootScope,$location,$window){
	$scope.lastscroll = 0;
	$scope.hidebar = false;

	numeral.language('es', numeral_language);
	$window.document.title = 'MINFIN - SIGPRO';

	$document.bind('scroll', function(){
		if($document[0].body.scrollTop > 15){
			if ($scope.lastscroll>$document[0].body.scrollTop) { //Scroll to Top
		        $scope.hidebar = false;
		    } else if($document[0].body.scrollTop>15) { //Scroll to Bottom
		        $scope.hidebar = true;
		    }
			$scope.$apply();
		}
		$scope.lastscroll = $document[0].body.scrollTop;
	});

	$scope.hideBarFromMenu=function(){
		$scope.hidebar = true;
		document.getElementById("title").scrollIntoView()
	}

	$scope.device = deviceDetector;

	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		if (location.hostname !== "localhost" || location.hostname !== "127.0.0.1"){
			$window.ga('create', 'UA-74443600-2', 'auto');
			$window.ga('send', 'pageview', $location.path());
		}
    });
}]);
