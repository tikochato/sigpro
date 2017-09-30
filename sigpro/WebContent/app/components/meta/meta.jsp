<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<style>
		.filaIngreso  > td{
			padding: 0px !important;
			padding-left: 8px !important;
			padding-right: 8px !important;
			vertical-align: initial !important; 
		}
	</style>	
	<div ng-controller="metaController as metac" class="all_page" id="title">
		<script type="text/ng-template" id="metaAvance.jsp">
	    	<%@ include file="/app/components/meta/metaAvance.jsp"%>
		</script>
		<div align="center">
			<div class="operation_buttons" align="right">
				<br/>
				<div class="btn-group btn-group-sm">
			       <shiro:hasPermission name="17040">
			       		<label class="btn btn-default" ng-click="metac.nuevaMeta()" uib-tooltip="Nueva Meta">
						<span class="glyphicon glyphicon-plus"></span></label>
			       </shiro:hasPermission> 
			   	</div>				
    		</div>
    		<shiro:hasPermission name="17010">
    		<div align="center">
    			<div class="grid_loading" ng-hide="!metac.mostrarCargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
							<br />
							<br /> <b>Cargando, por favor espere...</b> 
						</span>
					</div>
				</div>
					
				<table st-table="metac.metasCollection" st-safe-src="metac.metas" class="table">
				<thead>
				<tr>
					<th st-sort="id">ID</th>
					<th st-sort="nombre">Nombre</th>
					<th>Descripción</th>
					<th st-sort="unidadMedidaId">U. Medida</th>
					<th st-sort="datoTipoId">Tipo de Dato</th>
					<th st-sort="metaFinal">Meta Final</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<tr class="filaIngreso" ng-repeat="row in metac.metasCollection" ng-click="metac.metaSeleccionada(row)" ng-class="row.isSelected ? 'st-selected' : ''">
					<td style="vertical-align: middle;">{{row.id | uppercase}}</td>
					<td><input type="text" class="inputText" ng-model="row.nombre" style="width: 100%; text-align: left"></input></td>
					<td><input type="text" class="inputText" ng-model="row.descripcion" style="width: 100%; text-align: left"></input></td>
					<td>
						<select class="inputText" ng-model="row.unidadMedidaId"
							ng-options="unidad as unidad.nombre for unidad in metac.metaunidades track by unidad.id"
							 ng-required="true">
						<option disabled selected value>Seleccione Unidad</option>
						</select>
					</td>
					<td>
						<select class="inputText" ng-model="row.datoTipoId" ng-change="metac.getMetasAnio(row, metac.anio)"
							ng-options="tipo as tipo.nombre for tipo in metac.datoTipos track by tipo.id"
							 ng-required="true">
						<option disabled value>Seleccione Tipo</option>
						</select>
					</td>
					<td>
						<div ng-switch="row.datoTipoId.nombre">
							<div ng-switch-when="texto" >
								<input type="text" class="inputText" ng-model="row.metaFinalString" style="width: 100%; text-align: left"></input>
							</div>
							<div ng-switch-when="entero" class="form-group" >
								<input type="text" class="inputText" ng-model="row.metaFinalEntero" style="width: 100%; text-align: rigth" ui-number-mask="0"></input>
							</div>
							<div ng-switch-when="decimal" class="form-group" >
								<input type="text" class="inputText" ng-model="row.metaFinalDecimal" style="width: 100%; text-align: rigth" ui-number-mask="2"></input>
							</div>
							<div ng-switch-when="booleano" class="form-group" >
								<input type="checkbox" ng-model="row.metaFinalString" />
							</div>
							<div ng-switch-when="fecha" class="form-group" >
								<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="row.fechaControl" is-open="row.isOpen"
									datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(row)"
									ng-change="metac.guardarFechaMetaFinal(row)" ng-readonly="true"/>
									<span class="label-icon" ng-click="metac.abrirPopupFecha(row)">
										<i class="glyphicon glyphicon-calendar"></i>
									</span>
							</div>
						</div>
					</td>
					<shiro:hasPermission name="17030">
						<td>
				       		<label class="btn btn-default btn-xs" ng-click="metac.borrarMeta(row)" uib-tooltip="Borrar">
							<span class="glyphicon glyphicon-trash"></span></label>
						</td>
			       </shiro:hasPermission>
				</tr>
				</tbody>
				</table>
			</div>
			<div style="margin-top: 10px;">
				<div class="panel panel-default" >
					<div class="panel-heading label-form" style="text-align: center;">Valores</div>
					<div class="panel-body">
					<div class="form-group col-sm-2" style="text-align: left;">
						<select class="inputText" 
							ng-model="metac.anio" 
							ng-options="opcion for opcion in metac.anios"
							ng-change="metac.getMetasAnio(metac.meta, metac.anio)">
							<option value="">Seleccione un año</option>
						</select>
						<label for="nombre" class="floating-label">* Año</label>
					</div>
					<div align="center">
						<br>
		    			<table class="table table-striped table-hover"  style="height: 100%" ng-show="metac.mostrarValores">
							<thead >
								<tr>
									<th style="text-align: center;" class="label-form"></th>
			         				<th style="text-align: center;" class="label-form">Ene</th>
			         				<th style="text-align: center;" class="label-form">Feb</th>
			         				<th style="text-align: center;" class="label-form">Mar</th>
			         				<th style="text-align: center;" class="label-form">Abr</th>
			         				<th style="text-align: center;" class="label-form">May</th>
			         				<th style="text-align: center;" class="label-form">Jun</th>
			         				<th style="text-align: center;" class="label-form">Jul</th>
			         				<th style="text-align: center;" class="label-form">Ago</th>
			         				<th style="text-align: center;" class="label-form">Sep</th>
			         				<th style="text-align: center;" class="label-form">Oct</th>
			         				<th style="text-align: center;" class="label-form">Nov</th>
									<th style="text-align: center;" class="label-form">Dic</th>
			         				<th style="text-align: center;" class="label-form">Total</th>
			         			</tr>
							</thead>
							<tbody >
								<tr>
						      		<td style="vertical-align: middle;">Planificado</td>
						      		<td>
						      			<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.eneroString" ng-change="metac.almacenarPlanificado('eneroString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.eneroEntero" ng-change="metac.almacenarPlanificado('eneroEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.eneroDecimal" ng-change="metac.almacenarPlanificado('eneroDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.eneroString" ng-change="metac.almacenarPlanificado('eneroString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.eneroTiempo" is-open="metac.planificado.isOpenEnero"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1001)"
													ng-change="metac.almacenarPlanificado('eneroTiempo')" ng-readonly="true"/>
											</div>
										</div>
						      		</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.febreroString" ng-change="metac.almacenarPlanificado('febreroString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.febreroEntero" ng-change="metac.almacenarPlanificado('febreroEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.febreroDecimal" ng-change="metac.almacenarPlanificado('febreroDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.febreroString" ng-change="metac.almacenarPlanificado('febreroString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.febreroTiempo" is-open="metac.planificado.isOpenFebrero"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1002)"
													ng-change="metac.almacenarPlanificado('febreroTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.marzoString" ng-change="metac.almacenarPlanificado('marzoString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.marzoEntero" ng-change="metac.almacenarPlanificado('marzoEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.marzoDecimal" ng-change="metac.almacenarPlanificado('marzoDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.marzoString" ng-change="metac.almacenarPlanificado('marzoString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.marzoTiempo" is-open="metac.planificado.isOpenMarzo"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1003)"
													ng-change="metac.almacenarPlanificado('marzoTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.abrilString" ng-change="metac.almacenarPlanificado('abrilString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.abrilEntero" ng-change="metac.almacenarPlanificado('abrilEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.abrilDecimal" ng-change="metac.almacenarPlanificado('abrilDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.abrilString" ng-change="metac.almacenarPlanificado('abrilString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.abrilTiempo" is-open="metac.planificado.isOpenAbril"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1004)"
													ng-change="metac.almacenarPlanificado('abrilTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.mayoString" ng-change="metac.almacenarPlanificado('mayoString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.mayoEntero" ng-change="metac.almacenarPlanificado('mayoEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.mayoDecimal" ng-change="metac.almacenarPlanificado('mayoDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.mayoString" ng-change="metac.almacenarPlanificado('mayoString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.mayoTiempo" is-open="metac.planificado.isOpenMayo"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1005)"
													ng-change="metac.almacenarPlanificado('mayoTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.junioString" ng-change="metac.almacenarPlanificado('junioString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.junioEntero" ng-change="metac.almacenarPlanificado('junioEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.junioDecimal" ng-change="metac.almacenarPlanificado('junioDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.junioString" ng-change="metac.almacenarPlanificado('junioString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.junioTiempo" is-open="metac.planificado.isOpenJunio"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1006)"
													ng-change="metac.almacenarPlanificado('junioTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.julioString" ng-change="metac.almacenarPlanificado('julioString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.julioEntero" ng-change="metac.almacenarPlanificado('julioEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.julioDecimal" ng-change="metac.almacenarPlanificado('julioDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.julioString" ng-change="metac.almacenarPlanificado('julioString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.julioTiempo" is-open="metac.planificado.isOpenJulio"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1007)"
													ng-change="metac.almacenarPlanificado('julioTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.agostoString" ng-change="metac.almacenarPlanificado('agostoString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.agostoEntero" ng-change="metac.almacenarPlanificado('agostoEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.agostoDecimal" ng-change="metac.almacenarPlanificado('agostoDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.agostoString" ng-change="metac.almacenarPlanificado('agostoString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.agostoTiempo" is-open="metac.planificado.isOpenAgosto"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1008)"
													ng-change="metac.almacenarPlanificado('agostoTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.septiembreString" ng-change="metac.almacenarPlanificado('septiembreString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.septiembreEntero" ng-change="metac.almacenarPlanificado('septiembreEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.septiembreDecimal" ng-change="metac.almacenarPlanificado('septiembreDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.septiembreString" ng-change="metac.almacenarPlanificado('septiembreString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.septiembreTiempo" is-open="metac.planificado.isOpenSeptiembre"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1009)"
													ng-change="metac.almacenarPlanificado('septiembreTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.octubreString" ng-change="metac.almacenarPlanificado('octubreString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.octubreEntero" ng-change="metac.almacenarPlanificado('octubreEntero')" style="width: 100%; text-align: right;" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.octubreDecimal" ng-change="metac.almacenarPlanificado('octubreDecimal')" style="width: 100%; text-align: right;" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.octubreString" ng-change="metac.almacenarPlanificado('octubreString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.octubreTiempo" is-open="metac.planificado.isOpenOctubre"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1010)"
													ng-change="metac.almacenarPlanificado('octubreTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.noviembreString" ng-change="metac.almacenarPlanificado('noviembreString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.noviembreEntero" ng-change="metac.almacenarPlanificado('noviembreEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.noviembreDecimal" ng-change="metac.almacenarPlanificado('noviembreDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.noviembreString" ng-change="metac.almacenarPlanificado('noviembreString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.noviembreTiempo" is-open="metac.planificado.isOpenNoviembre"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1011)"
													ng-change="metac.almacenarPlanificado('noviembreTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.diciembreString" ng-change="metac.almacenarPlanificado('diciembreString')" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.diciembreEntero" ng-change="metac.almacenarPlanificado('diciembreEntero')" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.diciembreDecimal" ng-change="metac.almacenarPlanificado('diciembreDecimal')" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.diciembreString" ng-change="metac.almacenarPlanificado('diciembreString')"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" ng-model="metac.planificado.diciembreTiempo" is-open="metac.planificado.isOpenDiciembre"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.abrirPopupFecha(1012)"
													ng-change="metac.almacenarPlanificado('diciembreTiempo')" ng-readonly="true"/>
											</div>
										</div>
									</td>
									<td style="text-align: right; vertical-align: middle;">{{metac.planificado.total}}</td>
						      	</tr>
						      	<tr ng-click="metac.agregarAvances()">
						      		<td>Real</td>
						      		<td style="text-align: right;">{{metac.real.enero}}</td>
									<td style="text-align: right;">{{metac.real.febrero}}</td>
									<td style="text-align: right;">{{metac.real.marzo}}</td>
									<td style="text-align: right;">{{metac.real.abril}}</td>
									<td style="text-align: right;">{{metac.real.mayo}}</td>
									<td style="text-align: right;">{{metac.real.junio}}</td>
									<td style="text-align: right;">{{metac.real.julio}}</td>
									<td style="text-align: right;">{{metac.real.agosto}}</td>
									<td style="text-align: right;">{{metac.real.septiembre}}</td>
									<td style="text-align: right;">{{metac.real.octubre}}</td>
									<td style="text-align: right;">{{metac.real.noviembre}}</td>
									<td style="text-align: right;">{{metac.real.diciembre}}</td>
									<td style="text-align: right;">{{metac.real.total}}</td>
						      	</tr>
							</tbody>
						</table>
					</div>
					</div>
				</div>
			</div>
			
    		</shiro:hasPermission>
		</div>
	</div>