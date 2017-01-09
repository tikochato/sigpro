<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-body" id="modal-body">
  	<div>
    	<div class="form-group">
			<label for="nombre">Contraseña</label>
    		<input type="password" class="form-control col-md-8" id="usuario" placeholder="ingrese contraseña" ng-model="modalPassword.password.password1">
		</div>		
    </div>
    <br>
    <div>
    	<div class="form-group">
			<label for="nombre">Confirmación de contraseña</label>
    		<input type="password" class=" form-control col-md-8" id="usuario" placeholder="ingrese contraseña de nuevo" ng-model="modalPassword.password.password2">
		</div>
    </div>
    <br>
    <br>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="modalPassword.ok()"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="modalPassword.cancel()">Cancelar</label>
	    	</div>
	    </div>
  </div>
</div>