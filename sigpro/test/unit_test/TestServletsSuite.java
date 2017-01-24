package unit_test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import servlets.*;


@RunWith(Suite.class)
@SuiteClasses({ 
	SProyectoTipoTest.class,
	SComponenteTipoTest.class,
	SRiesgoTipoTest.class,
	SHitoTipoTest.class,
	SDesembolsoTipoTest.class
	})
public class TestServletsSuite {

}
