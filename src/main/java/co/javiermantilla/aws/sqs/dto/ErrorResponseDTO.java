package co.javiermantilla.aws.sqs.dto;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper=false)
public class ErrorResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8724862665022392659L;

	private static final Logger LOGGER = LogManager.getLogger(ErrorResponseDTO.class);

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date timestamp;
	private int code;

	private String status;

	private String message;

	private String stackTrace;

	private Object data;	

	public ErrorResponseDTO() {
		timestamp = new Date();
	}

	public ErrorResponseDTO(HttpStatus httpStatus, String message) {
		this();

		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
	}

	public ErrorResponseDTO(HttpStatus httpStatus, String message, String stackTrace) {
		this(httpStatus, message);
		this.stackTrace = stackTrace;
	}

	public ErrorResponseDTO(HttpStatus httpStatus, String message, String stackTrace, Object data) {
		this(httpStatus, message, stackTrace);
		this.data = data;
	}
	public ErrorResponseDTO(HttpStatus httpStatus, String message, Throwable error) {
		this();

		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = message;
		
		StringWriter stringWriter = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(stringWriter);
	    error.printStackTrace(printWriter);
	    this.stackTrace= stringWriter.toString();		
		
	}
	
	public ErrorResponseDTO(HttpStatus httpStatus,Throwable error) {
		this();
		this.code = httpStatus.value();
		this.status = httpStatus.name();
		this.message = error.getMessage();
		
		StringWriter stringWriter = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(stringWriter);
	    error.printStackTrace(printWriter);
	    this.stackTrace= stringWriter.toString();	    
	    LOGGER.error(this.stackTrace);
		
	}

}
