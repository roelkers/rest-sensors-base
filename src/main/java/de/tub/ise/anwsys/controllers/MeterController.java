package de.tub.ise.anwsys.controllers;

import java.net.URI;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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
	
	//@Autowired
	//MetricRepository metricRepository;
	
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
			//System.out.println(foundSmartMeter.toString());
			return foundSmartMeter;
		}
		else {
			throw new SmartMeterNotFoundException(meterId);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{meterId}/metrics")
	Map<String,Metric> returnAllMetrics(@PathVariable String meterId) {
		SmartMeter foundSmartMeter = meterRepository.findOne(meterId);
		if(foundSmartMeter != null){
			//System.out.println(foundSmartMeter.toString());
			return foundSmartMeter.getMetrics();
		}
		else {
			throw new SmartMeterNotFoundException(meterId);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{meterId}/metrics/{metricName}")
	MetricAverage getAverageMetric(@PathVariable String meterId, @PathVariable String metricName, @RequestParam long timeMillisMeasurement){
		//Metric metric = metricRepository.findOne(metricId);
		SmartMeter meter = meterRepository.findOne(meterId);
		if(meter == null)
			throw new SmartMeterNotFoundException(meterId);
		//System.out.println("MetricIDController: "+metricName);
		Metric metric = meter.getMetrics().get(metricName);
		//System.out.println("METRICS"+ meter.getMetrics().toString());
		if(metric == null){
			System.out.println("Metric is NULL");
			throw new MetricNotFoundException(metricName);
		}
		//System.out.println(meter.getMetrics().toString());
		//System.out.println(metric);
		
		//Determine Start Date and End DAte
		long millisIntervallLength = 15*60*1000;
		
		long totalIntervalls = timeMillisMeasurement/millisIntervallLength;
		
		long startTime = totalIntervalls*millisIntervallLength;
		long endTime = (totalIntervalls+1)*millisIntervallLength;
		
		//Get List of applicable measurements
		List<Measurement> measurements = measurementRepository.findByTimeMillisBetweenAndMetric(startTime, endTime, metric);
		
		System.out.println("MEASUREMENTS: "+measurements.toString());
		
		double sum = 0;
		int sampleSize = 0;
		for(Measurement mes : measurements){
			sum = sum + mes.getValue();
			sampleSize++;
		}
		return new MetricAverage(metricName, sum/sampleSize, sampleSize);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/{meterId}/metrics/{metricName}/measurements")
	ResponseEntity<?> postMeasurement(@PathVariable String meterId, @PathVariable String metricName, 
			@RequestParam(value="timeMillis") Long timeMillis, @RequestParam(value="value") double value){
		
		SmartMeter meter = meterRepository.findOne(meterId);
		//System.out.println(mes.toString());
		Metric metric = meter.getMetrics().get(metricName);
		
		Measurement newMes = measurementRepository.save(new Measurement(metric,timeMillis,value));
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newMes.getId()).toUri();

		return ResponseEntity.created(location).build();
	}
	
}
