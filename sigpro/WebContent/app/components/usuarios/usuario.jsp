<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/ng-template" id="buscarPermiso.jsp">
   	<%@ include file="/app/components/usuarios/buscarPermiso.jsp"%>
</script>
<script type="text/ng-template" id="cambiarPassword.jsp">
   	<%@ include file="/app/components/usuarios/cambiarPassword.jsp"%>
</script>
<script type="text/ng-template" id="buscarColaborador.jsp">
   	<%@ include file="/app/components/usuarios/buscarColaborador.jsp"%>
</script>
	<div ng-controller="usuarioController as usuarioc" class="maincontainer all_page" id="title">
		<h3>Usuarios</h3><br/>
		<div class="row" align="center" ng-hide="usuarioc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="34040">
						<label class="btn btn-primary" ng-click="usuarioc.nuevoUsuario()">Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34020">
						<label class="btn btn-primary" ng-click="usuarioc.editarUsuario()">Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34030">
						<label class="btn btn-primary" ng-click="usuarioc.eliminarUsuario()">Borrar</label>
					</shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="34010">
    			<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="usuarioc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="usuarioc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="usuarioc.myGrid">
						<div class="grid_loading" ng-hide="!usuarioc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<ul uib-pagination total-items="usuarioc.totalUsuarios"
						ng-model="usuarioc.paginaActual"
						max-size="usuarioc.numeroMaximoPaginas"
						items-per-page="usuarioc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="usuarioc.cambiarPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<shiro:hasPermission name="34010">
		<div class="row main-form" ng-show="usuarioc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="usuarioc.esNuevo ? (form1.$valid ? usuarioc.guardarUsuario() : '' ) :  (form.$valid ? usuarioc.guardarUsuario() : '' )" ng-disabled="usuarioc.esNuevo ? form1.$invalid : form.$invalid">Guardar</label>
			        <label class="btn btn-primary" ng-click="usuarioc.cancelar()">Ir a Tabla</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form name="form">

						<div class="form-group" ng-show="!usuarioc.esNuevo && !usuarioc.mostrarCambioPassword">
							<label for="nombre">Usuario</label>
    						<input type="text" class="form-control" id="usuario" placeholder="Usuario" ng-model="usuarioc.usuariosSelected.usuario" readonly>
						</div>
						<div class="form-group" ng-show="!usuarioc.mostrarCambioPassword">
							<label for="Descripcion" >Correo</label>
    						<input type="text" class="form-control" id="correo" placeholder="Correo electrónico" ng-model="usuarioc.usuariosSelected.email" ng-required="true">
						</div>
						<div class="form-group" ng-show="!usuarioc.esNuevo">
							<label for="Descripcion">Contraseña</label>
    						<input type="password" class="form-control" id="password1" placeholder="Contraseña" ng-model="usuarioc.usuariosSelected.password" ng-required="true">
						</div>

				</form>
				<form name="form1">
					<div class="form-group" ng-show="usuarioc.esNuevo">
							<label for="nombre">Usuario</label>
    						<input type="text" class="form-control" id="usuario" placeholder="Usuario" ng-model="usuarioc.usuariosSelected.usuario" ng-required="true">
					</div>
					<div class="form-group" ng-show="usuarioc.esNuevo">
							<label for="Descripcion">Contraseña</label>
    						<input type="password" class="form-control" id="password1" placeholder="Contraseña" ng-model="usuarioc.claves.password1" ng-required="true">
						</div>
						<div class="form-group" ng-show="usuarioc.esNuevo">
							<label for="Descripcion">Vuelva a ingresar la contraseña</label>
    						<input type="password" class="form-control" id="password2" placeholder="Contraseña" ng-model="usuarioc.claves.password2" ng-required="true">
						</div>

				</form>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <label class="btn btn-success" ng-click="usuarioc.esNuevo ? (form1.$valid ? usuarioc.guardarUsuario() : '' ) :  (form.$valid ? usuarioc.guardarUsuario() : '' )" ng-disabled="usuarioc.esNuevo ? form1.$invalid : form.$invalid">Guardar</label>
			        <label class="btn btn-primary" ng-click="usuarioc.cancelar()">Ir a Tabla</label>
    		</div>
    	</div>
		</div>
		<div class="row" ng-show="usuarioc.isCollapsed&&usuarioc.usuariosSelected.usuario!==''">
		<div class="form-group ">
		   <label for="campo6">Colaborador</label>
		   <div class="input-group" ng-show="!usuarioc.tieneColaborador">
		   		<button  class="btn btn-default" style="width:100%;" ng-click="usuarioc.buscarColaborador()"> {{usuarioc.mensajeActualizado.mensaje}}</button>
		   		<span class="input-group-addon" ng-click="usuarioc.asignarColaborador()"><i class="glyphicon glyphicon-ok"></i></span>
		   </div>
		   <div class="input-group" ng-show="usuarioc.tieneColaborador">
		   		<button  class="btn btn-default" style="width:100%;font-size:1.2em;"> {{usuarioc.usuariosSelected.colaborador}}</button>
		   </div>
		</div>
		<div class="form-group" >
		        <label for="campo6">Colaborador</label> 
		        <div class="input-group">
		          <input type="text" class="form-control" placeholder="Usuario" ng-model="colaborador.colaborador.usuario"  ng-disabled="true" ng-required="true"/>
		          <span class="input-group-addon"  ng-click="usuarioc.buscarColaborador()" uib-tooltip="Validar Usuario" ><i class="glyphicon glyphicon-search"></i></span>
		        </div>
		      </div>
		</div>

		<h3 ng-show="usuarioc.isCollapsed">Permisos</h3><br/>
		<div align="center" ng-show="usuarioc.isCollapsed">
				<div style="height: 35px; width: 75%">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href
								ng-click="usuarioc.buscarPermiso()" role="button"
								uib-tooltip="Asignar nuevo permiso" tooltip-placement="left">
								<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>

				<table style="width: 75%;"
				st-table="usuarioc.permisosAsignados"
				class="table table-striped  table-bordered">
				<thead >
					<tr>
						<th>Nombre</th>
						<th>Descripicon</th>
						<th style="width: 30px;">Quitar</th>

					</tr>
				</thead>
				<tbody>
					<tr st-select-row="row"
						ng-repeat="row in usuarioc.permisosAsignados">
						<td>{{row.nombre}}</td>
						<td>{{row.descripcion}}</td>
						<td>
							<button type="button"
								ng-click="usuarioc.eliminarPermiso(row)"
								class="btn btn-sm btn-danger">
								<i class="glyphicon glyphicon-minus-sign"> </i>
							</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		</shiro:hasPermission>


	</div>
