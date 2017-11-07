<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="desembolsoController as desembolsoc" class="maincontainer all_page" id="title">
	<script type="text/ng-template" id="buscarDesembolsoTipo.jsp">
    	<%@ include file="/app/components/desembolso/buscarDesembolsoTipo.jsp"%>
  	</script>
  	<script type="text/ng-template" id="buscarTipoMoneda.jsp">
    	<%@ include file="/app/components/desembolso/buscarTipoMoneda.jsp"%>
  	</script>
  		<shiro:lacksPermission name="9010">
			<span ng-init="desembolsoc.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		<div class="col-sm-12" align="center" style="padding-left: 0px;">
			<div class="row">
				<div align="left" class="col-sm-3">
					<div class="form-group">
						<input type="text" class="inputText input-money" ng-model="desembolsoc.montoPorDesembolsar" ui-number-mask="0" 
						ng-value="desembolsoc.montoPorDesembolsar" onblur="this.setAttribute('value', this.value);" ng-readonly="true"/>
						<label class="floating-label" >Monto por Desembolsar $</label>
					</div>
				</div>
				<div align="left" class="col-sm-3">
					<div class="form-group">
						<input type="text" class="inputText input-money" ng-model="desembolsoc.desembolsoAFechaUsd" ui-number-mask="0" 
						ng-value="desembolsoc.desembolsoAFechaUsd" onblur="this.setAttribute('value', this.value);" ng-readonly="true"/>
						<label class="floating-label" >Desembolso a la Fecha $</label>
					</div>
				</div>
				<div align="left" class="col-sm-3">
					<div class="form-group">
						<input type="text" class="inputText" ng-model="desembolsoc.fechaCierreActual"
							ng-readonly="true"	ng-value="desembolsoc.fechaCierreActual" onblur="this.setAttribute('value', this.value);"/>
						<label class="floating-label" >Fecha de Cierre Actual</label>
					</div>
				</div>
				<div class="operation_buttons" align="right">
					<div class="btn-group btn-group-sm">
				       <shiro:hasPermission name="9040">
				       		<label class="btn btn-default" ng-click="desembolsoc.nuevo()" uib-tooltip="Nuevo" tooltip-placement="bottom">
							<span class="glyphicon glyphicon-plus"></span></label>
				       </shiro:hasPermission> 
				    </div>				
	    		</div>	
			</div>
		</div>

		<div align="center">
    		<shiro:hasPermission name="9010">
    		<div align="center">
				<table st-table="desembolsoc.display_desembolsos" st-safe-src="desembolsos" class="table" >
					<thead>
						<tr>
							<th st-sort="fecha">Fecha</th>
							<th st-sort="monto">Monto</th>							
							<shiro:hasPermission name="9030">
							<th width="1%"></th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="row in desembolsoc.display_desembolsos track by $index" >
							<td style="padding: 0px;"><div class="form-group" style="padding: 3px;">
													<input type="text" class="inputText"   uib-datepicker-popup="{{desembolsoc.formatofecha}}" alt-input-formats="{{desembolsoc.altformatofecha}}"
														ng-model="row.fecha" is-open="row.c_abierto"
														datepicker-options="desembolsoc.opcionesFecha" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
														ng-value="row.fecha" onblur="this.setAttribute('value', this.value);"/>
													<span class="label-icon" ng-click="desembolsoc.mostrarCalendar($index)">
															<i class="glyphicon glyphicon-calendar"></i>
													</span></div></td>
							<td style="padding: 0px 5px 0px 5px;"><div class="form-group" style="padding: 3px;"><input type="text" class="inputText" ng-model="row.monto" ng-required="true"
												ng-value="row.monto" ng-required="true"
												onblur="this.setAttribute('value', this.value);" ui-number-mask="2" style="text-align: right;" /></div></td>
							<shiro:hasPermission name="9030"><td width="1%">
					       		<label class="btn btn-default btn-xs" ng-click="desembolsoc.borrar(row)" uib-tooltip="Borrar" tooltip-placement="bottom">
								<span class="glyphicon glyphicon-trash"></span></label>
					       </td></shiro:hasPermission>
						</tr>
					</tbody>
				</table>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<br>
		<div class="row">
			<div class="col-sm-6">
				Total: {{ desembolsoc.totalDesembolsos | currency:" ":2 }}
			</div>
	  </div>
		
	</div>
