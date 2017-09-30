<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/x-icon" href="/favicon.ico">
<%@ include file="/app/shared/includes.jsp"%>
<script src="/app/components/main_treeview/main_treeview.controller.js"></script>
<title></title>
</head>
<body ng-app="sipro" ng-controller="MainController as mainController">
<%@ include file="/app/components/menu/menu.jsp" %>
	<div id="mainview" class="all_page">
		<br/>
		<div class="row">
			<div class="col-sm-12">
			<div style="font-size: 11px; color: rgb(63,81,131); font-weight: bold; padding-left: 15px; margin-bottom: 5px;">Préstamo</div>
				<div angucomplete-alt id="ex1"
					  placeholder="Buscar Préstamos"
					  pause="100"
					  selected-object="mainController.cambioProyecto"
					  local-data="mainController.proyectos"
					  search-fields="nombre"
					  title-field="nombre"
					  minlength="1"
					  input-class="form-control form-control-small"
					  match-class="angucomplete-highlight"
					  initial-value="mainController.proyecto.nombre"></div>
			</div>
		</div>
		<div class="row">
			<div style="margin: 10px 0px 0px 15px; float: left; width: 250px; text-align: right;" class="horizontal-collapse" uib-collapse="mainController.hideTree" horizontal>
				<div class="btn-group btn-group-sm" ng-switch="mainController.nodo_seleccionado.objeto_tipo">
				       		<label ng-switch-when="1" class="btn btn-default" ng-click="mainController.nuevoObjeto(1)" uib-tooltip="Nuevo Componente" role="button" tabindex="0">
							<span class="glyphicon glyphicon-th"></span></label>
							<label ng-switch-when="2" class="btn btn-default" ng-click="mainController.nuevoObjeto(2)" uib-tooltip="Nuevo Producto" role="button" tabindex="0">
							<span class="glyphicon glyphicon-certificate"></span></label>
							<label ng-switch-when="3" class="btn btn-default" ng-click="mainController.nuevoObjeto(3)" uib-tooltip="Nuevo Subproducto" role="button" tabindex="0">
							<span class="glyphicon glyphicon-link"></span></label>
				       		<label ng-if="mainController.nodo_seleccionado" class="btn btn-default" ng-click="mainController.nuevoObjeto(4)" uib-tooltip="Nueva Actividad" role="button" tabindex="1">
							<span class="glyphicon glyphicon-th-list"></span></label>
				</div>
			</div>
			<div style="margin-top: 10px; height: 30px; width: 10px;"></div>
		</div>
		<div class="row cols_treeview">
			<div class="cols_treeview horizontal-collapse" uib-collapse="mainController.hideTree" horizontal style="margin: 10px 5px 0px 15px; float: left; width: 250px;">
					<div style="border: thin solid #c3c3c3; border-radius: 4px; overflow: auto; height: 100%;">
						<div treecontrol="" class="tree-light" tree-model="mainController.treedata" options="mainController.tree_options"  selected-node="mainController.nodo_seleccionado"
								expanded-nodes="mainController.nodos_expandidos" on-selection="mainController.showSelected(node)" style="width: 1000px; margin: 10px 0px 0px -5px;">
		     				  	  <span ng-switch="" on="node.objeto_tipo">
							             <span ng-switch-when="1" class="glyphicon glyphicon-record" aria-hidden="true" style="color: #4169E1;"></span>
							             <span ng-switch-when="2" class="glyphicon glyphicon-th" aria-hidden="true" style="color: #4169E1;"></span>
							             <span ng-switch-when="3" class="glyphicon glyphicon-certificate" aria-hidden="true" style="color: #4169E1;"></span>
							             <span ng-switch-when="4" class="glyphicon glyphicon-link" aria-hidden="true" style="color: #4169E1;"></span>
							             <span ng-switch-when="5" class="glyphicon glyphicon-th-list" aria-hidden="true" style="color: #4169E1;"></span>
							        </span>{{node.nombre}}
						</div>
	 				</div>
 			</div>
 			<div class="cols_treeview" style="float: left; margin-top: 10px; display: table;">
 				<div style="display: table-cell; vertical-align: middle; width:100%;" ng-click="mainController.hideTree=!mainController.hideTree">
 					<span ng-class="!mainController.hideTree ? 'glyphicon glyphicon-chevron-left' : 'glyphicon glyphicon-chevron-right'"></span>
 				</div>
 			</div>
			<div class="cols_treeview" style="margin: 10px 15px 0px 15px;">
				<div style="border: thin solid #c3c3c3; border-radius: 4px; height: 100%; overflow-y: scroll;">
					<div ng-view class="objeto"></div>
				</div>
			</div>
		</div>
    </div>
    <div class="div_alertas">
		<flash-message name="alertas">
		</flash-message>
	</div>
	<div class="footer"><div style="text-align: center; width: 100%;" class="label-form">- MINFIN 2017 -</div></div>
</body>
</html>

