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
		<shiro:lacksPermission name="34010">
			<p ng-init="usuarioc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Usuarios</h3></div>
		</div>

		<div class="row" align="center" ng-hide="usuarioc.isCollapsed">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="34040">
						<label class="btn btn-primary" ng-click="usuarioc.nuevoUsuario()" uib-tooltip="Nuevo">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34010">
						<label class="btn btn-primary" ng-click="usuarioc.editarUsuario()" uib-tooltip="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
					</shiro:hasPermission>
					<shiro:hasPermission name="34030">
						<label class="btn btn-danger" ng-click="usuarioc.eliminarUsuario()" uib-tooltip="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
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
				<div class="total-rows">
						  Total de {{  usuarioc.totalUsuarios + (usuarioc.totalUsuarios == 1 ? " Usuario" : " Usuarios" ) }}
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
		<div class="row second-main-form" ng-show="usuarioc.isCollapsed">
			<div class="page-header">
				<h2 ng-hide="!usuarioc.esNuevo"><small>Nuevo Usuario</small></h2>
				<h2 ng-hide="usuarioc.esNuevo"><small>Edición de Usuario</small></h2>
			</div>
			
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="34020">
						<label class="btn btn-success" ng-click="usuarioc.esNuevo ? (form1.$valid && !usuarioc.cargandoPermisos &&  usuarioc.tipoUsuario.grupo!= '' ? usuarioc.guardarUsuario() : '' ) :  (form.$valid  ? usuarioc.guardarUsuario() : '' )" 
							ng-disabled="usuarioc.esNuevo ? form1.$invalid || usuarioc.cargandoPermisos || usuarioc.tipoUsuario.grupo== '' : form.$invalid  || usuarioc.cargandoPermisos" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span>Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="usuarioc.cancelar()"  uib-tooltip="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span>Ir a Tabla</label>
    			</div>
    		</div>
			<div class="col-sm-12">
				<form name="form">

						<div class="form-group" ng-show="!usuarioc.esNuevo && !usuarioc.mostrarCambioPassword">
    						<input type="text" class="inputText" id="usuario" ng-model="usuarioc.usuariosSelected.usuario" 
    							ng-value="usuarioc.usuariosSelected.usuario" onblur="this.setAttribute('value', this.value);" readonly>
    						<label class="floating-label">* Usuario</label>
						</div>
						<div class="form-group" ng-show="!usuarioc.mostrarCambioPassword">
    						<input type="text" class="inputText" id="correo" ng-model="usuarioc.usuariosSelected.email" ng-required="true"
    							ng-value="usuarioc.usuariosSelected.email" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Correo electrónico</label>
						</div>
						<div class="form-group" ng-show="!usuarioc.esNuevo">
    						<input type="password" class="inputText" id="password1" ng-model="usuarioc.usuariosSelected.password" ng-required="true"
    							ng-value="usuarioc.usuariosSelected.password" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Contraseña</label>
						</div>
						<br>
						<h3 ng-show="!usuarioc.esNuevo">Préstamos Asignados</h3>
						<div class="col-sm-12 operation_buttons" align="right" style="margin-left: -1%;" ng-if="!usuarioc.esNuevo">
							<div class="btn-group">
								<label class="btn btn-default" ng-click="usuarioc.buscarPrestamosNuevos()"
														ng-disabled="" 
													uib-tooltip="Agregar permiso" ng-disabled="usuarioc.cargandoPermisos">
									<span class="glyphicon glyphicon-plus"></span>
									Agregar préstamo.
								</label>
							</div>
						</div>
						<table style="width: 100%; overflow-y: scroll;height: 175px;display: block;"
							st-table="usuarioc.prestamosAsignados"  ng-show="!usuarioc.esNuevo"
							class="table table-striped  table-bordered table-hover table-propiedades">
								<thead>
									<tr>
										<th style="width: 100%;">Nombre</th>
										<th style="width: 5%;">Quitar</th>
				
									</tr>
								</thead>
								<tbody>
									<tr st-select-row="row"
										ng-repeat="row in usuarioc.prestamosAsignados">
										<td>{{row.nombre}}</td>
										<td>
											<button type="button"
												ng-click="usuarioc.eliminarPrestamo($index,row)"
												class="btn btn-sm btn-danger">
												<i class="glyphicon glyphicon-minus-sign"> </i>
											</button>
										</td>
									</tr>
								</tbody>
							</table>
						<label ng-show="!usuarioc.esNuevo">
							<input type="checkbox" ng-model="usuarioc.verAreaPermisos" ng-true-value="'true'" ng-false-value="'false'">
						    	Ver permisos
						</label>

				</form>
				<form name="form1">
					<div class="form-group" ng-show="usuarioc.esNuevo">
    						<input type="text" class="inputText" id="usuario" ng-model="usuarioc.usuariosSelected.usuario" ng-required="true"
    							ng-value="usuarioc.usuariosSelected.usuario" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Usuario</label>
					</div>
					<div class="form-group" ng-show="usuarioc.esNuevo">
    						<input type="password" class="inputText" id="password1" ng-model="usuarioc.claves.password1" ng-required="true"
    							ng-value="usuarioc.claves.password1" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Contraseña</label>
						</div>
						<div class="form-group" ng-show="usuarioc.esNuevo">
    						<input type="password" class="inputText" id="password2" ng-model="usuarioc.claves.password2" ng-required="true"
    							ng-value="usuarioc.claves.password2" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">* Vuelva a ingresar la contraseña</label>
						</div>
						<div class="form-group-row row"  ng-show="usuarioc.esNuevo">
							<div class="form-group col-sm-4" >
						          <input type="text" class="inputText" ng-model="usuarioc.tipoUsuario.nombre"  ng-disabled="true" 
						          		ng-value="usuarioc.tipoUsuario.nombre" 
						          		ng-click="!usuarioc.cargandoPermisos? usuarioc.buscarPermiso(1) : ''" >
						          <span class="label-icon" ng-click="!usuarioc.cargandoPermisos? usuarioc.buscarPermiso(1) : ''"><i class="glyphicon glyphicon-search"></i></span>
						          <label class="floating-label" >Tipo de Usuario</label>
						    </div>
							<div class="form-group col-sm-4" ng-show="usuarioc.tipoUsuario.id==4 || usuarioc.tipoUsuario.id==5">
						          <input type="text" class="inputText" ng-model="usuarioc.usuariosSelected.colaborador"  ng-disabled="true" 
						          		ng-value="usuarioc.usuariosSelected.colaborador" 
						          		ng-click="usuarioc.buscarColaborador()" >
						          <span class="label-icon" ng-click=" usuarioc.buscarColaborador()"><i class="glyphicon glyphicon-search"></i></span>
						          <label class="floating-label" >Colaborador</label>
						    </div>
						    <div class="form-group col-sm-4" ng-show="usuarioc.tipoUsuario.id==4 || usuarioc.tipoUsuario.id==5">
						          <input type="text" class="inputText" ng-model="usuarioc.nombreUnidadEjecutora"  ng-disabled="true" 
						          		ng-value="usuarioc.nombreUnidadEjecutora" 
						          		 >
						          <label class="floating-label" >UnidadEjecutora</label>
						    </div>
						    <div class="form-group col-sm-8" ng-show="usuarioc.tipoUsuario.id==6">
						          <input type="text" class="inputText" ng-model="usuarioc.nombreCooperante"  ng-disabled="true" 
						          		ng-value="usuarioc.nombreCooperante"  >
						          <span class="label-icon" ng-click=" usuarioc.buscarPermiso(3)"><i class="glyphicon glyphicon-search"></i></span>
						          <label class="floating-label" >Cooperante</label>
						    </div>
						    <!--  tabla de los proyectos asignados -->
							<table style="width: 100%; overflow-y: scroll;height: 175px;display: block;"
							st-table="usuarioc.prestamosAsignados"
							class="table table-striped  table-bordered table-hover table-propiedades">
								<thead>
									<tr>
										<th style="width: 100%;">Nombre</th>
										<th style="width: 5%;">Quitar</th>
				
									</tr>
								</thead>
								<tbody>
									<tr st-select-row="row"
										ng-repeat="row in usuarioc.prestamosAsignados">
										<td>{{row.nombre}}</td>
										<td>
											<button type="button"
												ng-click="usuarioc.eliminarPrestamo($index)"
												class="btn btn-sm btn-danger">
												<i class="glyphicon glyphicon-minus-sign"> </i>
											</button>
										</td>
									</tr>
								</tbody>
							</table>
							<label>
						      <input type="checkbox" ng-model="usuarioc.verAreaPermisos" ng-true-value="'true'" ng-false-value="'false'">
						      Ver permisos
						    </label>
						</div>
						
					      
						<br>
				</form>
				
			</div>
			
			
			<div class="row">
			<div class="col-sm-12">
			
		<div align="center" ng-show="usuarioc.isCollapsed && usuarioc.verAreaPermisos == 'true'">
				<h3 ng-show="usuarioc.isCollapsed">Permisos</h3>
				<div class="col-sm-12 operation_buttons" align="right" style="margin-left: -1%;" ng-if="usuarioc.esNuevo">
					<div class="btn-group">
						<label class="btn btn-default" ng-click="usuarioc.buscarPermiso(0)"
												ng-disabled="" 
											uib-tooltip="Agregar permiso" ng-disabled="usuarioc.cargandoPermisos">
							<span class="glyphicon glyphicon-plus"></span>
							Agregar permiso.
						</label>
					</div>
				</div>
				<div class="col-sm-12 operation_buttons" align="right" style="margin-left: -1%;" ng-if="!usuarioc.esNuevo">
					<div class="btn-group">
						<label class="btn btn-default" ng-click="usuarioc.buscarPermiso(0)"
												ng-disabled="" 
											uib-tooltip="Agregar permiso" ng-disabled="usuarioc.cargandoPermisos">
							<span class="glyphicon glyphicon-plus"></span>
							Agregar permiso.
						</label>
					</div>
				</div>			
				<br>
				<table style="width: 95%; overflow-y: scroll;height: 175px;display: block;"
				st-table="usuarioc.permisosAsignados"
				class="table table-striped  table-bordered table-hover table-propiedades">
					<thead >
						<tr>
							<th style="width: 5%;">Nombre</th>
							<th>Descripción</th>
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
			
			<div class="grid_loading" ng-if="usuarioc.cargandoPermisos" style="margin-top:80px; width: 96%; margin-left: 1%;">
				<div class="msg">
					<span><i class="fa fa-spinner fa-spin fa-4x"></i>
						<br><br>
						<b>Cargando, por favor espere...</b>
					</span>
				</div>
			</div> 
			
		</div>
		</div>
		
		</div>
			<br>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			        <shiro:hasPermission name="34020">
						<label class="btn btn-success" ng-click="usuarioc.esNuevo ? (form1.$valid && !usuarioc.cargandoPermisos ? usuarioc.guardarUsuario() : '' ) :  (form.$valid && !usuarioc.cargandoPermisos  ? usuarioc.guardarUsuario() : '' )" 
							ng-disabled="usuarioc.esNuevo ? form1.$invalid || usuarioc.cargandoPermisos : form.$invalid  || usuarioc.cargandoPermisos" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span>Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="usuarioc.cancelar()"  uib-tooltip="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span>Ir a Tabla</label>
    		</div>
    	</div>
		</div>
		


	</div>
