<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="planejecucionController as planc" class="maincontainer all_page" id="title">
  	    <shiro:lacksPermission name="30010">
			<p ng-init="planc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Plan de Ejecución</h3></div>
		</div>
		<div class="subtitulo">
			{{ planc.objetoTipoNombre }} {{ planc.proyectoNombre }}
		</div>
		
		<div class="row" align="center" >
			<div class="col-sm-12 ">
			<div class="row">
				<div class="form-group col-sm-5">
					<select  class="inputText" ng-model="planc.prestamo"
						ng-options="a.text for a in planc.prestamos"></select>
					<label for="prestamo" class="floating-label">Préstamos</label>
				</div>
				
				
				<div class="col-sm-5">
					<div class="form-group" >
					  <input type="text"  class="inputText" uib-datepicker-popup="{{planc.formatofecha}}" ng-model="planc.ejercicioFiscal" is-open="planc.ef_abierto"
					            datepicker-options="planc.fechaOptions" close-text="Cerrar" current-text="Hoy" clear-text="Borrar"  ng-required="true"  ng-click="planc.abrirPopupFecha(1)"
					             date-disabled="disabled(date, mode)"
					            ng-value="planc.ejercicioFiscal" onblur="this.setAttribute('value', this.value);"/>
					            <span class="label-icon" ng-click="planc.abrirPopupFecha(1)">
					              <i class="glyphicon glyphicon-calendar"></i>
					            </span>
					  <label  class="floating-label">Ejercicio fiscal</label>
					</div>
				</div>
				
				<div class="form-group col-sm-1" >
					<label class="btn btn-default" ng-click="planc.generarReporte()" uib-tooltip="Generar" 
						tooltip-placement="bottom">
					<span class="glyphicon glyphicon-new-window"></span></label>
				</div>
				
				
				
			</div>
				
			
			
			
				<div class="panel panel-default" ng-hide="!planc.mostrar">
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label class="label-form" for="usuarioCreo">Número de Prestamo</label>
				  					<p>{{ planc.prestamo.numeroPrestamo }}</pl>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label  class="label-form"  for="fechaCreacion">Fecha Ultima Actualización</label>
				  					<p>{{ planc.prestamo.fechaActualizacion }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Código Presupuestario</label>
				  					<p>{{ planc.prestamo.codigoPresupuestario }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha del decreto</label>
				  					<p>{{ planc.prestamo.fechadecreto }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Organismo Financiero</label>
				  					<p>{{ planc.prestamo.cooperantenombre }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha del suscripción</label>
				  					<p>{{ planc.prestamo.fechaSuscripcion }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Entidad Ejecutora</label>
				  					<p>{{ planc.prestamo.nombreEntidadEjecutora }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha de vigencia</label>
				  					<p>{{ planc.prestamo.fechaVigencia }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Unidad Ejecutora</label>
				  					<p>{{ planc.prestamo.unidadEjecutoraNombre }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha de elegibilidad</label>
				  					<p>{{ planc.prestamo.fechaElegibilidadUe }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Moneda de prestamo</label>
				  					<p>{{ planc.prestamo.tipoMonedaNombre }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Fecha de cierre</label>
				  					<p>{{ planc.prestamo.fechaCierre }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Monto Aprobado</label>
				  					<p>{{ planc.prestamo.montoContratado }}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Meses de prorroga</label>
				  					<p>{{ planc.prestamo.mesesProrrogaUe }}</p>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-sm-6">
								<div class="form-group" style="text-align: right">
									<label  class="label-form" for="usuarioActualizo">Monto aprobado Q</label>
				  					<p>{{ planc.prestamo.montoContratadoQtz}}</p>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="form-group">
									<label for="fechaActualizacion"  class="label-form" >Plazo ejecución</label>
				  					<p>{{ planc.prestamo.plazoEjecucionUe }}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<canvas id="radar" class="chart chart-radar" ng-hide="!planc.mostrar"
				  chart-data="planc.dataRadar" chart-options="planc.options" chart-labels="planc.etiquetas"
				  chart-colors = "planc.radarColors">
				</canvas> 
					
			</div>
		  
	</div>
</div>
