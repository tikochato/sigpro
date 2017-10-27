<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style type="text/css">
	
	.divTablas{
			height: calc(100% - 32px);
			width: 100%;
	}
	
	.divTabla{
			float: left;			
			overflow-y:hidden;
			overflow-x:hidden;
			height: 100%;
			width: 100%;
		}
		
		.theadDatos {
			flex-shrink: 0; overflow-x: hidden;
		}
		
		.cuerpoTablaDatos {
		    overflow-y: auto;
		    overflow-x: hiden;
		    display: inline-block;
		    text-align: left;
		    font-size: 13px; 
		}
		
		.tablaDatos {
			display: flex;
		    flex-direction: column;
		    align-items: stretch;
		    min-height: 100%;
		    max-height: 100%;  
		}
		
		.label-form2 {
	    	font-size: 15px;
	    	opacity: 1;
	    	color: rgba(0,0,0,0.38) !important;
	    	font-weight: bold;
		}
</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="miembrosunidadejecutoraController as miembroc" class="maincontainer all_page" id="title">
	
  		<shiro:lacksPermission name="45010">
			<span ng-init="desembolsoc.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		
		
		<div class="row" id="miemborsue" style="height: 100%; width: 100%">
		
			<div class="col-sm-12" style=" height: 20%; ">
				<div class="panel panel-default" ng-if="!miembroc.esTreeview">
				  <div class="panel-heading"><h3>Miembros de Unidad Ejecutora</h3></div>
				</div>
				<div class="subtitulo" ng-if="!componentec.esTreeview">
					{{ miembroc.proyectoNombre }} - {{miembroc.unidadEjecutoraNombre}}
				</div>				
			</div>
			<div >
		
		<br/><br/><br/><br/><br/><br/>
		<div class="col-sm-12" style="height: 80%"  >
			
			<div class="operation_buttons">
				<div class="btn-group" style="float: right;">
					<shiro:hasPermission name="45020">
						<label class="btn btn-success" ng-click="form.$valid ? miembroc.guardar(form.$valid) : '' " ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
				</div>
			</div>
		
			<br/><br/>
			
			
		
			<div class="operation_buttons" align="right">
					<br/>
					<div class="btn-group btn-group-sm">
				       <shiro:hasPermission name="45040">
				       		<label class="btn btn-default" ng-click="miembroc.nuevoMiembro()" uib-tooltip="Nuevo Miembro">
							<span class="glyphicon glyphicon-plus"></span></label>
				       </shiro:hasPermission> 
				   	</div>				
	    	</div>
				<div class="divTablas">
						<div class="divPadreDatos" style="height: 100%;  width: 100%">
							<div class="divTabla">
								<form name="form">
								<table st-table="miembroc.displayedCollection" st-safe-src="miembroc.rowCollection"
										class="table table-striped tablaDatos" >
											<thead id="divCabecerasDatos" class="theadDatos" style="width: 100%" >
													<tr style="width: 100%; display: table!important;">
														<th class="label-form2" style="width: 5%; text-align: center">No.</th>
														<th class="label-form2" style=" text-align: center">Nombre</th>
														<th class="label-form2" style="width: 30%; text-align: center">Rol</th>
														<shiro:hasPermission name="45030">
															<th class="label-form2" style="width: 5%; text-align: center">Quitar</th>
														</shiro:hasPermission>
													</tr>
											</thead>
											<tbody style="max-height: 350px; width: 100% "  class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)">
												<tr style="width: 100%; display: table!important;" ng-repeat="row in miembroc.displayedCollection track by $index" >
													<td style="width: 5%;"> {{ $index +1 }} </td>
													
													<td ng-hide="!row.guardado"   >{{row.primerNombre + ' ' + row.segundoNombre + ' ' + row.primerApellido + ' ' + row.segundoApellido}}</td>
													<td ng-hide="row.guardado" >
														<div class="row">
														<div class="col-sm-3 form-group">
								    						<input type="text" class="inputText" ng-model="row.primerNombre"
								    						ng-value="row.primerNombre" onblur="this.setAttribute('value', this.value);"
								    						ng-required="true" >
								    						<label class="floating-label">Primer Nombre</label>
														</div>
														<div class="col-sm-3 form-group">
								    						<input type="text" class="inputText" ng-model="row.segundoNombre"
								    						ng-value="row.segundoNombre" onblur="this.setAttribute('value', this.value);"
								    						>
								    						<label class="floating-label">Segundo Nombre</label>
														</div>
														<div class="col-sm-3 form-group">
								    						<input type="text" class="inputText" ng-model="row.primerApellido"
								    						ng-value="row.primerApellido" onblur="this.setAttribute('value', this.value);"
								    						ng-required="true">
								    						<label class="floating-label">Primer Apellido</label>
														</div>
														<div class="col-sm-3 form-group">
								    						<input type="text" class="inputText" ng-model="row.segundoApellido"
								    						ng-value="row.segundoApellido" onblur="this.setAttribute('value', this.value);"
								    						>
								    						<label class="floating-label">Segundo Apellido</label>
														</div>
														</div>
													</td>
													
													<td ng-hide="!row.guardado" style="width: 30%">{{row.rolUnidadEjecotoraNombre}}</td>
													<td ng-hide="row.guardado" style="width: 30%">
														 <select class="inputText" ng-model="row.rolunidad" ng-change="miembroc.seleccionarRol($index,col)"
														 ng-required="true" ng-options="rol as rol.nombre for rol in miembroc.roles track by rol.id" >
														 	<option value="">Seleccione un rol</option>
														 	
														 </select>
													</td>
													<shiro:hasPermission name="45030">
														<td  style="width: 5%;">
															<label class="btn btn-default btn-xs" ng-click="miembroc.eliminarMiembro(row)" 
															uib-tooltip="Borrar">
															<span class="glyphicon glyphicon-trash"></span></label>
														</td>
													</shiro:hasPermission>
												</tr>
											</tbody>
											
								</table>
							</form>
						</div>
					</div>
				</div>
	    	
    	<br/>
		<div class="btn-group" style="float: right;">
			<shiro:hasPermission name="45020">
				<label class="btn btn-success" ng-click="form.$valid ? miembroc.guardar(form.$valid) : '' " ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			</shiro:hasPermission>
			
		</div>

		</div>
		</div>
	</div>
	</div>
