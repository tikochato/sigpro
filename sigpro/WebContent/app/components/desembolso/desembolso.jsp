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
			<p ng-init="desembolsoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Desembolso</h3></div>
		</div>
		<div class="subtitulo">
			{{ desembolsoc.objetoTipoNombre }} {{ desembolsoc.proyectonombre }}
		</div>
		
		<div class="row" align="center" ng-if="!desembolsoc.mostraringreso">
			
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			  <shiro:hasPermission name="9040">
			    <label class="btn btn-primary" ng-click="desembolsoc.nuevo()" uib-tooltip="Nuevo">
			    <span class="glyphicon glyphicon-plus"></span> Nuevo</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="9010">
			    <label class="btn btn-primary" ng-click="desembolsoc.editar()" uib-tooltip="Editar">
			    <span class="glyphicon glyphicon-pencil"></span> Editar</label>
			  </shiro:hasPermission>
			  <shiro:hasPermission name="9030">
			    <label class="btn btn-danger" ng-click="desembolsoc.borrar()" uib-tooltip="Borrar">
			    <span class="glyphicon glyphicon-trash"></span> Borrar</label>
			  </shiro:hasPermission>
			  </div>
			</div>
    		
    		<shiro:hasPermission name="9010">
    		<div class="col-sm-12" align="center">
    			<div style="height: 35px;">
					<div style="text-align: right;"><div class="btn-group" role="group" aria-label="">
						<a class="btn btn-default" href ng-click="desembolsoc.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left"><span class="glyphicon glyphicon-repeat" aria-hidden="true"></span></a>
					</div>
					</div>
				</div>
				<br/>
				<div id="maingrid" ui-grid="desembolsoc.gridOptions" ui-grid-save-state 
						ui-grid-move-columns ui-grid-resize-columns ui-grid-selection ui-grid-pinning ui-grid-pagination class="grid">
					<div class="grid_loading" ng-hide="!desembolsoc.mostrarcargando">
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				</div>
				<br>
				<div class="total-rows">
				  Total de {{  desembolsoc.totalDesembolsos + (desembolsoc.totalDesembolsos == 1 ? " Desembolsos" : " Desembolso" ) }}
				</div>
				<ul uib-pagination total-items="desembolsoc.totalDesembolsos" 
						ng-model="desembolsoc.paginaActual" 
						max-size="desembolsoc.numeroMaximoPaginas" 
						items-per-page="desembolsoc.elementosPorPagina"
						first-text="Primero"
						last-text="Último"
						next-text="Siguiente"
						previous-text="Anterior"
						class="pagination-sm" boundary-links="true" force-ellipses="true"
						ng-change="desembolsoc.cambioPagina()"
				></ul>
			</div>
    		</shiro:hasPermission>
    		
		</div>
		<div class="row second-main-form" ng-show="desembolsoc.mostraringreso">
			<div class="page-header">
			    <h2 ng-hide="!desembolsoc.esnuevo"><small>Nuevo Desembolso</small></h2>
			    <h2 ng-hide="desembolsoc.esnuevo"><small>Edición de Desembolso</small></h2>
			</div>
    		<div class="operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="9020">
			      <label class="btn btn-success" ng-click="form.$valid ? desembolsoc.guardar(): ''" ng-disabled="!form.$valid" title="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="desembolsoc.irATabla()" title="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
			
			<div class="col-sm-12">
				<form name="form" id="form">
					<div class="form-group">
					  <label for="id" class="floating-label">ID {{ desembolsoc.desembolso.id }}</label>
					  <br/><br/>
					</div>
					
					<div class="form-group" >
								  <input type="text"  class="inputText" uib-datepicker-popup="dd/MM/yyyy" ng-model="desembolsoc.fecha" is-open="desembolsoc.popup.abierto"
								            datepicker-options="desembolsoc.opcionesFecha" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true"  ng-click="desembolsoc.mostrarCalendar()"
								            ng-value="desembolsoc.fecha" onblur="this.setAttribute('value', this.value);"/>
								            <span class="label-icon" ng-click="desembolsoc.mostrarCalendar()">
								              <i class="glyphicon glyphicon-calendar"></i>
								            </span>
								  <label for="campo.id" class="floating-label">*Fecha</label>
					</div>
					
					<div class="form-group">
					   <input type="text" name="imonto"  class="inputText" id="imonto" ui-number-mask="2"
					     ng-model="desembolsoc.desembolso.monto" ng-value="desembolsoc.desembolso.monto"   
					     onblur="this.setAttribute('value', this.value);" ng-required="true" >
					   <label class="floating-label">* Monto</label>
					</div>
					<div class="form-group" >
						    <input type="text" class="inputText" id="itipomonedanombre" name="itipomonedanombre" ng-model="desembolsoc.desembolso.tipomonedanombre" ng-value="desembolsoc.desembolso.tipomonedanombre" 
							ng-click="desembolsoc.buscarTipoMoneda()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
							<span class="label-icon" ng-click="desembolsoc.buscarTipoMoneda()"><i class="glyphicon glyphicon-search"></i></span>
							<label for="campo3" class="floating-label">* Tipo de Moneda</label>
						</div>
			        <br/>
			        
			        <div class="form-group" >
						    <input type="text" class="inputText" id="idesembolsotipo" name="idesembolsotipo" ng-model="desembolsoc.desembolso.desembolsotipo" ng-value="desembolsoc.desembolso.desembolsotipo" 
							ng-click="desembolsoc.buscarTipoDesembolso()" onblur="this.setAttribute('value', this.value);" ng-readonly="true" ng-required="true"/>
							<span class="label-icon" ng-click="desembolsoc.buscarTipoDesembolso()"><i class="glyphicon glyphicon-search"></i></span>
							<label for="campo3" class="floating-label">* Tipo Desembolso</label>
						</div>
			        <br/>
					<div class="panel panel-default">
					<div class="panel-heading label-form" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo" class="label-form">Usuario que creo</label>
			   						<p >{{ desembolsoc.desembolso.usuarioCreo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion" class="label-form">Fecha de creación</label>
			   						<p >{{ desembolsoc.desembolso.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioActualizo">Usuario que actualizo</label>
			   						<p >{{ desembolsoc.desembolso.usuarioActualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label class="label-form" for="fechaActualizacion">Fecha de actualizacion</label>
			   						<p>{{ desembolsoc.desembolso.fechaActualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				</form>
			</div>
			<div align="center" class="label-form">Los campos marcados con * son obligatorios</div>
    		<div class="col-sm-12 operation_buttons" align="right">
			  <div class="btn-group">
			    <shiro:hasPermission name="9020">
			      <label class="btn btn-success" ng-click="form.$valid ? desembolsoc.guardar(): ''" ng-disabled="!form.$valid" title="Guardar">
			      <span class="glyphicon glyphicon-floppy-saved"></span> Guardar</label>
			    </shiro:hasPermission>
			    <label class="btn btn-primary" ng-click="desembolsoc.irATabla()" title="Ir a Tabla">
			    <span class="glyphicon glyphicon-list-alt"></span> Ir a Tabla</label>
			  </div>
			</div>
		</div>
	</div>
