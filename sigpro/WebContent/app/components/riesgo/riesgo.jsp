<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="riesgoController as riesgoc" class="maincontainer all_page" id="title">
	    <script type="text/ng-template" id="buscarPorRiesgo.jsp">
    		<%@ include file="/app/components/riesgo/buscarPorRiesgo.jsp"%>
  	    </script>
  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<div class="panel panel-default">
	   		<div class="panel-heading"><h3>Riesgos</h3><br/></div>
		</div>
		<h4>{{ riesgoc.proyectoNombre }}</h4><br/>
		<div class="row" align="center" ng-if="!riesgoc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="30040">
			       		<label class="btn btn-primary" ng-click="riesgoc.nuevo()" uib-tooltip="Nuevo">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="30010"><label class="btn btn-primary" ng-click="riesgoc.editar()" uib-tooltip="Editar">
				 	<span class="glyphicon glyphicon-pencil"></span> Editar</label>
					</shiro:hasPermission>
			       <shiro:hasPermission name="30030">
			       		<label class="btn btn-danger" ng-click="riesgoc.borrar()" uib-tooltip="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="30010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="riesgoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="riesgoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!riesgoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  riesgoc.totalRiesgos + (riesgoc.totalRiesgos == 1 ? " Riresgo" : " Riesgos" ) }}</div>
				<ul uib-pagination total-items="riesgoc.totalRiesgos" 
						ng-model="riesgoc.paginaActual" 
						max-size="riesgoc.numeroMaximoPaginas" 
						items-per-page="riesgoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="riesgoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
		</div>
		<div class="row second-main-form" ng-if="riesgoc.mostraringreso">
			<h2 ng-hide="!riesgoc.esnuevo"><small>Nuevo riesgo</small></h2>
			<h2 ng-hide="riesgoc.esnuevo"><small>Edición de riesgo</small></h2>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="30020">
			        	<label class="btn btn-success" ng-click="form.$valid ? riesgoc.guardar() : ''"  ng-disabled="!form.$valid" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="riesgoc.irATabla()" uib-tooltip="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group" ng-show="!riesgoc.esnuevo">
							<label for="id" class="floating-label">ID {{ riesgoc.riesgo.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="nombre" ng-model="riesgoc.riesgo.nombre" value="{{riesgoc.riesgo.nombre}}" onblur="this.setAttribute('value', this.value);" ng-required="true">
    						<label class="floating-label">* Nombre</label>
						</div>
						
						<div class="form-group">
				            	<input type="text" class="inputText" id="irietipo" name="irietipo" ng-model="riesgoc.riesgoTipoNombre" value="{{riesgoc.riesgoTipoNombre}}"
				            	onblur="this.setAttribute('value', this.value);" ng-click="riesgoc.buscarRiesgoTipo()" ng-readonly="true" ng-required="true"/>
				            	<span class="label-icon" ng-click="riesgoc.buscarRiesgoTipo()"><i class="glyphicon glyphicon-search"></i></span>
								<label for="campo3" class="floating-label">* Tipo Riesgo</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.impactoProyectado" value="{{riesgoc.riesgo.impactoProyectado}}" onblur="this.setAttribute('value', this.value);">
    						<label for="campo5" class="floating-label">Impacto proyectado</label>
						</div>
						
						<div class="form-group">
							<input type="number" class="inputText" ng-model="riesgoc.riesgo.impacto"  value="{{riesgoc.riesgo.impacto}}" onblur="this.setAttribute('value', this.value);">
							<label for="campo5" class="floating-label">Impacto</label>
						</div>
						
						<div class="form-group">
							<input type="number"  class="inputText" ng-model="riesgoc.riesgo.puntuacionImpacto" value="{{riesgoc.riesgo.puntuacionImpacto}}" onblur="this.setAttribute('value', this.value);"
							ng-min="1" ng-max="10">
							<label for="campo5" class="floating-label">Puntuación de impacto</label>
						</div>
						
						<div class="form-group">
							<select class="inputText" ng-model="riesgoc.probabilidad"
								ng-options="probabilidad as probabilidad.nombre for probabilidad in riesgoc.probabilidades track by probabilidad.valor">
								<option value="">Probabilidad</option>
							</select>
							<label for="nombre" class="floating-label">* Tipo dato</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.gatillosSintomas" value="{{riesgoc.riesgo.gatillosSintomas}}" onblur="this.setAttribute('value', this.value);">
    						<label for="descripcion" class="floating-label">Gatillos / sintomas</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.respuesta" value="{{riesgoc.riesgo.respuesta}}" onblur="this.setAttribute('value', this.value);">
    						<label for="descripcion" class="floating-label">Respuesta</label>
						</div>
						
						<div class="form-group">
			            	<input type="text" class="inputText" ng-model="riesgoc.colaboradorNombre" 
			            	ng-click="riesgoc.buscarColaborador()" value="{{riesgoc.colaboradorNombre}}" 
			            	onblur="this.setAttribute('value', this.value);" ng-readonly="true" />
			            	<span class="label-icon" ng-click="riesgoc.buscarColaborador()"><i class="glyphicon glyphicon-search"></i></span>
			            	<label for="campo3" class="floating-label">Responsable</label>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" ng-model="riesgoc.riesgo.riesgosSecundarios" value="{{riesgoc.riesgo.riesgosSecundarios}}" onblur="this.setAttribute('value', this.value);">
    						<label for="descripcion" class="floating-label">Riesgos secundarios</label>
						</div>
						
						<div class="form-group">
    						<input type="checkbox"  ng-model="riesgoc.ejecutado"> 
    						<label class="floating-label">Ejecutado</label>   						
						</div>
						
						<div class="form-group">
							<input type="text" class="inputText" uib-datepicker-popup="{{riesgoc.formatofecha}}" ng-model="riesgoc.riesgo.fechaEjecucion" is-open="riesgoc.fe_abierto"
									datepicker-options="riesgoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" 
									value="{{riesgoc.riesgo.fechaEjecucion}}" onblur="this.setAttribute('value', this.value);"   
									ng-click="riesgoc.abrirPopupFecha(1000)"/>
								<span class="label-icon" ng-click="riesgoc.abrirPopupFecha(1000)">
										<i class="glyphicon glyphicon-calendar"></i>
								</span>
							<label for="campo.id" class="floating-label">* Fecha de ejecución</label>
						</div>
						
						<div class="form-group" ng-repeat="campo in riesgoc.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{riesgoc.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="riesgoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="riesgoc.abrirPopupFecha($index)"
														value="{{campo.valor}}" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="riesgoc.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														value="{{number.valor}}">{{number.label}}</option>
								</select>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" id="descripcion" ng-model="riesgoc.riesgo.descripcion"
    						value="{{riesgoc.riesgo.descripcion}}" onblur="this.setAttribute('value', this.value);">
    						<label for="iprog" class="floating-label">Descripción</label>
						</div>
						<div class="panel panel-default">
					<div class="panel-heading  label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo"  class="label-form">Usuario que creo</label> 
									<p> {{ riesgoc.riesgo.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label  class="label-form" for="fechaCreacion">Fecha de creación</label>
									<p id="fechaCreacion"> {{ riesgoc.riesgo.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Usuario que actualizo</label> 
									<p id="usuarioCreo">{{ riesgoc.riesgo.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label  class="label-form" for="fechaActualizacion">Fecha de actualizacion</label> 
									<p id="usuarioCreo">{{ riesgoc.riesgo.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div align="center">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
				        <shiro:hasPermission name="30020">
				        	<label class="btn btn-success" ng-click="form.$valid ? riesgoc.guardar() : ''"  ng-disabled="!form.$valid" uib-tooltip="Guardar">
							<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
							</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="riesgoc.irATabla()" uib-tooltip="Ir a Tabla">
						<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
