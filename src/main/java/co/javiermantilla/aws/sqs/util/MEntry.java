package co.javiermantilla.aws.sqs.util;

import lombok.Data;

@Data
public class MEntry {

	private String accessKey;

	private String secretKey;

	private String url;

	private String region;

	/**
	 * Ambiente ejecutandose la lambda: [0=local; 1 = AWS]
	 */
	private String enviromentAWS;
}
