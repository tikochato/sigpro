package unit_test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import servlets.*;


@RunWith(Suite.class)
@SuiteClasses({ 
	SColaboradorTest.class,         
	SComponentePropiedadTest.class, 
	SComponenteTest.class,          
	SComponenteTipoTest.class,      
	SCooperanteTest.class,          
	SDatoTipoTest.class,            
	SDesembolsoTest.class,          
	SDesembolsoTipoTest.class,      
	SEntidadTest.class,             
	SEstadoTablaTest.class,         
	SHitoTest.class,                
	SHitoTipoTest.class,            
	SLoginTest.class,               
	SMetaTest.class,                
	SMetaTipoTest.class,            
	SMetaUnidadMedidaTest.class,    
	SPermisoTest.class,             
	SProductoPropiedadTest.class,   
	SProductoTest.class,            
	SProductoTipoTest.class,        
	SProyectoPropiedadTest.class,   
	SProyectoTest.class,            
	SProyectoTipoTest.class,        
	SRecursoPropiedadTest.class,    
	SRecursoTipoTest.class,         
	SRecursoUnidadMedidaTest.class, 
	SRiesgoPropiedadTest.class,     
	SRiesgoTest.class,              
	SRiesgoTipoTest.class,          
	SUnidadEjecutoraTest.class,     
	SUsuarioTest.class  
	})
public class TestServletsSuite {

}
