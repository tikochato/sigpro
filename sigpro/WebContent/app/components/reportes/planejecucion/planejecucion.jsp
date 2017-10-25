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
		
		<br>
		<div class="row" align="center" >
			<div class="col-sm-12 ">
			
			<form name="form">
				<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="planc.cambioPrestamo"
							  local-data="planc.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="planc.prestamoNombre" focus-out="planc.blurPrestamo()" input-name="prestamo"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
		    	<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="planc.cambioPep"
							  local-data="planc.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="planc.pepNombre" focus-out="planc.blurPep()" input-name="pep"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
				<div class="col-sm-12 operation_buttons" style="text-align: right;"  >
		    			<div class="btn-group" role="group" aria-label="" >
							<label class="btn btn-default" ng-click="planc.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!planc.mostrarExport">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							<label class="btn btn-default" ng-click="planc.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true" >
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
						</div>
		    		</div>
			</form>
			<br/> <br/><br/>
			
			<div class="row" style="height: 500px" ng-hide="!planc.mostrarCargando" >
				    	<div class="grid_loading"  style="margin-top: 50px;"   >
						  	<div class="msg">
						      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
								  <br /><br />
								  <b>Cargando, por favor espere...</b>
							  </span>
							</div>
						  </div>
             </div>
			
			
			
				<div class="panel panel-default" ng-hide="!planc.mostrar">
					
					<div class="panel-body">
					<div class = "table-responsive">
						<table class="table table-condensed borderless table-sm" >
							<tr>
	   							<td style="width: 50%; text-align: right;" >
	   								<label class="label-form1" >Mes Reportado</label>	
	   							</td>
	   							<td >
	   								<p class="datos"  >{{ planc.mesReportado }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td style="text-align: right;">
	   								<label class="label-form1" >Año Fiscal</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ planc.anioFiscal }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td style="text-align: right;">
	   								<label class="label-form1" >Proyecto/Programa</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ planc.prestamo.proyectoPrograma }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td style="text-align: right;">
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
				
				<table class="table table-hover table-condensed table-responsive table-striped borderless table-sm"
				ng-hide="!planc.mostrar">
						<tbody>
      						<tr>
      							<td style="width: 20%">
      								<label class="label-form1" >Número de {{etiquetas.proyecto}}</label>
      							</td>
      							<td style="width: 35%">
      								<p>{{ planc.prestamo.numeroPrestamo }}</pl>
      							</td>
      							<td style="width: 20%; ">
      								<label  class="label-form1"  >Fecha Ultima Actualización</label>
      							</td style="width: 20%">
      							<td style="text-align: left;">
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
      							<td style="text-align: left;">
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
      							<td style="text-align: left;">
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
      							<td style="text-align: left;">
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
      							<td style="text-align: left;">
				  					<p>{{ planc.prestamo.fechaElegibilidadUe }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Moneda de {{etiquetas.proyecto}}</label>
      							</td>
      							<td>
				  					<p>{{ planc.prestamo.tipoMonedaNombre }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha de cierre</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ planc.prestamo.fechaCierreActualUe }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto Aprobado</label>
      							</td>
      							<td style="text-align: left;">
				  					<p> $ {{ planc.prestamo.montoContratado | number:2 }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Meses de prorroga</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ planc.prestamo.mesesProrrogaUe }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto aprobado Q</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ planc.prestamo.montoContratadoQtz | formatoMillones : false}}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Plazo ejecución</label>
      							</td>
      							<td style="text-align: left;">
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
