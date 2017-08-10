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
	
	.datos{
		font-weight: bold;
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
				<div class="form-group col-sm-4" >
						<select  class="inputText" ng-model="planc.prestamoSeleccionado" 
							ng-options="a.text for a in planc.prestamos"
							ng-readonly="true"
							ng-required="true"
							ng-change = "planc.generarReporte()">
							<option value="">Seleccione una opción</option>
							</select>
						<label for="prestamo" class="floating-label">Préstamos</label>
				</div>
			</form>
			<br/> <br/><br/>
			
				<div class="panel panel-default" ng-hide="!planc.mostrar">
					
					<div class="panel-body">
					<div class = "table-responsive">
						<table class="table table-condensed borderless table-sm" >
							<tr>
	   							<td style="width: 35%" >
	   								<label class="label-form1" >Mes Reportado</label>	
	   							</td>
	   							<td >
	   								<p class="datos"  >{{ planc.mesReportado }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td>
	   								<label class="label-form1" >Año Fiscal</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ planc.anioFiscal }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td>
	   								<label class="label-form1" >Proyecto/Programa</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ planc.prestamo.proyectoPrograma }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td>
	   								<label class="label-form1" >Organismo Ejecutor</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ planc.prestamo.nombreEntidadEjecutora }}</pl>
	   							</td>
	   						</tr>
	   						
						</table>
					</div>
					</div>
				</div>
				
				<table class="table table-hover table-condensed table-responsive table-striped table-bordered table-sm"
				ng-hide="!planc.mostrar">
						<tbody>
      						<tr>
      							<td style="width: 20%">
      								<label class="label-form1" >Número de Prestamo</label>
      							</td>
      							<td style="width: 40%">
      								<p>{{ planc.prestamo.numeroPrestamo }}</pl>
      							</td>
      							<td style="width: 20%; ">
      								<label  class="label-form1"  >Fecha Ultima Actualización</label>
      							</td style="width: 20%">
      							<td style="text-align: right;">
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
      							<td style="text-align: right;">
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
      							<td style="text-align: right;">
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
      							<td style="text-align: right;">
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
      							<td style="text-align: right;">
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
      							<td style="text-align: right;">
				  					<p>{{ planc.prestamo.fechaCierreActualUe }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto Aprobado</label>
      							</td>
      							<td style="text-align: right;">
				  					<p> $ {{ planc.prestamo.montoContratado | number:2 }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Meses de prorroga</label>
      							</td>
      							<td style="text-align: right;">
				  					<p>{{ planc.prestamo.mesesProrrogaUe }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto aprobado Q</label>
      							</td>
      							<td style="text-align: right;">
				  					<p>{{ planc.prestamo.montoContratadoQtz | formatoMillones : false}}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Plazo ejecución</label>
      							</td>
      							<td style="text-align: right;">
				  					<p>{{ planc.prestamo.plazoEjecucionUe }}</p>
      							</td>
      						</tr>
      					</tbody>
					</table>
				
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
