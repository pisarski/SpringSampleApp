package com.lumesse.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseController.class);

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView commonExceptionHandler(Throwable exception) {
		UUID uuid = UUID.randomUUID();
		logger.error("Exception occured: " + uuid, exception);
		ModelAndView model = new ModelAndView("exception");
		model.addObject("uuid", uuid.toString());
		return model;
	}

}
