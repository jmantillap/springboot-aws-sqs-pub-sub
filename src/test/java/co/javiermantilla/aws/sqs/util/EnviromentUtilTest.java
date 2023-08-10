package co.javiermantilla.aws.sqs.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EnviromentUtilTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void test() {
		assertNull(EnviromentUtil.getVariable(null,"prueba.prueba" ));		
	}

}
