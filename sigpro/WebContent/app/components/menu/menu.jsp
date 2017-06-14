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
	            <a class="navbar-brand" href="/main.jsp"><span class="glyphicon glyphicon-home" aria-hidden="true"></span> Inicio</a>
		    </div>		    
		    <div class="collapse navbar-collapse" id="navBar">
	            <ul class="nav navbar-nav">
	                <li uib-dropdown>
	                	<shiro:hasPermission name="8010"><a href="#!/cooperante"><span class="glyphicon" aria-hidden="true"></span> Cooperante</a></shiro:hasPermission>	                    
	                </li>
                    <li>
                    	<shiro:hasPermission name="24010"><a href="#!/programa"><span class="glyphicon" aria-hidden="true"></span> Programas</a></shiro:hasPermission>                       
                    </li>
                    <li>
                    	<shiro:hasPermission name="24010"><a   href="#!/prestamo"><span class="glyphicon" aria-hidden="true"></span> Préstamos</a></shiro:hasPermission>                       
                    </li>
                    <li uib-dropdown>
                    	<a href="#" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Configuraciones <b class="caret"></b></a>
                    	<ul class="dropdown-menu multi-level" role="menu" aria-labelledby="split-button">
                    		<shiro:hasPermission name="3010">
                    		<li class="dropdown-submenu">
                    			<a><span class="glyphicon" aria-hidden="true"></span> Catálogos</a>
		                        <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
		                         <shiro:hasPermission name="36010"><li role="menuitem"><a href="#!/programatipo">Tipo de Programa</a></li></shiro:hasPermission>
		                         	<shiro:hasPermission name="3010"><li role="menuitem"><a href="#!/actividadtipo">Tipo de Actividad</a></li></shiro:hasPermission>                         	
		                         	<shiro:hasPermission name="7010"><li role="menuitem"><a href="#!/componentetipo">Tipo de Componente</a></li></shiro:hasPermission>   
		                         	<shiro:hasPermission name="35010"><li role="menuitem"><a href="#!/desembolsotipo">Tipo de Desembolso</a></li></shiro:hasPermission>
		                         	<shiro:hasPermission name="16010"><li role="menuitem"><a href="#!/hitotipo">Tipo de Hito</a></li></shiro:hasPermission>                         	
		                         	<shiro:hasPermission name="18010"><li role="menuitem"><a href="#!/metatipo">Tipo de Meta</a></li></shiro:hasPermission>                         	
			                        <shiro:hasPermission name="23010"><li role="menuitem"><a href="#!/productotipo">Tipo de Producto</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="36010"><li role="menuitem"><a href="#!/prestamotipo">Tipo de Prestamo</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="28010"><li role="menuitem"><a href="#!/recursotipo">Tipo de Recurso</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="32010"><li role="menuitem"><a href="#!/riesgotipo">Tipo de Riesgo</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="23010"><li role="menuitem"><a href="#!/subproductotipo">Tipo de Subproducto</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="19010"><li role="menuitem"><a href="#!/metaunidadmedida">Unidad de Medida para Metas</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="29010"><li role="menuitem"><a href="#!/recursounidadmedida">Unidad de Medida para Recurso</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="29010"><li role="menuitem"><a href="#!/responsabletipo/rv">Tipo de Responsable</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="29010"><li role="menuitem"><a href="#!/responsablerol/rv">Resonsable rol</a></li></shiro:hasPermission>
		                        </ul>
                    		</li>
                    		</shiro:hasPermission>
                    		
                    		<li class="dropdown-submenu">
                    			<a><span class="glyphicon" aria-hidden="true"></span> Propiedades</a>
		                        <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
		                         	<shiro:hasPermission name="25010"><li role="menuitem"><a href="#!/programapropiedad">Programa</a></li></shiro:hasPermission>
		                         	<shiro:hasPermission name="2010"><li role="menuitem"><a href="#!/actividadpropiedad">Actividad</a></li></shiro:hasPermission>	                      
			                        <shiro:hasPermission name="6010"> <li role="menuitem"><a href="#!/componentepropiedad">Componente</a></li></shiro:hasPermission>	                       
			                        <shiro:hasPermission name="22010"> <li role="menuitem"><a href="#!/productopropiedad">Producto</a></li></shiro:hasPermission>
			                        <shiro:hasPermission name="22010"> <li role="menuitem"><a href="#!/subproductopropiedad">Subproducto</a></li></shiro:hasPermission>	                       
			                        <shiro:hasPermission name="25010"><li role="menuitem"><a href="#!/prestamopropiedad">Préstamo</a></li></shiro:hasPermission>	                        
			                        <shiro:hasPermission name="27010"><li role="menuitem"><a href="#!/recursopropiedad">Recurso</a></li></shiro:hasPermission>	                        
			                        <shiro:hasPermission name="31010"> <li role="menuitem"><a href="#!/riesgopropiedad">Riesgo</a></li></shiro:hasPermission>	                       
			                     </ul>
                    		</li>
                    	</ul>
                    </li>
                    <li uib-dropdown>
                    	<a href="#" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Reportes <b class="caret"></b></a>
                    	<ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="#!/cargatrabajo/rp">Carga de Trabajo</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="#!/informacionPresupuestaria/rp">Información presupuestaria</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="#!/planejecucion/rp">Plan de ejecución</a></li></shiro:hasPermission>
                    		<shiro:hasPermission name="24010"><li role="menuitem"><a href="#!/prestamometas">Metas de Préstamo</a></li></shiro:hasPermission>
                    	</ul>
                    </li>
                    <shiro:hasPermission name="34010">
                    <li uib-dropdown>
                         <a href="" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Usuarios <b class="caret"></b></a>
                         <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
                         	<shiro:hasPermission name="34010"><li role="menuitem"><a href="#!/usuarios">Usuarios</a></li></shiro:hasPermission>                         	
                         	<shiro:hasPermission name="20010"><li role="menuitem"><a href="#!/permisos">Permisos</a></li></shiro:hasPermission>                         	  
                         	<shiro:hasPermission name="4010"><li role="menuitem"><a href="#!/colaborador">Colaboradores</a></li></shiro:hasPermission>                       	
                         </ul>
                    </li>
                    </shiro:hasPermission>
                   
                    <li uib-dropdown>
	                	<shiro:hasPermission name="24010"><a   href="#!/mapa"><span class="glyphicon" aria-hidden="true"></span> Mapa</a></shiro:hasPermission>	                    
	                </li>
	            </ul>
	            <ul class="nav navbar-nav navbar-right">
	            	<shiro:authenticated><li><a href="#!/usuarioinfo"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>Mi info.</a></li></shiro:authenticated>
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