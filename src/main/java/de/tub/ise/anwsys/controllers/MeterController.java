package de.tub.ise.anwsys.controllers;

import java.net.URI;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ch.qos.logback.core.util.Duration;
import de.tub.ise.anwsys.models.Measurement;
import de.tub.ise.anwsys.models.Metric;
import de.tub.ise.anwsys.models.MetricAverage;
import de.tub.ise.anwsys.models.SmartMeter;
import de.tub.ise.anwsys.repos.MeasurementRepository;
import de.tub.ise.anwsys.repos.MeterRepository;
import de.tub.ise.anwsys.repos.MetricRepository;

@RestController
@RequestMapping("/meters")
public class MeterController {
	
	@Autowired
	MeterRepository meterRepository;
	
	@Autowired
	MeasurementRepository measurementRepository;
	
	@Autowired
	MetricRepository metricRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	List<SmartMeter> returnAllSmartMeters() {
		List<SmartMeter> foundSmartMeters = (List<SmartMeter>) meterRepository.findAll();
		if(!foundSmartMeters.isEmpty()){
			return foundSmartMeters;
		}
		else {
			throw new SmartMeterNotFoundException("no smartmeters are present");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{meterId}")
	SmartMeter readSmartMeter(@PathVariable String meterId) {
		SmartMeter foundSmartMeter = meterRepository.findOne(meterId);
		if(foundSmartMeter != null){
			return foundSmartMeter;
		}
		else {
			throw new SmartMeterNotFoundException(meterId);
		}
	}
	/*
	@RequestMapping(method = RequestMethod.POST, value="/{meterId}")
	void addSmartMeter(@PathVariable String meterId, @RequestBody SmartMeter input) {
		SmartMeter sm = meterRepository.save(new SmartMeter(input.getId()));
		
		meterRepository.save(sm);
		
		returnAllSmartMeters();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/{meterId}")
	void updateSmartMeter(@PathVariable String meterId, @RequestBody Measurement input) {
		Measurement mm = new Measurement(input.getId(),input.getMetric(),input.getStartDate(),input.getCurrent());
		
		measurementRepository.save(mm);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{meterId}"){
		
	}
	*/
	
	@RequestMapping(method = RequestMethod.GET, value="/{meterId}/metrics/{metricId}")
	MetricAverage getAverageMetric(@PathVariable String meterId, @PathVariable String metricId, @RequestParam long timeMillisMeasurement){
		Metric metric = metricRepository.findOne(metricId);
		/*SmartMeter meter = meterRepository.findOne(meterId);
		System.out.println(metricId);
		Metric metric = meter.getMetrics().get(metricId);
		System.out.println(meter.getMetrics().toString());
		System.out.println(metric);*/
		//Determine Start Date and End DAte
		long millisIntervallLength = 15*60*1000;
		
		long totalIntervalls = timeMillisMeasurement/millisIntervallLength;
		
		long startTime = totalIntervalls*millisIntervallLength;
		long endTime = (totalIntervalls+1)*millisIntervallLength;
		
		//Get List of applicable measurements
		List<Measurement> measurements = measurementRepository.findByTimeMillisBetweenAndMetric(startTime, endTime, metric);
		
		System.out.println(measurements);
		
		double sum = 0;
		int sampleSize = 0;
		for(Measurement mes : measurements){
			sum = sum + mes.getValue();
			sampleSize++;
		}
		return new MetricAverage(metricId, sum/sampleSize, sampleSize);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{meterId}/metrics/{metricId}/measurements")
	ResponseEntity<?> postMeasurement(@PathVariable String meterId, @PathVariable String metricId, @RequestBody Measurement mes){
		
		SmartMeter meter = meterRepository.findOne(meterId);
		//System.out.println(mes.toString());
		Metric metric = meter.getMetrics().get(metricId);
		
		Measurement newMes = measurementRepository.save(new Measurement(metric,mes.getTimeMillis(),mes.getValue()));
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newMes.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
	/*
	@RequestMapping(method = RequestMethod.POST, value="/{meterId}/metrics/{metricId}/measurements")
	ResponseEntity<?> updateMetric(@PathVariable String meterId, @PathVariable String metricId, @RequestParam long timeMillis, @RequestParam double value){
		
		System.out.println("time: " + timeMillis);
		System.out.println("value: " + value);
		
		Metric metric = metricRepository.findOne(metricId);
		
		Measurement newMes = measurementRepository.save(new Measurement(metric,timeMillis,value));
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newMes.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	*/
	/*
	@RequestMapping(method = RequestMethod.POST, value="/{meterId}/metrics/{metricId}")
	void addMetric(@PathVariable String meterId, @PathVariable String metricId){
		
		metricRepository.save(new Metric(metricId));
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/{meterId}/metrics/{metricId}")
	void deleteMetric(@PathVariable String meterId, @PathVariable String metricId){
		
		Metric metric = metricRepository.findOne(metricId);
		
		metricRepository.delete(metric);
	}
	*/
}
