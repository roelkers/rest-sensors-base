package de.tub.ise.anwsys.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Metric implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	String id;
	
	private String metricName;
	
	private String metricText;
	
	@ManyToOne
	@JsonManagedReference
	SmartMeter smartmeter;
	
	@OneToMany(mappedBy = "metric", fetch = FetchType.EAGER)
	@JsonBackReference
	private List<Measurement> measurements;

	public String getId() {
		return id;
	}
	
	public Metric(){
		
	}

	public Metric(String metricName, String metricText, SmartMeter smartMeter){
		this.metricName = metricName;
		this.metricText = metricText;
		this.smartmeter = smartMeter;
		this.measurements = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		return "Metric [id=" + id + ", metricName=" + metricName + ", metricText=" + metricText + ", measurements=" + measurements + "]";
	}

	public String getMetricName() {
		return metricName;
	}

	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}

	public String getMetricText() {
		return metricText;
	}

	public void setMetricText(String metricText) {
		this.metricText = metricText;
	}

	public SmartMeter getSmartMeter() {
		return smartmeter;
	}

	public void setSmartMeter(SmartMeter smartMeter) {
		this.smartmeter = smartMeter;
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
