<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="recursoController as recursoc" class="maincontainer all_page" id="title">
	    <script type="text/ng-template" id="buscarRecursoTipo.jsp">
    		<%@ include file="/app/components/recurso/buscarRecursoTipo.jsp"%>
  	    </script>
  	    <shiro:lacksPermission name="26010">
  	   		<p ng-init="recursoc.redireccionSinPermisos()"></p>
  	    </shiro:lacksPermission>
		
	<div class="panel panel-default">
	  <div class="panel-heading"><h3>Recursos</h3></div>
	</div>
	
		<div align="center" ng-hide="recursoc.mostraringreso">
		<br>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="26040">
			       		<label class="btn btn-primary" ng-click="recursoc.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="26010"><label class="btn btn-primary" ng-click="recursoc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
				</shiro:hasPermission>
			       <shiro:hasPermission name="26030">
			       		<label class="btn btn-danger" ng-click="recursoc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="26010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="recursoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="recursoc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!recursoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  recursoc.totalRecursos + (recursoc.totalRecursos == 1 ? " Recurso" : " Recursos" ) }}</div>
				<ul uib-pagination total-items="recursoc.totalRecursos"
						ng-model="recursoc.paginaActual"
						max-size="recursoc.numeroMaximoPaginas"
						items-per-page="recursoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="recursoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="recursoc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!recursoc.esnuevo"><small>Nuevo recurso</small></h2>
				<h2 ng-hide="recursoc.esnuevo"><small>Edición de recurso</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="26020">
			        	<label class="btn btn-success" ng-click="form.$valid ? recursoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="recursoc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
		<br>
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label">ID {{ recursoc.recurso.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="nombre" ng-model="recursoc.recurso.nombre"  ng-value="recursoc.recurso.nombre" onblur="this.setAttribute('value', this.value);" ng-required="true" show-focus="recursoc.mostraringreso">
							<label for="nombre" class="floating-label">* Nombre</label>
						</div>

						<div class="form-group">
				            	<input type="text" class="inputText" id="irectipo" name="irectipo" ng-model="recursoc.recurso.recursotiponombre" ng-value="recursoc.recurso.recursotiponombre" 
		            		ng-click="recursoc.buscarRecursoTipo()" onblur="this.setAttribute('value', this.value);"  ng-readonly="true" ng-required="true"/>
				            	<span class="label-icon" ng-click="recursoc.buscarRecursoTipo()"><i class="glyphicon glyphicon-search"></i></span>
							<label for="campo3" class="floating-label">* Tipo Recurso</label>
						</div>
						<div class="form-group">
				            	<input type="text" class="inputText" id="iumedidad" name="iumedidad" ng-model="recursoc.recurso.medidanombre" ng-value="recursoc.recurso.medidanombre" 
		            		ng-click="recursoc.buscarUnidadMedida()" onblur="this.setAttribute('value', this.value);"  ng-readonly="true" ng-required="true"/>
				            	<span class="label-icon" ng-click="recursoc.buscarUnidadMedida()"><i class="glyphicon glyphicon-search"></i></span>
							<label for="campo3" class="floating-label">* Unidad de Medida</label>
						</div>

						<div class="form-group" ng-repeat="campo in recursoc.camposdinamicos">
							<div ng-switch="campo.tipo">
								<div ng-switch-when="texto" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText" 
										ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>	
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="entero" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="inputText"   
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="decimal" class="form-group" >
									<input type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="inputText"  
									ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="booleano" class="form-group" >
									<input type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="fecha" class="form-group" >
									<input type="text" id="{{ 'campo_'+campo.id }}" class="inputText" uib-datepicker-popup="{{recursoc.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
														datepicker-options="recursoc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-click="recursoc.abrirPopupFecha($index)"
														ng-value="campo.valor" onblur="this.setAttribute('value', this.value);"/>
														<span class="label-icon" ng-click="recursoc.abrirPopupFecha($index)">
															<i class="glyphicon glyphicon-calendar"></i>
														</span>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
								<div ng-switch-when="select" class="form-group" >
									<select id="{{ 'campo_'+campo.id }}" class="inputText" ng-model="campo.valor">
													<option value="">Seleccione una opción</option>
													<option ng-repeat="number in campo.opciones"
														ng-value="number.valor">{{number.label}}</option>
								</select>
									<label for="campo.id" class="floating-label">{{ campo.label }}</label>
								</div>
							</div>
						</div>
						
						<div class="form-group">
    						<input type="text" class="inputText" id="descripcion" ng-model="recursoc.recurso.descripcion"
    						ng-value="recursoc.recurso.descripcion" onblur="this.setAttribute('value', this.value);">
							<label for="descripcion" class="floating-label">Descripción</label>
						</div>
						<br/>
						<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label> 
									<p class=""> {{ recursoc.recurso.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" id="fechaCreacion"> {{ recursoc.recurso.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" id="usuarioCreo">{{ recursoc.recurso.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" id="usuarioCreo">{{ recursoc.recurso.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
				        <shiro:hasPermission name="26020">
				        	<label class="btn btn-success" ng-click="form.$valid ? recursoc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="recursoc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
