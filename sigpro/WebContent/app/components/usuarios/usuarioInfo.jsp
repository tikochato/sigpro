<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/ng-template" id="cambiarPassword.jsp">
   	<%@ include file="/app/components/usuarios/cambiarPassword.jsp"%>
</script>

	<div ng-controller="usuarioInfoController as usuarioc" class="maincontainer all_page" id="title">
	<shiro:authenticated>
	<h3><%= session.getAttribute("usuario") %></h3><br/>
		
		<div class="row main-form">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<label class="btn btn-primary" ng-click="usuarioc.editar()" ng-hide="!usuarioc.esoculto">Editar</label>
			        <label class="btn btn-success" ng-show="!usuarioc.esoculto" ng-click="usuarioc.guardarUsuario()" ng-disabled="form.$invalid" >Guardar</label>
			        <label class="btn btn-primary" ng-click="usuarioc.cancelar()" ng-hide="usuarioc.esoculto">cancelar</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" >
							<label for="nombre">Usuario:</label>
    						<p class="form-control-static">{{usuarioc.usuarioActual.usuario}}</p>
						</div>
						<div class="form-group" ng-show="usuarioc.esoculto" >
							<label for="nombre">Correo:</label>
    						<p class="form-control-static">{{usuarioc.usuarioActual.email}}</p>
						</div>
						<div class="form-group" ng-show="!usuarioc.esoculto" >
							<label for="nombre">Correo:</label>
    						<input  class="form-control" type="text" placeholder="Correo electrónico" ng-model="usuarioc.usuarioActual.email" ng-required="true"/>
						</div>
						<div class="form-group" ng-show="!usuarioc.esoculto">
								<label for="Descripcion">Password</label>
								<input class="form-control" type="password" ng-model="usuarioc.usuarioActual.password" ng-required="true"/>
						</div>
						<div ng-if="!usuarioc.esoculto">
							<div class="row" ng-show="usuarioc.tieneColaborador">
								
						      <div class="form-group col-sm-3" >
						        <label for="campo1">Primer Nombre:</label> 
						        <input type="text" class="form-control" id="campo1" name="campo1" placeholder="Primer Nombre" ng-model="usuarioc.usuarioActual.pnombre" ng-required="usuarioc.tieneColaborador"/>
						      </div>
						
						      <div class="form-group col-sm-3" >
						        <label for="campo2">Segundo Nombre:</label> 
						        <input type="text" class="form-control" id="campo2" name="campo2" placeholder="Segundo Nombre" ng-model="usuarioc.usuarioActual.snombre" />
						      </div>
						
						      <div class="form-group col-sm-3" >
						        <label for="campo3">Primer Apellido:</label> 
						        <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Primer Apellido" ng-model="usuarioc.usuarioActual.papellido" ng-required="usuarioc.tieneColaborador" />
						      </div>
						
						      <div class="form-group col-sm-3">
						        <label for="campo4">Segundo Apellido:</label> 
						        <input type="text" class="form-control" id="campo4" name="campo4" placeholder="Segundo Apellido" ng-model="usuarioc.usuarioActual.sapellido" />
						      </div>
					      </div>
			
					      <div class="row" ng-show="usuarioc.tieneColaborador">
						      <div class="form-group col-sm-12" >
						        <label for="campo5">* CUI:</label> 
						        <input type="number" id="campo5" name="campo5" class="form-control"  placeholder="CUI" ng-model="usuarioc.usuarioActual.cui" ng-maxlength="13" ng-required="usuarioc.tieneColaborador"/>
						      </div>
						  </div>
					
			      </div>
				</form>
			</div>
    	</div>
	</shiro:authenticated>
	</div>
