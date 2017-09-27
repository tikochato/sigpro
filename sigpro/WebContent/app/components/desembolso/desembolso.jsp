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
		<div align="center">
			<div class="operation_buttons" align="right">
				<div class="btn-group btn-group-sm">
			       <shiro:hasPermission name="9040">
			       		<label class="btn btn-default" ng-click="desembolsoc.nuevo()" uib-tooltip="Nuevo" tooltip-placement="bottom">
						<span class="glyphicon glyphicon-plus"></span></label>
			       </shiro:hasPermission> 
			    </div>				
    		</div>
    		<shiro:hasPermission name="9010">
    		<div align="center">
				<table st-table="desembolsosc.display_desembolsos" st-safe-src="desembolsoc.desembolsos" class="table" >
					<thead>
						<tr>
							<th st-sort="fecha">Fecha</th>
							<th st-sort="monto">Monto</th>
							<th st-sort="tipo_moneda" >T. Moneda</th>
							<shiro:hasPermission name="9030">
							<th width="1%"></th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="row in desembolsoc.desembolsos track by $index" >
							<td style="padding: 0px;"><div class="form-group" style="padding: 3px;">
													<input type="text" class="inputText"   uib-datepicker-popup="{{desembolsoc.formatofecha}}" ng-model="row.fecha" is-open="row.c_abierto"
														datepicker-options="desembolsoc.opcionesFecha" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-required="true"
														ng-value="row.fecha" onblur="this.setAttribute('value', this.value);"/>
													<span class="label-icon" ng-click="desembolsoc.mostrarCalendar($index)">
															<i class="glyphicon glyphicon-calendar"></i>
													</span></div></td>
							<td style="padding: 0px 5px 0px 5px;"><div class="form-group" style="padding: 3px;"><input type="text" class="inputText" ng-model="row.monto" ng-required="true"
												ng-value="row.monto" ng-required="true"
												onblur="this.setAttribute('value', this.value);" ui-number-mask="0" style="text-align: right;" /></div></td>
							<td style="padding: 0px;"><div class="form-group" style="padding: 3px;" >
								    <input type="text" class="inputText" ng-model="row.tipo_moneda_nombre"
									ng-click="desembolsoc.buscarTipoMoneda('Tipo de Moneda','',$index)" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
									<span class="label-icon" ng-click="desembolsoc.buscarTipoMoneda('Tipo de Moneda','',$index)"><i class="glyphicon glyphicon-search"></i></span>
								</div></td>
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
		
		
	</div>
