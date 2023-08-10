package co.javiermantilla.aws.sqs.service;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.queque.impl.AwsConfigQuequeSubImpl;
import co.javiermantilla.aws.sqs.service.impl.SQSServicePubImpl;
import co.javiermantilla.aws.sqs.util.MEntry;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SQSServicePubTest {
	
	
	@Autowired
	private SQSServicePubImpl sqsServicePubImpl; 
	
	@MockBean
	private AwsConfigQuequeSubImpl awsConfigQuequeImpl;	
	
	@Mock
	private SqsClient mockSqsClient;
	
	private String mensaje;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		this.mensaje="{hola mundo}";
	}

	@Test
	void testEnviarmensajeColaSQS() {
		MockitoAnnotations.openMocks(this);
		assertNotNull(mockSqsClient);
		Mockito.when(mockSqsClient.sendMessage(Mockito.any(SendMessageRequest.class)))
				.thenReturn(SendMessageResponse.builder().messageId("testMessageId-1").build());		
		MEntry entry= new MEntry("url","us-east-1");		
		this.sqsServicePubImpl.setSqsClient(mockSqsClient);		
		boolean isOK = this.sqsServicePubImpl.enviarmensajeColaSQS(this.mensaje,entry);		
		org.junit.jupiter.api.Assertions.assertEquals(true, isOK);
		Mockito.verify(mockSqsClient).sendMessage(Mockito.any(SendMessageRequest.class));
		
		
	}
	
	@Test
	void testEnviarmensajeColaSQSEnvLocal() throws Exception {
		MEntry entry= new MEntry("url","us-east-1");		
		this.sqsServicePubImpl.setSqsClient(null);
		TechnicalException exception = assertThrows(TechnicalException.class, () -> {			
			this.sqsServicePubImpl.enviarmensajeColaSQS(this.mensaje,entry );
		});		
		String expectedMessage = "Problemas Al escribir el mensaje";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));	
		
	}
	
	@Test
	void testEnviarmensajeColaSQSEnvLocalError() throws Exception {
		MEntry entry= new MEntry("url","us-east-1");
		entry.setEnviromentAWS("0");
		this.sqsServicePubImpl.setSqsClient(null);
		TechnicalException exception = assertThrows(TechnicalException.class, () -> {			
			this.sqsServicePubImpl.enviarmensajeColaSQS(this.mensaje,entry );
		});		
		String expectedMessage = "Problemas Al escribir el mensaje";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));	
		
	}

}
