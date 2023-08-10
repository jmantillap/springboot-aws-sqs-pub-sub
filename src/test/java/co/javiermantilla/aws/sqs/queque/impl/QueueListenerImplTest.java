package co.javiermantilla.aws.sqs.queque.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.service.RecibirMensajeService;

import static org.mockito.BDDMockito.given;

import jakarta.jms.Session;
import jakarta.jms.TextMessage;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class QueueListenerImplTest {

	
	private String mensajeRecibidoCola;
	
	@Autowired
	private QueueListenerImpl queueListenerImpl;
	
	@MockBean
	private RecibirMensajeService recibirMensajeService;
	@Mock
	private Session mockSession;
	@MockBean
	private AwsConfigQuequeSubImpl awsConfigQuequeImpl;

	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		this.mensajeRecibidoCola="Hola Mundo desde test";
	}

	@Test
	void testOnMessage() throws Exception {
		TextMessage message = mock();
		
		given(mockSession.createTextMessage(mensajeRecibidoCola)).willReturn(message);
		given(((TextMessage) message).getText()).willReturn(mensajeRecibidoCola);
		
		TextMessage text = mockSession.createTextMessage(mensajeRecibidoCola);
		this.queueListenerImpl.onMessage(text);
		assertTrue(true);  ;
	}
	
	@Test
	void testOnMessageError() throws Exception {
		
		TextMessage text = null;		
		TechnicalException exception = assertThrows(TechnicalException.class, () -> {
			this.queueListenerImpl.onMessage(text);
		});
		String expectedMessage = "because \"message\" is null";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}	
	

}
