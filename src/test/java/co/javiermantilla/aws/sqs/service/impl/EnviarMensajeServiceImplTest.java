package co.javiermantilla.aws.sqs.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import co.javiermantilla.aws.sqs.dto.InputMessageRequestDTO;
import co.javiermantilla.aws.sqs.dto.ResponseDTO;
import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.queque.impl.AwsConfigQuequeSubImpl;
import co.javiermantilla.aws.sqs.service.EnviarMensajeService;
import co.javiermantilla.aws.sqs.service.SQSServicePub;
import co.javiermantilla.aws.sqs.service.VariableQuequeService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EnviarMensajeServiceImplTest {

	@Autowired
	private EnviarMensajeService enviarMensajeService;
	
	@MockBean
	private AwsConfigQuequeSubImpl awsConfigQuequeImpl;	
	@MockBean
	private SQSServicePub sqsServicePush;
	@MockBean
	private VariableQuequeService variableQuequeService;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		
	}

	@Test
	void testEnviarMensaje() {
		ResponseDTO respuesta= this.enviarMensajeService.enviarMensaje(new InputMessageRequestDTO("hola mundo"));
		assertTrue(!respuesta.getMensaje().isEmpty());
	}
	
	@Test
	void testEnviarMensajeError() {
		Mockito.when(variableQuequeService.getQuequeDemoInfo()).thenThrow(new TechnicalException("Error occurred"));		
		Mockito.when(sqsServicePush.enviarmensajeColaSQS("hola mundo enviar mensaje",null)).thenThrow(new TechnicalException("Error occurred"));
		
		RuntimeException exception = assertThrows(TechnicalException.class, () -> {
			this.enviarMensajeService.enviarMensaje(new InputMessageRequestDTO("hola mundo"));
		});	
		assertTrue(!exception.getMessage().isEmpty());	
	}

}

