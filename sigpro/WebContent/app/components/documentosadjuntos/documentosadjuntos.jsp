<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div
	class="maincontainer all_page" id="title" contenteditable="true">
	<shiro:lacksPermission name="24010"> <!-- Cambiar el id del permiso al final de la cración de la pagian (Pendiente) -->
		<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
    
    <h3>Documentos Adjuntos</h3>
    <br />
    
    <div class="row main-form" align="center" style="width: 75%">
	    <div class="row col-sm-12">
	    	<h5 ng-show="doctos.isCollapsed">Agregar Documento</h5>
	    </div>
	    <form method="post" enctype="multipart/form-data;charset=UTF-8">
	    <div class="row col-sm-12" contenteditable="true">
				<div class="form-group" align="left">
					<label for="nombre">Descripción</label>
  					<input type="text" class="form-control" placeholder="Descripción" ng-model="doctos.descripcion" ng-required="true"/>  					
				</div>
				<div class="form-group" align="left">
					<input type="file" id="pickfile" name="pickfile" onchange="angular.element(this).scope().cargarDocumento(event)" 
						file-upload></input>
				</div>
				<div class="form-group" align="left">
					<div class="btn-group" role="group" aria-label="" align="left">
						<button type="button" 
							class="btn btn-default" 
							ng-required="doctos.descripcion"
							ng-click="doctos.agregarDocumento()">
							<i class="glyphicon glyphicon-plus-sign"> </i>
						</button>
					</div>
				</div>
    	</div>
    	</form>
    </div>
</div>
<div class="modal-footer">
	<div class="col-sm-12 operation_buttons" align="right">
		<div class="btn-group">
			<button class="btn btn-primary" type="button" ng-click="doctos.ok()">Ok</button>
		</div>
	</div>
</div>
