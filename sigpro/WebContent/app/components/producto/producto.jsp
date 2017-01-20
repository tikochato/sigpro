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

	<div ng-show="producto.esForma">

		<div class="col-sm-12 operation_buttons" align="right">

			<div class="btn-group">
				<label class="btn btn-success" ng-click="form.$valid ? producto.guardar() : ''" ng-disabled="!form.$valid">Guardar</label> 
				<label class="btn btn-primary" ng-click="producto.cancelar()">Ir a Tabla</label>
			</div>

		</div>

		<div>
			<form name="form" class="css-form" novalidate>

				<div class="row">
					<div class="form-group col-sm-3" ng-show="!producto.esNuevo">
						<label for="campo0">ID:</label> <input type="text" class="form-control" id="campo0" name="campo0" placeholder="ID" ng-model="producto.codigo" ng-readonly="true" />
					</div>
				</div>

				<div class="row">
					<div class="form-group col-sm-12"
						ng-class="{ 'has-error' : form.campo1.$invalid }">
						<label for="campo1">* Nombre:</label> 
						<input type="text" class="form-control" id="campo1" name="campo1" placeholder="Nombre del producto" ng-model="producto.nombre" required />
					</div>

					<div class="form-group col-sm-12"
						ng-class="{ 'has-error' : form.campo2.$invalid }">
						<label for="campo2">* Descripción:</label> 
						<input type="text" class="form-control" id="campo2" name="campo2" placeholder="Descripcion del producto" ng-model="producto.descripcion" required />
					</div>
					
					<div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo3.$invalid }">
			          <label for="campo3">* Tipo:</label>
			          <div class="input-group">
			            <input type="hidden" class="form-control" ng-model="producto.tipo" /> 
			            <input type="text" class="form-control" id="campo3" name="campo3" placeholder="Tipo de producto" ng-model="producto.tipoNombre" ng-readonly="true" required/>
			            <span class="input-group-addon" ng-click="producto.buscarTipo()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>

					<div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo4.$invalid }">
			          <label for="campo4">* Componente:</label>
			          <div class="input-group">
			            <input type="hidden" class="form-control" ng-model="producto.componente" /> 
			            <input type="text" class="form-control" id="campo4" name="campo4" placeholder="Componente al que pertenece" ng-model="producto.componenteNombre" ng-readonly="true" required/>
			            <span class="input-group-addon" ng-click="producto.buscarComponente()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>

					<div class="form-group col-sm-12" ng-class="{ 'has-error' : form.campo5.$invalid }">
			          <label for="campo5">Producto:</label>
			          <div class="input-group">
			            <input type="hidden" class="form-control" ng-model="producto.productoPadre" /> 
			            <input type="text" class="form-control" id="campo5" name="campo5" placeholder="Producto padre" ng-model="producto.productoPadreNombre" ng-readonly="true"/>
			            <span class="input-group-addon" ng-click="producto.buscarProducto()"><i class="glyphicon glyphicon-search"></i></span>
			          </div>
			        </div>
			        
			        <div class="form-group col-sm-12" ng-repeat="campo in producto.camposdinamicos">
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
				</div>
			</form>
		</div>

		<br />

		<h4>Propiedades</h4>
		<div>
			<div class="col-sm-12" align="center">
				<table st-table="producto.propiedadesValor" class="table table-striped" style="width: 400px;" ng-hide="producto.mostrarCargando">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Tipo</th>
							<th style="width: 5%"></th>
						</tr>
					</thead>
					<tbody>
						<tr st-select-row="row" st-select-mode="single" ng-repeat="propiedad in producto.propiedadesValor | filter: { estado: '!E'} track by $index">
							<td>{{propiedad.propiedad}}</td>
							<td>{{propiedad.propiedadTipo}}</td>
							<td align="center"><span ng-click="producto.editarPropiedadValor($index)" uib-tooltip="Editar Propiedad"><i style="color: red; font-size: 20px;" class="glyphicon glyphicon-pencil"></i></span></td>
						</tr>
					</tbody>
				</table>
				<div class="grid_loading" ng-hide="!producto.mostrarCargando">
					<div class="msg">
						<span><i class="fa fa-spinner fa-spin fa-4x"></i> <br />
							<br /> <b>Cargando, por favor espere...</b> </span>
					</div>
				</div>
			</div>
		</div>

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