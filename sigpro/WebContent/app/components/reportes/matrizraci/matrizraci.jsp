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
		
		.label-form {
		    font-size: 13px;
		    opacity: 1;
		    color: rgba(0,0,0,0.38) !important;
		    font-weight: bold;
		}
	
		th.rotate > div {
		  transform: translate(29px, 0px) rotate(270deg) ;
		  width: 7px;
		  
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
		position: absolute;
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
	    margin-top: 151px;
	}


		
	</style>
	<%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<div ng-controller="matrizraciController as racic" class="maincontainer all_page" id="title">
	
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
            	<button class="btn btn-primary" type="button" ng-click="infoc.ok()">Aceptar</button>
        	</div>
    </script>
	    
  	    <shiro:lacksPermission name="30010">
			<p ng-init="riesgoc.redireccionSinPermisos()"></p>
		</shiro:lacksPermission>
		
		<div class="panel panel-default">
			<div class="panel-heading"><h3>Matriz RACI</h3></div>
		</div>
		
		
		<div class="row" align="center" style="height: 70%" >
		
			
			<div class="col-sm-12 " >
				<form name="form">
					<div class="form-group col-sm-4" >
							<select  class="inputText" ng-model="racic.prestamoSeleccionado" 
								ng-options="a.text for a in racic.prestamos"
								ng-readonly="true"
								ng-required="true"
								ng-change = "racic.generarMatriz()">
								<option value="">Seleccione un préstamo</option>
								</select>
		
					</div>
				</form>
				<br/>
				<br/>
				</div>
			<div class="col-sm-12 " style="height: 100%">
				<div class="divTabla" ng-if="racic.mostrarTabla">
	  			
					<table st-table="racic.matrizRaci" class="table table-header-rotated  table-striped table-hover table-condensed" >
							<thead class="cabecera">
							<tr >
								<th class="thIcono"></th>
								<th  class="{{racic.claseHeader($index)}}"   ng-repeat="n in racic.encabezadoMatriz track by $index"
								 >
										<div><span>{{n.nombre}} </span></div>
								</th>
							</tr>
							</thead>
							<tbody>
							<tr  ng-repeat="row in racic.matrizRaci track by $index " >
								<td ng-repeat = "col in row track by $index" class="{{racic.claseBody(col)}}" 
								ng-click="$index > 0 ? racic.mostrarColaborador(col) : ''" 
								style="border-right: 2px solid #ddd;">
								 	 <div ng-if="$index == 0" class="{{ $index == 0 ? racic.claseIcon(row) : ''}}" style="margin-left: {{col.objetoTipo-1}}em"></div>
								 	 <span>{{col.rol}}</span>
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
