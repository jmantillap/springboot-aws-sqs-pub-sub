package co.javiermantilla.aws.sqs.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.service.VariableQuequeService;
import co.javiermantilla.aws.sqs.util.EnviromentUtil;
import co.javiermantilla.aws.sqs.util.MEntry;

@Service
public class VariableQuequeServiceImpl implements VariableQuequeService {

	private static final Logger LOGGER = LogManager.getLogger(VariableQuequeServiceImpl.class);

	@Autowired
	private Environment environment;
	
	private MEntry connectQueueDemo= null;

	// variables de la cola de postpago pupx
	private String envAwsColaDemo;
	private String awsAccesskeyDemo;
	private String awsSecretkeyDemo;
	
	@Value("${url.cola}")
	private String urlColaDemo;
	@Value("${region.cola}")
	private String awsRegionDemo;

	@Override
	public MEntry getQuequeDemoInfo() throws TechnicalException {

		if (connectQueueDemo != null) {
			return connectQueueDemo;
		}
		
		try {
			this.getEnviromentsOptionalDemo();
			connectQueueDemo = new MEntry();
			connectQueueDemo.setEnviromentAWS(this.envAwsColaDemo);
			if (connectQueueDemo.getEnviromentAWS() != null && !connectQueueDemo.getEnviromentAWS().isBlank()
					&& !connectQueueDemo.getEnviromentAWS().isEmpty() && connectQueueDemo.getEnviromentAWS().equals("0")) {
				connectQueueDemo.setSecretKey(this.awsSecretkeyDemo);
				connectQueueDemo.setAccessKey(this.awsAccesskeyDemo);
			}
			connectQueueDemo.setRegion(this.awsRegionDemo);
			connectQueueDemo.setUrl(this.urlColaDemo);
		} catch (Exception e) {
			LOGGER.error("loadParameters - Ocurrió un error: {} {}", e.getMessage(), e);
			throw new TechnicalException(e.getMessage());
		}
		
		return connectQueueDemo;
	}
	/**
	 * Es necesario este metodo para que no exista error en ambiente de AWS ya que estas variables no van a existir para dicho ambiente, Estas variables son necesaria en ambiente de local de codigo
	 * fuente.
	 */
	private void getEnviromentsOptionalDemo() {
		this.envAwsColaDemo = EnviromentUtil.getVariable(environment, "env.aws.cola");
		this.awsSecretkeyDemo= EnviromentUtil.getVariable(environment, "secretkey.cola");
		this.awsAccesskeyDemo= EnviromentUtil.getVariable(environment, "accesskey.cola");
	}


}
