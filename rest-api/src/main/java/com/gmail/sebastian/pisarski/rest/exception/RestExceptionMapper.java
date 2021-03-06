package com.gmail.sebastian.pisarski.rest.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.gmail.sebastian.pisarski.exception.ValidationException;

@Component
@Provider
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionMapper.class);

	@Override
	public Response toResponse(Exception exception) {

		if (exception instanceof Failure) {
			return ((Failure) exception).getResponse();
		} else if (exception instanceof WebApplicationException) {
			return ((WebApplicationException) exception).getResponse();
		} else if (exception instanceof AccessDeniedException) {
			return Response.status(Status.FORBIDDEN).build();
		} else if (exception instanceof ValidationException) {
			ValidationException validationException = (ValidationException) exception;
			return Response.status(Status.BAD_REQUEST).header("X-Status-Reason", "Validation failed")
					.type(MediaType.APPLICATION_JSON).entity(validationExceptionToResponseMap(validationException))
					.build();
		}

		UUID uuid = UUID.randomUUID();
		LOGGER.error(uuid.toString(), exception);

		return Response.serverError().entity("Error occured: " + uuid).build();
	}

	private Map<String, Object> validationExceptionToResponseMap(ValidationException exception) {
		Map<String, Object> result = new HashMap<>();
		result.put("Message", "Validation failed");
		result.put("ValidationErrors", exception.getCustomErrors());
		return result;
	}

}
