<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/ng-template" id="cambiarPassword.jsp">
   	<%@ include file="/app/components/usuarios/cambiarPassword.jsp"%>
</script>
<style type="text/css">

.myGrid {
	width: 100%;
	height: 600px;
}
</style>

	<div ng-controller="usuarioInfoController as usuarioc" class="maincontainer all_page" id="title">
	<shiro:authenticated>
	<h3><%= session.getAttribute("usuario") %></h3><br/>
		
		<div class="row">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<label class="btn btn-primary" ng-click="usuarioc.editar()" ng-hide="!usuarioc.esoculto">Editar</label>
			        <label class="btn btn-success" ng-click="usuarioc.guardarUsuario()">Guardar</label>
			        <label class="btn btn-default" ng-click="usuarioc.cambiarPassword()" ng-hide="usuarioc.esoculto">Cambiar Contraseña</label>
			        <label class="btn btn-primary" ng-click="usuarioc.editar()" ng-hide="usuarioc.esoculto">cancelar</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form>
						<div class="form-group" >
							<label for="nombre">Usuario:</label>
							<div ng-hide="!usuarioc.esoculto">
								{{usuarioc.usuarioActual.usuario}}
							</div>
							
    						<input type="text" class="form-control" id="usuario" placeholder="Usuario" ng-model="usuarioc.usuarioActual.usuario" ng-hide="usuarioc.esoculto" readonly>
						</div>
						<div class="form-group">
							<label for="Descripcion">Correo</label>
							<div ng-hide="!usuarioc.esoculto">
								{{usuarioc.usuarioActual.email}}
							</div>
							
    						<input type="text" class="form-control" id="correo" placeholder="Correo electrónico" ng-model="usuarioc.usuarioActual.email" ng-hide="usuarioc.esoculto">
						</div>
				</form>
			</div>
			<!-- 
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="usuarioc.guardarUsuario()">Guardar</label>
			        <label class="btn btn-default" ng-click="usuarioc.cambiarPassword()" ng-show="!usuarioc.esNuevo">Cambiar Contraseña</label>
    		</div>
			 -->
    	</div>
	
	
	</shiro:authenticated>
		

	</div>
