package com.yapily.characters.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Error handling controller layer
 * @author Sandesh
 *
 */
@Slf4j
@ControllerAdvice
public class ResponseEntityExceptionAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { HttpClientErrorException.class, RuntimeException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String bodyOfResponse;
		if (ex instanceof HttpClientErrorException) {
			bodyOfResponse = ((HttpClientErrorException) ex).getResponseBodyAsString();
		} else {
			bodyOfResponse = "Internal Service Error, please contact administrator";
		}
		log.error("Error response: {}", bodyOfResponse);
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
}
