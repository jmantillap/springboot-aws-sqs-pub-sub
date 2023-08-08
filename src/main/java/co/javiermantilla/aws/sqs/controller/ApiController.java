package co.javiermantilla.aws.sqs.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.javiermantilla.aws.sqs.dto.ErrorResponseDTO;
import co.javiermantilla.aws.sqs.dto.InputMessageRequestDTO;
import co.javiermantilla.aws.sqs.dto.ResponseDTO;
import co.javiermantilla.aws.sqs.service.EnviarMensajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("v1")
public class ApiController {

	private static final Logger LOGGER = LogManager.getLogger(ApiController.class);
	
	@Autowired
	private EnviarMensajeService enviarMensajeService;

	/**
	 * Metodo enviar el mensaje
	 * 
	 * @param data mensaje
	 * @return json ResponseDTO
	 * @author javier.mantilla
	 * @since Jul /2023.
	 */
	@Operation(summary = "Enviar mensaje a la cola", description = "Servicio enviar el mensaje a una cola sqs")
	@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseDTO.class)))
	@ApiResponse(responseCode = "500", description = "ERROR INTERNO", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	@PostMapping(value = "send-message", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> realizarLiquidacion(@RequestBody InputMessageRequestDTO data) {
		try {
			LOGGER.info("Se Recibe mensaje api controller: {}", data);
			return new ResponseEntity<>(this.enviarMensajeService.enviarMensaje(data), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
