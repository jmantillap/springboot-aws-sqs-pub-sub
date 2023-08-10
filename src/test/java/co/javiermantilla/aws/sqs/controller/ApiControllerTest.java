package co.javiermantilla.aws.sqs.controller;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.queque.AwsConfigQuequeSub;

import co.javiermantilla.aws.sqs.service.SQSServicePub;
import co.javiermantilla.aws.sqs.service.VariableQuequeService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@TestInstance(Lifecycle.PER_CLASS)
@WebMvcTest
@ActiveProfiles("test")
@ComponentScan(basePackages = {
        "co.javiermantilla.aws.sqs"        
})
class ApiControllerTest {
	
	private static final Logger LOGGER = LogManager.getLogger(ApiControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;	
		
	@MockBean
	private AwsConfigQuequeSub awsConfigQueque;

	@MockBean
	private VariableQuequeService variableQuequeService;
	
	@MockBean
	private SQSServicePub sqsServicePush;
	
	//@MockBean
	//private EnviarMensajeService enviarMensajeService;
		
	private String mensaje;

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		this.mensaje= "{ \"mensaje\":\"Hola mundo para enviar a la cola 3\"}";
	}
	
	@Test
	void test() throws Exception {
		LOGGER.info("Prueba con error al realizar la enviar mensaje");		
		mockMvc.perform(post("/v1/send-message").contentType(MediaType.APPLICATION_JSON)
				.content(mensaje)).andExpect(status().isOk()).andDo(print());
	}
		
	@Test
	void testEnviarError() throws Exception {		
		//EnviarMensajeService enviarMensajeService = mock(EnviarMensajeService.class) ;
		
		Mockito.when(variableQuequeService.getQuequeDemoInfo()).thenThrow(new TechnicalException("Error occurred"));		
		Mockito.when(sqsServicePush.enviarmensajeColaSQS(this.mensaje,null)).thenThrow(new TechnicalException("Error occurred"));		
		mockMvc.perform(post("/v1/send-message").contentType(MediaType.APPLICATION_JSON)
				.content(this.mensaje)).andExpect(status().isInternalServerError())
				.andDo(print());
	}

}
