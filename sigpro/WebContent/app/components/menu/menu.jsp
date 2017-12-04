<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="org.apache.shiro.SecurityUtils" %>
	<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
	<nav ng-class="{'showbar' : !hidebar, 'hidebar': hidebar}" class="navbar navbar-inverse navbar-fixed-top">
		<script type="text/javascript">
		</script>
		<div class="container">
	    
	        <input type="checkbox" id="navbar-toggle-cbox" />	       
	        <div class="navbar-header">
				<script type="text/javascript">
				</script>
	            <label for="navbar-toggle-cbox" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	                <span class="icon-bar"></span>
	                <span class="icon-bar"></span>
	                <span class="icon-bar"></span>
	            </label>
	            <a class="navbar-brand" href="/main.jsp"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> </a>
		    </div>		    
		    <div class="collapse navbar-collapse" id="navBar">
	            <ul class="nav navbar-nav">
	                <li>
                    	<shiro:hasPermission name="37010"><a href="/main.jsp#!/prestamo"><span class="glyphicon" aria-hidden="true"></span>Préstamos</a></shiro:hasPermission>                       
                    </li>
                    <shiro:hasPermission name="44010">
	                    <li uib-dropdown>
	                    	<a href="#" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Configuraciones <b class="caret"></b></a>
	                    	<ul class="dropdown-menu multi-level" role="menu" aria-labelledby="split-button">
	                    		<li class="dropdown-submenu">
	                    			<a><span class="glyphicon" aria-hidden="true"></span> Catálogos</a>
			                        <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
			                         	<shiro:hasPermission name="3010"><li role="menuitem"><a href="/main.jsp#!/actividadtipo">Tipo de Actividad</a></li></shiro:hasPermission>                         	
			                         	<shiro:hasPermission name="7010"><li role="menuitem"><a href="/main.jsp#!/componentetipo">Tipo de Componente</a></li></shiro:hasPermission>
			                         	<shiro:hasPermission name="7010"><li role="menuitem"><a href="/main.jsp#!/subcomponentetipo">Tipo de Subcomponente</a></li></shiro:hasPermission>
			                         	<shiro:hasPermission name="16010"><li role="menuitem"><a href="/main.jsp#!/hitotipo">Tipo de Hito</a></li></shiro:hasPermission>                         	
			                         	<shiro:hasPermission name="18010"><li role="menuitem"><a href="/main.jsp#!/metatipo">Tipo de Meta</a></li></shiro:hasPermission>                         	
				                        <shiro:hasPermission name="23010"><li role="menuitem"><a href="/main.jsp#!/productotipo">Tipo de Producto</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="36010"><li role="menuitem"><a href="/main.jsp#!/peptipo">Tipo de {{etiquetas.proyecto}}</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="36010"><li role="menuitem"><a href="/main.jsp#!/prestamotipo">Tipo de Préstamo</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="32010"><li role="menuitem"><a href="/main.jsp#!/riesgotipo">Tipo de Riesgo</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="42010"><li role="menuitem"><a href="/main.jsp#!/subproductotipo">Tipo de Subproducto</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="19010"><li role="menuitem"><a href="/main.jsp#!/unidadmedida">Unidad de Medida</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="29010"><li role="menuitem"><a href="/main.jsp#!/categoriaadquisicion">Categoría de Adquisiciones</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="29010"><li role="menuitem"><a href="/main.jsp#!/tipoadquisicion">Tipo de Adquisiciones</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="46010"><li role="menuitem"><a href="/main.jsp#!/rolunidadejecutora">Roles de Unidad Ejecutora</a></li></shiro:hasPermission>
			                        </ul>
	                    		</li>
	                    		<li class="dropdown-submenu">
	                    			<a><span class="glyphicon" aria-hidden="true"></span> Propiedades</a>
			                        <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
			                         	<shiro:hasPermission name="38010"><li role="menuitem"><a href="/main.jsp#!/prestamopropiedad">Préstamo</a></li></shiro:hasPermission>
			                         	<shiro:hasPermission name="2010"><li role="menuitem"><a href="/main.jsp#!/actividadpropiedad">Actividad</a></li></shiro:hasPermission>	                      
				                        <shiro:hasPermission name="6010"> <li role="menuitem"><a href="/main.jsp#!/componentepropiedad">Componente</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="6010"> <li role="menuitem"><a href="/main.jsp#!/subcomponentepropiedad">Subcomponente</a></li></shiro:hasPermission>	                       
				                        <shiro:hasPermission name="22010"> <li role="menuitem"><a href="/main.jsp#!/productopropiedad">Producto</a></li></shiro:hasPermission>
				                        <shiro:hasPermission name="41010"> <li role="menuitem"><a href="/main.jsp#!/subproductopropiedad">Subproducto</a></li></shiro:hasPermission>	                       
				                        <shiro:hasPermission name="25010"><li role="menuitem"><a href="/main.jsp#!/peppropiedad">{{etiquetas.proyecto}}</a></li></shiro:hasPermission>	                        				                        	                        
				                        <shiro:hasPermission name="31010"> <li role="menuitem"><a href="/main.jsp#!/riesgopropiedad">Riesgo</a></li></shiro:hasPermission>	                       
				                     </ul>
	                    		</li>
	                    	</ul>
	                    </li>
                    </shiro:hasPermission>
                    
                    <li uib-dropdown>
                    	<a href="#" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Reportes <b class="caret"></b></a>
                    	<ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/avanceactividades">Avance de Actividades e Hitos</a></li></shiro:hasPermission>                    		
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/prestamoindicadores">Avance de Indicadores</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/prestamometas">Avance de Metas</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/cargatrabajo">Carga de Trabajo</a></li></shiro:hasPermission>                    		
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/administracionTransaccional/">Detalle de Transacciones</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/desembolsos">Desembolsos</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/informacionPresupuestaria">Ejecución presupuestaria</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/flujocaja">Flujo de Caja</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/gestionadquisiciones">Gestión de adquisiciones</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/informeGeneralPEP">Informe General del PEP</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/planejecucion">Informe General del Préstamo</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/mapa">Mapa</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/matrizraci">Matriz RACI</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/reportefinancieroadquisiciones">Reporte Financiero de Adquisiciones</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/planestructuralproyecto">Plan estructural de préstamo</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="/main.jsp#!/planadquisiciones">Plan de adquisiciones</a></li></shiro:hasPermission>
                    	</ul>
                    </li>
                    <shiro:hasPermission name="4010">
                    <li uib-dropdown>
                         <a href="" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Usuarios <b class="caret"></b></a>
                         <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
                         	<shiro:hasPermission name="34010"><li role="menuitem"><a href="/main.jsp#!/usuarios">Usuarios</a></li></shiro:hasPermission>  
                         	                        	  
                         	<shiro:hasPermission name="4010"><li role="menuitem"><a href="/main.jsp#!/colaborador">Colaboradores</a></li></shiro:hasPermission>                       	
                         </ul>
                    </li>
                    </shiro:hasPermission>
                   
	            </ul>
	            <ul class="nav navbar-nav navbar-right">
	            	<shiro:authenticated><li><a href="/main.jsp#!/usuarioinfo"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>Mi info.</a></li></shiro:authenticated>
	            	<shiro:notAuthenticated><li><a href="/login.jsp"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>Entrar</a></li></shiro:notAuthenticated>
		            <shiro:authenticated><li><a href="/SLogout"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Salir</a></li></shiro:authenticated>
		            <li><a><span class="glyphicon glyphicon-chevron-up" aria-hidden="true" ng-click="hideBarFromMenu()"></span> </a></li>
		        </ul>
	        </div>
	        <div ng-hide ="!hidebar" class='triangle-down alineado' ng-click="showBarFromMenu()">
		    	<p>
		        	<i class='fa fa-chevron-down fa-12x isDown' id='toggle'></i>
		    	</p>
			</div>
		</div>
	</nav>
