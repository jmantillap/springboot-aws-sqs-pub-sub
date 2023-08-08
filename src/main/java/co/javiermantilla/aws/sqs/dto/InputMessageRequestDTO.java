package co.javiermantilla.aws.sqs.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@Schema(description = "Mensaje")
public class InputMessageRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6375192685704685220L;
	
	@JsonProperty("mensaje")
	private String mensaje;
}
