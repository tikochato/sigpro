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
			       		<label class="btn btn-default" ng-click="metac.congelado?'':metac.nuevaMeta()" ng-disabled="metac.congelado" uib-tooltip="Nueva">
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
					<th st-sort="nombre">* Nombre</th>
					<th>Descripción</th>
					<th st-sort="unidadMedidaId">* Medida</th>
					<th st-sort="datoTipoId">*Tipo</th>
					<th st-sort="metaFinal">Meta Final</th>
					<th></th>
				</tr>
				</thead>
				<tbody>
				<tr class="filaIngreso" ng-repeat="row in metac.metasCollection" ng-click="metac.metaSeleccionada(row)" ng-class="row.isSelected ? 'st-selected' : ''">
					<td><input type="text" class="inputText" ng-model="row.nombre" style="width: 100%; text-align: left" ng-required="true" ng-readonly="metac.congelado"></input></td>
					<td><input type="text" class="inputText" ng-model="row.descripcion" style="width: 100%; text-align: left" ng-readonly="metac.congelado"></input></td>
					<td>
						<select class="inputText" ng-model="row.unidadMedidaId"
							ng-options="unidad as unidad.nombre for unidad in metac.metaunidades track by unidad.id"
							 ng-required="true" ng-disabled="metac.congelado" >
							 <option value="">Elija</option>
						</select>
					</td>
					<td>
						<select class="inputText" ng-model="row.datoTipoId" ng-change="metac.getMetasAnio(row, metac.anio)"
							ng-options="tipo as tipo.nombre for tipo in metac.datoTipos track by tipo.id"
							 ng-required="true" ng-disabled="metac.congelado">
						<option disabled value>Seleccione Tipo</option>
						</select>
					</td>
					<td>
						<div ng-switch="row.datoTipoId.nombre">
							<div ng-switch-when="texto" >
								<input type="text" class="inputText" ng-model="row.metaFinalString" style="width: 100%; text-align: left" ng-readonly="metac.congelado"></input>
							</div>
							<div ng-switch-when="entero" class="form-group" >
								<input type="text" class="inputText" ng-model="row.metaFinalEntero" style="width: 100%; text-align: rigth" ui-number-mask="0" ng-readonly="metac.congelado"></input>
							</div>
							<div ng-switch-when="decimal" class="form-group" >
								<input type="text" class="inputText" ng-model="row.metaFinalDecimal" style="width: 100%; text-align: rigth" ui-number-mask="2" ng-readonly="metac.congelado"></input>
							</div>
							<div ng-switch-when="booleano" class="form-group" >
								<input type="checkbox" ng-model="row.metaFinalString" ng-disabled="metac.congelado"/>
							</div>
							<div ng-switch-when="fecha" class="form-group" >
								<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
									ng-model="row.fechaControl" is-open="row.isOpen"
									datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(row)"
									ng-change="metac.guardarFechaMetaFinal(row)" ng-readonly="metac.congelado"/>
									<span class="label-icon" ng-click="metac.congelado?'':metac.abrirPopupFecha(row)" tabindex="-1">
										<i class="glyphicon glyphicon-calendar"></i>
									</span>
							</div>
						</div>
					</td>
					<shiro:hasPermission name="17030">
						<td>
				       		<label class="btn btn-default btn-xs" ng-click="metac.congelado?'':metac.borrarMeta(row)" ng-disabled="metac.congelado" uib-tooltip="Borrar">
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
								<tr ng-if="metac.objeto_tipo!=-1">
						      		<td style="vertical-align: middle;">Planificado</td>
						      		<td>
						      			<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.eneroString" ng-change="metac.almacenarPlanificado('eneroString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.eneroEntero" ng-change="metac.almacenarPlanificado('eneroEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.eneroDecimal" ng-change="metac.almacenarPlanificado('eneroDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.eneroString" ng-change="metac.almacenarPlanificado('eneroString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.eneroTiempo" is-open="metac.planificado.isOpenEnero" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1001)"
													ng-change="metac.almacenarPlanificado('eneroTiempo')"/>
											</div>
										</div>
						      		</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.febreroString" ng-change="metac.almacenarPlanificado('febreroString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.febreroEntero" ng-change="metac.almacenarPlanificado('febreroEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.febreroDecimal" ng-change="metac.almacenarPlanificado('febreroDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.febreroString" ng-change="metac.almacenarPlanificado('febreroString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.febreroTiempo" is-open="metac.planificado.isOpenFebrero" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1002)"
													ng-change="metac.almacenarPlanificado('febreroTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.marzoString" ng-change="metac.almacenarPlanificado('marzoString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.marzoEntero" ng-change="metac.almacenarPlanificado('marzoEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.marzoDecimal" ng-change="metac.almacenarPlanificado('marzoDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.marzoString" ng-change="metac.almacenarPlanificado('marzoString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.marzoTiempo" is-open="metac.planificado.isOpenMarzo" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1003)"
													ng-change="metac.almacenarPlanificado('marzoTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.abrilString" ng-change="metac.almacenarPlanificado('abrilString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.abrilEntero" ng-change="metac.almacenarPlanificado('abrilEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.abrilDecimal" ng-change="metac.almacenarPlanificado('abrilDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.abrilString" ng-change="metac.almacenarPlanificado('abrilString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.abrilTiempo" is-open="metac.planificado.isOpenAbril" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1004)"
													ng-change="metac.almacenarPlanificado('abrilTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.mayoString" ng-change="metac.almacenarPlanificado('mayoString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.mayoEntero" ng-change="metac.almacenarPlanificado('mayoEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.mayoDecimal" ng-change="metac.almacenarPlanificado('mayoDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.mayoString" ng-change="metac.almacenarPlanificado('mayoString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.mayoTiempo" is-open="metac.planificado.isOpenMayo" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1005)"
													ng-change="metac.almacenarPlanificado('mayoTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.junioString" ng-change="metac.almacenarPlanificado('junioString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.junioEntero" ng-change="metac.almacenarPlanificado('junioEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.junioDecimal" ng-change="metac.almacenarPlanificado('junioDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.junioString" ng-change="metac.almacenarPlanificado('junioString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.junioTiempo" is-open="metac.planificado.isOpenJunio" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1006)"
													ng-change="metac.almacenarPlanificado('junioTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.julioString" ng-change="metac.almacenarPlanificado('julioString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.julioEntero" ng-change="metac.almacenarPlanificado('julioEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.julioDecimal" ng-change="metac.almacenarPlanificado('julioDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.julioString" ng-change="metac.almacenarPlanificado('julioString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.julioTiempo" is-open="metac.planificado.isOpenJulio" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1007)"
													ng-change="metac.almacenarPlanificado('julioTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.agostoString" ng-change="metac.almacenarPlanificado('agostoString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.agostoEntero" ng-change="metac.almacenarPlanificado('agostoEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.agostoDecimal" ng-change="metac.almacenarPlanificado('agostoDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.agostoString" ng-change="metac.almacenarPlanificado('agostoString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.agostoTiempo" is-open="metac.planificado.isOpenAgosto" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1008)"
													ng-change="metac.almacenarPlanificado('agostoTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.septiembreString" ng-change="metac.almacenarPlanificado('septiembreString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.septiembreEntero" ng-change="metac.almacenarPlanificado('septiembreEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.septiembreDecimal" ng-change="metac.almacenarPlanificado('septiembreDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.septiembreString" ng-change="metac.almacenarPlanificado('septiembreString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.septiembreTiempo" is-open="metac.planificado.isOpenSeptiembre" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1009)"
													ng-change="metac.almacenarPlanificado('septiembreTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.octubreString" ng-change="metac.almacenarPlanificado('octubreString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.octubreEntero" ng-change="metac.almacenarPlanificado('octubreEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right;" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.octubreDecimal" ng-change="metac.almacenarPlanificado('octubreDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right;" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.octubreString" ng-change="metac.almacenarPlanificado('octubreString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.octubreTiempo" is-open="metac.planificado.isOpenOctubre" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1010)"
													ng-change="metac.almacenarPlanificado('octubreTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.noviembreString" ng-change="metac.almacenarPlanificado('noviembreString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.noviembreEntero" ng-change="metac.almacenarPlanificado('noviembreEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.noviembreDecimal" ng-change="metac.almacenarPlanificado('noviembreDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.noviembreString" ng-change="metac.almacenarPlanificado('noviembreString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.noviembreTiempo" is-open="metac.planificado.isOpenNoviembre" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1011)"
													ng-change="metac.almacenarPlanificado('noviembreTiempo')"/>
											</div>
										</div>
									</td>
									<td>
										<div ng-switch="metac.meta.datoTipoId.nombre">
											<div ng-switch-when="texto" >
												<input type="text" class="inputText" ng-model="metac.planificado.diciembreString" ng-change="metac.almacenarPlanificado('diciembreString')" ng-readonly="metac.congelado" style="width: 100%; text-align: left"></input>
											</div>
											<div ng-switch-when="entero" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.diciembreEntero" ng-change="metac.almacenarPlanificado('diciembreEntero')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="0"></input>
											</div>
											<div ng-switch-when="decimal" class="form-group" >
												<input type="text" class="inputText" ng-model="metac.planificado.diciembreDecimal" ng-change="metac.almacenarPlanificado('diciembreDecimal')" ng-readonly="metac.congelado" style="width: 100%; text-align: right" ui-number-mask="2"></input>
											</div>
											<div ng-switch-when="booleano" class="form-group" >
												<input type="checkbox" ng-model="metac.planificado.diciembreString" ng-change="metac.almacenarPlanificado('diciembreString')" ng-readonly="metac.congelado"/>
											</div>
											<div ng-switch-when="fecha" class="form-group" >
												<input type="text" class="inputText" uib-datepicker-popup="{{metac.formatofecha}}" alt-input-formats="{{metac.altformatofecha}}"
													ng-model="metac.planificado.diciembreTiempo" is-open="metac.planificado.isOpenDiciembre" ng-readonly="metac.congelado"
													datepicker-options="metac.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="metac.congelado?'':metac.abrirPopupFecha(1012)"
													ng-change="metac.almacenarPlanificado('diciembreTiempo')"/>
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