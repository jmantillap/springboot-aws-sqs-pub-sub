package co.javiermantilla.aws.sqs.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import co.javiermantilla.aws.sqs.queque.impl.AwsConfigQuequeSubImpl;
import co.javiermantilla.aws.sqs.service.SQSServicePub;
import co.javiermantilla.aws.sqs.service.VariableQuequeService;
import co.javiermantilla.aws.sqs.util.MEntry;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class VariableQuequeServiceImplTest {

	@Autowired
	private VariableQuequeService variableQuequeService; 
	
	@MockBean
	private AwsConfigQuequeSubImpl awsConfigQuequeImpl;	
	@MockBean
	private SQSServicePub sqsServicePush;
	
	@BeforeAll
	void setUpBeforeClass() throws Exception {
		
	}

	@Test
	void testGetQuequeDemoInfo() {
		MEntry entry=variableQuequeService.getQuequeDemoInfo();
		assertEquals("0",entry.getEnviromentAWS());
		
	}
	
	@Test
	void testGetQuequeDemoInfoObject() {
		MEntry entry=variableQuequeService.getQuequeDemoInfo();
		assertEquals("0",entry.getEnviromentAWS());
		
	}

}
