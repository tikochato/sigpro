<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<div ng-controller="controlProducto as producto" class="maincontainer all_page">

	<script type="text/ng-template" id="buscarPorProducto.jsp">
	    <%@ include file="/app/components/producto/buscarPorProducto.jsp"%>
	</script>

	<h3>{{ producto.esForma ? (producto.esNuevo ? "Nuevo Producto" : "Editar Producto") : "Producto" }}</h3>
	<h4>{{ producto.componenteNombre }}</h4><br/>

	<br />
  
	<div align="center" ng-hide="producto.esForma">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<shiro:hasPermission name="crearTipoProducto">
					<label class="btn btn-primary" ng-click="producto.nuevo()">Nuevo</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="editarTipoProducto">
					<label class="btn btn-primary" ng-click="producto.editar()">Editar</label>
				</shiro:hasPermission>
				<shiro:hasPermission name="eliminarTipoProducto">
					<label class="btn btn-primary" ng-click="producto.borrar()">Borrar</label>
				</shiro:hasPermission>
			</div>
		</div>
		<shiro:hasPermission name="verTipoProducto">
			<div class="col-sm-12" align="center">
				<div style="height: 35px;">
					<div style="text-align: right;">
						<div class="btn-group" role="group" aria-label="">
							<a class="btn btn-default" href ng-click="producto.reiniciarVista()" role="button" uib-tooltip="Reiniciar la vista de la tabla" tooltip-placement="left">
								<span class="glyphicon glyphicon-repeat" aria-hidden="true"></span>
							</a>
						</div>
					</div>
				</div>
				<br/>
				<div id="grid1" ui-grid="producto.opcionesGrid"
					ui-grid-save-state ui-grid-move-columns ui-grid-resize-columns
					ui-grid-selection ui-grid-pinning ui-grid-pagination>
					<div class="grid_loading" ng-hide="!producto.mostrarCargando">
						<div class="msg">
							<span><i class="fa fa-spinner fa-spin fa-4x"></i> 
								<br />
								<br /> <b>Cargando, por favor espere...</b> 
							</span>
						</div>
					</div>
				</div>
				<br/>
			<div class="total-rows">Total de {{  producto.totalElementos + (producto.totalElementos == 1 ? " Producto" : " Productos" ) }}</div>
				<ul uib-pagination total-items="producto.totalElementos"
					ng-model="producto.paginaActual"
					max-size="producto.numeroMaximoPaginas"
					items-per-page="producto.elementosPorPagina"
					first-text="Primero" last-text="Último" next-text="Siguiente"
					previous-text="Anterior" class="pagination-sm"
					boundary-links="true" force-ellipses="true"
					ng-change="producto.cambioPagina()"></ul>
			</div>
		</shiro:hasPermission>

	</div>

	<div ng-show="producto.esForma" class="row main-form">
		<h4 ng-hide="!actividadc.esnuevo">Nueva Producto</h4>
			<h4 ng-hide="actividadc.esnuevo">Edición de Producto</h4>
			<div class="col-sm-12 operation_buttons" align="left"></div>
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? producto.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				<label class="btn btn-primary" ng-click="producto.cancelar()">Ir a Tabla</label>
			</div>
		</div>
		<div>
		<div class="col-sm-12">
			<form name="form" class="css-form">
				
					<div class="form-group col-sm-3" ng-show="!producto.esNuevo">
						<label for="campo0">ID:</label> 
						<p class="form-control-static" id="campo0" name="campo0" >{{producto.producto.id}}</p>
					</div>
				
				
					<div class="form-group">
						<label for="campo1">* Nombre:</label> 
						<input type="text" class="form-control" placeholder="Nombre del producto" ng-model="producto.producto.nombre" ng-required="true" />
					</div>
					
					<div class="form-group"  >
						<label for="isnip">SNIP</label>
						<input type="number"   ng-model="producto.producto.snip" class="form-control" placeholder="SNIP" >
					</div>
				
					<div class="form-group row" >
						<div class="form-group col-sm-2 " >
						       <label for="iprog">Programa</label>
						       <input type="number" class="form-control" placeholder="Programa" ng-model="producto.producto.programa"  ng-maxlength="4" style="text-align: center" />
						</div>
						<div class="form-group col-sm-2 " >
						  <label for="isubprog">Subprograma</label>
						  <input type="number" class="form-control" placeholder="Sub-programa" ng-model="producto.producto.subprograma" ng-maxlength="4" style="text-align: center" />
						</div>
						<div class="form-group col-sm-2 " >
						  <label for="iproy_">Proyecto:</label>
						  <input type="number" class="form-control" placeholder="Proyecto" ng-model="producto.producto.proyecto_"  ng-maxlength="4" style="text-align: center" />
						</div>
						<div class="form-group col-sm-2 " >
						  <label for="iproy_">Actividad:</label>
						  <input type="number" class="form-control" placeholder="Proyecto" ng-model="producto.producto.actividad" ng-maxlength="4" style="text-align: center"  />
						</div>
						<div class="form-group col-sm-2 " >
						  <label for="iobra">Obra</label>
						  <input type="number" class="form-control" placeholder="Obra" ng-model="producto.producto.obra" ng-maxlength="4" style="text-align: center"/>
						</div>
						<div class="form-group col-sm-2 " >
						  <label for="campo5">Fuente</label>
						  <input type="number" class="form-control" placeholder="Fuente" ng-model="producto.producto.fuente" ng-maxlength="4" style="text-align: center"/>
						</div>
					</div>

					<div class="form-group" >
						<label for="campo2"> Descripción:</label> 
						<input type="text" class="form-control" placeholder="Descripcion del producto" ng-model="producto.producto.descripcion" />
					</div>
					
					<div class="form-group" >
			          <label for="campo3">* Tipo:</label>
			          <div class="input-group"> 
			            <input type="text" class="form-control" placeholder="Tipo de producto" ng-model="producto.tipoNombre" ng-readonly="true" ng-required="true"/>
			            <span class="input-group-addon" ng-click="producto.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>

					<div class="form-group">
			          <label for="campo5">Producto:</label>
			          <div class="input-group"> 
			            <input type="text" class="form-control" placeholder="Producto padre" ng-model="producto.productoPadreNombre" ng-readonly="true" ng-required="true"/>
			            <span class="input-group-addon" ng-click="producto.buscarProducto()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>
			        
			        <div class="form-group">
			          <label for="campo5">Unidad Ejecutora:</label>
			          <div class="input-group"> 
			            <input type="text" class="form-control" placeholder="Producto padre" ng-model="producto.unidadEjecutoraNombre" ng-readonly="true" ng-required="true"/>
			            <span class="input-group-addon" ng-click="producto.buscarUnidadEjecutora()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>
			        
			        <div class="form-group" ng-repeat="campo in producto.camposdinamicos">
						<label for="campo.id">{{ campo.label }}</label>
						<div ng-switch="campo.tipo">
							<input ng-switch-when="texto" type="text" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
							<input ng-switch-when="entero" type="number" id="{{ 'campo_'+campo.id }}" numbers-only ng-model="campo.valor" class="form-control" />
							<input ng-switch-when="decimal" type="number" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" class="form-control" />
							<input ng-switch-when="booleano" type="checkbox" id="{{ 'campo_'+campo.id }}" ng-model="campo.valor" />
							<p ng-switch-when="fecha" class="input-group">
								<input type="text" id="{{ 'campo_'+campo.id }}" class="form-control" uib-datepicker-popup="{{producto.formatofecha}}" ng-model="campo.valor" is-open="campo.isOpen"
										datepicker-options="producto.fechaOptions" close-text="Cerrar" />
								<span class="input-group-btn">
									<button type="button" class="btn btn-default" ng-click="producto.abrirPopupFecha($index)">
										<i class="glyphicon glyphicon-calendar"></i>
									</button>
								</span>
							</p>
						</div>
					</div>
					
				<div class="panel panel-default">
					<div class="panel-heading" style="text-align: center;">Datos de auditoría</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioCreo">Usuario que creo</label>
				  					<p class="form-control-static">{{ producto.producto.usuarioCreo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaCreacion">Fecha de creación</label>
				  					<p class="form-control-static">{{ producto.producto.fechaCreacion }}</p>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label for="usuarioActualizo">Usuario que actualizo</label>
				  					<p class="form-control-static">{{ producto.producto.usuarioactualizo }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion">Fecha de actualizacion</label>
				  					<p class="form-control-static">{{ producto.producto.fechaactualizacion }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
			</div>
		</div>
		<br />
		<div class="col-sm-12" align="center">Los campos marcados con * son obligatorios</div>
		<br />
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? producto.guardar() : '' " ng-disabled="!form.$valid">Guardar</label> 
				<label class="btn btn-primary" ng-click="producto.cancelar()">Ir a Tabla</label>
			</div>
		</div>
	</div>

</div>