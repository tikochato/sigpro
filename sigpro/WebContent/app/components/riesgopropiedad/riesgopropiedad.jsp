<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="riesgopropiedadController as riesgopropiedadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="31010">
			<p ng-init="riesgopropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		<h3>Propiedad Riesgo</h3><br/>
		<div class="row" align="center" ng-hide="riesgopropiedadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="31040">
			       		<label class="btn btn-primary" ng-click="riesgopropiedadc.nuevo()" uib-tooltip="Nuevo">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="31010"><label class="btn btn-primary" ng-click="riesgopropiedadc.editar()" uib-tooltip="Editar">
					<span class="glyphicon glyphicon-pencil"></span> Editar</label>
					</shiro:hasPermission>
			       <shiro:hasPermission name="31030">
			       		<label class="btn btn-primary" ng-click="riesgopropiedadc.borrar()" uib-tooltip="Borrar">
					<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="31010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="riesgopropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
							<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
						</a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="riesgopropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!riesgopropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  riesgopropiedadc.totalRiesgoPropiedades + (riesgopropiedadc.totalRiesgoPropiedades == 1 ? " Riesgo" : " Riesgos" ) }}</div>
				<ul uib-pagination total-items="riesgopropiedadc.totalRiesgoPropiedades" 
						ng-model="riesgopropiedadc.paginaActual"
						max-size="riesgopropiedadc.numeroMaximoPaginas"
						items-per-page="riesgopropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="riesgopropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="riesgopropiedadc.mostraringreso">
			<h2 ng-hide="!riesgopropiedadc.esnuevo">Nueva Propiedad</h2>
			<h2 ng-hide="riesgopropiedadc.esnuevo">Edición de Propiedad</h2>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="31020">
			        	<label class="btn btn-success" ng-click="form.$valid ? riesgopropiedadc.guardar() : ''"  ng-disabled="!form.$valid" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="riesgopropiedadc.irATabla()" uib-tooltip="Ir a Tabla">
					<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label">ID {{ riesgopropiedadc.riesgopropiedad.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="nombre" ng-model="riesgopropiedadc.riesgopropiedad.nombre" 
    							value="{{riesgopropiedadc.riesgopropiedad.nombre}}" onblur="this.setAttribute('value', this.value);"ng-required="true">
    						<label class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							<select class="inputText" ng-model="riesgopropiedadc.riesgopropiedad.datotipoid"
								ng-options="tipo as tipo.nombre for tipo in riesgopropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!riesgopropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione una opción</option>
							</select>
							<label class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText" id="descripcion" ng-model="riesgopropiedadc.riesgopropiedad.descripcion"
    							value="{{riesgopropiedadc.riesgopropiedad.descripcion}}" onblur="this.setAttribute('value', this.value);">
    						<label class="floating-label">Descripción</label>
						</div>
						<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label> 
									<p class="form-control-static"> {{ riesgopropiedadc.riesgopropiedad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion">Fecha de creación</label>
									<p class="form-control-static" id="fechaCreacion"> {{ riesgopropiedadc.riesgopropiedad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label> 
									<p class="form-control-static" id="usuarioCreo">{{ riesgopropiedadc.riesgopropiedad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label> 
									<p class="form-control-static" id="usuarioCreo">{{ riesgopropiedadc.riesgopropiedad.fechaActualizacion }} </p>
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
				        <shiro:hasPermission name="31020">
			        		<label class="btn btn-success" ng-click="form.$valid ? riesgopropiedadc.guardar() : ''"  ng-disabled="!form.$valid" uib-tooltip="Guardar">
							<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        	</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="riesgopropiedadc.irATabla()" uib-tooltip="Guardar">
						<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
