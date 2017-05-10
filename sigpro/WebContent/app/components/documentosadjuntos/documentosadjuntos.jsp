<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div
	class="maincontainer all_page" id="title">
	<shiro:lacksPermission name="24010"> <!-- Cambiar el id del permiso al final de la cración de la pagian (Pendiente) -->
		<p ng-init="controller.redireccionSinPermisos()"></p>
	</shiro:lacksPermission>
    
    <div class="subtitulo">
			Documentos Adjuntos
		</div>
    <br />
    
    <div class="row second-main-form" align="center" style="width: 75%">
	    <div class="row col-sm-12">
	    	<h5 ng-show="doctos.isCollapsed">Agregar Documento</h5>
	    </div>
	    <form method="post" enctype="multipart/form-data;charset=UTF-8" name="dForm">
		    <div class="row col-sm-12">
				<div class="form-group" align="left">
					<input type="file" id="pickfile" name="pickfile" onchange="angular.element(this).scope().cargarDocumento(event)" file-upload required></input>
				</div>
				<div class="form-group" align="left">
  					<input type="text" class="inputText" ng-model="doctos.descripcion" ng-value="doctos.descripcion" onblur="this.setAttribute('value', this.value);"/>  
					<label for="nombre" class="floating-label">Descripción</label>					
				</div>
				<br>
				<div class="form-group" align="right">
					<div class="btn-group" role="group" aria-label="" align="left" uib-tooltip="Cargar archivo">
						<button type="button" 
							class="btn btn-primary" 
							ng-disabled="dForm.$invalid"
							ng-click="doctos.agregarDocumento()">
							<i class="glyphicon glyphicon-plus-sign"> </i>
							Cargar Archivo
						</button>
					</div>
				</div>
	    	</div>
    	</form>
    </div>
</div>