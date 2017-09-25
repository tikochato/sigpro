var app = angular.module('sipro',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate',
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps','ng.deviceDetector','ui.grid.grouping','ui.grid.autoResize','ngFlash','ngUtilidades','documentoAdjunto','dialogoConfirmacion',
                                       'ngAria','ngMaterial','ngMessages','angucomplete-alt','ui.utils.masks','treeControl']);

app.config(['$routeProvider', '$locationProvider','FlashProvider',function ($routeProvider, $locationProvider,FlashProvider) {
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
		    .when('/gantt/:objeto_id/:objeto_tipo',{
            	template: '<div load-on-demand="\'ganttController\'" class="all_page"></div>'
            })
            .when('/prestamo/:id',{
            	template: '<div load-on-demand="\'proyectoController\'" class="all_page"></div>'
            })
            .when('/prestamometas/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'prestamometasController\'" class="all_page"></div>'
            })
            .when('/producto/:componente_id/:id',{
            	template: '<div load-on-demand="\'moduloProducto\'" class="all_page"></div>'
            })
            .when('/subproducto/:producto_id/:id',{
            	template: '<div load-on-demand="\'moduloSubproducto\'" class="all_page"></div>'
            })
            .when('/meta/:id/:tipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metaController\'" class="all_page"></div>'
            })
            .when('/metavalor/:metaid/:datotipoid/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metavalorController\'" class="all_page"></div>'
            })
            .when('/test',{
            	template: '<div load-on-demand="\'testController\'" class="all_page"></div>'
            })
            .when('/desembolso/:proyecto_id/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'desembolsoController\'" class="all_page"></div>'
            })
            .when('/componente/:proyecto_id/:id',{
            	template: '<div load-on-demand="\'componenteController\'" class="all_page"></div>'
            })
            .when('/riesgo/:objeto_id/:objeto_tipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgoController\'" class="all_page"></div>'
            })
            .when('/hito/:proyecto_id/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'hitoController\'" class="all_page"></div>'
            })
            .when('/recurso/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'recursoController\'" class="all_page"></div>'
            })
            .when('/actividad/:objeto_id/:objeto_tipo/:id',{
            	template: '<div load-on-demand="\'actividadController\'" class="all_page"></div>'
            })
            .when('/programa/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'programaController\'" class="all_page"></div>'
            })
            .when('/mapa/:proyecto_id/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'mapaController\'" class="all_page"></div>'
            })
            .when('/agenda/:proyectoId',{
            	template: '<div load-on-demand="\'agendaController\'" class="all_page"></div>'
            })
            .when('/matrizraci/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'matrizraciController\'" class="all_page"></div>'
            })
            .when("/:redireccion?",{
            	controller:"MainController"
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
	    	   name: 'ganttController',
	    	   script: '/app/components/gantt/gantt.controller.js',
	    	   template: '/app/components/gantt/gantt.jsp'
	       },
	       {
	    	   name: 'cooperanteController',
	    	   script: '/app/components/cooperante/cooperante.controller.js',
	    	   template: '/app/components/cooperante/cooperante.jsp'
	       },
	       {
	    	   name: 'proyectoController',
	    	   script: '/app/components/prestamo/proyecto.controller.js',
	    	   template: '/app/components/prestamo/proyecto.jsp'
	       }, {
	    	   name: 'prestamometasController',
	    	   script: '/app/components/reportes/prestamometas/prestamometas.controller.js',
	    	   template: '/app/components/reportes/prestamometas/prestamometas.jsp'
	       }, {
	    	   name: 'moduloColaborador',
	    	   script: '/app/components/colaborador/colaborador.controller.js',
	    	   template: '/app/components/colaborador/colaborador.jsp'
	       }, {
	    	   name: 'moduloProducto',
	    	   script: '/app/components/producto/producto.controller.js',
	    	   template: '/app/components/producto/producto.jsp'
	       }, {
	    	   name: 'moduloSubproducto',
	    	   script: '/app/components/subproducto/subproducto.controller.js',
	    	   template: '/app/components/subproducto/subproducto.jsp'
	       },
	       {
	    	   name: 'metaController',
	    	   script: '/app/components/meta/meta.controller.js',
	    	   template: '/app/components/meta/meta.jsp'
	       },{
	    	   name: 'metavalorController',
	    	   script: '/app/components/metavalor/metavalor.controller.js',
	    	   template: '/app/components/metavalor/metavalor.jsp'
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
	    	   name: 'recursoController',
	    	   script: '/app/components/recurso/recurso.controller.js',
	    	   template: '/app/components/recurso/recurso.jsp'
	       },
	       {
	    	   name: 'actividadController',
	    	   script: '/app/components/actividad/actividad.controller.js',
	    	   template: '/app/components/actividad/actividad.jsp'
	       },
	       {
	    	   name: 'programaController',
	    	   script: '/app/components/programa/programa.controller.js',
	    	   template: '/app/components/programa/programa.jsp'
	       },
	       {
	    	   name: 'mapaController',
	    	   script: '/app/components/mapas/mapa.controller.js',
	    	   template: '/app/components/mapas/mapa.jsp'
	       },
	       {
	    	   name: 'desembolsosController',
	    	   script: '/app/components/reportes/desembolsos/desembolsos.controller.js',
	    	   template: '/app/components/reportes/desembolsos/desembolsos.jsp'
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

app.controller('MainController',['$scope','$document','deviceDetector','$rootScope','$location','$window','Utilidades', "$routeParams","$http",
   function($scope,$document,deviceDetector,$rootScope,$location,$window,$utilidades, $routeParams,$http){
	$scope.lastscroll = 0;
	$scope.hidebar = false;
	
	mi = this;
	mi.treedata=[];
	mi.expanded=[];
	mi.proyectos=[];
	mi.proyecto=null;
	mi.nodo_seleccionado;
	
	mi.tree_options={
		
	};
	
	$rootScope.catalogo_entidades_anos=1;
	$rootScope.treeview = true;

	numeral.language('es', numeral_language);
	$window.document.title =  'MINFIN - '+$utilidades.sistema_nombre;
	
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
	}
	

	$scope.showBarFromMenu=function(){
		$scope.hidebar = false;
	}

	$scope.device = deviceDetector;
	
	$rootScope.$on( "$routeChangeStart", function(event, next, current) {
		if(mi.proyecto===undefined || mi.proyecto==null){
		  //event.preventDefault();  
		  $location.path('/main');
		}
	});
	
	

	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		if($routeParams.redireccion=="forbidden"){
			$utilidades.mensaje('danger','No tiene permiso de acceder a esta área');	
		}
		if (location.hostname !== "localhost" || location.hostname !== "127.0.0.1"){
			$window.ga('create', 'UA-74443600-2', 'auto');
			$window.ga('send', 'pageview', $location.path());
		}
    });
	
	mi.showSelected=function(nodo){
		mi.nodo_seleccionado = nodo;
		switch(nodo.objeto_tipo){
			case 1:
				$location.path('/prestamo/'+nodo.id); break;
			case 2:
				$location.path('/componente/'+nodo.parent.id+'/'+nodo.id); break;
			case 3:
				$location.path('/producto/'+nodo.parent.id+'/'+nodo.id); break;
			case 4:
				$location.path('/subproducto/'+nodo.parent.id+'/'+nodo.id); break;
			case 5:
				$location.path('/actividad/'+nodo.parent.id+'/'+nodo.parent.objeto_tipo+'/'+nodo.id); break;
		}
		
		
	}
	
	$http.post('/SProyecto', { accion: 'getProyectos'}).success(function(response) {
		mi.proyectos = response.entidades;
	});
	
	mi.setParentNode=function(nodo){
		for(var i=0; i<nodo.children.length;i++){
			nodo.children[i].parent = nodo;
			mi.setParentNode(nodo.children[i]);
		}
	}
	
	mi.cambioProyecto=function(selected){
		if(selected!==undefined){
			mi.proyecto = selected.originalObject;
			$http.post('/SProyecto',
					{ accion: 'controlArbol', id: mi.proyecto.id }).success(
				function(response) {
					mi.treedata=response.proyecto;
					if(mi.treedata.id==0){
						mi.expanded.push(mi.treedata.children[0]);
						mi.setParentNode(mi.treedata);
					}
				});
		}
	};
	
	$rootScope.$on("eliminarNodo", function(){
        mi.eliminaNodo();
     });
	
	$rootScope.$on("cambiarNombreNodo",function($evt, nombre){
		mi.cambiarNombreNodo(nombre);
	});
	
	mi.eliminaNodo=function(){
		if(mi.nodo_seleccionado){
			var parent = mi.nodo_seleccionado.parent;
			for(var i=0; i<parent.children.length;i++){
				if(parent.children[i].id==mi.nodo_seleccionado.id && parent.children[i].objeto_tipo==mi.nodo_seleccionado.objeto_tipo){
					parent.children.splice(i,1);
				}
			}
		}
	}
	
	mi.cambiarNombreNodo=function(nombre){
		if(mi.nodo_seleccionado){
			mi.nodo_seleccionado.nombre = nombre;
		}
	}
	
}]);
