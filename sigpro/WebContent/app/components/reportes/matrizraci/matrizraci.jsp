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
		  transform: translate(23px, 0px) rotate(270deg) ;
		  width: 7px;
		  
		}
		
		th.rotate > div > span {
	  		padding: 5px 5px;
		}
		
		
		.classRolR {
			text-align: center;
			background: #efd9c4;
			font-weight: bold;
		}
		
		.classRolA {
			text-align: center;
			background: #daefc4;
			font-weight: bold;
		}
		.classRolC {
			text-align: center;
			background: #c4daef;
			font-weight: bold;
		}
		.classRolI {
			text-align: center;
			background: #d9c4ef;
			font-weight: bold;
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
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Fecha Inicio</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.informacion.fechaInicio }}
					</div>
				</div>
				<div class="row">
					<div class="col-sm-6" style="text-align: right">
						<label for="nombre">Fecha Fin</label>
					</div>
					<div class="col-sm-6">
						{{ infoc.informacion.fechaFin }}
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
		<div class="subtitulo">
			{{ racic.objetoTipoNombre }} {{ racic.proyectoNombre }}
		</div>
		
		<div class="row" align="center" >
		
			
			<div class="col-sm-12 ">
				<form name="form">
					<div class="form-group col-sm-3" >
							<select  class="inputText" ng-model="racic.prestamoSeleccionado" 
								ng-options="a.text for a in racic.prestamos"
								ng-readonly="true"
								ng-required="true"
								ng-change = "racic.generarMatriz()">
								<option value="">Seleccione una opción</option>
								</select>
							<label for="prestamo" class="floating-label">Préstamos</label>
					</div>
				</form>
				<br/>
				<br/>
				<table st-table="racic.matrizRaci" class="table table-header-rotated  table-striped table-hover table-condensed" >
						<thead class="theadDatos">
						<tr >
							<th  class="{{racic.claseHeader($index)}}" ng-repeat="n in racic.encabezadoMatriz track by $index" style="text-align: center">
									<div ><span>{{n.nombre}} </span></div>
							</th>
						</tr>
						</thead>
						<tbody>
						<tr  ng-repeat="row in racic.matrizRaci track by $index " >
							<td ng-repeat = "col in row track by $index" class="{{racic.claseBody(col)}}" ng-click="$index > 0 ? racic.mostrarColaborador(col) : ''" >
							 	 <span >{{col.rol}} </span>
							 </td>
						</tr>
						</tbody>
				</table>	
				
				<br/>
				
				
			</div>
		  
	</div>
</div>
