<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
    <h3 class="modal-title">Congelar PEP</h3>
</div>
<div class="modal-body" id="modal-body">
	<div class="grid_loading" ng-hide="!modalcc.mostrarcargando" style="position:relative; z-index:4;   ">
			  	<div class="msg" style="height: 120px!important;">
			      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
					  <br /><br />
					  <b>Cargando, por favor espere...</b>
				  </span>
				</div>
	</div>
	<div class="row second-main-form">
		<form name="form" style="margin-top: 10px;">
			<div class="col-sm-12">
			   <div class="form-group" >
				      <input type="text" name="inombre"  class="inputText" id="inombre" ng-model="modalcc.nombre"
				       ng-value="modalcc.nombre" 
				      onblur="this.setAttribute('value', this.value);" ng-required="true" >
				      <label class="floating-label">* Nombre</label>
				</div>
			</div>
		</form>
	</div>
	<br />
	<div class="row">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid  ? modalcc.ok() : '' "
				 ng-disabled = "!form.$valid">
					&nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label> 
					<label class="btn btn-primary" ng-click="modalcc.cancel()">Cancelar</label>
			</div>

		</div>
	</div>
</div>