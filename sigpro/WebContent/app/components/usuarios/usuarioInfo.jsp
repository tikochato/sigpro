<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/ng-template" id="cambiarPassword.jsp">
   	<%@ include file="/app/components/usuarios/cambiarPassword.jsp"%>
</script>

	<div ng-controller="usuarioInfoController as usuarioc" class="maincontainer all_page" id="title">
	<shiro:authenticated>
	
	<div class="panel panel-default">
			<div class="panel-heading"><h3>Información de Usuario</h3></div>
		</div>
		
		<div class="row second-main-form">
		<br>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<label class="btn btn-primary" ng-click="usuarioc.editar()" ng-show="usuarioc.esoculto" uib-tooltip="Editar">
					<span class="glyphicon glyphicon-pencil"></span> Editar</label>				
			        <label class="btn btn-primary" ng-show="!usuarioc.esoculto" ng-click="usuarioc.guardarUsuario()" ng-disabled="form.$invalid" uib-tooltip="Guardar">
			        <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        <label class="btn btn-default" ng-click="usuarioc.cancelar()" ng-hide="usuarioc.esoculto" uib-tooltip="Cancelar">
			        Cancelar</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" >
						 	<input type="text" name="inombre"  class="inputText" ng-model="usuarioc.usuarioActual.usuario" ng-value="usuarioc.usuarioActual.usuario" ng-disabled="true" >
						    <label class="floating-label">Usuario</label>  
						</div>
						<div class="form-group" ng-show="usuarioc.esoculto" >
							<input type="text" name="inombre"  class="inputText" ng-model="usuarioc.usuarioActual.email" ng-value="usuarioc.usuarioActual.email" ng-disabled="true" >
						    <label for="nombre" class="floating-label">Correo electrónico</label>  
						</div>
						<div class="form-group" ng-show="!usuarioc.esoculto" >
    						<input  class="inputText" type="text" ng-model="usuarioc.usuarioActual.email" ng-value="usuarioc.usuarioActual.email" onblur="this.setAttribute('value', this.value);" ng-required="true"/>
    						<label for="nombre" class="floating-label">Correo electrónico</label>  
						</div>
						<div class="form-group" ng-show="!usuarioc.esoculto">
								<input class="inputText" type="password" ng-model="usuarioc.usuarioActual.password" ng-value="usuarioc.usuarioActual.password" onblur="this.setAttribute('value', this.value);"  ng-required="true"/>
								<label for="Descripcion" class="floating-label">Password</label>
						</div>
						<div ng-show="usuarioc.esoculto">
						      <div class="form-group" >
						      	<input type="text" class="inputText" ng-model="usuarioc.usuarioActual.pnombre" ng-value="usuarioc.usuarioActual.pnombre" ng-disabled="true" >
						        <label for="campo1" class="floating-label">Primer Nombre</label> 
						      </div>
						
						      <div class="form-group" >
						      	<input type="text" class="inputText" ng-model="usuarioc.usuarioActual.snombre" ng-value="usuarioc.usuarioActual.snombre" ng-disabled="true" >
						      	<label for="campo2" class="floating-label">Segundo Nombre</label> 
						      </div>
						
						      <div class="form-group" > 
						      	<input type="text" class="inputText" ng-model="usuarioc.usuarioActual.papellido" ng-value="usuarioc.usuarioActual.papellido" ng-disabled="true" >
						      	<label for="campo3" class="floating-label">Primer Apellido</label>
						      </div>
						
						      <div class="form-group"> 
						      	 <input type="text" class="inputText" ng-model="usuarioc.usuarioActual.sapellido" ng-value="usuarioc.usuarioActual.sapellido" ng-disabled="true" >
						      	<label for="campo4" class="floating-label">Segundo Apellido</label>
						      </div>
					      </div>
						<div ng-if="!usuarioc.esoculto">
							<div  ng-show="usuarioc.tieneColaborador">
								
						      <div class="form-group" >
						      	<input  class="inputText" id="campo1" type="text" ng-model="usuarioc.usuarioActual.pnombre" ng-value="usuarioc.usuarioActual.pnombre" onblur="this.setAttribute('value', this.value);" ng-required="usuarioc.tieneColaborador"/>
    						    <label for="campo1" class="floating-label">Primer Nombre:</label> 
						      </div>
						
						      <div class="form-group" > 
						        <input  class="inputText" id="campo2" type="text" ng-model="usuarioc.usuarioActual.snombre" ng-value="usuarioc.usuarioActual.snombre" onblur="this.setAttribute('value', this.value);"/>
    						    <label for="campo2" class="floating-label">Segundo Nombre:</label>
						      </div>
						
						      <div class="form-group" > 
						        <input  class="inputText" id="campo3" type="text" ng-model="usuarioc.usuarioActual.papellido" ng-value="usuarioc.usuarioActual.papellido" onblur="this.setAttribute('value', this.value);" ng-required="usuarioc.tieneColaborador"/>
    						    <label for="campo3" class="floating-label">Primer Apellido:</label>
						      </div>
						
						      <div class="form-group"> 
						        <input  class="inputText" id="campo4" type="text" ng-model="usuarioc.usuarioActual.sapellido" ng-value="usuarioc.usuarioActual.sapellido" onblur="this.setAttribute('value', this.value);"/>
    						    <label for="campo4" class="floating-label">Segundo Apellido:</label>
						      </div>
					      </div>
			
					      <div ng-show="usuarioc.tieneColaborador">
						      <div class="form-group" >
						        <input type="number" id="campo5" name="campo5" class="inputText" ng-model="usuarioc.usuarioActual.cui" ng-value="usuarioc.usuarioActual.cui" onblur="this.setAttribute('value', this.value);" ng-maxlength="13" ng-required="usuarioc.tieneColaborador"/>
						        <label for="campo5" class="floating-label">* CUI:</label> 
						      </div>
						  </div>
					
			      </div>
				</form>
			</div>
    	</div>
	</shiro:authenticated>
	</div>
