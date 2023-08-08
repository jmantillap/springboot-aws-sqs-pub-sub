package co.javiermantilla.aws.sqs.service;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.util.MEntry;

public interface VariableQuequeService {
	MEntry getQuequeDemoInfo() throws TechnicalException;
}
