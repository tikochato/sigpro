var app = angular.module('testController', ['indeterminate','treeControl']);

app.controller('testController',['$scope','$http','$interval','i18nService','Utilidades','$routeParams','$window','$location',
		function($scope, $http, $interval,i18nService,$utilidades,$routeParams,$window,$location) {
			var mi=this;
			
			$window.document.title = 'SIGPRO - Test';
			
			mi.treedata=[];
			mi.expanded=[];
			
			mi.nodo_seleccionado;
			
			mi.tree_options={
				injectClasses:{
					liSelected: 'tree-node-noselected',
					labelSelected: 'tree-node-noselected',
				}
			};
			
			mi.setParentNode=function(nodo){
				for(var i=0; i<nodo.children.length;i++){
					nodo.children[i].parent = nodo;
					mi.setParentNode(nodo.children[i]);
				}
			}
			
			$http.post('/SProyecto',
					{ accion: 'controlArbolTodosProyectos', usuario: 'admin' }).success(
				function(response) {
					mi.treedata=response.proyectos;
					if(mi.treedata.id==0){
						mi.expanded.push(mi.treedata.children[0]);
						mi.setParentNode(mi.treedata);
					}
				});
			
			mi.showSelected=function(nodo){
				//console.log(nodo);
			};
			
			mi.onChange = function(nodo){
				if(nodo.estado==null)
					nodo.estado = false;
				mi.checkDescendientes(nodo);
				mi.checkAncestros(nodo);
			}
			
			mi.checkDescendientes=function(nodo){
				for(var i=0; nodo.children!== null && i<nodo.children.length; i++){
					nodo.children[i].estado = nodo.estado;
					mi.checkDescendientes(nodo.children[i]);
				}
			}
			
			mi.checkAncestros=function(nodo){
				estado = true;
				for(var i = 0; nodo.parent!=null && nodo.parent.children!= null && i<nodo.parent.children.length; i++){
					estado = nodo.parent.children[i].estado!=true ? null : estado;
				}
				estado_2 = false;
				for(var i = 0; nodo.parent!=null && nodo.parent.children!= null && i<nodo.parent.children.length; i++){
					estado_2 = nodo.parent.children[i].estado!=false ? null : estado_2;
				}
				estado = (estado_2==null ? estado : estado_2);
				if(nodo.parent!=null){
					nodo.parent.estado = estado;
					mi.checkAncestros(nodo.parent);
				}
			}
			
			mi.getChecksArbol = function(nodo){
				var ret='';
				for(var i=0; nodo.children!=null && i<nodo.children.length; i++){
					if(nodo.children[i].estado==null || nodo.children[i].estado){
						ret = ret + '|' + nodo.children[i].objeto_tipo + ',' + nodo.children[i].id;
					}
					var temp = mi.getChecksArbol(nodo.children[i]);
					ret =  (temp.length>0 ? ret + '|' + temp : ret);
				}
				return ret.substring(1);
			}
			
			mi.onOk = function(){
				var str = mi.getChecksArbol(mi.treedata);
				//console.log(str);
			}
			
		} ]);