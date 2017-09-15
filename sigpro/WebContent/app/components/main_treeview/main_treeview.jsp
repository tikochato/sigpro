<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/app/shared/includes.jsp"%>
<link rel="stylesheet" type="text/css" href="/assets/css/angular-tree/tree-control-attribute.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/angular-tree/tree-control.css" />
<script src="/assets/libs/angular-tree-control.js"></script>
<script src="/app/components/main_treeview/main_treeview.controller.js"></script>
<title></title>
</head>
<body ng-app="sipro" ng-controller="MainController as mainController">
<%@ include file="/app/components/menu/menu.jsp" %>
	<div id="mainview" class="all_page">
		<div class="row cols_treeview">
			<div class="col-sm-3" style="margin-top: 20px; height: 100%;">
				<div class="col-sm-12" style="border: thin solid #c3c3c3; height: 100%; overflow: auto;">
					<div treecontrol="" class="tree-light" tree-model="mainController.treedata" options="mainController.tree_options" 
							expanded-nodes="mainController.expanded" on-selection="mainController.showSelected(node)" style="width: 1000px; margin: 10px 0 0 -30px;">
	     				  	  <input type="checkbox" /><span ng-switch="" on="node.objeto_tipo">
						             <span ng-switch-when="1" class="glyphicon glyphicon-record" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="2" class="glyphicon glyphicon-th" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="3" class="glyphicon glyphicon-certificate" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="4" class="glyphicon glyphicon-link" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="5" class="glyphicon glyphicon-th-list" aria-hidden="true" style="color: #4169E1;"></span>
						        </span>{{node.nombre}}
					</div>
 				</div>
			</div>
			<div class="col-sm-9 " style="margin: 20px 0px 0px 0px; height: 100%;">
				<div class="col-sm-12" style="border: thin solid #c3c3c3; height: 100%;">
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

