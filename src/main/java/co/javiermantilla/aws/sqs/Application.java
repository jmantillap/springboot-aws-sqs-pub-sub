package co.javiermantilla.aws.sqs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import co.javiermantilla.aws.sqs.exception.TechnicalException;
import co.javiermantilla.aws.sqs.queque.AwsConfigQuequeSub;
import co.javiermantilla.aws.sqs.util.EnviromentUtil;
import jakarta.annotation.PostConstruct;


@SpringBootApplication
public class Application {

	private static final Logger LOGGER = LogManager.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	ApplicationRunner applicationRunner(Environment environment) {		
		
		return args -> {			
						LOGGER.info("\n\r20230808.Variables properties:" 
					+ "\r\nCola AWS :: " 
					+ "\r\nenv.aws.cola --> " + EnviromentUtil.getVariable(environment, "env.aws.cola") 
					+ "\r\nurl.cola --> "+ environment.getProperty("url.cola") 
					+ "\r\naccesskey.cola --> " + EnviromentUtil.getVariable(environment, "accesskey.cola") 
					+ "\r\nccb.region.cola.entradapagos --> " + environment.getProperty("region.cola") + "\r\n"
					/*+ "\r\nBASE DE DATOS ::  "
					+ "\r\nspring.datasource.url--> " + environment.getProperty("spring.datasource.url")
					+ "\r\nspring.datasource.username--> " + environment.getProperty("spring.datasource.username")
					+ "\r\nspring.jpa.properties.hibernate.dialect--> " + environment.getProperty("spring.jpa.properties.hibernate.dialect")  + "\r\n"*/
					);
		};
	}
	
	@Autowired
	private AwsConfigQuequeSub awsService;

	@PostConstruct
	public void init() {
		try {
			awsService.suscriber();
			LOGGER.info("Suscripción a la cola AWS exitosa.");
		} catch (Exception e) {
			LOGGER.error("Error al suscribirse a la cola AWS --> ", e);
			throw new TechnicalException(e.getMessage(),e);
		}
	}

}
