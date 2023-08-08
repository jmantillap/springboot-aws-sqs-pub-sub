package co.javiermantilla.aws.sqs.exception;

public class TechnicalException extends RuntimeException {

	private static final long serialVersionUID = -6635960527844483637L;

	
	public TechnicalException(String message) {
		super(message);
	
	}
	
	public TechnicalException(String message, Throwable throwable) {
		super(message, throwable);	
	}

	

}
