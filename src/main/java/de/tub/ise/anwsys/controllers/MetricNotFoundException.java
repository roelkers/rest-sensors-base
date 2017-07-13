package de.tub.ise.anwsys.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MetricNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetricNotFoundException(String metricId) {
		super("could not find metric '" + metricId + "'.");
	}
}
