<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style>
	.label-form1{
		 font-size: 13px;
		 opacity: 1;
		 pointer-events: none;
		 color: rgba(0,0,0,0.38) !important;
		 font-weight: bold;
	}
	 table.borderless td,table.borderless th{
     border: none !important;
	}
</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="planejecucionController as planc" class="maincontainer all_page" id="title">
	
	
  	    <shiro:lacksPermission name="30010">
			<p ng-init="planc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Plan de Ejecución</h3></div>
		</div>
		
		
		<div class="row" align="center" >
			<div class="col-sm-12 ">
			
			<form name="form">
				<div class="row">
					<div class="form-group col-sm-12">
						<select  class="inputText" ng-model="planc.prestamoSeleccionado" 
							ng-options="a.text for a in planc.prestamos"
							ng-readonly="true"
							ng-required="true"
							ng-change = "planc.generarReporte()">
							<option value="">Seleccione una opción</option>
							</select>
						<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
					
					
					<div class="col-sm-5" ng-if="false">
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
				</div>
			</form>
				
			
				<div class="panel panel-default" ng-hide="!planc.mostrar">
					<div class="panel-body">
					<div class = "table-responsive">
						<table class="table table-condensed borderless"
						 style="width: 50%" align="left">
							<tr>
	   							<td style="width: 40%" >
	   								<label class="label-form1" >Mes Reportado</label>	
	   							</td>
	   							<td >
	   								<p>{{ planc.mesReportado }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td>
	   								<label class="label-form1" >Año Fiscal</label>
	   							</td>
	   							<td>
	   								<p>{{ planc.anioFiscal }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td>
	   								<label class="label-form1" >Proyecto/Programa</label>
	   							</td>
	   							<td>
	   								<p>{{ planc.prestamo.proyectoPrograma }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td>
	   								<label class="label-form1" >Organismo Ejecutor</label>
	   							</td>
	   							<td>
	   								<p>{{ planc.prestamo.nombreEntidadEjecutora }}</pl>
	   							</td>
	   						</tr>
	   						
						</table>
					</div>
						
					
					<table class="table table-hover table-condensed table-responsive table-striped table-bordered table-sm">
						<tbody>
      						<tr>
      							<td>
      								<label class="label-form1" >Número de Prestamo</label>
      							</td>
      							<td>
      								<p>{{ planc.prestamo.numeroPrestamo }}</pl>
      							</td>
      							<td>
      								<label  class="label-form1"  >Fecha Ultima Actualización</label>
      							</td>
      							<td>
      								<p>{{ planc.prestamo.fechaActualizacion }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Código Presupuestario</label>
				  					
      							</td>
      							<td>
      								<p>{{ planc.prestamo.codigoPresupuestario }}</p>
      							</td>
      							<td>
      								<label  class="label-form1" >Fecha del decreto</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.fechaDecreto }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Organismo Financiero</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.cooperantenombre }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha del suscripción</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.fechaSuscripcion }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Entidad Ejecutora</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.nombreEntidadEjecutora }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha de vigencia</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.fechaVigencia }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Unidad Ejecutora</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.unidadEjecutoraNombre }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha de elegibilidad</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.fechaElegibilidadUe }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Moneda de prestamo</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.tipoMonedaNombre }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha de cierre</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.fechaCierreActualUe }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto Aprobado</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.montoContratado | number:2 }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Meses de prorroga</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.mesesProrrogaUe }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto aprobado Q</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.montoContratadoQtz |  number:2}}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Plazo ejecución</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.plazoEjecucionUe }}</p>
      							</td>
      						</tr>
      					</tbody>
					</table>
					</div>
				</div>
				
				<br/> 
				<div style="width: 75%">
					<canvas id="radar" class="chart chart-radar" ng-hide="!planc.mostrar"
				  chart-data="planc.dataRadar" chart-options="planc.radarOptions" chart-labels="planc.etiquetas"
				  chart-legend="false" chart-series="planc.series"
				  chart-colors = "planc.radarColors">
				</canvas>
				</div>
			</div>
		  
	</div>
</div>
