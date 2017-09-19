<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="metaController as metac" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="17010">
			<p ng-init="metac.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Metas de {{ metac.nombreTipoPcp }}</h3></div>
		</div>
		<div class="subtitulo">
			{{metac.nombrePcp}}
		</div>
				
		<div align="center" ng-hide="metac.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="17040">
			       		<label class="btn btn-primary" ng-click="metac.nueva()" title="Nuevo" click="setFocusToTextBox()">
						<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission> 
			       <shiro:hasPermission name="17010">
			       		<label class="btn btn-primary" ng-click="metac.editar()" title="Editar">
						<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="17030">
			       		<label class="btn btn-danger" ng-click="metac.borrar()" title="Borrar">
						<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
			        
			        
    			</div>				
    		</div>
    		<shiro:hasPermission name="17010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="metac.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
						<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="metac.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!metac.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  metac.totalMetas + (metac.totalMetas == 1 ? " Meta" : " Metas" ) }}</div>
				<ul uib-pagination total-items="metac.totalMetas" 
						ng-model="metac.paginaActual" 
						max-size="metac.numeroMaximoPaginas" 
						items-per-page="metac.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="metac.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="metac.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!metac.esnueva"><small>Nueva Meta</small></h2>
			    <h2 ng-hide="metac.esnueva"><small>Edición de Meta</small></h2>
			</div>
		
		<div class="operation_buttons">
			<div class="btn-group" ng-hide="metac.esnueva">
				<label class="btn btn-default" ng-click="metac.irAMetaValores()" uib-tooltip="Valores" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-sound-5-1"></span></label>
		
			</div>
				<div class="btn-group"  style="float: right;">
					<shiro:hasPermission name="17020">
			        	<label class="btn btn-success" ng-click="form.$valid ? metac.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			        </shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="metac.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>
			
			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label id_class">ID {{ metac.meta.id  }}</label>
							<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" id="nombre" class="inputText"  ng-model="metac.meta.nombre"
    						ng-value="metac.meta.nombre" onblur="this.setAttribute('value', this.value);">
    						<label  class="floating-label">Nombre</label>
						</div>
						<div class="form-group">
    						<input type="text" class="inputText"  ng-model="metac.meta.descripcion"
    						ng-value="metac.meta.descripcion" onblur="this.setAttribute('value', this.value);">
    						<label  class="floating-label">Descripción</label>
						</div>
						<div class="form-group">
							<select class="inputText" ng-model="metac.tipoMetaSeleccionado"
								ng-options="tipo as tipo.nombre for tipo in metac.metatipos track by tipo.id"
								ng-readonly="true"
								ng-required="true">
								<option value="">Seleccione un tipo</option>
							</select>
							<label class="floating-label">* Tipo Meta</label>
						</div>
						<div class="form-group">
							<select class="inputText" ng-model="metac.unidadMedidaSeleccionado"
								ng-options="unidad as unidad.nombre for unidad in metac.metaunidades track by unidad.id"
								ng-readonly="true"
								ng-required="true">
								<option value="">Seleccione una unidad</option>
							</select>
							<label class="floating-label">* Unidad de Medida</label>
						</div>
						<div class="form-group" ng-hide="true">
							<select class="inputText" ng-model="metac.tipoValorSeleccionado"
								ng-options="tipoValor as tipoValor.nombre for tipoValor in metac.datoTipos track by tipoValor.id"
								ng-readonly="true"
								ng-required="true">
								<option value="">Seleccione una unidad</option>
							</select>
							<label class="floating-label">* Tipo Dato</label>
						</div>
					<br/>
					
						<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form">Usuario que creo</label> 
									<p class=""> {{ metac.meta.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label class="label-form">Fecha de creación</label>
									<p > {{ metac.meta.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form">Usuario que actualizo</label> 
									<p >{{ metac.meta.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label class="label-form">Fecha de actualizacion</label> 
									<p>{{ metac.meta.fechaActualizacion }} </p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
				<div class="col-sm-12 operation_buttons" align="right">
					<div class="btn-group">
						<shiro:hasPermission name="17020">
				        	<label class="btn btn-success" ng-click="form.$valid ? metac.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
				        </shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="metac.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
