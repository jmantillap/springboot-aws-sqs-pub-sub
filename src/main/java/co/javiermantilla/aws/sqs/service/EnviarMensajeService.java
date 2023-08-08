package co.javiermantilla.aws.sqs.service;

import co.javiermantilla.aws.sqs.dto.InputMessageRequestDTO;
import co.javiermantilla.aws.sqs.dto.ResponseDTO;

public interface EnviarMensajeService {
	ResponseDTO enviarMensaje(InputMessageRequestDTO data);
}
