<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="riesgoController as riesgoc" class="maincontainer_treeview all_page" id="title">
	    <script type="text/ng-template" id="buscarPorRiesgo.jsp">
    		<%@ include file="/app/components/riesgo/buscarPorRiesgo.jsp"%>
  	    </script>
  	    <shiro:lacksPermission name="30010">
			<span ng-init="riesgoc.redireccionSinPermisos()"></span>
		</shiro:lacksPermission>
		<br/>
		<div class="row" align="center" ng-show="!riesgoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group btn-group-sm">
			       <shiro:hasPermission name="30040">
			       		<label class="btn btn-default" ng-click="riesgoc.nuevo()" uib-tooltip="Nuevo Riesgo" tooltip-placement="left">
						<span class="glyphicon glyphicon-plus"></span></label>
			       </shiro:hasPermission> 		        
    			</div>				
    		</div>
    		<shiro:hasPermission name="30010">
    		<div class="col-sm-12" align="center">
    			<table st-table="riesgoc.display_riesgos" st-safe-src="riesgoc.riesgos" class="table table-striped">
					<thead>
						<tr>
							<th st-sort="nombre">Nombre</th>
							<th>Descripción</th>
							<shiro:hasPermission name="30030">
								<th width="1%"></th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="row in riesgoc.display_riesgos track by $index">
							<td  ng-click="riesgoc.editar(row)">{{ row.nombre }}</td>
							<td  ng-click="riesgoc.editar(row)">{{ row.descripcion }}</td>
							<shiro:hasPermission name="30030">
							<td><label class="btn btn-default btn-xs" ng-click="riesgoc.borrar($index)" uib-tooltip="Borrar" tooltip-placement="left">
								<span class="glyphicon glyphicon-trash"></span></label></td>
							</shiro:hasPermission>
						</tr>
					</tbody>
				</table>
			</div>
    		</shiro:hasPermission>
		</div>
		<div class="row second-main-form" ng-show="riesgoc.mostraringreso">
			<div class="page-header">
				<h2 ng-if="riesgoc.esnuevo"><small>Nuevo riesgo</small></h2>
				<h2 ng-if="!riesgoc.esnuevo"><small>Edición de riesgo</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group btn-group-sm">
					<label class="btn btn-default" ng-click="riesgoc.irATabla()" uib-tooltip="Ir a Riegos" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-list-alt"></span></label>
    			</div>
    		</div>
			<div class="col-sm-12">
						<div class="form-group" ng-show="!riesgoc.esnuevo">
							<label for="id" class="floating-label id_class">ID {{ riesgoc.riesgo.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="nombre" ng-model="riesgoc.riesgo.nombre" ng-value="riesgoc.riesgo.nombre" onblur="this.setAttribute('value', this.value);" ng-required="riesgoc.mostraringreso">
    						<label class="floating-label">* Nombre</label>
						</div>
						
						<div class="form-group">
				            	<input type="text" class="inputText" id="irietipo" name="irietipo" ng-model="riesgoc.riesgoTipoNombre" ng-value="riesgoc.riesgoTipoNombre"
				            	onblur="this.setAttribute('value', this.value);" ng-click="riesgoc.buscarRiesgoTipo()" ng-readonly="true" ng-required="riesgoc.mostraringreso"/>
				            	<span class="label-icon" ng-click="riesgoc.buscarRiesgoTipo()"><i class="glyphicon glyphicon-search"></i></span>
								<label class="floating-label">* Tipo Riesgo</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.impactoProyectado" ng-value="riesgoc.riesgo.impactoProyectado" onblur="this.setAttribute('value', this.value);" ng-required="riesgoc.mostraringreso">
    						<label class="floating-label">* Impacto proyectado</label>
						</div>
						
						<div class="form-group">
							<input type="number" class="inputText" ng-model="riesgoc.riesgo.impacto"  ng-value="riesgoc.riesgo.impacto" onblur="this.setAttribute('value', this.value);" ng-required="riesgoc.mostraringreso">
							<label class="floating-label">* Impacto</label>
						</div>
						
						<div class="form-group">
							<input type="number"  class="inputText" ng-model="riesgoc.riesgo.puntuacionImpacto" ng-value="riesgoc.riesgo.puntuacionImpacto" onblur="this.setAttribute('value', this.value);" 
							min="1" max="10" ng-required="riesgoc.mostraringreso">
							<label class="floating-label">* Puntuación de impacto</label>
						</div>
						
						<div class="form-group">
							<select class="inputText" ng-model="riesgoc.probabilidad"
								ng-options="probabilidad as probabilidad.nombre for probabilidad in riesgoc.probabilidades track by probabilidad.valor"
								ng-required="riesgoc.mostraringreso">
								<option value="">Seleccione probabilidad</option>
							</select>
							<label  class="floating-label">* Probabilidad</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.gatillosSintomas" ng-value="riesgoc.riesgo.gatillosSintomas" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Gatillos / sintomas</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.respuesta" ng-value="riesgoc.riesgo.respuesta" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Respuesta</label>
						</div>
						
						<div class="form-group">
			            	<input type="text" class="inputText" ng-model="riesgoc.colaboradorNombre" 
			            	ng-click="riesgoc.buscarColaborador()" ng-value="riesgoc.colaboradorNombre" 
			            	onblur="this.setAttribute('value', this.value);" ng-readonly="true" />
			            	<span class="label-icon" ng-click="riesgoc.buscarColaborador()"><i class="glyphicon glyphicon-search"></i></span>
			            	<label class="floating-label">Responsable</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.riesgosSecundarios" ng-value="riesgoc.riesgo.riesgosSecundarios" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Riesgos secundarios</label>
						</div>
						
						<div class="form-group">
    						<input type="checkbox"  ng-model="riesgoc.ejecutado" /> 
    						<label class="floating-label">Ejecutado</label>   						
						</div>
						
						<div class="form-group">
							<input type="text" class="inputText" uib-datepicker-popup="{{riesgoc.formatofecha}}" ng-model="riesgoc.fechaEjecucion" is-open="riesgoc.fe_abierto"
									datepicker-options="riesgoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="riesgoc.mostraringreso"
									ng-value="riesgoc.riesgo.fechaEjecucion" onblur="this.setAttribute('value', this.value);"/>
								<span class="label-icon" ng-click="riesgoc.abrirPopupFecha(1000)">
										<i class="glyphicon glyphicon-calendar"></i>
								</span><label class="floating-label">* Fecha de ejecución</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="descripcion" ng-model="riesgoc.riesgo.descripcion"
    						ng-value="riesgoc.riesgo.descripcion" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Descripción</label>
						</div>
						<div class="form-group" ng-repeat="campo in riesgoc.riesgo.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="1" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
									<label class="floating-label">{{ campo.nombre }}</label>
								</div>
								<div ng-switch-when="2" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label class="floating-label">{{ campo.nombre }}</label>
								</div>
								<div ng-switch-when="3" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label class="floating-label">{{ campo.nombre }}</label>
								</div>
								<div ng-switch-when="4" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
									<label class="floating-label">{{ campo.nombre }}</label>
								</div>
								<div ng-switch-when="5" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{riesgoc.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="riesgoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="riesgoc.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label class="floating-label">{{ campo.nombre }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														ng-value="number.valor">{{number.label}}</option>
								</select>
									<label class="floating-label">{{ campo.nombre }}</label>
								</div>
							</div>
						</div>
						<input type="hidden" ng-model="riesgoc.form_valid" name="form_valid" ng-required="riesgoc.mostraringreso" />
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group btn-group-sm">
					<label class="btn btn-default" ng-click="riesgoc.irATabla()" uib-tooltip="Ir a Riegos" tooltip-placement="left">
					<span class="glyphicon glyphicon-list-alt"></span></label>
    			</div>
    		</div>
		</div>
	</div>
