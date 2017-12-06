<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="servlets.SLogin.stetiqueta" %>
var app = angular.module('sipro',['ngRoute','ui.bootstrap','chart.js', 'loadOnDemand','ngAnimate',
                                       'ui.grid', 'ui.grid.treeView', 'ui.grid.selection','ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.saveState','ui.grid.pinning',
                                       'uiGmapgoogle-maps','ng.deviceDetector','ui.grid.grouping','ui.grid.autoResize','ngFlash','ngUtilidades','documentoAdjunto','dialogoConfirmacion','historia',
                                       'ngAria','ngMaterial','ngMessages','angucomplete-alt','ui.utils.masks']);

app.config(['$routeProvider', '$locationProvider','FlashProvider',function ($routeProvider, $locationProvider,FlashProvider) {
	   $locationProvider.hashPrefix('!');
	   $locationProvider.html5Mode({ enabled: false, requireBase: false});
	   
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
            .when('/cooperante/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'cooperanteController\'" class="all_page"></div>'
            })
            .when('/pep/:prestamo_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectoController\'" class="all_page"></div>'
            })
            .when('/prestamometas',{
            	template: '<div load-on-demand="\'prestamometasController\'" class="all_page"></div>'
            })
            .when('/prestamoindicadores',{
            	template: '<div load-on-demand="\'prestamoindicadoresController\'" class="all_page"></div>'
            })
            .when('/entidad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloEntidad\'" class="all_page"></div>'
            })
            .when('/unidadejecutora/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloUnidadEjecutora\'" class="all_page"></div>'
            })
            .when('/colaborador/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloColaborador\'" class="all_page"></div>'
            })
            .when('/productotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'ProductoTipoController\'" class="all_page"></div>'
            })
            .when('/productopropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloProductoPropiedad\'" class="all_page"></div>'
            })
            .when('/producto/:objeto_id?/:objeto_tipo?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloProducto\'" class="all_page"></div>'
            })
            .when('/subproductotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloSubproductoTipo\'" class="all_page"></div>'
            })
            .when('/subproductopropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloSubproductoPropiedad\'" class="all_page"></div>'
            })
            .when('/subproducto/:producto_id/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'moduloSubproducto\'" class="all_page"></div>'
            })
            .when('/peptipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectotipoController\'" class="all_page"></div>'
            })
            .when('/desembolsotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'desembolsotipoController\'" class="all_page"></div>'
            })
            .when('/unidadmedida/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'unidadmedidaController\'" class="all_page"></div>'
            })
            .when('/metatipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metatipoController\'" class="all_page"></div>'
            })
            .when('/meta/:id/:tipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metaController\'" class="all_page"></div>'
            })
            .when('/metavalor/:metaid/:datotipoid/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metavalorController\'" class="all_page"></div>'
            })
            .when('/metas/:id/:tipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'metasController\'" class="all_page"></div>'
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
            .when('/subcomponente/:componente_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'subcomponenteController\'" class="all_page"></div>'
            })
            .when('/componentetipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'componentetipoController\'" class="all_page"></div>'
            })
            .when('/subcomponentetipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'subcomponentetipoController\'" class="all_page"></div>'
            })
            .when('/hitotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'hitotipoController\'" class="all_page"></div>'
            })
            .when('/componentepropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'componentepropiedadController\'" class="all_page"></div>'
            })  
            .when('/subcomponentepropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'subcomponentepropiedadController\'" class="all_page"></div>'
            })            
            .when('/riesgopropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgopropiedadController\'" class="all_page"></div>'
            })
             .when('/usuarios/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'usuarioController\'" class="all_page"></div>'
            })
            .when('/riesgotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgotipoController\'" class="all_page"></div>'
            })
            .when('/riesgo/:objeto_id/:objeto_tipo?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'riesgoController\'" class="all_page"></div>'
            })
            .when('/hito/:proyecto_id/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'hitoController\'" class="all_page"></div>'
            })
            .when('/recursotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'recursotipoController\'" class="all_page"></div>'
            })
            .when('/formulariotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'formulariotipoController\'" class="all_page"></div>'
            })
            .when('/prestamopropiedad/:reiniciar_vista?',{
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
            .when('/actividad/:objeto_id/:objeto_tipo/:reiniciar_vista?',{
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
            .when('/programapropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'programapropiedadController\'" class="all_page"></div>'
            })
            .when('/programatipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'programatipoController\'" class="all_page"></div>'
            })
            .when('/prestamo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'prestamoController\'" class="all_page"></div>'
            })
            .when('/mapa/:proyecto_id?/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'mapaController\'" class="all_page"></div>'
            })
            .when('/matrizriesgo/:proyectoId',{
            	template: '<div load-on-demand="\'matrizriesgoController\'" class="all_page"></div>'
            })
            .when('/agenda/:proyectoId',{
            	template: '<div load-on-demand="\'agendaController\'" class="all_page"></div>'
            })
            .when('/porcentajeactividades/:proyectoId?',{
            	template: '<div load-on-demand="\'porcentajeactividadesController\'" class="all_page"></div>'
            })
            .when('/avanceactividades/',{
            	template: '<div load-on-demand="\'avanceActividadesController\'" class="all_page"></div>'
            })
            .when('/cargatrabajo/',{
            	template: '<div load-on-demand="\'cargatrabajoController\'" class="all_page"></div>'
            })
            .when('/informacionPresupuestaria/',{
            	template: '<div load-on-demand="\'informacionPresupuestariaController\'" class="all_page"></div>'
            })
            .when("/administracionTransaccional/",{
            	template: '<div load-on-demand="\'administracionTransaccionalController\'" class="all_page"></div>'
            })
            .when('/responsabletipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'responsabletipoController\'" class="all_page"></div>'
            })
            .when('/responsablerol/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'responsablerolController\'" class="all_page"></div>'
            })
            .when('/planadquisiciones/',{
            	template: '<div load-on-demand="\'planAdquisicionesController\'" class="all_page"></div>'
            })
            .when('/planejecucion/',{
            	template: '<div load-on-demand="\'planejecucionController\'" class="all_page"></div>'
            })
            .when('/desembolsos/',{
            	template: '<div load-on-demand="\'desembolsosController\'" class="all_page"></div>'
            })
            .when('/matrizraci/',{
            	template: '<div load-on-demand="\'matrizraciController\'" class="all_page"></div>'
            })
            .when('/categoriaadquisicion/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'categoriaAdquisicionController\'" class="all_page"></div>'
            })
            .when('/tipoadquisicion/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'tipoAdquisicionController\'" class="all_page"></div>'
            })
            .when('/reportefinancieroadquisiciones/',{
            	template: '<div load-on-demand="\'reporteFinancieroAdquisicionesController\'" class="all_page"></div>'
            })
            .when('/flujocaja',{
            	template: '<div load-on-demand="\'flujocajaController\'" class="all_page"></div>'
            })
            .when('/prestamotipo/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'prestamotipoController\'" class="all_page"></div>'
            })
            .when('/rolunidadejecutora/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'rolunidadejecutoraController\'" class="all_page"></div>'
            })
            .when('/miembrosunidadejecutora/:proyectoId?',{
            	template: '<div load-on-demand="\'miembrosunidadejecutoraController\'" class="all_page"></div>'
            })
            .when('/gestionadquisiciones/',{
            	template: '<div load-on-demand="\'gestionAdquisicionesController\'" class="all_page"></div>'
            })
            .when('/planestructuralproyecto/',{
            	template: '<div load-on-demand="\'planEstructuralProyectoController\'" class="all_page"></div>'
            })
            .when('/informeGeneralPEP/',{
            	template: '<div load-on-demand="\'informeGeneralPEPController\'" class="all_page"></div>'
            })
            .when('/peppropiedad/:reiniciar_vista?',{
            	template: '<div load-on-demand="\'proyectopropiedadController\'" class="all_page"></div>'
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
	    	   script: '/app/components/pep/proyecto.controller.js',
	    	   template: '/app/components/pep/proyecto.jsp'
	       }, {
	    	   name: 'prestamometasController',
	    	   script: '/app/components/reportes/prestamometas/prestamometas.controller.js',
	    	   template: '/app/components/reportes/prestamometas/prestamometas.jsp'
	       }, {
	    	   name: 'prestamoindicadoresController',
	    	   script: '/app/components/reportes/prestamoindicadores/prestamoindicadores.controller.js',
	    	   template: '/app/components/reportes/prestamoindicadores/prestamoindicadores.jsp'
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
	    	   name: 'ProductoTipoController',
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
	       },{
	    	   name: 'moduloSubproductoTipo',
	    	   script: '/app/components/subproductotipo/subproductotipo.controller.js',
	    	   template: '/app/components/subproductotipo/subproductotipo.jsp'
	       }, {
	    	   name: 'moduloSubproductoPropiedad',
	    	   script: '/app/components/subproductopropiedad/subproductopropiedad.controller.js',
	    	   template: '/app/components/subproductopropiedad/subproductopropiedad.jsp'
	       }, {
	    	   name: 'moduloSubproducto',
	    	   script: '/app/components/subproducto/subproducto.controller.js',
	    	   template: '/app/components/subproducto/subproducto.jsp'
	       },
	       {
	    	   name: 'proyectotipoController',
	    	   script: '/app/components/pep/proyectotipo.controller.js',
	    	   template: '/app/components/pep/proyectotipo.jsp'
	       },
	       {
	    	   name: 'desembolsotipoController',
	    	   script: '/app/components/desembolso/desembolsotipo.controller.js',
	    	   template: '/app/components/desembolso/desembolsotipo.jsp'
	       },{
	    	   name: 'unidadmedidaController',
	    	   script: '/app/components/unidadmedida/unidadmedida.controller.js',
	    	   template: '/app/components/unidadmedida/unidadmedida.jsp'
	       },{
	    	   name: 'metatipoController',
	    	   script: '/app/components/metatipo/metatipo.controller.js',
	    	   template: '/app/components/metatipo/metatipo.jsp'
	       },
	       {
	    	   name: 'metaController',
	    	   script: '/app/components/meta/meta.controller.js',
	    	   template: '/app/components/meta/metas.jsp'
	       },{
	    	   name: 'metasController',
	    	   script: '/app/components/meta/metas.controller.js',
	    	   template: '/app/components/meta/metas.jsp'
	       },{
	    	   name: 'metavalorController',
	    	   script: '/app/components/metavalor/metavalor.controller.js',
	    	   template: '/app/components/metavalor/metavalor.jsp'
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
	    	   name: 'subcomponenteController',
	    	   script: '/app/components/subcomponente/subcomponente.controller.js',
	    	   template: '/app/components/subcomponente/subcomponente.jsp'
	       },
	       {
	    	   name: 'subcomponentetipoController',
	    	   script: '/app/components/subcomponentetipo/subcomponentetipo.controller.js',
	    	   template: '/app/components/subcomponentetipo/subcomponentetipo.jsp'
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
	       },
	       {
	    	   name: 'subcomponentepropiedadController',
	    	   script: '/app/components/subcomponentepropiedad/subcomponentepropiedad.controller.js',
	    	   template: '/app/components/subcomponentepropiedad/subcomponentepropiedad.jsp'
	       },{
	    	   name: 'recursounidadmedidaController',
	    	   script: '/app/components/recursounidadmedida/recursounidadmedida.controller.js',
	    	   template: '/app/components/recursounidadmedida/recursounidadmedida.jsp'
	       },{
	    	   name: 'recursopropiedadController',
	    	   script: '/app/components/recursopropiedad/recursopropiedad.controller.js',
	    	   template: '/app/components/recursopropiedad/recursopropiedad.jsp'
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
	    	   name: 'formulariotipoController',
	    	   script: '/app/components/formulariotipo/formulariotipo.controller.js',
	    	   template: '/app/components/formulariotipo/formulariotipo.jsp'
	       },
	       {
	    	   name: 'proyectopropiedadController',
	    	   script: '/app/components/prestamopropiedad/proyectopropiedad.controller.js',
	    	   template: '/app/components/prestamopropiedad/proyectopropiedad.jsp'
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
	       },
	       {
	    	   name: 'programapropiedadController',
	    	   script: '/app/components/programapropiedad/programapropiedad.controller.js',
	    	   template: '/app/components/programapropiedad/programapropiedad.jsp'
	       },
	       {
	    	   name: 'programatipoController',
	    	   script: '/app/components/programatipo/programatipo.controller.js',
	    	   template: '/app/components/programatipo/programatipo.jsp'
	       },
	       {
	    	   name: 'prestamoController',
	    	   script: '/app/components/prestamo/prestamo.controller.js',
	    	   template: '/app/components/prestamo/prestamo.jsp'
	       },
	       {
	    	   name: 'mapaController',
	    	   script: '/app/components/mapas/mapa.controller.js',
	    	   template: '/app/components/mapas/mapa.jsp'
	       },
	       {
	    	   name: 'porcentajeactividadesController',
	    	   script: '/app/components/reportes/porcentajeactividades/porcentajeactividades.controller.js',
	    	   template: '/app/components/reportes/porcentajeactividades/porcentajeactividades.jsp'
	       },
	       {
	    	   name: 'matrizriesgoController',
	    	   script: '/app/components/reportes/matrizriesgo/matrizriesgo.controller.js',
	    	   template: '/app/components/reportes/matrizriesgo/matrizriesgo.jsp'
	       },
	       {
	    	   name: 'agendaController',
	    	   script: '/app/components/reportes/agenda/agenda.controller.js',
	    	   template: '/app/components/reportes/agenda/agenda.jsp'
	       },{
	    	   name: 'avanceActividadesController',
	    	   script: '/app/components/reportes/avanceactividades/avanceActividades.controller.js',
	    	   template: '/app/components/reportes/avanceactividades/avanceActividades.jsp'
	       },{
	    	   name: 'cargatrabajoController',
	    	   script: '/app/components/reportes/cargatrabajo/cargatrabajo.controller.js',
	    	   template: '/app/components/reportes/cargatrabajo/cargatrabajo.jsp'
	       },{
	    	   name: 'informacionPresupuestariaController',
	    	   script: '/app/components/reportes/informacionPresupuestaria/informacionPresupuestaria.controller.js',
	    	   template: '/app/components/reportes/informacionPresupuestaria/informacionPresupuestaria.jsp'
	       },{
	    	   name: 'responsabletipoController',
	    	   script: '/app/components/responsabletipo/responsabletipo.controller.js',
	    	   template: '/app/components/responsabletipo/responsabletipo.jsp'
	       },{
	    	   name: 'responsablerolController',
	    	   script: '/app/components/responsablerol/responsablerol.controller.js',
	    	   template: '/app/components/responsablerol/responsablerol.jsp'
	       },{
	    	   name: 'planAdquisicionesController',
	    	   script: '/app/components/reportes/planadquisiciones/planadquisiciones.controller.js',
	    	   template: '/app/components/reportes/planadquisiciones/planadquisiciones.jsp'
	       },{
	    	   name: 'planejecucionController',
	    	   script: '/app/components/reportes/planejecucion/planejecucion.controller.js',
	    	   template: '/app/components/reportes/planejecucion/planejecucion.jsp'
	       },
	       {
	    	   name: 'desembolsosController',
	    	   script: '/app/components/reportes/desembolsos/desembolsos.controller.js',
	    	   template: '/app/components/reportes/desembolsos/desembolsos.jsp'
	       },{
	    	   name: 'administracionTransaccionalController',
	    	   script: '/app/components/reportes/administraciontransaccional/administracionTransaccional.controller.js',
	    	   template: '/app/components/reportes/administraciontransaccional/administracionTransaccional.jsp'
	       },
	       {
	    	   name: 'matrizraciController',
	    	   script: '/app/components/reportes/matrizraci/matrizraci.controller.js',
	    	   template: '/app/components/reportes/matrizraci/matrizraci.jsp'
	       },
	       {
	    	   name: 'categoriaAdquisicionController',
	    	   script: '/app/components/categoriaadquisicion/categoriaAdquisicion.controller.js',
	    	   template: '/app/components/categoriaadquisicion/categoriaAdquisicion.jsp'
	       },
	       {
	    	   name: 'tipoAdquisicionController',
	    	   script: '/app/components/tipoadquisicion/tipoAdquisicion.controller.js',
	    	   template: '/app/components/tipoadquisicion/tipoAdquisicion.jsp'
	       },
	       {
	    	   name: 'reporteFinancieroAdquisicionesController',
	    	   script: '/app/components/reportes/reportefinancieroadquisiciones/reporteFinancieroAdquisiciones.controller.js',
	    	   template: '/app/components/reportes/reportefinancieroadquisiciones/reporteFinancieroAdquisiciones.jsp'
	       }, {
	    	   name: 'flujocajaController',
	    	   script: '/app/components/reportes/flujocaja/flujocaja.controller.js',
	    	   template: '/app/components/reportes/flujocaja/flujocaja.jsp'
	       } , {
	    	   name: 'treePathController',
	    	   script: '/app/components/utilidades/calcularTreePath.controller.js',
	    	   template: '/app/components/utilidades/calcularTreePath.jsp'
	       }, 
	       {
	       		name: 'prestamotipoController',
	       		script: '/app/components/prestamo/prestamoTipo.controller.js',
	       		template: '/app/components/prestamo/prestamotipo.jsp'
	       }, 
	       {
	       		name: 'miembrosunidadejecutoraController',
	       		script: '/app/components/miembrosunidadejecutora/miembrosunidadejecutora.controller.js',
	       		template: '/app/components/miembrosunidadejecutora/miembrosunidadejecutora.jsp'
	       },
	       {
	    	   name: 'rolunidadejecutoraController',
	    	   script: '/app/components/rolunidadejecutora/rolunidadejecutora.controller.js',
	    	   template: '/app/components/rolunidadejecutora/rolunidadejecutora.jsp'
	       },
	       {
	    	   name: 'gestionAdquisicionesController',
	    	   script: '/app/components/reportes/gestionadquisiciones/gestionAdquisiciones.controller.js',
	    	   template: '/app/components/reportes/gestionadquisiciones/gestionAdquisiciones.jsp'
	       },
	       {
	    	   name: 'planEstructuralProyectoController',
	    	   script: '/app/components/reportes/planestructuraproyecto/planestructuralproyecto.controller.js',
	    	   template: '/app/components/reportes/planestructuraproyecto/planestructuralproyecto.jsp'
	       },
	       {
	    	   name: 'informeGeneralPEPController',
	    	   script: '/app/components/reportes/informeGeneralPEP/informeGeneralPEP.controller.js',
	    	   template: '/app/components/reportes/informeGeneralPEP/informeGeneralPEP.jsp'
	       },
	       {
	    	   name: 'proyectopropiedadController',
	    	   script: '/app/components/peppropiedad/proyectopropiedad.controller.js',
	    	   template: '/app/components/peppropiedad/proyectopropiedad.jsp'
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

app.controller('MainController',['$scope','$document','deviceDetector','$rootScope','$http','$location','$window','Utilidades', "$routeParams",
   function($scope,$document,deviceDetector,$rootScope,$http,$location,$window,$utilidades, $routeParams){
	$scope.lastscroll = 0;
	$scope.hidebar = false;
	
	$rootScope.catalogo_entidades_anos=1;
	$rootScope.treeview = false;
	
	numeral.language('es', numeral_language);
	$window.document.title =  'MINFIN - '+$utilidades.sistema_nombre;
		
	$rootScope.etiquetas={
			proyecto: '<% 
				if(session.getAttribute("sistemausuario")!=null){
					stetiqueta etiquetas=((stetiqueta)session.getAttribute("sistemausuario"));
					out.print(etiquetas.proyecto);
				}
			%>'
	}
	
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
	
	$rootScope.$on('$routeChangeSuccess', function (event, next) {
	      
		if($routeParams.redireccion=="forbidden"){
			$utilidades.mensaje('danger','No tiene permiso de acceder a esta Ã¡rea');	
		}
		if (location.hostname !== "localhost" || location.hostname !== "127.0.0.1"){
			$window.ga('create', 'UA-74443600-2', 'auto');
			$window.ga('send', 'pageview', $location.path());
		}
    });
}]);
