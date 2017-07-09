package de.tub.ise.anwsys.controllers;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SmartMeterNotFoundException  extends RuntimeException  {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SmartMeterNotFoundException(String meterId) {
		super("could not find user '" + meterId + "'.");
	}
}

