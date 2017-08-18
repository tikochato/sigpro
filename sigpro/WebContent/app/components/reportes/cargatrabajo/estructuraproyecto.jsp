<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
.panel-body {
    overflow-y: scroll;
    overflow-x:hidden;
    height: 300px;
}

</style>
<div class="modal-body" id="modal-body">
  <div class="row">
    <div class="col-sm-12">
    <div class="row">
    	<div style="padding: 1px;" >
    		<div class="panel panel-default" >
				<div class="panel-body">
					<div
					    ivh-treeview="estructura.estructuraProyecto"
					    ivh-treeview-default-selected-state="false"
						 ivh-treeview-default-selected-state="false"
						    ivh-treeview-expand-to-depth="-1"
					    >
					  </div>
				</div>
			</div>
    	</div>
	</div>
    </div>
  </div>
    <br/>
    <div class="row">
	    <div class="col-sm-12 operation_buttons" align="right">
		    <div class="btn-group">
		        <label class="btn btn-success" ng-click="estructura.ok()"> &nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label>
				<label class="btn btn-primary" ng-click="estructura.cancel()">Cancelar</label>
	    	</div>
	      
	    </div>
  </div>
</div>