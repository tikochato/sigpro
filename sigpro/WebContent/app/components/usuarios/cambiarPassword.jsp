<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-body" id="modal-body">
  	<div>
    	<div class="form-group">
    		<input type="password" class="inputText" id="usuario" ng-model="modalPassword.password.password1" value="{{modalPassword.password.password1}}" 
    			onblur="this.setAttribute('value', this.value);">
    		<label class="floating-label">Contraseña</label>
		</div>		
    </div>
    <br>
    <div>
    	<div class="form-group">
    		<input type="password" class="inputText" id="usuario" ng-model="modalPassword.password.password2" value="{{modalPassword.password.password2}}" 
    			onblur="this.setAttribute('value', this.value);">
    		<label class="floating-label">Confirmación de contraseña</label>
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