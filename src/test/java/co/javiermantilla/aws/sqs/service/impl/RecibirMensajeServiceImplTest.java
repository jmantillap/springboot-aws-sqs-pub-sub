package co.javiermantilla.aws.sqs.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import co.javiermantilla.aws.sqs.service.RecibirMensajeService;


@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecibirMensajeServiceImplTest {

	
	private RecibirMensajeService recibirMensajeService;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		this.recibirMensajeService= new RecibirMensajeServiceImpl();
	}

	@Test
	void testProcess() {
		this.recibirMensajeService.process("Hola mundo prueba");
		assertTrue(true);
	}
	
	

}
