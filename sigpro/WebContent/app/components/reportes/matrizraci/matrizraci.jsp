<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<style type="text/css">
		th.rotate {
		  height: 140px;
		  white-space: nowrap;
		  overflow-y: hidden;
		  text-overflow: ellipsis; 
		  
		  font-size: 13px;
		    opacity: 1;
		    color: rgba(0,0,0,0.38) !important;
		    font-weight: bold; 
		   white-space: nowrap;
		}
		
		th.no_rotate {
		  height: 50px;
		  white-space: nowrap;
		  overflow-y: hidden;
		  text-overflow: ellipsis; 
		  
		  font-size: 13px;
		  opacity: 1;
		  color: rgba(0,0,0,0.38) !important;
		  font-weight: bold;
		  text-align: center;
		   white-space: nowrap;
		}
		
		.label-form {
		    font-size: 13px;
		    opacity: 1;
		    color: rgba(0,0,0,0.38) !important;
		    font-weight: bold;
		}
	
		th.rotate > div {
		  transform: translate(29px, 0px) rotate(270deg) ;
		  width: 7px;
		  margin-left:-8px;
		  
		}
		
		th.rotate > div > span {
	  		padding: 5px 5px;
		}
		
		
		.classRolR {
			text-align: center;
			background: #efd9c4;
			font-weight: bold;
			border-right: 1px solid #ddd;
			width: 55px;
		}
		
		.classRolA {
			text-align: center;
			background: #daefc4;
			font-weight: bold;
			border-right: 2px solid #ddd;
			width: 55px;
		}
		.classRolC {
			text-align: center;
			background: #c4daef;
			font-weight: bold;
			border-right: 2px solid #ddd;
			width: 55px;
		}
		.classRolI {
			text-align: center;
			background: #d9c4ef;
			font-weight: bold;
			border-right: 2px solid #ddd;
			width: 55px;
		}
		
		.leyendaTexto {
		    text-align: right;
		}
		
		.leyendaTexto li {
		    display: inline-block;
		    position: relative;
		    padding: 1px 8px 1px 15px;
		    font-size: smaller;
		}
		
		.leyendaTexto li span {
		    position: absolute;
		    left: 0;
		    width: 12px;
		    height: 12px;
		    border-radius: 4px;
		}
		
		
		.colorResponsableFondo{
			background-color: #efd9c4;
		}
		
		.colorAprobadorFondo{
			background-color: #daefc4;
		}
		.colorConsultadoFondo{
			background-color: #c4daef;
		}
		.colorInformadoFondo{
			background-color: #d9c4ef;
		}
		
		.nombreFormat {    
		    margin: 0 0 3px;
		    color: #333;
		    word-break: none;
		    word-wrap: break-word;
		    background-color: transparent;
		    border: none;
		    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
		    padding: 0px;
		    line-height: normal;		     
		}
		
		
		
		
	.cabecera {
		/*position: absolute;*/
	    margin-top: -141px;
	    flex-shrink: 0;
	    overflow-x: hidden;
	    width: 96%;
	}
	
	.cabecerath1{
		margin: 0px auto;
    	width: 96px;
    	overflow-x: hidden;
	}

	.thTarea {
		margin: 0px auto;
    	width: 1600px;	
	}
	
	.thIcono{
		margin: 0 auto;
    	width: 83px;
	}
	
	.divTabla{
	    width: 100%;
	    height: 100%;
	    overflow-y: auto;
	    margin-top: 40px;
	    overflow-x: hidden;
	}
	
	.contenedor{
		height: calc(100% - 120px);
	}
	
	


		
	</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="matrizraciController as racic" class="maincontainer all_page" id="title" >
	
		<script type="text/ng-template" id="modalInfo.html">
        	<div class="modal-header">
            	<h3 class="modal-title" id="modal-title"> Información </h3>
        	</div>
        	<div class="modal-body" id="modal-body">
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="id">Nombre tarea</label>
					</div>
					<div class="col-sm-6">
						 {{infoc.informacion.nombreTarea }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Rol</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.informacion.rol }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Nombre</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.informacion.nombreColaborador }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Estado</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.informacion.estadoColaborador }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Correo electrónico</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.informacion.email }}
					</div>
				</div>
				

				</div>

        	</div>
        	<div class="modal-footer">
            	<button class="btn btn-primary" type="button" ng-click="infoc.ok()">Cerrar</button>
        	</div>
    </script>
	    
  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Matriz RACI</h3></div>
		</div>
		
	
		<div align="center"  class="contenedor">
				<form name="form">
					<div class="row">
			    		<div class="form-group col-sm-6" align="left">
							<div id="prestamo" angucomplete-alt placeholder="" pause="100" selected-object="racic.cambioPrestamo"
								  local-data="racic.lprestamos" search-fields="proyectoPrograma" title-field="proyectoPrograma" field-required="true" field-label="* Préstamo"
								  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
								  initial-value="racic.prestamoNombre" focus-out="racic.blurPrestamo()" input-name="prestamo"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
			    	</div>
			    	<div class="row">
			    		<div class="form-group col-sm-6" align="left">
							<div id="pep" angucomplete-alt placeholder="" pause="100" selected-object="racic.cambioPep"
								  local-data="racic.peps" search-fields="nombre" title-field="nombre" field-required="true" field-label="* {{etiquetas.proyecto}}"
								  minlength="1" input-class="form-control form-control-small field-angucomplete inputText" match-class="angucomplete-highlight"
								  initial-value="racic.pepNombre" focus-out="racic.blurPep()" input-name="pep" disable-input="racic.prestamoId==null"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
			    	</div>
			    	<div class="row">
			    		<div class="form-group col-sm-6" align="left">
							<div id= "lineaBase" angucomplete-alt placeholder="" pause="100" selected-object="racic.cambioLineaBase"
								  local-data="racic.lineasBase" search-fields="nombre" title-field="nombre" 
								  field-required="true" field-label="* Linea Base" minlength="1" input-class="form-control form-control-small field-angucomplete inputText" 
								  match-class="angucomplete-highlight" initial-value="racic.lineaBaseNombre" 
								  focus-out="racic.blurLineaBase()" input-name="lineaBase"></div>
							<span class="label-icon" tabindex="-1"><i class="glyphicon glyphicon-search"></i></span>
						</div>
			    	</div>
					<div class="operation_buttons"  style="float: right;">
		    			<div class="btn-group" role="group" aria-label="">
							<label class="btn btn-default" ng-click="racic.exportarExcel()" uib-tooltip="Exportar a Excel" ng-hide="!racic.mostrarExport">
							<span class="glyphicon glyphicon glyphicon-export" aria-hidden="true"></span></label>
							<label class="btn btn-default" ng-click="racic.exportarPdf()" uib-tooltip="Exportar a PDF" ng-hide="true">
								<span class="glyphicon glyphicon glyphicon-save-file" aria-hidden="true"></span></label>
							
						</div>
		    		</div>
				</form>
				<div class="col-sm-12 " style="height: 94%">
				
				<div class="grid_loading"  ng-if="racic.mostrarcargando" >
				  	<div class="msg">
				      <span><i class="fa fa-spinner fa-spin fa-4x"></i>
						  <br /><br />
						  <b>Cargando, por favor espere...</b>
					  </span>
					</div>
				  </div>
				
				<div class="divTabla" ng-hide="!racic.mostrarExport">
	  			
					<table class="table table-header-rotated  table-striped table-hover table-condensed" >
							<thead class="cabecera">
							<tr>
								
								<th  class="{{racic.claseHeader($index)}}"   ng-repeat="n in racic.encabezadoMatriz track by $index">
										<div><span>{{ n.nombre}} </span></div>
								</th>
							</tr>
							</thead>
							<tbody vs-repeat>
							<tr  ng-repeat="row in racic.matrizRaci track by $index">
								<td ng-repeat = "col in row track by $index" class="{{racic.claseBody(col)}}" 
								ng-click="$index > 0 ? racic.mostrarColaborador(col) : ''" 
								style="border-right: 2px solid #ddd;">
								 	 <div ng-if="$index == 0" class="{{ $index == 0 ? racic.claseIcon(row) : ''}}" style="margin-left: {{col.nivel-1}}em"></div>
								 	 <span>{{ col.rol}}</span>
								 </td>
							</tr>
							</tbody>
					</table>
				</div>
				<div style="text-align: center; padding: 0 10 0 0 px;" ng-if="racic.mostrarTabla">
	    		<br>	
	    		<ol class="leyendaTexto"  >
					<li ><span class="colorResponsableFondo"></span>Responsable</li>
			        <li ><span class="colorAprobadorFondo"></span>Cuentadante</li>
			        <li ><span class="colorConsultadoFondo"></span>Consultor</li>
			        <li ><span class="colorInformadoFondo"></span>Quien informa</li>
				</ol>
	    	
					
	    	</div>

			</div>
			
			
	    	<br>
	    	</div>
		  
	
</div>
