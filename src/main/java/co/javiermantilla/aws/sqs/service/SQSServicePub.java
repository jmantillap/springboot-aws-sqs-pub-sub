package co.javiermantilla.aws.sqs.service;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.util.MEntry;

public interface SQSServicePub {
	/**
	 * Metodo con la logica de negocio de enviar el mensaje a la cola sqs de aws
	 * @param mensaje mensaje json
	 * @return boolean	 
	 * @throws TechnicalException error tecnico
	 * @author javier.mantilla	
	 * @since Jun/2023. Sprint 2 HU # 232694
	 */
	public boolean enviarmensajeColaSQS(String mensaje,MEntry queque) throws TechnicalException;

}
