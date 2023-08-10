package co.javiermantilla.aws.sqs.queque.impl;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.queque.AwsConfigQuequeSub;
import co.javiermantilla.aws.sqs.util.EnviromentUtil;
import co.javiermantilla.aws.sqs.util.MEntry;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
public class AwsConfigQuequeSubImpl implements AwsConfigQuequeSub {

	private static final Logger LOGGER = LogManager.getLogger(AwsConfigQuequeSubImpl.class);

	@Autowired
	private Environment environment;

	private MEntry connectQueue = null;
	private String envAwsCola;
	private String awsAccesskey;
	private String awsSecretkey;

	@Value("${url.cola}")
	private String urlCola;
	@Value("${region.cola}")
	private String awsRegion;
	
	@Autowired
	private ApplicationContext context;

	@Override
	public void suscriber() throws TechnicalException {
		if (connectQueue == null) {
			this.loadParameters();
		}
		SQSConnection connection = null;
		try {
			String[] valuesQueue = connectQueue.getUrl().split("/");
			SQSConnectionFactory connectionFactory = createExtendedSQSConnectionFactory(connectQueue);
			connection = connectionFactory.createConnection();
			AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
			if (!client.queueExists(valuesQueue[valuesQueue.length - 1])) {
				LOGGER.info("No Existe la Cola: {} o no tiene la politica del usuario--> sqs:GetQueueUrl",
						connectQueue.getUrl());
				client.getQueueUrl(valuesQueue[valuesQueue.length - 1]);
			}
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(valuesQueue[valuesQueue.length - 1]);
			MessageConsumer consumer = session.createConsumer(destination);
			QueueListenerImpl queueListener = context.getBean(QueueListenerImpl.class);
			consumer.setMessageListener(queueListener);
			connection.start();
		} catch (JMSException e) {
			LOGGER.error("suscriber - Ocurrió un error: {} {}", e.getMessage(), e);
			throw new TechnicalException(e.getMessage(), e);
		}

	}

	private void loadParameters() {
		try {
			this.getEnviromentsOptional();
			connectQueue = new MEntry();
			connectQueue.setEnviromentAWS(this.envAwsCola);
			if (connectQueue.getEnviromentAWS() != null && !connectQueue.getEnviromentAWS().isBlank()
					&& !connectQueue.getEnviromentAWS().isEmpty() && connectQueue.getEnviromentAWS().equals("0")) {
				connectQueue.setSecretKey(this.awsSecretkey);
				connectQueue.setAccessKey(this.awsAccesskey);
			}
			connectQueue.setRegion(this.awsRegion);
			connectQueue.setUrl(this.urlCola);
		} catch (Exception e) {
			LOGGER.error("loadParameters - Ocurrió un error: {} {}", e.getMessage(), e);
			throw new TechnicalException(e.getMessage());
		}
	}

	private static SQSConnectionFactory createExtendedSQSConnectionFactory(MEntry connectQueueGetPaid) {

		ProviderConfiguration providerConfiguration = new ProviderConfiguration();
		SqsClient sqsClient = null;
		if (connectQueueGetPaid.getEnviromentAWS() != null && !connectQueueGetPaid.getEnviromentAWS().isBlank()
				&& !connectQueueGetPaid.getEnviromentAWS().isEmpty()
				&& connectQueueGetPaid.getEnviromentAWS().equals("0")) {
			AwsBasicCredentials credentials = AwsBasicCredentials.create(connectQueueGetPaid.getAccessKey(),
					connectQueueGetPaid.getSecretKey());
			sqsClient = SqsClient.builder().region(Region.of(connectQueueGetPaid.getRegion()))
					.endpointOverride(URI.create(connectQueueGetPaid.getUrl()))
					.credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
		} else {
			LOGGER.info("Se ejecuta en ambiente AWS.");
			sqsClient = SqsClient.builder().region(Region.of(connectQueueGetPaid.getRegion()))
					.endpointOverride(URI.create(connectQueueGetPaid.getUrl())).build();
		}

		return new SQSConnectionFactory(providerConfiguration, new AmazonSQSExtendedClient(sqsClient));
	}

	/**
	 * Es necesario este metodo para que no exista error en ambiente de AWS ya que
	 * estas variables no van a existir para dicho ambiente, Estas variables son
	 * necesaria en ambiente de local de codigo fuente.
	 */
	private void getEnviromentsOptional() {
		this.envAwsCola = EnviromentUtil.getVariable(environment, "env.aws.cola");
		this.awsAccesskey = EnviromentUtil.getVariable(environment, "accesskey.cola");
		this.awsSecretkey = EnviromentUtil.getVariable(environment, "secretkey.cola");
	}

}
