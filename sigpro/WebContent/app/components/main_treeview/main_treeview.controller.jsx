<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="servlets.SLogin.stetiqueta" %>
var app = angular.module('sipro',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate',
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps','ng.deviceDetector','ui.grid.grouping','ui.grid.autoResize','ngFlash','ngUtilidades','documentoAdjunto','dialogoConfirmacion','historia',
                                       'ngAria','ngMaterial','ngMessages','angucomplete-alt','ui.utils.masks','treeControl','angularResizable']);

app.config(['$routeProvider', '$locationProvider','FlashProvider',function ($routeProvider, $locationProvider,FlashProvider) {
	   $locationProvider.hashPrefix('!');
	   
	$routeProvider
	   		.when('/pep/:prestamo_id/:id/:nuevo?',{
	       		template: '<div load-on-demand="\'proyectoController\'" class="all_page"></div>'
	       })
	       .when('/componente/:proyecto_id/:id/:nuevo?',{
            	template: '<div load-on-demand="\'componenteController\'" class="all_page"></div>'
            })
            .when('/subcomponente/:componente_id/:id/:nuevo?',{
            	template: '<div load-on-demand="\'subcomponenteController\'" class="all_page"></div>'
            })
	   		.when('/producto/:objeto_id/:objeto_tipo/:id/:nuevo?',{
            	template: '<div load-on-demand="\'moduloProducto\'" class="all_page"></div>'
            })
            .when('/subproducto/:producto_id/:id/:nuevo?',{
            	template: '<div load-on-demand="\'moduloSubproducto\'" class="all_page"></div>'
            })
            .when('/actividad/:objeto_id/:objeto_tipo/:id/:nuevo?',{
            	template: '<div load-on-demand="\'actividadController\'" class="all_page"></div>'
            })
            .when('/gantt/:objeto_id/:objeto_tipo',{
            	template: '<div load-on-demand="\'ganttController\'" class="all_page"></div>'
            })
            .when('/hito/:proyecto_id/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'hitoController\'" class="all_page"></div>'
            })
            .when("/:prestamo_id",{
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
	    	   name: 'proyectoController',
	    	   script: '/app/components/pep/proyecto.controller.js',
	    	   template: '/app/components/pep/proyecto.jsp'
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
	    	   name: 'componenteController',
	    	   script: '/app/components/componente/componente.controller.js',
	    	   template: '/app/components/componente/componente.jsp'
	       }, {
	    	   name: 'subcomponenteController',
	    	   script: '/app/components/subcomponente/subcomponente.controller.js',
	    	   template: '/app/components/subcomponente/subcomponente.jsp'
	       }, {
	    	   name: 'hitoController',
	    	   script: '/app/components/hito/hito.controller.js',
	    	   template: '/app/components/hito/hito.jsp'
	       },
	       {
	    	   name: 'actividadController',
	    	   script: '/app/components/actividad/actividad.controller.js',
	    	   template: '/app/components/actividad/actividad.jsp'
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
	mi.proyectos=[];
	mi.proyecto=null;
	mi.nodo_seleccionado;
	mi.nodos_expandidos=[];
	mi.prestamoid=null;
	
	mi.letter=/^[0-9]+$/;  
	if($location.$$path.substring(1).match(mi.letter))   
	{  
		mi.prestamoid=parseInt($location.$$path.substring(1));  
	}else{
		$window.location='/main.jsp#!/prestamo';
	}  
	
	mi.tree_options={
			allowDeselect: false
	};
	
	$rootScope.catalogo_entidades_anos=1;
	$rootScope.treeview = true;

	$rootScope.etiquetas={
			proyecto: '<% 
				if(session.getAttribute("sistemausuario")!=null){
					stetiqueta etiquetas=((stetiqueta)session.getAttribute("sistemausuario"));
					out.print(etiquetas.proyecto);
				}
			%>'
	}
	
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
		if((mi.proyecto=== undefined || mi.proyecto==null)){
			if(next!==undefined && next.$$route.originalPath!="/:prestamo_id" && next.$$route.originalPath!="/pep/:prestamo_id/:id?/:nuevo?")
				$location.path('/main');
			else if(next===undefined)
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
			case 0:
				$location.path('/pep/'+mi.prestamoid+'/'+nodo.id); break;
			case 1:
				$location.path('/componente/'+nodo.parent.id+'/'+nodo.id); break;
			case 2:
				$location.path('/subcomponente/'+nodo.parent.id+'/'+nodo.id); break;
			case 3:
				$location.path('/producto/'+nodo.parent.id+'/'+nodo.parent.objeto_tipo+'/'+nodo.id); break;
			case 4:
				$location.path('/subproducto/'+nodo.parent.id+'/'+nodo.id); break;
			case 5:
				$location.path('/actividad/'+nodo.parent.id+'/'+nodo.parent.objeto_tipo+'/'+nodo.id); break;
		}
		
		
	}
	
	$http.post('/SProyecto', { accion: 'getProyectos', prestamoid: mi.prestamoid }).success(function(response) {
		mi.proyectos = response.entidades;
		if(mi.proyectos.length==1){
			var selected = mi.proyectos[0];
			selected.originalObject = mi.proyectos[0];
			mi.cambioProyecto(selected);
		}
		if($location.$$path.substring(0,4)=='/pep' && $routeParams.id!==undefined && $routeParams.id>0){
			for(var i = 0; i< mi.proyectos.length; i++){
				if(mi.proyectos[i].id==$routeParams.id){
					mi.proyecto = mi.proyectos[i];
					$http.post('/SProyecto',
							{ accion: 'controlArbol', id: mi.proyecto.id }).success(
						function(response) {
							mi.treedata=response.proyecto;
							if(mi.treedata.id==0){
								mi.nodos_expandidos.push(mi.treedata.children[0]);
								mi.setParentNode(mi.treedata);
								mi.nodo_seleccionado = mi.treedata.children[0];
							}
						});
					break;
				}
			}
		}
	});
	
	mi.setParentNode=function(nodo){
		for(var i=0; i < nodo.children.length;i++){
			nodo.children[i].parent = nodo;
			mi.setParentNode(nodo.children[i]);
		}
	}
	
	mi.cambioProyecto=function(selected){
		if(mi.proyecto == null || (selected!==undefined && mi.proyecto!=null && selected.originalObject.id!=mi.proyecto.id)){
			mi.proyecto = selected.originalObject;
			mi.treedata=[];
			mi.nodos_expandidos=[];
			mi.nodo_seleccionado=null;
			$http.post('/SProyecto',
					{ accion: 'controlArbol', id: mi.proyecto.id }).success(
				function(response) {
					mi.treedata=response.proyecto;
					if(mi.treedata.id==0){
						mi.nodos_expandidos.push(mi.treedata.children[0]);
						mi.setParentNode(mi.treedata);
						mi.nodo_seleccionado = mi.treedata.children[0];
						$location.path('/pep/'+mi.prestamoid+'/'+mi.proyecto.id); 
					}
				});
		}
	};
	
	mi.recargarArbol=function(){
		mi.treedata=[];
		mi.nodos_expandidos=[];
		mi.nodo_seleccionado=null;
		$http.post('/SProyecto',
				{ accion: 'controlArbol', id: mi.proyecto.id }).success(
			function(response) {
				mi.treedata=response.proyecto;
				if(mi.treedata.id==0){
					mi.nodos_expandidos.push(mi.treedata.children[0]);
					mi.setParentNode(mi.treedata);
					mi.nodo_seleccionado = mi.treedata.children[0];
					$location.path('/pep/'+mi.proyecto.prestamoId + '/'+mi.proyecto.id); 
				}
			});
	}
	
	mi.nuevoObjeto=function(tipo){
		switch(tipo){
			case 0: //componente
				$location.path('/componente/'+mi.nodo_seleccionado.id+'/0/1'); break;
			case 1: //subcomponente
				$location.path('/subcomponente/'+mi.nodo_seleccionado.id+'/0/1'); break;
			case 2: //producto
				$location.path('/producto/'+mi.nodo_seleccionado.id+'/'+mi.nodo_seleccionado.objeto_tipo+'/0/1'); break;
			case 3: //subproducto
				$location.path('/subproducto/'+mi.nodo_seleccionado.id+'/0/1'); break;
			case 4: //actividad
				$location.path('/actividad/'+mi.nodo_seleccionado.id+'/'+mi.nodo_seleccionado.objeto_tipo+'/0/1'); break;
		}
	}
	
	$rootScope.$on("eliminarNodo", function(){
        mi.eliminaNodo();
     });
	
	$rootScope.$on("cambiarNombreNodo",function($evt, nombre){
		mi.cambiarNombreNodo(nombre);
	});
	
	$rootScope.$on("crearNodo", function($evt, datos){
		mi.crearNodo(datos.id, datos.nombre, datos.objeto_tipo, datos.estado);
	});
	
	$rootScope.$on("recargarArbol", function($evt,datos){
		mi.recargarArbol();
	});
	
	mi.eliminaNodo=function(){
		if(mi.nodo_seleccionado && mi.nodo_seleccionado.parent!=null){
			var parent = mi.nodo_seleccionado.parent;
			for(var i=0; i<parent.children.length;i++){
				if(parent.children[i].id==mi.nodo_seleccionado.id && parent.children[i].objeto_tipo==mi.nodo_seleccionado.objeto_tipo){
					parent.children.splice(i,1);
				}
			}
			mi.nodo_seleccionado = parent;
			mi.showSelected(mi.nodo_seleccionado);
		}
		else
			mi.treedata=[];
	}
	
	mi.cambiarNombreNodo=function(nombre){
		if(mi.nodo_seleccionado){
			mi.nodo_seleccionado.nombre = nombre;
		}
	}
	
	mi.crearNodo=function(id,nombre,objeto_tipo,estado){
		if(mi.nodo_seleccionado){
			if(mi.nodo_seleccionado.children==null || mi.nodo_seleccionado.children===undefined){
				mi.nodo_seleccionado.children=[];
			}
			mi.nodo_seleccionado.children.push({id: id, nombre: nombre, objeto_tipo: objeto_tipo, estado: estado, nivel: mi.nodo_seleccionado.nivel+1, parent: mi.nodo_seleccionado });
			mi.nodos_expandidos.push(mi.nodo_seleccionado);
			mi.nodo_seleccionado=mi.nodo_seleccionado.children[mi.nodo_seleccionado.children.length-1];
		}
	}
	
}]);
