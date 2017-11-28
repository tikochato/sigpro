<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal-header">
    <h3 class="modal-title">Generar Plan Anual de Ejecución</h3>
</div>
<div class="modal-body" id="modal-body">
	<div class="row second-main-form">
		<div class="col-sm-12">
			<div class="row form-group" >
				<div class="col-sm-6">
				  <input type="text"  class="inputText" uib-datepicker-popup="{{modalrc.formatofecha}}" alt-input-formats="{{modalrc.altformatofecha}}"
				  			ng-model="modalrc.fechaCorte" is-open="modalrc.fi_abierto"
				            datepicker-options="modalrc.fi_opciones" close-text="Cerrar" current-text="Hoy" clear-text="Borrar" ng-change="" ng-required="true"  
				            ng-value="modalrc.fechaCorte" onblur="this.setAttribute('value', this.value);" />
				            <span class="label-icon" ng-click="modalrc.abrirPopupFecha(1000)" tabindex="-1">
				              <i class="glyphicon glyphicon-calendar"></i>
				            </span>
				  <label class="floating-label">* Fecha de Corte</label>
				</div>
			</div>
		
			<div class="form-group">
			   <textarea class="inputText" rows="4"
			   ng-model="modalrc.observaciones" ng-value="modalrc.observaciones"   
			   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
			   <label class="floating-label">Observaciones / Desafíos</label>
			</div>
			
			<div class="form-group">
			   <textarea class="inputText" rows="4"
			   ng-model="modalrc.alertivos" ng-value="modalrc.alertivos"   
			   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
			   <label class="floating-label">Alertivos</label>
			</div>
			
			
			<div class="form-group">
			   <textarea class="inputText" rows="4"
			   ng-model="modalrc.elaborado" ng-value="modalrc.elaborado"   
			   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
			   <label class="floating-label">Elaborado por:</label>
			</div>
			
			<div class="form-group">
			   <textarea class="inputText" rows="4"
			   ng-model="modalrc.aprobado" ng-value="modalrc.aprobado"   
			   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
			   <label class="floating-label">Aprobado por:</label>
			</div>
						
			<div class="form-group">
			   <textarea class="inputText" rows="4"
			   ng-model="modalrc.autoridad" ng-value="modalrc.autoridad"   
			   onblur="this.setAttribute('value', this.value);" ng-required="false" ></textarea>
			   <label class="floating-label">Máxima Autoridad:</label>
			</div>
			
		</div>
	</div>
	<br />
	<div class="row">
		<div class="col-sm-12 operation_buttons" align="right">
			<div class="btn-group">
				<label class="btn btn-success" ng-click="modalrc.ok()">
					&nbsp;&nbsp;&nbsp;&nbsp;Ok&nbsp;&nbsp;&nbsp;&nbsp;</label> <label
					class="btn btn-primary" ng-click="modalrc.cancel()">Cancelar</label>
			</div>

		</div>
	</div>
</div>