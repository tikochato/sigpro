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
	<div ng-controller="informeGeneralPEPController as informec" class="maincontainer all_page" id="title">
	
	
  	    <shiro:lacksPermission name="30010">
			<p ng-init="informec.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Informe General del PEP</h3></div>
		</div>
		
		<br>
		<div class="row" align="center" >
			<div class="col-sm-12 ">
			
			<form name="form">
				
		    	
				<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="informec.cambioPrestamo"
							  local-data="informec.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="informec.prestamoNombre" focus-out="informec.blurPrestamo()" input-name="prestamo"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
		    	<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="informec.cambioPep"
							  local-data="informec.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
							  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
							  initial-value="informec.pepNombre" focus-out="informec.blurPep()" input-name="pep" disable-input="informec.prestamoId==null"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
		    	<div class="row">
		    		<div class="form-group col-sm-6" align="left">
						<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="informec.cambioLineaBase"
							  local-data="informec.lineasBase" search-fields="nombre" title-field="nombre" 
							  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
							  match-class="angucomplete-highlight" initial-value="informec.lineaBaseNombre" 
							  focus-out="informec.blurLineaBase()" input-name="lineaBase"></div>
						<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
					</div>
		    	</div>
		    	
		    	<div class="col-sm-12 operation_buttons" style="text-align: right;"  >
		    			<div class="btn-group" role="group" aria-label="" >
							<label class="btn btn-default" ng-click="informec.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!informec.mostrarExport">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							<label class="btn btn-default" ng-click="informec.exportarJasper()" uib-tooltip="Exportar a PDF" ng-hide="!informec.mostrarExport">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
						</div>
		    		</div>
			</form>
			<br/> <br/><br/>
			
			<div class="row" style="height: 500px" ng-hide="!informec.mostrarCargando" >
				    	<div class="grid_loading"  style="margin-top: 50px;"   >
						  	<div class="msg">
						      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
								  <br /><br />
								  <b>Cargando, por favor espere...</b>
							  </span>
							</div>
						  </div>
             </div>
			
			
			
				<div class="panel panel-default" ng-hide="!informec.mostrar">
					
					<div class="panel-body">
					<div class = "table-responsive">
						<table class="table table-condensed borderless table-sm" >
							<tr>
	   							<td style="width: 50%; text-align: right;" >
	   								<label class="label-form1" >Mes Reportado</label>	
	   							</td>
	   							<td >
	   								<p class="datos"  >{{ informec.mesReportado }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td style="text-align: right;">
	   								<label class="label-form1" >Año Fiscal</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ informec.anioFiscal }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td style="text-align: right;">
	   								<label class="label-form1" >Proyecto/Programa</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ informec.prestamo.proyectoPrograma }}</pl>
	   							</td>
	   						</tr>
	   						<tr>
	   							<td style="text-align: right;">
	   								<label class="label-form1" >Organismo Ejecutor</label>
	   							</td>
	   							<td>
	   								<p class="datos">{{ informec.prestamo.nombreEntidadEjecutora }}</pl>
	   							</td>
	   						</tr>
	   						
						</table>
					</div>
					</div>
				</div>
				
				<table class="table table-hover table-condensed table-responsive table-striped borderless table-sm"
				ng-hide="!informec.mostrar">
						<tbody>
      						<tr>
      							<td style="width: 20%">
      								<label class="label-form1" >Número de {{etiquetas.proyecto}}</label>
      							</td>
      							<td style="width: 35%">
      								<p>{{ informec.prestamo.numeroPrestamo }}</pl>
      							</td>
      							<td style="width: 20%; ">
      								<label  class="label-form1"  >Fecha Ultima Actualización</label>
      							</td style="width: 20%">
      							<td style="text-align: left;">
      								<p>{{ informec.prestamo.fechaActualizacion }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Código Presupuestario</label>
				  					
      							</td>
      							<td>
      								<p>{{ informec.prestamo.codigoPresupuestario }}</p>
      							</td>
      							<td>
      								<label  class="label-form1" >Fecha del decreto</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ informec.prestamo.fechaDecreto }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Organismo Financiero</label>
      							</td>
      							<td>
				  					<p>{{ informec.prestamo.cooperantenombre }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha del suscripción</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ informec.prestamo.fechaSuscripcion }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Moneda de {{etiquetas.proyecto}}</label>
      							</td>
      							<td>
				  					<p>{{ informec.prestamo.tipoMonedaNombre }}</p>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha de vigencia</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ informec.prestamo.fechaVigencia }}</p>
      							</td>
      						</tr>
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto Contratado</label>
      							</td>
      							<td style="text-align: left;">
				  					<p> $ {{ informec.prestamo.montoContratadoEntidadUsd | number:2 }}</p>
      							</td>
      							
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Fecha de elegibilidad</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ informec.prestamo.fechaElegibilidadUe }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Desembolsos realizados a la fecha</label>
      							</td>
      							<td style="text-align: left;">
				  					<span> {{ informec.prestamo.desembolsadoAFecha | number:2 }}</span> &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;
				  					<span> {{ (informec.prestamo.desembolsadoAFecha / informec.prestamo.montoContratadoEntidadUsd) * 100 | number:2}}% </span>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Meses de Prórroga</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ informec.prestamo.mesesProrrogaUe }}</p>
      							</td>
      						</tr>
      						
      						<tr>
      							<td>
      								<label  class="label-form1" >Monto por desembolsar</label>
      							</td>
      							<td style="text-align: left;">
				  					<span> {{ informec.prestamo.montoContratadoEntidadUsd -  informec.prestamo.desembolsadoAFecha | number:2}}</span>
				  					&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;
				  					<span> {{ ((informec.prestamo.montoContratadoEntidadUsd -  informec.prestamo.desembolsadoAFecha) / informec.prestamo.montoContratadoEntidadUsd) * 100 | number:2}}% </span>
      							</td>
      							<td>
      								<label for="fechaActualizacion"  class="label-form1" >Plazo ejecución</label>
      							</td>
      							<td style="text-align: left;">
				  					<p>{{ informec.prestamo.plazoEjecucionPEP | number : 2  }} %</p>
      							</td>
      						</tr>
      						
      						
      					</tbody>
					</table>
				
				<br/> 
				<div style="width: 75%">
					<canvas id="radar" class="chart chart-radar" ng-hide="!informec.mostrar"
				  chart-data="informec.dataRadar" chart-options="informec.radarOptions" chart-labels="informec.etiquetas"
				  chart-legend="false" chart-series="informec.series"
				  chart-colors = "informec.radarColors">
				</canvas>
				</div>
			</div>
		  
	</div>
</div>
