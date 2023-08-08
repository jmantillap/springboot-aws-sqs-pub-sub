package co.javiermantilla.aws.sqs.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.service.SQSServicePub;
import co.javiermantilla.aws.sqs.util.MEntry;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
public class SQSServicePubImpl implements SQSServicePub {

	private static final Logger LOGGER = LogManager.getLogger(SQSServicePubImpl.class);
	
	private SqsClient sqsClient;
	
	@Override
	public boolean enviarmensajeColaSQS(String mensaje, MEntry queque) throws TechnicalException {
		try {
			LOGGER.info("Inicio de enviarmensajeColaSQS, mensaje : {}, cola : {}  ", mensaje, queque.getUrl());
			this.writeMessage(mensaje, queque);
			LOGGER.info("Fin de enviarmensajeColaSQS, mensaje : {}, cola: {}", mensaje, queque.getUrl());
		} catch (Exception e) {
			LOGGER.error("Ocurrió un error con el mensaje {}; err: {}", mensaje, e.getMessage());
			throw new TechnicalException(e.getMessage(), e);
		}
		return true;
	}
	
	/**
	 * Metodo para escribir el mensaje en la cola
	 * 
	 * @param message mensaje
	 * @return identificador del mensaje
	 * @throws TechnicalException error
	 * @author javier.mantilla
	 * @since Jun/2023. 
	 */
	private String writeMessage(String message, MEntry queque) throws TechnicalException {
		String messageId;
		try {
			if (this.sqsClient == null && queque.getEnviromentAWS() != null && !queque.getEnviromentAWS().isBlank()
					&& !queque.getEnviromentAWS().isEmpty() && queque.getEnviromentAWS().equals("0")) {
				LOGGER.info("Se ejecuta en ambiente local de desarrollo.");
				AwsBasicCredentials credentials = AwsBasicCredentials.create(queque.getAccessKey(), queque.getSecretKey());
				this.sqsClient = SqsClient.builder().region(Region.of(queque.getRegion()))
						.credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
			} else if (this.sqsClient == null) {
				LOGGER.info("Se ejecuta en ambiente AWS.");
				this.sqsClient = SqsClient.builder().region(Region.of(queque.getRegion())).build();
			}
			SendMessageRequest sendMessageRequest = SendMessageRequest.builder().queueUrl(queque.getUrl()).messageBody(message).build();
			SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
			messageId = sendMessageResponse.messageId();
			LOGGER.info("Mensaje: {}, enviado a la cola : {}, Identificador del mensaje: {}  ", message, queque.getUrl(), messageId);
		} catch (Exception e) {
			LOGGER.error("Error al enviar mensaje: {}, a la cola: {}, detalle:{}", message, queque.getUrl(), e);
			throw new TechnicalException("Problemas Al escribir el mensaje : " + e.getMessage(), e);
		}
		return messageId;

	}

	public void setSqsClient(SqsClient sqsClient) {
		this.sqsClient = sqsClient;
	}

}
