package co.javiermantilla.aws.sqs.queque.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.queque.AwsListener;
import co.javiermantilla.aws.sqs.service.RecibirMensajeService;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

@Component
public class QueueListenerImpl implements AwsListener {
	
	private static final Logger LOGGER = LogManager.getLogger(QueueListenerImpl.class);

	
	@Autowired
	private RecibirMensajeService recibirMensajeService;
	
	@Override
	public void onMessage(Message message) {
		String mensaje=null;
		String mensajeId=null;
		try {			
			mensaje = ((TextMessage) message).getText();			
			mensajeId= message.getJMSMessageID();
			LOGGER.info("INICIO de proceso Recibe mensaje cola onMessage: {}; id-mensaje: {}", mensaje,mensajeId);
			recibirMensajeService.process(mensaje);
			LOGGER.info("FIN proceso mensaje {}; id-mensaje: {}", mensaje,mensajeId);
		} catch (Exception   e) {
			LOGGER.error("Ocurrió un error al recibir el mensaje {}; id-mensaje: {}, Error: {}", mensaje,mensajeId, e);
			throw new TechnicalException(e.getMessage(),e);
		}
		LOGGER.info("FIN RECIBIR DESDE LA COLA-----------------------------------------------------------------------------");
		
	}

}
