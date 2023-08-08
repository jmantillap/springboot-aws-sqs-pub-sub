package co.javiermantilla.aws.sqs.util;

import org.springframework.core.env.Environment;

public class EnviromentUtil {

	private EnviromentUtil() {		
	}
	public static String getVariable(Environment environment, String llave) {
		try {
			return environment.getProperty(llave);
		} catch (Exception e) {
			return null;
		}
	}

}
