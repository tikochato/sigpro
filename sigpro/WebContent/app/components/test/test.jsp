<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="testController as testc" class="maincontainer all_page" id="title">
		<h3>Test</h3><br/>
		<button type="button" class="btn btn-default btn-sm" ng-click="testc.onOk()">Get Checked</button>
		<div treecontrol="" class="tree-light" tree-model="testc.treedata" options="testc.tree_options" 
							expanded-nodes="testc.expanded" on-selection="testc.showSelected(node)" style="width: 1000px; margin: 10px 0 0 -30px;">
	     				  	  <span ng-switch="" on="node.objeto_tipo">
						             <span ng-switch-when="1" class="glyphicon glyphicon-record" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="2" class="glyphicon glyphicon-th" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="3" class="glyphicon glyphicon-certificate" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="4" class="glyphicon glyphicon-link" aria-hidden="true" style="color: #4169E1;"></span>
						             <span ng-switch-when="5" class="glyphicon glyphicon-th-list" aria-hidden="true" style="color: #4169E1;"></span>
						        </span><input type="checkbox" ng-model='node.estado' ng-change='testc.onChange(node)' indeterminate id="{{ node.objeto_tipo + '_' +node.id}}" />{{node.nombre}}
					</div>
		
	</div>
