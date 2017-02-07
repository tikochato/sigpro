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
            .when('/entidad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloEntidad\'" class="all_page"></div>'
            })
            .when('/unidadEjecutora/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloUnidadEjecutora\'" class="all_page"></div>'
            })
            .when('/colaborador/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloColaborador\'" class="all_page"></div>'
            })
            .when('/productoTipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloProductoTipo\'" class="all_page"></div>'
            })
            .when('/productoPropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloProductoPropiedad\'" class="all_page"></div>'
            })
            .when('/producto/:componente_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloProducto\'" class="all_page"></div>'
            })
            .when('/proyectotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectotipoController\'" class="all_page"></div>'
            })
            .when('/desembolsotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'desembolsotipoController\'" class="all_page"></div>'
            })
            .when('/metaunidadmedida/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metaunidadmedidaController\'" class="all_page"></div>'
            })
            .when('/metatipos/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metatipoController\'" class="all_page"></div>'
            })
            .when('/meta/:id/:tipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metaController\'" class="all_page"></div>'
            })
            .when('/test',{
            	template: '<div load-on-demand="\'testController\'" class="all_page"></div>'
            })
            .when('/desembolso/:proyecto_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'desembolsoController\'" class="all_page"></div>'
            })
            .when('/componente/:proyecto_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'componenteController\'" class="all_page"></div>'
            })
            .when('/componentetipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'componentetipoController\'" class="all_page"></div>'
            })
            .when('/hitotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'hitotipoController\'" class="all_page"></div>'
            })
            .when('/componentepropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'componentepropiedadController\'" class="all_page"></div>'
            })
            .when('/recursounidadmedida/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'recursounidadmedidaController\'" class="all_page"></div>'
            })
            .when('/riesgopropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgopropiedadController\'" class="all_page"></div>'
            })
            .when('/permisos/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'permisoController\'" class="all_page"></div>'
            })
             .when('/usuarios/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'usuarioController\'" class="all_page"></div>'
            })
            .when('/riesgotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgotipoController\'" class="all_page"></div>'
            })
            .when('/riesgo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgoController\'" class="all_page"></div>'
            })
            .when('/hito/:proyecto_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'hitoController\'" class="all_page"></div>'
            })
            .when('/recursotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'recursotipoController\'" class="all_page"></div>'
            })
            .when('/recursounidadmedida/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'recursounidadmedidaController\'" class="all_page"></div>'
            })
            .when('/formulariotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'formulariotipoController\'" class="all_page"></div>'
            })
            .when('/proyectopropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectopropiedadController\'" class="all_page"></div>'
            })
            .when('/usuarioinfo/',{
            	template: '<div load-on-demand="\'usuarioInfoController\'" class="all_page"></div>'
            })
            .when('/recurso/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'recursoController\'" class="all_page"></div>'
            })
             .when('/formularioitemtipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'formularioitemtipoController\'" class="all_page"></div>'
            })
            .when('/actividad/:objeto_id?/:objeto_tipo?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'actividadController\'" class="all_page"></div>'
            })
            .when('/actividadtipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'actividadtipoController\'" class="all_page"></div>'
            })
            .when('/actividadpropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'actividadpropiedadController\'" class="all_page"></div>'
            })
            .when('/formulario/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'formularioController\'" class="all_page"></div>'
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
	       }, {
	    	   name: 'moduloColaborador',
	    	   script: '/app/components/colaborador/colaborador.controller.js',
	    	   template: '/app/components/colaborador/colaborador.jsp'
	       }, {
	    	   name: 'moduloProductoTipo',
	    	   script: '/app/components/productotipo/productotipo.controller.js',
	    	   template: '/app/components/productotipo/productotipo.jsp'
	       }, {
	    	   name: 'moduloProductoPropiedad',
	    	   script: '/app/components/productopropiedad/productopropiedad.controller.js',
	    	   template: '/app/components/productopropiedad/productopropiedad.jsp'
	       }, {
	    	   name: 'moduloProducto',
	    	   script: '/app/components/producto/producto.controller.js',
	    	   template: '/app/components/producto/producto.jsp'
	       },
	       {
	    	   name: 'proyectotipoController',
	    	   script: '/app/components/proyecto/proyectotipo.controller.js',
	    	   template: '/app/components/proyecto/proyectotipo.jsp'
	       },
	       {
	    	   name: 'desembolsotipoController',
	    	   script: '/app/components/desembolso/desembolsotipo.controller.js',
	    	   template: '/app/components/desembolso/desembolsotipo.jsp'
	       },{
	    	   name: 'metaunidadmedidaController',
	    	   script: '/app/components/metaunidadmedida/metaunidadmedida.controller.js',
	    	   template: '/app/components/metaunidadmedida/metaunidadmedida.jsp'
	       },{
	    	   name: 'metatipoController',
	    	   script: '/app/components/metatipo/metatipo.controller.js',
	    	   template: '/app/components/metatipo/metatipo.jsp'
	       },
	       {
	    	   name: 'metaController',
	    	   script: '/app/components/meta/meta.controller.js',
	    	   template: '/app/components/meta/meta.jsp'
	       },{
	    	   name: 'testController',
	    	   script: '/app/components/test/test.controller.js',
	    	   template: '/app/components/test/test.jsp'
	       },
	       {
	    	   name: 'desembolsoController',
	    	   script: '/app/components/desembolso/desembolso.controller.js',
	    	   template: '/app/components/desembolso/desembolso.jsp'
	       },
	       {
	    	   name: 'componenteController',
	    	   script: '/app/components/componente/componente.controller.js',
	    	   template: '/app/components/componente/componente.jsp'
	       },
	       {
	    	   name: 'componentetipoController',
	    	   script: '/app/components/componentetipo/componentetipo.controller.js',
	    	   template: '/app/components/componentetipo/componentetipo.jsp'
	       },
	       {
	    	   name: 'hitotipoController',
	    	   script: '/app/components/hitotipo/hitotipo.controller.js',
	    	   template: '/app/components/hitotipo/hitotipo.jsp'
	       },
	       {
	    	   name: 'componentepropiedadController',
	    	   script: '/app/components/componentepropiedad/componentepropiedad.controller.js',
	    	   template: '/app/components/componentepropiedad/componentepropiedad.jsp'
	       },{
	    	   name: 'recursounidadmedidaController',
	    	   script: '/app/components/recursounidadmedida/recursounidadmedida.controller.js',
	    	   template: '/app/components/recursounidadmedida/recursounidadmedida.jsp'
	       },{
	    	   name: 'riesgopropiedadController',
	    	   script: '/app/components/riesgopropiedad/riesgopropiedad.controller.js',
	    	   template: '/app/components/riesgopropiedad/riesgopropiedad.jsp'
	       }, {
	    	   name: 'permisoController',
	    	   script: '/app/components/permiso/permiso.controller.js',
	    	   template: '/app/components/permiso/permiso.jsp'
	       }, {
	    	   name: 'usuarioController',
	    	   script: '/app/components/usuarios/usuario.controller.js',
	    	   template: '/app/components/usuarios/usuario.jsp'
         },{
	    	   name: 'riesgotipoController',
	    	   script: '/app/components/riesgotipo/riesgotipo.controller.js',
	    	   template: '/app/components/riesgotipo/riesgotipo.jsp'
	       }, {
	    	   name: 'riesgoController',
	    	   script: '/app/components/riesgo/riesgo.controller.js',
	    	   template: '/app/components/riesgo/riesgo.jsp'
	       }, {
	    	   name: 'hitoController',
	    	   script: '/app/components/hito/hito.controller.js',
	    	   template: '/app/components/hito/hito.jsp'
	       },
	       {
	    	   name: 'recursotipoController',
	    	   script: '/app/components/recursotipo/recursotipo.controller.js',
	    	   template: '/app/components/recursotipo/recursotipo.jsp'
	       },
	       {
	    	   name: 'recursounidadmedidaController',
	    	   script: '/app/components/recursounidadmedida/recursounidadmedida.controller.js',
	    	   template: '/app/components/recursounidadmedida/recursounidadmedida.jsp'
	       },
	       {
	    	   name: 'formulariotipoController',
	    	   script: '/app/components/formulariotipo/formulariotipo.controller.js',
	    	   template: '/app/components/formulariotipo/formulariotipo.jsp'
	       },
	       {
	    	   name: 'proyectopropiedadController',
	    	   script: '/app/components/proyectopropiedad/proyectopropiedad.controller.js',
	    	   template: '/app/components/proyectopropiedad/proyectopropiedad.jsp'
	       },
	       {
	    	   name: 'usuarioInfoController',
	    	   script: '/app/components/usuarios/usuarioInfo.controller.js',
	    	   template: '/app/components/usuarios/usuarioInfo.jsp'
	       },
	       {
	    	   name: 'recursoController',
	    	   script: '/app/components/recurso/recurso.controller.js',
	    	   template: '/app/components/recurso/recurso.jsp'
	       },
	       {
	    	   name: 'formularioitemtipoController',
	    	   script: '/app/components/formularioitemtipo/formularioitemtipo.controller.js',
	    	   template: '/app/components/formularioitemtipo/formularioitemtipo.jsp'
	       },
	       {
	    	   name: 'actividadController',
	    	   script: '/app/components/actividad/actividad.controller.js',
	    	   template: '/app/components/actividad/actividad.jsp'
	       },
	       {
	    	   name: 'actividadtipoController',
	    	   script: '/app/components/actividadtipo/actividadtipo.controller.js',
	    	   template: '/app/components/actividadtipo/actividadtipo.jsp'
	       },
	       {
	    	   name: 'actividadpropiedadController',
	    	   script: '/app/components/actividadpropiedad/actividadpropiedad.controller.js',
	    	   template: '/app/components/actividadpropiedad/actividadpropiedad.jsp'
	       },
	       {
	    	   name: 'formularioController',
	    	   script: '/app/components/formulario/formulario.controller.js',
	    	   template: '/app/components/formulario/formulario.jsp'
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