<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="programapropiedadController as programapropiedadc" class="maincontainer all_page" id="title">
		<shiro:lacksPermission name="38010">
			<p ng-init="programapropiedadc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
	  		<div class="panel-heading"><h3>Propiedades de Programa</h3></div>
		</div>
		
		<div align="center" ng-hide="programapropiedadc.mostraringreso">
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
			       <shiro:hasPermission name="38040">
			       		<label class="btn btn-primary" ng-click="programapropiedadc.nuevo()" uib-tooltip="Nuevo">
				<span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			       </shiro:hasPermission>
			       <shiro:hasPermission name="38010">
			       	<label class="btn btn-primary" ng-click="programapropiedadc.editar()" uib-tooltip="Editar">
				<span class="glyphicon glyphicon-pencil"></span> Editar</label>
			       	</shiro:hasPermission>
			       <shiro:hasPermission name="38030">
			       		<label class="btn btn-danger" ng-click="programapropiedadc.borrar()" uib-tooltip="Borrar">
				<span class="glyphicon glyphicon-trash"></span> Borrar</label>
			       </shiro:hasPermission>
    			</div>
    		</div>
    		<shiro:hasPermission name="38010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="programapropiedadc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
							<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
						</a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="programapropiedadc.gridOptions" ui-grid-save-state
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!programapropiedadc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br/>
				<div class="total-rows">Total de {{  programapropiedadc.totalProgramaPropiedades + (programapropiedadc.totalProgramaPropiedades == 1 ? " Propiedad" : " Propiedades" ) }}</div>
				<ul uib-pagination total-items="programapropiedadc.totalProgramaPropiedades"
						ng-model="programapropiedadc.paginaActual"
						max-size="programapropiedadc.numeroMaximoPaginas"
						items-per-page="programapropiedadc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="programapropiedadc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>

		</div>
		<div class="row second-main-form" ng-show="programapropiedadc.mostraringreso">
			<div class="page-header">
				<h2 ng-hide="!programapropiedadc.esnuevo"><small>Nueva Propiedad</small></h2>
				<h2 ng-hide="programapropiedadc.esnuevo"><small>Edición de Propiedad</small></h2>
			</div>
			<div class="col-sm-12 operation_buttons" align="right">
				<div class="btn-group">
					<shiro:hasPermission name="38020">
			        	<label class="btn btn-success" ng-click="form.$valid ? programapropiedadc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar" tooltip-placement="bottom">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
					</shiro:hasPermission>
			        <label class="btn btn-primary" ng-click="programapropiedadc.irATabla()" uib-tooltip="Ir a Tabla" tooltip-placement="bottom">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
    			</div>
    		</div>

			<div class="col-sm-12">
				<form name="form">
						<div class="form-group">
							<label for="id" class="floating-label id_class">ID {{ programapropiedadc.programapropiedad.id }}</label>
    						<br/><br/>
						</div>
						<div class="form-group">
    						<input type="text" id="nombre" class="inputText" ng-model="programapropiedadc.programapropiedad.nombre"  ng-value="programapropiedadc.programapropiedad.nombre" 
    						onblur="this.setAttribute('value', this.value);" ng-required="true">
							<label for="nombre" class="floating-label">* Nombre</label>
						</div>
						<div class="form-group">
							<select class="inputText" ng-model="programapropiedadc.datotiposeleccionado"
								ng-options="tipo as tipo.nombre for tipo in programapropiedadc.tipodatos track by tipo.id"
								ng-readonly="true"
								ng-disabled="!programapropiedadc.esnuevo" ng-required="true">
								<option value="">Seleccione un tipo</option>
							</select>
							<label for="nombre" class="floating-label">* Tipo dato</label>
						</div>
						<div class="form-group">
							<input type="text" class="inputText" ng-model="programapropiedadc.programapropiedad.descripcion" ng-value="programapropiedadc.programapropiedad.descripcion" onblur="this.setAttribute('value', this.value);" >
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
									<p class=""> {{ programapropiedadc.programapropiedad.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group" >
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
									<p class="" > {{ programapropiedadc.programapropiedad.fechaCreacion }} </p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo" class="label-form">Usuario que actualizo</label> 
									<p class="" >{{ programapropiedadc.programapropiedad.usuarioActualizo }} </p>
								</div>	
							</div>
							<div class="col-sm-6">		
								<div class="form-group">
									<label for="fechaActualizacion" class="label-form">Fecha de actualizacion</label> 
									<p class="" >{{ programapropiedadc.programapropiedad.fechaActualizacion }} </p>
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
				        <shiro:hasPermission name="38020">
				        	<label class="btn btn-success" ng-click="form.$valid ? programapropiedadc.guardar() : ''" ng-disabled="!form.$valid" uib-tooltip="Guardar">
					<span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
						</shiro:hasPermission>
				        <label class="btn btn-primary" ng-click="programapropiedadc.irATabla()" uib-tooltip="Ir a Tabla">
				<span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
	    			</div>
	    		</div>
    		</div>
		</div>
	</div>
