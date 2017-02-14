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
	                    <a href="#!/cooperante"><span class="glyphicon" aria-hidden="true"></span> Cooperante</a>
	                </li>
                    <li>
                         <a   href="#!/proyecto"><span class="glyphicon" aria-hidden="true"></span> Proyectos</a>
                    </li>
                    <li uib-dropdown>
                         <a href="#" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Catálogos <b class="caret"></b></a>
                         <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
                         	<li role="menuitem"><a href="#!/cooperante">Cooperantes</a></li>
                         	<li role="menuitem"><a href="#!/actividadtipo">Tipo de Actividad</a></li>
                         	<li role="menuitem"><a href="#!/componentetipo">Tipo de Componente</a></li>
                         	<li role="menuitem"><a href="#!/hitotipo">Tipo Hito</a></li>
                         	<li role="menuitem"><a href="#!/metatipo">Tipo de Meta</a></li>
	                        <li role="menuitem"><a href="#!/productoTipo">Tipo de Producto</a></li>
	                        <li role="menuitem"><a href="#!/proyectotipo">Tipo de Proyecto</a></li>
	                        <li role="menuitem"><a href="#!/recursotipo">Tipo de Recurso</a></li>
	                        <li role="menuitem"><a href="#!/riesgotipo">Tipo de Riesgo</a></li>
	                        <li role="menuitem"><a href="#!/metaunidadmedida">Unidad de Medida para Metas</a></li>
	                        <li role="menuitem"><a href="#!/recursounidadmedida">Unidad de Medida para Recurso</a></li>
                         </ul>
                    </li>
                    <li uib-dropdown>
                         <a href="#" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Propiedades <b class="caret"></b></a>
                         <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
	                        <li role="menuitem"><a href="#!/actividadpropiedad">Actividad</a></li>
	                        <li role="menuitem"><a href="#!/componentepropiedad">Componente</a></li>
	                        <li role="menuitem"><a href="#!/productopropiedad">Producto</a></li>
	                        <li role="menuitem"><a href="#!/proyectopropiedad">Proyecto</a></li>
	                        <li role="menuitem"><a href="#!/recursopropiedad">Recurso</a></li>
	                        <li role="menuitem"><a href="#!/riesgopropiedad">Riesgo</a></li>
	                     </ul>
                    </li>
                    <li uib-dropdown>
                         <a href="" uib-dropdown-toggle><span class="glyphicon" aria-hidden="true"></span> Usuarios <b class="caret"></b></a>
                         <ul uib-dropdown-menu role="menu" aria-labelledby="split-button">
                         	<li role="menuitem"><a href="#!/usuarios">Usuarios</a></li>
                         	<li role="menuitem"><a href="#!/permisos">Permisos</a></li>                         	
                         	<li role="menuitem"><a href="#!/colaborador">Colaboradores</a></li>
                         </ul>
                    </li>
	            </ul>
	            <ul class="nav navbar-nav navbar-right">
	            	<shiro:authenticated><li><a href="#!/usuarioinfo"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>Mi info.</a></li></shiro:authenticated>
	            	<shiro:notAuthenticated><li><a href="/login.jsp"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>Entrar</a></li></shiro:notAuthenticated>
		            <shiro:authenticated><li><a href="/SLogout"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Salir</a></li></shiro:authenticated> 
		            <li><a><span class="glyphicon glyphicon-chevron-up" aria-hidden="true" ng-click="hideBarFromMenu()"></span> </a></li>
		        </ul>
	        </div>
	    </div>
	</nav>