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
		
		
	
</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="miembrosunidadejecutoraController as miembroc" class="maincontainer all_page" id="title">
	
  		<shiro:lacksPermission name="24010">
			<span ng-init="desembolsoc.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		
		
		<div class="row" id="miemborsue" style="height: 100%; width: 100%">
		
			<div class="col-sm-12" style=" height: 20%; ">
				<div class="panel panel-default" ng-if="!miembroc.esTreeview">
				  <div class="panel-heading"><h3>Miembros de Unidad Ejecutora</h3></div>
				</div>
				<div class="subtitulo" ng-if="!componentec.esTreeview">
					{{ miembroc.prestamoNombre }}
				</div>				
			</div>
			
			<div >
		
		<br/><br/><br/><br/><br/>
		<div class="col-sm-12" style="height: 80%"  >
			
			<div class="operation_buttons">
				<div class="btn-group" style="float: right;">
					<shiro:hasPermission name="24020">
						<label class="btn btn-success" ng-click="miembroc.guardar(form.$valid) " ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
				</div>
			</div>
		
			<br/><br/>
			
			
		
			<div class="operation_buttons" align="right">
					<br/>
					<div class="btn-group btn-group-sm">
				       <shiro:hasPermission name="24040">
				       		<label class="btn btn-default" ng-click="miembroc.nuevoMiembro()" uib-tooltip="Nuevo Miembro">
							<span class="glyphicon glyphicon-plus"></span></label>
				       </shiro:hasPermission> 
				   	</div>				
	    	</div>
    	
			
				<div class="divTablas">
					
						<div class="divPadreDatos" style="height: 100%;  width: 100%">
							<div class="divTabla">
								<table st-table="miembroc.displayedCollection" st-safe-src="miembroc.rowCollection"
										class="table table-striped tablaDatos" >
											<thead id="divCabecerasDatos" class="theadDatos" style="width: 100%" >
													<tr style="width: 100%">
														<th class="label-form" style="width: 5%;">No.</th>
														<th class="label-form" style="width: 43%">Nombre</th>
														<th class="label-form" style="width: 25%">Rol</th>
														<th class="label-form" style="width: 25%">Correo electrónico</th>
														<th class="label-form" style="width: 5%;">Quitar</th>
													</tr>
											</thead>
											<tbody style="max-height: 350px; width: 100% "  class="cuerpoTablaDatos" id="divTablaDatos" onmouseover="activarScroll(this.id)">
												<tr ng-repeat="row in miembroc.displayedCollection track by $index">
													<td style="width: 5%;"> {{ $index +1 }} </td>
													
													<td ng-hide="!row.guardado" style="width: 43%"  >{{row.colaboradorNombre}}</td>
													<td ng-hide="row.guardado" style="width: 43%">
														 <select class="inputText" ng-model="row.colaboradorId" ng-change="miembroc.seleccionarColaborador($index,col)"
														 ng-required="true">
														 	<option value="">Seleccione un colaborador</option>
														 	<option ng-repeat="col in miembroc.colaboradores"
																	ng-value="col.id">{{col.nombre}}</option>
														 </select>
													</td>
													
													<td ng-hide="!row.guardado" style="width: 25%">{{row.rolUnidadEjecotoraNombre}}</td>
													<td ng-hide="row.guardado" style="width: 25%">
														 <select class="inputText" ng-model="row.rolunidad" ng-change="miembroc.seleccionarRol($index,col)"
														 ng-required="true" ng-options="rol as rol.nombre for rol in miembroc.roles track by rol.id" >
														 	<option value="">Seleccione un rol</option>
														 	
														 </select>
													</td>
													
													<td style="width: 25%">{{row.email}}</td>
													<td  style="width: 5%;">
														<label class="btn btn-default btn-xs" ng-click="miembroc.eliminarMiembro(row)" 
														uib-tooltip="Borrar">
														<span class="glyphicon glyphicon-trash"></span></label>
												</td>
												</tr>
											</tbody>
											
								</table>
							</div>
							</div>
					
				</div>
	    	
    	<br/>
		<div class="btn-group" style="float: right;">
			<shiro:hasPermission name="24020">
				<label class="btn btn-success" ng-click="miembroc.guardar(form.$valid) " ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			</shiro:hasPermission>
			
		</div>

		</div>
		</div>
	</div>
	</div>
