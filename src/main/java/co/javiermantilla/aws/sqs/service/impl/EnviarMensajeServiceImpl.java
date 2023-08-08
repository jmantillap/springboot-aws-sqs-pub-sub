package co.javiermantilla.aws.sqs.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.javiermantilla.aws.sqs.dto.InputMessageRequestDTO;
import co.javiermantilla.aws.sqs.dto.ResponseDTO;
import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.service.EnviarMensajeService;
import co.javiermantilla.aws.sqs.service.SQSServicePub;
import co.javiermantilla.aws.sqs.service.VariableQuequeService;
import co.javiermantilla.aws.sqs.util.MEntry;

@Service
public class EnviarMensajeServiceImpl implements EnviarMensajeService {

	private static final Logger LOGGER = LogManager.getLogger(EnviarMensajeServiceImpl.class);

	@Autowired
	private SQSServicePub sqsServicePush;
	@Autowired
	private VariableQuequeService variableQuequeService;

	/* Cola de pospago valores */
	private MEntry connectQueueDemo = null;

	@Override
	public ResponseDTO enviarMensaje(InputMessageRequestDTO data) {
		try {
			this.verificacionInfoColas();
			this.sqsServicePush.enviarmensajeColaSQS(data.getMensaje(), connectQueueDemo);
		} catch (Exception e) {
			LOGGER.error("Ocurrió un error al enviar el mensaje {}; err: {}", data.getMensaje(), e.getMessage());
			throw new TechnicalException(e.getMessage(), e);
		}
		LOGGER.info("FIN ENVIO A LA COLA SQS-----------------------------------------------------------------------------");
		return new ResponseDTO("Mensaje Enviado Con éxito ");
	}

	private void verificacionInfoColas() {
		if (connectQueueDemo == null) {
			connectQueueDemo = this.variableQuequeService.getQuequeDemoInfo();
		}

	}

}
