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
			        <label class="btn btn-success" ng-show="!usuarioc.esoculto" ng-click="usuarioc.guardarUsuario()">Guardar</label>
			        <label class="btn btn-success" ng-click="usuarioc.cambiarPassword()" ng-hide="usuarioc.esoculto">Cambiar Contraseña</label>
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
						<div class="row">
							<div class="form-group col-sm-6">
								<label for="Descripcion">Correo</label>
								<div ng-hide="!usuarioc.esoculto">
									{{usuarioc.usuarioActual.email}}
								</div>
							</div>
							<div class="form-group col-sm-6" ng-show="!usuarioc.esoculto">
								<label for="Descripcion">Password</label>
								<input ng-hide="!usuarioc.esoculto" type="password" ng-mode="usuarioc.usuarioActual.password">
									
							</div>
							
							
    						<input type="text" class="form-control" id="correo" placeholder="Correo electrónico" ng-model="usuarioc.usuarioActual.email" ng-hide="usuarioc.esoculto">
						</div>
						<div ng-show="!usuarioc.esoculto">
							<div class="row">
								
						      <div class="form-group col-sm-3" >
						        <label for="campo1">* Primer Nombre:</label> 
						        <input type="text" class="form-control" id="campo1" name="campo1" placeholder="Primer Nombre" ng-model="usuarioc.usuarioActual.pnombre" required />
						      </div>
						
						      <div class="form-group col-sm-3" >
						        <label for="campo2">Segundo Nombre:</label> 
						        <input type="text" class="form-control" id="campo2" name="campo2" placeholder="Segundo Nombre" ng-model="usuarioc.usuarioActual.snombre" />
						      </div>
						
						      <div class="form-group col-sm-3" >
						        <label for="campo3">* Primer Apellido:</label> 
						        <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Primer Apellido" ng-model="usuarioc.usuarioActual.papellido" required />
						      </div>
						
						      <div class="form-group col-sm-3">
						        <label for="campo4">Segundo Apellido:</label> 
						        <input type="text" class="form-control" id="campo4" name="campo4" placeholder="Segundo Apellido" ng-model="usuarioc.usuarioActual.sapellido" />
						      </div>
					      </div>
			
					      <div class="row">
						      <div class="form-group col-sm-12" >
						        <label for="campo5">* CUI:</label> 
						        <input type="number" id="campo5" name="campo5" class="form-control"  placeholder="CUI" ng-model="usuarioc.usuarioActual.cui" ng-maxlength="13" required />
						      </div>
						  </div>
					
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
