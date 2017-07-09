package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Metric implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	String id;
	
	private String metricText;
	
	@ManyToOne
	@JsonBackReference
	SmartMeter smartMeter;
	
	@OneToMany(mappedBy = "metric")
	@JsonManagedReference
	private List<Measurement> measurements;

	public String getId() {
		return id;
	}
	
	public Metric(){
		
	}

	public Metric(String id, String metricText, SmartMeter smartMeter){
		this.id = id;
		this.metricText = metricText;
		this.smartMeter = smartMeter;
		this.measurements = new ArrayList<>();
	}
	
	public String getMetricText() {
		return metricText;
	}

	public void setMetricText(String metricText) {
		this.metricText = metricText;
	}

	public SmartMeter getSmartMeter() {
		return smartMeter;
	}

	public void setSmartMeter(SmartMeter smartMeter) {
		this.smartMeter = smartMeter;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}
	
}
