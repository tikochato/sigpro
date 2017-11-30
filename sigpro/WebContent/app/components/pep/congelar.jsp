<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
    <h3 class="modal-title">Congelar PEP</h3>
</div>
<div class="modal-body" id="modal-body">
	<div class="row second-main-form">
		<div class="col-sm-12">
		   <div class="form-group" >
			      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="modalcc.nombre"
			       ng-value="modalcc.nombre" 
			      onblur="this.setAttribute('value', this.value);" ng-required="true" >
			      <label class="floating-label">* Nombre</label>
			</div>
		</div>
	</div>
	<br />
	<div class="row">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="modalcc.ok()">
					&nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label> <label
					class="btn btn-primary" ng-click="modalcc.cancel()">Cancelar</label>
			</div>

		</div>
	</div>
</div>