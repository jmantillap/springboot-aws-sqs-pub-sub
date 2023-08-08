package co.javiermantilla.aws.sqs.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import co.javiermantilla.aws.sqs.service.RecibirMensajeService;




@Service
public class RecibirMensajeServiceImpl implements RecibirMensajeService {

	private static final Logger LOGGER = LogManager.getLogger(RecibirMensajeServiceImpl.class);
	
	@Override
	public void process(String message) {
		LOGGER.info("Se Recibe mensaje cola para el servicio process: {}", message);
		LOGGER.info("Hacer con el mensaje .................");
	}

}
