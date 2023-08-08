package co.javiermantilla.aws.sqs.service;

public interface RecibirMensajeService {
	void process(String message);
}
